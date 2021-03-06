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
/*收发类别功能*/
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
			on.put("message", "编号"+recvSendCate.getRecvSendCateId()+"已存在，请重新输入！");
		}else {
			rscd.insertRecvSendCate(recvSendCate);
			on.put("isSuccess", true);
			on.put("message", "收发类别新增成功");
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}
	
	//按照编号查询收发类别
	@Override
	public RecvSendCate selectRecvSendCateByRecvSendCateId(String recvSendCateId){
		
		RecvSendCate recvSendCate = rscd.selectRecvSendCateByRecvSendCateId(recvSendCateId);
		return recvSendCate;
	}

	//查询所有收发类别
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
	
    //导入
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,RecvSendCate> recvSendCateMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,RecvSendCate> entry : recvSendCateMap.entrySet()) {
			if(rscd.selectRecvSendCateByRecvSendCateId(entry.getValue().getRecvSendCateId())!=null) {
				isSuccess=false;
				message="收发类别档案中有收发类别已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				rscd.insertRecvSendCate(entry.getValue());
				isSuccess=true;
				message="收发类别档案导入成功！";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //导入excle
	private Map<String, RecvSendCate> uploadScoreInfo(MultipartFile  file){
		Map<String,RecvSendCate> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"收发类别编码");//收发类别编号
				//创建实体类
				RecvSendCate recvSendCate=new RecvSendCate();
				if(temp.containsKey(orderNo)) {
					recvSendCate = temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				recvSendCate.setRecvSendCateId(orderNo);//收发类别编码
				recvSendCate.setRecvSendCateNm(GetCellData(r,"收发类别名称"));//收发类别名称
				recvSendCate.setRecvSendInd(new Double(GetCellData(r,"收发类别标识")).intValue());//收发类别标识
				recvSendCate.setIco(GetCellData(r,"图标"));//图标
				recvSendCate.setLevel(new Double(GetCellData(r,"级别")).intValue());//级别
				recvSendCate.setPid(GetCellData(r,"父级编码"));//父类编号
				recvSendCate.setMemo(GetCellData(r,"备注"));//备注
				
				temp.put(orderNo,recvSendCate);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}
	
}
