package com.px.mis.sell.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.sell.entity.Printers;

public interface PrintersDao {

	public int deletePrinters(Integer idx);
	
	public int updatePrinters(Printers printers);
	
	public List<Printers> selectListPrinters(Map map);
	
	public int insertPrinters (Printers printers);
	
	public int printersByidx(Map map);
	
	public Printers selectByIdx(Integer idx);
}
