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
/*部门档案实现层*/
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
				message="编号"+deptDoc.getDeptId()+"已存在，请重新输入！";
			}else {
				int a= ddd.insertDeptDoc(deptDoc);
				isSuccess=true;
				message="部门档案新增成功";
			}
		    resp=BaseJson.returnRespObj("purc/DeptDoc/insertDeptDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//修改部门档案
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
			on.put("message", "修改部门档案成功！");
		}else {
			on.put("isSuccess", false);
			on.put("message", "修改部门档案失败");
		}
		return on;
	}
	
	//批量修改部门档案
	@Override
	public String updateDeptDocByDeptEncd(List<DeptDoc> deptDoc) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		for(DeptDoc deptDocs:deptDoc) {
			if(ddd.selectDeptDocByDeptEncd(deptDocs.getDeptId())!=null) {
				ddd.updateDeptDocByDeptEncd(deptDocs);
				isSuccess=true;
				message="部门档案修改成功！";
			}else {
				isSuccess=false;
				message="该部门档案不存在！";
			}
		}
		try {
		    resp=BaseJson.returnRespObj("purc/DeptDoc/updateDeptDocByDeptEncd", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//删除部门档案
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
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}	
			resp=BaseJson.returnRespObj("purc/DeptDoc/deleteDeptDocList", isSuccess, message, null);
		} catch (IOException e) {
		e.printStackTrace();
		}
		return resp;
	}
	/**
	 * id放入list
	 * 
	 * @param id
	 *            id(多个已逗号分隔)
	 * @return List集合
	*/
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
		    list.add(str[i]);
	    }
		return list;
   }
	
	//按照编号查询部门档案
	@Override
	public DeptDoc selectDeptDocByDeptEncd(String deptEncd){
		
		DeptDoc deptDoc = ddd.selectDeptDocByDeptEncd(deptEncd);
		return deptDoc;
	}

	//查询所有部门档案
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
			resp=BaseJson.returnRespList("purc/DeptDoc/selectDeptDocList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages,deptDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	//打印及输入输出查询全部部门档案
	@Override
	public String printingDeptDocList(Map map) {
		String resp="";
		List<DeptDoc> deptDocList = ddd.printingDeptDocList(map);
		try {
			resp=BaseJson.returnRespObjList("purc/DeptDoc/printingDeptDocList", true, "查询成功！", null,deptDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
	//导入
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,DeptDoc> DeptDocMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,DeptDoc> entry : DeptDocMap.entrySet()) {
			if(ddd.selectDeptId(entry.getValue().getDeptId())!=null) {
				isSuccess=false;
				message="部门档案中部分部门编码"+entry.getValue().getDeptId()+"已存在，无法导入！";
				throw new RuntimeException(message);
			}else {
				int a= ddd.insertDeptDoc(entry.getValue());
				isSuccess=true;
				message="部门档案导入成功！";
			}
			
		}
		try {
			resp=BaseJson.returnRespObj("purc/DeptDoc/uploadDeptDocFile", isSuccess, message, null);
		} catch (IOException e) {
			message="导入失败";
			throw new RuntimeException(message);
		}
		return resp;
	}
    //导入excle
	private Map<String,DeptDoc> uploadScoreInfo(MultipartFile  file){
		Map<String,DeptDoc> temp = new HashMap<>();
		int j=1;
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				String orderNo = GetCellData(r,"部门编码");
				if (orderNo==null) {
					throw new RuntimeException("部门编码不能为空,或底部有空行,请检查数据格式!");
				}
				//创建实体类
				DeptDoc deptDoc=new DeptDoc();
				if(temp.containsKey(orderNo)) {
					deptDoc = temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在DeptDoc实体的各个属性上
				deptDoc.setDeptId(orderNo);//部门编号
				deptDoc.setDeptName(GetCellData(r,"部门名称"));//部门名称
				deptDoc.setTel(GetCellData(r,"电话"));//电话
				deptDoc.setAddr(GetCellData(r,"地址"));//地址
				deptDoc.setMemo(GetCellData(r,"备注"));//备注
				temp.put(orderNo,deptDoc);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
	

	

}
