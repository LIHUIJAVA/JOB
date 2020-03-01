package com.px.mis.system.service;

import java.util.List;
import java.util.Map;

import com.px.mis.system.entity.MisLog;

public interface MisLogService {

	public String add(MisLog menu);

	public String edit(MisLog menu);

	public String editList(List<MisLog> menu);

	public String delete(String id);

	public String queryList(Map<?, ?> map);

	public String queryPrint(Map<?, ?> map);

}
