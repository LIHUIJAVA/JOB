package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursInvSubTab;

public interface PursInvSubTabDao{
	
	//�����ɹ�ר�÷�Ʊ�ӱ�
	int insertPursInvSubTab(PursInvSubTab pursInvSubTab);
	
	//�޸Ĳɹ�ר�÷�Ʊ�ӱ�
	int updatePursInvSubTabByOrdrNum(PursInvSubTab pursInvSubTab);
	
	//ɾ���ɹ�ר�÷�Ʊ�ӱ�
	int deletePursInvSubTabByOrdrNum(String pursInvNum);
	
	//������Ų�ѯ�ɹ�ר�÷�Ʊ�ӱ���Ϣ
	PursInvSubTab selectPursInvSubTabByOrdrNum(Integer ordrNum);
	
    List<PursInvSubTab> selectPursInvSubTabList(Map map);
    
    int selectPursInvSubTabCount();
    
    //�����ɹ�ר�÷�Ʊ�ӱ�
  	int insertPursInvSubTabUpload(List<PursInvSubTab> pursInvSubTab);
    
}
