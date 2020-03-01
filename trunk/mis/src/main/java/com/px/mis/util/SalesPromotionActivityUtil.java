package com.px.mis.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.px.mis.ec.dao.GoodsActivityMiddleDao;
import com.px.mis.ec.dao.PresentModeDao;
import com.px.mis.ec.dao.PresentRangeListDao;
import com.px.mis.ec.dao.ProActivityDao;
import com.px.mis.ec.dao.ProActivitysDao;
import com.px.mis.ec.dao.ProPlanDao;
import com.px.mis.ec.dao.ProPlansDao;
import com.px.mis.ec.entity.GoodsActivityMiddle;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.PresentMode;
import com.px.mis.ec.entity.PresentRangeList;
import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.entity.ProPlan;
import com.px.mis.ec.entity.ProPlans;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;

@Component
public class SalesPromotionActivityUtil {
//	private static final Logger logger = LoggerFactory.getLogger(GetOrderNo.class);
	private static final Logger logger = LoggerFactory.getLogger(SalesPromotionActivityUtil.class);

	@Autowired
	private GoodsActivityMiddleDao goodsActivityMiddleDao;// ��Ʒ��м��
	@Autowired
	private ProActivitysDao proActivitysDao;// ������ӱ�
	@Autowired
	private ProActivityDao proActivityDao;// ���������
	@Autowired
	private ProPlansDao proPlansDao;// ���������ӱ�
	@Autowired
	private ProPlanDao proPlanDao;// ���������
	@Autowired
	private PresentModeDao presentModeDao;// ��Ʒ��ʽ��
	@Autowired
	private PresentRangeListDao presentRangeListDao;// ��Ʒ��Χ��
	@Autowired
	private InvtyDocDao invtyDocDao;// �������

	// ƥ������
	public List<SellSnglSub> matchSalesPromotion(PlatOrder platOrder, List<PlatOrders> list, SellSngl sellSngl,
			List<SellSnglSub> sellSnglSubs) {
//		���ȸ��ݶ�����Ų�ѯ��ض�����Ϣ��Ȼ��ȥƥ���������򣬷�Ϊ�������:�ö���û�вμӻ����ֱ��
//		�������۵�������вμӻ�Ͱ�����ػ����ȥƥ���������۵���
//		ƥ�������̣�����ƥ��ƽ̨���̣����Ƿ��л�μӣ�û�вμӻ��ֱ���������۵��ˣ�
//		������̲μӻ�ˣ��̶�ƥ�䶩������Ʒ��������Ʒ�Ƿ�μӻ�����û�вμӻ��ֱ���������۵��ˣ�
//		����μӻ�ˣ���һ����Ʒ�μ��˼����������Ƕ���Ҫѡ�����ȼ��ߵĻȡƥ��
//		ƥ����֮���ٿ��Ƿ���������--�Ƿ���Ʒ����--��������--��װ��Ʒ����ӵ����۵��ӱ���
		List<SellSnglSub> sellSnglSubsOne = new ArrayList<SellSnglSub>();
		String storeId = platOrder.getStoreId();// ���̱��
		String payTime = platOrder.getPayTime();// ����ʱ��
		List<GoodsActivityMiddle> goodsActivityMiddles = goodsActivityMiddleDao
				.selectGoodsActivityMiddleByStoreId(payTime, storeId);
		if (goodsActivityMiddles != null && goodsActivityMiddles.size() > 0) {// a �ж���Ʒ��м�����ǲ���������������ڲμӻ
			for (SellSnglSub sellSnglSub : sellSnglSubs) {
				// Ȼ���ٿ���������̹������Ʒ��μ�����Щ���
				BigDecimal prcTaxSum = sellSnglSub.getPrcTaxSum();// ��˰�ϼ� ʵ�����
				BigDecimal qty = sellSnglSub.getQty();// ��Ʒ������
				String invtyEncd = sellSnglSub.getInvtyEncd();// �������
				List<GoodsActivityMiddle> goodsActivityMiddleOnes = goodsActivityMiddleDao
						.selectGoodsActivityMiddleByInvtyEncd(payTime, storeId, invtyEncd);
				GoodsActivityMiddle goodsActivityMiddle = goodsActivityMiddleOnes.get(0);
				if (goodsActivityMiddleOnes != null && goodsActivityMiddleOnes.size() == 1) {// b �ö����е������Ʒ��ֻ�μ���һ���
					sellSnglSubsOne = matchManyActivity(sellSngl, sellSnglSubs, goodsActivityMiddle, prcTaxSum, qty);
				} else if (goodsActivityMiddleOnes != null && goodsActivityMiddleOnes.size() > 1) {// b
																									// �ö����е������Ʒ�ֻ�μ���һ���
					// ��ʱ���Ҫ�������ȼ����жϣ���ʱ����������м���ϣ�Ȼ��ȡ���ȼ���С(���ȼ����)�Ļȡƥ�䣻
					Integer priority = goodsActivityMiddle.getPriority();
					GoodsActivityMiddle goodsActivityMiddleMain = goodsActivityMiddle;
					for (GoodsActivityMiddle goodsActivityMiddleOne : goodsActivityMiddleOnes) {// �Ƚ����ȼ�������ȷ�����ȼ���ߵ��Ǹ���м��
						Integer priorityOne = goodsActivityMiddleOne.getPriority();
						if (priorityOne < priority) {
							priority = priorityOne;
							goodsActivityMiddleMain = goodsActivityMiddleOne;
						}
					}
					sellSnglSubsOne = matchManyActivity(sellSngl, sellSnglSubs, goodsActivityMiddleMain, prcTaxSum,
							qty);
				} else {
					System.out.println("����Ʒû�вμӴ����������");
				}
			}
		} else {// a ����û�вμӴ����
			System.out.println("�õ���û�вμӴ������");
		}
		return sellSnglSubsOne;
	}

	// ƥ�����ȼ���ѡ���ȼ���ߵĻȥƥ����
	private List<SellSnglSub> matchManyActivity(SellSngl sellSngl, List<SellSnglSub> sellSnglSubs,
			GoodsActivityMiddle goodsActivityMiddle, BigDecimal prcTaxSum, BigDecimal qty) {
		List<SellSnglSub> SellSnglSubsOne = new ArrayList<SellSnglSub>();
		Integer no = goodsActivityMiddle.getSublistNo();// ������ӱ����
		ProActivitys proActivitys = proActivitysDao.selectById(no);// ������ӱ�����
		String proPlanId = proActivitys.getProPlanId();// ��������id
		String proActId = proActivitys.getProActId();// ���������id
		ProActivity proActivity = proActivityDao.select(proActId);// �������������
		ProPlan proPlan = proPlanDao.select(proPlanId);// ������������
		List<ProPlans> proPlansList = proPlansDao.select(proPlanId);// ���������ӱ�
		Integer giftMul = proPlan.getGiftMul();// �Ƿ���Ʒ������1����Ʒ�ۼӣ�0����Ʒ���ۼӣ�
		int limitPro = proActivity.getLimitPro();// �Ƿ���������
		if (limitPro == 0) {// c ������
			Long proCriteria = proPlan.getProCriteria();// ������������
			if (giftMul == 0) {// d ���ۼ�
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 0);
			} else if (giftMul == 1) {// d �ۼ�
				// �ۼ�ǰ����߼������ۼ��ǲ��ģ�Ȼ���������ƥ�伯�д�������ʱ���������������ͽ��ʱ�ɱ����ӵ�ʱ����ƷҲ��Ӧ�ɱ�����
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 1);
			}
		} else if (limitPro == 1) {// c ���� Ҫ�鿴��ӱ��е���Ʒ�����Ƿ������������������:�μӻ������������
			Long proCriteria = proPlan.getProCriteria();// ������������
			if (giftMul == 0) {// d ���ۼ�
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 2);
			} else if (giftMul == 1) {// d �ۼ�
				// �ۼ�ǰ����߼������ۼ��ǲ��ģ�Ȼ���������ƥ�伯�д�������ʱ���������������ͽ��ʱ�ɱ����ӵ�ʱ����ƷҲ��Ӧ�ɱ�����
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 3);
			}
		}
		return SellSnglSubsOne;
	}

	// ƥ������ ����1�����۵�����2�����۵��ӱ�3��ʵ����4����Ʒ������5�������������룬6:���������ӱ�7��������ӱ�
	// 8��֧����Ʒ������ǣ�0�����������ۼ�;1���������ۼ�;2:�������ۼ�;3:�����ۼ�;
	private List<SellSnglSub> matchActivityCondition(SellSngl sellSngl, List<SellSnglSub> sellSnglSubs,
			BigDecimal prcTaxSum, BigDecimal qty, Long proCriteria, List<ProPlans> proPlansList,
			ProActivitys proActivitys, Integer flag) {
		List<SellSnglSub> sellSnglSubsOne = new ArrayList<SellSnglSub>();
		Long proPlansNo = proPlansList.get(0).getNo();// ���������ӱ�id
		Integer giftWay = proPlansList.get(0).getGiftWay();
		Integer giftNum = proPlansList.get(0).getGiftNum();// ��Ʒ���� �ͼ�����Ʒ
		Integer allGiftNum = proActivitys.getGiftNum();// ������ӱ�����������ʱ�ܵ���Ʒ������
		Integer hasGiftNum = proActivitys.getHasGiftNum();// ������ӱ�����������ʱ�ܵ�����������
		// ƥ���������
		if (proCriteria == 1) {// ��һ��һ ��һ��һ��ʱ�򣬴��������ӱ�ֻ�����һ�����ݣ���Ϊһ�������Ѿ��ܹ���������
			int intValue = qty.intValue();
			if (intValue >= 1) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ����
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0�����������ۼ�;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(1), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(intValue), sellSnglSubs,
							proActivitys);// �ۼ�֮��ͱ����n��n��
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum > hasGiftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(1), sellSnglSubs,
								proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					if (allGiftNum >= hasGiftNum + intValue) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(intValue), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + intValue) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		} else if (proCriteria == 2) {// ��m��n �о���������û�б�Ҫ����һ��һ���ۼӾ�����n��n�ˡ�
			int intValue = qty.intValue();
			Integer number = proPlansList.get(0).getNumber();// ������
			if (intValue >= number) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ����
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0�����������ۼ�;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					giftNum = intValue / number * giftNum;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					giftNum = intValue / number * giftNum;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		} else if (proCriteria == 3) {// ������ ������Ҳ��ֻ����һ�����������ӱ����ݣ�
			BigDecimal money = proPlansList.get(0).getMoney();// ������Ǯ
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ���룬�������
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0�����������ۼ�;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		} else if (proCriteria == 4) {// ǰxx����xx
			// ǰ����������Ʒ��������������������ֻҪ�µ�����
			PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
			String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
			String presentEncd = "";// ��Ʒ����
			presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
			if (flag == 2 || flag == 3) {// ���� �˴������������£���������������
				if (allGiftNum >= hasGiftNum + giftNum) {
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
							sellSnglSubs, proActivitys);
				}
			}
		} else if (proCriteria == 5) {// ǰxxСʱ��xx
			// ���Դ���ʼʱ��ͽ���ʱ��������ǰxxСʱ��ֻҪ�����
			int intValue = qty.intValue();
			if (intValue >= 1) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ����
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 2 || flag == 3) {// ����
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 0 || flag == 1) {// ������
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				}
			}
		} else if (proCriteria == 6) {// ǰxx����xxx��xxx ���ִ�������������proCriteria���ڡ�4��ʱ��������ƣ���������������
			// Ҳ�ǿ������ó�����������ֻҪ����xxx��������Ʒ����������֮�󣬻������
			BigDecimal money = proPlansList.get(0).getMoney();// ������Ǯ
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ����
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 2) {// 2���������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3�������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		} else if (proCriteria == 7) {// ǰxxСʱ��xx��xx
			// ���Դ���ʼʱ��ͽ���ʱ��������ǰxxСʱ��ֻҪ���������������ǲ���������
			BigDecimal money = proPlansList.get(0).getMoney();// ������Ǯ
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// ��Ʒ��ʽ����
				String presentEncd = "";// ��Ʒ���룬�������
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0�����������ۼ�;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		}
		return sellSnglSubsOne;
	}

	// ��ȡ��Ʒ���룻
	private String getPresentEncd(Long proPlansNo, String presentModeCode, String presentEncd) {
//        if ("0001".equals(presentModeCode)) {// �͵��������͵�����ʱ����Ʒ��Χ��ֻ��һ����Ʒ���룩
//            List<PresentRangeList> presentRangeLists = presentRangeListDao.selectPresentRangeListByProPlansNo(proPlansNo);
//            System.out.println(presentRangeLists.size() + "========================");
//            presentEncd = presentRangeLists.get(0).getPresentEncd();// ��Ʒ����
//        } else if ("0002".equals(presentModeCode)) {// ��ѡһ
//            List<PresentRangeList> presentRangeLists = presentRangeListDao.selectPresentRangeListByProPlansNo(proPlansNo);
//            int nextInt = new Random().nextInt(presentRangeLists.size());// ����һ�������intֵ����ֵ����[0,presentRangeLists.size())������
//            presentEncd = presentRangeLists.get(nextInt).getPresentEncd();// ��Ʒ����
//        }
		return presentEncd;
	}

	// ��װ��Ʒ���󣬲���ӵ����۵��ӱ���
	private List<SellSnglSub> assembleGift(SellSngl sellSngl, String presentEncd, BigDecimal giftNum,
			List<SellSnglSub> sellSnglSubs, ProActivitys proActivitys) {
		SellSnglSub sellSnglSubPresent = new SellSnglSub();// �͵���Ʒ
		sellSnglSubPresent.setIsComplimentary(1);// �Ƿ�����Ʒ��1����Ʒ��0������Ʒ
		sellSnglSubPresent.setInvtyEncd(presentEncd);// ��Ʒ���룬�������
		// �ֿ��ţ���Ʒ�Ĳֿ�����ǹ̶�����
		// ���۵����
		sellSnglSubPresent.setSellSnglId(sellSngl.getSellSnglId());
		// ����
		sellSnglSubPresent.setQty(giftNum);
		// ����
		// ���������Ʒ�к�˰���ۣ���˰�ϼƣ���˰����˰���ۣ�˰�˰�ʵ�������ص�����ͳͳ����Ϊ�㣻
		sellSnglSubPresent.setCntnTaxUprc(new BigDecimal(0));// ��˰���ۣ�
		sellSnglSubPresent.setPrcTaxSum(new BigDecimal(0));// ��˰�ϼƣ�
		sellSnglSubPresent.setNoTaxAmt(new BigDecimal(0));// ��˰��
		sellSnglSubPresent.setNoTaxUprc(new BigDecimal(0));// ��˰����
		sellSnglSubPresent.setTaxAmt(new BigDecimal(0));// ˰��
		InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(presentEncd);
		sellSnglSubPresent.setTaxRate(invtyDoc.getOptaxRate());// ˰��
		// ʧЧ���ڣ��������ڣ�������
		// ���Ҫ����ƷҲ���õ����۵��ӱ���
		// ��������������
		Integer giftNum2 = proActivitys.getHasGiftNum();
		giftNum2 += giftNum.intValue();
		proActivitysDao.update(proActivitys);
//		sellSnglSubs.add(sellSnglSubPresent);

		List<SellSnglSub> newList = new ArrayList<SellSnglSub>();
		newList.addAll(sellSnglSubs);
		newList.add(sellSnglSubPresent);
		sellSnglSubs = newList;
		return sellSnglSubs;
	}

	// ƥ������
	public PlatOrder matchSalesPromotions(PlatOrder platOrder) {
//		���ȸ��ݶ�����Ų�ѯ��ض�����Ϣ��Ȼ��ȥƥ���������򣬷�Ϊ�������:�ö���û�вμӻ����ֱ��
//		�������۵�������вμӻ�Ͱ�����ػ����ȥƥ���������۵���
//		ƥ�������̣�����ƥ��ƽ̨���̣����Ƿ��л�μӣ�û�вμӻ��ֱ���������۵��ˣ�
//		������̲μӻ�ˣ��̶�ƥ�䶩������Ʒ��������Ʒ�Ƿ�μӻ�����û�вμӻ��ֱ���������۵��ˣ�
//		����μӻ�ˣ���һ����Ʒ�μ��˼����������Ƕ���Ҫѡ�����ȼ��ߵĻȡƥ��
//		ƥ����֮���ٿ��Ƿ���������--�Ƿ���Ʒ����--��������--��װ��Ʒ����ӵ����۵��ӱ���
		logger.info("�ж��Ƿ�ƥ����");

		if (platOrder.getCanMatchActive() != null && platOrder.getCanMatchActive() == 1) {
			// �ж��Ƿ�ƥ����
			logger.info("ƥ����");

			return platOrder;
		}
		logger.info("û��ƥ����");
		platOrder.setCanMatchActive(1);
		String storeId = platOrder.getStoreId();// ���̱��
		String payTime = platOrder.getPayTime();// ����ʱ��

		List<ProActivity> proActivities = proActivityDao.selectStorePayTime(payTime, storeId);
		if (proActivities != null && proActivities.size() > 0) {// a �ж���Ʒ��м�����ǲ���������������ڲμӻ
			List<PlatOrders> list = platOrder.getPlatOrdersList();
			for (PlatOrders platOrders : list) {
				if (platOrders.getIsGift() == 1) {
//					��������Ʒ����
					continue;
				}
				// Ȼ���ٿ���������̹������Ʒ��μ�����Щ���
				BigDecimal prcTaxSum = platOrders.getPayMoney();// ��˰�ϼ� ʵ�����
				BigDecimal qty = BigDecimal.valueOf(platOrders.getInvNum());// ��Ʒ������
				String invtyEncd = platOrders.getInvId();// �������
				ProActivity proActivity = proActivityDao.selectStorePayTimeORDERLIMIT(payTime, storeId, invtyEncd);
				if (proActivity != null) {// b ���ȼ���ߵ�
					platOrder = matchManyActivitys(platOrder,  proActivity, prcTaxSum, qty, invtyEncd);
				} else {
					logger.info(invtyEncd + "����Ʒû�вμӴ����������");

				}

			}
		} else {// a ����û�вμӴ����
			logger.info("�õ���û�вμӴ������");
		}
		return platOrder;
	}

	// ƥ�����ȼ���ѡ���ȼ���ߵĻȥƥ����
	private PlatOrder matchManyActivitys(PlatOrder platOrder,  ProActivity proActivity,
			BigDecimal prcTaxSum, BigDecimal qty, String invtyEncd) {
//����ƥ����ѯ���л�ӱ�
		String proActId = proActivity.getProActId();// ���������id
		List<ProActivitys> proActivitysList = proActivitysDao.selectProActIdAllGoods(proActId, invtyEncd);// ������ӱ�����
//        ѭ�����������ӱ�
		int limitPro = proActivity.getLimitPro();// �Ƿ���������

		for (ProActivitys proActivitys : proActivitysList) {
			List<PlatOrders> list =platOrder.getPlatOrdersList();
			ProPlan proPlan = proPlanDao.select(proActivitys.getProPlanId());// ������������

			Integer giftMul = proPlan.getGiftMul();// �Ƿ���Ʒ������1����Ʒ�ۼӣ�0����Ʒ���ۼӣ�
			Long proCriteria = proPlan.getProCriteria();// ������������
			if (limitPro == 0 && giftMul == 0) {// ������ ���ۼ�
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 0);

			} else if (limitPro == 0 && giftMul == 1) {// ������ �ۼ�
				// �ۼ�ǰ����߼������ۼ��ǲ��ģ�Ȼ���������ƥ�伯�д�������ʱ���������������ͽ��ʱ�ɱ����ӵ�ʱ����ƷҲ��Ӧ�ɱ�����
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 1);

			} else if (limitPro == 1 && giftMul == 0) {// ���ۼ� ���� Ҫ�鿴��ӱ��е���Ʒ�����Ƿ������������������:�μӻ������������
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 2);

			} else if (limitPro == 1 && giftMul == 1) {// ���ۼ� ���� Ҫ�鿴��ӱ��е���Ʒ�����Ƿ������������������:�μӻ������������
				// �ۼ�ǰ����߼������ۼ��ǲ��ģ�Ȼ���������ƥ�伯�д�������ʱ���������������ͽ��ʱ�ɱ����ӵ�ʱ����ƷҲ��Ӧ�ɱ�����
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 3);

			} else {
				logger.info("�ۼ���������");
			}

		}

		return platOrder;
	}

	// ƥ������ ����1����������2�������ӱ�3��ʵ����4����Ʒ������5�������������룬6:���������ӱ�7��������ӱ�
	// 8��֧����Ʒ������ǣ�0�����������ۼ�;1���������ۼ�;2:�������ۼ�;3:�����ۼ�;
	/**
	 * @param platOrder    ��������
	 * @param list         �����ӱ�
	 * @param prcTaxSum    ʵ�����
	 * @param qty          ��Ʒ����
	 * @param proCriteria  ������������
	 * @param proActivitys ������ӱ�
	 * @param flag         ֧����Ʒ������ǣ�0�����������ۼ�;1���������ۼ�;2:�������ۼ�;3:�����ۼ�;
	 */
	private PlatOrder matchActivityConditions(PlatOrder platOrder, List<PlatOrders> list, BigDecimal prcTaxSum,
			BigDecimal qty, Long proCriteria, ProActivitys proActivitys, Integer flag) {

//        Long proPlansNo;// ���������ӱ�id
//        Integer giftWay;// ��Ʒ��ʽ

		Integer allGiftNum = proActivitys.getGiftNum();// ������ӱ�����������ʱ�ܵ���Ʒ������
		Integer hasGiftNum = proActivitys.getHasGiftNum();// ������ӱ�����������ʱ�ܵ�����������
		// ƥ���������
		if (proCriteria == 1) {// ��һ��һ ��һ��һ��ʱ�򣬴��������ӱ�ֻ�����һ�����ݣ���Ϊһ�������Ѿ��ܹ���������
			int intValue = qty.intValue();
			if (intValue >= 1) {

				ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());// ���������ӱ�
				if (proPlans == null) {
					logger.info("��һ��һ�޴��������ӱ�");
					return platOrder;
				}
				proPlans.getGiftNum();// ��Ʒ����
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����
				if (flag == 0) {// 0�����������ۼ�;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(1), proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(intValue), proActivitys);// �ۼ�֮��ͱ����n��n��
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum > hasGiftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(1), proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
//					��������ʱ�ܵ���Ʒ����-��������+��һ��һ 
					logger.info("allGiftNum >= hasGiftNum + intValue"  +"\t"+allGiftNum  +"\t"+ hasGiftNum  +"\t"+ intValue);
					if (allGiftNum >= hasGiftNum + intValue) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(intValue),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + intValue) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				}
			}
		} else if (proCriteria == 2) {// ��m��n �о���������û�б�Ҫ����һ��һ���ۼӾ�����n��n�ˡ�
			int intValue = qty.intValue();
			ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());// ���������ӱ�
			if (proPlans == null) {
				logger.info("��m��n�޷��ϴ��������ӱ�");
				return platOrder;
			}
			int giftNum = proPlans.getGiftNum();
			int number = proPlans.getNumber();
			List<PresentRangeList> presentEncdList = presentRangeListDao
					.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����
			if (intValue >= number) {

				if (flag == 0) {// 0�����������ۼ�;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					giftNum = intValue / number * giftNum;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					giftNum = intValue / number * giftNum;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				}
			}
		} else if (proCriteria == 3) {// ������ ������Ҳ��ֻ����һ�����������ӱ����ݣ�
//            BigDecimal money = proPlansList.get(0).getMoney();// ������Ǯ
			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");// ���������ӱ�
			if (proPlans == null) {
				logger.info("�������޷��ϴ��������ӱ�");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();
			int giftNum = proPlans.getGiftNum();
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����

				if (flag == 0) {// 0�����������ۼ�;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				}
			}
		} else if (proCriteria == 4) {// ǰxx����xx
			// ǰ����������Ʒ��������������������ֻҪ�µ�����
			ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());
			if (proPlans == null) {
				logger.info("ǰxx����xx�޷��ϴ��������ӱ�");
				return platOrder;
			}
			int giftNum = proPlans.getGiftNum();
			List<PresentRangeList> presentEncdList = presentRangeListDao
					.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����

			if (flag == 2 || flag == 3) {// ���� �˴������������£���������������
				if (allGiftNum >= hasGiftNum + giftNum) {
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(allGiftNum - hasGiftNum),
							proActivitys);
				}
			}
		} else if (proCriteria == 5) {// ǰxxСʱ��xx
			// ���Դ���ʼʱ��ͽ���ʱ��������ǰxxСʱ��ֻҪ�����
			int intValue = qty.intValue();
			if (intValue >= 1) {
				ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());
				if (proPlans == null) {
					logger.info("ǰxxСʱ��xx�޷��ϴ��������ӱ�");
					return platOrder;
				}
				int giftNum = proPlans.getGiftNum();
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����

				if (flag == 2 || flag == 3) {// ����
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 0 || flag == 1) {// ������
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				}
			}
		} else if (proCriteria == 6) {// ǰxx����xxx��xxx ���ִ�������������proCriteria���ڡ�4��ʱ��������ƣ���������������
			// Ҳ�ǿ������ó�����������ֻҪ����xxx��������Ʒ����������֮�󣬻������
//            BigDecimal money = proPlansList.get(0).getMoney();// ������Ǯ

			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");
			if (proPlans == null) {
				logger.info("ǰxx����xxx��xxx�޷��ϴ��������ӱ�");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();// ������Ǯ
			int giftNum = proPlans.getGiftNum();

			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����

				if (flag == 2) {// 2���������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3�������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				}
			}
		} else if (proCriteria == 7) {// ǰxxСʱ��xx��xx
			// ���Դ���ʼʱ��ͽ���ʱ��������ǰxxСʱ��ֻҪ���������������ǲ���������

			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");
			if (proPlans == null) {
				logger.info("ǰxxСʱ��xx��xx�޷��ϴ��������ӱ�");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();// ������Ǯ
			int giftNum = proPlans.getGiftNum();

			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// ��Ʒ����

				if (flag == 0) {// 0�����������ۼ�;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1���������ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
				} else if (flag == 2) {// 2:�������ۼ�;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:�����ۼ�;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					if (allGiftNum >= hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + divide.intValue()) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				}
			}
		}
		return platOrder;
	}

	// ��װ��Ʒ���󣬲���ӵ����۵��ӱ���
	private PlatOrder assembleGifts(PlatOrder platOrder, List<PlatOrders> list, List<PresentRangeList> presentEncdList,
			BigDecimal giftNum, ProActivitys proActivitys) {
		List<PlatOrders> newList = new ArrayList<PlatOrders>();
		newList.addAll(list);
		for (PresentRangeList presentEncd : presentEncdList) {
			// ���������Ƿ���Ʒ
			platOrder.setHasGift(1);
			PlatOrders platOrders = new PlatOrders();// �͵���Ʒ
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(presentEncd.getPresentEncd());// ��Ʒ����

//               String goodId;//��Ʒ���
			platOrders.setGoodNum(giftNum.intValue());// ��Ʒ���� ԭ������
			platOrders.setGoodMoney(BigDecimal.ZERO);// ��Ʒ���
			platOrders.setPayMoney(BigDecimal.ZERO);// ʵ�����
			platOrders.setGoodName(invtyDoc.getInvtyNm());// ƽ̨��Ʒ����
//               String goodSku;//��Ʒsku
			platOrders.setOrderId(platOrder.getOrderId());// �������
//               batchNo;//����
			platOrders.setExpressCom(platOrder.getExpressCode());// ��ݹ�˾
			platOrders.setProActId(proActivitys.getProActId());// ��������
			platOrders.setDiscountMoney(BigDecimal.ZERO);// ϵͳ�Żݽ��
			platOrders.setAdjustMoney(BigDecimal.ZERO);// ���ҵ������
//               String memo;//��ע
			platOrders.setGoodPrice(BigDecimal.ZERO);// ��Ʒ����
			platOrders.setPayPrice(BigDecimal.ZERO);// ʵ������
			platOrders.setDeliverWhs(platOrder.getDeliverWhs());// �����ֿ����
			platOrders.setSellerPrice(BigDecimal.ZERO);// ���㵥��
			platOrders.setEcOrderId(platOrder.getEcOrderId());// ƽ̨������
			platOrders.setInvId(presentEncd.getPresentEncd());// ��Ʒ���룬�������
			platOrders.setInvNum(giftNum.intValue());// �������
//               String ptoCode;//ĸ������
//               String ptoName;//ĸ������
			platOrders.setCanRefNum(giftNum.intValue());// ��������
			platOrders.setCanRefMoney(BigDecimal.ZERO);// ���˽��
//               Integer splitNum;//�������
			platOrders.setIsGift(1);// �Ƿ�����Ʒ��1����Ʒ��0������Ʒ
//                prdcDt;//��������
//                invldtnDt;//ʧЧ����
			proActivitys.setHasGiftNum(giftNum.intValue());
			// ���Ҫ����ƷҲ���õ������ӱ���
			// ��������������
			proActivitysDao.updateHasGiftNum(proActivitys);
			newList.add(platOrders);
		}

		platOrder.setPlatOrdersList(newList);
		return platOrder;
	}


}
