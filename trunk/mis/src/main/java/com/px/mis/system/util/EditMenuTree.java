package com.px.mis.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Menu;

public class EditMenuTree {

	private static List<Menu> roleMenus;

	public static List<Map> getTree(List<Menu> allMenuList, List<Menu> roleMenuList) {
		roleMenus = roleMenuList;
		return getSonList(null, allMenuList);
	}

	private static Map getObjList(Menu menu, List menuList) {
		Map map = new HashMap();
		map.put("id", menu.getId());
		map.put("name", menu.getName());
		map.put("url", menu.getUrl());
		map.put("ico", menu.getIco());
		map.put("pid", menu.getPid());
		if (menuList != null && menuList.size() != 0) {
			map.put("children", menuList);
		}
		if (menu.getLevel() == 3) {
			map.put("selected", false);
			if (roleMenus != null) {
				for (Menu roleMenu : roleMenus) {
					if (menu.getId().equals(roleMenu.getId())) {
						map.put("selected", true);
					}
				}
			}
		}
		return map;
	}

	private static List<Map> getSonList(Menu menu, List<Menu> allMenuList) {
		List<Map> mList = new ArrayList();
		if (menu == null) {
			for (Menu sonMenu : allMenuList) {
				if (sonMenu.getLevel() != 3) {
					boolean hasSon = false;
					for (Menu sonsMenu : allMenuList) {
						if (sonsMenu.getPid().equals(sonMenu.getPid())) {
							hasSon = true;
						}
					}
					if (hasSon) {
						mList.add(getObjList(sonMenu, getSonList(sonMenu, allMenuList)));
					}
				}
			}
		} else {
			for (Menu sonMenu : allMenuList) {
				if (sonMenu.getLevel() - 1 == menu.getLevel() && sonMenu.getPid().equals(menu.getId())) {
					List<Map> mapList = new ArrayList<Map>();
					if (sonMenu.getLevel() < 3) {
						mList.add(getObjList(sonMenu, getSonList(sonMenu, allMenuList)));
					} else {
						mList.add(getObjList(sonMenu, null));
					}
				}
			}
		}
		return mList;
	}

}
