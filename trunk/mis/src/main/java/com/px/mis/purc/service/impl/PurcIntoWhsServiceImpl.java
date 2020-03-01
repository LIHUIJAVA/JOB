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

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.PursOrdrDao;
import com.px.mis.purc.dao.PursOrdrSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.service.IntoWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;

/*采购入库单功能*/
@Transactional
@Service
public class PurcIntoWhsServiceImpl extends poiTool implements IntoWhsService {
	@Autowired
	private IntoWhsDao iwd;// 入库主
	@Autowired
	private IntoWhsSubDao iwsd;// 入库子
	@Autowired
	private InvtyTabDao itd;// 库存表
	@Autowired
	InvtyGdsBitListMapper bitListMapper;// 货位单号存货对应关系表
	@Autowired
	InvtyNumMapper invtyNumMapper;//
	// 订单号
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private InvtyGdsBitListMapper invtyGdsBitListMapper;
	@Autowired
	private ToGdsSnglDao toGdsSnglDao;// 到货单主表
	@Autowired
	private ToGdsSnglSubDao toGdsSnglSubDao;// 到货单子表
	@Autowired
	private IntoWhsSubDao intoWhsSubDao;// 采购入库单子表
	@Autowired
	private PursOrdrSubDao pursOrdrSubDao;// 采购订单子表
	@Autowired
	private WhsDocMapper whsDocMapper;// 仓库档案
	@Autowired
	private InvtyDocDao invtyDocDao;

	private Logger logger = LoggerFactory.getLogger(PurcIntoWhsServiceImpl.class);

	// 新增采购入库单
	@Override
	public String addIntoWhs(String userId, IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList, String loginTime)
			throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			String number = "";
			// 获取订单号
			if (intoWhs.getIsNtRtnGood() == 1) {
				number = getOrderNo.getSeqNo("CT", userId, loginTime);
			} else {
				number = getOrderNo.getSeqNo("RK", userId, loginTime);
			}
			if (iwd.selectIntoWhsById(number) != null) {
				message = "编号" + number + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				intoWhs.setIntoWhsSnglId(number);
				if (intoWhs.getRecvSendCateId() == "") {
					intoWhs.setRecvSendCateId("1");
				}
				for (IntoWhsSub intoWhsSub : intoWhsSubList) {
					intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
					InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(intoWhsSub.getInvtyEncd());
					if (invtyDoc.getIsNtPurs() == null) {
						isSuccess = false;
						message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
								+ "没有设置是否采购属性，无法保存！";
						throw new RuntimeException(message);
					} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
						isSuccess = false;
						message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
								+ "非可采购存货，无法保存！";
						throw new RuntimeException(message);
					}
					if (invtyDoc.getIsQuaGuaPer() == null) {
						isSuccess = false;
						message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
								+ "没有设置是否保质期管理属性，无法保存！";
						throw new RuntimeException(message);
					} else if(invtyDoc.getIsQuaGuaPer() == 1){
						if (intoWhsSub.getPrdcDt() == "" || intoWhsSub.getPrdcDt() == null) {
							isSuccess = false;
							message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
									+ "是保质期管理，请输入生产日期！";
							throw new RuntimeException(message);
						}
						if (intoWhsSub.getInvldtnDt() == ""|| intoWhsSub.getInvldtnDt() == null) {
							isSuccess = false;
							message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
									+ "是保质期管理，请输入失效日期！";
							throw new RuntimeException(message);
						}
					}
					if (intoWhsSub.getPrdcDt() == "") {
						intoWhsSub.setPrdcDt(null);
					}
					if (intoWhsSub.getInvldtnDt() == "") {
						intoWhsSub.setInvldtnDt(null);
					}

					intoWhsSub.setUnBllgQty(intoWhsSub.getQty().abs());// 默认未开票数量=入库数量
					intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum().abs());// 未开票金额=含税金额
//					intoWhsSub.setUnBllgUprc(intoWhsSub.getPrcTaxSum().abs().divide(intoWhsSub.getQty().abs()));//默认未开票单价=含税金额/数量
					if (intoWhs.getIsNtRtnGood() == 0) {
						intoWhsSub.setReturnQty(intoWhsSub.getQty());// 默认未退货数量=入库数量
					}
				}
				int a = iwd.insertIntoWhs(intoWhs);
				// 无重复值
				int b = iwsd.insertIntoWhsSub(intoWhsSubList);
				if (a >= 1 && b >= 1) {
					message = "新增成功！";
					isSuccess = true;
				} else {
					isSuccess = false;
					message = "新增失败！";
					throw new RuntimeException(message);
				}
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/addIntoWhs", isSuccess, message, intoWhs);

		} catch (DuplicateKeyException e) {
			throw new RuntimeException("新增失败!,单号已存在");
		} catch (Exception e) {
			throw new RuntimeException("新增失败,请联系技术查看");
		}
		return resp;
	}

	// 修改采购入库单
	@Override
	public String editIntoWhs(IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList) throws Exception {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
//			TreeSet<IntoWhsSub> intoWhsSubSet = new TreeSet();
//			intoWhsSubSet.addAll(intoWhsSubList);
//			if(intoWhsSubSet.size() < intoWhsSubList.size()) {
//				resp = BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", false, "表体存货明细不允许同一仓库中存在同一存货的相同批次！",null);
//				return resp;
//			}
			if (intoWhs.getRecvSendCateId() == "") {
				intoWhs.setRecvSendCateId("1");
			}
			for (IntoWhsSub intoWhsSub : intoWhsSubList) {
				intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
				InvtyDoc invtyDoc = invtyDocDao.selectAllByInvtyEncd(intoWhsSub.getInvtyEncd());
				if (invtyDoc.getIsNtPurs() == null) {
					isSuccess = false;
					message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
							+ "没有设置是否采购属性，无法保存！";
					throw new RuntimeException(message);
				} else if (invtyDoc.getIsNtPurs().intValue() != 1) {
					isSuccess = false;
					message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
							+ "非可采购存货，无法保存！";
					throw new RuntimeException(message);
				}
				if (invtyDoc.getIsQuaGuaPer() == null) {
					isSuccess = false;
					message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
							+ "没有设置是否保质期管理属性，无法保存！";
					throw new RuntimeException(message);
				} else if(invtyDoc.getIsQuaGuaPer() == 1){
					if (intoWhsSub.getPrdcDt() == "" || intoWhsSub.getPrdcDt() == null) {
						isSuccess = false;
						message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
								+ "是保质期管理，请输入生产日期！";
						throw new RuntimeException(message);
					}
					if (intoWhsSub.getInvldtnDt() == ""|| intoWhsSub.getInvldtnDt() == null) {
						isSuccess = false;
						message = "该采购入库单对应的存货:" + intoWhsSub.getInvtyEncd()
								+ "是保质期管理，请输入失效日期！";
						throw new RuntimeException(message);
					}
				}
				if (intoWhsSub.getPrdcDt() == "") {
					intoWhsSub.setPrdcDt(null);
				}
				if (intoWhsSub.getInvldtnDt() == "") {
					intoWhsSub.setInvldtnDt(null);
				}
				intoWhsSub.setUnBllgQty(intoWhsSub.getQty().abs());// 默认未开票数量=入库数量
				intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum().abs());// 未开票金额=含税金额
//				intoWhsSub.setUnBllgUprc(intoWhsSub.getPrcTaxSum().abs().divide(intoWhsSub.getQty().abs()));//默认未开票单价=含税金额/数量
				if (intoWhs.getIsNtRtnGood() == 0) {
					intoWhsSub.setReturnQty(intoWhsSub.getQty());// 默认未退货数量=入库数量
				}
			}
			int a = iwsd.deleteIntoWhsSubByIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
			int b = iwd.updateIntoWhsByIntoWhsSnglId(intoWhs);
			int c = iwsd.insertIntoWhsSub(intoWhsSubList);
			if (a >= 1 && b >= 1 && c >= 1) {
				message = "更新成功！";
				isSuccess = true;
			} else {
				isSuccess = false;
				message = "更新失败！";
				throw new RuntimeException(message);
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", isSuccess, message, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 单个删除采购入库单
	@Override
	public String deleteIntoWhs(String intoWhsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (iwd.selectIntoWhsByIntoWhsSnglId(intoWhsSnglId) != null) {
			iwd.deleteIntoWhsByIntoWhsSnglId(intoWhsSnglId);
			iwsd.deleteIntoWhsSubByIntoWhsSnglId(intoWhsSnglId);
			isSuccess = true;
			message = "删除成功！";
		} else {
			isSuccess = false;
			message = "编号" + intoWhsSnglId + "不存在！";
		}

		try {
			resp = BaseJson.returnRespObj("purc/IntoWhs/deleteIntoWhs", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 按照编号查询采购入库单
	@Override
	public String queryIntoWhs(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		/* List<IntoWhsSub> intoWhsSub = new ArrayList<>(); */
		IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(intoWhsSnglId);
		if (intoWhs != null) {
			/* intoWhsSub=iwsd.selectIntoWhsSubByIntoWhsSnglId(intoWhsSnglId); */
			message = "查询成功！";
		} else {
			isSuccess = false;
			message = "编号" + intoWhsSnglId + "不存在！";
		}
		try {
			resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhs", isSuccess, message, intoWhs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 采购入库单主子列表
	@Override
	public String queryIntoWhsList(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);

		// 仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));// 仓库编码
		map.put("whsIdList", whsIdList);

		List<IntoWhs> poList = iwd.selectIntoWhsList(map);
		int count = poList.size();
		if (poList.size() < (int) map.get("num")) {

		} else {
			poList = poList.subList((int) map.get("index"), (int) map.get("num") + (int) map.get("index"));
		}
//		int count = iwd.selectIntoWhsCountLess(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsList", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除入库单据
	@Override
	public String deleteIntoWhsList(String intoWhsSnglId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(intoWhsSnglId);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (iwd.selectIntoWhsIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = 0;
				try {
					a = deleteIntoWhsList(lists2);
				} catch (Exception e) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					message += "单据号为：" + lists2.toString() + "的订单删除失败！\n";
					e.printStackTrace();
				}
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为：" + lists2.toString() + "的订单删除成功!\n";
				} else {
					isSuccess = false;
					message += "单据号为：" + lists2.toString() + "的订单删除失败！111111\n";
				}
			}
			if (lists3.size() > 0) {
				isSuccess = false;
				message += "单据号为：" + lists3.toString() + "的订单已被审核，无法删除！\n";
			}
			resp = BaseJson.returnRespObj("purc/IntoWhs/deleteIntoWhsList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Transactional
	private int deleteIntoWhsList(List<String> lists2) {
		iwd.insertIntoWhsDl(lists2);
		iwsd.insertIntoWhsSubDl(lists2);
		int a = iwd.deleteIntoWhsList(lists2);
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

	// 采购入库单和采购退货单审核弃审功能
	@Override
	public Map<String, Object> updateIntoWhsIsNtChk(IntoWhs inwh) throws Exception {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// 采购入库单主表
			// 判断前端传回的审核状态及退货状态
			if (intoWhs.getIsNtRtnGood() == 0) {
				if (inwh.getIsNtChk() == 1) {
					// 采购入库单审核
					message.append(updateIntoWhsIsNtChkOK(inwh).get("message"));
				} else if (inwh.getIsNtChk() == 0) {
					// 采购入库单弃审
					message.append(updateIntoWhsIsNtChkNO(inwh).get("message"));

				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			} else if (intoWhs.getIsNtRtnGood() == 1) {
				if (inwh.getIsNtChk() == 1) {
					// 采购退货单审核
					message.append(updateReturnWhsIsNtChkOK(inwh).get("message"));

				} else if (inwh.getIsNtChk() == 0) {
					// 采购退货单弃审
					message.append(updateReturnWhsIsNtChkNO(inwh).get("message"));

				} else {
					isSuccess = false;
					message.append("审核状态错误，无法审核！\n");
				}
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 采购入库单审核
	private Map<String, Object> updateIntoWhsIsNtChkOK(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 0) {
			IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// 采购入库单主表
			List<IntoWhsSub> intoWhsSubs = intoWhs.getIntoWhsSub();// 获取子表数据
			String whsId = "";
			if (intoWhs.getToGdsSnglId() == null || intoWhs.getToGdsSnglId().equals("")) {
				for (IntoWhsSub inWhSub : intoWhsSubs) {
					// 根据批次、存货编码、仓库编码查询库存表中是否有该对象
					InvtyTab invtyTab = itd.selectInvtyTabByTerm(inWhSub);// 应该加锁
					if (invtyTab == null) {
						int a = itd.insertInvtyTabToIntoWhs(inWhSub);// 新增可用量、现存量
						if (a < 1) {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败！请查明原因再审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);// 修改可用量、现存量
						if (b < 1) {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败！请查明原因再审核！\n";
							throw new RuntimeException(message);
						}
					}
					whsId = inWhSub.getWhsEncd();
				}
			} else {
				for (IntoWhsSub inWhSub : intoWhsSubs) {
					// 根据批次、存货编码、仓库编码查询库存表中是否有该对象
					InvtyTab invtyTab = itd.selectInvtyTabByTerm(inWhSub);
					if (invtyTab == null) {
						int a = itd.insertInvtyTabToIntoWhs(inWhSub);// 新增可用量、现存量
						if (a < 1) {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败！请查明原因再审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);// 修改可用量、现存量
//						int b = itd.updateInvtyTabJiaToIntoWhs(inWhSub);
						if (b < 1) {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败！请查明原因再审核！\n";
							throw new RuntimeException(message);
						}
					}
					if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
						map.put("ordrNum", inWhSub.getToOrdrNum());// 子表序号
						BigDecimal unIntoWhsQty = toGdsSnglSubDao.selectUnIntoWhsQtyByInvWhsBat(map);// 查询到货单中的未入库数量
						if (unIntoWhsQty != null) {
							if (unIntoWhsQty.compareTo(inWhSub.getQty()) == 1
									|| unIntoWhsQty.compareTo(inWhSub.getQty()) == 0) {
								map.put("unIntoWhsQty", inWhSub.getQty());// 修改未入库数量
								toGdsSnglSubDao.updateToGdsSnglSubByInvWhsBat(map);// 根据到货单子表序号修改到货单中的未入库数量
							} else {
								isSuccess = false;
								message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单中存货【" + inWhSub.getInvtyEncd()
										+ "】累计入库数量大于到货数量，无法审核！\n";
								throw new RuntimeException(message);
							}
						} else {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单对应的到货单中未入库数量不存在，无法审核！\n";
							throw new RuntimeException(message);
						}
					}
					whsId = inWhSub.getWhsEncd();
				}
				toGdsSnglDao.updateToGdsSnglDealStatOK(intoWhs.getToGdsSnglId());// 修改到货单的处理状态为处理完成
				// 判断采购入库单中采购订单编码是否存在
				if (intoWhs.getPursOrdrId() != null && !intoWhs.getPursOrdrId().equals("")) {
					// 如果采购订单编码存在 ,根据采购订单号查询采购订单对应的全部采购入库单信息,按照采购订单子表序号分组查询
					map.put("pursOrdrIds", intoWhs.getPursOrdrId());
					// 采订子表序号集合
					List<Long> detailCount = pursOrdrSubDao.selectPursOrdrSubIdsByPursOrdrId(intoWhs.getPursOrdrId());
					// 定义是否移除变量
					boolean remove = false;
					List<Map> intoQtyAndPuOrNumList = iwd.selectIntoWhsQtyByPursOrdrId(map);// 根据采订子表号,查询明细入库数量

					for (Map intoQtyAndPuOrNum : intoQtyAndPuOrNumList) {
						// 获取采购订单子表序号
						map.put("pursOrdrNum", intoQtyAndPuOrNum.get("pursOrdrNum"));
						BigDecimal intoQty = (BigDecimal) intoQtyAndPuOrNum.get("intoQty");
						// 根据采购订单子表序号查询采购订单中的数量
						BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);

						if (pursQty != null) {
							if (pursQty.compareTo(intoQty) == 1 || pursQty.compareTo(intoQty) == 0) {
								BigDecimal unToGdsQty = pursQty.subtract(intoQty);// 订单数量减去采购入库单数量计算未到货数量
								map.put("unToGdsQty", unToGdsQty);
								pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// 根据存货编码修改到货单中的未入库数量
							}
							System.out.println(map.get("pursOrdrNum"));
							remove = detailCount.remove((Long)map.get("pursOrdrNum"));
						} else {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单对应的到货单中未入库数量不存在，无法审核！\n";
							throw new RuntimeException(message);
						}
					}

					if (detailCount.size() > 0 && remove) {
						for (Long ordrNum : detailCount) {
							map.put("pursOrdrNum", ordrNum);
							BigDecimal intoQty = new BigDecimal(0);
							BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);
							if (pursQty != null) {
								if (pursQty.compareTo(intoQty) == 1 || pursQty.compareTo(intoQty) == 0) {
									BigDecimal unToGdsQty = pursQty.subtract(intoQty);// 订单数量减去采购入库单数量计算未到货数量
									map.put("unToGdsQty", unToGdsQty);
									pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// 根据存货编码修改到货单中的未入库数量
								}
							} else {
								isSuccess = false;
								message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单对应的到货单中未入库数量不存在，无法审核！\n";
								throw new RuntimeException(message);
							}
						}

					} else if (!remove) {
						isSuccess = false;
						message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单库存扣减出错,请联系技术查明原因!";
						throw new RuntimeException(message);
					}

				}
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if (whs != null && whs == 1) {
				intoWhsHuoWeiJia(inwh.getIntoWhsSnglId(), intoWhsSubs);
			}
			int c = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
			if (c >= 1) {
				isSuccess = true;
				message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核成功！\n";
			} else {
				isSuccess = false;
				message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败！请查明原因再审核！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 采购入库单弃审
	private Map<String, Object> updateIntoWhsIsNtChkNO(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		InvtyTab invtyTab = new InvtyTab();// 库存表
		// 判断采购入库单中的审核状态是否审核
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 1) {
			// 判断采购入库单是否记账
			if (iwd.selectIntoWhsIsNtBookEntry(inwh.getIntoWhsSnglId()) == 0) {
				// 判断采购入库单是否开票
				if (iwd.selectIntoWhsIsNtBllg(inwh.getIntoWhsSnglId()) == 0) {
					// 判断采购入库单是否结算
					if (iwd.selectIntoWhsIsNtStl(inwh.getIntoWhsSnglId()) == 0) {
						IntoWhs intoWhs = iwd.selectIntoWhsByIntoWhsSnglId(inwh.getIntoWhsSnglId());// 采购入库单主表
						List<IntoWhsSub> intoWhsSubs = intoWhs.getIntoWhsSub();// 获取采购入库单子表
						String whsId = "";
						if (intoWhs.getToGdsSnglId() == null || intoWhs.getToGdsSnglId().equals("")) {
							for (IntoWhsSub inWhSub : intoWhsSubs) {
								invtyTab = itd.selectInvtyTabByTerm(inWhSub);
								if (invtyTab == null) {
									isSuccess = false;
									message += inwh.getIntoWhsSnglId() + "的存货编码：" + inWhSub.getInvtyEncd() + "批次："
											+ inWhSub.getBatNum() + "的库存不存在，无法弃审\n";
									throw new RuntimeException(message);
								} else {
									// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
									if ((invtyTab.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
											&& (invtyTab.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
										itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
									} else {
										isSuccess = false;
										message += inwh.getIntoWhsSnglId() + "的采购入库单中存货编码:" + inWhSub.getInvtyEncd()
												+ ",批次:" + inWhSub.getBatNum() + "的库存不足，无法弃审\n";
										throw new RuntimeException(message);
									}
								}
								whsId = inWhSub.getWhsEncd();
							}
						} else {
							for (IntoWhsSub inWhSub : intoWhsSubs) {
								invtyTab = itd.selectInvtyTabByTerm(inWhSub);
								if (invtyTab == null) {
									isSuccess = false;
									message += inwh.getIntoWhsSnglId() + "的存货编码：" + inWhSub.getInvtyEncd() + "批次："
											+ inWhSub.getBatNum() + "的库存不存在，无法弃审\n";
									throw new RuntimeException(message);
								} else {
									// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
									if ((invtyTab.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
											&& (invtyTab.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
										itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
									} else {
										isSuccess = false;
										message += inwh.getIntoWhsSnglId() + "的采购入库单中存货编码:" + inWhSub.getInvtyEncd()
												+ ",批次:" + inWhSub.getBatNum() + "的库存不足，无法弃审\n";
										throw new RuntimeException(message);
									}
									// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
//									if (invtyTab.getNowStok().compareTo(inWhSub.getQty()) == 1
//											|| invtyTab.getNowStok().compareTo(inWhSub.getQty()) == 0) {
//										itd.updateInvtyTabJianToIntoWhs(inWhSub);
//
//									} else if (invtyTab.getNowStok().compareTo(inWhSub.getQty()) == -1) {
//										isSuccess = false;
//										message += inwh.getIntoWhsSnglId() + "库存中数量不足，无法弃审\n";
//										throw new RuntimeException(message);
//									}
								}
								if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
									map.put("ordrNum", inWhSub.getToOrdrNum());// 子表序号
									BigDecimal unIntoWhsQty = toGdsSnglSubDao.selectUnIntoWhsQtyByInvWhsBat(map);// 查询到货单中的未入库数量
									if (unIntoWhsQty != null) {
										map.put("unIntoWhsQty", inWhSub.getQty().multiply(new BigDecimal(-1)));// 修改未入库数量
										toGdsSnglSubDao.updateToGdsSnglSubByInvWhsBat(map);// 根据存货编码修改到货单中的未入库数量
									} else {
										isSuccess = false;
										message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单对应的到货单中未入库数量不存在，无法弃审！\n";
										throw new RuntimeException(message);
									}
								}
								// 判断采购入库单中采购订单编码是否存在
								if (intoWhs.getPursOrdrId() != null && !intoWhs.getPursOrdrId().equals("")) {
									// 如果采购订单编码存在 ,根据采购订单号查询采购订单对应的全部采购入库单信息,按照采购订单子表序号分组查询
									map.put("pursOrdrIds", intoWhs.getPursOrdrId());
									// 采订子表序号集合
									List<Long> detailCount = pursOrdrSubDao
											.selectPursOrdrSubIdsByPursOrdrId(intoWhs.getPursOrdrId());
									// 定义是否移除变量
									boolean remove = false;
									List<Map> intoQtyAndPuOrNumList = iwd.selectIntoWhsQtyByPursOrdrId(map);
									for (Map intoQtyAndPuOrNum : intoQtyAndPuOrNumList) {
										// 获取采购订单子表序号
										map.put("pursOrdrNum", intoQtyAndPuOrNum.get("pursOrdrNum"));
										// 根据采购订单子表序号查询采购订单中的数量
										BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);
										if (pursQty != null) {
											map.put("unToGdsQty", pursQty);
											pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// 根据采购订单子表序号修改采购订单未到货数量
											remove = detailCount.remove((Long)intoQtyAndPuOrNum.get("pursOrdrNum"));
										} else {
											isSuccess = false;
											message += "单据号为：" + inwh.getIntoWhsSnglId()
													+ "的采购入库单对应的到货单中未入库数量不存在，无法审核！\n";
											throw new RuntimeException(message);
										}
									}
									if (detailCount.size() > 0 && remove) {
										for (Long pursOrdrNum : detailCount) {

											map.put("pursOrdrNum", pursOrdrNum);
											BigDecimal pursQty = pursOrdrSubDao.selectQtyByOrdrNum(map);

											if (pursQty != null) {
												map.put("unToGdsQty", pursQty);
												pursOrdrSubDao.updatePursOrdrSubUnToGdsQty(map);// 根据采购订单子表序号修改采购订单未到货数量
											} else {
												isSuccess = false;
												message += "单据号为：" + inwh.getIntoWhsSnglId()
														+ "的采购入库单对应的到货单中未入库数量不存在，无法审核！\n";
												throw new RuntimeException(message);
											}
										}
										;
									} else if (!remove) {
										isSuccess = false;
										message += "单号为" + inwh.getIntoWhsSnglId() + "的采购入库单审核失败!(明细条数不符)\n";
										throw new RuntimeException(message);
									}

								}
								whsId = inWhSub.getWhsEncd();
							}
							toGdsSnglDao.updateToGdsSnglDealStatNO(intoWhs.getToGdsSnglId());// 修改到货单的处理状态为处理中
						}
						Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
						if (whs != null && whs == 1) {
							intoWhsHuoWeiJian(inwh.getIntoWhsSnglId(), intoWhsSubs);
						}
						int updateFlag = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
						if (updateFlag >= 1) {
							isSuccess = true;
							message += inwh.getIntoWhsSnglId() + "采购入库单弃审成功！\n";
						} else {
							isSuccess = false;
							message += inwh.getIntoWhsSnglId() + "采购入库单已弃审,不需要重复弃审！\n";
							throw new RuntimeException(message);
						}

					} else {
						isSuccess = false;
						message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已结算，无法弃审！\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已开票，无法弃审！\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已记账，无法弃审！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单未审核，请先审核该单据！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 采购退货单审核
	private Map<String, Object> updateReturnWhsIsNtChkOK(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
//		InvtyTab invtyTab = new InvtyTab();// 库存表
		List<IntoWhsSub> intoWhsSubs = new ArrayList<>();// 入库子表
		// 判断采购入库单是否审核，只有审核状态为0时才可以做审核操作
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 0) {

			intoWhsSubs = iwsd.selectIntoWhsSubByIntoWhsSnglId(inwh.getIntoWhsSnglId());
			String whsId = "";
			for (IntoWhsSub inWhSub : intoWhsSubs) {
				InvtyTab invtyTabs = itd.selectInvtyTabByTerm(inWhSub);
				if (invtyTabs == null) {
					isSuccess = false;
					message += inwh.getIntoWhsSnglId() + "的采购退货单中存货编码:" + inWhSub.getInvtyEncd() + ",批次:"
							+ inWhSub.getBatNum() + "的库存无数量，无法审核\n";
					throw new RuntimeException(message);
				} else {
					// a.compareTo(b) -1表示小于 1 表示大于 0表示等于
					if ((invtyTabs.getNowStok().compareTo(inWhSub.getQty().abs()) >= 0)
							&& (invtyTabs.getAvalQty().compareTo(inWhSub.getQty().abs()) >= 0)) {
						// 由于采购退货单中的数量为负，所以执行增库存现存量和可用量
						int b = itd.updateInvtyTabJiaToIntoWhsReturn(inWhSub);
						if (b < 1) {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单审核失败！请查明原因再审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += inwh.getIntoWhsSnglId() + "的采购退货单中存货编码:" + inWhSub.getInvtyEncd() + ",批次:"
								+ inWhSub.getBatNum() + "的库存不足，无法审核\n";
						throw new RuntimeException(message);
					}
				}
				if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
					map.put("ordrNum", inWhSub.getToOrdrNum());// 子表序号
					BigDecimal unReturnQty = intoWhsSubDao.selectUnReturnQtyByOrdrNum(map);// 查询采购入库单中的未退货数量
					if (unReturnQty != null) {
						if (unReturnQty.compareTo(inWhSub.getQty().abs()) >= 0) {
							map.put("returnQty", inWhSub.getQty().abs());// 修改未退货数量
							intoWhsSubDao.updateIntoWhsSubUnReturnQty(map);// 修改采购入库单未退货数量
						} else {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单中存货【" + inWhSub.getInvtyEncd()
									+ "】累计入货数量大于退货数量，无法审核！\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单对应的到货单中未退货数量不存在，无法审核！\n";
						throw new RuntimeException(message);
					}
				}
				whsId = inWhSub.getWhsEncd();
			}
			Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
			if (whs != null && whs == 1) {
				intoWhsHuoWeiJian(inwh.getIntoWhsSnglId(), intoWhsSubs);
			}
			int a = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
			if (a >= 1) {
				isSuccess = true;
				message += inwh.getIntoWhsSnglId() + "采购退货单审核成功！\n";
			} else {
				isSuccess = false;
				message += inwh.getIntoWhsSnglId() + "采购退货单审核失败！\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单已审核，不需要重新审核！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 采购退货单弃审
	private Map<String, Object> updateReturnWhsIsNtChkNO(IntoWhs inwh) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		InvtyTab invtyTab = new InvtyTab();// 库存表
		List<IntoWhsSub> intoWhsSubs = new ArrayList<>();// 入库子表
		if (iwd.selectIntoWhsIsNtChk(inwh.getIntoWhsSnglId()) == 1) {
			// 判断采购入库单是否记账
			if (iwd.selectIntoWhsIsNtBookEntry(inwh.getIntoWhsSnglId()) == 0) {
				// 判断采购入库单是否开票
				if (iwd.selectIntoWhsIsNtBllg(inwh.getIntoWhsSnglId()) == 0) {
					// 判断采购入库单是否结算
					if (iwd.selectIntoWhsIsNtStl(inwh.getIntoWhsSnglId()) == 0) {
						intoWhsSubs = iwsd.selectIntoWhsSubByIntoWhsSnglId(inwh.getIntoWhsSnglId());
						String whsId = "";
						for (IntoWhsSub inWhSub : intoWhsSubs) {
							// 根据批次、存货编码、仓库编码查询库存表中是否有该对象
							invtyTab = itd.selectInvtyTabByTerm(inWhSub);
							if (invtyTab == null) {
								isSuccess = false;
								message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单中存货编码:" + inWhSub.getInvtyEncd()
										+ ",批次:" + inWhSub.getBatNum() + "的库存不存在，无法弃审\n";
								throw new RuntimeException(message);
							} else {
								// 由于采购退货单中的数量为负，所以执行减库存，实际库存量增加
								int b = itd.updateInvtyTabJianToIntoWhsReturn(inWhSub);
								if (b < 1) {
									isSuccess = false;
									message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单弃审失败！请查明原因再弃审！\n";
									throw new RuntimeException(message);
								}
							}
							if (inWhSub.getToOrdrNum() != null && inWhSub.getToOrdrNum() != 0) {
								map.put("ordrNum", inWhSub.getToOrdrNum());// 子表序号
								BigDecimal unReturnQty = intoWhsSubDao.selectUnReturnQtyByOrdrNum(map);// 查询采购入库单中的未退货数量
								if (unReturnQty != null) {
									map.put("returnQty", inWhSub.getQty());// 修改未退货数量
									intoWhsSubDao.updateIntoWhsSubUnReturnQty(map);// 修改采购入库单未退货数量
								} else {
									isSuccess = false;
									message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单对应的到货单中未退货数量不存在，无法弃审！\n";
									throw new RuntimeException(message);
								}
							}
							whsId = inWhSub.getWhsEncd();
						}
						Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsId);
						if (whs != null && whs == 1) {
							intoWhsHuoWeiJia(inwh.getIntoWhsSnglId(), intoWhsSubs);
						}
						int c = iwd.updateIntoWhsIsNtChkByIntoWhs(inwh);
						if (c >= 1) {
							isSuccess = true;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单弃审弃审成功！\n";
						} else {
							isSuccess = false;
							message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购退货单弃审失败！请查明原因再弃审！\n";
							throw new RuntimeException(message);
						}
					} else {
						isSuccess = false;
						message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已结算，无法弃审！\n";
						throw new RuntimeException(message);
					}
				} else {
					isSuccess = false;
					message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已开票，无法弃审！\n";
					throw new RuntimeException(message);
				}
			} else {
				isSuccess = false;
				message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单已记账，无法弃审！\n";
			}
		} else {
			isSuccess = false;
			message += "单据号为：" + inwh.getIntoWhsSnglId() + "的采购入库单未审核，不需要弃审\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 增加货位
	private void intoWhsHuoWeiJia(String intoWhsSnglId, List<IntoWhsSub> intoWhsSubList) {
		/**
		 * 货位判断
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(intoWhsSnglId);// 查询生成的采购入库单中是否有录入货位信息
		if (tabs.size() == 0) {
			throw new RuntimeException("部分明细没有录入货位信息,请核对后再操作");
		}
		for (IntoWhsSub intoWhsSub : intoWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(intoWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(intoWhsSub.getInvtyEncd());
			bitTab.setBatNum(intoWhsSub.getBatNum());
			bitTab.setSerialNum(intoWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(intoWhsSnglId);
			// 查询该单据该存货所有货位总数量
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);
			// 判断该单据该存货所有货位总数量是否不大于单据存货总数量一致
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + "批次：" + bitTab.getBatNum() + "没有录入对应的货位信息");
			} else if (bitTab2.getQty().abs().compareTo(intoWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException(
						"存货：" + bitTab.getInvtyEncd() + "批次：" + bitTab.getBatNum() + " 录入的货位数量与入库数量不匹配,请核对货位数量再操作");
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

	// 减少货位
	private void intoWhsHuoWeiJian(String intoWhsSnglId, List<IntoWhsSub> intoWhsSubList) {
		/**
		 * 货位判断
		 */
		List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(intoWhsSnglId);// 查询生成的采购入库单中是否有录入货位信息
		if (tabs.size() == 0) {
			throw new RuntimeException("部分明细没有录入货位信息,请核对后再操作");
		}
		for (IntoWhsSub intoWhsSub : intoWhsSubList) {
			MovBitTab bitTab = new MovBitTab();
			bitTab.setWhsEncd(intoWhsSub.getWhsEncd());
			bitTab.setInvtyEncd(intoWhsSub.getInvtyEncd());
			bitTab.setBatNum(intoWhsSub.getBatNum());
			bitTab.setSerialNum(intoWhsSub.getOrdrNum().toString());
			bitTab.setOrderNum(intoWhsSnglId);
			MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);// 判断该单据该存货所有货位总数量是否不大于单据存货总数量一致
			if (bitTab2 == null) {
				throw new RuntimeException(bitTab.getInvtyEncd() + "批次：" + bitTab.getBatNum() + "没有录入对应的货位信息,请核对后再操作");
			} else if (bitTab2.getQty().abs().compareTo(intoWhsSub.getQty().abs()) != 0) {
				throw new RuntimeException(
						"存货：" + bitTab.getInvtyEncd() + "批次：" + bitTab.getBatNum() + " 录入的货位数量与入库数量不匹配,请核对货位数量再操作");
			}
		}
		/* 货位信息 */
		for (MovBitTab tab : tabs) {
			tab.setQty(tab.getQty().abs());
			MovBitTab whsTab = invtyNumMapper.selectMbit(tab);// 查询库存表中是否存在该货位信息
			if (whsTab == null) {
				throw new RuntimeException("仓库：" + tab.getWhsEncd() + ",存货：" + tab.getInvtyEncd() + ",批号："
						+ tab.getBatNum() + "无货位数据,请核对后再操作");
			} else if (whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 1
					|| whsTab.getQty().abs().compareTo(tab.getQty().abs()) == 0) {
				tab.setMovBitEncd(whsTab.getMovBitEncd());
				invtyNumMapper.updateJianMbit(tab);// 出库
			} else {
				throw new RuntimeException("仓库：" + tab.getWhsEncd() + ",存货：" + tab.getInvtyEncd() + ",批号："
						+ tab.getBatNum() + "对应的货位数量不足");
			}
		}
	}

	// 打印采购入库单
	@Override
	public String printingIntoWhsList(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);

		// 仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));// 仓库编码
		map.put("whsIdList", whsIdList);
		List<zizhu> intoWhsList = iwd.printingIntoWhsList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("purc/IntoWhs/printingIntoWhsList", true, "查询成功！", null, intoWhsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 采购入库明细查询
	@Override
	public String queryIntoWhsByInvtyEncd(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
					|| map.get("sortOrder").equals("")) {
				map.put("sort", "intoWhsDt");
				map.put("sortOrder", "desc");
			}
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = iwd.selectIntoWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "查询成功！", poList,
						tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

	// 入库明细表--查询
	@Override
	public String queryIntoWhsByInvtyEncdPrint(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
					|| map.get("sortOrder").equals("")) {
				map.put("sort", "intoWhsDt");
				map.put("sortOrder", "desc");
			}
			List<Map> poList = iwd.selectIntoWhsByInvtyEncd(map);
			Map tableSums = iwd.selectIntoWhsListSum(map);
			if (null != tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
					String s = df
							.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
					entry.setValue(s);
				}
			}
			int count = iwd.selectIntoWhsByInvtyEncdCount(map);

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = poList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, poList, tableSums);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			List<Map> poList = iwd.selectIntoWhsByInvtyEncdPrint(map);
			// 导出不需要总计
//				Map tableSums = iwd.selectIntoWhsListSum(map);
//				if (null!=tableSums) {
//					DecimalFormat df = new DecimalFormat("#,##0.00");
//					for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
//						String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
//						entry.setValue(s);
//					}
//				}
			try {
				resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsByInvtyEncd", true, "查询成功！", poList, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resp;
	}

	// 采购订收货报表查询
	@Override
	public String selectIntoWhsAndPursOrdr(Map map) {
		String resp = "";
		List<Map> poList = iwd.selectIntoWhsAndPursOrdr(map);
		Map tableSums = iwd.selectIntoWhsAndPursOrdrSums(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/selectIntoWhsAndPursOrdr", true, "查询成功！", poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入采购入库单
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, IntoWhs> intoWhsMap = null;
		if (i == 0) {
			intoWhsMap = uploadScoreInfo(file);
		} else if (i == 1) {
			intoWhsMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}
		// 将Map转为List，然后批量插入父表数据
		List<IntoWhs> intoWhsList = intoWhsMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
		List<List<IntoWhs>> intoWhsLists = Lists.partition(intoWhsList, 1000);

		for (List<IntoWhs> intoWhs : intoWhsLists) {
			iwd.insertIntoWhsUpload(intoWhs);
		}
		List<IntoWhsSub> intoWhsSubList = new ArrayList<>();
		int flag = 0;
		for (IntoWhs entry : intoWhsList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<IntoWhsSub> tempList = entry.getIntoWhsSub();
			tempList.forEach(s -> s.setIntoWhsSnglId(entry.getIntoWhsSnglId()));
			intoWhsSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (intoWhsSubList.size() >= 1000 || intoWhsMap.size() == flag) {
				iwsd.insertIntoWhsSub(intoWhsSubList);
				intoWhsSubList.clear();
			}
		}
		isSuccess = true;
		message = "采购入库单导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", isSuccess, message, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, IntoWhs> uploadScoreInfo(MultipartFile file) {
		Map<String, IntoWhs> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "采购入库单编码");
				// 创建实体类
				IntoWhs intoWhs = new IntoWhs();
				if (temp.containsKey(orderNo)) {
					intoWhs = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				intoWhs.setPursTypId(GetCellData(r, "采购类型编码"));// 采购类型编码
//				intoWhs.setPursTypNm(GetCellData(r,"采购类型名称"));//采购类型名称
				intoWhs.setIntoWhsSnglId(orderNo); // 采购入库单编码
				if (GetCellData(r, "采购入库单日期") == null || GetCellData(r, "采购入库单日期").equals("")) {
					intoWhs.setIntoWhsDt(null);
				} else {
					intoWhs.setIntoWhsDt(GetCellData(r, "采购入库单日期"));// 采购入库单日期
				}
				intoWhs.setProvrId(GetCellData(r, "供应商编码"));// 供应商编码
//				intoWhs.setProvrNm(GetCellData(r,"供应商名称"));//供应商名称
				intoWhs.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				intoWhs.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//				intoWhs.setDepName(GetCellData(r,"部门名称"));//部门名称
				intoWhs.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				intoWhs.setUserName(GetCellData(r, "业务员名称"));// 用户名称
				intoWhs.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型
				intoWhs.setRecvSendCateId(GetCellData(r, "收发类别编码"));// 收发类别编码
				intoWhs.setOutIntoWhsTypId(GetCellData(r, "出入库类别编码"));// 出入库类别编码
				intoWhs.setPursOrdrId(GetCellData(r, "采购订单编码"));// 采购订单编码
				intoWhs.setToGdsSnglId(GetCellData(r, "到货单编码"));// 到货单号
				intoWhs.setSetupPers(GetCellData(r, "创建人"));// 创建人
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					intoWhs.setSetupTm(null);
				} else {
					intoWhs.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				intoWhs.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue());// 是否审核
				intoWhs.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					intoWhs.setChkTm(null);
				} else {
					intoWhs.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				intoWhs.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					intoWhs.setModiTm(null);
				} else {
					intoWhs.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				intoWhs.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue());// 是否记账
				intoWhs.setBookEntryPers(GetCellData(r, "记账人"));// 记账人
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					intoWhs.setBookEntryTm(null);
				} else {
					intoWhs.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				intoWhs.setIsNtBllg(new Double(GetCellData(r, "是否开票")).intValue());// 是否开票
				intoWhs.setIsNtStl(new Double(GetCellData(r, "是否结算")).intValue());// 是否结算
				intoWhs.setIsNtRtnGood(new Double(GetCellData(r, "是否退货")).intValue());// 是否退货主
				intoWhs.setMemo(GetCellData(r, " 表头备注"));// 备注
				intoWhs.setExaRep(GetCellData(r, "检验检疫报告"));// 检验检疫报告
				intoWhs.setToFormTypEncd(GetCellData(r, "来源单据类型编码"));// 来源单据类型编码
				intoWhs.setIsNtMakeVouch(new Double(GetCellData(r, "是否生成凭证")).intValue());
				intoWhs.setMakVouchPers(GetCellData(r, "制凭证人"));// 制凭证人
				if (GetCellData(r, "制凭证时间") == null || GetCellData(r, "制凭证时间").equals("")) {
					intoWhs.setMakVouchTm(null);
				} else {
					intoWhs.setMakVouchTm(GetCellData(r, "制凭证时间").replaceAll("[^0-9:-]", " "));// 制凭证时间
				}
				List<IntoWhsSub> intoWhsSubList = intoWhs.getIntoWhsSub();// 采购入库单子表
				if (intoWhsSubList == null) {
					intoWhsSubList = new ArrayList<>();
				}
				IntoWhsSub intoWhsSub = new IntoWhsSub();
				intoWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				intoWhsSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//				intoWhsSub.setWhsNm(GetCellData(r, "仓库名称"));// 仓库名称
				intoWhsSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				intoWhsSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				intoWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8));// 8表示小数位数 //含税单价
				intoWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8));// 8表示小数位数 //无税单价
				intoWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8));// 8表示小数位数 //无税金额
				intoWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8));// 8表示小数位数 //税额
				intoWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8));// 8表示小数位数 //价税合计
				intoWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				intoWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				intoWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				intoWhsSub.setBatNum(GetCellData(r, "批次"));// 批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					intoWhsSub.setPrdcDt(null);
				} else {
					intoWhsSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				intoWhsSub.setBaoZhiQi(GetCellData(r, "保质期"));// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					intoWhsSub.setInvldtnDt(null);
				} else {
					intoWhsSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				intoWhsSub.setIntlBat(GetCellData(r, "国际批次"));// 国际批次
				intoWhsSub.setStlQty(GetBigDecimal(GetCellData(r, "结算数量"), 8));// 8表示小数位数 //结算数量
				intoWhsSub.setStlUprc(GetBigDecimal(GetCellData(r, "结算单价"), 8));// 8表示小数位数 //结算单价
				intoWhsSub.setStlAmt(GetBigDecimal(GetCellData(r, "结算金额"), 8));// 8表示小数位数 //结算金额
				intoWhsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8));// 8表示小数位数 //未开票数量
				intoWhsSub.setUnBllgUprc(GetBigDecimal(GetCellData(r, "未开票单价"), 8));// 8表示小数位数 //未开票单价
				intoWhsSub.setUnBllgAmt(GetBigDecimal(GetCellData(r, "未开票金额"), 8));// 8表示小数位数 //未开票金额
				intoWhsSub.setTeesQty(GetBigDecimal(GetCellData(r, "暂估数量"), 8));// 8表示小数位数 //暂估数量
				intoWhsSub.setTeesUprc(GetBigDecimal(GetCellData(r, "暂估单价"), 8));// 8表示小数位数 //暂估单价
				intoWhsSub.setTeesAmt(GetBigDecimal(GetCellData(r, "暂估金额"), 8));// 8表示小数位数//暂估金额
//				intoWhsSub.setCrspdInvNum(GetCellData(r, "对应发票号"));// 对应发票号
//				intoWhsSub.setGdsBitEncd(GetCellData(r, "货位编码"));// 货位编码
//				intoWhsSub.setRegnEncd(GetCellData(r, "区域编码"));// 区域编码
				intoWhsSub.setMemo(GetCellData(r, "表体备注"));// 子备注
				if (GetCellData(r, "来源单据子表序号") == null || GetCellData(r, "来源单据子表序号") == "") {
					intoWhsSub.setToOrdrNum(null);
				} else {
					intoWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "来源单据子表序号")));
				}
				intoWhsSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				intoWhsSubList.add(intoWhsSub);
				intoWhs.setIntoWhsSub(intoWhsSubList);
				temp.put(orderNo, intoWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, IntoWhs> uploadScoreInfoU8(MultipartFile file) {
		Map<String, IntoWhs> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "入库单号");
				// 创建实体类
				IntoWhs intoWhs = new IntoWhs();
				if (temp.containsKey(orderNo)) {
					intoWhs = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				intoWhs.setPursTypId("1");// 采购类型编码
//					intoWhs.setPursTypNm(GetCellData(r,"采购类型名称"));//采购类型名称
				intoWhs.setIntoWhsSnglId(orderNo); // 采购入库单编码
				if (GetCellData(r, "入库日期") == null || GetCellData(r, "入库日期").equals("")) {
					intoWhs.setIntoWhsDt(null);
				} else {
					intoWhs.setIntoWhsDt(GetCellData(r, "入库日期"));// 采购入库单日期
				}
				intoWhs.setProvrId(GetCellData(r, "供应商编码"));// 供应商编码
//					intoWhs.setProvrNm(GetCellData(r,"供应商名称"));//供应商名称
				intoWhs.setProvrOrdrNum(GetCellData(r, "供应商订单号"));// 供应商订单号
				intoWhs.setDeptId(GetCellData(r, "部门编码"));// 部门编码
//					intoWhs.setDepName(GetCellData(r,"部门名称"));//部门名称
				intoWhs.setAccNum(GetCellData(r, "业务员编码"));// 用户编码
				intoWhs.setUserName(GetCellData(r, "业务员"));// 用户名称

				intoWhs.setFormTypEncd(
						GetBigDecimal(GetCellData(r, "数量"), 8).compareTo(BigDecimal.ZERO) == -1 ? "006" : "002");// 单据类型
				intoWhs.setRecvSendCateId("1");// 收发类别编码
				intoWhs.setOutIntoWhsTypId("9");// 出入库类别编码
				intoWhs.setPursOrdrId(GetCellData(r, "订单号"));// 采购订单编码
				intoWhs.setToGdsSnglId(GetCellData(r, "到货单号"));// 到货单号
				intoWhs.setSetupPers(GetCellData(r, "制单人"));// 创建人
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					intoWhs.setSetupTm(null);
				} else {
					intoWhs.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 创建时间
				}
				intoWhs.setIsNtChk(1);// 是否审核
				intoWhs.setChkr(GetCellData(r, "审核人"));// 审核人
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					intoWhs.setChkTm(null);
				} else {
					intoWhs.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}
				intoWhs.setMdfr(GetCellData(r, "修改人")); // 修改人
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					intoWhs.setModiTm(null);
				} else {
					intoWhs.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 修改时间
				}
				intoWhs.setIsNtBookEntry(0);// 是否记账
//				intoWhs.setBookEntryPers(GetCellData(r, "记账人"));// 记账人
//					if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
//						intoWhs.setBookEntryTm(null);
//					} else {
//						intoWhs.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
//					}
				intoWhs.setIsNtBllg(0);// 是否开票
				intoWhs.setIsNtStl(0);// 是否结算
				intoWhs.setIsNtRtnGood(GetBigDecimal(GetCellData(r, "数量"), 8).compareTo(BigDecimal.ZERO) == -1 ? 1 : 0);// 是否退货主
				intoWhs.setMemo(GetCellData(r, "备注"));// 备注
				intoWhs.setExaRep(null);// 检验检疫报告
				intoWhs.setToFormTypEncd("002");// 来源单据类型编码
				intoWhs.setIsNtMakeVouch(0);
//				intoWhs.setMakVouchPers(null);// 制凭证人
//					if (GetCellData(r, "制凭证时间") == null || GetCellData(r, "制凭证时间").equals("")) {
//						intoWhs.setMakVouchTm(null);
//					} else {
//						intoWhs.setMakVouchTm(GetCellData(r, "制凭证时间").replaceAll("[^0-9:-]", " "));//制凭证时间
//					}
				List<IntoWhsSub> intoWhsSubList = intoWhs.getIntoWhsSub();// 采购入库单子表
				if (intoWhsSubList == null) {
					intoWhsSubList = new ArrayList<>();
				}
				IntoWhsSub intoWhsSub = new IntoWhsSub();
//				intoWhsSub.setOrdrNum(Long.parseLong(GetCellData(r, "序号")));
				intoWhsSub.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
//					intoWhsSub.setWhsNm(GetCellData(r, "仓库名称"));// 仓库名称
				intoWhsSub.setInvtyEncd(GetCellData(r, "存货编码"));// 存货编码
				intoWhsSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8));// 8表示小数位数 //数量
				intoWhsSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "原币含税单价"), 8));// 8表示小数位数 //含税单价
				intoWhsSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "本币无税单价"), 8));// 8表示小数位数 //无税单价
				intoWhsSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "本币无税金额"), 8));// 8表示小数位数 //无税金额
				intoWhsSub.setTaxAmt(GetBigDecimal(GetCellData(r, "本币税额"), 8));// 8表示小数位数 //税额
				intoWhsSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "本币价税合计"), 8));// 8表示小数位数 //价税合计
				intoWhsSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8));// 8表示小数位数 //税率
				intoWhsSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8));// 8表示小数位数 //箱数
				intoWhsSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 8表示小数位数 //箱规
				intoWhsSub.setBatNum(GetCellData(r, "批号"));// 批次
				if (GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) {
					intoWhsSub.setPrdcDt(null);
				} else {
					intoWhsSub.setPrdcDt(GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " "));// 生产日期
				}
				intoWhsSub.setBaoZhiQi(GetCellData(r, "保质期"));// 保质期
				if (GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) {
					intoWhsSub.setInvldtnDt(null);
				} else {
					intoWhsSub.setInvldtnDt(GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " "));// 失效日期
				}
				intoWhsSub.setIntlBat(GetCellData(r, "国际批号"));// 国际批次
//					intoWhsSub.setStlQty(GetBigDecimal(GetCellData(r, "结算数量"), 8));// 8表示小数位数 //结算数量
//					intoWhsSub.setStlUprc(GetBigDecimal(GetCellData(r, "结算单价"), 8));// 8表示小数位数 //结算单价
//					intoWhsSub.setStlAmt(GetBigDecimal(GetCellData(r, "结算金额"), 8));// 8表示小数位数 //结算金额
				intoWhsSub.setUnBllgQty(GetBigDecimal(GetCellData(r, "未开票数量"), 8).abs());// 8表示小数位数 //未开票数量
//					intoWhsSub.setUnBllgUprc(GetBigDecimal(GetCellData(r, "未开票单价"), 8));// 8表示小数位数 //未开票单价
				intoWhsSub.setUnBllgAmt(GetBigDecimal(GetCellData(r, "未开票金额"), 8).abs());// 8表示小数位数 //未开票金额
//					intoWhsSub.setTeesQty(GetBigDecimal(GetCellData(r, "暂估数量"), 8));// 8表示小数位数 //暂估数量
//					intoWhsSub.setTeesUprc(GetBigDecimal(GetCellData(r, "暂估单价"), 8));// 8表示小数位数 //暂估单价
//					intoWhsSub.setTeesAmt(GetBigDecimal(GetCellData(r, "暂估金额"), 8));// 8表示小数位数//暂估金额
//					intoWhsSub.setCrspdInvNum(GetCellData(r, "对应发票号"));// 对应发票号
//					intoWhsSub.setGdsBitEncd(GetCellData(r, "货位编码"));// 货位编码
//					intoWhsSub.setRegnEncd(GetCellData(r, "区域编码"));// 区域编码
				intoWhsSub.setMemo(GetCellData(r, "表体备注"));// 子备注
//				if (GetCellData(r, "到货单子表序号") == null || GetCellData(r, "到货单子表序号") == "") {
//					intoWhsSub.setToOrdrNum(null);
//				} else {
//					intoWhsSub.setToOrdrNum(Long.parseLong(GetCellData(r, "到货单子表序号")));
//				}
//				if (GetCellData(r, "采购订单子表序号") == null || GetCellData(r, "采购订单子表序号") == "") {
//					intoWhsSub.setPursOrdrNum(null);
//				} else {
//					intoWhsSub.setPursOrdrNum(Long.parseLong(GetCellData(r, "采购订单子表序号")));
//				}
				intoWhsSub.setProjEncd(GetCellData(r, "项目编码"));// 项目编码
				intoWhsSubList.add(intoWhsSub);
				intoWhs.setIntoWhsSub(intoWhsSubList);
				temp.put(orderNo, intoWhs);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 采购发票参照时按照编号查询采购入库单子表信息
	@Override
	public String queryIntoWhsByIntoWhsIds(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(intoWhsSnglId);
		List<IntoWhsSub> intoWhsSubList = iwsd.selectIntoWhsSubByIntoWhsSnglIds(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/IntoWhs/queryIntoWhsByIntoWhsIds", true, "查询成功！", null,
					intoWhsSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 采购退货参照时按照编号查询采购入库单子表信息
	@Override
	public String selectIntoWhsSubByUnReturnQty(String intoWhsSnglId) {
		// TODO Auto-generated method stub
		String resp = "";
		List<String> lists = getList(intoWhsSnglId);
		List<IntoWhsSub> intoWhsSubList = iwsd.selectIntoWhsSubByUnReturnQty(lists);
		try {
			resp = BaseJson.returnRespObjList("purc/IntoWhs/selectIntoWhsSubByUnReturnQty", true, "查询成功！", null,
					intoWhsSubList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 参照时查询主表信息
	@Override
	public String queryIntoWhsLists(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号

		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
	
		
		List<IntoWhs> poList = iwd.selectIntoWhsLists(map);
		int count = iwd.selectIntoWhsCounts(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsLists", true, "查询成功！", count, pageNo, pageSize,
					listNum, pages, poList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 分页+排序
	@Override
	public String queryIntoWhsListOrderBy(Map map) {
		String resp = "";
		List<String> intoWhsSnglIdList = getList((String) map.get("intoWhsSnglId"));// 采购入库单单号
		List<String> provrIdList = getList((String) map.get("provrId"));// 供应商编码
		List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> invtyCdList = getList((String) map.get("invtyCd"));// 存货代码
		List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
		List<String> provrOrdrNumList = getList((String) map.get("provrOrdrNum"));// 供应商订单号
		List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
		List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
		List<String> batNumList = getList((String) map.get("batNum"));// 批次
		List<String> pursOrdrIdList = getList((String) map.get("pursOrdrId"));// 采购订单号
		List<String> toGdsSnglIdList = getList((String) map.get("toGdsSnglId"));// 到货单号
		List<String> intlBatList = getList((String) map.get("intlBat"));// 国际批次
		map.put("intoWhsSnglIdList", intoWhsSnglIdList);
		map.put("provrIdList", provrIdList);
		map.put("accNumList", accNumList);
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("invtyCdList", invtyCdList);
		map.put("deptIdList", deptIdList);
		map.put("provrOrdrNumList", provrOrdrNumList);
		map.put("invtyEncdList", invtyEncdList);
		map.put("whsEncdList", whsEncdList);
		map.put("batNumList", batNumList);
		map.put("pursOrdrIdList", pursOrdrIdList);
		map.put("toGdsSnglIdList", toGdsSnglIdList);
		map.put("intlBatList", intlBatList);

		// 仓库权限控制
		List<String> whsIdList = getList((String) map.get("whsId"));// 仓库编码
		map.put("whsIdList", whsIdList);

		List<zizhu> poList;
		if (map.get("sort") == null || map.get("sort").equals("") || map.get("sortOrder") == null
				|| map.get("sortOrder").equals("")) {
			map.put("sort", "iw.into_whs_dt");
			map.put("sortOrder", "desc");
		}

		poList = iwd.selectIntoWhsListOrderBy(map);
		Map tableSums = iwd.selectIntoWhsListSum(map);
		if (null != tableSums) {
			DecimalFormat df = new DecimalFormat("#,##0.00");
			for (Map.Entry entry : (Set<Entry>) tableSums.entrySet()) {
				String s = df.format((BigDecimal) entry.getValue() == null ? new BigDecimal(0.00) : entry.getValue());
				entry.setValue(s);
			}
		}
		int count = iwd.selectIntoWhsCount(map);

		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = poList.size();
		int pages = count / pageSize + 1;

		try {
			resp = BaseJson.returnRespList("purc/IntoWhs/queryIntoWhsListOrderBy", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, poList, tableSums);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static class zizhu {

		@JsonProperty("采购入库单编码")
		public String intoWhsSnglId;// 入库单号
		@JsonProperty("采购入库单日期")
		public String intoWhsDt;// 入库日期
		@JsonProperty("采购类型编码")
		public String pursTypId;// 采购类型编号
		@JsonProperty("采购类型名称")
		public String pursTypNm;// 采购类型名称
		@JsonProperty("单据类型编码")
		public String formTypEncd;// 单据类型编码
		@JsonProperty("单据类型名称")
		public String formTypName;// 单据类型名称
		@JsonProperty("出入库类型编码")
		public String outIntoWhsTypId;// 出入库类型编号
		@JsonProperty("出入库类型名称")
		public String outIntoWhsTypNm;// 出入库类型名称
		@JsonProperty("收发类别编码")
		public String recvSendCateId;// 收发类别编号
		@JsonProperty("收发类别")
		public String recvSendCateNm;// 收发类别
		@JsonProperty("供应商编码")
		public String provrId;// 供应商编号
		@JsonProperty("供应商名称")
		public String provrNm;// 供应商名称
		@JsonProperty("用户编码")
		public String accNum;// 用户编号
		@JsonProperty("用户名称")
		public String userName;// 用户名称
		@JsonProperty("部门编码")
		public String deptId;// 部门编号
		@JsonProperty("部门名称")
		public String deptName;// 部门名称
		@JsonProperty("采购订单编码")
		public String pursOrdrId;// 采购订单编号
		@JsonProperty("到货单编码")
		public String toGdsSnglId;// 到货单编号
		@JsonProperty("供应商订单号")
		public String provrOrdrNum;// 供应商订单号
		@JsonProperty("是否开票")
		public Integer isNtBllg;// 是否开票
		@JsonProperty("是否结算")
		public Integer isNtStl;// 是否结算
		@JsonProperty("是否审核")
		public Integer isNtChk;// 是否审核
		@JsonProperty("审核人")
		public String chkr;// 审核人
		@JsonProperty("审核时间")
		public String chkTm;// 审核时间
//		@JsonProperty("是否关闭")
//		public Integer isNtClos;//是否关闭
//		@JsonProperty("是否完成")
//		public Integer isNtCmplt;//是否完成
		@JsonProperty("创建人")
		public String setupPers;// 创建人
		@JsonProperty("创建时间")
		public String setupTm;// 创建时间
		@JsonProperty("修改人")
		public String mdfr;// 修改人
		@JsonProperty("修改时间")
		public String modiTm;// 修改时间
		@JsonProperty("是否记账")
		public Integer isNtBookEntry;// 是否记账
		@JsonProperty("记账人")
		public String bookEntryPers;// 记账人
		@JsonProperty("记账时间")
		public String bookEntryTm;// 记账时间
		@JsonProperty("检验检疫报告")
		public String exaRep;// 检验检疫报告
		@JsonProperty("表头备注 ")
		public String memo;// 备注
		@JsonProperty("拒收备注")
		public String returnMemo;// 拒收备注
		@JsonProperty("是否退货")
		public Integer isNtRtnGood;// 是否有退货

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
		@JsonProperty("仓库编码")
		public String whsEncd;// 仓库编码
		@JsonProperty("仓库名称")
		public String whsNm;// 仓库名称
//		@JsonProperty("采购订单子表标识")
//		public String pursOrdrSubTabInd;//采购订单子表标识
//		@JsonProperty("采购到货单子表标识")
//		public String pursToGdsSnglSubTabInd;//采购到货单子表标识
		@JsonProperty("数量")
		public BigDecimal qty;// 数量
		@JsonProperty("箱数")
		public BigDecimal bxQty;// 箱数
		@JsonProperty("税率")
		public BigDecimal taxRate;// 税率
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
//		@JsonProperty("结算数量")
//		public BigDecimal stlQty;//结算数量
//		@JsonProperty("结算单价")
//		public BigDecimal stlUprc;//结算单价
//		@JsonProperty("结算金额")
//		public BigDecimal stlAmt;//结算金额
		@JsonProperty("未开票数量")
		public BigDecimal unBllgQty;// 未开票数量
//		@JsonProperty("未开票单价")
//		public BigDecimal unBllgUprc;//未开票单价
//		@JsonProperty("未开票金额")
//		public BigDecimal unBllgAmt;//未开票金额
//		@JsonProperty("暂估数量")
//		public BigDecimal teesQty;//暂估数量
//		@JsonProperty("暂估单价")
//		public BigDecimal teesUprc;//暂估单价
//		@JsonProperty("暂估金额")
//		public BigDecimal teesAmt;//暂估金额
//		@JsonProperty("对应发票号")
//		public String crspdInvNum;//对应发票号

		@JsonProperty("国际批次")
		public String intlBat;// 国际批次
		@JsonProperty("批号")
		public String batNum;// 批号
		@JsonProperty("生产日期")
		public String prdcDt;// 生产日期
		@JsonProperty("失效日期")
		public String invldtnDt;// 失效日期
		@JsonProperty("保质期")
		public String baoZhiQi;// 保质期
//		@JsonProperty("货位编码")
//		public String gdsBitEncd;//货位编码
//		@JsonProperty("是否赠品")
//		public Integer isComplimentary;//是否赠品
//		@JsonProperty("是否退货（1:入库  0：退货）")
//		public Integer isNtRtnGoods;//是否退货（1:入库  0：退货）
		@JsonProperty("未拒收数量")
		public BigDecimal returnQty;// 未拒收数量
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
		@JsonProperty("计量单位编号")
		public String measrCorpId;// 计量单位编号
		@JsonProperty("计量单位名称")
		public String measrCorpNm;// 计量单位名称
		@JsonProperty("对应条形码")
		public String crspdBarCd;// 对应条形码
//		@JsonProperty("存货分类编码")
//		public String invtyClsEncd;//存货分类编码
//		@JsonProperty("存货分类名称")
//		public String invtyClsNm;//存货分类名称
//		@JsonProperty("区域编码")
//		public String regnEncd;//区域编码
		@JsonProperty("来源单据子表标识")
		public Long toOrdrNum;// 来源单据子表标识
		@JsonProperty("表体备注 ")
		public String memos;// 备注
	}

}
