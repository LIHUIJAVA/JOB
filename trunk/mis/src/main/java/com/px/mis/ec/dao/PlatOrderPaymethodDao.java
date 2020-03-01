package com.px.mis.ec.dao;

import java.util.List;
import com.px.mis.ec.entity.PlatOrderPaymethod;

public interface PlatOrderPaymethodDao {
	
	//付款方式批量增加
	int insert(List<PlatOrderPaymethod> payMentList);
	
	

}
