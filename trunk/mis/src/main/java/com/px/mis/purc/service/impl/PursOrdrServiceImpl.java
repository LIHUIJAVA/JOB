package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.PursOrdrDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.service.PursOrdrService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;

/*采购订单功能*/
@Transactional
@Service
public class PursOrdrServiceImpl extends poiTool implements PursOrdrService {
	@Autowired
	private PursOrdrDao pod;
	@Autowired
	private PursOrdrSubDao posd;
	@Autowired
	private ToGdsSnglDao tgsd;
	@Autowired
	private InvtyDocDao invtyDocDao;
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;

	// 新增采购订单
	@Override
	public String addPursOrdr(String userId, PursOrdr pursOrdr, List<PursOrdrSub> pursOrdrSubList, String loginTime)
			throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			// 获取订单号
			String number = getOrderNo.getSeqNo("CG", userId, loginTime);
			if (pod.selectPursOrdrById(number) != null) {
				isSuccess = false;
				message = "编码" + number + "已存在，请重新输入！";
			} else {
				pursOrdr.setPursOrdrId(number);
				for (PursOrdrSub pursOrdrSub : pursOrdrSubList) {
					pursOrdrSub.setPursOrdrId(pursOrdr.getPursOrdrId());
					if (pursOrdrSub.getPlanToGdsDt() == "") {
						pursOrdrSub.setPlanToGdsDt(null);
					}
					pursOrdrSub.setUnToGdsQty(pursOrdrSub.getQty());// 默认未到货数量=采购订单数量
					pursOrdrSub.setUnApplPayQty(pursOrdrSub.getQty());// 默认未付款数量=采购订单数量
					pursOrdrSub.setUnApplPayAmt(pursOrdrSub.getPrcTaxSum());// 默认未付款金额=采购订单含税金额
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(pursOrdrSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
						isSuccess = false;
						message = "编码为" + pursOrdr.getPursOrdrId() + "的采购订单对应的存货:" + pursOrdrSub.getInvtyEncd()
								+ "没有设置是否采购属性，无法保存！";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "编码为" + pursOrdr.getPursOrdrId() + "的采购订单对应的存货:" + pursOrdrSub.getInvtyEncd()
								+ "非可采购存货，无法保存！";
						throw new RuntimeException(message);
					}
				}
				int a = pod.insertPursOrdr(pursOrdr);
				int b = posd.insertPursOrdrSub(pursOrdrSubList);
				if (a >= 1 && b >= 1) {
					message = "新增成功！";
					isSuccess = true;
				} else {
					isSuccess = false;
					message = "新增失败！";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/addPursOrdr", isSuccess, message, pursOrdr);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());

		}
		return resp;
	}

	// 修改采购订单
	@Override
	public String editPursOrdr(PursOrdr pursOrdr, List<PursOrdrSub> pursOrdrSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<PursOrdrSub> pursOrdrSubSet = new TreeSet();
//			pursOrdrSubSet.addAll(pursOrdrSubList);
//			if(pursOrdrSubSet.size() < pursOrdrSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/PursOrdr/editPursOrdr", false, "表体存货明细不允许存在相同的存货！",null);
//				return resp;
//			}
			for (PursOrdrSub pursOrdrSub : pursOrdrSubList) {
				pursOrdrSub.setPursOrdrId(pursOrdr.getPursOrdrId());
				if (pursOrdrSub.getPlanToGdsDt() == "") {
					pursOrdrSub.setPlanToGdsDt(null);
				}
				pursOrdrSub.setUnToGdsQty(pursOrdrSub.getQty());// 默认未到货数量=采购订单数量
				pursOrdrSub.setUnApplPayQty(pursOrdrSub.getQty());// 默认未付款数量=采购订单数量
				pursOrdrSub.setUnApplPayAmt(pursOrdrSub.getPrcTaxSum());// 默认未付款金额=采购订单含税金额
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(pursOrdrSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
					isSuccess = false;
					message = "编码为" + pursOrdr.getPursOrdrId() + "的采购订单对应的存货:" + pursOrdrSub.getInvtyEncd()
							+ "没有设置是否采购属性，无法保存！";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "编码为" + pursOrdr.getPursOrdrId() + "的采购订单对应的存货:" + pursOrdrSub.getInvtyEncd()
							+ "非可采购存货，无法保存！";
					throw new RuntimeException(message);
				}
			}
			int a = posd.deletePursOrdrSubByPursOrdrId(pursOrdr.getPursOrdrId());
			int b = pod.updatePursOrdrByPursOrdrId(pursOrdr);
			int c = posd.insertPursOrdrSub(pursOrdrSubList);
			if (a >= 1 && b >= 1 && c >= 1) {
				message = "更新成功！";
				isSuccess = true;
			} else {
				isSuccess = false;
				message = "更新失败！";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/editPursOrdr", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 删除采购订单
	@Override
	public String deletePursOrdr(String pursOrdrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (pod.selectPursOrdrByPursOrdrId(pursOrdrId) != null) {
			pod.deletePursOrdrByPursOrdrId(pursOrdrId);
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编码" + pursOrdrId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/deletePursOrdr", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 按照编码查询采购订单
	@Override
	public String queryPursOrdr(String pursOrdrId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<PursOrdrSub> PursOrdrSub = new ArrayList<>(); */
		PursOrdr pursOrdr = pod.selectPursOrdrByPursOrdrId(pursOrdrId);
		if (pursOrdr != null) {
			/* PursOrdrSub=posd.selectPursOrdrSubByPursOrdrId(pursOrdrId); */
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + pursOrdrId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/queryPursOrdr", isSuccess, message, pursOrdr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 分页查询采购订单列表
	@Override
	public String queryPursOrdrList(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<PursOrdr> poList = pod.selectPursOrdrList(map);

		int count = pod.selectPursOrdrCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
	@Override
	public String deletePursOrdrList(String pursOrdrId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(pursOrdrId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (pod.selectPursOrdrIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deletePursOrdrList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					e.printStackTrace();
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
				}
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
			}
			resp = BaseJson.returnRespObj("purc/PursOrdr/deletePursOrdrList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	public int deletePursOrdrList(List<String> lists2) throws Exception {
		pod.insertPursOrdrDl(lists2);
		posd.insertPursOrdrSubDl(lists2);

		return pod.deletePursOrdrList(lists2);
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

	// 采购订单审核时修改审核状态
	@Override
	public String updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		for (PursOrdr pOrdr : pursOrdr) {
			if (pOrdr.getIsNtChk() == 1) {
				if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 0) {
					int a = pod.updatePursOrdrIsNtChk(pOrdr);
					if (a >= 1) {
						isSuccess = true;
						message += pOrdr.getPursOrdrId() + "采购订单审核成功！\n";
					} else {
						isSuccess = false;
						message += pOrdr.getPursOrdrId() + "采购订单审核失败！\n";
						throw new RuntimeException(message);
					}
				} else if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 1) {
					isSuccess = false;
					message += pOrdr.getPursOrdrId() + "该单据已审核，不需要重复审核\n";
				}
			} else if (pOrdr.getIsNtChk() == 0) {
				if (pod.selectPursOrdrIsNtChk(pOrdr.getPursOrdrId()) == 1) {
					List<ToGdsSngl> toGdsSnglList = tgsd.selectToGdsSnglByPursOrdrId(pOrdr.getPursOrdrId());
					if (toGdsSnglList.size() > 0) {
						isSuccess = false;
						message += "单据号为" + pOrdr.getPursOrdrId() + "的采购订单已存在下游单据【到货单】，无法弃审！";
					} else {
						int a = pod.updatePursOrdrIsNtChk(pOrdr);
						if (a >= 1) {
							isSuccess = true;
							message += pOrdr.getPursOrdrId() + "采购订单弃审成功！\n";
						} else {
							isSuccess = false;
							message += pOrdr.getPursOrdrId() + "采购订单弃审失败！\n";
							throw new RuntimeException(message);
						}
					}
				} else {
					isSuccess = false;
					message += pOrdr.getPursOrdrId() + "该单据未审核，请先审核该单据\n";
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("purc/PursOrdr/updatePursOrdrIsNtChk", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 打印及输入输出
	@Override
	public String printingPursOrdrList(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		List<zizhu> pursOrdrList = pod.printingPursOrdrList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/PursOrdr/printingPursOrdrList", true, "查询成功！", null,
					pursOrdrList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 导入
	public String uploadFileAddDb(MultipartFile file, int i, HashMap params) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, PursOrdr> pusOrderMap = null;
		if (i == 0) {
			pusOrderMap = uploadScoreInfo(file, params);
		} else if (i == 1) {
			pusOrderMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
		List<PursOrdr> pursOrdrList = pusOrderMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<PursOrdr>> pursOrdrLists = Lists.partition(pursOrdrList, 1000);

		for (List<PursOrdr> pursOrdr : pursOrdrLists) {
			try {
				pod.insertPursOrdrUpload(pursOrdr);
			} catch (DuplicateKeyException e) {
				String err = e.getRootCause().getMessage();
				err = err.substring(17, 35);
				throw new RuntimeException("单号" + err + "重复插入,请检查数据,或者底部有空行!");
			}
		}
		List<PursOrdrSub> pursOrdrSubList = new ArrayList<>();
		int flag = 0;
		for (PursOrdr entry : pursOrdrList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<PursOrdrSub> tempList = entry.getPursOrdrSub();
			tempList.forEach(s -> s.setPursOrdrId(entry.getPursOrdrId()));
			pursOrdrSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (pursOrdrSubList.size() >= 1000 || pusOrderMap.size() == flag) {
				try {
					posd.insertPursOrdrSub(pursOrdrSubList);
				} catch (Exception e) {
					throw new RuntimeException("子表新增出错!请联系后台");
				}

				pursOrdrSubList.clear();
			}
		}
		isSuccess = true;
		message = "采购订单导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/PursOrdr/uploadPursOrdrFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, PursOrdr> uploadScoreInfo(MultipartFile file, HashMap params) {
		Map<String, PursOrdr> temp = new HashMap<>();
		int j = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
			// 设置列名称
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = getOrderNo.getSeqNo("CG", (String) params.get("accNum"),
						(String) params.get("loginTime"));// GetCellData(r, "采购订单编码");

				// 创建实体类
				PursOrdr pursOrdr = new PursOrdr();
				if (temp.containsKey(orderNo)) {
					pursOrdr = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				pursOrdr.setPursTypId(GetCellData(r, "采购类型编码"));// 采购类型编码
				pursOrdr.setPursOrdrId(orderNo);// 采购订单编码
				if (GetCellData(r, "采购订单日期") == null || GetCellData(r, "采购订单日期").equals("")) {
					pursOrdr.setPursOrdrDt(null);
				} else {
					pursOrdr.setPursOrdrDt(GetCellData(r, "采购订单日期").replaceAll("[^0-9:-]", " "));// 采购订单日期
				}
				pursOrdr.setProvrId(GetCellData(r, "供应商编码"));// 供应商编码
				pursOrdr.setDeptId(GetCellData(r, "部门编码"));// 部门编码
				pursOrdr.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				pursOrdr.setUserName(GetCellData(r, "业务员"));// 用户名称
				pursOrdr.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				pursOrdr.setFormTypEncd("001");// 单据类型编码 GetCellData(r, "单据类型编码")
				pursOrdr.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					pursOrdr.setSetupTm(null);
				} else {
					pursOrdr.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
//				pursOrdr.setSetupTm((String) params.get("loginTime"));// 创建时间
				pursOrdr.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				pursOrdr.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					pursOrdr.setChkTm(null);
				} else {
					pursOrdr.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				pursOrdr.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					pursOrdr.setModiTm(null);
				} else {
					pursOrdr.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				pursOrdr.setMemo(GetCellData(r, "表头备注"));// 备注
				List<PursOrdrSub> pursOrdrSubList = pursOrdr.getPursOrdrSub();// 采购订单子表
				if (pursOrdrSubList == null) {
					pursOrdrSubList = new ArrayList<>();
				}
				PursOrdrSub pursOrderSub = new PursOrdrSub();
//				pursOrderSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));应该自动生成

//				String invtyEncd = GetCellData(r, "存货编码");	
				pursOrderSub.setInvtyEncd(GetCellData(r, "存货编码"));
				pursOrderSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				pursOrderSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 箱数
//				if ((GetBigDecimal(GetCellData(r, "箱数"), 8) == null || (GetBigDecimal(GetCellData(r, "箱数"), 8).equals("")) {
//					
//				};
				pursOrderSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				pursOrderSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				pursOrderSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				pursOrderSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				pursOrderSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				pursOrderSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				pursOrderSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				pursOrderSub.setUnToGdsQty(GetBigDecimal(GetCellData(r, "未到货数量"), 8));// 8表示小数位数 //未到货数量
				pursOrderSub.setUnApplPayQty(GetBigDecimal(GetCellData(r, "未申请付款数量"), 8));// 8表示小数位数 //未付款数量
				pursOrderSub.setUnApplPayAmt(GetBigDecimal(GetCellData(r, "未申请付款金额"), 8));// 8表示小数位数 //未付款金额
				if (GetCellData(r, "计划到货时间") == null || GetCellData(r, "计划到货时间").equals("")) {
					pursOrderSub.setPlanToGdsDt(null);
				} else {
					pursOrderSub.setPlanToGdsDt(GetCellData(r, "计划到货时间").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}
				pursOrderSub.setMemo(GetCellData(r, "表体备注"));
				pursOrdrSubList.add(pursOrderSub);

				pursOrdr.setPursOrdrSub(pursOrdrSubList);
				temp.put(orderNo, pursOrdr);

			}
			fileIn.close();

		} catch (Exception e) {

			if (e instanceof OfficeXmlFileException) {
				throw new RuntimeException("Excel版本错误,请打开文件后另存为xls格式(97-2003版本文件)");
			}
			if (e instanceof RuntimeException) {
				e.printStackTrace();
				throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
			}
		}
		return temp;
	}

	// 导入excle
	private Map<String, PursOrdr> uploadScoreInfoU8(MultipartFile file) {
		Map<String, PursOrdr> temp = new HashMap<>();
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
			// 设置列名称
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "订单编码");
				// 创建实体类
				PursOrdr pursOrdr = new PursOrdr();
				if (temp.containsKey(orderNo)) {
					pursOrdr = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				pursOrdr.setPursTypId("1");// 采购类型编码
				pursOrdr.setPursOrdrId(orderNo);// 采购订单编码
				if (GetCellData(r, "日期") == null || GetCellData(r, "日期").equals("")) {
					pursOrdr.setPursOrdrDt(null);
				} else {
					pursOrdr.setPursOrdrDt(GetCellData(r, "日期").replaceAll("[^0-9:-]", " "));// 采购订单日期
				}
				pursOrdr.setProvrId(GetCellData(r, "供应商编码"));// 供应商编码
				pursOrdr.setDeptId(GetCellData(r, "部门编码"));// 部门编码
				pursOrdr.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				pursOrdr.setUserName(GetCellData(r, "业务员"));// 用户名称
				pursOrdr.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				pursOrdr.setFormTypEncd("001");// 单据类型编码
				pursOrdr.setSetupPers(GetCellData(r, "制单人"));// 创建人
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					pursOrdr.setSetupTm(null);
				} else {
					pursOrdr.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				pursOrdr.setIsNtChk(1);// 是否审核
				pursOrdr.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					pursOrdr.setChkTm(null);
				} else {
					pursOrdr.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				pursOrdr.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					pursOrdr.setModiTm(null);
				} else {
					pursOrdr.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				pursOrdr.setMemo(GetCellData(r, "备注"));// 备注
				List<PursOrdrSub> pursOrdrSubList = pursOrdr.getPursOrdrSub();// 采购订单子表
				if (pursOrdrSubList == null) {
					pursOrdrSubList = new ArrayList<>();
				}
				PursOrdrSub pursOrderSub = new PursOrdrSub();
//				pursOrderSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				pursOrderSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				pursOrderSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				pursOrderSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				pursOrderSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				pursOrderSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "原币含税单价"), 8));// 8表示小数位数 //含税单价
				pursOrderSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "本币单价"), 8));// 8表示小数位数 //无税单价
				pursOrderSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "本币金额"), 8));// 8表示小数位数 //无税金额
				pursOrderSub.setTaxAmt(GetBigDecimal(GetCellData(r, "本币税额"), 8));// 8表示小数位数 //税额
				pursOrderSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "本币价税合计"), 8));// 8表示小数位数 //价税合计
//				税率=表头税率
				pursOrderSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				pursOrderSub.setUnToGdsQty(BigDecimal.ZERO);// 8表示小数位数 //未到货数量
				pursOrderSub.setUnApplPayQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //未付款数量
				pursOrderSub.setUnApplPayAmt(GetBigDecimal(GetCellData(r, "本币价税合计"), 8));// 8表示小数位数 //未付款金额
				if (GetCellData(r, "计划到货日期") == null || GetCellData(r, "计划到货日期").equals("")) {
					pursOrderSub.setPlanToGdsDt(null);
				} else {
					pursOrderSub.setPlanToGdsDt(GetCellData(r, "计划到货日期").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}
				pursOrderSub.setMemo(GetCellData(r, "表体备注"));
				pursOrdrSubList.add(pursOrderSub);

				pursOrdr.setPursOrdrSub(pursOrdrSubList);
				temp.put(orderNo, pursOrdr);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 采购明细查询
	@Override
	public String queryPursOrdrByInvtyEncd(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			List<Map> poList = pod.selectPursOrdrByInvtyEncd(map);
			Map tableSums = pod.selectPursOrdrByInvtyEncdSums(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = pod.selectPursOrdrByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrByInvtyEncd", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = pod.selectPursOrdrByInvtyEncd(map);
			try {
				resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrByInvtyEncd", true, "查询成功！", poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	// 采购明细-导出
	@Override
	public String queryPursOrdrByInvtyEncdPrint(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<Map> poList = pod.selectPursOrdrByInvtyEncdPrint(map);
		try {
			resp = BaseJson.returnRespListAnno("purc/PursOrdr/queryPursOrdrByInvtyEncdPrint", true, "导出成功！", poList);

		} catch (IOException e) {

			e.printStackTrace();

		}

		return resp;
	}

	// 参照时查询所有未申请付款数量的采购订单子表信息
	@Override
	public String selectUnApplPayQtyByPursOrdrId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<PursOrdrSub> pursOrdrSubList = posd.selectUnApplPayQtyByPursOrdrId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/PursOrdr/selectUnApplPayQtyByPursOrdrId", true, "查询成功！", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 参照时查询所有未到货数量的采购订单子表信息
	@Override
	public String selectUnToGdsQtyByPursOrdrId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<PursOrdrSub> pursOrdrSubList = posd.selectUnToGdsQtyByPursOrdrId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/PursOrdr/selectUnToGdsQtyByPursOrdrId", true, "查询成功！", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 到货单、付款申请单参照时条件查询采购订单主表信息
	@Override
	public String queryPursOrdrLists(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		List<PursOrdr> poList = pod.selectPursOrdrLists(map);
		int count = poList.size();
		if (poList.size()<(int) map.get("num")) {
			//do nothing 
		}else {
			poList = poList.subList((int) map.get("index"),
					(int) map.get("num") + (int) map.get("index"));
		}
//		int count = pod.selectPursOrdrCounts(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrLists", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 分页+排序查询采购订单列表
	@Override
	public String queryPursOrdrListOrderBy(Map map) {
		String resp = "";
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码

		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);

		List<zizhu> poList;

		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "po.purs_ordr_dt");
			map.put("sortOrder", "desc");
		}

		poList = pod.selectPursOrdrListOrderBy(map);
		Map tableSums = pod.selectPursOrdrListSum(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = pod.selectPursOrdrCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/PursOrdr/queryPursOrdrListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 子主类
	public static class zizhu {
		// 主表
		@JsonProperty("序号")
		public Long ordrNum;// 序号
		@JsonProperty("采购订单编码")
		public String pursOrdrId;// 采购订单编码

		@JsonProperty("采购订单日期")
		public String pursOrdrDt;// 采购订单日期

		@JsonProperty("采购类型编码")
		public String pursTypId;// 采购类型编码

		@JsonProperty("采购类型名称")
		public String pursTypNm;// 采购类型名称
		@JsonProperty("单据类型编码")
		public String formTypEncd;// 单据类型编码
		@JsonProperty("单据类型名称")
		public String formTypName;// 单据类型名称
		@JsonProperty("供应商编码")
		public String provrId;// 供应商编码

		@JsonProperty("供应商名称")
		public String provrNm;// 供应商名称
		@JsonProperty("业务员编码")
		public String accNum;// 用户编码

		@JsonProperty("业务员")
		public String userName;// 用户名称

		@JsonProperty("部门编码")
		public String deptId;// 部门编码

		@JsonProperty("部门名称")
		public String deptName;// 部门名称
		@JsonProperty("供应商订单号")
		public String provrOrdrNum;// 供应商订单号

		@JsonProperty("是否审核")
		public Integer isNtChk;// 是否审核

		@JsonProperty("审核人")
		public String chkr;// 审核人

		@JsonProperty("审核时间")
		public String chkTm;// 审核时间

		@JsonProperty("创建人")
		public String setupPers;// 创建人

		@JsonProperty("创建时间")
		public String setupTm;// 创建时间

		@JsonProperty("修改人")
		public String mdfr;// 修改人

		@JsonProperty("修改时间")
		public String modiTm;// 修改时间

		@JsonProperty("表头备注")
		public String memo;// 备注

		// 子表
		@JsonProperty("存货编码")
		public String invtyEncd;// 存货编码

		@JsonProperty("存货名称")
		public String invtyNm;// 存货名称
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;// 无税单价

		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;// 无税金额

		@JsonProperty("税额")
		public BigDecimal taxAmt;// 税额

		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;// 含税单价

		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;// 价税合计

		@JsonProperty("税率")
		public BigDecimal taxRate;// 税率

//		@JsonProperty("折扣额")
//		public BigDecimal discntAmt;// 折扣额(目前未用到)
		@JsonProperty("数量")
		public BigDecimal qty;// 数量

		@JsonProperty("箱数")
		public BigDecimal bxQty;// 箱数

		@JsonProperty("计划到货时间")
		public String planToGdsDt;// 计划到货时间

		@JsonProperty("计量单位编码")
		public String measrCorpId;// 计量单位编码
		@JsonProperty("计量单位名称")
		public String measrCorpNm;// 计量单位名称
//		@JsonProperty("是否赠品")
//		public Integer isComplimentary;// 是否赠品
		// 联查存货档案字段、计量单位名称
		@JsonProperty("规格型号")
		public String spcModel;// 规格型号
		@JsonProperty("存货代码")
		public String invtyCd;// 存货代码
		@JsonProperty("箱规")
		public BigDecimal bxRule;// 箱规

		@JsonProperty("最高进价")
		public BigDecimal highestPurPrice;// 最高进价
		@JsonProperty("最低售价")
		public BigDecimal loSellPrc;// 最低售价
		@JsonProperty("参考成本")
		public BigDecimal refCost;// 参考成本
		@JsonProperty("参考售价")
		public BigDecimal refSellPrc;// 参考售价
		@JsonProperty("最新成本")
		public BigDecimal ltstCost;// 最新成本
		@JsonProperty("对应条形码")
		public String crspdBarCd;// 对应条形码
		@JsonProperty("未到货数量")
		public BigDecimal unToGdsQty;// 未到货数量

		@JsonProperty("未申请付款数量")
		public BigDecimal unApplPayQty;// 未申请付款数量

		@JsonProperty("未申请付款金额")
		public BigDecimal unApplPayAmt;// 未申请付款金额

		@JsonProperty("保质期")
		public String baoZhiQi;// 保质期

		@JsonProperty("表体备注")
		public String memos;// 表体备注

	}

}
