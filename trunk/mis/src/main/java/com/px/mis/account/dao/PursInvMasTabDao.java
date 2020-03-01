package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursInvMasTab;
import com.px.mis.account.entity.PursInvSubTab;

public interface PursInvMasTabDao{
	
	//�����ɹ�ר�÷�Ʊ
	int insertPursInvMasTab(PursInvMasTab pursInvMasTab);
	
	//�޸Ĳɹ�ר�÷�Ʊ
	int updatePursInvMasTabById(PursInvMasTab pursInvMasTab);
	
	//ɾ���ɹ�ר�÷�Ʊ
	int deletePursInvMasTabById(String pursInvNum);
	
	//���շ�Ʊ�Ų�ѯ�ɹ�ר�÷�Ʊ����
	PursInvMasTab selectPursInvMasTabById(String pursInvNum);

	//��ҳ��ѯ�ɹ�ר�÷�Ʊ
    List<PursInvMasTab> selectPursInvMasTabList(Map map);
    
    //��ѯ�ɹ�ר�÷�Ʊ������
    int selectPursInvMasTabCount(Map map);
    
    //����ɾ���ɹ�ר�÷�Ʊ
    int deletePursInvMasTabList(List<String> pursInvNum);
    
    //��ѯ�ɹ�ר�÷�Ʊ�Ƿ����
    int selectPursInvMasTabIsNtChk(String pursInvNum);
    
    //���ղɹ���Ʊ��������ⵥ
    PursInvSubTab countPursNoTaxAmt(Map map);
    //���ղɹ���ⵥ��������ⵥ
    PursInvSubTab countIntoNoTaxAmt(Map<String, Object> dataMap);
    //����ɹ���Ʊ�����ͽ�� -�ɱ�
   	PursInvSubTab countPursNoTaxAmtAndQty(Map<String, Object> dataMap);
    //�������ۺ��������������ͽ��-����ɱ�
   	PursInvSubTab countSellAndOthAmt(Map<String, Object> dataMap);
   	
   	List<PursInvSubTab> countPursNoTaxAmtAndQtyList(Map<String, Object> dataMap);
   	List<PursInvSubTab> countSellAndOthAmtList(Map<String, Object> dataMap);
   	
    //�޸����״̬
    int updatePursInvMasTabIsNtChk(PursInvMasTab pursInvMasTab);
    
    //��ѯ�ɹ���Ʊ����״̬
    int selectPursInvMasTabIsNtBookEntry(String pursInvNum);
    
    String selectIntoWhsSnglIdByPursInvMasTab(String pursInvNum);
    
    //����ʱ�����ɹ�ר�÷�Ʊ
    int insertPursInvMasTabUpload(PursInvMasTab pursInvMasTab);
    
    //����ʱ��ѯ�ӿ�
    List<PursInvMasTab> printingPursInvMasTabList(Map map);

	

	

	
   

	
}
