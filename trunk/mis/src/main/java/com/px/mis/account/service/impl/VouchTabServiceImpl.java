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
	private VouchCateDocDao vouchCateDocDao; // ƾ֤���
	@Autowired
	private GetOrderNo getOrderNo; // ���ɵ���
	@Autowired
	private FormBookEntryDao formBookEntryDao; // ���˱�
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; // �����ӱ�
	@Autowired
	private VouchTabSubDao vouchTabSubDao; // ƾ֤�ӱ�

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
			on.put("message", "��Ŀ���" + vouchTab.getSubjId() + "�����ڣ�����ʧ�ܣ�");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�Է���Ŀ���" + vouchTab.getCntPtySubjId() + "�����ڣ�����ʧ�ܣ�");
		} else if (deptDocDao.selectDeptDocByDeptEncd(vouchTab.getDeptId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "���ű��" + vouchTab.getDeptId() + "�����ڣ�����ʧ�ܣ�");
		} else if (custDocDao.selectCustDocByCustId(vouchTab.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�ͻ����" + vouchTab.getCustId() + "�����ڣ�����ʧ�ܣ�");
		} else if (provrDocDao.selectProvrDocByProvrId(vouchTab.getProvrId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "��Ӧ�̱��" + vouchTab.getProvrId() + "�����ڣ�����ʧ�ܣ�");
		} else {
			int insertResult = vouchTabDao.insertVouchTab(vouchTab);
			if (insertResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			} else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
			on.put("message", "����ʧ��,��������Ϊ��");
		} else if (vouchTabDao.selectVouchTabByOrdrNum(ordrNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����" + ordrNum + "�����ڣ�");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getSubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "��Ŀ���" + vouchTab.getSubjId() + "�����ڣ�����ʧ�ܣ�");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(vouchTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�Է���Ŀ���" + vouchTab.getCntPtySubjId() + "�����ڣ�����ʧ�ܣ�");
		} else if (deptDocDao.selectDeptDocByDeptEncd(vouchTab.getDeptId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "���ű��" + vouchTab.getDeptId() + "�����ڣ�����ʧ�ܣ�");
		} else if (custDocDao.selectCustDocByCustId(vouchTab.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�ͻ����" + vouchTab.getCustId() + "�����ڣ�����ʧ�ܣ�");
		} else if (provrDocDao.selectProvrDocByProvrId(vouchTab.getProvrId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "��Ӧ�̱��" + vouchTab.getProvrId() + "�����ڣ�����ʧ�ܣ�");
		} else {
			int updateResult = vouchTabDao.updateVouchTabByOrdrNum(vouchTab);
			if (updateResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "���³ɹ�");
			} else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
			on.put("message", "ɾ��ʧ�ܣ����" + ordrNum + "�����ڣ�");
		} else {
			int deleteResult = vouchTabDao.deleteVouchTabByOrdrNum(ordrNum);
			if (deleteResult == 1) {
				on.put("isSuccess", true);
				on.put("message", "ɾ���ɹ�");
			} else if (deleteResult == 0) {
				on.put("isSuccess", true);
				on.put("message", "û��Ҫɾ��������");
			} else {
				on.put("isSuccess", false);
				on.put("message", "ɾ��ʧ��");
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
		resp = BaseJson.returnRespList("/account/vouchTab/selectVouchTab", true, "��ѯ�ɹ���", list);
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
		// ��ѯƾ֤
		// ��������
		String resp = "";
		String vouchId = (String) map.get("comnVouchId");
		List<String> vouchList = strToList(vouchId);
		map.put("vouchList", vouchList);
		List<VouchTabSub> dataList = vouchTabSubDao.selectVouchTabSubListByMap(map);
		try {
			resp = BaseJson.returnRespList("/account/vouchTab/selectVouchTab", true, "��ѯ�ɹ���", dataList);
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
		String comnVouchComnt = (String) map.get("comnVouchComnt"); // ժҪ
		String loginTime = (String) map.get("loginTime");
		String userName = (String) map.get("userName");
		String accNum = (String) map.get("accNum");
		// List<FormBookEntrySub> paltOrdersList = (List)map.get("paltOrdersList");
		List<VouchTabSub> paltOrdersList = (List) map.get("paltOrdersList");
		List<VouchTabSub> tabList = new ArrayList<>();

		// �ж�ƾ֤����ڵ����ƿ�Ŀ
		VouchCateDoc vouchCateDoc = vouchCateDocDao.selectVouchCateDocByVouchCateWor(vouchCateWor);
		Boolean isLmt = true;
		if (vouchCateDoc != null) {
			Map<String, Object> docMap = judgeVouchCate(vouchCateDoc, isLmt);
			Map<String, Object> debitMap = (Map<String, Object>) docMap.get("debitMap");// �跽����
			Map<String, Object> crdtMap = (Map<String, Object>) docMap.get("crdtMap"); // ��������
			Map<String, Object> voucNoMap = (Map<String, Object>) docMap.get("voucNoMap"); // ƾ֤����
			Map<String, Object> voucMap = (Map<String, Object>) docMap.get("voucMap"); // ƾ֤����

			isLmt = (Boolean) docMap.get("isLmt");
			if (isLmt) {
				if (paltOrdersList.size() > 0) {

					List<VouchTabSub> list = mergedTabSubject(paltOrdersList);

					for (VouchTabSub sub : list) {

						String debitId = sub.getSubDebitId(); // �跽��Ŀ����
						String crdtId = sub.getSubDebitId(); // ������Ŀ����

						// �ж��Ƿ��и��������Ŀ
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

						// �Ƿ���ĩ����Ŀ
						int de = acctItmDocDao.selectIsFinalSubj(debitId);
						int cr = acctItmDocDao.selectIsFinalSubj(crdtId);
						if (de != 0 && cr != 0) {
							message = "��Ŀ��ĩ��";
							isSuccess = false;
							logger.info("����ƾ֤�쳣����Ŀ��ĩ��");
							return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
									null);
						}

						// ƾ֤����
						if (!voucNoMap.isEmpty()) {
							if (voucNoMap.containsKey(debitId) || voucNoMap.containsKey(crdtId)) {
								message = "�跽�������Ŀ������ѡ��Ŀ";
								isSuccess = false;
								logger.info("����ƾ֤�쳣���跽�������Ŀ������ѡ��Ŀ");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}

						// ��������
						if (!crdtMap.isEmpty()) {
							if (!crdtMap.containsKey(crdtId)) {
								message = "������Ŀ����������";
								isSuccess = false;
								logger.info("����ƾ֤�쳣��������Ŀ����������");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}
						// �跽���� ��Ŀ
						if (!debitMap.isEmpty()) {
							if (!debitMap.containsKey(debitId)) {
								message = "�跽��Ŀ����������";
								isSuccess = false;
								logger.info("����ƾ֤�쳣���跽��Ŀ����������");
								return BaseJson.returnRespObj("account/vouchTab/voucher/generate", isSuccess, message,
										null);
							} else {
								tabList.add(sub);

							}
						}

						// ƾ֤����
						if (!voucMap.isEmpty()) {
							if (!voucMap.containsKey(debitId) && !voucMap.containsKey(crdtId)) {
								message = "�跽�������Ŀ������ѡ��Ŀ";
								isSuccess = false;
								logger.info("����ƾ֤�쳣���跽�������Ŀ������ѡ��Ŀ");
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
			message = "�Ƶ��ɹ�";
		} else {
			message = "�Ƶ�ʧ��";
		}
		return BaseJson.returnRespObj("/account/vouchTab/selectVouchTab", isSuccess, message, null);
	}

	private Map<String, Object> selectIsAid(AcctItmDoc debitDoc, VouchTabSub sub, String debitId) throws IOException {

		boolean isSuccess = true;
		String message = "";
		Map<String, Object> map = new HashMap<>();
		if (debitDoc.getIsNtDeptAccti() != null && debitDoc.getIsNtDeptAccti() == 1) {
			// ���Ÿ���
			if (StringUtils.isEmpty(sub.getDeptId())) {
				isSuccess = false;
				message = "��Ŀ���룺" + debitId + "��Ҫ���Ÿ������㣬���ݺţ�" + sub.getFormNum() + "ȱ�ٲ�����Ϣ";

			}
		}
		if (debitDoc.getIsNtCustRecoAccti() != null && debitDoc.getIsNtCustRecoAccti() == 1) {
			// �ͻ�����
			if (StringUtils.isEmpty(sub.getCustId())) {
				isSuccess = false;
				message = "��Ŀ���룺" + debitId + "��Ҫ�ͻ��������㣬���ݺţ�" + sub.getFormNum() + "ȱ�ٿͻ���Ϣ";

			}
		}
		if (debitDoc.getIsNtProvrRecoAccti() != null && debitDoc.getIsNtProvrRecoAccti() == 1) {
			// ��Ӧ�̸���
			if (StringUtils.isEmpty(sub.getProvrId())) {
				isSuccess = false;
				message = "��Ŀ���룺" + debitId + "��Ҫ��Ӧ�̸������㣬���ݺţ�" + sub.getFormNum() + "ȱ�ٹ�Ӧ����Ϣ";

			}
		}
		if (debitDoc.getIsNtProjAccti() != null && debitDoc.getIsNtProjAccti() == 1) {
			// ��Ŀ����
			if (StringUtils.isEmpty(sub.getProjEncd())) {
				isSuccess = false;
				message = "��Ŀ���룺" + debitId + "��Ҫ��Ŀ�������㣬���ݺţ�" + sub.getFormNum() + "ȱ����Ŀ��Ϣ";

			}
		}
		if (debitDoc.getIsNtIndvRecoAccti() != null && debitDoc.getIsNtIndvRecoAccti() == 1) {
			// ��������-����
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
			String debitId = sub.getSubDebitId(); // �跽��Ŀ����
			String crdtId = sub.getSubDebitId(); // ������Ŀ����

			// �ж��Ƿ��и��������Ŀ
			AcctItmDoc debitDoc = acctItmDocDao.selectAcctItmDocBySubjId(debitId);
			AcctItmDoc crdtDoc = acctItmDocDao.selectAcctItmDocBySubjId(crdtId);
			if (debitDoc.getIsNtProjAccti() != null && debitDoc.getIsNtProjAccti() == 1) {
				// ��Ŀ����
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
	 * ƾ֤�ӱ���Ϣ
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
			// ��ѯ�Ƿ�������ƾ֤
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
			// ��ѯ���һ��ƾ֤���ƾ֤��
			VouchTab last = vouchTabDao.selectLastTab();
			String comnVouchId = getOrderNo.getSeqNo("VT", accNum, loginTime);
			int tabNum = 0;
			if (last != null) {
				tabNum = last.getTabNum() + 1;

			} else {
				tabNum = 1;
			}

			List<VouchTabSub> tabSubList = new ArrayList<>();
			List<FormBookEntry> formBookList = new ArrayList<>(); // ����ⵥ��
			List<FormBookEntry> groupFormList = new ArrayList<>(); // ��ϵ���
			List<FormBookEntry> sellSnglList = new ArrayList<>(); // ���۵�
			List<FormBookEntry> pursInvMasList = new ArrayList<>(); // �ɹ���Ʊ
			List<FormBookEntry> sellInvMasList = new ArrayList<>(); // ���۷�Ʊ
			List<FormBookEntry> intoWhsAdjSnglList = new ArrayList<>(); // ��������
			List<FormBookEntry> outWhsAdjSnglList = new ArrayList<>(); // ���������
			List<FormBookEntry> formBackFlushList = new ArrayList<>(); // �س嵥
			List<FormBookEntry> rtnGoodsList = new ArrayList<>(); // �˻���
			List<FormBookEntry> entrsAgnDelvList = new ArrayList<>(); // ί�д���������
			List<FormBookEntry> entrsAgnDelvRtnList = new ArrayList<>(); // ί�д����˻���

			FormBookEntry form = new FormBookEntry();

			VouchTab tab = new VouchTab(); // ƾ֤
			tab.setImported(0); // �Ƿ�������
			tab.setComnVouchId(comnVouchId); // ƾ֤���
			tab.setTabNum(tabNum);
			if (StringUtils.isNotEmpty(comnVouchComnt)) {
				tab.setComnVouchComnt(comnVouchComnt);
			} else {
				tab.setComnVouchComnt(tabList.get(0).getFormTypName() + "-" + "�Ƶ�");
			}

			tab.setVouchCateWor(vouchCateWor); // ƾ֤�����
			tab.setAccNum(accNum); // �Ƶ���
			tab.setUserName(userName);
			tab.setCtime(loginTime); // �Ƶ�ʱ��
			tab.setSubjId(tabList.get(0).getSubDebitId());
			tab.setSubjNm(tabList.get(0).getSubDebitNm());
			tab.setCntPtySubjId(tabList.get(0).getSubCreditId());
			tab.setCntPtySubjNm(tabList.get(0).getSubCreditNm());

			// �跽
			tab.setQtyDebit(Optional.ofNullable(tab.getQtyDebit()).orElse(new BigDecimal(0.00000000)));
			tab.setDebitAmt(Optional.ofNullable(tab.getDebitAmt()).orElse(new BigDecimal(0.00000000)));
			// ����
			tab.setQtyCrdt(Optional.ofNullable(tab.getQtyCrdt()).orElse(new BigDecimal(0.00000000)));
			tab.setCrdtAmt(Optional.ofNullable(tab.getCrdtAmt()).orElse(new BigDecimal(0.00000000)));
			tabMap = new HashMap<>();

			for (VouchTabSub sub : tabList) {
				String formType = sub.getFormTypEncd();
				sub.setComnVouchId(comnVouchId);

				// ����-��װ-��ж
				if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
					VouchTabSub tabSub = new VouchTabSub();

					if (tabMap.containsKey(sub.getFormNum())) {
						tabSub = (VouchTabSub) tabMap.get(sub.getFormNum());
						tabSub.setComnVouchId(comnVouchId);
						// ��
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

					// �ɹ�-����-���������
					// ���۵�
					// �ɹ���Ʊ
					// ���۷�Ʊ(ר��/��ͨ)
					// ��������
					// ���������
					// ���ֻس嵥
					// ���ֻس嵥
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

				// ����ⵥ��
				if (formType.equals("004") || formType.equals("009") || formType.equals("014") || formType.equals("015")
						|| formType.equals("028")) {

					formBookList.add(form);

				}
				// ����-��װ-��ж
				if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
					form.setSellOrdrInd(sub.getFormNum());
					groupFormList.add(form);
				}
				// ���۵�
				if (formType.equals("007")) {
					sellSnglList.add(form);
				}

				// �ɹ���Ʊ
				if (formType.equals("019") || formType.equals("20")) {
					pursInvMasList.add(form);
				}
				// ���۷�Ʊ(ר��/��ͨ)
				if (formType.equals("021") || formType.equals("022")) {
					sellInvMasList.add(form);
				}
				// ��������
				if (formType.equals("030")) {
					intoWhsAdjSnglList.add(form);
				}
				// ���������
				if (formType.equals("031")) {
					outWhsAdjSnglList.add(form);
				}
				// ���ֻس嵥
				if (formType.equals("016")) {
					formBackFlushList.add(form);
				}
				// ���ֻس嵥
				if (formType.equals("017")) {
					formBackFlushList.add(form);
				}
				// �˻���
				if (formType.equals("008")) {
					rtnGoodsList.add(form);
				}
				// ί�д���������
				if (formType.equals("023")) {

					entrsAgnDelvList.add(form);
				}
				// ί�д����˻���
				if (formType.equals("024")) {
					entrsAgnDelvRtnList.add(form);
				}

			}
			if (tabSubList.size() > 0) {

				// �����޸���ص���

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

				// ��������ƾ֤�ӱ�
				vouchTabSubDao.insertList(tabSubList);
				// ƾ֤����
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
	 * ���ݵ����������ֵ���
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
		// ����ⵥ��
		if (formType.equals("004") || formType.equals("009") || formType.equals("014") || formType.equals("015")
				|| formType.equals("028")) {

			formBookList.add(form);

		}
		// ����-��װ-��ж
		if (formType.equals("011") || formType.equals("012") || formType.equals("013")) {
			form.setSellOrdrInd(sub.getFormNum());
			groupFormList.add(form);
		}
		// ���۵�
		if (formType.equals("007")) {
			sellSnglList.add(form);
		}

		// �ɹ���Ʊ
		if (formType.equals("019") || formType.equals("20")) {
			pursInvMasList.add(form);
		}
		// ���۷�Ʊ(ר��/��ͨ)
		if (formType.equals("021") || formType.equals("022")) {
			sellInvMasList.add(form);
		}
		// ��������
		if (formType.equals("030")) {
			intoWhsAdjSnglList.add(form);
		}
		// ���������
		if (formType.equals("031")) {
			outWhsAdjSnglList.add(form);
		}
		// ���ֻس嵥
		if (formType.equals("016")) {
			formBackFlushList.add(form);
		}
		// ���ֻس嵥
		if (formType.equals("017")) {
			formBackFlushList.add(form);
		}
		// �˻���
		if (formType.equals("008")) {
			rtnGoodsList.add(form);
		}
		// ί�д���������
		if (formType.equals("023")) {

			entrsAgnDelvList.add(form);
		}
		// ί�д����˻���
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
	 * ���ݵ������ͽ�����������
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
	 * �ж�ƾ֤����ֵ����ƿ�Ŀ
	 * 
	 * @param vouchCateDoc
	 * @param isLmt
	 * @return
	 */
	private Map<String, Object> judgeVouchCate(VouchCateDoc vouchCateDoc, boolean isLmt) {
		Map<String, Object> debitMap = new HashMap<>(); // �跽����
		Map<String, Object> crdtMap = new HashMap<>(); // ��������
		Map<String, Object> voucNoMap = new HashMap<>(); // ƾ֤����
		Map<String, Object> voucMap = new HashMap<>(); // ƾ֤����
		// �ж����ƿ�Ŀ
		Map<String, Object> docMap = new HashMap<>();

		if (vouchCateDoc.getLmtSubjList().size() != 0) {

			if (vouchCateDoc.getLmtMode().contains("ƾ֤����")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {
					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						voucNoMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("��������")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {

					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						crdtMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("�跽����")) {
				for (VouchCateDocSubTab vouc : vouchCateDoc.getLmtSubjList()) {

					List<String> subjIdList = strToList(vouc.getLmtSubjId());
					for (String subjId : subjIdList) {
						debitMap.put(subjId, vouc);
					}

				}
			} else if (vouchCateDoc.getLmtMode().contains("ƾ֤����")) {
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
				List<FormBookEntry> formBookList = new ArrayList<>(); // ����ⵥ��
				List<FormBookEntry> groupFormList = new ArrayList<>(); // ��ϵ�
				List<FormBookEntry> sellSnglList = new ArrayList<>(); // ���۵�
				List<FormBookEntry> pursInvMasList = new ArrayList<>(); // �ɹ���Ʊ
				List<FormBookEntry> sellInvMasList = new ArrayList<>(); // ���۷�Ʊ
				List<FormBookEntry> intoWhsAdjSnglList = new ArrayList<>(); // ��������
				List<FormBookEntry> outWhsAdjSnglList = new ArrayList<>(); // ���������
				List<FormBookEntry> formBackFlushList = new ArrayList<>(); // �س嵥
				List<FormBookEntry> rtnGoodsList = new ArrayList<>(); // �˻���
				List<FormBookEntry> entrsAgnDelvList = new ArrayList<>(); // ί�д���������
				List<FormBookEntry> entrsAgnDelvRtnList = new ArrayList<>(); // ί�д����˻���
				Map<String, Object> dataMap = new HashMap<>();

				FormBookEntry form = new FormBookEntry();

				for (VouchTab tab : vouchTabList) {
					if (tab.getImported() == 1) {
						message += "ƾ֤���:" + tab.getComnVouchId() + "�Ѿ��ϴ�����,�޷�ɾ��";
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

					// ƾ֤ɾ������
					vouchTabDao.insertVouchTabDlList(tabSubList);
					vouchTabSubDao.insertVouchTabSubDlList(tabSubList);
					vouchTabDao.delectVouchTabSubList(tabSubList);
					// ����ƾ֤����
					dataMap.clear();
					dataMap.put("tabSubList", tabSubList);

					List<VouchTab> sortOutList = vouchTabDao.selectVouchTab(dataMap);
					if (sortOutList.size() > 0) {
						for (int i = 1; i <= sortOutList.size(); i++) {
							sortOutList.get(i - 1).setTabNum(i);

						}
						vouchTabDao.updateVouchList(sortOutList);
					}

					message = "ɾ���ɹ�";
					isSuccess = true;
				}

			} else {
				message = "ɾ��ʧ��";
				isSuccess = false;
			}

		} else {
			message = "ɾ��ʧ��";
			isSuccess = false;
		}
		return BaseJson.returnRespObj("/account/vouchTab/selectVouchTab", isSuccess, message, null);
	}

	/**
	 * setƾֵ֤
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
//		//�޸ĵ���
//		if(tabList.size() > 0) {
//			VouchTab dTab = new VouchTab(); //�跽
//			VouchTab cTab = new VouchTab(); //����
//			tabList = mergeSubject(tabList);
//			
//			for(VouchTab tab : tabList) {
//				String num = String.valueOf(vouchTabSubDao.selectVouchTabSubListByMap(map).size());
//				tab.setAcctYr(Integer.valueOf(tab.getCtime().substring(0,4)));
//				tab.setAcctiMth(Integer.valueOf(tab.getCtime().substring(5,7)));
//				tab.setCurrencyName("�����");
//				tab.setAttachedFormNumbers(num);
//				map.put("comnVouchId", tab.getComnVouchId());
//				
//				dTab = new VouchTab(); //�跽		
//				BeanUtils.copyProperties(dTab, tab);			
//				dTab.setCrdtAmt(null);
//				dTab.setQtyCrdt(null);
//				
//				cTab = new VouchTab(); //����
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
//		message = "�����ɹ�";
//	} else {
//		isSuccess = false;
//		message = "����ʧ��,��ѯ�����ѵ���������Ϊ��";
//	}
//		return BaseJson.returnRespListAnno("/account/vouchTab/voucher/export", isSuccess, message, dataList);
//	}
	// ����
	@Override
	public String exportvoucherList(Map map) throws Exception {
		String resp = "";
		List<VouchTab> list = vouchTabDao.selectVouchTabList(map);
		if (list.size() > 0) {
			list = mergeSubject(list);
		}
		resp = BaseJson.returnRespListAnno("/account/vouchTab/voucher/export", true, "��ѯ�ɹ���", list);
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
				message = "ƾ֤�б��в���ƾ֤����Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			} else {
				vouchTabDao.insertVouchTab(entry.getValue());

				isSuccess = true;
				message = "ƾ֤����ɹ���";
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
			// ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			HSSFWorkbook wb0 = new HSSFWorkbook(fileIn);

			// ��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			// ��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			// ��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			// ���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "ƾ֤����");

				VouchTab tab = new VouchTab(); // ƾ֤
				tab.setImported(0); // �Ƿ�������
				if (temp.containsKey(orderNo)) {
					tab = temp.get(orderNo);
				}
				tab.setComnVouchId(orderNo); // ƾ֤���

				tab.setVouchCateWor(GetCellData(r, "ƾ֤�����"));
				tab.setComnVouchComnt(GetCellData(r, "ƾ֤˵��"));
				tab.setAccNum(GetCellData(r, "�Ƶ���"));

				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					// sellComnInv.setSetupTm(null);
					tab.setCtime(GetCellData(r, "�Ƶ�ʱ��"));
				} else {
					tab.setCtime(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				tab.setChkr(GetCellData(r, "�����"));
				tab.setBookOkAcc(GetCellData(r, "������"));
				tab.setMemo(GetCellData(r, "ժҪ"));
				tab.setSubjId(GetCellData(r, "�跽��Ŀ���"));
				tab.setCntPtySubjId(GetCellData(r, "������Ŀ���"));
				tab.setDebitAmt(new BigDecimal(GetCellData(r, "�跽���")));
				tab.setCrdtAmt(new BigDecimal(GetCellData(r, "�������")));
				tab.setQtyDebit(new BigDecimal(GetCellData(r, "�跽����")));
				tab.setQtyCrdt(new BigDecimal(GetCellData(r, "��������")));

				// tab.setSubjId(GetCellData(r, "�跽��Ŀ���"));//��Ŀ���
				// tab.setCntPtySubjId(GetCellData(r, "������Ŀ���")); //�Է���Ŀ���

				tab.setImported(0); // �Ƿ�������
				tab.setImportNm(accNum);// ������
				tab.setImportDt(sf.format(new Date()));
				temp.put(orderNo, tab);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}
}
