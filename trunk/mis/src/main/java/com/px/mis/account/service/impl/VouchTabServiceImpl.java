package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.*;
import com.px.mis.account.entity.*;
import com.px.mis.account.service.VouchTabService;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.ProvrDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class VouchTabServiceImpl extends poiTool implements VouchTabService {

	@Autowired
	private VouchTabDao vouchTabDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
	@Autowired
	private CustDocDao custDocDao;
//	@Autowired
//	private BizMemDocDao bizMemDocDao;
	@Autowired
	private ProvrDocDao provrDocDao;
	@Autowired
	private VouchCateDocDao vouchCateDocDao; // 凭证类别
	@Autowired
	private GetOrderNo getOrderNo; // 生成单号
	@Autowired
	private FormBookEntryDao formBookEntryDao; // 记账表
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; // 记账子表
	@Autowired
	private VouchTabSubDao vouchTabSubDao; // 凭证子表

	private Logger logger = LoggerFactory.getLogger(VouchTabServiceImpl.class);

	@Override
	public ObjectNode insertVouchTab(VouchTab vouchTab) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getSubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "科目编号" + vouchTab.getSubjId() + "不存在，新增失败！");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "对方科目编号" + vouchTab.getCntPtySubjId() + "不存在，新增失败！");
		} else if (deptDocDao.selectDeptDocByDeptEncd(vouchTab.getDeptId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "部门编号" + vouchTab.getDeptId() + "不存在，新增失败！");
		} else if (custDocDao.selectCustDocByCustId(vouchTab.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "客户编号" + vouchTab.getCustId() + "不存在，新增失败！");
		} else if (provrDocDao.selectProvrDocByProvrId(vouchTab.getProvrId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "供应商编号" + vouchTab.getProvrId() + "不存在，新增失败！");
		} else {
			int insertResult = vouchTabDao.insertVouchTab(vouchTab);
			if (insertResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateVouchTabByOrdrNum(VouchTab vouchTab) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = vouchTab.getOrdrNum();
		if (ordrNum == null) {
			on.put("isSuccess", false);
			on.put("message", "新增失败,主键不能为空");
		} else if (vouchTabDao.selectVouchTabByOrdrNum(ordrNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号" + ordrNum + "不存在！");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getSubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "科目编号" + vouchTab.getSubjId() + "不存在，更新失败！");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "对方科目编号" + vouchTab.getCntPtySubjId() + "不存在，更新失败！");
		} else if (deptDocDao.selectDeptDocByDeptEncd(vouchTab.getDeptId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "部门编号" + vouchTab.getDeptId() + "不存在，更新失败！");
		} else if (custDocDao.selectCustDocByCustId(vouchTab.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "客户编号" + vouchTab.getCustId() + "不存在，更新失败！");
		} else if (provrDocDao.selectProvrDocByProvrId(vouchTab.getProvrId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "供应商编号" + vouchTab.getProvrId() + "不存在，更新失败！");
		} else {
			int updateResult = vouchTabDao.updateVouchTabByOrdrNum(vouchTab);
			if (updateResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "更新成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "更新失败");
			}
		}

		return on;
	}

	@Override
	public ObjectNode deleteVouchTabByOrdrNum(Integer ordrNum) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (vouchTabDao.selectVouchTabByOrdrNum(ordrNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号" + ordrNum + "不存在！");
		} else {
			int deleteResult = vouchTabDao.deleteVouchTabByOrdrNum(ordrNum);
			if (deleteResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "删除成功");
			} else if (deleteResult == 0) {
				on.put("isSuccess", true);
				on.put("message", "没有要删除的数据");
			} else {
				on.put("isSuccess", false);
				on.put("message", "删除失败");
			}
		}

		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public VouchTab selectVouchTabByOrdrNum(Integer ordrNum) {
		VouchTab vouchTab = vouchTabDao.selectVouchTabByOrdrNum(ordrNum);
		return vouchTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectVouchTabList(Map map) throws Exception {
		String resp = "";
		List<VouchTab> list = vouchTabDao.selectVouchTabList(map);
		if (list.size() > 0) {
			list = mergeSubject(list);
		}
		resp = BaseJson.returnRespList("/account/vouchTab/selectVouchTab", true, "查询成功！", list);
		return resp;
	}

	private List<VouchTab> mergeSubject(List<VouchTab> list) throws ParseException {
		List<VouchTab> subList = new ArrayList<>();
		Map<String, VouchTab> deMap = new HashMap<>();
		Map<String, VouchTab> crMap = new HashMap<>();

		for (VouchTab sub : list) {
			sub.setCtime(sub.getCtime().substring(0, 10));

			if (deMap.containsKey(sub.getComnVouchId() + "-" + sub.getSubjId())) {
				VouchTab sub1 = (VouchTab) deMap.get(sub.getComnVouchId() + "-" + sub.getSubjId());
				sub1.setQtyDebit(sub1.getQtyDebit().add(sub.getQtyDebit()));
				sub1.setDebitAmt(sub1.getDebitAmt().add(sub.getDebitAmt()));

				deMap.put(sub.getComnVouchId() + "-" + sub.getSubjId(), sub1);

			} else {
				deMap.put(sub.getComnVouchId() + "-" + sub.getSubjId(), sub);
			}

			if (crMap.containsKey(sub.getComnVouchId() + "-" + sub.getCntPtySubjId())) {
				VouchTab sub1 = (VouchTab) crMap.get(sub.getComnVouchId() + "-" + sub.getCntPtySubjId());
				sub1.setQtyCrdt(sub1.getQtyCrdt().add(sub.getQtyCrdt()));
				sub1.setCrdtAmt(sub1.getCrdtAmt().add(sub.getCrdtAmt()));

				crMap.put(sub.getComnVouchId() + "-" + sub.getCntPtySubjId(), sub1);

			} else {
				crMap.put(sub.getComnVouchId() + "-" + sub.getCntPtySubjId(), sub);
			}
		}

		if (!deMap.isEmpty()) {
			for (Map.Entry<String, VouchTab> key : deMap.entrySet()) {
				VouchTab forms = (VouchTab) key.getValue();

				if (!crMap.isEmpty()) {
					for (Map.Entry<String, VouchTab> keys : crMap.entrySet()) {
						VouchTab cr = (VouchTab) keys.getValue();
						String str = forms.getComnVouchId() + "-" + forms.getCntPtySubjId();
						String st1 = cr.getComnVouchId() + "-" + cr.getCntPtySubjId();
						if (str.equals(st1)) {

							forms.setQtyCrdt(cr.getQtyCrdt());
							forms.setCrdtAmt(cr.getCrdtAmt());

						}
					}
				}
				subList.add(forms);
			}
		}

		return subList;
	}

	@Override
	public String selectvoucherList(Map map) {
		// 查询凭证
		// 单据联查
		String resp = "";
		String vouchId = (String) map.get("comnVouchId");
		List<String> vouchList = strToList(vouchId);
		map.put("vouchList", vouchList);
		List<VouchTabSub> dataList = vouchTabSubDao.selectVouchTabSubListByMap(map);
		try {
			resp = BaseJson.returnRespList("/account/vouchTab/selectVouchTab", true, "查询成功！", dataList);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String voucherGenerate(Map map) throws IOException, IllegalAccessException, InvocationTargetException {
		String message = "";
		Boolean isSuccess = true;
		String vouchCateWor = (String) map.get("vouchCateWor");
		String comnVouchComnt = (String) map.get("comnVouchComnt"); // 摘要
		String loginTime = (String) map.get("loginTime");
		String userName = (String) map.get("userName");
		String accNum = (String) map.get("accNum");
		// List<FormBookEntrySub> paltOrdersList = (List)map.get("paltOrdersList");
		List<VouchTabSub> paltOrdersList = (List) map.get("paltOrdersList");
		List<VouchTabSub> tabList = new ArrayList<>();

		// 判断凭证类别内的限制科目
		VouchCateDoc vouchCateDoc = vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor);
		Boolean isLmt = true;
		if (vouchCateDoc != null) {
			Map<String, Object> docMap = judgeVouchCate(vouchCateDoc, isLmt);
			Map<String, Object> debitMap = (Map<String, Object>) docMap.get("debitMap");// 借方必有
			Map<String, Object> crdtMap = (Map<String, Object>) docMap.get("crdtMap"); // 贷方必有
			Map<String, Object> voucNoMap = (Map<String, Object>) docMap.get("voucNoMap"); // 凭证必无
			Map<String, Object> voucMap = (Map<String, Object>) docMap.get("voucMap"); // 凭证必有

			isLmt = (Boolean) docMap.get("isLmt");
			if (isLmt) {
				if (paltOrdersList.size() > 0) {

					List<VouchTabSub> list = mergedTabSubject(paltOrdersList);

					for (VouchTabSub sub : list) {

						String debitId = sub.getSubDebitId(); // 借方科目编码
						String crdtId = sub.getSubDebitId(); // 贷方科目编码

						// 判断是否有辅助核算科目
						AcctItmDoc debitDoc = acctItmDocDao.selectAcctItmDocBySubjId(debitId);
						AcctItmDoc crdtDoc = acctItmDocDao.selectAcctItmDocBySubjId(crdtId);

						Map<String, Object> temp = selectIsAid(debitDoc, sub, debitId);
						isSuccess = (Boolean) temp.get("isSuccess");
						if (isSuccess) {
							temp = selectIsAid(crdtDoc, sub, crdtId);
							isSuccess = (Boolean) temp.get("isSuccess");
							if (!isSuccess) {
								message = (String) temp.get("message");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							}
						} else {
							message = (String) temp.get("message");
							return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
									null);
						}

						// 是否是末级科目
						int de = acctItmDocDao.selectIsFinalSubj(debitId);
						int cr = acctItmDocDao.selectIsFinalSubj(crdtId);
						if (de != 0 && cr != 0) {
							message = "科目非末级";
							isSuccess = false;
							logger.info("生成凭证异常：科目非末级");
							return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
									null);
						}

						// 凭证必无
						if (!voucNoMap.isEmpty()) {
							if (voucNoMap.containsKey(debitId) || voucNoMap.containsKey(crdtId)) {
								message = "借方或贷方科目必无所选科目";
								isSuccess = false;
								logger.info("生成凭证异常：借方或贷方科目必无所选科目");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}

						// 贷方必有
						if (!crdtMap.isEmpty()) {
							if (!crdtMap.containsKey(crdtId)) {
								message = "贷方科目不满足条件";
								isSuccess = false;
								logger.info("生成凭证异常：贷方科目不满足条件");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}
						// 借方必有 科目
						if (!debitMap.isEmpty()) {
							if (!debitMap.containsKey(debitId)) {
								message = "借方科目不满足条件";
								isSuccess = false;
								logger.info("生成凭证异常：借方科目不满足条件");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}

						// 凭证必有
						if (!voucMap.isEmpty()) {
							if (!voucMap.containsKey(debitId) && !voucMap.containsKey(crdtId)) {
								message = "借方或贷方科目必有所选科目";
								isSuccess = false;
								logger.info("生成凭证异常：借方或贷方科目必有所选科目");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}

					}
				}
			}
		}
		if (tabList.size() > 0) {
			isSuccess = dealVouchTab(tabList, vouchCateDoc.getVouchCateWor(), comnVouchComnt, accNum, userName,
					loginTime, isSuccess);

		}
		if (isSuccess) {
			message = "制单成功";
		} else {
			message = "制单失败";
		}
		return BaseJson.returnRespObj("/account/vouchTab/selectVouchTab", isSuccess, message, null);
	}

	private Map<String, Object> selectIsAid(AcctItmDoc debitDoc, VouchTabSub sub, String debitId) throws IOException {

		boolean isSuccess = true;
		String message = "";
		Map<String, Object> map = new HashMap<>();
		if (debitDoc.getIsNtDeptAccti() != null && debitDoc.getIsNtDeptAccti() == 1) {
			// 部门辅助
			if (StringUtils.isEmpty(sub.getDeptId())) {
				isSuccess = false;
				message = "科目编码：" + debitId + "需要部门辅助核算，单据号：" + sub.getFormNum() + "缺少部门信息";

			}
		}
		if (debitDoc.getIsNtCustRecoAccti() != null && debitDoc.getIsNtCustRecoAccti() == 1) {
			// 客户辅助
			if (StringUtils.isEmpty(sub.getCustId())) {
				isSuccess = false;
				message = "科目编码：" + debitId + "需要客户辅助核算，单据号：" + sub.getFormNum() + "缺少客户信息";

			}
		}
		if (debitDoc.getIsNtProvrRecoAccti() != null && debitDoc.getIsNtProvrRecoAccti() == 1) {
			// 供应商辅助
			if (StringUtils.isEmpty(sub.getProvrId())) {
				isSuccess = false;
				message = "科目编码：" + debitId + "需要供应商辅助核算，单据号：" + sub.getFormNum() + "缺少供应商信息";

			}
		}
		if (debitDoc.getIsNtProjAccti() != null && debitDoc.getIsNtProjAccti() == 1) {
			// 项目辅助
			if (StringUtils.isEmpty(sub.getProjEncd())) {
				isSuccess = false;
				message = "科目编码：" + debitId + "需要项目辅助核算，单据号：" + sub.getFormNum() + "缺少项目信息";

			}
		}
		if (debitDoc.getIsNtIndvRecoAccti() != null && debitDoc.getIsNtIndvRecoAccti() == 1) {
			// 个人往来-暂无
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private List<VouchTabSub> mergedTabSubject(List<VouchTabSub> paltOrdersList) {

		List<VouchTabSub> subList = new ArrayList<>();
		Map<String, VouchTabSub> deMap = new HashMap<>();
		Map<String, VouchTabSub> crMap = new HashMap<>();

		for (VouchTabSub sub : paltOrdersList) {
			String debitId = sub.getSubDebitId(); // 借方科目编码
			String crdtId = sub.getSubDebitId(); // 贷方科目编码

			// 判断是否有辅助核算科目
			AcctItmDoc debitDoc = acctItmDocDao.selectAcctItmDocBySubjId(debitId);
			AcctItmDoc crdtDoc = acctItmDocDao.selectAcctItmDocBySubjId(crdtId);
			if (debitDoc.getIsNtProjAccti() != null && debitDoc.getIsNtProjAccti() == 1) {
				// 项目辅助
				if (StringUtils.isNotEmpty(sub.getProjEncd())) {
					if (deMap.containsKey(sub.getFormNum() + "-" + sub.getSubDebitId() + "-" + sub.getProjEncd())) {
						VouchTabSub sub1 = (VouchTabSub) deMap
								.get(sub.getFormNum() + "-" + sub.getSubDebitId() + "-" + sub.getProjEncd());
						sub1.setSubDebitMoney(sub1.getSubDebitMoney().add(sub.getSubDebitMoney()));
						sub1.setSubDebitNum(sub1.getSubDebitNum().add(sub.getSubDebitNum()));

						deMap.put(sub.getFormNum() + "-" + sub.getSubDebitId() + "-" + sub.getProjEncd(), sub1);

					} else {
						deMap.put(sub.getFormNum() + "-" + sub.getSubDebitId() + "-" + sub.getProjEncd(), sub);
					}
				}
			} else {
				if (deMap.containsKey(sub.getFormNum() + "-" + sub.getSubDebitId())) {
					VouchTabSub sub1 = (VouchTabSub) deMap.get(sub.getFormNum() + "-" + sub.getSubDebitId());
					sub1.setSubDebitMoney(sub1.getSubDebitMoney().add(sub.getSubDebitMoney()));
					sub1.setSubDebitNum(sub1.getSubDebitNum().add(sub.getSubDebitNum()));

					deMap.put(sub.getFormNum() + "-" + sub.getSubDebitId(), sub1);

				} else {
					deMap.put(sub.getFormNum() + "-" + sub.getSubDebitId(), sub);
				}
			}
			
			
			if (crdtDoc.getIsNtProjAccti() != null && crdtDoc.getIsNtProjAccti() == 1) {

				if (crMap.containsKey(sub.getFormNum() + "-" + sub.getSubCreditId() + "-" + sub.getProjEncd())) {
					VouchTabSub sub1 = (VouchTabSub) crMap
							.get(sub.getFormNum() + "-" + sub.getSubCreditId() + "-" + sub.getProjEncd());
					sub1.setSubCreditMoney(sub1.getSubCreditMoney().add(sub.getSubCreditMoney()));
					sub1.setSubCreditNum(sub1.getSubCreditNum().add(sub.getSubCreditNum()));

					crMap.put(sub.getFormNum() + "-" + sub.getSubCreditId() + "-" + sub.getProjEncd(), sub1);

				} else {
					crMap.put(sub.getFormNum() + "-" + sub.getSubCreditId() + "-" + sub.getProjEncd(), sub);
				}

			} else {

				if (crMap.containsKey(sub.getFormNum() + "-" + sub.getSubCreditId())) {
					VouchTabSub sub1 = (VouchTabSub) crMap.get(sub.getFormNum() + "-" + sub.getSubCreditId());
					sub1.setSubCreditMoney(sub1.getSubCreditMoney().add(sub.getSubCreditMoney()));
					sub1.setSubCreditNum(sub1.getSubCreditNum().add(sub.getSubCreditNum()));

					crMap.put(sub.getFormNum() + "-" + sub.getSubCreditId(), sub1);

				} else {
					crMap.put(sub.getFormNum() + "-" + sub.getSubCreditId(), sub);
				}

			}

		}

		if (!deMap.isEmpty()) {
			for (Map.Entry<String, VouchTabSub> key : deMap.entrySet()) {
				VouchTabSub forms = (VouchTabSub) key.getValue();

				if (!crMap.isEmpty()) {
					for (Map.Entry<String, VouchTabSub> keys : crMap.entrySet()) {
						VouchTabSub cr = (VouchTabSub) keys.getValue();
						if (forms.getFormNum().equals(cr.getFormNum())) {
							forms.setSubCreditMoney(cr.getSubCreditMoney());
							forms.setSubCreditNum(cr.getSubCreditNum());
							forms.setSubCreditType(cr.getSubCreditType());
							forms.setSubCreditId(cr.getSubCreditId());
							forms.setSubCreditPath(cr.getSubCreditPath());
							forms.setSubCreditNm(cr.getSubCreditNm());
						}
					}
				}
				subList.add(forms);
			}
		}

		return subList;
	}

	/**
	 * 凭证子表信息
	 * 
	 * @param tabList
	 * @param vouchCateWor
	 * @param accNum
	 * @param userName
	 * @param loginTime
	 * @param comnVouchComnt
	 * @param isSuccess
	 * @return
	 */
	private boolean dealVouchTab(List<VouchTabSub> tabList, String vouchCateWor, String comnVouchComnt, String accNum,
			String userName, String loginTime, boolean isSuccess) {
		Map<String, VouchTabSub> tabMap = new HashMap<>();
		isSuccess = false;
		if (tabList.size() > 0) {
			// 查询是否已生成凭证
			Map<String, Object> map = new HashMap<>();
			List<String> strList = new ArrayList<>();
			for (VouchTabSub sub : tabList) {
				strList.add(sub.getFormNum());
			}
			map.put("formNumList", strList);
			List<VouchTabSub> list = vouchTabSubDao.selectVouchTabSubListByMap(map);
			if (list.size() > 0) {
				return false;
			}
			// 查询最后一张凭证表的凭证号
			VouchTab last = vouchTabDao.selectLastTab();
			String comnVouchId = getOrderNo.getSeqNo("VT", accNum, loginTime);
			int tabNum = 0;
			if (last != null) {
				tabNum = last.getTabNum() + 1;

			} else {
				tabNum = 1;
			}

			List<VouchTabSub> tabSubList = new ArrayList<>();
			List<FormBookEntry> formBookList = new ArrayList<>(); // 出入库单据
			List<FormBookEntry> groupFormList = new ArrayList<>(); // 组合单据
			List<FormBookEntry> sellSnglList = new ArrayList<>(); // 销售单
			List<FormBookEntry> pursInvMasList = new ArrayList<>(); // 采购发票
			List<FormBookEntry> sellInvMasList = new ArrayList<>(); // 销售发票
			List<FormBookEntry> intoWhsAdjSnglList = new ArrayList<>(); // 入库调整单
			List<FormBookEntry> outWhsAdjSnglList = new ArrayList<>(); // 出库调整单
			List<FormBookEntry> formBackFlushList = new ArrayList<>(); // 回冲单
			List<FormBookEntry> rtnGoodsList = new ArrayList<>(); // 退货单
			List<FormBookEntry> entrsAgnDelvList = new ArrayList<>(); // 委托代销发货单
			List<FormBookEntry> entrsAgnDelvRtnList = new ArrayList<>(); // 委托代销退货单

			FormBookEntry form = new FormBookEntry();

			VouchTab tab = new VouchTab(); // 凭证
			tab.setImported(0); // 是否导入总账
			tab.setComnVouchId(comnVouchId); // 凭证编号
			tab.setTabNum(tabNum);
			if (StringUtils.isNotEmpty(comnVouchComnt)) {
				tab.setComnVouchComnt(comnVouchComnt);
			} else {
				tab.setComnVouchComnt(tabList.get(0).getFormTypName() + "-" + "制单");
			}

			tab.setVouchCateWor(vouchCateWor); // 凭证类别字
			tab.setAccNum(accNum); // 制单人
			tab.setUserName(userName);
			tab.setCtime(loginTime); // 制单时间
			tab.setSubjId(tabList.get(0).getSubDebitId());
			tab.setSubjNm(tabList.get(0).getSubDebitNm());
			tab.setCntPtySubjId(tabList.get(0).getSubCreditId());
			tab.setCntPtySubjNm(tabList.get(0).getSubCreditNm());

			// 借方
			tab.setQtyDebit(Optional.ofNullable(tab.getQtyDebit()).orElse(new BigDecimal(0.00000000)));
			tab.setDebitAmt(Optional.ofNullable(tab.getDebitAmt()).orElse(new BigDecimal(0.00000000)));
			// 贷方
			tab.setQtyCrdt(Optional.ofNullable(tab.getQtyCrdt()).orElse(new BigDecimal(0.00000000)));
			tab.setCrdtAmt(Optional.ofNullable(tab.getCrdtAmt()).orElse(new BigDecimal(0.00000000)));
			tabMap = new HashMap<>();

			for (VouchTabSub sub : tabList) {
				String formType = sub.getFormTypEncd();
				sub.setComnVouchId(comnVouchId);

				// 调拨-组装-拆卸
				if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
					VouchTabSub tabSub = new VouchTabSub();

					if (tabMap.containsKey(sub.getFormNum())) {
						tabSub = (VouchTabSub) tabMap.get(sub.getFormNum());
						tabSub.setComnVouchId(comnVouchId);
						// 借
						if (tabSub.getSubCreditPath() != null && tabSub.getSubDebitPath() == 1) {
							tabSub.setSubCreditId(sub.getSubCreditId());
							tabSub.setSubCreditType(sub.getSubCreditType());
							tabSub.setSubCreditNm(sub.getSubCreditNm());
							tabSub.setSubCreditMoney(sub.getSubCreditMoney());
							tabSub.setSubCreditNum(sub.getSubCreditNum());
							tabSub.setSubCreditPath(2);
						} else {
							tabSub.setSubDebitId(sub.getSubDebitId());
							tabSub.setSubDebitType(sub.getSubDebitType());
							tabSub.setSubDebitNm(sub.getSubDebitNm());
							tabSub.setSubDebitMoney(sub.getSubDebitMoney());
							tabSub.setSubDebitNum(sub.getSubDebitNum());
							tabSub.setSubDebitPath(1);
						}
					} else {
						tabMap.put(sub.getFormNum(), sub);
					}

				} else {

					// 采购-销售-其他出入库
					// 销售单
					// 采购发票
					// 销售发票(专用/普通)
					// 入库调整单
					// 出库调整单
					// 蓝字回冲单
					// 红字回冲单
					sub.setSubDebitPath(1);
					sub.setSubCreditPath(2);

				}

				tab.setQtyDebit(tab.getQtyDebit().add(sub.getSubDebitNum()));
				tab.setDebitAmt(tab.getDebitAmt().add(sub.getSubDebitMoney()));
				tab.setQtyCrdt(tab.getQtyCrdt().add(sub.getSubCreditNum()));
				tab.setCrdtAmt(tab.getCrdtAmt().add(sub.getSubCreditMoney()));
				tabSubList.add(sub);

				form = new FormBookEntry();
				form.setFormNum(sub.getFormNum());
				form.setIsNtMakeVouch(1);
				form.setFormTypEncd(sub.getFormTypEncd());
				form.setMakVouchPers(userName);
				form.setMakVouchTm(loginTime);
				form.setMakeVouchId(comnVouchId);

				// 出入库单据
				if (formType.equals("004") || formType.equals("009") || formType.equals("014") || formType.equals("015")
						|| formType.equals("028")) {

					formBookList.add(form);

				}
				// 调拨-组装-拆卸
				if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
					form.setSellOrdrInd(sub.getFormNum());
					groupFormList.add(form);
				}
				// 销售单
				if (formType.equals("007")) {
					sellSnglList.add(form);
				}

				// 采购发票
				if (formType.equals("019") || formType.equals("20")) {
					pursInvMasList.add(form);
				}
				// 销售发票(专用/普通)
				if (formType.equals("021") || formType.equals("022")) {
					sellInvMasList.add(form);
				}
				// 入库调整单
				if (formType.equals("030")) {
					intoWhsAdjSnglList.add(form);
				}
				// 出库调整单
				if (formType.equals("031")) {
					outWhsAdjSnglList.add(form);
				}
				// 红字回冲单
				if (formType.equals("016")) {
					formBackFlushList.add(form);
				}
				// 蓝字回冲单
				if (formType.equals("017")) {
					formBackFlushList.add(form);
				}
				// 退货单
				if (formType.equals("008")) {
					rtnGoodsList.add(form);
				}
				// 委托代销发货单
				if (formType.equals("023")) {

					entrsAgnDelvList.add(form);
				}
				// 委托代销退货单
				if (formType.equals("024")) {
					entrsAgnDelvRtnList.add(form);
				}

			}
			if (tabSubList.size() > 0) {

				// 批量修改相关单据

				if (formBookList.size() > 0) {
					formBookEntryDao.updateFormVouch(formBookList);
				}
				if (groupFormList.size() > 0) {
					formBookEntryDao.updateGruopFormVouch(formBookList);
				}
				if (sellSnglList.size() > 0) {
					formBookEntryDao.updateSellSnglFormVouch(sellSnglList);
				}
				if (pursInvMasList.size() > 0) {
					formBookEntryDao.updatePursInvMasFormVouch(pursInvMasList);
				}
				if (sellInvMasList.size() > 0) {
					formBookEntryDao.updateSellInvMasFormVouch(sellInvMasList);
				}
				if (intoWhsAdjSnglList.size() > 0) {
					formBookEntryDao.updateIntoWhsAdjFormVouch(intoWhsAdjSnglList);
				}
				if (formBackFlushList.size() > 0) {
					formBookEntryDao.updateFormBackFlushFormVouch(formBackFlushList);
				}
				if (rtnGoodsList.size() > 0) {
					formBookEntryDao.updateRtnGoodsFormVouch(rtnGoodsList);
				}
				if (entrsAgnDelvList.size() > 0) {
					formBookEntryDao.updateEntrsAgnDelvFormVouch(rtnGoodsList);
				}
				if (entrsAgnDelvRtnList.size() > 0) {
					formBookEntryDao.updateEntrsAgnDelvFormVouch(rtnGoodsList);
				}

				// 批量新增凭证子表
				vouchTabSubDao.insertList(tabSubList);
				// 凭证主表
				vouchTabDao.insertVouchTab(tab);
				isSuccess = true;
			} else {
				isSuccess = false;
			}
		} else {
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * 根据单据类型区分单据
	 * 
	 * @param form
	 * @param sub
	 * @param userName
	 * @param loginTime
	 * @param formType
	 * @param formBookList
	 * @param sellSnglList
	 * @param pursInvMasList
	 * @param sellInvMasList
	 * @param intoWhsAdjSnglList
	 * @param outWhsAdjSnglList
	 * @param formBackFlushList
	 * @param rtnGoodsList
	 * @param entrsAgnDelvList
	 * @param entrsAgnDelvRtnList
	 * @return
	 */
	private Map<String, Object> judgeFormTyp(boolean isDel, FormBookEntry form, VouchTabSub sub, String userName,
			String loginTime, String formType, List<FormBookEntry> formBookList, List<FormBookEntry> groupFormList,
			List<FormBookEntry> sellSnglList, List<FormBookEntry> pursInvMasList, List<FormBookEntry> sellInvMasList,
			List<FormBookEntry> intoWhsAdjSnglList, List<FormBookEntry> outWhsAdjSnglList,
			List<FormBookEntry> formBackFlushList, List<FormBookEntry> rtnGoodsList,
			List<FormBookEntry> entrsAgnDelvList, List<FormBookEntry> entrsAgnDelvRtnList) {

		Map<String, Object> dataMap = new HashMap<>();
		form = new FormBookEntry();

		if (isDel) {
			form.setFormNum(sub.getFormNum());
			form.setIsNtMakeVouch(0);
			form.setFormTypEncd(sub.getFormTypEncd());
			form.setMakVouchPers("");
			form.setMakVouchTm("0000-00-00 00:00:00");
			form.setMakeVouchId("");
		} else {
			form.setFormNum(sub.getFormNum());
			form.setIsNtMakeVouch(1);
			form.setFormTypEncd(sub.getFormTypEncd());
			form.setMakVouchPers(userName);
			form.setMakVouchTm(loginTime);
		}
		// 出入库单据
		if (formType.equals("004") || formType.equals("009") || formType.equals("014") || formType.equals("015")
				|| formType.equals("028")) {

			formBookList.add(form);

		}
		// 调拨-组装-拆卸
		if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
			form.setSellOrdrInd(sub.getFormNum());
			groupFormList.add(form);
		}
		// 销售单
		if (formType.equals("007")) {
			sellSnglList.add(form);
		}

		// 采购发票
		if (formType.equals("019") || formType.equals("20")) {
			pursInvMasList.add(form);
		}
		// 销售发票(专用/普通)
		if (formType.equals("021") || formType.equals("022")) {
			sellInvMasList.add(form);
		}
		// 入库调整单
		if (formType.equals("030")) {
			intoWhsAdjSnglList.add(form);
		}
		// 出库调整单
		if (formType.equals("031")) {
			outWhsAdjSnglList.add(form);
		}
		// 红字回冲单
		if (formType.equals("016")) {
			formBackFlushList.add(form);
		}
		// 蓝字回冲单
		if (formType.equals("017")) {
			formBackFlushList.add(form);
		}
		// 退货单
		if (formType.equals("008")) {
			rtnGoodsList.add(form);
		}
		// 委托代销发货单
		if (formType.equals("023")) {

			entrsAgnDelvList.add(form);
		}
		// 委托代销退货单
		if (formType.equals("024")) {
			entrsAgnDelvRtnList.add(form);
		}
		dataMap.put("formBookList", formBookList);
		dataMap.put("sellSnglList", sellSnglList);
		dataMap.put("pursInvMasList", pursInvMasList);
		dataMap.put("sellInvMasList", sellInvMasList);
		dataMap.put("intoWhsAdjSnglList", intoWhsAdjSnglList);
		dataMap.put("outWhsAdjSnglList", outWhsAdjSnglList);
		dataMap.put("formBackFlushList", formBackFlushList);
		dataMap.put("rtnGoodsList", rtnGoodsList);
		dataMap.put("rtnGoodsList", rtnGoodsList);
		dataMap.put("entrsAgnDelvList", entrsAgnDelvList);
		dataMap.put("entrsAgnDelvRtnList", entrsAgnDelvRtnList);
		return dataMap;
	}

	/**
	 * 根据单据类型进行批量更新
	 * 
	 * @param formBookList
	 * @param sellSnglList
	 * @param pursInvMasList
	 * @param sellInvMasList
	 * @param intoWhsAdjSnglList
	 * @param outWhsAdjSnglList
	 * @param formBackFlushList
	 * @param rtnGoodsList
	 * @param entrsAgnDelvList
	 * @param entrsAgnDelvRtnList
	 */
	private boolean updateListByFormTyp(boolean isSuccess, List<FormBookEntry> formBookList,
			List<FormBookEntry> groupFormList, List<FormBookEntry> sellSnglList, List<FormBookEntry> pursInvMasList,
			List<FormBookEntry> sellInvMasList, List<FormBookEntry> intoWhsAdjSnglList,
			List<FormBookEntry> outWhsAdjSnglList, List<FormBookEntry> formBackFlushList,
			List<FormBookEntry> rtnGoodsList, List<FormBookEntry> entrsAgnDelvList,
			List<FormBookEntry> entrsAgnDelvRtnList) {
		if (formBookList.size() > 0) {
			formBookEntryDao.updateFormVouch(formBookList);
			isSuccess = true;
		}
		if (groupFormList.size() > 0) {
			formBookEntryDao.updateGruopFormVouch(formBookList);
		}
		if (sellSnglList.size() > 0) {
			formBookEntryDao.updateSellSnglFormVouch(sellSnglList);
			isSuccess = true;
		}
		if (pursInvMasList.size() > 0) {
			formBookEntryDao.updatePursInvMasFormVouch(pursInvMasList);
			isSuccess = true;
		}
		if (sellInvMasList.size() > 0) {
			formBookEntryDao.updateSellInvMasFormVouch(sellInvMasList);
			isSuccess = true;
		}
		if (intoWhsAdjSnglList.size() > 0) {
			formBookEntryDao.updateIntoWhsAdjFormVouch(intoWhsAdjSnglList);
			isSuccess = true;
		}
		if (formBackFlushList.size() > 0) {
			formBookEntryDao.updateFormBackFlushFormVouch(formBackFlushList);
			isSuccess = true;
		}
		if (rtnGoodsList.size() > 0) {
			formBookEntryDao.updateRtnGoodsFormVouch(rtnGoodsList);
			isSuccess = true;
		}
		if (entrsAgnDelvList.size() > 0) {
			formBookEntryDao.updateEntrsAgnDelvFormVouch(rtnGoodsList);
			isSuccess = true;
		}
		if (entrsAgnDelvRtnList.size() > 0) {
			formBookEntryDao.updateEntrsAgnDelvFormVouch(rtnGoodsList);
			isSuccess = true;
		}
		return isSuccess;
	}

	/**
	 * 判断凭证类别字的限制科目
	 * 
	 * @param vouchCateDoc
	 * @param isLmt
	 * @return
	 */
	private Map<String, Object> judgeVouchCate(VouchCateDoc vouchCateDoc, boolean isLmt) {
		Map<String, Object> debitMap = new HashMap<>(); // 借方必有
		Map<String, Object> crdtMap = new HashMap<>(); // 贷方必有
		Map<String, Object> voucNoMap = new HashMap<>(); // 凭证必无
		Map<String, Object> voucMap = new HashMap<>(); // 凭证必有
		// 判断限制科目
		Map<String, Object> docMap = new HashMap<>();

		if (vouchCateDoc.getLmtSubjList().size() != 0) {

			if (vouchCateDoc.getLmtMode().contains("凭证必无")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {
					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						voucNoMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("贷方必有")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {

					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						crdtMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("借方必有")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {

					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						debitMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("凭证必有")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {

					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						voucMap.put(subjId, vouc);
					}

				}
			}

			isLmt = true;
		} else {
			isLmt = false;
		}

		docMap.put("debitMap", debitMap);
		docMap.put("crdtMap", crdtMap);
		docMap.put("voucNoMap", voucNoMap);
		docMap.put("voucMap", voucMap);
		docMap.put("isLmt", isLmt);
		return docMap;
	}

	@Override
	@Transactional
	public String voucherDel(Map map) throws IOException {
		String message = "";
		Boolean isSuccess = true;
		String comnVouchId = (String) map.get("comnVouchId");
		String loginTime = (String) map.get("loginTime");
		String userName = (String) map.get("userName");
		String accNum = (String) map.get("accNum");

		if (StringUtils.isNotEmpty(comnVouchId)) {

			List<String> vouchList = strToList(comnVouchId);
			map.put("vouchList", vouchList);
			List<VouchTab> vouchTabList = vouchTabDao.selectVouchTabMap(map);

			if (vouchTabList.size() != 0) {

				List<VouchTabSub> tabSubList = new ArrayList<>();
				List<FormBookEntry> formBookList = new ArrayList<>(); // 出入库单据
				List<FormBookEntry> groupFormList = new ArrayList<>(); // 组合单
				List<FormBookEntry> sellSnglList = new ArrayList<>(); // 销售单
				List<FormBookEntry> pursInvMasList = new ArrayList<>(); // 采购发票
				List<FormBookEntry> sellInvMasList = new ArrayList<>(); // 销售发票
				List<FormBookEntry> intoWhsAdjSnglList = new ArrayList<>(); // 入库调整单
				List<FormBookEntry> outWhsAdjSnglList = new ArrayList<>(); // 出库调整单
				List<FormBookEntry> formBackFlushList = new ArrayList<>(); // 回冲单
				List<FormBookEntry> rtnGoodsList = new ArrayList<>(); // 退货单
				List<FormBookEntry> entrsAgnDelvList = new ArrayList<>(); // 委托代销发货单
				List<FormBookEntry> entrsAgnDelvRtnList = new ArrayList<>(); // 委托代销退货单
				Map<String, Object> dataMap = new HashMap<>();

				FormBookEntry form = new FormBookEntry();

				for (VouchTab tab : vouchTabList) {
					if (tab.getImported() == 1) {
						message += "凭证编号:" + tab.getComnVouchId() + "已经上传总账,无法删除";
						isSuccess = false;
					} else {
						map.put("comnVouchId", tab.getComnVouchId());
						tabSubList.addAll(vouchTabSubDao.selectVouchTabSubListByMap(map));

					}
				}

				if (tabSubList.size() > 0) {

					for (VouchTabSub sub : tabSubList) {
						String formType = sub.getFormTypEncd();

						dataMap = judgeFormTyp(true, form, sub, userName, loginTime, formType, formBookList,
								groupFormList, sellSnglList, pursInvMasList, sellInvMasList, intoWhsAdjSnglList,
								outWhsAdjSnglList, formBackFlushList, rtnGoodsList, entrsAgnDelvList,
								entrsAgnDelvRtnList);
					}
					dataMap.put("tabSubList", tabSubList);
				}

				if (!dataMap.isEmpty()) {
					formBookList = (List<FormBookEntry>) dataMap.get("formBookList");
					sellSnglList = (List<FormBookEntry>) dataMap.get("sellSnglList");
					pursInvMasList = (List<FormBookEntry>) dataMap.get("pursInvMasList");
					sellInvMasList = (List<FormBookEntry>) dataMap.get("sellInvMasList");
					intoWhsAdjSnglList = (List<FormBookEntry>) dataMap.get("intoWhsAdjSnglList");
					outWhsAdjSnglList = (List<FormBookEntry>) dataMap.get("outWhsAdjSnglList");
					formBackFlushList = (List<FormBookEntry>) dataMap.get("formBackFlushList");
					rtnGoodsList = (List<FormBookEntry>) dataMap.get("rtnGoodsList");
					entrsAgnDelvList = (List<FormBookEntry>) dataMap.get("entrsAgnDelvList");
					entrsAgnDelvRtnList = (List<FormBookEntry>) dataMap.get("entrsAgnDelvRtnList");

					isSuccess = updateListByFormTyp(isSuccess, formBookList, groupFormList, sellSnglList,
							pursInvMasList, sellInvMasList, intoWhsAdjSnglList, outWhsAdjSnglList, formBackFlushList,
							rtnGoodsList, entrsAgnDelvList, entrsAgnDelvRtnList);
					tabSubList = (List<VouchTabSub>) dataMap.get("tabSubList");

					// 凭证删除留存
					vouchTabDao.insertVouchTabDlList(tabSubList);
					vouchTabSubDao.insertVouchTabSubDlList(tabSubList);
					vouchTabDao.delectVouchTabSubList(tabSubList);
					// 整理凭证编码
					dataMap.clear();
					dataMap.put("tabSubList", tabSubList);

					List<VouchTab> sortOutList = vouchTabDao.selectVouchTab(dataMap);
					if (sortOutList.size() > 0) {
						for (int i = 1; i <= sortOutList.size(); i++) {
							sortOutList.get(i - 1).setTabNum(i);

						}
						vouchTabDao.updateVouchList(sortOutList);
					}

					message = "删除成功";
					isSuccess = true;
				}

			} else {
				message = "删除失败";
				isSuccess = false;
			}

		} else {
			message = "删除失败";
			isSuccess = false;
		}
		return BaseJson.returnRespObj("/account/vouchTab/selectVouchTab", isSuccess, message, null);
	}

	/**
	 * set凭证值
	 */
	private VouchTab getVouchTab(VouchTab tab, FormBookEntrySub sub, BigDecimal debitMoney, BigDecimal creditMoney,
			BigDecimal qtyDebit, BigDecimal qtyCrdt) {

		if (sub.getSubDebitMoney() != null) {
			debitMoney = debitMoney.add(sub.getSubDebitMoney());
		}
		if (sub.getSubDebitNum() != null) {
			qtyDebit = qtyDebit.add(sub.getSubDebitNum());
		}
		if (sub.getSubCreditMoney() != null) {
			creditMoney = creditMoney.add(sub.getSubCreditMoney());
		}

		if (sub.getSubCreditNum() != null) {
			qtyCrdt = qtyCrdt.add(sub.getSubCreditNum());
		}
		tab.setDebitAmt(debitMoney);
		tab.setCrdtAmt(creditMoney);
		tab.setQtyCrdt(qtyCrdt);
		tab.setQtyDebit(qtyDebit);
		return tab;
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

//	@Override
//	public String exportvoucherList(Map map) throws Exception{
//		map.put("imported","0");
//		String resp = "";
//		String message = "";
//		boolean isSuccess = true;
//		String loginTime = (String)map.get("loginTime");
//		String userName = (String)map.get("user");
//		List<VouchTab> tabList = vouchTabDao.exportVouchTabList(map);
//		List<VouchTab> dataList = new ArrayList<>();
//		
//		
//		//修改单据
//		if(tabList.size() > 0) {
//			VouchTab dTab = new VouchTab(); //借方
//			VouchTab cTab = new VouchTab(); //贷方
//			tabList = mergeSubject(tabList);
//			
//			for(VouchTab tab : tabList) {
//				String num = String.valueOf(vouchTabSubDao.selectVouchTabSubListByMap(map).size());
//				tab.setAcctYr(Integer.valueOf(tab.getCtime().substring(0,4)));
//				tab.setAcctiMth(Integer.valueOf(tab.getCtime().substring(5,7)));
//				tab.setCurrencyName("人民币");
//				tab.setAttachedFormNumbers(num);
//				map.put("comnVouchId", tab.getComnVouchId());
//				
//				dTab = new VouchTab(); //借方		
//				BeanUtils.copyProperties(dTab, tab);			
//				dTab.setCrdtAmt(null);
//				dTab.setQtyCrdt(null);
//				
//				cTab = new VouchTab(); //贷方
//				BeanUtils.copyProperties(cTab, tab);		
//				cTab.setSubjId(tab.getCntPtySubjId());
//				cTab.setSubjNm(tab.getCntPtySubjNm());
//				cTab.setDebitAmt(null);
//				cTab.setQtyDebit(null);
//				
//				dataList.add(dTab);
//				dataList.add(cTab);
//				tab.setImported(1);
//				tab.setImportNm(userName);
//				tab.setImportDt(loginTime);
//				
//			vouchTabDao.updateVouchList(tabList);
//			
//		}
//		message = "导出成功";
//	} else {
//		isSuccess = false;
//		message = "导出失败,查询数据已导出或数据为空";
//	}
//		return BaseJson.returnRespListAnno("/account/vouchTab/voucher/export", isSuccess, message, dataList);
//	}
	// 导出
	@Override
	public String exportvoucherList(Map map) throws Exception {
		String resp = "";
		List<VouchTab> list = vouchTabDao.selectVouchTabList(map);
		if (list.size() > 0) {
			list = mergeSubject(list);
		}
		resp = BaseJson.returnRespListAnno("/account/vouchTab/voucher/export", true, "查询成功！", list);
		return resp;
	}

	@Override
	public String importVoucherList(MultipartFile file, String accNum) throws Exception {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, VouchTab> vouchTabMap = uploadVoucher(file, accNum);

		for (Map.Entry<String, VouchTab> entry : vouchTabMap.entrySet()) {
			if (vouchTabDao.selectVouchTabBycomnVouchId(entry.getValue().getComnVouchId()) != null) {
				isSuccess = false;
				message = "凭证列表中部分凭证编号已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			} else {
				vouchTabDao.insertVouchTab(entry.getValue());

				isSuccess = true;
				message = "凭证导入成功！";
			}
		}

		resp = BaseJson.returnRespObj("account/vouchTab/voucher/import", isSuccess, message, null);

		return resp;

	}

	private Map<String, VouchTab> uploadVoucher(MultipartFile file, String accNum) {
		Map<String, VouchTab> temp = new HashMap<>();
		int j = 1;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			InputStream fileIn = file.getInputStream();
			// 根据指定的文件输入流导入Excel从而产生Workbook对象
			HSSFWorkbook wb0 = new HSSFWorkbook(fileIn);

			// 获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			// 获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			// 获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			// 设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "凭证编码");

				VouchTab tab = new VouchTab(); // 凭证
				tab.setImported(0); // 是否导入总账
				if (temp.containsKey(orderNo)) {
					tab = temp.get(orderNo);
				}
				tab.setComnVouchId(orderNo); // 凭证编号

				tab.setVouchCateWor(GetCellData(r, "凭证类别字"));
				tab.setComnVouchComnt(GetCellData(r, "凭证说明"));
				tab.setAccNum(GetCellData(r, "制单人"));

				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					// sellComnInv.setSetupTm(null);
					tab.setCtime(GetCellData(r, "制单时间"));
				} else {
					tab.setCtime(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				tab.setChkr(GetCellData(r, "审核人"));
				tab.setBookOkAcc(GetCellData(r, "记账人"));
				tab.setMemo(GetCellData(r, "摘要"));
				tab.setSubjId(GetCellData(r, "借方科目编号"));
				tab.setCntPtySubjId(GetCellData(r, "贷方科目编号"));
				tab.setDebitAmt(new BigDecimal(GetCellData(r, "借方金额")));
				tab.setCrdtAmt(new BigDecimal(GetCellData(r, "贷方金额")));
				tab.setQtyDebit(new BigDecimal(GetCellData(r, "借方数量")));
				tab.setQtyCrdt(new BigDecimal(GetCellData(r, "贷方数量")));

				// tab.setSubjId(GetCellData(r, "借方科目编号"));//科目编号
				// tab.setCntPtySubjId(GetCellData(r, "贷方科目编号")); //对方科目编号

				tab.setImported(0); // 是否导入总账
				tab.setImportNm(accNum);// 导入人
				tab.setImportDt(sf.format(new Date()));
				temp.put(orderNo, tab);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}
}
