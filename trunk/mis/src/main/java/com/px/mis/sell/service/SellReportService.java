package com.px.mis.sell.service;

import java.util.Map;

public interface SellReportService {

	public String sellNowStokReport(Map map);

	public String sellSellStatisticalReport(Map map);

	public String sellSalesStatisticsReport(Map map);

	public String sellProcurementNowStokReport(Map<String, Object> map);

	public String sellStatisticalCount(Map<String, Object> map);

}
