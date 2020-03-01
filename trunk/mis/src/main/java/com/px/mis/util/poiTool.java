package com.px.mis.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class poiTool {

	private Map<String, Integer> ColMap = new HashMap<String, Integer>();

	private List<String> cellName = new ArrayList<>();

	public List<String> getCellNames() {
		cellName.clear();
		cellName.add("是否审核");
		cellName.add("是否退货");
		cellName.add("是否记账");
		cellName.add("是否结算");
		cellName.add("是否开票");
		cellName.add("是否销售");
		cellName.add("是否采购");
		cellName.add("是否内销");
		cellName.add("是否PTO");
		cellName.add("是否");
		cellName.add("是否退货");
		cellName.add("是否记账");
		cellName.add("是否保质期管理");
		cellName.add("是否允许BOM主件");
		cellName.add("是否允许BOM子件");
		cellName.add("是否条形码管理");
		cellName.add("是否委外");
		cellName.add("是否服务");
		cellName.add("是否自发货");
		cellName.add("收发类别标识");
		cellName.add("虚拟仓");
		cellName.add("是否拣货");
		cellName.add("是否自发货");
		cellName.add("保质期");
		cellName.add("是否应税劳务");
		cellName.add("是否折扣");
		return cellName;
	}

	/// 使用中文获取指定Cell值，返回为String类型
	public String GetCellData(Row row, String ColNameCN) throws Exception {
		Integer cellNum = ColMap.get(ColNameCN.trim());
		if (cellNum == null) {
			throw new Exception("不存在列名 [" + ColNameCN + "],请检查");
		}
		String result = getValue(row.getCell(cellNum));
//	   System.out.println(cellName);
		if ((result == null) || result.trim().equals("")) {
			if (cellName.contains(ColNameCN)) {
				result = "0";
			}
		}
		if (null != result) {
			result = result.endsWith(".0") ? result.replace(".0", "") : result;
		}
		return result == null ? result : result.trim();
	}

	/// 获取单元格值
	public String getValue(Cell hssfCell) {
		if (hssfCell == null) {
			return null;
		}
		if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	/// 获取BigDecimal类型值，空值返回0
	public BigDecimal GetBigDecimal(String s, int bit) {
		if ((s == null) || s.equals("")) {
			return new BigDecimal(0);
		} else {
			return new BigDecimal(s).setScale(bit, BigDecimal.ROUND_HALF_UP);
		}
	}

	/// 设置ColMap对应下标键值
	public void SetColIndex(Row row) throws Exception {
		Cell cell;
		ColMap.clear();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			cell = row.getCell(i);
			if (cell == null) {
				continue;
			}
			if (getValue(cell).trim().equals("")) {
				continue;
			}
			ColMap.put(getValue(cell).trim(), i);
		}
	}

}
