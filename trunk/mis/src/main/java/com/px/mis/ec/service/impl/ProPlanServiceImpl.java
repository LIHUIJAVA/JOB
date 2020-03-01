package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ProActivityDao;
import com.px.mis.ec.dao.ProPlanDao;
import com.px.mis.ec.dao.ProPlansDao;
import com.px.mis.ec.entity.ProPlan;
import com.px.mis.ec.entity.ProPlans;
import com.px.mis.ec.service.ProPlanService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ProPlanServiceImpl implements ProPlanService {

    private Logger logger = LoggerFactory.getLogger(ProPlanServiceImpl.class);

    @Autowired
    private ProPlanDao proPlanDao;

    @Autowired
    private ProPlansDao proPlansDao;

    @Autowired
    private ProActivityDao proActDao;
    @Override
    public String add(ProPlan proPlan, List<ProPlans> proPlansList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            if (proPlansList.size() == 0) {
                message = "�������ϸ����������Ϣ��";
                isSuccess = false;
            } else if (proPlanDao.select(proPlan.getProPlanId()) != null) {
                message = "���" + proPlan.getProPlanId() + "�Ѵ��ڣ����������룡";
                isSuccess = false;
            } else {
                if (proPlansDao.select(proPlan.getProPlanId()) != null) {
                    proPlansDao.delete(proPlan.getProPlanId());
                }
                proPlanDao.insert(proPlan);
                proPlansDao.insert(proPlansList);
                message = "�����ɹ���";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObj("ec/proPlan/add", isSuccess, message, null);
        } catch (IOException e) {
            logger.error("URL��ec/proPlan/add �쳣˵����", e);
            throw new RuntimeException();
        }
        return resp;
    }

    @Override
    public String edit(ProPlan proPlan, List<ProPlans> proPlansList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<String> list = proActDao.selectpro(null,proPlan.getProPlanId(),null);
            for (String str : list) {
                if (str == null || str.equals("1")) {
                    isSuccess= false;
                    message="����ʧ�ܣ����������ˣ�";
                    return   resp = BaseJson.returnRespObj("ec/proPlan/edit", isSuccess, message, null);

                }
            }
            if (proPlansList.size() == 0) {
                message = "�������ϸ����������Ϣ��";
                isSuccess = false;
            } else {
                proPlansDao.delete(proPlan.getProPlanId());
                proPlanDao.update(proPlan);
                proPlansDao.insert(proPlansList);

                message = "���³ɹ���";
            }
            resp = BaseJson.returnRespObj("ec/proPlan/edit", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL��ec/proPlan/edit �쳣˵����", e);
            throw new RuntimeException(e);
        }
        return resp;
    }

    @Override
    public String delete(String proPlanId) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String[] proPlanIds = proPlanId.split(",");
            List<String> proPlanIdList = Arrays.asList(proPlanIds);
            for (String proPlan : proPlanIdList) {
                List<String> list = proActDao.selectpro(null, proPlan, null);
                for (String str : list) {
                    if (str == null || str.equals("1")) {
                        isSuccess = false;
                        message = "ɾ��ʧ�ܣ���Ӧ���������ˣ�";
                        return resp = BaseJson.returnRespObj("ec/proPlan/delete", isSuccess, message, null);

                    }
                }
            }

            proPlanDao.delete(proPlanIdList);
//			if (proPlanDao.select(proPlanId) != null) {
//				proPlanDao.delete(proPlanId);
            isSuccess = true;
            message = "ɾ���ɹ���";
//			} else {
//				isSuccess = false;
//				message = "���" + proPlanId + "�����ڣ�";
//			}
            resp = BaseJson.returnRespObj("ec/proPlan/delete", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL��ec/proPlan/delete �쳣˵����", e);
            isSuccess = false;
            message = "ɾ��ʧ��";
            try {
				resp = BaseJson.returnRespObj("ec/proPlan/delete", isSuccess, message, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        return resp;
    }

    @Override
    public String query(String proPlanId) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<ProPlans> proPlansList = new ArrayList<>();
            ProPlan proPlan = proPlanDao.select(proPlanId);
            if (proPlan != null) {
                proPlansList = proPlansDao.select(proPlanId);
                message = "��ѯ�ɹ���";
            } else {
                isSuccess = false;
                message = "���" + proPlanId + "�����ڣ�";
            }
            resp = BaseJson.returnRespObjList("ec/proPlan/query", isSuccess, message, proPlan, proPlansList);
        } catch (IOException e) {
            logger.error("URL��ec/proPlan/query �쳣˵����", e);
        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";
        try {
            List<ProPlan> proList = proPlanDao.selectList(map);
            int count = proPlanDao.selectCount(map);
            int pageNo = Integer.parseInt(map.get("pageNo").toString());
            int pageSize = Integer.parseInt(map.get("pageSize").toString());
            resp = BaseJson.returnRespList("ec/proPlan/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
                    proList);
        } catch (IOException e) {
            logger.error("URL��ec/proPlan/queryList �쳣˵����", e);
        }
        return resp;
    }

}
