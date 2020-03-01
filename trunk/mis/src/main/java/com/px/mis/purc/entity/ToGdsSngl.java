package com.px.mis.purc.entity;

import java.util.List;
//到货单
public class ToGdsSngl {
	
    private String toGdsSnglId;//到货单编号
    private String toGdsSnglDt;//到货日期
    private String toGdsSnglDt1;//到货单日期1
    private String toGdsSnglDt2;//到货单日期2
    private String pursTypId;//采购类型编号
    private String formTypEncd;//单据类型编码
    private String provrId;//供应商编号
    private String deptId;//部门编号
    private String deptName;//部门名称
    private String accNum;//用户编号
    private String userName;//用户名称
    private String pursOrdrId;//采购订单编号
    private String provrOrdrNum;//供应商订单号
    private Integer isNtChk;//是否审核
    private String chkr;//审核人
    private String chkTm;//审核时间
    private String setupPers;//创建人
    private String setupTm;//创建时间
    private String mdfr;//修改人
    private String modiTm;//修改时间
    private String memo;//备注
    private Integer isNtRtnGood;//是否有退货
    private String dealStat;//处理状态
    private String returnMemo;//拒收备注
    private List<ToGdsSnglSub> toGdsSnglSub;//到货单子表集合
    //关联查询供应商名称、用户名称、部门名称、采购类型名称
    private String provrNm;//供应商名称
    private String pursTypNm;//采购类型名称
    private String formTypName;//单据类型名称
    private String toFormTypEncd;//来源单据类型编码

	public String getToFormTypEncd() {
		return toFormTypEncd;
	}

	public void setToFormTypEncd(String toFormTypEncd) {
		this.toFormTypEncd = toFormTypEncd;
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
    
    public List<ToGdsSnglSub> getToGdsSnglSub() {
		return toGdsSnglSub;
	}

	public Integer getIsNtRtnGood() {
		return isNtRtnGood;
	}

	public void setIsNtRtnGood(Integer isNtRtnGood) {
		this.isNtRtnGood = isNtRtnGood;
	}

	public String getToGdsSnglDt1() {
		return toGdsSnglDt1;
	}

	public void setToGdsSnglDt1(String toGdsSnglDt1) {
		this.toGdsSnglDt1 = toGdsSnglDt1;
	}

	public String getToGdsSnglDt2() {
		return toGdsSnglDt2;
	}

	public void setToGdsSnglDt2(String toGdsSnglDt2) {
		this.toGdsSnglDt2 = toGdsSnglDt2;
	}

	public String getReturnMemo() {
		return returnMemo;
	}

	public void setReturnMemo(String returnMemo) {
		this.returnMemo = returnMemo;
	}

	public void setToGdsSnglSub(List<ToGdsSnglSub> toGdsSnglSub) {
		this.toGdsSnglSub = toGdsSnglSub;
	}

	public String getPursOrdrId() {
		return pursOrdrId;
	}

	public void setPursOrdrId(String pursOrdrId) {
		this.pursOrdrId = pursOrdrId;
	}

	public String getToGdsSnglId() {
        return toGdsSnglId;
    }

    public void setToGdsSnglId(String toGdsSnglId) {
        this.toGdsSnglId = toGdsSnglId == null ? null : toGdsSnglId.trim();
    }

    public String getToGdsSnglDt() {
        return toGdsSnglDt;
    }

    public void setToGdsSnglDt(String toGdsSnglDt) {
        this.toGdsSnglDt = toGdsSnglDt;
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

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
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
    
	public String getDealStat() {
		return dealStat;
	}

	public void setDealStat(String dealStat) {
		this.dealStat = dealStat;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPursTypNm() {
		return pursTypNm;
	}

	public void setPursTypNm(String pursTypNm) {
		this.pursTypNm = pursTypNm;
	}
    
   
}