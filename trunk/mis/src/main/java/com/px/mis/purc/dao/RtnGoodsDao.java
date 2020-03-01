package com.px.mis.purc.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.service.impl.RtnGoodsServiceImpl;
import com.px.mis.purc.service.impl.RtnGoodsServiceImpl.zizhu;

public interface RtnGoodsDao {
	
    int deleteRtnGoodsByRtnGoodsId(String rtnGoodsId);

    int insertRtnGoods(RtnGoods rtnGoods);
    //废弃备份
    int insertRtnGoodsDl(List<String> lists2);
    
    int insertRtnGoodsbyUpload(List<RtnGoods> rtnGoods);

    RtnGoods selectRtnGoodsByRtnGoodsId(String rtnGoodsId);
    
    RtnGoods selectRtnGoodsById(String rtnGoodsId);
   
    int updateRtnGoodsByRtnGoodsId(RtnGoods rtnGoods);
    
	List<RtnGoods> selectRtnGoodsList(Map map);
	//分页+排序
	List<RtnGoodsServiceImpl.zizhu> selectRtnGoodsListOrderBy(Map map);
	
	int selectRtnGoodsCount(Map map);
	
	int deleteRtnGoodsList(List<String> rtnGoodsId);
	
	List<zizhu> printingRtnGoodsList(Map map);
	//========================zds create=================================
	RtnGoods selectRtnGoodsBySellSnglId(String sellOrderId);
	RtnGoods selectRtnGoodsByRefId(String refId);
	
	int updateRtnGoodsIsNtChk(RtnGoods rtnGoods);
	
	int updateRtnGoodsIsNtChkList(List<RtnGoods> rtnGoods);
	
	int selectRtnGoodsIsNtChk(String rtnGoodsId);
	
	//修改开票状态
	int updateRtnGoodsIsNtBllgOK(String rtnGoodsId);
	
	int updateRtnGoodsIsNtBllgNO(String rtnGoodsId);
	
	//查询退货主表信息，开票参照时使用
	List<RtnGoods> selectRtnGoodsListToCZ(Map map);
	int selectRtnGoodsListToCZCount(Map map);

	Map selectRtnGoodsListSums(Map map);
}