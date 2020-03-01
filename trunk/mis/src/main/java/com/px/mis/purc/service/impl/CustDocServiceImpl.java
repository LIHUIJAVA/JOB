package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.PredicatedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.CustClsDao;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.service.CustDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

/*客户档案功能实现*/
@Transactional
@Service
public class CustDocServiceImpl<E> extends poiTool implements CustDocService {
	@Autowired
	private CustDocDao cdd;
	@Autowired
	private CustClsDao ccd;

	@Override
	public String insertCustDoc(CustDoc custDoc) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (custDoc.getCustId() != "" && custDoc.getCustId() != null) {
				if (ccd.selectCustClsByClsId(custDoc.getClsId()) != null) {
					if (cdd.selectClsId(custDoc.getCustId()) != null) {
						isSuccess = false;
						message = "编号" + custDoc.getCustId() + "已存在，请重新输入！";
					} else {
						if (custDoc.getLtstInvTm() == "") {
							custDoc.setLtstInvTm(null);
						}
						if (custDoc.getLtstRecvTm() == "") {
							custDoc.setLtstRecvTm(null);
						}
						if (custDoc.getDevDt() == "") {
							custDoc.setDevDt(null);
							;
						}
						cdd.insertCustDoc(custDoc);
						isSuccess = true;
						message = "客户档案新增成功！";
					}
				} else {
					isSuccess = false;
					message = "客户档案分类不存在！";
				}
			} else {
				isSuccess = false;
				message = "客户编码不能为空！";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/insertCustDoc", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public ObjectNode updateCustDocByCustId(CustDoc custDoc) {

		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cdd.selectCustDocByCustId(custDoc.getCustId()) != null) {
			if (custDoc.getLtstInvTm() == "") {
				custDoc.setLtstInvTm(null);
			}
			if (custDoc.getLtstRecvTm() == "") {
				custDoc.setLtstRecvTm(null);
			}
			if (custDoc.getDevDt() == "") {
				custDoc.setDevDt(null);
				;
			}
			int a = cdd.updateCustDocByCustId(custDoc);
			if (a == 1) {
				on.put("isSuccess", true);
				on.put("message", "客户档案修改成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "客户档案修改失败");
			}
		} else {
			on.put("isSuccess", false);
			on.put("message", "客户档案编号不存在！");
		}
		return on;
	}

	@Override
	public String deleteCustDocList(String custId) {

		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(custId);
			int a = cdd.deleteCustDocList(list);
			if (a >= 1) {
				isSuccess = true;
				message = "删除成功！";
			} else {
				isSuccess = false;
				message = "删除失败！";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/deleteCustDocList", isSuccess, message, null);
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
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		if (StringUtils.isNotEmpty(id)) {
			if (id.contains(",")) {
				String[] str = id.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(str[i]);
				}
			} else {
				if (StringUtils.isNotEmpty(id)) {
					list.add(id);
				}
			}
		}
		return list;
	}

	// 按照编号简单查 客户档案
	@Override
	public String selectCustDocByCustId(String custId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			CustCls custCls = cdd.selectCustDocByCustId(custId);
			if (custCls == null) {
				isSuccess = false;
				message = "该客户不存在！";
			}
			resp = BaseJson.returnRespObj("purc/CustDoc/selectCustDocByCustId", isSuccess, message, custCls);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 查询全部客户档案
	@Override
	public String selectCustDocList(Map map) {
		String resp = "";
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		map.put("custIdList", custIdList);

		List<String> custTotlCorpIdList = getList((String) map.get("custTotlCorpId"));// 客户总公司编码
		map.put("custTotlCorpIdList", custTotlCorpIdList);
		String clsId = (String) map.get("clsId");
		if (clsId != null && clsId != "") {
			if (ccd.selectCustClsByClsId(clsId).getLevel() == 0) {
				map.put("clsId", "");
			}
		}
		List<CustCls> custDocList = new ArrayList<CustCls>();
		if (map.get("start") != null && map.get("end") != null && ((String) map.get("start")).trim().length() > 0
				&& ((String) map.get("end")).trim().length() > 0) {
			// 此处是区间查询
			String startStr = (String) map.get("start");
			map.put("tag", startStr.contains("-") ? startStr.split("-", 2)[0] : "");
			custDocList = cdd.selectCustDocListByItv(map);
		} else {
			custDocList = cdd.selectCustDocList(map);
		}

		int count = cdd.selectCustDocCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = custDocList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("purc/CustDoc/selectCustDocList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, custDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 打印及输入输出查询全部客户档案
	@Override
	public String printingCustDocList(Map map) {
		String resp = "";
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		map.put("custIdList", custIdList);
		String clsId = (String) map.get("clsId");
		if (clsId != null && clsId != "") {
			if (ccd.selectCustClsByClsId(clsId).getLevel() == 0) {
				map.put("clsId", "");
			}
		}
		List<CustCls> custDocList = cdd.printingCustDocList(map);
		try {
			resp = BaseJson.returnRespObjList("purc/CustDoc/printingCustDocList", true, "查询成功！", null, custDocList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 导入
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, CustDoc> custDocMap = uploadScoreInfo(file);
		for (Map.Entry<String, CustDoc> entry : custDocMap.entrySet()) {
			if (cdd.selectClsId(entry.getValue().getCustId()) != null) {
				isSuccess = false;
				message = "客户档案中部分客户已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			} else {
				cdd.insertCustDocUpload(entry.getValue());
				isSuccess = true;
				message = "客户档案导入成功！";
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/CustDoc/uploadCustDocFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, CustDoc> uploadScoreInfo(MultipartFile file) {
		Map<String, CustDoc> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "客户编码");// 客户编码
				// 创建实体类
				CustDoc custDoc = new CustDoc();
				if (temp.containsKey(orderNo)) {
					custDoc = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				custDoc.setCustId(orderNo);
				custDoc.setCustNm(GetCellData(r, "客户名称"));// 客户名称
				custDoc.setCustShtNm(GetCellData(r, "客户简称"));// 客户简称
				custDoc.setBllgCorp(GetCellData(r, "开票单位"));// 开票单位
				custDoc.setClsId(GetCellData(r, "客户分类编码"));// 客户分类编码
				custDoc.setOpnBnk(GetCellData(r, "开户银行"));// 开户银行
				custDoc.setBkatNum(GetCellData(r, "银行账号"));// 银行账号
				custDoc.setBankEncd(GetCellData(r, "所属银行编码"));// 所属银行编码
				custDoc.setSctyCrdtCd(GetCellData(r, "统一社会信用代码"));// 统一社会信用代码
				custDoc.setContcr(GetCellData(r, "联系人"));// 联系人
				custDoc.setTel(GetCellData(r, "电话"));// 电话
				custDoc.setAddr(GetCellData(r, "地址"));// 地址
				custDoc.setCustTotlCorpId(GetCellData(r, "客户总公司编码"));
				custDoc.setCustTotlCorp(GetCellData(r, "客户总公司名称"));
				custDoc.setDelvAddr(GetCellData(r, "发货地址"));// 发货地址
				if (GetCellData(r, "发展日期") == null || GetCellData(r, "发展日期").equals("")) {
					custDoc.setDevDt(null);
				} else {
					custDoc.setDevDt(GetCellData(r, "发展日期"));// 发展日期
				}
				custDoc.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					custDoc.setSetupDt(null);
				} else {
					custDoc.setSetupDt(GetCellData(r, "创建时间"));// 创建时间
				}
				custDoc.setMdfr(GetCellData(r, "修改人"));// 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					custDoc.setModiDt(null);
				} else {
					custDoc.setModiDt(GetCellData(r, "修改时间"));// 修改时间
				}
				custDoc.setMemo(GetCellData(r, "备注"));// 备注
				custDoc.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				if (GetCellData(r, "最近发票时间") == null || GetCellData(r, "最近发票时间").equals("")) {
					custDoc.setLtstInvTm(null);
				} else {
					custDoc.setLtstInvTm(GetCellData(r, "最近发票时间"));// 最近发票时间
				}
				custDoc.setLtstInvAmt(GetBigDecimal(GetCellData(r, "最近发票金额"), 8));// 8表示小数位数 //最近发票金额
				if (GetCellData(r, "最近收款时间") == null || GetCellData(r, "最近收款时间").equals("")) {
					custDoc.setLtstRecvTm(null);
				} else {
					custDoc.setLtstRecvTm(GetCellData(r, "最近收款时间"));// 最近收款时间
				}
				custDoc.setLtstRecvAmt(GetBigDecimal(GetCellData(r, "最近收款金额"), 8));// 8表示小数位数 //最近收款金额
				custDoc.setRecvblBal(GetBigDecimal(GetCellData(r, "应收余额"), 8));// 8表示小数位数 //应收余额
				temp.put(orderNo, custDoc);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

}
