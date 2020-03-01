package com.px.mis.account.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.FormBackFlushDao;
import com.px.mis.account.dao.FormBackFlushSubDao;
import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.dao.FormBookEntrySubDao;
import com.px.mis.account.dao.InvtyDetailDao;
import com.px.mis.account.dao.InvtyMthTermBgnTabDao;
import com.px.mis.account.dao.InvtySendMthTermBgnTabDao;
import com.px.mis.account.dao.MthEndStlDao;
import com.px.mis.account.dao.MthTermBgnTabDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglSubTabDao;
import com.px.mis.account.dao.PursInvMasTabDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellInvMasTabDao;
import com.px.mis.account.dao.TermBgnBalDao;
import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.InvtyMthTermBgnTab;
import com.px.mis.account.entity.MthEndStl;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.account.thread.Task;
import com.px.mis.account.thread.TaskDistributor;
import com.px.mis.account.thread.WorkThread;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.taobao.api.response.AlibabaWdkMarketingExpirePromotionCreateResponse.T;

/**
 * ���ݼ�����ؽӿ�
 *
 */
@Service
public class FormBookEntryUtil {

	private Logger logger = LoggerFactory.getLogger(FormBookServiceImpl.class);

	@Autowired
	private IntoWhsDao intoWhsDao; // �ɹ���ⵥ
	@Autowired
	private OthOutIntoWhsMapper othOutDao; // ��������ⵥ
	@Autowired
	private SellOutWhsDao sellOutWhsDao; // ���۳��ⵥ
	@Autowired
	private AcctItmDocDao accItemDao; // ��ƿ�Ŀ
	@Autowired
	private MthTermBgnTabDao mthTermBgnTabDao; // �ڳ����
	@Autowired
	private TermBgnBalDao termBgnBalDao; // �ڳ�
	@Autowired
	private PursInvMasTabDao pursInvMasTabDao; // �ɹ���Ʊ
	@Autowired
	private SellInvMasTabDao sellInvMasTabDao; // ���۷�Ʊ
	@Autowired
	private InvtyDetailDao invtyDetailDao; // �����ϸ��
	@Autowired
	private InvtyMthTermBgnTabDao invtyMthTermBgnTabDao; // ��������ڳ�
	@Autowired
	private GetOrderNo getOrderNo; // ���ɵ���
	@Autowired
	private OutIntoWhsAdjSnglDao outIntoWhsAdjSnglDao; // �����������
	@Autowired
	private OutIntoWhsAdjSnglSubTabDao outIntoWhsAdjSnglSubTabDao; // ��������ӵ�
	@Autowired
	private FormBookEntryDao formBookEntryDao; // ��������
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; // �����ӱ�
	@Autowired
	private MthEndStlDao mthEndStlDao; // ��ĩ����
	@Autowired
	private InvtyDocDao invtyDocDao; // �������
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; // ������Ʒ�����ڳ�
	@Autowired
	private SellSnglDao sellSnglDao; // ���۵�
	@Autowired
	private SellComnInvDao sellComnInvDao; // ��Ʊ
	@Autowired
	private FormBackFlushDao formBackFlushDao; // �س嵥
	@Autowired
	private FormBackFlushSubDao formBackFlushSubDao; // �س嵥�ӱ�
	@Autowired
	private InvtyClsDao invtyClsDao; // �������
	@Autowired
	private CustDocDao custDocDao;
	@Autowired
	private InvtyTabDao invtyTabDao; // ����

	/**
	 * ���ݼ���
	 */
	public String formBookEntryAss(List<FormBookEntry> dataList, String userName, String loginTime) throws Exception {
		Map<String, Object> parmMap = new HashMap<>();

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		// ���ݼ��˹���ҵ��
		if (dataList.size() != 0) {
			parmMap.put("formNumList", dataList);
			parmMap.put("isPage", "");
			parmMap.put("isNtBookOk", "0");
			dataList = formBookEntryDao.selectStreamALLList(parmMap);
			if (dataList.size() > 0) {

				// �ɱ�����-���۳ɱ�����
				message = countAvgUprc(dataList, loginTime, userName);

			} else {
				message = "����������ݷ����쳣";
				isSuccess = false;
			}
		} else {
			message = "����������ݷ����쳣";
			isSuccess = false;

		}

		resp = BaseJson.returnRespObj("account/form/normal/book", isSuccess, message, null);
		return resp;

	}

	/**
	 * avgƽ���ɱ�
	 */
	private String countAvgUprc(List<FormBookEntry> dataList, String loginTime, String accNum) {

		Map<String, Object> dataMap = new HashMap<>();

		Long beginTime = new Date().getTime(); // �ӿڿ�ʼʱ��
		int count = 0;
		for (FormBookEntry il : dataList) {
			count += il.getBookEntrySub().size();
		}

		dataMap.put("bookDt", loginTime);

		if (dataList.size() > 0) {
			List<List<FormBookEntry>> ilist = Lists.partition(dataList, 100);

			ilist.forEach(formList -> {
				List<FormBookEntrySub> pursIntoList = new ArrayList<>();
				List<FormBookEntrySub> othList = new ArrayList<>();
				List<FormBookEntrySub> subList = new ArrayList<>();

				formList.forEach(form -> {

					form.getBookEntrySub().forEach(ss -> ss.setProjEncd(form.getCustId()));

					if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {

						pursIntoList.addAll(form.getBookEntrySub());
					} else {
						othList.addAll(form.getBookEntrySub());
					}
					form.getBookEntrySub().clear();

				});

				if (pursIntoList.size() > 0) {
					// �ɹ��ɱ�
					dataMap.put("pursIntoList", pursIntoList);
					List<PursInvSubTab> pursInvList = pursInvMasTabDao.countPursNoTaxAmtAndQtyList(dataMap);

					if (pursInvList.size() > 0) {
						Map<String, PursInvSubTab> piMap = new HashMap<>();
						pursInvList.forEach(pi -> {
							String key = pi.getInvtyEncd() + pi.getBatNum() + pi.getCrspdIntoWhsSnglNum();
							if (!piMap.containsKey(key)) {
								piMap.put(key, pi);
							}
						});

						pursIntoList.forEach(sub -> {
							String key = sub.getInvtyEncd() + sub.getBatNum() + sub.getFormNum();
							if (piMap.containsKey(key)) {
								if (piMap.get(key).getNoTaxUprc() != null) {
									sub.setNoTaxUprc(piMap.get(key).getNoTaxUprc());
									sub.setNoTaxAmt(piMap.get(key).getNoTaxUprc().multiply(sub.getQty()));
								}

							}
						});

					}
					subList.addAll(pursIntoList);
				}

				if (othList.size() > 0) {
					// ����ɱ�
					dataMap.put("othList", othList);
					List<PursInvSubTab> othInvList = pursInvMasTabDao.countSellAndOthAmtList(dataMap);
					if (othInvList.size() > 0) {
						Map<String, PursInvSubTab> outMap = new HashMap<>();
						othInvList.forEach(pi -> {
							String key = pi.getWhsEncd() + pi.getInvtyEncd() + pi.getBatNum();
							if (!outMap.containsKey(key)) {
								outMap.put(key, pi);
							}
						});

						othInvList.forEach(sub -> {
							String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum();
							if (outMap.containsKey(key)) {
								if (outMap.get(key).getNoTaxUprc() != null) {
									sub.setNoTaxUprc(outMap.get(key).getNoTaxUprc());
									sub.setNoTaxAmt(outMap.get(key).getNoTaxUprc().multiply(sub.getQty()));
								}

							}
						});
					}
					subList.addAll(othList);
				}

				if (subList.size() > 0) {

					othList.clear();

					// ��ѯ�����Ӧ�ĸ����ڳ�����
					dataMap.clear();
					dataMap.put("invtyList", subList);
					dataMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
					dataMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
					dataMap.put("bookDt", loginTime);
					Map<String, InvtyDetail> ivMap = new HashMap<>();
					Map<String, InvtyMthTermBgnTab> imtMap = new HashMap<>(); // �շ���
					Map<String, InvtyMthTermBgnTab> isdMap = new HashMap<>(); // ������Ʒ
					Map<String, SellComnInvSub> selMap = new HashMap<>(); // ����-��ϸ

					// ��ϸ-���
					List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(dataMap);
					if (itList.size() > 0) {
						itList.forEach(it -> {
							String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd();
							if (!ivMap.containsKey(key)) {
								ivMap.put(key, it);
							}
						});

					}

					// ����ڳ�
					List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(dataMap);
					if (tabList.size() > 0) {
						tabList.forEach(it -> {
							String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd();
							if (!imtMap.containsKey(key)) {
								imtMap.put(key, it);
							}
						});
					}
					// ������Ʒ-�ڳ�
					List<InvtyMthTermBgnTab> sendTabList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(dataMap);
					if (sendTabList.size() > 0) {
						sendTabList.forEach(it -> {
							String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd()
									+ it.getCustId();
							if (!isdMap.containsKey(key)) {
								isdMap.put(key, it);
							}
						});
					}
					// ������Ʒ-������ϸ
					List<SellComnInvSub> sellSubList = sellComnInvDao.selectSellComnInvToQty(dataMap);
					if (sellSubList.size() > 0) {
						sellSubList.forEach(it -> {
							String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getOutWhsId();
							if (!selMap.containsKey(key)) {
								selMap.put(key, it);
							}
						});
					}

					List<InvtyDetails> idsList = new ArrayList<>(); // ������ϸ
					List<InvtyMthTermBgnTab> imList = new ArrayList<>(); // ������ϸ
					List<InvtyMthTermBgnTab> isdList = new ArrayList<>(); // ������ϸ
					List<InvtyDetail> idList = new ArrayList<>();
					subList.forEach(sub -> {

						formList.forEach(form -> {
							InvtyDetails its = new InvtyDetails();

							// ���۳ɱ�����
							if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
								othList.add(sub);
							}

							if (sub.getFormNum().equals(form.getFormNum())) {
								if (form.getBookEntrySub().size() > 0) {
									List<FormBookEntrySub> tempList = form.getBookEntrySub();
									tempList.add(sub);
									form.setBookEntrySub(tempList);
								} else {
									List<FormBookEntrySub> tempList = new ArrayList<>();
									tempList.add(sub);
									form.setBookEntrySub(tempList);
								}
								form.setIsNtBookOk(1);
								form.setBookOkAcc(accNum);
								form.setBookOkDt(loginTime);
								form.setIsNtBookOk(1);
								form.setBookOkAcc(accNum);
								form.setBookOkDt(loginTime);
								form.setIsNtMakeVouch(0);
								form.setIsMthEndStl(0);
								form.setIsNtUploadHead(0);
							}
							// ��ϸ��
							String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum()
									+ sub.getInvtyClsEncd();
							InvtyDetail it = new InvtyDetail();

							if (ivMap.containsKey(key)) {
								it = ivMap.get(key);
							} else {

								it = new InvtyDetail(sub.getInvtyNm(), sub.getSpcModel(), sub.getMeasrCorpNm(),
										sub.getInvtyEncd(), sub.getInvtyCd(), sub.getBatNum(), sub.getWhsEncd(),
										sub.getInvtyClsEncd(), sub.getInvtyClsNm(), sub.getWhsNm());

								int i = invtyDetailDao.insertInvtyDetail(it);
								// idList.add(it);
							}

							// �ڳ�
							InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();
							if (imtMap.containsKey(key)) {
								im = imtMap.get(key);
							} else {

								im = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum,
										loginTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form, sub);
								im.setOrdrNum(-1);
							}
							im = initImValue(im);

							// ������Ʒ
							InvtyMthTermBgnTab ise = new InvtyMthTermBgnTab();
							if (isdMap.containsKey(key + it.getCustId())) {
								ise = isdMap.get(key + it.getCustId());
							} else {

								ise = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum,
										loginTime, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form, sub);
								ise.setOrdrNum(-1);
							}
							ise = initImValue(ise);

							if (CommonUtil.strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
								// ����
								its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
										form.getOutIntoWhsTypNm(), sub.getNoTaxUprc(), sub.getQty(), sub.getNoTaxAmt(),
										null, null, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
										form.getFormNum(), it.getDetailId(), sub.getOrdrNum());

								im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
								im.setInQty(im.getInQty().add(sub.getQty()));
								if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
									im.setInUnitPrice(new BigDecimal(0.00000000));
								} else {
									im.setInUnitPrice(
											im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}

							}

							if (CommonUtil.strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
								// ����
								its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
										form.getOutIntoWhsTypNm(), null, null, null, sub.getNoTaxUprc(), sub.getQty(),
										sub.getNoTaxAmt(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
										form.getFormNum(), it.getDetailId(), sub.getOrdrNum());

								if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
									ise.setInMoeny(ise.getInMoeny().add(sub.getNoTaxAmt()));
									ise.setInQty(ise.getInQty().add(sub.getQty()));
									if (ise.getInQty().compareTo(BigDecimal.ZERO) == 0) {
										ise.setInUnitPrice(new BigDecimal(0.00000000));
									} else {
										ise.setInUnitPrice(ise.getInMoeny()
												.divide(ise.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
									}

									// ������Ʒ-�ڳ�
									if (selMap.containsKey(sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum()
											+ sub.getFormNum())) {
										SellComnInvSub sel = selMap.get(sub.getWhsEncd() + sub.getInvtyEncd()
												+ sub.getBatNum() + sub.getFormNum());
										ise.setSendQty(ise.getSendQty().add(sel.getQty()));
										ise.setSendMoeny(ise.getSendMoeny().add(sel.getNoTaxAmt()));
										if (ise.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
											ise.setSendUnitPrice(new BigDecimal(0.00000000));
										} else {
											ise.setSendUnitPrice(ise.getSendMoeny()
													.divide(ise.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
										}
										ise.setOthQty(ise.getQty().add(ise.getInQty()).subtract(ise.getSendQty()));
										ise.setOthMoeny(
												ise.getAmt().add(ise.getInMoeny()).subtract(ise.getSendMoeny()));
										if (ise.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
											ise.setOthUnitPrice(new BigDecimal(0.00000000));
										} else {
											ise.setOthUnitPrice(ise.getOthMoeny()
													.divide(ise.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
										}
									}
									isdList.add(ise);
								}
								// fa
								im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
								im.setSendQty(im.getSendQty().add(sub.getQty()));
								if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
									im.setSendUnitPrice(new BigDecimal(0.00000000));
								} else {
									im.setSendUnitPrice(im.getSendMoeny()
											.divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}

							}
							// ��浥�� = (�����ڳ����+����������-���µķ���)/(�����ڳ�����+������������-����)
							im.setOthQty(im.getQty().add(im.getInQty()).subtract(im.getSendQty()));
							im.setOthMoeny(im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny()));
							if (im.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
								im.setOthUnitPrice(new BigDecimal(0.00000000));
							} else {
								im.setOthUnitPrice(
										im.getOthMoeny().divide(im.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}
							its.setOthMoeny(im.getOthMoeny());
							its.setOthQty(im.getOthQty());
							its.setOthUnitPrice(im.getOthUnitPrice());
							idsList.add(its);

							imList.add(im);

						});

					});

					// ���۳ɱ�����
					if (othList.size() > 0) {
						List<List<FormBookEntrySub>> lists = Lists.partition(othList, 100);
						for (List<FormBookEntrySub> iList : lists) {
							sellOutWhsDao.updateCost(iList);
						}
					}

					if (imList.size() > 0) {
						List<InvtyMthTermBgnTab> imU = new ArrayList<>();
						List<InvtyMthTermBgnTab> imI = new ArrayList<>();

						imList.forEach(im -> {
							if (im.getOrdrNum() == -1) {
								imI.add(im);
							} else {
								imU.add(im);
							}
						});
						if (imI.size() > 0) {
							invtyMthTermBgnTabDao.insertMthList(imI);
						}
						if (imU.size() > 0) {
							invtyMthTermBgnTabDao.updateMthList(imU);
						}
					}
					if (isdList.size() > 0) {
						List<InvtyMthTermBgnTab> imU = new ArrayList<>();
						List<InvtyMthTermBgnTab> imI = new ArrayList<>();
						isdList.forEach(im -> {
							if (im.getOrdrNum() == -1) {
								// insert
								imI.add(im);
							} else {
								// update
								imU.add(im);
							}
						});
						if (imI.size() > 0) {
							invtySendMthTermBgnTabDao.insertMthList(imI);
						}
						if (imU.size() > 0) {
							invtySendMthTermBgnTabDao.updateSendMthList(imU);
						}
					}
					if (idsList.size() > 0) {
						List<List<InvtyDetails>> list = Lists.partition(idsList, 100);
						list.forEach(li -> {
							invtyDetailDao.insertInvtyDetailsList(li);
						});

					}
					if (idList.size() > 0) {
						List<List<InvtyDetail>> list = Lists.partition(idList, 100);
						list.forEach(li -> {
							invtyDetailDao.insertInvtyDetailList(li);
						});
					}
					if (formList.size() > 0) {
						List<List<FormBookEntry>> list = Lists.partition(formList, 100);

						list.forEach(li -> {
							List<FormBookEntry> selList = new ArrayList<>();
							List<FormBookEntry> intList = new ArrayList<>();
							List<FormBookEntry> outList = new ArrayList<>();

							li.forEach(lis -> {
								if (Integer.valueOf(lis.getOutIntoWhsTypId()) == 9) {
									intList.add(lis);
								} else if (Integer.valueOf(lis.getOutIntoWhsTypId()) == 10) {
									selList.add(lis);
								} else {
									outList.add(lis);
								}
							});
							formBookEntryDao.insertFormList(li);
							if (selList.size() > 0) {
								formBookEntryDao.updateSellOutWhsBookEntryList(selList);
							}
							if (intList.size() > 0) {
								formBookEntryDao.updateIntoWhsBookEntryList(intList);
							}
							if (outList.size() > 0) {
								formBookEntryDao.updateOutInWhsBookEntry(outList);
							}

						});

					}

				}

				if (subList.size() > 0) {
					List<List<FormBookEntrySub>> list = Lists.partition(subList, 100);
					list.forEach(li -> {
						formBookEntrySubDao.insertSubList(li);
					});
				}

			});

		}

		Long opetime = new Date().getTime() - beginTime;
		long hour = opetime / (60 * 60 * 1000);
		long minute = (opetime - hour * 60 * 60 * 1000) / (60 * 1000);
		long second = (opetime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;

		return "���ݼ��˽ӿڵ�����ɣ����ι��ƣ�" + count + "�����ݣ���ʱ��" + hour + "ʱ" + minute + "�� " + second + "��";

	}

	public void dealInvty(List<FormBookEntry> formList, String loginTime, String accNum) {
		if (formList.size() > 0) {
			List<FormBookEntrySub> subList = new ArrayList<>();
			formList.forEach(fr -> subList.addAll(fr.getBookEntrySub()));

			List<String> invtysList = subList.stream().map(FormBookEntrySub::getFormNum).collect(Collectors.toList());
			invtysList = invtysList.stream().distinct().collect(Collectors.toList());

			List<String> whsList = subList.stream().map(FormBookEntrySub::getWhsEncd).collect(Collectors.toList());
			whsList = whsList.stream().distinct().collect(Collectors.toList());

			List<String> batNumList = subList.stream().map(FormBookEntrySub::getBatNum).collect(Collectors.toList());
			batNumList = batNumList.stream().distinct().collect(Collectors.toList());

			Map<String, Object> dataMap = new HashMap<>();

			// ��ѯ�����Ӧ�ĸ����ڳ�����
			dataMap.clear();
			dataMap.put("invtysList", invtysList);
			dataMap.put("whsList", whsList);
			dataMap.put("batNumList", batNumList);
			dataMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
			dataMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
			dataMap.put("bookDt", loginTime);
			Map<String, InvtyDetail> ivMap = new HashMap<>();
			Map<String, InvtyMthTermBgnTab> imtMap = new HashMap<>(); // �շ���
			Map<String, InvtyMthTermBgnTab> isdMap = new HashMap<>(); // ������Ʒ
			Map<String, SellComnInvSub> selMap = new HashMap<>(); // ����-��ϸ

			// ��ϸ-���
			List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(dataMap);
			if (itList.size() > 0) {
				itList.forEach(it -> {
					String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd();
					if (!ivMap.containsKey(key)) {
						ivMap.put(key, it);
					}
				});

			}

			// ����ڳ�
			List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(dataMap);
			if (tabList.size() > 0) {
				tabList.forEach(it -> {
					String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd();
					if (!imtMap.containsKey(key)) {
						imtMap.put(key, it);
					}
				});
			}
			// ������Ʒ-�ڳ�
			List<InvtyMthTermBgnTab> sendTabList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(dataMap);
			if (sendTabList.size() > 0) {
				sendTabList.forEach(it -> {
					String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd()
							+ it.getCustId();
					if (!isdMap.containsKey(key)) {
						isdMap.put(key, it);
					}
				});
			}
			// ������Ʒ-������ϸ
			List<SellComnInvSub> sellSubList = sellComnInvDao.selectSellComnInvToQty(dataMap);
			if (sellSubList.size() > 0) {
				sellSubList.forEach(it -> {
					String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getOutWhsId();
					if (!selMap.containsKey(key)) {
						selMap.put(key, it);
					}
				});
			}

			List<InvtyDetails> idsList = new ArrayList<>(); // ������ϸ
			List<InvtyMthTermBgnTab> imList = new ArrayList<>(); // ������ϸ
			List<InvtyMthTermBgnTab> isdList = new ArrayList<>(); // ������ϸ
			List<InvtyDetail> idList = new ArrayList<>();
			formList.forEach(form -> {

				subList.forEach(sub -> {
					InvtyDetails its = new InvtyDetails();

					// ��ϸ��
					String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum() + sub.getInvtyClsEncd();
					InvtyDetail it = new InvtyDetail();

					if (ivMap.containsKey(key)) {
						it = ivMap.get(key);
					} else {

						it = new InvtyDetail(sub.getInvtyNm(), sub.getSpcModel(), sub.getMeasrCorpNm(),
								sub.getInvtyEncd(), sub.getInvtyCd(), sub.getBatNum(), sub.getWhsEncd(),
								sub.getInvtyClsEncd(), sub.getInvtyClsNm(), sub.getWhsNm());

						int i = invtyDetailDao.insertInvtyDetail(it);
						// idList.add(it);
					}

					// �ڳ�
					InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();
					if (imtMap.containsKey(key)) {
						im = imtMap.get(key);
					} else {

						im = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum, loginTime,
								BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form, sub);
						im.setOrdrNum(-1);
					}
					im = initImValue(im);

					// ������Ʒ
					InvtyMthTermBgnTab ise = new InvtyMthTermBgnTab();
					if (isdMap.containsKey(key + it.getCustId())) {
						ise = isdMap.get(key + it.getCustId());
					} else {

						ise = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum, loginTime,
								BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form, sub);
						ise.setOrdrNum(-1);
					}
					ise = initImValue(ise);

					if (CommonUtil.strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
						// ����
						its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
								form.getOutIntoWhsTypNm(), sub.getNoTaxUprc(), sub.getQty(), sub.getNoTaxAmt(), null,
								null, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form.getFormNum(),
								it.getDetailId(), sub.getOrdrNum());

						im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
						im.setInQty(im.getInQty().add(sub.getQty()));
						if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
							im.setInUnitPrice(new BigDecimal(0.00000000));
						} else {
							im.setInUnitPrice(im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
						}

					}

					if (CommonUtil.strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
						// ����
						its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
								form.getOutIntoWhsTypNm(), null, null, null, sub.getNoTaxUprc(), sub.getQty(),
								sub.getNoTaxAmt(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, form.getFormNum(),
								it.getDetailId(), sub.getOrdrNum());

						if (Integer.valueOf(form.getOutIntoWhsTypId()) == 10) {
							ise.setInMoeny(ise.getInMoeny().add(sub.getNoTaxAmt()));
							ise.setInQty(ise.getInQty().add(sub.getQty()));
							if (ise.getInQty().compareTo(BigDecimal.ZERO) == 0) {
								ise.setInUnitPrice(new BigDecimal(0.00000000));
							} else {
								ise.setInUnitPrice(
										ise.getInMoeny().divide(ise.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}

							// ������Ʒ-�ڳ�
							if (selMap.containsKey(
									sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum() + sub.getFormNum())) {
								SellComnInvSub sel = selMap.get(
										sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum() + sub.getFormNum());
								ise.setSendQty(ise.getSendQty().add(sel.getQty()));
								ise.setSendMoeny(ise.getSendMoeny().add(sel.getNoTaxAmt()));
								if (ise.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
									ise.setSendUnitPrice(new BigDecimal(0.00000000));
								} else {
									ise.setSendUnitPrice(ise.getSendMoeny()
											.divide(ise.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}
								ise.setOthQty(ise.getQty().add(ise.getInQty()).subtract(ise.getSendQty()));
								ise.setOthMoeny(ise.getAmt().add(ise.getInMoeny()).subtract(ise.getSendMoeny()));
								if (ise.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
									ise.setOthUnitPrice(new BigDecimal(0.00000000));
								} else {
									ise.setOthUnitPrice(ise.getOthMoeny()
											.divide(ise.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
								}
							}
							isdList.add(ise);
						}
						// fa
						im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
						im.setSendQty(im.getSendQty().add(sub.getQty()));
						if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
							im.setSendUnitPrice(new BigDecimal(0.00000000));
						} else {
							im.setSendUnitPrice(
									im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
						}

					}
					// ��浥�� = (�����ڳ����+����������-���µķ���)/(�����ڳ�����+������������-����)
					im.setOthQty(im.getQty().add(im.getInQty()).subtract(im.getSendQty()));
					im.setOthMoeny(im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny()));
					if (im.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
						im.setOthUnitPrice(new BigDecimal(0.00000000));
					} else {
						im.setOthUnitPrice(im.getOthMoeny().divide(im.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
					}
					its.setOthMoeny(im.getOthMoeny());
					its.setOthQty(im.getOthQty());
					its.setOthUnitPrice(im.getOthUnitPrice());
					idsList.add(its);

					imList.add(im);

				});

			});

			if (imList.size() > 0) {
				List<InvtyMthTermBgnTab> imU = new ArrayList<>();
				List<InvtyMthTermBgnTab> imI = new ArrayList<>();

				imList.forEach(im -> {
					if (im.getOrdrNum() == -1) {
						imI.add(im);
					} else {
						imU.add(im);
					}
				});
				if (imI.size() > 0) {
					invtyMthTermBgnTabDao.insertMthList(imI);
				}
				if (imU.size() > 0) {
					invtyMthTermBgnTabDao.updateMthList(imU);
				}
			}
			if (isdList.size() > 0) {
				List<InvtyMthTermBgnTab> imU = new ArrayList<>();
				List<InvtyMthTermBgnTab> imI = new ArrayList<>();
				isdList.forEach(im -> {
					if (im.getOrdrNum() == -1) {
						// insert
						imI.add(im);
					} else {
						// update
						imU.add(im);
					}
				});
				if (imI.size() > 0) {
					invtySendMthTermBgnTabDao.insertMthList(imI);
				}
				if (imU.size() > 0) {
					invtySendMthTermBgnTabDao.updateSendMthList(imU);
				}
			}
			if (idsList.size() > 0) {
				List<List<InvtyDetails>> list = Lists.partition(idsList, 100);
				list.forEach(li -> {
					invtyDetailDao.insertInvtyDetailsList(li);
				});

			}
			if (idList.size() > 0) {
				List<List<InvtyDetail>> list = Lists.partition(idList, 100);
				list.forEach(li -> {
					invtyDetailDao.insertInvtyDetailList(li);
				});
			}
		}
	}

	/**
	 * ��ʼ���ڳ�����ֹ���ֿ�ָ��
	 * 
	 * @param im
	 * @return
	 */
	private InvtyMthTermBgnTab initImValue(InvtyMthTermBgnTab im) {
		im.setSendQty(Optional.ofNullable(im.getSendQty()).orElse(BigDecimal.ZERO));
		im.setSendMoeny(Optional.ofNullable(im.getSendMoeny()).orElse(BigDecimal.ZERO));
		im.setInQty(Optional.ofNullable(im.getInQty()).orElse(BigDecimal.ZERO));
		im.setInMoeny(Optional.ofNullable(im.getInMoeny()).orElse(BigDecimal.ZERO));
		im.setQty(Optional.ofNullable(im.getQty()).orElse(BigDecimal.ZERO));
		im.setAmt(Optional.ofNullable(im.getAmt()).orElse(BigDecimal.ZERO));
		return im;
	}

	/**
	 * �洢�����ڳ�ֵ
	 */
	private InvtyMthTermBgnTab setMthValue(String acctYr, String acctiMth, String accNum, String logintTime,
			BigDecimal qty, BigDecimal unit, BigDecimal money, FormBookEntry form, FormBookEntrySub sub) {
		InvtyMthTermBgnTab mth = new InvtyMthTermBgnTab();
		try {
			BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
			ConvertUtils.register(bd, java.math.BigDecimal.class);
			BeanUtils.copyProperties(mth, sub);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		mth.setAccNum(accNum);
		mth.setCtime(sf.format(new Date()).toString());
		mth.setAccTime(logintTime);
		mth.setMakeVouchAbst("�ڳ����");
		mth.setAcctYr(acctYr);
		mth.setAcctiMth(acctiMth);
		mth.setQty(qty);
		mth.setUprc(unit);
		mth.setAmt(money);
		mth.setIsFinalDeal(0); // �Ƿ���ĩ����
		mth.setIsMthEndStl(0); // �Ƿ���ĩ����
		mth.setCustId(form.getCustId());
		return mth;
	}

	public void formBookBackEntryAss() {
		// ���ݻָ����˹���ҵ��

		// ���۳��ⵥ-���۵�sell_sngl
		// �ɹ���ⵥ-�ɹ���Ʊpurs_comn_inv
		// ���۷�Ʊ(ר��)sell_comn_inv
		// ���۷�Ʊ(��ͨ)sell_comn_inv
		// ��������out_into_whs_adj_sngl
		// ���������out_into_whs_adj_sngl
		// �˻���rtn_goods
		// ί�д���������entrs_agn_delv
		// ί�д����˻���entrs_agn_delv
	}

	/**
	 * ���ݼ���
	 */
	@Transactional
	public String formBookEntryAss2(List<FormBookEntry> dataList, String userName, String loginTime) throws Exception {

		Map<String, Object> parmMap = new HashMap<>();

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int count = 0;
		isSuccess = checkOutIsEndOfMonth(loginTime);
		Long beginTime = new Date().getTime(); // �ӿڿ�ʼʱ��
		// ���ݼ��˹���ҵ��
		if (dataList.size() != 0) {
			parmMap.put("formNumList", dataList);
			parmMap.put("isPage", "");
			parmMap.put("isNtBookOk", "0");
			dataList = formBookEntryDao.selectStreamALLList(parmMap);
			if (dataList.size() > 0) {
				count = formBookEntryByStored(dataList, userName, loginTime, count);
				// �ɱ�����
//				List<List<FormBookEntry>> list = Lists.partition(dataList, 100);

				// ��ʼ��Ҫִ�е������б�
//				List<Task> taskList = new ArrayList<>();
//				
//				for (int i = 0, l = list.size(); i < l; i++) {
//				count = costUprc(dataList, loginTime, userName, count);
//					for (FormBookEntry f : list.get(i)) {
//						count += f.getBookEntrySub().size();
//					}
//
//					taskList.add(new Task(i, list.get(i), userName, loginTime));
//				}
//				
//				// �趨Ҫ�����Ĺ����߳���Ϊ 4 ��
//				int threadCount = 4;
//				List[] taskListPerThread = TaskDistributor.distributeTasks(taskList, threadCount);
//
//				System.out.println("ʵ��Ҫ�����Ĺ����߳�����" + taskListPerThread.length);
//
//				for (int i = 0; i < taskListPerThread.length; i++) {
//					Thread workThread = new WorkThread(taskListPerThread[i], i);
//					workThread.start();
//				}

				Long opetime = new Date().getTime() - beginTime;
				long hour = opetime / (60 * 60 * 1000);
				long minute = (opetime - hour * 60 * 60 * 1000) / (60 * 1000);
				long second = (opetime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
				isSuccess = true;
				message = "���ݼ��˽ӿڵ�����ɣ����ι��ƣ�" + count + "�����ݣ���ʱ��" + hour + "ʱ" + minute + "�� " + second + "��";
			} else {
				message = "����������ݷ����쳣";
				isSuccess = false;
			}
		} else {
			message = "����������ݷ����쳣";
			isSuccess = false;

		}

		resp = BaseJson.returnRespObj("account/form/normal/book", isSuccess, message, null);
		return resp;

	}

	public int costUprc(List<FormBookEntry> dataList, String loginTime, String accNum, int count) throws Exception {

		List<InvtyDetails> itsList = new ArrayList<>();
		List<FormBookEntry> selList = new ArrayList<>();
		List<FormBookEntry> intList = new ArrayList<>();
		List<FormBookEntry> outList = new ArrayList<>();
		List<FormBookEntrySub> selsList = new ArrayList<>();
		List<FormBookEntrySub> subList = new ArrayList<>();
		for (FormBookEntry form : dataList) {
			int typId = Integer.valueOf(form.getOutIntoWhsTypId());
			for (FormBookEntrySub sub : form.getBookEntrySub()) {
				count++;
				sub = countPrice(typId, sub); // �ɱ�
				itsList = invtyDetail(form.getOutIntoWhsTypId(), sub, loginTime, accNum, form.getCustId(),
						form.getOutIntoWhsTypNm(), itsList);// ��ϸ��
				if (typId == 10) {
					selsList.add(sub);
				}
				subList.add(sub);
			}
			form.setIsNtBookOk(1);
			form.setBookOkAcc(accNum);
			form.setBookOkDt(loginTime);
			form.setIsNtMakeVouch(0);
			form.setIsMthEndStl(0);
			form.setIsNtUploadHead(0);
			if (typId == 9) {
				intList.add(form);
			} else if (typId == 10) {
				selList.add(form);
			} else {
				outList.add(form);
			}

		}
		if (selsList.size() > 0) {
			sellOutWhsDao.updateCost(selsList);
		}

		formBookEntryDao.insertFormList(dataList);
		formBookEntrySubDao.insertSubList(subList);

		if (selList.size() > 0) {
			formBookEntryDao.updateSellOutWhsBookEntryList(selList);
		}
		if (intList.size() > 0) {
			formBookEntryDao.updateIntoWhsBookEntryList(intList);
		}
		if (outList.size() > 0) {
			formBookEntryDao.updateOutInWhsBookEntry(outList);
		}
		if (itsList.size() > 0) {
			invtyDetailDao.insertInvtyDetailsList(itsList);
		}
		return count;
	}

	/**
	 * ����Ƿ��ڽ�������
	 */
	private boolean checkOutIsEndOfMonth(String loginTime) {

		boolean isIn = false;

		Map map = new HashMap<>();

		if (loginTime.length() > 7) {
			int year = Integer.valueOf(loginTime.substring(0, 4));
			int month = Integer.valueOf(loginTime.substring(5, 7));

			// �ж��ϸ����Ƿ��ѽ���
			if (month == 1) {
				map.put("year", year - 1);
				map.put("month", 12);
			} else {
				map.put("year", year);
				map.put("month", month - 1);
			}
			List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
			if (mthList.size() > 0) {
				MthEndStl mth = mthList.get(0);
				if (mth.getIsMthEndStl() == 1) {
					isIn = checkIsInNowMonth(map, year, month);
				}

			} else {
				isIn = checkIsInNowMonth(map, year, month);
			}

		}
		return isIn;
	}

	private boolean checkIsInNowMonth(Map map, int year, int month) {
		// �жϱ����Ƿ����
		map.put("year", year);
		map.put("month", month);
		map.put("isMthEndStl", "0");
		List<MthEndStl> mthList = mthEndStlDao.selectByMap(map);
		if (mthList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ϸ��
	 * 
	 * @param sub
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private List<InvtyDetails> invtyDetail(String typId, FormBookEntrySub sub, String loginTime, String accNum,
			String custId, String typNm, List<InvtyDetails> itsList)
			throws IllegalAccessException, InvocationTargetException {
		String year = loginTime.substring(0, 4);
		String month = loginTime.substring(5, 7);
		boolean isIt = false;
		boolean isIs = false;
		// ���Ҷ�Ӧ�Ĵ����ϸ
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());
		List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(paramMap);
		InvtyDetail invty = new InvtyDetail(); // ��ϸ
		if (itList.size() > 0) {
			invty = itList.get(0);
		} else {
			BeanUtils.copyProperties(invty, sub);
			invty.setDetailId(invtyDetailDao.insertInvtyDetail(invty));
		}

		paramMap.put("year", year);
		paramMap.put("month", month);
		List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(paramMap);
		InvtyMthTermBgnTab im = new InvtyMthTermBgnTab(); // ��������ڳ�
		if (tabList.size() > 0) {
			im = tabList.get(0);
		} else {

			isIt = true;
			im = setMthValue(year, month, accNum, loginTime, "", sub);
		}
		im = initImValue(im);
		InvtyDetails its = new InvtyDetails();
		if (CommonUtil.strToList("1,3,5,8,9,12").contains(typId)) {
			// ����
			its = new InvtyDetails(loginTime, null, null, String.valueOf(typId), typNm, sub.getNoTaxUprc(),
					sub.getQty(), sub.getNoTaxAmt(), null, null, null, BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, sub.getFormNum(), invty.getDetailId(), sub.getOrdrNum());

			im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
			im.setInQty(im.getInQty().add(sub.getQty()));
			if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setInUnitPrice(new BigDecimal(0.00000000));
			} else {

				im.setInUnitPrice(im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		}

		if (CommonUtil.strToList("2,4,6,7,10,11").contains(typId)) {
			// ����

			its = new InvtyDetails(loginTime, null, null, String.valueOf(typId), typNm, null, null, null,
					sub.getNoTaxUprc(), sub.getQty(), sub.getNoTaxAmt(), BigDecimal.ZERO, BigDecimal.ZERO,
					BigDecimal.ZERO, sub.getFormNum(), invty.getDetailId(), sub.getOrdrNum());
			im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
			im.setSendQty(im.getSendQty().add(sub.getQty()));
			if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {
				im.setSendUnitPrice(im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		}
		// ��浥�� = (�����ڳ����+����������-���µķ���)/(�����ڳ�����+������������-����)
		BigDecimal othQty = im.getQty().add(im.getInQty()).subtract(im.getSendQty());
		BigDecimal othMoeny = im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny());
		BigDecimal taxUprc = BigDecimal.ZERO;
		if (othQty.compareTo(BigDecimal.ZERO) == 0) {
			taxUprc = new BigDecimal(0.00000000);
		} else {
			taxUprc = othMoeny.divide(othQty, 8, BigDecimal.ROUND_HALF_UP).abs();
		}
		its.setOthMoeny(othMoeny);
		its.setOthQty(othQty);
		its.setOthUnitPrice(taxUprc);
		itsList.add(its);

		// ���½������
		im.setOthMoeny(othMoeny);
		im.setOthUnitPrice(taxUprc);
		im.setOthQty(othQty);

		// ��������ڳ� ������Ʒ�����ڳ�
		if (isIt) {
			// insert �����ڳ�
			invtyMthTermBgnTabDao.insertMth(im);
		} else {
			invtyMthTermBgnTabDao.updateMth(im);

		}
		if (Integer.valueOf(typId) == 10) {
			paramMap.put("custId", custId);
			List<InvtyMthTermBgnTab> sendList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(paramMap);
			InvtyMthTermBgnTab imSend = new InvtyMthTermBgnTab();
			if (sendList.size() > 0) {
				imSend = sendList.get(0);
			} else {
				isIs = true;
				imSend = setMthValue(year, month, accNum, loginTime, "", sub);
			}
			imSend = initImValue(imSend);
			othQty = new BigDecimal(0); // �������
			othMoeny = new BigDecimal(0.00); // �����
			taxUprc = new BigDecimal(0.00);// ��浥��

			// ������Ʒ���뷢������
			imSend = setSendMth(imSend, loginTime, custId, sub);

			// ���
			BigDecimal othSendQty = imSend.getQty().add(imSend.getInQty()).subtract(imSend.getSendQty());
			BigDecimal othSendMoeny = imSend.getAmt().add(imSend.getInMoeny()).subtract(imSend.getSendMoeny());
			BigDecimal uprc = new BigDecimal(0);
			if (othSendQty.compareTo(BigDecimal.ZERO) == 0) {
				uprc = new BigDecimal(0.00000000);
			} else {
				uprc = othSendMoeny.divide(othSendQty, 8, BigDecimal.ROUND_HALF_UP).abs();
			}
			imSend.setOthMoeny(othSendMoeny);
			imSend.setOthQty(othSendQty);
			imSend.setOthUnitPrice(uprc);
			if (isIs) {
				invtySendMthTermBgnTabDao.insertSendMth(imSend);

			} else {
				invtySendMthTermBgnTabDao.updateSendMth(imSend);

			}
		}

		return itsList;
	}

	/**
	 * ������Ʒ�����ڳ�
	 */
	private InvtyMthTermBgnTab setSendMth(InvtyMthTermBgnTab imSend, String loginTime, String custId,
			FormBookEntrySub sub) {
		imSend.setSendQty(Optional.ofNullable(imSend.getSendQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setSendMoeny(Optional.ofNullable(imSend.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
		imSend.setInQty(Optional.ofNullable(imSend.getInQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setInMoeny(Optional.ofNullable(imSend.getInMoeny()).orElse(new BigDecimal(0.00000000)));
		Map map = new HashMap<>();
		map.put("invtyEncd", sub.getInvtyEncd());
		map.put("whsEncd", sub.getWhsEncd());
		map.put("batNum", sub.getBatNum());
		map.put("outWhsId", sub.getFormNum());
		// map.put("custId", form.getCustId());
		map.put("bookDt", loginTime);
		map.put("toOrdrNum", sub.getToOrdrNum());

		List<SellComnInvSub> sList = sellComnInvDao.selectSellComnInvToQty(map);
		BigDecimal openQty = BigDecimal.ZERO;
		if (sList.size() > 0) {
			openQty = sList.get(0).getQty();
		}
		if (openQty != null) {
			if (openQty.compareTo(sub.getQty()) == 0) {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxAmt()));
				imSend.setSendQty(imSend.getSendQty().add(sub.getQty()));
			} else {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxUprc().multiply(openQty)));
				imSend.setSendQty(imSend.getSendQty().add(openQty));
			}

			if (imSend.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				imSend.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {

				imSend.setSendUnitPrice(
						imSend.getSendMoeny().divide(imSend.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		}

		// ����
		imSend.setInMoeny(imSend.getInMoeny().add(sub.getNoTaxAmt()));
		imSend.setInQty(imSend.getInQty().add(sub.getQty()));
		if (imSend.getInQty().compareTo(BigDecimal.ZERO) == 0) {
			imSend.setInUnitPrice(new BigDecimal(0.00000000));
		} else {
			imSend.setInUnitPrice(imSend.getInMoeny().divide(imSend.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
		}

		imSend.setCustId(custId);

		return imSend;
	}

	/**
	 * �洢�����ڳ�ֵ
	 */
	private InvtyMthTermBgnTab setMthValue(String acctYr, String acctiMth, String accNum, String logintTime,
			String custId, FormBookEntrySub sub) {
		InvtyMthTermBgnTab mth = new InvtyMthTermBgnTab();
		try {
			BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
			ConvertUtils.register(bd, java.math.BigDecimal.class);
			BeanUtils.copyProperties(mth, sub);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		mth.setAccNum(accNum);
		mth.setCtime(sf.format(new Date()).toString());
		mth.setAccTime(logintTime);
		mth.setMakeVouchAbst("�ڳ����");
		mth.setAcctYr(acctYr);
		mth.setAcctiMth(acctiMth);
		mth.setQty(BigDecimal.ZERO);
		mth.setUprc(BigDecimal.ZERO);
		mth.setAmt(BigDecimal.ZERO);
		mth.setIsFinalDeal(0); // �Ƿ���ĩ����
		mth.setIsMthEndStl(0); // �Ƿ���ĩ����
		mth.setCustId(custId);
		mth.setOrdrNum(-1);
		return mth;
	}

	/**
	 * ����ɱ�
	 */
	private FormBookEntrySub countPrice(int typId, FormBookEntrySub sub) {

		// ����ⵥ�ɱ�

		BigDecimal noTaxAmt = new BigDecimal(0.00); // �ܽ��
		BigDecimal qty = sub.getQty(); // ����

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("formNum", sub.getFormNum());
		dataMap.put("invtyEncd", sub.getInvtyEncd());
		dataMap.put("batNum", sub.getBatNum());
		dataMap.put("whsEncd", sub.getWhsEncd());

		// �ɹ���ⵥ
		// �ɹ���ⵥ ƽ������ = (�ɹ���Ʊ���/�ݹ����)/������� Ӧ���÷�Ʊ����
		if (typId == 9) {
			qty = sub.getQty();
			noTaxAmt = sub.getNoTaxAmt();
			// �ɹ���ⵥ ƽ������ = (�ɹ���Ʊ���/�ݹ����)/������� Ӧ���÷�Ʊ����
			PursInvSubTab purs = pursInvMasTabDao.countPursNoTaxAmtAndQty(dataMap); // ���ݴ��/�ֿ�/���� sum�ܽ��

			if (purs != null) {
				if (sub.getQty().compareTo(purs.getQty()) != 0) {
					qty = purs.getQty();
					noTaxAmt = purs.getNoTaxAmt();
				}
			}

			if (qty.compareTo(BigDecimal.ZERO) != 0) {

				sub.setNoTaxUprc(noTaxAmt.divide(qty, 8, BigDecimal.ROUND_HALF_UP).abs()); // ���㵥��
				sub.setNoTaxAmt(sub.getQty().multiply(sub.getNoTaxUprc()));
			}

		} else if (typId != 1 && typId != 2) {

			qty = sub.getQty(); // �������
			noTaxAmt = sub.getNoTaxAmt(); // �����
			if (!CommonUtil.strToList("9,10").contains(typId)) {
				if (noTaxAmt.compareTo(BigDecimal.ZERO) != 0) {
					// ���۳��ⵥand������ ƽ������ = (������½����(�����ڳ�)+���������(���²ɹ���Ʊ/�ݹ���ⵥ���)+�ɹ����) /��������+�������
					PursInvSubTab purs = pursInvMasTabDao.countSellAndOthAmt(dataMap);

					if (purs != null) {
						purs.setQty(Optional.ofNullable(purs.getQty()).orElse(BigDecimal.ZERO));
						purs.setNoTaxAmt(Optional.ofNullable(purs.getNoTaxAmt()).orElse(BigDecimal.ZERO));
						noTaxAmt = purs.getNoTaxAmt();
						qty = purs.getQty();

					}
				}
			} else {
				// ���۳��ⵥand������ ƽ������ = (������½����(�����ڳ�)+���������(���²ɹ���Ʊ/�ݹ���ⵥ���)+�ɹ����) /��������+�������
				PursInvSubTab purs = pursInvMasTabDao.countSellAndOthAmt(dataMap);

				if (purs != null) {
					purs.setQty(Optional.ofNullable(purs.getQty()).orElse(BigDecimal.ZERO));
					purs.setNoTaxAmt(Optional.ofNullable(purs.getNoTaxAmt()).orElse(BigDecimal.ZERO));

					noTaxAmt = purs.getNoTaxAmt();
					qty = purs.getQty();

				}
			}

			if (noTaxAmt.compareTo(BigDecimal.ZERO) != 0 && qty.compareTo(BigDecimal.ZERO) != 0) {
				sub.setNoTaxUprc(noTaxAmt.divide(qty, 8, BigDecimal.ROUND_HALF_UP).abs());
				sub.setNoTaxAmt(sub.getQty().multiply(sub.getNoTaxUprc()));

			}

		}

		return sub;
	}

	/**
	 * ������Ʒ���� 2019-12-24
	 * 
	 * @throws InvocationTargetException
	 * @throws Exception
	 */

	public int formBookEntryByStored(List<FormBookEntry> dataList, String userName, String loginTime, int count)
			throws Exception {

		Map<String, Object> parmMap = new HashMap<>();

		parmMap.put("bookDt", loginTime);
		parmMap.put("userName", userName);
		parmMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
		parmMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
		if (dataList.size() > 0) {
			List<List<FormBookEntry>> list = Lists.partition(dataList, 1000);
			List<InvtyDetails> itsList = new ArrayList<>();
			List<FormBookEntry> selList = new ArrayList<>();
			List<FormBookEntry> intList = new ArrayList<>();
			List<FormBookEntry> outList = new ArrayList<>();
			List<FormBookEntrySub> selsList = new ArrayList<>();
			List<FormBookEntrySub> subList = new ArrayList<>();

			InvtyDetails its = new InvtyDetails();
			Map<String, InvtyMthTermBgnTab> mthMap = new HashMap<>();
			Map<String, InvtyMthTermBgnTab> sendMap = new HashMap<>();
			Map<String, SellComnInvSub> sellComMap = new HashMap<>();
			for (int i = 0, l = list.size(); i < l; i++) {
				itsList = new ArrayList<>();
				selList = new ArrayList<>();
				intList = new ArrayList<>();
				outList = new ArrayList<>();
				selsList = new ArrayList<>();
				subList = new ArrayList<>();
				sellComMap = new HashMap<>();
				sendMap = new HashMap<>();
				mthMap = new HashMap<>();

				// �޸�ԭ������Ϊ����
				for (FormBookEntry fr : list.get(i)) {
					fr.setIsNtBookOk(1);
					fr.setBookOkAcc(userName);
					fr.setBookOkDt(loginTime);
					fr.setIsNtMakeVouch(0);
					fr.setIsMthEndStl(0);
					fr.setIsNtUploadHead(0);
					if (Integer.valueOf(fr.getOutIntoWhsTypId()) == 9) {
						intList.add(fr);
					} else if (Integer.valueOf(fr.getOutIntoWhsTypId()) == 10) {
						selList.add(fr);
					} else {
						outList.add(fr);
					}

					for (FormBookEntrySub s : fr.getBookEntrySub()) {
						count++;
						parmMap.put("formNum", s.getFormNum());
						parmMap.put("invtyEncd", s.getInvtyEncd());
						parmMap.put("whsEncd", s.getWhsEncd());
						parmMap.put("batNum", s.getBatNum());
						parmMap.put("loginTime", loginTime);
						s.setQty(Optional.ofNullable(s.getQty()).orElse(BigDecimal.ZERO));
						s.setNoTaxAmt(Optional.ofNullable(s.getNoTaxAmt()).orElse(BigDecimal.ZERO));
						List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(parmMap);

						InvtyDetail invty = new InvtyDetail();

						if (itList.size() > 0) {
							invty = itList.get(0);
						} else {
							BeanUtils.copyProperties(invty, s);
							invty.setDetailId(invtyDetailDao.insertInvtyDetail(invty));
						}

						if (Integer.valueOf(fr.getOutIntoWhsTypId()) == 9) {

							BigDecimal uprc = formBookEntryDao.updateInUprc(parmMap);
							if (uprc != null && uprc.compareTo(BigDecimal.ZERO) != 0) {
								s.setNoTaxUprc(uprc);
								s.setNoTaxAmt(uprc.multiply(s.getQty()));
							}

						} else {

							BigDecimal uprc = formBookEntryDao.updateOutUprc(parmMap);
							if (uprc == null || uprc.compareTo(BigDecimal.ZERO) == 0) {
								uprc = s.getNoTaxUprc();
							}

							if (Integer.valueOf(fr.getOutIntoWhsTypId()) != 1
									|| Integer.valueOf(fr.getOutIntoWhsTypId()) != 2) {
								if (s.getQty().compareTo(BigDecimal.ZERO) != 0
										&& s.getNoTaxAmt().compareTo(BigDecimal.ZERO) != 0) {
									s.setNoTaxUprc(uprc);
									s.setNoTaxAmt(uprc.multiply(s.getQty()));
								}

							} else {
								s.setNoTaxUprc(uprc);
								s.setNoTaxAmt(uprc.multiply(s.getQty()));

							}
						}
						s.setTyepId(fr.getOutIntoWhsTypId());
						if (Integer.valueOf(fr.getOutIntoWhsTypId()) == 10) {

							CustDoc cd = custDocDao.selectCustTotalByCustId(fr.getCustId());
							if (cd != null) {
								s.setSubCreditType(cd.getCustTotlCorpId()); // ��ʱ�ͻ�
							} else {
								s.setSubCreditType(fr.getCustId()); // ��ʱ�ͻ�
							}
							// ��ѯ�ϼ��ͻ�
							selsList.add(s);

						}

						// �շ�
						if (CommonUtil.strToList("1,3,5,8,9,12").contains(fr.getOutIntoWhsTypId())) {
							// ����
							its = new InvtyDetails(loginTime, null, null, fr.getOutIntoWhsTypId(),
									fr.getOutIntoWhsTypNm(), s.getNoTaxUprc(), s.getQty(), s.getNoTaxAmt(), null, null,
									null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, fr.getFormNum(),
									invty.getDetailId(), s.getOrdrNum());

						}
						if (CommonUtil.strToList("2,4,6,7,10,11").contains(fr.getOutIntoWhsTypId())) {
							// ����
							its = new InvtyDetails(loginTime, null, null, fr.getOutIntoWhsTypId(),
									fr.getOutIntoWhsTypNm(), null, null, null, s.getNoTaxUprc(), s.getQty(),
									s.getNoTaxAmt(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, fr.getFormNum(),
									invty.getDetailId(), s.getOrdrNum());
							itsList.add(its);
						}

						subList.add(s);
						itsList.add(its);

					}

				}
				parmMap.clear();
				parmMap.put("invtyList", subList);
				parmMap.put("loginTime", loginTime);
				parmMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
				parmMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
				parmMap.put("bookDt", loginTime);
				if (selsList.size() > 0) {
					sellComMap = getSellSubMap(sellComMap, parmMap, loginTime); // ������Ϣ
					insertInvtySendMth(sellComMap, sendMap, parmMap, subList, loginTime, userName);// ������Ʒ��Ϣ
				}
				insertInvtyMth(mthMap, parmMap, subList, loginTime, userName);// �շ�����Ϣ

				if (selsList.size() > 0) {
					// ������Ʒ��ϸ
					sellOutWhsDao.updateCost(selsList);
				}

				formBookEntryDao.insertFormList(dataList);
				formBookEntrySubDao.insertSubList(subList);

				if (selList.size() > 0) {
					formBookEntryDao.updateSellOutWhsBookEntryList(selList);
				}
				if (intList.size() > 0) {
					formBookEntryDao.updateIntoWhsBookEntryList(intList);
				}
				if (outList.size() > 0) {
					formBookEntryDao.updateOutInWhsBookEntry(outList);
				}
				if (itsList.size() > 0) {
					invtyDetailDao.insertInvtyDetailsList(itsList);
				}

			}

		}

		return count;

	}

	/**
	 * insert+get ������ϸ��
	 */
	private void insertInvtySendMth(Map<String, SellComnInvSub> sellComMap, Map<String, InvtyMthTermBgnTab> isdMap,
			Map<String, Object> parmMap, List<FormBookEntrySub> sList, String loginTime, String accNum) {
		parmMap.put("year", Integer.valueOf(loginTime.substring(0, 4)));
		parmMap.put("month", Integer.valueOf(loginTime.substring(5, 7)));
		// ������Ʒ-�ڳ�
		List<InvtyMthTermBgnTab> sendTabList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(parmMap);

		parmMap.put("bookDt", loginTime);
		if (sendTabList.size() > 0) {

			sendTabList.forEach(it -> {
				String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getCustId();
				if (!isdMap.containsKey(key)) {
					it = initImValue(it);

					isdMap.put(key, it);
				}
			});
		}
		for (FormBookEntrySub sub : sList) {
			// ����
			if (Integer.valueOf(sub.getTyepId()) == 10) {
				String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum() + sub.getSubCreditType();

				InvtyMthTermBgnTab ims = new InvtyMthTermBgnTab();
				if (!isdMap.containsKey(key)) {

					ims = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum, loginTime,
							sub.getSubCreditType(), sub);

				} else {

					ims = isdMap.get(key);
				}
				ims = initImValue(ims);
				ims.setInMoeny(ims.getInMoeny().add(sub.getNoTaxAmt()));
				ims.setInQty(ims.getInQty().add(sub.getQty()));
				if (ims.getInQty().compareTo(BigDecimal.ZERO) == 0) {
					ims.setInUnitPrice(new BigDecimal(0.00000000));
				} else {
					ims.setInUnitPrice(ims.getInMoeny().divide(ims.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}
				key = sub.getFormNum() + sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum()
						+ sub.getSubCreditType();
				if (sellComMap.containsKey(key)) {

					SellComnInvSub sl = sellComMap.get(key);
					sl.setQty(Optional.ofNullable(sl.getQty()).orElse(BigDecimal.ZERO));
					sl.setNoTaxAmt(Optional.ofNullable(sl.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					ims.setSendMoeny(ims.getSendMoeny().add(sl.getNoTaxAmt()));
					ims.setSendQty(ims.getSendQty().add(sl.getQty()));
					if (ims.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
						ims.setSendUnitPrice(new BigDecimal(0.00000000));
					} else {
						ims.setSendUnitPrice(
								ims.getSendMoeny().divide(ims.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
					}
					ims.setOthQty(ims.getQty().add(ims.getInQty()).subtract(ims.getSendQty()));
					ims.setOthMoeny(ims.getAmt().add(ims.getInMoeny()).subtract(ims.getSendMoeny()));
					if (ims.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
						ims.setOthUnitPrice(new BigDecimal(0.00000000));
					} else {
						ims.setOthUnitPrice(
								ims.getOthMoeny().divide(ims.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
					}
				}

				ims.setOthQty(ims.getQty().add(ims.getInQty()).subtract(ims.getSendQty()));
				ims.setOthMoeny(ims.getAmt().add(ims.getInMoeny()).subtract(ims.getSendMoeny()));
				if (ims.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
					ims.setOthUnitPrice(new BigDecimal(0.00000000));
				} else {
					ims.setOthUnitPrice(ims.getOthMoeny().divide(ims.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}

				isdMap.put(key, ims);
			}

		}

		List<InvtyMthTermBgnTab> iList = new ArrayList<>();
		List<InvtyMthTermBgnTab> uList = new ArrayList<>();
		if (!isdMap.isEmpty()) {
			for (Map.Entry<String, InvtyMthTermBgnTab> key : isdMap.entrySet()) {
				InvtyMthTermBgnTab im = (InvtyMthTermBgnTab) key.getValue();
				if (im.getOrdrNum() == -1) {
					iList.add(im);
				} else {
					uList.add(im);
				}
			}

		}
		if (iList.size() > 0) {
			invtySendMthTermBgnTabDao.insertMthList(iList);
		}
		if (uList.size() > 0) {
			invtySendMthTermBgnTabDao.updateSendMthList(uList);
		}

	}

	/**
	 * get������Ʒ-����
	 */
	private Map<String, SellComnInvSub> getSellSubMap(Map<String, SellComnInvSub> selMap, Map<String, Object> parmMap,
			String loginTime) {
		parmMap.put("bookDt", loginTime);
		List<SellComnInvSub> sellSubList = sellComnInvDao.selectSellComnInvToQty(parmMap);
		if (sellSubList.size() > 0) {
			sellSubList.forEach(it -> {
				// projEncd = custId
				String key = it.getOutWhsId() + it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getProjEncd();

				if (!selMap.containsKey(key)) {
					selMap.put(key, it);
				}
			});

		}
		return selMap;
	}

	/**
	 * insert+get �շ���
	 */
	private Map<String, InvtyMthTermBgnTab> insertInvtyMth(Map<String, InvtyMthTermBgnTab> imtMap,
			Map<String, Object> parmMap, List<FormBookEntrySub> sList, String loginTime, String accNum) {
		// ����ڳ�
		List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(parmMap);

		if (tabList.size() > 0) {

			tabList.forEach(it -> {
				String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum();
				if (!imtMap.containsKey(key)) {
					it = initImValue(it);
					imtMap.put(key, it);
				}
			});
		}
		for (FormBookEntrySub sub : sList) {
			String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum();
			InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();
			if (!imtMap.containsKey(key)) {
				im = setMthValue(loginTime.substring(0, 4), loginTime.substring(5, 7), accNum, loginTime, "", sub);
				im = initImValue(im);
			} else {
				im = imtMap.get(key);
			}

			// ����
			if (CommonUtil.strToList("1,3,5,8,9,12").contains(sub.getTyepId())) {
				im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
				im.setInQty(im.getInQty().add(sub.getQty()));
				if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
					im.setInUnitPrice(new BigDecimal(0.00000000));
				} else {
					im.setInUnitPrice(im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}
			}
			// ����
			if (CommonUtil.strToList("2,4,6,7,10,11").contains(sub.getTyepId())) {
				im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
				im.setSendQty(im.getSendQty().add(sub.getQty()));
				if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
					im.setSendUnitPrice(new BigDecimal(0.00000000));
				} else {
					im.setSendUnitPrice(im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
				}
			}

			im.setOthQty(im.getQty().add(im.getInQty()).subtract(im.getSendQty()));
			im.setOthMoeny(im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny()));
			if (im.getOthQty().compareTo(BigDecimal.ZERO) == 0) {
				im.setOthUnitPrice(new BigDecimal(0.00000000));
			} else {
				im.setOthUnitPrice(im.getOthMoeny().divide(im.getOthQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
			imtMap.put(key, im);

		}

		List<InvtyMthTermBgnTab> iList = new ArrayList<>();
		List<InvtyMthTermBgnTab> uList = new ArrayList<>();
		if (!imtMap.isEmpty()) {
			for (Map.Entry<String, InvtyMthTermBgnTab> key : imtMap.entrySet()) {
				InvtyMthTermBgnTab im = (InvtyMthTermBgnTab) key.getValue();
				if (im.getOrdrNum() == -1) {
					iList.add(im);
				} else {
					uList.add(im);
				}
			}

		}

		if (iList.size() > 0) {
			invtyMthTermBgnTabDao.insertMthList(iList);
		}
		if (uList.size() > 0) {
			invtyMthTermBgnTabDao.updateMthList(uList);
		}

		return imtMap;
	}

	/**
	 * get�������ⵥ��NoTaxUprc
	 * 
	 * @param outMap
	 * @param parmMap
	 * @return
	 */
	private Map<String, PursInvSubTab> getUprcInSell(Map<String, PursInvSubTab> outMap, Map<String, Object> parmMap) {
		// ����ɱ�
		List<PursInvSubTab> othInvList = pursInvMasTabDao.countSellAndOthAmtList(parmMap);
		if (othInvList.size() > 0) {
			othInvList.forEach(pi -> {
				String key = pi.getWhsEncd() + pi.getInvtyEncd() + pi.getBatNum();
				if (!outMap.containsKey(key)) {
					outMap.put(key, pi);
				}
			});
		}
		// ���ɱ�
		List<PursInvSubTab> pursInvList = pursInvMasTabDao.countPursNoTaxAmtAndQtyList(parmMap);

		if (pursInvList.size() > 0) {

			pursInvList.forEach(pi -> {
				String key = pi.getWhsEncd() + pi.getInvtyEncd() + pi.getBatNum();
				if (!outMap.containsKey(key)) {
					outMap.put(key, pi);
				} else {
					PursInvSubTab tab = outMap.get(key);
					tab.setNoTaxUprc(pi.getNoTaxUprc());
					outMap.put(key, tab);
				}
			});
		}
		return outMap;
	}

	/**
	 * insert�����Ϣ
	 * 
	 * @param parmMap
	 * @param sList
	 */
	private void insertInvtyDetail(Map<String, Object> parmMap, List<FormBookEntrySub> sList) {
		List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(parmMap);
		Map<String, InvtyDetail> ivMap = new HashMap<>();
		if (itList.size() > 0) {
			List<InvtyDetail> iList = new ArrayList<>();
			itList.forEach(it -> {
				String key = it.getWhsEncd() + it.getInvtyEncd() + it.getBatNum() + it.getInvtyClsEncd();
				if (!ivMap.containsKey(key)) {
					ivMap.put(key, it);
				}
			});
			for (FormBookEntrySub sub : sList) {
				String key = sub.getWhsEncd() + sub.getInvtyEncd() + sub.getBatNum() + sub.getInvtyClsEncd();
				if (!ivMap.containsKey(key)) {

					InvtyDetail invty = new InvtyDetail(sub.getInvtyNm(), sub.getSpcModel(), sub.getMeasrCorpNm(),
							sub.getInvtyEncd(), sub.getInvtyCd(), sub.getBatNum(), sub.getWhsEncd(),
							sub.getInvtyClsEncd(), sub.getInvtyClsNm(), sub.getWhsNm());
					iList.add(invty);
				}
			}
			if (iList.size() > 0) {
				invtyDetailDao.insertInvtyDetailList(iList);
			}

		}

	}

	public int formBookEntryByOld(List<FormBookEntry> formList, String userName, String loginTime, int count)
			throws Exception {
		// 2.����ɱ�
		formList = countAvgUprcOld(formList, loginTime, userName);

		// 3.���Ӵ����ϸ��
		Boolean isAddI = addInvtyDetailBook1(formList, loginTime, userName);
		Boolean isDeal = dealSendProductMth(formList, loginTime, userName);

		List<FormBookEntry> selList = new ArrayList<>();
		List<FormBookEntry> intList = new ArrayList<>();
		List<FormBookEntry> outList = new ArrayList<>();
		List<FormBookEntrySub> subList = new ArrayList<>();
		List<FormBookEntrySub> selsList = new ArrayList<>();
		// subList
		for (FormBookEntry lis : formList) {

			if (Integer.valueOf(lis.getOutIntoWhsTypId()) == 9) {
				intList.add(lis);
			} else if (Integer.valueOf(lis.getOutIntoWhsTypId()) == 10) {
				selList.add(lis);
				selsList.addAll(lis.getBookEntrySub());
			} else {
				outList.add(lis);
			}
			subList.addAll(lis.getBookEntrySub());
		}

		if (formList.size() > 0) {
			formBookEntryDao.insertFormList(formList);
		}
		if (subList.size() > 0) {
			count = count + subList.size();
			formBookEntrySubDao.insertSubList(subList);
		}

		if (selList.size() > 0) {

			formBookEntryDao.updateSellOutWhsBookEntryList(selList);
			sellOutWhsDao.updateCost(selsList);

		}
		if (intList.size() > 0) {
			formBookEntryDao.updateIntoWhsBookEntryList(intList);
		}
		if (outList.size() > 0) {
			formBookEntryDao.updateOutInWhsBookEntry(outList);
		}

		return count;
	}

	/**
	 * avgƽ���ɱ�
	 */
	private List<FormBookEntry> countAvgUprcOld(List<FormBookEntry> formList, String loginTime, String userName) {

		Map<String, Object> dataMap = new HashMap<>();
		List<FormBookEntry> dataList = new ArrayList<>();

		BigDecimal inQty = new BigDecimal(0); // �������
		BigDecimal inUprc = new BigDecimal(0); // �����

		int year = Integer.valueOf(loginTime.substring(0, 4));
		int month = Integer.valueOf(loginTime.substring(5, 7));
		dataMap.put("year", year);
		dataMap.put("month", month);
		dataMap.put("bookDt", loginTime);
		dataMap.put("loginTime", loginTime);
		if (formList.size() > 0) {
			for (FormBookEntry form : formList) {
				form.setIsNtBookOk(1);
				form.setBookOkAcc(userName);
				form.setBookOkDt(loginTime);
				form.setIsNtMakeVouch(0);
				form.setIsMthEndStl(0);
				form.setIsNtUploadHead(0);

				for (FormBookEntrySub forms : form.getBookEntrySub()) {
					dataMap.put("invtyEncd", forms.getInvtyEncd());
					dataMap.put("batNum", forms.getBatNum());
					dataMap.put("whsEncd", forms.getWhsEncd());
					dataMap.put("formNum", forms.getFormNum());
					forms.setNoTaxUprc(Optional.ofNullable(forms.getNoTaxUprc()).orElse(BigDecimal.ZERO));
					forms.setNoTaxAmt(Optional.ofNullable(forms.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					forms.setQty(Optional.ofNullable(forms.getQty()).orElse(BigDecimal.ZERO));
					inQty = forms.getQty(); // �������
					inUprc = forms.getNoTaxUprc(); // ��ⵥ��
					if (Integer.valueOf(form.getOutIntoWhsTypId()) == 9) {
						// �ɹ���ⵥ ƽ������ = (�ɹ���Ʊ���/�ݹ����)/������� Ӧ���÷�Ʊ����
						BigDecimal uprc = formBookEntryDao.updateInUprc(dataMap);// �ɹ�
						if (uprc != null && uprc.compareTo(BigDecimal.ZERO) != 0) {

							inUprc = uprc;

						}

					} else if (Integer.valueOf(form.getOutIntoWhsTypId()) != 1
							&& Integer.valueOf(form.getOutIntoWhsTypId()) != 2) {
						if (!CommonUtil.strToList("9,10").contains(form.getOutIntoWhsTypId())) {
							if (inUprc.compareTo(BigDecimal.ZERO) != 0) {
								// ���۳��ⵥand������ ƽ������ = (������½����(�����ڳ�)+���������(���²ɹ���Ʊ/�ݹ���ⵥ���)+�ɹ����) /��������+�������
								BigDecimal uprc = formBookEntryDao.updateOutUprc(dataMap); // ����
								if (uprc != null && uprc.compareTo(BigDecimal.ZERO) != 0) {

									inUprc = uprc;

								}
							}
						} else {
							BigDecimal uprc = formBookEntryDao.updateOutUprc(dataMap); // ����
							if (uprc != null && uprc.compareTo(BigDecimal.ZERO) != 0) {

								inUprc = uprc;

							}
						}

					}

					if (inUprc.compareTo(BigDecimal.ZERO) != 0 && inQty.compareTo(BigDecimal.ZERO) != 0) {
						forms.setNoTaxAmt(inUprc.multiply(forms.getQty()));
						forms.setNoTaxUprc(inUprc);
						forms.setNoTaxAmt(forms.getQty().multiply(forms.getNoTaxUprc()));

					}

				}

				dataList.add(form);
			}
		}

		return formList;
	}

	@Transactional
	public Boolean addInvtyDetailBook1(List<FormBookEntry> formList, String loginTime, String accNum) throws Exception {

		Boolean isSuccess = true;
		String year = loginTime.substring(0, 4);
		String month = loginTime.substring(loginTime.indexOf("-") + 1, loginTime.lastIndexOf("-"));

		if (formList.size() != 0) {

			for (FormBookEntry form : formList) {

				if (form.getIsNtBookOk() == 1) {
					List<InvtyDetails> itsList = new ArrayList<>();

					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						// ���Ҷ�Ӧ�Ĵ����ϸ
						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("invtyEncd", sub.getInvtyEncd());
						paramMap.put("whsEncd", sub.getWhsEncd());
						paramMap.put("batNum", sub.getBatNum());
						paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());

						List<InvtyDetail> itList = invtyDetailDao.selectByInvtyDeatil(paramMap);

						InvtyDetail invty = new InvtyDetail();
						// ��������ڳ�
						InvtyMthTermBgnTab im = new InvtyMthTermBgnTab();

						Boolean isIt = true;
						Boolean isIs = true;

						if (itList.size() > 0) {
							invty = itList.get(0);
						} else {

							BeanUtils.copyProperties(invty, sub);

							Map<String, Object> map = new HashMap<>();
							map.put("clsEncd", sub.getInvtyClsEncd()); // ����������
							map.put("sellTypId", form.getSellTypId()); // ��������
							// List<AcctItmDoc> stockList = accItemDao.selectStockByClsEncd(map);

							// if (stockList.size() > 0) {
							// invty.setSubId(stockList.get(0).getSubjId());
							// invty.setSubNm(stockList.get(0).getSubjNm());
							// }

							int i = invtyDetailDao.insertInvtyDetail(invty);

						}

						InvtyDetails its = new InvtyDetails();

						// ��ѯ���º����ڳ�����
						List<InvtyMthTermBgnTab> termList = getMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month));

						if (termList.size() > 0) {

							isIt = true;
							im = termList.get(0);
						} else {

							isIt = false;
							im = setMthValue(year, month, accNum, loginTime, BigDecimal.ZERO, BigDecimal.ZERO,
									BigDecimal.ZERO, form, sub);

						}

						BigDecimal othQty = new BigDecimal(0); // �������
						BigDecimal othMoeny = new BigDecimal(0.00); // �����
						BigDecimal taxUprc = new BigDecimal(0.00);// ��浥��

						im.setSendQty(Optional.ofNullable(im.getSendQty()).orElse(BigDecimal.ZERO));
						im.setSendMoeny(Optional.ofNullable(im.getSendMoeny()).orElse(BigDecimal.ZERO));
						im.setInQty(Optional.ofNullable(im.getInQty()).orElse(BigDecimal.ZERO));
						im.setInMoeny(Optional.ofNullable(im.getInMoeny()).orElse(BigDecimal.ZERO));

						if (CommonUtil.strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
							// ����
							its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
									form.getOutIntoWhsTypNm(), sub.getNoTaxUprc(), sub.getQty(), sub.getNoTaxAmt(),
									null, null, null, taxUprc, othQty, othMoeny, form.getFormNum(), invty.getDetailId(),
									sub.getOrdrNum());

							im.setInMoeny(im.getInMoeny().add(sub.getNoTaxAmt()));
							im.setInQty(im.getInQty().add(sub.getQty()));
							if (im.getInQty().compareTo(BigDecimal.ZERO) == 0) {
								im.setInUnitPrice(new BigDecimal(0.00000000));
							} else {

								im.setInUnitPrice(
										im.getInMoeny().divide(im.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}
						}

						if (CommonUtil.strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
							// ����

							its = new InvtyDetails(loginTime, null, null, form.getOutIntoWhsTypId(),
									form.getOutIntoWhsTypNm(), null, null, null, sub.getNoTaxUprc(), sub.getQty(),
									sub.getNoTaxAmt(), taxUprc, othQty, othMoeny, form.getFormNum(),
									invty.getDetailId(), sub.getOrdrNum());
							im.setSendMoeny(im.getSendMoeny().add(sub.getNoTaxAmt()));
							im.setSendQty(im.getSendQty().add(sub.getQty()));
							if (im.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
								im.setSendUnitPrice(new BigDecimal(0.00000000));
							} else {
								im.setSendUnitPrice(
										im.getSendMoeny().divide(im.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
							}
						}

						// ��浥�� = (�����ڳ����+����������-���µķ���)/(�����ڳ�����+������������-����)
						othQty = im.getQty().add(im.getInQty()).subtract(im.getSendQty());
						othMoeny = im.getAmt().add(im.getInMoeny()).subtract(im.getSendMoeny());
						if (othQty.compareTo(BigDecimal.ZERO) == 0) {
							taxUprc = new BigDecimal(0.00000000);
						} else {
							taxUprc = othMoeny.divide(othQty, 8, BigDecimal.ROUND_HALF_UP).abs();
						}
						its.setOthMoeny(othMoeny);
						its.setOthQty(othQty);
						its.setOthUnitPrice(taxUprc);
						itsList.add(its);

						// ���½������
						im.setOthMoeny(othMoeny);
						im.setOthUnitPrice(taxUprc);
						im.setOthQty(othQty);

						// ��������ڳ� ������Ʒ�����ڳ�
						if (!isIt) {
							// insert �����ڳ�
							invtyMthTermBgnTabDao.insertMth(im);
						} else {
							invtyMthTermBgnTabDao.updateMth(im);

						}

					}
					// insert ��ϸ
					if (itsList.size() != 0) {

						invtyDetailDao.insertInvtyDetailsList(itsList);
						isSuccess = true;
					}
				} else {
					isSuccess = false;
					logger.info("���ݺ�:{}δ����,�޷�������ϸ�˴���", form.getFormNum());
				}
			}
		}
		return isSuccess;
	}

	/**
	 * ��ѯ�����ڳ�
	 */
	private List<InvtyMthTermBgnTab> getMthList(FormBookEntrySub sub, int year, int month) {

		List<InvtyMthTermBgnTab> tabList = invtyMthTermBgnTabDao.selectByMthTerm(putParamMap(sub, year, month));

		return tabList;
	}

	/**
	 * ��װmapֵ
	 */
	private Map<String, Object> putParamMap(FormBookEntrySub sub, int year, int month) {

		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("invtyClsEncd", sub.getInvtyClsEncd());
		paramMap.put("invtyEncd", sub.getInvtyEncd());
		paramMap.put("whsEncd", sub.getWhsEncd());
		paramMap.put("batNum", sub.getBatNum());
		paramMap.put("year", year);
		paramMap.put("month", month);
		return paramMap;
	}

	public boolean dealSendProductMth(List<FormBookEntry> formList, String loginTime, String accNum) {

		String year = loginTime.substring(0, 4);
		String month = loginTime.substring(5, 7);
		boolean isSuccess = false;
		if (formList.size() > 0) {
			for (FormBookEntry form : formList) {
				if (10 == Integer.valueOf(form.getOutIntoWhsTypId())) {
					for (FormBookEntrySub sub : form.getBookEntrySub()) {
						// ������Ʒ�ڳ�����
						List<InvtyMthTermBgnTab> termSendList = getSendMthList(sub, Integer.valueOf(year),
								Integer.valueOf(month), form.getCustId());
						// ������Ʒ�����ڳ�
						InvtyMthTermBgnTab imSend = new InvtyMthTermBgnTab();

						Boolean isIt = true;
						Boolean isIs = true;

						if (termSendList.size() > 0) {
							isIs = true;
							imSend = termSendList.get(0);
						} else {
							isIs = false;
							imSend = setMthValue(year, month, accNum, loginTime, BigDecimal.ZERO, BigDecimal.ZERO,
									BigDecimal.ZERO, form, sub);
						}

						imSend = initImValue(imSend);

						// ������Ʒ���뷢������
						// imSend = setSendMth(imSend, form, sub);

						// ���
						BigDecimal othSendQty = imSend.getQty().add(imSend.getInQty()).subtract(imSend.getSendQty());
						BigDecimal othSendMoeny = imSend.getAmt().add(imSend.getInMoeny())
								.subtract(imSend.getSendMoeny());
						BigDecimal uprc = new BigDecimal(0);
						if (othSendQty.compareTo(BigDecimal.ZERO) == 0) {
							uprc = new BigDecimal(0.00000000);
						} else {
							uprc = othSendMoeny.divide(othSendQty, 8, BigDecimal.ROUND_HALF_UP).abs();
						}
						imSend.setOthMoeny(othSendMoeny);
						imSend.setOthQty(othSendQty);
						imSend.setOthUnitPrice(uprc);
						if (!isIs) {
							invtySendMthTermBgnTabDao.insertSendMth(imSend);
							isSuccess = true;
						} else {
							invtySendMthTermBgnTabDao.updateSendMth(imSend);
							isSuccess = true;
						}
					}
				}
			}
		}
		return isSuccess;
	}

	/**
	 * ������Ʒ�����ڳ�
	 */
	private InvtyMthTermBgnTab setSendMth(InvtyMthTermBgnTab imSend, FormBookEntry form, FormBookEntrySub sub) {
		imSend.setSendQty(Optional.ofNullable(imSend.getSendQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setSendMoeny(Optional.ofNullable(imSend.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
		imSend.setInQty(Optional.ofNullable(imSend.getInQty()).orElse(new BigDecimal(0.00000000)));
		imSend.setInMoeny(Optional.ofNullable(imSend.getInMoeny()).orElse(new BigDecimal(0.00000000)));
		Map map = new HashMap<>();
		map.put("invtyEncd", sub.getInvtyEncd());
		map.put("whsEncd", sub.getWhsEncd());
		map.put("batNum", sub.getBatNum());
		map.put("formNum", form.getFormNum());
		// map.put("custId", form.getCustId());
		map.put("loginTime", form.getFormDt());
		map.put("orderNum", sub.getOrdrNum());

		// BigDecimal openQty = sellComnInvDao.selectSellComnInvQty(map);
		BigDecimal openQty = sellComnInvDao.selectSellQtyByStored(map);
		if (openQty != null) {
			if (openQty.compareTo(sub.getQty()) == 0) {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxAmt()));
				imSend.setSendQty(imSend.getSendQty().add(sub.getQty()));
			} else {
				imSend.setSendMoeny(imSend.getSendMoeny().add(sub.getNoTaxUprc().multiply(openQty)));
				imSend.setSendQty(imSend.getSendQty().add(openQty));
			}

			if (imSend.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
				imSend.setSendUnitPrice(new BigDecimal(0.00000000));
			} else {

				imSend.setSendUnitPrice(
						imSend.getSendMoeny().divide(imSend.getSendQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
			}
		}

		// ����
		imSend.setInMoeny(imSend.getInMoeny().add(sub.getNoTaxAmt()));
		imSend.setInQty(imSend.getInQty().add(sub.getQty()));
		if (imSend.getInQty().compareTo(BigDecimal.ZERO) == 0) {
			imSend.setInUnitPrice(new BigDecimal(0.00000000));
		} else {
			imSend.setInUnitPrice(imSend.getInMoeny().divide(imSend.getInQty(), 8, BigDecimal.ROUND_HALF_UP).abs());
		}

		imSend.setCustId(form.getCustId());

		return imSend;
	}

	/**
	 * ��ѯ������Ʒ�����ڳ�
	 */
	private List<InvtyMthTermBgnTab> getSendMthList(FormBookEntrySub sub, Integer year, Integer month, String custId) {
		Map<String, Object> map = putParamMap(sub, year, month);
		map.put("custId", custId);
		List<InvtyMthTermBgnTab> tabList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(map);
		return tabList;

	}

	// �ܿ��
	public void dealInvtyTab(String loginTime) throws Exception {

		List<InvtyTab> its = invtyTabDao.selectInvtyTabTerm();
		List<InvtyTab> itList = new ArrayList<>();

//		if (its.size() == 0) {
//			// ��ѯ�ɹ����+�������
//			// its = invtyTabDao.selectInvtyTabIn();
//		}

		if (its.size() > 0) {

			for (InvtyTab it : its) {
				Map<String, Object> parmMap = new HashMap<>();

				parmMap.put("invtyEncd", it.getInvtyEncd());
				parmMap.put("whsEncd", it.getWhsEncd());
				parmMap.put("batNum", it.getBatNum());
				parmMap.put("qtys", BigDecimal.ZERO);
//				TermBgnBal tab = invtyTabDao.sumTerm(it);
//				if(tab == null) {		
//					tab = new TermBgnBal();
//				}

				// �ڳ�����
//				tab.setQty(Optional.ofNullable(tab.getQty()).orElse(BigDecimal.ZERO));
//				tab.setNoTaxAmt(Optional.ofNullable(tab.getNoTaxAmt()).orElse(BigDecimal.ZERO));	

				// it = invtyTabDao.selectInvtyTabsByTerm(it);

				// it = new InvtyTab();
				// BeanUtils.copyProperties(it, tab);

				// ��ѯ������
				// �ִ�����ʽ���ڳ�+�ɹ����+������(ALL)-���۳���+�����˻�-�ɹ��˻�-������(ALL)
				/* BigDecimal nowInStock = invtyTabDao.sumNowStockIn(parmMap); */
				/*
				 * if(nowInStock == null) { nowInStock = BigDecimal.ZERO; }
				 * it.setNowStok(nowInStock);
				 */

//				TermBgnBal nowOutStock = invtyTabDao.sumNowStockOut(it);
//				if(nowInStock == null) {
//					nowInStock = new TermBgnBal();
//				}
//				if(nowOutStock == null) {
//					nowOutStock = new TermBgnBal();
//				}
//				nowOutStock.setQty(Optional.ofNullable(nowOutStock.getQty()).orElse(BigDecimal.ZERO));

//				nowInStock.setQty(Optional.ofNullable(nowInStock.getQty()).orElse(BigDecimal.ZERO));

//				it.setNowStok(tab.getQty().add(nowInStock.getQty()).subtract(nowOutStock.getQty()));

				/*
				 * //��ѯ�ִ��� //��������ʽ���ڳ�+�ɹ����+������(014)+������-���۳���+�����˻�-�ɹ��˻�-������-������(015) BigDecimal
				 * avalQty = invtyTabDao.sumAvalStockIn(parmMap);
				 * 
				 * /
				 **/

				// Ǯ
				BigDecimal avalAmt = invtyTabDao.sumAvalStockOut(parmMap);
//				if (avalQty == null) {
//					avalQty = BigDecimal.ZERO;
//				}
				if (avalAmt == null) {
					avalAmt = BigDecimal.ZERO;
				}

//				avalInStock.setQty(Optional.ofNullable(avalInStock.getQty()).orElse(BigDecimal.ZERO));
//				avalInStock.setNoTaxAmt(Optional.ofNullable(avalInStock.getNoTaxAmt()).orElse(BigDecimal.ZERO));
//				avalOutStock.setQty(Optional.ofNullable(avalOutStock.getQty()).orElse(BigDecimal.ZERO));
//				avalOutStock.setNoTaxAmt(Optional.ofNullable(avalOutStock.getNoTaxAmt()).orElse(BigDecimal.ZERO));

				/*
				 * it.setAvalQty(avalQty); it.setUnTaxAmt(avalAmt);
				 */

				// �����

//				it.setAvalQty(tab.getQty().add(avalInStock.getQty()).subtract(avalOutStock.getQty()));
//				it.setUnTaxAmt(tab.getNoTaxAmt().add(avalInStock.getNoTaxAmt()).subtract(avalOutStock.getNoTaxAmt()));
				// System.out.println(it.getNowStok()+"------"+it.getAvalQty());
				if (it.getAvalQty().compareTo(BigDecimal.ZERO) == 0) {

					it.setUnTaxAmt(new BigDecimal(0.00000000));

					it.setCntnTaxAmt(new BigDecimal(0.00000000));

				} else {
//					it.setUnTaxUprc(
//							it.getUnTaxAmt().divide(it.getNowStok().abs(), BigDecimal.ROUND_HALF_UP).setScale(8));
					it.setUnTaxUprc(it.getUnTaxAmt());
					it.setCntnTaxAmt(it.getUnTaxUprc().multiply(new BigDecimal(100).add(it.getTaxRate()))
							.divide(new BigDecimal(100)));
//
//					it.setCntnTaxUprc(
//							it.getCntnTaxAmt().divide(it.getNowStok().abs(), BigDecimal.ROUND_HALF_UP).setScale(8));

				}
				invtyTabDao.dealUpdate(it);
				// itList.add(it);
			}

		}

		if (itList.size() > 0) {
			List<List<InvtyTab>> li = Lists.partition(itList, 1000);
			li.forEach(l -> {
				// invtyTabDao.insertInvtyTab(l);
			});

		}
	}

}
