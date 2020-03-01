package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.aspectj.lang.annotation.Pointcut;

import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.LogisticsTabExport;

public interface LogisticsTabDao {

	public void insert(LogisticsTab logisticsTab);
	
	public void update(LogisticsTab logisticsTab);
	/**
	 * 批量修改快递公司
	 * @param list
	 * @param expressEncd
	 */
	public void updateExpress(@Param("list") List list,@Param("expressEncd") String expressEncd);
	/**
	 * 修改快递单号，填入快递单号
	 * @param list
	 * @param expressEncd
	 */
	public void updateExpressCode(@Param("ordrNum")String ordrNum,@Param("expressCode")String expressCode);
	
	public void delete(Integer ordrNum);
	
	public void deleteBySellSnglId(String sellSnglId);
	
	public LogisticsTab select(Integer ordrNum);
	
	public LogisticsTab selectByOrderId(String orderId);
	
	public List<Map> selectList(Map map);
	
	public int selectCount(Map map);

	public void insertList(List<LogisticsTab> logisticsTabList);
	
	public ExpressCodeAndName selectExpressNameByCode(String expressCode);
	
	public List<LogisticsTab> selectListPrint(List<String> list);
	
	public List<Map> exportList(@Param("orderIds")List<String> orderIds);
	
	public List<String> selectOrderIdByExportCondition(Map map);
	
	public int updatePickOK(Integer orderNum);
	public int updatePickOff(Integer orderNum);
	public void updateSellSnglPickOK(String sellSnglId);
	public void updateSellSnglPickOff(String sellSnglId);
	//public void updateShip(S)

}
