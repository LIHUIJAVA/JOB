package com.px.mis.account.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.dao.InvtySendMthTermBgnTabDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.service.impl.FormBookEntryUtil;
import com.px.mis.account.service.impl.FormBookServiceImpl;
import com.px.mis.ec.service.impl.PlatOrderMaiDu;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value="account/test",method=RequestMethod.POST)
@Controller
@CrossOrigin
public class AccTestController {
	
	@Autowired
	private FormBookServiceImpl formBookServiceImpl;
	@Autowired
	private FormBookEntryDao formBookEntryDao; //记账主表
	@Autowired
	private PlatOrderMaiDu platOrderMaiDu;
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; //发出商品各月期初
	@Autowired
	private SellSnglSubDao sellSnglSubDao; //销售子单
	@Autowired
	private SellComnInvDao sellComnInvDao; //发票
	@Autowired
	private FormBookEntryUtil formBookEntryUtil;
	
	@RequestMapping(value = "/normal/book")
	private @ResponseBody String testFormBook(@RequestBody String jsonBody) {
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();//当前用户
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //当前用户
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //登录时间
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);			
			map.put("isPage", "1");
			map.put("isNtBookOk", "0");
			map.put("loginTime", loginTime);
			List<FormBookEntry> dataList = formBookEntryDao.selectStreamALLList(map);
			resp = formBookServiceImpl.formBook(dataList, userName, loginTime);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
		return resp;
	}
}
