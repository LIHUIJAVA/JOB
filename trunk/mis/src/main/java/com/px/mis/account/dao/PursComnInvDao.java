package com.px.mis.account.dao;

import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.service.impl.PursComnInvServiceImpl.zizhu;

import java.util.List;
import java.util.Map;

public interface PursComnInvDao{
	
	//�����ɹ���ͨ��Ʊ����
	int insertPursComnInv(PursComnInv pursComnInv);
	
	//�޸Ĳɹ���ͨ��Ʊ����
	int updatePursComnInvById(PursComnInv pursComnInv);
	
	//ɾ���ɹ���ͨ��Ʊ����
	int deletePursComnInvById(String pursInvNum);
	
	//���շ�Ʊ�Ų�ѯ�ɹ���ͨ��Ʊ����
	PursComnInv selectPursComnInvById(String pursInvNum);
	
	//���շ�Ʊ�Ų�ѯ�ɹ���ͨ��Ʊ������
	String selectPursComnInvByIds(String pursInvNum);

	//��ҳ��ѯ�ɹ���ͨ��Ʊ����
    List<PursComnInv> selectPursComnInvList(Map map);
    
    //��ѯ�ɹ���ͨ��Ʊ����������
    int selectPursComnInvCount(Map map);
    
    //����ɾ���ɹ���ͨ��Ʊ����
    int deletePursComnInvList(List<String> pursInvNum);
    
    //��ѯ�ɹ���ͨ��Ʊ���״̬
    Integer selectPursComnInvIsNtChk(String pursInvNum);
    
    //��ѯ�ɹ���ͨ��Ʊ����״̬
    Integer selectPursComnInvIsNtBookEntry(String pursInvNum);
    
    //�޸Ĳɹ���ͨ��Ʊ�����״̬
    int updatePursComnInvIsNtChk(PursComnInv pursComnInv);
    
    //��ѯ�ɹ���ͨ��Ʊ�Ĳɹ���ⵥ��
    String selectIntoWhsSnglIdByPursComnInv(String pursInvNum);
    
    //����ʱ�����ɹ���ͨ��Ʊ����
    int insertPursComnInvUpload(List<PursComnInv> pursComnInv);
    
    //ԭ���ĵ����ӿ�,����ɾ
    List<PursComnInv> printingPursComnInvList(Map map);
    //�����ӿ�,����Key
    List<zizhu> printPursComnInvList(Map map);
    //����Ʊ��liststr,������ѯ��Ʊ
    List<PursComnInv> selectComnInvs(List<String> idList);
    //����U8״̬��д
	int updatePushed(String dscode);
}
