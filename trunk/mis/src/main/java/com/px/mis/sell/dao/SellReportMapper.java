package com.px.mis.sell.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.sell.entity.ProcurementNowStokReport;
import com.px.mis.sell.entity.SalesStatisticsReport;
import com.px.mis.sell.entity.SellNowStokReport;
import com.px.mis.sell.entity.SellStatisticalReport;
import com.px.mis.sell.entity.StatisticalCount;

public interface SellReportMapper {

	// 销售现存量
	List<SellNowStokReport> selectSellNowStokReportList(Map map);

	int selectSellNowStokReportCount(Map map);

	List<SellNowStokReport> selectSellOutList(Map map);

	List<SellNowStokReport> selectCannibSnglOut(Map map);

	List<SellNowStokReport> selectCannibSnglInto(Map map);

	List<SellNowStokReport> selectIntoWhsQty(Map map);

	// 发货统计
	List<SellStatisticalReport> sellSellStatisticalReport(Map map);

	int sellSellStatisticalReportCount(Map map);

	// 销售综合统计表
	List<SalesStatisticsReport> sellSalesStatisticsReport(Map map);

	int sellSalesStatisticsReportCount(Map map);

//采购现存量查询
	List<ProcurementNowStokReport> sellProcurementNowStokReport(Map<String, Object> map);

	int sellProcurementNowStokReportCount(Map<String, Object> map);
//发货汇总表
	int sellStatisticalCountCount(Map<String, Object> map);

	List<StatisticalCount> sellStatisticalCountList(Map<String, Object> map);

	Map sellSalesStatisticsReportSums(Map map);

	Map sellSellStatisticalReportSums(Map map);

	Map selectSellNowStokReportListSums(Map map);

	Map sellProcurementNowStokReportSums(Map<String, Object> map);

}
