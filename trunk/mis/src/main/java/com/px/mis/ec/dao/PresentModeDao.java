package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PresentMode;


public interface PresentModeDao {
	
	int insertPresentMode(PresentMode presentMode);
	
	int updatePresentModeById(PresentMode presentMode);
	
	int deletePresentModeById(Integer no);
	
	PresentMode selectPresentModeById(Integer no);

	PresentMode selectPresentModeByPresentModeCode(String presentModeCode);
	
    List<PresentMode> selectPresentModeList(Map map);
    
    int selectPresentModeCount();
}
