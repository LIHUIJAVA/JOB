package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.AuditStrategyDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StrategyGoodsDao;
import com.px.mis.ec.entity.AuditStrategy;
import com.px.mis.ec.entity.BusinessType;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StrategyGoods;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//@Transactional
@Service
public class AuditStrategyServiceImpl implements AuditStrategyService {

	private Logger logger = LoggerFactory.getLogger(AuditStrategyServiceImpl.class);
	
	@Autowired
	private AuditStrategyDao auditStrategyDao;
	@Autowired
	private StrategyGoodsDao strategyGoodsDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private GoodRecordDao goodRecordDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Override
	@Transactional
	public String insert(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<StrategyGoods> strategyGoodslist=new ArrayList<>();
		AuditStrategy strategy=new AuditStrategy();
		try {
			//����������ļ�¼
			strategy = BaseJson.getPOJO(requestString, AuditStrategy.class);
			auditStrategyDao.insert(strategy);
			//�������id�����ӱ�
			ArrayNode strategyGoodsIterator = (ArrayNode) BaseJson.getReqBody(requestString).get("list");
			if (strategyGoodsIterator.size()>0) {
				for (Iterator<JsonNode> it = strategyGoodsIterator.iterator(); it.hasNext();) {
					JsonNode strategys = it.next();
					StrategyGoods strategyGoods = JacksonUtil.getPOJO((ObjectNode) strategys, StrategyGoods.class);
					strategyGoods.setStrategyId(strategy.getId());
					strategyGoodslist.add(strategyGoods);
				}
				strategyGoodsDao.insert(strategyGoodslist);
			}
			message="����ɹ�";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/addStrategy �쳣˵����ת��ʵ��ʱ�򱣴�ʱ�쳣",e);
			isSuccess=false;
			message="����ʧ�ܣ�������";
		}
		try {
			resp=BaseJson.returnRespObj("ec/auditStrategy/addStrategy", isSuccess, message, strategy);
			//resp = BaseJson.returnRespObj("ec/auditStrategy/addStrategy", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/addStrategy �쳣˵������װrespʱ�쳣",e);
		}
		return resp;
	}
	@Override
	@Transactional
	public String delete(String reString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		try {
			AuditStrategy strategy = BaseJson.getPOJO(reString, AuditStrategy.class);
			int used = auditStrategyDao.checkUsedByStore(strategy.getId());
			if(used==0) {
				auditStrategyDao.deletezi(strategy);
				auditStrategyDao.deletezhu(strategy);
				message="ɾ���ɹ�";
				isSuccess=true;
			}else {
				message="Ҫɾ��������������ڵ�����ʹ�ã�ɾ��ʧ��";
				isSuccess=false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/delete �쳣˵����ת��ʵ��ʱ�򱣴�ʱ�쳣",e);
			message="ɾ��ʧ��";
		}
		try {
			resp = BaseJson.returnRespObj("ec/auditStrategy/delete", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/delete �쳣˵������װrespʱ�쳣",e);
		}
		
		return resp;
	}
	@Override
	@Transactional
	public String update(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<StrategyGoods> strategyGoodslist=new ArrayList<>();
		try {
			AuditStrategy strategy = BaseJson.getPOJO(requestString, AuditStrategy.class);
			//��������
			auditStrategyDao.update(strategy);
			//ɾ���ӱ�
			auditStrategyDao.deletezi(strategy);
			//�����ӱ�
			ArrayNode strategyGoodsIterator = (ArrayNode) BaseJson.getReqBody(requestString).get("list");
			if (strategyGoodsIterator.size()>0) {
				for (Iterator<JsonNode> it = strategyGoodsIterator.iterator(); it.hasNext();) {
					JsonNode strategys = it.next();
					StrategyGoods strategyGoods = JacksonUtil.getPOJO((ObjectNode) strategys, StrategyGoods.class);
					strategyGoods.setStrategyId(strategy.getId());
					strategyGoodslist.add(strategyGoods);
				}
				if (strategyGoodslist.size()>0) {
					strategyGoodsDao.insert(strategyGoodslist);
				}
			}
			message="�޸ĳɹ�";
			isSuccess=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/delete �쳣˵����ת��ʵ��ʱ�򱣴�ʱ�쳣",e);
			message="�޸�ʧ��";
		}
		try {
			resp = BaseJson.returnRespObj("ec/auditStrategy/delete", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/delete �쳣˵������װrespʱ�쳣",e);
		}
		
		return resp;
	}
	@Override
	@Transactional
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<AuditStrategy> strategylist;
		int count;
		int pageNo;
		int pageSize;
		int listNum;
		int pages;
		try {
			count = auditStrategyDao.selectCount(map);
			strategylist = auditStrategyDao.selectList(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum = strategylist.size();
			pages = count / pageSize + 1;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			isSuccess=false;
			message="��ѯʧ�ܣ������ԡ�";
			count=0;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum=0;
			pages=0;
			strategylist=null;
		}
		try {
			resp = BaseJson.returnRespList("ec/auditStrategy/selectList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, strategylist);
		} catch (IOException e) {
			logger.error("URL:ec/auditStrategy/selectList �쳣˵����ƴ��resp�쳣", e);
		}
		return resp;
	}
	@Override
	@Transactional
	public String findById(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		AuditStrategy strategy=new AuditStrategy();
		try {
			strategy=auditStrategyDao.findById(Integer.parseInt(BaseJson.getReqBody(requestString).get("id").asText()));
			message="��ѯ�ɹ�";
			isSuccess=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message="��ѯʧ��";
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/findById �쳣˵������ȡ�������ѯʱʧ��", e);
		}
		try {
			resp=BaseJson.returnRespObj("ec/auditStrategy/findById", isSuccess, message, strategy);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/findById �쳣˵����ƴ��resp�쳣", e);
		}
		return resp;
	}
	/**
	 * ���ݴ��붩�������ӱ���Ϣ��
	 * ��ȡ���̵����е���˲���id,
	 * ��ȡ������ϸ�е�sku��spu�ڵ�����Ʒ�������ж��Ƿ�2������Ʒ
	 * ��ȡ��˲���id���жϴ˶����Ƿ������Ӧ���̵��������ã�
	 * ������������ʱ����true����������������ʱ����false
	 */
	@Override
	@Transactional
	public boolean autoAuditCheck(PlatOrder order, List<PlatOrders> orders) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < orders.size(); i++) {
			if(StringUtils.isEmpty(orders.get(i).getGoodSku())&&StringUtils.isEmpty(orders.get(i).getGoodId())) {
				
			}else {
				//��ѭ����ϸ�Ƿ��ж������۵�sku
				GoodRecord goodrecord = goodRecordDao.selectBySkuAndEcGoodId(orders.get(i).getGoodSku(), orders.get(i).getGoodId());
				if(goodrecord!=null) {
					if(goodrecord.getIsSecSale()==1) {
						order.setAuditHint("�������ж�������SKU���޷��Զ����");
						platOrderDao.update(order);//���¶����������ʾ��
						return false;
					}
				}else {
					order.setAuditHint("sku:"+orders.get(i).getGoodSku()+"��spu:"+orders.get(i).getGoodId()+"δƥ�䵽��Ӧ��Ʒ����,�޷��Զ����");
					platOrderDao.update(order);//���¶����������ʾ��
					return false;
				}
			}
		}
		StoreRecord storeRecord = storeRecordDao.select(order.getStoreId());
		AuditStrategy auditStrategy = auditStrategyDao.findById(Integer.parseInt(storeRecord.getNoAuditId()));
		//�ж���˲����Ƿ�����,��˷�ʽ(0.����1.����������2.����)
		if(auditStrategy.getAuditWay()==0) {
			return true;
		}else if(auditStrategy.getAuditWay()==1) {//����������
			boolean flag = true;
			if(auditStrategy.getBuyerNoteNull()==1) {//������Էǿ�
				if(order.getBuyerNote()!=null&&order.getBuyerNote().length()>0) {
					//���������
					order.setAuditHint("������������ԣ��޷��Զ����");
					platOrderDao.update(order);//���¶����������ʾ��
					return false;
				}
			}else if(auditStrategy.getBuyerNote()==1) {
				//������԰���
				if(order.getBuyerNote()!=null&&order.getBuyerNote().length()>0) {
					List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
					//������ʽ��0���������԰����ֶΣ�1��������԰����ֶΣ�2������sku
					for (int i = 0; i < strategyGoodsList.size(); i++) {
						if(strategyGoodsList.get(i).getVaType()==1) {
							if(order.getBuyerNote().indexOf(strategyGoodsList.get(i).getVa())>0) {
								flag=false;
								break;
							}
						}
					}
					//��flag=falseʱ������false;
					if(!flag) {
						order.setAuditHint("������԰��������õĹؼ��֣��޷��Զ����");
						platOrderDao.update(order);//���¶����������ʾ��
						return false;
					}
				}
			}
			//�������Էǿ�
			if(auditStrategy.getSellerNoteNull()==1) {
				//������԰����ؼ���
				if (StringUtils.isNotEmpty(order.getSellerNote())) {
					order.setAuditHint("�������������ԣ��޷��Զ����");
					platOrderDao.update(order);//���¶����������ʾ��
					return false;
				}
			}else if(auditStrategy.getSellerNote()==1) {
				//�������԰����ؼ���
				if(StringUtils.isNotEmpty(order.getSellerNote())) {
					List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
					//������ʽ��0���������԰����ֶΣ�1��������԰����ֶΣ�2������sku
					for (int i = 0; i < strategyGoodsList.size(); i++) {
						if(strategyGoodsList.get(i).getVaType()==0) {
							if(order.getBuyerNote().indexOf(strategyGoodsList.get(i).getVa())>0) {
								flag=false;
								break;
							}
						}
					}
					//��flag=falseʱ������false;
					if(!flag) {
						order.setAuditHint("�������԰��������õĹؼ��֣��޷��Զ����");
						platOrderDao.update(order);//���¶����������ʾ��
						return false;
					}
				}
			}
			if(auditStrategy.getIncludeSku()==1) {
				//������������sku
				List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
				//�Ƚ�������ϸ�е�sku��spuƴ�ӳ�String,�ö��Ÿ���
				String skuspus = "";
				for (int i = 0; i < orders.size(); i++) {
					skuspus+=orders.get(i).getGoodSku()+","+orders.get(i).getGoodId()+",";
				}
				//������ʽ��0���������԰����ֶΣ�1��������԰����ֶΣ�2������sku
				for (int i = 0; i < strategyGoodsList.size(); i++) {
					if(strategyGoodsList.get(i).getVaType()==2) {
						if(skuspus.indexOf(strategyGoodsList.get(i).getVa())>0) {
							flag=false;
							break;
						}
					}
				}
				//��flag=falseʱ������false;
				if(!flag) {
					order.setAuditHint("������������sku��spu���޷��Զ����");
					platOrderDao.update(order);//���¶����������ʾ��
					return false;
				}
			}
			return true;
		}else {
			//����
			return false;
		}
	}

}
