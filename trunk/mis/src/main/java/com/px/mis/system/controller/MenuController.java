package com.px.mis.system.controller;

import java.io.IOException;
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

import com.px.mis.purc.entity.DeptDoc;
import com.px.mis.system.entity.Menu;
import com.px.mis.system.service.MenuService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value = "system/menu", method = RequestMethod.POST)
@Controller
public class MenuController {

	private Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:system/menu/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Menu menu = BaseJson.getPOJO(jsonBody, Menu.class);
			resp = menuService.add(menu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:system/menu/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Menu menu = BaseJson.getPOJO(jsonBody, Menu.class);
			resp = menuService.edit(menu);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	//批量修改菜单
	@RequestMapping("editList")
	public @ResponseBody Object updateDeptDocByDeptEncd(@RequestBody String jsonBody) throws Exception {
		
		String resp="";
		List<Map> mList=BaseJson.getList(jsonBody);
		List<Menu> cList=new ArrayList<>();
		for(Map map :mList) {
			Menu menu = new Menu();
			BeanUtils.populate(menu, map);
			cList.add(menu);
		}
		resp =menuService.editList(cList);
		return resp;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:system/role/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = menuService.delete(BaseJson.getReqBody(jsonBody).get("id").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:system/role/query");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = menuService.query(BaseJson.getReqBody(jsonBody).get("id").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:system/menu/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = menuService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "menuTree", method = RequestMethod.POST)
	@ResponseBody
	private String menuTree(@RequestBody String jsonBody) {
		logger.info("url:system/menu/menuTree");
		logger.info("请求参数：" + jsonBody);
		String resp = menuService.menuTree(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "editMenuTree", method = RequestMethod.POST)
	@ResponseBody
	private String editMenuTree(@RequestBody String jsonBody) {
		logger.info("url:system/menu/editMenuTree");
		logger.info("请求参数：" + jsonBody);
		String resp = menuService.editMenuTree(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "roleMenuList", method = RequestMethod.POST)
	@ResponseBody
	private String roleMenuList(@RequestBody String jsonBody) {
		logger.info("url:system/menu/roleMenuList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String roleId = BaseJson.getReqBody(jsonBody).get("roleId").asText();
			resp = menuService.roleMenuList(roleId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "userMenuList", method = RequestMethod.POST)
	@ResponseBody
	private String userMenuList(@RequestBody String jsonBody) {
		logger.info("url:system/menu/userMenuList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String userId = BaseJson.getReqBody(jsonBody).get("userId").asText();
			resp = menuService.userMenuList(userId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
