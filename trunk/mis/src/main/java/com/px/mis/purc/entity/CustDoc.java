package com.px.mis.purc.entity;
/*客户档案*/
import java.math.BigDecimal;

public class CustDoc {
    private String custId; //客户id
    private String custNm; //客户名称
    private String custShtNm; //客户简称
    private String clsId;//客户分类编号
    private BigDecimal recvblBal;//应收余额
    private String ltstInvTm;//最近发票时间
    private BigDecimal ltstInvAmt;//最近发票金额
    private String ltstRecvTm;//最近收款时间
    private BigDecimal ltstRecvAmt;//最近收款金额
    private String bizLicsPic;//营业执照照片
    private String bankOpenAcctLics;//银行开户许可证
    private String contcr;//联系人
    private String tel;//电话
    private String addr;//地址
    private String sctyCrdtCd;//统一社会信用代码
    private String memo;//备注
    private String bllgCorp;//开票单位
    private String opnBnk;//开户银行
    private String bkatNum;//银行账号
    private String bankEncd;//所属银行编号
    private String delvAddr;//发货地址
    private String devDt; //发展日期
    private String setupPers;//创建人
    private String setupDt;//创建时间
    private String mdfr;//修改人
    private String modiDt;//修改时间
    private BigDecimal taxRate;//税率
	private String custTotlCorp;//客户总公司
	private String custTotlCorpId;//客户总公司编码
	
	public String getCustTotlCorpId() {
		return custTotlCorpId;
	}

	public void setCustTotlCorpId(String custTotlCorpId) {
		this.custTotlCorpId = custTotlCorpId;
	}

	public String getCustTotlCorp() {
		return custTotlCorp;
	}

	public void setCustTotlCorp(String custTotlCorp) {
		this.custTotlCorp = custTotlCorp;
	}

	public String getContcr() {
		return contcr;
	}

	public void setContcr(String contcr) {
		this.contcr = contcr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSctyCrdtCd() {
		return sctyCrdtCd;
	}

	public void setSctyCrdtCd(String sctyCrdtCd) {
		this.sctyCrdtCd = sctyCrdtCd;
	}

	public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm == null ? null : custNm.trim();
    }

    public String getCustShtNm() {
        return custShtNm;
    }

    public void setCustShtNm(String custShtNm) {
        this.custShtNm = custShtNm == null ? null : custShtNm.trim();
    }

    public String getClsId() {
        return clsId;
    }

    public void setClsId(String clsId) {
        this.clsId = clsId == null ? null : clsId.trim();
    }

    public BigDecimal getRecvblBal() {
        return recvblBal;
    }

    public void setRecvblBal(BigDecimal recvblBal) {
        this.recvblBal = recvblBal;
    }

    public String getLtstInvTm() {
        return ltstInvTm;
    }

    public void setLtstInvTm(String ltstInvTm) {
        this.ltstInvTm = ltstInvTm;
    }

    public BigDecimal getLtstInvAmt() {
        return ltstInvAmt;
    }

    public void setLtstInvAmt(BigDecimal ltstInvAmt) {
        this.ltstInvAmt = ltstInvAmt;
    }

    public String getLtstRecvTm() {
        return ltstRecvTm;
    }

    public void setLtstRecvTm(String ltstRecvTm) {
        this.ltstRecvTm = ltstRecvTm;
    }

    public BigDecimal getLtstRecvAmt() {
        return ltstRecvAmt;
    }

    public void setLtstRecvAmt(BigDecimal ltstRecvAmt) {
        this.ltstRecvAmt = ltstRecvAmt;
    }

    public String getBizLicsPic() {
        return bizLicsPic;
    }

    public void setBizLicsPic(String bizLicsPic) {
        this.bizLicsPic = bizLicsPic == null ? null : bizLicsPic.trim();
    }

    public String getBankOpenAcctLics() {
        return bankOpenAcctLics;
    }

    public void setBankOpenAcctLics(String bankOpenAcctLics) {
        this.bankOpenAcctLics = bankOpenAcctLics == null ? null : bankOpenAcctLics.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public String getBllgCorp() {
		return bllgCorp;
	}

	public void setBllgCorp(String bllgCorp) {
		this.bllgCorp = bllgCorp;
	}

	public String getOpnBnk() {
		return opnBnk;
	}

	public void setOpnBnk(String opnBnk) {
		this.opnBnk = opnBnk;
	}

	public String getBkatNum() {
		return bkatNum;
	}

	public void setBkatNum(String bkatNum) {
		this.bkatNum = bkatNum;
	}

	public String getBankEncd() {
		return bankEncd;
	}

	public void setBankEncd(String bankEncd) {
		this.bankEncd = bankEncd;
	}

	public String getDelvAddr() {
		return delvAddr;
	}

	public void setDelvAddr(String delvAddr) {
		this.delvAddr = delvAddr;
	}

	public String getDevDt() {
		return devDt;
	}

	public void setDevDt(String devDt) {
		this.devDt = devDt;
	}

	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}

	public String getSetupDt() {
		return setupDt;
	}

	public void setSetupDt(String setupDt) {
		this.setupDt = setupDt;
	}

	public String getMdfr() {
		return mdfr;
	}

	public void setMdfr(String mdfr) {
		this.mdfr = mdfr;
	}

	public String getModiDt() {
		return modiDt;
	}

	public void setModiDt(String modiDt) {
		this.modiDt = modiDt;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	
}