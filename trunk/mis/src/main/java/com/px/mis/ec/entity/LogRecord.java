package com.px.mis.ec.entity;

public class LogRecord {
	
    private int logId;//��־���
    private String operatId;//����Ա���
    private String operatName;//����Ա����
    private String operatTime;//����ʱ��
    private int operatType;//��������
    private String operatOrder;//�����������
    private String operatContent;//��������
    private String memo;//��ע
    private String typeName;//��־����
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