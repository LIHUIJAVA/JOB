package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl.zizhu;


public interface ToGdsSnglDao {
	
	//ɾ��������������Ϣ
    int deleteToGdsSnglByToGdsSnglId(String toGdsSnglId);
    
    //������������Ϣ
    int insertToGdsSngl(ToGdsSngl toGdsSngl);
    
    //ɾ��ʱ�򱸷ݵ�������Ϣ
    int insertToGdsSnglDl(List<String> lists2);
    
    //���뵼��ʱ������������Ϣ
    int insertToGdsSnglUpload(List<ToGdsSngl> toGdsSngl);
    
    //�޸ĵ�������Ϣ
    int updateToGdsSnglByToGdsSnglId(ToGdsSngl toGdsSngl);
    
    //���ݵ����������ѯ���������ӹ�����Ϣ
    ToGdsSngl selectToGdsSnglByToGdsSnglId(String toGdsSnglId);
    
    //���ݵ����������ѯ������������Ϣ
    ToGdsSngl selectToGdsSnglById(String toGdsSnglId);

    //��ҳ��ѯ�������б�
	 List<ToGdsSngl> selectToGdsSnglList(Map map); 
    //��ҳ������
    List<ToGdsSnglServiceImpl.zizhu> selectToGdsSnglListOrderBy(Map map);
    
	int selectToGdsSnglCount(Map map);
	
	//����ɾ���������б�
	int deleteToGdsSnglList(List<String> toGdsSnglId);
	
	//�����޸����״̬
	int updateToGdsSnglIsNtChkList(List<ToGdsSngl> toGdsSngl);
	
	//�����޸����״̬
	Integer updateToGdsSnglIsNtChk(ToGdsSngl toGdsSngl);
	
	//��ѯ���״̬
	Integer selectToGdsSnglIsNtChk(String toGdsSnglId);
	
	//��ѯ�˻�״̬
	Integer selectToGdsSnglIsNtRtnGood(String toGdsSnglId);
	
	List<zizhu> printingToGdsSnglList(Map map);
	
	//���ݲɹ������Ų�ѯ��������Ϣ
	List<ToGdsSngl> selectToGdsSnglByPursOrdrId(String pursOrdrId);
	
	//Map selectToGdsSnglsByPursOrdrId(ToGdsSngl toGdsSngl);
	
	//������ϸ���ҳ��ѯ����
	List<Map> selectToGdsSnglByInvtyEncd(Map map);
	
	int selectToGdsSnglByInvtyEncdCount(Map map);
	
	//�ɹ���ⵥ����ʱ��ѯ���е�����������Ϣ
	List<ToGdsSngl> selectToGdsSnglLists(Map map);
	
	int selectToGdsSnglCounts(Map map);

	Map selectToGdsSnglListSums(Map map);
	
	//�޸ĵ���������״̬
	int updateToGdsSnglDealStatOK(String toGdsSnglId);
	int updateToGdsSnglDealStatNO(String toGdsSnglId);

}