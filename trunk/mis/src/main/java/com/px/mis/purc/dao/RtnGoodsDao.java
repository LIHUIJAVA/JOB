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
    //��������
    int insertRtnGoodsDl(List<String> lists2);
    
    int insertRtnGoodsbyUpload(List<RtnGoods> rtnGoods);

    RtnGoods selectRtnGoodsByRtnGoodsId(String rtnGoodsId);
    
    RtnGoods selectRtnGoodsById(String rtnGoodsId);
   
    int updateRtnGoodsByRtnGoodsId(RtnGoods rtnGoods);
    
	List<RtnGoods> selectRtnGoodsList(Map map);
	//��ҳ+����
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
	
	//�޸Ŀ�Ʊ״̬
	int updateRtnGoodsIsNtBllgOK(String rtnGoodsId);
	
	int updateRtnGoodsIsNtBllgNO(String rtnGoodsId);
	
	//��ѯ�˻�������Ϣ����Ʊ����ʱʹ��
	List<RtnGoods> selectRtnGoodsListToCZ(Map map);
	int selectRtnGoodsListToCZCount(Map map);

	Map selectRtnGoodsListSums(Map map);
}