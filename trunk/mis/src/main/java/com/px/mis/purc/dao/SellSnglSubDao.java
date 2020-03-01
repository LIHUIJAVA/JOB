package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.SellSnglSub;

public interface SellSnglSubDao {
	
    int deleteSellSnglSubBySellSnglId(String sellSnglId);

    int insertSellSnglSub(List<SellSnglSub> sellSnglSubList );
    //ɾ��ʱ�򱸷�һ��
    int insertSellSnglSubDl(List<String> list2);
    
    List<SellSnglSub> selectSellSnglSubBySellSnglId(String sellSnglId);
    
    //��ѯ���۵��е�����
    BigDecimal selectSellSnglSubQty(SellSnglSub sellSnglSub);

	int insertSellSnglSubUpload(List<SellSnglSub> sellSnglSub);
	
	//���մ�����ֿ⡢���Ų�ѯ���۵��ӱ���Ϣ
	SellSnglSub selectSellSnglSubByInvWhsBat(Map map);
	
	//�������۵��ӱ���Ų�ѯ�����ӱ���Ϣ
	SellSnglSub selectSellSnglSubBySelSnIdAndOrdrNum(Map map);
	
	//�޸����۵�δ��Ʊ����
	int updateSellSnglUnBllgQtyByOrdrNum(Map map);
	//��ѯ���۵�δ��Ʊ����
	BigDecimal selectSellSnglUnBllgQtyByOrdrNum(Map map);
	//����ʱ�������۵���������ѯ�ӱ���Ϣ
	List<SellSnglSub> selectSellSnglSubBySellSnglIdAndUnBllgQty(String sellSnglId);
	
	//�޸����۵�δ��Ʊ����
	int updateSellSnglRtnblQtyByOrdrNum(Map map);
	//��ѯ���۵�δ��Ʊ����
	BigDecimal selectRtnblQtyByOrdrNum(Map map);
	//����ʱ�������۵���������ѯ�ӱ���Ϣ
	List<SellSnglSub> selectSellSnglSubByRtnblQty(List<String> sellSnglId);
    
}