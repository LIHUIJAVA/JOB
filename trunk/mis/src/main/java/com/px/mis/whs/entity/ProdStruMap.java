package com.px.mis.whs.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//产品结构主子表
public class ProdStruMap {
    ////	@JsonProperty("序号")
//	@JsonIgnore
//	private String ordrNum;// 序号
//	@JsonProperty("母件编码")
//	private String momEncd;// 母件编码
//	@JsonProperty("母件名称")
//	private String momNm;// 母件名称
//	@JsonProperty("版本说明")
//	private String edComnt;// 版本说明
//	@JsonProperty("母件规格")
//	private String momSpc;// 母件规格
//	@JsonProperty("母件备注")
//	private String memo;// 备注
////	@JsonProperty("是否向WMS上传")
//	@JsonIgnore
//	private Integer isNtWms;// 是否向WMS上传
//	@JsonProperty("是否审核")
//	private Integer isNtChk;// 是否审核
////	@JsonProperty("是否完成")
//	@JsonIgnore
//	private Integer isNtCmplt;// 是否完成
////	@JsonProperty("是否关闭")
//	@JsonIgnore
//	private Integer isNtClos;// 是否关闭
////	@JsonProperty("打印次数")
//	@JsonIgnore
//	private Integer printCnt;// 打印次数
//	@JsonProperty("创建人")
//	private String setupPers;// 创建人
//	@JsonProperty("创建时间")
//	private String setupTm;// 创建时间
//	@JsonProperty("修改人")
//	private String mdfr;// 修改人
//	@JsonProperty("修改时间")
//	private String modiTm;// 修改时间
//	@JsonProperty("审核人")
//	private String chkr;// 审核人
//	@JsonProperty("审核时间")
//	private String chkTm;// 审核时间
//	@JsonProperty("母件计量单位名称")
//	private String measrCorpNm;// 计量单位名称
//
//	@JsonProperty("子件编码")
//	private String subEncd;// 子件编码
////	@JsonProperty("序号")
//	@JsonIgnore
//	private String ordrNumSub;// 序号
//	@JsonProperty("子件名称")
//	private String subNm;// 子件名称
//	@JsonProperty("子件规格")
//	private String subSpc;// 子件规格
//	@JsonProperty("子件计量单位")
//	private String measrCorp;// 计量单位
////	@JsonProperty("箱规")
//	@JsonIgnore
//	private BigDecimal bxRule;// 箱规
//	@JsonProperty("子件数量")
//	private BigDecimal subQty;// 子件数量
//	@JsonProperty("母件数量")
//	private BigDecimal momQty;// 母件数量
//	@JsonProperty("子件备注")
//	private String memos;// 备注s
//	@JsonProperty("子件计量单位名称")
//	private String measrCorpNms;// 计量单位名称s
    @JsonProperty("母件编码")
    private String momEncd;//母件编码
    @JsonIgnore
    private String ordrNum;//序号
    @JsonProperty("母件名称")
    private String momNm;//母件名称
    @JsonProperty("版本说明")
    private String edComnt;//版本说明
    @JsonProperty("母件规格")
    private String momSpc;//母件规格
    @JsonProperty("母件备注")
    private String memo;//备注
    @JsonIgnore
    private Integer isNtWms;//是否向WMS上传
    @JsonProperty("是否审核")
    private Integer isNtChk;//是否审核
    @JsonIgnore
    private Integer isNtCmplt;//是否完成
    @JsonIgnore
    private Integer isNtClos;//是否关闭
    @JsonIgnore
    private Integer printCnt;//打印次数
    @JsonProperty("创建人")
    private String setupPers;//创建人
    @JsonProperty("创建时间")
    private String setupTm;//创建时间
    @JsonProperty("修改人")
    private String mdfr;//修改人
    @JsonProperty("修改时间")
    private String modiTm;//修改时间
    @JsonProperty("审核人")
    private String chkr;//审核人
    @JsonProperty("审核时间")
    private String chkTm;//审核时间
    @JsonProperty("母件计量单位名称")
    private String measrCorpNm;// 计量单位名称
    @JsonProperty("母件箱规")
    private BigDecimal mbxRule;// 箱规
    @JsonProperty("母件税率")
    private BigDecimal moptaxRate;// 销项税率
    @JsonProperty("母件参考成本")
    private BigDecimal mrefCost;// 参考成本
    @JsonProperty("母件对应条形码")
    private String mcrspdBarCd;// 对应条形码
    @JsonProperty("母件存货代码")
    private String minvtyCd;// 存货代码
    @JsonProperty("母件保质期天数")
    private String mbaoZhiQiDt;// 保质期天数
    @JsonProperty("子件编码")
    private String subEncd;//子件编码
    @JsonProperty("序号")
    private String ordrNumSub;//序号
    @JsonProperty("子件名称")
    private String subNm;//子件名称
    @JsonProperty("子件规格")
    private String subSpc;//子件规格
//    @JsonIgnore
    @JsonProperty("子件计量单位编码")
    private String measrCorp;//计量单位
    @JsonProperty("子件箱规")
    private BigDecimal bxRule;//箱规
    @JsonProperty("子件数量")
    private BigDecimal subQty;//子件数量
    @JsonProperty("母件数量")
    private BigDecimal momQty;//母件数量
    @JsonProperty("子件备注")
    private String smemo;//备注
    @JsonProperty("子件计量单位名称")
    private String smeasrCorpNm;//计量单位名称
    @JsonProperty("子件保质期天数")
    private String sbaoZhiQiDt;// 保质期天数
    @JsonProperty("子件税率")
    private BigDecimal soptaxRate;// 销项税率
    @JsonProperty("子件存货代码")
    private String sinvtyCd;// 存货代码
    @JsonProperty("子件参考成本")
    private BigDecimal srefCost;// 参考成本
    @JsonProperty("子件对应条形码")
    private String scrspdBarCd;// 对应条形码

    public String getMomEncd() {
        return momEncd;
    }

    public void setMomEncd(String momEncd) {
        this.momEncd = momEncd;
    }

    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getMomNm() {
        return momNm;
    }

    public void setMomNm(String momNm) {
        this.momNm = momNm;
    }

    public String getEdComnt() {
        return edComnt;
    }

    public void setEdComnt(String edComnt) {
        this.edComnt = edComnt;
    }

    public String getMomSpc() {
        return momSpc;
    }

    public void setMomSpc(String momSpc) {
        this.momSpc = momSpc;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtWms() {
        return isNtWms;
    }

    public void setIsNtWms(Integer isNtWms) {
        this.isNtWms = isNtWms;
    }

    public Integer getIsNtChk() {
        return isNtChk;
    }

    public void setIsNtChk(Integer isNtChk) {
        this.isNtChk = isNtChk;
    }

    public Integer getIsNtCmplt() {
        return isNtCmplt;
    }

    public void setIsNtCmplt(Integer isNtCmplt) {
        this.isNtCmplt = isNtCmplt;
    }

    public Integer getIsNtClos() {
        return isNtClos;
    }

    public void setIsNtClos(Integer isNtClos) {
        this.isNtClos = isNtClos;
    }

    public Integer getPrintCnt() {
        return printCnt;
    }

    public void setPrintCnt(Integer printCnt) {
        this.printCnt = printCnt;
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

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
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

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
    }

    public BigDecimal getMbxRule() {
        return mbxRule;
    }

    public void setMbxRule(BigDecimal mbxRule) {
        this.mbxRule = mbxRule;
    }

    public BigDecimal getMoptaxRate() {
        return moptaxRate;
    }

    public void setMoptaxRate(BigDecimal moptaxRate) {
        this.moptaxRate = moptaxRate;
    }

    public BigDecimal getMrefCost() {
        return mrefCost;
    }

    public void setMrefCost(BigDecimal mrefCost) {
        this.mrefCost = mrefCost;
    }

    public String getMcrspdBarCd() {
        return mcrspdBarCd;
    }

    public void setMcrspdBarCd(String mcrspdBarCd) {
        this.mcrspdBarCd = mcrspdBarCd;
    }

    public String getMinvtyCd() {
        return minvtyCd;
    }

    public void setMinvtyCd(String minvtyCd) {
        this.minvtyCd = minvtyCd;
    }

    public String getMbaoZhiQiDt() {
        return mbaoZhiQiDt;
    }

    public void setMbaoZhiQiDt(String mbaoZhiQiDt) {
        this.mbaoZhiQiDt = mbaoZhiQiDt;
    }

    public String getSubEncd() {
        return subEncd;
    }

    public void setSubEncd(String subEncd) {
        this.subEncd = subEncd;
    }

    public String getOrdrNumSub() {
        return ordrNumSub;
    }

    public void setOrdrNumSub(String ordrNumSub) {
        this.ordrNumSub = ordrNumSub;
    }

    public String getSubNm() {
        return subNm;
    }

    public void setSubNm(String subNm) {
        this.subNm = subNm;
    }

    public String getSubSpc() {
        return subSpc;
    }

    public void setSubSpc(String subSpc) {
        this.subSpc = subSpc;
    }

    public String getMeasrCorp() {
        return measrCorp;
    }

    public void setMeasrCorp(String measrCorp) {
        this.measrCorp = measrCorp;
    }

    public BigDecimal getBxRule() {
        return bxRule;
    }

    public void setBxRule(BigDecimal bxRule) {
        this.bxRule = bxRule;
    }

    public BigDecimal getSubQty() {
        return subQty;
    }

    public void setSubQty(BigDecimal subQty) {
        this.subQty = subQty;
    }

    public BigDecimal getMomQty() {
        return momQty;
    }

    public void setMomQty(BigDecimal momQty) {
        this.momQty = momQty;
    }

    public String getSmemo() {
        return smemo;
    }

    public void setSmemo(String smemo) {
        this.smemo = smemo;
    }

    public String getSmeasrCorpNm() {
        return smeasrCorpNm;
    }

    public void setSmeasrCorpNm(String smeasrCorpNm) {
        this.smeasrCorpNm = smeasrCorpNm;
    }

    public String getSbaoZhiQiDt() {
        return sbaoZhiQiDt;
    }

    public void setSbaoZhiQiDt(String sbaoZhiQiDt) {
        this.sbaoZhiQiDt = sbaoZhiQiDt;
    }

    public BigDecimal getSoptaxRate() {
        return soptaxRate;
    }

    public void setSoptaxRate(BigDecimal soptaxRate) {
        this.soptaxRate = soptaxRate;
    }

    public String getSinvtyCd() {
        return sinvtyCd;
    }

    public void setSinvtyCd(String sinvtyCd) {
        this.sinvtyCd = sinvtyCd;
    }

    public BigDecimal getSrefCost() {
        return srefCost;
    }

    public void setSrefCost(BigDecimal srefCost) {
        this.srefCost = srefCost;
    }

    public String getScrspdBarCd() {
        return scrspdBarCd;
    }

    public void setScrspdBarCd(String scrspdBarCd) {
        this.scrspdBarCd = scrspdBarCd;
    }
}