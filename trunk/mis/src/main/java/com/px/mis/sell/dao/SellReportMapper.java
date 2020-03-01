package com.px.mis.sell.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.sell.entity.ProcurementNowStokReport;
import com.px.mis.sell.entity.SalesStatisticsReport;
import com.px.mis.sell.entity.SellNowStokReport;
import com.px.mis.sell.entity.SellStatisticalReport;
import com.px.mis.sell.entity.StatisticalCount;

public interface SellReportMapper {

	// �����ִ���
	List<SellNowStokReport> selectSellNowStokReportList(Map map);

	int selectSellNowStokReportCount(Map map);

	List<SellNowStokReport> selectSellOutList(Map map);

	List<SellNowStokReport> selectCannibSnglOut(Map map);

	List<SellNowStokReport> selectCannibSnglInto(Map map);

	List<SellNowStokReport> selectIntoWhsQty(Map map);

	// ����ͳ��
	List<SellStatisticalReport> sellSellStatisticalReport(Map map);

	int sellSellStatisticalReportCount(Map map);

	// �����ۺ�ͳ�Ʊ�
	List<SalesStatisticsReport> sellSalesStatisticsReport(Map map);

	int sellSalesStatisticsReportCount(Map map);

//�ɹ��ִ�����ѯ
	List<ProcurementNowStokReport> sellProcurementNowStokReport(Map<String, Object> map);

	int sellProcurementNowStokReportCount(Map<String, Object> map);
//�������ܱ�
	int sellStatisticalCountCount(Map<String, Object> map);

	List<StatisticalCount> sellStatisticalCountList(Map<String, Object> map);

	Map sellSalesStatisticsReportSums(Map map);

	Map sellSellStatisticalReportSums(Map map);

	Map selectSellNowStokReportListSums(Map map);

	Map sellProcurementNowStokReportSums(Map<String, Object> map);

}
