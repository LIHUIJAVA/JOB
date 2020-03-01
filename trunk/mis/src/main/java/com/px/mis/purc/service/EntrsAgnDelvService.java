package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;

public interface EntrsAgnDelvService {

	public String addEntrsAgnDelv(String userId,EntrsAgnDelv entrsAgnDelv,List<EntrsAgnDelvSub> entrsAgnDelvSubList,String loginTime);
	
	public String editEntrsAgnDelv(EntrsAgnDelv entrsAgnDelv,List<EntrsAgnDelvSub> entrsAgnDelvSubList);
	
	public String deleteEntrsAgnDelv(String delvSnglId);
	
	public String queryEntrsAgnDelv(String delvSnglId);
	
	public String queryEntrsAgnDelvList(Map map);
	
	String deleteEntrsAgnDelvList(String delvSnglId);
	
	String printingEntrsAgnDelvList(Map map);
	
	Map<String,Object> updateEntrsAgnDelvIsNtChkList (String userId,EntrsAgnDelv entrsAgnDelv,String loginTime) throws Exception;
	
	//导入委托代销发货单
	public String uploadFileAddDb(MultipartFile  file);
	
	//参照时查询委托代销主表信息
	String queryEntrsAgnDelvLists(Map map);
	
	//委托代销退货单参照时根据委托代销发货单子表信息
    public String selectEntDeSubUnBllgRtnGoodsQty(String delvSnglId);
    
    public String uploadFileAddDb(MultipartFile file,int i);

	String queryEntrsAgnDelvListOrderBy(Map map);

}
