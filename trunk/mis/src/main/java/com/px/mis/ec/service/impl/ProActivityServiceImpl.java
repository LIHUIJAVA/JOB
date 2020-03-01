package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ProActivityDao;
import com.px.mis.ec.dao.ProActivitysDao;
import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.service.ProActivityService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ProActivityServiceImpl implements ProActivityService {

	private Logger logger = LoggerFactory.getLogger(ProActivityServiceImpl.class);

	@Autowired
	private ProActivityDao proActDao;

	@Autowired
	private ProActivitysDao proActsDao;

	@Override
	public String add(ProActivity proAct, List<ProActivitys> proActsList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (proActDao.select(proAct.getProActId()) != null) {
				message = "���" + proAct.getProActId() + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				if (proActsDao.select(proAct.getProActId()) != null) {
					proActsDao.delete(proAct.getProActId());
				}
				proActDao.insert(proAct);
				List<ProActivitys> proActsLists = new ArrayList<ProActivitys>();
				for (ProActivitys proActivitys : proActsList) {
					if (proActivitys.getAllGoods() == 1) {
						proActsLists.add(proActivitys);
					} else {
						String goodsRange = proActivitys.getGoodsRange();
						String[] split = goodsRange.split(",");// �μӻ����Ʒ��Χ����Ʒ�����ö��Ÿ������ַ�����
						for (String invtyEncd : split) {
							ProActivitys activitys = new ProActivitys();
							BeanUtils.copyProperties(proActivitys, activitys);
							System.err.println("1" + proActivitys);
							System.err.println("2" + activitys);
							activitys.setGoodsRange(invtyEncd); // ��Ʒ��Χ
							proActsLists.add(activitys);
						}
					}
				}
				proActsDao.insert(proActsLists);

				// ============zds ����Ʒ��м����������ݣ�
//				for(ProActivitys proActivitys:proActsList) {
//					Integer no = proActivitys.getNo();
//					Integer allGoods = proActivitys.getAllGoods();//�Ƿ�ȫ����Ʒ�μӻ��1���ǣ�0����
//					if(allGoods==1) {
//						GoodsActivityMiddle goodsActivityMiddle=new GoodsActivityMiddle();
//						goodsActivityMiddle.setAllGoods(1);//�Ƿ�ȫ����Ʒ
//						goodsActivityMiddle.setInvtyEncd("all");//������� д�ɹ̶��ģ���ʾ������ȫ������Ʒ���μӴ����
//						goodsActivityMiddle.setNo(proActivitys.getNo());//������ӱ����
//						goodsActivityMiddle.setStoreId(proAct.getTakeStore());//�μӻ�ĵ��̱���
//						goodsActivityMiddle.setStartTime(proAct.getStartDate());//���ʼ����
//						goodsActivityMiddle.setEndTime(proAct.getEndDate());//��������ڣ�
//						goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
//					}else if(allGoods==0){
//						String goodsRange = proActivitys.getGoodsRange();
//						String[] split = goodsRange.split(",");//�μӻ����Ʒ��Χ����Ʒ�����ö��Ÿ������ַ�����
//						for(String invtyEncd:split) {
//							GoodsActivityMiddle goodsActivityMiddle=new GoodsActivityMiddle();
//							goodsActivityMiddle.setAllGoods(0);//�Ƿ�ȫ����Ʒ
//							goodsActivityMiddle.setInvtyEncd(invtyEncd);//�������
//							goodsActivityMiddle.setNo(proActivitys.getNo());//������ӱ����
//							goodsActivityMiddle.setStoreId(proAct.getTakeStore());//�μӻ�ĵ��̱���
//							goodsActivityMiddle.setStartTime(proAct.getStartDate());//���ʼ����
//							goodsActivityMiddle.setEndTime(proAct.getEndDate());//��������ڣ�
//							goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
//						}
//					}
//				}
//				//====================zds===========================

				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/proActivity/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/add �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String edit(ProActivity proAct, List<ProActivitys> proActsList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ProActivity proActs = proActDao.select(proAct.getProActId());
			if (proActs == null || proActs.getAuditResult().equals("1")) {
				resp = BaseJson.returnRespObj("ec/proActivity/edit", false, "���������", null);
				return resp;
			}
			proActsDao.delete(proAct.getProActId());
			proActDao.update(proAct);
//            proActsDao.insert(proActsList);
			List<ProActivitys> proActsLists = new ArrayList<ProActivitys>();
			for (ProActivitys proActivitys : proActsList) {
				if (proActivitys.getAllGoods() == 1) {
					proActsLists.add(proActivitys);
				} else {
					String goodsRange = proActivitys.getGoodsRange();
					String[] split = goodsRange.split(",");// �μӻ����Ʒ��Χ����Ʒ�����ö��Ÿ������ַ�����
					for (String invtyEncd : split) {
						ProActivitys activitys = new ProActivitys();
						BeanUtils.copyProperties(proActivitys, activitys);
						activitys.setGoodsRange(invtyEncd); // ��Ʒ��Χ
						proActsLists.add(activitys);
					}
				}
			}
			proActsDao.insert(proActsLists);
			message = "���³ɹ���";
			resp = BaseJson.returnRespObj("ec/proActivity/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/edit �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String delete(String proActId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ProActivity proActs = proActDao.select(proActId);
			if (proActs == null || proActs.getAuditResult().equals("1")) {
				resp = BaseJson.returnRespObj("ec/proActivity/delete", false, "����������", null);
				return resp;
			}
			if (proActDao.select(proActId) != null) {
				proActDao.delete(proActId);
				isSuccess = true;
				message = "ɾ���ɹ���";
			} else {
				isSuccess = false;
				message = "���" + proActId + "����������ڣ�";
			}
			resp = BaseJson.returnRespObj("ec/proActivity/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String proActId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<ProActivitys> proActsList = new ArrayList<>();
			ProActivity proAct = proActDao.select(proActId);
			if (proAct != null) {
				proActsList = proActsDao.select(proActId);
				message = "��ѯ�ɹ���";
			} else {
				isSuccess = false;
				message = "���" + proActId + "����������ڣ�";
			}
			resp = BaseJson.returnRespObjList("ec/proActivity/query", isSuccess, message, proAct, proActsList);
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		try {
			List<ProActivity> proList = proActDao.selectList(map);
			int count = proActDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/proActivity/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/queryList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String updateAudit(List<ProActivity> activities) {
		String resp = "";
		try {
			String userName = null;
			String auditResult = null;
			String auditDate = null;
			for (ProActivity proActId : activities) {
				userName = proActId.getAuditor();
				auditDate = proActId.getAuditDate();
				auditResult = proActId.getAuditResult();
			}
			List<ProActivity> activities2 = new ArrayList<ProActivity>();
			for (ProActivity proActId : activities) {
				ProActivity proAct = proActDao.select(proActId.getProActId());
				if (proAct != null && proAct.getAuditResult().equals("0")) {
					proAct.setAuditDate(auditDate);
					proAct.setAuditor(userName);
					proAct.setAuditResult(auditResult);

					activities2.add(proAct);
				}
			}
			if (activities2.size() > 0) {
				int i = proActDao.updateAudit(activities2);
				if (i == 0) {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "���ʧ��", null);
				} else {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", true, "��˳ɹ���", null);
				}
			} else {
				resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "��������ˣ����ʧ��", null);
			}

		} catch (IOException e) {
			logger.error("URL��ec/proActivity/updateAuditResult �쳣˵����", e);
		}
		return resp;
		// TODO Auto-generated method stub
	}

	@Override
	public String updateAuditNo(List<ProActivity> activities) {
		String resp = "";
		try {
			String userName = null;
			String auditResult = null;
			String auditDate = null;
			for (ProActivity proActId : activities) {
				userName = proActId.getAuditor();
				auditDate = proActId.getAuditDate();
				auditResult = proActId.getAuditResult();
			}

			List<ProActivity> activities2 = new ArrayList<ProActivity>();
			for (ProActivity proActId : activities) {
				ProActivity proAct = proActDao.select(proActId.getProActId());
				if (proAct != null && proAct.getAuditResult().equals("1")) {
					proAct.setAuditDate(auditDate);
					proAct.setAuditor(userName);
					proAct.setAuditResult(auditResult);

					activities2.add(proAct);
				}
			}
			if (activities2.size() > 0) {
				int i = proActDao.updateAudit(activities);
				if (i == 0) {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResultNo", false, "����ʧ��", null);
				} else {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResultNo", true, "����ɹ���", null);
				}
			} else {
				resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "��������������ʧ��", null);
			}
		} catch (IOException e) {
			logger.error("URL��ec/proActivity/updateAuditResultNo �쳣˵����", e);
		}
		return resp;
	}

}
