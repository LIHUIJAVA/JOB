package com.px.mis.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;

public interface FormBookEntrySubDao {
	
	//批量插入
	int insertSubList(List<FormBookEntrySub> list);

	//查询明细账总数量
	int selectDetailCount(Map map);
	
	//查询明细账
	List<FormBookEntrySub> selectDetailList(Map map);
	//入库总数量
	BigDecimal countBxQty(Map<String, String> dataMap);
	//批量修改结算成本
	int updateNoTaxUprc(List<FormBookEntrySub> dataList);

	int insertFormSub(FormBookEntrySub formSub);

	int updateSubj(List<FormBookEntrySub> list);

	List<FormBookEntrySub> selectSubByFormNum(String formNum);
	//批量删除
	int deleteList(List<FormBookEntrySub> list);
	
	
	
}
