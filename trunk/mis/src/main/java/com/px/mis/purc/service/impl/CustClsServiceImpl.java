package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
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

import com.px.mis.purc.dao.CustClsDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.service.CustClsService;
import com.px.mis.purc.util.CustClsTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;

/*�ͻ����๦��ʵ��*/
@Transactional
@Service
public class CustClsServiceImpl extends poiTool implements CustClsService {
	@Autowired
	private CustClsDao ccd;

	@Override
	public String insertCustCls(CustCls custCls) {
	
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(custCls.getLevel()<=3) {
				if(ccd.selectCustClsByClsId(custCls.getClsId())!=null) {
					isSuccess=false;
					message="���"+custCls.getClsId()+"�Ѵ��ڣ����������룡";
				}else {
					ccd.insertCustCls(custCls);
					isSuccess=true;
					message="�ͻ����������ɹ�";
				}
			}else {
				isSuccess=false;
				message="����ֻ�����������ڣ�����";
			}
	    resp=BaseJson.returnRespObj("purc/CustCls/insertCustCls", isSuccess, message, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String updateCustClsByClsId(CustCls custCls) {
		
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			int a=ccd.updateCustClsByClsId(custCls);;
			if(a==1) {
				isSuccess=true;
				message="�ͻ������޸ĳɹ�";
			}else {
				isSuccess=false;
				message="�ͻ������޸�ʧ��";
			}
		resp=BaseJson.returnRespObj("purc/CustCls/updateCustClsByClsId", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteCustClsByClsId(String clsId) {
		
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			int a= ccd.deleteCustClsByClsId(clsId);

			if(a==1) {
				isSuccess=true;
				message="�ͻ�����ɾ���ɹ�";
			}else {
				isSuccess=false;
				message="�ͻ�����ɾ��ʧ��";
			}
		resp=BaseJson.returnRespObj("purc/CustCls/deleteCustClsByClsId", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//�򵥲�  ���տͻ���ſͻ�����
	@Override
	public String selectCustClsByClsId(String clsId){
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			CustCls custCls = ccd.selectCustClsByClsId(clsId);
			if(custCls==null) {
				isSuccess=false;
				message="�ÿͻ����಻���ڣ�";
			}
			resp=BaseJson.returnRespObj("purc/CustCls/selectCustClsByClsId", isSuccess, message, custCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//��ѯȫ���ͻ�����
	@Override
	public List<Map> selectCustCls() {

		List<Map> mapList=null;
		List<CustCls> custList = ccd.selectCustCls();
		try {
			mapList = CustClsTree.getTree(custList);
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
		Map<String,CustCls> CustClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,CustCls> entry : CustClsMap.entrySet()) {
			if(ccd.selectCustClsByClsId(entry.getValue().getClsId())!=null) {
				isSuccess=false;
				message="�ͻ������в��ַ����Ѵ��ڣ��޷����룬������ٵ��룡";
				throw new RuntimeException(message);
			}else {
				ccd.insertCustCls(entry.getValue());
				isSuccess=true;
				message="�ͻ����ർ��ɹ���";
			}
		}
		try {
		resp=BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //����excle
	private Map<String,CustCls> uploadScoreInfo(MultipartFile  file){
		Map<String,CustCls> temp = new HashMap<>();
		int j=1;
		try {
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
				String orderNo = GetCellData(r,"�ͻ��������");//�ͻ�������
				//����ʵ����
				CustCls custCls=new CustCls();
				if(temp.containsKey(orderNo)) {
					custCls = temp.get(orderNo);
				}
				//ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
				custCls.setClsId(orderNo);//�ͻ����
				custCls.setClsNm(GetCellData(r,"�ͻ���������"));//�ͻ�����
				custCls.setLevel(new Double(GetCellData(r,"����")).intValue());//����
				custCls.setPid(GetCellData(r,"��������"));//�������
				custCls.setIco(GetCellData(r,"ͼ��"));//ͼ��
				custCls.setMemo(GetCellData(r,"��ע"));//��ע
				temp.put(orderNo,custCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
}
