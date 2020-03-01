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
import com.px.mis.purc.dao.RecvSendCateDao;
import com.px.mis.purc.entity.RecvSendCate;
import com.px.mis.purc.service.RecvSendCateService;
import com.px.mis.purc.util.RecvSendCateTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
/*�շ������*/
@Transactional
@Service
public class RecvSendCateServiceImpl extends poiTool implements RecvSendCateService {
	@Autowired
	private RecvSendCateDao rscd;

	@Override
	public ObjectNode insertRecvSendCate(RecvSendCate recvSendCate) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(rscd.selectRecvSendCateByRecvSendCateId(recvSendCate.getRecvSendCateId())!=null) {
			on.put("isSuccess", false);
			on.put("message", "���"+recvSendCate.getRecvSendCateId()+"�Ѵ��ڣ����������룡");
		}else {
			rscd.insertRecvSendCate(recvSendCate);
			on.put("isSuccess", true);
			on.put("message", "�շ���������ɹ�");
		}
		return on;
	}

	@Override
	public ObjectNode updateRecvSendCateByRecvSendCateId(RecvSendCate recvSendCate) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("{\"isSuccess\":\"\",\"reason\":\"\"}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a= rscd.updateRecvSendCateByRecvSendCateId(recvSendCate);

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
	public ObjectNode deleteRecvSendCateByRecvSendCateId(String recvSendCateId) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a=rscd.deleteRecvSendCateByRecvSendCateId(recvSendCateId);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}
	
	//���ձ�Ų�ѯ�շ����
	@Override
	public RecvSendCate selectRecvSendCateByRecvSendCateId(String recvSendCateId){
		
		RecvSendCate recvSendCate = rscd.selectRecvSendCateByRecvSendCateId(recvSendCateId);
		return recvSendCate;
	}

	//��ѯ�����շ����
	@Override
	public List<Map> selectRecvSendCate() {
		List<Map> mapList=null;
		List<RecvSendCate> recvList = rscd.selectRecvSendCate();
		try {
			mapList = RecvSendCateTree.getTree(recvList);
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
		Map<String,RecvSendCate> recvSendCateMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,RecvSendCate> entry : recvSendCateMap.entrySet()) {
			if(rscd.selectRecvSendCateByRecvSendCateId(entry.getValue().getRecvSendCateId())!=null) {
				isSuccess=false;
				message="�շ���𵵰������շ�����Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				rscd.insertRecvSendCate(entry.getValue());
				isSuccess=true;
				message="�շ���𵵰�����ɹ���";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //����excle
	private Map<String, RecvSendCate> uploadScoreInfo(MultipartFile  file){
		Map<String,RecvSendCate> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"�շ�������");//�շ������
				//����ʵ����
				RecvSendCate recvSendCate=new RecvSendCate();
				if(temp.containsKey(orderNo)) {
					recvSendCate = temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				recvSendCate.setRecvSendCateId(orderNo);//�շ�������
				recvSendCate.setRecvSendCateNm(GetCellData(r,"�շ��������"));//�շ��������
				recvSendCate.setRecvSendInd(new Double(GetCellData(r,"�շ�����ʶ")).intValue());//�շ�����ʶ
				recvSendCate.setIco(GetCellData(r,"ͼ��"));//ͼ��
				recvSendCate.setLevel(new Double(GetCellData(r,"����")).intValue());//����
				recvSendCate.setPid(GetCellData(r,"��������"));//������
				recvSendCate.setMemo(GetCellData(r,"��ע"));//��ע
				
				temp.put(orderNo,recvSendCate);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}
	
}
