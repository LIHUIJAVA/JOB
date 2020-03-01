package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//盘点单
public class CheckSnglMap {

//	private String checkFormNum;// 盘点单号
//	private String checkDt;// 盘点日期
//	private String bookDt;// 账面日期
//	private String whsEncd;// 仓库编码
//	private String checkBat;// 盘点批号
//	private String checkStatus;// 盘点状态
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
//	private String operator;// 操作员
//	private String operatorId;// 操作员编码
//	private String whsNm;// 仓库名
//	private String formTypEncd;// 单据类型编码
//	private String formTypName;// 单据类型名称
////子
//	private Long ordrNum;// 序号
//	private String invtyEncd;// 存货编码
//	private String invtyBigClsEncd;// 存货大类编码
//	private String barCd;// 条形码
//	private String batNum;// 批号
//	private String prdcDt;// 生产日期
//	private String baoZhiQi;// 保质期
//	private String invldtnDt;// 失效日期
//	private BigDecimal bookQty;// 账面数量
//	private BigDecimal checkQty;// 盘点数量
//	private BigDecimal prftLossQty;// 盈亏数量
//	private BigDecimal bookAdjustQty;// 账面调节数量
//	private BigDecimal adjIntoWhsQty;// 调整入库数量
//	private BigDecimal prftLossRatio;// 盈亏比例(%)
//	private BigDecimal adjOutWhsQty;// 调整出库数量
//	private String reasn;// 原因
//
//// // 联查存货档案字段、计量单位名称、仓库名称
//	private String invtyNm;// 存货名称
//	private String spcModel;// 规格型号
//	private String invtyCd;// 存货代码
//	private BigDecimal bxRule;// 箱规
////	private BigDecimal bxQty;// 箱规
//	private String measrCorpNm;// 计量单位名称
////	private String crspdBarCd;// 对应条形码
//	private String measrCorpId;// 计量单位编码
//	
//    private String  gdsBitEncd;//货位编码
//
//    private String  gdsBitNm;//货位名称

    @JsonProperty("盘点单号")
    private String checkFormNum;// 盘点单号
    @JsonProperty("盘点日期")
    private String checkDt;// 盘点日期
    @JsonProperty("账面日期")
    private String bookDt;// 账面日期
    @JsonProperty("仓库编码")
    private String whsEncd;// 仓库编码
    @JsonProperty("盘点批号")
    private String checkBat;// 盘点批号
    @JsonProperty("盘点状态")
    private String checkStatus;// 盘点状态
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
    //	@JsonProperty("是否完成")
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
    //	@JsonProperty("操作员")
    @JsonIgnore
    private String operator;// 操作员
    //	@JsonProperty("操作员编码")
    @JsonIgnore
    private String operatorId;// 操作员编码
    @JsonProperty("仓库名称")
    private String whsNm;// 仓库名
    @JsonProperty("单据类型编码")
    private String formTypEncd;// 单据类型编码
    @JsonProperty("单据类型名称")
    private String formTypName;// 单据类型名称
    //	@JsonProperty("序号")
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("存货编码")
    private String invtyEncd;// 存货编码
    @JsonProperty("存货大类编码")
    private String invtyBigClsEncd;// 存货大类编码
    @JsonProperty("条形码")
    private String barCd;// 条形码
    @JsonProperty("批号")
    private String batNum;// 批号
    @JsonProperty("生产日期")
    private String prdcDt;// 生产日期
    @JsonProperty("保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("账面数量")
    private BigDecimal bookQty;// 账面数量
    @JsonProperty("盘点数量")
    private BigDecimal checkQty;// 盘点数量
    @JsonProperty("盈亏数量")
    private BigDecimal prftLossQty;// 盈亏数量
    @JsonProperty("账面调节数量")
    private BigDecimal bookAdjustQty;// 账面调节数量
    @JsonProperty("调整入库数量")
    private BigDecimal adjIntoWhsQty;// 调整入库数量
    @JsonProperty("盈亏比例")
    private BigDecimal prftLossRatio;// 盈亏比例(%)
    @JsonProperty("调整出库数量")
    private BigDecimal adjOutWhsQty;// 调整出库数量
    @JsonProperty("原因")
    private String reasn;// 原因
    @JsonProperty("存货名称")
    private String invtyNm;// 存货名称
    @JsonProperty("规格型号")
    private String spcModel;// 规格型号
    @JsonProperty("存货代码")
    private String invtyCd;// 存货代码
    @JsonProperty("箱规")
    private BigDecimal bxRule;// 箱规
    @JsonProperty("计量单位名称")
    private String measrCorpNm;// 计量单位名称
    @JsonProperty("计量单位编码")
    private String measrCorpId;// 计量单位编码
    @JsonProperty("货位编码")
    private String gdsBitEncd;// 货位编码
    @JsonProperty("货位名称")
    private String gdsBitNm;// 货位名称
    @JsonProperty("项目编码")
    private String projEncd;// 项目编码
    @JsonProperty("项目名称")
    private String projNm;// 项目名称
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

    public final String getMeasrCorpId() {
        return measrCorpId;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
    }

    public String getGdsBitNm() {
        return gdsBitNm;
    }

    public void setGdsBitNm(String gdsBitNm) {
        this.gdsBitNm = gdsBitNm;
    }

    public final void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId;
    }

    public final String getCheckFormNum() {
        return checkFormNum;
    }

    public final void setCheckFormNum(String checkFormNum) {
        this.checkFormNum = checkFormNum;
    }

    public final String getCheckDt() {
        return checkDt;
    }

    public final void setCheckDt(String checkDt) {
        this.checkDt = checkDt;
    }

    public final String getBookDt() {
        return bookDt;
    }

    public final void setBookDt(String bookDt) {
        this.bookDt = bookDt;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final String getCheckBat() {
        return checkBat;
    }

    public final void setCheckBat(String checkBat) {
        this.checkBat = checkBat;
    }

    public final String getCheckStatus() {
        return checkStatus;
    }

    public final void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
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

    public final String getInvtyBigClsEncd() {
        return invtyBigClsEncd;
    }

    public final void setInvtyBigClsEncd(String invtyBigClsEncd) {
        this.invtyBigClsEncd = invtyBigClsEncd;
    }

    public final String getBarCd() {
        return barCd;
    }

    public final void setBarCd(String barCd) {
        this.barCd = barCd;
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

    public final BigDecimal getBookQty() {
        return bookQty;
    }

    public final void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public final BigDecimal getCheckQty() {
        return checkQty;
    }

    public final void setCheckQty(BigDecimal checkQty) {
        this.checkQty = checkQty;
    }

    public final BigDecimal getPrftLossQty() {
        return prftLossQty;
    }

    public final void setPrftLossQty(BigDecimal prftLossQty) {
        this.prftLossQty = prftLossQty;
    }

    public final BigDecimal getBookAdjustQty() {
        return bookAdjustQty;
    }

    public final void setBookAdjustQty(BigDecimal bookAdjustQty) {
        this.bookAdjustQty = bookAdjustQty;
    }

    public final BigDecimal getAdjIntoWhsQty() {
        return adjIntoWhsQty;
    }

    public final void setAdjIntoWhsQty(BigDecimal adjIntoWhsQty) {
        this.adjIntoWhsQty = adjIntoWhsQty;
    }

    public final BigDecimal getPrftLossRatio() {
        return prftLossRatio;
    }

    public final void setPrftLossRatio(BigDecimal prftLossRatio) {
        this.prftLossRatio = prftLossRatio;
    }

    public final BigDecimal getAdjOutWhsQty() {
        return adjOutWhsQty;
    }

    public final void setAdjOutWhsQty(BigDecimal adjOutWhsQty) {
        this.adjOutWhsQty = adjOutWhsQty;
    }

    public final String getReasn() {
        return reasn;
    }

    public final void setReasn(String reasn) {
        this.reasn = reasn;
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

    @Override
    public String toString() {
        return String.format(
                "CheckSnglMap [checkFormNum=%s, checkDt=%s, bookDt=%s, whsEncd=%s, checkBat=%s, checkStatus=%s, memo=%s, isNtWms=%s, isNtChk=%s, isNtBookEntry=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, bookEntryPers=%s, bookEntryTm=%s, operator=%s, operatorId=%s, whsNm=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, invtyEncd=%s, invtyBigClsEncd=%s, barCd=%s, batNum=%s, prdcDt=%s, baoZhiQi=%s, invldtnDt=%s, bookQty=%s, checkQty=%s, prftLossQty=%s, bookAdjustQty=%s, adjIntoWhsQty=%s, prftLossRatio=%s, adjOutWhsQty=%s, reasn=%s, invtyNm=%s, spcModel=%s, invtyCd=%s, bxRule=%s, measrCorpNm=%s]",
                checkFormNum, checkDt, bookDt, whsEncd, checkBat, checkStatus, memo, isNtWms, isNtChk, isNtBookEntry,
                isNtCmplt, isNtClos, printCnt, setupPers, setupTm, mdfr, modiTm, chkr, chkTm, bookEntryPers,
                bookEntryTm, operator, operatorId, whsNm, formTypEncd, formTypName, ordrNum, invtyEncd, invtyBigClsEncd,
                barCd, batNum, prdcDt, baoZhiQi, invldtnDt, bookQty, checkQty, prftLossQty, bookAdjustQty,
                adjIntoWhsQty, prftLossRatio, adjOutWhsQty, reasn, invtyNm, spcModel, invtyCd, bxRule, measrCorpNm);
    }

}