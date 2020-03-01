package com.px.mis.purc.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MappingIterator;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvResponse;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvTable;
import com.px.mis.purc.dao.PayApplFormDao;
import com.px.mis.purc.dao.PayApplFormSubDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.entity.PayApplForm;
import com.px.mis.purc.entity.PayApplFormSub;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.entity.PayApplFormForU8.U8PayApplForm;
import com.px.mis.purc.entity.PayApplFormForU8.U8PayApplFormResponse;
import com.px.mis.purc.entity.PayApplFormForU8.U8PayApplFormSub;
import com.px.mis.purc.entity.PayApplFormForU8.U8PayApplFormTable;
import com.px.mis.purc.service.PayApplFormService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

/** 付款申请单 */
@Transactional
@Service
public class PayApplFormServiceImpl implements PayApplFormService {
	@Autowired
	private PayApplFormDao payApplFormDao;
	@Autowired
	private PayApplFormSubDao payApplFormSubDao;
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;
	// 订单号
	@Autowired
	GetOrderNo getOrderNo;

	// 子主类
	public static class zizhu {
		/**
		 * 订单编号
		 */
		@JsonProperty("订单编号")
		public String payApplId;

		/**
		 * 订单日期
		 */
		@JsonProperty("订单日期")
		public String payApplDt;

		/**
		 * 供应商编号
		 */
		@JsonProperty("供应商编号")
		public String provrId;

		/**
		 * 单据类型编码
		 */
		@JsonProperty("单据类型编码")
		public String formTypEncd;
		/**
		 * 来源单据类型编码
		 */
		@JsonProperty("来源单据类型编码")
		public String toFormTypEncd;
		/**
		 * 用户编号
		 */
		@JsonProperty("用户编号")
		public String accNum;

		/**
		 * 用户名称
		 */
		@JsonProperty("用户名称")
		public String userName;

		/**
		 * 部门编码
		 */
		@JsonProperty("部门编码")
		public String deptId;

		/**
		 * 供应商订单号
		 */
		@JsonProperty("供应商订单号")
		public String provrOrdrNum;

		/**
		 * 是否付款
		 */
		@JsonProperty("是否付款")
		public Integer isNtPay;

		/**
		 * 付款人
		 */
		@JsonProperty("付款人")
		public String payr;

		/**
		 * 结算科目
		 */
		@JsonProperty("结算科目")
		public String stlSubj;

		/**
		 * 预付款余额
		 */
		@JsonProperty("预付款余额")
		public BigDecimal prepyMoneyBal;

		/**
		 * 应付款余额
		 */
		@JsonProperty("应付款余额")
		public BigDecimal acctPyblBal;

		/**
		 * 是否审核
		 */
		@JsonProperty("是否审核")
		public Integer isNtChk;

		/**
		 * 审核人
		 */
		@JsonProperty("审核人")
		public String chkr;

		/**
		 * 审核时间
		 */
		@JsonProperty("审核时间")
		public String chkTm;

		/**
		 * 创建人
		 */
		@JsonProperty("创建人")
		public String setupPers;

		/**
		 * 创建时间
		 */
		@JsonProperty("创建时间")
		public String setupTm;

		/**
		 * 修改人
		 */
		@JsonProperty("修改人")
		public String mdfr;

		/**
		 * 修改时间
		 */
		@JsonProperty("修改时间")
		public String modiTm;

		/**
		 * 备注
		 */
		@JsonProperty("备注")
		public String memo;
		/**
		 * 结算方式
		 */
		@JsonProperty("结算方式")
		public String stlMode;
		/**
		 * 采购订单编码
		 */
		@JsonProperty("采购订单编码")
		public String pursOrdrId;
		private static final long serialVersionUID = 1L;

		// 关联查询供应商名称、用户名称、部门名称、采购类型名称
		@JsonProperty("供应商名称")
		public String provrNm;// 供应商名称
		@JsonProperty("部门名称")
		public String deptName;// 部门名称
		@JsonProperty("单据类型名称")
		public String formTypName;// 单据类型名称
		// 子表
		/**
		 * 序号
		 */
		@JsonProperty("序")
		public Long ordrNum;

		/**
		 * 计划到货日期
		 */
		@JsonProperty("计划到货日期")
		public String expctPayDt;

		/**
		 * 数量
		 */
		@JsonProperty("数量")
		public BigDecimal qty;

		/**
		 * 来源单据号
		 */
		@JsonProperty("来源单据号")
		public String srcFormNum;

		/**
		 * 原单本次申请金额
		 */
		@JsonProperty("原单本次申请金额")
		public BigDecimal orgnlSnglCurrApplAmt;

		/**
		 * 金额
		 */
		@JsonProperty("金额")
		public BigDecimal amt;

		/**
		 * 来源子表序号
		 */
		@JsonProperty("单据来源子表序号")
		public Long formOrdrNum;

		/**
		 * 实际付款时间
		 */
		@JsonProperty("实际付款时间")
		public String actlPayTm;

		/**
		 * 行关闭人
		 */
		@JsonProperty(" 行关闭人")
		public String lineClosPers;

		@JsonProperty("来源子表序号")
		public Long toOrdrNum;// 来源子表序号
	}

	// 新增付款申请单
	@Override
	public String addPayApplForm(String userId, PayApplForm payApplForm, List<PayApplFormSub> payApplFormSubList,
			String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String number = getOrderNo.getSeqNo("FKSQ", userId, loginTime);
			if (payApplFormDao.selectByPrimaryKey(number) != null) {
				isSuccess = false;
				message = "编号" + number + "已存在，请重新输入！";
			} else {
				payApplForm.setPayApplId(number);// 主表获取订单号
				for (PayApplFormSub payTab : payApplFormSubList) {
					payTab.setPayApplId(payApplForm.getPayApplId());// 主表单据号
					if (payTab.getActlPayTm() == "") {
						payTab.setActlPayTm(null);
					}
					if (payTab.getExpctPayDt() == "") {
						payTab.setExpctPayDt(null);
					}
				}
				payApplFormDao.insertPayApplForm(payApplForm);
				payApplFormSubDao.insertPayApplFormSubList(payApplFormSubList);
				isSuccess = true;
				message = "新增成功！";
			}
			resp = BaseJson.returnRespObj("purc/PayApplForm/addPayApplForm", isSuccess, message, payApplForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("生成订单编号! 失败");
		} // 获取订单

		return resp;
	}

	// 修改付款申请单
	@Override
	public String editPayApplForm(PayApplForm payApplForm, List<PayApplFormSub> payApplFormSubList) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			PayApplForm pay = payApplFormDao.selectByPrimaryKey(payApplForm.getPayApplId());
			if (pay.getIsNtChk() == null) {
				throw new RuntimeException("单号审核状态异常");
			} else if (pay.getIsNtChk() == 1) {
				throw new RuntimeException("单号已审核不允许修改");
			}
			for (PayApplFormSub payTab : payApplFormSubList) {
				payTab.setPayApplId(payApplForm.getPayApplId());// 主表单据号
				if (payTab.getActlPayTm() == "") {
					payTab.setActlPayTm(null);
				}
				if (payTab.getExpctPayDt() == "") {
					payTab.setExpctPayDt(null);
				}
			}
			payApplFormSubDao.deleteByPrimaryKey(payApplForm.getPayApplId());

			payApplFormDao.updateByPrimaryKeySelective(payApplForm);
			payApplFormSubDao.insertPayApplFormSubList(payApplFormSubList);
			isSuccess = true;
			message = "更新成功！";

			resp = BaseJson.returnRespObj("purc/PayApplForm/editPayApplForm", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("修改失败" + e.getMessage());

		}
		return resp;
	}

	@Override
	public String deleteEntrsAgnAdj(String payApplId) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(payApplId);
			List<PayApplForm> applForms = payApplFormDao.selectByPrimaryKeyLsit(list);
			List<String> lists = new ArrayList<String>();
			List<String> lists2 = new ArrayList<String>();
			if (applForms.size() > 0) {
				for (PayApplForm form : applForms) {
					if (form.getIsNtChk() == 0) {
						lists.add(form.getPayApplId());
					} else {
						lists2.add(form.getPayApplId());
					}
				}

				if (lists.size() > 0) {
					try {
						deleteByPrimaryKeyList(lists);
						isSuccess = true;
						message = "删除成功！" + lists.toString();
					} catch (Exception e) {
						isSuccess = false;
						message = "单据号为" + lists.toString() + "刪除失败!";
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						e.printStackTrace();
					}
				}

				if (lists2.size() > 0) {
					message = message + "\r编号已审：" + lists2;
				}
			} else {
				isSuccess = false;
				message = "编号" + payApplId + "不存在！";
			}

			resp = BaseJson.returnRespObj("purc/PayApplForm/deleteEntrsAgnAdj", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("删除失败");
		}
		return resp;
	}

	@Transactional
	private void deleteByPrimaryKeyList(List<String> lists) {
		payApplFormDao.insertPayApplFormDl(lists);
		payApplFormSubDao.insertPayApplFormSubDl(lists);
		payApplFormDao.deleteByPrimaryKeyList(lists);
	}

	@Override
	public String queryPayApplForm(String payApplId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {

			PayApplForm applForm = payApplFormDao.selectByPrimaryList(payApplId);
			if (applForm == null) {
				isSuccess = false;
				message = "没有该单据号，查询失败";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/PayApplForm/queryPayApplForm", isSuccess, message, applForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询 失败");

		}
		return resp;
	}

	@Override
	public String queryPayApplFormList(Map map) {
		String resp = "";
		map.put("provrIdList", getList((String) map.get("provrId")));
		map.put("accNumList", getList((String) map.get("accNum")));
		map.put("userNameList", getList((String) map.get("userName")));
		map.put("deptIdList", getList((String) map.get("deptId")));
		map.put("provrOrdrNumList", getList((String) map.get("provrOrdrNum")));
		map.put("payrList", getList((String) map.get("payr")));
		map.put("stlSubjList", getList((String) map.get("stlSubj")));

		List<PayApplForm> applForms = payApplFormDao.selectPayApplFormList(map);
		int count = payApplFormDao.selectPayApplFormCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = applForms.size();
		int pages = count / pageSize;
		if (count % pageSize > 0) {
			pages += 1;
		}
		try {
			resp = BaseJson.returnRespList("purc/PayApplForm/queryPayApplFormList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, applForms);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotEmpty(id)) {
			if (id.contains(",")) {
				String[] str = id.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(str[i]);
				}
			} else {
				if (StringUtils.isNotEmpty(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> updatePayApplFormIsNtChk(PayApplForm payApplForm) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (payApplForm.getIsNtChk() == 1) {
				System.out.println("审核 999999999999");
				message.append(updatePayApplFormIsNtChkOK(payApplForm).get("message"));
			} else if (payApplForm.getIsNtChk() == 0) {
				System.out.println("弃审 999999999999");
				message.append(updatePayApplFormIsNtChkNO(payApplForm).get("message"));
			} else {
				isSuccess = false;
				message.append("审核状态错误，无法审核！\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	private Map<String, Object> updatePayApplFormIsNtChkOK(PayApplForm payApplForm) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (payApplFormDao.selectPayApplIsNtChk(payApplForm.getPayApplId()) == 0) {
			PayApplForm payAppFor = payApplFormDao.selectByPrimaryList(payApplForm.getPayApplId());// 付款申请单主表
			List<PayApplFormSub> payApplFormSubList = payAppFor.getList();// 获取付款申请单子表
			for (PayApplFormSub payApplFormSub : payApplFormSubList) {
				if (payApplFormSub.getFormOrdrNum() != null) {
					map.put("ordrNum", payApplFormSub.getFormOrdrNum());// 序号
					PursOrdrSub pursOrdrSub = pursOrdrSubDao.selectUnApplPayQtyByOrdrNum(map);// 根据来源子表序号查询未申请付款数量
					if (pursOrdrSub.getUnApplPayAmt() != null) {
						if (pursOrdrSub.getUnApplPayAmt().compareTo(payApplFormSub.getAmt()) == 1
								|| pursOrdrSub.getUnApplPayAmt().compareTo(payApplFormSub.getAmt()) == 0) {
//								map.put("unApplPayQty",payApplFormSub.getQty() );//修改未申请付款数量
							map.put("unApplPayAmt", payApplFormSub.getAmt());// 修改未申请付款金额
							pursOrdrSubDao.updateUnApplPayQtyByOrdrNum(map);// 根据来源子表序号查询采购订单子表的未付款数量和未付款金额
						} else {
							isSuccess = false;
							message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单中序号为【"
									+ payApplFormSub.getOrdrNum() + "】累计申请付款金额大于订单金额，无法审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单对应的采购订单中未申请付款数量或者金额不存在，无法审核！\n";
						throw new RuntimeException(message);
					}
				}
			}
			int a = payApplFormDao.updatePayApplFormIsNtChk(payApplForm);
			if (a >= 1) {
				isSuccess = true;
				message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单审核成功！\n";
			} else {
				isSuccess = false;
				message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单审核失败！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private Map<String, Object> updatePayApplFormIsNtChkNO(PayApplForm payApplForm) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (payApplFormDao.selectPayApplIsNtChk(payApplForm.getPayApplId()) == 1) {
			if (payApplFormDao.selectPayApplIsNtPay(payApplForm.getPayApplId()) == 1) {
				isSuccess = false;
				message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单已付款，无法弃审！\n";
				throw new RuntimeException(message);
			} else {
				PayApplForm payAppFor = payApplFormDao.selectByPrimaryList(payApplForm.getPayApplId());// 付款申请单主表
				List<PayApplFormSub> payApplFormSubList = payAppFor.getList();// 获取付款申请单子表
				for (PayApplFormSub payApplFormSub : payApplFormSubList) {
					if (payApplFormSub.getFormOrdrNum() != null) {
						map.put("ordrNum", payApplFormSub.getFormOrdrNum());// 序号
						PursOrdrSub pursOrdrSub = pursOrdrSubDao.selectUnApplPayQtyByOrdrNum(map);// 根据来源子表序号查询未申请付款数量
						if (pursOrdrSub.getUnApplPayAmt() != null) {
//							map.put("unApplPayQty",payApplFormSub.getQty().multiply(new BigDecimal(-1)));//修改未申请付款数量
							map.put("unApplPayAmt", payApplFormSub.getAmt().multiply(new BigDecimal(-1)));// 修改未申请付款金额
							pursOrdrSubDao.updateUnApplPayQtyByOrdrNum(map);// 根据来源子表序号查询采购订单子表的未付款数量和未付款金额
						} else {
							isSuccess = false;
							message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单对应的采购订单中未申请付款数量或者金额不存在，无法弃审！\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = payApplFormDao.updatePayApplFormIsNtChk(payApplForm);
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单弃审成功！\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单弃审失败！\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + payApplForm.getPayApplId() + "的付款申请单未审核，不需要弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 分页+排序
	@Override
	public String queryPayApplFormListOrderBy(Map map) {
		String resp = "";
		List<String> provrIdList = getList((String) map.get("provrId"));
		List<String> accNumList = getList((String) map.get("accNum"));
		List<String> userNameList = getList((String) map.get("userName"));
		List<String> deptIdList = getList((String) map.get("deptId"));
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));
		List<String> payrList = getList((String) map.get("payr"));
		List<String> stlSubjList = getList((String) map.get("stlSubj"));
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("userNameList", userNameList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("payrList", payrList);
		map.put("stlSubjList", stlSubjList);

		List<zizhu> applForms = new ArrayList<PayApplFormServiceImpl.zizhu>();

		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "pay_appl_form.pay_appl_dt");
			map.put("sortOrder", "desc");
		}

		applForms = payApplFormDao.selectPayApplFormListOrderBy(map);
		Map tableSums = payApplFormDao.selectPayApplFormListSums(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}

		int count = payApplFormDao.selectPayApplFormCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = applForms.size();
		int pages = count / pageSize;
		if (count % pageSize > 0) {
			pages += 1;
		}
		try {
			resp = BaseJson.returnRespList("purc/PayApplForm/queryPayApplFormListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, applForms, tableSums);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	// 导出接口
	@Override
	public String printPayApplFormList(Map map) {
		String resp = "";
		List<String> provrIdList = getList((String) map.get("provrId"));
		List<String> accNumList = getList((String) map.get("accNum"));
		List<String> userNameList = getList((String) map.get("userName"));
		List<String> deptIdList = getList((String) map.get("deptId"));
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));
		List<String> payrList = getList((String) map.get("payr"));
		List<String> stlSubjList = getList((String) map.get("stlSubj"));
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("userNameList", userNameList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("payrList", payrList);
		map.put("stlSubjList", stlSubjList);

		List<zizhu> applForms = new ArrayList<PayApplFormServiceImpl.zizhu>();

		applForms = payApplFormDao.printPayApplFormList(map);

		int count = payApplFormDao.selectPayApplFormCount(map);

		try {
			resp = BaseJson.returnRespListAnno("purc/PayApplForm/printPayApplFormList", true, "查询成功！", applForms);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	@Override
	public String pushToU8(String ids) throws IOException {
		List<String> idList = getList(ids);

		List<PayApplForm> U8PayApplFormList = payApplFormDao.selectPayApplForms(idList);
		// 表单对象
		U8PayApplFormTable table = new U8PayApplFormTable();
		// 行集合
		ArrayList<U8PayApplForm> rowList = new ArrayList<U8PayApplForm>();
		// 封装对象
		for (PayApplForm main : U8PayApplFormList) {
			rowList.add(encapsulation(main, main.getList()));
		}
		table.setDataRowList(rowList);

		/********** 以下是接口对接************ */
		System.err.println("请求结构::" + JacksonUtil.getXmlStr(table));

		String resultStr = connectToU8("SetPayApp", JacksonUtil.getXmlStr(table), "888");

		System.err.println("返回的XML结构::" + resultStr);

		/*** 请求发送结束 ****/

		MappingIterator<U8PayApplFormResponse> iterator = JacksonUtil.getResponse("SetPayAppResult",U8PayApplFormResponse.class, resultStr);

		ArrayList<String> failedList = new ArrayList<String>();

		StringBuffer message = new StringBuffer();
		int count = 0;
		while (iterator.hasNext()) {
			U8PayApplFormResponse response = (U8PayApplFormResponse) iterator.next();
			System.out.println(response.getInfor() + "!!!!");
			if (response.getType() == 1) {
				failedList.add(response.getDscode());
			} else {
				count++;
			}
		}
		message.append("共" + count + "张单据上传成功!" + '\n');
		if (failedList.size() > 0) {
			message.append("共" + failedList.size() + "张单据上传失败!" + "\n" + "失败单号为: " + failedList.toString());
		}

		String resp = BaseJson.returnRespList("url://", true, message.toString(), null);

		return resp;

	}

	private U8PayApplForm encapsulation(PayApplForm payApplForm, List<PayApplFormSub> U8PayApplFormSubList) {

		// 行对象-->单条发票
		U8PayApplForm dataRow = new U8PayApplForm();
		// 主表
		dataRow.setDscode(payApplForm.getProvrOrdrNum());
		dataRow.setCsscode("01");
		dataRow.setDdate(payApplForm.getPayApplDt());
		dataRow.setCdepcode(payApplForm.getDeptId());
		dataRow.setCvencode(payApplForm.getProvrId());
		BigDecimal[] ipaymoney =new BigDecimal[] {new BigDecimal(0)};
		
		dataRow.setCaccount("6222222222222222");
		dataRow.setCbank("ABC");
		dataRow.setCpersoncode("1005");
		dataRow.setRemark(payApplForm.getMemo());
		// 子表集合
		ArrayList<U8PayApplFormSub> detailsList = new ArrayList<U8PayApplFormSub>();
		// 循环添加子表
		U8PayApplFormSubList.forEach(item -> {
			U8PayApplFormSub details = new U8PayApplFormSub();
			details.setDdcode(item.getActlPayTm());
			details.setDpprepaydate(item.getExpctPayDt());
			details.setIpayAmt(item.getAmt());
		
			ipaymoney[0]=ipaymoney[0].add(Optional.ofNullable(item.getAmt()).orElse(new BigDecimal(0.00)));
			
			detailsList.add(details);
		});
		dataRow.setIpaymoney(ipaymoney[0]);// 付款金额合计
		dataRow.setSubList(detailsList);
		return dataRow;
	}

	private String connectToU8(String methodName, String dataXmlStr, String ztCodeStr) {
		String resultStr = "";
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			String url = "http://106.14.183.228:8081/YBService.asmx";
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://tempuri.org/" + methodName);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			/*
			 * 指定命名空间，参数： uri--即为wsdl文档的targetNamespace，命名空间 perfix--可不填
			 */
			OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
			// 指定方法
			OMElement method = fac.createOMElement(methodName, omNs);
			// 指定方法的参数
			OMElement dataXml = fac.createOMElement("dataXml", omNs);
			OMElement ztCode = fac.createOMElement("ztCode", omNs);
			dataXml.setText(dataXmlStr);
			ztCode.setText(ztCodeStr);

			method.addChild(dataXml);
			method.addChild(ztCode);
			method.build();

			// 远程调用web服务
			OMElement result = serviceClient.sendReceive(method);
			resultStr = result.toString();
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();

		}
		return resultStr;
	}
}
