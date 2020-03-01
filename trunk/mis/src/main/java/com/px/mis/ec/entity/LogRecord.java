package com.px.mis.ec.entity;

public class LogRecord {
	
    private int logId;//日志编号
    private String operatId;//操作员编号
    private String operatName;//操作员名称
    private String operatTime;//操作时间
    private int operatType;//操作类型
    private String operatOrder;//操作订单编号
    private String operatContent;//操作内容
    private String memo;//备注
    private String typeName;//日志类型
	public int getLogId() {
		return logId;
	}
	public void setLogId(int logId) {
		this.logId = logId;
	}
	public String getOperatId() {
		return operatId;
	}
	public void setOperatId(String operatId) {
		this.operatId = operatId;
	}
	public String getOperatName() {
		return operatName;
	}
	public void setOperatName(String operatName) {
		this.operatName = operatName;
	}
	public String getOperatTime() {
		return operatTime;
	}
	public void setOperatTime(String operatTime) {
		this.operatTime = operatTime;
	}
	public int getOperatType() {
		return operatType;
	}
	public void setOperatType(int operatType) {
		this.operatType = operatType;
	}
	public String getOperatOrder() {
		return operatOrder;
	}
	public void setOperatOrder(String operatOrder) {
		this.operatOrder = operatOrder;
	}
	public String getOperatContent() {
		return operatContent;
	}
	public void setOperatContent(String operatContent) {
		this.operatContent = operatContent;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
    
}