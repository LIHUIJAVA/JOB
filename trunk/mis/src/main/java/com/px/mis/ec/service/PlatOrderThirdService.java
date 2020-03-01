package com.px.mis.ec.service;

import java.util.List;
import com.px.mis.ec.entity.PlatOrderThird;

public interface PlatOrderThirdService {

	String exportPlatOrderThird(String userId,String platId,String storeId, List<PlatOrderThird> paltOrderList);

	String recallPlatOrderThird(String orderId, String storeId,String userId);

	String testPddOrder();
	
	
	

}
