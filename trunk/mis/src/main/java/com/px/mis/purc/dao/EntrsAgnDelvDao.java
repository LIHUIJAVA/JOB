package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.service.impl.EntrsAgnDelvServiceImpl;
import com.px.mis.purc.service.impl.EntrsAgnDelvServiceImpl.zizhu;

public interface EntrsAgnDelvDao {
	
    int deleteEntrsAgnDelvByDelvSnglId(String delvSnglId);

    int insertEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv);
    //删除前，备份一份
    int insertEntrsAgnDelvDl(List<String> lists2);
    
    int insertEntrsAgnDelvUpload(EntrsAgnDelv entrsAgnDelv);

    EntrsAgnDelv selectEntrsAgnDelvByDelvSnglId(String delvSnglId);
    
    EntrsAgnDelv selectEntrsAgnDelvById(String delvSnglId);

    int updateEntrsAgnDelvByDelvSnglId(EntrsAgnDelv entrsAgnDelv);
    
	List<EntrsAgnDelv> selectEntrsAgnDelvList(Map map);
	//分页带排序
	List<EntrsAgnDelvServiceImpl.zizhu> selectEntrsAgnDelvListOrderBy(Map map);
	
	int selectEntrsAgnDelvCount(Map map);
	
	int deleteEntrsAgnDelvList(List<String> delvSnglId);
	
	List<zizhu> printingEntrsAgnDelvList(Map map);
	
	List<EntrsAgnDelv> selectEntrsAgnDelvByEntrsAgnAdjList(Map map);
	
	//审核委托代销发货单
	int updateEntrsAgnDelvIsNtChk(EntrsAgnDelv entrsAgnDelv);
	//查询审核状态
	int selectEntrsAgnDelvIsNtChk(String delvSnglId);
	//查看结算状态
	int selectEntrsAgnDelvIsNtStl(String delvSnglId);
	//查看退货状态
	int selectEntrsAgnDelvIsNtRtnGood(String delvSnglId);
	//查询拣货状态
	int selectEntrsAgnDelvIsPick(String delvSnglId);
	//修改委托代销发货单的结算状态为1
	int updateEntrsAgnDelvToIsNtStlOK(String delvSnglId);
	//修改委托代销发货单的结算状态为0
    int updateEntrsAgnDelvToIsNtStlNO(String delvSnglId);
    
    //参照时查询主表信息
    List<EntrsAgnDelv> selectEntrsAgnDelvLists(Map map);
    int selectEntrsAgnDelvCounts(Map map);
    
    //导入时新增委托代销结算单
    int insertEntrsAgnStlUpload(EntrsAgnDelv entrsAgnDelv);

	Map selectEntrsAgnDelvListSums(Map map);
    
    
    
    
    
}