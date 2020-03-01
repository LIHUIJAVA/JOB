package com.px.mis.account.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.OutIntoWhsAdjSnglDao;
import com.px.mis.account.dao.OutIntoWhsAdjSnglSubTabDao;
import com.px.mis.account.entity.OutIntoWhsAdjSngl;
import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;
import com.px.mis.account.service.OutIntoWhsAdjSnglService;
import com.px.mis.account.service.OutIntoWhsAdjSnglSubTabService;
import com.px.mis.purc.dao.RecvSendCateDao;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//出入库调整单主表实体类
@Transactional
@Service
public class OutIntoWhsAdjSnglServiceImpl extends poiTool implements OutIntoWhsAdjSnglService {
	@Autowired
	private OutIntoWhsAdjSnglDao outIntoWhsAdjSnglDao;
	@Autowired
	private OutIntoWhsAdjSnglSubTabDao outIntoWhsAdjSnglSubTabDao;
	@Autowired
	private RecvSendCateDao RecvSendCateDao;
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private OutIntoWhsAdjSnglSubTabService outIntoWhsAdjSnglSubTabService;

	@Override
	public ObjectNode insertOutIntoWhsAdjSngl(String userId, OutIntoWhsAdjSngl outIntoWhsAdjSngl, String loginTime) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取订单号
		String number = getOrderNo.getSeqNo("TZ", userId, loginTime);
		if (number == null) {
			on.put("isSuccess", false);
			on.put("message", "出入库调整单新增失败,主键不能为空");
		} else if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(number) != null) {
			on.put("isSuccess", false);
			on.put("message", "出入库调整单单据号" + number + "已存在，新增失败！");
		} else {
			outIntoWhsAdjSngl.setFormNum(number);
			int outIntoResult = outIntoWhsAdjSnglDao.insertOutIntoWhsAdjSngl(outIntoWhsAdjSngl);
			if (outIntoResult == 1) {
				List<OutIntoWhsAdjSnglSubTab> subTabList = outIntoWhsAdjSngl.getOutIntoList();
				for (OutIntoWhsAdjSnglSubTab subTab : subTabList) {
					subTab.setFormNum(number);
					on = outIntoWhsAdjSnglSubTabService.insertOutIntoWhsAdjSnglSubTab(subTab);
				}
			} else {
				on.put("isSuccess", false);
				on.put("message", "出入库调整单主表处理失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateOutIntoWhsAdjSnglByFormNum(OutIntoWhsAdjSngl outIntoWhsAdjSngl) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String formNum = outIntoWhsAdjSngl.getFormNum();
		if (formNum == null) {
			on.put("isSuccess", false);
			on.put("message", "出入库调整单主表修改失败,主键不能为空");
		} else if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(formNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "出入库调整单主表中编号" + formNum + "不存在，更新失败！");
		} else if (RecvSendCateDao.selectRecvSendCateByRecvSendCateId(outIntoWhsAdjSngl.getRecvSendCateId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "出入库调整单主表中收发类别编号" + outIntoWhsAdjSngl.getRecvSendCateId() + "不存在，更新失败！");
		} else {
			int updateResult = outIntoWhsAdjSnglDao.updateOutIntoWhsAdjSnglByFormNum(outIntoWhsAdjSngl);
			int deleteResult = outIntoWhsAdjSnglSubTabService
					.deleteOutIntoWhsAdjSnglSubTabByFormNum(outIntoWhsAdjSngl.getFormNum());
			if (updateResult == 1 && deleteResult >= 1) {
				List<OutIntoWhsAdjSnglSubTab> outIntoList = outIntoWhsAdjSngl.getOutIntoList();
				for (OutIntoWhsAdjSnglSubTab subTab : outIntoList) {
					subTab.setFormNum(formNum);
					outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(subTab);
				}
				on.put("isSuccess", true);
				on.put("message", "修改成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "修改失败");
			}
		}
		return on;
	}

	// 单个删除
	@Override
	public ObjectNode deleteOutIntoWhsAdjSnglByFormNum(String formNums) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> lists = getList(formNums);
		List<String> lists2 = new ArrayList<>();
		List<String> lists3 = new ArrayList<>();
		for (String formNum : lists) {
			if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglIsNtBookEntry(formNum) == 0) {
				lists2.add(formNum);
			} else {
				lists3.add(formNum);
			}
		}
		if (lists2.size() > 0) {
			int a = outIntoWhsAdjSnglDao.deleteOutIntoWhsAdjSnglList(lists2);// dao层 批量删除
			if (a >= 1) {
				on.put("isSuccess", true);
				on.put("message", "单据号为：" + lists2.toString() + "的订单删除成功!\n");
			} else {
				on.put("isSuccess", false);
				on.put("message", "单据号为：" + lists2.toString() + "的订单删除失败!\n");
			}
		}
		if (lists3.size() > 0) {
			on.put("isSuccess", false);
			on.put("message", "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n");
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
	public OutIntoWhsAdjSngl selectOutIntoWhsAdjSnglByFormNum(String formNum) {
		OutIntoWhsAdjSngl selectOne = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(formNum);
		return selectOne;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectOutIntoWhsAdjSnglList(Map map) {
		String resp = "";
		List<OutIntoWhsAdjSngl> list = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
		int count = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglCount(map);
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl", true, "查询成功！", count,
					pageNo, pageSize, listNum, pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectOutIntoWhsAdjSnglPrint(Map map) {
		String resp = "";
		try {
			List<OutIntoWhsAdjSngl> list = outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglList(map);
			if(list.size() == 0) {
				OutIntoWhsAdjSngl s = new OutIntoWhsAdjSngl();
				List<OutIntoWhsAdjSnglSubTab> outIntoList = new ArrayList<>();
				OutIntoWhsAdjSnglSubTab ss = new OutIntoWhsAdjSnglSubTab();
				outIntoList.add(ss);
				s.setOutIntoList(outIntoList);
				list.add(s);
			}
			ArrayList<JsonNode> flattenList = flattening(list);
			resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint", true, "查询成功！",
					flattenList);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return resp;
	}

	// 导入excel
	@Override
	public String uploadFileAddDb(MultipartFile file) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		Map<String, OutIntoWhsAdjSngl> pusOrderMap = uploadScoreInfo(file);

		for (Map.Entry<String, OutIntoWhsAdjSngl> entry : pusOrderMap.entrySet()) {

			if (outIntoWhsAdjSnglDao.selectOutIntoWhsAdjSnglByFormNum(entry.getValue().getFormNum()) != null) {
				isSuccess = false;
				message = "插入重复单号 " + entry.getValue().getFormNum() + " 请检查";
				throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");
			} else {
				
				List<OutIntoWhsAdjSnglSubTab> subTabList = entry.getValue().getOutIntoList();
				if(subTabList.size() > 0) {
					outIntoWhsAdjSnglDao.EXinsertOutIntoWhsAdjSngl(entry.getValue());
					for (OutIntoWhsAdjSnglSubTab subTab : subTabList) {
						outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(subTab);
						// outIntoWhsAdjSnglSubTabService.insertOutIntoWhsAdjSnglSubTab(subTab);
						isSuccess = true;
						message = "调整单导入成功！";
					}
				} else {
					isSuccess = false;
					message = "导入失败，请检查文件内容！";
				}
				
			}

			
		}


		resp = BaseJson.returnRespObj("account/outIntoWhsAdjSngl/uploadFileAddDb", isSuccess, message, null);

		return resp;

	}

	// 导入excel (封装)
	private Map<String, OutIntoWhsAdjSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, OutIntoWhsAdjSngl> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "单据号");
				// 创建实体类
				OutIntoWhsAdjSngl outIntoWhsAdjSngl = new OutIntoWhsAdjSngl();
				if (temp.containsKey(orderNo)) {
					outIntoWhsAdjSngl = temp.get(orderNo);
				}
				List<OutIntoWhsAdjSnglSubTab> snglSubTabs = outIntoWhsAdjSngl.getOutIntoList();// 订单子表
				if (snglSubTabs == null) {
					snglSubTabs = new ArrayList<>();
				}
				OutIntoWhsAdjSnglSubTab sub = new OutIntoWhsAdjSnglSubTab();
				sub.setInvtyEncd(GetCellData(r, "存货编码"));
				sub.setInvtyNm(GetCellData(r, "存货名称"));
				sub.setWhsEncd(GetCellData(r, "仓库编码"));
				sub.setWhsNm(GetCellData(r, "仓库名称"));
				sub.setBatNum(GetCellData(r, "批号"));
				sub.setSpcModel(GetCellData(r, "规格型号"));
				sub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));
				sub.setAmt(GetBigDecimal(GetCellData(r, "金额"), 8));
				sub.setMeasrCorpNm(GetCellData(r, "计量单位名称"));
				if(StringUtils.isNotEmpty(GetCellData(r, "来源子表序号"))) {
					sub.setToOrdrNum(Long.valueOf(GetCellData(r, "来源子表序号")));
				}
				sub.setProjEncd(GetCellData(r, "项目编码"));
				sub.setProjNm(GetCellData(r, "项目名称"));
				sub.setFormNum(orderNo);
				snglSubTabs.add(sub);
				outIntoWhsAdjSngl.setOutIntoList(snglSubTabs);
				

			
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				outIntoWhsAdjSngl.setFormNum(orderNo); // 单据号
				// 单据日期
				if (GetCellData(r, "单据日期") == null) {
					outIntoWhsAdjSngl.setFormTm(null); // 单据日期
				} else {
					outIntoWhsAdjSngl.setFormTm(GetCellData(r, "单据日期").replaceAll("[^0-9:-]", " ").length() == 0 ? null
							: GetCellData(r, "单据日期").replaceAll("[^0-9:-]", " "));
				}
				outIntoWhsAdjSngl.setOutIntoList(snglSubTabs);
				// 收发类别编码
				outIntoWhsAdjSngl.setRecvSendCateId(GetCellData(r, "收发类别编码"));
				// 用户编码
				outIntoWhsAdjSngl.setAccNum(GetCellData(r, "用户编码"));
				// 用户名称
				outIntoWhsAdjSngl.setUserName(GetCellData(r, "用户名称"));
				// 部门编码
				outIntoWhsAdjSngl.setDeptId(GetCellData(r, "部门编码"));
				// 客户编码
				outIntoWhsAdjSngl.setCustId(GetCellData(r, "客户编码"));
				// 供应商编码
				outIntoWhsAdjSngl.setProvrId(GetCellData(r, "供应商编码"));
				// 出入库
				outIntoWhsAdjSngl.setIsFifoAdjBan(
						(new Double(GetCellData(r, "出入库") == null || GetCellData(r, "出入库").equals("") ? "0"
								: GetCellData(r, "出入库"))).intValue());
				// 是否记账
				outIntoWhsAdjSngl.setIsCrspdOutWhsSngl(
						(new Double(GetCellData(r, "是否记账") == null || GetCellData(r, "是否记账").equals("") ? "0"
								: GetCellData(r, "是否记账"))).intValue());
				// 记账人
				outIntoWhsAdjSngl.setBookEntryTm(GetCellData(r, "记账人"));

				// 记账时间
				if (GetCellData(r, "记账时间") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " ")); // 记账时间
				}
				// 修改人
				outIntoWhsAdjSngl.setMdfrPers(GetCellData(r, "修改人"));
				// 修改时间
				if (GetCellData(r, "修改时间") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // 修改时间
				}
				// 创建人
				outIntoWhsAdjSngl.setSetupPers(GetCellData(r, "创建人"));
				// 创建时间
				if (GetCellData(r, "创建时间") == null) {
					outIntoWhsAdjSngl.setBookEntryTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " ")); // 记账时间
				}
				// 表头备注
				outIntoWhsAdjSngl.setMemo(GetCellData(r, "表头备注"));
				// 是否先进先出
				outIntoWhsAdjSngl.setIsFifoAdjBan(
						GetCellData(r, "是否先进先出") == null || GetCellData(r, "是否先进先出").equals("") ? 0 : 1);
				// 是否对应出库单
				outIntoWhsAdjSngl.setIsCrspdOutWhsSngl(
						GetCellData(r, "是否对应出库单") == null || GetCellData(r, "是否对应出库单").equals("") ? 0 : 1);
				// 被调整单据号
				outIntoWhsAdjSngl.setBeadjOutIntoWhsMastabInd(GetCellData(r, "被调整单据号"));
				// 是否销售
				outIntoWhsAdjSngl
						.setIsNtSell(GetCellData(r, "是否销售") == null || GetCellData(r, "是否销售").equals("") ? 0 : 1);
				// 收发类别
				outIntoWhsAdjSngl.setRecvSendCateNm(GetCellData(r, "收发类别"));
				// 部门名称
				outIntoWhsAdjSngl.setDeptNm(GetCellData(r, "部门名称"));
				// 客户名称
				outIntoWhsAdjSngl.setCustNm(GetCellData(r, "客户名称"));
				// 供应商
				outIntoWhsAdjSngl.setProvrNm(GetCellData(r, "供应商"));
				// 来源单据类型编码
				outIntoWhsAdjSngl.setToFormTypEncd(GetCellData(r, "来源单据类型编码"));
				// 是否生成凭证
				outIntoWhsAdjSngl.setIsNtMakeVouch(
						GetCellData(r, "是否生成凭证") == null || GetCellData(r, "是否生成凭证").equals("") ? 0 : 1);
				// 制凭证人
				outIntoWhsAdjSngl.setMakVouchPers(GetCellData(r, "制凭证人"));
				// 制凭证时间
				if (GetCellData(r, "制凭证时间") == null) {
					outIntoWhsAdjSngl.setMakVouchTm(null);
				} else {
					outIntoWhsAdjSngl
							.setBookEntryTm(GetCellData(r, "制凭证时间").replaceAll("[^0-9:-]", " ").length() == 0 ? null
									: GetCellData(r, "制凭证时间").replaceAll("[^0-9:-]", " ")); // 制凭证时间
				}
				// 单据类型编码
				outIntoWhsAdjSngl.setFormTypEncd(GetCellData(r, "单据类型编码"));
				// 单据类型名称
				outIntoWhsAdjSngl.setFormTypName(GetCellData(r, "单据类型名称"));
				// 向集合中添加对象
				temp.put(orderNo, outIntoWhsAdjSngl);
			}
			// 关闭输入流
			fileIn.close();
		} catch (Exception e) {

			e.printStackTrace();

			throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());
		}
		return temp;
	}

	private <T> ArrayList<JsonNode> flattening(List<T> itList) throws IOException {
		try {
			ArrayList<JsonNode> nodeList = new ArrayList<JsonNode>();
			JacksonUtil.turnOnAnno();
			for (int i = 0; i < itList.size(); i++) {
				OutIntoWhsAdjSngl AdjSngl = (OutIntoWhsAdjSngl) itList.get(i);
				List<OutIntoWhsAdjSnglSubTab> outIntoList = AdjSngl.getOutIntoList();

				for (OutIntoWhsAdjSnglSubTab item : outIntoList) {
					AdjSngl.setOutIntoList(null);
					ObjectNode bond = JacksonUtil.getObjectNode("");
					ObjectNode mainNode = JacksonUtil.getObjectNode(AdjSngl);
					ObjectNode subNode = JacksonUtil.getObjectNode(item);
					bond.setAll(mainNode);
					bond.setAll(subNode);
					nodeList.add(bond);
				}
			}
			return nodeList;
		} finally {
			JacksonUtil.turnOffAnno();
		}
	}

}
