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

/*供应商分类功能实现*/
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
				on.put("message", "编号"+provrCls.getProvrClsId()+"已存在，请重新输入！");
			}else {
				pcd.insertProvrCls(provrCls);
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			}
		}else {
			on.put("isSuccess", false);
			on.put("message", "级别只能是三级以内！！！");
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}

	//查询所有供应商分类
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
	
	
    //导入
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,ProvrCls> ProvrClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,ProvrCls> entry : ProvrClsMap.entrySet()) {
			if(pcd.selectProvrClsByProvrClsId(entry.getValue().getProvrClsId())!=null) {
				isSuccess=false;
				message="供应商分类中部分分类已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				pcd.insertProvrCls(entry.getValue());
				isSuccess=true;
				message="供应商分类导入成功！";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/ProvrCls/uploadProvrClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //导入excle
	private Map<String,ProvrCls> uploadScoreInfo(MultipartFile  file){
		Map<String,ProvrCls> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"供应商分类编码");
				//创建实体类
				ProvrCls provrCls=new ProvrCls();
				if (temp.containsKey(orderNo)) {
					provrCls=temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				provrCls.setProvrClsId(orderNo);//分类编码
				provrCls.setProvrClsNm(GetCellData(r,"供应商分类名称"));//分类名称
				provrCls.setMemo(GetCellData(r,"图标"));//图标
				provrCls.setLevel(new Double(GetCellData(r,"级别")).intValue());//级别
				provrCls.setMemo(GetCellData(r,"备注"));//备注
				provrCls.setPid(GetCellData(r,"父级编码"));//父级编号
				
				temp.put(orderNo,provrCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}
	
	
	
	
	
}
