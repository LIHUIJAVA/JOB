package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.PlatOrder;

public interface OrderAdjustService {
	//������Ʒ֮ǰ��չʾ
	public String showAdjustGoods(String orderId);
	//������Ʒ
	public String editAdjustGoods(List<Map> list);
	//�ֹ���֮ǰ��չʾ
	public String showSplitOrder(String orderId);
	//�ֹ���
	public String editSplitOrder(PlatOrder platOrder);
	//�ֹ��ϲ�֮ǰ��չʾ
	public String showMergeOrder(String orderIds);
	//�ֹ��ϲ�
	public String editMergeOrder(List<Map> list,String orderId);
}
