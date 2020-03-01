package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.Brokerage;


public interface BrokerageDao {

	public void insert(Brokerage brokerage);
	
	public void update(Brokerage brokerage);
	
	public void delete(String brokId);
	
	public Brokerage select(String brokId);
	
	public List<Brokerage> selectList(Map map);
	
	public int selectCount(Map map);
	
}
