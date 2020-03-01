package com.px.mis.purc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ProvrCls;

public class ProvrClsTree {

	public static List<Map> getTree(List<ProvrCls> provrClsList) throws IOException {
		return getSonList(null, provrClsList);
	}

	private static Map getObjList(ProvrCls provrCls, List list) throws IOException {
		Map map = new HashMap();
		map.put("provrClsId", provrCls.getProvrClsId());
		map.put("provrClsNm", provrCls.getProvrClsNm());
		map.put("ico", provrCls.getIco());
		map.put("level", provrCls.getLevel());
		map.put("pid", provrCls.getPid());
		map.put("memo", provrCls.getMemo());

		if (list != null && list.size() != 0) {
			map.put("children", list);
		}
		return map;
	}

	private static List<Map> getSonList(ProvrCls provrCls, List<ProvrCls> provrClsList) throws IOException {

		List<Map> mList = new ArrayList();
		if (provrCls == null && provrClsList.size() > 0) {
			for (ProvrCls sonProvrCls : provrClsList) {
				if (sonProvrCls != null && sonProvrCls.getLevel() != null && sonProvrCls.getLevel() == 0) {
					mList.add(getObjList(sonProvrCls, getSonList(sonProvrCls, provrClsList)));
				}
			}
		} else {

			for (ProvrCls sonProvrCls : provrClsList) {
				if (sonProvrCls.getPid() != null && sonProvrCls.getLevel() - 1 == provrCls.getLevel()
						&& sonProvrCls.getPid().equals(provrCls.getProvrClsId())) {
					List<Map> mapList = new ArrayList<Map>();
					if (sonProvrCls.getLevel() < 3) {
						mList.add(getObjList(sonProvrCls, getSonList(sonProvrCls, provrClsList)));
					} else {
						mList.add(getObjList(sonProvrCls, null));
					}
				}
			}

		}
		return mList;
	}
}
