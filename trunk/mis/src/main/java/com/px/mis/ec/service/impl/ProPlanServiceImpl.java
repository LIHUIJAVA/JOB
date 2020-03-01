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
                message = "请添加详细促销方案信息！";
                isSuccess = false;
            } else if (proPlanDao.select(proPlan.getProPlanId()) != null) {
                message = "编号" + proPlan.getProPlanId() + "已存在，请重新输入！";
                isSuccess = false;
            } else {
                if (proPlansDao.select(proPlan.getProPlanId()) != null) {
                    proPlansDao.delete(proPlan.getProPlanId());
                }
                proPlanDao.insert(proPlan);
                proPlansDao.insert(proPlansList);
                message = "新增成功！";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObj("ec/proPlan/add", isSuccess, message, null);
        } catch (IOException e) {
            logger.error("URL：ec/proPlan/add 异常说明：", e);
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
                    message="更新失败，促销活动已审核！";
                    return   resp = BaseJson.returnRespObj("ec/proPlan/edit", isSuccess, message, null);

                }
            }
            if (proPlansList.size() == 0) {
                message = "请添加详细促销方案信息！";
                isSuccess = false;
            } else {
                proPlansDao.delete(proPlan.getProPlanId());
                proPlanDao.update(proPlan);
                proPlansDao.insert(proPlansList);

                message = "更新成功！";
            }
            resp = BaseJson.returnRespObj("ec/proPlan/edit", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL：ec/proPlan/edit 异常说明：", e);
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
                        message = "删除失败，对应促销活动已审核！";
                        return resp = BaseJson.returnRespObj("ec/proPlan/delete", isSuccess, message, null);

                    }
                }
            }

            proPlanDao.delete(proPlanIdList);
//			if (proPlanDao.select(proPlanId) != null) {
//				proPlanDao.delete(proPlanId);
            isSuccess = true;
            message = "删除成功！";
//			} else {
//				isSuccess = false;
//				message = "编号" + proPlanId + "不存在！";
//			}
            resp = BaseJson.returnRespObj("ec/proPlan/delete", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL：ec/proPlan/delete 异常说明：", e);
            isSuccess = false;
            message = "删除失败";
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
                message = "查询成功！";
            } else {
                isSuccess = false;
                message = "编号" + proPlanId + "不存在！";
            }
            resp = BaseJson.returnRespObjList("ec/proPlan/query", isSuccess, message, proPlan, proPlansList);
        } catch (IOException e) {
            logger.error("URL：ec/proPlan/query 异常说明：", e);
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
            resp = BaseJson.returnRespList("ec/proPlan/queryList", true, "查询成功！", count, pageNo, pageSize, 0, 0,
                    proList);
        } catch (IOException e) {
            logger.error("URL：ec/proPlan/queryList 异常说明：", e);
        }
        return resp;
    }

}
