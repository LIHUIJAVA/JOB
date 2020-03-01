package com.px.mis.purc.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.RecvSendCate;

public class RecvSendCateTree {
	
	public static List<Map> getTree(List<RecvSendCate> recvSendCateList) throws IOException {
		return  getSonList(null,recvSendCateList);
	}
	
	private static Map getObjList(RecvSendCate recvSendCate,List list) throws IOException {
		Map map=new HashMap();
		map.put("recvSendCateId", recvSendCate.getRecvSendCateId());
		map.put("recvSendCateNm", recvSendCate.getRecvSendCateNm());
		map.put("recvSendInd", recvSendCate.getRecvSendInd());
		map.put("cntPtySubjEncd", recvSendCate.getCntPtySubjEncd());
		map.put("ico", recvSendCate.getIco());
		map.put("level", recvSendCate.getLevel());
		map.put("pid", recvSendCate.getPid());
		map.put("memo", recvSendCate.getMemo());
		
		if(list!=null&&list.size()!=0) {
			map.put("children", list);
		}
		return map;
	}
	
	private static List<Map> getSonList(RecvSendCate recvSendCate,List<RecvSendCate> recvSendCateList) throws IOException {
		
		List<Map> mList=new ArrayList();
		if(recvSendCate==null) {
			for (RecvSendCate sonRecvSendCate : recvSendCateList) {
				if (sonRecvSendCate.getLevel()==0) {
					mList.add(getObjList(sonRecvSendCate,getSonList(sonRecvSendCate, recvSendCateList)));
				}
			}
		}else {
			for (RecvSendCate sonRecvSendCate : recvSendCateList) {
				if (sonRecvSendCate.getLevel()-1==recvSendCate.getLevel()&&sonRecvSendCate.getPid().equals(recvSendCate.getRecvSendCateId())) {
					List<Map> mapList=new ArrayList<Map>();
					if(sonRecvSendCate.getLevel()<3) {
						mList.add(getObjList(sonRecvSendCate,getSonList(sonRecvSendCate, recvSendCateList)));
					}else {
						mList.add(getObjList(sonRecvSendCate,null));
					}
				}
			}
		}
		return mList;
	}
}
