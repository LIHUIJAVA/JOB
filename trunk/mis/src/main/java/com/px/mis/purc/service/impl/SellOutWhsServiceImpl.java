package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.service.SellOutWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

/*销售出库单功能*/
@Transactional
@Service
public class SellOutWhsServiceImpl extends poiTool implements SellOutWhsService {
	@Autowired
	private SellOutWhsDao sowd;// 销售出库单主表
	@Autowired
	private SellOutWhsSubDao sowsd;// 销售出库单子表
	@Autowired
	private InvtyTabDao itd;// 库存表
	@Autowired
	InvtyGdsBitListMapper bitListMapper;// 货位存货单号对应表
	@Autowired
	InvtyNumMapper invtyNumMapper;// 货位
	@Autowired
	private WhsDocMapper whsDocMapper;// 仓库档案
	
	private Logger logger=LoggerFactory.getLogger(SellOutWhsServiceImpl.class);

	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;



	@Override
	public String addSellOutWhs(String userId, SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSubList,String loginTime) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellOutWhsSub> sellOutWhsSubSet = new TreeSet();
//			sellOutWhsSubSet.addAll(sellOutWhsSubList);
//			if(sellOutWhsSubSet.size() < sellOutWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellOutWhs/addSellOutWhs", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！",null);
//				return resp;
//			}

			// 获取订单号
			String number = getOrderNo.getSeqNo("CK", userId,loginTime);
			if (sowd.selectSellOutWhsById(number) != null) {
				message = "编码" + number + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				sellOutWhs.setOutWhsId(number);
				if (sellOutWhs.getBizTypId().equals("1")) {
					sellOutWhs.setRecvSendCateId("7");// 收发类型编码，线下销售
				}
				if (sellOutWhs.getBizTypId().equals("2")) {
					sellOutWhs.setRecvSendCateId("6");// 收发类型编码，线上销售
				}
				for (SellOutWhsSub sellOutWhsSub : sellOutWhsSubList) {
					sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());
					if (sellOutWhsSub.getPrdcDt() == "") {
						sellOutWhsSub.setPrdcDt(null);
					}
					if (sellOutWhsSub.getInvldtnDt() == "") {
						sellOutWhsSub.setInvldtnDt(null);
					}
				}
				sowd.insertSellOutWhs(sellOutWhs);
				sowsd.insertSellOutWhsSub(sellOutWhsSubList);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("purc/SellOutWhs/addSellOutWhs", isSuccess, message, sellOutWhs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 修改销售出库单
	@Override
	public String editSellOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSubList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<SellOutWhsSub> sellOutWhsSubSet = new TreeSet();
//			sellOutWhsSubSet.addAll(sellOutWhsSubList);
//			if(sellOutWhsSubSet.size() < sellOutWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/SellOutWhs/editSellOutWhs", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！",null);
//				return resp;
//			}
			if (sellOutWhs.getBizTypId().equals("1")) {
				sellOutWhs.setRecvSendCateId("7");// 收发类型编码，线下销售
			}
			if (sellOutWhs.getBizTypId().equals("2")) {
				sellOutWhs.setRecvSendCateId("6");// 收发类型编码，线上销售
			}
			for (SellOutWhsSub sellOutWhsSub : sellOutWhsSubList) {
				sellOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());
				if (sellOutWhsSub.getPrdcDt() == "") {
					sellOutWhsSub.setPrdcDt(null);
				}
				if (sellOutWhsSub.getInvldtnDt() == "") {
					sellOutWhsSub.setInvldtnDt(null);
				}
			}
			sowsd.deleteSellOutWhsSubByOutWhsId(sellOutWhs.getOutWhsId());
			sowd.updateSellOutWhsByOutWhsId(sellOutWhs);
			sowsd.insertSellOutWhsSub(sellOutWhsSubList);
			message = "更新成功！";
			System.out.println(sellOutWhs);
			resp = BaseJson.returnRespObj("purc/SellOutWhs/editSellOutWhs", isSuccess, message, sellOutWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deleteSellOutWhs(String outWhsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (sowd.selectSellOutWhsByOutWhsId(outWhsId) != null) {
			sowd.deleteSellOutWhsByOutWhsId(outWhsId);
			sowsd.deleteSellOutWhsSubByOutWhsId(outWhsId);
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编码" + outWhsId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/SellOutWhs/deleteSellOutWhs", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellOutWhs(String outWhsId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		SellOutWhs sellOutWhs = sowd.selectSellOutWhsByOutWhsId(outWhsId);
		if (sellOutWhs != null) {
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + outWhsId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/SellOutWhs/querySellOutWhs", isSuccess, message, sellOutWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String querySellOutWhsList(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// 销售出库单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		
		//仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));//仓库编码
		map.put("whsIdList", whsIdList);
		

		List<SellOutWhs> poList = sowd.selectSellOutWhsList(map);
		int count = sowd.selectSellOutWhsCount(map);
		
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
	@Override
	public String deleteSellOutWhsList(String outWhsId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(outWhsId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (sowd.selectIsNtChkByOutWhsId(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteSellOutWhsList(lists2);
				} catch (Exception e) {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
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
			resp = BaseJson.returnRespObj("purc/SellOutWhs/deleteSellOutWhsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteSellOutWhsList(List<String> lists2) {
		sowd.insertSellOutWhsDl(lists2);
		sowsd.insertSellOutWhsSubDl(lists2);
		int a = sowd.deleteSellOutWhsList(lists2);
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

	// 销售出库单审核
	@Override
	public Map<String, Object> updateSellOutWhsIsNtChk(SellOutWhs selOutWhs) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
//			int isNtRtnGood = sowd.selectIsNtRtnGoodByOutWhsId(selOutWhs.getOutWhsId());//根据出库单号查询销售出库单的退货状态
			SellOutWhs sellOutWhs = sowd.selectSellOutWhsAndSub(selOutWhs.getOutWhsId());
			if (sellOutWhs.getIsNtRtnGood() == 0) {
				if (selOutWhs.getIsNtChk() == 1) {
					message.append(updateSellOutWhsIsNtChkOK(sellOutWhs, selOutWhs).get("message"));
				} else if (selOutWhs.getIsNtChk() == 0) {
					message.append(updateSellOutWhsIsNtChkNO(sellOutWhs, selOutWhs).get("message"));
				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			} else if (sellOutWhs.getIsNtRtnGood() == 1) {
				if (selOutWhs.getIsNtChk() == 1) {
					message.append(updateReturnSellWhsIsNtChkOK(sellOutWhs, selOutWhs).get("message"));
				} else if (selOutWhs.getIsNtChk() == 0) {
					message.append(updateReturnSellWhsIsNtChkNO(sellOutWhs, selOutWhs).get("message"));
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

	// 销售出库单审核
	public Map<String, Object> updateSellOutWhsIsNtChkOK(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// 出库单子表
		if (sellOutWhs.getIsNtChk() == 0) {
			Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);//提高效率,遇并发直接串行化
			if (updateFlag==0) {
				throw new RuntimeException("单据已审核，不需要重复审核");
			}
			sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
			InvtyTab invtyTab = new InvtyTab();// 库存表
			String whsId="";
			for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
				invtyTab = new InvtyTab();// 库存表
				invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
				invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
				invtyTab.setBatNum(seOutWhsSubs.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);//加锁了
				if (invtyTab != null) {
					// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
					if (invtyTab.getNowStok().compareTo(seOutWhsSubs.getQty()) >= 0) {
						// 如果现存量足够，则修改库存表中的现存量和金额，通过现存量和金额计算其他有关金额的字段
						// invtyTab.setAvalQty(seOutWhsSubs.getQty());
						// itd.updateInvtyTabAvalQtyJian(invtyTab);
						itd.updateInvtyTabJianToSellOutWhs(seOutWhsSubs);
					} else {
						isSuccess = false;
						message += "编码为：" + selOutWhs.getOutWhsId() + "的销售出库单中, 仓库：" + seOutWhsSubs.getWhsEncd() + ",存货:" + seOutWhsSubs.getInvtyEncd() + "，批次"
								+ invtyTab.getBatNum() + "的库存中数量不足，无法审核\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "编码为：" + selOutWhs.getOutWhsId() + "的销售出库单中,仓库：" + seOutWhsSubs.getWhsEncd() + ",存货：" + seOutWhsSubs.getInvtyEncd() + ",批次："
							+ seOutWhsSubs.getBatNum() + "的库存不存在，无法审核\n";
					throw new RuntimeException(message);
				}
				whsId=seOutWhsSubs.getWhsEncd();//仓库编码
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if(whs != null && whs==1) {
				sellOutWhsHuoWeiJian(selOutWhs.getOutWhsId(),sellOutWhsSubs);
			}
			if (updateFlag==1) {
				isSuccess = true;
				message += selOutWhs.getOutWhsId() + "销售出库单审核成功！\n";
			}
		} else {
			isSuccess = false;
			message += selOutWhs.getOutWhsId() + "单据已审核，不需要重复审核\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 销售出库单弃审
	public Map<String, Object> updateSellOutWhsIsNtChkNO(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();

		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// 出库单子表
		// 判断销售出库单是否审核
		if (sellOutWhs.getIsNtChk() == 1) {
			// 判断销售出库单是否开票
			if (sellOutWhs.getIsNtBllg() == 0) {
				// 判断销售出库单是否记账
				if (sellOutWhs.getIsNtBookEntry() == 0) {
					sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
					String whsId="";
					for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
						InvtyTab invtyTab = new InvtyTab();// 库存表
						invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
						invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
						invtyTab.setBatNum(seOutWhsSubs.getBatNum());

						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						
						if (invtyTab != null) {
//							invtyTab.setAvalQty(seOutWhsSubs.getQty());
//							itd.updateInvtyTabAvalQtyJia(invtyTab);
							itd.updateInvtyTabJiaToSellOutWhs(seOutWhsSubs);
						} else {
							isSuccess = false;
							message += "编码为：" + selOutWhs.getOutWhsId() + "的销售出库单中,仓库：" + seOutWhsSubs.getWhsEncd() + ",存货：" + seOutWhsSubs.getInvtyEncd()
									+ ",批次：" + seOutWhsSubs.getBatNum() + "的库存不存在，无法弃审\n";
							throw new RuntimeException(message);
						}
						whsId=seOutWhsSubs.getWhsEncd();//仓库编码
					}
					Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);
					
					Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
					if(whs != null && whs==1) {
						sellOutWhsHuoWeiJia(selOutWhs.getOutWhsId(),sellOutWhsSubs);
					}
					if (updateFlag==1) {
						isSuccess = true;
						message += "编码为：" + selOutWhs.getOutWhsId() + "的销售出库单弃审成功！\n";
					}
					else {
						isSuccess=false;
						message+="编码为：" + selOutWhs.getOutWhsId() + "的销售出库单已弃审,不需要重复弃审!\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "单据号为" + selOutWhs.getOutWhsId() + "的销售出库单已记账，不能弃审\n";
				}
			} else {
				isSuccess = false;
				message += "单据号为" + selOutWhs.getOutWhsId() + "的销售出库单已开发票，不能弃审\n";
			}
		} else {
			isSuccess = false;
			message += "编码为：" + selOutWhs.getOutWhsId() + "的销售出库单未审核，不需要弃审\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 红字销售出库单审核
	public Map<String, Object> updateReturnSellWhsIsNtChkOK(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// 出库单子表

		// 判断红字销售出库单是否审核
		if (sellOutWhs.getIsNtChk() == 0) {
			sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
			String whsId="";
			for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
				InvtyTab invtyTab = new InvtyTab();// 库存表
				invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
				invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
				invtyTab.setBatNum(seOutWhsSubs.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
				if (invtyTab != null) {
					itd.updateInvtyTabJianToSellOutWhs(seOutWhsSubs);
				} else {
					seOutWhsSubs.setQty(seOutWhsSubs.getQty().abs());
					itd.insertInvtyTabBySellOutWhsSub(seOutWhsSubs);
//					isSuccess = false;
//					message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单中,存货编码：" + seOutWhsSubs.getInvtyEncd()
//							+ ",批次：" + seOutWhsSubs.getBatNum() + "的库存不存在，无法审核\n";
//					throw new RuntimeException(message);
				}
				whsId=seOutWhsSubs.getWhsEncd();
			}
			Integer updateFlag = sowd.updateSellOutWhsIsNtChk(selOutWhs);
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if(whs != null && whs==1) {
				sellOutWhsHuoWeiJia(selOutWhs.getOutWhsId(),sellOutWhsSubs);
			}
			if (updateFlag==1) {
				isSuccess = true;
				message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单审核成功！\n";
			}else {
				isSuccess = false;
				message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单已审核，不需要重复审核\n";
				throw new RuntimeException(message);
			}
			
		} else {
			isSuccess = false;
			message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}
	
	// 红字销售出库单弃审
	public Map<String, Object> updateReturnSellWhsIsNtChkNO(SellOutWhs sellOutWhs, SellOutWhs selOutWhs) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<SellOutWhsSub> sellOutWhsSubs = new ArrayList<>();// 出库单子表
		// 判断红字销售出库单是否审核
		if (sellOutWhs.getIsNtChk() == 1) {
			// 判断销售出库单是否开票
			if (sellOutWhs.getIsNtBllg() == 0) {
				// 判断销售出库单是否记账
				if (sellOutWhs.getIsNtBookEntry() == 0) {
					sellOutWhsSubs = sellOutWhs.getSellOutWhsSub();
					String whsId="";
					for (SellOutWhsSub seOutWhsSubs : sellOutWhsSubs) {
						InvtyTab invtyTab = new InvtyTab();// 库存表
						invtyTab.setWhsEncd(seOutWhsSubs.getWhsEncd());
						invtyTab.setInvtyEncd(seOutWhsSubs.getInvtyEncd());
						invtyTab.setBatNum(seOutWhsSubs.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						if (invtyTab != null) {
							// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
							if (invtyTab.getNowStok().compareTo(seOutWhsSubs.getQty().abs()) >= 0) {
								// 如果现存量足够，则修改库存表中的现存量和金额，通过现存量和金额计算其他有关金额的字段
								itd.updateInvtyTabJiaToSellOutWhs(seOutWhsSubs);
							} else {
								isSuccess = false;
								message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单中,仓库：" + seOutWhsSubs.getWhsEncd() + 
										",存货:" + seOutWhsSubs.getInvtyEncd() + ",批次:" + seOutWhsSubs.getBatNum()
										+ "的库存中数量不足，无法弃审\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单中,仓库：" + seOutWhsSubs.getWhsEncd()+",存货："
									+ seOutWhsSubs.getInvtyEncd() + ",批次：" + seOutWhsSubs.getBatNum() + "的库存不存在，无法弃审\n";
							throw new RuntimeException(message);
						}
						whsId=seOutWhsSubs.getWhsEncd();
					}
					Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
					if(whs != null && whs==1) {
						sellOutWhsHuoWeiJian(selOutWhs.getOutWhsId(),sellOutWhsSubs);
					}
					int a = sowd.updateSellOutWhsIsNtChk(selOutWhs);
					if (a >= 1) {
						isSuccess = true;
						message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单弃审成功！\n";
					} else {
						isSuccess = false;
						message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单弃审失败！\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "单据号为" + selOutWhs.getOutWhsId() + "的销售出库单已记账，不能弃审\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "单据号为" + selOutWhs.getOutWhsId() + "的销售出库单已开发票，不能弃审\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "编码为：" + selOutWhs.getOutWhsId() + "的红字销售出库单未审核，不需要弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	private void sellOutWhsHuoWeiJia(String outWhsId, List<SellOutWhsSub> selOutWhsSubList) {
		/**
		 * 货位判断
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(outWhsId);// 查询生成的采购入库单中是否有录入货位信息
		if (tabs.size() == 0) {
			throw new RuntimeException("部分明细没有录入货位信息,请核对后再操作");
		}
		for (SellOutWhsSub sellOutWhsSub : selOutWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(sellOutWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(sellOutWhsSub.getInvtyEncd());
			bitTab.setBatNum(sellOutWhsSub.getBatNum());
			bitTab.setSerialNum(sellOutWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(outWhsId);
			//查询该单据该存货所有货位总数量
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);
			// 判断该单据该存货所有货位总数量是否不大于单据存货总数量一致
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + 
						"批次：" + bitTab.getBatNum() + "没有录入对应的货位信息");
			} else if (bitTab2.getQty().abs().compareTo(sellOutWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException("存货：" + bitTab.getInvtyEncd() + "批次："
						+ bitTab.getBatNum() + " 录入的货位数量与入库数量不匹配,请核对货位数量再操作");
			}
		}
		/* 货位信息 */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// 查询库存表中是否存在该货位信息
			if (whsTab == null) {
				invtyNumMapper.insertMovBitTabJia(tab);// 不存在时新增
			} else {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJiaMbit(tab);// 存在时修改
			}
		}
	}
	
	private void sellOutWhsHuoWeiJian(String outWhsId, List<SellOutWhsSub> selOutWhsSubList) {
		/**
		 * 货位判断
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(outWhsId);// 查询生成的采购入库单中是否有录入货位信息
		if (tabs.size() == 0) {
			throw new RuntimeException("部分明细没有录入货位信息,请核对后再操作");
		}
		for (SellOutWhsSub sellOutWhsSub : selOutWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(sellOutWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(sellOutWhsSub.getInvtyEncd());
			bitTab.setBatNum(sellOutWhsSub.getBatNum());
			bitTab.setSerialNum(sellOutWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(outWhsId);
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);// 判断该单据该存货所有货位总数量是否不大于单据存货总数量一致
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + 
						"批次：" + bitTab.getBatNum() + "没有录入对应的货位信息,请核对后再操作");
			} else if (bitTab2.getQty().abs().compareTo(sellOutWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException("存货：" + bitTab.getInvtyEncd() + "批次："
						+ bitTab.getBatNum() + " 录入的货位数量与入库数量不匹配,请核对货位数量再操作");
			}
		}
		/* 货位信息 */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// 查询库存表中是否存在该货位信息
			if (whsTab == null) {
				throw new RuntimeException("仓库：" + tab.getWhsEncd() + ",存货："
						+ tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "无货位数据,请核对后再操作");
			} else if (whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 1 || whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 0) {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJianMbit(tab);// 出库
			} else {
				throw new RuntimeException("仓库：" + tab.getWhsEncd() + ",存货："
						+ tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "对应的货位数量不足");
			}
		}
	}

	// 打印销售出库单
	@Override
	public String printingSellOutWhsList(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// 销售出库单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);

		//仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));//仓库编码
		map.put("whsIdList", whsIdList);
				
		List<zizhu> sellOutWhsList = sowd.printingSellOutWhsList(map);
		try {
//			
			resp = BaseJson.returnRespObjListAnno("purc/SellOutWhs/printingSellOutWhsList", true, "查询成功！", null,
					sellOutWhsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 出库明细表
	@Override
	public String querySellOutWhsByInvtyEncd(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// 销售出库单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		List<?> poList;
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","outWhsDt");
				map.put("sortOrder","desc");
			}
			poList = sowd.selectSellOutWhsByInvtyEncd(map);
			Map tableSums = sowd.selectSellOutWhsByInvtyEncdSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			int count = sowd.selectSellOutWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncd", true, "查询成功！", count,
						pageNo, pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			poList = sowd.selectSellOutWhsByInvtyEncd(map);
			try {
				resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncd", true, "查询成功！", poList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
	// 出库明细表--导出
		@Override
		public String querySellOutWhsByInvtyEncdPrint(Map map) {
			
			String resp = "";
			List<String> outWhsIdList = getList((String) map.get("outWhsId"));// 销售出库单号
			List<String> custIdList = getList((String) map.get("custId"));// 客户编码
			List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
			List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
			List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
			List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
			List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
			List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
			List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
			List<String> batNumList = getList((String) map.get("batNum"));// 批次
			List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次

			map.put("outWhsIdList", outWhsIdList);
			map.put("custIdList", custIdList);
			map.put("accNumList", accNumList);
			map.put("invtyEncdList", invtyEncdList);
			map.put("invtyClsEncdList", invtyClsEncdList);
			map.put("invtyCdList", invtyCdList);
			map.put("deptIdList", deptIdList);
			map.put("whsEncdList", whsEncdList);
			map.put("custOrdrNumList", custOrdrNumList);
			map.put("batNumList", batNumList);
			map.put("intlBatList", intlBatList);
			List<?> poList;
				poList = sowd.selectSellOutWhsByInvtyEncdPrint(map);
				
				try {
					resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsByInvtyEncdPrint", true, "查询成功！",poList);
				} catch (Exception e) {
				logger.error(e.getMessage());
				}
	
			return resp;
		}


	// 导入销售出库单
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellOutWhs> sellOutWhsMap = null;
		if (i == 0) {
			sellOutWhsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellOutWhsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
		List<SellOutWhs> sellOutWhsList = sellOutWhsMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellOutWhs>> sellOutWhsLists = Lists.partition(sellOutWhsList, 1000);

		for (List<SellOutWhs> sellOutWhs : sellOutWhsLists) {
			sowd.batchInsertSellOutWhs(sellOutWhs);
		}
		List<SellOutWhsSub> sellOutWhsSubList = new ArrayList<>();
		int flag = 0;
		for (SellOutWhs entry : sellOutWhsList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<SellOutWhsSub> tempList = entry.getSellOutWhsSub();
			tempList.forEach(s -> s.setOutWhsId(entry.getOutWhsId()));
			sellOutWhsSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (sellOutWhsSubList.size() >= 1000 || sellOutWhsMap.size() == flag) {
				sowsd.insertSellOutWhsSubUpload(sellOutWhsSubList);
				sellOutWhsSubList.clear();
			}
		}

		isSuccess = true;
		message = "销售出库单导入成功！";

		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/SellOutWhs/uploadSellOutWhsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, SellOutWhs> uploadScoreInfo(MultipartFile file) {
		Map<String, SellOutWhs> temp = new HashMap<>();
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

				String orderNo = GetCellData(r, "销售出库单编码");
				// 创建实体类
				SellOutWhs sellOutWhs = new SellOutWhs();
				if (temp.containsKey(orderNo)) {
					sellOutWhs = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellOutWhs.setSellTypId(GetCellData(r, "销售类型编码"));// 销售类型编码
				sellOutWhs.setOutWhsId(orderNo); // 销售出库单编码
				if (GetCellData(r, "销售出库单日期") == null || GetCellData(r, "销售出库单日期").equals("")) {
					sellOutWhs.setOutWhsDt(null);
				} else {
					sellOutWhs.setOutWhsDt(GetCellData(r, "销售出库单日期"));// 销售出库单日期
				}
				sellOutWhs.setCustId(GetCellData(r, "客户编码"));// 客户编码
				sellOutWhs.setCustOrdrNum(GetCellData(r, "客户订单号"));// 客户订单号
				sellOutWhs.setDeptId(GetCellData(r, "部门编码"));// 部门编码
				sellOutWhs.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				sellOutWhs.setUserName(GetCellData(r, "业务员名称"));// 用户名称
				sellOutWhs.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
				sellOutWhs.setBizTypId(GetCellData(r, "业务类型编码"));// 业务类型编码
				sellOutWhs.setRecvSendCateId(GetCellData(r, "收发类别编码"));// 收发类别编码
				sellOutWhs.setSellOrdrInd(GetCellData(r, "来源单据号"));// 来源单据号
//				sellOutWhs.setRtnGoodsId(GetCellData(r,"退货单号"));//退货单号
				sellOutWhs.setDealStat(GetCellData(r, "处理状态"));// 处理状态
				sellOutWhs.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					sellOutWhs.setSetupTm(null);
				} else {
					sellOutWhs.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				sellOutWhs.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellOutWhs.setModiTm(null);
				} else {
					sellOutWhs.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				sellOutWhs.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				sellOutWhs.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellOutWhs.setChkTm(null);
				} else {
					sellOutWhs.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				sellOutWhs.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue());// 是否记账
				sellOutWhs.setBookEntryPers(GetCellData(r, "记账人"));// 记账人
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					sellOutWhs.setBookEntryTm(null);
				} else {
					sellOutWhs.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellOutWhs.setIsNtBllg(new Double(GetCellData(r, "是否开票")).intValue());// 是否开票
				sellOutWhs.setIsNtStl(new Double(GetCellData(r, "是否结算")).intValue());// 是否结算
				sellOutWhs.setIsNtRtnGood(new Double(GetCellData(r, "是否退货")).intValue());// 是否退货
				sellOutWhs.setMemo(GetCellData(r, "主备注"));// 主备注
				sellOutWhs.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue());
				List<SellOutWhsSub> sellOutWhsSubList = sellOutWhs.getSellOutWhsSub();// 销售出库单子表
				if (sellOutWhsSubList == null) {
					sellOutWhsSubList = new ArrayList<>();
				}
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
				sellOutWhsSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//				sellOutWhsSub.setWhsNm(GetCellData(r, "仓库名称"));// 仓库名称
				sellOutWhsSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				sellOutWhsSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				sellOutWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				sellOutWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				sellOutWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				sellOutWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				sellOutWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				sellOutWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				sellOutWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				sellOutWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				sellOutWhsSub.setBatNum(GetCellData(r, "批次"));// 批次
				sellOutWhsSub.setIntlBat(GetCellData(r, "国际批次"));// 国际批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					sellOutWhsSub.setPrdcDt(null);
				} else {
					sellOutWhsSub.setPrdcDt(GetCellData(r, "生产日期"));// 生产日期
				}
				sellOutWhsSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue());// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					sellOutWhsSub.setInvldtnDt(null);
				} else {
					sellOutWhsSub.setInvldtnDt(GetCellData(r, "失效日期"));// 失效日期
				}
				sellOutWhsSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
//				sellOutWhsSub.setProjNm(GetCellData(r, "项目名称"));// 项目名称
				sellOutWhsSub.setGdsBitEncd(GetCellData(r, "货位编码"));// 货位编码
				sellOutWhsSub.setRegnEncd(GetCellData(r, "区域编码"));// 区域编码
				sellOutWhsSub.setMemo(GetCellData(r, "子备注"));// 子备注

				sellOutWhsSubList.add(sellOutWhsSub);
				sellOutWhs.setSellOutWhsSub(sellOutWhsSubList);
				temp.put(orderNo, sellOutWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e + ",文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, SellOutWhs> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellOutWhs> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "出库单号");
				// 创建实体类
				SellOutWhs sellOutWhs = new SellOutWhs();
				if (temp.containsKey(orderNo)) {
					sellOutWhs = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("普通销售")) {
					sellOutWhs.setSellTypId("1");// 销售类型编码

				} else if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("委托代销")) {
					sellOutWhs.setSellTypId("2");// 销售类型编码

				}
				sellOutWhs.setOutWhsId(orderNo); // 销售出库单编码
				if (GetCellData(r, "出库日期") == null || GetCellData(r, "出库日期").equals("")) {
					sellOutWhs.setOutWhsDt(null);
				} else {
					sellOutWhs.setOutWhsDt(GetCellData(r, "出库日期"));// 销售出库单日期
				}
				sellOutWhs.setCustId(GetCellData(r, "客户编码"));// 客户编码
				sellOutWhs.setCustOrdrNum(GetCellData(r, "客户订单号"));// 客户订单号
				sellOutWhs.setDeptId(GetCellData(r, "部门编码"));// 部门编码
				sellOutWhs.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				sellOutWhs.setUserName(GetCellData(r, "业务员"));// 用户名称
				
				
				if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2B")) {
					sellOutWhs.setBizTypId("1");
					sellOutWhs.setRecvSendCateId("7");// 收发类别编码
				} else if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2C")) {
					sellOutWhs.setBizTypId("2");
					sellOutWhs.setRecvSendCateId("6");// 收发类别编码

				}
				// 是否委托代销 //0表示销售出库 1表示委托代销
				if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("普通销售")) {
					sellOutWhs.setIsNtEntrsAgn(0);

				} else if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("委托代销")) {
					sellOutWhs.setIsNtEntrsAgn(1);
				}
				sellOutWhs.setSellOrdrInd(GetCellData(r, "发货单号"));// 来源单据号
//				sellOutWhs.setRtnGoodsId(GetCellData(r,"退货单号"));//退货单号
				sellOutWhs.setDealStat("处理中");// 处理状态
				sellOutWhs.setSetupPers(GetCellData(r, "制单人"));// 创建人
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					sellOutWhs.setSetupTm(null);
				} else {
					sellOutWhs.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				sellOutWhs.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellOutWhs.setModiTm(null);
				} else {
					sellOutWhs.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				sellOutWhs.setIsNtChk(1);// 是否审核
				sellOutWhs.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellOutWhs.setChkTm(null);
				} else {
					sellOutWhs.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				sellOutWhs.setIsNtBookEntry(0);// 是否记账
				sellOutWhs.setBookEntryPers(GetCellData(r, "记账人"));// 记账人
//				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
//					sellOutWhs.setBookEntryTm(null);
//				} else {
//					sellOutWhs.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
//				}
				sellOutWhs.setIsNtBllg(0);// 是否开票
				sellOutWhs.setIsNtStl(0);// 是否结算
				sellOutWhs.setIsNtRtnGood(
						GetBigDecimal(GetCellData(r, "数量"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// 是否退货
				if (sellOutWhs.getIsNtRtnGood()==0) {
					sellOutWhs.setFormTypEncd("009");// 单据类型编码

				} else if (sellOutWhs.getIsNtRtnGood()==1) {
					sellOutWhs.setFormTypEncd("010");// 单据类型编码
				}
				sellOutWhs.setMemo(GetCellData(r, "备注"));// 主备注
				sellOutWhs.setIsNtMakeVouch(0);
				List<SellOutWhsSub> sellOutWhsSubList = sellOutWhs.getSellOutWhsSub();// 销售出库单子表
				if (sellOutWhsSubList == null) {
					sellOutWhsSubList = new ArrayList<>();
				}
				SellOutWhsSub sellOutWhsSub = new SellOutWhsSub();
//				sellOutWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				sellOutWhsSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
				sellOutWhsSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				sellOutWhsSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
			

//				sellOutWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "单价"), 8));// 8表示小数位数 //含税单价
				sellOutWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "单价"), 8));// 8表示小数位数 //无税单价
				sellOutWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "金额"), 8));// 8表示小数位数 //无税金额
//				sellOutWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
//				sellOutWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "金额"), 8));// 8表示小数位数 //价税合计
//				sellOutWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				sellOutWhsSub.setIsNtRtnGoods(
						GetBigDecimal(GetCellData(r, "数量"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// 是否退货
				sellOutWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				sellOutWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				sellOutWhsSub.setBatNum(GetCellData(r, "批号"));// 批次
				sellOutWhsSub.setIntlBat(GetCellData(r, "国际批号"));// 国际批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					sellOutWhsSub.setPrdcDt(null);
				} else {
					sellOutWhsSub.setPrdcDt(GetCellData(r, "生产日期"));// 生产日期
				}
				sellOutWhsSub.setBaoZhiQi(new Double(GetCellData(r, "保质期")).intValue());// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					sellOutWhsSub.setInvldtnDt(null);
				} else {
					sellOutWhsSub.setInvldtnDt(GetCellData(r, "失效日期"));// 失效日期
				}
				sellOutWhsSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				sellOutWhsSub.setProjNm(GetCellData(r, "项目"));// 项目名称
//				sellOutWhsSub.setGdsBitEncd(GetCellData(r, "货位编码"));// 货位编码
//				sellOutWhsSub.setRegnEncd(GetCellData(r, "区域编码"));// 区域编码
				sellOutWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "发货单子表id")));
				sellOutWhsSub.setMemo(GetCellData(r, "表体备注"));// 子备注

				sellOutWhsSubList.add(sellOutWhsSub);
				sellOutWhs.setSellOutWhsSub(sellOutWhsSubList);
				temp.put(orderNo, sellOutWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e + ",文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}
	//分页+排序列表展示
	@Override
	public String querySellOutWhsListOrderBy(Map map) {
		String resp = "";
		List<String> outWhsIdList = getList((String) map.get("outWhsId"));// 销售出库单号
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次
		List<String> expressNumList = getList((String)map.get("expressNum"));

		map.put("outWhsIdList", outWhsIdList);
		map.put("custIdList", custIdList);
		map.put("accNumList", accNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("whsEncdList", whsEncdList);
		map.put("custOrdrNumList", custOrdrNumList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("expressNums", expressNumList);
		
		//仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));//仓库编码
		map.put("whsIdList", whsIdList);
		
		List<?> poList;
		if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
			map.put("sort","sow.out_whs_dt");
			map.put("sortOrder","desc");
		}
			poList = sowd.selectSellOutWhsListOrderBy(map);
			Map tableSums = sowd.selectSellOutWhsListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
		
		int count = sowd.selectSellOutWhsCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/SellOutWhs/querySellOutWhsListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList,tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public static class zizhu {
		// 主表
		@JsonProperty("出库编码")
		public String outWhsId;//出库编码
		@JsonProperty("出库日期")
	    public String outWhsDt;//出库日期
//		@JsonProperty("出库日期1")
//	    public String outWhsDt1;//出库日期
//		@JsonProperty("出库日期2")
//	    public String outWhsDt2;//出库日期
		@JsonProperty("单据类型编码")
	    public String formTypEncd;//单据类型编码
		@JsonProperty("单据类型名称")
	    public String formTypName;//单据类型名称
		@JsonProperty("用户编码")
	    public String accNum;//用户编码
		@JsonProperty("用户名称")
	    public String userName;//用户名称
		@JsonProperty("部门编码")
	    public String deptId;//部门编码
		@JsonProperty("部门名称")
	    public String deptName;//部门名称
		@JsonProperty("客户编码")
	    public String custId;//客户编码
		@JsonProperty("客户名称")
	    public String custNm;//客户名称
		@JsonProperty("业务类型编码")
	    public String bizTypId;//业务类型编码
		@JsonProperty("业务类型名称")
	    public String bizTypNm;//业务类型名称
		@JsonProperty("销售类型编码")
	    public String sellTypId;//销售类型编码
		@JsonProperty("销售类型名称")
	    public String sellTypNm;//销售类型名称
		@JsonProperty("收发类型编码")
	    public String recvSendCateId;//收发类型编码
		@JsonProperty("收发类别名称")
	    public String recvSendCateNm;//收发类别名称
		@JsonProperty("出入库类型编码")
	    public String outIntoWhsTypId;//出入库类型编码
		@JsonProperty("出入库类别名称")
	    public String outIntoWhsTypNm;//出入库类别名称
		@JsonProperty("销售订单标识")
	    public String sellOrdrInd;//销售订单标识
		@JsonProperty("是否审核")
	    public Integer isNtChk;//是否审核
		@JsonProperty("审核人")
	    public String chkr;//审核人
		@JsonProperty("审核时间")
	    public String chkTm;//审核时间
		@JsonProperty("是否记账")
	    public Integer isNtBookEntry;//是否记账
		@JsonProperty("记账人")
	    public String bookEntryPers;//记账人
		@JsonProperty("记账时间")
	    public String bookEntryTm;//记账时间
		@JsonProperty("创建人")
	    public String setupPers;//创建人
		@JsonProperty("创建时间")
	    public String setupTm;//创建时间
		@JsonProperty("修改人")
	    public String mdfr;//修改人
		@JsonProperty("修改时间")
	    public String modiTm;//修改时间
		@JsonProperty("是否委托代销")
	    public Integer isNtEntrsAgn;//是否委托代销 //0表示销售出库  1表示委托代销 
		@JsonProperty("处理状态")
	    public String dealStat;//处理状态（1.处理完成 0.处理中）
		@JsonProperty("表头备注")
	    public String memo;//备注
		@JsonProperty("退货单编码")
	    public String rtnGoodsId;//退货单编码（暂时不用，都用sellOrdrInd）
		@JsonProperty("是否开票")
	    public Integer isNtBllg;//是否开票
		@JsonProperty("是否结算")
	    public Integer isNtStl;//是否结算
		@JsonProperty("客户订单号")
	    public String custOrdrNum;//客户订单号
		@JsonProperty("是否有退货")
	    public Integer isNtRtnGood;//是否有退货
		@JsonProperty("拒收备注")
	    public String returnMemo;//拒收备注
		@JsonProperty("来源单据类型编码")
	    public String toFormTypEncd;//来源单据类型编码
		@JsonProperty("是否生成凭证")
	    public Integer isNtMakeVouch;//是否生成凭证
		@JsonProperty("制凭证人")
	    public String makVouchPers;//制凭证人
		@JsonProperty("制凭证时间")
	    public String makVouchTm;//制凭证时间
	    //子表
		@JsonProperty("序号")
	    public Long ordrNum;//序号
		@JsonProperty("仓库编码")
	    public String whsEncd;//仓库编码
		@JsonProperty("仓库名称")
	    public String whsNm;//仓库名称
		@JsonProperty("存货编码")
	    public String invtyEncd;//存货编码
		@JsonProperty("存货名称")
	    public String invtyNm;//存货名称
		@JsonProperty("规格型号")
	    public String spcModel;//规格型号
		@JsonProperty("存货代码")
	    public String invtyCd;//存货代码
		@JsonProperty("箱规")
	    public BigDecimal bxRule;//箱规
		@JsonProperty("数量")
	    public BigDecimal qty;//数量
		@JsonProperty("箱数")
	    public BigDecimal bxQty;//箱数
		@JsonProperty("含税单价")
	    public BigDecimal cntnTaxUprc;//含税单价
		@JsonProperty("价税合计")
	    public BigDecimal prcTaxSum;//价税合计
		@JsonProperty("无税单价")
	    public BigDecimal noTaxUprc;//无税单价
		@JsonProperty("无税金额")
	    public BigDecimal noTaxAmt;//无税金额
		@JsonProperty("税额")
	    public BigDecimal taxAmt;//税额
		@JsonProperty("税率")
	    public BigDecimal taxRate;//税率
		@JsonProperty("批次")
	    public String batNum;//批号
		@JsonProperty("国际批次")
	    public String intlBat;//国际批号
		@JsonProperty("保质期")
	    public Integer baoZhiQi;//保质期
		@JsonProperty("生产日期")
		public String prdcDt;//生产日期
		@JsonProperty("失效日期")
	    public String invldtnDt;//失效日期
		@JsonProperty("是否赠品")
	    public Integer isComplimentary;//是否赠品
		@JsonProperty("是否退货")
	    public Integer isNtRtnGoods;//是否退货
		@JsonProperty("拒收数量")
	    public BigDecimal returnQty;//拒收数量
//		@JsonProperty("货位编码")
//	    public String gdsBitEncd;//货位编码--------
		@JsonProperty("项目编码")
	    public String projEncd;//项目编码
		@JsonProperty("项目名称")
	    public String projNm;//项目名称
	   //联查存货档案字段、计量单位名称、仓库名称
		
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
		@JsonProperty("计量单位编码")
	    public String measrCorpId;//计量单位编码
		@JsonProperty("计量单位名称")
	    public String measrCorpNm;//计量单位名称
		@JsonProperty("对应条形码")
	    public String crspdBarCd;//对应条形码
//		@JsonProperty("存货分类编码")
//	    public String invtyClsEncd;//存货分类名称
//		@JsonProperty("存货分类名称")
//	    public String invtyClsNm;//存货分类名称
//		@JsonProperty("区域编码")
//	    public String regnEncd;//区域编码--------
		@JsonProperty("来源单据子表序号")
	    public Long toOrdrNum;//来源单据子表标识
		@JsonProperty("结转单价")
	    public BigDecimal carrOvrUprc;//结转单价
		@JsonProperty("结转金额")
	    public BigDecimal carrOvrAmt;//结转金额
		@JsonProperty("结转单价reba")
	    public BigDecimal rebaUprc;//结转单价
		@JsonProperty("结转金额reba")
	    public BigDecimal rebaAmt;//结转金额
		@JsonProperty("表体备注")
	    public String memos;//表体备注
	}
}
