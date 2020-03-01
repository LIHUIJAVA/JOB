package com.px.mis.sell.service;

import java.io.IOException;
import java.util.Map;

public interface DeliveryStatisticService {

	String SelectDeliveryStatistic(Map map) throws IOException;

	String printDeliveryStatistic(Map map) throws IOException;
	
}
