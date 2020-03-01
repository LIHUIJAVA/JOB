package com.px.mis.account.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 回冲单
 * @author 
 */
public class FormBackFlush implements Serializable {
    /**
     * 回冲单编码
     */
    private String backNum;

    /**
     * 单据号
     */
    private String formNum;

    /**
     * 单据日期
     */
    private String formDt;

    /**
     * 回冲单日期
     */
    private String backDt;

    /**
     * 是否审核
     */
    private Integer isNtChk;

    /**
     * 审核人
     */
    private String chkr;

    /**
     * 审核日期
     */
    private String chkTm;

    /**
     * 出入库类型编号
     */
    private String outIntoWhsTypId;

    /**
     * 出入库名称
     */
    private String outIntoWhsTypNm;

    /**
     * 业务类型编号
     */
    private String bizTypId;

    /**
     * 业务类型名称
     */
    private String bizTypNm;

    /**
     * 销售类型编号
     */
    private String sellTypId;

    /**
     * 销售类型名称
     */
    private String sellTypNm;

    /**
     * 采购类型编号
     */
    private String pursTypId;

    /**
     * 采购类型名称
     */
    private String pursTypNm;

    /**
     * 供应商编码
     */
    private String provrId;

    /**
     * 供应商名称
     */
    private String provrNm;

    /**
     * 收发类型编码
     */
    private String recvSendCateId;

    /**
     * 收发类型名称
     */
    private String recvSendCateNm;

    /**
     * 客户编号
     */
    private String custId;

    /**
     * 客户名称
     */
    private String custNm;

    /**
     * 用户编号
     */
    private String accNum;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 是否记账完成
     */
    private Integer isNtBookOk;

    /**
     * 记账人
     */
    private String bookOkAcc;

    /**
     * 记账日期
     */
    private String bookOkDt;

    /**
     * 是否开票
     */
    private Integer isNtBllg;

    /**
     * 是否结算
     */
    private Integer isNtStl;

    /**
     * 是否生成凭证
     */
    private Integer isNtMakeVouch;

    /**
     * 是否上传总账
     */
    private Integer isNtUploadHead;

    /**
     * 凭证编号
     */
    private String makeVouchId;

    /**
     * 凭证摘要
     */
    private String makeVouchAbst;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否月末记账
     */
    private Integer isMthEndStl;

    /**
     * 上级单据号
     */
    private String sellOrdrInd;

    /**
     * 是否红字回冲
     */
    private Integer isRedBack;
    
    private String setupPers;//制单人

    private String setupTm;//创建时间
    
    private List<FormBackFlushSub> formBackFlushSub;
    
    //单据类型
    private String formTypEncd; 
    //单据类型名称 
    private String formTypName;
     
    private static final long serialVersionUID = 1L;

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
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
        this.chkr = chkr;
    }

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
    }

    public String getBizTypId() {
        return bizTypId;
    }

    public void setBizTypId(String bizTypId) {
        this.bizTypId = bizTypId;
    }

    public String getBizTypNm() {
        return bizTypNm;
    }

    public void setBizTypNm(String bizTypNm) {
        this.bizTypNm = bizTypNm;
    }

    public String getSellTypId() {
        return sellTypId;
    }

    public void setSellTypId(String sellTypId) {
        this.sellTypId = sellTypId;
    }

    public String getSellTypNm() {
        return sellTypNm;
    }

    public void setSellTypNm(String sellTypNm) {
        this.sellTypNm = sellTypNm;
    }

    public String getPursTypId() {
        return pursTypId;
    }

    public void setPursTypId(String pursTypId) {
        this.pursTypId = pursTypId;
    }

    public String getPursTypNm() {
        return pursTypNm;
    }

    public void setPursTypNm(String pursTypNm) {
        this.pursTypNm = pursTypNm;
    }

    public String getProvrId() {
        return provrId;
    }

    public void setProvrId(String provrId) {
        this.provrId = provrId;
    }

    public String getProvrNm() {
        return provrNm;
    }

    public void setProvrNm(String provrNm) {
        this.provrNm = provrNm;
    }

    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId;
    }

    public String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustNm() {
        return custNm;
    }

    public void setCustNm(String custNm) {
        this.custNm = custNm;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIsNtBookOk() {
        return isNtBookOk;
    }

    public void setIsNtBookOk(Integer isNtBookOk) {
        this.isNtBookOk = isNtBookOk;
    }

    public String getBookOkAcc() {
        return bookOkAcc;
    }

    public void setBookOkAcc(String bookOkAcc) {
        this.bookOkAcc = bookOkAcc;
    }



    public Integer getIsNtBllg() {
        return isNtBllg;
    }

    public void setIsNtBllg(Integer isNtBllg) {
        this.isNtBllg = isNtBllg;
    }

    public Integer getIsNtStl() {
        return isNtStl;
    }

    public void setIsNtStl(Integer isNtStl) {
        this.isNtStl = isNtStl;
    }

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public Integer getIsNtUploadHead() {
        return isNtUploadHead;
    }

    public void setIsNtUploadHead(Integer isNtUploadHead) {
        this.isNtUploadHead = isNtUploadHead;
    }

    public String getMakeVouchId() {
        return makeVouchId;
    }

    public void setMakeVouchId(String makeVouchId) {
        this.makeVouchId = makeVouchId;
    }

    public String getMakeVouchAbst() {
        return makeVouchAbst;
    }

    public void setMakeVouchAbst(String makeVouchAbst) {
        this.makeVouchAbst = makeVouchAbst;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsMthEndStl() {
        return isMthEndStl;
    }

    public void setIsMthEndStl(Integer isMthEndStl) {
        this.isMthEndStl = isMthEndStl;
    }

    public String getSellOrdrInd() {
        return sellOrdrInd;
    }

    public void setSellOrdrInd(String sellOrdrInd) {
        this.sellOrdrInd = sellOrdrInd;
    }

    public Integer getIsRedBack() {
        return isRedBack;
    }

    public void setIsRedBack(Integer isRedBack) {
        this.isRedBack = isRedBack;
    }
    
    public List<FormBackFlushSub> getFormBackFlushSub() {
		return formBackFlushSub;
	}

	public void setFormBackFlushSub(List<FormBackFlushSub> formBackFlushSub) {
		this.formBackFlushSub = formBackFlushSub;
	}

	public String getFormDt() {
		return formDt;
	}

	public void setFormDt(String formDt) {
		this.formDt = formDt;
	}

	public String getBackDt() {
		return backDt;
	}

	public void setBackDt(String backDt) {
		this.backDt = backDt;
	}

	public String getBookOkDt() {
		return bookOkDt;
	}

	public void setBookOkDt(String bookOkDt) {
		this.bookOkDt = bookOkDt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSetupPers() {
		return setupPers;
	}

	public void setSetupPers(String setupPers) {
		this.setupPers = setupPers;
	}

	public String getSetupTm() {
		return setupTm;
	}

	public void setSetupTm(String setupTm) {
		this.setupTm = setupTm;
	}

	public String getFormTypEncd() {
		return formTypEncd;
	}

	public void setFormTypEncd(String formTypEncd) {
		this.formTypEncd = formTypEncd;
	}

	public String getFormTypName() {
		return formTypName;
	}

	public void setFormTypName(String formTypName) {
		this.formTypName = formTypName;
	}
	
	
}