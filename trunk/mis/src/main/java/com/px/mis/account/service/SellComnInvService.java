package com.px.mis.account.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.SellComnInv;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.entity.SellComnInvForU8.U8SellComnInv;

public interface SellComnInvService {

	ObjectNode addSellComnInv(SellComnInv sellComnInv,String userId,String loginTime);
	
	ObjectNode editSellComnInv(SellComnInv sellComnInv,List<SellComnInvSub> sellComnInvSubList);
	
	ObjectNode deleteSellComnInvBySellInvNum(String sellInvNum);
	
	String deleteSellComnInvList(String sellInvNum);
	
	String selectSellComnInvBySellInvNum(String sellInvNum);
	
    String selectSellComnInvList(Map map);
    
    //���������ͨ��Ʊ
    Map<String,Object>  updateSellComnInvIsNtChkList(SellComnInv sellComnInv) throws Exception;
    
    //���۷�Ʊ����ʱ���������۳��ⵥ������������۳��ⵥ
    String selectSellComnInvBingList(List<SellComnInv> sellComnInvList);
    
  //�������۳��ⵥ
  	public String uploadFileAddDb(MultipartFile  file,int i);
  	
 
    String selectSellReturnEntrs(Map map) throws IOException;//�������۵����˻�����ί�д���������
    
    String selectSellComnInvBySellRtnEntList(List<SellComnInv> sellComnInvList);//����ʱ�����۵����˻�����ί�д����������ӱ���Ϣ����ǰ��

    //ԭ���ĵ����ӿ�
    String upLoadSellComnInvList(Map map);
    //�����ӿ�
	String printSellComnInvList(Map map);
	//ǰ�˽���,����
	String pushToU8(String ids) throws IOException;
	//���͵�U8
	U8SellComnInv encapsulation(SellComnInv sellComnInv, List<SellComnInvSub> sellComnInvSubList);

    
}
