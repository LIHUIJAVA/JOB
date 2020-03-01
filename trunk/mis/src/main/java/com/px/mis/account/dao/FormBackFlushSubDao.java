package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.entity.FormBackFlushSub;

public interface FormBackFlushSubDao {
	
	void insertRedFormBackFlushSubList(List<FormBackFlushSub> subList);
}
