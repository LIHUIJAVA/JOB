package com.px.mis.sell.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.sell.dao.SellReportMapper;
import com.px.mis.sell.entity.ProcurementNowStokReport;
import com.px.mis.sell.entity.SalesStatisticsReport;
import com.px.mis.sell.entity.SellNowStokReport;
import com.px.mis.sell.entity.SellStatisticalReport;
import com.px.mis.sell.entity.StatisticalCount;
import com.px.mis.sell.service.SellReportService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class SellReportServiceImpl implements SellReportService {

	@Autowired
	private SellReportMapper sellReportMapper;

	@Override
	public String sellNowStokReport(Map map) {
		String resp = "";
		
		map.put("whsEncd", getList((String) map.get("whsEncd")));
		map.put("batNum", getList((String) map.get("batNum")));
		map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
		map.put("invtyEncd", getList((String) map.get("invtyEncd")));
		
		
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			
			


			List<SellNowStokReport> proList = sellReportMapper.selectSellNowStokReportList(map);
			Map tableSums = sellReportMapper.selectSellNowStokReportListSums(map);
			int count =((Long) tableSums.get("count")).intValue();
			tableSums.put("count", null);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			
//			int count = sellReportMapper.selectSellNowStokReportCount(map);

//			// 待发货数量
//			List<SellNowStokReport> sellOut = sellReportMapper.selectSellOutList(map);
//			// 调拨出
//			List<SellNowStokReport> CannibOut = sellReportMapper.selectCannibSnglOut(map);
//			// 调拨入
//			List<SellNowStokReport> CanniInto = sellReportMapper.selectCannibSnglInto(map);
//			// 待入库数量intoWhsQty;
//			List<SellNowStokReport> intoWhsQty = sellReportMapper.selectIntoWhsQty(map);
//			for (SellNowStokReport report : proList) {
//				if (report != null) {
//
//					report.setNoTaxUprc(((report.getNoTaxAmt() == null) ? new BigDecimal(1) : report.getNoTaxAmt())
//							.divide(((report.getQty() == null || report.getQty().compareTo(BigDecimal.ZERO) == 0)
//									? new BigDecimal(1)
//									: report.getQty()), 8, BigDecimal.ROUND_HALF_UP));
//
//					report.setCntnTaxUprc(
//							((report.getPrcTaxSum() == null || report.getPrcTaxSum().compareTo(BigDecimal.ZERO) == 0)
//									? new BigDecimal(1)
//									: report.getPrcTaxSum())
//											.divide(((report.getQty() == null
//													|| report.getQty().compareTo(BigDecimal.ZERO) == 0)
//															? new BigDecimal(1)
//															: report.getQty()),
//													8, BigDecimal.ROUND_HALF_UP));
//
//					for (SellNowStokReport to : sellOut) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setSellWhsQty(to.getIntoWhsQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : CannibOut) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setOutIntoQty(to.getQty() == null ? new BigDecimal(0) : to.getQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : CanniInto) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setOutWhsQty(to.getQty() == null ? new BigDecimal(0) : to.getQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : intoWhsQty) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setIntoWhsQty(to.getQty());
//							}
//						}
//					}
//
//				}
//			}

			int listNum = proList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("sell/Report/sellNowStokReport", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, proList,tableSums);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<SellNowStokReport> proList = sellReportMapper.selectSellNowStokReportList(map);
			Map tableSums = sellReportMapper.selectSellNowStokReportListSums(map);
			int count = ((Long)tableSums.get("count")).intValue();
			tableSums.put("count", null);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}

//			// 待发货数量
//			List<SellNowStokReport> sellOut = sellReportMapper.selectSellOutList(map);
//			// 调拨出
//			List<SellNowStokReport> CannibOut = sellReportMapper.selectCannibSnglOut(map);
//			// 调拨入
//			List<SellNowStokReport> CanniInto = sellReportMapper.selectCannibSnglInto(map);
//			// 待入库数量intoWhsQty;
//			List<SellNowStokReport> intoWhsQty = sellReportMapper.selectIntoWhsQty(map);
//			for (SellNowStokReport report : proList) {
//				if (report != null) {
//
//					report.setNoTaxUprc(((report.getNoTaxAmt() == null) ? new BigDecimal(1) : report.getNoTaxAmt())
//							.divide(((report.getQty() == null || report.getQty().compareTo(BigDecimal.ZERO) == 0)
//									? new BigDecimal(1)
//									: report.getQty()), 8, BigDecimal.ROUND_HALF_UP));
//
//					report.setCntnTaxUprc(
//							((report.getPrcTaxSum() == null || report.getPrcTaxSum().compareTo(BigDecimal.ZERO) == 0)
//									? new BigDecimal(1)
//									: report.getPrcTaxSum())
//											.divide(((report.getQty() == null
//													|| report.getQty().compareTo(BigDecimal.ZERO) == 0)
//															? new BigDecimal(1)
//															: report.getQty()),
//													8, BigDecimal.ROUND_HALF_UP));
//
//					for (SellNowStokReport to : sellOut) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setSellWhsQty(to.getIntoWhsQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : CannibOut) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setOutIntoQty(to.getQty() == null ? new BigDecimal(0) : to.getQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : CanniInto) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setOutWhsQty(to.getQty() == null ? new BigDecimal(0) : to.getQty());
//							}
//						}
//					}
//					for (SellNowStokReport to : intoWhsQty) {
//						if (to != null) {
//							if (report.getWhsEncd().equals(to.getWhsEncd())
//									&& report.getInvtyEncd().equals(to.getInvtyEncd())
//									&& report.getBatNum().equals(to.getBatNum())) {
//								report.setIntoWhsQty(to.getQty());
//							}
//						}
//					}
//
//				}
//			}

			try {
				resp = BaseJson.returnRespListAnno("sell/Report/SellNowStokReport", true, "查询成功！", 1, 1, 1, 1, 1, proList, tableSums);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return resp;
	}

	@Override
	public String sellSellStatisticalReport(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		
		
		map.put("whsEncd", getList((String) map.get("whsEncd")));
		map.put("batNum", getList((String) map.get("batNum")));
		map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
		map.put("invtyEncd", getList((String) map.get("invtyEncd")));
		
		map.put("depId", getList((String) map.get("depId")));
		map.put("accNum", getList((String) map.get("accNum")));
		map.put("custId", getList((String) map.get("custId")));
		map.put("bizTypId", getList((String) map.get("bizTypId")));
		map.put("sellTypId", getList((String) map.get("sellTypId")));
		map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));

		
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			List<SellStatisticalReport> proList = sellReportMapper.sellSellStatisticalReport(map);
			 Map tableSums = sellReportMapper.sellSellStatisticalReportSums(map);
				if (null!=tableSums) {
					DecimalFormat df = new DecimalFormat("#,##0.00");
					for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
						String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
						entry.setValue(s);
					}
				}
			int count = sellReportMapper.sellSellStatisticalReportCount(map);

			int listNum = proList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("sell/Report/sellStatisticalReport", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, proList,tableSums);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<SellStatisticalReport> proList = sellReportMapper.sellSellStatisticalReport(map);

			try {
				resp = BaseJson.returnRespList("sell/Report/sellStatisticalReport", true, "查询成功！", proList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resp;
	}

	@Override
	public String sellSalesStatisticsReport(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		
		map.put("whsEncd", getList((String) map.get("whsEncd")));
		map.put("batNum", getList((String) map.get("batNum")));
		map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
		map.put("invtyEncd", getList((String) map.get("invtyEncd")));
		
		map.put("depId", getList((String) map.get("depId")));
		map.put("accNum", getList((String) map.get("accNum")));
		map.put("custId", getList((String) map.get("custId")));
		map.put("bizTypId", getList((String) map.get("bizTypId")));
		map.put("sellTypId", getList((String) map.get("sellTypId")));
		map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			List<SalesStatisticsReport> proList = sellReportMapper.sellSalesStatisticsReport(map);
			Map tableSums = sellReportMapper.sellSalesStatisticsReportSums(map);
			if (null!=tableSums) {
				DecimalFormat df = new DecimalFormat("#,##0.00");
				for(Map.Entry entry : (Set<Entry>)tableSums.entrySet()) {
					String s = df.format((BigDecimal) entry.getValue()==null?new BigDecimal(0.00):entry.getValue());
					entry.setValue(s);
				}
			}
			
			int count = sellReportMapper.sellSalesStatisticsReportCount(map);

			int listNum = proList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("sell/Report/sellSalesStatisticsReport", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, proList,tableSums);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<SalesStatisticsReport> proList = sellReportMapper.sellSalesStatisticsReport(map);

			try {
				resp = BaseJson.returnRespListAnno("sell/Report/sellSalesStatisticsReport", true, "查询成功！", proList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

//			resp = BaseJson.returnRespList("sell/Report/sellSalesStatisticsReport", true, "查询成功！", count, pageNo,
//					pageSize, listNum, pages, proList);

		return resp;
	}

	@Override
	public String sellProcurementNowStokReport(Map<String, Object> map) {

		String resp = "";
		map.put("whsEncd", getList((String) map.get("whsEncd")));
		map.put("batNum", getList((String) map.get("batNum")));
		map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
		map.put("invtyEncd", getList((String) map.get("invtyEncd")));
		
		map.put("depId", getList((String) map.get("depId")));
		map.put("accNum", getList((String) map.get("accNum")));
		map.put("custId", getList((String) map.get("custId")));
		map.put("bizTypId", getList((String) map.get("bizTypId")));
		map.put("sellTypId", getList((String) map.get("sellTypId")));
		map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<ProcurementNowStokReport> proList = sellReportMapper.sellProcurementNowStokReport(map);
			Map tableSums = sellReportMapper.sellProcurementNowStokReportSums(map);
			int count = sellReportMapper.sellProcurementNowStokReportCount(map);
			int listNum = proList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("sell/Report/sellProcurementNowStokReport", true, "查询成功", count, pageNo,
						pageSize, listNum, pages, proList,tableSums);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<ProcurementNowStokReport> proList = sellReportMapper.sellProcurementNowStokReport(map);
			try {
				resp = BaseJson.returnRespListAnno("sell/Report/sellProcurementNowStokReport", true, "查询成功！", proList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resp;
	}

	@Override
	public String sellStatisticalCount(Map<String, Object> map) {
		String resp = "";
		map.put("whsEncd", getList((String) map.get("whsEncd")));
		map.put("batNum", getList((String) map.get("batNum")));
		map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
		map.put("invtyEncd", getList((String) map.get("invtyEncd")));

		map.put("depId", getList((String) map.get("depId")));
		map.put("accNum", getList((String) map.get("accNum")));
		map.put("custId", getList((String) map.get("custId")));
		map.put("bizTypId", getList((String) map.get("bizTypId")));
		map.put("sellTypId", getList((String) map.get("sellTypId")));
		map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));
		if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<StatisticalCount> proList = sellReportMapper.sellStatisticalCountList(map);
			int count = sellReportMapper.sellStatisticalCountCount(map);

			int listNum = proList.size();
			int pages = count / pageSize + 1;
			try {
				resp = BaseJson.returnRespList("sell/Report/sellStatisticalCount", true, "查询成功！", count, pageNo,
						pageSize, listNum, pages, proList);
				System.err.println(proList);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<StatisticalCount> proList = sellReportMapper.sellStatisticalCountList(map);
			try {
				resp = BaseJson.returnRespListAnno("sell/Report/sellStatisticalCount", true, "查询成功！", proList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return resp;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		if (id == null || id.equals("")) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}
}