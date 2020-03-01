package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;

public interface ToGdsSnglService {
	
	public String addToGdsSngl(String userId, ToGdsSngl toGdsSngl,List<ToGdsSnglSub> toGdsSnglSubList,String loginTime) throws Exception;
	
	public String editToGdsSngl(ToGdsSngl toGdsSngl,List<ToGdsSnglSub> toGdsSnglSubList) throws Exception;
	
	public String deleteToGdsSngl(String toGdsSnglId);
	
	public String queryToGdsSngl(String toGdsSnglId);
	
	public String queryToGdsSnglList(Map map);
	
	String deleteToGdsSnglList(String toGdsSnglId);
	
	Map<String,Object> updateToGdsSnglIsNtChkList(ToGdsSngl toGdsSngl) throws Exception;
	
	String printingToGdsSnglList(Map map);
	
	//导入到货单
	public String uploadFileAddDb(MultipartFile  file,int i);
	
	//到货明细
	public String queryToGdsSnglByInvtyEncd(Map map);
	
	//参照时查询所有未到货数量的采购订单子表信息
    public String selectUnIntoWhsQtyByByToGdsSnglId(String toGdsSnglId);
    //参照时查询主表信息
    public String queryToGdsSnglLists(Map map);
    
    //参照时查询所有未拒收数量的采购订单子表信息
    public String selectUnReturnQtyByToGdsSnglId(String toGdsSnglId);

	public String queryToGdsSnglListOrderBy(Map map);
	
	public String updateToGdsSnglDealStat(String toGdsSnglId,int flat);
}

