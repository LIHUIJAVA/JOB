package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.BankDocDao;
import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.service.BankDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
@Service
@Transactional
public class BankDocServiceImpl  extends poiTool  implements BankDocService {
	@Autowired
	private BankDocDao bankDocDao;
	@Override
	public ObjectNode insertBankDoc(BankDoc bankDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(bankDocDao.selectBankDocByOrdrNum(bankDoc.getBankEncd())==null) {
			int insertResult = bankDocDao.insertBankDoc(bankDoc);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
			}
		}else {
			on.put("isSuccess", false);
			on.put("message", "新增失败，编码"+bankDoc.getBankEncd()+"已存在！");
		}
		
		return on;	
	}

	@Override
	public ObjectNode updateBankDocByordrNum(List<BankDoc> bankDocList) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		for(BankDoc bankDoc: bankDocList) {
			String bankEncd = bankDoc.getBankEncd();
			if(bankEncd == null) {
				on.put("isSuccess", false);
				on.put("message", "修改失败，主键不能为null！");
			}else if (bankDocDao.selectBankDocByOrdrNum(bankEncd)==null) {
				on.put("isSuccess", false);
				on.put("message", "修改失败，序号"+bankEncd+"不存在！");
			}else {
				int updateResult = bankDocDao.updateBankDocByOrdrNum(bankDoc);
				if(updateResult==1) {
					on.put("isSuccess", true);
					on.put("message", "修改成功");
				}else {
					on.put("isSuccess", false);
					on.put("message", "修改失败");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteBankDocByOrdrNum(String bankEncd) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		List<String> lists = getList(bankEncd);
		Integer deleteResult = bankDocDao.deleteBankDocByOrdrNum(lists);
		if(deleteResult>=1) {
			on.put("isSuccess", true);
			on.put("message", "删除成功");
		}else if(deleteResult==0){
			on.put("isSuccess", true);
			on.put("message", "没有要删除的数据");		
		}else {
			on.put("isSuccess", false);
			on.put("message", "删除失败");
		}
		
		return on;
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
	@Transactional(propagation = Propagation.SUPPORTS)
	public BankDoc selectBankDocByordrNum(String id) {
		BankDoc bankDoc = bankDocDao.selectBankDocByOrdrNum(id);
		return bankDoc;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectBankDocList(Map map) {
		String resp="";
		List<BankDoc> list = bankDocDao.selectBankDocList(map);
		int count = bankDocDao.selectBankDocCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/bankDoc/selectBankDoc", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectBankDocPrint(Map map) {

		String resp = "";
		List<BankDoc> list = bankDocDao.selectBankDocList(map);

		try {
			resp = BaseJson.returnRespList("/account/bankDoc/selectBankDocPrint", true, "查询成功！", list);
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
		Map<String, BankDoc> pusOrderMap = uploadScoreInfo(file);
		System.out.println(pusOrderMap.size());
		for (Map.Entry<String, BankDoc> entry : pusOrderMap.entrySet()) {

			BankDoc acctItmDoc = bankDocDao.selectBankDocByOrdrNum(entry.getValue().getBankEncd());

			if (acctItmDoc != null) {
				throw new RuntimeException("插入重复单号 " + entry.getValue().getBankEncd() + " 请检查");
			}
			try {
				bankDocDao.insertBankDoc(entry.getValue());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				throw new RuntimeException("插入sql问题");

			}
		}

		isSuccess = true;
		message = "档案新增成功！";
		try {
			resp = BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;

	}
	// 导入excle
	private Map<String, BankDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, BankDoc> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "银行编码");
				// 创建实体类
				BankDoc bankDoc = new BankDoc();
				if (temp.containsKey(orderNo)) {
					bankDoc = temp.get(orderNo);
				}
				// r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//						System.out.println(r.getCell(0));
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上

				bankDoc.setBankEncd(orderNo);//银行编码String
				bankDoc.setBankNm(GetCellData(r, "银行名称"));//银行名称String
				bankDoc.setIndvAcctIsFixlen(
						(new Double(GetCellData(r, "个人账号是否定长") == null || GetCellData(r, "个人账号是否定长").equals("") ? "0"
								: GetCellData(r, "个人账号是否定长"))).intValue());//个人bank_encd账号是否定长Integer
				bankDoc.setIndvAcctNumLen(
						(new Double(GetCellData(r, "个人账号长度") == null || GetCellData(r, "个人账号长度").equals("") ? "0"
								: GetCellData(r, "个人账号长度"))).intValue());//个人账号长度Integer
				bankDoc.setAutoOutIndvNumLen(
						(new Double(GetCellData(r, "自动带出的个人账号长度") == null || GetCellData(r, "自动带出的个人账号长度").equals("") ? "0"
								: GetCellData(r, "自动带出的个人账号长度"))).intValue());//自动带出的个人账号长度Integer
				bankDoc.setCorpEncd(GetCellData(r, "单位编码"));//单位编码String
				bankDoc.setCompAcctIsFixLen(
						(new Double(GetCellData(r, "企业账号是否定长") == null || GetCellData(r, "企业账号是否定长").equals("") ? "0"
								: GetCellData(r, "企业账号是否定长"))).intValue());//企业账号是否定长Integer
				bankDoc.setCompAcctNumLen(
						(new Double(GetCellData(r, "企业账号长度") == null || GetCellData(r, "企业账号长度").equals("") ? "0"
								: GetCellData(r, "企业账号长度"))).intValue());//企业账号长度Integer
				bankDoc.setBankId(
						(new Double(GetCellData(r, "银行标识") == null || GetCellData(r, "银行标识").equals("") ? "0"
								: GetCellData(r, "银行标识"))).intValue());//银行标识Integer
				bankDoc.setIsSysData(
						(new Double(GetCellData(r, "是否系统预制") == null || GetCellData(r, "是否系统预制").equals("") ? "0"
								: GetCellData(r, "是否系统预制"))).intValue());//是否系统预制Integer
				bankDoc.setPubufts(new Date());//时间戳Date
				
		
				temp.put(orderNo, bankDoc);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("解析格式问题第" + j + "行"+e.getMessage());
		}
		return temp;
	}
}
