package com.px.mis.ec.entity;

import java.math.BigDecimal;

public class WhsPlatExpressMapp {
	private int id;
	private String whsId;
	private String platId;//ƽ̨id
	private String expressId;//��ݹ�˾id
	private String whsName;//�ֿ�����
	private String platName;//ƽ̨����
	private String expressName;//��ݹ�˾����
	private BigDecimal weightMin;//������С����
	private BigDecimal weightMax;//�����������
	private String templateId;//��ӡģ����
	private String templateName;//��ӡģ������
	private String cloudPrint;//�ƴ�ӡģ��url
	private String cloudPrintCustom;//�Զ����ƴ�ӡģ��url

	
	public final String getCloudPrintCustom() {
		return cloudPrintCustom;
	}
	public final void setCloudPrintCustom(String cloudPrintCustom) {
		this.cloudPrintCustom = cloudPrintCustom;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public BigDecimal getWeightMin() {
		return weightMin;
	}
	public void setWeightMin(BigDecimal weightMin) {
		this.weightMin = weightMin;
	}
	public BigDecimal getWeightMax() {
		return weightMax;
	}
	public void setWeightMax(BigDecimal weightMax) {
		this.weightMax = weightMax;
	}
	public String getWhsName() {
		return whsName;
	}
	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}
	public String getPlatName() {
		return platName;
	}
	public void setPlatName(String platName) {
		this.platName = platName;
	}
	public String getExpressName() {
		return expressName;
	}
	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWhsId() {
		return whsId;
	}
	public void setWhsId(String whsId) {
		this.whsId = whsId;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	public String getExpressId() {
		return expressId;
	}
	public void setExpressId(String expressId) {
		this.expressId = expressId;
	}
	public final String getCloudPrint() {
		return cloudPrint;
	}
	public final void setCloudPrint(String cloudPrint) {
		this.cloudPrint = cloudPrint;
	}
	

}
