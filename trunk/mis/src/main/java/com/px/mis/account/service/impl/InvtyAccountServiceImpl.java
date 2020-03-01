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
	private FormBookEntryDao formBookDao; // 明细表主表
	@Autowired
	private FormBookEntrySubDao formBookSubDao; // 明细表子表
	@Autowired
	private MthTermBgnTabDao mthTermBgnTabDao; // 期初结存
	@Autowired
	private InvtyDetailDao invtyDetailDao; // 存货明细帐
	@Autowired
	private IntoWhsDao intoWhsDao; // 采购入库单
	@Autowired
	private OthOutIntoWhsMapper othOutDao; // 其他出入库单
	@Autowired
	private SellOutWhsDao sellOutWhsDao; // 销售出库单
	@Autowired
	private InvtyMthTermBgnTabDao invtyMthTermBgnTabDao; // 核算各月期初
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; // 发出商品各月期初
	@Autowired
	private FormBookServiceImpl formBookImpl;
	@Autowired
	private InvtyClsDao invtyClsDao; // 存货分类
	@Autowired
	private SellComnInvDao sellComnInvDao; // 发票

	@Override
	public String selectDetailList(Map map, String loginTime) throws IllegalAccessException, InvocationTargetException {
		// 明细表
		// 查询条件 存货分类,存货编码多个,记账日期,存货科目,批号
		String resp = "";
		String message = "";
		Boolean isSuccess = true;

		String invIds = (String) map.get("invtyEncd"); // 存货编码多个
		List<String> invIdList = new ArrayList<>();
		invIdList = strToList(invIds);
		map.put("invIdList", invIdList);

		// 记账日期不能跨年
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");
		List<InvtyDetail> dataList = new ArrayList<>();
		InvtyDetail invtyDetail = new InvtyDetail();

		if (!start.substring(0, 4).equals(end.substring(0, 4))) {
			message = "不支持跨年查询";
			isSuccess = false;
		} else {
			int num = Integer.valueOf(end.substring(5, 7)) - Integer.valueOf(start.substring(5, 7));
			if (num < 0) {
				message = "查询日期有误";
				isSuccess = false;
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

				dataList = selectFormDetailByMonth(map);
				if (dataList.size() > 0) {
					invtyDetail = dataList.get(0);
				}
				message = "查询成功!";
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

	// 明细账-导出
	@Override
	public String selectDetailedListExport(Map map, String loginTime) throws Exception {
		// 明细表
		// 查询条件 存货分类,存货编码多个,记账日期,存货科目,批号
		String resp = "";
		String message = "";
		Boolean isSuccess = true;

		String invIds = (String) map.get("invtyEncd"); // 存货编码多个
		List<String> invIdList = new ArrayList<>();
		invIdList = strToList(invIds);
		map.put("invIdList", invIdList);

		// 记账日期不能跨年
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");
		List<InvtyDetail> dataList = new ArrayList<>();
		InvtyDetail invtyDetail = new InvtyDetail();

		if (!start.substring(0, 4).equals(end.substring(0, 4))) {
			message = "不支持跨年导出";
			isSuccess = false;
		} else {
			int num = Integer.valueOf(end.substring(5, 7)) - Integer.valueOf(start.substring(5, 7));
			if (num < 0) {
				message = "查询日期有误";
				isSuccess = false;
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

				dataList = selectFormDetailByMonth(map);
				if (dataList.size() > 0) {
					invtyDetail = dataList.get(0);
				}
				message = "查询成功!";
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
		// 明细帐-收发存汇总表跳转
		String resp = "";
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		Map<String, Object> paramMap = new HashMap<>();
		InvtyDetail allDetail = new InvtyDetail();
		InvtyDetails itBegin = new InvtyDetails(); // 期初结存数据
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

				resp = BaseJson.returnRespObj(url, false, "不支持跨年查询", allDetail);
				return resp;
			}
		}

		// 查询库存期初
		List<InvtyMthTermBgnTab> tabs = invtyMthTermBgnTabDao.selectMthTermByInvty(map);
		if (tabs.size() > 0) {
			// 划分各月期初数据
			tabMap = initInvtyBgnTab(tabs, tabMap);

			if (!tabMap.isEmpty()) {

				for (Map.Entry<String, List<InvtyMthTermBgnTab>> key : tabMap.entrySet()) {

					detailsList = new ArrayList<>();
					List<InvtyMthTermBgnTab> tabValue = (List<InvtyMthTermBgnTab>) key.getValue();
					itBegin = new InvtyDetails("期初结存", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
							tabValue.get(0).getAmt());
					detailsList.add(itBegin);

					for (InvtyMthTermBgnTab tab : tabValue) {
						BeanUtils.copyProperties(allDetail, tab);

						month = new InvtyDetails();
						month.setMakeVouchAbst(tab.getAcctiMth() + "月合计");
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
				itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				detailsList.add(itBegin);
			}

		} else {
			itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00), new BigDecimal(0.00));
			detailsList.add(itBegin);
		}

		allDetail.setInvtyDetailsList(detailsList);

		resp = BaseJson.returnRespObj(url, true, "查询成功", allDetail);
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

			its = new InvtyDetails("期初结存", tabs.get(0).getUprc(), tabs.get(0).getQty(), tabs.get(0).getAmt());
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
				its = new InvtyDetails("期初结存", mthTabs.get(0).getUprc(), mthTabs.get(0).getQty(),
						mthTabs.get(0).getAmt());
				BeanUtils.copyProperties(it, mthTabs.get(0));
				detailList.add(its);
				for (MthTermBgnTab mthTab : mthTabs) {

					detailList = getInvtyDetailList(null, mthTab, detailList);
					it.setInvtyDetailsList(detailList);

				}
				dataList.add(it);
			} else {
				its = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00), new BigDecimal(0.00));
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

				month.setMakeVouchAbst(tab.getAcctiMth() + "月合计");
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

//发出商品明细账-查询
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

		// 查询仓库存货明细 存货分类/存货编码/记账日期/客户/批次
		// 记账日期不能跨年
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		if (!start.substring(0, 4).equals(end.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
			message = "不支持跨年查询";
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
						itBegin = new InvtyDetails("期初结存", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
								tabValue.get(0).getAmt());
						itList.add(itBegin);

						for (InvtyMthTermBgnTab tab : tabValue) {

							BeanUtils.copyProperties(allDetail, tab);

							month = new InvtyDetails();
							month.setMakeVouchAbst(tab.getAcctiMth() + "月合计");
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
							// 查询本月的销售出库单和对应发票

							List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap); // 收入
							List<InvtyDetails> sellList = sellComnInvDao.selectSellComnDetailList(paramMap);// 发出
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
					// map是空
					itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00),
							new BigDecimal(0.00));
					itList.add(itBegin);
				}
			} else {

				// 存货为空
				itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				itList.add(itBegin);
			}
		}
		allDetail.setInvtyDetailsList(itList);
		message = "查询成功";
		isSuccess = true;
		resp = BaseJson.returnRespObj("/account/invtyDtlAcct/selectInvtyDtlAcct", isSuccess, message, allDetail);

		return resp;
	}

	// 发出商品明细账-导出
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

		// 查询仓库存货明细 存货分类/存货编码/记账日期/客户/批次
		// 记账日期不能跨年
		String start = (String) map.get("bookOkSDt");
		String end = (String) map.get("bookOkEDt");

		if (!start.substring(0, 4).equals(end.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))
				|| !start.substring(0, 4).equals(loginTime.substring(0, 4))) {
			message = "不支持跨年查询";
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
						itBegin = new InvtyDetails("期初结存", tabValue.get(0).getUprc(), tabValue.get(0).getQty(),
								tabValue.get(0).getAmt());
						itList.add(itBegin);

						for (InvtyMthTermBgnTab tab : tabValue) {

							BeanUtils.copyProperties(allDetail, tab);

							month = new InvtyDetails();
							month.setMakeVouchAbst(tab.getAcctiMth() + "月合计");
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
							// 查询本月的销售出库单和对应发票

							List<InvtyDetail> detailList = invtyDetailDao.selectByInvty(paramMap); // 收入
							List<InvtyDetails> sellList = sellComnInvDao.selectSellComnDetailList(paramMap);// 发出
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
					// map是空
					itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00),
							new BigDecimal(0.00));
					itList.add(itBegin);
				}
			} else {

				// 存货为空
				itBegin = new InvtyDetails("期初结存", new BigDecimal(0.000000), new BigDecimal(0.00),
						new BigDecimal(0.00));
				itList.add(itBegin);
			}

		}
		allDetail.setInvtyDetailsList(itList);
		message = "查询成功";
		isSuccess = true;
		ArrayList<InvtyDetail> list = new ArrayList<InvtyDetail>();
		list.add(allDetail);// 规范入参
		ArrayList<JsonNode> nodeList = flattening(list);// 返回扁平json

		resp = BaseJson.returnRespList("account/invty/sendProduct/listExport", isSuccess, message, nodeList);

		return resp;
	}

	/**
	 * init各月期初的存货数据
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

	// 流水账-查询
	@Override
	public String selectStreamList(Map map, String loginTime) throws Exception {
		// 流水帐
		// 已记账/未记账 仓库,存货编码,单据日期
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
					// 收入
					if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {

						sub.setInAmt(sub.getNoTaxAmt());
						sub.setInQty(sub.getQty());
						sub.setInUprc(sub.getNoTaxUprc());
					}
					// 发出
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

		resp = BaseJson.returnRespList("mis/account/invty/stream/list", true, "查询成功", count, pageNo, pageSize, 0, 0,
				dataList);
		return resp;
	}

	// 流水账-导出
	@Override
	public String selectStreamListExport(Map map, String loginTime) throws Exception {
		// 流水帐
		// 已记账/未记账 仓库,存货编码,单据日期
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
		if (isNtBookOk.equals("0")) {// 未记账

			dataList = formBookDao.selectStreamALLList(map);// 采购入+销售出+其他出入
			if (dataList.size() > 0) {

				for (FormBookEntry form : dataList) {
					if (form.getBookEntrySub().size() > 0) {
						for (FormBookEntrySub sub : form.getBookEntrySub()) {
							if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {
								// 取每一条子表遍历,判断入库类型
								sub.setInAmt(sub.getNoTaxAmt());
								sub.setInQty(sub.getQty());
								sub.setInUprc(sub.getNoTaxUprc());
							}
							if (strToList("2,4,6,7,10,11").contains(form.getOutIntoWhsTypId())) {
								// 每条子表明细判断出库类型
								sub.setSendAmt(sub.getNoTaxAmt());
								sub.setSendQty(sub.getQty());
								sub.setSendUprc(sub.getNoTaxUprc());
							}
						}
					}
				}
			}

		} else {// 查询已记账
			dataList = formBookDao.selectStreamMap(map);
//			count = formBookDao.countSelectStreamMap(map);
		}

		if (dataList.size() > 0) {
			for (FormBookEntry form : dataList) {

				for (FormBookEntrySub sub : form.getBookEntrySub()) {
					// 收入
					if (strToList("1,3,5,8,9,12").contains(form.getOutIntoWhsTypId())) {

						sub.setInAmt(sub.getNoTaxAmt());
						sub.setInQty(sub.getQty());
						sub.setInUprc(sub.getNoTaxUprc());
					}
					// 发出
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
		resp = BaseJson.returnRespList("/account/invty/stream/listExport", true, "查询成功", nodeList);
		return resp;

	}

	/**
	 * 查询单据号是否存在
	 */
	private Boolean selectByFormNum(String formNum) {
		FormBookEntry formBook = formBookDao.selectByFormNum(formNum);
		if (formBook == null) {
			return false;
		}
		return true;
	}

	/**
	 * 字符串转list
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
	 * 计算明细
	 */
	private Map<String, Object> countDetail(int count, MthTermBgnTab tab, Map map, String start, String end,
			List<InvtyDetails> invtyDetailsList, InvtyDetail detail, int month)
			throws IllegalAccessException, InvocationTargetException {
		List<FormBookEntrySub> inList = new ArrayList<>(); // 收入单据集合
		List<FormBookEntrySub> outList = new ArrayList<>(); // 发出单据集合
		Map<String, Object> dataMap = new HashMap<>();

		BigDecimal sendQtyYear = new BigDecimal(0); // 年
		BigDecimal sendMoenyYear = new BigDecimal(0.00);
		BigDecimal inQtyYear = new BigDecimal(0);
		BigDecimal inMoenyYear = new BigDecimal(0.00);

		BigDecimal inQty = new BigDecimal(0); // 月
		BigDecimal inMoeny = new BigDecimal(0.00);
		BigDecimal sendQty = new BigDecimal(0);
		BigDecimal sendMoeny = new BigDecimal(0.00);
		BigDecimal othUnitPrice = new BigDecimal(0.00);
		BigDecimal othQty = new BigDecimal(0);
		BigDecimal othMoeny = new BigDecimal(0.00);
		int inCount = 0;
		int outCount = 0;
		InvtyDetails it = new InvtyDetails(); // 期初

		if (tab == null) {

			it = new InvtyDetails("期初结存", new BigDecimal(0.00), new BigDecimal(0), new BigDecimal(0.00));

		} else {
			String whsCode = tab.getWhsEncd();
			map.put("whsCode", whsCode);
			it = new InvtyDetails("期初结存", tab.getUprc(), tab.getQty(), tab.getAmt());
		}
		invtyDetailsList.add(it);

		List<String> inIdList = new ArrayList<>();
		inIdList = strToList("1,3,5,8,9,12");
		map.put("inWhsTyp", inIdList);

		map.put("bookOkSDt", start);
		map.put("bookOkEDt", end);

		inCount = formBookSubDao.selectDetailCount(map);
		inList = formBookSubDao.selectDetailList(map);
		// 发出 2,4,6,7,10,11;
		List<String> outIdList = new ArrayList<>();

		outIdList = strToList("2,4,6,7,10,11");

		inIdList.clear();
		map.put("inWhsTyp", inIdList);
		map.put("outWhsTyp", outIdList);

		outCount = formBookSubDao.selectDetailCount(map);
		outList = formBookSubDao.selectDetailList(map);
		count = inCount + outCount;

		// 收入
		for (FormBookEntrySub in : inList) {
			InvtyDetails itIn = new InvtyDetails();

			BeanUtils.copyProperties(itIn, in);
			itIn.setInUnitPrice(in.getNoTaxUprc());
			itIn.setInQty(in.getBxQty());
			itIn.setInMoeny(in.getNoTaxAmt());
			itIn.setSendQty(new BigDecimal(0));

			// 结算 = 期初+收入-发出
			itIn.setOthQty(it.getOthQty().add(itIn.getInQty()));
			itIn.setOthUnitPrice(in.getNoTaxUprc());
			itIn.setOthMoeny(it.getOthMoeny().add(itIn.getInMoeny()));

			inQty = inQty.add(itIn.getInQty()); // 本月收入数量
			inMoeny = inMoeny.add(itIn.getInMoeny()); // 本月收入金额
			othUnitPrice = othUnitPrice.add(in.getNoTaxUprc()); // 单价和

			invtyDetailsList.add(itIn);

		}
		// 发出
		for (FormBookEntrySub out : outList) {
			InvtyDetails itOut = new InvtyDetails();

			BeanUtils.copyProperties(itOut, out);
			itOut.setSendUnitPrice(out.getNoTaxUprc());
			itOut.setSendQty(out.getBxQty());
			itOut.setSendMoeny(out.getNoTaxAmt());

			// 结算 = 期初-发出
			itOut.setOthQty(it.getOthQty().subtract(itOut.getSendQty()));
			itOut.setOthUnitPrice(out.getNoTaxUprc());
			itOut.setOthMoeny(it.getOthMoeny().subtract(itOut.getSendMoeny()));

			sendQty = sendQty.add(itOut.getSendQty()); // 本月发出数量
			sendMoeny = sendMoeny.add(itOut.getSendMoeny()); // 本月发出金额
			othUnitPrice = othUnitPrice.add(out.getNoTaxUprc()); // 单价和

			invtyDetailsList.add(itOut);

		}
		inQtyYear = inQtyYear.add(inQty);
		inMoenyYear = inMoenyYear.add(inMoeny);
		sendQtyYear = sendQtyYear.add(sendQty);
		sendMoenyYear = sendMoenyYear.add(sendMoeny);
		BigDecimal de = new BigDecimal(1);
		if (outList.size() > 0 && inList.size() > 0) {
			de = new BigDecimal(outList.size() + inList.size());

			othUnitPrice = othUnitPrice.divide(de, 6, BigDecimal.ROUND_HALF_UP); // 求结算单价

			InvtyDetails itM = new InvtyDetails(month + "月合计", inQty, inMoeny, sendQty, sendMoeny, othUnitPrice, othQty,
					othMoeny);

			invtyDetailsList.add(itM);
		}

		dataMap.put("invtyDetailsList", invtyDetailsList);
		dataMap.put("sendQtyYear", sendQtyYear); // 年
		dataMap.put("sendMoenyYear", sendMoenyYear);
		dataMap.put("inQtyYear", inQtyYear);
		dataMap.put("inMoenyYear", inMoenyYear);
		dataMap.put("inQty", inQty); // 月
		dataMap.put("inMoeny", inMoeny);
		dataMap.put("sendQty", sendQty);
		dataMap.put("sendMoeny", sendMoeny);
		dataMap.put("othUnitPrice", othUnitPrice);
		dataMap.put("othQty", othQty);
		dataMap.put("othMoeny", othMoeny);
		dataMap.put("count", count); //
		dataMap.put("outCount", outCount); // 发
		dataMap.put("inList", inList);
		dataMap.put("outList", outList);
		return dataMap;
	}

	/**
	 * 获取两个时间的天差
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
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	private static String getFristDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int firstDay = cal.getActualMinimum(Calendar.DATE);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// 格式化日期
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
				message = "不支持跨年查询";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceive/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}

		// 按照仓库汇总
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// 按照批次汇总
			map.put("isBatNumPool", "batNum");

		} else {
			// 按照存货分类汇总
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

		resp = BaseJson.returnRespList("account/invty/sendAndReceive/pool", true, "查询成功", count, pageNo, pageSize, 0, 0,
				itList);
		return resp;

	}

//发出商品汇总表-查询
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
				message = "不支持跨年查询";
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
				message = "不支持跨年查询";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendProducts/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, tabList);
			}
		}
		// 按照仓库汇总
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// 按照存货汇总
			map.put("isInvtyEncdPool", "invtyEncd");
		} else {
			// 按照存货+客户汇总
			map.put("isCustPool", "cust");
		}

		tabList = invtySendMthTermBgnTabDao.sendProductMthPool(map);
		count = invtySendMthTermBgnTabDao.countSendProductMthPool(map);

		resp = BaseJson.returnRespList("account/invty/sendProducts/pool", true, "查询成功", count, pageNo, pageSize, 0, 0,
				tabList);
		return resp;
	}

	//发出商品汇总表-导出
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
					message = "不支持跨年查询";
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
					message = "不支持跨年查询";
					isSuccess = false;
					return BaseJson.returnRespListAnno("account/invty/sendProducts/pool", isSuccess, message, tabList);
				}
			}
			// 按照仓库汇总
			if (poolType == 1) {
				map.put("isWhsEncdPool", "whsEncd");

			} else if (poolType == 2) {
				// 按照存货汇总
				map.put("isInvtyEncdPool", "invtyEncd");
			} else {
				// 按照存货+客户汇总
				map.put("isCustPool", "cust");
			}

			tabList = invtySendMthTermBgnTabDao.sendProductMthPool(map);
			count = invtySendMthTermBgnTabDao.countSendProductMthPool(map);

			resp = BaseJson.returnRespListAnno("account/invty/sendProducts/pool", true, "查询成功",tabList);
			return resp;
		}

	// 进销存统计表-查询
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
				message = "不支持跨年查询";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/invoiving/pool", isSuccess, message, count, pageNo,
						pageSize, 0, 0, invtyList);
			}

			// 获取起始月份
			String month = start.substring(5, 7);
			String year = start.substring(0, 4);
			map.put("month", Integer.valueOf(month));
			map.put("year", Integer.valueOf(year));
			// 存货编码
			// 期初数量/金额-核算带入
			invtyList = invtyDetailDao.selectBeginDataByMap(map);
			count = invtyDetailDao.selectBeginDataByMapCount(map);
			message = "查询成功";
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
					// 查询存货明细帐
					dataMap.put("invtyEncd", invtyDetail.getInvtyEncd());
					// 初始化invtyDetail
					invtyDetail = initInvtyDetail(invtyDetail);

					// 期初数据
					InvtyDetail fi = invtyDetailDao.sumQtyAndAmtByInvtyEncd(dataMap);
					if (fi != null) {
						invtyDetail.setAmt(fi.getAmt());
						invtyDetail.setQty(fi.getQty());
					} else {
						invtyDetail.setAmt(BigDecimal.ZERO);
						invtyDetail.setQty(BigDecimal.ZERO);
					}

					// 采购数量
					dataMap.put("outIntoTypeId", "9");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setPurcQty(sub.getQty());
						invtyDetail.setPurcAmt(sub.getNoTaxAmt());

					}
					// 暂估
					dataMap.put("isNtBllg", "0");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setPurcTempQty(sub.getQty());
						invtyDetail.setPurcTempAmt(sub.getNoTaxAmt());

					}
					dataMap.put("outIntoTypeId", "");
					dataMap.put("isNtBllg", "");
					// 其他出入库
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
					// 调拨数量-需要取销售调拨单-暂无
					// 查询发票 --销售金额取发票金额
					// 销售收入取发票未税金额,销售金额取含税金额
					SellComnInvSub sell = sellComnInvDao.sumSellQtyAndAmt(dataMap);
					if (sell != null) {
						invtyDetail.setSellInAmt(sell.getNoTaxAmt());// 销售收入
						invtyDetail.setSellAmt(sell.getPrcTaxSum()); // 销售金额
						invtyDetail.setOutQty(sell.getQty());// 出库数量
						invtyDetail.setSellQty(sell.getQty());// 销售数量
						// 含税/不含均价
						if (invtyDetail.getSellQty().compareTo(BigDecimal.ZERO) != 0) {
							invtyDetail.setNoTaxUprc(invtyDetail.getSellInAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));
							invtyDetail.setCntnTaxUprc(invtyDetail.getSellAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));
						}
					}

					// 销售成本
					SellComnInvSub sel = sellComnInvDao.sumSellCost(dataMap);
					if (sel != null) {
						// 销售成本
						invtyDetail.setSellCost(sel.getNoTaxAmt());

					}
					// 毛利＝销售收入－销售成本
					invtyDetail.setGross(invtyDetail.getSellInAmt().subtract(invtyDetail.getSellCost()));
					// 毛利率＝毛利÷销售收入×100%
					if (invtyDetail.getSellInAmt().compareTo(BigDecimal.ZERO) != 0) {
						BigDecimal rate = new BigDecimal(0);
						String ra = invtyDetail.getGross()
								.divide(invtyDetail.getSellInAmt(), 8, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						invtyDetail.setGrossRate(ra + "%");
					}

					// 期末数量=期初+采购+其他入库-出库-其他出库
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

	// 进销存统计表-导出
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
				message = "不支持跨年导出";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/invoiving/pool", isSuccess, message, invtyList);
			}

			// 获取起始月份
			String month = start.substring(5, 7);
			String year = start.substring(0, 4);
			map.put("month", Integer.valueOf(month));
			map.put("year", Integer.valueOf(year));
			// 存货编码
			// 期初数量/金额-核算带入
			invtyList = invtyDetailDao.selectBeginDataByMap(map);

			message = "查询成功";

			if (invtyList.size() > 0) {
				Map<String, Object> dataMap = new HashMap<String, Object>();

				for (InvtyDetail invtyDetail : invtyList) {// 遍历invtyDetail结果集
					FormBookEntrySub sub = new FormBookEntrySub();
					dataMap = new HashMap<String, Object>();
					dataMap.put("formSDt", map.get("formSDt"));
					dataMap.put("formEDt", map.get("formEDt"));
					dataMap.put("year", year);
					dataMap.put("start", map.get("formSDt").toString().substring(5, 7));
					dataMap.put("end", map.get("formEDt").toString().substring(5, 7));
					// 查询存货明细帐
					dataMap.put("invtyEncd", invtyDetail.getInvtyEncd());// 明细账数据,以存货编码为key
					// 初始化invtyDetail
					invtyDetail = initInvtyDetail(invtyDetail);// 为null赋值为0

					// 期初数据
					InvtyDetail fi = invtyDetailDao.sumQtyAndAmtByInvtyEncd(dataMap);// 按存货查询期初数据
					if (fi != null) {// 期初不为空 赋值存货明细bean
						invtyDetail.setAmt(fi.getAmt());
						invtyDetail.setQty(fi.getQty());
					} else {
						invtyDetail.setAmt(BigDecimal.ZERO);
						invtyDetail.setQty(BigDecimal.ZERO);
					}

					// 采购数量
					dataMap.put("outIntoTypeId", "9");// 采购订
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);// 根据dataMap查询采购入/其他入库数量金额
					if (sub != null) {
						invtyDetail.setPurcQty(sub.getQty());
						invtyDetail.setPurcAmt(sub.getNoTaxAmt());

					}
					// 暂估
					dataMap.put("isNtBllg", "0");
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {// 暂估数量,暂估金额
						invtyDetail.setPurcTempQty(sub.getQty());
						invtyDetail.setPurcTempAmt(sub.getNoTaxAmt());

					}
					dataMap.put("outIntoTypeId", "");// 出入库类型(判断借出出库等)
					dataMap.put("isNtBllg", "");
					// 其他出入库
					dataMap.put("formTypeList", strToList("1,3,5,8,12"));// 单据类型,其他入
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthInQty(sub.getQty());
						invtyDetail.setOthInAmt(sub.getNoTaxAmt());

					}
					dataMap.put("formTypeList", strToList("2,4,6,7,11"));// 单据类型,其他出
					sub = formBookDao.sumFormBookQtyAndAmt(dataMap);
					if (sub != null) {
						invtyDetail.setOthOutQty(sub.getQty());
						invtyDetail.setOthOutAmt(sub.getNoTaxAmt());

					}
					// 调拨数量-需要取销售调拨单-暂无
					// 查询发票 --销售金额取发票金额
					// 销售收入取发票未税金额,销售金额取含税金额
					SellComnInvSub sell = sellComnInvDao.sumSellQtyAndAmt(dataMap);
					if (sell != null) {
						invtyDetail.setSellInAmt(sell.getNoTaxAmt());// 销售收入
						invtyDetail.setSellAmt(sell.getPrcTaxSum()); // 销售金额
						invtyDetail.setOutQty(sell.getQty());// 出库数量
						invtyDetail.setSellQty(sell.getQty());// 销售数量
						// 含税/不含均价
						if (invtyDetail.getSellQty() != null
								&& invtyDetail.getSellQty().compareTo(BigDecimal.ZERO) != 0) {
							invtyDetail.setNoTaxUprc(invtyDetail.getSellInAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));// 无税均价
							invtyDetail.setCntnTaxUprc(invtyDetail.getSellAmt().divide(invtyDetail.getSellQty(), 8,
									BigDecimal.ROUND_HALF_UP));// 含税均价
						}
					}

					// 销售成本
					SellComnInvSub sel = sellComnInvDao.sumSellCost(dataMap);
					if (sel != null) {
						// 销售成本
						invtyDetail.setSellCost(sel.getNoTaxAmt());

					}
					// 毛利＝销售收入－销售成本
					BigDecimal sellInAmt = Optional.ofNullable(invtyDetail.getSellInAmt())
							.orElse(new BigDecimal(0.00000000));
					BigDecimal sellCost = Optional.ofNullable(invtyDetail.getSellCost())
							.orElse(new BigDecimal(0.00000000));
					invtyDetail.setGross(sellInAmt.subtract(sellCost));
					// 毛利率＝毛利÷销售收入×100%
					if (invtyDetail.getSellInAmt() != null
							&& invtyDetail.getSellInAmt().compareTo(BigDecimal.ZERO) != 0) {
						BigDecimal rate = new BigDecimal(0);
						String ra = invtyDetail.getGross()
								.divide(invtyDetail.getSellInAmt(), 8, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
						invtyDetail.setGrossRate(ra + "%");
					}

					// 期末数量=期初+采购+其他入库-出库-其他出库
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

//收发存汇总表
	@Override
	public String sendAndReceiveInvtyClsPool(Map map, String loginTime) throws Exception {
		// 收发分类
		List<InvtyDetail> itList = new ArrayList<>();
		List<InvtyDetail> dataList = new ArrayList<>();// 处理汇总数据集合
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
				message = "不支持跨年查询";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceiveCls/pool", isSuccess, message, itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}

		// 按照仓库汇总
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// 按照批次汇总
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
			// 查询一级分类

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
					message = "查询存货分类名称：" + it.getInvtyClsNm() + "对应上级存货分类失败";
					isSuccess = false;
				}
			}
			if (isSuccess) {
				dataList = groupByClsSuperPool(clsOneMap, dataList, itsList, true);

			}

		}

		resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/pool", true, "查询成功", dataList);
		return resp;

	}

	// 收发存分类汇总表-导出
	@Override
	public String sendAndReceiveInvtyClsPoolExport(Map map, String loginTime) throws Exception {

		// 收发分类
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
				message = "不支持跨年导出";
				isSuccess = false;
				return BaseJson.returnRespList("account/invty/sendAndReceiveCls/poolExport", isSuccess, message,
						itList);
			} else {
				map.put("year", start.substring(0, 4));
				map.put("start", start.substring(5, 7));
				map.put("end", end.substring(5, 7));

			}
		}
		// 按照仓库汇总
		if (poolType == 1) {
			map.put("isWhsEncdPool", "whsEncd");

		} else if (poolType == 2) {
			// 按照批次汇总
			map.put("isBatNumPool", "batNum");

		}

		itList = invtyDetailDao.selectSendAndRecePool(map);// 查询

		ArrayList<JsonNode> nodeList = flattening(itList);

		resp = BaseJson.returnRespList("account/invty/sendAndReceiveInvtyCls/poolExport", true, "查询成功", nodeList);
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
