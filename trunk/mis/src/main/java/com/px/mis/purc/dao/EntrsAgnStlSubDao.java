package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnStlSub;

public interface EntrsAgnStlSubDao {
	
    int deleteEntrsAgnStlSubByStlSnglId(String stlSnglId);

    int insertEntrsAgnStlSub(List<EntrsAgnStlSub> entrsAgnStlSubList);
    //ɾ��֮ǰ������һ��
    int insertEntrsAgnStlSubDl(List<String> lists2);

    List<EntrsAgnStlSub> selectEntrsAgnStlSubByStlSnglId(String stlSnglId);
    
   //�����ӱ�����޸�ί�д������㵥δ��Ʊ����
    int updateEntrsAgnStlUnBllgQtyByOrdrNum(Map map);
    
    //�����ӱ���Ų�ѯί�д������㵥δ��Ʊ����
    BigDecimal selectEntrsAgnStlUnBllgQtyByOrdrNum(Map map);

    //����ί�д����������ӱ���Ų�ѯί�д����ӱ���Ϣ,���մ�����ʱʹ��
    EntrsAgnStlSub selectEntrsAgnStlByStlSnIdAndOrdrNum(Map map);
    //����ʱ����ί�д����������ż��ӱ�δ��Ʊ����������ѯ�ӱ���Ϣ
    List<EntrsAgnStlSub> selectEntAgStSubByStlIdAndUnBllgQty(String stlSnglId);
}