package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ToGdsSnglSub;

public interface ToGdsSnglSubDao {

	
    int deleteToGdsSnglSubByToGdsSnglId(String toGdsSnglId);

    int insertToGdsSnglSub(List<ToGdsSnglSub> toGdsSnglSubList);
    //ɾ��ʱ�򣬱��ݵ������ӱ�
    int insertToGdsSnglSubDl(List<String> lists2);

    List<ToGdsSnglSub> selectToGdsSnglSubByToGdsSnglId(String toGdsSnglId);

    BigDecimal selectUnIntoWhsQtyByInvWhsBat(Map map);//���ݴ�����롢�ֿ⡢���Ų�ѯ�ӱ�δ�������
    
    //��ⵥ����ʱ��ѯδ�����ĵ������ӱ���ϸ
    List<ToGdsSnglSub> selectUnIntoWhsQtyByByToGdsSnglId(List<String> toGdsSnglId);
    
    int updateToGdsSnglSubByInvWhsBat(Map map);//�޸�δ�������
    
    BigDecimal selectUnReturnQtyByOrdrNum(Map map);//���ݴ�����롢�ֿ⡢���Ų�ѯ�ӱ�δ��������
    
    //�������յ�����ʱ��ѯδ���յĵ������ӱ���ϸ
    List<ToGdsSnglSub> selectUnReturnQtyByToGdsSnglId(List<String> toGdsSnglId);
    
    int updateToGdsSnglSubUnReturnQty(Map map);//�޸�δ��������
    
    
}