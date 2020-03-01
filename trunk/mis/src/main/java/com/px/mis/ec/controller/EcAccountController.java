package com.px.mis.ec.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
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

import com.px.mis.ec.service.AftermarketService;
import com.px.mis.ec.service.EcAccountService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//电商对账单
@RequestMapping(value = "ec/ecAcccount", method = RequestMethod.POST)
@Controller
public class EcAccountController {

    private Logger logger = LoggerFactory.getLogger(EcAccountController.class);

    @Autowired
    private EcAccountService ecAccountService;

    /**
     * 导入
     */
    @RequestMapping(value = "importAccount", method = RequestMethod.POST)
    @ResponseBody
    public Object importAccount(HttpServletRequest request) throws IOException {
        try {
            //创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                //转换成多部分request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "请选择文件！", null);
                }
                String userId = request.getParameter("accNum");
                String storeId = request.getParameter("storeId");
                return ecAccountService.importAccount(file, userId, storeId);
            } else {
                return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "请选择文件！", null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "导入异常：" + e.getMessage(), null);

        }
    }

    /**
     * 勾兑
     *
     * @param jsonBody ecOrderIds 用逗号分隔的平台订单号
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public String check(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/check");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            String ecOrderIds = BaseJson.getReqBody(jsonBody).get("ecOrderIds").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String startDate = BaseJson.getReqBody(jsonBody).get("startDate").asText();
            String endDate = BaseJson.getReqBody(jsonBody).get("endDate").asText();
            resp = ecAccountService.check(accNum, ecOrderIds, startDate + " 00:00:00", endDate + " 23:59:59");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/check", false, "勾兑异常：" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /**
     * 删除
     *
     * @param jsonBody billNos  逗号分隔的对账单id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/delete");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            String billNos = BaseJson.getReqBody(jsonBody).get("billNos").asText();
            resp = ecAccountService.delete(billNos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/delete", false, "删除异常：" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /**
     * 列表查询
     *
     * @param jsonBody
     * @return
     */
    @RequestMapping(value = "selectList", method = RequestMethod.POST)
    @ResponseBody
    public String selectList(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/selectList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            //去除map中val前后空格
//            Map.Entry<String, Object> entry;
//            String val = null;
//            int pageNo = 0;
//            int pageSize = 0;
//            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
//            while (iterator.hasNext()) {
//                entry = iterator.next();
//                String key = entry.getKey();
//                System.out.println(key + "-->"+entry.getValue());
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


            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);


            //费用开始时间
            String startTime1 = (String) map.get("startTime1");
            String startTime2 = (String) map.get("startTime2");
            if (StringUtils.isNotEmpty(startTime1)) {
                startTime1 = startTime1 + " 00:00:00";
                map.put("startTime1", startTime1);
            }
            if (StringUtils.isNotEmpty(startTime2)) {
                startTime2 = startTime2 + " 23:59:59";
                map.put("startTime2", startTime2);
            }
            //结算时间
            String jiesuanTime1 = (String) map.get("jiesuanTime1");
            String jiesuanTime2 = (String) map.get("jiesuanTime2");
            if (StringUtils.isNotEmpty(jiesuanTime1)) {
                jiesuanTime1 = jiesuanTime1 + " 00:00:00";
                map.put("jiesuanTime1", jiesuanTime1);
            }
            if (StringUtils.isNotEmpty(jiesuanTime2)) {
                jiesuanTime2 = jiesuanTime2 + " 23:59:59";
                map.put("jiesuanTime2", jiesuanTime2);
            }
            //勾兑时间
            String checkTime1 = (String) map.get("checkTime1");
            String checkTime2 = (String) map.get("checkTime2");
            if (StringUtils.isNotEmpty(checkTime1)) {
                checkTime1 = checkTime1 + " 00:00:00";
                map.put("checkTime1", checkTime1);
            }
            if (StringUtils.isNotEmpty(checkTime2)) {
                checkTime2 = checkTime2 + " 23:59:59";
                map.put("checkTime2", checkTime2);
            }

            resp = ecAccountService.selectList(map);
        } catch (IOException e) {
            try {
                resp = BaseJson.returnRespList("ec/ecAcccount/selectList", true, "查询异常，请重试！", 0, 1, 0, 0, 0,
                        null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            logger.error("URL:ec/ecAcccount/selectList 错误信息：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }


    /**
     * 下载
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ResponseBody
    public String download(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/download");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            String storeId = BaseJson.getReqBody(jsonBody).get("storeId").asText();
            String startDate = BaseJson.getReqBody(jsonBody).get("startDate").asText();
            String endDate = BaseJson.getReqBody(jsonBody).get("endDate").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = ecAccountService.download(accNum, storeId, startDate, endDate);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/download", false, "下载异常：" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /**
     * 自动勾兑
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "autoCheck", method = RequestMethod.POST)
    @ResponseBody
    public String autoCheck(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/autoCheck");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            //String startDate = BaseJson.getReqBody(jsonBody).get("startDate").asText();
            ///String endDate = BaseJson.getReqBody(jsonBody).get("endDate").asText();

            String checker = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String checkTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText();
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            resp = ecAccountService.autoCheck(checker, checkTime);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/autoCheck", false, "自动勾兑异常：" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /**
     * 勾兑页面查询
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "goToCheck", method = RequestMethod.POST)
    @ResponseBody
    public String goToCheck(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/goToCheck");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            //账单日期
            String startDate = (String) map.get("startDate");
            String endDate = (String) map.get("endDate");
            if (StringUtils.isNotEmpty(startDate)) {
                startDate = startDate + " 00:00:00";
                map.put("startDate", startDate);
            }
            if (StringUtils.isNotEmpty(endDate)) {
                endDate = endDate + " 23:59:59";
                map.put("endDate", endDate);
            }
            resp = ecAccountService.goToCheck(map);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/goToCheck", false, "勾兑查询异常：" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

}
