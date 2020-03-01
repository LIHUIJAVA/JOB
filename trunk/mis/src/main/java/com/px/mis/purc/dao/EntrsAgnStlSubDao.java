package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnStlSub;

public interface EntrsAgnStlSubDao {
	
    int deleteEntrsAgnStlSubByStlSnglId(String stlSnglId);

    int insertEntrsAgnStlSub(List<EntrsAgnStlSub> entrsAgnStlSubList);
    //删除之前，备份一份
    int insertEntrsAgnStlSubDl(List<String> lists2);

    List<EntrsAgnStlSub> selectEntrsAgnStlSubByStlSnglId(String stlSnglId);
    
   //根据子表序号修改委托代销结算单未开票数量
    int updateEntrsAgnStlUnBllgQtyByOrdrNum(Map map);
    
    //根据子表序号查询委托代销结算单未开票数量
    BigDecimal selectEntrsAgnStlUnBllgQtyByOrdrNum(Map map);

    //根据委托代销发货单子表序号查询委托代销子表信息,参照带数据时使用
    EntrsAgnStlSub selectEntrsAgnStlByStlSnIdAndOrdrNum(Map map);
    //参照时根据委托代销发货单号及子表未开票数量批量查询子表信息
    List<EntrsAgnStlSub> selectEntAgStSubByStlIdAndUnBllgQty(String stlSnglId);
}