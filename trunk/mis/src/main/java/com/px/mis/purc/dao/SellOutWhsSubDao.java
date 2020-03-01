package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.whs.entity.InvtyTab;

public interface SellOutWhsSubDao {
	
    int deleteSellOutWhsSubByOutWhsId(String outWhsId);

    int insertSellOutWhsSub(List<SellOutWhsSub> sellOutWhsSubList);
    //导入销售出库单
    int insertSellOutWhsSubUpload(List<SellOutWhsSub> sellOutWhsSubList);
    //删除之前，备份一份废弃表
    int insertSellOutWhsSubDl(List<String> lists2);
    
    List<SellOutWhsSub> selectSellOutWhsSubByOutWhsId(String outWhsId);
    
    BigDecimal selectInvtyTabByDetailed(InvtyTab invtyTab);
    
}