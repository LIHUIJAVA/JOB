package com.px.mis.purc.entity;

import java.util.List;
//委托代销发货单
public class EntrsAgnDelv {
	
    private String delvSnglId;//发货单编号
    private String delvSnglDt;//发货单日期
    private String formTypEncd;//单据类型编码
    private String sellTypId;//销售类型编号
    private String bizTypId;//业务类型编号
    private String recvSendCateId;//收发类型编号
    private String custId;//客户编号
    private String accNum;//用户编号
    private String userName;//用户名称
    private String deptId;//部门编号
    private String deptName;//部门名称
    private String setupPers;//创建人
    private String setupTm;//创建时间
    private Integer isNtBookEntry;//是否记账
    private String bookEntryPers;//记账人
    private String bookEntryTm;//记账时间
    private Integer isNtChk;//是否审核
    private String chkr;//审核人
    private String chkDt;//审核时间
    private String mdfr;//修改人
    private String modiTm;//修改时间
    private String delvAddrId;//发货地址编号
    private String delvAddrNm;//发货地址名称
    private String sellOrdrId;//销售订单号
    private String sellInvId;//销售发票号
    private String sellInvMasTabInd;//销售发票主表标识
    private Integer isNtSellUse;//是否销售 
    private Integer isNtOutWhsBllg;//是否出库开票
    private Integer isNtNeedBllg;//是否需要开票
    private Integer isNtStl;//是否结算
    private String recvr;//收件人
    private String recvrTel;//收件电话
    private String recvrAddr;//收件人地址
    private String recvrEml;//收件人邮箱
    private Integer isPick;//是否拣货 
    private Integer isNtRtnGood;//是否退货
    private String custOrdrNum;//客户订单号
	private String memo;//备注
	 
    private List<EntrsAgnDelvSub> entrsAgnDelvSub;

    //关联查询客户名称、用户名称、部门名称、销售类型名称、收发类别名称、业务类型名称
    private String custNm;//客户名称
    private String sellTypNm;//销售类型名称
    private String recvSendCateNm;//收发类别名称
    private String bizTypNm;//业务类型名称
    
    private String formTypName;//单据类型名称
    private Integer isNtMakeVouch;//是否生成凭证
    private String makVouchPers;//制凭证人
    private String makVouchTm;//制凭证时间
    
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