package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;
import com.px.mis.purc.entity.PursOrdr;
import com.px.mis.purc.service.impl.PursOrdrServiceImpl.zizhu;

public interface PursOrdrDao {
	
    int deletePursOrdrByPursOrdrId(String pursOrdrId);

    int insertPursOrdr(PursOrdr pursOrdr);
    
    int insertPursOrdrUpload(List<PursOrdr> pursOrdr);//����ʱʹ�õ������ɹ������ӿ�
    
    int updatePursOrdrByPursOrdrId(PursOrdr pursOrdr);

    PursOrdr selectPursOrdrByPursOrdrId(String pursOrdrId);
    
    PursOrdr selectPursOrdrById(String pursOrdrId);
    
	List<PursOrdr> selectPursOrdrList(Map map);
	//��ҳ+����
	List<zizhu> selectPursOrdrListOrderBy(Map map);
	
	int selectPursOrdrCount(Map map);
	
	//����ɾ���ɹ�����
	int deletePursOrdrList(List<String> pursOrdrId);
	
	//�����������״̬
	int  updatePursOrdrIsNtChkList(List<PursOrdr> pursOrdr);
	//�����޸����״̬
	int  updatePursOrdrIsNtChk(PursOrdr pursOrdr);
	//��ѯ���״̬
    int selectPursOrdrIsNtChk(String pursOrdrId);
		
	List<zizhu> printingPursOrdrList(Map map);
	
	//�ɹ���ϸ��
	List<Map> selectPursOrdrByInvtyEncd(Map map);
	
	int selectPursOrdrByInvtyEncdCount(Map map);
	
	//�������뵥����ʱ������ѯ�ɹ�����������Ϣ
	List<PursOrdr> selectPursOrdrLists(Map map);
	
	int selectPursOrdrCounts(Map map);
	
	int insertPursOrdrDl(List<String> lists2);

	Map<String, String> selectTableSum(Map map);

	Map selectPursOrdrListSum(Map map);//�ɹ������б��ܼ�

	Map selectPursOrdrByInvtyEncdSums(Map map);//��ϸ���ܼ�
	
	//����������ʱ������ѯ�ɹ�����������Ϣ
	List<PursOrdr> selectPursOrdrListByToGdsSngl(Map map);
	
	int selectPursOrdrCountByToGdsSngl(Map map);
	//�ɹ���ϸ��--����
	List<Map> selectPursOrdrByInvtyEncdPrint(Map map);
	
	
	
	
	
	
}