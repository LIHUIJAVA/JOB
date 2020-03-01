package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;

public interface SellInvMasTabDao {
	
	int insertSellInvMasTab(SellInvMasTab sellInvMasTab);
	
	int updateSellInvMasTabBySellInvNum(SellInvMasTab sellInvMasTab);
	
	int deleteSellInvMasTabBySellInvNum(String sellInvNum);
	
	int deleteSellInvMasTabList(List<String> sellInvNum);
	
	SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum);

    List<SellInvMasTab> selectSellInvMasTabList(Map map);
    
    int selectSellInvMasTabCount(Map map);
    
    int selectSellInvMasTabIsNtChk(String sellInvNum);
    //�������۵��ż����ܽ��
	BigDecimal countSellNoTaxAmt(Map<String, String> dataMap);
	
	//�޸����״̬
    int updateSellInvMasTabIsNtChk(SellInvMasTab sellInvMasTab);
    
    //��ѯ����״̬
    int selectSellInvMasTabIsNtBookEntry(String sellInvNum);
    
    String selectSellSnglNumBySellInvMasTab(String sellInvNum);
    
    //����ʱ�������۷�Ʊ����
    int insertSellInvMasTabUpload(SellInvMasTab sellInvMasTab);
    
    //����ʱ��ѯ�ӿ�
    List<SellInvMasTab> printingSellInvMasTabList(Map map);
}
