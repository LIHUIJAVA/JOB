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
	
	//根据平台编号,存货编码查找仓库编码
	public PlatSpecialWhs selectByInvtyCodeAndPlatCode(@Param("platCode")String platCode,@Param("invtyCode")String invtyCode);
	//根据省市查找区域编码
	public City selectCityCode(@Param("province")String province,@Param("city") String city);
	//根据平台编号,区域编码查找仓库编码集合
	public List<PlatWhs> selectByInvtyCodeAndCityCode(@Param("platCode")String platCode,@Param("cityCode") String cityCode);
	//根据map查询特殊存货仓库集合
	public List<PlatSpecialWhs> selectPlatWhsSpecialListByMap(Map map);
	//根据map查询特殊存货仓库总数
	public int selectPlatWhsSpecialListCountByMap(Map map);
	//新增特殊存货
	public int addPlatWhsSpecial(PlatSpecialWhs whs);
	//删除特殊存货
	public int deleteById(List<String> list);
	//修改特殊存货
	public int updatePlatWhsSpecialById(PlatSpecialWhs whs);
	//根据id查找特殊存货
	public PlatSpecialWhs selectPlatWhsSpecialById(int stId);
	
}
