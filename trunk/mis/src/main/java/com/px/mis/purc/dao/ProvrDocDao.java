package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.entity.ProvrDoc;

public interface ProvrDocDao {
	
    int deleteProvrDocByProvrId(String provrId);

    int insertProvrDoc(ProvrDoc provrDoc);
    
    int insertProvrDocUpload(ProvrDoc provrDoc);

    int updateProvrDocByProvrId(ProvrDoc provrDoc);
    
    ProvrCls selectProvrDocByProvrId(String provrId);
 
    List<ProvrCls> selectProvrDocList(Map map);
    
    List<ProvrCls> selectProvrDocListByItv(Map map);
    
    int selectProvrDocCount(Map map);
    
    List<ProvrCls> printingProvrDocList(Map map);
    
    String selectProvrId(String provrId);//≤È—Ø±‡∫≈≤È—Ø±‡∫≈ «∑Ò¥Ê‘⁄
    
    int deleteProvrDocList(List<String> provrId);
    
    /*List<ProvrCls> selectProvrDocByProvrClsId(String provrClsId);*/
}