package com.px.mis.system.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;

public interface UserWhsDao {

	List<WhsDoc> selectUserWhsByMap(Map<String,Object> map);

	List<WhsDoc> selectWhsByMap(Map<String, Object> map);

	void insertList(List<UserWhs> list);

	void delList(List<String> delList);
	//≤È—Ø≤÷ø‚¥Û≤÷
	List<RealWhs> selectRealWhs(Map<String, Object> map);

}
