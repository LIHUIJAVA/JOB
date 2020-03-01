package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.account.dao.TermBgnBalDao;
import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.account.service.TermBgnBalService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.poiTool;

/*�����ڳ�����*/
@Transactional()
@Service
public class TermBgnBalServiceImpl extends poiTool implements TermBgnBalService {
	@Autowired
	private TermBgnBalDao tbbd; // �ڳ����

	// �����ڳ����
	@Override
	public String insertTermBgnBal(TermBgnBal termBgnBal) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			termBgnBal.setIsNtBookEntry(0);
			int a = tbbd.insertTermBgnBal(termBgnBal);
			if (a >= 1) {
				isSuccess = true;
				message = "�ڳ���������ɹ���";
			} else {
				isSuccess = false;
				message = "�ڳ��������ʧ�ܣ�";
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/addTermBgnBal", isSuccess, message, termBgnBal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �޸��ڳ����
	@Override
	public String updateTermBgnBalByOrdrNum(List<TermBgnBal> termBgnBalList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			for (TermBgnBal termBgnBal : termBgnBalList) {
				int a = tbbd.updateTermBgnBalByOrdrNum(termBgnBal);
				if (a >= 1) {
					isSuccess = true;
					message = "�ڳ�����޸ĳɹ���";
				} else {
					isSuccess = false;
					message = "�ڳ�����޸�ʧ�ܣ������ԭ�����޸ģ�";
				}
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/editTermBgnBal", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

//�ڳ����-��ѯ
	@Override
	public String queryTermBgnBalList(Map map) {
		String resp = "";
		List<TermBgnBal> poList = tbbd.selectTermBgnBalList(map);
		int count = tbbd.selectTermBgnBalCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("account/TermBgnBal/queryTermBgnBalList", true, "��ѯ�ɹ���", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// �ڳ����-����
	@Override
	public String queryTermBgnBalListPrint(Map map) {
		String resp = "";
		List<TermBgnBal> poList = tbbd.selectTermBgnBalList(map);

		try {
			resp = BaseJson.returnRespListAnno("account/TermBgnBal/queryTermBgnBalList", true, "��ѯ�ɹ���", poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����ɾ���ڳ����
	@Override
	public String deleteTermBgnBalByOrdrNum(String ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = CommonUtil.strToList(ordrNum);
			List<Integer> lists2 = new ArrayList<>();
			List<Integer> lists3 = new ArrayList<>();
			for (String list : lists) {
				TermBgnBal termBgnBal = tbbd.selectTermBgnBalByOrdrNum(Integer.valueOf(list));
				if (termBgnBal != null) {
					if (termBgnBal.getIsNtBookEntry() == 0) {
						lists2.add(Integer.valueOf(list));
					} else {
						lists3.add(Integer.valueOf(list));
					}
				}

			}
			if (lists2.size() > 0) {
				int a = tbbd.deleteTermBgnBalByOrdrNum(lists2);
				if (a >= 1) {
					isSuccess = true;
					message += "�ڳ����ɾ���ɹ�!\n";
				} else {
					isSuccess = false;
					message += "�ڳ����ɾ��ʧ��!\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "�����ڳ�����ѱ����ˣ��޷�ɾ����\n";
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/deleteTermBgnBalList", isSuccess, message, null);
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
	public List<Integer> getList(Integer id) {
		List<Integer> list = new ArrayList<Integer>();
		String[] str = id.toString().split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(Integer.parseInt(str[i]));
		}
		return list;
	}

	// ����
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, TermBgnBal> termBgnBalMap = uploadScoreInfo(file);
		for (Map.Entry<String, TermBgnBal> entry : termBgnBalMap.entrySet()) {
			try {
				tbbd.insertTermBgnBalUpload(entry.getValue());
			} catch (DuplicateKeyException e) {
				isSuccess = false;
				message = "�ڳ�����в��������Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}
		}
		isSuccess = true;
		message = "�ڳ�����ɹ���";
		try {
			resp = BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// ����excle
	private Map<String, TermBgnBal> uploadScoreInfo(MultipartFile file) {
		Map<String, TermBgnBal> temp = new HashMap<>();
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
			// ��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if (r.getRowNum() < 1) {
					continue;
				}
//				String orderNo = GetCellData(r, "���");
				String whsEncd = GetCellData(r, "�ֿ����");
				String invtyEncd = GetCellData(r, "�������");
				String batNum = GetCellData(r, "����");
				String orderNo=whsEncd+invtyEncd+batNum;
				if (temp.get(orderNo)==null) {
					
				}
				// ����ʵ����
				TermBgnBal termBgnBal = new TermBgnBal();
				termBgnBal.setWhsEncd(whsEncd); // '�ֿ����',
				termBgnBal.setInvtyEncd(invtyEncd); // '�������',
				termBgnBal.setDeptId(GetCellData(r,"���ű���")); // '���ű���', 
				termBgnBal.setQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				termBgnBal.setBxQty(GetBigDecimal(GetCellData(r, "����"), 8)); // '����',
				termBgnBal.setCntnTaxUprc(GetBigDecimal(GetCellData(r,"��˰����"),8)); // '��˰����', 
				termBgnBal.setPrcTaxSum(GetBigDecimal(GetCellData(r,"��˰�ϼ�"),8)); // '��˰�ϼ�',
				termBgnBal.setNoTaxUprc(GetBigDecimal(GetCellData(r, "��˰����"), 8)); // '��˰����',
				termBgnBal.setNoTaxAmt(GetBigDecimal(GetCellData(r, "��˰���"), 8)); // '��˰���',
				termBgnBal.setTaxAmt(GetBigDecimal(GetCellData(r,"˰��"),8)); // '˰��', 
				termBgnBal.setTaxRate(GetBigDecimal(GetCellData(r,"˰��"),8)); // '˰��', 
				termBgnBal.setBatNum(batNum); // '����',
				termBgnBal.setIntlBat(GetCellData(r, "��������")); // '��������',
				if (GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) {
					termBgnBal.setPrdcDt(null);
				} else {
					termBgnBal.setPrdcDt(GetCellData(r, "��������").replaceAll("[^0-9:-]", " "));// ��������
				}
				if (GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) {
					termBgnBal.setInvldtnDt(null);
				} else {
					termBgnBal.setInvldtnDt(GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " "));// ʧЧ����
				}
				termBgnBal.setBaoZhiQi(GetCellData(r, "������")); // '������',
				termBgnBal.setIsNtBookEntry(new Double(GetCellData(r, "�Ƿ����")).intValue()); // '�Ƿ����', int(11
				termBgnBal.setBookEntryPers(GetCellData(r, "������")); // ������'
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					termBgnBal.setBookEntryTm(null);
				} else {
					termBgnBal.setBookEntryTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				termBgnBal.setSetupPers(GetCellData(r, "������")); // '������'
				if (GetCellData(r, "����ʱ��") == null || GetCellData(r, "����ʱ��").equals("")) {
					termBgnBal.setSetupTm(null);
				} else {
					termBgnBal.setSetupTm(GetCellData(r, "����ʱ��").replaceAll("[^0-9:-]", " "));// ����ʱ��
				}
				termBgnBal.setModiPers(GetCellData(r, "�޸���")); // '�޸���'
				if (GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) {
					termBgnBal.setModiTm(null);
				} else {
					termBgnBal.setModiTm(GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " "));// �޸�ʱ��
				}
				termBgnBal.setInvtySubjId(GetCellData(r, "�����Ŀ����"));// �����Ŀ����
				termBgnBal.setProjEncd(GetCellData(r, "��Ŀ����"));// ��Ŀ����
				temp.put(orderNo, termBgnBal);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�" + j + "�е����ʽ�����޷�����!" + e.getMessage());
		}
		return temp;
	}
}
