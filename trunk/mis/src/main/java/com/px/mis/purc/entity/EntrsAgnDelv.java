package com.px.mis.purc.entity;

import java.util.List;
//ί�д���������
public class EntrsAgnDelv {
	
    private String delvSnglId;//���������
    private String delvSnglDt;//����������
    private String formTypEncd;//�������ͱ���
    private String sellTypId;//�������ͱ��
    private String bizTypId;//ҵ�����ͱ��
    private String recvSendCateId;//�շ����ͱ��
    private String custId;//�ͻ����
    private String accNum;//�û����
    private String userName;//�û�����
    private String deptId;//���ű��
    private String deptName;//��������
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private Integer isNtBookEntry;//�Ƿ����
    private String bookEntryPers;//������
    private String bookEntryTm;//����ʱ��
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkDt;//���ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private String delvAddrId;//������ַ���
    private String delvAddrNm;//������ַ����
    private String sellOrdrId;//���۶�����
    private String sellInvId;//���۷�Ʊ��
    private String sellInvMasTabInd;//���۷�Ʊ�����ʶ
    private Integer isNtSellUse;//�Ƿ����� 
    private Integer isNtOutWhsBllg;//�Ƿ���⿪Ʊ
    private Integer isNtNeedBllg;//�Ƿ���Ҫ��Ʊ
    private Integer isNtStl;//�Ƿ����
    private String recvr;//�ռ���
    private String recvrTel;//�ռ��绰
    private String recvrAddr;//�ռ��˵�ַ
    private String recvrEml;//�ռ�������
    private Integer isPick;//�Ƿ��� 
    private Integer isNtRtnGood;//�Ƿ��˻�
    private String custOrdrNum;//�ͻ�������
	private String memo;//��ע
	 
    private List<EntrsAgnDelvSub> entrsAgnDelvSub;

    //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String sellTypNm;//������������
    private String recvSendCateNm;//�շ��������
    private String bizTypNm;//ҵ����������
    
    private String formTypName;//������������
    private Integer isNtMakeVouch;//�Ƿ�����ƾ֤
    private String makVouchPers;//��ƾ֤��
    private String makVouchTm;//��ƾ֤ʱ��
    
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

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
	}
	public List<EntrsAgnDelvSub> getEntrsAgnDelvSub() {
		return entrsAgnDelvSub;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public void setEntrsAgnDelvSub(List<EntrsAgnDelvSub> entrsAgnDelvSub) {
		this.entrsAgnDelvSub = entrsAgnDelvSub;
	}

	public Integer getIsNtStl() {
		return isNtStl;
	}

	public void setIsNtStl(Integer isNtStl) {
		this.isNtStl = isNtStl;
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

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public Integer getIsPick() {
		return isPick;
	}

	public void setIsPick(Integer isPick) {
		this.isPick = isPick;
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

	public String getDelvSnglId() {
        return delvSnglId;
    }

    public void setDelvSnglId(String delvSnglId) {
        this.delvSnglId = delvSnglId == null ? null : delvSnglId.trim();
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

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId == null ? null : recvSendCateId.trim();
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

    public String getDelvSnglDt() {
		return delvSnglDt;
	}

	public void setDelvSnglDt(String delvSnglDt) {
		this.delvSnglDt = delvSnglDt;
	}

	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}

	public String getBookEntryTm() {
		return bookEntryTm;
	}

	public void setBookEntryTm(String bookEntryTm) {
		this.bookEntryTm = bookEntryTm;
	}

	public String getChkDt() {
		return chkDt;
	}

	public void setChkDt(String chkDt) {
		this.chkDt = chkDt;
	}

	public String getModiTm() {
		return modiTm;
	}

	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}

	public String getBookEntryPers() {
        return bookEntryPers;
    }

    public void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers == null ? null : bookEntryPers.trim();
    }

    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr == null ? null : chkr.trim();
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getDelvAddrId() {
        return delvAddrId;
    }

    public void setDelvAddrId(String delvAddrId) {
        this.delvAddrId = delvAddrId == null ? null : delvAddrId.trim();
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

    public String getSellInvId() {
        return sellInvId;
    }

    public void setSellInvId(String sellInvId) {
        this.sellInvId = sellInvId == null ? null : sellInvId.trim();
    }

    public String getSellInvMasTabInd() {
        return sellInvMasTabInd;
    }

    public void setSellInvMasTabInd(String sellInvMasTabInd) {
        this.sellInvMasTabInd = sellInvMasTabInd == null ? null : sellInvMasTabInd.trim();
    }

    public Integer getIsNtSellUse() {
        return isNtSellUse;
    }

    public void setIsNtSellUse(Integer isNtSellUse) {
        this.isNtSellUse = isNtSellUse;
    }

    public Integer getIsNtOutWhsBllg() {
        return isNtOutWhsBllg;
    }

    public void setIsNtOutWhsBllg(Integer isNtOutWhsBllg) {
        this.isNtOutWhsBllg = isNtOutWhsBllg;
    }

    public Integer getIsNtNeedBllg() {
        return isNtNeedBllg;
    }

    public void setIsNtNeedBllg(Integer isNtNeedBllg) {
        this.isNtNeedBllg = isNtNeedBllg;
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

	public Integer getIsNtBookEntry() {
		return isNtBookEntry;
	}

	public void setIsNtBookEntry(Integer isNtBookEntry) {
		this.isNtBookEntry = isNtBookEntry;
	}

	public Integer getIsNtChk() {
		return isNtChk;
	}

	public void setIsNtChk(Integer isNtChk) {
		this.isNtChk = isNtChk;
	}

	public String getBizTypNm() {
		return bizTypNm;
	}

	public void setBizTypNm(String bizTypNm) {
		this.bizTypNm = bizTypNm;
	}

}