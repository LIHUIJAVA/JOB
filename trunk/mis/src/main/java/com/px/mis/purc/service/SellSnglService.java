package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;

public interface SellSnglService {

	public String addSellSngl(String userId,SellSngl sellSngl,List<SellSnglSub> sellSnglSubList,String loginTime);
	
	public String editSellSngl(SellSngl sellSngl,List<SellSnglSub> sellSnglSubList);
	
	public String deleteSellSngl(String sellSnglId);
	
	public String querySellSngl(String sellSnglId);
	
	public String querySellSnglList(Map map);
	
	String deleteSellSnglList(String sellSnglId);
	
	Map<String,Object> updateSellSnglIsNtChkList (String userId,SellSngl sellSngl,String loginTime) throws Exception;
	
	String printingSellSnglList(Map map);
	
	/**
	 * 销售明细查询
	 * @param map
	 * @return
	 */
	String querySellSnglByInvtyEncd(Map map);

	//导入功能
	public String uploadFileAddDb(MultipartFile file,int i);
	
	String updateA(Map map);
	/*销售统计表查询*/
	String selectXSTJList(Map map);
	
	//退货单参照销售单时查询销售单子表信息
	String selectSellSnglSubByRtnblQty(String sellSnglId);
	
	/*参照时查询主表信息*/
	String querySellSnglLists(Map map);

	String querySellSnglListOrderBy(Map map);
	
}
