package com.px.mis.ec.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.PresentMode;

public interface PresentModeService {

	ObjectNode insertPresentMode(PresentMode presentMode);
	
	ObjectNode updatePresentModeById(PresentMode presentMode);
	
	ObjectNode deletePresentModeById(Integer no);
	
	PresentMode selectPresentModeById(Integer no);
	
    String selectPresentModeList(Map map);
}
