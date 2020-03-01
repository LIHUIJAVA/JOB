package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//其他出入库单
public class OthOutIntoWhsMap {
//	private String formNum;// 单号
//	private String formDt;// 单据日期
//	private String whsEncd;// 仓库编码
//	private String srcFormNum;// 来源单据号
//	private String outIntoWhsTyp;// 出入库类型
//	private String outStatus;// 出库状态
//	private String inStatus;// 入库状态
//	private String memo;// 备注
//	private Integer isNtWms;// 是否向WMS上传
//	private Integer isNtChk;// 是否审核
//	private Integer isNtBookEntry;// 是否记账
//	private Integer isNtCmplt;// 是否完成
//	private Integer isNtClos;// 是否关闭
//	private Integer printCnt;// 打印次数
//	private String setupPers;// 创建人
//	private String setupTm;// 创建时间
//	private String mdfr;// 修改人
//	private String modiTm;// 修改时间
//	private String chkr;// 审核人
//	private String chkTm;// 审核时间
//	private String bookEntryPers;// 记账人
//	private String bookEntryTm;// 记账时间
//	private String operator;// 操作人
//	private String operatorId;// 操作人编码
//	private String outIntoWhsTypId;// 出入库类型编码
//	private String outIntoWhsTypNm;// 出入库类型名称
//	private String whsNm;// 仓库名称
//	private String formTypEncd;// 单据类型编码
//	private String formTypName;// 单据类型名称
//	private Integer isNtMakeVouch;// 是否生成凭证
//	private String makVouchPers;// 制凭证人
//	private String makVouchTm;// 制凭证时间
//
//	// 子
//	private Long ordrNum;// 序号
//	private String invtyEncd;// 存货编码
//	private BigDecimal qty;// 数量
//	private BigDecimal taxRate;// 税率
//	private BigDecimal cntnTaxUprc;// 含税单价
//	private BigDecimal unTaxUprc;// 未税单价[单价(根据批次带入单价)]
//	private BigDecimal bookEntryUprc;// 记账单价
//	private BigDecimal cntnTaxAmt;// 含税金额
//	private BigDecimal unTaxAmt;// 未税金额
//	private String intlBat;// 国际批号
//	private String batNum;// 批号
//	private String prdcDt;// 生产日期
//	private String baoZhiQi;// 保质期
//	private String invldtnDt;// 失效日期
//
//	// 联查存货档案字段、计量单位名称、仓库名称
//	private String invtyNm;// 存货名称
//	private String spcModel;// 规格型号
//	private String invtyCd;// 存货代码
//	private BigDecimal bxRule;// 箱规
//	private BigDecimal bxQty;// 箱数
//	private String measrCorpNm;// 计量单位名称
//	private String crspdBarCd;// 对应条形码
//	private String invtyClsEncd;// 存货分类编码
//	private String invtyClsNm;// 存货分类名称
//	private String measrCorpId;// 计量单位编码
//	private String memos;// 行备注

    @JsonProperty("单据号")
    private String formNum;// 单号
    @JsonProperty("单据日期")
    private String formDt;// 单据日期
    @JsonProperty("仓库编码")
    private String whsEncd;// 仓库编码
    @JsonProperty("来源单据号")
    private String srcFormNum;// 来源单据号
//    @JsonProperty("出入库类型")
    @JsonIgnore
    private String outIntoWhsTyp;// 出入库类型
    //	@JsonProperty("出库状态")
    @JsonIgnore
    private String outStatus;// 出库状态
    //	@JsonProperty("入库状态")
    @JsonIgnore
    private String inStatus;// 入库状态
    @JsonProperty("备注")
    private String memo;// 备注
    //	@JsonProperty("是否向WMS上传")
    @JsonIgnore
    private Integer isNtWms;// 是否向WMS上传
    @JsonProperty("是否审核")
    private Integer isNtChk;// 是否审核
    	@JsonProperty("是否记账")
//    @JsonIgnore
    private Integer isNtBookEntry;// 是否记账
//    	@JsonProperty("是否完成")
    @JsonIgnore
    private Integer isNtCmplt;// 是否完成
    //	@JsonProperty("是否关闭")
    @JsonIgnore
    private Integer isNtClos;// 是否关闭
    //	@JsonProperty("打印次数")
    @JsonIgnore
    private Integer printCnt;// 打印次数
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    @JsonProperty("审核人")
    private String chkr;// 审核人
    @JsonProperty("审核时间")
    private String chkTm;// 审核时间
    @JsonProperty("记账人")
    private String bookEntryPers;// 记账人
    @JsonProperty("记账时间")
    private String bookEntryTm;// 记账时间
    //	@JsonProperty("操作人")
    @JsonIgnore
    private String operator;// 操作人
    //	@JsonProperty("操作人编码")
    @JsonIgnore
    private String operatorId;// 操作人编码
    @JsonProperty("出入库类型编码")
    private String outIntoWhsTypId;// 出入库类型编码
    @JsonProperty("出入库类型名称")
    private String outIntoWhsTypNm;// 出入库类型名称
    @JsonProperty("仓库名称")
    private String whsNm;// 仓库名称
    @JsonProperty("单据类型编码")
    private String formTypEncd;// 单据类型编码
    @JsonProperty("单据类型名称")
    private String formTypName;// 单据类型名称
    @JsonProperty("是否生成凭证")
    private Integer isNtMakeVouch;// 是否生成凭证
    @JsonProperty("制凭证人")
    private String makVouchPers;// 制凭证人
    @JsonProperty("制凭证时间")
    private String makVouchTm;// 制凭证时间
    @JsonProperty("收发类别编码")
    private String recvSendCateId;// 收发类别id
    @JsonProperty("收发类别名称")
    private String recvSendCateNm;// 收发类别名称
    @JsonProperty("调拨单出库仓库编码")
    private String  outWhsEncd;
    @JsonProperty("调拨单入库仓库编码")
    private String   inWhsEncd;
    @JsonProperty("调拨单出库仓库名称")
    private String   outWhsNm;
    @JsonProperty("调拨单入库仓库名称")
    private String   inWhsNm;

    //	@JsonProperty("序号")
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("存货编码")
    private String invtyEncd;// 存货编码
    @JsonProperty("数量")
    private BigDecimal qty;// 数量
    @JsonProperty("税率")
    private BigDecimal taxRate;// 税率
    @JsonProperty("含税单价")
    private BigDecimal cntnTaxUprc;// 含税单价
    @JsonProperty("未税单价")
    private BigDecimal unTaxUprc;// 未税单价[单价(根据批次带入单价)]
    @JsonProperty("记账单价")
    private BigDecimal bookEntryUprc;// 记账单价
    @JsonProperty("含税金额")
    private BigDecimal cntnTaxAmt;// 含税金额
    @JsonProperty("未税金额")
    private BigDecimal unTaxAmt;// 未税金额
    @JsonProperty("税额")
    private BigDecimal taxAmt;//税额
    @JsonProperty("国际批号")
    private String intlBat;// 国际批号
    @JsonProperty("批号")
    private String batNum;// 批号
    @JsonProperty("生产日期")
    private String prdcDt;// 生产日期
    @JsonProperty("保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("存货名称")
    private String invtyNm;// 存货名称
    @JsonProperty("规格型号")
    private String spcModel;// 规格型号
    @JsonProperty("存货代码")
    private String invtyCd;// 存货代码
    @JsonProperty("箱规")
    private BigDecimal bxRule;// 箱规
    @JsonProperty("箱数")
    private BigDecimal bxQty;// 箱数
    @JsonProperty("计量单位名称")
    private String measrCorpNm;// 计量单位名称
    @JsonProperty("对应条形码")
    private String crspdBarCd;// 对应条形码
    @JsonProperty("存货分类编码")
    private String invtyClsEncd;// 存货分类编码
    @JsonProperty("存货分类名称")
    private String invtyClsNm;// 存货分类名称
    @JsonProperty("计量单位编码")
    private String measrCorpId;// 计量单位编码
    @JsonProperty("子表备注")
    private String memos;// 行备注
    @JsonProperty("项目编码")
    private String projEncd;// 项目编码
    @JsonProperty("项目名称")
    private String projNm;// 项目名称

    public String getOutWhsEncd() {
        return outWhsEncd;
    }

    public void setOutWhsEncd(String outWhsEncd) {
        this.outWhsEncd = outWhsEncd;
    }

    public String getInWhsEncd() {
        return inWhsEncd;
    }

    public void setInWhsEncd(String inWhsEncd) {
        this.inWhsEncd = inWhsEncd;
    }

    public String getOutWhsNm() {
        return outWhsNm;
    }

    public void setOutWhsNm(String outWhsNm) {
        this.outWhsNm = outWhsNm;
    }

    public String getInWhsNm() {
        return inWhsNm;
    }

    public void setInWhsNm(String inWhsNm) {
        this.inWhsNm = inWhsNm;
    }

    public BigDecimal getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(BigDecimal taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd;
    }

    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public Integer getIsNtMakeVouch() {
        return isNtMakeVouch;
    }

    public void setIsNtMakeVouch(Integer isNtMakeVouch) {
        this.isNtMakeVouch = isNtMakeVouch;
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

    public BigDecimal getBxQty() {
        return bxQty;
    }

    public void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public final String getFormNum() {
        return formNum;
    }

    public final void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public final String getFormDt() {
        return formDt;
    }

    public final void setFormDt(String formDt) {
        this.formDt = formDt;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getSrcFormNum() {
        return srcFormNum;
    }

    public final void setSrcFormNum(String srcFormNum) {
        this.srcFormNum = srcFormNum;
    }

    public final String getOutIntoWhsTyp() {
        return outIntoWhsTyp;
    }

    public final void setOutIntoWhsTyp(String outIntoWhsTyp) {
        this.outIntoWhsTyp = outIntoWhsTyp;
    }

    public final String getOutStatus() {
        return outStatus;
    }

    public final void setOutStatus(String outStatus) {
        this.outStatus = outStatus;
    }

    public final String getInStatus() {
        return inStatus;
    }

    public final void setInStatus(String inStatus) {
        this.inStatus = inStatus;
    }

    public final String getMemo() {
        return memo;
    }

    public final void setMemo(String memo) {
        this.memo = memo;
    }

    public final Integer getIsNtWms() {
        return isNtWms;
    }

    public final void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public final Integer getIsNtChk() {
        return isNtChk;
    }

    public final void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public final Integer getIsNtBookEntry() {
        return isNtBookEntry;
    }

    public final void setIsNtBookEntry(Integer isNtBookEntry) {
        this.isNtBookEntry = isNtBookEntry;
    }

    public final Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public final void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public final Integer getIsNtClos() {
        return isNtClos;
    }

    public final void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public final Integer getPrintCnt() {
        return printCnt;
    }

    public final void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
    }

    public final String getSetupPers() {
        return setupPers;
    }

    public final void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public final String getSetupTm() {
        return setupTm;
    }

    public final void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

    public final String getMdfr() {
        return mdfr;
    }

    public final void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public final String getModiTm() {
        return modiTm;
    }

    public final void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }

    public final String getChkr() {
        return chkr;
    }

    public final void setChkr(String chkr) {
        this.chkr = chkr;
    }

    public final String getChkTm() {
        return chkTm;
    }

    public final void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }

    public final String getBookEntryPers() {
        return bookEntryPers;
    }

    public final void setBookEntryPers(String bookEntryPers) {
        this.bookEntryPers = bookEntryPers;
    }

    public final String getBookEntryTm() {
        return bookEntryTm;
    }

    public final void setBookEntryTm(String bookEntryTm) {
        this.bookEntryTm = bookEntryTm;
    }

    public final String getOperator() {
        return operator;
    }

    public final void setOperator(String operator) {
        this.operator = operator;
    }

    public final String getOperatorId() {
        return operatorId;
    }

    public final void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public final String getOutIntoWhsTypId() {
        return outIntoWhsTypId;
    }

    public final void setOutIntoWhsTypId(String outIntoWhsTypId) {
        this.outIntoWhsTypId = outIntoWhsTypId;
    }

    public final String getOutIntoWhsTypNm() {
        return outIntoWhsTypNm;
    }

    public final void setOutIntoWhsTypNm(String outIntoWhsTypNm) {
        this.outIntoWhsTypNm = outIntoWhsTypNm;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getFormTypEncd() {
        return formTypEncd;
    }

    public final void setFormTypEncd(String formTypEncd) {
        this.formTypEncd = formTypEncd;
    }

    public final String getFormTypName() {
        return formTypName;
    }

    public final void setFormTypName(String formTypName) {
        this.formTypName = formTypName;
    }

    public final Long getOrdrNum() {
        return ordrNum;
    }

    public final void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public final String getInvtyEncd() {
        return invtyEncd;
    }

    public final void setInvtyEncd(String invtyEncd) {
        this.invtyEncd = invtyEncd;
    }

    public final BigDecimal getQty() {
        return qty;
    }

    public final void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public final BigDecimal getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public final BigDecimal getCntnTaxUprc() {
        return cntnTaxUprc;
    }

    public final void setCntnTaxUprc(BigDecimal cntnTaxUprc) {
        this.cntnTaxUprc = cntnTaxUprc;
    }

    public final BigDecimal getUnTaxUprc() {
        return unTaxUprc;
    }

    public final void setUnTaxUprc(BigDecimal unTaxUprc) {
        this.unTaxUprc = unTaxUprc;
    }

    public final BigDecimal getBookEntryUprc() {
        return bookEntryUprc;
    }

    public final void setBookEntryUprc(BigDecimal bookEntryUprc) {
        this.bookEntryUprc = bookEntryUprc;
    }

    public final BigDecimal getCntnTaxAmt() {
        return cntnTaxAmt;
    }

    public final void setCntnTaxAmt(BigDecimal cntnTaxAmt) {
        this.cntnTaxAmt = cntnTaxAmt;
    }

    public final BigDecimal getUnTaxAmt() {
        return unTaxAmt;
    }

    public final void setUnTaxAmt(BigDecimal unTaxAmt) {
        this.unTaxAmt = unTaxAmt;
    }

    public final String getIntlBat() {
        return intlBat;
    }

    public final void setIntlBat(String intlBat) {
        this.intlBat = intlBat;
    }

    public final String getBatNum() {
        return batNum;
    }

    public final void setBatNum(String batNum) {
        this.batNum = batNum;
    }

    public final String getPrdcDt() {
        return prdcDt;
    }

    public final void setPrdcDt(String prdcDt) {
        this.prdcDt = prdcDt;
    }

    public final String getBaoZhiQi() {
        return baoZhiQi;
    }

    public final void setBaoZhiQi(String baoZhiQi) {
        this.baoZhiQi = baoZhiQi;
    }

    public final String getInvldtnDt() {
        return invldtnDt;
    }

    public final void setInvldtnDt(String invldtnDt) {
        this.invldtnDt = invldtnDt;
    }

    public final String getInvtyNm() {
        return invtyNm;
    }

    public final void setInvtyNm(String invtyNm) {
        this.invtyNm = invtyNm;
    }

    public final String getSpcModel() {
        return spcModel;
    }

    public final void setSpcModel(String spcModel) {
        this.spcModel = spcModel;
    }

    public final String getInvtyCd() {
        return invtyCd;
    }

    public final void setInvtyCd(String invtyCd) {
        this.invtyCd = invtyCd;
    }

    public final BigDecimal getBxRule() {
        return bxRule;
    }

    public final void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public final String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public final void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd;
    }

    public final String getInvtyClsNm() {
        return invtyClsNm;
    }

    public final void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm;
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


}