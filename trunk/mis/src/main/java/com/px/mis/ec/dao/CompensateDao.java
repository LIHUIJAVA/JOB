package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.Compensate;
import com.px.mis.purc.entity.SellSnglSub;

public interface CompensateDao  {

	public void insert(List<Compensate> compensateList);
	
	public void update(Compensate compensate);
	
	public List<Compensate> selectList(Map map);
	
	public List<Compensate> selectByIds(List<String> compensateIdList);
	
	public int selectCount(Map map);

	int audit(@Param("compensateIdList")List<String> compensateIdList,@Param("auditTime")String auditTime,@Param("auditUserId")String auditUserId,@Param("auditUserName")String auditUserName);
	
	public Compensate selectById(String compensateId);
	
	//�������۵��Ų�ѯ�����ӱ��˰�ϼƷ�0����ϸ��¼
	public List<SellSnglSub> selectSellSnglSubBySellSnglId(String sellSnglId);
	
	public void delete(Compensate compensate);
	
}
