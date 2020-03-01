package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.PursOrdrDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.service.PursOrdrService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;

/*�ɹ���������*/
@Transactional
@Service
public class PursOrdrServiceImpl extends poiTool implements PursOrdrService {
	@Autowired
	private PursOrdrDao pod;
	@Autowired
	private PursOrdrSubDao posd;
	@Autowired
	private ToGdsSnglDao tgsd;
	@Autowired
	private InvtyDocDao invtyDocDao;
	// ������
	@Autowired
	private GetOrderNo getOrderNo;

	// �����ɹ�����
	@Override
	public String addPursOrdr(String userId, PursOrdr pursOrdr, List<PursOrdrSub> pursOrdrSubList, String loginTime)
			throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			// ��ȡ������
			String number = getOrderNo.getSeqNo("CG", userId, loginTime);
			if (pod.selectPursOrdrById(number) != null) {
				isSuccess = false;
				message = "����" + number + "�Ѵ��ڣ����������룡";
			} else {
				pursOrdr.setPursOrdrId(number);
				for (PursOrdrSub pursOrdrSub : pursOrdrSubList) {
					pursOrdrSub.setPursOrdrId(pursOrdr.getPursOrdrId());
					if (pursOrdrSub.getPlanToGdsDt() == "") {
						pursOrdrSub.setPlanToGdsDt(null);
					}
					pursOrdrSub.setUnToGdsQty(pursOrdrSub.getQty());// Ĭ��δ��������=�ɹ���������
					pursOrdrSub.setUnApplPayQty(pursOrdrSub.getQty());// Ĭ��δ��������=�ɹ���������
					pursOrdrSub.setUnApplPayAmt(pursOrdrSub.getPrcTaxSum());// Ĭ��δ������=�ɹ�������˰���
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(pursOrdrSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
						isSuccess = false;
						message = "����Ϊ" + pursOrdr.getPursOrdrId() + "�Ĳɹ�������Ӧ�Ĵ��:" + pursOrdrSub.getInvtyEncd()
								+ "û�������Ƿ�ɹ����ԣ��޷����棡";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "����Ϊ" + pursOrdr.getPursOrdrId() + "�Ĳɹ�������Ӧ�Ĵ��:" + pursOrdrSub.getInvtyEncd()
								+ "�ǿɲɹ�������޷����棡";
						throw new RuntimeException(message);
					}
				}
				int a = pod.insertPursOrdr(pursOrdr);
				int b = posd.insertPursOrdrSub(pursOrdrSubList);
				if (a >= 1 && b >= 1) {
					message = "�����ɹ���";
					isSuccess = true;
				} else {
					isSuccess = false;
					message = "����ʧ�ܣ�";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/addPursOrdr", isSuccess, message, pursOrdr);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());

		}
		return resp;
	}

	// �޸Ĳɹ�����
	@Override
	public String editPursOrdr(PursOrdr pursOrdr, List<PursOrdrSub> pursOrdrSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<PursOrdrSub> pursOrdrSubSet = new TreeSet();
//			pursOrdrSubSet.addAll(pursOrdrSubList);
//			if(pursOrdrSubSet.size() < pursOrdrSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/PursOrdr/editPursOrdr", false, "��������ϸ�����������ͬ�Ĵ����",null);
//				return resp;
//			}
			for (PursOrdrSub pursOrdrSub : pursOrdrSubList) {
				pursOrdrSub.setPursOrdrId(pursOrdr.getPursOrdrId());
				if (pursOrdrSub.getPlanToGdsDt() == "") {
					pursOrdrSub.setPlanToGdsDt(null);
				}
				pursOrdrSub.setUnToGdsQty(pursOrdrSub.getQty());// Ĭ��δ��������=�ɹ���������
				pursOrdrSub.setUnApplPayQty(pursOrdrSub.getQty());// Ĭ��δ��������=�ɹ���������
				pursOrdrSub.setUnApplPayAmt(pursOrdrSub.getPrcTaxSum());// Ĭ��δ������=�ɹ�������˰���
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(pursOrdrSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
					isSuccess = false;
					message = "����Ϊ" + pursOrdr.getPursOrdrId() + "�Ĳɹ�������Ӧ�Ĵ��:" + pursOrdrSub.getInvtyEncd()
							+ "û�������Ƿ�ɹ����ԣ��޷����棡";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "����Ϊ" + pursOrdr.getPursOrdrId() + "�Ĳɹ�������Ӧ�Ĵ��:" + pursOrdrSub.getInvtyEncd()
							+ "�ǿɲɹ�������޷����棡";
					throw new RuntimeException(message);
				}
			}
			int a = posd.deletePursOrdrSubByPursOrdrId(pursOrdr.getPursOrdrId());
			int b = pod.updatePursOrdrByPursOrdrId(pursOrdr);
			int c = posd.insertPursOrdrSub(pursOrdrSubList);
			if (a >= 1 && b >= 1 && c >= 1) {
				message = "���³ɹ���";
				isSuccess = true;
			} else {
				isSuccess = false;
				message = "����ʧ�ܣ�";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/editPursOrdr", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// ɾ���ɹ�����
	@Override
	public String deletePursOrdr(String pursOrdrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (pod.selectPursOrdrByPursOrdrId(pursOrdrId) != null) {
			pod.deletePursOrdrByPursOrdrId(pursOrdrId);
			isSuccess = true;
			message = "ɾ���ɹ���";
		} else {
			isSuccess = false;
			message = "����" + pursOrdrId + "�����ڣ�";
		}
		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/deletePursOrdr", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ���ձ����ѯ�ɹ�����
	@Override
	public String queryPursOrdr(String pursOrdrId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<PursOrdrSub> PursOrdrSub = new ArrayList<>(); */
		PursOrdr pursOrdr = pod.selectPursOrdrByPursOrdrId(pursOrdrId);
		if (pursOrdr != null) {
			/* PursOrdrSub=posd.selectPursOrdrSubByPursOrdrId(pursOrdrId); */
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "����" + pursOrdrId + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/queryPursOrdr", isSuccess, message, pursOrdr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ҳ��ѯ�ɹ������б�
	@Override
	public String queryPursOrdrList(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<PursOrdr> poList = pod.selectPursOrdrList(map);

		int count = pod.selectPursOrdrCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deletePursOrdrList(String pursOrdrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(pursOrdrId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (pod.selectPursOrdrIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deletePursOrdrList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
				}
				if (a >= 1) {
					isSuccess = true;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "���ݺ�Ϊ��" + lists2.toString() + "�Ķ���ɾ��ʧ�ܣ�\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "���ݺ�Ϊ��" + lists3.toString() + "�Ķ����ѱ���ˣ��޷�ɾ����\n";
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/deletePursOrdrList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	public int deletePursOrdrList(List<String> lists2) throws Exception {
		pod.insertPursOrdrDl(lists2);
		posd.insertPursOrdrSubDl(lists2);

		return pod.deletePursOrdrList(lists2);
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

	// �ɹ��������ʱ�޸����״̬
	@Override
	public String updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		for (PursOrdr pOrdr : pursOrdr) {
			if (pOrdr.getIsNtChk() == 1) {
				if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 0) {
					int a = pod.updatePursOrdrIsNtChk(pOrdr);
					if (a >= 1) {
						isSuccess = true;
						message += pOrdr.getPursOrdrId() + "�ɹ�������˳ɹ���\n";
					} else {
						isSuccess = false;
						message += pOrdr.getPursOrdrId() + "�ɹ��������ʧ�ܣ�\n";
						throw new RuntimeException(message);
					}
				} else if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 1) {
					isSuccess = false;
					message += pOrdr.getPursOrdrId() + "�õ�������ˣ�����Ҫ�ظ����\n";
				}
			} else if (pOrdr.getIsNtChk() == 0) {
				if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 1) {
					List<ToGdsSngl> toGdsSnglList = tgsd.selectToGdsSnglByPursOrdrId(pOrdr.getPursOrdrId());
					if (toGdsSnglList.size() > 0) {
						isSuccess = false;
						message += "���ݺ�Ϊ" + pOrdr.getPursOrdrId() + "�Ĳɹ������Ѵ������ε��ݡ������������޷�����";
					} else {
						int a = pod.updatePursOrdrIsNtChk(pOrdr);
						if (a >= 1) {
							isSuccess = true;
							message += pOrdr.getPursOrdrId() + "�ɹ���������ɹ���\n";
						} else {
							isSuccess = false;
							message += pOrdr.getPursOrdrId() + "�ɹ���������ʧ�ܣ�\n";
							throw new RuntimeException(message);
						}
					}
				} else {
					isSuccess = false;
					message += pOrdr.getPursOrdrId() + "�õ���δ��ˣ�������˸õ���\n";
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/updatePursOrdrIsNtChk", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ӡ���������
	@Override
	public String printingPursOrdrList(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		List<zizhu> pursOrdrList = pod.printingPursOrdrList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/PursOrdr/printingPursOrdrList", true, "��ѯ�ɹ���", null,
					pursOrdrList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����
	public String uploadFileAddDb(MultipartFile file, int i, HashMap params) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, PursOrdr> pusOrderMap = null;
		if (i == 0) {
			pusOrderMap = uploadScoreInfo(file, params);
		} else if (i == 1) {
			pusOrderMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "������쳣����";
			throw new RuntimeException(message);
		}

		// ��MapתΪList��Ȼ���������븸������
		List<PursOrdr> pursOrdrList = pusOrderMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<PursOrdr>> pursOrdrLists = Lists.partition(pursOrdrList, 1000);

		for (List<PursOrdr> pursOrdr : pursOrdrLists) {
			try {
				pod.insertPursOrdrUpload(pursOrdr);
			} catch (DuplicateKeyException e) {
				String err = e.getRootCause().getMessage();
				err = err.substring(17, 35);
				throw new RuntimeException("����" + err + "�ظ�����,��������,���ߵײ��п���!");
			}
		}
		List<PursOrdrSub> pursOrdrSubList = new ArrayList<>();
		int flag = 0;
		for (PursOrdr entry : pursOrdrList) {
			flag++;
			// ���������ֱ�͸���Ĺ����ֶ�
			List<PursOrdrSub> tempList = entry.getPursOrdrSub();
			tempList.forEach(s -> s.setPursOrdrId(entry.getPursOrdrId()));
			pursOrdrSubList.addAll(tempList);
			// �������룬ÿ���ڵ���1000������һ��
			if (pursOrdrSubList.size() >= 1000 || pusOrderMap.size() == flag) {
				try {
					posd.insertPursOrdrSub(pursOrdrSubList);
				} catch (Exception e) {
					throw new RuntimeException("�ӱ���������!����ϵ��̨");
				}

				pursOrdrSubList.clear();
			}
		}
		isSuccess = true;
		message = "�ɹ���������ɹ���";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, PursOrdr> uploadScoreInfo(MultipartFile file, HashMap params) {
		Map<String, PursOrdr> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				String orderNo = getOrderNo.getSeqNo("CG", (String) params.get("accNum"),
						(String) params.get("loginTime"));// GetCellData(r, "�ɹ���������");

				// ����ʵ����
				PursOrdr pursOrdr = new PursOrdr();
				if (temp.containsKey(orderNo)) {
					pursOrdr = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				pursOrdr.setPursTypId(GetCellData(r, "�ɹ����ͱ���"));// �ɹ����ͱ���
				pursOrdr.setPursOrdrId(orderNo);// �ɹ���������
				if (GetCellData(r, "�ɹ���������") == null || GetCellData(r, "�ɹ���������").equals("")) {
					pursOrdr.setPursOrdrDt(null);
				} else {
					pursOrdr.setPursOrdrDt(GetCellData(r, "�ɹ���������").replaceAll("[^0-9:-]", " "));// �ɹ���������
				}
				pursOrdr.setProvrId(GetCellData(r, "��Ӧ�̱���"));// ��Ӧ�̱���
				pursOrdr.setDeptId(GetCellData(r, "���ű���"));// ���ű���
				pursOrdr.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				pursOrdr.setUserName(GetCellData(r, "ҵ��Ա"));// �û�����
				pursOrdr.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				pursOrdr.setFormTypEncd("001");// �������ͱ��� GetCellData(r, "�������ͱ���")
				pursOrdr.setSetupPers(GetCellData(r, "������"));// ������
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					pursOrdr.setSetupTm(null);
				} else {
					pursOrdr.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
//				pursOrdr.setSetupTm((String) params.get("loginTime"));// ����ʱ��
				pursOrdr.setIsNtChk(new Double(GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
				pursOrdr.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					pursOrdr.setChkTm(null);
				} else {
					pursOrdr.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				pursOrdr.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					pursOrdr.setModiTm(null);
				} else {
					pursOrdr.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				pursOrdr.setMemo(GetCellData(r, "��ͷ��ע"));// ��ע
				List<PursOrdrSub> pursOrdrSubList = pursOrdr.getPursOrdrSub();// �ɹ������ӱ�
				if (pursOrdrSubList == null) {
					pursOrdrSubList = new ArrayList<>();
				}
				PursOrdrSub pursOrderSub = new PursOrdrSub();
//				pursOrderSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));Ӧ���Զ�����

//				String invtyEncd = GetCellData(r, "�������");	
				pursOrderSub.setInvtyEncd(GetCellData(r, "�������"));
				pursOrderSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				pursOrderSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// ����
//				if ((GetBigDecimal(GetCellData(r, "����"), 8) == null || (GetBigDecimal(GetCellData(r, "����"), 8).equals("")) {
//					
//				};
				pursOrderSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				pursOrderSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				pursOrderSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8));// 8��ʾС��λ�� //��˰����
				pursOrderSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8));// 8��ʾС��λ�� //��˰���
				pursOrderSub.setTaxAmt(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				pursOrderSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "��˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
				pursOrderSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				pursOrderSub.setUnToGdsQty(GetBigDecimal(GetCellData(r, "δ��������"), 8));// 8��ʾС��λ�� //δ��������
				pursOrderSub.setUnApplPayQty(GetBigDecimal(GetCellData(r, "δ���븶������"), 8));// 8��ʾС��λ�� //δ��������
				pursOrderSub.setUnApplPayAmt(GetBigDecimal(GetCellData(r, "δ���븶����"), 8));// 8��ʾС��λ�� //δ������
				if (GetCellData(r, "�ƻ�����ʱ��") == null || GetCellData(r, "�ƻ�����ʱ��").equals("")) {
					pursOrderSub.setPlanToGdsDt(null);
				} else {
					pursOrderSub.setPlanToGdsDt(GetCellData(r, "�ƻ�����ʱ��").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}
				pursOrderSub.setMemo(GetCellData(r, "���屸ע"));
				pursOrdrSubList.add(pursOrderSub);

				pursOrdr.setPursOrdrSub(pursOrdrSubList);
				temp.put(orderNo, pursOrdr);

			}
			fileIn.close();

		} catch (Exception e) {

			if (e instanceof OfficeXmlFileException) {
				throw new RuntimeException("Excel�汾����,����ļ������Ϊxls��ʽ(97-2003�汾�ļ�)");
			}
			if (e instanceof RuntimeException) {
				e.printStackTrace();
				throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
			}
		}
		return temp;
	}

	// ����excle
	private Map<String, PursOrdr> uploadScoreInfoU8(MultipartFile file) {
		Map<String, PursOrdr> temp = new HashMap<>();
		int j = 0;
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
				String orderNo = GetCellData(r, "��������");
				// ����ʵ����
				PursOrdr pursOrdr = new PursOrdr();
				if (temp.containsKey(orderNo)) {
					pursOrdr = temp.get(orderNo);
				}
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				pursOrdr.setPursTypId("1");// �ɹ����ͱ���
				pursOrdr.setPursOrdrId(orderNo);// �ɹ���������
				if (GetCellData(r, "����") == null || GetCellData(r, "����").equals("")) {
					pursOrdr.setPursOrdrDt(null);
				} else {
					pursOrdr.setPursOrdrDt(GetCellData(r, "����").replaceAll("[^0-9:-]", " "));// �ɹ���������
				}
				pursOrdr.setProvrId(GetCellData(r, "��Ӧ�̱���"));// ��Ӧ�̱���
				pursOrdr.setDeptId(GetCellData(r, "���ű���"));// ���ű���
				pursOrdr.setAccNum(GetCellData(r, "ҵ��Ա����"));// �û�����
				pursOrdr.setUserName(GetCellData(r, "ҵ��Ա"));// �û�����
				pursOrdr.setProvrOrdrNum(GetCellData(r, "��Ӧ�̶�����"));// ��Ӧ�̶�����
				pursOrdr.setFormTypEncd("001");// �������ͱ���
				pursOrdr.setSetupPers(GetCellData(r, "�Ƶ���"));// ������
				if (GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) {
					pursOrdr.setSetupTm(null);
				} else {
					pursOrdr.setSetupTm(GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				pursOrdr.setIsNtChk(1);// �Ƿ����
				pursOrdr.setChkr(GetCellData(r, "�����"));// �����
				if (GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) {
					pursOrdr.setChkTm(null);
				} else {
					pursOrdr.setChkTm(GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " "));// ���ʱ��
				}
				pursOrdr.setMdfr(GetCellData(r, "�޸���")); // �޸���
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					pursOrdr.setModiTm(null);
				} else {
					pursOrdr.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				pursOrdr.setMemo(GetCellData(r, "��ע"));// ��ע
				List<PursOrdrSub> pursOrdrSubList = pursOrdr.getPursOrdrSub();// �ɹ������ӱ�
				if (pursOrdrSubList == null) {
					pursOrdrSubList = new ArrayList<>();
				}
				PursOrdrSub pursOrderSub = new PursOrdrSub();
//				pursOrderSub.setOrdrNum(Long.parseLong(GetCellData(r, "���")));
				pursOrderSub.setInvtyEncd(GetCellData(r, "�������"));// �������
				pursOrderSub.setQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				pursOrderSub.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //����
				pursOrderSub.setBxRule(GetBigDecimal(GetCellData(r, "���"), 8));// 8��ʾС��λ�� //���
				pursOrderSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "ԭ�Һ�˰����"), 8));// 8��ʾС��λ�� //��˰����
				pursOrderSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "���ҵ���"), 8));// 8��ʾС��λ�� //��˰����
				pursOrderSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "���ҽ��"), 8));// 8��ʾС��λ�� //��˰���
				pursOrderSub.setTaxAmt(GetBigDecimal(GetCellData(r, "����˰��"), 8));// 8��ʾС��λ�� //˰��
				pursOrderSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "���Ҽ�˰�ϼ�"), 8));// 8��ʾС��λ�� //��˰�ϼ�
//				˰��=��ͷ˰��
				pursOrderSub.setTaxRate(GetBigDecimal(GetCellData(r, "˰��"), 8));// 8��ʾС��λ�� //˰��
				pursOrderSub.setUnToGdsQty(BigDecimal.ZERO);// 8��ʾС��λ�� //δ��������
				pursOrderSub.setUnApplPayQty(GetBigDecimal(GetCellData(r, "����"), 8));// 8��ʾС��λ�� //δ��������
				pursOrderSub.setUnApplPayAmt(GetBigDecimal(GetCellData(r, "���Ҽ�˰�ϼ�"), 8));// 8��ʾС��λ�� //δ������
				if (GetCellData(r, "�ƻ���������") == null || GetCellData(r, "�ƻ���������").equals("")) {
					pursOrderSub.setPlanToGdsDt(null);
				} else {
					pursOrderSub.setPlanToGdsDt(GetCellData(r, "�ƻ���������").replaceAll("[^0-9:-]", " "));// �ƻ�����ʱ��
				}
				pursOrderSub.setMemo(GetCellData(r, "���屸ע"));
				pursOrdrSubList.add(pursOrderSub);

				pursOrdr.setPursOrdrSub(pursOrdrSubList);
				temp.put(orderNo, pursOrdr);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}

	// �ɹ���ϸ��ѯ
	@Override
	public String queryPursOrdrByInvtyEncd(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			List<Map> poList = pod.selectPursOrdrByInvtyEncd(map);
			Map tableSums = pod.selectPursOrdrByInvtyEncdSums(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = pod.selectPursOrdrByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrByInvtyEncd", true, "��ѯ�ɹ���", count, pageNo,
						pageSize, listNum, pages, poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = pod.selectPursOrdrByInvtyEncd(map);
			try {
				resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrByInvtyEncd", true, "��ѯ�ɹ���", poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	// �ɹ���ϸ-����
	@Override
	public String queryPursOrdrByInvtyEncdPrint(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<Map> poList = pod.selectPursOrdrByInvtyEncdPrint(map);
		try {
			resp = BaseJson.returnRespListAnno("purc/PursOrdr/queryPursOrdrByInvtyEncdPrint", true, "�����ɹ���", poList);

		} catch (IOException e) {

			e.printStackTrace();

		}

		return resp;
	}

	// ����ʱ��ѯ����δ���븶�������Ĳɹ������ӱ���Ϣ
	@Override
	public String selectUnApplPayQtyByPursOrdrId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<PursOrdrSub> pursOrdrSubList = posd.selectUnApplPayQtyByPursOrdrId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/PursOrdr/selectUnApplPayQtyByPursOrdrId", true, "��ѯ�ɹ���", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ʱ��ѯ����δ���������Ĳɹ������ӱ���Ϣ
	@Override
	public String selectUnToGdsQtyByPursOrdrId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<PursOrdrSub> pursOrdrSubList = posd.selectUnToGdsQtyByPursOrdrId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/PursOrdr/selectUnToGdsQtyByPursOrdrId", true, "��ѯ�ɹ���", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ���������������뵥����ʱ������ѯ�ɹ�����������Ϣ
	@Override
	public String queryPursOrdrLists(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		List<PursOrdr> poList = pod.selectPursOrdrLists(map);
		int count = poList.size();
		if (poList.size()<(int) map.get("num")) {
			//do nothing 
		}else {
			poList = poList.subList((int) map.get("index"),
					(int) map.get("num") + (int) map.get("index"));
		}
//		int count = pod.selectPursOrdrCounts(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrLists", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ��ҳ+�����ѯ�ɹ������б�
	@Override
	public String queryPursOrdrListOrderBy(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// �ɹ�������
		List<String> provrIdList = getList((String) map.get("provrId"));// �ͻ�����
		List<String> accNumList = getList((String) map.get("accNum"));// ҵ��Ա����
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// ����������
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// �������
		List<String> deptIdList = getList((String) map.get("deptId"));// ���ű���
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// �ͻ�������
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// �������

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<zizhu> poList;

		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "po.purs_ordr_dt");
			map.put("sortOrder", "desc");
		}

		poList = pod.selectPursOrdrListOrderBy(map);
		Map tableSums = pod.selectPursOrdrListSum(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = pod.selectPursOrdrCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrListOrderBy", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ������
	public static class zizhu {
		// ����
		@JsonProperty("���")
		public Long ordrNum;// ���
		@JsonProperty("�ɹ���������")
		public String pursOrdrId;// �ɹ���������

		@JsonProperty("�ɹ���������")
		public String pursOrdrDt;// �ɹ���������

		@JsonProperty("�ɹ����ͱ���")
		public String pursTypId;// �ɹ����ͱ���

		@JsonProperty("�ɹ���������")
		public String pursTypNm;// �ɹ���������
		@JsonProperty("�������ͱ���")
		public String formTypEncd;// �������ͱ���
		@JsonProperty("������������")
		public String formTypName;// ������������
		@JsonProperty("��Ӧ�̱���")
		public String provrId;// ��Ӧ�̱���

		@JsonProperty("��Ӧ������")
		public String provrNm;// ��Ӧ������
		@JsonProperty("ҵ��Ա����")
		public String accNum;// �û�����

		@JsonProperty("ҵ��Ա")
		public String userName;// �û�����

		@JsonProperty("���ű���")
		public String deptId;// ���ű���

		@JsonProperty("��������")
		public String deptName;// ��������
		@JsonProperty("��Ӧ�̶�����")
		public String provrOrdrNum;// ��Ӧ�̶�����

		@JsonProperty("�Ƿ����")
		public Integer isNtChk;// �Ƿ����

		@JsonProperty("�����")
		public String chkr;// �����

		@JsonProperty("���ʱ��")
		public String chkTm;// ���ʱ��

		@JsonProperty("������")
		public String setupPers;// ������

		@JsonProperty("����ʱ��")
		public String setupTm;// ����ʱ��

		@JsonProperty("�޸���")
		public String mdfr;// �޸���

		@JsonProperty("�޸�ʱ��")
		public String modiTm;// �޸�ʱ��

		@JsonProperty("��ͷ��ע")
		public String memo;// ��ע

		// �ӱ�
		@JsonProperty("�������")
		public String invtyEncd;// �������

		@JsonProperty("�������")
		public String invtyNm;// �������
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

		@JsonProperty("˰��")
		public BigDecimal taxRate;// ˰��

//		@JsonProperty("�ۿ۶�")
//		public BigDecimal discntAmt;// �ۿ۶�(Ŀǰδ�õ�)
		@JsonProperty("����")
		public BigDecimal qty;// ����

		@JsonProperty("����")
		public BigDecimal bxQty;// ����

		@JsonProperty("�ƻ�����ʱ��")
		public String planToGdsDt;// �ƻ�����ʱ��

		@JsonProperty("������λ����")
		public String measrCorpId;// ������λ����
		@JsonProperty("������λ����")
		public String measrCorpNm;// ������λ����
//		@JsonProperty("�Ƿ���Ʒ")
//		public Integer isComplimentary;// �Ƿ���Ʒ
		// �����������ֶΡ�������λ����
		@JsonProperty("����ͺ�")
		public String spcModel;// ����ͺ�
		@JsonProperty("�������")
		public String invtyCd;// �������
		@JsonProperty("���")
		public BigDecimal bxRule;// ���

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
		@JsonProperty("��Ӧ������")
		public String crspdBarCd;// ��Ӧ������
		@JsonProperty("δ��������")
		public BigDecimal unToGdsQty;// δ��������

		@JsonProperty("δ���븶������")
		public BigDecimal unApplPayQty;// δ���븶������

		@JsonProperty("δ���븶����")
		public BigDecimal unApplPayAmt;// δ���븶����

		@JsonProperty("������")
		public String baoZhiQi;// ������

		@JsonProperty("���屸ע")
		public String memos;// ���屸ע

	}

}
