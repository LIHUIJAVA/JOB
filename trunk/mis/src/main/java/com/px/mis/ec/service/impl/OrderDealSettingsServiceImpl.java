package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.OrderDealSettingsDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.OrderDealSettings;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.PlatSpecialWhs;
import com.px.mis.ec.entity.PlatWhs;
import com.px.mis.ec.service.OrderDealSettingsService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;

@Transactional
@Service
public class OrderDealSettingsServiceImpl implements OrderDealSettingsService {

	private Logger logger = LoggerFactory.getLogger(OrderDealSettingsServiceImpl.class);

	@Autowired
	private OrderDealSettingsDao orderDealSettingsDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private GoodRecordDao goodRecordDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private ProdStruMapper prodStruDto;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private InvtyNumMapper invtyNumDao;
	@Override
	public String edit(OrderDealSettings orderDealSettings) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			orderDealSettingsDao.update(orderDealSettings);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("ec/orderDealSetting/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/orderDealSetting/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String settingId) {
		String resp = "";
		try {
			OrderDealSettings orderDealSettings = orderDealSettingsDao.select(settingId);
			resp = BaseJson.returnRespObj("ec/orderDealSetting/query", true, "查询成功!", orderDealSettings);
		} catch (IOException e) {
			logger.error("URL：ec/orderDealSetting/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public Map<String,Object> matchWareHouse(PlatOrder platOrder, String platId) {
		
		Map<String,Object> resultMap = new HashMap<>();
		String resp = "";
		boolean flag = true;
		//判断是否需要匹配仓库 1.平台仓发货2.无存货编码
		//订单
		if(platOrder.getDeliverSelf() == 0) {
			resp = "订单为平台仓发货,无法匹配仓库";
			flag = false;
			
		} else if(platOrder.getPlatOrdersList().size() > 0) {
			for(PlatOrders platOrders : platOrder.getPlatOrdersList()) {
				if(StringUtils.isEmpty(platOrders.getInvId())) {				
					resp = "商品:"+platOrders.getGoodId()+"对应存货编码为空,无法匹配仓库";
					flag = false;
					break;			
				}  
				if(platOrders.getInvNum() == null) {
					resp = "商品编号:"+platOrders.getGoodId()+"对应存货数量为空,无法匹配仓库";
					flag = false;
					break;
				}
			}
		}
		if(flag) {
			//匹配特殊存货仓库编码
			resultMap = mathWhsSpecialHouse(platOrder,platId);
			Integer isSpec = (Integer)resultMap.get("isSpec");
			if(isSpec == null) {
				flag = false;
				resp = "匹配是否特殊存货失败";
				
			} else {
				if(isSpec == 1) {
					
					List<PlatWhs> whsCodeList = (List<PlatWhs>) resultMap.get("whsCodeList");
					if(whsCodeList == null) {
						whsCodeList = new ArrayList<>();
						flag = false;
						resp = "匹配是否特殊存货失败";
						
					} else {
						return matchWhsCode(whsCodeList,platOrder.getPlatOrdersList());
					}
					
				} else {
					//匹配正常存货仓库编码
					resultMap = matchWhsCityHouse(platOrder,platId);
					Integer isWhs = (Integer)resultMap.get("isWhs");
					if(isWhs == null) {
						flag = false;
						resp = "匹配是否正常存货失败";
						
					} else {
						if(isWhs == 1) {
							//匹配仓库的库存量是否充足
							List<PlatWhs> whsCodeList = (List<PlatWhs>) resultMap.get("whsCodeList");
							return matchWhsCode(whsCodeList,platOrder.getPlatOrdersList());
						} else {
							flag = false;
							resp = resultMap.get("message").toString();
						}
							
					}
				}
			}
		} 
		
		resultMap.put("message",resp);
		resultMap.put("isSuccess", flag);		 
		return resultMap;
			
				
	}
	
	/**
	 * 匹配订单是否为特殊存货
	 */
	private Map<String,Object> mathWhsSpecialHouse(PlatOrder platOrder,String platId) {
		List<ProdStruSubTab> invtyCodeList = new ArrayList<>(); 
		List<PlatWhs> whsCodeList = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<>();
		String resp = "";
		boolean flag = true;
		
						
		//查找存货编码,平台编码查找特殊存货对应的仓库编号
		for(PlatOrders prodStu :platOrder.getPlatOrdersList()) {
			PlatSpecialWhs platSpecialWhs = orderDealSettingsDao.selectByInvtyCodeAndPlatCode(platId, prodStu.getInvId());
			
			if(platSpecialWhs == null) {
				//仓库编码为""
				resp = "未获取到存货编码:["+prodStu.getInvId()+"]对应仓库编码";
				flag = false;
			} else {
				
				flag= true;
				
				PlatWhs whs = new PlatWhs();
				whs.setPlatCode(platId); //平台编码
				whs.setInvtyCode(prodStu.getInvId()); //存货编号
				whs.setWhsCode(platSpecialWhs.getWhsCode()); //仓库编码
				//TODO 可用量
				
				whs.setSubQtyAll(new BigDecimal(prodStu.getInvNum())); //下单量用于计算库存
				whsCodeList.add(whs);
				resultMap.put("whsCodeList", whsCodeList);
				//logger.info("特殊匹配仓库编码:平台编码->{},存货编码->{},仓库编码->{},商品量->{}",platId,prodStu.getInvId(),invtyCodeList.get(0).getSubEncd(),prodStu.getInvNum());
			}
			
		}				
		
		resultMap.put("isSuccess", flag);
		if(flag) {
			resultMap.put("isSpec", 1); //是否为特殊存货标识
			return resultMap;
		} else {
			resultMap.put("isSpec", 0); //是否为特殊存货标识
			resultMap.put("message", resp);
			return resultMap;
		}
	}
	/**
	 * 匹配订单的正常存货编码
	 * @param orderId
	 * @param platId
	 * @return
	 */
	private Map<String,Object> matchWhsCityHouse(PlatOrder platOrder,String platId){
		Map<String,Object> resultMap = new HashMap<>();
		List<PlatWhs> whsCodeList = new ArrayList<>();
		List<ProdStruSubTab> invtyCodeList = new ArrayList<>(); 
		String resp = "";
		Boolean flag = true;
		//BigDecimal subqty = new BigDecimal(0);
		
		
		String orderId = platOrder.getOrderId();
		//计算下单件用于计算库存量
		List<PlatOrders> platOrdersList = platOrder.getPlatOrdersList();//platOrdersDao.select(orderId); //对应订单的sku集合
		if(platOrdersList.size() == 0) {
			resp = "未获取到订单编号:["+orderId+"]对应商品sku";
			flag = false;
		} else {
			
			for(PlatOrders platOrders : platOrdersList) {
				//BigDecimal item = new BigDecimal(platOrders.getInvNum()); //sku对应的下单件数
				//subqty = subqty.add(item); 	//总件数
				
				ProdStruSubTab prodStruSub = new ProdStruSubTab();
				prodStruSub.setSubEncd(platOrders.getInvId());//产品编码(subEncd)
				
				prodStruSub.setSubQty(new BigDecimal(platOrders.getInvNum())); //总数量
				invtyCodeList.add(prodStruSub);	
			}
		}
		
		//兑换存货编码
		//resultMap = matchWhsCodeByOrder(platOrder);
		//Integer isEncd = (Integer)resultMap.get("isEncd");
		//if(isEncd == null) {
			
		//	return resultMap;
		//} else {
		//	if(isEncd == 1) {
		//		invtyCodeList = (List<ProdStruSubTab>) resultMap.get("invtyCodeList");	
				
		//	} 
		//}
			
		//1.根据订单编号获取订单地址
		//PlatOrder platOrder = platOrderDao.select(orderId);
		if(StringUtils.isEmpty(platOrder.getProvince()) || StringUtils.isEmpty(platOrder.getCity())) {	
			
			 resp = "未获取到订单编号:["+orderId+"]对应收货地址";
			 flag = false;
			 
		} else {
			
			//2.根据订单地址获取区域省份城市
			String address = platOrder.getRecAddress(); //地址
			String province = platOrder.getProvince(); //省
			String city = platOrder.getCity(); //市
			
			//3.匹配区域的地址获取区域编码
			City citys = orderDealSettingsDao.selectCityCode(province,city);
			if(citys == null) {
				 resp = "未获取到平台:["+platId+"],区域:["+province+city+"]对应区域编码";
				 flag = false;
			} else {
				//4.根据区域编码和平台编码获取仓库编码
				String cityCode = citys.getCodeId();
				List<PlatWhs> platWhsList = orderDealSettingsDao.selectByInvtyCodeAndCityCode(platId, cityCode);
				if(platWhsList.size() == 0) {
					//仓库编码为空
					resp = "未获取到平台:["+platId+"],区域:["+citys.getCodeName()+"]+对应仓库编码";
					flag = false;
				} else {
					
					for(PlatWhs platWhs : platWhsList) {
						for(ProdStruSubTab prod : invtyCodeList) {
							PlatWhs whs = new PlatWhs();
							whs.setPlatCode(platId); //平台编码
							whs.setCityCode(cityCode); //区域编号
							
							if(invtyCodeList.size() == 0) {					
								resp = "存货编码查找为空";
								flag = false;
							} else {
								whs.setInvtyCode(prod.getSubEncd()); //存货编码
							}
							whs.setWhsCode(platWhs.getWhsCode()); //仓库编码
							whs.setSubQtyAll(prod.getSubQty()); //下单量用于计算库存
							whsCodeList.add(whs);
							logger.info("正常匹配仓库编码:平台编码->{},区域编码->{},仓库编码->{},商品量->{}",platId,cityCode,platWhs.getWhsCode(),prod.getSubQty());
						}
										
					}
					
					resultMap.put("whsCodeList", whsCodeList);
				}
			}
		}
				
		
		if(flag) {
			resultMap.put("isWhs", 1); //是否为正常存货标识 1为真
			resultMap.put("message",resp);
			resultMap.put("isSuccess", flag);
			return resultMap;
		} else {
			resultMap.put("isWhs", 0); //是否为正常存货标识 1为真
			resultMap.put("message", resp);
			resultMap.put("isSuccess", flag);
			return resultMap;
		}
	}
	/**
	 * 根据仓库编码,存货编码匹配仓库的可用量
	 * @param whsCodeList
	 * @param platOrdersList 
	 * @return
	 */
	private Map<String,Object> matchWhsCode(List<PlatWhs> whsCodeList, List<PlatOrders> platOrdersList){
		Map<String,Object> resultMap = new HashMap<>();
		String whsEncd = "";
		Boolean isSuccess = true;
		String error = "";
		
		if(whsCodeList.size() == 0) {		
			error = "仓库编码集合为空";
			isSuccess = false;	
		} else {
			for(PlatWhs plat:whsCodeList) {
				InvtyTab invtyTab = new InvtyTab();
				
				for(int i=0;i<platOrdersList.size();i++) {
					PlatOrders platOrders = platOrdersList.get(i);
					
					invtyTab.setWhsEncd(plat.getWhsCode());//仓库编码
					invtyTab.setInvtyEncd(platOrders.getInvId()); //存货编码
					List<InvtyTab> tabs = invtyNumDao.selectInvtyTabByBatNum(invtyTab); //查询可用量
					if(tabs.size() == 0) {
						//未查到仓库
						error = "匹配仓库时未匹配到有可用量的仓库";
						whsEncd = "";
						isSuccess = false;
						break;
					} else {	
						for(InvtyTab tab : tabs) {
							BigDecimal avalQty = tab.getAvalQty();//可用量
							int isAval = avalQty.compareTo(plat.getSubQtyAll());
							System.out.println("-------------------仓库匹配中----------------------------");
							System.out.println("仓库编码:"+plat.getWhsCode()+"仓库可用量:"+avalQty);
							System.out.println("存货编码:"+platOrders.getInvId()+"商品量:"+plat.getSubQtyAll());
							if(isAval >= 0) {
								if(i == 0) {
									resultMap.put("whsCode", plat.getWhsCode());
									
								}
								String whsCode = (String)resultMap.get("whsCode");
								if(whsCode.equals(tab.getWhsEncd())) {
									
									whsCode = plat.getWhsCode();
									isSuccess = false;
									if(i == platOrdersList.size() - 1) {
										whsEncd = plat.getWhsCode();
										isSuccess = true;
										break;
									}
								}							
								
							} else {								
								continue;
							}
						}
					}
				}			
				
			}
		}
		resultMap.put("whsCode", whsEncd);
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("message", error);
		return resultMap;
	}
	/**
	 * 兑换订单的存货编码
	 * @param orderId
	 */
	private Map<String,Object> matchWhsCodeByOrder(PlatOrder platOrder) {
		Map<String,Object> resultMap = new HashMap<>(); 
		List<ProdStruSubTab> invtyCodeList = new ArrayList<>(); //存货编码集合
		String resp = "";
		Boolean flag = true;
		
		//根据订单编号获取sku
		List<PlatOrders> platOrdersList = platOrder.getPlatOrdersList();//platOrdersDao.select(orderId); //对应订单的sku集合
		
		if(platOrdersList.size() > 0) {
			for(PlatOrders platOrders : platOrdersList) {
								
				String goodSku = platOrders.getGoodSku(); //商品sku
					
				//根据sku获取店铺商品档案(good_record)商品编码goodId
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodSku,platOrders.getEcOrderId());
				
				if(goodRecord == null) {
					resp = "未获取到商品sku:["+goodSku+"]对应店铺商品档案";
					flag = false;
					
				} else {
					String invtyId = goodRecord.getGoodId(); //商品编号goodId
					
					//按照商品编号查询存货档案(invty_doc),判断产品结构pto
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyId);
					if(invtyDoc == null) {
						resp = "未获取到商品编号:["+invtyId+"]对应存货档案";
						flag = false;
						
					} else {
						if(invtyDoc.getPto() == 1) {
							// pto组套母产品
							ProdStru prodStru = prodStruDto.selectMomEncd(invtyId);
							if(prodStru == null) {
								resp = "未获取到商品编号:["+invtyId+"]对应组件";
								flag = false;
								
							} else {
								//根据母产品编码(orderNum)获取子产品结构
								List<ProdStru> prodStruList = prodStruDto.selectProdStruByMom(prodStru.getOrdrNum());
								if(prodStruList.size() == 0) {			
									resp = "未获取到产品结构:["+invtyId+"]对应产品结构信息";
									flag = false;
									
								} else {
									for(int i=0;i<prodStruList.size();i++) {
										List<ProdStruSubTab> strucksublist = prodStruList.get(i).getStruSubList();
										if(strucksublist.size() == 0) {
											resp = "产品结构子表信息为空";
											flag = false;
											break;
										} else {
											for(ProdStruSubTab prodStruSubTab : strucksublist) {
												// 存储存货编码list
												ProdStruSubTab prodStruSub = new ProdStruSubTab();										
												prodStruSub.setSubEncd(prodStruSubTab.getSubEncd()); //子件编码(subEncd)
												//总数量
												prodStruSub.setSubQty(new BigDecimal(platOrders.getGoodNum().toString()).multiply(prodStruSubTab.getSubQty())); 
												invtyCodeList.add(prodStruSub);	
												resultMap.put("invtyCodeList",invtyCodeList);
											}
										}
									}
								}
							}
							
						} else if(invtyDoc.getPto() == 0) {
							//单件产品
							ProdStruSubTab prodStruSub = new ProdStruSubTab();
							prodStruSub.setSubEncd(invtyDoc.getInvtyEncd());//产品编码(subEncd)
							prodStruSub.setSubQty(new BigDecimal(platOrders.getGoodNum().toString())); //总数量
							invtyCodeList.add(prodStruSub);	
							resultMap.put("invtyCodeList",invtyCodeList);
						} else {
							resp = "无法识别到商品编号:["+invtyId+"]对应pto";
							flag = false;
							
						}
					}				
				}					
			}
		} else {
			resp = "获取订单商品明细失败";
			flag = false;
		}
					
		if(flag) {
			resultMap.put("isEncd", 1); //是否兑换为存货编码标识 1为是		
			return resultMap;
		} else {
			resultMap.put("isEncd", 0); //是否兑换为存货编码标识 0为否
			resultMap.put("error", resp);
			return resultMap;
		}
	}

	@Override
	public String selectPlatWhsSpecialList(String jsonBody) {
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<PlatSpecialWhs> platSpecialWhs = orderDealSettingsDao.selectPlatWhsSpecialListByMap(map);		
			int count = orderDealSettingsDao.selectPlatWhsSpecialListCountByMap(map);
			resp = BaseJson.returnRespList("ec/platWhsSpecial/selectList", true, "查询成功", count, pageNo, pageSize, 0, 0,
					platSpecialWhs);
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
		
		return resp;
	}

	@Override
	public String addPlatWhsSpecial(String jsonBody) {
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String platEncd = (String) map.get("platEncd");
			String whsEncd = (String) map.get("whsEncd");
			String invtyEncd = (String) map.get("invtyEncd");
			if(StringUtils.isEmpty(platEncd) && StringUtils.isEmpty(whsEncd) && StringUtils.isEmpty(invtyEncd)) {
				message = "解析参数异常";
				isSuccess = false;
			} else {
				map.put("index", 0);
				map.put("num", 99);
				List<PlatSpecialWhs> list = orderDealSettingsDao.selectPlatWhsSpecialListByMap(map);
				if(list.size() != 0) {
					message = "特殊存货已存在,请重新选择";
					isSuccess = false;
				} else {
					
					PlatSpecialWhs whs = new PlatSpecialWhs();
					whs.setInvtyCode(invtyEncd);
					whs.setPlatCode(platEncd);
					whs.setWhsCode(whsEncd);
					int w = orderDealSettingsDao.addPlatWhsSpecial(whs);
					if(w > 0) {
						message= "新增成功";
						isSuccess = true;
						logger.info("新增特殊仓库成功,操作人：{}",accNum);
					}
				}
			}
		
			resp = BaseJson.returnRespObj("ec/platWhsSpecial/add", isSuccess, message, null);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String deletePlatWhs(String jsonBody) {
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String stId = (String) map.get("stId");
			List<String> list = strToList(stId);
			if(list.size() > 0) {
				int i = orderDealSettingsDao.deleteById(list);
				if(i > 0) {
					message = "删除成功";
					isSuccess = true;
					logger.info("删除特殊仓库成功,操作人：{}",accNum);
				}
			} else {
				message = "解析参数异常";
				isSuccess = false;
			}		
			
			resp = BaseJson.returnRespObj("ec/platWhsSpecial/delete", isSuccess, message, null);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return resp;
	}
	
	@Override
	public String updatePlatWhs(String jsonBody) {
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String platEncd = (String) map.get("platEncd");
			String whsEncd = (String) map.get("whsEncd");
			String invtyEncd = (String) map.get("invtyEncd");
			String stId = (String) map.get("stId");
			if(StringUtils.isEmpty(platEncd) && StringUtils.isEmpty(whsEncd) && StringUtils.isEmpty(invtyEncd) && StringUtils.isEmpty(stId)) {
				message = "解析参数异常";
				isSuccess = false;
			} else {
				map.put("index", 0);
				map.put("num", 99);
				List<PlatSpecialWhs> list = orderDealSettingsDao.selectPlatWhsSpecialListByMap(map);
				if(list.size() != 0) {
					message = "特殊存货已存在,请重新修改";
					isSuccess = false;
				} else {
					PlatSpecialWhs whs = orderDealSettingsDao.selectPlatWhsSpecialById(Integer.valueOf(stId));
					
					if(whs == null) {
						message = "此特殊存货记录不存在,请核对参数";
						isSuccess = false;
					} else {
						whs.setPlatCode(platEncd);
						whs.setWhsCode(whsEncd);
						whs.setInvtyCode(invtyEncd);
						orderDealSettingsDao.updatePlatWhsSpecialById(whs);
						message = "修改成功";
						isSuccess = true;
					}
				}
			}
			resp = BaseJson.returnRespObj("ec/platWhsSpecial/update", isSuccess, message, null);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return resp;
	}
	
	/**
	 * 字符串转list
	 */
	private List<String> strToList(String param){
		
		List<String> list = new ArrayList<>();
		if(StringUtils.isNotEmpty(param)) {
			if(param.contains(",")) {
				String[] str = param.split(",");
				 for (int i = 0 ; i <str.length ; i++ ) {

					 list.add(str[i]);

				 }
			} else {
				if(StringUtils.isNotEmpty(param)) {
					list.add(param);
				}			
			}
		}
		
		return list;
		
	}

	
}
