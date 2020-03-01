package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.Aftermarket;
import com.px.mis.whs.entity.ProdStruSubTab;

public interface AftermarketDao  {

	public void insert(List<Aftermarket> aftermarketList);
	
	public void update(Aftermarket aftermarket);
	
	public List<Aftermarket> selectList(Map map);
	
	public Aftermarket selectById(int serviceId);
	
	public List<Aftermarket> selectByIds(List<String> serviceIdList);
	
	public int selectCount(Map map);

	public int audit(List<String> serviceIdList);
	
	public void delete(Aftermarket aftermarket);
	/**
	 * ���ݴ�������ѯ��Ӧ��Ʒ�ṹ���Ӽ��б�
	 * @param invCode �������
	 * @return List<ProdStruSubTab> 
	 */
	public List<ProdStruSubTab> selectProdStruSubTabByInvCode(String invCode);
	
}
