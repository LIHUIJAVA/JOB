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
			message = "���³ɹ���";
			resp = BaseJson.returnRespObj("ec/orderDealSetting/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/orderDealSetting/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String settingId) {
		String resp = "";
		try {
			OrderDealSettings orderDealSettings = orderDealSettingsDao.select(settingId);
			resp = BaseJson.returnRespObj("ec/orderDealSetting/query", true, "��ѯ�ɹ�!", orderDealSettings);
		} catch (IOException e) {
			logger.error("URL��ec/orderDealSetting/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public Map<String,Object> matchWareHouse(PlatOrder platOrder, String platId) {
		
		Map<String,Object> resultMap = new HashMap<>();
		String resp = "";
		boolean flag = true;
		//�ж��Ƿ���Ҫƥ��ֿ� 1.ƽ̨�ַ���2.�޴������
		//����
		if(platOrder.getDeliverSelf() == 0) {
			resp = "����Ϊƽ̨�ַ���,�޷�ƥ��ֿ�";
			flag = false;
			
		} else if(platOrder.getPlatOrdersList().size() > 0) {
			for(PlatOrders platOrders : platOrder.getPlatOrdersList()) {
				if(StringUtils.isEmpty(platOrders.getInvId())) {				
					resp = "��Ʒ:"+platOrders.getGoodId()+"��Ӧ�������Ϊ��,�޷�ƥ��ֿ�";
					flag = false;
					break;			
				}  
				if(platOrders.getInvNum() == null) {
					resp = "��Ʒ���:"+platOrders.getGoodId()+"��Ӧ�������Ϊ��,�޷�ƥ��ֿ�";
					flag = false;
					break;
				}
			}
		}
		if(flag) {
			//ƥ���������ֿ����
			resultMap = mathWhsSpecialHouse(platOrder,platId);
			Integer isSpec = (Integer)resultMap.get("isSpec");
			if(isSpec == null) {
				flag = false;
				resp = "ƥ���Ƿ�������ʧ��";
				
			} else {
				if(isSpec == 1) {
					
					List<PlatWhs> whsCodeList = (List<PlatWhs>) resultMap.get("whsCodeList");
					if(whsCodeList == null) {
						whsCodeList = new ArrayList<>();
						flag = false;
						resp = "ƥ���Ƿ�������ʧ��";
						
					} else {
						return matchWhsCode(whsCodeList,platOrder.getPlatOrdersList());
					}
					
				} else {
					//ƥ����������ֿ����
					resultMap = matchWhsCityHouse(platOrder,platId);
					Integer isWhs = (Integer)resultMap.get("isWhs");
					if(isWhs == null) {
						flag = false;
						resp = "ƥ���Ƿ��������ʧ��";
						
					} else {
						if(isWhs == 1) {
							//ƥ��ֿ�Ŀ�����Ƿ����
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
	 * ƥ�䶩���Ƿ�Ϊ������
	 */
	private Map<String,Object> mathWhsSpecialHouse(PlatOrder platOrder,String platId) {
		List<ProdStruSubTab> invtyCodeList = new ArrayList<>(); 
		List<PlatWhs> whsCodeList = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<>();
		String resp = "";
		boolean flag = true;
		
						
		//���Ҵ������,ƽ̨���������������Ӧ�Ĳֿ���
		for(PlatOrders prodStu :platOrder.getPlatOrdersList()) {
			PlatSpecialWhs platSpecialWhs = orderDealSettingsDao.selectByInvtyCodeAndPlatCode(platId, prodStu.getInvId());
			
			if(platSpecialWhs == null) {
				//�ֿ����Ϊ""
				resp = "δ��ȡ���������:["+prodStu.getInvId()+"]��Ӧ�ֿ����";
				flag = false;
			} else {
				
				flag= true;
				
				PlatWhs whs = new PlatWhs();
				whs.setPlatCode(platId); //ƽ̨����
				whs.setInvtyCode(prodStu.getInvId()); //������
				whs.setWhsCode(platSpecialWhs.getWhsCode()); //�ֿ����
				//TODO ������
				
				whs.setSubQtyAll(new BigDecimal(prodStu.getInvNum())); //�µ������ڼ�����
				whsCodeList.add(whs);
				resultMap.put("whsCodeList", whsCodeList);
				//logger.info("����ƥ��ֿ����:ƽ̨����->{},�������->{},�ֿ����->{},��Ʒ��->{}",platId,prodStu.getInvId(),invtyCodeList.get(0).getSubEncd(),prodStu.getInvNum());
			}
			
		}				
		
		resultMap.put("isSuccess", flag);
		if(flag) {
			resultMap.put("isSpec", 1); //�Ƿ�Ϊ��������ʶ
			return resultMap;
		} else {
			resultMap.put("isSpec", 0); //�Ƿ�Ϊ��������ʶ
			resultMap.put("message", resp);
			return resultMap;
		}
	}
	/**
	 * ƥ�䶩���������������
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
		//�����µ������ڼ�������
		List<PlatOrders> platOrdersList = platOrder.getPlatOrdersList();//platOrdersDao.select(orderId); //��Ӧ������sku����
		if(platOrdersList.size() == 0) {
			resp = "δ��ȡ���������:["+orderId+"]��Ӧ��Ʒsku";
			flag = false;
		} else {
			
			for(PlatOrders platOrders : platOrdersList) {
				//BigDecimal item = new BigDecimal(platOrders.getInvNum()); //sku��Ӧ���µ�����
				//subqty = subqty.add(item); 	//�ܼ���
				
				ProdStruSubTab prodStruSub = new ProdStruSubTab();
				prodStruSub.setSubEncd(platOrders.getInvId());//��Ʒ����(subEncd)
				
				prodStruSub.setSubQty(new BigDecimal(platOrders.getInvNum())); //������
				invtyCodeList.add(prodStruSub);	
			}
		}
		
		//�һ��������
		//resultMap = matchWhsCodeByOrder(platOrder);
		//Integer isEncd = (Integer)resultMap.get("isEncd");
		//if(isEncd == null) {
			
		//	return resultMap;
		//} else {
		//	if(isEncd == 1) {
		//		invtyCodeList = (List<ProdStruSubTab>) resultMap.get("invtyCodeList");	
				
		//	} 
		//}
			
		//1.���ݶ�����Ż�ȡ������ַ
		//PlatOrder platOrder = platOrderDao.select(orderId);
		if(StringUtils.isEmpty(platOrder.getProvince()) || StringUtils.isEmpty(platOrder.getCity())) {	
			
			 resp = "δ��ȡ���������:["+orderId+"]��Ӧ�ջ���ַ";
			 flag = false;
			 
		} else {
			
			//2.���ݶ�����ַ��ȡ����ʡ�ݳ���
			String address = platOrder.getRecAddress(); //��ַ
			String province = platOrder.getProvince(); //ʡ
			String city = platOrder.getCity(); //��
			
			//3.ƥ������ĵ�ַ��ȡ�������
			City citys = orderDealSettingsDao.selectCityCode(province,city);
			if(citys == null) {
				 resp = "δ��ȡ��ƽ̨:["+platId+"],����:["+province+city+"]��Ӧ�������";
				 flag = false;
			} else {
				//4.������������ƽ̨�����ȡ�ֿ����
				String cityCode = citys.getCodeId();
				List<PlatWhs> platWhsList = orderDealSettingsDao.selectByInvtyCodeAndCityCode(platId, cityCode);
				if(platWhsList.size() == 0) {
					//�ֿ����Ϊ��
					resp = "δ��ȡ��ƽ̨:["+platId+"],����:["+citys.getCodeName()+"]+��Ӧ�ֿ����";
					flag = false;
				} else {
					
					for(PlatWhs platWhs : platWhsList) {
						for(ProdStruSubTab prod : invtyCodeList) {
							PlatWhs whs = new PlatWhs();
							whs.setPlatCode(platId); //ƽ̨����
							whs.setCityCode(cityCode); //������
							
							if(invtyCodeList.size() == 0) {					
								resp = "����������Ϊ��";
								flag = false;
							} else {
								whs.setInvtyCode(prod.getSubEncd()); //�������
							}
							whs.setWhsCode(platWhs.getWhsCode()); //�ֿ����
							whs.setSubQtyAll(prod.getSubQty()); //�µ������ڼ�����
							whsCodeList.add(whs);
							logger.info("����ƥ��ֿ����:ƽ̨����->{},�������->{},�ֿ����->{},��Ʒ��->{}",platId,cityCode,platWhs.getWhsCode(),prod.getSubQty());
						}
										
					}
					
					resultMap.put("whsCodeList", whsCodeList);
				}
			}
		}
				
		
		if(flag) {
			resultMap.put("isWhs", 1); //�Ƿ�Ϊ���������ʶ 1Ϊ��
			resultMap.put("message",resp);
			resultMap.put("isSuccess", flag);
			return resultMap;
		} else {
			resultMap.put("isWhs", 0); //�Ƿ�Ϊ���������ʶ 1Ϊ��
			resultMap.put("message", resp);
			resultMap.put("isSuccess", flag);
			return resultMap;
		}
	}
	/**
	 * ���ݲֿ����,�������ƥ��ֿ�Ŀ�����
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
			error = "�ֿ���뼯��Ϊ��";
			isSuccess = false;	
		} else {
			for(PlatWhs plat:whsCodeList) {
				InvtyTab invtyTab = new InvtyTab();
				
				for(int i=0;i<platOrdersList.size();i++) {
					PlatOrders platOrders = platOrdersList.get(i);
					
					invtyTab.setWhsEncd(plat.getWhsCode());//�ֿ����
					invtyTab.setInvtyEncd(platOrders.getInvId()); //�������
					List<InvtyTab> tabs = invtyNumDao.selectInvtyTabByBatNum(invtyTab); //��ѯ������
					if(tabs.size() == 0) {
						//δ�鵽�ֿ�
						error = "ƥ��ֿ�ʱδƥ�䵽�п������Ĳֿ�";
						whsEncd = "";
						isSuccess = false;
						break;
					} else {	
						for(InvtyTab tab : tabs) {
							BigDecimal avalQty = tab.getAvalQty();//������
							int isAval = avalQty.compareTo(plat.getSubQtyAll());
							System.out.println("-------------------�ֿ�ƥ����----------------------------");
							System.out.println("�ֿ����:"+plat.getWhsCode()+"�ֿ������:"+avalQty);
							System.out.println("�������:"+platOrders.getInvId()+"��Ʒ��:"+plat.getSubQtyAll());
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
	 * �һ������Ĵ������
	 * @param orderId
	 */
	private Map<String,Object> matchWhsCodeByOrder(PlatOrder platOrder) {
		Map<String,Object> resultMap = new HashMap<>(); 
		List<ProdStruSubTab> invtyCodeList = new ArrayList<>(); //������뼯��
		String resp = "";
		Boolean flag = true;
		
		//���ݶ�����Ż�ȡsku
		List<PlatOrders> platOrdersList = platOrder.getPlatOrdersList();//platOrdersDao.select(orderId); //��Ӧ������sku����
		
		if(platOrdersList.size() > 0) {
			for(PlatOrders platOrders : platOrdersList) {
								
				String goodSku = platOrders.getGoodSku(); //��Ʒsku
					
				//����sku��ȡ������Ʒ����(good_record)��Ʒ����goodId
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodSku,platOrders.getEcOrderId());
				
				if(goodRecord == null) {
					resp = "δ��ȡ����Ʒsku:["+goodSku+"]��Ӧ������Ʒ����";
					flag = false;
					
				} else {
					String invtyId = goodRecord.getGoodId(); //��Ʒ���goodId
					
					//������Ʒ��Ų�ѯ�������(invty_doc),�жϲ�Ʒ�ṹpto
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyId);
					if(invtyDoc == null) {
						resp = "δ��ȡ����Ʒ���:["+invtyId+"]��Ӧ�������";
						flag = false;
						
					} else {
						if(invtyDoc.getPto() == 1) {
							// pto����ĸ��Ʒ
							ProdStru prodStru = prodStruDto.selectMomEncd(invtyId);
							if(prodStru == null) {
								resp = "δ��ȡ����Ʒ���:["+invtyId+"]��Ӧ���";
								flag = false;
								
							} else {
								//����ĸ��Ʒ����(orderNum)��ȡ�Ӳ�Ʒ�ṹ
								List<ProdStru> prodStruList = prodStruDto.selectProdStruByMom(prodStru.getOrdrNum());
								if(prodStruList.size() == 0) {			
									resp = "δ��ȡ����Ʒ�ṹ:["+invtyId+"]��Ӧ��Ʒ�ṹ��Ϣ";
									flag = false;
									
								} else {
									for(int i=0;i<prodStruList.size();i++) {
										List<ProdStruSubTab> strucksublist = prodStruList.get(i).getStruSubList();
										if(strucksublist.size() == 0) {
											resp = "��Ʒ�ṹ�ӱ���ϢΪ��";
											flag = false;
											break;
										} else {
											for(ProdStruSubTab prodStruSubTab : strucksublist) {
												// �洢�������list
												ProdStruSubTab prodStruSub = new ProdStruSubTab();										
												prodStruSub.setSubEncd(prodStruSubTab.getSubEncd()); //�Ӽ�����(subEncd)
												//������
												prodStruSub.setSubQty(new BigDecimal(platOrders.getGoodNum().toString()).multiply(prodStruSubTab.getSubQty())); 
												invtyCodeList.add(prodStruSub);	
												resultMap.put("invtyCodeList",invtyCodeList);
											}
										}
									}
								}
							}
							
						} else if(invtyDoc.getPto() == 0) {
							//������Ʒ
							ProdStruSubTab prodStruSub = new ProdStruSubTab();
							prodStruSub.setSubEncd(invtyDoc.getInvtyEncd());//��Ʒ����(subEncd)
							prodStruSub.setSubQty(new BigDecimal(platOrders.getGoodNum().toString())); //������
							invtyCodeList.add(prodStruSub);	
							resultMap.put("invtyCodeList",invtyCodeList);
						} else {
							resp = "�޷�ʶ����Ʒ���:["+invtyId+"]��Ӧpto";
							flag = false;
							
						}
					}				
				}					
			}
		} else {
			resp = "��ȡ������Ʒ��ϸʧ��";
			flag = false;
		}
					
		if(flag) {
			resultMap.put("isEncd", 1); //�Ƿ�һ�Ϊ��������ʶ 1Ϊ��		
			return resultMap;
		} else {
			resultMap.put("isEncd", 0); //�Ƿ�һ�Ϊ��������ʶ 0Ϊ��
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
			resp = BaseJson.returnRespList("ec/platWhsSpecial/selectList", true, "��ѯ�ɹ�", count, pageNo, pageSize, 0, 0,
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
				message = "���������쳣";
				isSuccess = false;
			} else {
				map.put("index", 0);
				map.put("num", 99);
				List<PlatSpecialWhs> list = orderDealSettingsDao.selectPlatWhsSpecialListByMap(map);
				if(list.size() != 0) {
					message = "�������Ѵ���,������ѡ��";
					isSuccess = false;
				} else {
					
					PlatSpecialWhs whs = new PlatSpecialWhs();
					whs.setInvtyCode(invtyEncd);
					whs.setPlatCode(platEncd);
					whs.setWhsCode(whsEncd);
					int w = orderDealSettingsDao.addPlatWhsSpecial(whs);
					if(w > 0) {
						message= "�����ɹ�";
						isSuccess = true;
						logger.info("��������ֿ�ɹ�,�����ˣ�{}",accNum);
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
					message = "ɾ���ɹ�";
					isSuccess = true;
					logger.info("ɾ������ֿ�ɹ�,�����ˣ�{}",accNum);
				}
			} else {
				message = "���������쳣";
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
				message = "���������쳣";
				isSuccess = false;
			} else {
				map.put("index", 0);
				map.put("num", 99);
				List<PlatSpecialWhs> list = orderDealSettingsDao.selectPlatWhsSpecialListByMap(map);
				if(list.size() != 0) {
					message = "�������Ѵ���,�������޸�";
					isSuccess = false;
				} else {
					PlatSpecialWhs whs = orderDealSettingsDao.selectPlatWhsSpecialById(Integer.valueOf(stId));
					
					if(whs == null) {
						message = "����������¼������,��˶Բ���";
						isSuccess = false;
					} else {
						whs.setPlatCode(platEncd);
						whs.setWhsCode(whsEncd);
						whs.setInvtyCode(invtyEncd);
						orderDealSettingsDao.updatePlatWhsSpecialById(whs);
						message = "�޸ĳɹ�";
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
	 * �ַ���תlist
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
