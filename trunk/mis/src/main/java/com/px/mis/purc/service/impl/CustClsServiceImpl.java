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

/*客户分类功能实现*/
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
					message="编号"+custCls.getClsId()+"已存在，请重新输入！";
				}else {
					ccd.insertCustCls(custCls);
					isSuccess=true;
					message="客户分类新增成功";
				}
			}else {
				isSuccess=false;
				message="级别只能是三级以内！！！";
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
				message="客户分类修改成功";
			}else {
				isSuccess=false;
				message="客户分类修改失败";
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
				message="客户分类删除成功";
			}else {
				isSuccess=false;
				message="客户分类删除失败";
			}
		resp=BaseJson.returnRespObj("purc/CustCls/deleteCustClsByClsId", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	//简单查  按照客户编号客户分类
	@Override
	public String selectCustClsByClsId(String clsId){
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			CustCls custCls = ccd.selectCustClsByClsId(clsId);
			if(custCls==null) {
				isSuccess=false;
				message="该客户分类不存在！";
			}
			resp=BaseJson.returnRespObj("purc/CustCls/selectCustClsByClsId", isSuccess, message, custCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//查询全部客户分类
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

    //导入
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,CustCls> CustClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,CustCls> entry : CustClsMap.entrySet()) {
			if(ccd.selectCustClsByClsId(entry.getValue().getClsId())!=null) {
				isSuccess=false;
				message="客户分类中部分分类已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				ccd.insertCustCls(entry.getValue());
				isSuccess=true;
				message="客户分类导入成功！";
			}
		}
		try {
		resp=BaseJson.returnRespObj("purc/CustCls/uploadCustClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //导入excle
	private Map<String,CustCls> uploadScoreInfo(MultipartFile  file){
		Map<String,CustCls> temp = new HashMap<>();
		int j=1;
		try {
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				String orderNo = GetCellData(r,"客户分类编码");//客户分类编号
				//创建实体类
				CustCls custCls=new CustCls();
				if(temp.containsKey(orderNo)) {
					custCls = temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				custCls.setClsId(orderNo);//客户编号
				custCls.setClsNm(GetCellData(r,"客户分类名称"));//客户名称
				custCls.setLevel(new Double(GetCellData(r,"级别")).intValue());//级别
				custCls.setPid(GetCellData(r,"父级编码"));//父级编号
				custCls.setIco(GetCellData(r,"图标"));//图标
				custCls.setMemo(GetCellData(r,"备注"));//备注
				temp.put(orderNo,custCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
}
