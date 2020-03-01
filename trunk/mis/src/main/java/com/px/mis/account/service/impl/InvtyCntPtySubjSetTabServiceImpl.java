package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.dao.InvtyCntPtySubjSetTabDao;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;
import com.px.mis.account.service.InvtyCntPtySubjSetTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.RecvSendCateDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//����Է���Ŀ���ñ�
@Service
@Transactional
public class InvtyCntPtySubjSetTabServiceImpl extends poiTool implements InvtyCntPtySubjSetTabService {
	@Autowired
	private InvtyCntPtySubjSetTabDao invtyCntPtSubjSetTabDao;
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
//	private BizMemDocDao bizMemDoc;
	@Autowired
	private RecvSendCateDao recvSendCateDao;
	@Autowired
	private AcctItmDocDao acctItmDocDao;

	@Override
	public ObjectNode insertInvtyCntPtySubjSetTab(InvtyCntPtySubjSetTab invtyCntPtySubjSetTab) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCntPtySubjSetTab.
		 * getInvtyEncd())==null){ on.put("isSuccess", false); on.put("message",
		 * "�������"+invtyCntPtySubjSetTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�"); }else
		 */
		if (invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCntPtySubjSetTab.getInvtyBigClsEncd()) == null) {
			on.put("isSuccess", false);
			on.put("message", "����������" + invtyCntPtySubjSetTab.getInvtyBigClsEncd() + "�����ڣ�����ʧ�ܣ�");
		} else/*
				 * if(deptDocDao.selectDeptDocByDeptEncd(invtyCntPtySubjSetTab.getDeptId())==
				 * null){ on.put("isSuccess", false); on.put("message",
				 * "���ű��"+invtyCntPtySubjSetTab.getDeptId()+"�����ڣ�����ʧ�ܣ�"); }else
				 */if (recvSendCateDao
				.selectRecvSendCateByRecvSendCateId(invtyCntPtySubjSetTab.getRecvSendCateId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�շ������" + invtyCntPtySubjSetTab.getRecvSendCateId() + "�����ڣ�����ʧ�ܣ�");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getCntPtySubjId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�Է���Ŀ����" + invtyCntPtySubjSetTab.getCntPtySubjId() + "�����ڣ�����ʧ�ܣ�");
		} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getTeesSubjEncd()) == null) {
			on.put("isSuccess", false);
			on.put("message", "�ݹ���Ŀ����" + invtyCntPtySubjSetTab.getTeesSubjEncd() + "�����ڣ�����ʧ�ܣ�");
		} else {
			int insertResult = invtyCntPtSubjSetTabDao.insertInvtyCntPtySubjSetTab(invtyCntPtySubjSetTab);
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
	public ObjectNode updateInvtyCntPtySubjSetTabByOrdrNum(List<InvtyCntPtySubjSetTab> invtyCntPtySubjSetTabList) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (InvtyCntPtySubjSetTab invtyCntPtySubjSetTab : invtyCntPtySubjSetTabList) {
			Integer ordrNum = invtyCntPtySubjSetTab.getOrdrNum();
			if (ordrNum == null) {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��,��������Ϊ��");
			} else if (invtyCntPtSubjSetTabDao
					.selectInvtyCntPtySubjSetTabByOrdrNum(invtyCntPtySubjSetTab.getOrdrNum()) == null) {
				on.put("isSuccess", false);
				on.put("message", "����ʧ�ܣ����" + invtyCntPtySubjSetTab.getOrdrNum() + "�����ڣ�");
			} else if (invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCntPtySubjSetTab.getInvtyBigClsEncd()) == null) {
				on.put("isSuccess", false);
				on.put("message", "����������" + invtyCntPtySubjSetTab.getInvtyBigClsEncd() + "�����ڣ�����ʧ�ܣ�");
			} else if (recvSendCateDao
					.selectRecvSendCateByRecvSendCateId(invtyCntPtySubjSetTab.getRecvSendCateId()) == null) {
				on.put("isSuccess", false);
				on.put("message", "�շ������" + invtyCntPtySubjSetTab.getRecvSendCateId() + "�����ڣ�����ʧ�ܣ�");
			} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getCntPtySubjId()) == null) {
				on.put("isSuccess", false);
				on.put("message", "�Է���Ŀ����" + invtyCntPtySubjSetTab.getCntPtySubjId() + "�����ڣ�����ʧ�ܣ�");
			} else if (acctItmDocDao.selectAcctItmDocBySubjId(invtyCntPtySubjSetTab.getTeesSubjEncd()) == null) {
				on.put("isSuccess", false);
				on.put("message", "�ݹ���Ŀ����" + invtyCntPtySubjSetTab.getTeesSubjEncd() + "�����ڣ�����ʧ�ܣ�");
			} else {
				int updateResult = invtyCntPtSubjSetTabDao.updateInvtyCntPtySubjSetTabByOrdrNum(invtyCntPtySubjSetTab);
				if (updateResult == 1) {
					on.put("isSuccess", true);
					on.put("message", "���³ɹ�");
				} else {
					on.put("isSuccess", false);
					on.put("message", "����ʧ��");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����" + ordrNum + "�����ڣ�");
		} else {
			Integer deleteResult = invtyCntPtSubjSetTabDao.deleteInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
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
	public InvtyCntPtySubjSetTab selectInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum) {
		InvtyCntPtySubjSetTab selectByOrdrNum = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
		return selectByOrdrNum;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyCntPtySubjSetTabList(Map map) {
		String resp = "";
		List<InvtyCntPtySubjSetTab> list = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabList(map);
		int count = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabCount();
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", true, "��ѯ�ɹ���",
					count, pageNo, pageSize, listNum, pages, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ��
	@Override
	public String deleteInvtyCntPtySubjSetTabList(String ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = new HashMap<>();
			map.put("ordrNum", ordrNum);
			int a = invtyCntPtSubjSetTabDao.deleteInvtyCntPtySubjSetTabList(map);
			if (a >= 1) {
				isSuccess = true;
				message = "ɾ���ɹ���";
			} else {
				isSuccess = false;
				message = "ɾ��ʧ�ܣ�";
			}

			resp = BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTabList", isSuccess,
					message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectInvtyCntPtySubjSetTabListPrint(Map map) {
		String resp = "";
		List<InvtyCntPtySubjSetTab> list = invtyCntPtSubjSetTabDao.selectInvtyCntPtySubjSetTabList(map);

		try {
			resp = BaseJson.returnRespListAnno("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint", true,
					"��ѯ�ɹ���", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, InvtyCntPtySubjSetTab> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());
		for (Map.Entry<String, InvtyCntPtySubjSetTab> entry : pusOrderMap.entrySet()) {
			InvtyCntPtySubjSetTab selectByOrdrNum = invtyCntPtSubjSetTabDao
					.selectInvtyCntPtySubjSetTabByOrdrNum(entry.getValue().getOrdrNum());

			if (selectByOrdrNum != null) {
				throw new RuntimeException("�����ظ����� " + entry.getValue().getOrdrNum() + " ����");
			}

			try {
				invtyCntPtSubjSetTabDao.insertInvtyCntPtySubjSetTab(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");

			}
		}

		isSuccess = true;
		message = "���������ɹ���";
		try {
			resp = BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}

	// ����excle
	private Map<String, InvtyCntPtySubjSetTab> uploadScoreInfo(MultipartFile file) {
		Map<String, InvtyCntPtySubjSetTab> temp = new HashMap<>();
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
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}

				String orderNo = GetCellData(r, "���");
				//��Stringװ��ΪDouble
				Double orderNo1 = Double.parseDouble(orderNo);
				//��Doubleװ��Ϊint;
				int orderNo2 = orderNo1.intValue();
				// ����ʵ����
				InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = new InvtyCntPtySubjSetTab();
				if (temp.containsKey(orderNo)) {
					invtyCntPtySubjSetTab = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������

				invtyCntPtySubjSetTab.setOrdrNum(Integer.valueOf(orderNo2));// ���Integer
				invtyCntPtySubjSetTab.setDeptId(GetCellData(r,"���ű���"));
				invtyCntPtySubjSetTab.setRecvSendCateId(GetCellData(r,"�շ�������"));
				invtyCntPtySubjSetTab.setRecvSendCateNm(GetCellData(r,"�շ��������"));
				invtyCntPtySubjSetTab.setInvtyEncd(GetCellData(r,"�������"));
				invtyCntPtySubjSetTab.setInvtyNm(GetCellData(r,"�������"));
				invtyCntPtySubjSetTab.setInvtyBigClsEncd(GetCellData(r,"����������"));
				invtyCntPtySubjSetTab.setInvtyBigClsNm(GetCellData(r,"�����������"));
				invtyCntPtySubjSetTab.setCntPtySubjId(GetCellData(r,"�Է���Ŀ����"));
				invtyCntPtySubjSetTab.setCntPtySubjNm(GetCellData(r,"�Է���Ŀ����"));
				invtyCntPtySubjSetTab.setTeesSubjEncd(GetCellData(r,"�ݹ���Ŀ����"));
				invtyCntPtySubjSetTab.setTeesSubjNm(GetCellData(r,"�ݹ���Ŀ����"));
				invtyCntPtySubjSetTab.setMemo(GetCellData(r,"��ע"));
				invtyCntPtySubjSetTab.setDeptNm(GetCellData(r,"��������"));
				/*invtyCntPtySubjSetTab.setInvtyEncd(GetCellData(r, "�������"));// �������
				invtyCntPtySubjSetTab.setInvtyBigClsEncd(GetCellData(r, "����������"));// ����������
//				invtyCntPtySubjSetTab.setDeptId(GetCellData(r, "���ű��"));// ���ű��
				invtyCntPtySubjSetTab.setRecvSendCateId(GetCellData(r, "�շ�������"));// �շ������
				invtyCntPtySubjSetTab.setCntPtySubjId(GetCellData(r, "�Է���Ŀ����"));// �Է���Ŀ����
				invtyCntPtySubjSetTab.setTeesSubjEncd(GetCellData(r, "�ݹ���Ŀ����"));// �ݹ���Ŀ����
				invtyCntPtySubjSetTab.setMemo(GetCellData(r, "��ע"));// ��ע*/
				/*invtyCntPtySubjSetTab.setInvtyNm(GetCellData(r, "�������"));// �������
				invtyCntPtySubjSetTab.setInvtyBigClsNm(GetCellData(r, "�����������"));// �����������
				invtyCntPtySubjSetTab.setDeptNm(GetCellData(r, "��������"));// ��������
				invtyCntPtySubjSetTab.setRecvSendCateNm(GetCellData(r, "�շ��������"));// �շ��������
				invtyCntPtySubjSetTab.setCntPtySubjNm(GetCellData(r, "�Է���Ŀ����"));// �Է���Ŀ����
				invtyCntPtySubjSetTab.setTeesSubjNm(GetCellData(r, "�ݹ���Ŀ����"));// �ݹ���Ŀ����*/

				temp.put(orderNo, invtyCntPtySubjSetTab);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
		}
		return temp;
	}

}
