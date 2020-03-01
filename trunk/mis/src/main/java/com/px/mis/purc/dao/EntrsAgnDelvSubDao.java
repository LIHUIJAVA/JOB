package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.EntrsAgnDelvSub;

public interface EntrsAgnDelvSubDao {
	
    int deleteEntrsAgnDelvSubByDelvSnglId(String delvSnglId);

    int insertEntrsAgnDelvSub(List<EntrsAgnDelvSub> entrsAgnDelvSubList);
   //删除前，备份一份
    int insertEntrsAgnDelvSubDl(List<String> lists2);

    List<EntrsAgnDelvSub> selectEntrsAgnDelvSubByDelvSnglId(String delvSnglId);
    
    //委托代销发货单审核时根据委托代销发货单编码、存货编码、仓库编码、批次查询对应的数量 
    BigDecimal selectEntrsAgnDelvSubQty(EntrsAgnDelvSub entrsAgnDelvSub);
    
    //审核委托代销结算单时修改委托代销发货单的金额、数量、单价 
    int updateEntrsAgnDelvSubByEntrsAgnStlSubJia(Map map);
    //弃审委托代销结算单时修改委托代销发货单的金额、数量、单价 
    int updateEntrsAgnDelvSubByEntrsAgnStlSubJian(Map map);
    
    //根据委托代销发货单编码、存货编码、仓库编码、批次查询对应的委托代销发货单子表 
    List<EntrsAgnDelvSub> selectEntrsAgnDelvSubByInvBatWhs(Map map);
    
    //委托代销退货单参照时根据委托代销发货单子表信息
    List<EntrsAgnDelvSub> selectEntDeSubUnBllgRtnGoodsQty(List<String> delvSnglId);
    
    int updateEntDeSubUnBllgRtnGoodsQty(Map map);
    
    EntrsAgnDelvSub selectEntDeSubToOrdrNum(Long ordrNum);

	List<String> selectEntrsAgnDelvSubNums(String sellOrdrId);
    
    
}