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
	private GoodsActivityMiddleDao goodsActivityMiddleDao;// 商品活动中间表
	@Autowired
	private ProActivitysDao proActivitysDao;// 促销活动子表
	@Autowired
	private ProActivityDao proActivityDao;// 促销活动主表
	@Autowired
	private ProPlansDao proPlansDao;// 促销方案子表
	@Autowired
	private ProPlanDao proPlanDao;// 促销活动主表
	@Autowired
	private PresentModeDao presentModeDao;// 赠品方式表
	@Autowired
	private PresentRangeListDao presentRangeListDao;// 赠品范围表
	@Autowired
	private InvtyDocDao invtyDocDao;// 存货档案

	// 匹配促销活动
	public List<SellSnglSub> matchSalesPromotion(PlatOrder platOrder, List<PlatOrders> list, SellSngl sellSngl,
			List<SellSnglSub> sellSnglSubs) {
//		首先根据订单编号查询相关订单信息，然后去匹配促销活动规则，分为几种情况:该订单没有参加活动，就直接
//		生成销售单，如果有参加活动就按照相关活动规则去匹配生成销售单。
//		匹配规则过程：首先匹配平台店铺，看是否有活动参加，没有参加活动就直接生成销售单了；
//		如果店铺参加活动了，继而匹配订单中商品，看该商品是否参加活动，如果没有参加活动就直接生成销售单了；
//		如果参加活动了，看一个商品参加了几个活动，如果是多个活动要选择优先级高的活动取匹配
//		匹配完活动之后再看是否限量促销--是否赠品倍增--促销条件--组装赠品并添加到销售单子表中
		List<SellSnglSub> sellSnglSubsOne = new ArrayList<SellSnglSub>();
		String storeId = platOrder.getStoreId();// 店铺编号
		String payTime = platOrder.getPayTime();// 付款时间
		List<GoodsActivityMiddle> goodsActivityMiddles = goodsActivityMiddleDao
				.selectGoodsActivityMiddleByStoreId(payTime, storeId);
		if (goodsActivityMiddles != null && goodsActivityMiddles.size() > 0) {// a 判断商品活动中间表中是不是有这个店铺正在参加活动
			for (SellSnglSub sellSnglSub : sellSnglSubs) {
				// 然后再看从这个店铺购买的商品项参加了哪些活动；
				BigDecimal prcTaxSum = sellSnglSub.getPrcTaxSum();// 价税合计 实付金额
				BigDecimal qty = sellSnglSub.getQty();// 商品项数量
				String invtyEncd = sellSnglSub.getInvtyEncd();// 存货编码
				List<GoodsActivityMiddle> goodsActivityMiddleOnes = goodsActivityMiddleDao
						.selectGoodsActivityMiddleByInvtyEncd(payTime, storeId, invtyEncd);
				GoodsActivityMiddle goodsActivityMiddle = goodsActivityMiddleOnes.get(0);
				if (goodsActivityMiddleOnes != null && goodsActivityMiddleOnes.size() == 1) {// b 该订单中的这个商品项只参加了一个活动
					sellSnglSubsOne = matchManyActivity(sellSngl, sellSnglSubs, goodsActivityMiddle, prcTaxSum, qty);
				} else if (goodsActivityMiddleOnes != null && goodsActivityMiddleOnes.size() > 1) {// b
																									// 该订单中的这个商品项不只参加了一个活动
					// 这时候就要根据优先级来判断，这时候便利整个中间表集合，然后取优先级最小(优先级最高)的活动取匹配；
					Integer priority = goodsActivityMiddle.getPriority();
					GoodsActivityMiddle goodsActivityMiddleMain = goodsActivityMiddle;
					for (GoodsActivityMiddle goodsActivityMiddleOne : goodsActivityMiddleOnes) {// 比较优先级参数，确定优先级最高的那个活动中间表
						Integer priorityOne = goodsActivityMiddleOne.getPriority();
						if (priorityOne < priority) {
							priority = priorityOne;
							goodsActivityMiddleMain = goodsActivityMiddleOne;
						}
					}
					sellSnglSubsOne = matchManyActivity(sellSngl, sellSnglSubs, goodsActivityMiddleMain, prcTaxSum,
							qty);
				} else {
					System.out.println("该商品没有参加促销活动。。。");
				}
			}
		} else {// a 店铺没有参加促销活动
			System.out.println("该店铺没有参加促销活动。");
		}
		return sellSnglSubsOne;
	}

	// 匹配优先级，选优先级最高的活动去匹配活动；
	private List<SellSnglSub> matchManyActivity(SellSngl sellSngl, List<SellSnglSub> sellSnglSubs,
			GoodsActivityMiddle goodsActivityMiddle, BigDecimal prcTaxSum, BigDecimal qty) {
		List<SellSnglSub> SellSnglSubsOne = new ArrayList<SellSnglSub>();
		Integer no = goodsActivityMiddle.getSublistNo();// 促销活动子表序号
		ProActivitys proActivitys = proActivitysDao.selectById(no);// 促销活动子表数据
		String proPlanId = proActivitys.getProPlanId();// 促销方案id
		String proActId = proActivitys.getProActId();// 促销活动主表id
		ProActivity proActivity = proActivityDao.select(proActId);// 促销活动主表数据
		ProPlan proPlan = proPlanDao.select(proPlanId);// 促销方案主表
		List<ProPlans> proPlansList = proPlansDao.select(proPlanId);// 促销方案子表
		Integer giftMul = proPlan.getGiftMul();// 是否赠品倍增；1：赠品累加；0：赠品不累加；
		int limitPro = proActivity.getLimitPro();// 是否限量促销
		if (limitPro == 0) {// c 不限量
			Long proCriteria = proPlan.getProCriteria();// 促销条件编码
			if (giftMul == 0) {// d 不累加
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 0);
			} else if (giftMul == 1) {// d 累加
				// 累加前面的逻辑跟不累加是差不多的，然后区别就是匹配集中促销条件时，如果购买的数量和金额时成倍增加的时候，赠品也对应成倍增加
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 1);
			}
		} else if (limitPro == 1) {// c 限量 要查看活动子表中的赠品数量是否大于已赠数量（大于:参加活动，否则活动结束）
			Long proCriteria = proPlan.getProCriteria();// 促销条件编码
			if (giftMul == 0) {// d 不累加
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 2);
			} else if (giftMul == 1) {// d 累加
				// 累加前面的逻辑跟不累加是差不多的，然后区别就是匹配集中促销条件时，如果购买的数量和金额时成倍增加的时候，赠品也对应成倍增加
				SellSnglSubsOne = matchActivityCondition(sellSngl, sellSnglSubs, prcTaxSum, qty, proCriteria,
						proPlansList, proActivitys, 3);
			}
		}
		return SellSnglSubsOne;
	}

	// 匹配活动条件 参数1：销售单主表，2：销售单子表，3：实付金额，4：商品数量，5：促销条件编码，6:促销方案子表，7：促销活动子表，
	// 8：支持商品倍增标记：0：不限量不累加;1：不限量累加;2:限量不累加;3:限量累加;
	private List<SellSnglSub> matchActivityCondition(SellSngl sellSngl, List<SellSnglSub> sellSnglSubs,
			BigDecimal prcTaxSum, BigDecimal qty, Long proCriteria, List<ProPlans> proPlansList,
			ProActivitys proActivitys, Integer flag) {
		List<SellSnglSub> sellSnglSubsOne = new ArrayList<SellSnglSub>();
		Long proPlansNo = proPlansList.get(0).getNo();// 促销方案子表id
		Integer giftWay = proPlansList.get(0).getGiftWay();
		Integer giftNum = proPlansList.get(0).getGiftNum();// 赠品数量 送几个商品
		Integer allGiftNum = proActivitys.getGiftNum();// 促销活动子表中限量促销时总的赠品数量；
		Integer hasGiftNum = proActivitys.getHasGiftNum();// 促销活动子表中限量促销时总的已赠数量；
		// 匹配促销条件
		if (proCriteria == 1) {// 买一送一 买一送一的时候，促销方案子表只能添加一条数据，因为一条数据已经能够满足需求；
			int intValue = qty.intValue();
			if (intValue >= 1) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0：不限量不累加;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(1), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(intValue), sellSnglSubs,
							proActivitys);// 累加之后就变成买n赠n啦
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum > hasGiftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(1), sellSnglSubs,
								proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
					if (allGiftNum >= hasGiftNum + intValue) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(intValue), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + intValue) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				}
			}
		} else if (proCriteria == 2) {// 买m送n 感觉这个活动条件没有必要，买一送一可累加就是买n送n了。
			int intValue = qty.intValue();
			Integer number = proPlansList.get(0).getNumber();// 数量；
			if (intValue >= number) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0：不限量不累加;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					giftNum = intValue / number * giftNum;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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
		} else if (proCriteria == 3) {// 满额送 满额送也是只能有一条促销方案子表数据；
			BigDecimal money = proPlansList.get(0).getMoney();// 满多少钱
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码，存货编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0：不限量不累加;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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
		} else if (proCriteria == 4) {// 前xx名赠xx
			// 前多少名赠赠品，可以理解成限量促销；只要下单就送
			PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
			String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
			String presentEncd = "";// 赠品编码
			presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
			if (flag == 2 || flag == 3) {// 限量 此促销条件类型下，必须是限量促销
				if (allGiftNum >= hasGiftNum + giftNum) {
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
							sellSnglSubs, proActivitys);
				}
			}
		} else if (proCriteria == 5) {// 前xx小时赠xx
			// 可以从起始时间和结束时间来控制前xx小时，只要买就赠
			int intValue = qty.intValue();
			if (intValue >= 1) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 2 || flag == 3) {// 限量
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 0 || flag == 1) {// 不限量
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				}
			}
		} else if (proCriteria == 6) {// 前xx名满xxx赠xxx 此种促销条件类型与proCriteria等于“4”时的情况相似，必须是限量促销
			// 也是可以设置成限量促销，只要满足xxx金额就送赠品，促销完了之后，活动结束；
			BigDecimal money = proPlansList.get(0).getMoney();// 满多少钱
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 2) {// 2：限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3：限量累加;
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
		} else if (proCriteria == 7) {// 前xx小时满xx赠xx
			// 可以从起始时间和结束时间来控制前xx小时，只要满足条件就赠，是不限量促销
			BigDecimal money = proPlansList.get(0).getMoney();// 满多少钱
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				PresentMode presentMode = presentModeDao.selectPresentModeById(giftWay);
				String presentModeCode = presentMode.getPresentModeCode();// 赠品方式编码
				String presentEncd = "";// 赠品编码，存货编码
				presentEncd = getPresentEncd(proPlansNo, presentModeCode, presentEncd);
				if (flag == 0) {// 0：不限量不累加;
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
							proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					sellSnglSubsOne = assembleGift(sellSngl, presentEncd, divide, sellSnglSubs, proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(giftNum), sellSnglSubs,
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						sellSnglSubsOne = assembleGift(sellSngl, presentEncd, new BigDecimal(allGiftNum - hasGiftNum),
								sellSnglSubs, proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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

	// 获取赠品编码；
	private String getPresentEncd(Long proPlansNo, String presentModeCode, String presentEncd) {
//        if ("0001".equals(presentModeCode)) {// 送单个（当送单个的时候赠品范围就只有一个赠品编码）
//            List<PresentRangeList> presentRangeLists = presentRangeListDao.selectPresentRangeListByProPlansNo(proPlansNo);
//            System.out.println(presentRangeLists.size() + "========================");
//            presentEncd = presentRangeLists.get(0).getPresentEncd();// 赠品编码
//        } else if ("0002".equals(presentModeCode)) {// 多选一
//            List<PresentRangeList> presentRangeLists = presentRangeListDao.selectPresentRangeListByProPlansNo(proPlansNo);
//            int nextInt = new Random().nextInt(presentRangeLists.size());// 生成一个随机的int值，该值介于[0,presentRangeLists.size())的区间
//            presentEncd = presentRangeLists.get(nextInt).getPresentEncd();// 赠品编码
//        }
		return presentEncd;
	}

	// 组装赠品对象，并添加到销售单子表中
	private List<SellSnglSub> assembleGift(SellSngl sellSngl, String presentEncd, BigDecimal giftNum,
			List<SellSnglSub> sellSnglSubs, ProActivitys proActivitys) {
		SellSnglSub sellSnglSubPresent = new SellSnglSub();// 送的赠品
		sellSnglSubPresent.setIsComplimentary(1);// 是否是赠品；1：赠品；0：非赠品
		sellSnglSubPresent.setInvtyEncd(presentEncd);// 赠品编码，存货编码
		// 仓库编号（赠品的仓库编码是固定的吗）
		// 销售单编号
		sellSnglSubPresent.setSellSnglId(sellSngl.getSellSnglId());
		// 数量
		sellSnglSubPresent.setQty(giftNum);
		// 批次
		// 促销活动送赠品中含税单价，价税合计，无税金额，无税单价，税额，税率等与金额相关的数据统统设置为零；
		sellSnglSubPresent.setCntnTaxUprc(new BigDecimal(0));// 含税单价；
		sellSnglSubPresent.setPrcTaxSum(new BigDecimal(0));// 价税合计；
		sellSnglSubPresent.setNoTaxAmt(new BigDecimal(0));// 无税金额；
		sellSnglSubPresent.setNoTaxUprc(new BigDecimal(0));// 无税单价
		sellSnglSubPresent.setTaxAmt(new BigDecimal(0));// 税额
		InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(presentEncd);
		sellSnglSubPresent.setTaxRate(invtyDoc.getOptaxRate());// 税率
		// 失效日期，生成日期，保质期
		// 最后要把赠品也放置到销售单子表中
		// 更新已赠数量；
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

	// 匹配促销活动
	public PlatOrder matchSalesPromotions(PlatOrder platOrder) {
//		首先根据订单编号查询相关订单信息，然后去匹配促销活动规则，分为几种情况:该订单没有参加活动，就直接
//		生成销售单，如果有参加活动就按照相关活动规则去匹配生成销售单。
//		匹配规则过程：首先匹配平台店铺，看是否有活动参加，没有参加活动就直接生成销售单了；
//		如果店铺参加活动了，继而匹配订单中商品，看该商品是否参加活动，如果没有参加活动就直接生成销售单了；
//		如果参加活动了，看一个商品参加了几个活动，如果是多个活动要选择优先级高的活动取匹配
//		匹配完活动之后再看是否限量促销--是否赠品倍增--促销条件--组装赠品并添加到销售单子表中
		logger.info("判断是否匹配过活动");

		if (platOrder.getCanMatchActive() != null && platOrder.getCanMatchActive() == 1) {
			// 判断是否匹配过活动
			logger.info("匹配过活动");

			return platOrder;
		}
		logger.info("没有匹配过活动");
		platOrder.setCanMatchActive(1);
		String storeId = platOrder.getStoreId();// 店铺编号
		String payTime = platOrder.getPayTime();// 付款时间

		List<ProActivity> proActivities = proActivityDao.selectStorePayTime(payTime, storeId);
		if (proActivities != null && proActivities.size() > 0) {// a 判断商品活动中间表中是不是有这个店铺正在参加活动
			List<PlatOrders> list = platOrder.getPlatOrdersList();
			for (PlatOrders platOrders : list) {
				if (platOrders.getIsGift() == 1) {
//					订单中赠品跳过
					continue;
				}
				// 然后再看从这个店铺购买的商品项参加了哪些活动；
				BigDecimal prcTaxSum = platOrders.getPayMoney();// 价税合计 实付金额
				BigDecimal qty = BigDecimal.valueOf(platOrders.getInvNum());// 商品项数量
				String invtyEncd = platOrders.getInvId();// 存货编码
				ProActivity proActivity = proActivityDao.selectStorePayTimeORDERLIMIT(payTime, storeId, invtyEncd);
				if (proActivity != null) {// b 优先级最高的
					platOrder = matchManyActivitys(platOrder,  proActivity, prcTaxSum, qty, invtyEncd);
				} else {
					logger.info(invtyEncd + "该商品没有参加促销活动。。。");

				}

			}
		} else {// a 店铺没有参加促销活动
			logger.info("该店铺没有参加促销活动。");
		}
		return platOrder;
	}

	// 匹配优先级，选优先级最高的活动去匹配活动；
	private PlatOrder matchManyActivitys(PlatOrder platOrder,  ProActivity proActivity,
			BigDecimal prcTaxSum, BigDecimal qty, String invtyEncd) {
//根据匹配活动查询所有活动子表
		String proActId = proActivity.getProActId();// 促销活动主表id
		List<ProActivitys> proActivitysList = proActivitysDao.selectProActIdAllGoods(proActId, invtyEncd);// 促销活动子表数据
//        循环促销方案子表
		int limitPro = proActivity.getLimitPro();// 是否限量促销

		for (ProActivitys proActivitys : proActivitysList) {
			List<PlatOrders> list =platOrder.getPlatOrdersList();
			ProPlan proPlan = proPlanDao.select(proActivitys.getProPlanId());// 促销方案主表

			Integer giftMul = proPlan.getGiftMul();// 是否赠品倍增；1：赠品累加；0：赠品不累加；
			Long proCriteria = proPlan.getProCriteria();// 促销条件编码
			if (limitPro == 0 && giftMul == 0) {// 不限量 不累加
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 0);

			} else if (limitPro == 0 && giftMul == 1) {// 不限量 累加
				// 累加前面的逻辑跟不累加是差不多的，然后区别就是匹配集中促销条件时，如果购买的数量和金额时成倍增加的时候，赠品也对应成倍增加
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 1);

			} else if (limitPro == 1 && giftMul == 0) {// 不累加 限量 要查看活动子表中的赠品数量是否大于已赠数量（大于:参加活动，否则活动结束）
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 2);

			} else if (limitPro == 1 && giftMul == 1) {// 不累加 限量 要查看活动子表中的赠品数量是否大于已赠数量（大于:参加活动，否则活动结束）
				// 累加前面的逻辑跟不累加是差不多的，然后区别就是匹配集中促销条件时，如果购买的数量和金额时成倍增加的时候，赠品也对应成倍增加
				platOrder = matchActivityConditions(platOrder, list, prcTaxSum, qty, proCriteria, proActivitys, 3);

			} else {
				logger.info("累加限量错误");
			}

		}

		return platOrder;
	}

	// 匹配活动条件 参数1：订单主表，2：订单子表，3：实付金额，4：商品数量，5：促销条件编码，6:促销方案子表，7：促销活动子表，
	// 8：支持商品倍增标记：0：不限量不累加;1：不限量累加;2:限量不累加;3:限量累加;
	/**
	 * @param platOrder    订单主表
	 * @param list         订单子表
	 * @param prcTaxSum    实付金额
	 * @param qty          商品数量
	 * @param proCriteria  促销条件编码
	 * @param proActivitys 促销活动子表
	 * @param flag         支持商品倍增标记：0：不限量不累加;1：不限量累加;2:限量不累加;3:限量累加;
	 */
	private PlatOrder matchActivityConditions(PlatOrder platOrder, List<PlatOrders> list, BigDecimal prcTaxSum,
			BigDecimal qty, Long proCriteria, ProActivitys proActivitys, Integer flag) {

//        Long proPlansNo;// 促销方案子表id
//        Integer giftWay;// 赠品方式

		Integer allGiftNum = proActivitys.getGiftNum();// 促销活动子表中限量促销时总的赠品数量；
		Integer hasGiftNum = proActivitys.getHasGiftNum();// 促销活动子表中限量促销时总的已赠数量；
		// 匹配促销条件
		if (proCriteria == 1) {// 买一送一 买一送一的时候，促销方案子表只能添加一条数据，因为一条数据已经能够满足需求；
			int intValue = qty.intValue();
			if (intValue >= 1) {

				ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());// 促销方案子表
				if (proPlans == null) {
					logger.info("买一送一无促销方案子表");
					return platOrder;
				}
				proPlans.getGiftNum();// 赠品数量
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码
				if (flag == 0) {// 0：不限量不累加;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(1), proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(intValue), proActivitys);// 累加之后就变成买n赠n啦
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum > hasGiftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(1), proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
//					限量促销时总的赠品数量-已赠数量+买一送一 
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
		} else if (proCriteria == 2) {// 买m送n 感觉这个活动条件没有必要，买一送一可累加就是买n送n了。
			int intValue = qty.intValue();
			ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());// 促销方案子表
			if (proPlans == null) {
				logger.info("买m送n无符合促销方案子表");
				return platOrder;
			}
			int giftNum = proPlans.getGiftNum();
			int number = proPlans.getNumber();
			List<PresentRangeList> presentEncdList = presentRangeListDao
					.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码
			if (intValue >= number) {

				if (flag == 0) {// 0：不限量不累加;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					giftNum = intValue / number * giftNum;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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
		} else if (proCriteria == 3) {// 满额送 满额送也是只能有一条促销方案子表数据；
//            BigDecimal money = proPlansList.get(0).getMoney();// 满多少钱
			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");// 促销方案子表
			if (proPlans == null) {
				logger.info("满额送无符合促销方案子表");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();
			int giftNum = proPlans.getGiftNum();
			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码

				if (flag == 0) {// 0：不限量不累加;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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
		} else if (proCriteria == 4) {// 前xx名赠xx
			// 前多少名赠赠品，可以理解成限量促销；只要下单就送
			ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());
			if (proPlans == null) {
				logger.info("前xx名赠xx无符合促销方案子表");
				return platOrder;
			}
			int giftNum = proPlans.getGiftNum();
			List<PresentRangeList> presentEncdList = presentRangeListDao
					.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码

			if (flag == 2 || flag == 3) {// 限量 此促销条件类型下，必须是限量促销
				if (allGiftNum >= hasGiftNum + giftNum) {
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(allGiftNum - hasGiftNum),
							proActivitys);
				}
			}
		} else if (proCriteria == 5) {// 前xx小时赠xx
			// 可以从起始时间和结束时间来控制前xx小时，只要买就赠
			int intValue = qty.intValue();
			if (intValue >= 1) {
				ProPlans proPlans = proPlansDao.selectORDERLIMIT(proActivitys.getProPlanId());
				if (proPlans == null) {
					logger.info("前xx小时赠xx无符合促销方案子表");
					return platOrder;
				}
				int giftNum = proPlans.getGiftNum();
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码

				if (flag == 2 || flag == 3) {// 限量
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 0 || flag == 1) {// 不限量
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				}
			}
		} else if (proCriteria == 6) {// 前xx名满xxx赠xxx 此种促销条件类型与proCriteria等于“4”时的情况相似，必须是限量促销
			// 也是可以设置成限量促销，只要满足xxx金额就送赠品，促销完了之后，活动结束；
//            BigDecimal money = proPlansList.get(0).getMoney();// 满多少钱

			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");
			if (proPlans == null) {
				logger.info("前xx名满xxx赠xxx无符合促销方案子表");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();// 满多少钱
			int giftNum = proPlans.getGiftNum();

			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码

				if (flag == 2) {// 2：限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3：限量累加;
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
		} else if (proCriteria == 7) {// 前xx小时满xx赠xx
			// 可以从起始时间和结束时间来控制前xx小时，只要满足条件就赠，是不限量促销

			ProPlans proPlans = proPlansDao.selectMoneyORDERLIMIT(proActivitys.getProPlanId(),
					prcTaxSum.doubleValue() + "");
			if (proPlans == null) {
				logger.info("前xx小时满xx赠xx无符合促销方案子表");
				return platOrder;
			}
			BigDecimal money = proPlans.getMoney();// 满多少钱
			int giftNum = proPlans.getGiftNum();

			if (prcTaxSum.doubleValue() > money.doubleValue()) {
				List<PresentRangeList> presentEncdList = presentRangeListDao
						.selectByPresentRangeList(proPlans.getGiftRange());// 赠品编码

				if (flag == 0) {// 0：不限量不累加;
					platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum), proActivitys);
				} else if (flag == 1) {// 1：不限量累加;
					BigDecimal divide = prcTaxSum.divide(money, 0, BigDecimal.ROUND_DOWN)
							.multiply(new BigDecimal(giftNum));
					platOrder = assembleGifts(platOrder, list, presentEncdList, divide, proActivitys);
				} else if (flag == 2) {// 2:限量不累加;
					if (allGiftNum >= hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList, new BigDecimal(giftNum),
								proActivitys);
					} else if (allGiftNum > hasGiftNum && allGiftNum < hasGiftNum + giftNum) {
						platOrder = assembleGifts(platOrder, list, presentEncdList,
								new BigDecimal(allGiftNum - hasGiftNum), proActivitys);
					}
				} else if (flag == 3) {// 3:限量累加;
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

	// 组装赠品对象，并添加到销售单子表中
	private PlatOrder assembleGifts(PlatOrder platOrder, List<PlatOrders> list, List<PresentRangeList> presentEncdList,
			BigDecimal giftNum, ProActivitys proActivitys) {
		List<PlatOrders> newList = new ArrayList<PlatOrders>();
		newList.addAll(list);
		for (PresentRangeList presentEncd : presentEncdList) {
			// 设置主表是否含赠品
			platOrder.setHasGift(1);
			PlatOrders platOrders = new PlatOrders();// 送的赠品
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(presentEncd.getPresentEncd());// 赠品编码

//               String goodId;//商品编号
			platOrders.setGoodNum(giftNum.intValue());// 商品数量 原单数量
			platOrders.setGoodMoney(BigDecimal.ZERO);// 商品金额
			platOrders.setPayMoney(BigDecimal.ZERO);// 实付金额
			platOrders.setGoodName(invtyDoc.getInvtyNm());// 平台商品名称
//               String goodSku;//商品sku
			platOrders.setOrderId(platOrder.getOrderId());// 订单编号
//               batchNo;//批号
			platOrders.setExpressCom(platOrder.getExpressCode());// 快递公司
			platOrders.setProActId(proActivitys.getProActId());// 促销活动编号
			platOrders.setDiscountMoney(BigDecimal.ZERO);// 系统优惠金额
			platOrders.setAdjustMoney(BigDecimal.ZERO);// 卖家调整金额
//               String memo;//备注
			platOrders.setGoodPrice(BigDecimal.ZERO);// 商品单价
			platOrders.setPayPrice(BigDecimal.ZERO);// 实付单价
			platOrders.setDeliverWhs(platOrder.getDeliverWhs());// 发货仓库编码
			platOrders.setSellerPrice(BigDecimal.ZERO);// 结算单价
			platOrders.setEcOrderId(platOrder.getEcOrderId());// 平台订单号
			platOrders.setInvId(presentEncd.getPresentEncd());// 赠品编码，存货编码
			platOrders.setInvNum(giftNum.intValue());// 存货数量
//               String ptoCode;//母件编码
//               String ptoName;//母件名称
			platOrders.setCanRefNum(giftNum.intValue());// 可退数量
			platOrders.setCanRefMoney(BigDecimal.ZERO);// 可退金额
//               Integer splitNum;//拆分数量
			platOrders.setIsGift(1);// 是否是赠品；1：赠品；0：非赠品
//                prdcDt;//生产日期
//                invldtnDt;//失效日期
			proActivitys.setHasGiftNum(giftNum.intValue());
			// 最后要把赠品也放置到订单子表中
			// 更新已赠数量；
			proActivitysDao.updateHasGiftNum(proActivitys);
			newList.add(platOrders);
		}

		platOrder.setPlatOrdersList(newList);
		return platOrder;
	}


}
