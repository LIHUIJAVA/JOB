package com.px.mis.purc.entity;

import java.util.List;

public class IntoWhs {
    private String intoWhsSnglId;//��ⵥ��
    private String intoWhsDt;//�������
    private String intoWhsDt1;//�������1
    private String intoWhsDt2;//�������2
    private String pursTypId;//�ɹ����ͱ��
    private String formTypEncd;//�������ͱ���
    private String outIntoWhsTypId;//��������ͱ��
    private String recvSendCateId;//�շ������
    private String provrId;//��Ӧ�̱��
    private String accNum;//�û����
    private String userName;//�û�����
    private String deptId;//���ű��
    private String deptName;//��������
    private String pursOrdrId;//�ɹ��������
    private String toGdsSnglId;//���������
    private String provrOrdrNum;//��Ӧ�̶�����
    private Integer isNtBllg;//�Ƿ�Ʊ
    private Integer isNtStl;//�Ƿ����
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkTm;//���ʱ��
    private Integer isNtClos;//�Ƿ�ر�
    private Integer isNtCmplt;//�Ƿ����
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private Integer isNtBookEntry;//�Ƿ����
    private String bookEntryPers;//������
    private String bookEntryTm;//����ʱ��
    private String exaRep;//������߱���
    private String memo;//��ע
    private List<IntoWhsSub> intoWhsSub;//�ɹ���ⵥ�ӱ�  
	private String returnMemo;//���ձ�ע
    private Integer isNtRtnGood;//�Ƿ����˻�
    private List<IntoWhsSub> iList;//--
    //������ѯ��Ӧ�����ơ��û����ơ��ɹ���������
    private String provrNm;//��Ӧ������
    private String pursTypNm;//�ɹ���������
    private String recvSendCateNm;//�շ����
    private String outIntoWhsTypNm;//�������������--
    
    //�������ˮ��
    private String formDt1;//����������
    private String formDt2;//����������
    private String formNum;//���ݺ�
    private String chkTm1;//���������
    private String chkTm2;//���������
    
    private String formTypName;//������������
    private String toFormTypEncd;//��Դ�������ͱ���
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

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}

	public String getIntoWhsDt1() {
		return intoWhsDt1;
	}

	public void setIntoWhsDt1(String intoWhsDt1) {
		this.intoWhsDt1 = intoWhsDt1;
	}

	public String getIntoWhsDt2() {
		return intoWhsDt2;
	}

	public void setIntoWhsDt2(String intoWhsDt2) {
		this.intoWhsDt2 = intoWhsDt2;
	}

	public List<IntoWhsSub> getIntoWhsSub() {
		return intoWhsSub;
	}

	public void setIntoWhsSub(List<IntoWhsSub> intoWhsSub) {
		this.intoWhsSub = intoWhsSub;
	}
	
	public String getOutIntoWhsTypId() {
		return outIntoWhsTypId;
	}

	public void setOutIntoWhsTypId(String outIntoWhsTypId) {
		this.outIntoWhsTypId = outIntoWhsTypId;
	}

	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getIntoWhsSnglId() {
        return intoWhsSnglId;
    }

    public void setIntoWhsSnglId(String intoWhsSnglId) {
        this.intoWhsSnglId = intoWhsSnglId == null ? null : intoWhsSnglId.trim();
    }

    public String getIntoWhsDt() {
        return intoWhsDt;
    }

    public void setIntoWhsDt(String intoWhsDt) {
        this.intoWhsDt = intoWhsDt;
    }

    public String getPursTypId() {
		return pursTypId;
	}

	public void setPursTypId(String pursTypId) {
		this.pursTypId = pursTypId;
	}

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId == null ? null : recvSendCateId.trim();
    }

    public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId == null ? null : provrId.trim();
    }

	public String getPursOrdrId() {
        return pursOrdrId;
    }

    public void setPursOrdrId(String pursOrdrId) {
        this.pursOrdrId = pursOrdrId == null ? null : pursOrdrId.trim();
    }

    public String getToGdsSnglId() {
        return toGdsSnglId;
    }

    public void setToGdsSnglId(String toGdsSnglId) {
        this.toGdsSnglId = toGdsSnglId == null ? null : toGdsSnglId.trim();
    }
    
    public String getProvrOrdrNum() {
        return provrOrdrNum;
    }

    public void setProvrOrdrNum(String provrOrdrNum) {
        this.provrOrdrNum = provrOrdrNum == null ? null : provrOrdrNum.trim();
    }

    public Integer getIsNtStl() {
		return isNtStl;
	}

	public void setIsNtStl(Integer isNtStl) {
		this.isNtStl = isNtStl;
	}

	public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
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

    public Integer getIsNtClos() {
        return isNtClos;
    }

    public void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
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

    public Integer getIsNtBllg() {
		return isNtBllg;
	}

	public void setIsNtBllg(Integer isNtBllg) {
		this.isNtBllg = isNtBllg;
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

    public String getBookEntryTm() {
        return bookEntryTm;
    }

    public void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public String getExaRep() {
        return exaRep;
    }

    public void setExaRep(String exaRep) {
        this.exaRep = exaRep == null ? null : exaRep.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public List<IntoWhsSub> getiList() {
		return iList;
	}

	public void setiList(List<IntoWhsSub> iList) {
		this.iList = iList;
	}

	public String getOutIntoWhsTypNm() {
		return outIntoWhsTypNm;
	}

	public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
		this.outIntoWhsTypNm = outIntoWhsTypNm;
	}

	public String getFormDt1() {
		return formDt1;
	}

	public void setFormDt1(String formDt1) {
		this.formDt1 = formDt1;
	}

	public String getFormDt2() {
		return formDt2;
	}

	public void setFormDt2(String formDt2) {
		this.formDt2 = formDt2;
	}

	public String getFormNum() {
		return formNum;
	}

	public void setFormNum(String formNum) {
		this.formNum = formNum;
	}

	public String getChkTm1() {
		return chkTm1;
	}

	public void setChkTm1(String chkTm1) {
		this.chkTm1 = chkTm1;
	}

	public String getChkTm2() {
		return chkTm2;
	}

	public void setChkTm2(String chkTm2) {
		this.chkTm2 = chkTm2;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPursTypNm() {
		return pursTypNm;
	}

	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}

	public String getRecvSendCateNm() {
		return recvSendCateNm;
	}

	public void setRecvSendCateNm(String recvSendCateNm) {
		this.recvSendCateNm = recvSendCateNm;
	} 
    
}