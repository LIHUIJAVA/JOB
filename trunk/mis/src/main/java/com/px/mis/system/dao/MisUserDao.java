package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.MisUser;

public interface MisUserDao {

	public void insert(MisUser misUser);

	public void delete(String accNum);

	public void update(MisUser misUser);

	public MisUser select(String accNum);

	// ������ԃ
	public List<MisUser> select2(String accNum);

	public List<MisUser> selectList(Map map);

	public List<MisUser> selectAll(Map map);

	public int selectCount(Map map);

	public void delMisUser(List<String> ids);

	public void updateBatch(List<MisUser> misUsers);
	//����ɾ��
	public void delete2(String accNum);
}
