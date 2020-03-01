package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.ProPlan;
import com.px.mis.ec.entity.ProPlans;
import com.px.mis.ec.service.ProPlanService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;


//促销方案
@RequestMapping(value = "ec/proPlan", method = RequestMethod.POST)
@Controller
public class ProPlanController {

    private Logger logger = LoggerFactory.getLogger(ProPlanController.class);

    @Autowired
    private ProPlanService proPlanService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    private String add(@RequestBody String jsonBody) {
        logger.info("url:ec/proPlan/add");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            ProPlan proPlan = BaseJson.getPOJO(jsonBody, ProPlan.class);
            ArrayNode proPlansArray = (ArrayNode) BaseJson.getReqBody(jsonBody).get("list");
            List<ProPlans> proPlansList = new ArrayList<>();
            for (Iterator<JsonNode> proPlansArrayIterator = proPlansArray.iterator(); proPlansArrayIterator
                    .hasNext();) {
                JsonNode proPlansNode = proPlansArrayIterator.next();
                ProPlans proPlans = JacksonUtil.getPOJO((ObjectNode) proPlansNode, ProPlans.class);
                proPlans.setProPlanId(proPlan.getProPlanId());
                proPlansList.add(proPlans);
            }
            resp = proPlanService.add(proPlan, proPlansList);
        } catch (Exception e) {
            logger.error("url:ec/proPlan/add 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    private String edit(@RequestBody String jsonBody) {
        logger.info("url:ec/proPlan/edit");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            ProPlan proPlan = BaseJson.getPOJO(jsonBody, ProPlan.class);
            ArrayNode proPlansArray = (ArrayNode) BaseJson.getReqBody(jsonBody).get("list");
            List<ProPlans> proPlansList = new ArrayList<>();
            if (proPlansArray.size() > 0) {
                for (Iterator<JsonNode> proPlansArrayIterator = proPlansArray.iterator(); proPlansArrayIterator
                        .hasNext();) {
                    JsonNode proPlansNode = proPlansArrayIterator.next();
                    ProPlans proPlans = JacksonUtil.getPOJO((ObjectNode) proPlansNode, ProPlans.class);
                    proPlans.setProPlanId(proPlan.getProPlanId());

                    proPlansList.add(proPlans);
                }
            }
            resp = proPlanService.edit(proPlan, proPlansList);
        } catch (Exception e) {
            logger.error("url:ec/proPlan/edit 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    private String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/proPlan/delete");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            resp = proPlanService.delete(BaseJson.getReqBody(jsonBody).get("proPlanId").asText());
        } catch (IOException e) {
            logger.error("url:ec/proPlan/delete 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) {
        logger.info("url:ec/proPlan/query");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            resp = proPlanService.query(BaseJson.getReqBody(jsonBody).get("proPlanId").asText());
        } catch (IOException e) {
            logger.error("url:ec/proPlan/query 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) {
        logger.info("url:ec/proPlan/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = proPlanService.queryList(map);
        } catch (IOException e) {
            logger.error("url:ec/proPlan/queryList 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

}
