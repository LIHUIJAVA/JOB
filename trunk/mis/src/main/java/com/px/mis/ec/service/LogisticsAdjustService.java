package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.LogisticsTab;

public interface LogisticsAdjustService {
	//�ֹ���������֮ǰ��չʾ
	public String showSplitLogistics(Integer ordrNum);
	//�ֹ���������
	public String editSplitLogistics(LogisticsTab logisticsTab);
	//�ֹ��ϲ�������֮ǰ��չʾ
	public String showMergeLogistics(String ordrNums);
	//�ֹ��ϲ�������
	public String editMergeLogistics(List<Map> list,Integer ordrNum);
}
