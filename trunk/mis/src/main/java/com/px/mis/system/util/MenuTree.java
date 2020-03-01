package com.px.mis.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.Menu;

public class MenuTree {

	private List<Menu> userMenuList;
	private List<Menu> allMenuList;

	public MenuTree(List<Menu> userMenuList, List<Menu> allMenuList) {
		this.userMenuList = userMenuList;
		this.allMenuList = allMenuList;
	}
	
	public List<Map> getTree() {
		List<Map> mList = new ArrayList<>();
		mList = getSonList(null);
		return mList;
	}

	private Map getObjList(Menu menu, List list) {
		Map map = new HashMap();
		map.put("id", menu.getId());
		map.put("name", menu.getName());
		map.put("url", menu.getUrl());
		map.put("ico", menu.getIco());
		map.put("pid", menu.getPid());
		map.put("targetType", menu.getTargetType());
		map.put("isShow", 1);
		if (list != null && list.size() != 0) {
			map.put("children", list);
		}
		return map;
	}

	private List<Map> getSonList(Menu menu) {
		List<Map> mList = new ArrayList();
		
		if (menu == null) {
			for (Menu sonMenu : allMenuList) {
				if (sonMenu.getLevel() == 1 && hasSon(sonMenu)) {
					mList.add(getObjList(sonMenu, getSonList(sonMenu)));
				}
			}
		} else {
			if (menu.getLevel() == 1) {
				for (Menu sonMenu : allMenuList) {
					if (sonMenu.getPid().equals(menu.getId()) && hasSon(sonMenu)) {
						mList.add(getObjList(sonMenu, getSonList(sonMenu)));
					}
				}
			}else if (menu.getLevel() == 2) {
				for (Menu sonMenu : userMenuList) {
					if (sonMenu.getPid().equals(menu.getId())) {
						mList.add(getObjList(sonMenu, null));
					}
				}
			}
		}
		return mList;
	}

	private boolean hasSon(Menu menu) {
		boolean hasSon = false;
		if (menu.getLevel() == 1) {
			for (Menu sonMenu : allMenuList) {
				if (sonMenu.getLevel() == 2 && sonMenu.getPid().equals(menu.getId())) {
					if (hasSon(sonMenu)) {
						hasSon=true;
						break;
					}
				}
			}
		} else if (menu.getLevel() == 2) {
			for (Menu sonMenu : userMenuList) {
				if (sonMenu.getPid().equals(menu.getId())) {
					hasSon = true;
				}
			}
		}
		return hasSon;
	}
}
