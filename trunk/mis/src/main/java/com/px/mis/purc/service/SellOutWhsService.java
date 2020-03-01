package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;

public interface SellOutWhsService {

	public String addSellOutWhs(String userId,SellOutWhs sellOutWhs,List<SellOutWhsSub> sellOutWhsSubList,String loginTime);
	
	public String editSellOutWhs(SellOutWhs sellOutWhs,List<SellOutWhsSub> sellOutWhsSubList);
	
	public String deleteSellOutWhs(String outWhsId);
	
	public String querySellOutWhs(String outWhsId);
	
	public String querySellOutWhsList(Map map);
	
	String deleteSellOutWhsList(String outWhsId);
	
	Map<String,Object> updateSellOutWhsIsNtChk(SellOutWhs sellOutWhs) throws Exception;
	
	String printingSellOutWhsList(Map map);
	
	//导入销售出库单
	public String uploadFileAddDb(MultipartFile  file,int i);
	
	//出库明细查询
	String querySellOutWhsByInvtyEncd(Map map);

	String querySellOutWhsListOrderBy(Map map);

	public String querySellOutWhsByInvtyEncdPrint(Map map);

}
