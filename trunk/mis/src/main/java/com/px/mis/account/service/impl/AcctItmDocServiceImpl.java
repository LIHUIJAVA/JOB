package com.px.mis.account.service.impl;

import com.px.mis.account.dao.AcctItmDocDao;
import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.service.AcctItmDocService;
import com.px.mis.account.utils.AcctltmDocTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class AcctItmDocServiceImpl extends poiTool  implements AcctItmDocService {
	@Autowired
	private AcctItmDocDao acctItmDocDao;

	@Override
	public String insertAcctItmDoc(AcctItmDoc acctItmDoc) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(acctItmDoc.getSubjId()==null) {
				isSuccess=false;
				message="新增失败,科目编码不能为空";
			}else if(acctItmDocDao.selectAcctItmDocBySubjId(acctItmDoc.getSubjId())==null) {
				if(acctItmDoc.getEncdLvlSub()==1 || (acctItmDoc.getEncdLvlSub()>1 && acctItmDoc.getpId()!=null)) {
					if(acctItmDocDao.selectAcctItmDocByPId(acctItmDoc.getpId())!=null){
						int insertResult = acctItmDocDao.insertAcctItmDoc(acctItmDoc);
						if(insertResult==1) {
							isSuccess=true;
							message="新增成功";
						}else {
							isSuccess=false;
							message="新增失败";
						}
					}else {
						isSuccess=false;
						message="父级科目编码不存在，无法新增！";
					}
				}else {
					isSuccess=false;
					message="新增数据有误，请检查再新增";
				}
			}else {
				isSuccess=false;
				message="新增失败，序号"+acctItmDoc.getSubjId()+"已存在！";
			}
			resp=BaseJson.returnRespObj("/account/acctItmDoc/insertAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resp;
	}

	@Override
	public String updateAcctItmDocById(List<AcctItmDoc> acctItmDocList) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			for(AcctItmDoc acctItmDoc:acctItmDocList) {
				if(acctItmDoc.getSubjId()==null) {
					isSuccess=false;
					message="更新失败,科目编码不能为空";
				}else if(acctItmDocDao.selectAcctItmDocBySubjId(acctItmDoc.getSubjId())==null) {
					isSuccess=false;
					message="更新失败，序号"+acctItmDoc.getSubjId()+"不存在！";
				}else {
					if(acctItmDoc.getEncdLvlSub()>1 && acctItmDoc.getpId()!=null) {
						if(acctItmDocDao.selectAcctItmDocByPId(acctItmDoc.getpId())!=null){
							int updateResult = acctItmDocDao.updateAcctItmDocBySubjId(acctItmDoc);
							if(updateResult==1) {
								isSuccess=true;
								message="更新成功";
							}else {
								isSuccess=false;
								message="更新失败";
							}
						}else {
							isSuccess=false;
							message="父级科目编码不存在，无法修改！";
						}
					}else {
						isSuccess=false;
						message="修改科目的编码级次为一级,无法修改！";
					}
				}
			}
			resp=BaseJson.returnRespObj("/account/acctItmDoc/updateAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resp;
	}

	@Override
	public String deleteAcctItmDocById(String subjId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			List<String> list = getList(subjId);
			Integer deleteResult = acctItmDocDao.deleteAcctItmDocBySubjId(list);
			if(deleteResult>=1) {
				isSuccess=true;
				message="删除成功";
			}else if(deleteResult==0){
				isSuccess=false;
				message="没有要删除的数据";		
			}
		resp=BaseJson.returnRespObj("/account/acctItmDoc/deleteAcctItmDoc", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;
	}
	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
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

	@Override
	public String selectAcctItmDocById(String subjId) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			AcctItmDoc acctItmDoc=acctItmDocDao.selectAcctItmDocBySubjId(subjId);
		if(acctItmDoc!=null) {
			message="查询成功！";
		}else {
			isSuccess=false;
			message="编码"+subjId+"不存在！";
		}
		
		resp=BaseJson.returnRespObj("/account/acctItmDoc/selectAcctItmDocById", isSuccess, message, acctItmDoc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryAcctItmDocList(Map map) {
		String resp="";
		try {
		List<Map> mapList=null;
		List<AcctItmDoc> poList =acctItmDocDao.selectAcctItmDocList(map);
		
		mapList = AcctltmDocTree.getTree(poList);
		
		int count = acctItmDocDao.selectAcctItmDocCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;
		resp=BaseJson.returnRespList("/account/acctItmDoc/queryAcctItmDocList", true, "查询成功！", count, pageNo, pageSize,
				listNum, pages, mapList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryAcctItmDocPrint(Map map) {
		String resp = "";
		try {
			List<Map> mapList = null;
			map.put("index",null);
			map.put("num",null);
			List<AcctItmDoc> poList = acctItmDocDao.selectAcctItmDocList(map);
//			mapList = Acctl/tmDocTree.getTree(poList);
			resp = BaseJson.returnRespList("/account/acctItmDoc/queryAcctItmDocPrint", true, "查询成功！", poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, AcctItmDoc> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());

		for (Map.Entry<String, AcctItmDoc> entry : pusOrderMap.entrySet()) {

			AcctItmDoc acctItmDoc = acctItmDocDao.selectAcctItmDocBySubjId(entry.getValue().getSubjId());

			if (acctItmDoc != null) {
				throw new RuntimeException("插入重复单号 " + entry.getValue().getSubjId() + " 请检查");
			}
			try {
				acctItmDocDao.insertAcctItmDoc(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");

			}
		}

		isSuccess = true;
		message = "会计档案新增成功！";
		try {
			resp = BaseJson.returnRespObj("account/acctItmDoc/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}
	// 导入excle
	private Map<String, AcctItmDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, AcctItmDoc> temp = new HashMap<>();
		int j = 1;
		try {
			InputStream fileIn = file.getInputStream();
			// 根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);

			// 获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			// 获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			// 获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			// 设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}

				String orderNo = GetCellData(r, "科目编码");
				// 创建实体类
				AcctItmDoc acctItmDoc = new AcctItmDoc();
				if (temp.containsKey(orderNo)) {
					acctItmDoc = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上

				acctItmDoc.setSubjId(orderNo); // 科目编码 String
				acctItmDoc.setSubjNm(GetCellData(r, "科目名称")); // 科目名称 String
				acctItmDoc.setSubjTyp(GetCellData(r, "科目类型")); // 科目类型 String
//				acctItmDoc.setSubjCharc(GetCellData(r, "科目性质")); // 科目性质 String
				acctItmDoc.setEncdLvlSub(
						(new Double(GetCellData(r, "级次") == null || GetCellData(r, "级次").equals("") ? "0"
								: GetCellData(r, "级次"))).intValue()); // 编码级次 Integer
				acctItmDoc.setSubjMnem(GetCellData(r, "科目助记码")); // 科目助记码 String
//				acctItmDoc.setIsNtIndvRecoAccti(
//						(new Double(GetCellData(r, "是否个人往来核算") == null || GetCellData(r, "是否个人往来核算").equals("") ? "0"
//								: GetCellData(r, "是否个人往来核算"))).intValue()); // 是否个人往来核算 Integer
//				acctItmDoc.setIsNtCustRecoAccti(
//						(new Double(GetCellData(r, "是否客户往来核算") == null || GetCellData(r, "是否客户往来核算").equals("") ? "0"
//								: GetCellData(r, "是否客户往来核算"))).intValue()); // 是否客户往来核算 Integer
//				acctItmDoc.setIsNtProvrRecoAccti(
//						(new Double(GetCellData(r, "是否供应商往来核算") == null || GetCellData(r, "是否供应商往来核算").equals("") ? "0"
//								: GetCellData(r, "是否供应商往来核算"))).intValue()); // 是否供应商往来核算 Integer
//				acctItmDoc.setIsNtDeptAccti(
//						(new Double(GetCellData(r, "是否部门核算") == null || GetCellData(r, "是否部门核算").equals("") ? "0"
//								: GetCellData(r, "是否部门核算"))).intValue()); // 是否部门核算 Integer
//				acctItmDoc.setIsNtEndLvl(
//						(new Double(GetCellData(r, "是否项目核算") == null || GetCellData(r, "是否项目核算").equals("") ? "0"
//								: GetCellData(r, "是否项目核算"))).intValue()); // 是否末级 Integer
				acctItmDoc.setIsNtCashSubj(
						(new Double(GetCellData(r, "是否现金科目") == null || GetCellData(r, "是否现金科目").equals("") ? "0"
								: GetCellData(r, "是否现金科目"))).intValue()); // 是否现金科目 Integer
				acctItmDoc.setIsNtBankSubj(
						(new Double(GetCellData(r, "是否银行科目") == null || GetCellData(r, "是否银行科目").equals("") ? "0"
								: GetCellData(r, "是否银行科目"))).intValue()); // 是否银行科目 Integer
//				acctItmDoc.setIsNtComnCashflowQtySubj((new Double(
//						GetCellData(r, "是否常用现金流量科目") == null || GetCellData(r, "是否常用现金流量科目").equals("") ? "0"
//								: GetCellData(r, "是否常用现金流量科目"))).intValue()); // 是否常用现金流量科目 Integer
				acctItmDoc.setIsNtBkat(
						(new Double(GetCellData(r, "是否银行账") == null || GetCellData(r, "是否银行账").equals("") ? "0"
								: GetCellData(r, "是否银行账"))).intValue()); // 是否银行账 Integer
				acctItmDoc.setIsNtDayBookEntry(
						(new Double(GetCellData(r, "是否日记账") == null || GetCellData(r, "是否日记账").equals("") ? "0"
								: GetCellData(r, "是否日记账"))).intValue()); // 是否日记账 Integer
				acctItmDoc.setMemo(GetCellData(r, "受控系统")); // 备注 String
				acctItmDoc.setpId(GetCellData(r, "父级编码")); // 父级Id String
				acctItmDoc.setBalDrct(
						(new Double(GetCellData(r, "余额方向") == null || GetCellData(r, "余额方向").equals("") ? "0"
								: GetCellData(r, "余额方向"))).intValue()); // 余额方向 Integer

				temp.put(orderNo, acctItmDoc);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
		}
		return temp;
	}

}
