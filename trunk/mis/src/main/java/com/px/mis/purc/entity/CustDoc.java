package com.px.mis.purc.entity;
/*�ͻ�����*/
import java.math.BigDecimal;

public class CustDoc {
    private String custId; //�ͻ�id
    private String custNm; //�ͻ�����
    private String custShtNm; //�ͻ����
    private String clsId;//�ͻ�������
    private BigDecimal recvblBal;//Ӧ�����
    private String ltstInvTm;//�����Ʊʱ��
    private BigDecimal ltstInvAmt;//�����Ʊ���
    private String ltstRecvTm;//����տ�ʱ��
    private BigDecimal ltstRecvAmt;//����տ���
    private String bizLicsPic;//Ӫҵִ����Ƭ
    private String bankOpenAcctLics;//���п������֤
    private String contcr;//��ϵ��
    private String tel;//�绰
    private String addr;//��ַ
    private String sctyCrdtCd;//ͳһ������ô���
    private String memo;//��ע
    private String bllgCorp;//��Ʊ��λ
    private String opnBnk;//��������
    private String bkatNum;//�����˺�
    private String bankEncd;//�������б��
    private String delvAddr;//������ַ
    private String devDt; //��չ����
    private String setupPers;//������
    private String setupDt;//����ʱ��
    private String mdfr;//�޸���
    private String modiDt;//�޸�ʱ��
    private BigDecimal taxRate;//˰��
	private String custTotlCorp;//�ͻ��ܹ�˾
	private String custTotlCorpId;//�ͻ��ܹ�˾����
	
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