package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.PayApplFormSub;

public interface PayApplFormSubDao {
	
	//按照子表主键删除子表信息
    int deleteByPrimaryKey(String payApplId);

    //批量新增子表信息
    int insertPayApplFormSubList(List<PayApplFormSub> payApplFormSub);
    //删除之前，插入备份
    int insertPayApplFormSubDl(List<String> lists2);

    //修改子表信息
    int updateByPrimaryKeySelective(PayApplFormSub payApplFormSub);
    
    //按照子表主键查询子表信息
    PayApplFormSub selectByPrimaryKey(Long ordrNum);
 
    //按照付款申请单编码查询子表信息
    List<PayApplFormSub> selectByPrimaryList(String payApplId);
    
}