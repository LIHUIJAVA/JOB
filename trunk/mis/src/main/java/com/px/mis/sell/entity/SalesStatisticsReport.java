package com.px.mis.sell.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 销售综合统计表
 */
public class SalesStatisticsReport {
	@JsonProperty("存货编码")
	private String invtyEncd; // 存货编码
	@JsonProperty("存货名称")
	private String invtyNm; // 存货名称
//	private String sellSnglDt; // 日期
//	private String sellSnglId; // 销售单号
	@JsonProperty("订单数量")
	private BigDecimal sellQty; // 订单数量
	@JsonProperty("订单金额")
	private BigDecimal sellnoTaxAmt; // 订单金额
	@JsonProperty("订单价税合计")
	private BigDecimal sellprcTaxSum; // 订单价税合计
//	private String sellOutWhsDt; // 出库日期
//	private String sellOutWhsId; // 出库单号
	@JsonProperty("出库数量")
	private BigDecimal sellOutQty; // 出库数量
	@JsonProperty("出库金额")
	private BigDecimal sellOutNoTaxAmt; // 出库金额
	@JsonProperty("出库价税合计")
	private BigDecimal sellOutPrcTaxSum; // 出库价税合计
//	private String invSellSnglNum; // 发票号
//	private String invBllgDt; // 开票时间
	@JsonProperty("开票数量")
	private BigDecimal invQty; // 开票数量
	@JsonProperty("开票金额")
	private BigDecimal invNoTaxAmt; // 开票金额
	@JsonProperty("开票价税合计")
	private BigDecimal invPrcTaxSum; // 开票价税合计
//	private String documentsTypes; // 单据类型
	@JsonProperty("客户编号")
	private String custId; // 客户编号
	@JsonProperty("部门")
	private String depName; // 部门
	@JsonProperty("部门编码")
	private String depId; // 部门编码
	@JsonProperty("业务员")
	private String userName; // 业务员
	@JsonProperty("业务员编码")
	private String accNum; // 业务员编码
	@JsonProperty("客户名称")
	private String custNm; // 客户名称
	

	public final BigDecimal getSellQty() {
		return sellQty;
	}

	public final void setSellQty(BigDecimal sellQty) {
		this.sellQty = sellQty;
	}

	public final BigDecimal getSellnoTaxAmt() {
		return sellnoTaxAmt;
	}

	public final void setSellnoTaxAmt(BigDecimal sellnoTaxAmt) {
		this.sellnoTaxAmt = sellnoTaxAmt;
	}

	public final BigDecimal getSellprcTaxSum() {
		return sellprcTaxSum;
	}

	public final void setSellprcTaxSum(BigDecimal sellprcTaxSum) {
		this.sellprcTaxSum = sellprcTaxSum;
	}

	public final BigDecimal getSellOutQty() {
		return sellOutQty;
	}

	public final void setSellOutQty(BigDecimal sellOutQty) {
		this.sellOutQty = sellOutQty;
	}

	public final BigDecimal getSellOutNoTaxAmt() {
		return sellOutNoTaxAmt;
	}

	public final void setSellOutNoTaxAmt(BigDecimal sellOutNoTaxAmt) {
		this.sellOutNoTaxAmt = sellOutNoTaxAmt;
	}

	public final BigDecimal getSellOutPrcTaxSum() {
		return sellOutPrcTaxSum;
	}

	public final void setSellOutPrcTaxSum(BigDecimal sellOutPrcTaxSum) {
		this.sellOutPrcTaxSum = sellOutPrcTaxSum;
	}

	public final BigDecimal getInvQty() {
		return invQty;
	}

	public final void setInvQty(BigDecimal invQty) {
		this.invQty = invQty;
	}

	public final BigDecimal getInvNoTaxAmt() {
		return invNoTaxAmt;
	}

	public final void setInvNoTaxAmt(BigDecimal invNoTaxAmt) {
		this.invNoTaxAmt = invNoTaxAmt;
	}

	public final BigDecimal getInvPrcTaxSum() {
		return invPrcTaxSum;
	}

	public final void setInvPrcTaxSum(BigDecimal invPrcTaxSum) {
		this.invPrcTaxSum = invPrcTaxSum;
	}

	public final String getCustId() {
		return custId;
	}

	public final void setCustId(String custId) {
		this.custId = custId;
	}

	public final String getDepName() {
		return depName;
	}

	public final void setDepName(String depName) {
		this.depName = depName;
	}

	public final String getDepId() {
		return depId;
	}

	public final void setDepId(String depId) {
		this.depId = depId;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getAccNum() {
		return accNum;
	}

	public final void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public final String getCustNm() {
		return custNm;
	}

	public final void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public final String getInvtyEncd() {
		return invtyEncd;
	}

	public final void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public final String getInvtyNm() {
		return invtyNm;
	}

	public final void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

}
