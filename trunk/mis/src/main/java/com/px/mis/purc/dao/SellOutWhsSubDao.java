package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;

import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.whs.entity.InvtyTab;

public interface SellOutWhsSubDao {
	
    int deleteSellOutWhsSubByOutWhsId(String outWhsId);

    int insertSellOutWhsSub(List<SellOutWhsSub> sellOutWhsSubList);
    //�������۳��ⵥ
    int insertSellOutWhsSubUpload(List<SellOutWhsSub> sellOutWhsSubList);
    //ɾ��֮ǰ������һ�ݷ�����
    int insertSellOutWhsSubDl(List<String> lists2);
    
    List<SellOutWhsSub> selectSellOutWhsSubByOutWhsId(String outWhsId);
    
    BigDecimal selectInvtyTabByDetailed(InvtyTab invtyTab);
    
}