package com.px.mis.purc.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.CustCls;

public class CustClsTree {
	
	public static List<Map> getTree(List<CustCls> custClsList) throws IOException {
		return  getSonList(null,custClsList);
	}
	
	private static Map getObjList(CustCls custCls,List list) throws IOException {
		Map map=new HashMap();
		map.put("clsId", custCls.getClsId());
		map.put("clsNm", custCls.getClsNm());
		map.put("ico", custCls.getIco());
		map.put("level", custCls.getLevel());
		map.put("pid", custCls.getPid());
		map.put("memo", custCls.getMemo());
		
		if(list!=null&&list.size()!=0) {
			map.put("children", list);
		}
		return map;
	}
	
	private static List<Map> getSonList(CustCls custCls,List<CustCls> custClsList) throws IOException {
		
		List<Map> mList=new ArrayList();
		if(custCls==null) {
			for (CustCls sonCustCls : custClsList) {
				if (sonCustCls.getLevel()==0) {
					mList.add(getObjList(sonCustCls,getSonList(sonCustCls, custClsList)));
				}
			}
		}else {
			for (CustCls sonCustCls : custClsList) {
				if (sonCustCls.getLevel()-1==custCls.getLevel()&&sonCustCls.getPid().equals(custCls.getClsId())) {
					List<Map> mapList=new ArrayList<Map>();
					if(sonCustCls.getLevel()<3) {
						mList.add(getObjList(sonCustCls,getSonList(sonCustCls, custClsList)));
					}else {
						mList.add(getObjList(sonCustCls,null));
					}
				}
			}
		}
		return mList;
	}
}
