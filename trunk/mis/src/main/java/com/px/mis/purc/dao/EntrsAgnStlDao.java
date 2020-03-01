package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.service.impl.EntrsAgnStlServiceImpl;

public interface EntrsAgnStlDao {
	
    int deleteEntrsAgnStlByStlSnglId(String stlSnglId);

    int insertEntrsAgnStl(EntrsAgnStl entrsAgnStl);
    //ɾ��֮ǰ������һ��
    int insertEntrsAgnStlDl(List<String> lists2);

    EntrsAgnStl selectEntrsAgnStlByStlSnglId(String stlSnglId);
    
    EntrsAgnStl selectEntrsAgnStlById(String stlSnglId);

    int updateEntrsAgnStlByStlSnglId(EntrsAgnStl entrsAgnStl);
    
	List<EntrsAgnStl> selectEntrsAgnStlList(Map map);
	
	List<EntrsAgnStlServiceImpl.zizhu> selectEntrsAgnStlListOrderBy(Map map);
	
	int selectEntrsAgnStlCount(Map map);
	
	int deleteEntrsAgnStlList(List<String> stlSnglId);
	
	List<EntrsAgnStl> printingEntrsAgnStlList(Map map);
	
	int updateEntrsAgnStlIsNtChk(EntrsAgnStl entrsAgnStlList);
	
	int selectEntrsAgnStlByIsNtChk(String stlSnglId);
	
	int updateEntrsAgnStlIsNtBllgOK(String stlSnglId);
	
	int updateEntrsAgnStlIsNtBllgNO(String stlSnglId);
	
	//��ѯί�д�������������Ϣ����Ʊ����ʱʹ��
	List<EntrsAgnStl> selectEntrsAgnStlListToCZ(Map map);
	int selectEntrsAgnStlListToCZCount(Map map);
	
	String selectStlSnglId(String stlSnglId);
	
	int insertEntrsAgnStlUpload(EntrsAgnStl entrsAgnStl);

	Map selectEntrsAgnStlListSums(Map map);
	
	
}