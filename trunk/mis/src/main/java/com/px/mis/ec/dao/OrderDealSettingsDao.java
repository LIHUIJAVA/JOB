package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.OrderDealSettings;
import com.px.mis.ec.entity.PlatSpecialWhs;
import com.px.mis.ec.entity.PlatWhs;
import com.px.mis.whs.entity.City;


public interface OrderDealSettingsDao {
	
	public void update(OrderDealSettings orderDealSettings);
	
	public OrderDealSettings select(String settingId);
	
	//����ƽ̨���,���������Ҳֿ����
	public PlatSpecialWhs selectByInvtyCodeAndPlatCode(@Param("platCode")String platCode,@Param("invtyCode")String invtyCode);
	//����ʡ�в����������
	public City selectCityCode(@Param("province")String province,@Param("city") String city);
	//����ƽ̨���,���������Ҳֿ���뼯��
	public List<PlatWhs> selectByInvtyCodeAndCityCode(@Param("platCode")String platCode,@Param("cityCode") String cityCode);
	//����map��ѯ�������ֿ⼯��
	public List<PlatSpecialWhs> selectPlatWhsSpecialListByMap(Map map);
	//����map��ѯ�������ֿ�����
	public int selectPlatWhsSpecialListCountByMap(Map map);
	//����������
	public int addPlatWhsSpecial(PlatSpecialWhs whs);
	//ɾ��������
	public int deleteById(List<String> list);
	//�޸�������
	public int updatePlatWhsSpecialById(PlatSpecialWhs whs);
	//����id����������
	public PlatSpecialWhs selectPlatWhsSpecialById(int stId);
	
}
