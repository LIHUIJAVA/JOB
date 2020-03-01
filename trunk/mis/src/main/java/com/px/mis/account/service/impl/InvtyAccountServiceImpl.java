package com.px.mis.account.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.dao.FormBookEntrySubDao;
import com.px.mis.account.dao.InvtyDetailDao;
import com.px.mis.account.dao.InvtyMthTermBgnTabDao;
import com.px.mis.account.dao.InvtySendMthTermBgnTabDao;
import com.px.mis.account.dao.MthTermBgnTabDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.account.entity.InvtyDetails;
import com.px.mis.account.entity.InvtyMthTermBgnTab;
import com.px.mis.account.entity.MthTermBgnTab;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.service.InvtyAccountService;

import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.service.impl.InvtyClsServiceImpl;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;

@Service
@Transactional
public class InvtyAccountServiceImpl<E> implements InvtyAccountService {

	private Logger logger = LoggerFactory.getLogger(InvtyAccountServiceImpl.class);

	@Autowired
	private FormBookEntryDao formBookDao; // ��ϸ������
	@Autowired
	private FormBookEntrySubDao formBookSubDao; // ��ϸ���ӱ�
	@Autowired
	private MthTermBgnTabDao mthTermBgnTabDao; // �ڳ����
	@Autowired
	private InvtyDetailDao invtyDetailDao; // �����ϸ��
	@Autowired
	private IntoWhsDao intoWhsDao; // �ɹ���ⵥ
	@Autowired
	private OthOutIntoWhsMapper othOutDao; // ��������ⵥ
	@Autowired
	private SellOutWhsDao sellOutWhsDao; // ���۳��ⵥ
	@Autowired
	private InvtyMthTermBgnTabDao invtyMthTermBgnTabDao; // ��������ڳ�
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; // ������Ʒ�����ڳ�
	@Autowired
	private FormBookServiceImpl formBookImpl;
	@Autowired
	private InvtyClsDao invtyClsDao; // �������
	@Autowired
	private SellComnInvDao sellComnInvDao; // ��Ʊ

	@Override
	public String selectDetailList(Map map, String loginTime) throws IllegalAccessException, InvocationTargetException {
		// ��ϸ��
		// ��ѯ���� �������,���������,��������,�����Ŀ,����
		String resp = "";
		String message = "";
		Boolean isSuccess = true;

		String invIds = (String) map.get("invtyEncd"); // ���������
		List<String> invIdList = new ArrayList<>();
		invIdList = strToList(invIds);
		map.put("invIdList", invIdList);

		// �������ڲ��ܿ���
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");
		List<InvtyDetail> dataList = new ArrayList<>();
		InvtyDetail invtyDetail = new InvtyDetail();

		if (!start.substring(0, 4).equals(end.substring(0, 4))) {
			message = "��֧�ֿ����ѯ";
			isSuccess = false;
		} else {
			int num = Integer.valueOf(end.substring(5, 7)) - Integer.valueOf(start.substring(5, 7));
			if (num < 0) {
				message = "��ѯ��������";
				isSuccess = false;
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

				dataList = selectFormDetailByMonth(map);
				if (dataList.size() > 0) {
					invtyDetail = dataList.get(0);
				}
				message = "��ѯ�ɹ�!";
				isSuccess = true;
			}

		}
		try {
			resp = BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcct", isSuccess, message, invtyDetail);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return resp;
	}

	// ��ϸ��-����
	@Override
	public String selectDetailedListExport(Map map, String loginTime) throws Exception {
		// ��ϸ��
		// ��ѯ���� �������,���������,��������,�����Ŀ,����
		String resp = "";
		String message = "";
		Boolean isSuccess = true;

		String invIds = (String) map.get("invtyEncd"); // ���������
		List<String> invIdList = new ArrayList<>();
		invIdList = strToList(invIds);
		map.put("invIdList", invIdList);

		// �������ڲ��ܿ���
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");
		List<InvtyDetail> dataList = new ArrayList<>();
		InvtyDetail invtyDetail = new InvtyDetail();

		if (!start.substring(0, 4).equals(end.substring(0, 4))) {
			message = "��֧�ֿ��굼��";
			isSuccess = false;
		} else {
			int num = Integer.valueOf(end.substring(5, 7)) - Integer.valueOf(start.substring(5, 7));
			if (num < 0) {
				message = "��ѯ��������";
				isSuccess = false;
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

				dataList = selectFormDetailByMonth(map);
				if (dataList.size() > 0) {
					invtyDetail = dataList.get(0);
				}
				message = "��ѯ�ɹ�!";
				isSuccess = true;
			}
		}
		try {
			ArrayList<InvtyDetail> list = new ArrayList<InvtyDetail>();
			list.add(invtyDetail);
			ArrayList<JsonNode> nodeList = flattening(list);
			resp = BaseJson.returnRespList("account/invty/detailed/listExport", isSuccess, message, nodeList);

		} catch (IOException e) {

			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectDetailedList(Map map, String loginTime, String url) throws Exception {
		// ��ϸ��-�շ�����ܱ���ת
		String resp = "";
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		Map<String, Object> paramMap = new HashMap<>();
		InvtyDetail allDetail = new InvtyDetail();
		InvtyDetails itBegin = new InvtyDetails(); // �ڳ��������
		InvtyDetails month = new InvtyDetails();

		List<InvtyDetails> detailsList = new ArrayList<>();
		Map<String, List<InvtyMthTermBgnTab>> tabMap = new HashMap<>();
		List<InvtyMthTermBgnTab> tabList = new ArrayList<>();

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (start.substring(0, 4).equals(end.substring(0, 4))) {

				String year = start.substring(0, 4);
				start = start.substring(5, 7);
				end = end.substring(5, 7);
				map.put("year", year);
				map.put("start", start);
				map.put("end", end);

			} else {

				resp = BaseJson.returnRespObj(url, false, "��֧�ֿ����ѯ", allDetail);
				return resp;
			}
		}

		// ��ѯ����ڳ�
		List<InvtyMthTermBgnTab> tabs = invtyMthTermBgnTabDao.selectMthTermByInvty(map);
		if (tabs.size() > 0) {
			// ���ָ����ڳ�����
			tabMap = initInvtyBgnTab(tabs, tabMap);

			if (!tabMap.isEmpty()) {

				for (Map.Entry<String, List<InvtyMthTermBgnTab>> key : tabMap.entrySet()) {

					detailsList = new ArrayList<>();
					List<InvtyMthTermBgnTab> tabValue = (List<InvtyMthTermBgnTab>) key.getValue();
					itBegin = new InvtyDetails("�ڳ����", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
							tabValue.get(0).getAmt());
					detailsList.add(itBegin);

					for (InvtyMthTermBgnTab tab : tabValue) {
						BeanUtils.copyProperties(allDetail, tab);

						month = new InvtyDetails();
						month.setMakeVouchAbst(tab.getAcctiMth() + "�ºϼ�");
						month.setInMoeny(Optional.ofNullable(month.getInMoeny()).orElse(new BigDecimal(0.00000000)));
						month.setInQty(Optional.ofNullable(month.getInQty()).orElse(new BigDecimal(0.00000000)));
						month.setSendMoeny(
								Optional.ofNullable(month.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
						month.setSendQty(Optional.ofNullable(month.getSendQty()).orElse(new BigDecimal(0.00000000)));
						month.setDetailId(-1);
						paramMap.put("invtyClsEncd", tab.getInvtyClsEncd());
						paramMap.put("invtyEncd", tab.getInvtyEncd());
						paramMap.put("whsEncd", tab.getWhsEncd());
						paramMap.put("batNum", tab.getBatNum());
						paramMap.put("bookOkDt", tab.getAcctYr() + "-" + tab.getAcctiMth() + "-01");

						List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap);
						if (detailList.size() > 0) {
							for (InvtyDetail it : detailList) {
								detailsList.addAll(it.getInvtyDetailsList());

								for (InvtyDetails its : it.getInvtyDetailsList()) {
									if (its.getInMoeny() != null) {

										BigDecimal inMoney = month.getInMoeny().add(its.getInMoeny());
										month.setInMoeny(inMoney);
									}
									if (its.getInQty() != null) {

										BigDecimal inQty = month.getInQty().add(its.getInQty());
										month.setInQty(inQty);
									}
									if (its.getSendMoeny() != null) {

										BigDecimal sendMoeny = month.getSendMoeny().add(its.getSendMoeny());
										month.setSendMoeny(sendMoeny);
									}
									if (its.getSendQty() != null) {

										BigDecimal sendQty = month.getSendQty().add(its.getSendQty());
										month.setSendQty(sendQty);
									}
								}
								month.setOthMoeny(it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1)
										.getOthMoeny());
								month.setOthUnitPrice(it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1)
										.getOthUnitPrice());
								month.setOthQty(
										it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1).getOthQty());

							}
						}
						detailsList.add(month);
					}

				}

			} else {
				itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				detailsList.add(itBegin);
			}

		} else {
			itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00), new BigDecimal(0.00));
			detailsList.add(itBegin);
		}

		allDetail.setInvtyDetailsList(detailsList);

		resp = BaseJson.returnRespObj(url, true, "��ѯ�ɹ�", allDetail);
		return resp;

	}

	private List<InvtyDetail> selectFormDetailByMonth(Map map)
			throws IllegalAccessException, InvocationTargetException {

		Map<String, Object> initMap = new HashMap<>();
		List<InvtyDetails> detailList = new ArrayList<>();
		List<InvtyDetail> dataList = new ArrayList<>();
		InvtyDetail it = new InvtyDetail();
		InvtyDetails its = new InvtyDetails();
		List<InvtyMthTermBgnTab> tabs = invtyMthTermBgnTabDao.selectByMthTerm(map);

		if (tabs.size() > 0) {

			its = new InvtyDetails("�ڳ����", tabs.get(0).getUprc(), tabs.get(0).getQty(), tabs.get(0).getAmt());
			BeanUtils.copyProperties(it, tabs.get(0));
			detailList.add(its);

			for (InvtyMthTermBgnTab tab : tabs) {

				detailList = getInvtyDetailList(tab, null, detailList);
				it.setInvtyDetailsList(detailList);

			}
			dataList.add(it);
		} else {

			List<MthTermBgnTab> mthTabs = mthTermBgnTabDao.selectMthTermInitList(map);

			if (mthTabs.size() > 0) {

				initMap.clear();
				its = new InvtyDetails("�ڳ����", mthTabs.get(0).getUprc(), mthTabs.get(0).getQty(),
						mthTabs.get(0).getAmt());
				BeanUtils.copyProperties(it, mthTabs.get(0));
				detailList.add(its);
				for (MthTermBgnTab mthTab : mthTabs) {

					detailList = getInvtyDetailList(null, mthTab, detailList);
					it.setInvtyDetailsList(detailList);

				}
				dataList.add(it);
			} else {
				its = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00), new BigDecimal(0.00));
				detailList.add(its);
				it.setInvtyDetailsList(detailList);
				dataList.add(it);
			}

		}

		return dataList;

	}

	private List<InvtyDetails> getInvtyDetailList(InvtyMthTermBgnTab tab, MthTermBgnTab mthTab,
			List<InvtyDetails> dataList) {

		Map<String, Object> paramMap = new HashMap<>();
		if (tab != null) {
			paramMap.put("invtyClsEncd", tab.getInvtyClsEncd());
			paramMap.put("invtyEncd", tab.getInvtyEncd());
			paramMap.put("whsEncd", tab.getWhsEncd());
			paramMap.put("bat", tab.getBatNum());
			paramMap.put("bookOkDt", tab.getAcctYr() + "-" + tab.getAcctiMth() + "-01");
		}
		if (mthTab != null) {
			paramMap.put("invtyClsEncd", "");
			paramMap.put("invtyEncd", mthTab.getInvtyEncd());
			paramMap.put("whsEncd", mthTab.getWhsEncd());
			paramMap.put("bat", mthTab.getBat());

			paramMap.put("bookOkDt", mthTab.getAcctYr() + "-" + mthTab.getAcctiMth() + "-01");
		}

		List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap);
		if (detailList.size() > 0) {
			InvtyDetails month = new InvtyDetails();

			for (InvtyDetail it : detailList) {

				month.setMakeVouchAbst(tab.getAcctiMth() + "�ºϼ�");
				month.setInMoeny(Optional.ofNullable(month.getInMoeny()).orElse(new BigDecimal(0.00000000)));
				month.setInQty(Optional.ofNullable(month.getInQty()).orElse(new BigDecimal(0.00000000)));
				month.setSendMoeny(Optional.ofNullable(month.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
				month.setSendQty(Optional.ofNullable(month.getSendQty()).orElse(new BigDecimal(0.00000000)));

				for (InvtyDetails its : it.getInvtyDetailsList()) {

					if (its.getInMoeny() != null) {
						// BigDecimal inMoney =
						// it.getInvtyDetailsList().stream().map(InvtyDetails::getInMoeny).reduce(BigDecimal.ZERO,
						// BigDecimal::add);
						BigDecimal inMoney = month.getInMoeny().add(its.getInMoeny());
						month.setInMoeny(inMoney);
					}
					if (its.getInQty() != null) {
						// BigDecimal inQty =
						// it.getInvtyDetailsList().stream().map(InvtyDetails::getInQty).reduce(BigDecimal.ZERO,
						// BigDecimal::add);
						BigDecimal inQty = month.getInQty().add(its.getInQty());
						month.setInQty(inQty);
					}
					if (its.getSendMoeny() != null) {
						// BigDecimal sendMoeny =
						// it.getInvtyDetailsList().stream().map(InvtyDetails::getSendMoeny).reduce(BigDecimal.ZERO,
						// BigDecimal::add);
						BigDecimal sendMoeny = month.getSendMoeny().add(its.getSendMoeny());
						month.setSendMoeny(sendMoeny);
					}
					if (its.getSendQty() != null) {
						// BigDecimal sendQty =
						// it.getInvtyDetailsList().stream().map(InvtyDetails::getSendQty).reduce(BigDecimal.ZERO,
						// BigDecimal::add);
						BigDecimal sendQty = month.getSendQty().add(its.getSendQty());
						month.setSendQty(sendQty);
					}
				}
				month.setOthMoeny(it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1).getOthMoeny());
				month.setOthUnitPrice(
						it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1).getOthUnitPrice());
				month.setOthQty(it.getInvtyDetailsList().get(it.getInvtyDetailsList().size() - 1).getOthQty());

				dataList.addAll(it.getInvtyDetailsList());
			}
			dataList.add(month);

		}

		return dataList;
	}

//������Ʒ��ϸ��-��ѯ
	@Override
	public String selectsendProductList(Map map, String loginTime) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		List<InvtyDetails> itList = new ArrayList<>();
		Map<String, List<InvtyMthTermBgnTab>> tabMap = new HashMap<>();
		InvtyDetails month = new InvtyDetails();
		InvtyDetail allDetail = new InvtyDetail();
		Map<String, Object> paramMap = new HashMap<>();
		String resp = "";

		// ��ѯ�ֿ�����ϸ �������/�������/��������/�ͻ�/����
		// �������ڲ��ܿ���
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		if (!start.substring(0, 4).equals(end.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
			message = "��֧�ֿ����ѯ";
			isSuccess = false;
		} else {
			map.put("year", start.substring(0, 4));
			map.put("month", start.substring(5, 7));

			List<InvtyMthTermBgnTab> tabList = invtySendMthTermBgnTabDao.selectSendMthByMth(map);
			InvtyDetails itBegin = new InvtyDetails();
			if (tabList.size() > 0) {
				tabMap = initInvtyBgnTab(tabList, tabMap);

				if (!tabMap.isEmpty()) {

					for (Map.Entry<String, List<InvtyMthTermBgnTab>> key : tabMap.entrySet()) {

						itList = new ArrayList<>();
						List<InvtyMthTermBgnTab> tabValue = (List<InvtyMthTermBgnTab>) key.getValue();
						itBegin = new InvtyDetails("�ڳ����", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
								tabValue.get(0).getAmt());
						itList.add(itBegin);

						for (InvtyMthTermBgnTab tab : tabValue) {

							BeanUtils.copyProperties(allDetail, tab);

							month = new InvtyDetails();
							month.setMakeVouchAbst(tab.getAcctiMth() + "�ºϼ�");
							month.setInMoeny(
									Optional.ofNullable(month.getInMoeny()).orElse(new BigDecimal(0.00000000)));
							month.setInQty(Optional.ofNullable(month.getInQty()).orElse(new BigDecimal(0.00000000)));
							month.setSendMoeny(
									Optional.ofNullable(month.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
							month.setSendQty(
									Optional.ofNullable(month.getSendQty()).orElse(new BigDecimal(0.00000000)));
							month.setDetailId(-1);
							paramMap.put("invtyClsEncd", tab.getInvtyClsEncd());
							paramMap.put("invtyEncd", tab.getInvtyEncd());
							paramMap.put("whsEncd", tab.getWhsEncd());
							paramMap.put("batNum", tab.getBatNum());
							paramMap.put("bookOkDt", tab.getAcctYr() + "-" + tab.getAcctiMth() + "-01");
							paramMap.put("recvSendCateId", "10");
							// ��ѯ���µ����۳��ⵥ�Ͷ�Ӧ��Ʊ

							List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap); // ����
							List<InvtyDetails> sellList = sellComnInvDao.selectSellComnDetailList(paramMap);// ����
							if (detailList.size() > 0) {
								for (InvtyDetail it : detailList) {

									for (InvtyDetails its : it.getInvtyDetailsList()) {
										InvtyDetails itss = new InvtyDetails();
										BeanUtils.copyProperties(itss, its);
										itss.setInQty(its.getSendQty());
										itss.setInMoeny(its.getSendMoeny());
										itss.setFormNum(its.getFormNum());
										itss.setDetailId(-10);

										BigDecimal inMoney = month.getInMoeny().add(itss.getInMoeny());
										month.setInMoeny(inMoney);

										itss.setOthMoeny(itBegin.getOthMoeny().add(itss.getInMoeny()));
										itss.setOthQty(itBegin.getOthQty().add(itss.getInQty()));
										BigDecimal inQty = month.getInQty().add(itss.getInQty());
										month.setInQty(inQty);

										if (itss.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
											itss.setOthUnitPrice(itss.getOthMoeny().divide(itss.getOthQty(), 8,
													BigDecimal.ROUND_HALF_UP));
										}
										itList.add(itss);
									}
								}
							}

							if (sellList.size() > 0) {
								for (InvtyDetails its : sellList) {

									its.setDetailId(-20);
									if (its.getSendMoeny() != null) {

										BigDecimal sendMoney = month.getSendMoeny().add(its.getSendMoeny());
										month.setSendMoeny(sendMoney);
									}
									if (its.getSendQty() != null) {

										BigDecimal sendQty = month.getSendQty().add(its.getSendQty());
										month.setSendQty(sendQty);
									}
									its.setOthMoeny(itBegin.getOthMoeny().subtract(its.getSendMoeny()));
									its.setOthQty(itBegin.getOthQty().subtract(its.getSendQty()));
									if (its.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
										its.setOthUnitPrice(
												its.getOthMoeny().divide(its.getOthQty(), 8, BigDecimal.ROUND_HALF_UP));

									}
									itList.addAll(sellList);
								}
								month.setOthMoeny(
										itBegin.getOthMoeny().add(month.getInMoeny()).subtract(month.getSendMoeny()));
								month.setOthQty(itBegin.getOthQty().add(month.getInQty()).subtract(month.getSendQty()));
								if (month.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
									month.setOthUnitPrice(
											month.getOthMoeny().divide(month.getOthQty(), 8, BigDecimal.ROUND_HALF_UP));

								}
								itList.add(month);
							}
						}

					}

				} else {
					// map�ǿ�
					itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00),
							new BigDecimal(0.00));
					itList.add(itBegin);
				}
			} else {

				// ���Ϊ��
				itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				itList.add(itBegin);
			}
		}
		allDetail.setInvtyDetailsList(itList);
		message = "��ѯ�ɹ�";
		isSuccess = true;
		resp = BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcct", isSuccess, message, allDetail);

		return resp;
	}

	// ������Ʒ��ϸ��-����
	@Override
	public String selectsendProductListExport(Map map, String loginTime) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		List<InvtyDetails> itList = new ArrayList<>();
		Map<String, List<InvtyMthTermBgnTab>> tabMap = new HashMap<>();
		InvtyDetails month = new InvtyDetails();
		InvtyDetail allDetail = new InvtyDetail();
		Map<String, Object> paramMap = new HashMap<>();
		String resp = "";

		// ��ѯ�ֿ�����ϸ �������/�������/��������/�ͻ�/����
		// �������ڲ��ܿ���
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		if (!start.substring(0, 4).equals(end.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
			message = "��֧�ֿ����ѯ";
			isSuccess = false;
		} else {
			map.put("year", start.substring(0, 4));
			map.put("month", start.substring(5, 7));

			List<InvtyMthTermBgnTab> tabList = invtySendMthTermBgnTabDao.selectSendMthByMth(map);
			InvtyDetails itBegin = new InvtyDetails();
			if (tabList.size() > 0) {
				tabMap = initInvtyBgnTab(tabList, tabMap);

				if (!tabMap.isEmpty()) {

					for (Map.Entry<String, List<InvtyMthTermBgnTab>> key : tabMap.entrySet()) {

						itList = new ArrayList<>();
						List<InvtyMthTermBgnTab> tabValue = (List<InvtyMthTermBgnTab>) key.getValue();
						itBegin = new InvtyDetails("�ڳ����", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
								tabValue.get(0).getAmt());
						itList.add(itBegin);

						for (InvtyMthTermBgnTab tab : tabValue) {

							BeanUtils.copyProperties(allDetail, tab);

							month = new InvtyDetails();
							month.setMakeVouchAbst(tab.getAcctiMth() + "�ºϼ�");
							month.setInMoeny(
									Optional.ofNullable(month.getInMoeny()).orElse(new BigDecimal(0.00000000)));
							month.setInQty(Optional.ofNullable(month.getInQty()).orElse(new BigDecimal(0.00000000)));
							month.setSendMoeny(
									Optional.ofNullable(month.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
							month.setSendQty(
									Optional.ofNullable(month.getSendQty()).orElse(new BigDecimal(0.00000000)));
							month.setDetailId(-1);
							paramMap.put("invtyClsEncd", tab.getInvtyClsEncd());
							paramMap.put("invtyEncd", tab.getInvtyEncd());
							paramMap.put("whsEncd", tab.getWhsEncd());
							paramMap.put("batNum", tab.getBatNum());
							paramMap.put("bookOkDt", tab.getAcctYr() + "-" + tab.getAcctiMth() + "-01");
							paramMap.put("recvSendCateId", "10");
							// ��ѯ���µ����۳��ⵥ�Ͷ�Ӧ��Ʊ

							List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap); // ����
							List<InvtyDetails> sellList = sellComnInvDao.selectSellComnDetailList(paramMap);// ����
							if (detailList.size() > 0) {
								for (InvtyDetail it : detailList) {

									for (InvtyDetails its : it.getInvtyDetailsList()) {
										InvtyDetails itss = new InvtyDetails();
										itss.setInQty(its.getSendQty());
										itss.setInMoeny(its.getSendMoeny());
										itss.setFormNum(its.getFormNum());
										itss.setDetailId(-10);

										BigDecimal inMoney = month.getInMoeny().add(itss.getInMoeny());
										month.setInMoeny(inMoney);

										itss.setOthMoeny(itBegin.getOthMoeny().add(itss.getInMoeny()));
										itss.setOthQty(itBegin.getOthQty().add(itss.getInQty()));
										BigDecimal inQty = month.getInQty().add(itss.getInQty());
										month.setInQty(inQty);

										if (itss.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
											itss.setOthUnitPrice(itss.getOthMoeny().divide(itss.getOthQty(), 8,
													BigDecimal.ROUND_HALF_UP));
										}
										itList.add(itss);
									}
								}
							}

							if (sellList.size() > 0) {
								for (InvtyDetails its : sellList) {

									its.setDetailId(-20);
									if (its.getSendMoeny() != null) {

										BigDecimal sendMoney = month.getSendMoeny().add(its.getSendMoeny());
										month.setSendMoeny(sendMoney);
									}
									if (its.getSendQty() != null) {

										BigDecimal sendQty = month.getSendQty().add(its.getSendQty());
										month.setSendQty(sendQty);
									}
									its.setOthMoeny(itBegin.getOthMoeny().subtract(its.getSendMoeny()));
									its.setOthQty(itBegin.getOthQty().subtract(its.getSendQty()));
									if (its.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
										its.setOthUnitPrice(
												its.getOthMoeny().divide(its.getOthQty(), 8, BigDecimal.ROUND_HALF_UP));

									}
									itList.addAll(sellList);
								}
								month.setOthMoeny(
										itBegin.getOthMoeny().add(month.getInMoeny()).subtract(month.getSendMoeny()));
								month.setOthQty(itBegin.getOthQty().add(month.getInQty()).subtract(month.getSendQty()));
								if (month.getOthQty().compareTo(BigDecimal.ZERO) != 0) {
									month.setOthUnitPrice(
											month.getOthMoeny().divide(month.getOthQty(), 8, BigDecimal.ROUND_HALF_UP));

								}
								itList.add(month);
							}
						}

					}

				} else {
					// map�ǿ�
					itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00),
							new BigDecimal(0.00));
					itList.add(itBegin);
				}
			} else {

				// ���Ϊ��
				itBegin = new InvtyDetails("�ڳ����", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				itList.add(itBegin);
			}

		}
		allDetail.setInvtyDetailsList(itList);
		message = "��ѯ�ɹ�";
		isSuccess = true;
		ArrayList<InvtyDetail> list = new ArrayList<InvtyDetail>();
		list.add(allDetail);// �淶���
		ArrayList<JsonNode> nodeList = flattening(list);// ���ر�ƽjson

		resp = BaseJson.returnRespList("account/invty/sendProduct/listExport", isSuccess, message, nodeList);

		return resp;
	}

	/**
	 * init�����ڳ��Ĵ������
	 * 
	 * @param tabList
	 * @param tabMap
	 * @return
	 */
	private Map<String, List<InvtyMthTermBgnTab>> initInvtyBgnTab(List<InvtyMthTermBgnTab> tabList,
			Map<String, List<InvtyMthTermBgnTab>> tabMap) {

		for (InvtyMthTermBgnTab tab : tabList) {

			if (!tabMap.containsKey(tab.getWhsEncd() + "-" + tab.getInvtyEncd() + "-" + tab.getBatNum())) {

				tabList = new ArrayList<>();
			} else {
				tabList = (List<InvtyMthTermBgnTab>) tabMap
						.get(tab.getWhsEncd() + "-" + tab.getInvtyEncd() + "-" + tab.getBatNum());

			}

			tabList.add(tab);
			tabMap.put(tab.getWhsEncd() + "-" + tab.getInvtyEncd() + "-" + tab.getBatNum(), tabList);
		}
		return tabMap;
	}

	// ��ˮ��-��ѯ
	@Override
	public String selectStreamList(Map map, String loginTime) throws Exception {
		// ��ˮ��
		// �Ѽ���/δ���� �ֿ�,�������,��������
		String resp = "";
		int count = 0;
		int num = (int) map.get("num");
		List<FormBookEntry> dataList = new ArrayList<>();
		List<FormBookEntrySub> subList = new ArrayList<>();
		List<String> invIdList = strToList((String) map.get("invtyEncd"));
		List<String> formCodeList = strToList((String) map.get("formType"));
		List<String> whsEncdList = strToList((String) map.get("whsEncd"));
		String isNtBookOk = (String) map.get("isNtBookOk");
		map.put("invIdList", invIdList);
		map.put("formCodeList", formCodeList);
		map.put("whsEncdList", whsEncdList);
		if (isNtBookOk.equals("0")) {

			dataList = formBookDao.selectStreamALLList(map);
			if (dataList.size() > 0) {

				for (FormBookEntry form : dataList) {
					if (form.getBookEntrySub().size() > 0) {
						for (FormBookEntrySub sub : form.getBookEntrySub()) {
							if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
								sub.setInAmt(sub.getNoTaxAmt());
								sub.setInQty(sub.getQty());
								sub.setInUprc(sub.getNoTaxUprc());
							}
							if (strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
								sub.setSendAmt(sub.getNoTaxAmt());
								sub.setSendQty(sub.getQty());
								sub.setSendUprc(sub.getNoTaxUprc());
							}
						}
					}
				}
			}
			count = formBookDao.selectStreamALLCount(map);

		} else {

			dataList = formBookDao.selectStreamMap(map);
			count = formBookDao.countSelectStreamMap(map);
		}

		if (dataList.size() > 0) {
			for (FormBookEntry form : dataList) {

				for (FormBookEntrySub sub : form.getBookEntrySub()) {
					// ����
					if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {

						sub.setInAmt(sub.getNoTaxAmt());
						sub.setInQty(sub.getQty());
						sub.setInUprc(sub.getNoTaxUprc());
					}
					// ����
					if (strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {

						sub.setSendQty(sub.getQty());
						sub.setSendUprc(sub.getNoTaxUprc());
						sub.setSendAmt(sub.getNoTaxAmt());

					}
				}
			}
		}

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		resp = BaseJson.returnRespList("mis/account/invty/stream/list", true, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0,
				dataList);
		return resp;
	}

	// ��ˮ��-����
	@Override
	public String selectStreamListExport(Map map, String loginTime) throws Exception {
		// ��ˮ��
		// �Ѽ���/δ���� �ֿ�,�������,��������
		String resp = "";
//		int num = (int) map.get("num");
		List<FormBookEntry> dataList = new ArrayList<>();
		List<FormBookEntrySub> subList = new ArrayList<>();
		List<String> invIdList = strToList((String) map.get("invtyEncd"));
		List<String> formCodeList = strToList((String) map.get("formType"));
		List<String> whsEncdList = strToList((String) map.get("whsEncd"));
		String isNtBookOk = (String) map.get("isNtBookOk");
		map.put("invIdList", invIdList);
		map.put("formCodeList", formCodeList);
		map.put("whsEncdList", whsEncdList);
		if (isNtBookOk.equals("0")) {// δ����

			dataList = formBookDao.selectStreamALLList(map);// �ɹ���+���۳�+��������
			if (dataList.size() > 0) {

				for (FormBookEntry form : dataList) {
					if (form.getBookEntrySub().size() > 0) {
						for (FormBookEntrySub sub : form.getBookEntrySub()) {
							if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
								// ȡÿһ���ӱ����,�ж��������
								sub.setInAmt(sub.getNoTaxAmt());
								sub.setInQty(sub.getQty());
								sub.setInUprc(sub.getNoTaxUprc());
							}
							if (strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
								// ÿ���ӱ���ϸ�жϳ�������
								sub.setSendAmt(sub.getNoTaxAmt());
								sub.setSendQty(sub.getQty());
								sub.setSendUprc(sub.getNoTaxUprc());
							}
						}
					}
				}
			}

		} else {// ��ѯ�Ѽ���
			dataList = formBookDao.selectStreamMap(map);
//			count = formBookDao.countSelectStreamMap(map);
		}

		if (dataList.size() > 0) {
			for (FormBookEntry form : dataList) {

				for (FormBookEntrySub sub : form.getBookEntrySub()) {
					// ����
					if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {

						sub.setInAmt(sub.getNoTaxAmt());
						sub.setInQty(sub.getQty());
						sub.setInUprc(sub.getNoTaxUprc());
					}
					// ����
					if (strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {

						sub.setSendQty(sub.getQty());
						sub.setSendUprc(sub.getNoTaxUprc());
						sub.setSendAmt(sub.getNoTaxAmt());

					}
				}
			}
		}

		ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
		try {
			JacksonUtil.turnOnAnno();
			for (int i = 0; i < dataList.size(); i++) {
				FormBookEntry main = (FormBookEntry) dataList.get(i);
				List<FormBookEntrySub> bookEntrySub = main.getBookEntrySub();
				for (FormBookEntrySub sub : bookEntrySub) {
					main.setBookEntrySub(null);
					ObjectNode bond = JacksonUtil.getObjectNode("");
					ObjectNode mainNode = JacksonUtil.getObjectNode(main);
					ObjectNode subNode = JacksonUtil.getObjectNode(sub);
					bond.setAll(mainNode);
					bond.setAll(subNode);
					nodeList.add(bond);
				}
			}
		} finally {
			JacksonUtil.turnOffAnno();
		}
		resp = BaseJson.returnRespList("/account/invty/stream/listExport", true, "��ѯ�ɹ�", nodeList);
		return resp;

	}

	/**
	 * ��ѯ���ݺ��Ƿ����
	 */
	private Boolean selectByFormNum(String formNum) {
		FormBookEntry formBook = formBookDao.selectByFormNum(formNum);
		if (formBook == null) {
			return false;
		}
		return true;
	}

	/**
	 * �ַ���תlist
	 */
	private List<String> strToList(String param) {

		List<String> list = new ArrayList<>();
		if (StringUtils.isNotEmpty(param)) {
			if (param.contains(",")) {
				String[] str = param.split(",");
				for (int i = 0; i < str.length; i++) {

					list.add(str[i]);

				}
			} else {
				if (StringUtils.isNotEmpty(param)) {
					list.add(param);
				}
			}
		}

		return list;

	}

	/**
	 * ������ϸ
	 */
	private Map<String, Object> countDetail(int count, MthTermBgnTab tab, Map map, String start, String end,
			List<InvtyDetails> invtyDetailsList, InvtyDetail detail, int month)
			throws IllegalAccessException, InvocationTargetException {
		List<FormBookEntrySub> inList = new ArrayList<>(); // ���뵥�ݼ���
		List<FormBookEntrySub> outList = new ArrayList<>(); // �������ݼ���
		Map<String, Object> dataMap = new HashMap<>();

		BigDecimal sendQtyYear = new BigDecimal(0); // ��
		BigDecimal sendMoenyYear = new BigDecimal(0.00);
		BigDecimal inQtyYear = new BigDecimal(0);
		BigDecimal inMoenyYear = new BigDecimal(0.00);

		BigDecimal inQty = new BigDecimal(0); // ��
		BigDecimal inMoeny = new BigDecimal(0.00);
		BigDecimal sendQty = new BigDecimal(0);
		BigDecimal sendMoeny = new BigDecimal(0.00);
		BigDecimal othUnitPrice = new BigDecimal(0.00);
		BigDecimal othQty = new BigDecimal(0);
		BigDecimal othMoeny = new BigDecimal(0.00);
		int inCount = 0;
		int outCount = 0;
		InvtyDetails it = new InvtyDetails(); // �ڳ�

		if (tab == null) {

			it = new InvtyDetails("�ڳ����", new BigDecimal(0.00), new BigDecimal(0), new BigDecimal(0.00));

		} else {
			String whsCode = tab.getWhsEncd();
			map.put("whsCode", whsCode);
			it = new InvtyDetails("�ڳ����", tab.getUprc(), tab.getQty(), tab.getAmt());
		}
		invtyDetailsList.add(it);

		List<String> inIdList = new ArrayList<>();
		inIdList = strToList("1,3,5,8,9,12");
		map.put("inWhsTyp", inIdList);

		map.put("bookOkSDt", start);
		map.put("bookOkEDt", end);

		inCount = formBookSubDao.selectDetailCount(map);
		inList = formBookSubDao.selectDetailList(map);
		// ���� 2,4,6,7,10,11;
		List<String> outIdList = new ArrayList<>();

		outIdList = strToList("2,4,6,7,10,11");

		inIdList.clear();
		map.put("inWhsTyp", inIdList);
		map.put("outWhsTyp", outIdList);

		outCount = formBookSubDao.selectDetailCount(map);
		outList = formBookSubDao.selectDetailList(map);
		count = inCount + outCount;

		// ����
		for (FormBookEntrySub in : inList) {
			InvtyDetails itIn = new InvtyDetails();

			BeanUtils.copyProperties(itIn, in);
			itIn.setInUnitPrice(in.getNoTaxUprc());
			itIn.setInQty(in.getBxQty());
			itIn.setInMoeny(in.getNoTaxAmt());
			itIn.setSendQty(new BigDecimal(0));

			// ���� = �ڳ�+����-����
			itIn.setOthQty(it.getOthQty().add(itIn.getInQty()));
			itIn.setOthUnitPrice(in.getNoTaxUprc());
			itIn.setOthMoeny(it.getOthMoeny().add(itIn.getInMoeny()));

			inQty = inQty.add(itIn.getInQty()); // ������������
			inMoeny = inMoeny.add(itIn.getInMoeny()); // ����������
			othUnitPrice = othUnitPrice.add(in.getNoTaxUprc()); // ���ۺ�

			invtyDetailsList.add(itIn);

		}
		// ����
		for (FormBookEntrySub out : outList) {
			InvtyDetails itOut = new InvtyDetails();

			BeanUtils.copyProperties(itOut, out);
			itOut.setSendUnitPrice(out.getNoTaxUprc());
			itOut.setSendQty(out.getBxQty());
			itOut.setSendMoeny(out.getNoTaxAmt());

			// ���� = �ڳ�-����
			itOut.setOthQty(it.getOthQty().subtract(itOut.getSendQty()));
			itOut.setOthUnitPrice(out.getNoTaxUprc());
			itOut.setOthMoeny(it.getOthMoeny().subtract(itOut.getSendMoeny()));

			sendQty = sendQty.add(itOut.getSendQty()); // ���·�������
			sendMoeny = sendMoeny.add(itOut.getSendMoeny()); // ���·������
			othUnitPrice = othUnitPrice.add(out.getNoTaxUprc()); // ���ۺ�

			invtyDetailsList.add(itOut);

		}
		inQtyYear = inQtyYear.add(inQty);
		inMoenyYear = inMoenyYear.add(inMoeny);
		sendQtyYear = sendQtyYear.add(sendQty);
		sendMoenyYear = sendMoenyYear.add(sendMoeny);
		BigDecimal de = new BigDecimal(1);
		if (outList.size() > 0 && inList.size() > 0) {
			de = new BigDecimal(outList.size() + inList.size());

			othUnitPrice = othUnitPrice.divide(de, 6, BigDecimal.ROUND_HALF_UP); // ����㵥��

			InvtyDetails itM = new InvtyDetails(month + "�ºϼ�", inQty, inMoeny, sendQty, sendMoeny, othUnitPrice, othQty,
					othMoeny);

			invtyDetailsList.add(itM);
		}

		dataMap.put("invtyDetailsList", invtyDetailsList);
		dataMap.put("sendQtyYear", sendQtyYear); // ��
		dataMap.put("sendMoenyYear", sendMoenyYear);
		dataMap.put("inQtyYear", inQtyYear);
		dataMap.put("inMoenyYear", inMoenyYear);
		dataMap.put("inQty", inQty); // ��
		dataMap.put("inMoeny", inMoeny);
		dataMap.put("sendQty", sendQty);
		dataMap.put("sendMoeny", sendMoeny);
		dataMap.put("othUnitPrice", othUnitPrice);
		dataMap.put("othQty", othQty);
		dataMap.put("othMoeny", othMoeny);
		dataMap.put("count", count); //
		dataMap.put("outCount", outCount); // ��
		dataMap.put("inList", inList);
		dataMap.put("outList", outList);
		return dataMap;
	}

	/**
	 * ��ȡ����ʱ������
	 */
	private static long getMonthPoor(String start, String end) {
		// SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		// 2018-10-26 15:43:55
		DateTime startDate = null;
		DateTime endDate = null;

		startDate = formatter.parseDateTime(start);
		endDate = formatter.parseDateTime(end);

		int months = Months.monthsBetween(startDate, endDate).getMonths();
		return months;
	}

	private static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ���������
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// �����������·ݵ��������
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	private static String getFristDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ���������
		int firstDay = cal.getActualMinimum(Calendar.DATE);
		// �����������·ݵ��������
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	@Override
	public String sendAndReceivePool(Map map, String loginTime) throws Exception {

		List<InvtyDetail> itList = new ArrayList<>();
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int count = 0;
		int poolType = Integer.valueOf((String) map.get("poolType"));
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ����ѯ";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceive/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}

		// ���ղֿ����
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// �������λ���
			map.put("isBatNumPool", "batNum");

		} else {
			// ���մ���������
			map.put("isInvtyEncdCls", "isInvtyEncdCls");

		}

		itList = invtyDetailDao.selectSendAndRecePool(map);
		count = invtyDetailDao.countSelectSendAndRecePool(map);
		if (itList.size() > 0) {
			for (InvtyDetail it : itList) {
				it.setQty(Optional.ofNullable(it.getQty()).orElse(new BigDecimal(0.00000000)));
				if (it.getQty().compareTo(BigDecimal.ZERO) != 0) {
					it.setUprc(it.getAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
				}
				if (it.getInvtyDetailsList().size() > 0) {
					for (InvtyDetails its : it.getInvtyDetailsList()) {
						its.setSendQty(Optional.ofNullable(its.getSendQty()).orElse(new BigDecimal(0.00000000)));
						if (its.getSendQty().compareTo(BigDecimal.ZERO) != 0) {
							its.setSendUnitPrice(
									its.getSendMoeny().divide(its.getSendQty(), 8, BigDecimal.ROUND_HALF_UP));
						}
						its.setInQty(Optional.ofNullable(its.getInQty()).orElse(new BigDecimal(0.00000000)));
						if (its.getInQty().compareTo(BigDecimal.ZERO) != 0) {
							its.setInUnitPrice(its.getInMoeny().divide(its.getInQty(), 8, BigDecimal.ROUND_HALF_UP));
						}
					}

				}

			}
		}

		resp = BaseJson.returnRespList("account/invty/sendAndReceive/pool", true, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0,
				itList);
		return resp;

	}

//������Ʒ���ܱ�-��ѯ
	@Override
	public String sendProductsPool(Map map, String loginTime) throws Exception {
		List<InvtyMthTermBgnTab> tabList = new ArrayList<>();
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int count = 0;
		int poolType = Integer.valueOf((String) map.get("poolType"));
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");
		String start1 = (String) map.get("bookOkSDt");
		String end1 = (String) map.get("bookOkEDt");
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ����ѯ";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendProducts/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, tabList);
			} else {
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));
				map.put("year", start.substring(0, 4));
			}
		}

		if (StringUtils.isNotEmpty(start1) && StringUtils.isNotEmpty(end1)) {
			if (!start1.substring(0, 4).equals(end1.substring(0, 4))
					|| !start1.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start1.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ����ѯ";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendProducts/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, tabList);
			}
		}
		// ���ղֿ����
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// ���մ������
			map.put("isInvtyEncdPool", "invtyEncd");
		} else {
			// ���մ��+�ͻ�����
			map.put("isCustPool", "cust");
		}

		tabList = invtySendMthTermBgnTabDao.sendProductMthPool(map);
		count = invtySendMthTermBgnTabDao.countSendProductMthPool(map);

		resp = BaseJson.returnRespList("account/invty/sendProducts/pool", true, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0,
				tabList);
		return resp;
	}

	//������Ʒ���ܱ�-����
		@Override
		public String sendProductsPoolExport(Map map, String loginTime) throws Exception {
			List<InvtyMthTermBgnTab> tabList = new ArrayList<>();
			String resp = "";
			String message = "";
			Boolean isSuccess = true;
			int count = 0;
			int poolType = Integer.valueOf((String) map.get("poolType"));
			String start = (String) map.get("formSDt");
			String end = (String) map.get("formEDt");
			String start1 = (String) map.get("bookOkSDt");
			String end1 = (String) map.get("bookOkEDt");
//			int pageNo = Integer.parseInt(map.get("pageNo").toString());
//			int pageSize = Integer.parseInt(map.get("pageSize").toString());

			if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
				if (!start.substring(0, 4).equals(end.substring(0, 4))
						|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
						|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
					message = "��֧�ֿ����ѯ";
					isSuccess = false;
					return BaseJson.returnRespListAnno("account/invty/sendProducts/pool", isSuccess, message, tabList);
				} else {
					map.put("start", start.substring(5, 7));
					map.put("end", end.substring(5, 7));
					map.put("year", start.substring(0, 4));
				}
			}

			if (StringUtils.isNotEmpty(start1) && StringUtils.isNotEmpty(end1)) {
				if (!start1.substring(0, 4).equals(end1.substring(0, 4))
						|| !start1.substring(0, 4).equals(loginTime.substring(0, 4))
						|| !start1.substring(0, 4).equals(loginTime.substring(0, 4))) {
					message = "��֧�ֿ����ѯ";
					isSuccess = false;
					return BaseJson.returnRespListAnno("account/invty/sendProducts/pool", isSuccess, message, tabList);
				}
			}
			// ���ղֿ����
			if (poolType == 1) {
				map.put("isWhsEncdPool", "whsEncd");

			} else if (poolType == 2) {
				// ���մ������
				map.put("isInvtyEncdPool", "invtyEncd");
			} else {
				// ���մ��+�ͻ�����
				map.put("isCustPool", "cust");
			}

			tabList = invtySendMthTermBgnTabDao.sendProductMthPool(map);
			count = invtySendMthTermBgnTabDao.countSendProductMthPool(map);

			resp = BaseJson.returnRespListAnno("account/invty/sendProducts/pool", true, "��ѯ�ɹ�",tabList);
			return resp;
		}

	// ������ͳ�Ʊ�-��ѯ
	@Override
	public String invoicingPool(Map map, String loginTime) throws Exception {
		int count = 0;
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		List<InvtyDetail> invtyList = new ArrayList<>();
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ����ѯ";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/invoiving/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, invtyList);
			}

			// ��ȡ��ʼ�·�
			String month = start.substring(5, 7);
			String year = start.substring(0, 4);
			map.put("month", Integer.valueOf(month));
			map.put("year", Integer.valueOf(year));
			// �������
			// �ڳ�����/���-�������
			invtyList = invtyDetailDao.selectBeginDataByMap(map);
			count = invtyDetailDao.selectBeginDataByMapCount(map);
			message = "��ѯ�ɹ�";
			if (invtyList.size() > 0) {
				Map<String, Object> dataMap = new HashMap<String, Object>();

				for (InvtyDetail invtyDetail : invtyList) {
					FormBookEntrySub sub = new FormBookEntrySub();
					dataMap = new HashMap<String, Object>();
					dataMap.put("formSDt", map.get("formSDt"));
					dataMap.put("formEDt", map.get("formEDt"));
					dataMap.put("year", year);
					dataMap.put("start", map.get("formSDt").toString().substring(5, 7));
					dataMap.put("end", map.get("formEDt").toString().substring(5, 7));
					// ��ѯ�����ϸ��
					dataMap.put("invtyEncd", invtyDetail.getInvtyEncd());
					// ��ʼ��invtyDetail
					invtyDetail = initInvtyDetail(invtyDetail);

					// �ڳ�����
					InvtyDetail fi = invtyDetailDao.sumQtyAndAmtByInvtyEncd(dataMap);
					if (fi != null) {
						invtyDetail.setAmt(fi.getAmt());
						invtyDetail.setQty(fi.getQty());
					} else {
						invtyDetail.setAmt(BigDecimal.ZERO);
						invtyDetail.setQty(BigDecimal.ZERO);
					}

					// �ɹ�����
					dataMap.put("outIntoTypeId", "9");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setPurcQty(sub.getQty());
						invtyDetail.setPurcAmt(sub.getNoTaxAmt());

					}
					// �ݹ�
					dataMap.put("isNtBllg", "0");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setPurcTempQty(sub.getQty());
						invtyDetail.setPurcTempAmt(sub.getNoTaxAmt());

					}
					dataMap.put("outIntoTypeId", "");
					dataMap.put("isNtBllg", "");
					// ���������
					dataMap.put("formTypeList", strToList("1,3,5,8,12"));
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthInQty(sub.getQty());
						invtyDetail.setOthInAmt(sub.getNoTaxAmt());

					}
					dataMap.put("formTypeList", strToList("2,4,6,7,11"));
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthOutQty(sub.getQty());
						invtyDetail.setOthOutAmt(sub.getNoTaxAmt());

					}
					// ��������-��Ҫȡ���۵�����-����
					// ��ѯ��Ʊ --���۽��ȡ��Ʊ���
					// ��������ȡ��Ʊδ˰���,���۽��ȡ��˰���
					SellComnInvSub sell = sellComnInvDao.sumSellQtyAndAmt(dataMap);
					if (sell != null) {
						invtyDetail.setSellInAmt(sell.getNoTaxAmt());// ��������
						invtyDetail.setSellAmt(sell.getPrcTaxSum()); // ���۽��
						invtyDetail.setOutQty(sell.getQty());// ��������
						invtyDetail.setSellQty(sell.getQty());// ��������
						// ��˰/��������
						if (invtyDetail.getSellQty().compareTo(BigDecimal.ZERO) != 0) {
							invtyDetail.setNoTaxUprc(invtyDetail.getSellInAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));
							invtyDetail.setCntnTaxUprc(invtyDetail.getSellAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));
						}
					}

					// ���۳ɱ�
					SellComnInvSub sel = sellComnInvDao.sumSellCost(dataMap);
					if (sel != null) {
						// ���۳ɱ�
						invtyDetail.setSellCost(sel.getNoTaxAmt());

					}
					// ë�����������룭���۳ɱ�
					invtyDetail.setGross(invtyDetail.getSellInAmt().subtract(invtyDetail.getSellCost()));
					// ë���ʣ�ë�������������100%
					if (invtyDetail.getSellInAmt().compareTo(BigDecimal.ZERO) != 0) {
						BigDecimal rate = new BigDecimal(0);
						String ra = invtyDetail.getGross()
								.divide(invtyDetail.getSellInAmt(), 8, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						invtyDetail.setGrossRate(ra + "%");
					}

					// ��ĩ����=�ڳ�+�ɹ�+�������-����-��������
					invtyDetail.setFinalAmt(
							invtyDetail.getAmt().add(invtyDetail.getPurcAmt()).add(invtyDetail.getOthInAmt())
									.subtract(invtyDetail.getSellCost()).subtract(invtyDetail.getOthOutAmt()));
					invtyDetail.setFinalQty(
							invtyDetail.getQty().add(invtyDetail.getPurcQty()).add(invtyDetail.getOthInQty())
									.subtract(invtyDetail.getOutQty()).subtract(invtyDetail.getOthOutQty()));

				}
			}
		}
		resp = BaseJson.returnRespList("account/invty/invoiving/pool", isSuccess, message, count, pageNo, pageSize, 0,
				0, invtyList);
		return resp;
	}

	// ������ͳ�Ʊ�-����
	@Override
	public String invoicingPoolExport(Map map, String loginTime) throws Exception {

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		List<InvtyDetail> invtyList = new ArrayList<>();
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ��굼��";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/invoiving/pool", isSuccess, message, invtyList);
			}

			// ��ȡ��ʼ�·�
			String month = start.substring(5, 7);
			String year = start.substring(0, 4);
			map.put("month", Integer.valueOf(month));
			map.put("year", Integer.valueOf(year));
			// �������
			// �ڳ�����/���-�������
			invtyList = invtyDetailDao.selectBeginDataByMap(map);

			message = "��ѯ�ɹ�";

			if (invtyList.size() > 0) {
				Map<String, Object> dataMap = new HashMap<String, Object>();

				for (InvtyDetail invtyDetail : invtyList) {// ����invtyDetail�����
					FormBookEntrySub sub = new FormBookEntrySub();
					dataMap = new HashMap<String, Object>();
					dataMap.put("formSDt", map.get("formSDt"));
					dataMap.put("formEDt", map.get("formEDt"));
					dataMap.put("year", year);
					dataMap.put("start", map.get("formSDt").toString().substring(5, 7));
					dataMap.put("end", map.get("formEDt").toString().substring(5, 7));
					// ��ѯ�����ϸ��
					dataMap.put("invtyEncd", invtyDetail.getInvtyEncd());// ��ϸ������,�Դ������Ϊkey
					// ��ʼ��invtyDetail
					invtyDetail = initInvtyDetail(invtyDetail);// Ϊnull��ֵΪ0

					// �ڳ�����
					InvtyDetail fi = invtyDetailDao.sumQtyAndAmtByInvtyEncd(dataMap);// �������ѯ�ڳ�����
					if (fi != null) {// �ڳ���Ϊ�� ��ֵ�����ϸbean
						invtyDetail.setAmt(fi.getAmt());
						invtyDetail.setQty(fi.getQty());
					} else {
						invtyDetail.setAmt(BigDecimal.ZERO);
						invtyDetail.setQty(BigDecimal.ZERO);
					}

					// �ɹ�����
					dataMap.put("outIntoTypeId", "9");// �ɹ���
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);// ����dataMap��ѯ�ɹ���/��������������
					if (sub != null) {
						invtyDetail.setPurcQty(sub.getQty());
						invtyDetail.setPurcAmt(sub.getNoTaxAmt());

					}
					// �ݹ�
					dataMap.put("isNtBllg", "0");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {// �ݹ�����,�ݹ����
						invtyDetail.setPurcTempQty(sub.getQty());
						invtyDetail.setPurcTempAmt(sub.getNoTaxAmt());

					}
					dataMap.put("outIntoTypeId", "");// ���������(�жϽ�������)
					dataMap.put("isNtBllg", "");
					// ���������
					dataMap.put("formTypeList", strToList("1,3,5,8,12"));// ��������,������
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthInQty(sub.getQty());
						invtyDetail.setOthInAmt(sub.getNoTaxAmt());

					}
					dataMap.put("formTypeList", strToList("2,4,6,7,11"));// ��������,������
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthOutQty(sub.getQty());
						invtyDetail.setOthOutAmt(sub.getNoTaxAmt());

					}
					// ��������-��Ҫȡ���۵�����-����
					// ��ѯ��Ʊ --���۽��ȡ��Ʊ���
					// ��������ȡ��Ʊδ˰���,���۽��ȡ��˰���
					SellComnInvSub sell = sellComnInvDao.sumSellQtyAndAmt(dataMap);
					if (sell != null) {
						invtyDetail.setSellInAmt(sell.getNoTaxAmt());// ��������
						invtyDetail.setSellAmt(sell.getPrcTaxSum()); // ���۽��
						invtyDetail.setOutQty(sell.getQty());// ��������
						invtyDetail.setSellQty(sell.getQty());// ��������
						// ��˰/��������
						if (invtyDetail.getSellQty() != null
								&& invtyDetail.getSellQty().compareTo(BigDecimal.ZERO) != 0) {
							invtyDetail.setNoTaxUprc(invtyDetail.getSellInAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));// ��˰����
							invtyDetail.setCntnTaxUprc(invtyDetail.getSellAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));// ��˰����
						}
					}

					// ���۳ɱ�
					SellComnInvSub sel = sellComnInvDao.sumSellCost(dataMap);
					if (sel != null) {
						// ���۳ɱ�
						invtyDetail.setSellCost(sel.getNoTaxAmt());

					}
					// ë�����������룭���۳ɱ�
					BigDecimal sellInAmt = Optional.ofNullable(invtyDetail.getSellInAmt())
							.orElse(new BigDecimal(0.00000000));
					BigDecimal sellCost = Optional.ofNullable(invtyDetail.getSellCost())
							.orElse(new BigDecimal(0.00000000));
					invtyDetail.setGross(sellInAmt.subtract(sellCost));
					// ë���ʣ�ë�������������100%
					if (invtyDetail.getSellInAmt() != null
							&& invtyDetail.getSellInAmt().compareTo(BigDecimal.ZERO) != 0) {
						BigDecimal rate = new BigDecimal(0);
						String ra = invtyDetail.getGross()
								.divide(invtyDetail.getSellInAmt(), 8, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						invtyDetail.setGrossRate(ra + "%");
					}

					// ��ĩ����=�ڳ�+�ɹ�+�������-����-��������
					invtyDetail.setFinalAmt(Optional.ofNullable(invtyDetail.getAmt()).orElse(new BigDecimal(0.00000000))
							.add(Optional.ofNullable(invtyDetail.getPurcAmt()).orElse(new BigDecimal(0.00000000)))
							.add(Optional.ofNullable(invtyDetail.getOthInAmt()).orElse(new BigDecimal(0.00000000)))
							.subtract(Optional.ofNullable(invtyDetail.getSellCost()).orElse(new BigDecimal(0.00000000)))
							.subtract(Optional.ofNullable(invtyDetail.getOthOutAmt())
									.orElse(new BigDecimal(0.00000000))));

					invtyDetail.setFinalQty(Optional.ofNullable(invtyDetail.getQty()).orElse(new BigDecimal(0.00000000))
							.add(Optional.ofNullable(invtyDetail.getPurcQty()).orElse(new BigDecimal(0.00000000)))
							.add(Optional.ofNullable(invtyDetail.getOthInQty()).orElse(new BigDecimal(0.00000000)))
							.subtract(Optional.ofNullable(invtyDetail.getOutQty()).orElse(new BigDecimal(0.00000000)))
							.subtract(Optional.ofNullable(invtyDetail.getOthOutQty())
									.orElse(new BigDecimal(0.00000000))));

				}
			}
		}
		ArrayList<JsonNode> nodeList = flattening(invtyList);
		resp = BaseJson.returnRespListAnno("account/invty/invoiving/poolExport", isSuccess, message, nodeList);
		return resp;
	}

	private InvtyDetail initInvtyDetail(InvtyDetail invtyDetail) {
		invtyDetail.setQty(Optional.ofNullable(invtyDetail.getQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setAmt(Optional.ofNullable(invtyDetail.getAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setPurcQty(Optional.ofNullable(invtyDetail.getPurcQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setPurcAmt(Optional.ofNullable(invtyDetail.getPurcAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail
				.setPurcTempQty(Optional.ofNullable(invtyDetail.getPurcTempQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail
				.setPurcTempAmt(Optional.ofNullable(invtyDetail.getPurcTempAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setOthInQty(Optional.ofNullable(invtyDetail.getOthInQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setOthInAmt(Optional.ofNullable(invtyDetail.getOthInAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setOthOutQty(Optional.ofNullable(invtyDetail.getOthOutQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setOthOutAmt(Optional.ofNullable(invtyDetail.getOthOutAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setSellQty(Optional.ofNullable(invtyDetail.getSellQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setSellAmt(Optional.ofNullable(invtyDetail.getSellAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setSellInAmt(Optional.ofNullable(invtyDetail.getSellInAmt()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setOutQty(Optional.ofNullable(invtyDetail.getOutQty()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setSellCost(Optional.ofNullable(invtyDetail.getSellCost()).orElse(new BigDecimal(0.00000000)));
		invtyDetail.setGross(Optional.ofNullable(invtyDetail.getGross()).orElse(new BigDecimal(0.00000000)));
		return invtyDetail;
	}

//�շ�����ܱ�
	@Override
	public String sendAndReceiveInvtyClsPool(Map map, String loginTime) throws Exception {
		// �շ�����
		List<InvtyDetail> itList = new ArrayList<>();
		List<InvtyDetail> dataList = new ArrayList<>();// ����������ݼ���
		List<InvtyDetails> itsList = new ArrayList<>();
		Map<String, List<InvtyDetail>> clsMap = new HashMap<>();
		Map<String, List<InvtyDetail>> clsOneMap = new HashMap<>();

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int count = 0;
		int poolType = Integer.valueOf((String) map.get("poolType"));
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ����ѯ";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceiveCls/pool", isSuccess, message, itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}

		// ���ղֿ����
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// �������λ���
			map.put("isBatNumPool", "batNum");

		}

		itList = invtyDetailDao.selectSendAndRecePool(map);
		count = invtyDetailDao.countSelectSendAndRecePool(map);
		if (itList.size() > 0) {

			for (InvtyDetail it : itList) {

				List<InvtyDetail> list = new ArrayList<>();

				if (clsMap.containsKey(it.getInvtyClsEncd())) {
					list = (List<InvtyDetail>) clsMap.get(it.getInvtyClsEncd());

				} else {
					list = new ArrayList<>();

				}
				list.add(it);
				clsMap.put(it.getInvtyClsEncd(), list);
			}

			dataList = groupByClsPool(clsMap, dataList, itsList, false);
			// ��ѯһ������

			for (InvtyDetail it : dataList) {
				InvtyCls cls = invtyClsDao.selectInvtyClsSuper(it.getInvtyClsEncd());
				if (cls != null) {
					List<InvtyDetail> list = new ArrayList<>();

					if (clsOneMap.containsKey(cls.getInvtyClsEncd())) {
						list = (List<InvtyDetail>) clsOneMap.get(cls.getInvtyClsEncd());
						list.add(it);
					} else {
						list = new ArrayList<>();
						list.add(it);
					}
					clsOneMap.put(cls.getInvtyClsEncd(), list);

				} else {
					message = "��ѯ����������ƣ�" + it.getInvtyClsNm() + "��Ӧ�ϼ��������ʧ��";
					isSuccess = false;
				}
			}
			if (isSuccess) {
				dataList = groupByClsSuperPool(clsOneMap, dataList, itsList, true);

			}

		}

		resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/pool", true, "��ѯ�ɹ�", dataList);
		return resp;

	}

	// �շ��������ܱ�-����
	@Override
	public String sendAndReceiveInvtyClsPoolExport(Map map, String loginTime) throws Exception {

		// �շ�����
		List<InvtyDetail> itList = new ArrayList<>();

		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		int poolType = Integer.valueOf((String) map.get("poolType"));
		String start = (String) map.get("formSDt");
		String end = (String) map.get("formEDt");

		if (StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
			if (!start.substring(0, 4).equals(end.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
					|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
				message = "��֧�ֿ��굼��";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceiveCls/poolExport", isSuccess, message,
						itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}
		// ���ղֿ����
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// �������λ���
			map.put("isBatNumPool", "batNum");

		}

		itList = invtyDetailDao.selectSendAndRecePool(map);// ��ѯ

		ArrayList<JsonNode> nodeList = flattening(itList);

		resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/poolExport", true, "��ѯ�ɹ�", nodeList);
		return resp;
	}

	private <T> ArrayList<JsonNode> flattening(List<T> itList) throws IOException {
		try {
			ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
			JacksonUtil.turnOnAnno();
			for (int i = 0; i < itList.size(); i++) {
				InvtyDetail invtyDetail = (InvtyDetail) itList.get(i);
				List<InvtyDetails> invtyDetailsList = invtyDetail.getInvtyDetailsList();

				for (InvtyDetails invtyDetails : invtyDetailsList) {
					invtyDetail.setInvtyDetailsList(null);
					ObjectNode bond = JacksonUtil.getObjectNode("");
					ObjectNode mainNode = JacksonUtil.getObjectNode(invtyDetail);
					ObjectNode subNode = JacksonUtil.getObjectNode(invtyDetails);
					bond.setAll(mainNode);
					bond.setAll(subNode);
					nodeList.add(bond);
				}
			}
			return nodeList;
		} finally {
			JacksonUtil.turnOffAnno();
		}
	}

	private List<InvtyDetail> groupByClsPool(Map<String, List<InvtyDetail>> clsMap, List<InvtyDetail> dataList,
			List<InvtyDetails> itsList, Boolean isSuper) {

		if (!clsMap.isEmpty()) {
			dataList = new ArrayList<>();
			for (Map.Entry<String, List<InvtyDetail>> key : clsMap.entrySet()) {

				List<InvtyDetail> list = (List<InvtyDetail>) key.getValue();

				InvtyDetail clsLevelTwo = new InvtyDetail();
				InvtyDetails clsLevelsTwo = new InvtyDetails();

				for (InvtyDetail it : list) {

					it.setAmt(Optional.ofNullable(it.getAmt()).orElse(new BigDecimal(0.00000000)));
					it.setQty(Optional.ofNullable(it.getQty()).orElse(new BigDecimal(0.00000000)));
					clsLevelTwo.setAmt(Optional.ofNullable(clsLevelTwo.getAmt()).orElse(new BigDecimal(0.00000000)));
					clsLevelTwo.setQty(Optional.ofNullable(clsLevelTwo.getQty()).orElse(new BigDecimal(0.00000000)));

					if (it.getQty().compareTo(BigDecimal.ZERO) != 0) {
						it.setUprc(it.getAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
					}
					clsLevelTwo.setQty(clsLevelTwo.getQty().add(it.getQty()));
					clsLevelTwo.setAmt(clsLevelTwo.getAmt().add(it.getAmt()));
					if (isSuper) {

						InvtyCls cls = invtyClsDao.selectInvtyClsSuper(it.getInvtyClsEncd());
						clsLevelTwo.setInvtyEncd(cls.getInvtyClsEncd());
						clsLevelTwo.setInvtyNm(cls.getInvtyClsNm());
						clsLevelTwo.setInvtyClsEncd(cls.getInvtyClsEncd());
						clsLevelTwo.setInvtyClsNm(cls.getInvtyClsNm());
						clsLevelTwo.setLevel(String.valueOf(cls.getLevel()));
					} else {
						clsLevelTwo.setInvtyEncd(it.getInvtyClsEncd());
						clsLevelTwo.setInvtyNm(it.getInvtyClsNm());
						clsLevelTwo.setInvtyClsEncd(it.getInvtyClsEncd());
						clsLevelTwo.setInvtyClsNm(it.getInvtyClsNm());
						clsLevelTwo.setLevel("2");
					}
					it.setLevel("3");

					if (it.getInvtyDetailsList().size() > 0) {
						for (InvtyDetails its : it.getInvtyDetailsList()) {

							its.setOthQty(Optional.ofNullable(its.getOthQty()).orElse(new BigDecimal(0.00000000)));
							its.setOthMoeny(Optional.ofNullable(its.getOthMoeny()).orElse(new BigDecimal(0.00000000)));
							its.setInMoeny(Optional.ofNullable(its.getInMoeny()).orElse(new BigDecimal(0.00000000)));
							its.setInQty(Optional.ofNullable(its.getInQty()).orElse(new BigDecimal(0.00000000)));
							its.setSendMoeny(
									Optional.ofNullable(its.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
							its.setSendQty(Optional.ofNullable(its.getSendQty()).orElse(new BigDecimal(0.00000000)));

							clsLevelsTwo.setOthQty(
									Optional.ofNullable(clsLevelsTwo.getOthQty()).orElse(new BigDecimal(0.00000000)));
							clsLevelsTwo.setOthMoeny(
									Optional.ofNullable(clsLevelsTwo.getOthMoeny()).orElse(new BigDecimal(0.00000000)));
							clsLevelsTwo.setInMoeny(
									Optional.ofNullable(clsLevelsTwo.getInMoeny()).orElse(new BigDecimal(0.00000000)));
							clsLevelsTwo.setInQty(
									Optional.ofNullable(clsLevelsTwo.getInQty()).orElse(new BigDecimal(0.00000000)));
							clsLevelsTwo.setSendMoeny(Optional.ofNullable(clsLevelsTwo.getSendMoeny())
									.orElse(new BigDecimal(0.00000000)));
							clsLevelsTwo.setSendQty(
									Optional.ofNullable(clsLevelsTwo.getSendQty()).orElse(new BigDecimal(0.00000000)));

							if (its.getSendQty().compareTo(BigDecimal.ZERO) != 0) {
								its.setSendUnitPrice(
										its.getSendMoeny().divide(its.getSendQty(), 8, BigDecimal.ROUND_HALF_UP));
							}
							its.setInQty(Optional.ofNullable(its.getInQty()).orElse(new BigDecimal(0.00000000)));
							if (its.getInQty().compareTo(BigDecimal.ZERO) != 0) {
								its.setInUnitPrice(
										its.getInMoeny().divide(its.getInQty(), 8, BigDecimal.ROUND_HALF_UP));
							}

							clsLevelsTwo.setInMoeny(clsLevelsTwo.getInMoeny().add(its.getInMoeny()));
							clsLevelsTwo.setInQty(clsLevelsTwo.getInQty().add(its.getInQty()));
							clsLevelsTwo.setSendMoeny(clsLevelsTwo.getSendMoeny().add(its.getSendMoeny()));
							clsLevelsTwo.setSendQty(clsLevelsTwo.getSendQty().add(its.getSendQty()));
							clsLevelsTwo.setOthMoeny(clsLevelsTwo.getOthMoeny().add(its.getOthMoeny()));
							clsLevelsTwo.setOthQty(clsLevelsTwo.getOthQty().add(its.getOthQty()));
							itsList = new ArrayList<>();
							itsList.add(clsLevelsTwo);
							clsLevelTwo.setInvtyDetailsList(itsList);
						}

					}
					dataList.add(it);
				}
				dataList.add(clsLevelTwo);
			}
		}
		return dataList;
	}

	private List<InvtyDetail> groupByClsSuperPool(Map<String, List<InvtyDetail>> clsOneMap, List<InvtyDetail> dataList,
			List<InvtyDetails> itsList, Boolean isSuper) {
		if (!clsOneMap.isEmpty()) {
			dataList = new ArrayList<>();
			for (Map.Entry<String, List<InvtyDetail>> key : clsOneMap.entrySet()) {

				List<InvtyDetail> list = (List<InvtyDetail>) key.getValue();

				InvtyDetail clsLevelTwo = new InvtyDetail();
				InvtyDetails clsLevelsTwo = new InvtyDetails();

				for (InvtyDetail it : list) {

					it.setAmt(Optional.ofNullable(it.getAmt()).orElse(new BigDecimal(0.00000000)));
					it.setQty(Optional.ofNullable(it.getQty()).orElse(new BigDecimal(0.00000000)));
					clsLevelTwo.setAmt(Optional.ofNullable(clsLevelTwo.getAmt()).orElse(new BigDecimal(0.00000000)));
					clsLevelTwo.setQty(Optional.ofNullable(clsLevelTwo.getQty()).orElse(new BigDecimal(0.00000000)));
					if (it.getLevel().equals("3")) {
						dataList.add(it);
					}
					if (it.getLevel().equals("2")) {

						clsLevelTwo.setQty(clsLevelTwo.getQty().add(it.getQty()));
						clsLevelTwo.setAmt(clsLevelTwo.getAmt().add(it.getAmt()));
						InvtyCls cls = invtyClsDao.selectInvtyClsSuper(it.getInvtyClsEncd());
						clsLevelTwo.setInvtyEncd(cls.getInvtyClsEncd());
						clsLevelTwo.setInvtyNm(cls.getInvtyClsNm());
						clsLevelTwo.setInvtyClsEncd(cls.getInvtyClsEncd());
						clsLevelTwo.setInvtyClsNm(cls.getInvtyClsNm());
						clsLevelTwo.setLevel("1");
						
						if (it.getInvtyDetailsList().size() > 0) {
							for (InvtyDetails its : it.getInvtyDetailsList()) {

								its.setOthQty(Optional.ofNullable(its.getOthQty()).orElse(new BigDecimal(0.00000000)));
								its.setOthMoeny(
										Optional.ofNullable(its.getOthMoeny()).orElse(new BigDecimal(0.00000000)));
								its.setInMoeny(
										Optional.ofNullable(its.getInMoeny()).orElse(new BigDecimal(0.00000000)));
								its.setInQty(Optional.ofNullable(its.getInQty()).orElse(new BigDecimal(0.00000000)));
								its.setSendMoeny(
										Optional.ofNullable(its.getSendMoeny()).orElse(new BigDecimal(0.00000000)));
								its.setSendQty(
										Optional.ofNullable(its.getSendQty()).orElse(new BigDecimal(0.00000000)));

								clsLevelsTwo.setOthQty(Optional.ofNullable(clsLevelsTwo.getOthQty())
										.orElse(new BigDecimal(0.00000000)));
								clsLevelsTwo.setOthMoeny(Optional.ofNullable(clsLevelsTwo.getOthMoeny())
										.orElse(new BigDecimal(0.00000000)));
								clsLevelsTwo.setInMoeny(Optional.ofNullable(clsLevelsTwo.getInMoeny())
										.orElse(new BigDecimal(0.00000000)));
								clsLevelsTwo.setInQty(Optional.ofNullable(clsLevelsTwo.getInQty())
										.orElse(new BigDecimal(0.00000000)));
								clsLevelsTwo.setSendMoeny(Optional.ofNullable(clsLevelsTwo.getSendMoeny())
										.orElse(new BigDecimal(0.00000000)));
								clsLevelsTwo.setSendQty(Optional.ofNullable(clsLevelsTwo.getSendQty())
										.orElse(new BigDecimal(0.00000000)));

								clsLevelsTwo.setInMoeny(clsLevelsTwo.getInMoeny().add(its.getInMoeny()));
								clsLevelsTwo.setInQty(clsLevelsTwo.getInQty().add(its.getInQty()));
								clsLevelsTwo.setSendMoeny(clsLevelsTwo.getSendMoeny().add(its.getSendMoeny()));
								clsLevelsTwo.setSendQty(clsLevelsTwo.getSendQty().add(its.getSendQty()));
								clsLevelsTwo.setOthMoeny(clsLevelsTwo.getOthMoeny().add(its.getOthMoeny()));
								clsLevelsTwo.setOthQty(clsLevelsTwo.getOthQty().add(its.getOthQty()));
								itsList = new ArrayList<>();
								itsList.add(clsLevelsTwo);
								clsLevelTwo.setInvtyDetailsList(itsList);
							}

						}
						dataList.add(it);
					}

				}
				
				dataList.add(clsLevelTwo);
			}
		}
		return dataList;
	}

}
