package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.LogisticsTab;

public interface LogisticsAdjustService {
	//手工拆物流表之前的展示
	public String showSplitLogistics(Integer ordrNum);
	//手工拆物流表
	public String editSplitLogistics(LogisticsTab logisticsTab);
	//手工合并物流表之前的展示
	public String showMergeLogistics(String ordrNums);
	//手工合并物流表
	public String editMergeLogistics(List<Map> list,Integer ordrNum);
}
