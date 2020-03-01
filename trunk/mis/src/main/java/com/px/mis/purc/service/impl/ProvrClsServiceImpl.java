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
import com.px.mis.purc.dao.ProvrClsDao;
import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.service.ProvrClsService;
import com.px.mis.purc.util.ProvrClsTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

/*��Ӧ�̷��๦��ʵ��*/
@Transactional
@Service
public class ProvrClsServiceImpl  extends poiTool implements ProvrClsService {
	@Autowired
	private ProvrClsDao pcd;

	@Override
	public ObjectNode insertProvrCls(ProvrCls provrCls) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(provrCls.getLevel()<=3) {
			if(pcd.selectProvrClsByProvrClsId(provrCls.getProvrClsId())!=null) {
				on.put("isSuccess", false);
				on.put("message", "���"+provrCls.getProvrClsId()+"�Ѵ��ڣ����������룡");
			}else {
				pcd.insertProvrCls(provrCls);
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
	public ObjectNode updateProvrClsByProvrClsId(ProvrCls provrCls) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int a= pcd.updateProvrClsByProvrClsId(provrCls);

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
	public ObjectNode deleteProvrClsByProvrClsId(String provrClsId) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int a= pcd.deleteProvrClsByProvrClsId(provrClsId);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}

	//��ѯ���й�Ӧ�̷���
	@Override
	public List<Map> selectProvrCls() {
		List<Map> mapList=null;
		List<ProvrCls> proList = pcd.selectProvrCls();
	
		try {
			mapList = ProvrClsTree.getTree(proList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapList;
	}

	@Override
	public ProvrCls selectProvrClsByProvrClsId(String provrClsId) {
		// TODO Auto-generated method stub
		ProvrCls provrCls = pcd.selectProvrClsByProvrClsId(provrClsId);
		return provrCls;
	}
	
	
    //����
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,ProvrCls> ProvrClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,ProvrCls> entry : ProvrClsMap.entrySet()) {
			if(pcd.selectProvrClsByProvrClsId(entry.getValue().getProvrClsId())!=null) {
				isSuccess=false;
				message="��Ӧ�̷����в��ַ����Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				pcd.insertProvrCls(entry.getValue());
				isSuccess=true;
				message="��Ӧ�̷��ർ��ɹ���";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/ProvrCls/uploadProvrClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //����excle
	private Map<String,ProvrCls> uploadScoreInfo(MultipartFile  file){
		Map<String,ProvrCls> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"��Ӧ�̷������");
				//����ʵ����
				ProvrCls provrCls=new ProvrCls();
				if (temp.containsKey(orderNo)) {
					provrCls=temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				provrCls.setProvrClsId(orderNo);//�������
				provrCls.setProvrClsNm(GetCellData(r,"��Ӧ�̷�������"));//��������
				provrCls.setMemo(GetCellData(r,"ͼ��"));//ͼ��
				provrCls.setLevel(new Double(GetCellData(r,"����")).intValue());//����
				provrCls.setMemo(GetCellData(r,"��ע"));//��ע
				provrCls.setPid(GetCellData(r,"��������"));//�������
				
				temp.put(orderNo,provrCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
	
	
	
}
