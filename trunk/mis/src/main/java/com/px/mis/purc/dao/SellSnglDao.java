package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDetail;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.purc.entity.BeiYong;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.service.impl.SellSnglServiceImpl.zizhu;

public interface SellSnglDao {
	
    int deleteSellSnglBySellSnglId(String sellSnglId);

    int insertSellSngl(SellSngl sellSngl);
    //ɾ��ʱ�򱸷�һ��
    int insertSellSnglDl(List<String> lists2);

    SellSngl selectSellSnglBySellSnglId(String sellSnglId);
    
    SellSngl selectSellSnglById(String sellSnglId);

    int updateSellSnglBySellSnglId(SellSngl sellSngl);

    List<SellSngl> selectSellSnglList(Map map);
    //��ҳ+����
    List<zizhu> selectSellSnglListOrderBy(Map map);
	
	int selectSellSnglCount(Map map);
	
	int deleteSellSnglList(List<String> sellSnglId);
	
	//�����������״̬
	int updateSellSnglIsNtChkList(List<SellSngl> sellSngl);
	//�����������״̬
	int updateSellSnglIsNtChk(SellSngl sellSngl);
	//��ѯ���״̬
	int selectSellSnglIsNtChk(String sellSnglId);
	//��ѯ���״̬
	int selectSellSnglIsPick(String sellSnglId);
	
	
	List<zizhu> printingSellSnglList(Map map);
	
	//���۵�����ʱɾ���������е���Ϣ
	int deleteLogisticsTab(String saleEncd);
	
	//===========================================================
	SellSngl selectSellSnglByOrderId(String orderId);
	
	//��ѯ������ϸ��
    List<BeiYong> selectSellSnglInvtyEncd(Map map);
	int selectSellSnglInvtyEncdCount(Map map);

	int insertSellSnglUpload(List<SellSngl> sellSnglList);
	
	//�������۵��Ų�ѯ��������Ϣ
	LogisticsTab selectLogisticsTabBySellSnglId(String sellSnglId);
	
	int updateSellSnglIsNtBllgOK(String sellSnglId);
	
	int updateSellSnglIsNtBllgNO(String sellSnglId);
	
	//��ѯ���۳��ⵥ�ӱ���ⵥ�� 
	List<String> selectB(Map map);
	
	int updateA(String sellSnglId);
	
	SellSngl selectSellSnglAndSubBySellSnglId(String sellSnglId);
	
	List<Map> selectXSTJ(Map map);//����ͳ�Ʊ��ѯ
	Integer selectXSTJCount(Map map);//��ѯ����ͳ�Ʊ�������
	
    List<InvtyDetail> selectSalesCountList(Map map);
	int countSalesCountList(Map map);
	BigDecimal selectSalesCost(Map map); //���۳ɱ�
	
	//��ѯ���۷���������Ϣ���˻�ʱ����ʱʹ��
	List<SellSngl> selectSellSnglListToCZ(Map map);
	int selectSellSnglListToCZCount(Map map);
	
	//��ѯ���۷���������Ϣ����Ʊ����ʱʹ��
	List<SellSngl> selectSellSnglCZLists(Map map);
	int selectSellSnglCZListsCount(Map map);

	Map selectSellSnglListSums(Map map);

	Map selectSellSnglInvtyEncdSums(Map map);

}