package com.px.mis.account.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * form_book_entry
 * @author 
 */
public class FormBookEntry implements Serializable {
	
	public FormBookEntry() {}
	
	public FormBookEntry(String formNum, String formDt,Integer isNtChk,String chkr,String chkTm,String outIntoWhsTypId,String outIntoWhsTypNm, 
			String bizTypId, String bizTypNm, String sellTypId, String sellTypNm,String pursTypId, String pursTypNm, String provrId,
			String provrNm, String custId, String custNm,String accNum, String userName, Integer isNtBookOk, Integer isNtMakeVouch, String memo,
			Integer isNtBllg,Integer isNtStl,Integer isMthEndStl,String sellOrdrInd,String setupPers,String setupTm,String formTypEncd,
			String formTypName,String recvSendCateId,String recvSendCateNm,String deptId,String deptName) {
		super();
		this.formNum = formNum;
		this.formDt = formDt;
		this.isNtChk = isNtChk;
		this.chkr = chkr;
		this.chkTm = chkTm;
		this.outIntoWhsTypId = outIntoWhsTypId;
		this.outIntoWhsTypNm = outIntoWhsTypNm;
		this.bizTypId = bizTypId;
		this.bizTypNm = bizTypNm;
		this.sellTypId = sellTypId;
		this.sellTypNm = sellTypNm;
		this.pursTypId = pursTypId;
		this.pursTypNm = pursTypNm;
		this.provrId = provrId;
		this.provrNm = provrNm;
		this.custId = custId;
		this.custNm = custNm;
		this.accNum = accNum;
		this.userName = userName;
		this.isNtBookOk = isNtBookOk;
		this.isNtMakeVouch = isNtMakeVouch;
		this.memo = memo;
		this.isNtBllg = isNtBllg;
		this.isNtStl = isNtStl;
		this.isMthEndStl = isMthEndStl;
		this.sellOrdrInd = sellOrdrInd;
		this.setupPers = setupPers;
		this.setupTm = setupTm;
		this.formTypEncd = formTypEncd;
		this.formTypName = formTypName;
		this.recvSendCateId = recvSendCateId;
		this.recvSendCateNm = recvSendCateNm;
		this.deptId = deptId;
		this.deptName = deptName;
	}
	
	public FormBookEntry(String formNum, String formDt, Integer isNtChk, String chkr, String chkTm1, String outIntoWhsTypId,
			String outIntoWhsTypNm, String bizTypId, String bizTypNm, String sellTypId, String sellTypNm,
			String pursTypId, String pursTypNm, String provrId, String provrNm, String custId, String custNm,
			String accNum, String userName, Integer isNtBookOk, Integer isNtMakeVouch, String memo,Integer isNtBllg,
			Integer isNtStl, Integer isNtUploadHead, String bookName, String bookOkDt, String recvSendCateId,
			String recvSendCateNm,Integer isMthEndStl) {
		super();
		this.formNum = formNum;
		this.isNtChk = isNtChk;
		this.chkr = chkr;
		this.outIntoWhsTypId = outIntoWhsTypId;
		this.outIntoWhsTypNm = outIntoWhsTypNm;
		this.bizTypId = bizTypId;
		this.bizTypNm = bizTypNm;
		this.sellTypId = sellTypId;
		this.sellTypNm = sellTypNm;
		this.pursTypId = pursTypId;
		this.pursTypNm = pursTypNm;
		this.provrId = provrId;
		this.provrNm = provrNm;
		this.custId = custId;
		this.custNm = custNm;
		this.accNum = accNum;
		this.userName = userName;
		this.isNtBookOk = isNtBookOk;
		this.isNtMakeVouch = isNtMakeVouch;
		this.memo = memo;
		this.formDt = formDt;
		this.chkTm = chkTm1;
		this.isNtBllg = isNtBllg;
		this.isNtStl = isNtStl;
		this.isNtUploadHead = isNtUploadHead;
		this.bookOkAcc = bookName;
		this.bookOkDt = bookOkDt;
		this.recvSendCateId = recvSendCateId;
		this.recvSendCateNm = recvSendCateNm;
		this.isMthEndStl = isMthEndStl;
	}

	/**
     * 单据号
     */
	@JsonProperty("单据号")
    private String formNum;

    /**
     * 单据日期
     */
	@JsonProperty("单据日期")
    private String formDt;

    /**
     * 是否审核
     */
	@JsonProperty("是否审核")
    private Integer isNtChk;

    /**
     * 审核人
     */
	@JsonProperty("审核人")
    private String chkr;

    /**
     * 审核日期
     */
	@JsonProperty("审核日期")
    private String chkTm;

    /**
     * 出入库类型编码
     */
	@JsonProperty("出入库类型编码")
    private String outIntoWhsTypId;

    /**
     * 出入库名称
     */
	@JsonProperty("出入库名称")
    private String outIntoWhsTypNm;

    /**
     * 业务类型编码
     */
	@JsonProperty("业务类型编码")
    private String bizTypId;

    /**
     * 业务类型名称
     */
	@JsonProperty("业务类型名称")
    private String bizTypNm;

    /**
     * 销售类型编码
     */
	@JsonProperty("销售类型编码")
    private String sellTypId;

    /**
     * 销售类型名称
     */
	@JsonProperty("销售类型名称")
    private String sellTypNm;

    /**
     * 采购类型编码
     */
	@JsonProperty("采购类型编码")
    private String pursTypId;

    /**
     * 采购类型名称
     */
	@JsonProperty("采购类型名称")
    private String pursTypNm;

    /**
     * 供应商编码
     */
	@JsonProperty("供应商编码")
    private String provrId;

    /**
     * 供应商名称
     */
	@JsonProperty("供应商名称")
    private String provrNm;

    /**
     * 客户编码
     */
	@JsonProperty("客户编码")
    private String custId;

    /**
     * 客户名称
     */
	@JsonProperty("客户名称")
    private String custNm;

    /**
     * 用户编码
     */
	@JsonProperty("用户编码")
    private String accNum;

    /**
     * 用户名称
     */
	@JsonProperty("用户名称")
    private String userName;

    /**
     * 是否记账完成
     */
	@JsonProperty("是否记账完成")
    private Integer isNtBookOk;
    
    /**
     * 是否生成凭证
     */
	@JsonProperty("是否生成凭证")
    private Integer isNtMakeVouch;

    /**
     * 备注
     */
	@JsonProperty("表头备注")
    private String memo;
	
	@JsonInclude(Include.NON_NULL)
    private List<FormBookEntrySub> bookEntrySub;
	@JsonIgnore
    private FormBookEntrySub formBookEntrySub;
    
	@JsonProperty("是否开票")
    private Integer isNtBllg;//是否开票
	@JsonProperty("是否结算")
    private Integer isNtStl;//是否结算
	@JsonProperty("是否上传总账")
    private Integer isNtUploadHead; //是否上传总账
	@JsonProperty("记账人")
    private String  bookOkAcc; //记账人
	@JsonProperty("记账时间")
    private String bookOkDt; //记账时间
	@JsonProperty("收发类型编码")
    private String recvSendCateId;//收发类型编码
	@JsonProperty("收发类型名称")
    private String recvSendCateNm;//收发类型名称
	@JsonProperty("凭证编码")
    private String makeVouchId;//凭证编码
	@JsonProperty("凭证摘要")
    private String makeVouchAbst;//凭证摘要
	@JsonProperty("是否月末结账")
    private Integer isMthEndStl; //是否月末结账
	@JsonProperty("销售订单号")
    private String sellOrdrInd;//销售订单号
	@JsonProperty("制单人")
    private String setupPers;//制单人
	@JsonProperty("创建时间")
    private String setupTm;//创建时间
	@JsonProperty("单据类型编码")
    private String formTypEncd;//单据类型编码
	@JsonProperty("单据类型名称")
    private String formTypName;//单据类型名称
	@JsonProperty("制凭证人")
    private String makVouchPers;//制凭证人
	@JsonProperty("制凭证时间")
    private String makVouchTm;//制凭证时间
	@JsonProperty("部门编码")
    private String deptId;//部门编码
	@JsonProperty("部门名称")
    private String deptName;//部门名称
    
    private String prdcDt;//生产日期
    
    
	public String getPrdcDt() {
		return prdcDt;
	}

	public void setPrdcDt(String prdcDt) {
		this.prdcDt = prdcDt;
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
    private static final long serialVersionUID = 1L;

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public String getFormDt() {
		return formDt;
	}

	public void setFormDt(String formDt) {
		this.formDt = formDt;
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

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

	public List<FormBookEntrySub> getBookEntrySub() {
		return bookEntrySub;
	}

	public void setBookEntrySub(List<FormBookEntrySub> bookEntrySub) {
		this.bookEntrySub = bookEntrySub;
	}
	
	public FormBookEntrySub getFormBookEntrySub() {
		return formBookEntrySub;
	}

	public void setFormBookEntrySub(FormBookEntrySub formBookEntrySub) {
		this.formBookEntrySub = formBookEntrySub;
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
	
	public Integer getIsNtUploadHead() {
		return isNtUploadHead;
	}

	public void setIsNtUploadHead(Integer isNtUploadHead) {
		this.isNtUploadHead = isNtUploadHead;
	}


	public String getBookOkAcc() {
		return bookOkAcc;
	}

	public void setBookOkAcc(String bookOkAcc) {
		this.bookOkAcc = bookOkAcc;
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

	@Override
	public String toString() {
		return "FormBookEntry [formNum=" + formNum + ", formDt=" + formDt + ", isNtChk=" + isNtChk + ", chkr=" + chkr
				+ ", chkTm=" + chkTm + ", outIntoWhsTypId=" + outIntoWhsTypId + ", outIntoWhsTypNm=" + outIntoWhsTypNm
				+ ", bizTypId=" + bizTypId + ", bizTypNm=" + bizTypNm + ", sellTypId=" + sellTypId + ", sellTypNm="
				+ sellTypNm + ", pursTypId=" + pursTypId + ", pursTypNm=" + pursTypNm + ", provrId=" + provrId
				+ ", provrNm=" + provrNm + ", custId=" + custId + ", custNm=" + custNm + ", accNum=" + accNum
				+ ", userName=" + userName + ", isNtBookOk=" + isNtBookOk + ", isNtMakeVouch=" + isNtMakeVouch
				+ ", memo=" + memo + "]";
	}
    
}