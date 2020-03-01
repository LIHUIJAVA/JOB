package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.expr.NewArray;
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
import com.google.common.collect.Lists;
import com.mysql.cj.log.Log;
import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.BeiYong;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.purc.service.SellSnglService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*销售单功能*/
@Transactional
@Service
public class SellSnglServiceImpl extends poiTool implements SellSnglService {
	@Autowired
	private SellSnglDao ssd; // 销售单主
	@Autowired
	private SellSnglSubDao sssd; // 销售单子
	@Autowired
	private SellOutWhsDao sowd; // 销售出库主
	@Autowired
	private SellOutWhsSubDao sowds; // 销售出库子
	@Autowired
	private InvtyTabDao itd; // 库存表
	@Autowired
	private LogisticsTabDao ltd; // 物流表
	@Autowired
	private InvtyDocDao idd;// 存货档案
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private IntoWhsDao intoWhsDao;// 采购入库单
	@Autowired
	private InvtyDocDao invtyDocDao;
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;

	// 新增销售单
	@Override
	public String addSellSngl(String userId, SellSngl sellSngl, List<SellSnglSub> sellSnglSubList, String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellSnglSub> sellSnglSubSet = new TreeSet();
//			sellSnglSubSet.addAll(sellSnglSubList);
//			if (sellSnglSubSet.size() < sellSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellSngl/addSellSngl", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！", null);
//				return resp;
//			}

			// 获取订单号
			String number = getOrderNo.getSeqNo("XS", userId, loginTime);
			if (ssd.selectSellSnglById(number) != null) {
				message = "编号" + number + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				sellSngl.setSellSnglId(number);
				if (sellSngl.getDeliverSelf() == null) {
					sellSngl.setDeliverSelf(0);
				}

				for (SellSnglSub sellSnglSub : sellSnglSubList) {
					sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());
					sellSnglSub.setRtnblQty(sellSnglSub.getQty());// 可退数量
					sellSnglSub.setUnBllgQty(sellSnglSub.getQty());// 未开票数量
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(sellSnglSub.getInvtyEncd());
					if (invtyDoc.getIsNtSell() == null) {
						isSuccess = false;
						message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
								+ "没有设置是否销售属性，无法保存！";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtSell().intValue() != 1) {
						isSuccess = false;
						message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()+ "非可销售存货，无法保存！";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
								+ "没有设置是否保质期管理属性，无法保存！";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (sellSnglSub.getPrdcDt() == "" || sellSnglSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
									+ "是保质期管理，请输入生产日期！";
							throw new RuntimeException(message);
						}
						if (sellSnglSub.getInvldtnDt() == ""|| sellSnglSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
									+ "是保质期管理，请输入失效日期！";
							throw new RuntimeException(message);
						}
					}
					if (sellSnglSub.getPrdcDt() == "") {
						sellSnglSub.setPrdcDt(null);
					}
					if (sellSnglSub.getInvldtnDt() == "") {
						sellSnglSub.setInvldtnDt(null);
					}
				}
				ssd.insertSellSngl(sellSngl);
				sssd.insertSellSnglSub(sellSnglSubList);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/SellSngl/addSellSngl", isSuccess, message, sellSngl);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	@Override
	public String editSellSngl(SellSngl sellSngl, List<SellSnglSub> sellSnglSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellSnglSub> sellSnglSubSet = new TreeSet();
//			sellSnglSubSet.addAll(sellSnglSubList);
//			if (sellSnglSubSet.size() < sellSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellSngl/editSellSngl", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！", null);
//				return resp;
//			}
			sssd.deleteSellSnglSubBySellSnglId(sellSngl.getSellSnglId());
			ssd.updateSellSnglBySellSnglId(sellSngl);
			for (SellSnglSub sellSnglSub : sellSnglSubList) {
				sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());
				sellSnglSub.setRtnblQty(sellSnglSub.getQty()); // 可退数量
				sellSnglSub.setUnBllgQty(sellSnglSub.getQty());// 未开票数量
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(sellSnglSub.getInvtyEncd());
				if (invtyDoc.getIsNtSell() == null) {
					isSuccess = false;
					message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
							+ "没有设置是否销售属性，无法保存！";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtSell().intValue() != 1) {
					isSuccess = false;
					message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()+ "非可销售存货，无法保存！";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
							+ "没有设置是否保质期管理属性，无法保存！";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (sellSnglSub.getPrdcDt() == "" || sellSnglSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
								+ "是保质期管理，请输入生产日期！";
						throw new RuntimeException(message);
					}
					if (sellSnglSub.getInvldtnDt() == ""|| sellSnglSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "该销售单对应的存货:" + sellSnglSub.getInvtyEncd()
								+ "是保质期管理，请输入失效日期！";
						throw new RuntimeException(message);
					}
				}
				if (sellSnglSub.getPrdcDt() == "") {
					sellSnglSub.setPrdcDt(null);
				}
				if (sellSnglSub.getInvldtnDt() == "") {
					sellSnglSub.setInvldtnDt(null);
				}
			}
			sssd.insertSellSnglSub(sellSnglSubList);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("purc/SellSngl/editSellSngl", isSuccess, message, null);
		} catch (IOException e) {
			////e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteSellSngl(String sellSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellSngl sngl = ssd.selectSellSnglBySellSnglId(sellSnglId);
		if (sngl != null && !sngl.getBizTypId().equals("2")) {
			ssd.deleteSellSnglBySellSnglId(sellSnglId);
			isSuccess = true;
			message = "删除成功！";
		} else if (sngl.getBizTypId().equals("2")) {
			throw new RuntimeException("不能手动删除B2C的销售单!");
		} else {
			isSuccess = false;
			message = "编号" + sellSnglId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/deleteSellSngl", isSuccess, message, null);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellSngl(String sellSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellSngl sellSngl = ssd.selectSellSnglBySellSnglId(sellSnglId);
		if (sellSngl != null) {
			isSuccess = true;
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编号" + sellSnglId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/querySellSngl", isSuccess, message, sellSngl);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellSnglList(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// 销售单号
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// 客户订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.containsKey("sort") && map.containsKey("sortOrder")) {
			poList = ssd.selectSellSnglListOrderBy(map);
		} else {
//			poList = ssd.selectSellSnglListOrderBy(map);
			poList = ssd.selectSellSnglList(map);
		}
		int count = ssd.selectSellSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// 第几页
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// 每页条数
		int listNum = poList.size();
		int pages = count / pageSize + 1;// 总页数

		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// 批量删除销售单
	@Override
	public String deleteSellSnglList(String sellSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			SellSngl sngl = ssd.selectSellSnglById(sellSnglId);
			if (sngl.getBizTypId().equals("2")) {
				throw new RuntimeException("不能手动删除B2C的销售单!");
			}

			List<String> lists = getList(sellSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();

			for (String list : lists) {
				if (ssd.selectSellSnglIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteSellSnglList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "删除失败！";
					throw new RuntimeException("单据号为：" + lists2.toString() + "删除失败！");
				}
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/SellSngl/deleteSellSnglList", isSuccess, message, null);
		} catch (Exception e) {
			throw new RuntimeException(message);
		}
		return resp;
	}

	@Transactional
	private int deleteSellSnglList(List<String> lists2) {
		ssd.insertSellSnglDl(lists2);
		sssd.insertSellSnglSubDl(lists2);
		int a = ssd.deleteSellSnglList(lists2);
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

	// 审核销售单
	@Override
	public Map<String, Object> updateSellSnglIsNtChkList(String userId, SellSngl sellSngl, String loginTime)
			throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {

			// 物流表信息
			LogisticsTab logisticsTab = new LogisticsTab();
			BigDecimal goodNum = new BigDecimal(0);// 整单件数
			if (sellSngl.getIsNtChk() == 1) {
//				isSuccess = (Boolean) updateSellSnglIsNtChkOK(logisticsTab,goodNum,userId,sellSngl).get("isSuccess");
				message.append(
						updateSellSnglIsNtChkOK(logisticsTab, goodNum, userId, sellSngl, loginTime).get("message"));
			} else if (sellSngl.getIsNtChk() == 0) {
//				isSuccess = (Boolean) updateSellSnglIsNtChkNO(sellSngl).get("isSuccess");
				message.append(updateSellSnglIsNtChkNO(sellSngl).get("message"));
			} else {
				isSuccess = false;
				message.append("审核状态错误，无法审核！\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 销售单审核
	public Map<String, Object> updateSellSnglIsNtChkOK(LogisticsTab logisticsTab, BigDecimal goodNum, String userId,
			SellSngl sngls, String loginTime) throws RuntimeException {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 通过销售单编号获取销售单主表信息
		SellSngl sellSngl = ssd.selectSellSnglAndSubBySellSnglId(sngls.getSellSnglId());
		// 通过销售单编号获取销售单子表信息
		List<SellSnglSub> sellSnglSubList = sellSngl.getSellSnglSub();
		String isJianInvtyTab = "";
		if (sellSngl.getBizTypId().equals("1")) {
			isJianInvtyTab = "1";
		}
		if (sellSngl.getBizTypId().equals("2")) {
			isJianInvtyTab = "2";
		}

		// 判断销售单的审核状态
		if (sellSngl.getIsNtChk() == 0) {
			Map<String, List<SellSnglSub>> handleMap = sellSnglSubList.stream()
					.collect(Collectors.groupingBy(SellSnglSub::getWhsEncd));
			for (Map.Entry<String, List<SellSnglSub>> entry : handleMap.entrySet()) {
				SellOutWhs sellOutWhs = new SellOutWhs();// 销售出库单主表

				String number = getOrderNo.getSeqNo("CK", userId, loginTime);
				try {
					BeanUtils.copyProperties(sellOutWhs, sellSngl);
					// 将委托代销主表复制给销售专用发票
					sellOutWhs.setOutWhsId(number);// 出库单编码
//					CommonUtil.getLoginTime(loginTime);
					sellOutWhs.setOutWhsDt(CommonUtil.getLoginTime(loginTime));// 出库单日期
					sellOutWhs.setSellOrdrInd(sellSngl.getSellSnglId());// 销售出库单中对应的销售单编码
					sellOutWhs.setFormTypEncd("009");// 单据类型编码
					sellOutWhs.setToFormTypEncd(sellSngl.getFormTypEncd());// 来源单据类型
					// 通过业务类型判断收发类别
					if (sellSngl.getBizTypId().equals("1")) {
						sellOutWhs.setRecvSendCateId("7");// 收发类型编号，线下销售
					}
					if (sellSngl.getBizTypId().equals("2")) {
						sellOutWhs.setRecvSendCateId("6");// 收发类型编号，线上销售
					}
					sellOutWhs.setIsNtRtnGood(0);// 是否退货
					sellOutWhs.setOutIntoWhsTypId("10");// 出入库类型编码
					sellOutWhs.setSetupPers(sngls.getChkr());// 创建人,即销售单的审核人

					// Date dt = new Date();//服务器日期+时间
					// "2019-11-30"
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//					SimpleDateFormat.
					// 转换传入日期为日历对象
					// 设置日历对象的时间部分
					// 输出日历对象，格式：日期+时间

					sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));// 创建时间=当前时间

					List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();// 销售出库单子表集合
					for (SellSnglSub sellSnglSub : entry.getValue()) {
						if (sellSnglSub.getShdTaxLabour() == null || sellSnglSub.getIsNtDiscnt() == null) {
							isSuccess = false;
							message += "编号为：" + sellSnglSub.getSellSnglId() + "的销售单中,存货编号：" + sellSnglSub.getInvtyEncd()
									+ "没有设置应税劳务属性或是否折扣属性，无法审核\n";
							throw new RuntimeException(message);
						}
						if (sellSnglSub.getShdTaxLabour().intValue() == 1
								|| sellSnglSub.getIsNtDiscnt().intValue() == 1) {
							continue;
						} else {
							goodNum = goodNum.add(sellSnglSub.getQty());
							// 库存表实体
							if (isJianInvtyTab.equals("1")) {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// 这里边的库存是加锁的
								// 销售单审核时判断库存表中是否有该存货信息
								if (invtyTab != null) {
									// a.compareTo(b) -1表示小于 1 表示大于 0表示等于 比较销售单的数量和库存表的可用量
									if (invtyTab.getAvalQty().compareTo(sellSnglSub.getQty()) >= 0) {
										SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// 销售出库单子表
										BeanUtils.copyProperties(sellOutWhsSub, sellSnglSub);// 将销售单子表复制给销售出库单
										sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// 将销售出库单主表编码复制给销售出库单子表中主表编码
										// 如果没有生产日期和保质期时，失效日期默认为null
										if (sellSnglSub.getPrdcDt() == null || sellSnglSub.getBaoZhiQi() == null) {
											sellOutWhsSub.setInvldtnDt(null);
										} else {
											// 计算销售出库单的失效日期
											sellOutWhsSub.setPrdcDt(sellSnglSub.getPrdcDt());// 生产日期
											sellOutWhsSub.setBaoZhiQi(sellSnglSub.getBaoZhiQi());// 保质期
											sellOutWhsSub.setInvldtnDt(CalcAmt.getDate(sellOutWhsSub.getPrdcDt(),
													sellOutWhsSub.getBaoZhiQi()));
										}
										// 查询库存表中无税单价
										// 将获取的无税单价赋给销售出库的无税单价
										setSellOutWhsCB(invtyTab, sellSnglSub, sellOutWhsSub);
										sellOutWhsSub.setIsNtRtnGoods(0);// 是否退货
										sellOutWhsSub.setToOrdrNum(sellSnglSub.getOrdrNum());// 来源单据子表序号
										sellOutWhsSubList.add(sellOutWhsSub);
										invtyTab.setAvalQty(sellSnglSub.getQty());
										itd.updateInvtyTabAvalQtyJian(invtyTab);

									} else {
										isSuccess = false;
										message += "编号为：" + sngls.getSellSnglId() + "的销售单中,仓库编码："
												+ sellSnglSub.getWhsEncd() + ",存货编号：" + sellSnglSub.getInvtyEncd()
												+ ",批次:" + sellSnglSub.getBatNum() + "库存中数量不足，无法审核\n";
										throw new RuntimeException(message);
									}
								} else {
									isSuccess = false;
									message += "编号为：" + sngls.getSellSnglId() + "的销售单中,仓库编码：" + sellSnglSub.getWhsEncd()
											+ ",存货编号：" + sellSnglSub.getInvtyEncd() + ",批次：" + sellSnglSub.getBatNum()
											+ "的库存不存在，无法审核\n";
									throw new RuntimeException(message);
								}
							} else {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// 防止并发修改,加锁了
								SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();// 销售出库单子表
								BeanUtils.copyProperties(sellOutWhsSub, sellSnglSub);// 将销售单子表复制给销售出库单
								sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());// 将销售出库单主表编码复制给销售出库单子表中主表编码
								// 如果没有生产日期和保质期时，失效日期默认为null
								if (sellSnglSub.getPrdcDt() == null || sellSnglSub.getBaoZhiQi() == null) {
									sellOutWhsSub.setInvldtnDt(null);
								} else {
									// 计算销售出库单的失效日期
									sellOutWhsSub.setPrdcDt(sellSnglSub.getPrdcDt());// 生产日期
									sellOutWhsSub.setBaoZhiQi(sellSnglSub.getBaoZhiQi());// 保质期
									sellOutWhsSub.setInvldtnDt(
											CalcAmt.getDate(sellOutWhsSub.getPrdcDt(), sellOutWhsSub.getBaoZhiQi()));
								}
								// 查询库存表中无税单价
								// 将获取的无税单价赋给销售出库的无税单价
								setSellOutWhsCB(invtyTab, sellSnglSub, sellOutWhsSub);
								sellOutWhsSub.setIsNtRtnGoods(0);// 是否退货
								sellOutWhsSub.setToOrdrNum(sellSnglSub.getOrdrNum());// 来源单据子表序号
								sellOutWhsSubList.add(sellOutWhsSub);
							}
						}
					}
					if (sellOutWhsSubList.size() > 0) {
						logisticsTab.setIsShip(0);// 是否发货
						logisticsTab.setIsBackPlatform(0);// 是否回传平台
						// 审核后新增物流表信息
						PlatOrder platOrder = new PlatOrder();
						if (sellSngl.getTxId() != null && !sellSngl.getTxId().equals("")) {
							platOrder = platOrderDao.select(sellSngl.getTxId());
							logisticsTab.setDeliverWhs(platOrder.getDeliverWhs());// 发货仓库
							if (platOrder.getIsShip() == 1) {
								logisticsTab.setExpressCode(platOrder.getExpressNo());// 快递单号
								logisticsTab.setIsShip(1);// 发货状态
								logisticsTab.setIsBackPlatform(1);// 是否回传平台
							}
						}
						if (sellSngl.getDeliverSelf() == null) {
							logisticsTab.setDeliverSelf(0);// 是否自发货
						} else {
							logisticsTab.setDeliverSelf(sellSngl.getDeliverSelf());// 是否自发货
						}
						if (!sellSngl.getTxId().equals("") && sellSngl.getTxId() != null
								&& sellSngl.getDeliverSelf() == 1) {// 需要自发货
							logisticsTab.setExpressEncd(platOrder.getExpressCode());// 快递公司编码
							logisticsTab.setTemplateId(platOrder.getExpressTemplate());// 打印模板
							logisticsTab.setStoreId(platOrder.getStoreId());// 店铺编码
							logisticsTab.setPlatId(storeRecordDao.select(platOrder.getStoreId()).getEcId());// 平台编码
						}
						logisticsTab.setSaleEncd(sellSngl.getSellSnglId());// 销售单号
						logisticsTab.setOrderId(sellSngl.getTxId());// 订单编号 去销售单中的交易编号
						logisticsTab.setOutWhsId(sellOutWhs.getOutWhsId());// 销售出库单号
						logisticsTab.setEcOrderId(sellSngl.getEcOrderId());// 电商订单号

						logisticsTab.setIsPick(0);// 是否拣货完成
						// 整单商品件数
						logisticsTab.setBuyerNote(sellSngl.getBuyerNote());// 买家留言
						logisticsTab.setRecAddress(sellSngl.getRecvrAddr());// 收货人详细地址
						logisticsTab.setRecName(sellSngl.getRecvr());// 收件人姓名
						logisticsTab.setRecMobile(sellSngl.getRecvrTel());// 收件人手机号
						logisticsTab.setBizTypId(sellSngl.getBizTypId());// 业务类型编号
						logisticsTab.setSellTypId(sellSngl.getSellTypId());// 销售类型编号
						logisticsTab.setRecvSendCateId(sellSngl.getRecvSendCateId());// 收发类别编号
						logisticsTab.setGoodNum(goodNum);
						goodNum = new BigDecimal(0);
						ltd.insert(logisticsTab);
						sowd.insertSellOutWhs(sellOutWhs);
						sowds.insertSellOutWhsSub(sellOutWhsSubList);
						sellOutWhsSubList.clear();
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			int updateFlag = ssd.updateSellSnglIsNtChk(sngls);
			if (updateFlag == 1) {
				isSuccess = true;
				message += "编号为：" + sngls.getSellSnglId() + "的销售单审核成功！\n";
			} else {
				isSuccess = false;
				message += "编号为：" + sngls.getSellSnglId() + "的销售单已审核，不可重复审核\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "编号为：" + sngls.getSellSnglId() + "的销售单已审核，不可重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void setSellOutWhsCB(InvtyTab invtyTab, SellSnglSub sellSnglSub, SellOutWhsSub sellOutWhsSub) {
		Map<String, Object> map = new HashMap<>();
		if (invtyTab.getUnTaxUprc() == null) {
			map.put("invtyEncd", sellOutWhsSub.getInvtyEncd());
			map.put("whsEncd", sellOutWhsSub.getWhsEncd());
			map.put("batNum", sellOutWhsSub.getBatNum());
			BigDecimal pursunTaxUprc = intoWhsDao.selectUnTaxUprc(map);// 采购入库单最近一次入库单价
			if (pursunTaxUprc == null) {
				BigDecimal noTaxUprc = idd.selectRefCost(sellOutWhsSub.getInvtyEncd());// 取存货档案中最近一次参考成本
				sellOutWhsSub.setNoTaxUprc(noTaxUprc);
				sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// 税率
				// 计算未税金额 金额=未税数量*未税单价
				sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sellOutWhsSub.setTaxAmt(
						CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
								.divide(new BigDecimal(100)));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			} else {
				sellOutWhsSub.setNoTaxUprc(pursunTaxUprc);
				sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// 税率
				// 计算未税金额 金额=未税数量*未税单价
				sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
				// 计算税额 税额=未税金额*税率
				sellOutWhsSub.setTaxAmt(
						CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
								.divide(new BigDecimal(100)));
				// 计算含税单价 含税单价=无税单价*税率+无税单价
				sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
				// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
				sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
						sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			}
		} else {
			sellOutWhsSub.setNoTaxUprc(invtyTab.getUnTaxUprc());
			sellOutWhsSub.setTaxRate((sellSnglSub.getTaxRate()));// 税率
			// 计算未税金额 金额=未税数量*未税单价
			sellOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty()));
			// 计算税额 税额=未税金额*税率
			sellOutWhsSub.setTaxAmt(
					CalcAmt.taxAmt(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(), sellOutWhsSub.getTaxRate())
							.divide(new BigDecimal(100)));
			// 计算含税单价 含税单价=无税单价*税率+无税单价
			sellOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
					sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
			// 计算价税合计 价税合计=无税金额*税率+无税金额=税额+无税金额
			sellOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sellOutWhsSub.getNoTaxUprc(), sellOutWhsSub.getQty(),
					sellOutWhsSub.getTaxRate().divide(new BigDecimal(100))));
		}

	}

	// 销售单弃审
	@SuppressWarnings("deprecation")
	private Map<String, Object> updateSellSnglIsNtChkNO(SellSngl sngls) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 通过销售单编号获取销售单主表信息
		SellSngl sellSngl = ssd.selectSellSnglAndSubBySellSnglId(sngls.getSellSnglId());
		String isJiaInvtyTab = "";
		if (sellSngl.getBizTypId().equals("1")) {
			isJiaInvtyTab = "1";
		}
		if (sellSngl.getBizTypId().equals("2")) {
			isJiaInvtyTab = "2";
		}
		// 首先判断销售单的审核状态，审核状态为1时才可以执行弃操作审
		if (sellSngl.getIsNtChk() == 1) {
			// 根据销售标识查询销售出库单的审核状态 和 是否拣货
			if (sellSngl.getIsPick() == 0) {
				List<SellOutWhs> sellOutWhsList = sowd.selectOutWhsIdBySellOrdrInd(sngls.getSellSnglId());
				int isNtChkQty = 0;
				for (SellOutWhs sellOutWhs : sellOutWhsList) {
					if (sellOutWhs.getIsNtChk() == 1) {
						isNtChkQty++;
					}
				}
				if (isNtChkQty > 0) {
					isSuccess = false;
					message += "编号为：" + sngls.getSellSnglId() + "的销售单对应的下游单据【销售出库单】已审核，无法弃审\n";
					throw new RuntimeException(message);

				} else {

					// 通过销售单编号获取销售单子表信息
					List<SellSnglSub> sellSnglSubList = sellSngl.getSellSnglSub();

					if (isJiaInvtyTab.equals("1")) {
						for (SellSnglSub selSnSubs : sellSnglSubList) {
							if (selSnSubs.getShdTaxLabour() == null || selSnSubs.getIsNtDiscnt() == null) {
								isSuccess = false;
								message += "编号为：" + selSnSubs.getSellSnglId() + "的销售单中,存货编号：" + selSnSubs.getInvtyEncd()
										+ "没有设置应税劳务属性或是否折扣属性，无法弃审\n";
								throw new RuntimeException(message);
							}
							if (selSnSubs.getShdTaxLabour().intValue() == 1
									|| selSnSubs.getIsNtDiscnt().intValue() == 1) {
								continue;
							} else {
								// 库存表实体
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(selSnSubs.getWhsEncd());
								invtyTab.setInvtyEncd(selSnSubs.getInvtyEncd());
								invtyTab.setBatNum(selSnSubs.getBatNum());
								invtyTab = itd.selectInvtyTabsByTerm(invtyTab);// 防止并发修改,加锁了
								if (invtyTab != null) {
									invtyTab.setAvalQty(selSnSubs.getQty());
									itd.updateInvtyTabAvalQtyJia(invtyTab);
								} else {
									isSuccess = false;
									message += "编号为：" + sngls.getSellSnglId() + "的销售单中,仓库编码：" + selSnSubs.getWhsEncd()
											+ ",存货编号：" + selSnSubs.getInvtyEncd() + ",批次：" + selSnSubs.getBatNum()
											+ "的库存不存在，无法弃审\n";
									throw new RuntimeException(message);
								}
							}
						}
					}
					int updateFlag = ssd.updateSellSnglIsNtChk(sngls);
					if (updateFlag == 1) {
						isSuccess = true;
						message += "编号为：" + sngls.getSellSnglId() + "的销售单弃审成功\n";
					} else {
						isSuccess = false;
						message += "编号为：" + sngls.getSellSnglId() + "的销售单已弃审，不可重复弃审\n";
						throw new RuntimeException(message);
					}
					sowd.deleteSellOutWhsBySellOrdrInd(sngls.getSellSnglId());// 删除销售出库单
					ssd.deleteLogisticsTab(sngls.getSellSnglId());// 删除物流表
				}
			} else {
				isSuccess = false;
				message += "编号为：" + sngls.getSellSnglId() + "的销售单已拣货，无法弃审\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "编号为：" + sngls.getSellSnglId() + "的销售单已弃审，不可重复弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 销售明细表查询功能
	@Override
	public String querySellSnglByInvtyEncd(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// 销售单号
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// 客户订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);

		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			List<BeiYong> poList = ssd.selectSellSnglInvtyEncd(map);// 默认按时间倒序,可传参
			Map tableSums = ssd.selectSellSnglInvtyEncdSums(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = ssd.selectSellSnglInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglByInvtyEncd", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		} else {
			List<BeiYong> poList = ssd.selectSellSnglInvtyEncd(map);
			try {
				resp = BaseJson.returnRespListAnno("purc/SellSngl/querySellSnglByInvtyEncd", true, "成功！", poList);
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		return resp;
	}

	// 导入
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellSngl> sellSnglMap = null;
		if (i == 0) {
			sellSnglMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellSnglMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
		List<SellSngl> sellSnglList = sellSnglMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellSngl>> sellSnglLists = Lists.partition(sellSnglList, 1000);

		for (List<SellSngl> sellSngl : sellSnglLists) {
			ssd.insertSellSnglUpload(sellSngl);
		}
		List<SellSnglSub> sellSnglSubList = new ArrayList<>();
		int flag = 0;
		for (SellSngl entry : sellSnglList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<SellSnglSub> tempList = entry.getSellSnglSub();
			tempList.forEach(s -> s.setSellSnglId(entry.getSellSnglId()));
			sellSnglSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (sellSnglSubList.size() >= 1000 || sellSnglMap.size() == flag) {
				sssd.insertSellSnglSubUpload(sellSnglSubList);
				sellSnglSubList.clear();
			}
		}
		isSuccess = true;
		message = "销售单导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/SellSngl/uploadSellSnglFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, SellSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, SellSngl> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "销售单编码");
				// 创建实体类
				SellSngl sellSngl = new SellSngl();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellSngl.setSellSnglId(orderNo); // '销售单编码', varchar(200
				if (GetCellData(r, "销售单日期") == null || GetCellData(r, "销售单日期").equals("")) {
					sellSngl.setSellSnglDt(null);
				} else {
					sellSngl.setSellSnglDt(GetCellData(r, "销售单日期").replaceAll("[^0-9:-]", " "));// 销售单日期
				}
				sellSngl.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				sellSngl.setUserName(GetCellData(r, "业务员名称")); // 业务员名称 varchar(200
				sellSngl.setCustId(GetCellData(r, "客户编码")); // '客户编码', varchar(200
				sellSngl.setDeptId(GetCellData(r, "部门编码")); // '部门编码', varchar(200
				sellSngl.setBizTypId(GetCellData(r, "业务类型编码")); // '业务类型编码', varchar(200
				sellSngl.setSellTypId(GetCellData(r, "销售类型编码")); // '销售类型编码', varchar(200
				sellSngl.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
//				sellSngl.setRecvSendCateId(GetCellData(r,"收发类别编码")); // '收发类别编码', varchar(200

//				sellSngl.setDelvAddr(GetCellData(r,"发货地址")); // '发货地址', varchar(200
				sellSngl.setTxId(GetCellData(r, "交易编码")); // '交易编码(订单编码', varchar(200
				sellSngl.setIsNtBllg(new Double(GetCellData(r, "是否开票")).intValue()); // '是否开票', int(11
				sellSngl.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue()); // '是否审核', int(11
				sellSngl.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellSngl.setChkTm(null);
				} else {
					sellSngl.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
//				sellSngl.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue()); // '是否记账', int(11
//				sellSngl.setChkr(GetCellData(r, "记账人")); // '审核人', varchar(200
//				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
//					sellSngl.setChkTm(null);
//				} else {
//					sellSngl.setChkTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 审核时间
//				}
				sellSngl.setSetupPers(GetCellData(r, "创建人")); // '创建人', varchar(200
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					sellSngl.setSetupTm(null);
				} else {
					sellSngl.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellSngl.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellSngl.setModiTm(null);
				} else {
					sellSngl.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellSngl.setMemo(GetCellData(r, "表头备注")); // '备注', varchar(2000
				sellSngl.setIsPick(new Double(GetCellData(r, "是否拣货")).intValue()); // '是否拣货', int(2
				sellSngl.setEcOrderId(GetCellData(r, "电商订单号")); // '电商订单号', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "收件人")); // '收件人', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "收件人电话")); // '收件人电话'
				sellSngl.setRecvrAddr(GetCellData(r, "收件人地址")); // '收件人地址'
				sellSngl.setRecvrEml(GetCellData(r, "收件人邮箱")); // '收件人邮箱'
				sellSngl.setBuyerNote(GetCellData(r, "买家留言")); // '买家留言'
				sellSngl.setDeliverSelf(new Double(GetCellData(r, "是否自发货")).intValue()); // '是否自发货'
				sellSngl.setCustOrdrNum(GetCellData(r, "客户订单号")); // '客户订单号'
				sellSngl.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue());

				List<SellSnglSub> sellSnglSubist = sellSngl.getSellSnglSub();// 销售订单子表
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				SellSnglSub sellSnglSub = new SellSnglSub();
				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				sellSnglSub.setWhsEncd(GetCellData(r, "仓库编码")); // '仓库编码',
				sellSnglSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
//				sellSnglSub.setSellSnglId(GetCellData(r,"销售单编码")); // '销售单编码', 
				if (GetCellData(r, "预计发货日期") == null || GetCellData(r, "预计发货日期").equals("")) {
					sellSnglSub.setExpctDelvDt(null);
				} else {
					sellSnglSub.setExpctDelvDt(GetCellData(r, "预计发货日期").replaceAll("[^0-9:-]", " "));// 预计发货日期
				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8)); // '未开票数量',
				sellSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				sellSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				sellSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				sellSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				sellSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				sellSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				sellSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				sellSnglSub.setIntlBat(GetCellData(r, "国际批次")); // 国际批次
				sellSnglSub.setBatNum(GetCellData(r, "批次")); // '批次',
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

				sellSnglSub.setIsNtRtnGoods(new Double(GetCellData(r, "退货标识")).intValue()); // '退货标识',
//				sellSnglSub.setIsComplimentary(new Double(GetCellData(r,"是否赠品")).intValue()); // '是否赠品', 
				sellSnglSub.setMemo(GetCellData(r, "表体备注")); // '备注',
				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "可退数量"), 8)); // '可退数量'
				sellSnglSub.setHadrtnQty(GetBigDecimal(GetCellData(r, "已退数量"), 8)); // '已退数量'
				sellSnglSub.setProjEncd(GetCellData(r, "项目编码")); // '项目编码'
				sellSnglSub.setProjNm(GetCellData(r, "项目名称")); // '项目名称'
				sellSnglSub.setDiscntRatio(GetCellData(r, "折扣比例")); // '折扣比例'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setSellSnglSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, SellSngl> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellSngl> temp = new HashMap<>();
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
				SellSngl sellSngl = new SellSngl();
				if (temp.containsKey(orderNo)) {
					sellSngl = temp.get(orderNo);
				}

				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellSngl.setSellSnglId(orderNo); // '销售单编码', varchar(200
				if (GetCellData(r, "发货日期") == null || GetCellData(r, "发货日期").equals("")) {
					sellSngl.setSellSnglDt(null);
				} else {
					sellSngl.setSellSnglDt(GetCellData(r, "发货日期").replaceAll("[^0-9:-]", " "));// 销售单日期
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
				sellSngl.setIsNtBookEntry(0); // '是否记账', int(11
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
				sellSngl.setIsPick(0); // '是否拣货', int(2
//				sellSngl.setEcOrderId(GetCellData(r, "客户订单号")); // '电商订单号', varchar(200)
				sellSngl.setRecvr(GetCellData(r, "收货联系人")); // '收件人', varchar(200)
				sellSngl.setRecvrTel(GetCellData(r, "收货联系人手机")); // '收件人电话'
				sellSngl.setRecvrAddr(GetCellData(r, "发货地址")); // '收件人地址'
//				sellSngl.setRecvrEml(GetCellData(r, "收件人邮箱")); // '收件人邮箱'
				sellSngl.setBuyerNote(GetCellData(r, "买家留言")); // '买家留言'
				sellSngl.setDeliverSelf(1); // '是否自发货'
				sellSngl.setCustOrdrNum(GetCellData(r, "客户订单号")); // '客户订单号'
				sellSngl.setIsNtMakeVouch(0);

				List<SellSnglSub> sellSnglSubist = sellSngl.getSellSnglSub();// 销售订单子表
				if (sellSnglSubist == null) {
					sellSnglSubist = new ArrayList<>();
				}
				SellSnglSub sellSnglSub = new SellSnglSub();
//				sellSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "发货单子表ID")));
				sellSnglSub.setWhsEncd(GetCellData(r, "仓库编号")); // '仓库编码',
				sellSnglSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				sellSnglSub.setSellSnglId(orderNo); // '销售单编码',
				if (GetCellData(r, "发货日期") == null || GetCellData(r, "发货日期").equals("")) {
					sellSnglSub.setExpctDelvDt(null);
				} else {
					sellSnglSub.setExpctDelvDt(GetCellData(r, "发货日期").replaceAll("[^0-9:-]", " "));// 预计发货日期
				}

				sellSnglSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellSnglSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8).abs()); // '未开票数量',
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
				sellSnglSub.setRtnblQty(GetBigDecimal(GetCellData(r, "可退数量"), 8).abs()); // '可退数量'
				sellSnglSub.setHadrtnQty(GetBigDecimal(GetCellData(r, "已退数量"), 8).abs()); // '已退数量'
				sellSnglSub.setProjEncd(GetCellData(r, "项目编码")); // '项目编码'
				sellSnglSub.setProjNm(GetCellData(r, "项目名称")); // '项目名称'
				sellSnglSub.setDiscntRatio("0"); // '折扣比例'
				sellSnglSubist.add(sellSnglSub);

				sellSngl.setSellSnglSub(sellSnglSubist);
				temp.put(orderNo, sellSngl);
			}
			fileIn.close();
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 打印查询接口
	@Override
	public String printingSellSnglList(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// 销售单号
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// 客户订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<zizhu> sellSnglList = ssd.printingSellSnglList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/SellSngl/printingSellSnglList", true, "查询成功！", null,
					sellSnglList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

	// 根据销售出库单生成销售单
	@Override
	public String updateA(Map map) {
		Boolean isSuccess = true;
		String resp = "";
		List<String> sellSnglIdList = ssd.selectB(map);
		for (String selSngId : sellSnglIdList) {
			System.out.println("销售单号" + selSngId);
			int a = ssd.updateA(selSngId);
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellSngl/updateSellBySellOut", isSuccess, "处理成功", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

	// 退货单参照时查询销售单子表信息
	@Override
	public String selectSellSnglSubByRtnblQty(String sellSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(sellSnglId);
		List<SellSnglSub> sellSnglSubList = sssd.selectSellSnglSubByRtnblQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/SellSngl/selectSellSnglSubByRtnblQty", true, "查询成功！", null,
					sellSnglSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// 参照时查询主表信息
	@Override
	public String querySellSnglLists(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// 销售单号
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// 客户订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次

		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		if (map.containsKey("sort")) {
			map.put("sort",((String)map.get("sort")).replace("a.", ""));
			//使用union all 语句时候,不可以带表别名,其他不动
		}
	
		List<SellSngl> poList = ssd.selectSellSnglListToCZ(map);
		int count = ssd.selectSellSnglListToCZCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// 第几页
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// 每页条数
		int listNum = poList.size();
		int pages = count / pageSize + 1;// 总页数
		
		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglLists", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	// 查询销售统计表
	@Override
	public String selectXSTJList(Map map) {
		return selectSalesCountTable(map);
	}

	/**
	 * 销售统计表
	 */
	private String selectSalesCountTable(Map map) {
		// 查询销售发票,按照部门存货和客户分组
		map.put("isAll", "111");
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		map.put("custIdList", custIdList);
		map.put("invtyEncdList", invtyEncdList);
		List<InvtyDetail> dataList = ssd.selectSalesCountList(map);

		int count = ssd.countSalesCountList(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = dataList.size();
		int pages = count / pageSize + 1;

		String resp = "";
		if (dataList.size() > 0) {
			BigDecimal cost = new BigDecimal(0);
			for (InvtyDetail it : dataList) {
				it.setQty(Optional.ofNullable(it.getQty()).orElse(new BigDecimal(0.00000000)));
				it.setAmt(Optional.ofNullable(it.getAmt()).orElse(new BigDecimal(0.00000000)));

				if (it.getQty().compareTo(BigDecimal.ZERO) != 0) {
					it.setNoTaxUprc(it.getAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
					it.setCntnTaxUprc(it.getSellInAmt().divide(it.getQty(), 8, BigDecimal.ROUND_HALF_UP));
				}
				map.put("invtyEncd", it.getInvtyEncd());
				map.put("custId", it.getCustId());
				map.put("deptId", it.getDeptId());
				// 查询销售出库单金额作为成本
				it.setSellCost(ssd.selectSalesCost(map));
				it.setSellCost(Optional.ofNullable(it.getSellCost()).orElse(new BigDecimal(0.00000000)));
				cost = cost.add(it.getSellCost());
			}
			map.put("isAll", "");
			// 总计
			List<InvtyDetail> allList = ssd.selectSalesCountList(map);
			BigDecimal sellCost = new BigDecimal(0);
			if (allList.size() > 0) {
				InvtyDetail all = allList.get(0);
				all.setDeptName("总计");
				all.setAccNum("");
				all.setCustNm("");
				all.setInvtyNm("");
				all.setSellCost(cost);

				dataList.add(dataList.size(), all);
			}
			for (InvtyDetail it : dataList) {
				// 计算毛利 = 销售-成本
				// 毛利率=毛利? 销售收入×100%
				it.setSellCost(Optional.ofNullable(it.getSellCost()).orElse(new BigDecimal(0.00000000)));
				it.setGross(it.getAmt().subtract(it.getSellCost()));
				it.setGross(Optional.ofNullable(it.getGross()).orElse(new BigDecimal(0.00000000)));
				sellCost.add(it.getSellCost());
				if (it.getAmt().compareTo(BigDecimal.ZERO) != 0) {
					String ra = it.getGross().divide(it.getAmt(), 8, BigDecimal.ROUND_HALF_UP)
							.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					it.setGrossRate(ra + "%");
				}
			}
		}
		try {
			resp = BaseJson.returnRespList("purc/SellSngl/selectXSTJList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, dataList);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;

	}

	// 分页+排序
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String querySellSnglListOrderBy(Map map) {
		String resp = "";
		List<String> sellSnglIdList = getList(((String) map.get("sellSnglId")));// 销售单号
		List<String> custIdList = getList(((String) map.get("custId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> custOrdrNumList = getList(((String) map.get("custOrdrNum")));// 客户订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次
//		String memo = ((String)map.get("memo"));//备注

//		map.put("memo", memo);
		map.put("sellSnglIdList", sellSnglIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "ss.sell_sngl_dt");
			map.put("sortOrder", "desc");
		}
		poList = ssd.selectSellSnglListOrderBy(map);// 查询
		zizhu summary = (zizhu) poList.remove(poList.size() - 1);// 最后一行是总计+计数
		int count = Integer.parseInt(summary.sellSnglId);// 接收计数
		Map tableSums = new HashMap();
		try {
			tableSums.put("qty", summary.qty);
			tableSums.put("bxQty", summary.bxQty);
			tableSums.put("prcTaxSum", summary.prcTaxSum);
			tableSums.put("noTaxAmt", summary.noTaxAmt);
			tableSums.put("taxAmt", summary.taxAmt);
		} catch (Exception e) {
			tableSums = null;
			System.err.println("转map错误");
		}

//			Map tableSums = ssd.selectSellSnglListSums(map);//总计
//			if (null!=tableSums) {
//				DecimalFormat df = new DecimalFormat("#,##0.00");
//				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
//					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
//					entry.setValue(s);
//				}
//			}

//		int count = ssd.selectSellSnglCount(map);//计数

		int pageNo = Integer.parseInt(map.get("pageNo").toString());// 第几页
		int pageSize = Integer.parseInt(map.get("pageSize").toString());// 每页条数
		int listNum = poList.size();
		int pages = count / pageSize + 1;// 总页数

		try {
			resp = BaseJson.returnRespList("purc/SellSngl/querySellSnglListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {

		@JsonProperty("销售单编号")
		public String sellSnglId;// 销售单编号
		@JsonProperty("销售单日期")
		public String sellSnglDt;// 销售单日期
		@JsonProperty("单据类型编码")
		public String formTypEncd;// 单据类型编码
		@JsonProperty("单据类型名称")
		public String formTypName;// 单据类型名称
		@JsonProperty("用户编号 ")
		public String accNum;// 用户编号
		@JsonProperty("用户名称 ")
		public String userName;// 用户名称
		@JsonProperty("部门编号 ")
		public String deptId;// 部门编号
		@JsonProperty("部门名称")
		public String deptName;// 部门名称
		@JsonProperty("客户编号")
		public String custId;// 客户编号
		@JsonProperty("客户名称")
		public String custNm;// 客户名称
		@JsonProperty("业务类型编号")
		public String bizTypId;// 业务类型编号
		@JsonProperty("业务类型名称")
		public String bizTypNm;// 业务类型名称
		@JsonProperty("销售类型编号")
		public String sellTypId;// 销售类型编号
		@JsonProperty("销售类型名称")
		public String sellTypNm;// 销售类型名称
//		@JsonProperty("收发类别编号")
//		public String recvSendCateId;// 收发类别编号
//		@JsonProperty("收发类别名称")
//		public String recvSendCateNm;// 收发类别名称
//		@JsonProperty("发货地址")
//		public String delvAddr;// 发货地址
		@JsonProperty("交易编号 ")
		public String txId;// 交易编号
		@JsonProperty("电商订单号 ")
		public String ecOrderId;// 电商订单号
		@JsonProperty("客户订单号")
		public String custOrdrNum;// 客户订单号
		@JsonProperty("是否开票")
		public Integer isNtBllg;// 是否开票
		@JsonProperty("是否审核")
		public Integer isNtChk;// 是否审核
		@JsonProperty("审核人")
		public String chkr;// 审核人
		@JsonProperty("审核时间")
		public String chkTm;// 审核时间
		@JsonProperty("是否记账")
		public Integer isNtBookEntry;// 是否记账
		@JsonProperty("记账人")
		public String bookEntryPers;// 记账人
		@JsonProperty("记账时间")
		public String bookEntryTm;// 记账时间
		@JsonProperty("创建人")
		public String setupPers;// 创建人
		@JsonProperty("创建时间")
		public String setupTm;// 创建时间
		@JsonProperty("修改人")
		public String mdfr;// 修改人
		@JsonProperty("修改时间")
		public String modiTm;// 修改时间
		@JsonProperty("表头备注 ")
		public String memo;// 备注
		@JsonProperty("收件人 ")
		public String recvr;// 收件人
		@JsonProperty("收件人电话 ")
		public String recvrTel;// 收件人电话
		@JsonProperty("收件人地址 ")
		public String recvrAddr;// 收件人地址
		@JsonProperty("收件人邮箱")
		public String recvrEml;// 收件人邮箱
		@JsonProperty("买家留言")
		public String buyerNote;// 买家留言
		@JsonProperty("是否拣货（是否生成拣货单标识） ")
		public Integer isPick;// 是否拣货（是否生成拣货单标识）
		@JsonProperty("是否自发货（0否 1是）")
		public Integer deliverSelf;// 是否自发货（0否 1是）
		@JsonProperty("是否生成凭证")
		public Integer isNtMakeVouch;// 是否生成凭证
		@JsonProperty("制凭证人")
		public String makVouchPers;// 制凭证人
		@JsonProperty("制凭证时间")
		public String makVouchTm;// 制凭证时间
		@JsonProperty("序号")
		public Long ordrNum;// 序号
		@JsonProperty("仓库编码")
		public String whsEncd;// 仓库编码
		@JsonProperty("存货编码")
		public String invtyEncd;// 存货编码
		@JsonProperty("预计发货日期")
		public String expctDelvDt;// 预计发货日期
		@JsonProperty("数量")
		public BigDecimal qty;// 数量
		@JsonProperty("箱数")
		public BigDecimal bxQty;// 箱数
		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;// 含税单价
		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;// 价税合计
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;// 无税单价
		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;// 无税金额
		@JsonProperty("税额")
		public BigDecimal taxAmt;// 税额
		@JsonProperty("税率")
		public BigDecimal taxRate;// 税率
		@JsonProperty("批号")
		public String batNum;// 批号
		@JsonProperty("国际批次")
		public String intlBat;// 国际批次
		@JsonProperty("保质期")
		public Integer baoZhiQi;// 保质期
		@JsonProperty("生产日期")
		public String prdcDt;// 生产日期
		@JsonProperty("失效日期")
		public String invldtnDt;// 失效日期
		@JsonProperty("是否退货")
		public Integer isNtRtnGoods;// 是否退货
		@JsonProperty("是否赠品")
		public Integer isComplimentary;// 是否赠品
		@JsonProperty("可退数量")
		public BigDecimal rtnblQty;// 可退数量
		@JsonProperty("已退数量")
		public BigDecimal hadrtnQty;// 已退数量
		@JsonProperty("项目编码")
		public String projEncd;// 项目编码
		@JsonProperty("项目名称")
		public String projNm;// 项目名称
		@JsonProperty("折扣比例")
		public String discntRatio;// 折扣比例
		@JsonProperty("存货名称")
		public String invtyNm;// 存货名称
		@JsonProperty("规格型号")
		public String spcModel;// 规格型号
		@JsonProperty("存货代码")
		public String invtyCd;// 存货代码
		@JsonProperty("箱规")
		public BigDecimal bxRule;// 箱规
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
		@JsonProperty("仓库名称")
		public String whsNm;// 仓库名称
		@JsonProperty("对应条形码")
		public String crspdBarCd;// 对应条形码
		@JsonProperty("应税劳务")
		public Integer shdTaxLabour;// 应税劳务
		@JsonProperty("是否折扣")
		public Integer isNtDiscnt;// 是否折扣
		@JsonProperty("未开票数量")
		public BigDecimal unBllgQty;// 未开票数量
		@JsonProperty("表体备注")
		public String memos;// 表体备注

	}
}
