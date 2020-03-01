package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.service.LogisticsTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value = "ec/logisticsTab", method = RequestMethod.POST)
@Controller
public class LogisticsTabController {
    private Logger logger = LoggerFactory.getLogger(LogisticsTabController.class);
    @Autowired
    private LogisticsTabService logisticsTabService;

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    @ResponseBody
    private String insert(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/insert");
        logger.info("���������" + jsonBody);
        String resp = "";
        LogisticsTab logisticsTab = null;
        try {
            logisticsTab = BaseJson.getPOJO(jsonBody, LogisticsTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/insert �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/insert", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ������������ʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/insert �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.insert(logisticsTab);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/insert �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    private String update(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/update");
        logger.info("���������" + jsonBody);
        String resp = "";
        LogisticsTab logisticsTab = null;
        try {
            logisticsTab = BaseJson.getPOJO(jsonBody, LogisticsTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/update �쳣˵����", e1);
            try {

                resp = BaseJson.returnRespObj("ec/logisticsTab/insert", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ���������޸�ʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/update �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.update(logisticsTab);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/update �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    private String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/delete");
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = "";
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();

            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/delete �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/delete", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ��������ɾ��ʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/delete �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.delete(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/delete �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "select", method = RequestMethod.POST)
    @ResponseBody
    private String select(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/select");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        Integer ordrNum = null;
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/select �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/delete", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ������������ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/select �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.select(ordrNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/select �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "selectList", method = RequestMethod.POST)
    @ResponseBody
    private String selectList(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/selectList");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());


            //ȥ��map��valǰ��ո�
//            Map.Entry<String, Object> entry;
//            String val = null;
//            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
//            while (iterator.hasNext()) {
//                entry = iterator.next();
//                String key = entry.getKey();
//                System.out.println(key + "-----------");
//                val = entry.getValue().toString();
//                if (val != null && val.length() > 0) {
//                    val = entry.getValue().toString().trim();
//                    if (key.equals("pageNo")) {
//                        pageNo = (int) map.get("pageNo");
//                        continue;
//                    }
//                    if (key.equals("pageSize")) {
//                        pageSize = (int) map.get("pageSize");
//                        continue;
//                    }
//                    map.put(key, val);
//                }
//            }

            pageNo = (int) map.get("pageNo");
            pageSize = (int) map.get("pageSize");
            if (pageNo == 0 || pageSize == 0) {
                resp = BaseJson.returnRespList("/ec/logisticsTab/selectList", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/selectList �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("ec/logisticsTab/selectList", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ����ҳ��ѯʧ�ܡ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/selectList �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            String createDate1 = (String) map.get("createDate1");
            String createDate2 = (String) map.get("createDate2");
            if (StringUtils.isNotEmpty(createDate1)) {
                createDate1 = createDate1 + " 00:00:00";
                map.put("createDate1", createDate1);
            }
            if (StringUtils.isNotEmpty(createDate2)) {
                createDate2 = createDate2 + " 23:59:59";
                map.put("createDate2", createDate2);
            }

            String printTimeBegin = (String) map.get("printTimeBegin");
            String printTimeEnd = (String) map.get("printTimeEnd");
            if (StringUtils.isNotEmpty(printTimeBegin)) {
                printTimeBegin = printTimeBegin + " 00:00:00";
                map.put("printTimeBegin", printTimeBegin);
            }
            if (StringUtils.isNotEmpty(printTimeEnd)) {
                printTimeEnd = printTimeEnd + " 23:59:59";
                map.put("printTimeEnd", printTimeEnd);
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/logisticsTab/selectList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);
                resp = logisticsTabService.selectList(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/selectList �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //��ȡ�����浥
    @RequestMapping(value = "achieveECExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String achieveECExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveECExpressOrder");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressOrder �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressOrder", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveECExpressOrder �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveECExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressOrder �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }


    //��ȡ�����浥 ��ֱӪ�Ϳ�ݹ�˾
    @RequestMapping(value = "achieveJDWLOrder", method = RequestMethod.POST)
    @ResponseBody
    private String achieveJDWLOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveJDWLOrder");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveJDWLOrder �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveJDWLOrder", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveJDWLOrder �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveJDWLOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveJDWLOrder �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }


    //��ȡ�ƴ�ӡ�浥
    @RequestMapping(value = "achieveECExpressCloudPrint", method = RequestMethod.POST)
    @ResponseBody
    private String achieveECExpressCloudPrint(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveECExpressCloudPrint");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressCloudPrint �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressCloudPrint", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveECExpressCloudPrint �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveECExpressCloudPrint(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressCloudPrintachieveECExpressCloudPrint �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //ƽ̨��������
    @RequestMapping(value = "platOrderShip", method = RequestMethod.POST)
    @ResponseBody
    private String platOrderShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/platOrderShip");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.platOrderShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //ǿ�Ʒ���
    @RequestMapping(value = "forceShip", method = RequestMethod.POST)
    @ResponseBody
    private String forceShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/forceShip");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/forceShip �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/forceShip", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/forceShip �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.forceShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/forceShip �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //ȡ������
    @RequestMapping(value = "cancelShip", method = RequestMethod.POST)
    @ResponseBody
    private String cancelShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelShip");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelShip �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelShip", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelShip �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelShip �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //ȡ�������浥
    @RequestMapping(value = "cancelECExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String cancelECExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelECExpressOrder");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelECExpressOrder �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelECExpressOrder", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelECExpressOrder �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelECExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelECExpressOrder �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }


    //ȡ����ݵ���  ��ֱӪ��ݹ�˾ �������
    @RequestMapping(value = "cancelExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String cancelExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelExpressOrder");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelExpressOrder �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelExpressOrder", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelExpressOrder �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelExpressOrder �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }


    @RequestMapping(value = "selectPrint", method = RequestMethod.POST)
    @ResponseBody
    private String selectPrint(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/selectPrint");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/selectPrint �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/selectPrint", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ������������ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/selectPrint �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.selectPrint(ordrNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/selectPrint �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    
    @RequestMapping(value = "updatePick", method = RequestMethod.POST)
    @ResponseBody
    private String updatePick(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/updatePick");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String accNum ="";
        String type = "";
        String ordrNums = null;
        try {
            ordrNums = BaseJson.getReqBody(jsonBody).get("ordrNums").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            type = BaseJson.getReqBody(jsonBody).get("type").asText();
        } catch (Exception e1) {
            //e1.printStackTrace();
            logger.error("url:ec/logisticsTab/updatePick �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ������������ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/updatePick �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.updatePick(accNum, ordrNums, type);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/updatePick �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    

    //��ӡ��ݵ�
    @RequestMapping(value = "print", method = RequestMethod.POST)
    @ResponseBody
    private String print(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/print");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/print �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/print", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/print �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.print(ordrNum, accNum);
            //resp = logisticsTabService.platOrderShip(ordrNum,accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/print �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "exportList", method = RequestMethod.POST)
    @ResponseBody
    private String exportList(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/exportList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String createDate1 = (String) map.get("createDate1");
            String createDate2 = (String) map.get("createDate2");
            if (StringUtils.isNotEmpty(createDate1)) {
                createDate1 = createDate1 + " 00:00:00";
                map.put("createDate1", createDate1);
            }
            if (StringUtils.isNotEmpty(createDate2)) {
                createDate2 = createDate2 + " 23:59:59";
                map.put("createDate2", createDate2);
            }

            String printTimeBegin = (String) map.get("printTimeBegin");
            String printTimeEnd = (String) map.get("printTimeEnd");
            if (StringUtils.isNotEmpty(printTimeBegin)) {
                printTimeBegin = printTimeBegin + " 00:00:00";
                map.put("printTimeBegin", printTimeBegin);
            }
            if (StringUtils.isNotEmpty(printTimeEnd)) {
                printTimeEnd = printTimeEnd + " 23:59:59";
                map.put("printTimeEnd", printTimeEnd);
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/logisticsTab/exportList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);
                resp = logisticsTabService.exportList(map);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //�����޸�
    @RequestMapping(value = "batchUpdate", method = RequestMethod.POST)
    @ResponseBody
    private String batchUpdate(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/batchUpdate");
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = "";
        String accNum = "";
        String expressEncd = "";
        try {
            //Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();
            expressEncd = BaseJson.getReqBody(jsonBody).get("expressEncd").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = logisticsTabService.batchUpdate(accNum, ordrNum, expressEncd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //�޸������ݵ���
    @RequestMapping(value = "updateExpressCode", method = RequestMethod.POST)
    @ResponseBody
    private String updateExpressCode(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/updateExpressCode");
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = "";
        String accNum = "";
        String expressCode = "";
        try {
            //Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();
            expressCode = BaseJson.getReqBody(jsonBody).get("expressCode").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = logisticsTabService.updateExpressCode(ordrNum, expressCode);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    //���붩��
    @RequestMapping("importExpressCode")
    @ResponseBody
    public Object importExpressCode(HttpServletRequest request) throws IOException {
        try {
            //����һ��ͨ�õĶಿ�ֽ�����
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
            if (multipartResolver.isMultipart(request)) {
                //ת���ɶಿ��request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("ec/logisticsTab/importExpressCode", false, "��ѡ���ļ���", null);
                }
                String userId = request.getParameter("accNum");
                //System.out.println("=========================:"+userId);
                return logisticsTabService.importExpressCode(file, userId);
            } else {
                return BaseJson.returnRespObj("ec/logisticsTab/importExpressCode", false, "��ѡ���ļ���", null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("purc/logisticsTab/importExpressCode", false, e.getMessage(), null);

        }
    }

    //�޸������ݵ���
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    private String test(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/test");
        logger.info("���������" + jsonBody);
        String resp = "";
        String ordrNum = "";
        String accNum = "";
        String expressCode = "";
        try {

            logisticsTabService.loadExpressCode();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    
    //ƽ̨��������
    @RequestMapping(value = "ceshi", method = RequestMethod.POST)
    @ResponseBody
    private String ceshi(@RequestBody String jsonBody) {
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", false, "���������������:" + e1.getMessage() + ";������������Ƿ���ȷ", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e);
            }
            return resp;
        }
        try {
			/*
			 * for (int i = 0; i < 100; i++) { resp =
			 * logisticsTabService.platOrderShip(ordrNum, accNum); System.out.println(resp);
			 * System.out.println(i); }
			 */
        	logisticsTabService.platOrderShip(ordrNum, accNum);
            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip �쳣˵����", e);
        }
       
        return resp;
    }

}
