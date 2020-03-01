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

//���̶��˵�
@RequestMapping(value = "ec/ecAcccount", method = RequestMethod.POST)
@Controller
public class EcAccountController {

    private Logger logger = LoggerFactory.getLogger(EcAccountController.class);

    @Autowired
    private EcAccountService ecAccountService;

    /**
     * ����
     */
    @RequestMapping(value = "importAccount", method = RequestMethod.POST)
    @ResponseBody
    public Object importAccount(HttpServletRequest request) throws IOException {
        try {
            //����һ��ͨ�õĶಿ�ֽ�����
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
            if (multipartResolver.isMultipart(request)) {
                //ת���ɶಿ��request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "��ѡ���ļ���", null);
                }
                String userId = request.getParameter("accNum");
                String storeId = request.getParameter("storeId");
                return ecAccountService.importAccount(file, userId, storeId);
            } else {
                return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "��ѡ���ļ���", null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("ec/ecAcccount/importAccount", false, "�����쳣��" + e.getMessage(), null);

        }
    }

    /**
     * ����
     *
     * @param jsonBody ecOrderIds �ö��ŷָ���ƽ̨������
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public String check(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/check");
        logger.info("���������" + jsonBody);
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
                return BaseJson.returnRespObj("ec/ecAcccount/check", false, "�����쳣��" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * ɾ��
     *
     * @param jsonBody billNos  ���ŷָ��Ķ��˵�id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/delete");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String billNos = BaseJson.getReqBody(jsonBody).get("billNos").asText();
            resp = ecAccountService.delete(billNos);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                return BaseJson.returnRespObj("ec/ecAcccount/delete", false, "ɾ���쳣��" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * �б��ѯ
     *
     * @param jsonBody
     * @return
     */
    @RequestMapping(value = "selectList", method = RequestMethod.POST)
    @ResponseBody
    public String selectList(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/selectList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            //ȥ��map��valǰ��ո�
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


            //���ÿ�ʼʱ��
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
            //����ʱ��
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
            //����ʱ��
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
                resp = BaseJson.returnRespList("ec/ecAcccount/selectList", true, "��ѯ�쳣�������ԣ�", 0, 1, 0, 0, 0,
                        null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            logger.error("URL:ec/ecAcccount/selectList ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }


    /**
     * ����
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ResponseBody
    public String download(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/download");
        logger.info("���������" + jsonBody);
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
                return BaseJson.returnRespObj("ec/ecAcccount/download", false, "�����쳣��" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * �Զ�����
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "autoCheck", method = RequestMethod.POST)
    @ResponseBody
    public String autoCheck(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/autoCheck");
        logger.info("���������" + jsonBody);
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
                return BaseJson.returnRespObj("ec/ecAcccount/autoCheck", false, "�Զ������쳣��" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * ����ҳ���ѯ
     *
     * @param jsonBody
     * @param
     * @return
     */
    @RequestMapping(value = "goToCheck", method = RequestMethod.POST)
    @ResponseBody
    public String goToCheck(@RequestBody String jsonBody) {
        logger.info("url:ec/ecAcccount/goToCheck");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            //�˵�����
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
                return BaseJson.returnRespObj("ec/ecAcccount/goToCheck", false, "���Ҳ�ѯ�쳣��" + e.getMessage(), null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

}
