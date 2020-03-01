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
		cellName.add("�Ƿ����");
		cellName.add("�Ƿ��˻�");
		cellName.add("�Ƿ����");
		cellName.add("�Ƿ����");
		cellName.add("�Ƿ�Ʊ");
		cellName.add("�Ƿ�����");
		cellName.add("�Ƿ�ɹ�");
		cellName.add("�Ƿ�����");
		cellName.add("�Ƿ�PTO");
		cellName.add("�Ƿ�");
		cellName.add("�Ƿ��˻�");
		cellName.add("�Ƿ����");
		cellName.add("�Ƿ����ڹ���");
		cellName.add("�Ƿ�����BOM����");
		cellName.add("�Ƿ�����BOM�Ӽ�");
		cellName.add("�Ƿ����������");
		cellName.add("�Ƿ�ί��");
		cellName.add("�Ƿ����");
		cellName.add("�Ƿ��Է���");
		cellName.add("�շ�����ʶ");
		cellName.add("�����");
		cellName.add("�Ƿ���");
		cellName.add("�Ƿ��Է���");
		cellName.add("������");
		cellName.add("�Ƿ�Ӧ˰����");
		cellName.add("�Ƿ��ۿ�");
		return cellName;
	}

	/// ʹ�����Ļ�ȡָ��Cellֵ������ΪString����
	public String GetCellData(Row row, String ColNameCN) throws Exception {
		Integer cellNum = ColMap.get(ColNameCN.trim());
		if (cellNum == null) {
			throw new Exception("���������� [" + ColNameCN + "],����");
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

	/// ��ȡ��Ԫ��ֵ
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

	/// ��ȡBigDecimal����ֵ����ֵ����0
	public BigDecimal GetBigDecimal(String s, int bit) {
		if ((s == null) || s.equals("")) {
			return new BigDecimal(0);
		} else {
			return new BigDecimal(s).setScale(bit, BigDecimal.ROUND_HALF_UP);
		}
	}

	/// ����ColMap��Ӧ�±��ֵ
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
