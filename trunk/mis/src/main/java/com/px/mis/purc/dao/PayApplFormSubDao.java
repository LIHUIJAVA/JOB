package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.PayApplFormSub;

public interface PayApplFormSubDao {
	
	//�����ӱ�����ɾ���ӱ���Ϣ
    int deleteByPrimaryKey(String payApplId);

    //���������ӱ���Ϣ
    int insertPayApplFormSubList(List<PayApplFormSub> payApplFormSub);
    //ɾ��֮ǰ�����뱸��
    int insertPayApplFormSubDl(List<String> lists2);

    //�޸��ӱ���Ϣ
    int updateByPrimaryKeySelective(PayApplFormSub payApplFormSub);
    
    //�����ӱ�������ѯ�ӱ���Ϣ
    PayApplFormSub selectByPrimaryKey(Long ordrNum);
 
    //���ո������뵥�����ѯ�ӱ���Ϣ
    List<PayApplFormSub> selectByPrimaryList(String payApplId);
    
}