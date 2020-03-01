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

/** �������뵥 */
@Transactional
@Service
public class PayApplFormServiceImpl implements PayApplFormService {
	@Autowired
	private PayApplFormDao payApplFormDao;
	@Autowired
	private PayApplFormSubDao payApplFormSubDao;
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;
	// ������
	@Autowired
	GetOrderNo getOrderNo;

	// ������
	public static class zizhu {
		/**
		 * �������
		 */
		@JsonProperty("�������")
		public String payApplId;

		/**
		 * ��������
		 */
		@JsonProperty("��������")
		public String payApplDt;

		/**
		 * ��Ӧ�̱��
		 */
		@JsonProperty("��Ӧ�̱��")
		public String provrId;

		/**
		 * �������ͱ���
		 */
		@JsonProperty("�������ͱ���")
		public String formTypEncd;
		/**
		 * ��Դ�������ͱ���
		 */
		@JsonProperty("��Դ�������ͱ���")
		public String toFormTypEncd;
		/**
		 * �û����
		 */
		@JsonProperty("�û����")
		public String accNum;

		/**
		 * �û�����
		 */
		@JsonProperty("�û�����")
		public String userName;

		/**
		 * ���ű���
		 */
		@JsonProperty("���ű���")
		public String deptId;

		/**
		 * ��Ӧ�̶�����
		 */
		@JsonProperty("��Ӧ�̶�����")
		public String provrOrdrNum;

		/**
		 * �Ƿ񸶿�
		 */
		@JsonProperty("�Ƿ񸶿�")
		public Integer isNtPay;

		/**
		 * ������
		 */
		@JsonProperty("������")
		public String payr;

		/**
		 * �����Ŀ
		 */
		@JsonProperty("�����Ŀ")
		public String stlSubj;

		/**
		 * Ԥ�������
		 */
		@JsonProperty("Ԥ�������")
		public BigDecimal prepyMoneyBal;

		/**
		 * Ӧ�������
		 */
		@JsonProperty("Ӧ�������")
		public BigDecimal acctPyblBal;

		/**
		 * �Ƿ����
		 */
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;

		/**
		 * �����
		 */
		@JsonProperty("�����")
		public String chkr;

		/**
		 * ���ʱ��
		 */
		@JsonProperty("���ʱ��")
		public String chkTm;

		/**
		 * ������
		 */
		@JsonProperty("������")
		public String setupPers;

		/**
		 * ����ʱ��
		 */
		@JsonProperty("����ʱ��")
		public String setupTm;

		/**
		 * �޸���
		 */
		@JsonProperty("�޸���")
		public String mdfr;

		/**
		 * �޸�ʱ��
		 */
		@JsonProperty("�޸�ʱ��")
		public String modiTm;

		/**
		 * ��ע
		 */
		@JsonProperty("��ע")
		public String memo;
		/**
		 * ���㷽ʽ
		 */
		@JsonProperty("���㷽ʽ")
		public String stlMode;
		/**
		 * �ɹ���������
		 */
		@JsonProperty("�ɹ���������")
		public String pursOrdrId;
		private static final long serialVersionUID = 1L;

		// ������ѯ��Ӧ�����ơ��û����ơ��������ơ��ɹ���������
		@JsonProperty("��Ӧ������")
		public String provrNm;// ��Ӧ������
		@JsonProperty("��������")
		public String deptName;// ��������
		@JsonProperty("������������")
		public String formTypName;// ������������
		// �ӱ�
		/**
		 * ���
		 */
		@JsonProperty("��")
		public Long ordrNum;

		/**
		 * �ƻ���������
		 */
		@JsonProperty("�ƻ���������")
		public String expctPayDt;

		/**
		 * ����
		 */
		@JsonProperty("����")
		public BigDecimal qty;

		/**
		 * ��Դ���ݺ�
		 */
		@JsonProperty("��Դ���ݺ�")
		public String srcFormNum;

		/**
		 * ԭ������������
		 */
		@JsonProperty("ԭ������������")
		public BigDecimal orgnlSnglCurrApplAmt;

		/**
		 * ���
		 */
		@JsonProperty("���")
		public BigDecimal amt;

		/**
		 * ��Դ�ӱ����
		 */
		@JsonProperty("������Դ�ӱ����")
		public Long formOrdrNum;

		/**
		 * ʵ�ʸ���ʱ��
		 */
		@JsonProperty("ʵ�ʸ���ʱ��")
		public String actlPayTm;

		/**
		 * �йر���
		 */
		@JsonProperty(" �йر���")
		public String lineClosPers;

		@JsonProperty("��Դ�ӱ����")
		public Long toOrdrNum;// ��Դ�ӱ����
	}

	// �����������뵥
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
				message = "���" + number + "�Ѵ��ڣ����������룡";
			} else {
				payApplForm.setPayApplId(number);// �����ȡ������
				for (PayApplFormSub payTab : payApplFormSubList) {
					payTab.setPayApplId(payApplForm.getPayApplId());// �����ݺ�
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
				message = "�����ɹ���";
			}
			resp = BaseJson.returnRespObj("purc/PayApplForm/addPayApplForm", isSuccess, message, payApplForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("���ɶ������! ʧ��");
		} // ��ȡ����

		return resp;
	}

	// �޸ĸ������뵥
	@Override
	public String editPayApplForm(PayApplForm payApplForm, List<PayApplFormSub> payApplFormSubList) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			PayApplForm pay = payApplFormDao.selectByPrimaryKey(payApplForm.getPayApplId());
			if (pay.getIsNtChk() == null) {
				throw new RuntimeException("�������״̬�쳣");
			} else if (pay.getIsNtChk() == 1) {
				throw new RuntimeException("��������˲������޸�");
			}
			for (PayApplFormSub payTab : payApplFormSubList) {
				payTab.setPayApplId(payApplForm.getPayApplId());// �����ݺ�
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
			message = "���³ɹ���";

			resp = BaseJson.returnRespObj("purc/PayApplForm/editPayApplForm", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�޸�ʧ��" + e.getMessage());

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
						message = "ɾ���ɹ���" + lists.toString();
					} catch (Exception e) {
						isSuccess = false;
						message = "���ݺ�Ϊ" + lists.toString() + "�h��ʧ��!";
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						e.printStackTrace();
					}
				}

				if (lists2.size() > 0) {
					message = message + "\r�������" + lists2;
				}
			} else {
				isSuccess = false;
				message = "���" + payApplId + "�����ڣ�";
			}

			resp = BaseJson.returnRespObj("purc/PayApplForm/deleteEntrsAgnAdj", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ɾ��ʧ��");
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
				message = "û�иõ��ݺţ���ѯʧ��";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/PayApplForm/queryPayApplForm", isSuccess, message, applForm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("��ѯ ʧ��");

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
			resp = BaseJson.returnRespList("purc/PayApplForm/queryPayApplFormList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, applForms);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
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
				System.out.println("��� 999999999999");
				message.append(updatePayApplFormIsNtChkOK(payApplForm).get("message"));
			} else if (payApplForm.getIsNtChk() == 0) {
				System.out.println("���� 999999999999");
				message.append(updatePayApplFormIsNtChkNO(payApplForm).get("message"));
			} else {
				isSuccess = false;
				message.append("���״̬�����޷���ˣ�\n");
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
			PayApplForm payAppFor = payApplFormDao.selectByPrimaryList(payApplForm.getPayApplId());// �������뵥����
			List<PayApplFormSub> payApplFormSubList = payAppFor.getList();// ��ȡ�������뵥�ӱ�
			for (PayApplFormSub payApplFormSub : payApplFormSubList) {
				if (payApplFormSub.getFormOrdrNum() != null) {
					map.put("ordrNum", payApplFormSub.getFormOrdrNum());// ���
					PursOrdrSub pursOrdrSub = pursOrdrSubDao.selectUnApplPayQtyByOrdrNum(map);// ������Դ�ӱ���Ų�ѯδ���븶������
					if (pursOrdrSub.getUnApplPayAmt() != null) {
						if (pursOrdrSub.getUnApplPayAmt().compareTo(payApplFormSub.getAmt()) == 1
								|| pursOrdrSub.getUnApplPayAmt().compareTo(payApplFormSub.getAmt()) == 0) {
//								map.put("unApplPayQty",payApplFormSub.getQty() );//�޸�δ���븶������
							map.put("unApplPayAmt", payApplFormSub.getAmt());// �޸�δ���븶����
							pursOrdrSubDao.updateUnApplPayQtyByOrdrNum(map);// ������Դ�ӱ���Ų�ѯ�ɹ������ӱ��δ����������δ������
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥�����Ϊ��"
									+ payApplFormSub.getOrdrNum() + "���ۼ����븶������ڶ������޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥��Ӧ�Ĳɹ�������δ���븶���������߽����ڣ��޷���ˣ�\n";
						throw new RuntimeException(message);
					}
				}
			}
			int a = payApplFormDao.updatePayApplFormIsNtChk(payApplForm);
			if (a >= 1) {
				isSuccess = true;
				message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥��˳ɹ���\n";
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥���ʧ�ܣ�\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥����ˣ�����Ҫ�ظ����\n";
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
				message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥�Ѹ���޷�����\n";
				throw new RuntimeException(message);
			} else {
				PayApplForm payAppFor = payApplFormDao.selectByPrimaryList(payApplForm.getPayApplId());// �������뵥����
				List<PayApplFormSub> payApplFormSubList = payAppFor.getList();// ��ȡ�������뵥�ӱ�
				for (PayApplFormSub payApplFormSub : payApplFormSubList) {
					if (payApplFormSub.getFormOrdrNum() != null) {
						map.put("ordrNum", payApplFormSub.getFormOrdrNum());// ���
						PursOrdrSub pursOrdrSub = pursOrdrSubDao.selectUnApplPayQtyByOrdrNum(map);// ������Դ�ӱ���Ų�ѯδ���븶������
						if (pursOrdrSub.getUnApplPayAmt() != null) {
//							map.put("unApplPayQty",payApplFormSub.getQty().multiply(new BigDecimal(-1)));//�޸�δ���븶������
							map.put("unApplPayAmt", payApplFormSub.getAmt().multiply(new BigDecimal(-1)));// �޸�δ���븶����
							pursOrdrSubDao.updateUnApplPayQtyByOrdrNum(map);// ������Դ�ӱ���Ų�ѯ�ɹ������ӱ��δ����������δ������
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥��Ӧ�Ĳɹ�������δ���븶���������߽����ڣ��޷�����\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = payApplFormDao.updatePayApplFormIsNtChk(payApplForm);
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥����ɹ���\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥����ʧ�ܣ�\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + payApplForm.getPayApplId() + "�ĸ������뵥δ��ˣ�����Ҫ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ��ҳ+����
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
			resp = BaseJson.returnRespList("purc/PayApplForm/queryPayApplFormListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, applForms, tableSums);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	// �����ӿ�
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
			resp = BaseJson.returnRespListAnno("purc/PayApplForm/printPayApplFormList", true, "��ѯ�ɹ���", applForms);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return resp;
	}

	@Override
	public String pushToU8(String ids) throws IOException {
		List<String> idList = getList(ids);

		List<PayApplForm> U8PayApplFormList = payApplFormDao.selectPayApplForms(idList);
		// ������
		U8PayApplFormTable table = new U8PayApplFormTable();
		// �м���
		ArrayList<U8PayApplForm> rowList = new ArrayList<U8PayApplForm>();
		// ��װ����
		for (PayApplForm main : U8PayApplFormList) {
			rowList.add(encapsulation(main, main.getList()));
		}
		table.setDataRowList(rowList);

		/********** �����ǽӿڶԽ�************ */
		System.err.println("����ṹ::" + JacksonUtil.getXmlStr(table));

		String resultStr = connectToU8("SetPayApp", JacksonUtil.getXmlStr(table), "888");

		System.err.println("���ص�XML�ṹ::" + resultStr);

		/*** �����ͽ��� ****/

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
		message.append("��" + count + "�ŵ����ϴ��ɹ�!" + '\n');
		if (failedList.size() > 0) {
			message.append("��" + failedList.size() + "�ŵ����ϴ�ʧ��!" + "\n" + "ʧ�ܵ���Ϊ: " + failedList.toString());
		}

		String resp = BaseJson.returnRespList("url://", true, message.toString(), null);

		return resp;

	}

	private U8PayApplForm encapsulation(PayApplForm payApplForm, List<PayApplFormSub> U8PayApplFormSubList) {

		// �ж���-->������Ʊ
		U8PayApplForm dataRow = new U8PayApplForm();
		// ����
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
		// �ӱ���
		ArrayList<U8PayApplFormSub> detailsList = new ArrayList<U8PayApplFormSub>();
		// ѭ������ӱ�
		U8PayApplFormSubList.forEach(item -> {
			U8PayApplFormSub details = new U8PayApplFormSub();
			details.setDdcode(item.getActlPayTm());
			details.setDpprepaydate(item.getExpctPayDt());
			details.setIpayAmt(item.getAmt());
		
			ipaymoney[0]=ipaymoney[0].add(Optional.ofNullable(item.getAmt()).orElse(new BigDecimal(0.00)));
			
			detailsList.add(details);
		});
		dataRow.setIpaymoney(ipaymoney[0]);// ������ϼ�
		dataRow.setSubList(detailsList);
		return dataRow;
	}

	private String connectToU8(String methodName, String dataXmlStr, String ztCodeStr) {
		String resultStr = "";
		try {
			ServiceClient serviceClient = new ServiceClient();
			// ���������ַWebService��URL,ע�ⲻ��WSDL��URL
			String url = "http://106.14.183.228:8081/YBService.asmx";
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// ȷ�����÷�����wsdl �����ռ��ַ (wsdl�ĵ��е�targetNamespace) �� �������� ����ϣ�
			options.setAction("http://tempuri.org/" + methodName);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			/*
			 * ָ�������ռ䣬������ uri--��Ϊwsdl�ĵ���targetNamespace�������ռ� perfix--�ɲ���
			 */
			OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
			// ָ������
			OMElement method = fac.createOMElement(methodName, omNs);
			// ָ�������Ĳ���
			OMElement dataXml = fac.createOMElement("dataXml", omNs);
			OMElement ztCode = fac.createOMElement("ztCode", omNs);
			dataXml.setText(dataXmlStr);
			ztCode.setText(ztCodeStr);

			method.addChild(dataXml);
			method.addChild(ztCode);
			method.build();

			// Զ�̵���web����
			OMElement result = serviceClient.sendReceive(method);
			resultStr = result.toString();
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();

		}
		return resultStr;
	}
}
