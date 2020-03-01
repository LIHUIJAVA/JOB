package com.px.mis.account.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.px.mis.account.entity.PursComnInv;
import com.px.mis.account.entity.PursComnInvSub;
import com.px.mis.account.entity.PursComnInvForU8.U8PursComnInv;
import com.px.mis.purc.entity.IntoWhs;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface PursComnInvService {

	// 新增采购普通发票
	String addPursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList, String userId,
			String loginTime);

	// 修改采购普通发票
	String updatePursComnInv(PursComnInv pursComnInv, List<PursComnInvSub> pursComnInvSubList);

	// 删除采购普通发票
	String deletePursComnInv(String pursInvNum);

	// 查询采购普通发票
	String queryPursComnInvByPursInvNum(String pursInvNum);

	// 分页查询采购普通发票
	String queryPursComnInvList(Map map);

	// 批量删除采购普通发票
	String deletePursComnInvList(String pursInvNum);

	// 审核采购普通发票
	Map<String, Object> updatePursComnInvIsNtChkList(PursComnInv pursComnInv) throws Exception;

	// 采购发票新增时，参照采购入库单并带入多张采购入库单
	String selectPursComnInvBingList(List<IntoWhs> intoWhsList);

	// 导入销售出库单
	public String uploadFileAddDb(MultipartFile file, int i);

	// 导出时，使用的查询接口
	String printPursComnInvList(Map map);

	// 原来的导出接口,不敢删
	String upLoadPursComnInvList(Map map);

	// 推送到U8
	U8PursComnInv encapsulation(PursComnInv pursComnInv, List<PursComnInvSub> list) throws Exception;

	// 批量查询->推送到U8
	String pushToU8(String ids) throws Exception;

	



	

}
