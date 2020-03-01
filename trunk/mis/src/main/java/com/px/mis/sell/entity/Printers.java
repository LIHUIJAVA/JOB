package com.px.mis.sell.entity;


//打印机列表
public class Printers {
	private Integer idx;//编号
	private String  warehouseId;//仓库编号
	private String 	printerId;//打印机编码
	private String 	printerName;//打印机名称
	private Integer printerTypeValue;//类型编码
	private Integer printerWide;//标签宽度
	private Integer printerHigh;//标签长度
	private Integer printerSpeed;//打印速度
	public Integer getIdx() {
		return idx;
	}
	public void setIdx(Integer idx) {
		this.idx = idx;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getPrinterId() {
		return printerId;
	}
	public void setPrinterId(String printerId) {
		this.printerId = printerId;
	}
	public String getPrinterName() {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	public Integer getPrinterTypeValue() {
		return printerTypeValue;
	}
	public void setPrinterTypeValue(Integer printerTypeValue) {
		this.printerTypeValue = printerTypeValue;
	}
	public Integer getPrinterWide() {
		return printerWide;
	}
	public void setPrinterWide(Integer printerWide) {
		this.printerWide = printerWide;
	}
	public Integer getPrinterHigh() {
		return printerHigh;
	}
	public void setPrinterHigh(Integer printerHigh) {
		this.printerHigh = printerHigh;
	}
	public Integer getPrinterSpeed() {
		return printerSpeed;
	}
	public void setPrinterSpeed(Integer printerSpeed) {
		this.printerSpeed = printerSpeed;
	}
	
	
	
}
