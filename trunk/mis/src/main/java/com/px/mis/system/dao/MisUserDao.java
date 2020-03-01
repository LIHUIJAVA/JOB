package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.MisUser;

public interface MisUserDao {

	public void insert(MisUser misUser);

	public void delete(String accNum);

	public void update(MisUser misUser);

	public MisUser select(String accNum);

	// 批量查詢
	public List<MisUser> select2(String accNum);

	public List<MisUser> selectList(Map map);

	public List<MisUser> selectAll(Map map);

	public int selectCount(Map map);

	public void delMisUser(List<String> ids);

	public void updateBatch(List<MisUser> misUsers);
	//批量删除
	public void delete2(String accNum);
}
