package com.px.mis.purc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhsSub;

public interface IntoWhsService {

	//新增采购入库单
	public String addIntoWhs(String userId, IntoWhs intoWhs,List<IntoWhsSub> intoWhsSubList,String loginTime) throws Exception;
	
	//修改采购入库单
	public String editIntoWhs(IntoWhs intoWhs,List<IntoWhsSub> intoWhsSubList) throws Exception;
	
	//单个删除采购入库单
	public String deleteIntoWhs(String intoWhsSnglId);
	
	//单个查询采购入库单
	public String queryIntoWhs(String intoWhsSnglId);
	
	//分页查询采购入库单
	public String queryIntoWhsList(Map map);

	//删除采购入库单
	String deleteIntoWhsList(String intoWhsSnglId);
	
	//采购入库单审核
	Map<String,Object> updateIntoWhsIsNtChk(IntoWhs intoWhs) throws Exception;
	
	//打印采购入库单
	String printingIntoWhsList(Map map);
	
	//导入到货单
	public String uploadFileAddDb(MultipartFile  file,int i);
	
	//入库明细表
	String queryIntoWhsByInvtyEncd(Map map);
	
	//采购订收货报表查询
	String selectIntoWhsAndPursOrdr(Map map);
	
	//采购发票参照时查询采购入库单子表信息
	public String queryIntoWhsByIntoWhsIds(String intoWhsSnglId);
	
	//参照时查询采购入库单主表信息
	public String queryIntoWhsLists(Map map);
	
	//采购退货单参照时查询采购入库单子表信息
    public String selectIntoWhsSubByUnReturnQty(String intoWhsSnglId);

	String queryIntoWhsListOrderBy(Map map);
	//入库明细表-导出
	public String queryIntoWhsByInvtyEncdPrint(Map map);
	
}
