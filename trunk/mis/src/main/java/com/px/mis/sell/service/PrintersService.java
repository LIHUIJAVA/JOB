package com.px.mis.sell.service;


import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.sell.entity.Printers;

public interface PrintersService {
	
	public ObjectNode deletePrinters(Integer idx);
	
	public ObjectNode updatePrinters(Printers printers);
	
	public String selectListPrinters(Map map);
	
	public ObjectNode insertPrinters (Printers printers);
	
	public Printers selectByIdx(int idx);
}
