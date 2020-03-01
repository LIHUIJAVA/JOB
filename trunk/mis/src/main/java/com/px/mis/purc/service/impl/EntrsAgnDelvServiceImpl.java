package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.purc.dao.EntrsAgnDelvDao;
import com.px.mis.purc.dao.EntrsAgnDelvSubDao;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.EntrsAgnDelvService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*委托代销发货单*/
@Transactional
@Service
public class EntrsAgnDelvServiceImpl extends poiTool implements EntrsAgnDelvService {
	@Autowired
	private EntrsAgnDelvDao eadd;
	@Autowired
	private EntrsAgnDelvSubDao eadsd;
	@Autowired
	private SellOutWhsDao sowd; // 销售出库主
	@Autowired
	private SellOutWhsSubDao sowds; // 销售出库子
	@Autowired
	private SellSnglDao ssd; // 销售单主
	@Autowired
	private InvtyTabDao itd;// 库存表
	@Autowired
	private LogisticsTabDao ltd; // 物流表
	@Autowired
	private IntoWhsDao intoWhsDao;// 采购入库单
	@Autowired
	private InvtyDocDao idd;// 存货档案
	@Autowired
	private InvtyDocDao invtyDocDao;
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;	

	// 新增委托代销发货单
	@Override
	public String addEntrsAgnDelv(String userId, EntrsAgnDelv entrsAgnDelv, List<EntrsAgnDelvSub> entrsAgnDelvSubList,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<EntrsAgnDelvSub> entrsAgnDelvSubSet = new TreeSet();
//			entrsAgnDelvSubSet.addAll(entrsAgnDelvSubList);
//			if(entrsAgnDelvSubSet.size() < entrsAgnDelvSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！",null);
//				return resp;
//			}
			// 获取订单号
			String number = getOrderNo.getSeqNo("WF", userId,loginTime);
			if (eadd.selectEntrsAgnDelvById(number) != null) {
				message = "编码" + number + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				entrsAgnDelv.setDelvSnglId(number);
				for (EntrsAgnDelvSub entrsAgnDelvSub : entrsAgnDelvSubList) {
					entrsAgnDelvSub.setDelvSnglId(entrsAgnDelv.getDelvSnglId());
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
					if (invtyDoc.getIsNtSell() == null) {
						isSuccess = false;
						message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
								+ "没有设置是否销售属性，无法保存！";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtSell().intValue() != 1) {
						isSuccess = false;
						message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()+ "非可销售存货，无法保存！";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
								+ "没有设置是否保质期管理属性，无法保存！";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (entrsAgnDelvSub.getPrdcDt() == "" || entrsAgnDelvSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
									+ "是保质期管理，请输入生产日期！";
							throw new RuntimeException(message);
						}
						if (entrsAgnDelvSub.getInvldtnDt() == ""|| entrsAgnDelvSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
									+ "是保质期管理，请输入失效日期！";
							throw new RuntimeException(message);
						}
					}
					if (entrsAgnDelvSub.getPrdcDt() == "") {
						entrsAgnDelvSub.setPrdcDt(null);
					}
					if (entrsAgnDelvSub.getInvldtnDt() == "") {
						entrsAgnDelvSub.setInvldtnDt(null);
					}
					entrsAgnDelvSub.setStlQty(new BigDecimal(0));// 结算数量
					entrsAgnDelvSub.setStlUprc(new BigDecimal(0));// 结算单价
					entrsAgnDelvSub.setStlAmt(new BigDecimal(0));// 结算金额
					entrsAgnDelvSub.setBllgQty(new BigDecimal(0));// 开票数量
					if (entrsAgnDelv.getIsNtRtnGood() == 0) {
						entrsAgnDelvSub.setUnBllgRtnGoodsQty(entrsAgnDelvSub.getQty());// 可退数量
					}

				}
				eadd.insertEntrsAgnDelv(entrsAgnDelv);
				eadsd.insertEntrsAgnDelvSub(entrsAgnDelvSubList);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", isSuccess, message, entrsAgnDelv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改委托代销发货单
	@Override
	public String editEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv, List<EntrsAgnDelvSub> entrsAgnDelvSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			for (EntrsAgnDelvSub entrsAgnDelvSub : entrsAgnDelvSubList) {
				entrsAgnDelvSub.setDelvSnglId(entrsAgnDelv.getDelvSnglId());
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
				if (invtyDoc.getIsNtSell() == null) {
					isSuccess = false;
					message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
							+ "没有设置是否销售属性，无法保存！";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtSell().intValue() != 1) {
					isSuccess = false;
					message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()+ "非可销售存货，无法保存！";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
							+ "没有设置是否保质期管理属性，无法保存！";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (entrsAgnDelvSub.getPrdcDt() == "" || entrsAgnDelvSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
								+ "是保质期管理，请输入生产日期！";
						throw new RuntimeException(message);
					}
					if (entrsAgnDelvSub.getInvldtnDt() == ""|| entrsAgnDelvSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "该委托代销单对应的存货:" + entrsAgnDelvSub.getInvtyEncd()
								+ "是保质期管理，请输入失效日期！";
						throw new RuntimeException(message);
					}
				}
				if (entrsAgnDelvSub.getPrdcDt() == "") {
					entrsAgnDelvSub.setPrdcDt(null);
				}
				if (entrsAgnDelvSub.getInvldtnDt() == "") {
					entrsAgnDelvSub.setInvldtnDt(null);
				}
				entrsAgnDelvSub.setStlQty(new BigDecimal(0));// 结算数量
				entrsAgnDelvSub.setStlUprc(new BigDecimal(0));// 结算单价
				entrsAgnDelvSub.setStlAmt(new BigDecimal(0));// 结算金额
				entrsAgnDelvSub.setBllgQty(new BigDecimal(0));// 开票数量
				if (entrsAgnDelv.getIsNtRtnGood() == 0) {
					entrsAgnDelvSub.setUnBllgRtnGoodsQty(entrsAgnDelvSub.getQty());// 可退数量
				}
			}
			eadsd.deleteEntrsAgnDelvSubByDelvSnglId(entrsAgnDelv.getDelvSnglId());
			eadd.updateEntrsAgnDelvByDelvSnglId(entrsAgnDelv);
			eadsd.insertEntrsAgnDelvSub(entrsAgnDelvSubList);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/editEntrsAgnDelv", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 单个删除委托代销发货单
	@Override
	public String deleteEntrsAgnDelv(String delvSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (eadd.selectEntrsAgnDelvByDelvSnglId(delvSnglId) != null) {
			eadd.deleteEntrsAgnDelvByDelvSnglId(delvSnglId);
			eadsd.deleteEntrsAgnDelvSubByDelvSnglId(delvSnglId);
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编码" + delvSnglId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/deleteEntrsAgnDelv", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 单个查询委托代销发货单
	@Override
	public String queryEntrsAgnDelv(String delvSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<EntrsAgnDelvSub> entrsAgnDelvSub = new ArrayList<>(); */
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(delvSnglId);
		if (entrsAgnDelv != null) {
			/* entrsAgnDelvSub=eadsd.selectEntrsAgnDelvSubByDelvSnglId(delvSnglId); */
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + delvSnglId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/queryEntrsAgnDelv", isSuccess, message, entrsAgnDelv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 查询所以委托代销发货单
	@Override
	public String queryEntrsAgnDelvList(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// 委托代销发货单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		
		List<EntrsAgnDelv> poList = eadd.selectEntrsAgnDelvList(map);
		int count = eadd.selectEntrsAgnDelvCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除委托代销发货单
	@Override
	public String deleteEntrsAgnDelvList(String delvSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(delvSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (eadd.selectEntrsAgnDelvIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteEntrsAgnDelvList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败!";
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
			resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/deleteEntrsAgnDelvList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	private int deleteEntrsAgnDelvList(List<String> lists2) {
		eadd.insertEntrsAgnDelvDl(lists2);
		eadsd.insertEntrsAgnDelvSubDl(lists2);
		int a = eadd.deleteEntrsAgnDelvList(lists2);
		return a;
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

	// 打印 输入输出委托代销发货单
	@Override
	public String printingEntrsAgnDelvList(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// 委托代销发货单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<zizhu> entrsAgnDelvList = eadd.printingEntrsAgnDelvList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/EntrsAgnDelv/printingEntrsAgnDelvList", true, "查询成功！", null,
					entrsAgnDelvList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 批量审核委托代销发货单和委托代销退货单
	@Override
	public Map<String, Object> updateEntrsAgnDelvIsNtChkList(String userId, EntrsAgnDelv entrsAgnDelvs,String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(entrsAgnDelvs.getDelvSnglId());
			if (entrsAgnDelv.getIsNtRtnGood() == 0) {
				if (entrsAgnDelvs.getIsNtChk() == 1) {
					// 委托代销发货单审核
					message.append(updateEntrsAgnDelvIsNtChkOK(userId, entrsAgnDelvs,loginTime).get("message"));
				} else if (entrsAgnDelvs.getIsNtChk() == 0) {
					// 委托代销发货单弃审
					message.append(updateEntrsAgnDelvIsNtChkNO(entrsAgnDelvs).get("message"));
				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			} else if (entrsAgnDelv.getIsNtRtnGood() == 1) {
				if (entrsAgnDelvs.getIsNtChk() == 1) {
					// 委托代销退货单审核
					message.append(updateReturnEntrsAgnDelvIsNtChkOK(userId, entrsAgnDelvs,loginTime).get("message"));
				} else if (entrsAgnDelvs.getIsNtChk() == 0) {
					// 委托代销退货单弃审
					message.append(updateReturnEntrsAgnDelvIsNtChkNO(entrsAgnDelvs).get("message"));
				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 委托代销发货单审核
	private Map<String, Object> updateEntrsAgnDelvIsNtChkOK(String userId, EntrsAgnDelv eAgnDelv,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 销售出库单子表集合
//		List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();
		// 物流表信息
		LogisticsTab logisticsTab = new LogisticsTab();
		BigDecimal goodNum = new BigDecimal(0);// 整单件数
		// 通过委托代销发货单编码获取委托代销发货单单主子表信息
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		// 委托代销发货单主表中有委托代销发货单子表信息，所以不需要另外查询子表信息
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();

		// 查询委托代销发货单的审核状态
		if (entrsAgnDelv.getIsNtChk() == 0) {
			Map<String,List<EntrsAgnDelvSub>> handleMap = entrsAgnDelvSubList.stream().collect(Collectors.groupingBy(EntrsAgnDelvSub::getWhsEncd));
			for(Map.Entry<String, List<EntrsAgnDelvSub>> entry : handleMap.entrySet()){
				SellOutWhs  sellOutWhs= new SellOutWhs();//销售出库单主表
				String number = getOrderNo.getSeqNo("WCK", userId,loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, entrsAgnDelv);
					//将委托代销主表复制给销售专用发票
					sellOutWhs.setOutWhsId(number);//出库单编码
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));//出库单日期
					sellOutWhs.setSellOrdrInd(entrsAgnDelv.getDelvSnglId());//销售出库单中对应的销售单编码
					sellOutWhs.setFormTypEncd("009");//单据类型编码
					sellOutWhs.setToFormTypEncd(entrsAgnDelv.getFormTypEncd());//来源单据类型
					// 通过业务类型判断收发类别
					if (entrsAgnDelv.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// 收发类型编号，线下销售
					}
					if (entrsAgnDelv.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// 收发类型编号，线上销售
					}
					sellOutWhs.setIsNtRtnGood(0);//是否退货
					sellOutWhs.setOutIntoWhsTypId("10");//出入库类型编码
					sellOutWhs.setSetupPers(eAgnDelv.getChkr());// 创建人,即销售单的审核人
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 创建时间=当前时间
					
					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();//销售出库单子表集合
					for(EntrsAgnDelvSub entrsAgnDelvSub:entry.getValue()) {
						if (entrsAgnDelvSub.getShdTaxLabour() == null || entrsAgnDelvSub.getIsNtDiscnt() == null) {
							isSuccess = false;
							message += "编号为：" + entrsAgnDelvSub.getDelvSnglId() + "的委托代销发货单中,存货编号：" + entrsAgnDelvSub.getInvtyEncd()
							+ "没有设置应税劳务属性或是否折扣属性，无法审核\n";
					       throw new RuntimeException(message);
						}
						if (entrsAgnDelvSub.getShdTaxLabour().intValue() == 1 || entrsAgnDelvSub.getIsNtDiscnt().intValue() == 1) {
							continue;
						} else {
							goodNum = goodNum.add(entrsAgnDelvSub.getQty());
							// 库存表实体
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(entrsAgnDelvSub.getWhsEncd());
							invtyTab.setInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
							invtyTab.setBatNum(entrsAgnDelvSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
							// 销售单审核时判断库存表中是否有该存货信息
							if (invtyTab != null) {
								// a.compareTo(b) -1表示小于 1 表示大于 0表示等于 比较销售单的数量和库存表的可用量
								if(invtyTab.getAvalQty().compareTo(entrsAgnDelvSub.getQty()) >= 0) {
									SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();//销售出库单子表
									BeanUtils.copyProperties(sellOutWhsSub, entrsAgnDelvSub);//将销售单子表复制给销售出库单
									sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//将销售出库单主表编码复制给销售出库单子表中主表编码
									// 如果没有生产日期和保质期时，失效日期默认为null
									if (entrsAgnDelvSub.getPrdcDt() == null || entrsAgnDelvSub.getBaoZhiQi() == null) {
										sellOutWhsSub.setInvldtnDt(null);
									} else {
										// 计算销售出库单的失效日期
										sellOutWhsSub.setPrdcDt(entrsAgnDelvSub.getPrdcDt());// 生产日期
										sellOutWhsSub.setBaoZhiQi(entrsAgnDelvSub.getBaoZhiQi());// 保质期
										sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
									}
									// 查询库存表中无税单价
									// 将获取的无税单价赋给销售出库的无税单价
									setSellOutWhsCB(invtyTab,entrsAgnDelvSub,sellOutWhsSub);
									sellOutWhsSub.setIsNtRtnGoods(0);// 是否退货
									sellOutWhsSub.setToOrdrNum(entrsAgnDelvSub.getOrdrNum());//来源单据子表序号
									sellOutWhsSubList.add(sellOutWhsSub);
									invtyTab.setAvalQty(entrsAgnDelvSub.getQty().abs());
									itd.updateInvtyTabAvalQtyJian(invtyTab);
								}else{
									isSuccess = false;
									message += "编号为：" + eAgnDelv.getDelvSnglId()+ "的委托代销发货单中,存货编号：" + entrsAgnDelvSub.getInvtyEncd()
											+ ",批次:" + entrsAgnDelvSub.getBatNum() + "库存中数量不足，无法审核\n";
									throw new RuntimeException(message);
								}
						}else {
							isSuccess = false;
							message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单中,存货编码："
									+ entrsAgnDelvSub.getInvtyEncd() + ",批次：" + entrsAgnDelvSub.getBatNum() + "的库存不存在，无法审核\n";
							throw new RuntimeException(message);
						}
					}
			      }
				if(sellOutWhsSubList.size() > 0){
					// 审核后新增物流表信息
					PlatOrder platOrder = new PlatOrder();
					logisticsTab.setSaleEncd(entrsAgnDelv.getDelvSnglId());// 委托代销单号
//					logisticsTab.setOrderId(entrsAgnDelv.getTxId());// 订单编码 去销售单中的交易编码
					logisticsTab.setOutWhsId(sellOutWhs.getOutWhsId());// 销售出库单号
//					logisticsTab.setEcOrderId(entrsAgnDelv.getEcOrderId());// 电商订单号
					logisticsTab.setIsBackPlatform(0);
					logisticsTab.setIsShip(0);
					logisticsTab.setIsPick(0);
					// 整单商品件数
//					logisticsTab.setBuyerNote(entrsAgnDelv.getBuyerNote());// 买家留言
					logisticsTab.setRecAddress(entrsAgnDelv.getRecvrAddr());// 收货人详细地址
					logisticsTab.setRecName(entrsAgnDelv.getRecvr());// 收件人姓名
					logisticsTab.setRecMobile(entrsAgnDelv.getRecvrTel());// 收件人手机号
					logisticsTab.setBizTypId(entrsAgnDelv.getBizTypId());// 业务类型编码
					logisticsTab.setSellTypId(entrsAgnDelv.getSellTypId());// 销售类型编码
//					logisticsTab.setDeliverWhs(entrsAgnDelv.getDelvAddr());// 发货仓库
					logisticsTab.setRecvSendCateId(entrsAgnDelv.getRecvSendCateId());// 收发类别编码
					
					logisticsTab.setGoodNum(goodNum);
					goodNum=new BigDecimal(0);
					ltd.insert(logisticsTab);
					int a = sowd.insertSellOutWhs(sellOutWhs);
					sowds.insertSellOutWhsSub(sellOutWhsSubList);
					sellOutWhsSubList.clear();
				}
			    }catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int a = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
			if (a==1) {
				isSuccess = true;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单审核成功\n";
			}else {
				isSuccess = false;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单已审核，不可重复审核\n";
				throw new RuntimeException(message);
			}
			
		} else {
			isSuccess = false;
			message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单已审核，不可重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	private void setSellOutWhsCB(InvtyTab invtyTab,EntrsAgnDelvSub eAgnDelvSub,SellOutWhsSub sowSub) {
		Map<String, Object> map = new HashMap<>();
		// 查询库存表中无税单价 将获取的无税单价赋给销售出库的无税单价
		if (invtyTab.getUnTaxUprc() == null) {
			map.put("invtyEncd", sowSub.getInvtyEncd());
			map.put("whsEncd", sowSub.getWhsEncd());
			map.put("batNum", sowSub.getBatNum());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// 采购入库单最近一次入库单价
			if (pursunTaxUprc == null) {
				BigDecimal noTaxUprc = idd.selectRefCost(sowSub.getInvtyEncd());// 取存货档案中最近一次参考成本
				// 将获取的无税单价赋给委托代销销售出库的无税单价
				sowSub.setNoTaxUprc(noTaxUprc);
				sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// 税率
				// 计算未税金额 金额=未税数量*未税单价
				sowSub.setNoTaxAmt(
						CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sowSub.setTaxAmt(CalcAmt
						.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
						.divide(new BigDecimal(100)));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				
			} else {
				sowSub.setNoTaxUprc(pursunTaxUprc);
				sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// 税率
				// 计算未税金额 金额=未税数量*未税单价
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sowSub.setTaxAmt(CalcAmt
						.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
						.divide(new BigDecimal(100)));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(),
						sowSub.getQty(), sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			}
		} else {
			sowSub.setNoTaxUprc(invtyTab.getUnTaxUprc());
			sowSub.setTaxRate((eAgnDelvSub.getTaxRate()));// 税率
			// 计算未税金额 金额=未税数量*未税单价
			sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
			// 计算税额 税额=未税金额*税率
			sowSub.setTaxAmt(
					CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(), sowSub.getTaxRate())
							.divide(new BigDecimal(100)));
			// 计算含税单价 含税单价=无税单价*税率+无税单价
			sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
					sowSub.getTaxRate().divide(new BigDecimal(100))));
			// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
			sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
					sowSub.getTaxRate().divide(new BigDecimal(100))));
		}
	}
	
	// 委托代销发货单弃审
	private Map<String, Object> updateEntrsAgnDelvIsNtChkNO(EntrsAgnDelv eAgnDelv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 通过委托代销发货单编码获取委托代销发货单单主子表信息
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		
		// 首先判断销售单的审核状态，审核状态为1时才可以执行弃操作审
		if (entrsAgnDelv.getIsNtChk() == 1) {
			// 根据销售标识查询销售出库单的审核状态 和 是否生成拣货单
			if (entrsAgnDelv.getIsPick() == 0) {
				List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(eAgnDelv.getDelvSnglId());
				int isNtChkQty=0;
				for(SellOutWhs sellOutWhs:sellOutWhsList){
					if(sellOutWhs.getIsNtChk()==1){
						isNtChkQty++;
					}
				}
				if (isNtChkQty > 0) {
					isSuccess = false;
					message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单对应的在下游单据【销售出库单】已审核，无法弃审\n";
					throw new RuntimeException(message);
//					
				} else {
					// 委托代销发货单主表中有委托代销发货单子表信息，所以不需要另外查询子表信息
					List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
					for (EntrsAgnDelvSub enAgnDeSubs : entrsAgnDelvSubList) {
						// 库存表实体
						InvtyTab invtyTab = new InvtyTab();
						invtyTab.setWhsEncd(enAgnDeSubs.getWhsEncd());
						invtyTab.setInvtyEncd(enAgnDeSubs.getInvtyEncd());
						invtyTab.setBatNum(enAgnDeSubs.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						if (invtyTab != null) {
							invtyTab.setAvalQty(enAgnDeSubs.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else {
							isSuccess = false;
							message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单没有库存，无法弃审\n";
							throw new RuntimeException(message);
						}
					}
					int updateFlag = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
					if (updateFlag>=1) {
						isSuccess = true;
						message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单弃审成功!\n";
					}else {
						isSuccess = false;
						message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单已弃审,不需重复弃审!\n";
						throw new RuntimeException(message);
					}
					sowd.deleteSellOutWhsBySellOrdrInd(eAgnDelv.getDelvSnglId());// 删除销售出库单
					ssd.deleteLogisticsTab(eAgnDelv.getDelvSnglId());// 删除物流表
				}
			} else {
				isSuccess = false;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单已生成对应的拣货单，需先弃审拣货单\n";
				throw new RuntimeException(message);
			}
		} else {
			message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单已弃审，不可重复弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 审核委托代销退货单
	private Map<String, Object> updateReturnEntrsAgnDelvIsNtChkOK(String userId, EntrsAgnDelv eAgnDelv,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 通过委托代销发货单编码获取委托代销发货单单主子表信息
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		// 委托代销发货单主表中有委托代销发货单子表信息，所以不需要另外查询子表信息
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
		if (entrsAgnDelv.getIsNtChk() == 0) {
			Map<String,List<EntrsAgnDelvSub>> handleMap = entrsAgnDelvSubList.stream().collect(Collectors.groupingBy(EntrsAgnDelvSub::getWhsEncd));
			for(Map.Entry<String, List<EntrsAgnDelvSub>> entry : handleMap.entrySet()){
				SellOutWhs  sellOutWhs= new SellOutWhs();//销售出库单主表
				String number = getOrderNo.getSeqNo("WCK", userId,loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, entrsAgnDelv);
					//将委托代销主表复制给销售专用发票
					sellOutWhs.setOutWhsId(number);//出库单编码
					sellOutWhs.setOutWhsDt(loginTime);//出库单日期
					sellOutWhs.setSellOrdrInd(entrsAgnDelv.getDelvSnglId());//销售出库单中对应的销售单编码
					sellOutWhs.setFormTypEncd("010");//单据类型编码
					sellOutWhs.setToFormTypEncd(entrsAgnDelv.getFormTypEncd());//来源单据类型
					// 通过业务类型判断收发类别
					if (entrsAgnDelv.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// 收发类型编号，线下销售
					}
					if (entrsAgnDelv.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// 收发类型编号，线上销售
					}
					sellOutWhs.setIsNtRtnGood(1);//是否退货
					sellOutWhs.setOutIntoWhsTypId("10");//出入库类型编码
					sellOutWhs.setSetupPers(eAgnDelv.getChkr());// 创建人,即销售单的审核人
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 创建时间=当前时间
					
					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();//销售出库单子表集合
					for(EntrsAgnDelvSub entrsAgnDelvSub:entry.getValue()) {
						BigDecimal qtyList = eadsd.selectEntrsAgnDelvSubQty(entrsAgnDelvSub);
						if (qtyList == null) {
							isSuccess = false;
							message += "编码为：" + eAgnDelv.getDelvSnglId() + "的退货单中数量为空，无法审核\n";
							throw new RuntimeException(message);
						} else if (qtyList.intValue() == 0) {
							continue;
						} else {
							// 库存表实体
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(entrsAgnDelvSub.getWhsEncd());
							invtyTab.setInvtyEncd(entrsAgnDelvSub.getInvtyEncd());
							invtyTab.setBatNum(entrsAgnDelvSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
							SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();//销售出库单子表
							BeanUtils.copyProperties(sellOutWhsSub, entrsAgnDelvSub);//将销售单子表复制给销售出库单
							sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//将销售出库单主表编码复制给销售出库单子表中主表编码
							// 如果没有生产日期和保质期时，失效日期默认为null
							if (entrsAgnDelvSub.getPrdcDt() == null || entrsAgnDelvSub.getBaoZhiQi() == null) {
								sellOutWhsSub.setInvldtnDt(null);
							} else {
								// 计算销售出库单的失效日期
								sellOutWhsSub.setPrdcDt(entrsAgnDelvSub.getPrdcDt());// 生产日期
								sellOutWhsSub.setBaoZhiQi(entrsAgnDelvSub.getBaoZhiQi());// 保质期
								sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
							}
							// 查询库存表中无税单价
							// 将获取的无税单价赋给销售出库的无税单价
							setRtnSellOutWhsCB(invtyTab,entrsAgnDelv,entrsAgnDelvSub,sellOutWhsSub);
							sellOutWhsSub.setIsNtRtnGoods(1);// 是否退货
							sellOutWhsSub.setToOrdrNum(entrsAgnDelvSub.getOrdrNum());//来源单据子表序号
							sellOutWhsSubList.add(sellOutWhsSub);
							// 销售单审核时判断库存表中是否有该存货信息
							if (invtyTab != null) {
								invtyTab.setAvalQty(entrsAgnDelvSub.getQty().abs());
								// 在库存表中将对应的可用量增加
								itd.updateInvtyTabAvalQtyJia(invtyTab);
							} else {
								// 库存中没有该存货时，需要新增库存信息，传入的是退货单实体，因此需要将退货单数量设置成正数并传给库存表
								entrsAgnDelvSub.setQty(entrsAgnDelvSub.getQty().abs());
								itd.insertInvtyTabToEnAgDelSub(entrsAgnDelvSub);
							}
					}
					if (entrsAgnDelvSub.getToOrdrNum() != null && entrsAgnDelvSub.getToOrdrNum() != 0) {
						EntrsAgnDelvSub eAgnDelvSubs = eadsd.selectEntDeSubToOrdrNum(entrsAgnDelvSub.getToOrdrNum());// 根据退货单子表序号查询销售单中可退数量							BigDecimal rtnblQty = eAgnDelvSubs.getUnBllgRtnGoodsQty();
						BigDecimal rtnblQty = eAgnDelvSubs.getUnBllgRtnGoodsQty();
						if (rtnblQty != null) {
							if (rtnblQty.compareTo(entrsAgnDelvSub.getQty().abs()) >0) {
								map.put("unBllgRtnGoodsQty", entrsAgnDelvSub.getQty().abs());// 修改可退数量
								eadsd.updateEntDeSubUnBllgRtnGoodsQty(map);// 根据退货单子表序号修改销售单中可退数量
							} else {
								isSuccess = false;
								message += "单据号为：" + eAgnDelv.getDelvSnglId() + "的退货单单中存货【"
										+ entrsAgnDelvSub.getInvtyEncd() + "】累计退货数量大于可退数量，无法审核！\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "单据号为：" + eAgnDelv.getDelvSnglId() + "的退货单对应的采购订单中可退数量不存在，无法审核！\n";
							throw new RuntimeException(message);
						}
					}
			     }
				if (sellOutWhsSubList.size() > 0) {
					sowd.insertSellOutWhs(sellOutWhs);
					sowds.insertSellOutWhsSub(sellOutWhsSubList);
					sellOutWhsSubList.clear();
				} 
			    }catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int b = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
			if (b >= 1) {
				isSuccess = true;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单审核成功！\n";
			} else {
			    isSuccess = false;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单审核失败！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单已审核，不可重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setRtnSellOutWhsCB( InvtyTab invtyTab,EntrsAgnDelv eAgnDelv,EntrsAgnDelvSub eAgnDelvSub,
			SellOutWhsSub sowSub) {
		Map<String, Object> map = new HashMap<>();
		if (eAgnDelvSub.getToOrdrNum() != null) {
			map.put("toOrdrNum", eAgnDelvSub.getToOrdrNum());
			map.put("sellSnglId", eAgnDelv.getDelvSnglId());//委托代销发货单号
			//原销售单对应的出库单子表序号
			BigDecimal noTaxUprc1 = sowd.selectSellOutWhsSubByToOrdrNum(map);
			if (noTaxUprc1 != null) {
				sowSub.setNoTaxUprc(noTaxUprc1);
				// 计算未税金额 金额=未税数量*未税单价
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				throw new RuntimeException("编号为" + eAgnDelv.getDelvSnglId() + "的委托代销退货单部分对应的发货单不存在，请查明原因再审核\n");
			}
		} else {
			map.put("invtyEncd", eAgnDelvSub.getInvtyEncd());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// 采购入库单最近一次入库单价
			if (pursunTaxUprc != null) {
				// 将获取的无税单价赋给委托代销销售出库的无税单价
				sowSub.setNoTaxUprc(pursunTaxUprc);
				// 计算未税金额 金额=未税数量*未税单价
				sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
						sowSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				// 查询存货档案中的参考成本
				BigDecimal noTaxUprc2 = idd.selectRefCost(eAgnDelvSub.getInvtyEncd());
				if (noTaxUprc2 != null) {
					// 将获取的无税单价赋给委托代销销售出库的无税单价
					sowSub.setNoTaxUprc(noTaxUprc2);
					// 计算未税金额 金额=未税数量*未税单价
					sowSub.setNoTaxAmt(CalcAmt.noTaxAmt(sowSub.getNoTaxUprc(), sowSub.getQty()));
					// 计算税额 税额=未税金额*税率
					sowSub.setTaxAmt(CalcAmt.taxAmt(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
					// 计算含税单价 含税单价=无税单价*税率+无税单价
					sowSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
					// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
					sowSub.setPrcTaxSum(CalcAmt.prcTaxSum(sowSub.getNoTaxUprc(), sowSub.getQty(),
							sowSub.getTaxRate().divide(new BigDecimal(100))));
				} else {
					throw new RuntimeException(
							"编码为" + eAgnDelv.getDelvSnglId() + "的委托代销退货单无法生成对应红字销售出库单，审核失败！请查明原因再审核\n");
				}
			}
		}
	}

	// 弃审委托代销退货单
	private Map<String, Object> updateReturnEntrsAgnDelvIsNtChkNO(EntrsAgnDelv eAgnDelv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 通过委托代销发货单编码获取委托代销发货单单主子表信息
		EntrsAgnDelv entrsAgnDelv = eadd.selectEntrsAgnDelvByDelvSnglId(eAgnDelv.getDelvSnglId());
		
		// 首先判断销售单的审核状态，审核状态为1时才可以执行弃操作审
		if (entrsAgnDelv.getIsNtChk() == 1) {
			// 根据委托代销发货单查询对应的销售出库单信息
			List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(eAgnDelv.getDelvSnglId());
			int isNtChkQty=0;
			for(SellOutWhs sellOutWhs:sellOutWhsList){
				if(sellOutWhs.getIsNtChk()==1){
					isNtChkQty++;
				}
			}
			if (isNtChkQty > 0) {
				isSuccess = false;
				message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销发货单对应的在下游单据【销售出库单】已审核，无法弃审\n";
				throw new RuntimeException(message);
			} else {
				// 委托代销发货单主表中有委托代销发货单子表信息，所以不需要另外查询子表信息
				List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();
				for (EntrsAgnDelvSub enAgnDeSubs : entrsAgnDelvSubList) {
					// 库存表实体
					InvtyTab invtyTab = new InvtyTab();
					invtyTab.setWhsEncd(enAgnDeSubs.getWhsEncd());
					invtyTab.setInvtyEncd(enAgnDeSubs.getInvtyEncd());
					invtyTab.setBatNum(enAgnDeSubs.getBatNum());
					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
					if (invtyTab != null) {
						if (invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == 1
								|| invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == 0) {
							invtyTab.setAvalQty(enAgnDeSubs.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else if (invtyTab.getAvalQty().compareTo(enAgnDeSubs.getQty().abs()) == -1) {
							isSuccess = false;
							message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单中存货编码：" + enAgnDeSubs.getInvtyEncd()
									+ ",批次：" + enAgnDeSubs.getBatNum() + "的库存不足，无法弃审\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单中,存货编码：" + enAgnDeSubs.getInvtyEncd()
								+ ",批次：" + enAgnDeSubs.getBatNum() + "的库存不存在，无法弃审\n";
						throw new RuntimeException(message);
					}
				}
				int a = eadd.updateEntrsAgnDelvIsNtChk(eAgnDelv);
				sowd.deleteSellOutWhsBySellOrdrInd(eAgnDelv.getDelvSnglId());// 删除销售出库单
				if (a >= 1) {
					isSuccess = true;
					message += "编码为" + eAgnDelv.getDelvSnglId() + "的委托代销退货单弃审成功！\n";
				} else {
					isSuccess = false;
					message += "编码为" + eAgnDelv.getDelvSnglId() + "的委托代销退货单弃审失败！\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "编码为：" + eAgnDelv.getDelvSnglId() + "的委托代销退货单未审核，请先审核该单据\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 导入委托代销发货单
	@Override
	public String uploadFileAddDb(MultipartFile file) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, EntrsAgnDelv> entrsAgnDelvMap = uploadScoreInfo(file);

		for (Map.Entry<String, EntrsAgnDelv> entry : entrsAgnDelvMap.entrySet()) {
			if (eadd.selectEntrsAgnDelvById(entry.getValue().getDelvSnglId()) != null) {
				isSuccess = false;
				message = "委托代销发货单中部分订单编码已存在，无法导入，请查明再导入！";
				throw new RuntimeException(message);
			} else {
				eadd.insertEntrsAgnDelvUpload(entry.getValue());
				for (EntrsAgnDelvSub entrsAgnDelvSub : entry.getValue().getEntrsAgnDelvSub()) {
					entrsAgnDelvSub.setDelvSnglId(entry.getValue().getDelvSnglId());
				}
				eadsd.insertEntrsAgnDelvSub(entry.getValue().getEntrsAgnDelvSub());
			}
			isSuccess = true;
			message = "委托代销发货单导入成功！";
		}

		try {
			resp = BaseJson.returnRespObj("/purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, EntrsAgnDelv> uploadScoreInfo(MultipartFile file) {
		Map<String, EntrsAgnDelv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "委托代销发货单编码");
				// 创建实体类
				EntrsAgnDelv entrsAgnDelv = new EntrsAgnDelv();
				if (temp.containsKey(orderNo)) {
					entrsAgnDelv = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				entrsAgnDelv.setSellTypId(GetCellData(r, "销售类型编码"));// 销售类型编码
				entrsAgnDelv.setDelvSnglId(orderNo); // 委托代销发货单编码
				if (GetCellData(r, "委托代销发货单日期") == null || GetCellData(r, "委托代销发货单日期").equals("")) {
					entrsAgnDelv.setDelvSnglDt(null);
				} else {
					entrsAgnDelv.setDelvSnglDt(GetCellData(r, "委托代销发货单日期").replaceAll("[^0-9:-]", " "));// 销售出库单日期
				}
				entrsAgnDelv.setCustId(GetCellData(r, "客户编码"));// 客户编码
				entrsAgnDelv.setCustOrdrNum(GetCellData(r, "客户订单号"));// 客户订单号
				entrsAgnDelv.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//				entrsAgnDelv.setDepName(GetCellData(r, "部门名称"));//部门名称
				entrsAgnDelv.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				entrsAgnDelv.setUserName(GetCellData(r, "业务员名称"));// 用户名称
				entrsAgnDelv.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型
				entrsAgnDelv.setBizTypId(GetCellData(r, "业务类型编码"));// 业务类别编码
//				entrsAgnDelv.setRecvSendCateId(GetCellData(r, "用户编码"));//收发类别编码
				entrsAgnDelv.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					entrsAgnDelv.setSetupTm(null);
				} else {
					entrsAgnDelv.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				entrsAgnDelv.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				entrsAgnDelv.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					entrsAgnDelv.setChkDt(null);
				} else {
					entrsAgnDelv.setChkDt(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				entrsAgnDelv.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					entrsAgnDelv.setModiTm(null);
				} else {
					entrsAgnDelv.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				entrsAgnDelv.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue());// 是否记账
				entrsAgnDelv.setBookEntryPers(GetCellData(r, "记账人"));// 记账人
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					entrsAgnDelv.setBookEntryTm(null);
				} else {
					entrsAgnDelv.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				entrsAgnDelv.setMemo(GetCellData(r, "表头备注"));// 主备注
				entrsAgnDelv.setIsNtRtnGood(new Double(GetCellData(r, "退货标识")).intValue());// 是否退货
				// entrsAgnDelv.setIsPick(new Double(GetCellData(r,"是否拣货")).intValue());//是否拣货
				entrsAgnDelv.setIsNtStl(new Double(GetCellData(r, "是否结算")).intValue());// 是否结算
				entrsAgnDelv.setRecvr(GetCellData(r, "收件人"));// 收件人
				entrsAgnDelv.setRecvrTel(GetCellData(r, "收件人电话"));// 收件人电话
				entrsAgnDelv.setRecvrAddr(GetCellData(r, "收件人地址"));// 收件人地址
				entrsAgnDelv.setRecvrEml(GetCellData(r, "收件人邮箱"));// 收件人邮箱
				entrsAgnDelv.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue());

				List<EntrsAgnDelvSub> entrsAgnDelvSubList = entrsAgnDelv.getEntrsAgnDelvSub();// 委托代销发货单子表
				if (entrsAgnDelvSubList == null) {
					entrsAgnDelvSubList = new ArrayList<>();
				}
				EntrsAgnDelvSub entrsAgnDelvSub = new EntrsAgnDelvSub();
				entrsAgnDelvSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//				entrsAgnDelvSub.setWhsNm(GetCellData(r, "仓库名称"));//仓库名称
				entrsAgnDelvSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				entrsAgnDelvSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				entrsAgnDelvSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8).abs());// 8表示小数位数 //未开票数量
				entrsAgnDelvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // 含税单价
				entrsAgnDelvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // 无税单价
				entrsAgnDelvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // 无税金额
				entrsAgnDelvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // 税额
				entrsAgnDelvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // 价税合计
				entrsAgnDelvSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // 税率
				entrsAgnDelvSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // 箱数
				entrsAgnDelvSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8)); // 箱规
				entrsAgnDelvSub.setBatNum(GetCellData(r, "批次"));// 批次
				entrsAgnDelvSub.setIntlBat(GetCellData(r, "国际批次"));// 国际批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					entrsAgnDelvSub.setPrdcDt(null);
				} else {
					entrsAgnDelvSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				entrsAgnDelvSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue());// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					entrsAgnDelvSub.setInvldtnDt(null);
				} else {
					entrsAgnDelvSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				entrsAgnDelvSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				entrsAgnDelvSub.setProjNm(GetCellData(r, "项目名称"));// 项目名称
//				entrsAgnDelvSub.setDiscntRatio(GetCellData(r, "折扣比例"));//折扣比例
				entrsAgnDelvSub.setStlQty(GetBigDecimal(GetCellData(r, "结算数量"), 8));// 8表示小数位数 //结算数量
				entrsAgnDelvSub.setStlUprc(GetBigDecimal(GetCellData(r, "结算单价"), 8));// 8表示小数位数 //结算单价
				entrsAgnDelvSub.setStlAmt(GetBigDecimal(GetCellData(r, "结算金额"), 8));// 8表示小数位数 //结算金额

				entrsAgnDelvSub.setUnBllgRtnGoodsQty(GetBigDecimal(GetCellData(r, "可退数量"), 8)); // 可退数量
				entrsAgnDelvSub.setBllgQty(GetBigDecimal(GetCellData(r, "开票数量"), 8));// 8表示小数位数 //开票数量
//				entrsAgnDelvSub.setFinalStlQty(GetBigDecimal(GetCellData(r, "最后结算数量"), 8));//8表示小数位数 //最后结算数量
//				entrsAgnDelvSub.setFinalStlAmt(GetBigDecimal(GetCellData(r, "最后结算金额"), 8));//8表示小数位数 //最后结算金额
//				entrsAgnDelvSub.setAccmStlAmt(GetBigDecimal(GetCellData(r, "累计结算金额"), 8));//8表示小数位数 //累计结算金额
//				entrsAgnDelvSub.setAccmOutWhsQty(GetBigDecimal(GetCellData(r, "累计出库数量"), 8));//8表示小数位数 //累计出库数量
				entrsAgnDelvSub.setMemo(GetCellData(r, "表体备注"));// 子备注
				entrsAgnDelvSub.setIsComplimentary(new Double(GetCellData(r, "赠品")).intValue());// 是否赠品
//				entrsAgnDelvSub.setOrdrNum(Long.parseLong(GetCellData(r, "发货单子表id")));

				entrsAgnDelvSubList.add(entrsAgnDelvSub);
				entrsAgnDelv.setEntrsAgnDelvSub(entrsAgnDelvSubList);
				temp.put(orderNo, entrsAgnDelv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 参照时查询所以委托代销发货单
	@Override
	public String queryEntrsAgnDelvLists(Map map) {
		String resp = "";
		List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// 委托代销发货单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("delvSnglIdList", delvSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<EntrsAgnDelv> poList = eadd.selectEntrsAgnDelvLists(map);
		int count = eadd.selectEntrsAgnDelvCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvLists", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 委托代销退货单参照时根据委托代销发货单子表信息
	@Override
	public String selectEntDeSubUnBllgRtnGoodsQty(String delvSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(delvSnglId);
		List<EntrsAgnDelvSub> entrsAgnDelvSubList = eadsd.selectEntDeSubUnBllgRtnGoodsQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/EntrsAgnDelv/selectEntDeSubUnBllgRtnGoodsQty", true, "查询成功！", null,
					entrsAgnDelvSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		// TODO Auto-generated method stub
		return null;
	}
	// 分页+排序
		@Override
		public String queryEntrsAgnDelvListOrderBy(Map map) {
			String resp = "";
			List<String> delvSnglIdList = getList((String) map.get("delvSnglId"));// 委托代销发货单号
			List<String> custIdList = getList((String) map.get("custId"));// 客户编码
			List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
			List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
			List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
			List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
			List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
			List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
			List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
			List<String> batNumList = getList((String) map.get("batNum"));// 批次

			map.put("delvSnglIdList", delvSnglIdList);
			map.put("custIdList", custIdList);
			map.put("accNumList", accNumList);
			map.put("invtyClsEncdList", invtyClsEncdList);
			map.put("invtyCdList", invtyCdList);
			map.put("deptIdList", deptIdList);
			map.put("whsEncdList", whsEncdList);
			map.put("custOrdrNumList", custOrdrNumList);
			map.put("invtyEncdList", invtyEncdList);
			map.put("batNumList", batNumList);
			List<zizhu> poList;
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","ead.delv_sngl_dt");
				map.put("sortOrder","desc");
			}
				poList =eadd.selectEntrsAgnDelvListOrderBy(map);
				Map tableSums = eadd.selectEntrsAgnDelvListSums(map);
				if (null!=tableSums) {
					DecimalFormat df = new DecimalFormat("#,##0.00");
					for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
						String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
						entry.setValue(s);
					}
				}
				
			int count = eadd.selectEntrsAgnDelvCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/EntrsAgnDelv/queryEntrsAgnDelvListOrderBy", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
		
		public static class zizhu{
			@JsonProperty("发货单号")
			public String delvSnglId;//发货单编号
			@JsonProperty("发货日期")
			public String delvSnglDt;//发货单日期
			@JsonProperty("单据类型编码")
			public String formTypEncd;//单据类型编码
			@JsonProperty("销售类型编号")
			public String sellTypId;//销售类型编号
			@JsonProperty("业务类型编号")
			public String bizTypId;//业务类型编号
			@JsonProperty("收发类型编号")
			public String recvSendCateId;//收发类型编号
			@JsonProperty("客户编号")
			public String custId;//客户编号
			@JsonProperty("用户编号")
			public String accNum;//用户编号
			@JsonProperty("用户名称")
			public String userName;//用户名称
			@JsonProperty("部门编号")
			public String deptId;//部门编号
			@JsonProperty("部门名称")
			public String deptName;//部门名称
			@JsonProperty("创建人")
			public String setupPers;//创建人
			@JsonProperty("创建时间")
			public String setupTm;//创建时间
			@JsonProperty("是否记账")
			public Integer isNtBookEntry;//是否记账
			@JsonProperty("记账人")
			public String bookEntryPers;//记账人
			@JsonProperty("记账时间")
			public String bookEntryTm;//记账时间
			@JsonProperty("是否审核")
			public Integer isNtChk;//是否审核
			@JsonProperty("审核人")
			public String chkr;//审核人
			@JsonProperty("审核时间")
			public String chkDt;//审核时间
			@JsonProperty("修改人")
			public String mdfr;//修改人
			@JsonProperty("修改时间")
			public String modiTm;//修改时间
			@JsonProperty("发货地址编号")
			public String delvAddrId;//发货地址编号
			@JsonProperty("发货地址名称")
			public String delvAddrNm;//发货地址名称
			@JsonProperty("销售订单号")
			public String sellOrdrId;//销售订单号
			@JsonProperty("销售发票号")
			public String sellInvId;//销售发票号
			@JsonProperty("销售发票主表标识")
			public String sellInvMasTabInd;//销售发票主表标识
			@JsonProperty("是否销售 ")
			public Integer isNtSellUse;//是否销售 
			@JsonProperty("是否出库开票")
			public Integer isNtOutWhsBllg;//是否出库开票
			@JsonProperty("是否需要开票")
			public Integer isNtNeedBllg;//是否需要开票
			@JsonProperty("是否结算")
			public Integer isNtStl;//是否结算
			@JsonProperty("收件人")
			public String recvr;//收件人
			@JsonProperty("收件电话")
			public String recvrTel;//收件电话
			@JsonProperty("收件人地址")
			public String recvrAddr;//收件人地址
			@JsonProperty("收件人邮箱")
			public String recvrEml;//收件人邮箱
			@JsonProperty("是否拣货 ")
			public Integer isPick;//是否拣货 
			@JsonProperty("是否退货(主表)")
			public Integer isNtRtnGood;//是否退货
			@JsonProperty("客户订单号")
		    public String custOrdrNum;//客户订单号
			@JsonProperty("表体备注")
			public String memos;//备注
			@JsonProperty("表头备注")
			public String memo;//备注

		    //关联查询客户名称、用户名称、部门名称、销售类型名称、收发类别名称、业务类型名称
			@JsonProperty("客户名称")
		    public String custNm;//客户名称
			@JsonProperty("销售类型名称")
		    public String sellTypNm;//销售类型名称
			@JsonProperty("收发类别名称")
		    public String recvSendCateNm;//收发类别名称
			@JsonProperty("业务类型名称")
		    public String bizTypNm;//业务类型名称
			@JsonProperty("单据类型名称")
		    public String formTypName;//单据类型名称
			@JsonProperty("是否生成凭证")
		    public Integer isNtMakeVouch;//是否生成凭证
			@JsonProperty("制凭证人")
		    public String makVouchPers;//制凭证人
			@JsonProperty("制凭证时间")
		    public String makVouchTm;//制凭证时间
			@JsonProperty("序号")
		    public Long ordrNum;//序号
			@JsonProperty("仓库编码")
		    public String whsEncd;//仓库编码
			@JsonProperty("存货编码")
		    public String invtyEncd;//存货编码
			@JsonProperty("数量")
		    public BigDecimal qty;//数量
			@JsonProperty("报价")
		    public BigDecimal quotn;//报价
			@JsonProperty("无税单价")
		    public BigDecimal noTaxUprc;//无税单价
			@JsonProperty("无税金额")
		    public BigDecimal noTaxAmt;//无税金额
			@JsonProperty("税率")
		    public BigDecimal taxRate;//税率
			@JsonProperty("税额")
		    public BigDecimal taxAmt;//税额
			@JsonProperty("含税单价")
		    public BigDecimal cntnTaxUprc;//含税单价
			@JsonProperty("价税合计")
		    public BigDecimal prcTaxSum;//价税合计
			@JsonProperty("未开票退货数量")
		    public BigDecimal unBllgRtnGoodsQty;//可退数量
			@JsonProperty("开票数量")
		    public BigDecimal bllgQty;//开票数量
			@JsonProperty("退货标识")
		    public Integer rtnGoodsInd;//退货标识
			@JsonProperty("结算单价")
		    public BigDecimal stlUprc;//结算单价
			@JsonProperty("结算数量")
		    public BigDecimal stlQty;//结算数量
			@JsonProperty("结算金额")
		    public BigDecimal stlAmt;//结算金额
			@JsonProperty("累计结算金额")
		    public BigDecimal accmStlAmt;//累计结算金额
			@JsonProperty("最后结算数量")
		    public BigDecimal finalStlQty;//最后结算数量
			@JsonProperty("最后结算金额")
		    public BigDecimal finalStlAmt;//最后结算金额
			@JsonProperty("累计出库数量")
		    public BigDecimal accmOutWhsQty;//累计出库数量
			@JsonProperty("生产日期")
		    public String prdcDt;//生产日期
			@JsonProperty("保质期")
		    public Integer baoZhiQi;//保质期
			@JsonProperty("失效日期")
		    public String invldtnDt;//失效日期
			@JsonProperty("批号")
		    public String batNum;//批号
			@JsonProperty("国际批次")
		    public String intlBat;//国际批次
			@JsonProperty("是否赠品")
		    public Integer isComplimentary;//是否赠品
			@JsonProperty("是否退货(子表)")
		    public Integer isNtRtnGoods;//是否退货
			@JsonProperty("箱数")
		    public BigDecimal bxQty;//箱数
			@JsonProperty("项目编号")
		    public String projEncd;//项目编号
			@JsonProperty("项目名称")
		    public String projNm;//项目名称
			@JsonProperty("折扣比例")
		    public String discntRatio;//折扣比例
			@JsonProperty("存货名称")
		    //联查存货档案字段、计量单位名称、仓库名称
		    public String invtyNm;//存货名称
			@JsonProperty("规格型号")
		    public String spcModel;//规格型号
			@JsonProperty("存货代码")
		    public String invtyCd;//存货代码
			@JsonProperty("箱规")
		    public BigDecimal bxRule;//箱规
			@JsonProperty("进项税率")
		    public BigDecimal iptaxRate;//进项税率
			@JsonProperty("销项税率")
		    public BigDecimal optaxRate;//销项税率
			@JsonProperty("最高进价")
		    public BigDecimal highestPurPrice;//最高进价
			@JsonProperty("最低售价")
		    public BigDecimal loSellPrc;//最低售价
			@JsonProperty("参考成本")
		    public BigDecimal refCost;//参考成本
			@JsonProperty("参考售价")
		    public BigDecimal refSellPrc;//参考售价
			@JsonProperty("最新成本")
		    public BigDecimal ltstCost;//最新成本
			@JsonProperty("计量单位名称")
		    public String measrCorpNm;//计量单位名称
			@JsonProperty("仓库名称")
		    public String whsNm;//仓库名称
			@JsonProperty("对应条形码")
		    public String crspdBarCd;//对应条形码
			@JsonProperty("应税劳务")
		    public Integer shdTaxLabour;//应税劳务
			@JsonProperty("来源子表序号")
		    public Long toOrdrNum;//来源子表序号
			@JsonProperty("未开票数量")
		    public BigDecimal unBllgQty;//未开票数量
			@JsonProperty("是否折扣")
		    public Integer isNtDiscnt;//是否折扣
		}
}
