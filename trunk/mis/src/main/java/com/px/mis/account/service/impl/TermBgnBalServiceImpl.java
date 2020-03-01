package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.account.dao.TermBgnBalDao;
import com.px.mis.account.entity.TermBgnBal;
import com.px.mis.account.service.TermBgnBalService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.poiTool;

/*核算期初余额功能*/
@Transactional()
@Service
public class TermBgnBalServiceImpl extends poiTool implements TermBgnBalService {
	@Autowired
	private TermBgnBalDao tbbd; // 期初余额

	// 新增期初余额
	@Override
	public String insertTermBgnBal(TermBgnBal termBgnBal) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			termBgnBal.setIsNtBookEntry(0);
			int a = tbbd.insertTermBgnBal(termBgnBal);
			if (a >= 1) {
				isSuccess = true;
				message = "期初余额新增成功！";
			} else {
				isSuccess = false;
				message = "期初余额新增失败！";
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/addTermBgnBal", isSuccess, message, termBgnBal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改期初余额
	@Override
	public String updateTermBgnBalByOrdrNum(List<TermBgnBal> termBgnBalList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			for (TermBgnBal termBgnBal : termBgnBalList) {
				int a = tbbd.updateTermBgnBalByOrdrNum(termBgnBal);
				if (a >= 1) {
					isSuccess = true;
					message = "期初余额修改成功！";
				} else {
					isSuccess = false;
					message = "期初余额修改失败，请查明原因再修改！";
				}
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/editTermBgnBal", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

//期初余额-查询
	@Override
	public String queryTermBgnBalList(Map map) {
		String resp = "";
		List<TermBgnBal> poList = tbbd.selectTermBgnBalList(map);
		int count = tbbd.selectTermBgnBalCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("account/TermBgnBal/queryTermBgnBalList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 期初余额-导出
	@Override
	public String queryTermBgnBalListPrint(Map map) {
		String resp = "";
		List<TermBgnBal> poList = tbbd.selectTermBgnBalList(map);

		try {
			resp = BaseJson.returnRespListAnno("account/TermBgnBal/queryTermBgnBalList", true, "查询成功！", poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除期初余额
	@Override
	public String deleteTermBgnBalByOrdrNum(String ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = CommonUtil.strToList(ordrNum);
			List<Integer> lists2 = new ArrayList<>();
			List<Integer> lists3 = new ArrayList<>();
			for (String list : lists) {
				TermBgnBal termBgnBal = tbbd.selectTermBgnBalByOrdrNum(Integer.valueOf(list));
				if (termBgnBal != null) {
					if (termBgnBal.getIsNtBookEntry() == 0) {
						lists2.add(Integer.valueOf(list));
					} else {
						lists3.add(Integer.valueOf(list));
					}
				}

			}
			if (lists2.size() > 0) {
				int a = tbbd.deleteTermBgnBalByOrdrNum(lists2);
				if (a >= 1) {
					isSuccess = true;
					message += "期初余额删除成功!\n";
				} else {
					isSuccess = false;
					message += "期初余额删除失败!\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "部分期初余额已被记账，无法删除！\n";
			}
			resp = BaseJson.returnRespObj("account/TermBgnBal/deleteTermBgnBalList", isSuccess, message, null);
		} catch (IOException e) {
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
	public List<Integer> getList(Integer id) {
		List<Integer> list = new ArrayList<Integer>();
		String[] str = id.toString().split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(Integer.parseInt(str[i]));
		}
		return list;
	}

	// 导入
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, TermBgnBal> termBgnBalMap = uploadScoreInfo(file);
		for (Map.Entry<String, TermBgnBal> entry : termBgnBalMap.entrySet()) {
			try {
				tbbd.insertTermBgnBalUpload(entry.getValue());
			} catch (DuplicateKeyException e) {
				isSuccess = false;
				message = "期初余额中部分数据已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			}
		}
		isSuccess = true;
		message = "期初余额导入成功！";
		try {
			resp = BaseJson.returnRespObj("account/TermBgnBal/uploadTermBgnBalFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, TermBgnBal> uploadScoreInfo(MultipartFile file) {
		Map<String, TermBgnBal> temp = new HashMap<>();
		int j = 0;
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
//				String orderNo = GetCellData(r, "序号");
				String whsEncd = GetCellData(r, "仓库编码");
				String invtyEncd = GetCellData(r, "存货编码");
				String batNum = GetCellData(r, "批次");
				String orderNo=whsEncd+invtyEncd+batNum;
				if (temp.get(orderNo)==null) {
					
				}
				// 创建实体类
				TermBgnBal termBgnBal = new TermBgnBal();
				termBgnBal.setWhsEncd(whsEncd); // '仓库编码',
				termBgnBal.setInvtyEncd(invtyEncd); // '存货编码',
				termBgnBal.setDeptId(GetCellData(r,"部门编码")); // '部门编码', 
				termBgnBal.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				termBgnBal.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				termBgnBal.setCntnTaxUprc(GetBigDecimal(GetCellData(r,"含税单价"),8)); // '含税单价', 
				termBgnBal.setPrcTaxSum(GetBigDecimal(GetCellData(r,"价税合计"),8)); // '价税合计',
				termBgnBal.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				termBgnBal.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				termBgnBal.setTaxAmt(GetBigDecimal(GetCellData(r,"税额"),8)); // '税额', 
				termBgnBal.setTaxRate(GetBigDecimal(GetCellData(r,"税率"),8)); // '税率', 
				termBgnBal.setBatNum(batNum); // '批次',
				termBgnBal.setIntlBat(GetCellData(r, "国际批次")); // '国际批次',
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					termBgnBal.setPrdcDt(null);
				} else {
					termBgnBal.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					termBgnBal.setInvldtnDt(null);
				} else {
					termBgnBal.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				termBgnBal.setBaoZhiQi(GetCellData(r, "保质期")); // '保质期',
				termBgnBal.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue()); // '是否记账', int(11
				termBgnBal.setBookEntryPers(GetCellData(r, "记账人")); // 记账人'
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					termBgnBal.setBookEntryTm(null);
				} else {
					termBgnBal.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				termBgnBal.setSetupPers(GetCellData(r, "创建人")); // '创建人'
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					termBgnBal.setSetupTm(null);
				} else {
					termBgnBal.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				termBgnBal.setModiPers(GetCellData(r, "修改人")); // '修改人'
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					termBgnBal.setModiTm(null);
				} else {
					termBgnBal.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				termBgnBal.setInvtySubjId(GetCellData(r, "存货科目编码"));// 存货科目编码
				termBgnBal.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				temp.put(orderNo, termBgnBal);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}
}
