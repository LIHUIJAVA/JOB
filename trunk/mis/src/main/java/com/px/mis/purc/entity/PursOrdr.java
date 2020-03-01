package com.px.mis.purc.entity;

import java.util.List;

public class PursOrdr {
	
    private String pursOrdrId;//采购订单编号
    private String pursOrdrDt;//采购订单日期
    private String pursOrdrDt1;//采购订单日期1
    private String pursOrdrDt2;//采购订单日期2
    private String pursTypId;//采购类型编号
    private String formTypEncd;//单据类型编码
	private String provrId;//供应商编号
	private String accNum;//用户编号
	private String userName;//用户名称
	private String deptId;//部门编码
	private String deptName;//部门名称
    private String provrOrdrNum;//供应商订单号
    private Integer isNtChk;//是否审核
    private String chkr;//审核人
    private String chkTm;//审核时间
    private String setupPers;//创建人
    private String setupTm;//创建时间
    private String mdfr;//修改人
    private String modiTm;//修改时间
    private String memo;//备注
    private List<PursOrdrSub> pursOrdrSub;//采购订单子表信息
    //关联查询供应商名称、用户名称、部门名称、采购类型名称
    private String provrNm;//供应商名称
    private String pursTypNm;//采购类型名称
    private String formTypName;//单据类型名称

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