package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.EntrsAgnAdjSub;

public interface EntrsAgnAdjSubDao {
	
    int deleteEntrsAgnAdjSubByAdjSnglId(String adjSnglId);

    int insertEntrsAgnAdjSub(List<EntrsAgnAdjSub> entrsAgnAdjSubList );

    List<EntrsAgnAdjSub> selectEntrsAgnAdjSubByAdjSnglId(String adjSnglId);
    
    

}