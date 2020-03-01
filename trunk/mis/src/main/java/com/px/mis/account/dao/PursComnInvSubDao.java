package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursComnInvSub;

public interface PursComnInvSubDao{
	
	//新增采购普通发票子表
	int insertPursComnInvSub(PursComnInvSub pursComnInvSub);
	
	//修改采购普通发票子表
	int updatePursComnInvSubByOrdrNum(PursComnInvSub pursComnInvSub);
	
	//删除采购普通发票子表
	int deletePursComnInvSubByOrdrNum(String pursInvNum);
	
	//按照序号查询采购普通发票子表信息
	PursComnInvSub selectPursComnInvSubByOrdrNum(Integer ordrNum);
	
    List<PursComnInvSub> selectPursComnInvSubList(Map map);
    
    int selectPursComnInvSubCount();
    
    //导入时新增采购普通发票子表
  	int insertPursComnInvSubUpload(List<PursComnInvSub> pursComnInvSub);
  	
    
}
