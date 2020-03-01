package com.px.mis.purc.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.entity.PursOrdrSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.ToGdsSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.entity.InvtyTab;

/*到货单功能*/
@Transactional
@Service
public class ToGdsSnglServiceImpl extends poiTool implements ToGdsSnglService {
	@Autowired
	private ToGdsSnglDao tgsd;
	@Autowired
	private ToGdsSnglSubDao tgssd;
	@Autowired
	private InvtyTabDao itd;
	@Autowired
	private IntoWhsDao iwd;
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;
	@Autowired
	private ToGdsSnglSubDao toGdsSnglSubDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;
		
	// 新增到货单信息
	@Override
	public String addToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList,String loginTime) throws Exception{
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<ToGdsSnglSub> toGdsSnglSubSet = new TreeSet();
//			toGdsSnglSubSet.addAll(toGdsSnglSubList);
//			if (toGdsSnglSubSet.size() < toGdsSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/ToGdsSngl/addToGdsSngl", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！", null);
//				return resp;
//			}
			// 获取订单号
			String number = getOrderNo.getSeqNo("DH", userId,loginTime);
			if (tgsd.selectToGdsSnglById(number) != null) {
				message = "编码" + number + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				toGdsSngl.setToGdsSnglId(number);
				for (ToGdsSnglSub toGdsSnglSub : toGdsSnglSubList) {
					toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
					toGdsSnglSub.setUnIntoWhsQty(toGdsSnglSub.getQty().abs());// 未入库数量
					if (toGdsSngl.getIsNtRtnGood() == 0) {
						toGdsSnglSub.setReturnQty(toGdsSnglSub.getQty());// 未拒收数量
					}
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(toGdsSnglSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null) {
						isSuccess = false;
						message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
								+ "没有设置是否采购属性，无法保存！";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
								+ "非可采购存货，无法保存！";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
								+ "没有设置是否保质期管理属性，无法保存！";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (toGdsSnglSub.getPrdcDt() == "" || toGdsSnglSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
									+ "是保质期管理，请输入生产日期！";
							throw new RuntimeException(message);
						}
						if (toGdsSnglSub.getInvldtnDt() == ""|| toGdsSnglSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
									+ "是保质期管理，请输入失效日期！";
							throw new RuntimeException(message);
						}
					}
					if (toGdsSnglSub.getPrdcDt() == "") {
						toGdsSnglSub.setPrdcDt(null);
					}
					if (toGdsSnglSub.getInvldtnDt() == "") {
						toGdsSnglSub.setInvldtnDt(null);
					}
				}
				int a  = tgsd.insertToGdsSngl(toGdsSngl);
				int b = tgssd.insertToGdsSnglSub(toGdsSnglSubList);
				if(a>=1 && b>=1) {
					message = "新增成功！";
					isSuccess = true;
				}else {
					isSuccess = false;
					message = "新增失败！";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/addToGdsSngl", isSuccess, message, toGdsSngl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 修改到货单信息
	@Override
	public String editToGdsSngl(ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<ToGdsSnglSub> toGdsSnglSubSet = new TreeSet();
//			toGdsSnglSubSet.addAll(toGdsSnglSubList);
//			if (toGdsSnglSubSet.size() < toGdsSnglSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/ToGdsSngl/editToGdsSngl", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！",
//						null);
//				return resp;
//			}
			for (ToGdsSnglSub toGdsSnglSub : toGdsSnglSubList) {
				toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
				toGdsSnglSub.setUnIntoWhsQty(toGdsSnglSub.getQty().abs());// 未入库数量
				if (toGdsSngl.getIsNtRtnGood() == 0) {
					toGdsSnglSub.setReturnQty(toGdsSnglSub.getQty());// 未拒收数量
				}
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(toGdsSnglSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null || invtyDoc.getIsNtPurs().equals("")) {
					isSuccess = false;
					message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
							+ "没有设置是否采购属性，无法保存！";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
							+ "非可采购存货，无法保存！";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
							+ "没有设置是否保质期管理属性，无法保存！";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (toGdsSnglSub.getPrdcDt() == "" || toGdsSnglSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
								+ "是保质期管理，请输入生产日期！";
						throw new RuntimeException(message);
					}
					if (toGdsSnglSub.getInvldtnDt() == ""|| toGdsSnglSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "该到货单对应的存货:" + toGdsSnglSub.getInvtyEncd()
								+ "是保质期管理，请输入失效日期！";
						throw new RuntimeException(message);
					}
				}
				if (toGdsSnglSub.getPrdcDt() == "") {
					toGdsSnglSub.setPrdcDt(null);
				}
				if (toGdsSnglSub.getInvldtnDt() == "") {
					toGdsSnglSub.setInvldtnDt(null);
				}
			}
			int a = tgssd.deleteToGdsSnglSubByToGdsSnglId(toGdsSngl.getToGdsSnglId());// 删除到货单主表信息
			int b = tgsd.updateToGdsSnglByToGdsSnglId(toGdsSngl);// 修改到货单主表信息
			int c = tgssd.insertToGdsSnglSub(toGdsSnglSubList);// 新增到货单子表信息
			if(a>=1 && b>=1 && c>=1) {
				message = "更新成功！";
				isSuccess = true;
			}else {
				isSuccess = false;
				message = "更新失败！";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/editToGdsSngl", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 删除到货单信息
	@Override
	public String deleteToGdsSngl(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (tgsd.selectToGdsSnglByToGdsSnglId(toGdsSnglId) != null) {
			tgsd.deleteToGdsSnglByToGdsSnglId(toGdsSnglId);
			tgssd.deleteToGdsSnglSubByToGdsSnglId(toGdsSnglId);
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编码" + toGdsSnglId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/deleteToGdsSngl", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 根据到货单编码查询到货单信息
	@Override
	public String queryToGdsSngl(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<ToGdsSnglSub> toGdsSnglSub = new ArrayList<>(); */
		ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(toGdsSnglId);
		if (toGdsSngl != null) {
			/* toGdsSnglSub=tgssd.selectToGdsSnglSubByToGdsSnglId(toGdsSnglId); */
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编码" + toGdsSnglId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/queryToGdsSngl", isSuccess, message, toGdsSngl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 分页查询所有到货单信息
	@Override
	public String queryToGdsSnglList(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 客户编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单编码

		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<ToGdsSngl> poList = tgsd.selectToGdsSnglList(map);
		int count = tgsd.selectToGdsSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除到货单列表
	@Override
	public String deleteToGdsSnglList(String toGdsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(toGdsSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (tgsd.selectToGdsSnglIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteToGdsSnglList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "删除失败！\n";
				}
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "删除失败！\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
			}
			resp = BaseJson.returnRespObj("purc/ToGdsSngl/deleteToGdsSnglList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	public int deleteToGdsSnglList(List<String> lists2) {
			tgsd.insertToGdsSnglDl(lists2);
			tgssd.insertToGdsSnglSubDl(lists2);
		return tgsd.deleteToGdsSnglList(lists2);
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

	// 审核功能
	@Override
	public Map<String, Object> updateToGdsSnglIsNtChkList(ToGdsSngl tGdsSngl) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());
			if (toGdsSngl.getIsNtRtnGood() == 0) {
				if (tGdsSngl.getIsNtChk() == 1) {
					message.append(updateToGdsSnglIsNtChkOK(tGdsSngl).get("message"));
				} else if (tGdsSngl.getIsNtChk() == 0) {
					message.append(updateToGdsSnglIsNtChkNO(tGdsSngl).get("message"));
				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			} else if (toGdsSngl.getIsNtRtnGood() == 1) {
				if (tGdsSngl.getIsNtChk() == 1) {
					message.append(updateReturnToGdsSnglIsNtChkOK(tGdsSngl).get("message"));
				} else if (tGdsSngl.getIsNtChk() == 0) {
					message.append(updateReturnToGdsSnglIsNtChkNO(tGdsSngl).get("message"));
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

	// 到货单审核
	public Map<String, Object> updateToGdsSnglIsNtChkOK(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 0) {
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// 到货单主表
			if (toGdsSngl.getPursOrdrId() != null) {
				List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// 获得到货单子表 
				for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
					// 库存实体
//					InvtyTab invtyTab = new InvtyTab();
//					invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//					invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//					invtyTab.setBatNum(toGdsSub.getBatNum());
//					InvtyTab invtyTab1 = itd.selectInvtyTabsByTerm(invtyTab);
//					if (invtyTab1 == null) {
//						itd.insertInvtyTabByToGdsSngl(toGdsSub);
//					} else {
//						invtyTab1.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJia(invtyTab1);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// 序号
						BigDecimal unToGdsQty = pursOrdrSubDao.selectUnToGdsQtyByInvtyEncd(map);// 根据存货编码和采购订单编码查询未到货数量
						if (unToGdsQty != null) {
							if (unToGdsQty.compareTo(toGdsSub.getQty()) == 1
									|| unToGdsQty.compareTo(toGdsSub.getQty()) == 0) {
								map.put("unToGdsQty", toGdsSub.getQty());// 修改未到货数量
								pursOrdrSubDao.updatePursOrdrSubByInvtyEncd(map);// 根据存货编码修改采购订单中的未到货数量
							} else {
								isSuccess = false;
								message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单中存货【" + toGdsSub.getInvtyEncd()
										+ "】累计到货数量大于订单数量，无法审核！\n";
								throw new RuntimeException(message);
							}
						}
					}
				}
				int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单审核成功！\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单审核失败！\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单对应的采购订单不存在，无法审核\n";
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单已审核，不需要重复审核\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 到货单弃审
	public Map<String, Object> updateToGdsSnglIsNtChkNO(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		// 到货单子表集合
		List<ToGdsSnglSub> toGdsSnglSub = new ArrayList<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 1) {
			List<IntoWhs> intoWhsList = iwd.selectIntoWhsByToGdsSnglId(tGdsSngl.getToGdsSnglId());
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// 获得销售单主表
			toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// 获得销售单子表
			// 判断采购入库中有没有根据该到货单生成入库单，并判断审核状态
			if (intoWhsList.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单已存在下游单据【采购入库单】，无法弃审\n";
			} else {
				for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
					// 库存实体
//					InvtyTab invtyTab = new InvtyTab();
//					invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//					invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//					invtyTab.setBatNum(toGdsSub.getBatNum());
//					invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//					if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == 1
//							|| invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == 0) {
//						invtyTab.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJian(invtyTab);
//					} else if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty()) == -1) {
//						isSuccess = false;
//						message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单中,仓库:"+ toGdsSub.getWhsEncd()+ "存货：" + toGdsSub.getInvtyEncd()
//								+ "，批次：" + toGdsSub.getBatNum() + "的库存中数量不足，无法弃审\n";
//						throw new RuntimeException(message);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// 序号
						BigDecimal unToGdsQty = pursOrdrSubDao.selectUnToGdsQtyByInvtyEncd(map);// 根据存货编码和采购订单编码查询未到货数量
						if (unToGdsQty != null) {
							map.put("unToGdsQty", toGdsSub.getQty().multiply(new BigDecimal(-1)));
							pursOrdrSubDao.updatePursOrdrSubByInvtyEncd(map);// 根据序号修改采购订单中的未到货数量
						} else {
							isSuccess = false;
							message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单对应的采购订单中未到货数量不存在，无法弃审！\n";
							throw new RuntimeException(message);
						}
					}
				}
				int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单弃审成功！\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货单弃审失败！\n";
					throw new RuntimeException(message);//通过审核状态判定操作合法性
				}
			}
		} else {
			isSuccess = false;
			message += tGdsSngl.getToGdsSnglId() + "单据未审核，请先审核该单据\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 到货拒收单审核
	public Map<String, Object> updateReturnToGdsSnglIsNtChkOK(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 0) {
			// 到货单子表集合
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// 获得到货单主表
			List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// 获得销到货单子表
			for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
				// 库存实体
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//				invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//				invtyTab.setBatNum(toGdsSub.getBatNum());
//				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//				if (invtyTab == null) {
//					isSuccess = false;
//					message += "单据号为:" + tGdsSngl.getToGdsSnglId() + "的到货拒收单审核失败，由于没有该单据对应的存货信息，请检查库存中的存货是否存在！\n";
//					throw new RuntimeException(message);
//				} else {
//					if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == 1
//							|| invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == 0) {
//						invtyTab.setAvalQty(toGdsSub.getQty());
//						itd.updateInvtyTabAvalQtyJia(invtyTab);
//					} else if (invtyTab.getAvalQty().compareTo(toGdsSub.getQty().abs()) == -1) {
//						isSuccess = false;
//						message += tGdsSngl.getToGdsSnglId() + "中," + "存货" + invtyTab.getInvtyEncd() + "，批次"
//								+ invtyTab.getBatNum() + "的库存中数量不足，无法审核\n";
//						throw new RuntimeException(message);
//					}
					if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
						map.put("ordrNum", toGdsSub.getToOrdrNum());// 序号
						BigDecimal returnQty = toGdsSnglSubDao.selectUnReturnQtyByOrdrNum(map);// 根据子表序号查询未拒收数量
						if (returnQty != null) {
							if (returnQty.compareTo(toGdsSub.getQty().abs()) == 1
									|| returnQty.compareTo(toGdsSub.getQty().abs()) == 0) {
								map.put("returnQty", toGdsSub.getQty().abs());
								toGdsSnglSubDao.updateToGdsSnglSubUnReturnQty(map);// 根据序号修改到货单中的未拒收数量
							} else {
								isSuccess = false;
								message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货拒收单中存货【" + toGdsSub.getInvtyEncd()
										+ "】累计拒收数量大于到货数量，无法审核！\n";
								throw new RuntimeException(message);
							}

						} else {
							isSuccess = false;
							message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货拒收单对应的到货单中未拒收数量不存在，无法审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货拒收单没有来源子表序号，无法审核！\n";
						throw new RuntimeException(message);
					}
//				}
			}
			int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
			if (a >= 1) {
				isSuccess = true;
				message += "单号为" + tGdsSngl.getToGdsSnglId() + "的到货拒收单审核成功！\n";
			} else {
				isSuccess = false;
				message += "单号为" + tGdsSngl.getToGdsSnglId() + "的到货拒收单审核失败!禁止重复审核\\n";
			}
		} else {
			isSuccess = false;
			message += "单据为" + tGdsSngl.getToGdsSnglId() + "的到货拒收单已审核，不需要重复审核\n";
		}

		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 到货拒收单弃审
	public Map<String, Object> updateReturnToGdsSnglIsNtChkNO(ToGdsSngl tGdsSngl) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (tgsd.selectToGdsSnglIsNtChk(tGdsSngl.getToGdsSnglId()) == 1) {
			// 到货单子表集合
			ToGdsSngl toGdsSngl = tgsd.selectToGdsSnglByToGdsSnglId(tGdsSngl.getToGdsSnglId());// 获得到货单主表
			List<ToGdsSnglSub> toGdsSnglSub = toGdsSngl.getToGdsSnglSub();// 获得销到货单子表
			for (ToGdsSnglSub toGdsSub : toGdsSnglSub) {
				// 库存实体
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(toGdsSub.getWhsEncd());
//				invtyTab.setInvtyEncd(toGdsSub.getInvtyEncd());
//				invtyTab.setBatNum(toGdsSub.getBatNum());
//				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
//				if (invtyTab == null) {
//					isSuccess = false;
//					message += "单据号为:" + tGdsSngl.getToGdsSnglId() + "的到货拒收单弃审失败，由于没有该单据对应的存货信息，请检查库存中的存货是否存在！\n";
//				} else {
//					invtyTab.setAvalQty(toGdsSub.getQty());
//					itd.updateInvtyTabAvalQtyJian(invtyTab);
//				}
				if (toGdsSub.getToOrdrNum() != null && toGdsSub.getToOrdrNum() != 0) {
					map.put("ordrNum", toGdsSub.getToOrdrNum());// 序号
					BigDecimal returnQty = toGdsSnglSubDao.selectUnReturnQtyByOrdrNum(map);// 根据子表序号查询未拒收数量
					if (returnQty != null) {
						map.put("returnQty", toGdsSub.getQty());
						toGdsSnglSubDao.updateToGdsSnglSubUnReturnQty(map);// 根据序号修改到货单中的未拒收数量
					} else {
						isSuccess = false;
						message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货拒收单对应的到货单中未拒收数量不存在，无法弃审！\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "单据号为：" + tGdsSngl.getToGdsSnglId() + "的到货拒收单没有来源子表序号，无法弃审！\n";
					throw new RuntimeException(message);
				}

			}
			int a = tgsd.updateToGdsSnglIsNtChk(tGdsSngl);
			if (a >= 1) {
				isSuccess = true;
				message += "单据号为:" + tGdsSngl.getToGdsSnglId() + "的到货拒收单弃审成功！\n";
			} else {
				isSuccess = false;
				message += "单据号为:" + tGdsSngl.getToGdsSnglId() + "的到货拒收单弃审失败！\\n";
			}
		} else {
			isSuccess = false;
			message += "单据号为:" + tGdsSngl.getToGdsSnglId() + "的到货拒收单未审核，请先审核该单据\n";
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 打印输入输出到货单信息
	@Override
	public String printingToGdsSnglList(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// 到货单号
		List<String> provrIdList = getList(((String) map.get("provrId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货分类编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// 供应商订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// 采购订单编码
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		
		List<zizhu> toGdsSnglList = tgsd.printingToGdsSnglList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("purc/ToGdsSngl/printingToGdsSnglList", true, "查询成功！", null,
					toGdsSnglList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 到货明细查询
	@Override
	public String queryToGdsSnglByInvtyEncd(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// 到货单号
		List<String> provrIdList = getList(((String) map.get("provrId")));// 供应商编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货分类编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// 供应商订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// 采购订单编码
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null||map.get("sort").equals("") ||
				map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
				map.put("sort","toGdsSnglDt");
				map.put("sortOrder","desc");
			}
			List<Map> poList = tgsd.selectToGdsSnglByInvtyEncd(map);
			Map tableSums = tgsd.selectToGdsSnglListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			int count = tgsd.selectToGdsSnglByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;

			try {
				resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglByInvtyEncd", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = tgsd.selectToGdsSnglByInvtyEncd(map);
			Map tableSums = tgsd.selectToGdsSnglListSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			try {
				resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglByInvtyEncd", true, "查询成功！", poList,tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}

	// 导入到货单
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		Map<String, ToGdsSngl> toGdsSnglMap = null;

		if (i == 0) {
			toGdsSnglMap = uploadScoreInfo(file);
		} else if (i == 1) {
			toGdsSnglMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}
		
		//将Map转为List，然后批量插入父表数据
		List<ToGdsSngl> toGdsSnglList = toGdsSnglMap.entrySet().stream().map(e -> e.getValue())
			.collect(Collectors.toList());
		List<List<ToGdsSngl>> toGdsSnglLists=Lists.partition(toGdsSnglList,1000);
		
		for(List<ToGdsSngl> toGdsSngl :toGdsSnglLists){
			tgsd.insertToGdsSnglUpload(toGdsSngl);
		}
		List<ToGdsSnglSub> toGdsSnglSubList = new ArrayList<>();
		int flag=0;
		for (ToGdsSngl entry : toGdsSnglList) {
			flag++;
			//批量设置字表和父表的关联字段
			List<ToGdsSnglSub> tempList = entry.getToGdsSnglSub();
			tempList.forEach(s -> s.setToGdsSnglId(entry.getToGdsSnglId()));
			toGdsSnglSubList.addAll(tempList);
			//批量插入，每大于等于1000条插入一次
			if(toGdsSnglSubList.size() >= 1000 || toGdsSnglMap.size()==flag){
				tgssd.insertToGdsSnglSub(toGdsSnglSubList);
				toGdsSnglSubList.clear();
			}
		}
		isSuccess = true;
		message = "到货单导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, ToGdsSngl> uploadScoreInfo(MultipartFile file) {
		Map<String, ToGdsSngl> temp = new HashMap<>();
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
			//设置列名称
			getCellNames();
			// 对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				// 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if (r.getRowNum() < 1) {
					continue;
				}
				String orderNo = GetCellData(r, "到货单编码");
				// 创建实体类
				ToGdsSngl toGdsSngl = new ToGdsSngl();
				if (temp.containsKey(orderNo)) {
					toGdsSngl = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				toGdsSngl.setPursTypId(GetCellData(r, "采购类型编码"));// 采购类型编码
//				toGdsSngl.setPursTypNm(GetCellData(r,"采购类型名称"));//采购类型名称
				toGdsSngl.setToGdsSnglId(orderNo);// 到货单编码
				if (GetCellData(r, "到货单日期") == null || GetCellData(r, "到货单日期").equals("")) {
					toGdsSngl.setToGdsSnglDt(null);
				} else {
					toGdsSngl.setToGdsSnglDt(GetCellData(r, "到货单日期").replaceAll("[^0-9:-]", " "));// 到货单日期
				}
				toGdsSngl.setProvrId(GetCellData(r, "供应商编码"));// 供应商编码
//				toGdsSngl.setProvrNm(GetCellData(r,"供应商名称"));//供应商名称
				toGdsSngl.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				toGdsSngl.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//				toGdsSngl.setDepName(GetCellData(r,"部门名称"));//部门名称
				toGdsSngl.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				toGdsSngl.setUserName(GetCellData(r, "业务员名称"));// 用户名称
				toGdsSngl.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型
				toGdsSngl.setPursOrdrId(GetCellData(r, "采购订单编码"));// 采购订单编码
				toGdsSngl.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					toGdsSngl.setSetupTm(null);
				} else {
					toGdsSngl.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				toGdsSngl.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				toGdsSngl.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					toGdsSngl.setChkTm(null);
				} else {
					toGdsSngl.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				toGdsSngl.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					toGdsSngl.setModiTm(null);
				} else {
					toGdsSngl.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				toGdsSngl.setMemo(GetCellData(r, "表头备注"));// 备注
				toGdsSngl.setIsNtRtnGood(new Double(GetCellData(r, "是否退货")).intValue());// 是否退货
				toGdsSngl.setToFormTypEncd(GetCellData(r, "来源单据类型编码"));// 来源单据类型编码

				List<ToGdsSnglSub> toGdsSnglSubList = toGdsSngl.getToGdsSnglSub();// 到货单子表
				if (toGdsSnglSubList == null) {
					toGdsSnglSubList = new ArrayList<>();
				}
				ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
				toGdsSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				toGdsSnglSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//				toGdsSnglSub.setWhsNm(GetCellData(r,"仓库名称"));//仓库名称
				toGdsSnglSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				toGdsSnglSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				toGdsSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				toGdsSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				toGdsSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				toGdsSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				toGdsSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				toGdsSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				toGdsSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				toGdsSnglSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "未入库数量"), 8));// 8表示小数位数 //未入库数量
				toGdsSnglSub.setBatNum(GetCellData(r, "批次"));// 批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					toGdsSnglSub.setPrdcDt(null);
				} else {
					toGdsSnglSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				toGdsSnglSub.setBaoZhiQi(GetCellData(r, "保质期"));// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					toGdsSnglSub.setInvldtnDt(null);
				} else {
					toGdsSnglSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				toGdsSnglSub.setIntlBat(GetCellData(r, "国际批次"));// 国际批次
				toGdsSnglSub.setMemo(GetCellData(r, "表体备注"));// 子备注
				toGdsSnglSub.setToOrdrNum(Long.parseLong(GetCellData(r, "来源单据子表序号")));// 来源单据子表序号
				toGdsSnglSub.setPursOrdrNum(GetCellData(r, "采购订单编码"));// 采购订单编码

				toGdsSnglSubList.add(toGdsSnglSub);

				toGdsSngl.setToGdsSnglSub(toGdsSnglSubList);
				temp.put(orderNo, toGdsSngl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, ToGdsSngl> uploadScoreInfoU8(MultipartFile file) {
		Map<String, ToGdsSngl> temp = new HashMap<>();
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
			//设置列名称
			getCellNames();
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
				ToGdsSngl toGdsSngl = new ToGdsSngl();
				if (temp.containsKey(orderNo)) {
					toGdsSngl = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				toGdsSngl.setPursTypId("1");// 采购类型编码
//					toGdsSngl.setPursTypNm(GetCellData(r,"采购类型名称"));//采购类型名称
				toGdsSngl.setToGdsSnglId(orderNo);// 到货单编码
				if (GetCellData(r, "日期") == null || GetCellData(r, "日期").equals("")) {
					toGdsSngl.setToGdsSnglDt(null);
				} else {
					toGdsSngl.setToGdsSnglDt(GetCellData(r, "日期").replaceAll("[^0-9:-]", " "));// 到货单日期
				}
				toGdsSngl.setProvrId(GetCellData(r, "供货单位编码"));// 供应商编码
//					toGdsSngl.setProvrNm(GetCellData(r,"供应商名称"));//供应商名称
				toGdsSngl.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				toGdsSngl.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//					toGdsSngl.setDepName(GetCellData(r,"部门名称"));//部门名称
				toGdsSngl.setAccNum(GetCellData(r, "职员编码"));// 用户编码
				toGdsSngl.setUserName(GetCellData(r, "业 务 员"));// 用户名称

				toGdsSngl.setFormTypEncd("002");// 单据类型

//				订单号=采购委外订单号
				toGdsSngl.setPursOrdrId(GetCellData(r, "订单号"));// 采购订单编码
				toGdsSngl.setSetupPers(GetCellData(r, "制单人"));// 创建人
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					toGdsSngl.setSetupTm(null);
				} else {
					toGdsSngl.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				toGdsSngl.setIsNtChk(1);// 是否审核
				toGdsSngl.setChkr(GetCellData(r, "审批人"));// 审核人
				if (GetCellData(r, "审批日期") == null || GetCellData(r, "审批日期").equals("")) {
					toGdsSngl.setChkTm(null);
				} else {
					toGdsSngl.setChkTm(GetCellData(r, "审批日期").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				toGdsSngl.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					toGdsSngl.setModiTm(null);
				} else {
					toGdsSngl.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				toGdsSngl.setMemo(GetCellData(r, "备注"));// 备注
				toGdsSngl.setIsNtRtnGood(0);// 是否退货
				toGdsSngl.setToFormTypEncd("001");// 来源单据类型编码

				List<ToGdsSnglSub> toGdsSnglSubList = toGdsSngl.getToGdsSnglSub();// 到货单子表
				if (toGdsSnglSubList == null) {
					toGdsSnglSubList = new ArrayList<>();
				}
				ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
//				toGdsSnglSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				toGdsSnglSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//					toGdsSnglSub.setWhsNm(GetCellData(r,"仓库名称"));//仓库名称
				toGdsSnglSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				toGdsSnglSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				toGdsSnglSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "原币含税单价"), 8));// 8表示小数位数 //含税单价
				toGdsSnglSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "本币单价"), 8));// 8表示小数位数 //无税单价
				toGdsSnglSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "本币金额"), 8));// 8表示小数位数 //无税金额
				toGdsSnglSub.setTaxAmt(GetBigDecimal(GetCellData(r, "本币税额"), 8));// 8表示小数位数 //税额
				toGdsSnglSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "本币价税合计"), 8));// 8表示小数位数 //价税合计
				toGdsSnglSub.setTaxRate(GetBigDecimal(GetCellData(r, "表体税率"), 8));// 8表示小数位数 //税率
				toGdsSnglSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				toGdsSnglSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
//				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //未入库数量
				toGdsSnglSub.setReturnQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 未拒收数量
//				toGdsSnglSub.setUnIntoWhsQty(GetBigDecimal(GetCellData(r, "未入库数量"), 8));// 8表示小数位数 //未入库数量
				toGdsSnglSub.setBatNum(GetCellData(r, "批号"));// 批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					toGdsSnglSub.setPrdcDt(null);
				} else {
					toGdsSnglSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				toGdsSnglSub.setBaoZhiQi(GetCellData(r, "保质期"));// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					toGdsSnglSub.setInvldtnDt(null);
				} else {
					toGdsSnglSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				toGdsSnglSub.setIntlBat(GetCellData(r, "国际批号"));// 国际批次
				toGdsSnglSub.setMemo(GetCellData(r, "行备注"));// 子备注
//				toGdsSnglSub.setToOrdrNum(Long.parseLong(GetCellData(r, "来源单据子表序号")));// 来源单据子表序号
				toGdsSnglSub.setPursOrdrNum(GetCellData(r, "订单号"));// 采购订单编码

				toGdsSnglSubList.add(toGdsSnglSub);

				toGdsSngl.setToGdsSnglSub(toGdsSnglSubList);
				temp.put(orderNo, toGdsSngl);

			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 参照时查询所有未入库数量的到货单子表信息
	@Override
	public String selectUnIntoWhsQtyByByToGdsSnglId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<ToGdsSnglSub> pursOrdrSubList = tgssd.selectUnIntoWhsQtyByByToGdsSnglId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/ToGdsSngl/selectUnIntoWhsQtyByByToGdsSnglId", true, "查询成功！", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 参照时查询所有未拒收数量的到货单子表信息
	@Override
	public String selectUnReturnQtyByToGdsSnglId(String pursOrdrId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(pursOrdrId);
		List<ToGdsSnglSub> pursOrdrSubList = tgssd.selectUnReturnQtyByToGdsSnglId(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/ToGdsSngl/selectUnReturnQtyByToGdsSnglId", true, "查询成功！", null,
					pursOrdrSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 参照时查询主表信息
	@Override
	public String queryToGdsSnglLists(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// 到货单号
		List<String> provrIdList = getList(((String) map.get("provrId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货分类编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// 供应商订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// 采购订单编码
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<ToGdsSngl> poList = tgsd.selectToGdsSnglLists(map);
		int count = tgsd.selectToGdsSnglCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglLists", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	// 分页查询所有到货单信息
	@Override
	public String queryToGdsSnglListOrderBy(Map map) {
		String resp = "";
		List<String> toGdsSnglIdList = getList(((String)map.get("toGdsSnglId")));// 到货单号
		List<String> provrIdList = getList(((String) map.get("provrId")));// 客户编码
		List<String> accNumList = getList(((String) map.get("accNum")));// 业务员编码
		List<String> invtyClsEncdList = getList(((String) map.get("invtyClsEncd")));// 存货分类编码
		List<String> invtyCdList = getList(((String) map.get("invtyCd")));// 存货代码
		List<String> deptIdList = getList(((String) map.get("deptId")));// 部门编码
		List<String> provrOrdrNumList = getList(((String) map.get("provrOrdrNum")));// 供应商订单号
		List<String> invtyEncdList = getList(((String) map.get("invtyEncd")));// 存货编码
		List<String> whsEncdList = getList(((String) map.get("whsEncd")));// 仓库编码
		List<String> batNumList = getList(((String) map.get("batNum")));// 批次
		List<String> intlBatList = getList(((String) map.get("intlBat")));// 国际批次
		List<String> pursOrdrIdList = getList(((String) map.get("pursOrdrId")));// 采购订单编码
		
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("intlBatList", intlBatList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		List<?> poList;
		if (map.get("sort") == null||map.get("sort").equals("") ||
			map.get("sortOrder") == null||map.get("sortOrder").equals("")) {
			map.put("sort","tgs.to_gds_sngl_dt");
			map.put("sortOrder","desc");
		}
		poList = tgsd.selectToGdsSnglListOrderBy(map);
		Map tableSums = tgsd.selectToGdsSnglListSums(map);
		if (null!=tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
				entry.setValue(s);
			}
		}
		int count = tgsd.selectToGdsSnglCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/ToGdsSngl/queryToGdsSnglListOrderBy", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList,tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String updateToGdsSnglDealStat(String toGdsSnglId,int flat) {
		String message="";
		Boolean isSuccess=true;
		String resp="";
		try {
			if(flat==1){
				List<String> lists = getList(toGdsSnglId);
				for(String toGdsSngId: lists){
					ToGdsSngl toGdsSngl=tgsd.selectToGdsSnglById(toGdsSngId);
					if(toGdsSngl.getDealStat().equals("已打开")){
						int i=tgsd.updateToGdsSnglDealStatOK(toGdsSngId);
						if(i>=1){
							isSuccess=true;
							message+="单据号为"+toGdsSngId+"关闭成功\n";
						}	
					}else{
						isSuccess=false;
						message+="单据号为"+toGdsSngId+"的单据已关闭\n";
					}
				}
				resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglDealStatOK", isSuccess, message, null);
			}else if(flat==0){
				List<String> lists = getList(toGdsSnglId);
				for(String toGdsSngId: lists){
					ToGdsSngl toGdsSngl=tgsd.selectToGdsSnglById(toGdsSngId);
					if(toGdsSngl.getDealStat().equals("已关闭")){
						int i=tgsd.updateToGdsSnglDealStatNO(toGdsSngId);
						if(i>=1){
							isSuccess=true;
							message+="单据号为"+toGdsSngId+"打开成功\n";
						}
					}else{
						isSuccess=false;
						message+="单据号为"+toGdsSngId+"的单据已打开\n";
					}
				}
				resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglDealStatNO", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	public static class zizhu {
		@JsonProperty("到货单编码")
		public String toGdsSnglId;//到货单编码
		@JsonProperty("到货单日期")
	    public String toGdsSnglDt;//到货日期
		@JsonProperty("采购类型编码")
	    public String pursTypId;//采购类型编码
		@JsonProperty("采购类型名称")
	    public String pursTypNm;//采购类型名称
		@JsonProperty("单据类型编码")
	    public String formTypEncd;//单据类型编码
		@JsonProperty("单据类型名称")
	    public String formTypName;//单据类型名称
		@JsonProperty("供应商编码")
	    public String provrId;//供应商编码
		@JsonProperty("供应商名称")
	    public String provrNm;//供应商名称
		@JsonProperty("部门编码")
	    public String deptId;//部门编码
		@JsonProperty("部门名称")
	    public String deptName;//部门名称
		@JsonProperty("用户编码")
	    public String accNum;//用户编码
		@JsonProperty("用户名称")
	    public String userName;//用户名称
		@JsonProperty("采购订单编码")
	    public String pursOrdrId;//采购订单编码
		@JsonProperty("供应商订单号")
	    public String provrOrdrNum;//供应商订单号
		@JsonProperty("是否审核")
	    public Integer isNtChk;//是否审核
		@JsonProperty("审核人")
	    public String chkr;//审核人
		@JsonProperty("审核时间")
	    public String chkTm;//审核时间
		@JsonProperty("创建人")
	    public String setupPers;//创建人
		@JsonProperty("创建时间")
	    public String setupTm;//创建时间
		@JsonProperty("修改人")
	    public String mdfr;//修改人
		@JsonProperty("修改时间")
	    public String modiTm;//修改时间
		@JsonProperty("表头备注")
	    public String memo;//备注
		@JsonProperty("是否有退货")
	    public Integer isNtRtnGood;//是否有退货
		@JsonProperty("处理状态")
	    public String dealStat;//处理状态
		@JsonProperty("拒收备注")
	    public String returnMemo;//拒收备注
		
		@JsonProperty("来源单据类型编码")
	    public String toFormTypEncd;//来源单据类型编码
	    //子表
		@JsonProperty("序号")
	    public Long ordrNum;//序号
		@JsonProperty("存货编码")
		public String invtyEncd;//存货编码
		@JsonProperty("仓库编码")
	    public String whsEncd;//仓库编码
//		@JsonProperty("采购订单号")
//	    public String pursOrdrNum;//采购订单号
		@JsonProperty("数量")
	    public BigDecimal qty;//数量
		@JsonProperty("箱数")
	    public BigDecimal bxQty;//箱数
		@JsonProperty("税率")
	    public BigDecimal taxRate;//税率
		@JsonProperty("无税单价")
	    public BigDecimal noTaxUprc;//无税单价
		@JsonProperty("无税金额")
	    public BigDecimal noTaxAmt;//无税金额
		@JsonProperty("税额")
	    public BigDecimal taxAmt;//税额
		@JsonProperty("含税单价")
	    public BigDecimal cntnTaxUprc;//含税单价
		@JsonProperty("价税合计")
	    public BigDecimal prcTaxSum;//价税合计
		@JsonProperty("保质期")
	    public String baoZhiQi;//保质期
		@JsonProperty("国际批次")
	    public String intlBat;//国际批次
		@JsonProperty("批次")
	    public String batNum;//批号
		@JsonProperty("生产日期")
	    public String prdcDt;//生产日期
		@JsonProperty("失效日期")
	    public String invldtnDt;//失效日期
//		@JsonProperty("是否赠品")
//	    public Integer isComplimentary;//是否赠品
//		@JsonProperty("是否退货")
//	    public Integer isNtRtnGoods;//是否退货（1:到货  0：退货）
		@JsonProperty("未拒收数量")
	    public BigDecimal returnQty;//未拒收数量
//		@JsonProperty("货位编码")
//	    public String gdsBitEncd;//货位编码--------
		//联查存货档案字段、计量单位名称、仓库名称
		@JsonProperty("存货名称")
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
		@JsonProperty("未入库数量")
	    public BigDecimal unIntoWhsQty;//未入库数量
		@JsonProperty("来源单据子表序号")
	    public Long toOrdrNum;//来源单据子表序号
		@JsonProperty("表体备注")
	    public String memos;//表体备注
	}

}
