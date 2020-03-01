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
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        LogisticsTab logisticsTab = null;
        try {
            logisticsTab = BaseJson.getPOJO(jsonBody, LogisticsTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/insert 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/insert", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表新增失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/insert 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.insert(logisticsTab);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/insert 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    private String update(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/update");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        LogisticsTab logisticsTab = null;
        try {
            logisticsTab = BaseJson.getPOJO(jsonBody, LogisticsTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/update 异常说明：", e1);
            try {

                resp = BaseJson.returnRespObj("ec/logisticsTab/insert", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表修改失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/update 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.update(logisticsTab);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/update 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    private String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/delete");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = "";
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();

            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/delete 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/delete", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表删除失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/delete 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.delete(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/delete 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "select", method = RequestMethod.POST)
    @ResponseBody
    private String select(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/select");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Integer ordrNum = null;
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/select 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/delete", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表单个查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/select 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.select(ordrNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/select 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "selectList", method = RequestMethod.POST)
    @ResponseBody
    private String selectList(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/selectList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());


            //去除map中val前后空格
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
                resp = BaseJson.returnRespList("/ec/logisticsTab/selectList", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/selectList 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("ec/logisticsTab/selectList", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，分页查询失败。", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/selectList 异常说明：", e);
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
                resp = BaseJson.returnRespObj("whs/logisticsTab/selectList", false, "用户没有仓库权限", null);
            } else {
                map.put("whsId", whsId);
                resp = logisticsTabService.selectList(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/selectList 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //获取电子面单
    @RequestMapping(value = "achieveECExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String achieveECExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveECExpressOrder");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressOrder 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressOrder", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveECExpressOrder 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveECExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressOrder 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }


    //获取电子面单 限直营型快递公司
    @RequestMapping(value = "achieveJDWLOrder", method = RequestMethod.POST)
    @ResponseBody
    private String achieveJDWLOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveJDWLOrder");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveJDWLOrder 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveJDWLOrder", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveJDWLOrder 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveJDWLOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveJDWLOrder 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }


    //获取云打印面单
    @RequestMapping(value = "achieveECExpressCloudPrint", method = RequestMethod.POST)
    @ResponseBody
    private String achieveECExpressCloudPrint(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/achieveECExpressCloudPrint");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressCloudPrint 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressCloudPrint", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/achieveECExpressCloudPrint 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.achieveECExpressCloudPrint(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/achieveECExpressCloudPrintachieveECExpressCloudPrint 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //平台订单发货
    @RequestMapping(value = "platOrderShip", method = RequestMethod.POST)
    @ResponseBody
    private String platOrderShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/platOrderShip");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.platOrderShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //强制发货
    @RequestMapping(value = "forceShip", method = RequestMethod.POST)
    @ResponseBody
    private String forceShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/forceShip");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/forceShip 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/forceShip", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/forceShip 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.forceShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/forceShip 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //取消发货
    @RequestMapping(value = "cancelShip", method = RequestMethod.POST)
    @ResponseBody
    private String cancelShip(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelShip");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelShip 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelShip", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelShip 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelShip(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelShip 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //取消电子面单
    @RequestMapping(value = "cancelECExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String cancelECExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelECExpressOrder");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelECExpressOrder 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelECExpressOrder", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelECExpressOrder 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelECExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelECExpressOrder 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }


    //取消快递单号  限直营快递公司 京东快递
    @RequestMapping(value = "cancelExpressOrder", method = RequestMethod.POST)
    @ResponseBody
    private String cancelExpressOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/cancelExpressOrder");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelExpressOrder 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/cancelExpressOrder", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/cancelExpressOrder 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.cancelExpressOrder(ordrNum, accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/cancelExpressOrder 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }


    @RequestMapping(value = "selectPrint", method = RequestMethod.POST)
    @ResponseBody
    private String selectPrint(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/selectPrint");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/selectPrint 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/selectPrint", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表单个查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/selectPrint 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.selectPrint(ordrNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/selectPrint 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }
    
    @RequestMapping(value = "updatePick", method = RequestMethod.POST)
    @ResponseBody
    private String updatePick(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/updatePick");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
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
            logger.error("url:ec/logisticsTab/updatePick 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确，物流表单个查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/updatePick 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.updatePick(accNum, ordrNums, type);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/updatePick 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }
    

    //打印快递单
    @RequestMapping(value = "print", method = RequestMethod.POST)
    @ResponseBody
    private String print(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/print");
        logger.info("请求http：" + jsonBody);
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        String ordrNum = null;
        String accNum = "";
        try {
            ordrNum = BaseJson.getReqBody(jsonBody).get("logisticsNum").asText();
            accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:ec/logisticsTab/print 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/print", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/print 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = logisticsTabService.print(ordrNum, accNum);
            //resp = logisticsTabService.platOrderShip(ordrNum,accNum);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:ec/logisticsTab/print 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "exportList", method = RequestMethod.POST)
    @ResponseBody
    private String exportList(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/exportList");
        logger.info("请求参数：" + jsonBody);
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
                resp = BaseJson.returnRespObj("whs/logisticsTab/exportList", false, "用户没有仓库权限", null);
            } else {
                map.put("whsId", whsId);
                resp = logisticsTabService.exportList(map);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    //批量修改
    @RequestMapping(value = "batchUpdate", method = RequestMethod.POST)
    @ResponseBody
    private String batchUpdate(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/batchUpdate");
        logger.info("请求参数：" + jsonBody);
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
        logger.info("返回参数：" + resp);
        return resp;
    }

    //修改填入快递单号
    @RequestMapping(value = "updateExpressCode", method = RequestMethod.POST)
    @ResponseBody
    private String updateExpressCode(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/updateExpressCode");
        logger.info("请求参数：" + jsonBody);
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
        logger.info("返回参数：" + resp);
        return resp;
    }

    //导入订单
    @RequestMapping("importExpressCode")
    @ResponseBody
    public Object importExpressCode(HttpServletRequest request) throws IOException {
        try {
            //创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                //转换成多部分request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("ec/logisticsTab/importExpressCode", false, "请选择文件！", null);
                }
                String userId = request.getParameter("accNum");
                //System.out.println("=========================:"+userId);
                return logisticsTabService.importExpressCode(file, userId);
            } else {
                return BaseJson.returnRespObj("ec/logisticsTab/importExpressCode", false, "请选择文件！", null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("purc/logisticsTab/importExpressCode", false, e.getMessage(), null);

        }
    }

    //修改填入快递单号
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    private String test(@RequestBody String jsonBody) {
        logger.info("url:ec/logisticsTab/test");
        logger.info("请求参数：" + jsonBody);
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
        logger.info("返回参数：" + resp);
        return resp;
    }
    
    //平台订单发货
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
            logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", false, "请求参数解析错误:" + e1.getMessage() + ";请检查请求参数是否正确", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e);
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
            logger.error("url:ec/logisticsTab/platOrderShip 异常说明：", e);
        }
       
        return resp;
    }

}
