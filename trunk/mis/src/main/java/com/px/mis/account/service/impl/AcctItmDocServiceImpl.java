package com.px.mis.account.service.impl;

import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.service.AcctItmDocService;
import com.px.mis.account.utils.AcctltmDocTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class AcctItmDocServiceImpl extends poiTool  implements AcctItmDocService {
	@Autowired
	private AcctItmDocDao acctItmDocDao;

	@Override
	public String insertAcctItmDoc(AcctItmDoc acctItmDoc) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(acctItmDoc.getSubjId()==null) {
				isSuccess=false;
				message="����ʧ��,��Ŀ���벻��Ϊ��";
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(acctItmDoc.getSubjId())==null) {
				if(acctItmDoc.getEncdLvlSub()==1 || (acctItmDoc.getEncdLvlSub()>1 && acctItmDoc.getpId()!=null)) {
					if(acctItmDocDao.selectAcctItmDocByPId(acctItmDoc.getpId())!=null){
						int insertResult = acctItmDocDao.insertAcctItmDoc(acctItmDoc);
						if(insertResult==1) {
							isSuccess=true;
							message="�����ɹ�";
						}else {
							isSuccess=false;
							message="����ʧ��";
						}
					}else {
						isSuccess=false;
						message="������Ŀ���벻���ڣ��޷�������";
					}
				}else {
					isSuccess=false;
					message="����������������������";
				}
			}else {
				isSuccess=false;
				message="����ʧ�ܣ����"+acctItmDoc.getSubjId()+"�Ѵ��ڣ�";
			}
			resp=BaseJson.returnRespObj("/account/acctItmDoc/insertAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resp;
	}

	@Override
	public String updateAcctItmDocById(List<AcctItmDoc> acctItmDocList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			for(AcctItmDoc acctItmDoc:acctItmDocList) {
				if(acctItmDoc.getSubjId()==null) {
					isSuccess=false;
					message="����ʧ��,��Ŀ���벻��Ϊ��";
				}else if(acctItmDocDao.selectAcctItmDocBySubjId(acctItmDoc.getSubjId())==null) {
					isSuccess=false;
					message="����ʧ�ܣ����"+acctItmDoc.getSubjId()+"�����ڣ�";
				}else {
					if(acctItmDoc.getEncdLvlSub()>1 && acctItmDoc.getpId()!=null) {
						if(acctItmDocDao.selectAcctItmDocByPId(acctItmDoc.getpId())!=null){
							int updateResult = acctItmDocDao.updateAcctItmDocBySubjId(acctItmDoc);
							if(updateResult==1) {
								isSuccess=true;
								message="���³ɹ�";
							}else {
								isSuccess=false;
								message="����ʧ��";
							}
						}else {
							isSuccess=false;
							message="������Ŀ���벻���ڣ��޷��޸ģ�";
						}
					}else {
						isSuccess=false;
						message="�޸Ŀ�Ŀ�ı��뼶��Ϊһ��,�޷��޸ģ�";
					}
				}
			}
			resp=BaseJson.returnRespObj("/account/acctItmDoc/updateAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resp;
	}

	@Override
	public String deleteAcctItmDocById(String subjId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> list = getList(subjId);
			Integer deleteResult = acctItmDocDao.deleteAcctItmDocBySubjId(list);
			if(deleteResult>=1) {
				isSuccess=true;
				message="ɾ���ɹ�";
			}else if(deleteResult==0){
				isSuccess=false;
				message="û��Ҫɾ��������";		
			}
		resp=BaseJson.returnRespObj("/account/acctItmDoc/deleteAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
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
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	@Override
	public String selectAcctItmDocById(String subjId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			AcctItmDoc acctItmDoc=acctItmDocDao.selectAcctItmDocBySubjId(subjId);
		if(acctItmDoc!=null) {
			message="��ѯ�ɹ���";
		}else {
			isSuccess=false;
			message="����"+subjId+"�����ڣ�";
		}
		
		resp=BaseJson.returnRespObj("/account/acctItmDoc/selectAcctItmDocById", isSuccess, message, acctItmDoc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryAcctItmDocList(Map map) {
		String resp="";
		try {
		List<Map> mapList=null;
		List<AcctItmDoc> poList =acctItmDocDao.selectAcctItmDocList(map);
		
		mapList = AcctltmDocTree.getTree(poList);
		
		int count = acctItmDocDao.selectAcctItmDocCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		resp=BaseJson.returnRespList("/account/acctItmDoc/queryAcctItmDocList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
				listNum, pages, mapList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryAcctItmDocPrint(Map map) {
		String resp = "";
		try {
			List<Map> mapList = null;
			map.put("index",null);
			map.put("num",null);
			List<AcctItmDoc> poList = acctItmDocDao.selectAcctItmDocList(map);
//			mapList = Acctl/tmDocTree.getTree(poList);
			resp = BaseJson.returnRespList("/account/acctItmDoc/queryAcctItmDocPrint", true, "��ѯ�ɹ���", poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, AcctItmDoc> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());

		for (Map.Entry<String, AcctItmDoc> entry : pusOrderMap.entrySet()) {

			AcctItmDoc acctItmDoc = acctItmDocDao.selectAcctItmDocBySubjId(entry.getValue().getSubjId());

			if (acctItmDoc != null) {
				throw new RuntimeException("�����ظ����� " + entry.getValue().getSubjId() + " ����");
			}
			try {
				acctItmDocDao.insertAcctItmDoc(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("����sql����");

			}
		}

		isSuccess = true;
		message = "��Ƶ��������ɹ���";
		try {
			resp = BaseJson.returnRespObj("account/acctItmDoc/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}
	// ����excle
	private Map<String, AcctItmDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, AcctItmDoc> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "��Ŀ����");
				// ����ʵ����
				AcctItmDoc acctItmDoc = new AcctItmDoc();
				if (temp.containsKey(orderNo)) {
					acctItmDoc = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������

				acctItmDoc.setSubjId(orderNo); // ��Ŀ���� String
				acctItmDoc.setSubjNm(GetCellData(r, "��Ŀ����")); // ��Ŀ���� String
				acctItmDoc.setSubjTyp(GetCellData(r, "��Ŀ����")); // ��Ŀ���� String
//				acctItmDoc.setSubjCharc(GetCellData(r, "��Ŀ����")); // ��Ŀ���� String
				acctItmDoc.setEncdLvlSub(
						(new Double(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
								: GetCellData(r, "����"))).intValue()); // ���뼶�� Integer
				acctItmDoc.setSubjMnem(GetCellData(r, "��Ŀ������")); // ��Ŀ������ String
//				acctItmDoc.setIsNtIndvRecoAccti(
//						(new Double(GetCellData(r, "�Ƿ������������") == null || GetCellData(r, "�Ƿ������������").equals("") ? "0"
//								: GetCellData(r, "�Ƿ������������"))).intValue()); // �Ƿ������������ Integer
//				acctItmDoc.setIsNtCustRecoAccti(
//						(new Double(GetCellData(r, "�Ƿ�ͻ���������") == null || GetCellData(r, "�Ƿ�ͻ���������").equals("") ? "0"
//								: GetCellData(r, "�Ƿ�ͻ���������"))).intValue()); // �Ƿ�ͻ��������� Integer
//				acctItmDoc.setIsNtProvrRecoAccti(
//						(new Double(GetCellData(r, "�Ƿ�Ӧ����������") == null || GetCellData(r, "�Ƿ�Ӧ����������").equals("") ? "0"
//								: GetCellData(r, "�Ƿ�Ӧ����������"))).intValue()); // �Ƿ�Ӧ���������� Integer
//				acctItmDoc.setIsNtDeptAccti(
//						(new Double(GetCellData(r, "�Ƿ��ź���") == null || GetCellData(r, "�Ƿ��ź���").equals("") ? "0"
//								: GetCellData(r, "�Ƿ��ź���"))).intValue()); // �Ƿ��ź��� Integer
//				acctItmDoc.setIsNtEndLvl(
//						(new Double(GetCellData(r, "�Ƿ���Ŀ����") == null || GetCellData(r, "�Ƿ���Ŀ����").equals("") ? "0"
//								: GetCellData(r, "�Ƿ���Ŀ����"))).intValue()); // �Ƿ�ĩ�� Integer
				acctItmDoc.setIsNtCashSubj(
						(new Double(GetCellData(r, "�Ƿ��ֽ��Ŀ") == null || GetCellData(r, "�Ƿ��ֽ��Ŀ").equals("") ? "0"
								: GetCellData(r, "�Ƿ��ֽ��Ŀ"))).intValue()); // �Ƿ��ֽ��Ŀ Integer
				acctItmDoc.setIsNtBankSubj(
						(new Double(GetCellData(r, "�Ƿ����п�Ŀ") == null || GetCellData(r, "�Ƿ����п�Ŀ").equals("") ? "0"
								: GetCellData(r, "�Ƿ����п�Ŀ"))).intValue()); // �Ƿ����п�Ŀ Integer
//				acctItmDoc.setIsNtComnCashflowQtySubj((new Double(
//						GetCellData(r, "�Ƿ����ֽ�������Ŀ") == null || GetCellData(r, "�Ƿ����ֽ�������Ŀ").equals("") ? "0"
//								: GetCellData(r, "�Ƿ����ֽ�������Ŀ"))).intValue()); // �Ƿ����ֽ�������Ŀ Integer
				acctItmDoc.setIsNtBkat(
						(new Double(GetCellData(r, "�Ƿ�������") == null || GetCellData(r, "�Ƿ�������").equals("") ? "0"
								: GetCellData(r, "�Ƿ�������"))).intValue()); // �Ƿ������� Integer
				acctItmDoc.setIsNtDayBookEntry(
						(new Double(GetCellData(r, "�Ƿ��ռ���") == null || GetCellData(r, "�Ƿ��ռ���").equals("") ? "0"
								: GetCellData(r, "�Ƿ��ռ���"))).intValue()); // �Ƿ��ռ��� Integer
				acctItmDoc.setMemo(GetCellData(r, "�ܿ�ϵͳ")); // ��ע String
				acctItmDoc.setpId(GetCellData(r, "��������")); // ����Id String
				acctItmDoc.setBalDrct(
						(new Double(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
								: GetCellData(r, "����"))).intValue()); // ���� Integer

				temp.put(orderNo, acctItmDoc);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("������ʽ�����" + j + "��"+e.getMessage());
		}
		return temp;
	}

}
