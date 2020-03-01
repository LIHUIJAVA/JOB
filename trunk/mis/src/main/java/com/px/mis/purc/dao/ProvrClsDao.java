package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.ProvrCls;

public interface ProvrClsDao {
	
	List<ProvrCls> selectProvrCls();
	
	ProvrCls selectProvrClsByProvrClsId(String provrClsId);
	
	/*ProvrCls selectProvrClsByProvrClsId(@Param("provrClsId") String provrClsId);*/
	
    int deleteProvrClsByProvrClsId(String provrClsId);

    int insertProvrCls(ProvrCls provrCls);

    int updateProvrClsByProvrClsId(ProvrCls provrCls);

/*    int updateByPrimaryKey(ProvrCls provrCls);
    
    int insertSelective(ProvrCls provrCls);*/
}