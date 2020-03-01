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

//��Ʒ��Χ�б�ӿ�
@Controller
@RequestMapping(value = "ec/presentRangeList", method = RequestMethod.POST)
public class PresentRangeListController {
    private Logger logger = LoggerFactory.getLogger(PresentRangeListController.class);
    @Autowired
    private PresentRangeListService presentRangeListService;

    /* ��� */
    @RequestMapping("insertPresentRangeList")
    public @ResponseBody Object insertPresentRangeList(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/insertPresentRangeList");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        PresentRange presentRange = null;
        List<PresentRangeList> cList = new ArrayList<>();
        try {
            presentRange = BaseJson.getPOJO(jsonData, PresentRange.class);
            // �ӱ�
            List<Map> cTabMap = BaseJson.getList(jsonData);
            for (Map map : cTabMap) {
                PresentRangeList presentRangeList = new PresentRangeList();
                BeanUtils.populate(presentRangeList, map);
                presentRangeList.setPresentRangeEncd(presentRange.getPresentRangeEncd());
                cList.add(presentRangeList);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/insertPresentRangeList �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList", false,
                        "���������������������������Ƿ���д��ȷ������쳣��", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/insertPresentRangeList �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = presentRangeListService.insertPresentRange(presentRange,cList);
            if (on == null) {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList", false, "����쳣��", null);
            } else {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/insertPresentRangeList",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/insertPresentRangeList �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�� */
    @RequestMapping("deletePresentRangeList")
	public @ResponseBody Object deletePresentRangeList(@RequestBody String jsonData) {
		logger.info("url:/ec/presentRangeList/deletePresentRangeList");
		logger.info("���������" + jsonData);
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
			logger.error("url:/ec/presentRangeList/deletePresentRangeList �쳣˵����", e1);
			try {
				resp = BaseJson.returnRespObj("/ec/presentRangeList/deletePresentRangeList", false,
						"���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentRangeList/deletePresentRangeList �쳣˵����", e);
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
			logger.error("url:/ec/presentRangeList/deletePresentRangeList �쳣˵����", e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}

    /* ���� */
    @RequestMapping("updatePresentRangeList")
    public @ResponseBody Object updatePresentRangeList(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/updatePresentRangeList");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        PresentRange presentRange = null;
        List<PresentRangeList> cList = new ArrayList<>();

        try {
            presentRange = BaseJson.getPOJO(jsonData, PresentRange.class);
            // �ӱ�
            List<Map> cTabMap = BaseJson.getList(jsonData);
            for (Map map : cTabMap) {
                PresentRangeList presentRangeList = new PresentRangeList();
                BeanUtils.populate(presentRangeList, map);
                presentRangeList.setPresentRangeEncd(presentRange.getPresentRangeEncd());
                cList.add(presentRangeList);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/updatePresentRangeList �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/updatePresentRangeList", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/updatePresentRangeList �쳣˵����", e);
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
            logger.error("url:/ec/presentRangeList/updatePresentRangeList �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectPresentRangeList")
    public @ResponseBody Object selectPresentRangeList(@RequestBody String jsonBody) {
        logger.info("url:/ec/presentRangeList/selectPresentRangeList");
        logger.info("���������" + jsonBody);
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
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeList �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespList("/ec/presentRangeList/selectPresentRangeList", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/selectPresentRangeList �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = presentRangeListService.selectPresentRangeListList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeList �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectPresentRangeListById")
    public @ResponseBody Object selectPresentRangeListById(@RequestBody String jsonData) {
        logger.info("url:/ec/presentRangeList/selectPresentRangeListById");
        logger.info("���������" + jsonData);
        String resp = "";
        String presentRangeEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            presentRangeEncd = reqBody.get("presentRangeEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeListById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", false,
                        "���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/ec/presentRangeList/selectPresentRangeListById �쳣˵����", e);
            }
            return resp;
        }
        try {
             resp = presentRangeListService.selectPresentRangeListById(presentRangeEncd);
//            if (presentRangeList != null) {
//                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", true, "����ɹ���",
//                        presentRangeList);
//            } else {
//                resp = BaseJson.returnRespObj("/ec/presentRangeList/selectPresentRangeListById", false, "û�в鵽���ݣ�",
//                        presentRangeList);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/ec/presentRangeList/selectPresentRangeListById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
}
