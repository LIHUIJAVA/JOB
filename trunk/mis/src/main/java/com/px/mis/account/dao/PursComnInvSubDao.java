package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursComnInvSub;

public interface PursComnInvSubDao{
	
	//�����ɹ���ͨ��Ʊ�ӱ�
	int insertPursComnInvSub(PursComnInvSub pursComnInvSub);
	
	//�޸Ĳɹ���ͨ��Ʊ�ӱ�
	int updatePursComnInvSubByOrdrNum(PursComnInvSub pursComnInvSub);
	
	//ɾ���ɹ���ͨ��Ʊ�ӱ�
	int deletePursComnInvSubByOrdrNum(String pursInvNum);
	
	//������Ų�ѯ�ɹ���ͨ��Ʊ�ӱ���Ϣ
	PursComnInvSub selectPursComnInvSubByOrdrNum(Integer ordrNum);
	
    List<PursComnInvSub> selectPursComnInvSubList(Map map);
    
    int selectPursComnInvSubCount();
    
    //����ʱ�����ɹ���ͨ��Ʊ�ӱ�
  	int insertPursComnInvSubUpload(List<PursComnInvSub> pursComnInvSub);
  	
    
}
