package com.px.mis.account.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.dao.SellComnInvSubDao;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInvResponse;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvResponse;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInvTable;
import com.px.mis.account.service.SellComnInvService;
import com.px.mis.purc.dao.CustDocDao;
import com.px.mis.purc.dao.EntrsAgnStlDao;
import com.px.mis.purc.dao.EntrsAgnStlSubDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.CustDoc;
import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.entity.EntrsAgnStlSub;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;

//销售普通发票主表
@Service
@Transactional
public class SellComnInvServiceImpl<E> extends poiTool implements SellComnInvService {
	@Autowired
	private SellComnInvDao sellComnInvDao;
	@Autowired
	private CustDocDao custDocDao;
	@Autowired
	private SellComnInvSubDao sellComnInvSubDao;
	@Autowired
	private SellSnglDao sellSnglDao;// 销售单主表
	@Autowired
	private SellSnglSubDao sellSnglSubDao;// 销售单子表
	@Autowired
	private RtnGoodsDao rtnGoodsDao;// 退货单主表
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;// 退货单子表
	@Autowired
	private EntrsAgnStlDao entrsAgnStlDao;// 委托代销结算单主表
	@Autowired
	private EntrsAgnStlSubDao entrsAgnStlSubDao;// 委托代销结算单子表
	@Autowired
	private GetOrderNo getOrderNo;// 订单号

	// 新增销售普通发票
	@Override
	public ObjectNode addSellComnInv(SellComnInv sellComnInv, String userId, String loginTime) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellComnInv.getSellInvNum();
		if (sellInvNum == null || sellInvNum.equals("")) {
			String number = getOrderNo.getSeqNo("XSFP", userId, loginTime);
			if (sellComnInvDao.selectSellComnInvBySellInvNum(number) != null) {
				on.put("isSuccess", false);
				on.put("message", "销售发票号" + sellInvNum + "已存在，新增失败！");
			} else {
				sellComnInv.setSellInvNum(number);
				int insertResult = sellComnInvDao.insertSellComnInv(sellComnInv);
				if (insertResult == 1) {
					List<SellComnInvSub> sellSubList = sellComnInv.getSellComnInvSubList();
					BigDecimal totalAmt=BigDecimal.ZERO;
					for (SellComnInvSub sellComnInvSub : sellSubList) {
						sellComnInvSub.setSellInvNum(sellComnInv.getSellInvNum());
						totalAmt=totalAmt.add(Optional.ofNullable(sellComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					}
					if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
						//此处先占用联系人字段,区别红蓝发票,金额大于0为蓝字,金额小于0为红字
						sellComnInv.setColor("蓝");
					}else if (totalAmt.compareTo(BigDecimal.ZERO)<0) {
						sellComnInv.setColor("红");
					}
					sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
					sellComnInvSubDao.insertSellComnInvSubList(sellSubList);
					on.put("isSuccess", true);
					on.put("message", "销售发票新增成功");
				} else {
					on.put("isSuccess", false);
					on.put("message", "销售发票新增失败");
				}
			}
		} else {
			if (sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum) != null) {
				on.put("isSuccess", false);
				on.put("message", "销售发票号" + sellInvNum + "已存在，新增失败！");
			} else {
				int insertResult = sellComnInvDao.insertSellComnInv(sellComnInv);
				if (insertResult == 1) {
					List<SellComnInvSub> sellSubList = sellComnInv.getSellComnInvSubList();
					BigDecimal totalAmt=BigDecimal.ZERO;
					for (SellComnInvSub sellComnInvSub : sellSubList) {
						sellComnInvSub.setSellInvNum(sellInvNum);
						totalAmt=totalAmt.add(Optional.ofNullable(sellComnInvSub.getNoTaxAmt()).orElse(BigDecimal.ZERO));
					}
					if (totalAmt.compareTo(BigDecimal.ZERO)>0) {
						//此处先占用联系人字段,区别红蓝发票,金额大于0为蓝字,金额小于0为红字
						sellComnInv.setColor("蓝");
					}else if (totalAmt.compareTo(BigDecimal.ZERO)<0) {
						sellComnInv.setColor("红");
					}
					sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
					sellComnInvSubDao.insertSellComnInvSubList(sellSubList);
					on.put("isSuccess", true);
					on.put("message", "销售发票新增成功");
				} else {
					on.put("isSuccess", false);
					on.put("message", "销售发票新增失败");
				}
			}
		}
		return on;
	}

	@Override
	public ObjectNode editSellComnInv(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sellInvNum = sellComnInv.getSellInvNum();
		if (sellInvNum == null || sellInvNum.equals("")) {
			on.put("isSuccess", false);
			on.put("message", "销售发票号不能为空,更新失败!");
		} else if (sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum) == null) {
			on.put("isSuccess", false);
			on.put("message", "销售发票号" + sellInvNum + "不存在，更新失败！");
		} else if (custDocDao.selectCustDocByCustId(sellComnInv.getCustId()) == null) {
			on.put("isSuccess", false);
			on.put("message", "客户编号" + sellComnInv.getCustId() + "不存在，更新失败！");
		} else {
			int updateResult = sellComnInvDao.updateSellComnInvBySellInvNum(sellComnInv);
			int deleteResult = sellComnInvSubDao.deleteSellComnInvSubBySellInvNum(sellInvNum);
			if (updateResult == 1 && deleteResult >= 1) {
				for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
					sellComnInvSub.setSellInvNum(sellInvNum);
				}
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);
				on.put("isSuccess", true);
				on.put("message", "销售发票更新成功");
			} else {
				on.put("isSuccess", false);
				on.put("message", "销售发票更新失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteSellComnInvBySellInvNum(String sellInvNum) {
		ObjectNode on = null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int a = sellComnInvDao.deleteSellComnInvBySellInvNum(sellInvNum);
		int b = sellComnInvSubDao.deleteSellComnInvSubBySellInvNum(sellInvNum);
		if (a >= 1 && b >= 1) {
			on.put("isSuccess", true);
			on.put("message", "删除成功");
		} else if (a == 0 || b == 0) {
			on.put("isSuccess", false);
			on.put("message", "没有要删除的数据");
		} else {
			on.put("isSuccess", false);
			on.put("message", "删除失败");
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectSellComnInvList(Map map) {
		String resp = "";
		// List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));//
		// 存货分类编码
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		// map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);

		List<SellComnInv> list = sellComnInvDao.selectSellComnInvList(map);
		int count = sellComnInvDao.selectSellComnInvCount(map);
		int listNum = 0;
		if (list != null) {
			listNum = list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count + pageSize - 1) / pageSize;
			resp = BaseJson.returnRespList("/account/SellComnInv/selectSellComnInvList", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 批量删除
	@Override
	public String deleteSellComnInvList(String sellInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> lists = getList(sellInvNum);
			List<String> lists2 = new ArrayList<>();
			List<String> lists3 = new ArrayList<>();
			for (String list : lists) {
				if (sellComnInvDao.selectSellComnInvIsNtChk(list) == 0) {
					lists2.add(list);
				} else {
					lists3.add(list);
				}
			}
			if (lists2.size() > 0) {
				int a = sellComnInvDao.deleteSellComnInvList(lists2);
				;
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
			resp = BaseJson.returnRespObj("/account/SellComnInv/deleteSellComnInvList", isSuccess, message, null);
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

	// 单个查询销售普通发票
	@Override
	public String selectSellComnInvBySellInvNum(String sellInvNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			SellComnInv sellComnInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellInvNum);
			if (sellComnInv != null) {
				isSuccess = true;
				message = "查询成功！";
			} else {
				isSuccess = false;
				message = "编号" + sellInvNum + "不存在！";
			}
			resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvById", isSuccess, message,
					sellComnInv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 审核采购发票
	@Override
	public Map<String, Object> updateSellComnInvIsNtChkList(SellComnInv sellComnInv) {
		StringBuilder message = new StringBuilder();
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		try {
			if (sellComnInv.getIsNtChk() == 1) {
				message.append(updateSellComnInvIsNtChkOK(sellComnInv).get("message"));
			} else if (sellComnInv.getIsNtChk() == 0) {
				message.append(updateSellComnInvIsNtChkNO(sellComnInv).get("message"));
			} else {
				isSuccess = false;
				message.append("审核状态错误，无法审核！\n");
			}
			map.put("isSuccess", isSuccess);
			map.put("message", message.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return map;
	}

	// 销售发票弃审
	public Map<String, Object> updateSellComnInvIsNtChkNO(SellComnInv sellComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (sellComnInvDao.selectSellComnInvIsNtBookEntry(sellComnInv.getSellInvNum()) == 0) {
			if (sellComnInvDao.selectSellComnInvIsNtChk(sellComnInv.getSellInvNum()) == 1) {
				SellComnInv selComInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellComnInv.getSellInvNum());// 根据销售发票编码查询销售发票主表信息
				List<SellComnInvSub> sellComnInvSubList = selComInv.getSellComnInvSubList();// 销售专用发票子表
				String toFormTypEncd = selComInv.getToFormTypEncd();// 获取对应单据类型
				if (toFormTypEncd != null && !toFormTypEncd.equals("")) {
					if (toFormTypEncd.equals("007")) {
						String sellSnglNum = selComInv.getSellSnglNum();// 获取对应销售发货单号
						updateSellComnInvBySellSnglIsNtChkNO(sellComnInv, sellComnInvSubList, sellSnglNum);// 修改销售单生成的销售发票
					} else if (toFormTypEncd.equals("008")) {
						String rtnGoodsId = selComInv.getSellSnglNum();// 获取退货单单号
						updateSellComnInvByRtnGoodIsNtChkNO(sellComnInv, sellComnInvSubList, rtnGoodsId);// 修改退货单生成的发票
					} else if (toFormTypEncd.equals("025")) {
						String stlSnglId = selComInv.getSellSnglNum();// 获取委托代销发货单号
						updateSellComnInvByEntrsAgnStlIsNtChkNO(sellComnInv, sellComnInvSubList, stlSnglId);// 修改委托代销结算单生成的发票
					}
					// 修改审核状态
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票弃审成功！\n";
					} else {
						isSuccess = false;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票弃审失败！\n";
						throw new RuntimeException(message);
					}
				} else {
					// 修改审核状态
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票弃审成功！\n";
					} else {
						isSuccess = false;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票弃审失败！\n";
						throw new RuntimeException(message);
					}
				}
			} else {
				isSuccess = false;
				message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票未审核，请先审核该单据\n";
				throw new RuntimeException(message);
			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票已记账，无法弃审！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 销售单对应的销售发票弃审
	private Map<String, Object> updateSellComnInvBySellSnglIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String sellSnglNum) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (sellSnglNum != null && !sellSnglNum.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中销售单子表序号
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// 未开票数量
				BigDecimal unBllgQty = sellSnglSubDao.selectSellSnglUnBllgQtyByOrdrNum(map);
				if (unBllgQty != null) {
					sellSnglSubDao.updateSellSnglUnBllgQtyByOrdrNum(map);// 根据销售发货单子表序号修改未开票数量
				} else {
					isSuccess = false;
					message += "单据号为：" + sellComnInv.getSellInvNum() + "的采购发票对应的销售单中未开票数量不存在，无法弃审！\n";
					throw new RuntimeException(message);
				}
			}
			List<String> sellSnglIdList = getSellSnglList(sellSnglNum);// 获取销售单编号
			for (String sellSnglId : sellSnglIdList) {

				List<SellSnglSub> sellSnglSubList = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSnglId);
				int num = 0;
				for (SellSnglSub sellSnglSub : sellSnglSubList) {
					if (sellSnglSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					sellSnglDao.updateSellSnglIsNtBllgNO(sellSnglId);// 修改开票状态
				}
			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的发票对应业务单据的单据号不存在，无法弃审！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 退货单对应的销售发票弃审
	private Map<String, Object> updateSellComnInvByRtnGoodIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (rtnGoodsId != null && !rtnGoodsId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中退货单子表序号
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// 未开票数量
				BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// 获取退货单中的未开票数量
				if (unBllgQty != null) {
					rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// 根据退货单子表序号修改未开票数量

				} else {
					isSuccess = false;
					message += "单据号为：" + sellComnInv.getSellInvNum() + "的采购发票对应的销售单中未开票数量不存在，无法弃审！\n";
					throw new RuntimeException(message);
				}
			}
			List<String> rtnGoodsIdList = getSellSnglList(rtnGoodsId);// 获取销售单编号
			for (String rtnGoodId : rtnGoodsIdList) {
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoodId);
				int num = 0;
				for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
					if (rtnGoodsSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					rtnGoodsDao.updateRtnGoodsIsNtBllgNO(rtnGoodId);// 修改开票状态
				}
			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的发票对应业务单据的单据号不存在，无法弃审！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 委托代销发货单对应的销售发票弃审
	private Map<String, Object> updateSellComnInvByEntrsAgnStlIsNtChkNO(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String stlSnglId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (stlSnglId != null && !stlSnglId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中委托代销单子表序号
				map.put("unBllgQty", sellComnInvSub.getQty().multiply(new BigDecimal(-1)));// 未开票数量
				BigDecimal unBllgQty = entrsAgnStlSubDao.selectEntrsAgnStlUnBllgQtyByOrdrNum(map);// 获取委托代销发货单未开票数量
				if (unBllgQty != null) {
					entrsAgnStlSubDao.updateEntrsAgnStlUnBllgQtyByOrdrNum(map);// 根据委托代销发货单子表序号修改未开票数量
				} else {
					isSuccess = false;
					message += "单据号为：" + sellComnInv.getSellInvNum() + "的采购发票对应的销售单中未开票数量不存在，无法弃审！\n";
					throw new RuntimeException(message);
				}
			}
			List<String> stlSnglIdList = getSellSnglList(stlSnglId);// 获取委托代销发货单编号
			for (String stlSngId : stlSnglIdList) {
				List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(stlSngId);
				int num = 0;
				for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
					if (entrsAgnStlSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num > 0) {
					entrsAgnStlDao.updateEntrsAgnStlIsNtBllgNO(stlSngId);// 修改开票状态
				}
			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的发票对应业务单据的单据号不存在，无法弃审！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 销售发票审核
	public Map<String, Object> updateSellComnInvIsNtChkOK(SellComnInv sellComnInv) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		SellComnInv selComInv = sellComnInvDao.selectSellComnInvBySellInvNum(sellComnInv.getSellInvNum());// 根据销售发票编码查询销售发票主表信息
		List<SellComnInvSub> sellComnInvSubList = selComInv.getSellComnInvSubList();// 销售专用发票子表
		if (selComInv.getIsNtChk() == 0) {
			String sellSnglNum = selComInv.getSellSnglNum();// 获取对应销售发货单号
			if (sellSnglNum != null && !sellSnglNum.equals("")) {
				if (selComInv.getToFormTypEncd() != null) {
					updateSellComnInvByEntrsAgnStlIsNtChkOK(sellComnInv, sellComnInvSubList, sellSnglNum);
					// 修改审核状态
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核成功！\n";
					} else {
						isSuccess = false;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核失败！\n";
						throw new RuntimeException(message);
					}
				} else {
					updateSellComnInvBySellSnglIsNtChkOK(sellComnInv, sellComnInvSubList, sellSnglNum);// 修改销售单生成的销售发票
					// 修改审核状态
					int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
					if (a >= 1) {
						isSuccess = true;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核成功！\n";
					} else {
						isSuccess = false;
						message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核失败！\n";
						throw new RuntimeException(message);
					}
				}
			} else {
				// 修改审核状态
				int a = sellComnInvDao.updateSellComnInvIsNtChk(sellComnInv);
				if (a >= 1) {
					isSuccess = true;
					message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核成功！\n";
				} else {
					isSuccess = false;
					message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票审核失败！\n";
					throw new RuntimeException(message);
				}
			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的销售发票已审核，不需要重复审核\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 销售单对应的销售发票审核
	private Map<String, Object> updateSellComnInvBySellSnglIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String sellSnglNum) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
			if (sellComnInvSub.getIsNtRtnGoods() != null) {
				if (sellComnInvSub.getIsNtRtnGoods() == 0) {
					map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中销售单子表序号
					map.put("unBllgQty", sellComnInvSub.getQty().abs());// 未开票数量
					BigDecimal unBllgQty = sellSnglSubDao.selectSellSnglUnBllgQtyByOrdrNum(map);
					if (unBllgQty != null) {
						if (unBllgQty.compareTo(sellComnInvSub.getQty().abs()) >= 0) {
							sellSnglSubDao.updateSellSnglUnBllgQtyByOrdrNum(map);// 根据销售发货单子表序号修改未开票数量
						} else {
							isSuccess = false;
							message += "单据号为：" + sellComnInv.getSellInvNum() + "的销售发票中存货【"
									+ sellComnInvSub.getInvtyEncd() + "】、批次【" + sellComnInvSub.getBatNum()
									+ "】累计开票数量大于销售数量，无法审核！\n";
							throw new RuntimeException(message);
						}
					}
				} else {
					map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中退货单子表序号
					map.put("unBllgQty", sellComnInvSub.getQty().abs());// 未开票数量
					BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// 获取退货单中的未开票数量
					if (unBllgQty != null) {
						if (unBllgQty.compareTo(sellComnInvSub.getQty().abs()) >= 0) {
							rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// 根据退货单子表序号修改未开票数量
						} else {
							isSuccess = false;
							message += "单据号为：" + sellComnInv.getSellInvNum() + "的销售发票中存货【"
									+ sellComnInvSub.getInvtyEncd() + "】、批次【" + sellComnInvSub.getBatNum()
									+ "】累计开票数量大于退货数量，无法审核！\n";
							throw new RuntimeException(message);
						}
					}
				}
			}
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 委托代销发货单对应的销售发票审核
	private Map<String, Object> updateSellComnInvByEntrsAgnStlIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String stlSnglId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
			map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中委托代销单子表序号
			map.put("unBllgQty", sellComnInvSub.getQty());// 未开票数量
			BigDecimal unBllgQty = entrsAgnStlSubDao.selectEntrsAgnStlUnBllgQtyByOrdrNum(map);// 获取委托代销发货单未开票数量
			if (unBllgQty != null) {
				if (unBllgQty.compareTo(sellComnInvSub.getQty()) == 1
						|| unBllgQty.compareTo(sellComnInvSub.getQty()) == 0) {
					entrsAgnStlSubDao.updateEntrsAgnStlUnBllgQtyByOrdrNum(map);// 根据委托代销发货单子表序号修改未开票数量
				} else {
					isSuccess = false;
					message += "单据号为：" + sellComnInv.getSellInvNum() + "的销售发票中存货【" + sellComnInvSub.getInvtyEncd()
							+ "】、批次【" + sellComnInvSub.getBatNum() + "】累计开票数量大于委托代销发货数量，无法审核！\n";
					throw new RuntimeException(message);
				}
			}
		}
		List<String> stlSnglIdList = getSellSnglList(stlSnglId);// 获取委托代销发货单编号
		for (String stlSngId : stlSnglIdList) {
			List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao.selectEntrsAgnStlSubByStlSnglId(stlSngId);
			if (entrsAgnStlSubList.size() > 0) {
				int num = 0;
				for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
					if (entrsAgnStlSub.getUnBllgQty().intValue() > 0) {
						num++;
					}
				}
				if (num == 0) {
					entrsAgnStlDao.updateEntrsAgnStlIsNtBllgOK(stlSngId);// 修改开票状态
				}
			}
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	// 退货单对应的销售发票审核
	private Map<String, Object> updateSellComnInvByRtnGoodIsNtChkOK(SellComnInv sellComnInv,
			List<SellComnInvSub> sellComnInvSubList, String rtnGoodsId) {
		String message = "";
		Boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		if (rtnGoodsId != null && !rtnGoodsId.equals("")) {
			for (SellComnInvSub sellComnInvSub : sellComnInvSubList) {
				map.put("ordrNum", sellComnInvSub.getSellSnglSubId());// 发票中退货单子表序号
				map.put("unBllgQty", sellComnInvSub.getQty());// 未开票数量
				BigDecimal unBllgQty = rtnGoodsSubDao.selectRtnGoodsUnBllgQtyByOrdrNum(map);// 获取退货单中的未开票数量
				if (unBllgQty != null) {
					if (unBllgQty.compareTo(sellComnInvSub.getQty()) == 1
							|| unBllgQty.compareTo(sellComnInvSub.getQty()) == 0) {
						rtnGoodsSubDao.updateRtnGoodsUnBllgQtyByOrdrNum(map);// 根据退货单子表序号修改未开票数量
					} else {
						isSuccess = false;
						message += "单据号为：" + sellComnInv.getSellInvNum() + "的销售发票中存货【" + sellComnInvSub.getInvtyEncd()
								+ "】、批次【" + sellComnInvSub.getBatNum() + "】累计开票数量大于退货数量，无法审核！\n";
						throw new RuntimeException(message);
					}
				}
			}
			List<String> rtnGoodsIdList = getSellSnglList(rtnGoodsId);// 获取销售单编号
			for (String rtnGoodId : rtnGoodsIdList) {
				List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoodId);
				if (rtnGoodsSubList.size() > 0) {
					int num = 0;
					for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
						if (rtnGoodsSub.getUnBllgQty().intValue() > 0) {
							num++;
						}
					}
					if (num == 0) {
						rtnGoodsDao.updateRtnGoodsIsNtBllgOK(rtnGoodId);// 修改开票状态
					}
				}

			}
		} else {
			isSuccess = false;
			message += "单据号为" + sellComnInv.getSellInvNum() + "的发票对应业务单据的单据号不存在，无法审核！\n";
			throw new RuntimeException(message);
		}
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		return map;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getSellSnglList(String id) {
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

	// 导入
	@Override
	public String uploadFileAddDb(MultipartFile file, int i) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Map<String, SellComnInv> sellComnInvMap = null;

		if (i == 0) {
			sellComnInvMap = uploadScoreInfo(file);
		} else if (i == 1) {
			sellComnInvMap = uploadScoreInfoU8(file);
		} else {
			isSuccess = false;
			message = "导入出异常啦！";
			throw new RuntimeException(message);
		}

		// 将Map转为List，然后批量插入父表数据
		List<SellComnInv> sellComnInvList = sellComnInvMap.entrySet().stream().map(e -> e.getValue())
				.collect(Collectors.toList());
		List<List<SellComnInv>> sellComnInvLists = Lists.partition(sellComnInvList, 1000);

		for (List<SellComnInv> sellComnInv : sellComnInvLists) {
			sellComnInvDao.insertSellComnInvUpload(sellComnInv);
		}
		List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
		int flag = 0;
		for (SellComnInv entry : sellComnInvList) {
			flag++;
			// 批量设置字表和父表的关联字段
			List<SellComnInvSub> tempList = entry.getSellComnInvSubList();
			tempList.forEach(s -> s.setSellInvNum(entry.getSellInvNum()));
			sellComnInvSubList.addAll(tempList);
			// 批量插入，每大于等于1000条插入一次
			if (sellComnInvSubList.size() >= 1000 || sellComnInvMap.size() == flag) {
				sellComnInvSubDao.insertSellComnInvSubList(sellComnInvSubList);
				sellComnInvSubList.clear();
			}
		}
		isSuccess = true;
		message = "销售发票导入成功！";
		try {
			if (i == 0) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFile", isSuccess, message, null);
			} else if (i == 1) {
				resp = BaseJson.returnRespObj("/account/SellComnInv/uploadSellComnInvFileU8", isSuccess, message, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	// 导入excle
	private Map<String, SellComnInv> uploadScoreInfo(MultipartFile file) {
		Map<String, SellComnInv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "销售发票编码");
				// 创建实体类
				SellComnInv sellComnInv = new SellComnInv();
				if (temp.containsKey(orderNo)) {
					sellComnInv = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellComnInv.setSellInvNum(orderNo);// 销售发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					sellComnInv.setBllgDt(null);
				} else {
					sellComnInv.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));// 开票日期
				}
				sellComnInv.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				sellComnInv.setUserName(GetCellData(r, "业务员名称")); // 业务员编码', varchar(200
				sellComnInv.setCustId(GetCellData(r, "客户编码"));// 客户编码
				sellComnInv.setBizTypId(GetCellData(r, "业务类型编码"));// 业务类型编码
				sellComnInv.setSellTypId(GetCellData(r, "销售类型编码")); // '销售类型编码', varchar(200
				sellComnInv.setDeptId(GetCellData(r, "部门编码")); // '部门编码', varchar(200
				sellComnInv.setInvTyp(GetCellData(r, "发票类型"));// 发票类型
				sellComnInv.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码
				sellComnInv.setToFormTypEncd(GetCellData(r, "对应单据类型编码"));// 对应单据类型编码
				sellComnInv.setIsNtChk(new Double(GetCellData(r, "是否审核")).intValue()); // '是否审核', int(11
				sellComnInv.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
					sellComnInv.setChkTm(null);
				} else {
					sellComnInv.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
				}

				sellComnInv.setIsNtBookEntry(new Double(GetCellData(r, "是否记账")).intValue()); // 是否记账
				sellComnInv.setBookEntryPers(GetCellData(r, "记账人")); // 记账人',
				if (GetCellData(r, "记账时间") == null || GetCellData(r, "记账时间").equals("")) {
					sellComnInv.setBookEntryTm(null);
				} else {
					sellComnInv.setBookEntryTm(GetCellData(r, "记账时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}

				sellComnInv.setSetupPers(GetCellData(r, "创建人")); // '创建人', varchar(200
				if (GetCellData(r, "创建时间") == null || GetCellData(r, "创建时间").equals("")) {
					sellComnInv.setSetupTm(null);
				} else {
					sellComnInv.setSetupTm(GetCellData(r, "创建时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellComnInv.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellComnInv.setModiTm(null);
				} else {
					sellComnInv.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellComnInv.setSellSnglNum(GetCellData(r, "来源单据号"));
				sellComnInv.setMemo(GetCellData(r, "表头备注")); // '备注', varchar(2000
				List<SellComnInvSub> sellComnInvSubList = sellComnInv.getSellComnInvSubList();// 销售发票子表
				if (sellComnInvSubList == null) {
					sellComnInvSubList = new ArrayList<>();
				}
				SellComnInvSub sellComnInvSub = new SellComnInvSub();

				sellComnInvSub.setWhsEncd(GetCellData(r, "仓库编码")); // '仓库编码',
				sellComnInvSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				sellComnInvSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				sellComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 箱规
				sellComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				sellComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				sellComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				sellComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				sellComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				sellComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "税率"), 8)); // '税率',
				sellComnInvSub.setIntlBat(GetCellData(r, "国际批次")); // 国际批号
				sellComnInvSub.setBatNum(GetCellData(r, "批次")); // '批号',
				sellComnInvSub.setMemo(GetCellData(r, "表体备注")); // '备注',
				sellComnInvSub.setIsNtRtnGoods(new Double(GetCellData(r, "是否退货")).intValue());
				sellComnInvSub.setSellSnglNums(GetCellData(r, "来源单据号"));
				sellComnInvSub.setOutWhsId(GetCellData(r, "出库单号"));
//				sellComnInvSub.setVouchNums(GetCellData(r, "委托代销结算单号"));
				sellComnInvSub.setProjEncd(GetCellData(r, "项目编码"));
				sellComnInvSubList.add(sellComnInvSub);
				sellComnInv.setSellComnInvSubList(sellComnInvSubList);
				temp.put(orderNo, sellComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 导入excle
	private Map<String, SellComnInv> uploadScoreInfoU8(MultipartFile file) {
		Map<String, SellComnInv> temp = new HashMap<>();
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
				String orderNo = GetCellData(r, "发票号");
				// 创建实体类
				SellComnInv sellComnInv = new SellComnInv();
				if (temp.containsKey(orderNo)) {
					sellComnInv = temp.get(orderNo);
				}
				// 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
				sellComnInv.setSellInvNum(orderNo);// 销售发票编码
				if (GetCellData(r, "开票日期") == null || GetCellData(r, "开票日期").equals("")) {
					sellComnInv.setBllgDt(null);
				} else {
					sellComnInv.setBllgDt(GetCellData(r, "开票日期").replaceAll("[^0-9:-]", " "));// 开票日期
				}
				sellComnInv.setAccNum(GetCellData(r, "业务员编码")); // 业务员编码', varchar(200
				sellComnInv.setUserName(GetCellData(r, "业 务 员")); // 业务员编码', varchar(200
				sellComnInv.setCustId(GetCellData(r, "客户编号"));// 客户编码

				if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2B")) {
					sellComnInv.setBizTypId("1");// 业务类型编码
				} else if (GetCellData(r, "销售类型") != null && GetCellData(r, "销售类型").equals("B2C")) {
					sellComnInv.setBizTypId("2");// 业务类型编码
				}
				int sellTyp = 0;
				int rtnGoods = 0;
				if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("普通销售")) {
					sellComnInv.setSellTypId("1"); // '销售类型编码', varchar(200
					sellTyp = 0;
				} else if (GetCellData(r, "业务类型") != null && GetCellData(r, "业务类型").equals("委托")) {
					sellComnInv.setSellTypId("2"); // '销售类型编码', varchar(200
					sellTyp = 1;
				}

				sellComnInv.setDeptId(GetCellData(r, "部门编号")); // '部门编码', varchar(200

				if (GetCellData(r, "发票类型") != null && GetCellData(r, "发票类型").equals("销售专用发票")) {
					sellComnInv.setFormTypEncd("021");// 单据类型编码

				} else if (GetCellData(r, "发票类型") != null && GetCellData(r, "发票类型").equals("销售普通发票")) {
					sellComnInv.setFormTypEncd("022");// 单据类型编码
				}
				sellComnInv.setInvTyp(GetCellData(r, "发票类型"));// 发票类型
				if (GetCellData(r, "退货标志") != null && GetCellData(r, "退货标志").equals("是")) {
					rtnGoods = 1;
				} else if (GetCellData(r, "退货标志") != null && GetCellData(r, "退货标志").equals("否")) {
					rtnGoods = 0;
				}
				// 退貨
				if (rtnGoods == 0) {
					// 委托
					if (sellTyp == 0) {
						sellComnInv.setToFormTypEncd("007");// 对应单据类型编码

					} else if (sellTyp == 1) {

						sellComnInv.setToFormTypEncd("023");// 对应单据类型编码
					}

				} else if (rtnGoods == 1) {
					// 委托
					if (sellTyp == 0) {
						sellComnInv.setToFormTypEncd("008");// 对应单据类型编码

					} else if (sellTyp == 1) {
						sellComnInv.setToFormTypEncd("024");// 对应单据类型编码

					}
				}

				sellComnInv.setIsNtChk(1); // '是否审核', int(11
				sellComnInv.setChkr(GetCellData(r, "审核人")); // '审核人', varchar(200
//				if (GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) {
//					sellComnInv.setChkTm(null);
//				} else {
//					sellComnInv.setChkTm(GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " "));// 审核时间
//				}

				sellComnInv.setIsNtBookEntry(0); // 是否记账
				sellComnInv.setBookEntryPers(GetCellData(r, "记账人")); // 记账人',
//				if(GetCellData(r,"记账时间")==null || GetCellData(r,"记账时间").equals("")) {
//					sellComnInv.setBookEntryTm(null); 
//				}else {
//					sellComnInv.setBookEntryTm(GetCellData(r,"记账时间").replaceAll("[^0-9:-]"," "));//记账时间 
//				}

				sellComnInv.setSetupPers(GetCellData(r, "制单人")); // '创建人', varchar(200
				if (GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) {
					sellComnInv.setSetupTm(null);
				} else {
					sellComnInv.setSetupTm(GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellComnInv.setMdfr(GetCellData(r, "修改人")); // '修改人', varchar(200
				if (GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) {
					sellComnInv.setModiTm(null);
				} else {
					sellComnInv.setModiTm(GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " "));// 记账时间
				}
				sellComnInv.setSellSnglNum(GetCellData(r, "发货单号"));// 主表来源单据号
				sellComnInv.setMemo(GetCellData(r, "备 注")); // '备注', varchar(2000
				List<SellComnInvSub> sellComnInvSubList = sellComnInv.getSellComnInvSubList();// 销售发票子表
				if (sellComnInvSubList == null) {
					sellComnInvSubList = new ArrayList<>();
				}
				SellComnInvSub sellComnInvSub = new SellComnInvSub();

				sellComnInvSub.setWhsEncd(GetCellData(r, "仓库编号")); // '仓库编码',
				sellComnInvSub.setInvtyEncd(GetCellData(r, "存货编码")); // '存货编码',
				sellComnInvSub.setQty(GetBigDecimal(GetCellData(r, "数量"), 8)); // '数量',
				sellComnInvSub.setBxQty(GetBigDecimal(GetCellData(r, "箱数"), 8)); // '箱数',
				sellComnInvSub.setBxRule(GetBigDecimal(GetCellData(r, "箱规"), 8));// 箱规
				sellComnInvSub.setCntnTaxUprc(GetBigDecimal(GetCellData(r, "含税单价"), 8)); // '含税单价',
				sellComnInvSub.setPrcTaxSum(GetBigDecimal(GetCellData(r, "价税合计"), 8)); // '价税合计',
				sellComnInvSub.setNoTaxUprc(GetBigDecimal(GetCellData(r, "无税单价"), 8)); // '无税单价',
				sellComnInvSub.setNoTaxAmt(GetBigDecimal(GetCellData(r, "无税金额"), 8)); // '无税金额',
				sellComnInvSub.setTaxAmt(GetBigDecimal(GetCellData(r, "税额"), 8)); // '税额',
				sellComnInvSub.setTaxRate(GetBigDecimal(GetCellData(r, "表头税率"), 8)); // '税率',
				sellComnInvSub.setIntlBat(GetCellData(r, "国际批号")); // 国际批号
				sellComnInvSub.setBatNum(GetCellData(r, "批号")); // '批号',
				sellComnInvSub.setMemo(GetCellData(r, "表体备注")); // '备注',
				if (GetCellData(r, "退货标志") != null && GetCellData(r, "退货标志").equals("是")) {
					sellComnInvSub.setIsNtRtnGoods(1);
				} else if (GetCellData(r, "退货标志") != null && GetCellData(r, "退货标志").equals("否")) {
					sellComnInvSub.setIsNtRtnGoods(0);
				}
				sellComnInvSub.setSellSnglNums(GetCellData(r, "发货单号子"));
				sellComnInvSub.setSellSnglSubId(Long.parseLong(GetCellData(r, "发货单子表id")));
				sellComnInvSub.setOutWhsId(GetCellData(r, "出库单号"));
				sellComnInvSub.setProjEncd(GetCellData(r, "项目编码"));
				sellComnInvSubList.add(sellComnInvSub);
				sellComnInv.setSellComnInvSubList(sellComnInvSubList);
				temp.put(orderNo, sellComnInv);
			}
			fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("文件的第" + j + "行导入格式有误，无法导入!" + e.getMessage());
		}
		return temp;
	}

	// 不带分页的全查接口
	@Override
	public String upLoadSellComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);
		List<SellComnInv> poList = sellComnInvDao.printingSellComnInvList(map);
		try {
			resp = BaseJson.returnRespObjList("/account/SellComnInv/printingSellComnInvList", true, "查询成功！", null,
					poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 导出接口
	@Override
	public String printSellComnInvList(Map map) {
		String resp = "";
		List<String> invtyClsEncdList = getList((String) map.get("invtyClsEncd"));// 存货分类编码
		List<String> custIdList = getList((String) map.get("custId"));// 客户编码
		map.put("invtyClsEncdList", invtyClsEncdList);
		map.put("custIdList", custIdList);
		List<zizhu> poList = sellComnInvDao.printSellComnInvList(map);
		try {
			resp = BaseJson.returnRespObjListAnno("/account/SellComnInv/printSellComnInvList", true, "查询成功！", null,
					poList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 多张销售单生成一张销售发票
//	@RequestMapping(value="selectSellComnInvBingList",method = RequestMethod.POST)
//	@ResponseBody
	@Override
	public String selectSellComnInvBingList(List<SellComnInv> sellSnglList) {
		String resp = "";
		String message = "";
		Boolean isSuccess = true;
		try {
			// 实例化一个新的对象 销售普通发票主表
			SellComnInv sellComnInv = new SellComnInv();
			List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
			String sellSnglId = "";// 销售发货单号
			String rtnGoodsId = "";// 退货单号
			String stlSnglId = "";// 委托代销单号
			for (SellComnInv selComInv : sellSnglList) {
				if (selComInv.getSellInvNum() != null && !selComInv.getSellInvNum().equals("")) {
					sellSnglId += selComInv.getSellInvNum() + ",";// 销售发货单号
					sellComnInv.setSellSnglNum(sellSnglId.substring(0, sellSnglId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("单据选择错误,可能由于所参照的单据无单据类型！");
					} else if (selComInv.getFormTypEncd().equals("007")) {// 销售单
						BeanUtils.copyProperties(sellComnInv, selComInv);// 将销售单主表单拷贝给销售发票主表
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// 对应来源单据类型
						// 根据主表主键查询相关的子表信息
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// 或许页面中的子表信息
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							SellSnglSub sellSnglSub = sellSnglSubDao.selectSellSnglSubBySelSnIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, sellSnglSub);// 将销售单子表拷贝给销售发票子表
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("参照的单据对应的单据类型错误，请查明再生单！");
					}
				}
				if (selComInv.getSellSnglNum() != null && !selComInv.getSellSnglNum().equals("")) {
					rtnGoodsId += selComInv.getSellSnglNum() + ",";// 退货单号
					sellComnInv.setSellSnglNum(rtnGoodsId.substring(0, rtnGoodsId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("单据选择错误,可能由于所参照的单据无单据类型！");
					} else if (selComInv.getFormTypEncd().equals("008")) {// 退货单
						BeanUtils.copyProperties(sellComnInv, selComInv);// 将销售单主表单拷贝给销售发票主表
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// 对应来源单据类型
						// 根据主表主键查询相关的子表信息
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// 或许页面中的子表信息
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							RtnGoodsSub rtnGoodsSub = rtnGoodsSubDao.selectRtnGoodsSubByRtGoIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, rtnGoodsSub);// 将退货单子表拷贝给销售发票子表
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("参照的单据对应的单据类型错误，请查明再生单！");
					}
				}
				if (selComInv.getSellSnglNum() != null && !selComInv.getSellSnglNum().equals("")) {
					stlSnglId += selComInv.getSellSnglNum() + ",";// 委托代销单号
					sellComnInv.setSellSnglNum(stlSnglId.substring(0, stlSnglId.length() - 1));
					if (selComInv.getFormTypEncd() == null || selComInv.getFormTypEncd().equals("")) {
						isSuccess = false;
						throw new RuntimeException("单据选择错误,可能由于所参照的单据无单据类型！");
					} else if (selComInv.getFormTypEncd().equals("025")) {// 销售单
						BeanUtils.copyProperties(sellComnInv, selComInv);// 将销售单主表单拷贝给销售发票主表
						sellComnInv.setToFormTypEncd(selComInv.getFormTypEncd());// 对应来源单据类型
						// 根据主表主键查询相关的子表信息
						List<SellComnInvSub> sellComInvSuList = selComInv.getSellComnInvSubList();// 或许页面中的子表信息
						Map<String, Object> map = new HashMap<>();
						for (SellComnInvSub sellComnInvSub : sellComInvSuList) {
							map.put("ordrNum", sellComnInvSub.getOrdrNum());
							EntrsAgnStlSub entrsAgnStlSub = entrsAgnStlSubDao.selectEntrsAgnStlByStlSnIdAndOrdrNum(map);
							SellComnInvSub selComnInSub = new SellComnInvSub();
							BeanUtils.copyProperties(selComnInSub, entrsAgnStlSub);// 将委托代销发货单子表拷贝给销售发票子表
							sellComnInvSubList.add(selComnInSub);
						}
					} else {
						isSuccess = false;
						throw new RuntimeException("参照的单据对应的单据类型错误，请查明再生单！");
					}
				}
				isSuccess = true;
				message = "生单成功！";
			}
			sellComnInv.setSellComnInvSubList(sellComnInvSubList);
			resp = BaseJson.returnRespObj("/account/SellComnInv/selectSellComnInvBingList", isSuccess, message,
					sellComnInv);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 销售发票参照功能
	@Override
	public String selectSellReturnEntrs(Map map) throws IOException {
		String resp = "";
		List<SellComnInv> sellComnInvList = new ArrayList<>();
		int count = 0;
		try {
			if (map.get("formTypEncd").equals("007")) {
				List<String> formIdList = getList((String) map.get("sellInvNum"));
				String formDate1 = (String) map.get("formDt1");
				String formDate2 = (String) map.get("formDt2");
				if (formDate1 != null && formDate1 != "") {
					formDate1 = formDate1 + " 00:00:00";
				}
				if (formDate2 != null && formDate2 != "") {
					formDate2 = formDate2 + " 23:59:59";
				}
				List<String> custIdList = getList((String) map.get("custId"));// 客户编码
				List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
				List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码
				List<String> whsEncdList = getList((String) map.get("whsEncd"));// 仓库编码
				List<String> custOrdrNumList = getList((String) map.get("custOrdrNum"));// 客户订单号
				List<String> invtyEncdList = getList((String) map.get("invtyEncd"));// 存货编码
				List<String> batNumList = getList((String) map.get("batNum"));// 批次
				
				map.put("formIdList", formIdList);
				map.put("formDate1", formDate1);
				map.put("formDate2", formDate2);
				map.put("custIdList", custIdList);
				map.put("accNumList", accNumList);
				map.put("deptIdList", deptIdList);
				map.put("whsEncdList", whsEncdList);
				map.put("custOrdrNumList", custOrdrNumList);
				map.put("invtyEncdList", invtyEncdList);
				map.put("batNumList", batNumList);
				
				String[] flag = {null};
				if(custIdList.size()>0) {
					custIdList.forEach(i->{
						CustCls selectCustDocByCustId = custDocDao.selectCustDocByCustId(i);
						if (null==flag[0]) {
							flag[0] = selectCustDocByCustId.getCustDoc().get(0).getCustTotlCorpId();
						}else {
							if (flag[0]!=selectCustDocByCustId.getCustDoc().get(0).getCustTotlCorpId()) {
								throw new RuntimeException("同一张单据的客户总公司应该一致,请核对客户!");
							}
						}
						
						
					});
				}
				
				if (map.containsKey("sort")) {
					map.put("sort",((String)map.get("sort")).replace("a.", ""));
				}
				List<SellSngl> sellSnglList = sellSnglDao.selectSellSnglCZLists(map);
				count = sellSnglDao.selectSellSnglCZListsCount(map);

				for (SellSngl sellSngl : sellSnglList) {
					SellComnInv sellComnInv = new SellComnInv();
					try {
						BeanUtils.copyProperties(sellComnInv, sellSngl);
						sellComnInv.setSellInvNum(sellSngl.getSellSnglId());// 单据编号
						sellComnInv.setBllgDt(sellSngl.getSellSnglDt());// 单据日期
						sellComnInvList.add(sellComnInv);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (map.get("formTypEncd").equals("025")) {
				map.put("stlSnglId", map.get("sellInvNum"));
				String stlSnglDt1 = (String) map.get("formDt1");
				String stlSnglDt2 = (String) map.get("formDt2");
				if (stlSnglDt1 != null && stlSnglDt1 != "") {
					stlSnglDt1 = stlSnglDt1 + " 00:00:00";
				}
				if (stlSnglDt2 != null && stlSnglDt2 != "") {
					stlSnglDt2 = stlSnglDt2 + " 23:59:59";
				}
				map.put("stlSnglDt1", stlSnglDt1);
				map.put("stlSnglDt2", stlSnglDt2);
				List<String> custIdList = getList((String) map.get("custId"));// 客户编码
				List<String> accNumList = getList((String) map.get("accNum"));// 业务员编码
				List<String> deptIdList = getList((String) map.get("deptId"));// 部门编码

				map.put("custIdList", custIdList);
				map.put("accNumList", accNumList);
				map.put("deptIdList", deptIdList);

				List<EntrsAgnStl> entrsAgnStlList = entrsAgnStlDao.selectEntrsAgnStlListToCZ(map);
				count = entrsAgnStlDao.selectEntrsAgnStlListToCZCount(map);

				for (EntrsAgnStl entrsAgnStl : entrsAgnStlList) {
					SellComnInv sellComnInv = new SellComnInv();
					try {
						BeanUtils.copyProperties(sellComnInv, entrsAgnStl);
						sellComnInv.setSellInvNum(entrsAgnStl.getStlSnglId());// 单据编号
						sellComnInv.setBllgDt(entrsAgnStl.getStlSnglDt());// 单据日期
						sellComnInvList.add(sellComnInv);
					} catch (IllegalAccessException | InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			int pageNo = Integer.parseInt(map.get("pageNo").toString());// 第几页
			int pageSize = Integer.parseInt(map.get("pageSize").toString());// 每页条数
			int listNum = sellComnInvList.size();
			int pages = count / pageSize + 1;// 总页数\
			resp = BaseJson.returnRespList("account/SellComnInv/selectSellReturnEntrs", true, "查询成功！", count, pageNo,
					pageSize, listNum, pages, sellComnInvList);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return resp;
	}

	// 参照时根据销售单、退货单、委托代销发货单编码查询子表信息
	@Override
	public String selectSellComnInvBySellRtnEntList(List<SellComnInv> sellComnInvList) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<SellComnInvSub> sellComnInvSubList = new ArrayList<>();
			for (SellComnInv sellComnInv : sellComnInvList) {
				if (sellComnInv.getFormTypEncd().equals("007")) {
					List<SellSnglSub> sellSnglSubList = sellSnglSubDao
							.selectSellSnglSubBySellSnglIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (SellSnglSub sellSnglSub : sellSnglSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, sellSnglSub);
							selComnInSub.setSellInvNum(sellSnglSub.getSellSnglId());// 销售单子表中销售单号
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 将退货单子表拷贝给销售发票子表
						sellComnInvSubList.add(selComnInSub);
					}
				}
				if (sellComnInv.getFormTypEncd().equals("008")) {
					List<RtnGoodsSub> rtnGoodsSubList = rtnGoodsSubDao
							.selectRtnGoodsSubByRtnGoodsIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (RtnGoodsSub rtnGoodsSub : rtnGoodsSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, rtnGoodsSub);
							selComnInSub.setSellInvNum(rtnGoodsSub.getRtnGoodsId());// 退货单子表中退货单号
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 将退货单子表拷贝给销售发票子表
						sellComnInvSubList.add(selComnInSub);
					}
				}
				if (sellComnInv.getFormTypEncd().equals("025")) {
					List<EntrsAgnStlSub> entrsAgnStlSubList = entrsAgnStlSubDao
							.selectEntAgStSubByStlIdAndUnBllgQty(sellComnInv.getSellInvNum());
					for (EntrsAgnStlSub entrsAgnStlSub : entrsAgnStlSubList) {
						SellComnInvSub selComnInSub = new SellComnInvSub();
						try {
							BeanUtils.copyProperties(selComnInSub, entrsAgnStlSub);
							selComnInSub.setSellInvNum(entrsAgnStlSub.getStlSnglId());// 委托代销发货单子表中委托代销发货单号
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 将退货单子表拷贝给销售发票子表
						sellComnInvSubList.add(selComnInSub);
					}
				}
			}

			resp = BaseJson.returnRespObjList("/account/SellComnInv/selectSellComnInvBySellRtnEntList", true, "查询成功！",
					null, sellComnInvSubList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String pushToU8(String ids) throws IOException {
		Logger logger = LoggerFactory.getLogger(SellComnInvServiceImpl.class);

		List<String> idList = getList(ids);

		List<SellComnInv> sellComnInvList = sellComnInvDao.selectComnInvs(idList);
		// 表单对象
		U8SellComnInvTable table = new U8SellComnInvTable();
		// 行集合
		ArrayList<U8SellComnInv> rowList = new ArrayList<U8SellComnInv>();
		// 封装对象
		for (SellComnInv main : sellComnInvList) {
			rowList.add(encapsulation(main, main.getSellComnInvSubList()));
		}
		table.setDataRowList(rowList);

		/********** 以下是接口对接************ */
		System.err.println("请求结构::" + JacksonUtil.getXmlStr(table));

		String resultStr = connectToU8("SetSOBill", JacksonUtil.getXmlStr(table), "905");

		System.err.println("返回的XML结构::" + resultStr);

		/*** 请求发送结束 ****/

		MappingIterator<U8SellComnInvResponse> iterator = JacksonUtil.getResponse("SetSOBillResult",
				U8SellComnInvResponse.class, resultStr);

		ArrayList<U8SellComnInvResponse> failedList = new ArrayList<U8SellComnInvResponse>();

		StringBuffer message = new StringBuffer();
		int count = 0;
		while (iterator.hasNext()) {
			U8SellComnInvResponse response = (U8SellComnInvResponse) iterator.next();
			if (response.getType() == 1 && response.getDscode() != null) {
				failedList.add(response);
				logger.info("单号: " + response.getDscode() + "的发票推送失败,原因为 :" + response.getInfor());

			} else if (response.getType() == 0) {
				sellComnInvDao.updatePushed(response.getDscode());
				count++;
			} else {
				failedList.add(response);
				logger.info("单号: " + "--未知单号--" + "的发票推送失败,原因为 :" + response.getInfor());
			}
		}
		message.append("共" + count + "张单据上传成功!" + '\n');
		if (failedList.size() > 0) {
			message.append("共" + failedList.size() + "张单据上传失败!" + "\n");
		}
		String resp = BaseJson.returnRespList("url://", true, message.toString(), null);

		return resp;

	}

	@Override
	public U8SellComnInv encapsulation(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList) {

		// 行对象-->单条发票
		U8SellComnInv dataRow = new U8SellComnInv();
		// 主表
		dataRow.setDscode(sellComnInv.getSellInvNum());
		dataRow.setVouttype(sellComnInv.getInvTyp().equals("销售专用发票") ? "26" : "27");// 26专票 27普票
		dataRow.setCstcode("01");
		dataRow.setDdate(sellComnInv.getBllgDt());
		dataRow.setCdepcode(sellComnInv.getDeptId());
		dataRow.setCcuscode(sellComnInv.getCustId());
		if (dataRow.getVouttype().equals("26")) {
			CustCls custCls = custDocDao.selectCustDocByCustId(sellComnInv.getCustId());
			CustDoc custDoc = custCls.getCustDoc().get(0);

			dataRow.setCcusbank(custDoc.getOpnBnk());// 客户银行 专票必填
			dataRow.setCcusaccount(custDoc.getBkatNum());// 客户账户
		}
		dataRow.setCpersoncode(sellComnInv.getAccNum());
		dataRow.setRemark(sellComnInv.getMemo());
		// 子表集合
		ArrayList<U8SellComnInvSub> detailsList = new ArrayList<U8SellComnInvSub>();
		BigDecimal[] headTaxRate = new BigDecimal[] { BigDecimal.ZERO };
		// 循环添加子表
		sellComnInvSubList.forEach(item -> {
			U8SellComnInvSub details = new U8SellComnInvSub();
			details.setCinvcode(item.getInvtyEncd());
			details.setQuantity(item.getQty());
			details.setIprice(item.getNoTaxUprc());
			details.setCitemcode(item.getProjEncd());
			details.setItaxrate(item.getTaxRate());
			headTaxRate[0] = item.getTaxRate();// 表头税率
			details.setCbatch(item.getBatNum());
			details.setCwhcode(item.getWhsEncd());
			detailsList.add(details);
		});
		// 表头税率循环定
		dataRow.setItaxrate(sellComnInv.getTabHeadTaxRate() == null ? headTaxRate[0]
				: new BigDecimal(sellComnInv.getTabHeadTaxRate()));
		dataRow.setSubList(detailsList);
		return dataRow;
	}

	public String connectToU8(String methodName, String dataXmlStr, String ztCodeStr) {
		String resultStr = "";
		try {
			ServiceClient serviceClient = new ServiceClient();
			// 创建服务地址WebService的URL,注意不是WSDL的URL
			String url = "http://106.14.183.228:8081/YBService.asmx";
			EndpointReference targetEPR = new EndpointReference(url);
			Options options = serviceClient.getOptions();
			options.setTo(targetEPR);
			// 确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
			options.setAction("http://tempuri.org/" + methodName);

			OMFactory fac = OMAbstractFactory.getOMFactory();
			/*
			 * 指定命名空间，参数： uri--即为wsdl文档的targetNamespace，命名空间 perfix--可不填
			 */
			OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
			// 指定方法
			OMElement method = fac.createOMElement(methodName, omNs);
			// 指定方法的参数
			OMElement dataXml = fac.createOMElement("dataXml", omNs);
			OMElement ztCode = fac.createOMElement("ztCode", omNs);
			dataXml.setText(dataXmlStr);
			ztCode.setText(ztCodeStr);

			method.addChild(dataXml);
			method.addChild(ztCode);
			method.build();

			// 远程调用web服务
			OMElement result = serviceClient.sendReceive(method);
			resultStr = result.toString();
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();

		}
		return resultStr;
	}

	// 子主类
	public static class zizhu {
		@JsonProperty("销售发票号")
		public String sellInvNum;
		@JsonProperty("开票日期")
		public String bllgDt;
		@JsonProperty("发票类型")
		public String invTyp;
		@JsonProperty("销售类型")
		public String sellTypNm;
		@JsonProperty("业务类型")
		public String bizTypNm;
		@JsonProperty("业务员编号")
		public String accNum;
		@JsonProperty("部门")
		public String deptNm;
		@JsonProperty("客户")
		public String custNm;
		@JsonProperty("是否审核")
		public Integer isNtChk;
		@JsonProperty("表头备注")
		public String memo;
		@JsonProperty("序号")
		public Integer ordrNum;
		@JsonProperty("销售单号")
		public String sellSnglNum;
		@JsonProperty("销售单号(参照子表)")
		public Long sellSnglSubId;
		@JsonProperty("仓库名称")
		public String whsNm;
		@JsonProperty("仓库编码")
		public String whsEncd;
		@JsonProperty("存货编码")
		public String invtyEncd;
		@JsonProperty("存货名称")
		public String invtyNm;
		@JsonProperty("项目编码")
		public String projEncd;
		@JsonProperty("规格型号")
		public String spcModel;
		@JsonProperty("计量单位")
		public String measrCorpNm;
		@JsonProperty("数量")
		public BigDecimal qty;
		@JsonProperty("含税单价")
		public BigDecimal cntnTaxUprc;
		@JsonProperty("价税合计")
		public BigDecimal prcTaxSum;
		@JsonProperty("无税单价")
		public BigDecimal noTaxUprc;
		@JsonProperty("无税金额")
		public BigDecimal noTaxAmt;
		@JsonProperty("税额")
		public BigDecimal taxAmt;
		@JsonProperty("税率")
		public BigDecimal taxRate;
		@JsonProperty("箱规")
		public BigDecimal bxRule;
		@JsonProperty("箱数")
		public BigDecimal bxQty;
		@JsonProperty("批次")
		public String batNum;
		@JsonProperty("国际批次")
		public String intlBat;
		@JsonProperty("表体备注")
		public String memos;

		// 忽略
		@JsonIgnore
		@JsonProperty("业务类型编号")
		public String bizTypId;
		@JsonIgnore
		@JsonProperty("销售类型编号")
		public String sellTypId;
		@JsonIgnore
		@JsonProperty("表头税率")
		public String tabHeadTaxRate;
		@JsonIgnore
		@JsonProperty("收发类别编号")
		public String recvSendCateId;
		@JsonIgnore
		@JsonProperty("客户编号")
		public String custId;
		@JsonIgnore
		@JsonProperty("部门编号")
		public String deptId;
		@JsonIgnore
		@JsonProperty("创建人")
		public String setupPers;
		@JsonIgnore
		@JsonProperty("创建日期")
		public String setupTm;
		@JsonIgnore
		@JsonProperty("修改人")
		public String mdfr;
		@JsonIgnore
		@JsonProperty("修改时间")
		public String modiTm;
		@JsonIgnore
		@JsonProperty("审核人")
		public String chkr;
		@JsonIgnore
		@JsonProperty("审核时间")
		public String chkTm;
		@JsonIgnore
		@JsonProperty("是否记账")
		public Integer isNtBookEntry;
		@JsonIgnore
		@JsonProperty("记账人")
		public String bookEntryPers;
		@JsonIgnore
		@JsonProperty("记账时间")
		public String bookEntryTm;
		@JsonIgnore
		@JsonProperty("科目编码")
		public String subjEncd;
		@JsonIgnore
		@JsonProperty("客户联系人")
		public String contcr;
		@JsonIgnore
		@JsonProperty("客户银行")
		public String bank;
		@JsonIgnore
		@JsonProperty("客户账号")
		public String acctNum;
		@JsonIgnore
		@JsonProperty("制单时间")
		public String makDocTm;
		@JsonIgnore
		@JsonProperty("制单人")
		public String makDocPers;
		@JsonIgnore
		@JsonProperty("用户名称")
		public String userName;
		@JsonIgnore
		@JsonProperty("单据类型编码")
		public String formTypEncd;
		@JsonIgnore
		@JsonProperty("来源单据类型编码")
		public String toFormTypEncd;
		@JsonIgnore
		@JsonProperty("是否生成凭证")
		public Integer isNtMakeVouch;
		@JsonIgnore
		@JsonProperty("制凭证人")
		public String makVouchPers;
		@JsonIgnore
		@JsonProperty("制凭证时间")
		public String makVouchTm;
		@JsonIgnore
		@JsonProperty("计量单位编号")
		public String measrCorpId;
		@JsonIgnore
		@JsonProperty("是否是赠品")
		public Integer isComplimentary;
		@JsonIgnore
		@JsonProperty("发货单号")
		public String delvSnglNum;
		@JsonIgnore
		@JsonProperty("存货一级分类")
		public String invtyFstLvlCls;
		@JsonIgnore
		@JsonProperty("发货单标识")
		public String sellSnglNums;
		@JsonIgnore
		@JsonProperty("存货代码")
		public String invtyCd;
		@JsonIgnore
		@JsonProperty("保质期天数")
		public String baoZhiQiDt;
		@JsonIgnore
		@JsonProperty("对应条形码")
		public String crspdBarCd;
		@JsonIgnore
		@JsonProperty("是否退货")
		public Integer isNtRtnGoods;
		@JsonIgnore
		@JsonProperty("销售出库单编码")
		public String outWhsId;
		@JsonIgnore
		@JsonProperty("销售出库单子表标识")
		public Long outWhsSubId;
		@JsonIgnore
		@JsonProperty("项目名称")
		public String projNm;
	}
}
