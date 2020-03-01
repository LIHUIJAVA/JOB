package com.px.mis.account.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ��������ڳ���ʵ����
 *
 */
public class InvtyMthTermBgnTab {
	public InvtyMthTermBgnTab() {

	}

	@JsonProperty("���")
	private Integer ordrNum; // ��id

	@JsonProperty("��Ŀ����")
	private String subId;

	@JsonProperty("��Ŀ����")
	private String subNm;

	@JsonProperty("�跽����")
	private String debitNm; // �跽����
	@JsonProperty("�������")
	private String invtyNm; // �������

	@JsonProperty("���")
	private BigDecimal bxRule;// ���;

	@JsonProperty("������λ")
	private String measrCorpNm; // ������λ

	@JsonProperty("����������")
	private String invtyClsEncd; // �������

	@JsonProperty("�����������")
	private String invtyClsNm; // �������
	@JsonProperty("�������")
	private String invtyEncd; // �������

	@JsonProperty("�������")
	private String invtyCd; // �������

	@JsonProperty("����")
	private String batNum; // ����

	@JsonProperty("�ֿ����")
	private String whsEncd; // �ֿ����

	@JsonProperty("ƾ֤����")
	private String makeVouchId;// ƾ֤����

	@JsonProperty("ƾ֤ժҪ")
	private String makeVouchAbst;// ƾ֤ժҪ
	@JsonProperty("���뵥��")
	private BigDecimal inUnitPrice; // ���뵥��
	@JsonProperty("��������")
	private BigDecimal inQty;// ��������
	@JsonProperty("������")
	private BigDecimal inMoeny;// ������
	@JsonProperty("��������")
	private BigDecimal sendUnitPrice;// ��������
	@JsonProperty("��������")
	private BigDecimal sendQty;// ��������
	@JsonProperty("�������")
	private BigDecimal sendMoeny;// �������
	@JsonProperty("��浥��")
	private BigDecimal othUnitPrice;// ��浥��
	@JsonProperty("�������")
	private BigDecimal othQty;// �������
	@JsonProperty("�����")
	private BigDecimal othMoeny;// �����
	@JsonProperty("�ڳ�����")
	private BigDecimal uprc;// �ڳ�����
	@JsonProperty("�ڳ�����")
	private BigDecimal qty;// �ڳ�����
	@JsonProperty("�ڳ����")
	private BigDecimal amt;// �ڳ����

	@JsonProperty("������")
	private String accNum;// ������

	@JsonProperty("����ʱ��")
	private String accTime; // ����ʱ��

	@JsonProperty("����ʱ��")
	private String ctime; // ����ʱ��

	@JsonProperty("�����")
	private String acctYr;// �����

	@JsonProperty("�����")
	private String acctiMth;// �����
	@JsonProperty("�Ƿ���ĩ����")

	private Integer isFinalDeal;// �Ƿ���ĩ����
	@JsonProperty("�Ƿ���ĩ����")

	private Integer isMthEndStl;// �Ƿ���ĩ����
	@JsonProperty("�ͻ�����")
	private String custId; // �ͻ�����
	@JsonProperty("�ͻ�����")
	private String custNm; // �ͻ�����

	@JsonProperty("����ͺ�")
	private BigDecimal spcModel; // ����ͺ�

	public InvtyMthTermBgnTab(String acctYr, String acctiMth, String makeVouchAbst, String invtyClsEncd, String invtyNm,
			BigDecimal bxRule, String measrCorpNm, String invtyEncd, String invtyCd, String batNum, String whsEncd,
			Integer isMthEndStl) {
		super();
		this.invtyNm = invtyNm;
		this.bxRule = bxRule;
		this.measrCorpNm = measrCorpNm;
		this.invtyEncd = invtyEncd;
		this.invtyCd = invtyCd;
		this.batNum = batNum;
		this.whsEncd = whsEncd;
		this.makeVouchAbst = makeVouchAbst;
		this.acctYr = acctYr;
		this.acctiMth = acctiMth;
		this.invtyClsEncd = invtyClsEncd;
		this.isMthEndStl = isMthEndStl;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getSubNm() {
		return subNm;
	}

	public void setSubNm(String subNm) {
		this.subNm = subNm;
	}

	public Integer getOrdrNum() {
		return ordrNum;
	}

	public void setOrdrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}

	public String getDebitNm() {
		return debitNm;
	}

	public void setDebitNm(String debitNm) {
		this.debitNm = debitNm;
	}

	public String getInvtyNm() {
		return invtyNm;
	}

	public void setInvtyNm(String invtyNm) {
		this.invtyNm = invtyNm;
	}

	public BigDecimal getBxRule() {
		return bxRule;
	}

	public void setBxRule(BigDecimal bxRule) {
		this.bxRule = bxRule;
	}

	public String getMeasrCorpNm() {
		return measrCorpNm;
	}

	public void setMeasrCorpNm(String measrCorpNm) {
		this.measrCorpNm = measrCorpNm;
	}

	public String getInvtyEncd() {
		return invtyEncd;
	}

	public void setInvtyEncd(String invtyEncd) {
		this.invtyEncd = invtyEncd;
	}

	public String getInvtyCd() {
		return invtyCd;
	}

	public void setInvtyCd(String invtyCd) {
		this.invtyCd = invtyCd;
	}

	public String getBatNum() {
		return batNum;
	}

	public void setBatNum(String batNum) {
		this.batNum = batNum;
	}

	public String getWhsEncd() {
		return whsEncd;
	}

	public void setWhsEncd(String whsEncd) {
		this.whsEncd = whsEncd;
	}

	public BigDecimal getUprc() {
		return uprc;
	}

	public void setUprc(BigDecimal uprc) {
		this.uprc = uprc;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getAccTime() {
		return accTime;
	}

	public void setAccTime(String accTime) {
		this.accTime = accTime;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getAcctYr() {
		return acctYr;
	}

	public void setAcctYr(String acctYr) {
		this.acctYr = acctYr;
	}

	public String getAcctiMth() {
		return acctiMth;
	}

	public void setAcctiMth(String acctiMth) {
		this.acctiMth = acctiMth;
	}

	public String getMakeVouchId() {
		return makeVouchId;
	}

	public void setMakeVouchId(String makeVouchId) {
		this.makeVouchId = makeVouchId;
	}

	public String getMakeVouchAbst() {
		return makeVouchAbst;
	}

	public void setMakeVouchAbst(String makeVouchAbst) {
		this.makeVouchAbst = makeVouchAbst;
	}

	public BigDecimal getInUnitPrice() {
		return inUnitPrice;
	}

	public void setInUnitPrice(BigDecimal inUnitPrice) {
		this.inUnitPrice = inUnitPrice;
	}

	public BigDecimal getInQty() {
		return inQty;
	}

	public void setInQty(BigDecimal inQty) {
		this.inQty = inQty;
	}

	public BigDecimal getInMoeny() {
		return inMoeny;
	}

	public void setInMoeny(BigDecimal inMoeny) {
		this.inMoeny = inMoeny;
	}

	public BigDecimal getSendUnitPrice() {
		return sendUnitPrice;
	}

	public void setSendUnitPrice(BigDecimal sendUnitPrice) {
		this.sendUnitPrice = sendUnitPrice;
	}

	public BigDecimal getSendQty() {
		return sendQty;
	}

	public void setSendQty(BigDecimal sendQty) {
		this.sendQty = sendQty;
	}

	public BigDecimal getSendMoeny() {
		return sendMoeny;
	}

	public void setSendMoeny(BigDecimal sendMoeny) {
		this.sendMoeny = sendMoeny;
	}

	public String getInvtyClsEncd() {
		return invtyClsEncd;
	}

	public void setInvtyClsEncd(String invtyClsEncd) {
		this.invtyClsEncd = invtyClsEncd;
	}

	public Integer getIsFinalDeal() {
		return isFinalDeal;
	}

	public void setIsFinalDeal(Integer isFinalDeal) {
		this.isFinalDeal = isFinalDeal;
	}

	public Integer getIsMthEndStl() {
		return isMthEndStl;
	}

	public void setIsMthEndStl(Integer isMthEndStl) {
		this.isMthEndStl = isMthEndStl;
	}

	public BigDecimal getOthUnitPrice() {
		return othUnitPrice;
	}

	public void setOthUnitPrice(BigDecimal othUnitPrice) {
		this.othUnitPrice = othUnitPrice;
	}

	public BigDecimal getOthQty() {
		return othQty;
	}

	public void setOthQty(BigDecimal othQty) {
		this.othQty = othQty;
	}

	public BigDecimal getOthMoeny() {
		return othMoeny;
	}

	public void setOthMoeny(BigDecimal othMoeny) {
		this.othMoeny = othMoeny;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public BigDecimal getSpcModel() {
		return spcModel;
	}

	public void setSpcModel(BigDecimal spcModel) {
		this.spcModel = spcModel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accNum == null) ? 0 : accNum.hashCode());
		result = prime * result + ((accTime == null) ? 0 : accTime.hashCode());
		result = prime * result + ((acctYr == null) ? 0 : acctYr.hashCode());
		result = prime * result + ((acctiMth == null) ? 0 : acctiMth.hashCode());
		result = prime * result + ((amt == null) ? 0 : amt.hashCode());
		result = prime * result + ((batNum == null) ? 0 : batNum.hashCode());
		result = prime * result + ((ctime == null) ? 0 : ctime.hashCode());
		result = prime * result + ((debitNm == null) ? 0 : debitNm.hashCode());
		result = prime * result + ((inMoeny == null) ? 0 : inMoeny.hashCode());
		result = prime * result + ((inQty == null) ? 0 : inQty.hashCode());
		result = prime * result + ((inUnitPrice == null) ? 0 : inUnitPrice.hashCode());
		result = prime * result + ((invtyCd == null) ? 0 : invtyCd.hashCode());
		result = prime * result + ((invtyClsEncd == null) ? 0 : invtyClsEncd.hashCode());
		result = prime * result + ((invtyEncd == null) ? 0 : invtyEncd.hashCode());
		result = prime * result + ((invtyNm == null) ? 0 : invtyNm.hashCode());
		result = prime * result + ((isFinalDeal == null) ? 0 : isFinalDeal.hashCode());
		result = prime * result + ((isMthEndStl == null) ? 0 : isMthEndStl.hashCode());
		result = prime * result + ((makeVouchAbst == null) ? 0 : makeVouchAbst.hashCode());
		result = prime * result + ((makeVouchId == null) ? 0 : makeVouchId.hashCode());
		result = prime * result + ((measrCorpNm == null) ? 0 : measrCorpNm.hashCode());
		result = prime * result + ((ordrNum == null) ? 0 : ordrNum.hashCode());
		result = prime * result + ((othMoeny == null) ? 0 : othMoeny.hashCode());
		result = prime * result + ((othQty == null) ? 0 : othQty.hashCode());
		result = prime * result + ((othUnitPrice == null) ? 0 : othUnitPrice.hashCode());
		result = prime * result + ((qty == null) ? 0 : qty.hashCode());
		result = prime * result + ((sendMoeny == null) ? 0 : sendMoeny.hashCode());
		result = prime * result + ((sendQty == null) ? 0 : sendQty.hashCode());
		result = prime * result + ((sendUnitPrice == null) ? 0 : sendUnitPrice.hashCode());
		result = prime * result + ((bxRule == null) ? 0 : bxRule.hashCode());
		result = prime * result + ((subId == null) ? 0 : subId.hashCode());
		result = prime * result + ((subNm == null) ? 0 : subNm.hashCode());
		result = prime * result + ((uprc == null) ? 0 : uprc.hashCode());
		result = prime * result + ((whsEncd == null) ? 0 : whsEncd.hashCode());
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
		InvtyMthTermBgnTab other = (InvtyMthTermBgnTab) obj;
		if (accNum == null) {
			if (other.accNum != null)
				return false;
		} else if (!accNum.equals(other.accNum))
			return false;
		if (accTime == null) {
			if (other.accTime != null)
				return false;
		} else if (!accTime.equals(other.accTime))
			return false;
		if (acctYr == null) {
			if (other.acctYr != null)
				return false;
		} else if (!acctYr.equals(other.acctYr))
			return false;
		if (acctiMth == null) {
			if (other.acctiMth != null)
				return false;
		} else if (!acctiMth.equals(other.acctiMth))
			return false;
		if (amt == null) {
			if (other.amt != null)
				return false;
		} else if (!amt.equals(other.amt))
			return false;
		if (batNum == null) {
			if (other.batNum != null)
				return false;
		} else if (!batNum.equals(other.batNum))
			return false;
		if (ctime == null) {
			if (other.ctime != null)
				return false;
		} else if (!ctime.equals(other.ctime))
			return false;
		if (debitNm == null) {
			if (other.debitNm != null)
				return false;
		} else if (!debitNm.equals(other.debitNm))
			return false;
		if (inMoeny == null) {
			if (other.inMoeny != null)
				return false;
		} else if (!inMoeny.equals(other.inMoeny))
			return false;
		if (inQty == null) {
			if (other.inQty != null)
				return false;
		} else if (!inQty.equals(other.inQty))
			return false;
		if (inUnitPrice == null) {
			if (other.inUnitPrice != null)
				return false;
		} else if (!inUnitPrice.equals(other.inUnitPrice))
			return false;
		if (invtyCd == null) {
			if (other.invtyCd != null)
				return false;
		} else if (!invtyCd.equals(other.invtyCd))
			return false;
		if (invtyClsEncd == null) {
			if (other.invtyClsEncd != null)
				return false;
		} else if (!invtyClsEncd.equals(other.invtyClsEncd))
			return false;
		if (invtyEncd == null) {
			if (other.invtyEncd != null)
				return false;
		} else if (!invtyEncd.equals(other.invtyEncd))
			return false;
		if (invtyNm == null) {
			if (other.invtyNm != null)
				return false;
		} else if (!invtyNm.equals(other.invtyNm))
			return false;
		if (isFinalDeal == null) {
			if (other.isFinalDeal != null)
				return false;
		} else if (!isFinalDeal.equals(other.isFinalDeal))
			return false;
		if (isMthEndStl == null) {
			if (other.isMthEndStl != null)
				return false;
		} else if (!isMthEndStl.equals(other.isMthEndStl))
			return false;
		if (makeVouchAbst == null) {
			if (other.makeVouchAbst != null)
				return false;
		} else if (!makeVouchAbst.equals(other.makeVouchAbst))
			return false;
		if (makeVouchId == null) {
			if (other.makeVouchId != null)
				return false;
		} else if (!makeVouchId.equals(other.makeVouchId))
			return false;
		if (measrCorpNm == null) {
			if (other.measrCorpNm != null)
				return false;
		} else if (!measrCorpNm.equals(other.measrCorpNm))
			return false;
		if (ordrNum == null) {
			if (other.ordrNum != null)
				return false;
		} else if (!ordrNum.equals(other.ordrNum))
			return false;
		if (othMoeny == null) {
			if (other.othMoeny != null)
				return false;
		} else if (!othMoeny.equals(other.othMoeny))
			return false;
		if (othQty == null) {
			if (other.othQty != null)
				return false;
		} else if (!othQty.equals(other.othQty))
			return false;
		if (othUnitPrice == null) {
			if (other.othUnitPrice != null)
				return false;
		} else if (!othUnitPrice.equals(other.othUnitPrice))
			return false;
		if (qty == null) {
			if (other.qty != null)
				return false;
		} else if (!qty.equals(other.qty))
			return false;
		if (sendMoeny == null) {
			if (other.sendMoeny != null)
				return false;
		} else if (!sendMoeny.equals(other.sendMoeny))
			return false;
		if (sendQty == null) {
			if (other.sendQty != null)
				return false;
		} else if (!sendQty.equals(other.sendQty))
			return false;
		if (sendUnitPrice == null) {
			if (other.sendUnitPrice != null)
				return false;
		} else if (!sendUnitPrice.equals(other.sendUnitPrice))
			return false;
		if (bxRule == null) {
			if (other.bxRule != null)
				return false;
		} else if (!bxRule.equals(other.bxRule))
			return false;
		if (subId == null) {
			if (other.subId != null)
				return false;
		} else if (!subId.equals(other.subId))
			return false;
		if (subNm == null) {
			if (other.subNm != null)
				return false;
		} else if (!subNm.equals(other.subNm))
			return false;
		if (uprc == null) {
			if (other.uprc != null)
				return false;
		} else if (!uprc.equals(other.uprc))
			return false;
		if (whsEncd == null) {
			if (other.whsEncd != null)
				return false;
		} else if (!whsEncd.equals(other.whsEncd))
			return false;
		return true;
	}

}
