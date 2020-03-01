package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnStl;
import com.px.mis.purc.service.impl.EntrsAgnStlServiceImpl;

public interface EntrsAgnStlDao {
	
    int deleteEntrsAgnStlByStlSnglId(String stlSnglId);

    int insertEntrsAgnStl(EntrsAgnStl entrsAgnStl);
    //删除之前，备份一份
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
	
	//查询委托代销发货主表信息，开票参照时使用
	List<EntrsAgnStl> selectEntrsAgnStlListToCZ(Map map);
	int selectEntrsAgnStlListToCZCount(Map map);
	
	String selectStlSnglId(String stlSnglId);
	
	int insertEntrsAgnStlUpload(EntrsAgnStl entrsAgnStl);

	Map selectEntrsAgnStlListSums(Map map);
	
	
}