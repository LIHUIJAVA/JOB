package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.PursOrdrSub;

public interface PursOrdrSubDao {
	
    int deletePursOrdrSubByPursOrdrId(String pursOrdrId);

    int insertPursOrdrSub(List<PursOrdrSub> pursOrdrSubList);

    //���ݲɹ����������ѯ�ɹ������ӱ���Ϣ
    List<PursOrdrSub> selectPursOrdrSubByPursOrdrId(String pursOrdrId);
    //���ݲɹ����������ѯ�ɹ������ӱ���ż���
    List<Long> selectPursOrdrSubIdsByPursOrdrId(String pursOrdrId);
    
    //��ѯδ��������
    BigDecimal selectUnToGdsQtyByInvtyEncd(Map map);
    
    //�޸�δ��������
    int updatePursOrdrSubByInvtyEncd(Map map);
    
    //�������뵥����ʱ��ѯ����δ���븶�������Ĳɹ������ӱ���Ϣ
    List<PursOrdrSub> selectUnApplPayQtyByPursOrdrId(List<String> pursOrdrId);
    
    //��ѯδ���븶������
    PursOrdrSub selectUnApplPayQtyByOrdrNum(Map map);
    //�޸�δ���븶������
    int updateUnApplPayQtyByOrdrNum(Map map);
    
    //����������ʱ��ѯ����δ���������Ĳɹ������ӱ���Ϣ
    List<PursOrdrSub> selectUnToGdsQtyByPursOrdrId(List<String> pursOrdrId);

	int insertPursOrdrSubDl(List<String> lists2);
	
	//���ݲɹ������ӱ���Ų�ɹ������������ɹ����ʱʹ�� 
	BigDecimal selectQtyByOrdrNum(Map map);
	
	int updatePursOrdrSubUnToGdsQty(Map map);

   
}