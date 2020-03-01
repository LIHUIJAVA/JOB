package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.PursOrdrSub;

public interface PursOrdrSubDao {
	
    int deletePursOrdrSubByPursOrdrId(String pursOrdrId);

    int insertPursOrdrSub(List<PursOrdrSub> pursOrdrSubList);

    //根据采购订单编码查询采购订单子表信息
    List<PursOrdrSub> selectPursOrdrSubByPursOrdrId(String pursOrdrId);
    //根据采购订单编码查询采购订单子表序号集合
    List<Long> selectPursOrdrSubIdsByPursOrdrId(String pursOrdrId);
    
    //查询未到货数量
    BigDecimal selectUnToGdsQtyByInvtyEncd(Map map);
    
    //修改未到货数量
    int updatePursOrdrSubByInvtyEncd(Map map);
    
    //付款申请单参照时查询所有未申请付款数量的采购订单子表信息
    List<PursOrdrSub> selectUnApplPayQtyByPursOrdrId(List<String> pursOrdrId);
    
    //查询未申请付款数量
    PursOrdrSub selectUnApplPayQtyByOrdrNum(Map map);
    //修改未申请付款数量
    int updateUnApplPayQtyByOrdrNum(Map map);
    
    //到货单参照时查询所有未到货数量的采购订单子表信息
    List<PursOrdrSub> selectUnToGdsQtyByPursOrdrId(List<String> pursOrdrId);

	int insertPursOrdrSubDl(List<String> lists2);
	
	//根据采购订单子表序号查采购订单数量，采购入库时使用 
	BigDecimal selectQtyByOrdrNum(Map map);
	
	int updatePursOrdrSubUnToGdsQty(Map map);

   
}