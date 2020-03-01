package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.WhsPlatExpressMapp;

public interface WhsPlatExpressMappDao {
	
	
	public void insert(WhsPlatExpressMapp whsPlatExpressMapp);
	
	public void delete(WhsPlatExpressMapp whsPlatExpressMapp);
	
	public void update(WhsPlatExpressMapp whsPlatExpressMapp);
	
	public List<WhsPlatExpressMapp> selectList(Map map);

	public int selectCount(Map map);
	
	/**
	 * 判断是否已经存在对应
	 * @param whsPlatExpressMapp
	 * 当传入参数没有主键id时 适用新增时的判断
	 * 当传入参数有主键id时，适用修改时的判断
	 * @return 大于0说明存在，不能新增或修改
	 */
	public int checkExsist(WhsPlatExpressMapp whsPlatExpressMapp);
	
	
	public List<WhsPlatExpressMapp> selectListByPlatIdAndWhsCode(@Param("platId")String platId,@Param("whsCode")String whsCode);

	//云打印模板
	List<WhsPlatExpressMapp> selectCloudPrint(@Param("platId")String platId,@Param("cpCode")String cpCode);
	int updateCloudPrint(@Param("list")List<WhsPlatExpressMapp> list,@Param("cloudPrint")String cloudPrint,@Param("cloudPrintCustom") String cloudPrintCustom);

}
