package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.service.InvtyClsService;
import com.px.mis.purc.util.InvtyClsTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
/*������๦��*/
@Transactional
@Service
public class InvtyClsServiceImpl extends poiTool implements InvtyClsService {
	@Autowired
	private InvtyClsDao icd;

	@Override
	public ObjectNode insertInvtyCls(InvtyCls invtyCls) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(invtyCls.getLevel()<=3) {
			if(icd.selectInvtyClsByInvtyClsEncd(invtyCls.getInvtyClsEncd())!=null) {
				on.put("isSuccess", false);
				on.put("message", "���"+invtyCls.getInvtyClsEncd()+"�Ѵ��ڣ����������룡");
			}else {
				icd.insertInvtyCls(invtyCls);
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ֻ�����������ڣ�����");
		}
		return on;
	}

	@Override
	public ObjectNode updateInvtyClsByInvtyClsEncd(InvtyCls invtyCls) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a= icd.updateInvtyClsByInvtyClsEncd(invtyCls);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}

	@Override
	public ObjectNode deleteInvtyClsByInvtyClsEncd(String invtyClsEncd) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a= icd.deleteInvtyClsByInvtyClsEncd(invtyClsEncd);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}
	
	//�򵥲�  �������
	@Override
	public InvtyCls selectInvtyClsByInvtyClsEncd(String invtyClsEncd) {
		
		InvtyCls invtyCls = icd.selectInvtyClsByInvtyClsEncd(invtyClsEncd);
		return invtyCls;
		
	}

	@Override
	public List<Map> selectInvtyCls() {
		List<Map> mapList=null;
		List<InvtyCls> invtyList = icd.selectInvtyCls();
		try {
			mapList = InvtyClsTree.getTree(invtyList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapList;
	}

    //����
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,InvtyCls> InvtyClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,InvtyCls> entry : InvtyClsMap.entrySet()) {
			if(icd.selectInvtyClsByInvtyClsEncd(entry.getValue().getInvtyClsEncd())!=null) {
				isSuccess=false;
				message="��������в��ַ����Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				icd.insertInvtyCls(entry.getValue());
				isSuccess=true;
				message="������ർ��ɹ���";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/InvtyCls/uploadInvtyClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //����excle
	private Map<String, InvtyCls> uploadScoreInfo(MultipartFile  file){
		Map<String,InvtyCls> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"����������");
				//����ʵ����
				InvtyCls invtyCls=new InvtyCls();
				if(temp.containsKey(orderNo)) {
					invtyCls = temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				invtyCls.setInvtyClsEncd(orderNo);//����������
				invtyCls.setInvtyClsNm(GetCellData(r,"�����������"));//��������
				invtyCls.setIco(GetCellData(r,"��Ӧ������"));//��Ӧ������
				invtyCls.setLevel(new Double(GetCellData(r,"����")).intValue());//����
				invtyCls.setPid(GetCellData(r,"��������"));//�������
				invtyCls.setMemo(GetCellData(r,"��ע"));//��ע
				
				temp.put(orderNo,invtyCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}


}
