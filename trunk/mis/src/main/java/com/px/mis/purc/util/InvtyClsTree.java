package com.px.mis.purc.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.InvtyCls;

public class InvtyClsTree {
	
	public static List<Map> getTree(List<InvtyCls> invtyClsList) throws IOException {
		return  getSonList(null,invtyClsList);
	}
	
	private static Map getObjList(InvtyCls invtyCls,List list) throws IOException {
		Map map=new HashMap();
		map.put("invtyClsEncd", invtyCls.getInvtyClsEncd());
		map.put("invtyClsNm", invtyCls.getInvtyClsNm());
		map.put("ico", invtyCls.getIco());
		map.put("level", invtyCls.getLevel());
		map.put("pid", invtyCls.getPid());
		map.put("memo", invtyCls.getMemo());
		
		if(list!=null&&list.size()!=0) {
			map.put("children", list);
		}
		return map;
	}
	
	private static List<Map> getSonList(InvtyCls invtyCls,List<InvtyCls> invtyClsList) throws IOException {
		
		List<Map> mList=new ArrayList();
		if(invtyCls==null) {
			for (InvtyCls sonInvtyCls : invtyClsList) {
				if (sonInvtyCls.getLevel()==0) {
					mList.add(getObjList(sonInvtyCls,getSonList(sonInvtyCls, invtyClsList)));
				}
			}
		}else {
			for (InvtyCls sonInvtyCls : invtyClsList) {
				if (sonInvtyCls.getLevel()-1==invtyCls.getLevel()&&sonInvtyCls.getPid().equals(invtyCls.getInvtyClsEncd())) {
					List<Map> mapList=new ArrayList<Map>();
					if(sonInvtyCls.getLevel()<3) {
						mList.add(getObjList(sonInvtyCls,getSonList(sonInvtyCls, invtyClsList)));
					}else {
						mList.add(getObjList(sonInvtyCls,null));
					}
				}
			}
		}
		return mList;
	}
}
