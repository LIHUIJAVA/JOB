package com.px.mis.purc.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.*;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.RtnGoodsService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

/*退货单功能*/
@Transactional
@Service
public class RtnGoodsServiceImpl extends poiTool implements RtnGoodsService {
	@Autowired
	private RtnGoodsDao rgd;
	@Autowired
	private RtnGoodsSubDao rgsd;
	@Autowired
	private SellSnglSubDao sssd;// 销售单子表
	@Autowired
	private SellOutWhsDao sowd;// 销售出库单主表
	@Autowired
	private SellOutWhsSubDao sowds;// 销售出库子表
	@Autowired
	private InvtyTabDao itd;// 库存表
	@Autowired
	private InvtyDocDao idd;// 存货档案
	@Autowired
	private IntoWhsDao intoWhsDao;// 采购入库单
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;

	@Override
	public String addRtnGoods(String userId, RtnGoods rtnGoods, List<RtnGoodsSub> rtnGoodsSubList, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		// 获取订单号
		String number = getOrderNo.getSeqNo("TH", userId, loginTime);
		if (rgd.selectRtnGoodsById(number) != null) {
			message = "编码" + number + "已存在，请重新输入！";
			isSuccess = false;
		} else {
			rtnGoods.setRtnGoodsId(number);
			for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
				rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());
				if (rtnGoodsSub.getPrdcDt() == "") {
					rtnGoodsSub.setPrdcDt(null);
				}
				if (rtnGoodsSub.getInvldtnDt() == "") {
					rtnGoodsSub.setInvldtnDt(null);
				}
				rtnGoodsSub.setUnBllgQty(rtnGoodsSub.getQty().abs());// 未开票数量
			}
			rgd.insertRtnGoods(rtnGoods);
			rgsd.insertRtnGoodsSub(rtnGoodsSubList);
			message = "新增成功！";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/addRtnGoods", isSuccess, message, rtnGoods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改退货单
	@Override
	public String editRtnGoods(RtnGoods rtnGoods, List<RtnGoodsSub> rtnGoodsSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		rgsd.deleteRtnGoodsSubByRtnGoodsId(rtnGoods.getRtnGoodsId());
		rgd.updateRtnGoodsByRtnGoodsId(rtnGoods);
		for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
			rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());
			if (rtnGoodsSub.getPrdcDt() == "") {
				rtnGoodsSub.setPrdcDt(null);
			}
			if (rtnGoodsSub.getInvldtnDt() == "") {
				rtnGoodsSub.setInvldtnDt(null);
			}
			rtnGoodsSub.setUnBllgQty(rtnGoodsSub.getQty().abs());// 未开票数量
		}
		rgsd.insertRtnGoodsSub(rtnGoodsSubList);
		isSuccess = true;
		message = "更新成功！";
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/editRtnGoods", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteRtnGoods(String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		RtnGoods rtn = rgd.selectRtnGoodsByRtnGoodsId(rtnGoodsId);
		
		if (rtn != null) {
			rgd.deleteRtnGoodsByRtnGoodsId(rtnGoodsId);
			rgsd.deleteRtnGoodsSubByRtnGoodsId(rtnGoodsId);
			if (rtn.getBizTypId().equals("2")) {
				
				throw new RuntimeException("不能手动删除B2C的退货单!");
			}
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编码" + rtnGoodsId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/deleteRtnGoods", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(message);
		}
		return resp;
	}

	@Override
	public String queryRtnGoods(String rtnGoodsId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<RtnGoodsSub> rtnGoodsSub = new ArrayList<>(); */
		RtnGoods rtnGoods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoodsId);
		if (rtnGoods != null) {
			/* rtnGoodsSub=rgsd.selectRtnGoodsSubByRtnGoodsId(rtnGoodsId); */
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + rtnGoodsId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/RtnGoods/queryRtnGoods", isSuccess, message, rtnGoods);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String queryRtnGoodsList(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// 退货单单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<RtnGoods> poList = rgd.selectRtnGoodsList(map);

		int count = rgd.selectRtnGoodsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/RtnGoods/queryRtnGoodsList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
	@Override
	public String deleteRtnGoodsList(String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (rgd.selectRtnGoodsById(rtnGoodsId).getBizTypId().equals("2")) {
			throw new RuntimeException("不能手动删除B2C的退货单!");
		}
		try {
			List<String> lists = getList(rtnGoodsId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (rgd.selectRtnGoodsIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteRtnGoodsList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
					throw new RuntimeException(message);
				}

				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
					throw new RuntimeException(message);
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/RtnGoods/deleteRtnGoodsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteRtnGoodsList(List<String> lists2) {
		rgd.insertRtnGoodsDl(lists2);
		rgsd.insertRtnGoodsSubDl(lists2);
		int a = rgd.deleteRtnGoodsList(lists2);
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

	@Override
	public String printingRtnGoodsList(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// 退货单单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<zizhu> rtnGoodsList = rgd.printingRtnGoodsList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/RtnGoods/printingRtnGoodsList", true, "查询成功！", null,
					rtnGoodsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 退货单审核和弃审
	@Override
	public Map<String, Object> updateRtnGoodsIsNtChksList(String userId, RtnGoods rtnGoods, String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (rtnGoods.getIsNtChk() == 1) {
				message.append(updateRtnGoodsIsNtChkOK(userId, rtnGoods, loginTime).get("message"));
			} else if (rtnGoods.getIsNtChk() == 0) {
				message.append(updateRtnGoodsIsNtChkNO(rtnGoods).get("message"));
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 退货单审核
	public Map<String, Object> updateRtnGoodsIsNtChkOK(String userId, RtnGoods rtnGoods, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		RtnGoods rtnGods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());// 查数据库
		List<RtnGoodsSub> rtnGoodsSubList = rtnGods.getRtnGoodsSub();// 获得退货单子表

		// 首先判断退货单的审核状态，审核状态为0时才可以执行审核操作
		if (rtnGods.getIsNtChk() == 0) {
			Map<String, List<RtnGoodsSub>> handleMap = rtnGoodsSubList.stream()
					.collect(Collectors.groupingBy(RtnGoodsSub::getWhsEncd));
			for (Map.Entry<String, List<RtnGoodsSub>> entry : handleMap.entrySet()) {
				SellOutWhs sellOutWhs = new SellOutWhs();// 销售出库单主表
				String number = getOrderNo.getSeqNo("CK", userId, loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, rtnGods);
					// 将委托代销主表复制给销售专用发票
					sellOutWhs.setOutWhsId(number);// 出库单编码
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));// 出库单日期
					sellOutWhs.setSellOrdrInd(rtnGods.getRtnGoodsId());// 销售出库单中对应的销售单编码
					sellOutWhs.setFormTypEncd("010");// 单据类型编码
					sellOutWhs.setToFormTypEncd(rtnGods.getFormTypEncd());// 来源单据类型
					// 通过业务类型判断收发类别
					if (rtnGods.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// 收发类型编码，线下销售
					}
					if (rtnGods.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// 收发类型编码，线上销售
					}
					sellOutWhs.setIsNtRtnGood(1);// 是否退货
					sellOutWhs.setOutIntoWhsTypId("10");// 出入库类型编码
					sellOutWhs.setSetupPers(rtnGoods.getChkr());// 创建人,即销售单的审核人
					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 创建时间=当前时间

					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();// 销售出库单子表集合
					for (RtnGoodsSub rtnGoodsSub : entry.getValue()) {
						BigDecimal qtyList = rgsd.selectRtnGoodsSubQty(rtnGoodsSub);
						if (qtyList == null) {
							isSuccess = false;
							message += "编码为：" + rtnGoods.getRtnGoodsId() + "的退货单中数量为空，无法审核\n";
							throw new RuntimeException(message);
						} else if (qtyList.intValue() == 0) {
							continue;
						} else {
							// 库存表实体
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setWhsEncd(rtnGoodsSub.getWhsEncd());
							invtyTab.setInvtyEncd(rtnGoodsSub.getInvtyEncd());
							invtyTab.setBatNum(rtnGoodsSub.getBatNum());
							invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// 防止并发修改,FOR UPDATE
							SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// 销售出库单子表
							BeanUtils.copyProperties(sellOutWhsSub, rtnGoodsSub);// 将销售单子表复制给销售出库单
							sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// 将销售出库单主表编码复制给销售出库单子表中主表编码
							// 如果没有生产日期和保质期时，失效日期默认为null
							if (rtnGoodsSub.getPrdcDt() == null || rtnGoodsSub.getBaoZhiQi() == null) {
								sellOutWhsSub.setInvldtnDt(null);
							} else {
								// 计算销售出库单的失效日期
								sellOutWhsSub.setPrdcDt(rtnGoodsSub.getPrdcDt());// 生产日期
								sellOutWhsSub.setBaoZhiQi(rtnGoodsSub.getBaoZhiQi());// 保质期
								sellOutWhsSub.setInvldtnDt(
										CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
							}
							// 查询库存表中无税单价
							// 将获取的无税单价赋给销售出库的无税单价
							setRtnSellOutWhsCB(invtyTab, rtnGods, rtnGoodsSub, sellOutWhsSub);
							sellOutWhsSub.setIsNtRtnGoods(1);// 是否退货
							sellOutWhsSub.setToOrdrNum(rtnGoodsSub.getOrdrNum());// 来源单据子表序号
							sellOutWhsSubList.add(sellOutWhsSub);
							// 销售单审核时判断库存表中是否有该存货信息
							if (invtyTab != null) {
								invtyTab.setAvalQty(rtnGoodsSub.getQty());
								// 在库存表中将对应的可用量增加（由于退货单中的数量是负数，所以库存表中要减可用量,负负得正）
								itd.updateInvtyTabAvalQtyJian(invtyTab);
							} else {
								// 库存中没有该存货时，需要新增库存信息，传入的是退货单实体，因此需要将退货单数量设置成正数并传给库存表
								rtnGoodsSub.setQty(rtnGoodsSub.getQty().abs());
								itd.insertInvtyTabToRtnGoods(rtnGoodsSub);
							}
						}
						if (rtnGoodsSub.getToOrdrNum() != null && rtnGoodsSub.getToOrdrNum() != 0) {
							map.put("ordrNum", rtnGoodsSub.getToOrdrNum());// 序号
							map.put("sellSnglId", rtnGods.getSellOrdrId());// 销售单号
							BigDecimal rtnblQty = sssd.selectRtnblQtyByOrdrNum(map);// 根据退货单子表序号查询销售单中可退数量
							if (rtnblQty != null) {
								System.out.println("退货单数量" + rtnGoodsSub.getQty().abs());
								if (rtnGoodsSub.getQty().abs().compareTo(rtnblQty) <= 0) {
									map.put("rtnblQty", rtnGoodsSub.getQty().abs());// 修改可退数量
									sssd.updateSellSnglRtnblQtyByOrdrNum(map);// 根据退货单子表序号修改销售单中可退数量
								} else {
									isSuccess = false;
									message += "单据号为：" + rtnGoods.getRtnGoodsId() + "的退货单单中存货【"
											+ rtnGoodsSub.getInvtyEncd() + "】累计退货数量大于可退数量，无法审核！\n";
									throw new RuntimeException(message);
								}
							} else {
								isSuccess = false;
								message += "单据号为：" + rtnGoods.getRtnGoodsId() + "的退货单对应的销售单中可退数量不存在，无法审核！\n";
								throw new RuntimeException(message);
							}
						}
					}
					if (sellOutWhsSubList.size() > 0) {
						sowd.insertSellOutWhs(sellOutWhs);
						sowds.insertSellOutWhsSub(sellOutWhsSubList);
						sellOutWhsSubList.clear();
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int a = rgd.updateRtnGoodsIsNtChk(rtnGoods);
			if (a >= 1) {
				isSuccess = true;
				message += "编码为" + rtnGoods.getRtnGoodsId() + "的退货单审核成功\n";
			} else {
				isSuccess = false;
				message += "编码为" + rtnGoods.getRtnGoodsId() + "的退货单审核失败\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "编码为" + rtnGoods.getRtnGoodsId() + "的单据已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 退货单弃审
	public Map<String, Object> updateRtnGoodsIsNtChkNO(RtnGoods rtnGoods) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		RtnGoods rtnGods = rgd.selectRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());
		// 首先判断销售单的审核状态，审核状态为1时才可以执行弃操作审
		if (rtnGods.getIsNtChk() == 1) {
			// 根据退货单号取销售出库单的主表信息
			List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(rtnGoods.getRtnGoodsId());
			int isNtChkQty = 0;
			if (sellOutWhsList.size() > 0) {
				for (SellOutWhs sellOutWhs : sellOutWhsList) {
					if (sellOutWhs.getIsNtChk() == 1) {
						isNtChkQty++;
					}
				}
			}
			if (isNtChkQty > 0) {
				isSuccess = false;
				message += "编码为" + rtnGoods.getRtnGoodsId() + "的退货单对应的下游单据【销售出库单】已审核，无法弃审！\n";
				throw new RuntimeException(message);
			} else {
				// 通过退货单号取退货单子表信息
				List<RtnGoodsSub> rtnGoodsSubList = rtnGods.getRtnGoodsSub();// 获得退货单子表
				for (RtnGoodsSub rGSub : rtnGoodsSubList) {
					InvtyTab invtyTab = new InvtyTab();// 库存实体
					invtyTab.setWhsEncd(rGSub.getWhsEncd());
					invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
					invtyTab.setBatNum(rGSub.getBatNum());
					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
					if (invtyTab != null) {
						if (invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) >= 0) {
							invtyTab.setAvalQty(rGSub.getQty());
							itd.updateInvtyTabAvalQtyJia(invtyTab);
						} else if (invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == -1) {
							isSuccess = false;
							message += "编码为：" + rtnGoods.getRtnGoodsId() + "的退货单中存货编码：" + rGSub.getInvtyEncd() + ",批次："
									+ rGSub.getBatNum() + "的库存不足，无法弃审\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "编码为：" + rtnGoods.getRtnGoodsId() + "的退货单中,存货编码：" + rGSub.getInvtyEncd() + ",批次："
								+ rGSub.getBatNum() + "的库存不存在，无法	弃审\n";
						throw new RuntimeException(message);
					}
					if (rGSub.getToOrdrNum() != null && rGSub.getToOrdrNum() != 0) {
						map.put("ordrNum", rGSub.getToOrdrNum());// 序号
						map.put("sellSnglId", rtnGods.getSellOrdrId());
						BigDecimal rtnblQty = sssd.selectRtnblQtyByOrdrNum(map);// 根据退货单子表序号查询销售单中可退数量
						if (rtnblQty != null) {
							map.put("rtnblQty", rGSub.getQty());// 修改可退数量
							sssd.updateSellSnglRtnblQtyByOrdrNum(map);// 根据退货单子表序号修改销售单中可退数量
						} else {
							isSuccess = false;
							message += "单据号为：" + rtnGoods.getRtnGoodsId() + "的退货单对应的销售单中可退数量不存在，无法弃审！\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = rgd.updateRtnGoodsIsNtChk(rtnGoods);
				if (sellOutWhsList.size() > 0) {
					sowd.deleteSellOutWhsBySellOrdrInd(rtnGoods.getRtnGoodsId());// 删除销售出库单
				}
				if (a >= 1) {
					isSuccess = true;
					message += "编码为" + rtnGoods.getRtnGoodsId() + "的退货单弃审成功\n";
				} else {
					isSuccess = false;
					message += "编码为" + rtnGoods.getRtnGoodsId() + "的退货单弃审失败\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message = "编码为" + rtnGoods.getRtnGoodsId() + "的退货单未审核，请先审核该单据\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setRtnSellOutWhsCB(InvtyTab invtyTab, RtnGoods rtnGoods, RtnGoodsSub rGSub, SellOutWhsSub sOutWhsSub) {
		Map<String, Object> map = new HashMap<>();
		if (rGSub.getToOrdrNum() != null && rGSub.getToOrdrNum() != 0) {
			// 原销售单对应的出库单子表序号
			map.put("toOrdrNum", rGSub.getToOrdrNum());
			map.put("sellSnglId", rtnGoods.getSellOrdrId());// 销售单号
			BigDecimal noTaxUprc1 = sowd.selectSellOutWhsSubByToOrdrNum(map);
			if (noTaxUprc1 != null) {
				sOutWhsSub.setNoTaxUprc(noTaxUprc1);
				// 计算未税金额 金额=未税数量*未税单价
				sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				throw new RuntimeException("编码为" + rtnGoods.getRtnGoodsId() + "的退货单部分存货未找到对应的出库成本，请查明原因再审核\n");
			}
		} else {
			map.put("invtyEncd", rGSub.getInvtyEncd());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// 采购入库单最近一次入库单价
			if (pursunTaxUprc != null) {
				// 获取无税单价,取到的单价为正数，需要转成负数
				sOutWhsSub.setNoTaxUprc(pursunTaxUprc);
				// 计算未税金额 金额=未税数量*未税单价
				sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
						sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				// 查询存货档案中的参考成本
				BigDecimal noTaxUprc2 = idd.selectRefCost(rGSub.getInvtyEncd());
				if (noTaxUprc2 != null) {
					// 获取无税单价,取到的单价为正数，需要转成负数
					sOutWhsSub.setNoTaxUprc(noTaxUprc2);
					// 计算未税金额 金额=未税数量*未税单价
					sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
					// 计算税额 税额=未税金额*税率
					sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					// 计算含税单价 含税单价=无税单价*税率+无税单价
					sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
					sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty(),
							sOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
					System.out.println("参考售价");
				} else {
					throw new RuntimeException("编码为" + rtnGoods.getRtnGoodsId() + "的退货单取不到对应的成本，无法生成对应销售出库单，审核失败！\n");
				}
			}
		}
	}

	// 导入退货单
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, RtnGoods> rtnGoodsMap = null;
		if (i == 0) {
			rtnGoodsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			rtnGoodsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
		List<RtnGoods> rtnGoodsList = rtnGoodsMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<RtnGoods>> rtnGoodsLists = Lists.partition(rtnGoodsList, 1000);

		for (List<RtnGoods> rtnGoods : rtnGoodsLists) {
			rgd.insertRtnGoodsbyUpload(rtnGoods);
		}
		List<RtnGoodsSub> rtnGoodsSubList = new ArrayList<>();
		int flag = 0;
		for (RtnGoods entry : rtnGoodsList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<RtnGoodsSub> tempList = entry.getRtnGoodsSub();
			tempList.forEach(s -> s.setRtnGoodsId(entry.getRtnGoodsId()));
			rtnGoodsSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (rtnGoodsSubList.size() >= 1000 || rtnGoodsMap.size() == flag) {
				rgsd.insertRtnGoodsSub(rtnGoodsSubList);
				rtnGoodsSubList.clear();
			}
		}
		isSuccess = true;
		message = "退货单导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/RtnGoods/uploadRtnGoodsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, RtnGoods> uploadScoreInfo(MultipartFile file) {
		Map<String, RtnGoods> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "退货单编码");
				// 创建实体类
				RtnGoods rtnGoods = new RtnGoods();
				if (temp.containsKey(orderNo)) {
					rtnGoods = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				rtnGoods.setSellTypId(GetCellData(r, "销售类型编码"));// 销售类型编码
				rtnGoods.setRtnGoodsId(orderNo); // 退货单编码
				if (GetCellData(r, "退货单日期") == null || GetCellData(r, "退货单日期").equals("")) {
					rtnGoods.setRtnGoodsDt(null);
				} else {
					rtnGoods.setRtnGoodsDt(GetCellData(r, "退货单日期").replaceAll("[^0-9:-]", " "));// 退货单日期
				}
				rtnGoods.setCustId(GetCellData(r, "客户编码"));// 客户编码
//				rtnGoods.setCustNm(GetCellData(r, "客户名称"));// 客户名称
				rtnGoods.setCustOrdrNum(GetCellData(r, "客户订单号"));// 客户订单号
				rtnGoods.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//				rtnGoods.setDepName(GetCellData(r, "部门名称"));// 部门名称
				rtnGoods.setAccNum(GetCellData(r, "业务员编码"));// 业务员编码
				rtnGoods.setUserName(GetCellData(r, "业务员名称"));// 业务员名称
				rtnGoods.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
				rtnGoods.setBizTypId(GetCellData(r, "业务类型编码"));// 业务类型编码
//				rtnGoods.setRecvSendCateId(GetCellData(r,"收发类别编码"));//收发类别编码
				rtnGoods.setSellOrdrId(GetCellData(r, "销售单编码"));// 销售单编码
//				rtnGoods.setRefId(GetCellData(r,"退款单号"));//退款单号 
				rtnGoods.setTxId(GetCellData(r, "交易编码"));// 交易编码
				rtnGoods.setRecvr(GetCellData(r, "收件人")); // '收件人'
				rtnGoods.setRecvrTel(GetCellData(r, "收件人电话")); // '收件人电话'
				rtnGoods.setRecvrAddr(GetCellData(r, "收件人地址")); // '收件人地址'
				rtnGoods.setRecvrEml(GetCellData(r, "收件人邮箱")); // '收件人邮箱'
				rtnGoods.setBuyerNote(GetCellData(r, "买家留言")); // '买家留言'
				rtnGoods.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					rtnGoods.setSetupTm(null);
				} else {
					rtnGoods.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				rtnGoods.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					rtnGoods.setModiTm(null);
				} else {
					rtnGoods.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				rtnGoods.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				rtnGoods.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					rtnGoods.setChkTm(null);
				} else {
					rtnGoods.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				rtnGoods.setIsNtBllg(new Double(GetCellData(r, "是否开票")).intValue());// 是否开票
				rtnGoods.setMemo(GetCellData(r, "表头备注"));// 主备注
				rtnGoods.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue());
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoods.getRtnGoodsSub();// 退货单子表
				if (rtnGoodsSubList == null) {
					rtnGoodsSubList = new ArrayList<>();
				}
				RtnGoodsSub rtnGoodsSub = new RtnGoodsSub();
				rtnGoodsSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//				rtnGoodsSub.setWhsNm(GetCellData(r, "仓库名称"));// 仓库名称
				rtnGoodsSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				rtnGoodsSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				rtnGoodsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				rtnGoodsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				rtnGoodsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				rtnGoodsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				rtnGoodsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				rtnGoodsSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				rtnGoodsSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				rtnGoodsSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				rtnGoodsSub.setBatNum(GetCellData(r, "批次"));// 批次
				rtnGoodsSub.setIntlBat(GetCellData(r, "国际批次"));// 国际批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					rtnGoodsSub.setPrdcDt(null);
				} else {
					rtnGoodsSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				rtnGoodsSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue());// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					rtnGoodsSub.setInvldtnDt(null);
				} else {
					rtnGoodsSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				rtnGoodsSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				rtnGoodsSub.setProjNm(GetCellData(r, "项目名称"));// 项目名称
				rtnGoodsSub.setDiscntRatio(GetCellData(r, "折扣比例"));// 折扣比例
				rtnGoodsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8));
				rtnGoodsSub.setMemo(GetCellData(r, "表体备注"));// 子备注

				rtnGoodsSubList.add(rtnGoodsSub);
				rtnGoods.setRtnGoodsSub(rtnGoodsSubList);
				temp.put(orderNo, rtnGoods);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, RtnGoods> uploadScoreInfoU8(MultipartFile file) {
		Map<String, RtnGoods> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "发货单号");
				// 创建实体类
				RtnGoods sellSngl = new RtnGoods();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}

				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellSngl.setRtnGoodsId(orderNo); // '销售单编码', varchar(200
				if (GetCellData(r, "发货日期") == null || GetCellData(r, "发货日期").equals("")) {
					sellSngl.setRtnGoodsDt(null);
				} else {
					sellSngl.setRtnGoodsDt(GetCellData(r, "发货日期").replaceAll("[^0-9:-]", " "));// 销售单日期
				}

				sellSngl.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				sellSngl.setUserName(GetCellData(r, "业 务 员")); // 业务员名称 varchar(200
				sellSngl.setCustId(GetCellData(r, "客户编码")); // '客户编码', varchar(200
				sellSngl.setDeptId(GetCellData(r, "部门编码")); // '部门编码', varchar(200
				sellSngl.setSellTypId("1"); // 销售类型编码
				String bizTypId = null;

				if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2B")) {
					bizTypId = "1";
				} else if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2C")) {
					bizTypId = "2";
				}
				sellSngl.setFormTypEncd("008");// 单据类型编码

				sellSngl.setBizTypId(bizTypId); // 业务类型编码
//				007	销售单
//				008	退货单

//				if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("否")) {
//					sellSngl.setFormTypEncd("007");// 单据类型编码
//				} else if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("是")) {
//					sellSngl.setFormTypEncd("008");// 单据类型编码
//				}

//					sellSngl.setRecvSendCateId(GetCellData(r,"收发类别编码")); // '收发类别编码', varchar(200

//				sellSngl.setDelvAddr(GetCellData(r,"发货地址")); // '发货地址', varchar(200
				sellSngl.setTxId(GetCellData(r, "交易编号")); // '交易编码(订单编码', varchar(200
				sellSngl.setIsNtBllg(0); // '是否开票', int(11
				sellSngl.setIsNtChk(1); // '是否审核', int(11
				sellSngl.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellSngl.setChkTm(null);
				} else {
					sellSngl.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
//				sellSngl.setIsNtBookEntry(0); // '是否记账', int(11
//					sellSngl.setChkr(GetCellData(r, "记账人")); // '审核人', varchar(200
//					if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
//						sellSngl.setChkTm(null);
//					} else {
//						sellSngl.setChkTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 审核时间
//					}

				sellSngl.setSetupPers(GetCellData(r, "制单人")); // '创建人', varchar(200
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					sellSngl.setSetupTm(null);
				} else {
					sellSngl.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellSngl.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellSngl.setModiTm(null);
				} else {
					sellSngl.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellSngl.setMemo(GetCellData(r, "备 注")); // '备注', varchar(2000
//				sellSngl.setIsPick(0); // '是否拣货', int(2
//				sellSngl.setEcOrderId(GetCellData(r, "客户订单号")); // '电商订单号', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "收货联系人")); // '收件人', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "收货联系人手机")); // '收件人电话'
				sellSngl.setRecvrAddr(GetCellData(r, "发货地址")); // '收件人地址'
//				sellSngl.setRecvrEml(GetCellData(r, "收件人邮箱")); // '收件人邮箱'
				sellSngl.setBuyerNote(GetCellData(r, "买家留言")); // '买家留言'
//				sellSngl.setDeliverSelf(1); // '是否自发货'
				sellSngl.setCustOrdrNum(GetCellData(r, "客户订单号")); // '客户订单号'
				sellSngl.setIsNtMakeVouch(0);

				List<RtnGoodsSub> sellSnglSubist = sellSngl.getRtnGoodsSub();// 销售订单子表
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				RtnGoodsSub sellSnglSub = new RtnGoodsSub();
//				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "发货单子表ID")));
				sellSnglSub.setWhsEncd(GetCellData(r, "仓库编号")); // '仓库编码',
				sellSnglSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				sellSnglSub.setRtnGoodsId(orderNo); // '销售单编码',
//				if (GetCellData(r, "发货日期") == null || GetCellData(r, "发货日期").equals("")) {
//					sellSnglSub.setExpctDelvDt(null);
//				} else {
//					sellSnglSub.setExpctDelvDt(GetCellData(r, "发货日期").replaceAll("[^0-9:-]", " "));// 预计发货日期
//				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8).abs());// '未开票数量',
				sellSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				sellSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				sellSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				sellSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				sellSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				sellSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				sellSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "表头税率"), 8)); // '税率',
				sellSnglSub.setIntlBat(GetCellData(r, "国际批号")); // 国际批次
				sellSnglSub.setBatNum(GetCellData(r, "批号")); // '批次',
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					sellSnglSub.setPrdcDt(null);
				} else {
					sellSnglSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					sellSnglSub.setInvldtnDt(null);
				} else {
					sellSnglSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 计划到货时间
				}

				sellSnglSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue()); // '保质期',

				if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("否")) {
					sellSnglSub.setIsNtRtnGoods(0); // '退货标识',
				} else if (GetCellData(r, "退货标识") != null && GetCellData(r, "退货标识").equals("是")) {
					sellSnglSub.setIsNtRtnGoods(1); // '退货标识',
				}
				if (GetCellData(r, "赠品") != null && GetCellData(r, "赠品").equals("否")) {
					sellSnglSub.setIsComplimentary(0); // '是否赠品',
				} else if (GetCellData(r, "赠品") != null && GetCellData(r, "赠品").equals("是")) {
					sellSnglSub.setIsComplimentary(1); // '是否赠品',
				}

				sellSnglSub.setMemo(GetCellData(r, "表体备注")); // '备注',
//				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '可退数量'
//				sellSnglSub.setHadrtnQty(BigDecimal.ZERO); // '已退数量'
				sellSnglSub.setProjEncd(GetCellData(r, "项目编码")); // '项目编码'
				sellSnglSub.setProjNm(GetCellData(r, "项目名称")); // '项目名称'
				sellSnglSub.setDiscntRatio("0"); // '折扣比例'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setRtnGoodsSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 分页+排序
	@Override
	public String queryRtnGoodsListOrderBy(Map map) {
		String resp = "";
		List<String> rtnGoodsIdList = getList((String) map.get("rtnGoodsId"));// 退货单单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次

		map.put("rtnGoodsIdList", rtnGoodsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		List<?> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "rg.rtn_goods_dt");
			map.put("sortOrder", "desc");
		}
		poList = rgd.selectRtnGoodsListOrderBy(map);
		Map tableSums = rgd.selectRtnGoodsListSums(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = rgd.selectRtnGoodsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/RtnGoods/queryRtnGoodsListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {
		// 主表

		@JsonProperty("退货单编码")
		public String rtnGoodsId;// 退货单主表编码
		@JsonProperty("退货单日期")
		public String rtnGoodsDt;// 退货日期
		@JsonProperty("销售类型编码")
		public String sellTypId;// 销售类型编码
		@JsonProperty("销售类型名称")
		public String sellTypNm;// 销售类型名称
		@JsonProperty("业务类型编码")
		public String bizTypId;// 业务类型编码
		@JsonProperty("业务类型名称")
		public String bizTypNm;// 业务类型名称
		@JsonProperty("收发类别编码")
		public String recvSendCateId;// 收发类别编码
		@JsonProperty("收发类别名称")
		public String recvSendCateNm;// 收发类别名称
		@JsonProperty("单据类型编码")
		public String formTypEncd;// 单据类型编码
		@JsonProperty("单据类型名称")
		public String formTypName;// 单据类型名称
		@JsonProperty("客户订单号")
		public String custOrdrNum;// 客户订单号
		@JsonProperty("客户编码")
		public String custId;// 客户编码
		@JsonProperty("客户名称")
		public String custNm;// 客户名称
		@JsonProperty("用户编码")
		public String accNum;// 用户编码
		@JsonProperty("用户名称")
		public String userName;// 用户名称
		@JsonProperty("部门编码")
		public String deptId;// 部门编码
		@JsonProperty("部门名称")
		public String deptName;// 部门名称
		@JsonProperty("创建人")
		public String setupPers;// 创建人
		@JsonProperty("创建时间")
		public String setupTm;// 创建时间
		@JsonProperty("是否审核")
		public Integer isNtChk;// 是否审核
		@JsonProperty("审核人")
		public String chkr;// 审核人
		@JsonProperty("审核时间")
		public String chkTm;// 审核时间
		@JsonProperty("修改人")
		public String mdfr;// 修改人
		@JsonProperty("修改时间")
		public String modiTm;// 修改时间
		@JsonProperty("表头备注")
		public String memo;// 备注
		@JsonProperty("发货地址名称")
		public String delvAddrNm;// 发货地址名称
		@JsonProperty("销售单号")
		public String sellOrdrId;// 销售单号
		@JsonProperty("是否开票")
		public Integer isNtBllg;// 是否开票
		@JsonProperty("收件人")
		public String recvr;// 收件人
		@JsonProperty("收件电话")
		public String recvrTel;// 收件电话
		@JsonProperty("收件人地址")
		public String recvrAddr;// 收件人地址
		@JsonProperty("收件人邮箱")
		public String recvrEml;// 收件人邮箱
		@JsonProperty("买家留言")
		public String buyerNote;// 买家留言
		@JsonProperty("退款单编码")
		public String refId;// 退款单编码
		@JsonProperty("交易编码")
		public String txId;// 交易编码
		@JsonProperty("来源单据类型编码")
		public String toFormTypEncd;// 来源单据类型编码
		@JsonProperty("是否生成凭证")
		public Integer isNtMakeVouch;// 是否生成凭证
		@JsonProperty("制凭证人")
		public String makVouchPers;// 制凭证人
		@JsonProperty("制凭证时间")
		public String makVouchTm;// 制凭证时间
		@JsonProperty("序号")
		public Long ordrNum;// 序号
		@JsonProperty("存货编码")
		public String invtyEncd;// 存货编码
		@JsonProperty("存货名称")
		public String invtyNm;// 存货名称
		@JsonProperty("规格型号")
		public String spcModel;// 规格型号
		@JsonProperty("存货代码")
		public String invtyCd;// 存货代码
		@JsonProperty("箱规")
		public BigDecimal bxRule;// 箱规
		@JsonProperty("仓库编码")
		public String whsEncd;// 仓库编码
		@JsonProperty("仓库名称")
		public String whsNm;// 仓库名称
		@JsonProperty("数量")
		public BigDecimal qty;// 数量
		@JsonProperty("箱数")
		public BigDecimal bxQty;// 箱数
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;// 无税单价
		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;// 无税金额
		@JsonProperty("税率")
		public BigDecimal taxRate;// 税率
		@JsonProperty("税额")
		public BigDecimal taxAmt;// 税额
		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;// 含税单价
		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;// 价税合计
		@JsonProperty("生产日期")
		public String prdcDt;// 生产日期
		@JsonProperty("保质期")
		public Integer baoZhiQi;// 保质期
		@JsonProperty("失效日期")
		public String invldtnDt;// 失效日期
		@JsonProperty("批次")
		public String batNum;// 批号
		@JsonProperty("国际批次")
		public String intlBat;// 国际批次
		@JsonProperty("是否退货")
		public Integer isNtRtnGoods;// 是否退货
//		@JsonProperty("是否赠品")
//		public Integer isComplimentary;//是否赠品
		@JsonProperty("项目编码")
		public String projEncd;// 项目编码
		@JsonProperty("项目名称")
		public String projNm;// 项目名称
		@JsonProperty("对应条形码")
		public String crspdBarCd;// 对应条形码
		@JsonProperty("未开票数量")
		public BigDecimal unBllgQty;// 未开票数量
		@JsonProperty("折扣比例")
		public String discntRatio;// 折扣比例
		@JsonProperty("进项税率")
		public BigDecimal iptaxRate;// 进项税率
		@JsonProperty("销项税率")
		public BigDecimal optaxRate;// 销项税率
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
		@JsonProperty("计量单位名称")
		public String measrCorpNm;// 计量单位名称
		@JsonProperty("来源单据子表标识")
		public Long toOrdrNum;// 来源单据子表标识
		@JsonProperty("表体备注")
		public String memos;// 表体备注
		@JsonProperty("退货快递单号")
		public String expressNum;// 退货快递单号
	}
}
