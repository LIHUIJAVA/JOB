package com.px.mis.account.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellInvMasTab;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.purc.entity.SellSngl;

public interface SellInvMasTabService {

	ObjectNode insertSellInvMasTab(SellInvMasTab sellInvMasTab,String userId);
	
	ObjectNode updateSellInvMasTab(SellInvMasTab sellInvMasTab,List<SellInvSubTab> sellInvSubTabList);
	
	ObjectNode deleteSellInvMasTabBySellInvNum(String sellInvNum);
	
	String deleteSellInvMasTabList(String sellInvNum);
	
	SellInvMasTab selectSellInvMasTabBySellInvNum(String sellInvNum);
	
    String selectSellInvMasTabList(Map map);
    
    //�������ר�÷�Ʊ
    String  updateSellInvMasTabIsNtChkList(List<SellInvMasTab> sellInvMasTabList);
    
    //���۷�Ʊ����ʱ���������۳��ⵥ������������۳��ⵥ
    String selectSellInvMasTabBingList(List<SellComnInv> sellSnglList);
    
  //�������۳��ⵥ
  	public String uploadFileAddDb(MultipartFile  file);
  	
    //����ʱ��ʹ�õĲ�ѯ�ӿ�
    String upLoadSellInvMasTabList(Map map);
}
