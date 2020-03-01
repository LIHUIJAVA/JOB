package com.px.mis.ec.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.px.mis.ec.entity.PlatSpecialWhs;
import com.px.mis.ec.entity.PlatWhs;
import com.px.mis.ec.service.OrderDealSettingsService;
import com.px.mis.ec.service.PlatWhsMappService;
import com.px.mis.ec.util.Json;
import com.px.mis.util.GetOrderNo;
@RequestMapping(value = "test", method = RequestMethod.POST)
@Controller
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private GetOrderNo gon;
	
	
	@RequestMapping(value = "test2", method = RequestMethod.POST)
	@ResponseBody
	public String test2() {
		String id = gon.getSeqNo("ec", "abc");
		
		//logger.info(id);
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+id);
		return id;
	}
	
	@Autowired
	private PlatWhsMappService platWhsMappService;
	
	@RequestMapping(value = "test3", method = RequestMethod.POST)
	@ResponseBody
	public String test3() {
		Map map=new HashMap();
		map.put("type", "CN");
		map.put("online", "jcx201");
		String id = platWhsMappService.select(map);
		
		//logger.info(id);
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+id);
		return id;
	}
	@Autowired
	private OrderDealSettingsService orderService;
	
//	@RequestMapping(value = "test4", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> test4(String orderId,String platId){
//		Map<String,Object> maps = orderService.matchWareHouse(orderId, platId);
//		
//		//logger.info(id);
//		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//		return maps;
//	}
	
	
	
	@RequestMapping(value = "test5", method = RequestMethod.POST)
	@ResponseBody
	public String test5(){
		
		/*System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		List<PlatSpecialWhs> list = Json.jsonToArrayList(str,PlatSpecialWhs.class);
		for(PlatSpecialWhs plat :list) {
			System.out.println("我是第一层for:"+plat.getWhsCode());
			
			for(PlatWhs whs : plat.getPlats()) {
				System.out.println("我是第二层for:"+whs.getCityCode());
			}
		}
		return list.get(0).getPlats().get(0).getWhsCode();*/
		
		/*PlatSpecialWhs plat2 = Json.jsonToBean(str, PlatSpecialWhs.class);
		System.out.println("主platSpecialWhs:"+plat2.getWhsCode());
		System.out.println("子platWhs:"+plat2.getPlats().get(0).getCityCode());*/
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "11111");
		
		System.out.println(Json.mapToJson(map));
		return "true";
		
		
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		List<String> strthree =new ArrayList<>();
		strthree.add("三级一");
		strthree.add("三级二");
		strthree.add("三级三");
		List<String> strUser =new ArrayList<>();
		strUser.add("用户级一");
		strUser.add("用户级二");
		strUser.add("三级三");
		List<String> noList =new ArrayList<>();
		for (String string : strthree) {
			boolean flag= false;
			for (String str : strUser) {
				if (string.equals(str)) {
					flag = true;
				}
			}
			if (!flag) {
				noList.add(string);
			}
		}
			System.out.println(noList);
	}
}
