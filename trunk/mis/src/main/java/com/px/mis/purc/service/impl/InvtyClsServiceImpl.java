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
/*存货分类功能*/
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
				on.put("message", "编号"+invtyCls.getInvtyClsEncd()+"已存在，请重新输入！");
			}else {
				icd.insertInvtyCls(invtyCls);
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}
	
	//简单查  存货分类
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

    //导入
	public String uploadFileAddDb(MultipartFile  file) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		Map<String,InvtyCls> InvtyClsMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,InvtyCls> entry : InvtyClsMap.entrySet()) {
			if(icd.selectInvtyClsByInvtyClsEncd(entry.getValue().getInvtyClsEncd())!=null) {
				isSuccess=false;
				message="存货分类中部分分类已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				icd.insertInvtyCls(entry.getValue());
				isSuccess=true;
				message="存货分类导入成功！";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/InvtyCls/uploadInvtyClsFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //导入excle
	private Map<String, InvtyCls> uploadScoreInfo(MultipartFile  file){
		Map<String,InvtyCls> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"存货分类编码");
				//创建实体类
				InvtyCls invtyCls=new InvtyCls();
				if(temp.containsKey(orderNo)) {
					invtyCls = temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				invtyCls.setInvtyClsEncd(orderNo);//存货分类编码
				invtyCls.setInvtyClsNm(GetCellData(r,"存货分类名称"));//分类名称
				invtyCls.setIco(GetCellData(r,"对应条形码"));//对应条形码
				invtyCls.setLevel(new Double(GetCellData(r,"级别")).intValue());//级别
				invtyCls.setPid(GetCellData(r,"父级编码"));//父级编号
				invtyCls.setMemo(GetCellData(r,"备注"));//备注
				
				temp.put(orderNo,invtyCls);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}


}
