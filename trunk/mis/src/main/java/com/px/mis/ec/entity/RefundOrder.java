package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

public class RefundOrder {
	
    private String refId;//退款单编号
    private String orderId;//订单编号
    private String ecId;//电商平台编号
    private String ecOrderId;//电商订单编号
    private String storeId;//店铺编号
    private String storeName;//店铺名称
    private String ecRefId;//电商退款单号
    private String applyDate;//申请日期
    private String buyerId;//买家会员号
    private Integer isRefund;//是否退货
    private Integer allRefNum;//整单退货数量
    private BigDecimal allRefMoney;//整单退款金额
    private String refReason;//退款原因
    private String refExplain;//退款说明
    private Integer refStatus;//退款状态
    private String downTime;//下载时间
    private String treDate;//处理日期
    private String operator;//操作员
    private Integer isAudit;//是否审核
    private String memo;//备注
    private String auditTime;//审核时间
    private Integer source;//退款单来源 0：其他 1：自主售后 2：取消订单 3：赔付管理
    private String sourceNo;//来源单据号ID
    private String ecGoodId;//店铺商品编码
    private String auditUserId;
    private String auditUserName;
    private String operatorId;
    private String operatorTime;
	private String auditHint;//审核提示
	private String expressCode;//快递单号

    private List<RefundOrders> refundOrders;//退款单子表集合
    
	public String getAuditHint() {
		return auditHint;
	}
	public void setAuditHint(String auditHint) {
		this.auditHint = auditHint;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getEcGoodId() {
		return ecGoodId;
	}
	public void setEcGoodId(String ecGoodId) {
		this.ecGoodId = ecGoodId;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getSourceNo() {
		return sourceNo;
	}
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEcId() {
		return ecId;
	}
	public void setEcId(String ecId) {
		this.ecId = ecId;
	}
	public String getEcOrderId() {
		return ecOrderId;
	}
	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getEcRefId() {
		return ecRefId;
	}
	public void setEcRefId(String ecRefId) {
		this.ecRefId = ecRefId;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	public Integer getIsRefund() {
		return isRefund;
	}
	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}
	public Integer getAllRefNum() {
		return allRefNum;
	}
	public void setAllRefNum(Integer allRefNum) {
		this.allRefNum = allRefNum;
	}
	public BigDecimal getAllRefMoney() {
		return allRefMoney;
	}
	public void setAllRefMoney(BigDecimal allRefMoney) {
		this.allRefMoney = allRefMoney;
	}
	public String getRefReason() {
		return refReason;
	}
	public void setRefReason(String refReason) {
		this.refReason = refReason;
	}
	public String getRefExplain() {
		return refExplain;
	}
	public void setRefExplain(String refExplain) {
		this.refExplain = refExplain;
	}
	public Integer getRefStatus() {
		return refStatus;
	}
	public void setRefStatus(Integer refStatus) {
		this.refStatus = refStatus;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getTreDate() {
		return treDate;
	}
	public void setTreDate(String treDate) {
		this.treDate = treDate;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public List<RefundOrders> getRefundOrders() {
		return refundOrders;
	}
	public void setRefundOrders(List<RefundOrders> refundOrders) {
		this.refundOrders = refundOrders;
	}
	public Integer getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}
	public String getExpressCode() {
		return expressCode;
	}

	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allRefMoney == null) ? 0 : allRefMoney.hashCode());
		result = prime * result + ((allRefNum == null) ? 0 : allRefNum.hashCode());
		result = prime * result + ((applyDate == null) ? 0 : applyDate.hashCode());
		result = prime * result + ((auditHint == null) ? 0 : auditHint.hashCode());
		result = prime * result + ((auditTime == null) ? 0 : auditTime.hashCode());
		result = prime * result + ((auditUserId == null) ? 0 : auditUserId.hashCode());
		result = prime * result + ((auditUserName == null) ? 0 : auditUserName.hashCode());
		result = prime * result + ((buyerId == null) ? 0 : buyerId.hashCode());
		result = prime * result + ((downTime == null) ? 0 : downTime.hashCode());
		result = prime * result + ((ecGoodId == null) ? 0 : ecGoodId.hashCode());
		result = prime * result + ((ecId == null) ? 0 : ecId.hashCode());
		result = prime * result + ((ecOrderId == null) ? 0 : ecOrderId.hashCode());
		result = prime * result + ((ecRefId == null) ? 0 : ecRefId.hashCode());
		result = prime * result + ((isAudit == null) ? 0 : isAudit.hashCode());
		result = prime * result + ((isRefund == null) ? 0 : isRefund.hashCode());
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((operatorId == null) ? 0 : operatorId.hashCode());
		result = prime * result + ((operatorTime == null) ? 0 : operatorTime.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((refExplain == null) ? 0 : refExplain.hashCode());
		result = prime * result + ((refId == null) ? 0 : refId.hashCode());
		result = prime * result + ((refReason == null) ? 0 : refReason.hashCode());
		result = prime * result + ((refStatus == null) ? 0 : refStatus.hashCode());
		result = prime * result + ((refundOrders == null) ? 0 : refundOrders.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((sourceNo == null) ? 0 : sourceNo.hashCode());
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
		result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
		result = prime * result + ((treDate == null) ? 0 : treDate.hashCode());
		result = prime * result + ((expressCode == null) ? 0 : expressCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RefundOrder other = (RefundOrder) obj;
		if (allRefMoney == null) {
			if (other.allRefMoney != null)
				return false;
		} else if (!allRefMoney.equals(other.allRefMoney))
			return false;
		if (allRefNum == null) {
			if (other.allRefNum != null)
				return false;
		} else if (!allRefNum.equals(other.allRefNum))
			return false;
		if (applyDate == null) {
			if (other.applyDate != null)
				return false;
		} else if (!applyDate.equals(other.applyDate))
			return false;
		if (auditHint == null) {
			if (other.auditHint != null)
				return false;
		} else if (!auditHint.equals(other.auditHint))
			return false;
		if (auditTime == null) {
			if (other.auditTime != null)
				return false;
		} else if (!auditTime.equals(other.auditTime))
			return false;
		if (auditUserId == null) {
			if (other.auditUserId != null)
				return false;
		} else if (!auditUserId.equals(other.auditUserId))
			return false;
		if (auditUserName == null) {
			if (other.auditUserName != null)
				return false;
		} else if (!auditUserName.equals(other.auditUserName))
			return false;
		if (buyerId == null) {
			if (other.buyerId != null)
				return false;
		} else if (!buyerId.equals(other.buyerId))
			return false;
		if (downTime == null) {
			if (other.downTime != null)
				return false;
		} else if (!downTime.equals(other.downTime))
			return false;
		if (ecGoodId == null) {
			if (other.ecGoodId != null)
				return false;
		} else if (!ecGoodId.equals(other.ecGoodId))
			return false;
		if (ecId == null) {
			if (other.ecId != null)
				return false;
		} else if (!ecId.equals(other.ecId))
			return false;
		if (ecOrderId == null) {
			if (other.ecOrderId != null)
				return false;
		} else if (!ecOrderId.equals(other.ecOrderId))
			return false;
		if (ecRefId == null) {
			if (other.ecRefId != null)
				return false;
		} else if (!ecRefId.equals(other.ecRefId))
			return false;
		if (isAudit == null) {
			if (other.isAudit != null)
				return false;
		} else if (!isAudit.equals(other.isAudit))
			return false;
		if (isRefund == null) {
			if (other.isRefund != null)
				return false;
		} else if (!isRefund.equals(other.isRefund))
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (operatorId == null) {
			if (other.operatorId != null)
				return false;
		} else if (!operatorId.equals(other.operatorId))
			return false;
		if (operatorTime == null) {
			if (other.operatorTime != null)
				return false;
		} else if (!operatorTime.equals(other.operatorTime))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (refExplain == null) {
			if (other.refExplain != null)
				return false;
		} else if (!refExplain.equals(other.refExplain))
			return false;
		if (refId == null) {
			if (other.refId != null)
				return false;
		} else if (!refId.equals(other.refId))
			return false;
		if (refReason == null) {
			if (other.refReason != null)
				return false;
		} else if (!refReason.equals(other.refReason))
			return false;
		if (refStatus == null) {
			if (other.refStatus != null)
				return false;
		} else if (!refStatus.equals(other.refStatus))
			return false;
		if (refundOrders == null) {
			if (other.refundOrders != null)
				return false;
		} else if (!refundOrders.equals(other.refundOrders))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (sourceNo == null) {
			if (other.sourceNo != null)
				return false;
		} else if (!sourceNo.equals(other.sourceNo))
			return false;
		if (storeId == null) {
			if (other.storeId != null)
				return false;
		} else if (!storeId.equals(other.storeId))
			return false;
		if (storeName == null) {
			if (other.storeName != null)
				return false;
		} else if (!storeName.equals(other.storeName))
			return false;
		if (treDate == null) {
			if (other.treDate != null)
				return false;
		} else if (!treDate.equals(other.treDate))
			return false;
		if (expressCode == null) {
			if (other.expressCode != null)
				return false;
		} else if (!expressCode.equals(other.expressCode))
			return false;
		return true;
	}
}