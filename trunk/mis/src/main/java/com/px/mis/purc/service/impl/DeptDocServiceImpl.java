package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.entity.DeptDoc;
import com.px.mis.purc.service.DeptDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
/*���ŵ���ʵ�ֲ�*/
@Transactional
@Service
public class DeptDocServiceImpl extends poiTool implements DeptDocService {
	@Autowired
	private DeptDocDao ddd;

	@Override
	public String insertDeptDoc(DeptDoc deptDoc) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(ddd.selectDeptId(deptDoc.getDeptId()) !=null) {
				isSuccess=false;
				message="���"+deptDoc.getDeptId()+"�Ѵ��ڣ����������룡";
			}else {
				int a= ddd.insertDeptDoc(deptDoc);
				isSuccess=true;
				message="���ŵ��������ɹ�";
			}
		    resp=BaseJson.returnRespObj("purc/DeptDoc/insertDeptDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//�޸Ĳ��ŵ���
	@Override
	public ObjectNode updateDeptDocByDeptEncd(DeptDoc deptDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a= ddd.updateDeptDocByDeptEncd(deptDoc);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "�޸Ĳ��ŵ����ɹ���");
		}else {
			on.put("isSuccess", false);
			on.put("message", "�޸Ĳ��ŵ���ʧ��");
		}
		return on;
	}
	
	//�����޸Ĳ��ŵ���
	@Override
	public String updateDeptDocByDeptEncd(List<DeptDoc> deptDoc) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(DeptDoc deptDocs:deptDoc) {
			if(ddd.selectDeptDocByDeptEncd(deptDocs.getDeptId())!=null) {
				ddd.updateDeptDocByDeptEncd(deptDocs);
				isSuccess=true;
				message="���ŵ����޸ĳɹ���";
			}else {
				isSuccess=false;
				message="�ò��ŵ��������ڣ�";
			}
		}
		try {
		    resp=BaseJson.returnRespObj("purc/DeptDoc/updateDeptDocByDeptEncd", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//ɾ�����ŵ���
	@Override
	public String deleteDeptDocList(String deptId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> list = getList(deptId);
			int a = ddd.deleteDeptDocList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}	
			resp=BaseJson.returnRespObj("purc/DeptDoc/deleteDeptDocList", isSuccess, message, null);
		} catch (IOException e) {
		e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id����list
	 * 
	 * @param id
	 *            id(����Ѷ��ŷָ�)
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
	
	//���ձ�Ų�ѯ���ŵ���
	@Override
	public DeptDoc selectDeptDocByDeptEncd(String deptEncd){
		
		DeptDoc deptDoc = ddd.selectDeptDocByDeptEncd(deptEncd);
		return deptDoc;
	}

	//��ѯ���в��ŵ���
	@Override
	public String selectDeptDocList(Map map) {
		String resp="";
		List<DeptDoc> deptDocList = ddd.selectDeptDocList(map);
		int count = ddd.selectDeptDocCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = deptDocList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/DeptDoc/selectDeptDocList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
					listNum, pages,deptDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//��ӡ�����������ѯȫ�����ŵ���
	@Override
	public String printingDeptDocList(Map map) {
		String resp="";
		List<DeptDoc> deptDocList = ddd.printingDeptDocList(map);
		try {
			resp=BaseJson.returnRespObjList("purc/DeptDoc/printingDeptDocList", true, "��ѯ�ɹ���", null,deptDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
	//����
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,DeptDoc> DeptDocMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,DeptDoc> entry : DeptDocMap.entrySet()) {
			if(ddd.selectDeptId(entry.getValue().getDeptId())!=null) {
				isSuccess=false;
				message="���ŵ����в��ֲ��ű���"+entry.getValue().getDeptId()+"�Ѵ��ڣ��޷����룡";
				throw new RuntimeException(message);
			}else {
				int a= ddd.insertDeptDoc(entry.getValue());
				isSuccess=true;
				message="���ŵ�������ɹ���";
			}
			
		}
		try {
			resp=BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", isSuccess, message, null);
		} catch (IOException e) {
			message="����ʧ��";
			throw new RuntimeException(message);
		}
		return resp;
	}
    //����excle
	private Map<String,DeptDoc> uploadScoreInfo(MultipartFile  file){
		Map<String,DeptDoc> temp = new HashMap<>();
		int j=1;
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				String orderNo = GetCellData(r,"���ű���");
				if (orderNo==null) {
					throw new RuntimeException("���ű��벻��Ϊ��,��ײ��п���,�������ݸ�ʽ!");
				}
				//����ʵ����
				DeptDoc deptDoc=new DeptDoc();
				if(temp.containsKey(orderNo)) {
					deptDoc = temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��DeptDocʵ��ĸ���������
				deptDoc.setDeptId(orderNo);//���ű��
				deptDoc.setDeptName(GetCellData(r,"��������"));//��������
				deptDoc.setTel(GetCellData(r,"�绰"));//�绰
				deptDoc.setAddr(GetCellData(r,"��ַ"));//��ַ
				deptDoc.setMemo(GetCellData(r,"��ע"));//��ע
				temp.put(orderNo,deptDoc);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
	

	

}
