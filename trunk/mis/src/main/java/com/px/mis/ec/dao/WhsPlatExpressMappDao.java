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
	 * �ж��Ƿ��Ѿ����ڶ�Ӧ
	 * @param whsPlatExpressMapp
	 * ���������û������idʱ ��������ʱ���ж�
	 * ���������������idʱ�������޸�ʱ���ж�
	 * @return ����0˵�����ڣ������������޸�
	 */
	public int checkExsist(WhsPlatExpressMapp whsPlatExpressMapp);
	
	
	public List<WhsPlatExpressMapp> selectListByPlatIdAndWhsCode(@Param("platId")String platId,@Param("whsCode")String whsCode);

	//�ƴ�ӡģ��
	List<WhsPlatExpressMapp> selectCloudPrint(@Param("platId")String platId,@Param("cpCode")String cpCode);
	int updateCloudPrint(@Param("list")List<WhsPlatExpressMapp> list,@Param("cloudPrint")String cloudPrint,@Param("cloudPrintCustom") String cloudPrintCustom);

}
