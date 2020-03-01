package com.px.mis.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.stax2.ri.typed.ValueDecoderFactory.IntArrayDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.dao.MenuDao;
import com.px.mis.system.dao.RoleMenuDao;
import com.px.mis.system.dao.UserMenuDao;
import com.px.mis.system.entity.Menu;
import com.px.mis.system.service.MenuService;
import com.px.mis.system.util.EditMenuTree;
import com.px.mis.system.util.MenuTree;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@Transactional
@Service
public class MenuServiceImpl implements MenuService {

	private Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuDao menuDao;

	@Autowired
	private RoleMenuDao roleMenuDao;

	@Autowired
	private UserMenuDao userMenuDao;

	@Override
	public String add(Menu menu) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (menuDao.select(menu.getId()) != null) {
			message = "���" + menu.getId() + "�Ѵ��ڣ����������룡";
			isSuccess = false;
		} else {
			if (menu.getLevel() == 3) {
				menu.setTargetType("iframe-tab");
			}
			menuDao.insert(menu);
			message = "�����ɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("system/menu/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��system/menu/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(Menu menu) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (menuDao.select(menu.getId()) == null) {
			message = "�޸�ʧ�ܣ����" + menu.getId() + "�����ڣ�";
			isSuccess = false;
		} else {
			if (menu.getLevel() == 3) {
				menu.setTargetType("iframe-tab");
			}
			menuDao.update(menu);
			message = "�޸ĳɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("system/menu/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��system/menu/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String editList(List<Menu> menu) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		for (Menu menus : menu) {
			if (menuDao.select(menus.getId()) == null) {
				message = "�޸�ʧ�ܣ����" + menus.getId() + "�����ڣ�";
			} else {
				if (menus.getLevel() == 3) {
					menus.setTargetType("iframe-tab");
				}
				menuDao.update(menus);
				message = "�˵��޸ĳɹ���";
				isSuccess = true;
			}
		}
		try {
			resp = BaseJson.returnRespObj("system/menu/editList", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String delete(String id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		if (id.contains(",")) {
				String[] split = id.split(",");
				int length = split.length;
				id="('"+id.replace(",", "','")+"')";
				int a =menuDao.delete(id);
				message=(a==length?"ɾ���ɹ�":"ɾ��ʧ��");
				
		} else {
			if (menuDao.select(id) == null) {
				message = "ɾ��ʧ�ܣ����" + id + "�����ڣ�";
				isSuccess = false;
				throw new RuntimeException(message);
			} else {
				id="('"+id+"')";
				int delete = menuDao.delete(id);
				if (delete>=1) {
					message = "ɾ���ɹ���";
					isSuccess = true;
				}else {
					throw new RuntimeException(message);
				}
				
			}

		}

		try {
			resp = BaseJson.returnRespObj("system/menu/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��system/menu/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		Menu menu = menuDao.select(id);
		if (menu != null) {
			message = "��ѯ�ɹ���";
		} else {
			isSuccess = false;
			message = "���" + id + "�����ڣ�";
		}

		try {
			resp = BaseJson.returnRespObj("system/menu/query", isSuccess, message, menu);
		} catch (IOException e) {
			logger.error("URL��system/menu/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		List<Menu> menuList = menuDao.selectList(map);
		int count = menuDao.selectCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		try {
			resp = BaseJson.returnRespList("system/menu/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					menuList);
		} catch (IOException e) {
			logger.error("URL��system/menu/queryList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String menuTree(String jsonBody) {
		String resp = "";
		try {
			String userId = BaseJson.getReqBody(jsonBody).get("accNum").asText();
			List<Menu> allMenuList = menuDao.selectAll();
			List<Menu> userMenuList = userMenuDao.selectUserMenu(userId);
			resp = BaseJson.returnRespObjList("system/menu/userMenu", true, "�û��˵���ѯ��", null,
					new MenuTree(userMenuList, allMenuList).getTree());
		} catch (IOException e) {
			logger.error("URL��system/menu/roleMenu �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String editMenuTree(String jsonBody) {
		String resp = "";
		try {
			String roleId = BaseJson.getReqBody(jsonBody).get("roleId").asText();
			List<Menu> allMenuList = menuDao.selectAll();
			List<Menu> roleMenuList = roleMenuDao.selectRoleMenu(roleId);
			resp = BaseJson.returnRespObjList("system/menu/editMenuTree", true, "��ɫ�ɱ༭�˵���ѯ��", null,
					EditMenuTree.getTree(allMenuList, roleMenuList));
		} catch (IOException e) {
			logger.error("URL��system/menu/editMenuTree �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String roleMenuList(String roleId) {
		ObjectMapper mapper = new ObjectMapper();
		String resp = "";
		try {
			List<Menu> allMenuList = menuDao.selectAll();
			List<Menu> allThirdMenuList = new ArrayList<>();
			for (Menu menu : allMenuList) {
				if (menu.getLevel() == 3) {
					allThirdMenuList.add(menu);
				}
			}

			List<Menu> roleMenuList = roleMenuDao.selectRoleMenu(roleId);
			ObjectNode respBody = JacksonUtil.getObjectNode("");
			respBody.set("roleMenuList", JacksonUtil.getArrayNode(roleMenuList));
			respBody.set("noRoleMenuList", JacksonUtil.getArrayNode(getNoMenuList(allThirdMenuList, roleMenuList)));
			resp = BaseJson.returnResp("system/menu/roleMenuList", true, "��ѯ�ɹ���", respBody);
		} catch (IOException e) {
			logger.error("URL��system/menu/roleMenuList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String userMenuList(String userId) {
		ObjectMapper mapper = new ObjectMapper();
		String resp = "";
		try {
			// ��ѯ���в˵�
			List<Menu> allMenuList = menuDao.selectAll();
			// ���Ե������˵�
			List<Menu> allThirdMenuList = new ArrayList<>();
			for (Menu menu : allMenuList) {
				if (menu.getLevel() == 3) {
					allThirdMenuList.add(menu);
				}
			}
			// �����û�id��ѯ�˵��б�
			List<Menu> userMenuList = userMenuDao.selectUserMenu(userId);
			ObjectNode respBody = JacksonUtil.getObjectNode("");
			// �û��˵��б�
			respBody.set("userMenuList", JacksonUtil.getArrayNode(userMenuList).get("list"));
			respBody.set("noUserMenuList",
					JacksonUtil.getArrayNode(getNoMenuList(allThirdMenuList, userMenuList)).get("list"));
			resp = BaseJson.returnResp("system/menu/roleMenuList", true, "��ѯ�ɹ���", respBody);
		} catch (IOException e) {
			logger.error("URL��system/menu/userMenuList �쳣˵����", e);
		}
		return resp;
	}

	/**
	 * ��ȡ�����û������˵�
	 * 
	 * @param allThirdMenuList
	 * @param MenuList
	 * @return
	 */
	private List<Menu> getNoMenuList(List<Menu> allThirdMenuList, List<Menu> MenuList) {
		List<Menu> noRoleMenuList = new ArrayList<>();
		for (Menu menu : allThirdMenuList) {
			boolean has = false;
			for (Menu roleMenu : MenuList) {
				if (menu.getId().equals(roleMenu.getId())) {
					has = true;
				}
			}
			if (!has) {
				noRoleMenuList.add(menu);
			}
		}
		return noRoleMenuList;
	}

}
