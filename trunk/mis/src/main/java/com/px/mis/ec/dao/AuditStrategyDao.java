package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.AuditStrategy;

public interface AuditStrategyDao {
	public int insert(AuditStrategy entity);
	public void deletezhu(AuditStrategy entity);
	public void deletezi(AuditStrategy entity);
	public void update(AuditStrategy entity);
	public List<AuditStrategy> selectList(Map map);
	public int selectCount(Map map);
	public AuditStrategy findById(int id);
	public int checkUsedByStore(int id);
}
