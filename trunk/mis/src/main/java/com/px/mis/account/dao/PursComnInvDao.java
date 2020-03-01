package com.px.mis.account.dao;

import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.service.impl.PursComnInvServiceImpl.zizhu;

import java.util.List;
import java.util.Map;

public interface PursComnInvDao{
	
	//新增采购普通发票主表
	int insertPursComnInv(PursComnInv pursComnInv);
	
	//修改采购普通发票主表
	int updatePursComnInvById(PursComnInv pursComnInv);
	
	//删除采购普通发票主表
	int deletePursComnInvById(String pursInvNum);
	
	//按照发票号查询采购普通发票主表
	PursComnInv selectPursComnInvById(String pursInvNum);
	
	//按照发票号查询采购普通发票主表单号
	String selectPursComnInvByIds(String pursInvNum);

	//分页查询采购普通发票主表
    List<PursComnInv> selectPursComnInvList(Map map);
    
    //查询采购普通发票主表总条数
    int selectPursComnInvCount(Map map);
    
    //批量删除采购普通发票主表
    int deletePursComnInvList(List<String> pursInvNum);
    
    //查询采购普通发票审核状态
    Integer selectPursComnInvIsNtChk(String pursInvNum);
    
    //查询采购普通发票记账状态
    Integer selectPursComnInvIsNtBookEntry(String pursInvNum);
    
    //修改采购普通发票的审核状态
    int updatePursComnInvIsNtChk(PursComnInv pursComnInv);
    
    //查询采购普通发票的采购入库单号
    String selectIntoWhsSnglIdByPursComnInv(String pursInvNum);
    
    //导入时新增采购普通发票主表
    int insertPursComnInvUpload(List<PursComnInv> pursComnInv);
    
    //原来的导出接口,不敢删
    List<PursComnInv> printingPursComnInvList(Map map);
    //导出接口,中文Key
    List<zizhu> printPursComnInvList(Map map);
    //按发票号liststr,批量查询发票
    List<PursComnInv> selectComnInvs(List<String> idList);
    //推送U8状态回写
	int updatePushed(String dscode);
}
