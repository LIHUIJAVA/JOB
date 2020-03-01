package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.ToGdsSnglSub;

public interface ToGdsSnglSubDao {

	
    int deleteToGdsSnglSubByToGdsSnglId(String toGdsSnglId);

    int insertToGdsSnglSub(List<ToGdsSnglSub> toGdsSnglSubList);
    //删除时候，备份到货单子表
    int insertToGdsSnglSubDl(List<String> lists2);

    List<ToGdsSnglSub> selectToGdsSnglSubByToGdsSnglId(String toGdsSnglId);

    BigDecimal selectUnIntoWhsQtyByInvWhsBat(Map map);//根据存货编码、仓库、批号查询子表未入库数量
    
    //入库单参照时查询未到货的到货单子表明细
    List<ToGdsSnglSub> selectUnIntoWhsQtyByByToGdsSnglId(List<String> toGdsSnglId);
    
    int updateToGdsSnglSubByInvWhsBat(Map map);//修改未入库数量
    
    BigDecimal selectUnReturnQtyByOrdrNum(Map map);//根据存货编码、仓库、批号查询子表未拒收数量
    
    //到货拒收单参照时查询未拒收的到货单子表明细
    List<ToGdsSnglSub> selectUnReturnQtyByToGdsSnglId(List<String> toGdsSnglId);
    
    int updateToGdsSnglSubUnReturnQty(Map map);//修改未拒收数量
    
    
}