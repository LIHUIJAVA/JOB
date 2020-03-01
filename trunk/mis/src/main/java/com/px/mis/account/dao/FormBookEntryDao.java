package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.purc.entity.IntoWhs;

public interface FormBookEntryDao {
	//根据单据号查找单据
	FormBookEntry selectByFormNum(String formNum);
	//批量插入
	int insertFormList(List<FormBookEntry> list);
	//查询单据
	List<FormBookEntry> selectMap(Map<String,Object> map);
	//批量修改单据
	int updateIsNtBook(List<FormBookEntry> list);
	//批量修改单据
	int delectFormBookList(List<FormBookEntry> formList);
	
	int insertForm(FormBookEntry form);
	//批量修改已生成凭证状态
	int updateFormVouch(List<FormBookEntry> list);
	//生成凭证-组合单据
	void updateGruopFormVouch(List<FormBookEntry> formBookList);
	
	//查询流水帐
	List<FormBookEntry> selectStreamMap(Map map);
	
	int countSelectStreamMap(Map map);
	
	List<FormBookEntry> selectMapAndPage(Map map);
	List<FormBookEntry> selectFormNoVoucherGeneratedList(Map map);
	int countFormNoVoucherGeneratedList(Map map);
	
	//合并制单
	List<FormBookEntry> selectMergeMapAndPage(Map map);
	
	int countSelectMapAndPage(Map map);
	//sum采购/其他出入库数量和金额
	FormBookEntrySub sumFormBookQtyAndAmt(Map map);
	
	//生成凭证-选单-未生成凭证列表-销售单
	List<FormBookEntry> selectSellSnglNoVoucherGeneratedList(Map map);
	int countSellSnglNoVoucherGeneratedList(Map map);
	void updateSellSnglFormVouch(List<FormBookEntry> sellSnglList);
	
	//生成凭证-选单-未生成凭证列表-采购发票
	List<FormBookEntry> selectPursInvMasTabNoVoucherGeneratedList(Map map);
	int countPursInvMasTabNoVoucherGeneratedList(Map map);
	void updatePursInvMasFormVouch(List<FormBookEntry> pursInvMasList);
	
	//生成凭证-选单-未生成凭证列表-销售发票(专用/普通)
	List<FormBookEntry> selectSellInvMasTabNoVoucherGeneratedList(Map map);
	int countSellInvMasTabNoVoucherGeneratedList(Map map);
	void updateSellInvMasFormVouch(List<FormBookEntry> sellInvMasList);
	
	//生成凭证-选单-未生成凭证列表-出入库调整单
	List<FormBookEntry> selectOutIntoWhsAdjSnglNoVoucherGeneratedList(Map map);
	int countOutIntoWhsAdjSnglNoVoucherGeneratedList(Map map);
	void updateIntoWhsAdjFormVouch(List<FormBookEntry> intoWhsAdjSnglList);
	
	//生成凭证-选单-未生成凭证列表-红字蓝字回冲单
	List<FormBookEntry> selectFormBackFlushNoVoucherGeneratedList(Map map);
	int countFormBackFlushNoVoucherGeneratedList(Map map);
	void updateFormBackFlushFormVouch(List<FormBookEntry> formBackFlushList);
	
	//生成凭证-选单-未生成凭证列表-退货单
	List<FormBookEntry> selectRtnGoodsNoVoucherGeneratedList(Map map);
	int countRtnGoodsNoVoucherGeneratedList(Map map);
	void updateRtnGoodsFormVouch(List<FormBookEntry> formBackFlushList);
	
	//生成凭证-选单-未生成凭证列表-委托代销发货单/退货单
	List<FormBookEntry> selectEntrsAgnDelvNoVoucherGeneratedList(Map map);
	int countEntrsAgnDelvNoVoucherGeneratedList(Map map);
	void updateEntrsAgnDelvFormVouch(List<FormBookEntry> formBackFlushList);

	
	//流水帐查询
	List<FormBookEntry> selectStreamALLList(Map map);
	int selectStreamALLCount(Map map);
	
	//未记账单据-采购-销售-其他出入库
	List<FormBookEntry> selectToBookForm(Map map);
	
	void updateSellOutWhsBookEntryList(List<FormBookEntry> li);
	void updateIntoWhsBookEntryList(List<FormBookEntry> li);
	void updateOutInWhsBookEntry(List<FormBookEntry> li);
	//存储过程-计算单价
	
	BigDecimal updateInUprc(Map<String, Object> parmMap);
	
	BigDecimal updateOutUprc(Map<String, Object> parmMap);
	
	List<FormBookEntrySub> selectA();
	List<FormBookEntrySub> selectB();
	

	
	
	
	
	

	
	
		

}
