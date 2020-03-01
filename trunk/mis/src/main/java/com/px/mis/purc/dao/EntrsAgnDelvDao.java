package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.service.impl.EntrsAgnDelvServiceImpl;
import com.px.mis.purc.service.impl.EntrsAgnDelvServiceImpl.zizhu;

public interface EntrsAgnDelvDao {
	
    int deleteEntrsAgnDelvByDelvSnglId(String delvSnglId);

    int insertEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv);
    //ɾ��ǰ������һ��
    int insertEntrsAgnDelvDl(List<String> lists2);
    
    int insertEntrsAgnDelvUpload(EntrsAgnDelv entrsAgnDelv);

    EntrsAgnDelv selectEntrsAgnDelvByDelvSnglId(String delvSnglId);
    
    EntrsAgnDelv selectEntrsAgnDelvById(String delvSnglId);

    int updateEntrsAgnDelvByDelvSnglId(EntrsAgnDelv entrsAgnDelv);
    
	List<EntrsAgnDelv> selectEntrsAgnDelvList(Map map);
	//��ҳ������
	List<EntrsAgnDelvServiceImpl.zizhu> selectEntrsAgnDelvListOrderBy(Map map);
	
	int selectEntrsAgnDelvCount(Map map);
	
	int deleteEntrsAgnDelvList(List<String> delvSnglId);
	
	List<zizhu> printingEntrsAgnDelvList(Map map);
	
	List<EntrsAgnDelv> selectEntrsAgnDelvByEntrsAgnAdjList(Map map);
	
	//���ί�д���������
	int updateEntrsAgnDelvIsNtChk(EntrsAgnDelv entrsAgnDelv);
	//��ѯ���״̬
	int selectEntrsAgnDelvIsNtChk(String delvSnglId);
	//�鿴����״̬
	int selectEntrsAgnDelvIsNtStl(String delvSnglId);
	//�鿴�˻�״̬
	int selectEntrsAgnDelvIsNtRtnGood(String delvSnglId);
	//��ѯ���״̬
	int selectEntrsAgnDelvIsPick(String delvSnglId);
	//�޸�ί�д����������Ľ���״̬Ϊ1
	int updateEntrsAgnDelvToIsNtStlOK(String delvSnglId);
	//�޸�ί�д����������Ľ���״̬Ϊ0
    int updateEntrsAgnDelvToIsNtStlNO(String delvSnglId);
    
    //����ʱ��ѯ������Ϣ
    List<EntrsAgnDelv> selectEntrsAgnDelvLists(Map map);
    int selectEntrsAgnDelvCounts(Map map);
    
    //����ʱ����ί�д������㵥
    int insertEntrsAgnStlUpload(EntrsAgnDelv entrsAgnDelv);

	Map selectEntrsAgnDelvListSums(Map map);
    
    
    
    
    
}