package com.px.mis.ec.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.PresentRange;
import com.px.mis.ec.entity.PresentRangeList;
import com.px.mis.ec.service.PresentRangeListService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//赠品范围列表接口
@Controller
@RequestMapping(value = "ec/presentRangeList", method = RequestMethod.POST)
public class PresentRangeListController {
    private Logger logger = LoggerFactory.getLogger(PresentRangeListController.class);
    @Autowired
    private PresentRangeListService presentRangeListService;

    /* 添加 */
    @RequestMapping("insertPresentRangeList")
    public @ResponseBody Object insertPresentRangeList(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/insertPresentRangeList");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        PresentRange presentRange = null;
        List<PresentRangeList> cList = new ArrayList<>();
        try {
            presentRange = BaseJson.getPOJO(jsonData, PresentRange.class);
            // 子表
            List<Map> cTabMap = BaseJson.getList(jsonData);
            for (Map map : cTabMap) {
                PresentRangeList presentRangeList = new PresentRangeList();
                BeanUtils.populate(presentRangeList, map);
                presentRangeList.setPresentRangeEncd(presentRange.getPresentRangeEncd());
                cList.add(presentRangeList);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/insertPresentRangeList 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/insertPresentRangeList 异常说明：", e);
            }
            return resp;
        }
        try {
            on = presentRangeListService.insertPresentRange(presentRange,cList);
            if (on == null) {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList", false, "添加异常了", null);
            } else {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/insertPresentRangeList 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除 */
    @RequestMapping("deletePresentRangeList")
	public @ResponseBody Object deletePresentRangeList(@RequestBody String jsonData) {
		logger.info("url:/ec/presentRangeList/deletePresentRangeList");
		logger.info("请求参数：" + jsonData);
		ObjectNode on = null;
		String resp = "";
		String presentRangeEncd = null;
		boolean isSuccess = true;
		String message = "";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			presentRangeEncd = reqBody.get("presentRangeEncd").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentRangeList/deletePresentRangeList 异常说明：", e1);
			try {
				resp = BaseJson.returnRespObj("/ec/presentRangeList/deletePresentRangeList", false,
						"请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentRangeList/deletePresentRangeList 异常说明：", e);
			}
			return resp;
		}
		try {

			String[] str = presentRangeEncd.split(",");
			for (int i = 0; i < str.length; i++) {
				on = presentRangeListService.deletePresentRangeListById(str[i]);
				message = message + on.get("message").asText() + "\n";
				if (!on.get("isSuccess").asBoolean()) {
					isSuccess = on.get("isSuccess").asBoolean();
				}
			}

			if (on != null) {
				resp = BaseJson.returnRespObj("/ec/presentRangeList/deletePresentRangeList", isSuccess, message, null);
			} else {
				resp = BaseJson.returnRespObj("/ec/presentRangeList/deletePresentRangeList", false, "delete error!",
						null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentRangeList/deletePresentRangeList 异常说明：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

    /* 更新 */
    @RequestMapping("updatePresentRangeList")
    public @ResponseBody Object updatePresentRangeList(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/updatePresentRangeList");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        PresentRange presentRange = null;
        List<PresentRangeList> cList = new ArrayList<>();

        try {
            presentRange = BaseJson.getPOJO(jsonData, PresentRange.class);
            // 子表
            List<Map> cTabMap = BaseJson.getList(jsonData);
            for (Map map : cTabMap) {
                PresentRangeList presentRangeList = new PresentRangeList();
                BeanUtils.populate(presentRangeList, map);
                presentRangeList.setPresentRangeEncd(presentRange.getPresentRangeEncd());
                cList.add(presentRangeList);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/updatePresentRangeList 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/updatePresentRangeList", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/updatePresentRangeList 异常说明：", e);
            }
            return resp;
        }
        try {
            on = presentRangeListService.updatePresentRangeListById(presentRange,cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/updatePresentRangeList",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/updatePresentRangeList", false, "update error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/updatePresentRangeList 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有 */
    @RequestMapping(value = "selectPresentRangeList")
    public @ResponseBody Object selectPresentRangeList(@RequestBody String jsonBody) {
        logger.info("url:/ec/presentRangeList/selectPresentRangeList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            pageNo = (int) map.get("pageNo");
            pageSize = (int) map.get("pageSize");
            if (pageNo == 0 || pageSize == 0) {
                resp = BaseJson.returnRespList("/ec/presentRangeList/selectPresentRangeList", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeList 异常说明：", e1);
            try {
                resp = BaseJson.returnRespList("/ec/presentRangeList/selectPresentRangeList", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/selectPresentRangeList 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = presentRangeListService.selectPresentRangeListList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeList 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个 */
    @RequestMapping(value = "selectPresentRangeListById")
    public @ResponseBody Object selectPresentRangeListById(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/selectPresentRangeListById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        String presentRangeEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            presentRangeEncd = reqBody.get("presentRangeEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeListById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/selectPresentRangeListById 异常说明：", e);
            }
            return resp;
        }
        try {
             resp = presentRangeListService.selectPresentRangeListById(presentRangeEncd);
//            if (presentRangeList != null) {
//                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", true, "处理成功！",
//                        presentRangeList);
//            } else {
//                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", false, "没有查到数据！",
//                        presentRangeList);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeListById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }
}
