package com.px.mis.purc.entity;

import java.util.List;

public class RtnGoods {
    private String rtnGoodsId;//�˻�����
    private String rtnGoodsDt;//�˻�����
    private String formTypEncd;//�������ͱ���
    private String sellTypId;//�������ͱ��
    private String bizTypId;//ҵ�����ͱ��
    private String recvSendCateId;//�շ������
    private String custOrdrNum;//�ͻ�������
    private String custId;//�ͻ����
    private String accNum;//�û����
    private String userName;//�û�����
    private String deptId;//���ű��
    private String deptName;//��������
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkTm;//���ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private String delvAddrNm;//������ַ����
    private String sellOrdrId;//���۵���
    private Integer isNtBllg;//�Ƿ�Ʊ
    private String recvr;//�ռ���
    private String recvrTel;//�ռ��绰
    private String recvrAddr;//�ռ��˵�ַ
    private String recvrEml;//�ռ�������
    private String buyerNote;//�������
    private String memo;//��ע
    private String refId;//�˿���
    private String txId;//���ױ���
    
    private List<RtnGoodsSub> rtnGoodsSub;//�˻����ӱ�
    private String formTypName;//������������
   
    
   //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String sellTypNm;//������������
    private String recvSendCateNm;//�շ��������
    private String bizTypNm;//ҵ����������
    private String toFormTypEncd;//��Դ�������ͱ���
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
    private String makVouchPers;//��ƾ֤��
    private String makVouchTm;//��ƾ֤ʱ��
    private String expressNum;//��ݵ���
    
    
    
    public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
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

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
	}

	public Integer getIsNtMakeVouch() {
		return isNtMakeVouch;
	}

	public void setIsNtMakeVouch(Integer isNtMakeVouch) {
		this.isNtMakeVouch = isNtMakeVouch;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getTxId() {
		return txId;
	}

	public void setTxId(String txId) {
		this.txId = txId;
	}

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
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

	public List<RtnGoodsSub> getRtnGoodsSub() {
		return rtnGoodsSub;
	}

	public void setRtnGoodsSub(List<RtnGoodsSub> rtnGoodsSub) {
		this.rtnGoodsSub = rtnGoodsSub;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRecvSendCateId() {
		return recvSendCateId;
	}

	public void setRecvSendCateId(String recvSendCateId) {
		this.recvSendCateId = recvSendCateId;
	}

	public String getRtnGoodsId() {
        return rtnGoodsId;
    }

    public void setRtnGoodsId(String rtnGoodsId) {
        this.rtnGoodsId = rtnGoodsId == null ? null : rtnGoodsId.trim();
    }

    public String getRtnGoodsDt() {
        return rtnGoodsDt;
    }

    public void setRtnGoodsDt(String rtnGoodsDt) {
        this.rtnGoodsDt = rtnGoodsDt;
    }

    public String getSellTypId() {
        return sellTypId;
    }

    public void setSellTypId(String sellTypId) {
        this.sellTypId = sellTypId == null ? null : sellTypId.trim();
    }

    public String getBizTypId() {
        return bizTypId;
    }

    public void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId == null ? null : bizTypId.trim();
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId == null ? null : custId.trim();
    }

    public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
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

    public Integer getIsNtChk() {
		return isNtChk;
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

    public String getDelvAddrNm() {
        return delvAddrNm;
    }

    public void setDelvAddrNm(String delvAddrNm) {
        this.delvAddrNm = delvAddrNm == null ? null : delvAddrNm.trim();
    }

    public String getSellOrdrId() {
        return sellOrdrId;
    }

    public void setSellOrdrId(String sellOrdrId) {
        this.sellOrdrId = sellOrdrId == null ? null : sellOrdrId.trim();
    }

    public Integer getIsNtBllg() {
		return isNtBllg;
	}

	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}
    
}