package com.px.mis.purc.entity;

import java.util.List;
//ί�д���������
public class EntrsAgnAdj {
	
    private String adjSnglId;//���������

    private String adjSnglDt;//����������
    
    private String adjSnglDt1;//����������1
    
    private String adjSnglDt2;//����������2
    
    private String formTypEncd;//�������ͱ���

    private String sellTypId;//�������ͱ��

    private String bizTypId;//ҵ�����ͱ��

    private String recvSendCateId;//�շ������

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

    private String delvAddrNm;// ������ַ����

    private String delvSnglId;//ί�д������������

    private String sellInvId;//���۷�Ʊ��
    
    private String stlSnglId;//���۽��㵥���

    private String sellInvMasTabInd;//���۷�Ʊ�����ʶ

    private Integer isNtSellUse;//�Ƿ�����ʹ��

    private Integer isNtOutWhsBllg;//�Ƿ���⿪Ʊ

    private Integer isNtNeedBllg;//�Ƿ�Ʊ

    private String memo;//��ע
    
    private String delvSnglDt;//����������
    
    private String delvSnglDt1;//����������1
    
    private String delvSnglDt2;//����������2
    
    private Integer isNtStl;//�Ƿ����
    
    private String recvr;//�ռ���
    
    private String recvrTel;//�ռ��绰
    
    private String recvrAddr;//�ռ��˵�ַ
    
    private String recvrEml;//�ռ�������
    
    private String custOrdrNum;//�ͻ�������
    
    private List<EntrsAgnAdjSub> entrsAgnAdjSub;

    public Integer getIsNtStl() {
		return isNtStl;
	}

	public void setIsNtStl(Integer isNtStl) {
		this.isNtStl = isNtStl;
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

	public String getCustOrdrNum() {
		return custOrdrNum;
	}

	public void setCustOrdrNum(String custOrdrNum) {
		this.custOrdrNum = custOrdrNum;
	}

	public List<EntrsAgnAdjSub> getEntrsAgnAdjSub() {
		return entrsAgnAdjSub;
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

	public String getStlSnglId() {
		return stlSnglId;
	}

	public void setStlSnglId(String stlSnglId) {
		this.stlSnglId = stlSnglId;
	}

	public String getDelvSnglDt() {
		return delvSnglDt;
	}

	public void setDelvSnglDt(String delvSnglDt) {
		this.delvSnglDt = delvSnglDt;
	}

	public String getDelvSnglDt1() {
		return delvSnglDt1;
	}

	public void setDelvSnglDt1(String delvSnglDt1) {
		this.delvSnglDt1 = delvSnglDt1;
	}

	public String getDelvSnglDt2() {
		return delvSnglDt2;
	}

	public void setDelvSnglDt2(String delvSnglDt2) {
		this.delvSnglDt2 = delvSnglDt2;
	}

	public String getAdjSnglDt() {
		return adjSnglDt;
	}

	public String getAdjSnglDt1() {
		return adjSnglDt1;
	}

	public void setAdjSnglDt1(String adjSnglDt1) {
		this.adjSnglDt1 = adjSnglDt1;
	}

	public String getAdjSnglDt2() {
		return adjSnglDt2;
	}

	public void setAdjSnglDt2(String adjSnglDt2) {
		this.adjSnglDt2 = adjSnglDt2;
	}

	public void setAdjSnglDt(String adjSnglDt) {
		this.adjSnglDt = adjSnglDt;
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


	public void setEntrsAgnAdjSub(List<EntrsAgnAdjSub> entrsAgnAdjSub) {
		this.entrsAgnAdjSub = entrsAgnAdjSub;
	}

	public String getAdjSnglId() {
        return adjSnglId;
    }

    public void setAdjSnglId(String adjSnglId) {
        this.adjSnglId = adjSnglId == null ? null : adjSnglId.trim();
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

    public String getDelvSnglId() {
		return delvSnglId;
	}

	public void setDelvSnglId(String delvSnglId) {
		this.delvSnglId = delvSnglId;
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
    
    //������ѯ�ͻ����ơ��û����ơ��������ơ������������ơ��շ�������ơ�ҵ����������
    private String custNm;//�ͻ�����
    private String depName;//��������
    private String sellTypNm;//������������
    private String recvSendCateNm;//�շ��������
    private String bizTypNm;//ҵ����������

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

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
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

}