package com.px.mis.ec.entity;

import java.math.BigDecimal;
import java.util.List;

public class RefundOrderExport {
	
    private String refId;//�˿���
    private String orderId;//�������
    private String ecOrderId;//���̶������
    private String storeId;//���̱��
    private String storeName;//��������
    private String ecRefId;//�����˿��
    private String applyDate;//��������
    private String buyerId;//��һ�Ա��
    private Integer isRefund;//�Ƿ��˻�
    private Integer allRefNum;//�����˻�����
    private BigDecimal allRefMoney;//�����˿���
    private String refReason;//�˿�ԭ��
    private String refExplain;//�˿�˵��
    private Integer refStatus;//�˿�״̬
    private String downTime;//����ʱ��
    private String treDate;//��������
    private String operator;//����Ա
    private Integer isAudit;//�Ƿ����
    private String memo;//��ע
    private String auditTime;//���ʱ��
    private Integer source;//�˿��Դ 0������ 1�������ۺ� 2��ȡ������ 3���⸶����
    private String sourceNo;//��Դ���ݺ�ID
    private String operatorId;//�����˱��
    private String operatorTime;//����ʱ��
    private String auditUserId;//����˱��
    private String auditUserName;//�����
    private String auditHint;//�����ʾ
    private Long no;//��ϸ���
    private String goodId;//��Ʒ���
    private String goodName;//��Ʒ����
    private String ecGoodId;//������Ʒ����
    private String goodSku;//��Ʒsku
    private Integer canRefNum;//���˻�����
    private Integer refNum;//�˻�����
    private BigDecimal canRefMoney;//���˻����
    private BigDecimal refMoney;//�˻����
    private String refWhs;//�˻��ֿ����
    private String refWhsName;//�˻��ֿ�����
    private String batchNo;//����
    private String prdcDt;//��������
    private String invldtnDt;//ʧЧ����
    private Integer baozhiqi;//����������
    private int isGift;//�Ƿ���Ʒ
    private String memo1;//��ϸ��ע
    
    
    
	public String getRefWhsName() {
		return refWhsName;
	}
	public void setRefWhsName(String refWhsName) {
		this.refWhsName = refWhsName;
	}
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodSku() {
		return goodSku;
	}
	public void setGoodSku(String goodSku) {
		this.goodSku = goodSku;
	}
	public Integer getCanRefNum() {
		return canRefNum;
	}
	public void setCanRefNum(Integer canRefNum) {
		this.canRefNum = canRefNum;
	}
	public BigDecimal getCanRefMoney() {
		return canRefMoney;
	}
	public void setCanRefMoney(BigDecimal canRefMoney) {
		this.canRefMoney = canRefMoney;
	}
	public BigDecimal getRefMoney() {
		return refMoney;
	}
	public void setRefMoney(BigDecimal refMoney) {
		this.refMoney = refMoney;
	}
	public Integer getRefNum() {
		return refNum;
	}
	public void setRefNum(Integer refNum) {
		this.refNum = refNum;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getRefWhs() {
		return refWhs;
	}
	public void setRefWhs(String refWhs) {
		this.refWhs = refWhs;
	}
	public String getMemo1() {
		return memo1;
	}
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	public String getPrdcDt() {
		return prdcDt;
	}
	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
	}
	public String getInvldtnDt() {
		return invldtnDt;
	}
	public void setInvldtnDt(String invldtnDt) {
		this.invldtnDt = invldtnDt;
	}
	public Integer getBaozhiqi() {
		return baozhiqi;
	}
	public void setBaozhiqi(Integer baozhiqi) {
		this.baozhiqi = baozhiqi;
	}
	public int getIsGift() {
		return isGift;
	}
	public void setIsGift(int isGift) {
		this.isGift = isGift;
	}
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
	public Integer getIsAudit() {
		return isAudit;
	}
	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
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
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((sourceNo == null) ? 0 : sourceNo.hashCode());
		result = prime * result + ((storeId == null) ? 0 : storeId.hashCode());
		result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
		result = prime * result + ((treDate == null) ? 0 : treDate.hashCode());
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
		RefundOrderExport other = (RefundOrderExport) obj;
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
		return true;
	}
	
	
    
}