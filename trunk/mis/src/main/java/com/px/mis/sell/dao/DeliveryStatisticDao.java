package com.px.mis.sell.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.sell.entity.DeliveryStatistic;

public interface DeliveryStatisticDao {

public List<DeliveryStatistic> selectDeliveryStatistictList(Map map);

public int selectDeliveryStatistictListCount(Map map);

}
