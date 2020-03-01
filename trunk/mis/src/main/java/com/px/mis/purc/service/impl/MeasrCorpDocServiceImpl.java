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
/*计量单位功能描述*/
@Transactional
@Service
public class MeasrCorpDocServiceImpl extends poiTool implements MeasrCorpDocService {
	@Autowired
	private MeasrCorpDocDao mcd;

	//新增计量单位档案
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
			on.put("message", "编号"+measrCorpDoc.getMeasrCorpId()+"已存在，请重新输入！");
		}else {
			mcd.insertMeasrCorpDoc(measrCorpDoc);
			on.put("isSuccess", true);
			on.put("message", "新增成功");
		}
		return on;
	}

	//单个修改计量单位档案
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}
	
	//批量修改计量单位档案
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
				message="计量单位档案修改成功！";
			}else {
				isSuccess=false;
				message="计量单位档案不存在！";
			}
		}
		try {
		    resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/updateMeasrCorpDocByMeasrCorpId", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	//单个删除计量单位
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
			on.put("message", "处理成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "处理失败");
		}
		return on;
	}
	
	//批量删除计量单位档案
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
			    message="删除成功！";
			}else {
				isSuccess=false;
				message="删除失败！";
			}	
			resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/deleteMeasrCorpDocList", isSuccess, message, null);
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
	
	//按照编号  查询计量单位档案
	@Override
	public MeasrCorpDoc selectMeasrCorpDocByMeasrCorpId(String measrCorpId){
		MeasrCorpDoc measrCorpDoc = mcd.selectMeasrCorpDocByMeasrCorpId(measrCorpId);
		return measrCorpDoc;
	}

	//分页查询所有计量单位
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
			resp=BaseJson.returnRespList("purc/MeasrCorpDoc/selectMeasrCorpDocList", true, "查询成功！", count,pageNo, pageSize,
					listNum, pages, measrCorpDocList);
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
		Map<String,MeasrCorpDoc> measrCorpDocMap = uploadScoreInfo(file);
		
		for(Map.Entry<String,MeasrCorpDoc> entry : measrCorpDocMap.entrySet()) {
			if(mcd.selectMeasrCorpDocByMeasrCorpId(entry.getValue().getMeasrCorpId())!=null) {
				isSuccess=false;
				message="计量单位档案中有部分计量单位已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}else {
				mcd.insertMeasrCorpDoc(entry.getValue());
				isSuccess=true;
				message="计量单位档案导入成功！";
			}
		}
		
		try {
		resp=BaseJson.returnRespObj("purc/MeasrCorpDoc/uploadMeasrCorpDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
    //导入excle
	private Map<String, MeasrCorpDoc> uploadScoreInfo(MultipartFile  file){
		Map<String,MeasrCorpDoc> temp = new HashMap<>();
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
				String orderNo = GetCellData(r,"计量单位编码");
				//创建实体类
				MeasrCorpDoc measrCorpDoc=new MeasrCorpDoc();
				if(temp.containsKey(orderNo)) {
					measrCorpDoc = temp.get(orderNo);
				}
				//取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				measrCorpDoc.setMeasrCorpId(orderNo);//计量单位编号
				measrCorpDoc.setMeasrCorpNm(GetCellData(r,"计量单位名称"));//收发类别名称
				measrCorpDoc.setMemo(GetCellData(r,"备注"));//备注
				
				temp.put(orderNo,measrCorpDoc);
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+ e.getMessage());
		}
	   return temp;    
	}

	@Override
	public String printingMeasrCorpDocList() {
		String resp="";
		List<MeasrCorpDoc> measrCorpDocList = mcd.printingMeasrCorpDocList();
		try {
			resp=BaseJson.returnRespList("purc/MeasrCorpDoc/printingMeasrCorpDocList", true, "查询成功！", measrCorpDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
}
