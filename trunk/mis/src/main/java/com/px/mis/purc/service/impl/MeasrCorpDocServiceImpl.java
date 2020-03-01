package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.px.mis.purc.dao.MeasrCorpDocDao;
import com.px.mis.purc.entity.MeasrCorpDoc;
import com.px.mis.purc.service.MeasrCorpDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
/*������λ��������*/
@Transactional
@Service
public class MeasrCorpDocServiceImpl extends poiTool implements MeasrCorpDocService {
	@Autowired
	private MeasrCorpDocDao mcd;

	//����������λ����
	@Override
	public ObjectNode insertMeasrCorpDoc(MeasrCorpDoc measrCorpDoc) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mcd.selectMeasrCorpDocByMeasrCorpId(measrCorpDoc.getMeasrCorpId())!=null) {
			on.put("isSuccess", false);
			on.put("message", "���"+measrCorpDoc.getMeasrCorpId()+"�Ѵ��ڣ����������룡");
		}else {
			mcd.insertMeasrCorpDoc(measrCorpDoc);
			on.put("isSuccess", true);
			on.put("message", "�����ɹ�");
		}
		return on;
	}

	//�����޸ļ�����λ����
	@Override
	public ObjectNode updateMeasrCorpDocByMeasrCorpId(MeasrCorpDoc measrCorpDoc) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a= mcd.updateMeasrCorpDocByMeasrCorpId(measrCorpDoc);

		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}
	
	//�����޸ļ�����λ����
	@Override
	public String updateMeasrCorpDocByMeasrCorpId(List<MeasrCorpDoc> measrCorpDoc) {
		
		String message="";
		Boolean isSuccess=true;
		String resp="";
		List<MeasrCorpDoc> meaCorDoc =new ArrayList();
		for(MeasrCorpDoc meaCoDoc:measrCorpDoc) {
			if(mcd.selectMeasrCorpDocByMeasrCorpId(meaCoDoc.getMeasrCorpId())!=null) {
				mcd.updateMeasrCorpDocByMeasrCorpId(meaCoDoc);
				isSuccess=true;
				message="������λ�����޸ĳɹ���";
			}else {
				isSuccess=false;
				message="������λ���������ڣ�";
			}
		}
		try {
		    resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/updateMeasrCorpDocByMeasrCorpId", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//����ɾ��������λ
	@Override
	public ObjectNode deleteMeasrCorpDocByMeasrCorpId(String measrCorpId) {
		
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int a=mcd.deleteMeasrCorpDocByMeasrCorpId(measrCorpId);


		if(a==1) {
			on.put("isSuccess", true);
			on.put("message", "����ɹ�");
		}else {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��");
		}
		return on;
	}
	
	//����ɾ��������λ����
	@Override
	public String deleteMeasrCorpDocList(String measrCorpId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> list = getList(measrCorpId);
			int a = mcd.deleteMeasrCorpDocList(list);
			if(a>=1) {
			    isSuccess=true;
			    message="ɾ���ɹ���";
			}else {
				isSuccess=false;
				message="ɾ��ʧ�ܣ�";
			}	
			resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/deleteMeasrCorpDocList", isSuccess, message, null);
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
	
	//���ձ��  ��ѯ������λ����
	@Override
	public MeasrCorpDoc selectMeasrCorpDocByMeasrCorpId(String measrCorpId){
		MeasrCorpDoc measrCorpDoc = mcd.selectMeasrCorpDocByMeasrCorpId(measrCorpId);
		return measrCorpDoc;
	}

	//��ҳ��ѯ���м�����λ
	@Override
	public String selectMeasrCorpDocList(Map map) {
		String resp="";
		List<MeasrCorpDoc> measrCorpDocList = mcd.selectMeasrCorpDocList(map);
		int count = mcd.selectMeasrCorpDocCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = measrCorpDocList.size();
		int pages = count / pageSize + 1;
		
		try {
			resp=BaseJson.returnRespList("purc/MeasrCorpDoc/selectMeasrCorpDocList", true, "��ѯ�ɹ���", count,pageNo, pageSize,
					listNum, pages, measrCorpDocList);
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
		Map<String,MeasrCorpDoc> measrCorpDocMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,MeasrCorpDoc> entry : measrCorpDocMap.entrySet()) {
			if(mcd.selectMeasrCorpDocByMeasrCorpId(entry.getValue().getMeasrCorpId())!=null) {
				isSuccess=false;
				message="������λ�������в��ּ�����λ�Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				mcd.insertMeasrCorpDoc(entry.getValue());
				isSuccess=true;
				message="������λ��������ɹ���";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/uploadMeasrCorpDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //����excle
	private Map<String, MeasrCorpDoc> uploadScoreInfo(MultipartFile  file){
		Map<String,MeasrCorpDoc> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"������λ����");
				//����ʵ����
				MeasrCorpDoc measrCorpDoc=new MeasrCorpDoc();
				if(temp.containsKey(orderNo)) {
					measrCorpDoc = temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				measrCorpDoc.setMeasrCorpId(orderNo);//������λ���
				measrCorpDoc.setMeasrCorpNm(GetCellData(r,"������λ����"));//�շ��������
				measrCorpDoc.setMemo(GetCellData(r,"��ע"));//��ע
				
				temp.put(orderNo,measrCorpDoc);
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}

	@Override
	public String printingMeasrCorpDocList() {
		String resp="";
		List<MeasrCorpDoc> measrCorpDocList = mcd.printingMeasrCorpDocList();
		try {
			resp=BaseJson.returnRespList("purc/MeasrCorpDoc/printingMeasrCorpDocList", true, "��ѯ�ɹ���", measrCorpDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
}
