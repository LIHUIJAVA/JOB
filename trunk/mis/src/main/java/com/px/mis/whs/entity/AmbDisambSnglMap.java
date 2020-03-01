package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//组装拆卸单
public class AmbDisambSnglMap {

//	@JsonProperty("母配套件编码") 
//	private String momKitEncd;// 母配套件编码
//	private String formNum;// 单据号
//	private String formDt;// 单据日期
//	private String formTyp;// 单据类型(组装、拆卸)
//	private BigDecimal fee;// 费用(总的)
//	private String feeComnt;// 费用说明
//	private String memo;// 备注
//	private String typ;// 类型(套件、散件)
//	private String whsEncd;// 仓库编码
//	private BigDecimal momQty;// 母件数量
//	private BigDecimal taxRate;// 税率
//	private BigDecimal mcntnTaxUprc;// 含税单价
//	private BigDecimal munTaxUprc;// 未税单价
//	private BigDecimal mcntnTaxAmt;// 含税金额
//	private BigDecimal munTaxAmt;// 未税金额
//	private String mbatNum;// 批号
//	private String mprdcDt;// 生产日期
//	private String baoZhiQi;// 保质期
//	private String invldtnDt;// 失效日期
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
//
//	// 联查存货档案字段、计量单位名称、仓库名称
//	private String invtyNm;// 存货名称
//	private String spcModel;// 规格型号
//	private String invtyCd;// 存货代码
//	private BigDecimal bxRule;// 箱规
//	private BigDecimal bxQty;// 箱数
//
//	private String baoZhiQiDt;// 保质期天数
//	private String measrCorpNm;// 计量单位名称
//	private String whsNm;// 仓库名称
//	private String crspdBarCd;// 对应条形码
//	private String formTypEncd;// 单据类型编码
//	private String formTypName;// 单据类型名称
//	// 子
//	private Long ordrNum;// 序号
//	private String subKitEncd;// 子配套件编码
//	private String swhsEncd;// 仓库编码
//	private BigDecimal subQty;// 子件数量
//	private BigDecimal momSubRatio;// 母子比例
//	private BigDecimal staxRate;// 税率
//	private BigDecimal scntnTaxUprc;// 含税单价
//	private BigDecimal sunTaxUprc;// 未税单价
//	private BigDecimal scntnTaxAmt;// 含税金额
//	private BigDecimal sunTaxAmt;// 未税金额
//	private String sbatNum;// 批号
//	private String sprdcDt;// 生产日期
//	private String sbaoZhiQi;// 保质期
//	private String sinvldtnDt;// 失效日期
//	// 联查存货档案字段、计量单位名称、仓库名称
//	private String sinvtyNm;// 存货名称
//	private String sspcModel;// 规格型号
//	private String sinvtyCd;// 存货代码
//	private BigDecimal sbxRule;// 箱规
//	private BigDecimal sbxQty;// 箱数
//
//	private String sbaoZhiQiDt;// 保质期天数
//	private String smeasrCorpNm;// 计量单位名称
//	private String swhsNm;// 仓库名称
//	private String scrspdBarCd;// 对应条形码
//	

    @JsonProperty("母件编码")
    private String momKitEncd;// 母配套件编码
    @JsonProperty("单据号")
    private String formNum;// 单据号
    @JsonProperty("单据日期")
    private String formDt;// 单据日期
    @JsonProperty("单据类型(组装、拆卸)")
    private String formTyp;// 单据类型(组装、拆卸)
    @JsonProperty("费用")
    private BigDecimal fee;// 费用(总的)
    @JsonProperty("费用说明")
    private String feeComnt;// 费用说明
    @JsonProperty("备注")
    private String memo;// 备注
    @JsonProperty("类型(套件、散件)")
    private String typ;// 类型(套件、散件)
    @JsonProperty("母件仓库编码")
    private String whsEncd;// 仓库编码
    @JsonProperty("母件数量")
    private BigDecimal momQty;// 母件数量
    @JsonProperty("母件税率")
    private BigDecimal taxRate;// 税率
    @JsonProperty("母件含税单价")
    private BigDecimal mcntnTaxUprc;// 含税单价
    @JsonProperty("母件未税单价")
    private BigDecimal munTaxUprc;// 未税单价
    @JsonProperty("母件含税金额")
    private BigDecimal mcntnTaxAmt;// 含税金额
    @JsonProperty("母件未税金额")
    private BigDecimal munTaxAmt;// 未税金额
    @JsonProperty("母件批号")
    private String mbatNum;// 批号
    @JsonProperty("母件生产日期")
    private String mprdcDt;// 生产日期
    @JsonProperty("母件保质期")
    private String baoZhiQi;// 保质期
    @JsonProperty("母件失效日期")
    private String invldtnDt;// 失效日期
    @JsonProperty("母件税额")
    private BigDecimal mtaxAmt;//税额
    @JsonProperty("母件项目名称")
    private String mprojEncd;// 项目编码
    @JsonProperty("母件项目名称")
    private String mprojNm;// 项目名称
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
    @JsonProperty("母件存货名称")
    private String invtyNm;// 存货名称
    @JsonProperty("母件规格型号")
    private String spcModel;// 规格型号
    @JsonProperty("母件存货代码")
    private String invtyCd;// 存货代码
    @JsonProperty("母件箱规")
    private BigDecimal bxRule;// 箱规
    @JsonProperty("母件箱数")
    private BigDecimal bxQty;// 箱数
    @JsonProperty("母件保质期天数")
    private String baoZhiQiDt;// 保质期天数
    @JsonProperty("母件计量单位名称")
    private String measrCorpNm;// 计量单位名称
    @JsonProperty("母件仓库名称")
    private String whsNm;// 仓库名称
    @JsonProperty("母件条形码")
    private String crspdBarCd;// 对应条形码
    @JsonProperty("单据类型编码")
    private String formTypEncd;// 单据类型编码
    @JsonProperty("单据类型名称")
    private String formTypName;// 单据类型名称
    //	@JsonProperty("序号")
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("子件编码")
    private String subKitEncd;// 子件编码
    @JsonProperty("子件仓库编码")
    private String swhsEncd;// 仓库编码
    @JsonProperty("子件数量")
    private BigDecimal subQty;// 子件数量
    @JsonProperty("母子比例")
    private BigDecimal momSubRatio;// 母子比例
    @JsonProperty("子件税率")
    private BigDecimal staxRate;// 税率
    @JsonProperty("子件含税单价")
    private BigDecimal scntnTaxUprc;// 含税单价
    @JsonProperty("子件未税单价")
    private BigDecimal sunTaxUprc;// 未税单价
    @JsonProperty("子件含税金额")
    private BigDecimal scntnTaxAmt;// 含税金额
    @JsonProperty("子件未税金额")
    private BigDecimal sunTaxAmt;// 未税金额
    @JsonProperty("子件税额")
    private BigDecimal staxAmt;//税额
    @JsonProperty("子件批号")
    private String sbatNum;// 批号
    @JsonProperty("子件生产日期")
    private String sprdcDt;// 生产日期
    @JsonProperty("子件保质期")
    private String sbaoZhiQi;// 保质期
    @JsonProperty("子件失效日期")
    private String sinvldtnDt;// 失效日期
    @JsonProperty("子件存货名称")
    private String sinvtyNm;// 存货名称
    @JsonProperty("子件规格型号")
    private String sspcModel;// 规格型号
    @JsonProperty("子件存货代码")
    private String sinvtyCd;// 存货代码
    @JsonProperty("子件箱规")
    private BigDecimal sbxRule;// 箱规
    @JsonProperty("子件箱数")
    private BigDecimal sbxQty;// 箱数
    @JsonProperty("子件保质期天数")
    private String sbaoZhiQiDt;// 保质期天数
    @JsonProperty("子件计量单位名称")
    private String smeasrCorpNm;// 计量单位名称
    @JsonProperty("子件仓库名称")
    private String swhsNm;// 仓库名称
    @JsonProperty("子件条形码")
    private String scrspdBarCd;// 对应条形码
    @JsonProperty("子件项目编码")
    private String projEncd;// 项目编码
    @JsonProperty("子件项目名称")
    private String projNm;// 项目名称

    public BigDecimal getMtaxAmt() {
        return mtaxAmt;
    }

    public void setMtaxAmt(BigDecimal mtaxAmt) {
        this.mtaxAmt = mtaxAmt;
    }

    public BigDecimal getStaxAmt() {
        return staxAmt;
    }

    public void setStaxAmt(BigDecimal staxAmt) {
        this.staxAmt = staxAmt;
    }

    public String getMprojEncd() {
        return mprojEncd;
    }

    public void setMprojEncd(String mprojEncd) {
        this.mprojEncd = mprojEncd;
    }

    public String getMprojNm() {
        return mprojNm;
    }

    public void setMprojNm(String mprojNm) {
        this.mprojNm = mprojNm;
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

    public final BigDecimal getBxQty() {
        return bxQty;
    }

    public final void setBxQty(BigDecimal bxQty) {
        this.bxQty = bxQty;
    }

    public final BigDecimal getSbxQty() {
        return sbxQty;
    }

    public final void setSbxQty(BigDecimal sbxQty) {
        this.sbxQty = sbxQty;
    }

    public final String getMomKitEncd() {
        return momKitEncd;
    }

    public final void setMomKitEncd(String momKitEncd) {
        this.momKitEncd = momKitEncd;
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

    public final String getFormTyp() {
        return formTyp;
    }

    public final void setFormTyp(String formTyp) {
        this.formTyp = formTyp;
    }

    public final BigDecimal getFee() {
        return fee;
    }

    public final void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public final String getFeeComnt() {
        return feeComnt;
    }

    public final void setFeeComnt(String feeComnt) {
        this.feeComnt = feeComnt;
    }

    public final String getMemo() {
        return memo;
    }

    public final void setMemo(String memo) {
        this.memo = memo;
    }

    public final String getTyp() {
        return typ;
    }

    public final void setTyp(String typ) {
        this.typ = typ;
    }

    public final String getWhsEncd() {
        return whsEncd;
    }

    public final void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public final BigDecimal getMomQty() {
        return momQty;
    }

    public final void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public final BigDecimal getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public final BigDecimal getMcntnTaxUprc() {
        return mcntnTaxUprc;
    }

    public final void setMcntnTaxUprc(BigDecimal mcntnTaxUprc) {
        this.mcntnTaxUprc = mcntnTaxUprc;
    }

    public final BigDecimal getMunTaxUprc() {
        return munTaxUprc;
    }

    public final void setMunTaxUprc(BigDecimal munTaxUprc) {
        this.munTaxUprc = munTaxUprc;
    }

    public final BigDecimal getMcntnTaxAmt() {
        return mcntnTaxAmt;
    }

    public final void setMcntnTaxAmt(BigDecimal mcntnTaxAmt) {
        this.mcntnTaxAmt = mcntnTaxAmt;
    }

    public final BigDecimal getMunTaxAmt() {
        return munTaxAmt;
    }

    public final void setMunTaxAmt(BigDecimal munTaxAmt) {
        this.munTaxAmt = munTaxAmt;
    }

    public final String getMbatNum() {
        return mbatNum;
    }

    public final void setMbatNum(String mbatNum) {
        this.mbatNum = mbatNum;
    }

    public final String getMprdcDt() {
        return mprdcDt;
    }

    public final void setMprdcDt(String mprdcDt) {
        this.mprdcDt = mprdcDt;
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

    public final String getBaoZhiQiDt() {
        return baoZhiQiDt;
    }

    public final void setBaoZhiQiDt(String baoZhiQiDt) {
        this.baoZhiQiDt = baoZhiQiDt;
    }

    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public final String getWhsNm() {
        return whsNm;
    }

    public final void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public final String getCrspdBarCd() {
        return crspdBarCd;
    }

    public final void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
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

    public final String getSubKitEncd() {
        return subKitEncd;
    }

    public final void setSubKitEncd(String subKitEncd) {
        this.subKitEncd = subKitEncd;
    }

    public final String getSwhsEncd() {
        return swhsEncd;
    }

    public final void setSwhsEncd(String swhsEncd) {
        this.swhsEncd = swhsEncd;
    }

    public final BigDecimal getSubQty() {
        return subQty;
    }

    public final void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public final BigDecimal getMomSubRatio() {
        return momSubRatio;
    }

    public final void setMomSubRatio(BigDecimal momSubRatio) {
        this.momSubRatio = momSubRatio;
    }

    public final BigDecimal getStaxRate() {
        return staxRate;
    }

    public final void setStaxRate(BigDecimal staxRate) {
        this.staxRate = staxRate;
    }

    public final BigDecimal getScntnTaxUprc() {
        return scntnTaxUprc;
    }

    public final void setScntnTaxUprc(BigDecimal scntnTaxUprc) {
        this.scntnTaxUprc = scntnTaxUprc;
    }

    public final BigDecimal getSunTaxUprc() {
        return sunTaxUprc;
    }

    public final void setSunTaxUprc(BigDecimal sunTaxUprc) {
        this.sunTaxUprc = sunTaxUprc;
    }

    public final BigDecimal getScntnTaxAmt() {
        return scntnTaxAmt;
    }

    public final void setScntnTaxAmt(BigDecimal scntnTaxAmt) {
        this.scntnTaxAmt = scntnTaxAmt;
    }

    public final BigDecimal getSunTaxAmt() {
        return sunTaxAmt;
    }

    public final void setSunTaxAmt(BigDecimal sunTaxAmt) {
        this.sunTaxAmt = sunTaxAmt;
    }

    public final String getSbatNum() {
        return sbatNum;
    }

    public final void setSbatNum(String sbatNum) {
        this.sbatNum = sbatNum;
    }

    public final String getSprdcDt() {
        return sprdcDt;
    }

    public final void setSprdcDt(String sprdcDt) {
        this.sprdcDt = sprdcDt;
    }

    public final String getSbaoZhiQi() {
        return sbaoZhiQi;
    }

    public final void setSbaoZhiQi(String sbaoZhiQi) {
        this.sbaoZhiQi = sbaoZhiQi;
    }

    public final String getSinvldtnDt() {
        return sinvldtnDt;
    }

    public final void setSinvldtnDt(String sinvldtnDt) {
        this.sinvldtnDt = sinvldtnDt;
    }

    public final String getSinvtyNm() {
        return sinvtyNm;
    }

    public final void setSinvtyNm(String sinvtyNm) {
        this.sinvtyNm = sinvtyNm;
    }

    public final String getSspcModel() {
        return sspcModel;
    }

    public final void setSspcModel(String sspcModel) {
        this.sspcModel = sspcModel;
    }

    public final String getSinvtyCd() {
        return sinvtyCd;
    }

    public final void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public final BigDecimal getSbxRule() {
        return sbxRule;
    }

    public final void setSbxRule(BigDecimal sbxRule) {
        this.sbxRule = sbxRule;
    }

    public final String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public final void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public final String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public final void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public final String getSwhsNm() {
        return swhsNm;
    }

    public final void setSwhsNm(String swhsNm) {
        this.swhsNm = swhsNm;
    }

    public final String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public final void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }

    @Override
    public String toString() {
        return String.format(
                "AmbDisambSnglMap [momKitEncd=%s, formNum=%s, formDt=%s, formTyp=%s, fee=%s, feeComnt=%s, memo=%s, typ=%s, whsEncd=%s, momQty=%s, taxRate=%s, mcntnTaxUprc=%s, munTaxUprc=%s, mcntnTaxAmt=%s, munTaxAmt=%s, mbatNum=%s, mprdcDt=%s, baoZhiQi=%s, invldtnDt=%s, isNtWms=%s, isNtChk=%s, isNtBookEntry=%s, isNtCmplt=%s, isNtClos=%s, printCnt=%s, setupPers=%s, setupTm=%s, mdfr=%s, modiTm=%s, chkr=%s, chkTm=%s, bookEntryPers=%s, bookEntryTm=%s, invtyNm=%s, spcModel=%s, invtyCd=%s, bxRule=%s, baoZhiQiDt=%s, measrCorpNm=%s, whsNm=%s, crspdBarCd=%s, formTypEncd=%s, formTypName=%s, ordrNum=%s, subKitEncd=%s, swhsEncd=%s, subQty=%s, momSubRatio=%s, staxRate=%s, scntnTaxUprc=%s, sunTaxUprc=%s, scntnTaxAmt=%s, sunTaxAmt=%s, sbatNum=%s, sprdcDt=%s, sbaoZhiQi=%s, sinvldtnDt=%s, sinvtyNm=%s, sspcModel=%s, sinvtyCd=%s, sbxRule=%s, sbaoZhiQiDt=%s, smeasrCorpNm=%s, swhsNm=%s, scrspdBarCd=%s]",
                momKitEncd, formNum, formDt, formTyp, fee, feeComnt, memo, typ, whsEncd, momQty, taxRate, mcntnTaxUprc,
                munTaxUprc, mcntnTaxAmt, munTaxAmt, mbatNum, mprdcDt, baoZhiQi, invldtnDt, isNtWms, isNtChk,
                isNtBookEntry, isNtCmplt, isNtClos, printCnt, setupPers, setupTm, mdfr, modiTm, chkr, chkTm,
                bookEntryPers, bookEntryTm, invtyNm, spcModel, invtyCd, bxRule, baoZhiQiDt, measrCorpNm, whsNm,
                crspdBarCd, formTypEncd, formTypName, ordrNum, subKitEncd, swhsEncd, subQty, momSubRatio, staxRate,
                scntnTaxUprc, sunTaxUprc, scntnTaxAmt, sunTaxAmt, sbatNum, sprdcDt, sbaoZhiQi, sinvldtnDt, sinvtyNm,
                sspcModel, sinvtyCd, sbxRule, sbaoZhiQiDt, smeasrCorpNm, swhsNm, scrspdBarCd);
    }

}