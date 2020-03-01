package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnDelvSub;

public interface EntrsAgnDelvSubDao {
	
    int deleteEntrsAgnDelvSubByDelvSnglId(String delvSnglId);

    int insertEntrsAgnDelvSub(List<EntrsAgnDelvSub> entrsAgnDelvSubList);
   //ɾ��ǰ������һ��
    int insertEntrsAgnDelvSubDl(List<String> lists2);

    List<EntrsAgnDelvSub> selectEntrsAgnDelvSubByDelvSnglId(String delvSnglId);
    
    //ί�д������������ʱ����ί�д������������롢������롢�ֿ���롢���β�ѯ��Ӧ������ 
    BigDecimal selectEntrsAgnDelvSubQty(EntrsAgnDelvSub entrsAgnDelvSub);
    
    //���ί�д������㵥ʱ�޸�ί�д����������Ľ����������� 
    int updateEntrsAgnDelvSubByEntrsAgnStlSubJia(Map map);
    //����ί�д������㵥ʱ�޸�ί�д����������Ľ����������� 
    int updateEntrsAgnDelvSubByEntrsAgnStlSubJian(Map map);
    
    //����ί�д������������롢������롢�ֿ���롢���β�ѯ��Ӧ��ί�д����������ӱ� 
    List<EntrsAgnDelvSub> selectEntrsAgnDelvSubByInvBatWhs(Map map);
    
    //ί�д����˻�������ʱ����ί�д����������ӱ���Ϣ
    List<EntrsAgnDelvSub> selectEntDeSubUnBllgRtnGoodsQty(List<String> delvSnglId);
    
    int updateEntDeSubUnBllgRtnGoodsQty(Map map);
    
    EntrsAgnDelvSub selectEntDeSubToOrdrNum(Long ordrNum);

	List<String> selectEntrsAgnDelvSubNums(String sellOrdrId);
    
    
}