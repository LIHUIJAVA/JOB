package com.px.mis.sell.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.sell.dao.DeliveryStatisticDao;
import com.px.mis.sell.dao.PrintersDao;
import com.px.mis.sell.entity.DeliveryStatistic;
import com.px.mis.sell.entity.Printers;
import com.px.mis.sell.service.DeliveryStatisticService;
import com.px.mis.sell.service.PrintersService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@Transactional
@Service
public class DeliveryStatisticServiceImpl implements DeliveryStatisticService {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(DeliveryStatisticService.class);

	@Autowired
	private DeliveryStatisticDao DeliveryStatisticDao;

	@SuppressWarnings("unchecked")
	@Override
	public String SelectDeliveryStatistic(Map map) throws IOException {
		String resp="";
		int pageNo = 1;
		int pageSize = 500;
		
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			 pageNo = (int) map.get("pageNo");
			 pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
		
		}
		
		List<DeliveryStatistic> DeliveryStatistictList = DeliveryStatisticDao.selectDeliveryStatistictList(map);	
		
		int count=DeliveryStatisticDao.selectDeliveryStatistictListCount(map);
		
		try {
			
			resp=BaseJson.returnRespList("url:sell/Report/SelectDeliveryStatistic", true, "成功", count, pageNo, pageSize, DeliveryStatistictList.size(), (int)Math.ceil(count/pageSize), DeliveryStatistictList);
		} catch (IOException e) {
			e.printStackTrace();
			resp=BaseJson.returnRespList("url:sell/Report/SelectDeliveryStatistic", false, "查询失败",null);
		}
		
		return resp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String printDeliveryStatistic(Map map) throws IOException {

		@SuppressWarnings("rawtypes")
		List<DeliveryStatistic> DeliveryStatistictList = DeliveryStatisticDao.selectDeliveryStatistictList(map);
		String resp = "";
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
		}
		try {
			resp = BaseJson.returnRespListAnno("url:sell/Report/SelectDeliveryStatistic", true, "成功",
					DeliveryStatistictList);
		} catch (IOException e) {
			e.printStackTrace();
			resp = BaseJson.returnRespList("url:sell/Report/SelectDeliveryStatistic", false, "查询失败", null);
		}

		return resp;
	}

}
