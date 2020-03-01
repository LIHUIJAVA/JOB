package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.OrderDealSettings;
import com.px.mis.ec.entity.PlatOrder;

public interface OrderDealSettingsService {

	public String edit(OrderDealSettings orderDealSettings);
	
	public String query(String settingId);
	/**
	 * ƥ��ֿ�
	 * @param platOrder����ʵ��
	 * @param platIdƽ̨���
	 * @return Map
	 * message ������Ϣ
	 * isSuccess �Ƿ�ƥ��ɹ�
	 * whsCode �ֿ����
	 */
	public Map<String,Object> matchWareHouse(PlatOrder platOrder,String platId);
	
	//��ѯ������
	public String selectPlatWhsSpecialList(String jsonBody);
	//���������
	public String addPlatWhsSpecial(String jsonBody);
	//����ɾ��������
	public String deletePlatWhs(String jsonBody);
	//�޸�������
	public String updatePlatWhs(String jsonBody);
}
