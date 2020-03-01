package com.px.mis.ec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maidu.api.entity.custom.XS_OrdersGetListResponse;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.service.OrderDownloadService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.ec.service.impl.PlatOrderMaiDu;
import com.px.mis.ec.thread.Task;
import com.px.mis.ec.thread.WorkThread;
import com.px.mis.ec.util.SendUtil;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//ƽ̨����
@RequestMapping(value = "ec/platOrder", method = RequestMethod.POST)
@Controller
public class PlatOrderController {

    private Logger logger = LoggerFactory.getLogger(PlatOrderController.class);

    //ƽ̨ÿҳ���ض�������
    private static final int downloadPageSize = 40;
    //����ʹ�û���  �߳������Ϊ100  ȫ�ֿ���
    /**
     * ����1����С���ʱ��
     * ����2�������ʱ��
     * ����3������ʱ��
     * ����4������ʱ�䵥λ
     * ����5�����ز���
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(5, 100,30L,TimeUnit.SECONDS,new LinkedBlockingQueue<>(500));
    //��������ʹ��
//    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    private PlatOrderService platOrderService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    private String add(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/add");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            PlatOrder platOrder = BaseJson.getPOJO(jsonBody, PlatOrder.class);
            platOrder.setIsAudit(0);//�� Ĭ��Ϊ0��δ���

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            ArrayNode platOrdersArray = (ArrayNode) reqBody.get("list");
            List<PlatOrders> paltOrdersList = new ArrayList<>();
            for (Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext(); ) {
                JsonNode platOrdersNode = platOrdersArrayIterator.next();
                PlatOrders platOrders = JacksonUtil.getPOJO((ObjectNode) platOrdersNode, PlatOrders.class);
                paltOrdersList.add(platOrders);
            }
            resp = platOrderService.add(userId, platOrder, paltOrdersList);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/add ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    private String edit(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/edit");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            PlatOrder platOrder = BaseJson.getPOJO(jsonBody, PlatOrder.class);
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            ArrayNode platOrdersArray = (ArrayNode) reqBody.get("list");
            List<PlatOrders> paltOrdersList = new ArrayList<>();
            for (Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext(); ) {
                JsonNode platOrdersNode = platOrdersArrayIterator.next();
                PlatOrders platOrders = JacksonUtil.getPOJO((ObjectNode) platOrdersNode, PlatOrders.class);
                paltOrdersList.add(platOrders);
            }
            resp = platOrderService.edit(platOrder, paltOrdersList, userId);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/edit ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "batchEditWhs", method = RequestMethod.POST)
    @ResponseBody
    private String batchEditWhs(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/batchEditWhs");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderIds = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String whsEncd = BaseJson.getReqBody(jsonBody).get("whsEncd").asText();
            resp = platOrderService.batchEditWhs(orderIds, userId, whsEncd);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/batchEditWhs ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "batchEditExpress", method = RequestMethod.POST)
    @ResponseBody
    private String batchEditExpress(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/batchEditExpress");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderIds = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String expressCode = BaseJson.getReqBody(jsonBody).get("expressCode").asText();
            resp = platOrderService.batchEditExpress(orderIds, userId, expressCode);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/batchEditExpress ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    private String delete(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/delete");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            resp = platOrderService.delete(BaseJson.getReqBody(jsonBody).get("orderId").asText(), BaseJson.getReqHead(jsonBody).get("accNum").asText());
        } catch (IOException e) {
            logger.error("URL:ec/platOrder/delete ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/query");
        logger.info("����http��" + jsonBody);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            resp = platOrderService.query(BaseJson.getReqBody(jsonBody).get("orderId").asText());
        } catch (IOException e) {
            logger.error("URL:ec/platOrder/query ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());


            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

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

            resp = platOrderService.queryList(map);
        } catch (IOException e) {
            logger.error("URL:ec/platOrder/queryList ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @Autowired
    private OrderDownloadService orderDownloadService;

    @RequestMapping(value = "download", method = RequestMethod.POST)
    @ResponseBody
    private String download(@RequestBody String jsonBody) {
        String url = "ec/platOrder/download";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String startDate = map.get("startDate").toString();
            String endDate = map.get("endDate").toString();
            int pageNo = 1;
            int pageSize = downloadPageSize;
            String storeId = map.get("storeId").toString();
            //ԭ����
            //resp = platOrderService.download(userId, startDate, endDate, pageNo, pageSize, storeId);

            //��ѯ���ݿ� �Ƿ����ڲ���
            Boolean bs = orderDownloadService.isOperation(storeId);

            //true ���߳�
            if (!bs) {
                //��ʾ���˲���
                resp = BaseJson.returnRespObj("ec/platOrder/download", true, "���������ڲ�����ǰ����", null);
            } else {
                //������ڲ�������
                orderDownloadService.addState(storeId);

                //���̳߳� ִ��
//				executorService.execute(
//  					()-> platOrderService.download(userId, startDate, endDate, pageNo, pageSize, storeId)
//						new WorkThread(new Task(userId, startDate, endDate, pageNo, pageSize, storeId, platOrderService, orderDownloadService))
//				);

                Task task = new Task(userId, startDate, endDate, pageNo, pageSize, storeId, platOrderService, orderDownloadService);
                WorkThread workThread = new WorkThread(task);
                executorService.execute(workThread);

                //������ɸ�����ʾ-----���޸�
                resp = BaseJson.returnRespObj("ec/platOrder/download", true, "������ɣ����Ժ�鿴", null);
            }


            logger.info("���ز�����" + resp);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                resp = BaseJson.returnRespObj("ec/platOrder/download", false, "�����������ʱ�쳣", null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
                logger.error(url);
                logger.error("ƴ�ӷ���ֵʱ����" + resp);
            }
        }
        return resp;
    }

    @RequestMapping(value = "downloadByOrderId", method = RequestMethod.POST)
    @ResponseBody
    private String downloadByOrderId(@RequestBody String jsonBody) {
        String url = "ec/platOrder/downloadByOrderId";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        resp = platOrderService.downloadByOrderId(jsonBody);
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "unionQuery", method = RequestMethod.POST)
    @ResponseBody
    private String unionQuery(@RequestBody String jsonBody) {
        String url = "ec/platOrder/unionQuery";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            resp = platOrderService.unionQuery(BaseJson.getReqBody(jsonBody).get("orderId").asText());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "exportPlatOrder", method = RequestMethod.POST)
    @ResponseBody
    private String exportPlatOrder(@RequestBody String jsonBody) {
        String url = "ec/platOrder/exportPlatOrder";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
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
            resp = platOrderService.exportPlatOrder(map);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "autoMatch", method = RequestMethod.POST)
    @ResponseBody
    private String autoMatch(@RequestBody String jsonBody) {
        String url = "ec/platOrder/autoMatch";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderId = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = platOrderService.autoMatch(orderId, accNum);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "splitOrder", method = RequestMethod.POST)
    @ResponseBody
    private String splitOrder(@RequestBody String jsonBody) {
        String url = "ec/platOrder/splitOrder";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String platOrdersIds = BaseJson.getReqBody(jsonBody).get("platOrdersIds").asText();
            String splitNum = BaseJson.getReqBody(jsonBody).get("splitNum").asText();
            resp = platOrderService.splitOrder(orderId, platOrdersIds, splitNum, accNum);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    
    
    @RequestMapping(value = "closeOrder", method = RequestMethod.POST)
    @ResponseBody
    private String closeOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/closeOrder");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderIds = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = platOrderService.closeOrder(orderIds, userId);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/closeOrder ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    
    @RequestMapping(value = "openOrder", method = RequestMethod.POST)
    @ResponseBody
    private String openOrder(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/openOrder");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderIds = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            resp = platOrderService.openOrder(orderIds, userId);
        } catch (Exception e) {
            logger.error("URL:ec/platOrder/openOrder ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
    
    

    //���붩��
    @RequestMapping("importPlatOrder")
    @ResponseBody
    public Object importPlatOrder(HttpServletRequest request) throws IOException {
        try {
            //����һ��ͨ�õĶಿ�ֽ�����
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
            if (multipartResolver.isMultipart(request)) {
                //ת���ɶಿ��request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("ec/platOrder/importPlatOrder", false, "��ѡ���ļ���", null);
                }
                String userId = request.getParameter("accNum");
                //System.out.println("=========================:"+userId);
                return platOrderService.importPlatOrder(file, userId);
            } else {
                return BaseJson.returnRespObj("ec/platOrder/importPlatOrder", false, "��ѡ���ļ���", null);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("ec/platOrder/importPlatOrder", false, e.getMessage(), null);

        }
    }

    @RequestMapping(value = "near15DaysOrder", method = RequestMethod.POST)
    @ResponseBody
    private String near15DaysOrder(@RequestBody String jsonBody) {
        String url = "ec/platOrder/near15DaysOrder";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        resp = platOrderService.Near15DaysOrder();
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "batchSelect", method = RequestMethod.POST)
    @ResponseBody
    private String batchSelect(@RequestBody String jsonBody) {
        String url = "ec/platOrder/batchSelect";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            //String orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
            String isAudit = BaseJson.getReqBody(jsonBody).get("isAudit").asText();
            String ecOrderIds = BaseJson.getReqBody(jsonBody).get("ecOrderIds").asText();
            List<String> list = Arrays.asList(ecOrderIds.split(","));
            resp = platOrderService.batchSelect(list, isAudit);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * ������ϸ�б��ѯ
     *
     * @param jsonBody
     * @return
     */
    @RequestMapping(value = "orderssssList", method = RequestMethod.POST)
    @ResponseBody
    private String orderssssList(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/orderssssList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            //ȥ���ո�
//            Map.Entry<String, Object> entry;
//            String val = null;
//            int pageNo = 0;
//            int pageSize = 0;
//            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
//            while (iterator.hasNext()) {
//                entry = iterator.next();
//                String key = entry.getKey();
//                System.out.println(key + "-----------");
//                val = entry.getValue().toString();
//                if(val != null && val.length() > 0){
//                    val = entry.getValue().toString().trim();
//                    if (key.equals("pageNo")){
//                        pageNo = (int) map.get("pageNo");
//                        continue;
//                    }
//                    if (key.equals("pageSize")){
//                        pageSize = (int) map.get("pageSize");
//                        continue;
//                    }
//                    map.put(key,val);
//                }
//            }

            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

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

            resp = platOrderService.orderssssList(map);
        } catch (IOException e) {
            logger.error("URL:ec/platOrder/orderssssList ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * ������ϸ�б���
     *
     * @param jsonBody
     * @return
     */
    @RequestMapping(value = "exportOrderssssList", method = RequestMethod.POST)
    @ResponseBody
    private String exportOrderssssList(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/exportOrderssssList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
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

            resp = platOrderService.exportOrderssssList(map);
        } catch (IOException e) {
            logger.error("URL:ec/platOrder/exportOrderssssList ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "batchList", method = RequestMethod.POST)
    @ResponseBody
    private String batchList(@RequestBody String jsonBody) {
        String url = "ec/platOrder/batchList";
        logger.info("url:" + url);
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            //String orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
            //String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String ecOrderIds = BaseJson.getReqBody(jsonBody).get("ecOrderIds").asText();
            List<String> list = Arrays.asList(ecOrderIds.split(","));
            resp = platOrderService.batchList(list);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "selectecGooId", method = RequestMethod.POST)
    @ResponseBody
    private String selectecGooId(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/selectecGooId");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderId = BaseJson.getReqBody(jsonBody).get("orderId").asText();
            List<String> list = Arrays.asList(orderId.split(","));
            resp = platOrderService.selectecGooId(list);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "updateecGooId", method = RequestMethod.POST)
    @ResponseBody
    private String updateecGooId(@RequestBody String jsonBody) {
        logger.info("url:ec/platOrder/updateecGooId");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderIds = BaseJson.getReqBody(jsonBody).get("orderIds").asText();
            String invId = BaseJson.getReqBody(jsonBody).get("invId").asText();
            String invIdLast = BaseJson.getReqBody(jsonBody).get("invIdLast").asText();
            String multiple = BaseJson.getReqBody(jsonBody).get("multiple").asText();
            String[] orderIdsss = orderIds.split(",");
            for (int i = 0; i < orderIdsss.length; i++) {

                //���ݶ�����Ų�ѯplat_order //������
                List<PlatOrders> platOrders = platOrderService.selectecByorderIds(orderIdsss[i]);
                PlatOrder platOrderList = platOrderService.selectecByorderId(orderIdsss[i]);

                resp = platOrderService.updateGooId(platOrderList, invId, invIdLast, multiple, platOrders);
            }

        } catch (Exception e) {
            logger.error("URL:ec/platOrder/updateecGooId ������Ϣ��", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }
   
    @RequestMapping(value = "ceshi", method = RequestMethod.POST)
    @ResponseBody
    private String ceshi(@RequestBody String jsonBody) {
        String resp = "";
        try {
            for (int i = 0; i < 100; i++) {

            	/*Map<String, Object> params = new HashMap<>(); // ���ȷ����ش�����
            	params.put("pageNo", 1); // ���ȶ���ID
            	params.put("pageSize", 10); // ���ȶ���ID
    			params.put("storeId", "01"); // ���ȶ���ID
*/    			PlatOrderMaiDu platOrderMaiDu = new PlatOrderMaiDu();
    			//platOrderMaiDu.postMaiDuOrder(storeSettings, pageNo, pageSize, orderQueryList, orderCode, startDate, endDate, orderState)
    			//platOrderMaiDu.maiDuDownload("", 1, 10, "2019-01-02 00:00:00", "2019-12-31 00:00:00", "35", "");
    			
    			String appKey = "1056493";
    			String appSecret = "ef4553b7b1b64a8e85870f999f9cb5de";
    			String sign = platOrderMaiDu.getSign(appKey, appSecret); // ��������sign
    			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			// ���ͻ�ȡ��������
    			Map<String, Object> params = new LinkedHashMap<>();
    			params.put("AppKey", appKey);
    			params.put("TimeStamp", sf.format(new Date()));
    			params.put("Sign", sign); // ǩ��
    			params.put("ModuleKeyLogo", "000");
    			params.put("ActionKeyLogo", "000");
    			String orderCode = "376433129347820";
    			if (orderCode != null) {
    				params.put("OrderCode", orderCode);
    			} else {
    				params.put("OrderCode", ""); // 9569434542 //927057517117410
    			}
    			params.put("OrderStutas", ""); // 1������ 2������ 3�ѷ���4 �����5 ��ȡ��6 �˿���7 �ѹر�8 ������20 ����ɹ�
    			params.put("BusiId", "10000");
    			params.put("CusCode", "");
    			params.put("ReviewStatus", "");
    			String startDate = "2019-01-01 00:00:00";
    			String endDate = "2020-02-11 00:00:00";
    			if (StringUtils.isNotEmpty(startDate)) {

    				JSONArray array = new JSONArray();
    				array.add(startDate);
    				array.add(endDate);
    				params.put("OrderDates", array);
    				// params.put("OrderDates", "[\"" + startDate + "\",\"" + endDate + "\"]");
    			} else {
    				params.put("OrderDates", "");
    			}

    			params.put("OrderPayTimes", "");
    			params.put("UpdateTimes", "");
    			params.put("OutTimes", "");
    			params.put("IsSettlement", "");
    			params.put("IsJoinAuth", "");
    			params.put("IsJoin", "");
    			params.put("IsJoinSucess", "");
    			params.put("PrmCode", "");
    			params.put("PGId", "");
    			params.put("Title", "");
    			params.put("SkuProperties", "");
    			params.put("PageNo", 1);
    			params.put("PageSize", 10);
    			params.put("ExecutorId", "1");

    			/*
    			 * String sss = JSON.toJSONString(params);
    			 * System.out.println(JSON.toJSONString(params));
    			 * System.out.println(StringEscapeUtils.unescapeJava(JSON.toJSONString(params)))
    			 * ; int aa= 0; if(aa==0) { return orderQueryList; }
    			 */
    			//XS_OrdersGetListResponse result = JSON.parseObject(
    					//SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/GetList", JSON.toJSONString(params)),
    					//XS_OrdersGetListResponse.class);
    			String fff = SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/GetList", JSON.toJSONString(params));
				/*
				 * XS_OrdersGetListResponse result = JSON.parseObject(
				 * SendUtil.sendPost("http://meisu-mch.love-bears.cn/api/XS_Orders/GetList",
				 * JSON.toJSONString(params)), XS_OrdersGetListResponse.class);
				 */
    			
    			
    			
    			//http://meisu-mch.love-bears.cn
				/*
				 * String fff =
				 * SendUtil.sendPost("http://47.100.168.100:8088/mis/ec/storeRecord/queryList",
				 * jsonBody);
				 */
            	
            	
            	System.out.println(fff);
            	System.out.println(i);
            }

        } catch (Exception e) {
            logger.error("URL:ec/platOrder/updateecGooId ������Ϣ��", e);
        }
        return resp;
    }
}
