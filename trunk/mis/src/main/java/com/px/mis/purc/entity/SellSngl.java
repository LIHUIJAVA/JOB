package com.px.mis.purc.entity;

import java.util.List;
//���۵�����
public class SellSngl {
    private String sellSnglId;//���۵����
    private String sellSnglDt;//���۵�����
    private String sellSnglDt1;//���۵�����1   
    private String sellSnglDt2;//���۵�����2    
    private String formTypEncd;//�������ͱ���
    private String accNum;//�û����    
    private String userName;//�û�����    
    private String deptId;//���ű��    
    private String deptName;//��������
    private String custId;//�ͻ����
    private String bizTypId;//ҵ�����ͱ��
    private String sellTypId;//�������ͱ��
    private String recvSendCateId;//�շ������
    private String delvAddr;//������ַ
    private String txId;//���ױ��    
    private String ecOrderId;//���̶�����    
    private String custOrdrNum;//�ͻ�������
    private Integer isNtBllg;//�Ƿ�Ʊ
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkTm;//���ʱ��
    private Integer isNtBookEntry;//�Ƿ����
    private String bookEntryPers;//������   
    private String bookEntryTm;//����ʱ��
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private String memo;//��ע    
    private String recvr;//�ռ���    
    private String recvrTel;//�ռ��˵绰    
    private String recvrAddr;//�ռ��˵�ַ    
    private String recvrEml;//�ռ�������   
    private String buyerNote;//�������   
    private Integer isPick;//�Ƿ������Ƿ����ɼ������ʶ��    
    private Integer deliverSelf;//�Ƿ��Է�����0�� 1�ǣ�
    //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String sellTypNm;//������������
    private String recvSendCateNm;//�շ��������
    private String bizTypNm;//ҵ����������
    private String formTypName;//������������
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
    private String makVouchPers;//��ƾ֤��
    private String makVouchTm;//��ƾ֤ʱ��
    private String custDelv;//�ͻ���ַ
    
    public String getCustDelv() {
		return custDelv;
	}

	public void setCustDelv(String custDelv) {
		this.custDelv = custDelv;
	}

	public String getMakVouchPers() {
		return makVouchPers;
	}

	public void setMakVouchPers(String makVouchPers) {
		this.makVouchPers = makVouchPers;
	}

	public String getMakVouchTm() {
		return makVouchTm;
	}

	public void setMakVouchTm(String makVouchTm) {
		this.makVouchTm = makVouchTm;
	}

	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}

	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	private List<SellSnglSub> sellSnglSub;
    
    public List<SellSnglSub> getSellSnglSub() {
		return sellSnglSub;
	}

	public Integer getDeliverSelf() {
		return deliverSelf;
	}

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
	}

	public void setDeliverSelf(Integer deliverSelf) {
		this.deliverSelf = deliverSelf;
	}

	public void setSellSnglSub(List<SellSnglSub> sellSnglSub) {
		this.sellSnglSub = sellSnglSub;
	}

	public String getSellSnglDt1() {
		return sellSnglDt1;
	}

	public void setSellSnglDt1(String sellSnglDt1) {
		this.sellSnglDt1 = sellSnglDt1;
	}

	public String getSellSnglDt2() {
		return sellSnglDt2;
	}

	public void setSellSnglDt2(String sellSnglDt2) {
		this.sellSnglDt2 = sellSnglDt2;
	}

	public String getBuyerNote() {
		return buyerNote;
	}

	public void setBuyerNote(String buyerNote) {
		this.buyerNote = buyerNote;
	}

	public String getRecvr() {
		return recvr;
	}

	public void setRecvr(String recvr) {
		this.recvr = recvr;
	}

	public String getRecvrTel() {
		return recvrTel;
	}

	public void setRecvrTel(String recvrTel) {
		this.recvrTel = recvrTel;
	}

	public String getRecvrAddr() {
		return recvrAddr;
	}

	public void setRecvrAddr(String recvrAddr) {
		this.recvrAddr = recvrAddr;
	}

	public String getRecvrEml() {
		return recvrEml;
	}

	public void setRecvrEml(String recvrEml) {
		this.recvrEml = recvrEml;
	}

	public String getEcOrderId() {
		return ecOrderId;
	}

	public void setEcOrderId(String ecOrderId) {
		this.ecOrderId = ecOrderId;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public Integer getIsPick() {
		return isPick;
	}

	public void setIsPick(Integer isPick) {
		this.isPick = isPick;
	}

	public String getSellSnglId() {
        return sellSnglId;
    }

    public void setSellSnglId(String sellSnglId) {
        this.sellSnglId = sellSnglId == null ? null : sellSnglId.trim();
    }

    public String getSellSnglDt() {
        return sellSnglDt;
    }

    public void setSellSnglDt(String sellSnglDt) {
        this.sellSnglDt = sellSnglDt;
    }

    public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}

	public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    public String getBizTypId() {
        return bizTypId;
    }

    public void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId == null ? null : bizTypId.trim();
    }

    public String getSellTypId() {
        return sellTypId;
    }

    public void setSellTypId(String sellTypId) {
        this.sellTypId = sellTypId == null ? null : sellTypId.trim();
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId == null ? null : recvSendCateId.trim();
    }

    public String getDelvAddr() {
        return delvAddr;
    }

    public void setDelvAddr(String delvAddr) {
        this.delvAddr = delvAddr == null ? null : delvAddr.trim();
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId == null ? null : txId.trim();
    }

    public Integer getIsNtBllg() {
        return isNtBllg;
    }

    public void setIsNtBllg(Integer isNtBllg) {
        this.isNtBllg = isNtBllg;
    }

   
    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr == null ? null : chkr.trim();
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public Integer getIsNtChk() {
		return isNtChk;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}

	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}

	public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers == null ? null : bookEntryPers.trim();
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSellTypNm() {
		return sellTypNm;
	}

	public void setSellTypNm(String sellTypNm) {
		this.sellTypNm = sellTypNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	}

}