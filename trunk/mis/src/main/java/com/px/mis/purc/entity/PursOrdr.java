package com.px.mis.purc.entity;

import java.util.List;

public class PursOrdr {
	
    private String pursOrdrId;//�ɹ��������
    private String pursOrdrDt;//�ɹ���������
    private String pursOrdrDt1;//�ɹ���������1
    private String pursOrdrDt2;//�ɹ���������2
    private String pursTypId;//�ɹ����ͱ��
    private String formTypEncd;//�������ͱ���
	private String provrId;//��Ӧ�̱��
	private String accNum;//�û����
	private String userName;//�û�����
	private String deptId;//���ű���
	private String deptName;//��������
    private String provrOrdrNum;//��Ӧ�̶�����
    private Integer isNtChk;//�Ƿ����
    private String chkr;//�����
    private String chkTm;//���ʱ��
    private String setupPers;//������
    private String setupTm;//����ʱ��
    private String mdfr;//�޸���
    private String modiTm;//�޸�ʱ��
    private String memo;//��ע
    private List<PursOrdrSub> pursOrdrSub;//�ɹ������ӱ���Ϣ
    //������ѯ��Ӧ�����ơ��û����ơ��������ơ��ɹ���������
    private String provrNm;//��Ӧ������
    private String pursTypNm;//�ɹ���������
    private String formTypName;//������������

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

	public List<PursOrdrSub> getPursOrdrSub() {
		return pursOrdrSub;
	}

	public void setPursOrdrSub(List<PursOrdrSub> pursOrdrSub) {
		this.pursOrdrSub = pursOrdrSub;
	}

	public String getPursOrdrId() {
        return pursOrdrId;
    }

    public void setPursOrdrId(String pursOrdrId) {
        this.pursOrdrId = pursOrdrId == null ? null : pursOrdrId.trim();
    }

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPursTypId() {
        return pursTypId;
    }

    public void setPursTypId(String pursTypId) {
        this.pursTypId = pursTypId == null ? null : pursTypId.trim();
    }

    public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId == null ? null : provrId.trim();
    }
    
    public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

    public String getProvrOrdrNum() {
        return provrOrdrNum;
    }

    public void setProvrOrdrNum(String provrOrdrNum) {
        this.provrOrdrNum = provrOrdrNum == null ? null : provrOrdrNum.trim();
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

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers == null ? null : setupPers.trim();
    } 

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr == null ? null : mdfr.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

	public String getPursOrdrDt() {
		return pursOrdrDt;
	}

	public void setPursOrdrDt(String pursOrdrDt) {
		this.pursOrdrDt = pursOrdrDt;
	}

	public String getPursOrdrDt1() {
		return pursOrdrDt1;
	}

	public void setPursOrdrDt1(String pursOrdrDt1) {
		this.pursOrdrDt1 = pursOrdrDt1;
	}

	public String getPursOrdrDt2() {
		return pursOrdrDt2;
	}

	public void setPursOrdrDt2(String pursOrdrDt2) {
		this.pursOrdrDt2 = pursOrdrDt2;
	}

	public String getChkTm() {
		return chkTm;
	}

	public void setChkTm(String chkTm) {
		this.chkTm = chkTm;
	}

	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}

	public String getModiTm() {
		return modiTm;
	}

	public void setModiTm(String modiTm) {
		this.modiTm = modiTm;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}

	public String getProvrNm() {
		return provrNm;
	}

	public void setProvrNm(String provrNm) {
		this.provrNm = provrNm;
	}

	public String getPursTypNm() {
		return pursTypNm;
	}

	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}
	
}