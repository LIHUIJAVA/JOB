package com.px.mis.whs.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.MergePickSngl;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.PickSnglTab;
import com.px.mis.whs.service.PickSnglService;

//拣货单的单据
@Controller
@RequestMapping("/whs/pick_sngl")
public class PickSnglController {

    private static final Logger logger = LoggerFactory.getLogger(PickSnglController.class);
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    PickSnglService pickSnglService;
    @Autowired
    WhsDocMapper whsDocMapper;
    //	记账
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserServiceImpl misUserService;

    // 查询销售单
    @RequestMapping(value = "selectSellById", method = RequestMethod.POST)
    @ResponseBody
    private String selectSellById(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pick_sngl/selectSellById");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

//			reqBody = BaseJson.getReqBody(jsonBody);
//			String sellSnglId = reqBody.get("sellSnglId").asText();
//			resp = pickSnglService.selectSellById(sellSnglId, userId);

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (!BaseJson.getReqBody(jsonBody).has("whsEncd")) {
                throw new RuntimeException("请输入仓库");
            }
            if(!misUserService.isWhsPer(jsonBody,BaseJson.getReqBody(jsonBody).get("whsEncd").asText())){
                throw new RuntimeException("用户没有仓库权限");
            }
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            resp = pickSnglService.selectSellById(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/pick_sngl/selectSellById", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新加
    @RequestMapping(value = "insertSellPick", method = RequestMethod.POST)
    @ResponseBody
    private String insertSellPick(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pick_sngl/insertSellPick");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();

            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));

            reqBody = BaseJson.getReqBody(jsonBody);
            String sellSnglId = reqBody.get("sellSnglId").asText();

            resp = pickSnglService.insertPickSngl(sellSnglId, userId, userName, CommonUtil.getLoginTime(loginTime));
            misLogDAO.insertSelective(new MisLog("新增拣货单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增拣货单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/insertSellPick", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查询 拣货单主列表
    @RequestMapping(value = "queryAllList", method = RequestMethod.POST)
    @ResponseBody
    private String queryAllList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/queryAllList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText("");
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/pick_sngl/queryAllList", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);


                resp = pickSnglService.queryAllList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/pick_sngl/queryAllList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 通过主表拣货单据号查询子表信息
    @RequestMapping(value = "selectPSubTabById", method = RequestMethod.POST)
    @ResponseBody
    private String selectPSubTabById(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/selectPSubTabById");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String pickSnglNum = reqBody.get("pickSnglNum").asText();
            resp = pickSnglService.selectPSubTabById(pickSnglNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/pick_sngl/selectPSubTabById", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查询 拣货单主-子列表
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = pickSnglService.queryList(map);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/pick_sngl/queryList", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 查询 打印
    @RequestMapping(value = "queryListPrint", method = RequestMethod.POST)
    @ResponseBody
    private String queryListPrint(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/queryListPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            resp = pickSnglService.queryListPrint(map);
            misLogDAO.insertSelective(new MisLog("导出拣货单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出拣货单", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pick_sngl/queryListPrint", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 合并拣货单
    @RequestMapping(value = "mergePickSingl", method = RequestMethod.POST)
    @ResponseBody
    private String selectPSubTab(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pick_sngl/mergePickSingl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode aString = BaseJson.getReqHead(jsonBody);
            String userId = aString.get("accNum").asText();

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String pickSnglNum = reqBody.get("pickSnglNum").asText();
            String whsEncd = reqBody.get("whsEncd").asText();

            resp = pickSnglService.selectPSubTab(userId, pickSnglNum, whsEncd);
            misLogDAO.insertSelective(new MisLog("合并拣货单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("合并拣货单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除合并拣货单
    @RequestMapping(value = "deleteMergeSngl", method = RequestMethod.POST)
    @ResponseBody
    private String deleteMergeSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/deleteMergeSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            List<Map> mergeMap = BaseJson.getList(jsonBody);
            List<MergePickSngl> mSnglList = new ArrayList<>();
            for (Map map : mergeMap) {
                MergePickSngl mPickSngl = new MergePickSngl();
                BeanUtils.populate(mPickSngl, map);
                mSnglList.add(mPickSngl);
            }

            resp = pickSnglService.deleteMerPickSngl(mSnglList);
            misLogDAO.insertSelective(new MisLog("删除合并拣货单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除合并拣货单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/deleteMergeSngl", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // PDA 显示所有拣货单列表-
    @RequestMapping(value = "selectAllMerge", method = RequestMethod.POST)
    @ResponseBody
    private String selectAllMerge(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/selectAllMerge");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = pickSnglService.selectAllMerge(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pick_sngl/selectAllMerge", false, "用户无业务仓库", null);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/pick_sngl/selectAllMerge", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // PDA 回传接口 拣货完成标识 拣货完成时间
    @RequestMapping(value = "updatePTabPda", method = RequestMethod.POST)
    @ResponseBody
    private String updatePTabPda(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pick_sngl/updatePTabPda");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }

            List<MovBitTab> list = new ArrayList<MovBitTab>();
            PickSnglTab pickSnglTab = BaseJson.getPOJO(jsonBody, PickSnglTab.class);
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
//            JSONArray array = JSON.parseArray(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("list"));
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
//                JSON.parseObject(str.toString(), MovBitTab.class);
//				oSubTab.setOrderNum(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("formNum"));
//				oSubTab.setSerialNum(str.getString("ordrNum"));
                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {

                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);
                    oSubTabs.setQty(new BigDecimal(qtys[j]));
                    oSubTabs.setGdsBitEncd(strs[j]);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), oSubTabs.getGdsBitEncd());
                    if (regnEncd == null) {
                        logger.info("请检查货位的区域是否存在");
                        throw new RuntimeException("请检查仓库货位对应关系是否存在");
                    }
                    oSubTabs.setRegnEncd(regnEncd);

                    list.add(oSubTabs);
                }
            }

//			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
//			String operator = reqBody.get("operator").asText();
//			String operatorId = reqBody.get("operatorId").asText();
//			String pickNum = reqBody.get("pickNum").asText();
//			String pickSnglNum = reqBody.get("pickSnglNum").asText();
//
//			PickSnglTab pSnglTab = new PickSnglTab();// 拣货单
//			pSnglTab.setPickSnglNum(pickSnglNum);
//			pSnglTab.setPickPers(operator);
//			pSnglTab.setPickPersId(operatorId);
//			MergePickSngl mergePickSngl = new MergePickSngl();// 合并拣货单
//			mergePickSngl.setPickNum(pickNum);
//			mergePickSngl.setOperator(operator);
//			mergePickSngl.setOperatorId(operatorId);
//			mergePickSngl.setPickSnglNum(pickSnglNum);

            resp = pickSnglService.updatePTabPda(pickSnglTab, list);
            misLogDAO.insertSelective(new MisLog("PDA 回传接口拣货", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA 回传接口拣货", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/updatePTabPda", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新增拣货单(不要了 和查询销售单合并了)
//	@RequestMapping(value="insertPickSngl",method = RequestMethod.POST)
//	@ResponseBody
//	public Object insertPickSngl(@RequestBody String jsonBody) {
//		// System.out.println(jsonBody);
//		logger.info("url:whs/pick_sngl/insertPickSngl");
//		logger.info("请求参数："+jsonBody);
//		String resp="";
//		
//		try {
//			//主表
//			PickSnglTab pTab=BaseJson.getPOJO(jsonBody, PickSnglTab.class);
//		
//			//子表
//			List<Map> cTabMap=BaseJson.getList(jsonBody);
//			List<PickSnglSubTab> pList=new ArrayList();
//			for (Map map : cTabMap) {
//				PickSnglSubTab pSubTab=new PickSnglSubTab();
//				pSubTab.setPickSnglNum(pTab.getPickSnglNum());;//将主表主键提取插入子表
//				BeanUtils.populate(pSubTab, map);
//				pList.add(pSubTab);
//			}
//			
//			resp=pickSnglService.insertPickSngl(pTab, pList);
//			logger.info("返回参数："+resp);
//			
//		} catch (Exception e) {
//			
//		}
//		return resp;
//	}

    // 删除拣货单
    @RequestMapping(value = "deletePickSngl", method = RequestMethod.POST)
    @ResponseBody
    private String deletePickSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pick_sngl/deletePickSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String sellSnglId = reqBody.get("pickSnglId").asText();

            resp = pickSnglService.deletePickSngl(sellSnglId);
            misLogDAO.insertSelective(new MisLog("删除拣货单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除拣货单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/deletePickSngl", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 拣货完成标识 拣货完成时间
    @RequestMapping(value = "updateTabPC", method = RequestMethod.POST)
    @ResponseBody
    private String updateTabPC(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pick_sngl/updateTabPC");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
//				String operator = reqBody.get("operator").asText();//拣货人

            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();

//				String pickNum = reqBody.get("pickNum").asText();//拣货单号
            String pickSnglNum = reqBody.get("pickSnglNum").asText();// 拣货单号

            PickSnglTab pSnglTab = new PickSnglTab();// 拣货单
            pSnglTab.setPickSnglNum(pickSnglNum);
            pSnglTab.setPickPers(userName);
            pSnglTab.setPickPersId(userId);
            resp = pickSnglService.updatePTabPC(pSnglTab);
            misLogDAO.insertSelective(new MisLog("拣货单拣货完成", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("拣货单拣货完成", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/pick_sngl/updateTabPC", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    private String realWhsString(String whsEncd) {
        // TODO Auto-generated method stub
        List<String> aList = whsDocMapper.selectRealWhsList(getList(whsEncd));
        String s = "";
        for (int i = 0; i < aList.size(); i++) {
            if (i == aList.size() - 1) {
                s += aList.get(i);
            } else {
                s += aList.get(i) + ",";
            }
        }
        if (s.equals("")) {
            s = null;
        }
        return s;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
     */
    public List<String> getList(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }
}
