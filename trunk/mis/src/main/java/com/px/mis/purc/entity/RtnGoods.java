package com.px.mis.purc.entity;

import java.util.List;

public class RtnGoods {
    private String rtnGoodsId;//退货单号
    private String rtnGoodsDt;//退货日期
    private String formTypEncd;//单据类型编码
    private String sellTypId;//销售类型编号
    private String bizTypId;//业务类型编号
    private String recvSendCateId;//收发类别编号
    private String custOrdrNum;//客户订单号
    private String custId;//客户编号
    private String accNum;//用户编号
    private String userName;//用户名称
    private String deptId;//部门编号
    private String deptName;//部门名称
    private String setupPers;//创建人
    private String setupTm;//创建时间
    private Integer isNtChk;//是否审核
    private String chkr;//审核人
    private String chkTm;//审核时间
    private String mdfr;//修改人
    private String modiTm;//修改时间
    private String delvAddrNm;//发货地址名称
    private String sellOrdrId;//销售单号
    private Integer isNtBllg;//是否开票
    private String recvr;//收件人
    private String recvrTel;//收件电话
    private String recvrAddr;//收件人地址
    private String recvrEml;//收件人邮箱
    private String buyerNote;//买家留言
    private String memo;//备注
    private String refId;//退款单编号
    private String txId;//交易编码
    
    private List<RtnGoodsSub> rtnGoodsSub;//退货单子表
    private String formTypName;//单据类型名称
   
    
   //关联查询客户名称、用户名称、部门名称、销售类型名称、收发类别名称、业务类型名称
    private String custNm;//客户名称
    private String sellTypNm;//销售类型名称
    private String recvSendCateNm;//收发类别名称
    private String bizTypNm;//业务类型名称
    private String toFormTypEncd;//来源单据类型编码
    private Integer isNtMakeVouch;//是否生成凭证
    private String makVouchPers;//制凭证人
    private String makVouchTm;//制凭证时间
    private String expressNum;//快递单号
    
    
    
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