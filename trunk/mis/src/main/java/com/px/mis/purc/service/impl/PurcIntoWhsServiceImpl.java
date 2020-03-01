package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.PursOrdrDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.service.IntoWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

/*�ɹ���ⵥ����*/
@Transactional
@Service
public class PurcIntoWhsServiceImpl extends poiTool implements IntoWhsService {
	@Autowired
	private IntoWhsDao iwd;// �����
	@Autowired
	private IntoWhsSubDao iwsd;// �����
	@Autowired
	private InvtyTabDao itd;// ����
	@Autowired
	InvtyGdsBitListMapper bitListMapper;// ��λ���Ŵ����Ӧ��ϵ��
	@Autowired
	InvtyNumMapper invtyNumMapper;//
	// ������
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private InvtyGdsBitListMapper invtyGdsBitListMapper;
	@Autowired
	private ToGdsSnglDao toGdsSnglDao;// ����������
	@Autowired
	private ToGdsSnglSubDao toGdsSnglSubDao;// �������ӱ�
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;// �ɹ���ⵥ�ӱ�
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;// �ɹ������ӱ�
	@Autowired
	private WhsDocMapper whsDocMapper;// �ֿ⵵��
	@Autowired
	private InvtyDocDao invtyDocDao;

	private Logger logger = LoggerFactory.getLogger(PurcIntoWhsServiceImpl.class);

	// �����ɹ���ⵥ
	@Override
	public String addIntoWhs(String userId, IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList, String loginTime)
			throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String number = "";
			// ��ȡ������
			if (intoWhs.getIsNtRtnGood() == 1) {
				number = getOrderNo.getSeqNo("CT", userId, loginTime);
			} else {
				number = getOrderNo.getSeqNo("RK", userId, loginTime);
			}
			if (iwd.selectIntoWhsById(number) != null) {
				message = "���" + number + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				intoWhs.setIntoWhsSnglId(number);
				if (intoWhs.getRecvSendCateId() == "") {
					intoWhs.setRecvSendCateId("1");
				}
				for (IntoWhsSub intoWhsSub : intoWhsSubList) {
					intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(intoWhsSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null) {
						isSuccess = false;
						message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
								+ "û�������Ƿ�ɹ����ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
								+ "�ǿɲɹ�������޷����棡";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
								+ "û�������Ƿ����ڹ������ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (intoWhsSub.getPrdcDt() == "" || intoWhsSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
									+ "�Ǳ����ڹ����������������ڣ�";
							throw new RuntimeException(message);
						}
						if (intoWhsSub.getInvldtnDt() == ""|| intoWhsSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
									+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
							throw new RuntimeException(message);
						}
					}
					if (intoWhsSub.getPrdcDt() == "") {
						intoWhsSub.setPrdcDt(null);
					}
					if (intoWhsSub.getInvldtnDt() == "") {
						intoWhsSub.setInvldtnDt(null);
					}

					intoWhsSub.setUnBllgQty(intoWhsSub.getQty().abs());// Ĭ��δ��Ʊ����=�������
					intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum().abs());// δ��Ʊ���=��˰���
//					intoWhsSub.setUnBllgUprc(intoWhsSub.getPrcTaxSum().abs().divide(intoWhsSub.getQty().abs()));//Ĭ��δ��Ʊ����=��˰���/����
					if (intoWhs.getIsNtRtnGood() == 0) {
						intoWhsSub.setReturnQty(intoWhsSub.getQty());// Ĭ��δ�˻�����=�������
					}
				}
				int a = iwd.insertIntoWhs(intoWhs);
				// ���ظ�ֵ
				int b = iwsd.insertIntoWhsSub(intoWhsSubList);
				if (a >= 1 && b >= 1) {
					message = "�����ɹ���";
					isSuccess = true;
				} else {
					isSuccess = false;
					message = "����ʧ�ܣ�";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/addIntoWhs", isSuccess, message, intoWhs);

		} catch (DuplicateKeyException e) {
			throw new RuntimeException("����ʧ��!,�����Ѵ���");
		} catch (Exception e) {
			throw new RuntimeException("����ʧ��,����ϵ�����鿴");
		}
		return resp;
	}

	// �޸Ĳɹ���ⵥ
	@Override
	public String editIntoWhs(IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<IntoWhsSub> intoWhsSubSet = new TreeSet();
//			intoWhsSubSet.addAll(intoWhsSubList);
//			if(intoWhsSubSet.size() < intoWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", false, "��������ϸ������ͬһ�ֿ��д���ͬһ�������ͬ���Σ�",null);
//				return resp;
//			}
			if (intoWhs.getRecvSendCateId() == "") {
				intoWhs.setRecvSendCateId("1");
			}
			for (IntoWhsSub intoWhsSub : intoWhsSubList) {
				intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(intoWhsSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null) {
					isSuccess = false;
					message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
							+ "û�������Ƿ�ɹ����ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
							+ "�ǿɲɹ�������޷����棡";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
							+ "û�������Ƿ����ڹ������ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (intoWhsSub.getPrdcDt() == "" || intoWhsSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
								+ "�Ǳ����ڹ����������������ڣ�";
						throw new RuntimeException(message);
					}
					if (intoWhsSub.getInvldtnDt() == ""|| intoWhsSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "�òɹ���ⵥ��Ӧ�Ĵ��:" + intoWhsSub.getInvtyEncd()
								+ "�Ǳ����ڹ���������ʧЧ���ڣ�";
						throw new RuntimeException(message);
					}
				}
				if (intoWhsSub.getPrdcDt() == "") {
					intoWhsSub.setPrdcDt(null);
				}
				if (intoWhsSub.getInvldtnDt() == "") {
					intoWhsSub.setInvldtnDt(null);
				}
				intoWhsSub.setUnBllgQty(intoWhsSub.getQty().abs());// Ĭ��δ��Ʊ����=�������
				intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum().abs());// δ��Ʊ���=��˰���
//				intoWhsSub.setUnBllgUprc(intoWhsSub.getPrcTaxSum().abs().divide(intoWhsSub.getQty().abs()));//Ĭ��δ��Ʊ����=��˰���/����
				if (intoWhs.getIsNtRtnGood() == 0) {
					intoWhsSub.setReturnQty(intoWhsSub.getQty());// Ĭ��δ�˻�����=�������
				}
			}
			int a = iwsd.deleteIntoWhsSubByIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
			int b = iwd.updateIntoWhsByIntoWhsSnglId(intoWhs);
			int c = iwsd.insertIntoWhsSub(intoWhsSubList);
			if (a >= 1 && b >= 1 && c >= 1) {
				message = "���³ɹ���";
				isSuccess = true;
			} else {
				isSuccess = false;
				message = "����ʧ�ܣ�";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// ����ɾ���ɹ���ⵥ
	@Override
	public String deleteIntoWhs(String intoWhsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (iwd.selectIntoWhsByIntoWhsSnglId(intoWhsSnglId) != null) {
			iwd.deleteIntoWhsByIntoWhsSnglId(intoWhsSnglId);
			iwsd.deleteIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "���" + intoWhsSnglId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/IntoWhs/deleteIntoWhs", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ���ձ�Ų�ѯ�ɹ���ⵥ
	@Override
	public String queryIntoWhs(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<IntoWhsSub> intoWhsSub = new ArrayList<>(); */
		IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(intoWhsSnglId);
		if (intoWhs != null) {
			/* intoWhsSub=iwsd.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId); */
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "���" + intoWhsSnglId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhs", isSuccess, message, intoWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �ɹ���ⵥ�����б�
	@Override
	public String queryIntoWhsList(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);

		// �ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));// �ֿ����
		map.put("whsIdList", whsIdList);

		List<IntoWhs> poList = iwd.selectIntoWhsList(map);
		int count = poList.size();
		if (poList.size() < (int) map.get("num")) {

		} else {
			poList = poList.subList((int) map.get("index"), (int) map.get("num") + (int) map.get("index"));
		}
//		int count = iwd.selectIntoWhsCountLess(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ����ⵥ��
	@Override
	public String deleteIntoWhsList(String intoWhsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(intoWhsSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (iwd.selectIntoWhsIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteIntoWhsList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
					e.printStackTrace();
				}
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�111111\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/deleteIntoWhsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteIntoWhsList(List<String> lists2) {
		iwd.insertIntoWhsDl(lists2);
		iwsd.insertIntoWhsSubDl(lists2);
		int a = iwd.deleteIntoWhsList(lists2);
		return a;
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

	// �ɹ���ⵥ�Ͳɹ��˻������������
	@Override
	public Map<String, Object> updateIntoWhsIsNtChk(IntoWhs inwh) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// �ɹ���ⵥ����
			// �ж�ǰ�˴��ص����״̬���˻�״̬
			if (intoWhs.getIsNtRtnGood() == 0) {
				if (inwh.getIsNtChk() == 1) {
					// �ɹ���ⵥ���
					message.append(updateIntoWhsIsNtChkOK(inwh).get("message"));
				} else if (inwh.getIsNtChk() == 0) {
					// �ɹ���ⵥ����
					message.append(updateIntoWhsIsNtChkNO(inwh).get("message"));

				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			} else if (intoWhs.getIsNtRtnGood() == 1) {
				if (inwh.getIsNtChk() == 1) {
					// �ɹ��˻������
					message.append(updateReturnWhsIsNtChkOK(inwh).get("message"));

				} else if (inwh.getIsNtChk() == 0) {
					// �ɹ��˻�������
					message.append(updateReturnWhsIsNtChkNO(inwh).get("message"));

				} else {
					isSuccess = false;
					message.append("���״̬�����޷���ˣ�\n");
				}
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// �ɹ���ⵥ���
	private Map<String, Object> updateIntoWhsIsNtChkOK(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 0) {
			IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// �ɹ���ⵥ����
			List<IntoWhsSub> intoWhsSubs = intoWhs.getIntoWhsSub();// ��ȡ�ӱ�����
			String whsId = "";
			if (intoWhs.getToGdsSnglId() == null || intoWhs.getToGdsSnglId().equals("")) {
				for (IntoWhsSub inWhSub : intoWhsSubs) {
					// �������Ρ�������롢�ֿ�����ѯ�������Ƿ��иö���
					InvtyTab invtyTab = itd.selectInvtyTabByTerm(inWhSub);// Ӧ�ü���
					if (invtyTab == null) {
						int a = itd.insertInvtyTabToIntoWhs(inWhSub);// �������������ִ���
						if (a < 1) {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ�ܣ������ԭ������ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);// �޸Ŀ��������ִ���
						if (b < 1) {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ�ܣ������ԭ������ˣ�\n";
							throw new RuntimeException(message);
						}
					}
					whsId = inWhSub.getWhsEncd();
				}
			} else {
				for (IntoWhsSub inWhSub : intoWhsSubs) {
					// �������Ρ�������롢�ֿ�����ѯ�������Ƿ��иö���
					InvtyTab invtyTab = itd.selectInvtyTabByTerm(inWhSub);
					if (invtyTab == null) {
						int a = itd.insertInvtyTabToIntoWhs(inWhSub);// �������������ִ���
						if (a < 1) {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ�ܣ������ԭ������ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);// �޸Ŀ��������ִ���
//						int b = itd.updateInvtyTabJiaToIntoWhs(inWhSub);
						if (b < 1) {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ�ܣ������ԭ������ˣ�\n";
							throw new RuntimeException(message);
						}
					}
					if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
						map.put("ordrNum", inWhSub.getToOrdrNum());// �ӱ����
						BigDecimal unIntoWhsQty = toGdsSnglSubDao.selectUnIntoWhsQtyByInvWhsBat(map);// ��ѯ�������е�δ�������
						if (unIntoWhsQty != null) {
							if (unIntoWhsQty.compareTo(inWhSub.getQty()) == 1
									|| unIntoWhsQty.compareTo(inWhSub.getQty()) == 0) {
								map.put("unIntoWhsQty", inWhSub.getQty());// �޸�δ�������
								toGdsSnglSubDao.updateToGdsSnglSubByInvWhsBat(map);// ���ݵ������ӱ�����޸ĵ������е�δ�������
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�д����" + inWhSub.getInvtyEncd()
										+ "���ۼ�����������ڵ����������޷���ˣ�\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					}
					whsId = inWhSub.getWhsEncd();
				}
				toGdsSnglDao.updateToGdsSnglDealStatOK(intoWhs.getToGdsSnglId());// �޸ĵ������Ĵ���״̬Ϊ�������
				// �жϲɹ���ⵥ�вɹ����������Ƿ����
				if (intoWhs.getPursOrdrId() != null && !intoWhs.getPursOrdrId().equals("")) {
					// ����ɹ������������ ,���ݲɹ������Ų�ѯ�ɹ�������Ӧ��ȫ���ɹ���ⵥ��Ϣ,���ղɹ������ӱ���ŷ����ѯ
					map.put("pursOrdrIds", intoWhs.getPursOrdrId());
					// �ɶ��ӱ���ż���
					List<Long> detailCount = pursOrdrSubDao.selectPursOrdrSubIdsByPursOrdrId(intoWhs.getPursOrdrId());
					// �����Ƿ��Ƴ�����
					boolean remove = false;
					List<Map> intoQtyAndPuOrNumList = iwd.selectIntoWhsQtyByPursOrdrId(map);// ���ݲɶ��ӱ��,��ѯ��ϸ�������

					for (Map intoQtyAndPuOrNum : intoQtyAndPuOrNumList) {
						// ��ȡ�ɹ������ӱ����
						map.put("pursOrdrNum", intoQtyAndPuOrNum.get("pursOrdrNum"));
						BigDecimal intoQty = (BigDecimal) intoQtyAndPuOrNum.get("intoQty");
						// ���ݲɹ������ӱ���Ų�ѯ�ɹ������е�����
						BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);

						if (pursQty != null) {
							if (pursQty.compareTo(intoQty) == 1 || pursQty.compareTo(intoQty) == 0) {
								BigDecimal unToGdsQty = pursQty.subtract(intoQty);// ����������ȥ�ɹ���ⵥ��������δ��������
								map.put("unToGdsQty", unToGdsQty);
								pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// ���ݴ�������޸ĵ������е�δ�������
							}
							System.out.println(map.get("pursOrdrNum"));
							remove = detailCount.remove((Long)map.get("pursOrdrNum"));
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					}

					if (detailCount.size() > 0 && remove) {
						for (Long ordrNum : detailCount) {
							map.put("pursOrdrNum", ordrNum);
							BigDecimal intoQty = new BigDecimal(0);
							BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);
							if (pursQty != null) {
								if (pursQty.compareTo(intoQty) == 1 || pursQty.compareTo(intoQty) == 0) {
									BigDecimal unToGdsQty = pursQty.subtract(intoQty);// ����������ȥ�ɹ���ⵥ��������δ��������
									map.put("unToGdsQty", unToGdsQty);
									pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// ���ݴ�������޸ĵ������е�δ�������
								}
							} else {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷���ˣ�\n";
								throw new RuntimeException(message);
							}
						}

					} else if (!remove) {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ۼ�����,����ϵ��������ԭ��!";
						throw new RuntimeException(message);
					}

				}
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if (whs != null && whs == 1) {
				intoWhsHuoWeiJia(inwh.getIntoWhsSnglId(), intoWhsSubs);
			}
			int c = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
			if (c >= 1) {
				isSuccess = true;
				message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ��˳ɹ���\n";
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ�ܣ������ԭ������ˣ�\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ����ˣ�����Ҫ�ظ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �ɹ���ⵥ����
	private Map<String, Object> updateIntoWhsIsNtChkNO(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		InvtyTab invtyTab = new InvtyTab();// ����
		// �жϲɹ���ⵥ�е����״̬�Ƿ����
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 1) {
			// �жϲɹ���ⵥ�Ƿ����
			if (iwd.selectIntoWhsIsNtBookEntry(inwh.getIntoWhsSnglId()) == 0) {
				// �жϲɹ���ⵥ�Ƿ�Ʊ
				if (iwd.selectIntoWhsIsNtBllg(inwh.getIntoWhsSnglId()) == 0) {
					// �жϲɹ���ⵥ�Ƿ����
					if (iwd.selectIntoWhsIsNtStl(inwh.getIntoWhsSnglId()) == 0) {
						IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// �ɹ���ⵥ����
						List<IntoWhsSub> intoWhsSubs = intoWhs.getIntoWhsSub();// ��ȡ�ɹ���ⵥ�ӱ�
						String whsId = "";
						if (intoWhs.getToGdsSnglId() == null || intoWhs.getToGdsSnglId().equals("")) {
							for (IntoWhsSub inWhSub : intoWhsSubs) {
								invtyTab = itd.selectInvtyTabByTerm(inWhSub);
								if (invtyTab == null) {
									isSuccess = false;
									message += inwh.getIntoWhsSnglId() + "�Ĵ�����룺" + inWhSub.getInvtyEncd() + "���Σ�"
											+ inWhSub.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
									throw new RuntimeException(message);
								} else {
									// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
									if ((invtyTab.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
											&& (invtyTab.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
										itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
									} else {
										isSuccess = false;
										message += inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�д������:" + inWhSub.getInvtyEncd()
												+ ",����:" + inWhSub.getBatNum() + "�Ŀ�治�㣬�޷�����\n";
										throw new RuntimeException(message);
									}
								}
								whsId = inWhSub.getWhsEncd();
							}
						} else {
							for (IntoWhsSub inWhSub : intoWhsSubs) {
								invtyTab = itd.selectInvtyTabByTerm(inWhSub);
								if (invtyTab == null) {
									isSuccess = false;
									message += inwh.getIntoWhsSnglId() + "�Ĵ�����룺" + inWhSub.getInvtyEncd() + "���Σ�"
											+ inWhSub.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
									throw new RuntimeException(message);
								} else {
									// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
									if ((invtyTab.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
											&& (invtyTab.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
										itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
									} else {
										isSuccess = false;
										message += inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�д������:" + inWhSub.getInvtyEncd()
												+ ",����:" + inWhSub.getBatNum() + "�Ŀ�治�㣬�޷�����\n";
										throw new RuntimeException(message);
									}
									// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
//									if (invtyTab.getNowStok().compareTo(inWhSub.getQty()) == 1
//											|| invtyTab.getNowStok().compareTo(inWhSub.getQty()) == 0) {
//										itd.updateInvtyTabJianToIntoWhs(inWhSub);
//
//									} else if (invtyTab.getNowStok().compareTo(inWhSub.getQty()) == -1) {
//										isSuccess = false;
//										message += inwh.getIntoWhsSnglId() + "������������㣬�޷�����\n";
//										throw new RuntimeException(message);
//									}
								}
								if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
									map.put("ordrNum", inWhSub.getToOrdrNum());// �ӱ����
									BigDecimal unIntoWhsQty = toGdsSnglSubDao.selectUnIntoWhsQtyByInvWhsBat(map);// ��ѯ�������е�δ�������
									if (unIntoWhsQty != null) {
										map.put("unIntoWhsQty", inWhSub.getQty().multiply(new BigDecimal(-1)));// �޸�δ�������
										toGdsSnglSubDao.updateToGdsSnglSubByInvWhsBat(map);// ���ݴ�������޸ĵ������е�δ�������
									} else {
										isSuccess = false;
										message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷�����\n";
										throw new RuntimeException(message);
									}
								}
								// �жϲɹ���ⵥ�вɹ����������Ƿ����
								if (intoWhs.getPursOrdrId() != null && !intoWhs.getPursOrdrId().equals("")) {
									// ����ɹ������������ ,���ݲɹ������Ų�ѯ�ɹ�������Ӧ��ȫ���ɹ���ⵥ��Ϣ,���ղɹ������ӱ���ŷ����ѯ
									map.put("pursOrdrIds", intoWhs.getPursOrdrId());
									// �ɶ��ӱ���ż���
									List<Long> detailCount = pursOrdrSubDao
											.selectPursOrdrSubIdsByPursOrdrId(intoWhs.getPursOrdrId());
									// �����Ƿ��Ƴ�����
									boolean remove = false;
									List<Map> intoQtyAndPuOrNumList = iwd.selectIntoWhsQtyByPursOrdrId(map);
									for (Map intoQtyAndPuOrNum : intoQtyAndPuOrNumList) {
										// ��ȡ�ɹ������ӱ����
										map.put("pursOrdrNum", intoQtyAndPuOrNum.get("pursOrdrNum"));
										// ���ݲɹ������ӱ���Ų�ѯ�ɹ������е�����
										BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);
										if (pursQty != null) {
											map.put("unToGdsQty", pursQty);
											pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// ���ݲɹ������ӱ�����޸Ĳɹ�����δ��������
											remove = detailCount.remove((Long)intoQtyAndPuOrNum.get("pursOrdrNum"));
										} else {
											isSuccess = false;
											message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId()
													+ "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷���ˣ�\n";
											throw new RuntimeException(message);
										}
									}
									if (detailCount.size() > 0 && remove) {
										for (Long pursOrdrNum : detailCount) {

											map.put("pursOrdrNum", pursOrdrNum);
											BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);

											if (pursQty != null) {
												map.put("unToGdsQty", pursQty);
												pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// ���ݲɹ������ӱ�����޸Ĳɹ�����δ��������
											} else {
												isSuccess = false;
												message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId()
														+ "�Ĳɹ���ⵥ��Ӧ�ĵ�������δ������������ڣ��޷���ˣ�\n";
												throw new RuntimeException(message);
											}
										}
										;
									} else if (!remove) {
										isSuccess = false;
										message += "����Ϊ" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ���ʧ��!(��ϸ��������)\n";
										throw new RuntimeException(message);
									}

								}
								whsId = inWhSub.getWhsEncd();
							}
							toGdsSnglDao.updateToGdsSnglDealStatNO(intoWhs.getToGdsSnglId());// �޸ĵ������Ĵ���״̬Ϊ������
						}
						Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
						if (whs != null && whs == 1) {
							intoWhsHuoWeiJian(inwh.getIntoWhsSnglId(), intoWhsSubs);
						}
						int updateFlag = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
						if (updateFlag >= 1) {
							isSuccess = true;
							message += inwh.getIntoWhsSnglId() + "�ɹ���ⵥ����ɹ���\n";
						} else {
							isSuccess = false;
							message += inwh.getIntoWhsSnglId() + "�ɹ���ⵥ������,����Ҫ�ظ�����\n";
							throw new RuntimeException(message);
						}

					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�ѽ��㣬�޷�����\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�ѿ�Ʊ���޷�����\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�Ѽ��ˣ��޷�����\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥδ��ˣ�������˸õ��ݣ�\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �ɹ��˻������
	private Map<String, Object> updateReturnWhsIsNtChkOK(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
//		InvtyTab invtyTab = new InvtyTab();// ����
		List<IntoWhsSub> intoWhsSubs = new ArrayList<>();// ����ӱ�
		// �жϲɹ���ⵥ�Ƿ���ˣ�ֻ�����״̬Ϊ0ʱ�ſ�������˲���
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 0) {

			intoWhsSubs = iwsd.selectIntoWhsSubByIntoWhsSnglId(inwh.getIntoWhsSnglId());
			String whsId = "";
			for (IntoWhsSub inWhSub : intoWhsSubs) {
				InvtyTab invtyTabs = itd.selectInvtyTabByTerm(inWhSub);
				if (invtyTabs == null) {
					isSuccess = false;
					message += inwh.getIntoWhsSnglId() + "�Ĳɹ��˻����д������:" + inWhSub.getInvtyEncd() + ",����:"
							+ inWhSub.getBatNum() + "�Ŀ�����������޷����\n";
					throw new RuntimeException(message);
				} else {
					// a.compareTo(b) -1��ʾС�� 1 ��ʾ���� 0��ʾ����
					if ((invtyTabs.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
							&& (invtyTabs.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
						// ���ڲɹ��˻����е�����Ϊ��������ִ��������ִ����Ϳ�����
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);
						if (b < 1) {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻������ʧ�ܣ������ԭ������ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += inwh.getIntoWhsSnglId() + "�Ĳɹ��˻����д������:" + inWhSub.getInvtyEncd() + ",����:"
								+ inWhSub.getBatNum() + "�Ŀ�治�㣬�޷����\n";
						throw new RuntimeException(message);
					}
				}
				if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
					map.put("ordrNum", inWhSub.getToOrdrNum());// �ӱ����
					BigDecimal unReturnQty = intoWhsSubDao.selectUnReturnQtyByOrdrNum(map);// ��ѯ�ɹ���ⵥ�е�δ�˻�����
					if (unReturnQty != null) {
						if (unReturnQty.compareTo(inWhSub.getQty().abs()) >= 0) {
							map.put("returnQty", inWhSub.getQty().abs());// �޸�δ�˻�����
							intoWhsSubDao.updateIntoWhsSubUnReturnQty(map);// �޸Ĳɹ���ⵥδ�˻�����
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻����д����" + inWhSub.getInvtyEncd()
									+ "���ۼ�������������˻��������޷���ˣ�\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�����Ӧ�ĵ�������δ�˻����������ڣ��޷���ˣ�\n";
						throw new RuntimeException(message);
					}
				}
				whsId = inWhSub.getWhsEncd();
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if (whs != null && whs == 1) {
				intoWhsHuoWeiJian(inwh.getIntoWhsSnglId(), intoWhsSubs);
			}
			int a = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
			if (a >= 1) {
				isSuccess = true;
				message += inwh.getIntoWhsSnglId() + "�ɹ��˻�����˳ɹ���\n";
			} else {
				isSuccess = false;
				message += inwh.getIntoWhsSnglId() + "�ɹ��˻������ʧ�ܣ�\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�������ˣ�����Ҫ������ˣ�\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// �ɹ��˻�������
	private Map<String, Object> updateReturnWhsIsNtChkNO(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		InvtyTab invtyTab = new InvtyTab();// ����
		List<IntoWhsSub> intoWhsSubs = new ArrayList<>();// ����ӱ�
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 1) {
			// �жϲɹ���ⵥ�Ƿ����
			if (iwd.selectIntoWhsIsNtBookEntry(inwh.getIntoWhsSnglId()) == 0) {
				// �жϲɹ���ⵥ�Ƿ�Ʊ
				if (iwd.selectIntoWhsIsNtBllg(inwh.getIntoWhsSnglId()) == 0) {
					// �жϲɹ���ⵥ�Ƿ����
					if (iwd.selectIntoWhsIsNtStl(inwh.getIntoWhsSnglId()) == 0) {
						intoWhsSubs = iwsd.selectIntoWhsSubByIntoWhsSnglId(inwh.getIntoWhsSnglId());
						String whsId = "";
						for (IntoWhsSub inWhSub : intoWhsSubs) {
							// �������Ρ�������롢�ֿ�����ѯ�������Ƿ��иö���
							invtyTab = itd.selectInvtyTabByTerm(inWhSub);
							if (invtyTab == null) {
								isSuccess = false;
								message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻����д������:" + inWhSub.getInvtyEncd()
										+ ",����:" + inWhSub.getBatNum() + "�Ŀ�治���ڣ��޷�����\n";
								throw new RuntimeException(message);
							} else {
								// ���ڲɹ��˻����е�����Ϊ��������ִ�м���棬ʵ�ʿ��������
								int b = itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
								if (b < 1) {
									isSuccess = false;
									message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�������ʧ�ܣ������ԭ��������\n";
									throw new RuntimeException(message);
								}
							}
							if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
								map.put("ordrNum", inWhSub.getToOrdrNum());// �ӱ����
								BigDecimal unReturnQty = intoWhsSubDao.selectUnReturnQtyByOrdrNum(map);// ��ѯ�ɹ���ⵥ�е�δ�˻�����
								if (unReturnQty != null) {
									map.put("returnQty", inWhSub.getQty());// �޸�δ�˻�����
									intoWhsSubDao.updateIntoWhsSubUnReturnQty(map);// �޸Ĳɹ���ⵥδ�˻�����
								} else {
									isSuccess = false;
									message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�����Ӧ�ĵ�������δ�˻����������ڣ��޷�����\n";
									throw new RuntimeException(message);
								}
							}
							whsId = inWhSub.getWhsEncd();
						}
						Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
						if (whs != null && whs == 1) {
							intoWhsHuoWeiJia(inwh.getIntoWhsSnglId(), intoWhsSubs);
						}
						int c = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
						if (c >= 1) {
							isSuccess = true;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�����������ɹ���\n";
						} else {
							isSuccess = false;
							message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ��˻�������ʧ�ܣ������ԭ��������\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�ѽ��㣬�޷�����\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�ѿ�Ʊ���޷�����\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥ�Ѽ��ˣ��޷�����\n";
			}
		} else {
			isSuccess = false;
			message += "���ݺ�Ϊ��" + inwh.getIntoWhsSnglId() + "�Ĳɹ���ⵥδ��ˣ�����Ҫ����\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// ���ӻ�λ
	private void intoWhsHuoWeiJia(String intoWhsSnglId, List<IntoWhsSub> intoWhsSubList) {
		/**
		 * ��λ�ж�
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(intoWhsSnglId);// ��ѯ���ɵĲɹ���ⵥ���Ƿ���¼���λ��Ϣ
		if (tabs.size() == 0) {
			throw new RuntimeException("������ϸû��¼���λ��Ϣ,��˶Ժ��ٲ���");
		}
		for (IntoWhsSub intoWhsSub : intoWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(intoWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(intoWhsSub.getInvtyEncd());
			bitTab.setBatNum(intoWhsSub.getBatNum());
			bitTab.setSerialNum(intoWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(intoWhsSnglId);
			// ��ѯ�õ��ݸô�����л�λ������
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);
			// �жϸõ��ݸô�����л�λ�������Ƿ񲻴��ڵ��ݴ��������һ��
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + "���Σ�" + bitTab.getBatNum() + "û��¼���Ӧ�Ļ�λ��Ϣ");
			} else if (bitTab2.getQty().abs().compareTo(intoWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException(
						"�����" + bitTab.getInvtyEncd() + "���Σ�" + bitTab.getBatNum() + " ¼��Ļ�λ���������������ƥ��,��˶Ի�λ�����ٲ���");
			}
		}
		/* ��λ��Ϣ */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// ��ѯ�������Ƿ���ڸû�λ��Ϣ
			if (whsTab == null) {
				invtyNumMapper.insertMovBitTabJia(tab);// ������ʱ����
			} else {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJiaMbit(tab);// ����ʱ�޸�
			}
		}
	}

	// ���ٻ�λ
	private void intoWhsHuoWeiJian(String intoWhsSnglId, List<IntoWhsSub> intoWhsSubList) {
		/**
		 * ��λ�ж�
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(intoWhsSnglId);// ��ѯ���ɵĲɹ���ⵥ���Ƿ���¼���λ��Ϣ
		if (tabs.size() == 0) {
			throw new RuntimeException("������ϸû��¼���λ��Ϣ,��˶Ժ��ٲ���");
		}
		for (IntoWhsSub intoWhsSub : intoWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(intoWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(intoWhsSub.getInvtyEncd());
			bitTab.setBatNum(intoWhsSub.getBatNum());
			bitTab.setSerialNum(intoWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(intoWhsSnglId);
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);// �жϸõ��ݸô�����л�λ�������Ƿ񲻴��ڵ��ݴ��������һ��
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + "���Σ�" + bitTab.getBatNum() + "û��¼���Ӧ�Ļ�λ��Ϣ,��˶Ժ��ٲ���");
			} else if (bitTab2.getQty().abs().compareTo(intoWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException(
						"�����" + bitTab.getInvtyEncd() + "���Σ�" + bitTab.getBatNum() + " ¼��Ļ�λ���������������ƥ��,��˶Ի�λ�����ٲ���");
			}
		}
		/* ��λ��Ϣ */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// ��ѯ�������Ƿ���ڸû�λ��Ϣ
			if (whsTab == null) {
				throw new RuntimeException("�ֿ⣺" + tab.getWhsEncd() + ",�����" + tab.getInvtyEncd() + ",���ţ�"
						+ tab.getBatNum() + "�޻�λ����,��˶Ժ��ٲ���");
			} else if (whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 1
					|| whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 0) {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJianMbit(tab);// ����
			} else {
				throw new RuntimeException("�ֿ⣺" + tab.getWhsEncd() + ",�����" + tab.getInvtyEncd() + ",���ţ�"
						+ tab.getBatNum() + "��Ӧ�Ļ�λ��������");
			}
		}
	}

	// ��ӡ�ɹ���ⵥ
	@Override
	public String printingIntoWhsList(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);

		// �ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));// �ֿ����
		map.put("whsIdList", whsIdList);
		List<zizhu> intoWhsList = iwd.printingIntoWhsList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("purc/IntoWhs/printingIntoWhsList", true, "��ѯ�ɹ���", null, intoWhsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// �ɹ������ϸ��ѯ
	@Override
	public String queryIntoWhsByInvtyEncd(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
					|| map.get("sortOrder").equals("")) {
				map.put("sort", "intoWhsDt");
				map.put("sortOrder", "desc");
			}
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = iwd.selectIntoWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "��ѯ�ɹ���", poList,
						tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

	// �����ϸ��--��ѯ
	@Override
	public String queryIntoWhsByInvtyEncdPrint(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
					|| map.get("sortOrder").equals("")) {
				map.put("sort", "intoWhsDt");
				map.put("sortOrder", "desc");
			}
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = iwd.selectIntoWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = iwd.selectIntoWhsByInvtyEncdPrint(map);
			// ��������Ҫ�ܼ�
//				Map tableSums = iwd.selectIntoWhsListSum(map);
//				if (null!=tableSums) {
//					DecimalFormat df = new DecimalFormat("#,##0.00");
//					for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
//						String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
//						entry.setValue(s);
//					}
//				}
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "��ѯ�ɹ���", poList, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

	// �ɹ����ջ������ѯ
	@Override
	public String selectIntoWhsAndPursOrdr(Map map) {
		String resp = "";
		List<Map> poList = iwd.selectIntoWhsAndPursOrdr(map);
		Map tableSums = iwd.selectIntoWhsAndPursOrdrSums(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/selectIntoWhsAndPursOrdr", true, "��ѯ�ɹ���", poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɹ���ⵥ
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, IntoWhs> intoWhsMap = null;
		if (i == 0) {
			intoWhsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			intoWhsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}
		// ��MapתΪList��Ȼ���������븸������
		List<IntoWhs> intoWhsList = intoWhsMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		List<List<IntoWhs>> intoWhsLists = Lists.partition(intoWhsList, 1000);

		for (List<IntoWhs> intoWhs : intoWhsLists) {
			iwd.insertIntoWhsUpload(intoWhs);
		}
		List<IntoWhsSub> intoWhsSubList = new ArrayList<>();
		int flag = 0;
		for (IntoWhs entry : intoWhsList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<IntoWhsSub> tempList = entry.getIntoWhsSub();
			tempList.forEach(s -> s.setIntoWhsSnglId(entry.getIntoWhsSnglId()));
			intoWhsSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (intoWhsSubList.size() >= 1000 || intoWhsMap.size() == flag) {
				iwsd.insertIntoWhsSub(intoWhsSubList);
				intoWhsSubList.clear();
			}
		}
		isSuccess = true;
		message = "�ɹ���ⵥ����ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, IntoWhs> uploadScoreInfo(MultipartFile file) {
		Map<String, IntoWhs> temp = new HashMap<>();
		int j = 1;
		try {
			InputStream fileIn = file.getInputStream();
			// ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// ��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			// ��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			// ��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			// ���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			// ����������
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "�ɹ���ⵥ����");
				// ����ʵ����
				IntoWhs intoWhs = new IntoWhs();
				if (temp.containsKey(orderNo)) {
					intoWhs = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				intoWhs.setPursTypId(GetCellData(r, "�ɹ����ͱ���"));// �ɹ����ͱ���
//				intoWhs.setPursTypNm(GetCellData(r,"�ɹ���������"));//�ɹ���������
				intoWhs.setIntoWhsSnglId(orderNo); // �ɹ���ⵥ����
				if (GetCellData(r, "�ɹ���ⵥ����") == null || GetCellData(r, "�ɹ���ⵥ����").equals("")) {
					intoWhs.setIntoWhsDt(null);
				} else {
					intoWhs.setIntoWhsDt(GetCellData(r, "�ɹ���ⵥ����"));// �ɹ���ⵥ����
				}
				intoWhs.setProvrId(GetCellData(r, "��Ӧ�̱���"));// ��Ӧ�̱���
//				intoWhs.setProvrNm(GetCellData(r,"��Ӧ������"));//��Ӧ������
				intoWhs.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				intoWhs.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//				intoWhs.setDepName(GetCellData(r,"��������"));//��������
				intoWhs.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				intoWhs.setUserName(GetCellData(r, "ҵ��Ա����"));// �û�����
				intoWhs.setFormTypEncd(GetCellData(r, "�������ͱ���"));// ��������
				intoWhs.setRecvSendCateId(GetCellData(r, "�շ�������"));// �շ�������
				intoWhs.setOutIntoWhsTypId(GetCellData(r, "�����������"));// �����������
				intoWhs.setPursOrdrId(GetCellData(r, "�ɹ���������"));// �ɹ���������
				intoWhs.setToGdsSnglId(GetCellData(r, "����������"));// ��������
				intoWhs.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					intoWhs.setSetupTm(null);
				} else {
					intoWhs.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				intoWhs.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				intoWhs.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					intoWhs.setChkTm(null);
				} else {
					intoWhs.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				intoWhs.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					intoWhs.setModiTm(null);
				} else {
					intoWhs.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				intoWhs.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				intoWhs.setBookEntryPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					intoWhs.setBookEntryTm(null);
				} else {
					intoWhs.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				intoWhs.setIsNtBllg(new Double(GetCellData(r, "�Ƿ�Ʊ")).intValue());// �Ƿ�Ʊ
				intoWhs.setIsNtStl(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				intoWhs.setIsNtRtnGood(new Double(GetCellData(r, "�Ƿ��˻�")).intValue());// �Ƿ��˻���
				intoWhs.setMemo(GetCellData(r, " ��ͷ��ע"));// ��ע
				intoWhs.setExaRep(GetCellData(r, "������߱���"));// ������߱���
				intoWhs.setToFormTypEncd(GetCellData(r, "��Դ�������ͱ���"));// ��Դ�������ͱ���
				intoWhs.setIsNtMakeVouch(new Double(GetCellData(r, "�Ƿ�����ƾ֤")).intValue());
				intoWhs.setMakVouchPers(GetCellData(r, "��ƾ֤��"));// ��ƾ֤��
				if (GetCellData(r, "��ƾ֤ʱ��") == null || GetCellData(r, "��ƾ֤ʱ��").equals("")) {
					intoWhs.setMakVouchTm(null);
				} else {
					intoWhs.setMakVouchTm(GetCellData(r, "��ƾ֤ʱ��").replaceAll("[^0-9:-]", " "));// ��ƾ֤ʱ��
				}
				List<IntoWhsSub> intoWhsSubList = intoWhs.getIntoWhsSub();// �ɹ���ⵥ�ӱ�
				if (intoWhsSubList == null) {
					intoWhsSubList = new ArrayList<>();
				}
				IntoWhsSub intoWhsSub = new IntoWhsSub();
				intoWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				intoWhsSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//				intoWhsSub.setWhsNm(GetCellData(r, "�ֿ�����"));// �ֿ�����
				intoWhsSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				intoWhsSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				intoWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				intoWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				intoWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				intoWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				intoWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				intoWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				intoWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				intoWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				intoWhsSub.setBatNum(GetCellData(r, "����"));// ����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					intoWhsSub.setPrdcDt(null);
				} else {
					intoWhsSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				intoWhsSub.setBaoZhiQi(GetCellData(r, "������"));// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					intoWhsSub.setInvldtnDt(null);
				} else {
					intoWhsSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				intoWhsSub.setIntlBat(GetCellData(r, "��������"));// ��������
				intoWhsSub.setStlQty(GetBigDecimal(GetCellData(r, "��������"), 8));// 8��ʾС��λ�� //��������
				intoWhsSub.setStlUprc(GetBigDecimal(GetCellData(r, "���㵥��"), 8));// 8��ʾС��λ�� //���㵥��
				intoWhsSub.setStlAmt(GetBigDecimal(GetCellData(r, "������"), 8));// 8��ʾС��λ�� //������
				intoWhsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8));// 8��ʾС��λ�� //δ��Ʊ����
				intoWhsSub.setUnBllgUprc(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8));// 8��ʾС��λ�� //δ��Ʊ����
				intoWhsSub.setUnBllgAmt(GetBigDecimal(GetCellData(r, "δ��Ʊ���"), 8));// 8��ʾС��λ�� //δ��Ʊ���
				intoWhsSub.setTeesQty(GetBigDecimal(GetCellData(r, "�ݹ�����"), 8));// 8��ʾС��λ�� //�ݹ�����
				intoWhsSub.setTeesUprc(GetBigDecimal(GetCellData(r, "�ݹ�����"), 8));// 8��ʾС��λ�� //�ݹ�����
				intoWhsSub.setTeesAmt(GetBigDecimal(GetCellData(r, "�ݹ����"), 8));// 8��ʾС��λ��//�ݹ����
//				intoWhsSub.setCrspdInvNum(GetCellData(r, "��Ӧ��Ʊ��"));// ��Ӧ��Ʊ��
//				intoWhsSub.setGdsBitEncd(GetCellData(r, "��λ����"));// ��λ����
//				intoWhsSub.setRegnEncd(GetCellData(r, "�������"));// �������
				intoWhsSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע
				if (GetCellData(r, "��Դ�����ӱ����") == null || GetCellData(r, "��Դ�����ӱ����") == "") {
					intoWhsSub.setToOrdrNum(null);
				} else {
					intoWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "��Դ�����ӱ����")));
				}
				intoWhsSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				intoWhsSubList.add(intoWhsSub);
				intoWhs.setIntoWhsSub(intoWhsSubList);
				temp.put(orderNo, intoWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// ����excle
	private Map<String, IntoWhs> uploadScoreInfoU8(MultipartFile file) {
		Map<String, IntoWhs> temp = new HashMap<>();
		int j = 1;
		try {
			InputStream fileIn = file.getInputStream();
			// ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			// ��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			// ��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			// ��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			// ���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			// ����������
			getCellNames();
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "��ⵥ��");
				// ����ʵ����
				IntoWhs intoWhs = new IntoWhs();
				if (temp.containsKey(orderNo)) {
					intoWhs = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				intoWhs.setPursTypId("1");// �ɹ����ͱ���
//					intoWhs.setPursTypNm(GetCellData(r,"�ɹ���������"));//�ɹ���������
				intoWhs.setIntoWhsSnglId(orderNo); // �ɹ���ⵥ����
				if (GetCellData(r, "�������") == null || GetCellData(r, "�������").equals("")) {
					intoWhs.setIntoWhsDt(null);
				} else {
					intoWhs.setIntoWhsDt(GetCellData(r, "�������"));// �ɹ���ⵥ����
				}
				intoWhs.setProvrId(GetCellData(r, "��Ӧ�̱���"));// ��Ӧ�̱���
//					intoWhs.setProvrNm(GetCellData(r,"��Ӧ������"));//��Ӧ������
				intoWhs.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				intoWhs.setDeptId(GetCellData(r, "���ű���"));// ���ű���
//					intoWhs.setDepName(GetCellData(r,"��������"));//��������
				intoWhs.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				intoWhs.setUserName(GetCellData(r, "ҵ��Ա"));// �û�����

				intoWhs.setFormTypEncd(
						GetBigDecimal(GetCellData(r, "����"), 8).compareTo(BigDecimal.ZERO) == -1 ? "006" : "002");// ��������
				intoWhs.setRecvSendCateId("1");// �շ�������
				intoWhs.setOutIntoWhsTypId("9");// �����������
				intoWhs.setPursOrdrId(GetCellData(r, "������"));// �ɹ���������
				intoWhs.setToGdsSnglId(GetCellData(r, "��������"));// ��������
				intoWhs.setSetupPers(GetCellData(r, "�Ƶ���"));// ������
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					intoWhs.setSetupTm(null);
				} else {
					intoWhs.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				intoWhs.setIsNtChk(1);// �Ƿ����
				intoWhs.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					intoWhs.setChkTm(null);
				} else {
					intoWhs.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				intoWhs.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					intoWhs.setModiTm(null);
				} else {
					intoWhs.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				intoWhs.setIsNtBookEntry(0);// �Ƿ����
//				intoWhs.setBookEntryPers(GetCellData(r, "������"));// ������
//					if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
//						intoWhs.setBookEntryTm(null);
//					} else {
//						intoWhs.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
//					}
				intoWhs.setIsNtBllg(0);// �Ƿ�Ʊ
				intoWhs.setIsNtStl(0);// �Ƿ����
				intoWhs.setIsNtRtnGood(GetBigDecimal(GetCellData(r, "����"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// �Ƿ��˻���
				intoWhs.setMemo(GetCellData(r, "��ע"));// ��ע
				intoWhs.setExaRep(null);// ������߱���
				intoWhs.setToFormTypEncd("002");// ��Դ�������ͱ���
				intoWhs.setIsNtMakeVouch(0);
//				intoWhs.setMakVouchPers(null);// ��ƾ֤��
//					if (GetCellData(r, "��ƾ֤ʱ��") == null || GetCellData(r, "��ƾ֤ʱ��").equals("")) {
//						intoWhs.setMakVouchTm(null);
//					} else {
//						intoWhs.setMakVouchTm(GetCellData(r, "��ƾ֤ʱ��").replaceAll("[^0-9:-]", " "));//��ƾ֤ʱ��
//					}
				List<IntoWhsSub> intoWhsSubList = intoWhs.getIntoWhsSub();// �ɹ���ⵥ�ӱ�
				if (intoWhsSubList == null) {
					intoWhsSubList = new ArrayList<>();
				}
				IntoWhsSub intoWhsSub = new IntoWhsSub();
//				intoWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				intoWhsSub.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
//					intoWhsSub.setWhsNm(GetCellData(r, "�ֿ�����"));// �ֿ�����
				intoWhsSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				intoWhsSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				intoWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "ԭ�Һ�˰����"), 8));// 8��ʾС��λ�� //��˰����
				intoWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "������˰����"), 8));// 8��ʾС��λ�� //��˰����
				intoWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "������˰���"), 8));// 8��ʾС��λ�� //��˰���
				intoWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "����˰��"), 8));// 8��ʾС��λ�� //˰��
				intoWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "���Ҽ�˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				intoWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				intoWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				intoWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				intoWhsSub.setBatNum(GetCellData(r, "����"));// ����
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					intoWhsSub.setPrdcDt(null);
				} else {
					intoWhsSub.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				intoWhsSub.setBaoZhiQi(GetCellData(r, "������"));// ������
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					intoWhsSub.setInvldtnDt(null);
				} else {
					intoWhsSub.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				intoWhsSub.setIntlBat(GetCellData(r, "��������"));// ��������
//					intoWhsSub.setStlQty(GetBigDecimal(GetCellData(r, "��������"), 8));// 8��ʾС��λ�� //��������
//					intoWhsSub.setStlUprc(GetBigDecimal(GetCellData(r, "���㵥��"), 8));// 8��ʾС��λ�� //���㵥��
//					intoWhsSub.setStlAmt(GetBigDecimal(GetCellData(r, "������"), 8));// 8��ʾС��λ�� //������
				intoWhsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8).abs());// 8��ʾС��λ�� //δ��Ʊ����
//					intoWhsSub.setUnBllgUprc(GetBigDecimal(GetCellData(r, "δ��Ʊ����"), 8));// 8��ʾС��λ�� //δ��Ʊ����
				intoWhsSub.setUnBllgAmt(GetBigDecimal(GetCellData(r, "δ��Ʊ���"), 8).abs());// 8��ʾС��λ�� //δ��Ʊ���
//					intoWhsSub.setTeesQty(GetBigDecimal(GetCellData(r, "�ݹ�����"), 8));// 8��ʾС��λ�� //�ݹ�����
//					intoWhsSub.setTeesUprc(GetBigDecimal(GetCellData(r, "�ݹ�����"), 8));// 8��ʾС��λ�� //�ݹ�����
//					intoWhsSub.setTeesAmt(GetBigDecimal(GetCellData(r, "�ݹ����"), 8));// 8��ʾС��λ��//�ݹ����
//					intoWhsSub.setCrspdInvNum(GetCellData(r, "��Ӧ��Ʊ��"));// ��Ӧ��Ʊ��
//					intoWhsSub.setGdsBitEncd(GetCellData(r, "��λ����"));// ��λ����
//					intoWhsSub.setRegnEncd(GetCellData(r, "�������"));// �������
				intoWhsSub.setMemo(GetCellData(r, "���屸ע"));// �ӱ�ע
//				if (GetCellData(r, "�������ӱ����") == null || GetCellData(r, "�������ӱ����") == "") {
//					intoWhsSub.setToOrdrNum(null);
//				} else {
//					intoWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "�������ӱ����")));
//				}
//				if (GetCellData(r, "�ɹ������ӱ����") == null || GetCellData(r, "�ɹ������ӱ����") == "") {
//					intoWhsSub.setPursOrdrNum(null);
//				} else {
//					intoWhsSub.setPursOrdrNum(Long.parseLong(GetCellData(r, "�ɹ������ӱ����")));
//				}
				intoWhsSub.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				intoWhsSubList.add(intoWhsSub);
				intoWhs.setIntoWhsSub(intoWhsSubList);
				temp.put(orderNo, intoWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// �ɹ���Ʊ����ʱ���ձ�Ų�ѯ�ɹ���ⵥ�ӱ���Ϣ
	@Override
	public String queryIntoWhsByIntoWhsIds(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(intoWhsSnglId);
		List<IntoWhsSub> intoWhsSubList = iwsd.selectIntoWhsSubByIntoWhsSnglIds(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/IntoWhs/queryIntoWhsByIntoWhsIds", true, "��ѯ�ɹ���", null,
					intoWhsSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �ɹ��˻�����ʱ���ձ�Ų�ѯ�ɹ���ⵥ�ӱ���Ϣ
	@Override
	public String selectIntoWhsSubByUnReturnQty(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(intoWhsSnglId);
		List<IntoWhsSub> intoWhsSubList = iwsd.selectIntoWhsSubByUnReturnQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/IntoWhs/selectIntoWhsSubByUnReturnQty", true, "��ѯ�ɹ���", null,
					intoWhsSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ʱ��ѯ������Ϣ
	@Override
	public String queryIntoWhsLists(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
	
		
		List<IntoWhs> poList = iwd.selectIntoWhsLists(map);
		int count = iwd.selectIntoWhsCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsLists", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ҳ+����
	@Override
	public String queryIntoWhsListOrderBy(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// �ɹ���ⵥ����
		List<String> provrIdList = getList((String) map.get("provrId"));// ��Ӧ�̱���
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// ��Ӧ�̶�����
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// �ֿ����
		List<String> batNumList = getList((String) map.get("batNum"));// ����
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// ��������
		List<String> intlBatList = getList((String) map.get("intlBat"));// ��������
		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("intlBatList", intlBatList);

		// �ֿ�Ȩ�޿���
		List<String> whsIdList = getList((String) map.get("whsId"));// �ֿ����
		map.put("whsIdList", whsIdList);

		List<zizhu> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "iw.into_whs_dt");
			map.put("sortOrder", "desc");
		}

		poList = iwd.selectIntoWhsListOrderBy(map);
		Map tableSums = iwd.selectIntoWhsListSum(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = iwd.selectIntoWhsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {

		@JsonProperty("�ɹ���ⵥ����")
		public String intoWhsSnglId;// ��ⵥ��
		@JsonProperty("�ɹ���ⵥ����")
		public String intoWhsDt;// �������
		@JsonProperty("�ɹ����ͱ���")
		public String pursTypId;// �ɹ����ͱ��
		@JsonProperty("�ɹ���������")
		public String pursTypNm;// �ɹ���������
		@JsonProperty("�������ͱ���")
		public String formTypEncd;// �������ͱ���
		@JsonProperty("������������")
		public String formTypName;// ������������
		@JsonProperty("��������ͱ���")
		public String outIntoWhsTypId;// ��������ͱ��
		@JsonProperty("�������������")
		public String outIntoWhsTypNm;// �������������
		@JsonProperty("�շ�������")
		public String recvSendCateId;// �շ������
		@JsonProperty("�շ����")
		public String recvSendCateNm;// �շ����
		@JsonProperty("��Ӧ�̱���")
		public String provrId;// ��Ӧ�̱��
		@JsonProperty("��Ӧ������")
		public String provrNm;// ��Ӧ������
		@JsonProperty("�û�����")
		public String accNum;// �û����
		@JsonProperty("�û�����")
		public String userName;// �û�����
		@JsonProperty("���ű���")
		public String deptId;// ���ű��
		@JsonProperty("��������")
		public String deptName;// ��������
		@JsonProperty("�ɹ���������")
		public String pursOrdrId;// �ɹ��������
		@JsonProperty("����������")
		public String toGdsSnglId;// ���������
		@JsonProperty("��Ӧ�̶�����")
		public String provrOrdrNum;// ��Ӧ�̶�����
		@JsonProperty("�Ƿ�Ʊ")
		public Integer isNtBllg;// �Ƿ�Ʊ
		@JsonProperty("�Ƿ����")
		public Integer isNtStl;// �Ƿ����
		@JsonProperty("�Ƿ����")
		public Integer isNtChk;// �Ƿ����
		@JsonProperty("�����")
		public String chkr;// �����
		@JsonProperty("���ʱ��")
		public String chkTm;// ���ʱ��
//		@JsonProperty("�Ƿ�ر�")
//		public Integer isNtClos;//�Ƿ�ر�
//		@JsonProperty("�Ƿ����")
//		public Integer isNtCmplt;//�Ƿ����
		@JsonProperty("������")
		public String setupPers;// ������
		@JsonProperty("����ʱ��")
		public String setupTm;// ����ʱ��
		@JsonProperty("�޸���")
		public String mdfr;// �޸���
		@JsonProperty("�޸�ʱ��")
		public String modiTm;// �޸�ʱ��
		@JsonProperty("�Ƿ����")
		public Integer isNtBookEntry;// �Ƿ����
		@JsonProperty("������")
		public String bookEntryPers;// ������
		@JsonProperty("����ʱ��")
		public String bookEntryTm;// ����ʱ��
		@JsonProperty("������߱���")
		public String exaRep;// ������߱���
		@JsonProperty("��ͷ��ע ")
		public String memo;// ��ע
		@JsonProperty("���ձ�ע")
		public String returnMemo;// ���ձ�ע
		@JsonProperty("�Ƿ��˻�")
		public Integer isNtRtnGood;// �Ƿ����˻�

		@JsonProperty("��Դ�������ͱ���")
		public String toFormTypEncd;// ��Դ�������ͱ���
		@JsonProperty("�Ƿ�����ƾ֤")
		public Integer isNtMakeVouch;// �Ƿ�����ƾ֤
		@JsonProperty("��ƾ֤��")
		public String makVouchPers;// ��ƾ֤��
		@JsonProperty("��ƾ֤ʱ��")
		public String makVouchTm;// ��ƾ֤ʱ��

		@JsonProperty("���")
		public Long ordrNum;// ���
		@JsonProperty("�������")
		public String invtyEncd;// �������
		@JsonProperty("�������")
		public String invtyNm;// �������
		@JsonProperty("�ֿ����")
		public String whsEncd;// �ֿ����
		@JsonProperty("�ֿ�����")
		public String whsNm;// �ֿ�����
//		@JsonProperty("�ɹ������ӱ��ʶ")
//		public String pursOrdrSubTabInd;//�ɹ������ӱ��ʶ
//		@JsonProperty("�ɹ��������ӱ��ʶ")
//		public String pursToGdsSnglSubTabInd;//�ɹ��������ӱ��ʶ
		@JsonProperty("����")
		public BigDecimal qty;// ����
		@JsonProperty("����")
		public BigDecimal bxQty;// ����
		@JsonProperty("˰��")
		public BigDecimal taxRate;// ˰��
		@JsonProperty("��˰����")
		public BigDecimal noTaxUprc;// ��˰����
		@JsonProperty("��˰���")
		public BigDecimal noTaxAmt;// ��˰���
		@JsonProperty("˰��")
		public BigDecimal taxAmt;// ˰��
		@JsonProperty("��˰����")
		public BigDecimal cntnTaxUprc;// ��˰����
		@JsonProperty("��˰�ϼ�")
		public BigDecimal prcTaxSum;// ��˰�ϼ�
//		@JsonProperty("��������")
//		public BigDecimal stlQty;//��������
//		@JsonProperty("���㵥��")
//		public BigDecimal stlUprc;//���㵥��
//		@JsonProperty("������")
//		public BigDecimal stlAmt;//������
		@JsonProperty("δ��Ʊ����")
		public BigDecimal unBllgQty;// δ��Ʊ����
//		@JsonProperty("δ��Ʊ����")
//		public BigDecimal unBllgUprc;//δ��Ʊ����
//		@JsonProperty("δ��Ʊ���")
//		public BigDecimal unBllgAmt;//δ��Ʊ���
//		@JsonProperty("�ݹ�����")
//		public BigDecimal teesQty;//�ݹ�����
//		@JsonProperty("�ݹ�����")
//		public BigDecimal teesUprc;//�ݹ�����
//		@JsonProperty("�ݹ����")
//		public BigDecimal teesAmt;//�ݹ����
//		@JsonProperty("��Ӧ��Ʊ��")
//		public String crspdInvNum;//��Ӧ��Ʊ��

		@JsonProperty("��������")
		public String intlBat;// ��������
		@JsonProperty("����")
		public String batNum;// ����
		@JsonProperty("��������")
		public String prdcDt;// ��������
		@JsonProperty("ʧЧ����")
		public String invldtnDt;// ʧЧ����
		@JsonProperty("������")
		public String baoZhiQi;// ������
//		@JsonProperty("��λ����")
//		public String gdsBitEncd;//��λ����
//		@JsonProperty("�Ƿ���Ʒ")
//		public Integer isComplimentary;//�Ƿ���Ʒ
//		@JsonProperty("�Ƿ��˻���1:���  0���˻���")
//		public Integer isNtRtnGoods;//�Ƿ��˻���1:���  0���˻���
		@JsonProperty("δ��������")
		public BigDecimal returnQty;// δ��������
		@JsonProperty("����ͺ�")
		public String spcModel;// ����ͺ�
		@JsonProperty("�������")
		public String invtyCd;// �������
		@JsonProperty("���")
		public BigDecimal bxRule;// ���
		@JsonProperty("����˰��")
		public BigDecimal iptaxRate;// ����˰��
		@JsonProperty("����˰��")
		public BigDecimal optaxRate;// ����˰��
		@JsonProperty("��߽���")
		public BigDecimal highestPurPrice;// ��߽���
		@JsonProperty("����ۼ�")
		public BigDecimal loSellPrc;// ����ۼ�
		@JsonProperty("�ο��ɱ�")
		public BigDecimal refCost;// �ο��ɱ�
		@JsonProperty("�ο��ۼ�")
		public BigDecimal refSellPrc;// �ο��ۼ�
		@JsonProperty("���³ɱ�")
		public BigDecimal ltstCost;// ���³ɱ�
		@JsonProperty("������λ���")
		public String measrCorpId;// ������λ���
		@JsonProperty("������λ����")
		public String measrCorpNm;// ������λ����
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;// ��Ӧ������
//		@JsonProperty("����������")
//		public String invtyClsEncd;//����������
//		@JsonProperty("�����������")
//		public String invtyClsNm;//�����������
//		@JsonProperty("�������")
//		public String regnEncd;//�������
		@JsonProperty("��Դ�����ӱ��ʶ")
		public Long toOrdrNum;// ��Դ�����ӱ��ʶ
		@JsonProperty("���屸ע ")
		public String memos;// ��ע
	}

}
