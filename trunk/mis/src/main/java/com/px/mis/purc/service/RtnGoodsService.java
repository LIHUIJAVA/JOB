package com.px.mis.purc.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;

public interface RtnGoodsService {

	public String addRtnGoods(String userId,RtnGoods rtnGoods,List<RtnGoodsSub> rtnGoodsSubList,String loginTime);
	
	public String editRtnGoods(RtnGoods rtnGoods,List<RtnGoodsSub> rtnGoodsSubList);
	
	public String deleteRtnGoods(String rtnGoodsId);
	
	public String queryRtnGoods(String rtnGoodsId);
	
	public String queryRtnGoodsList(Map map);
	
	String deleteRtnGoodsList(String rtnGoodsId);
	
	String printingRtnGoodsList(Map map);
	
	Map<String,Object> updateRtnGoodsIsNtChksList(String userId,RtnGoods rtnGoods,String loginTime) throws Exception;
	
	//导入退货单
	public String uploadFileAddDb(MultipartFile  file,int i);

	String queryRtnGoodsListOrderBy(Map map);
	
	/*public String chkRtnGoods(String userId,RtnGoods rtnGoods,List<RtnGoodsSub> rtnGoodsSubList);*/

}
