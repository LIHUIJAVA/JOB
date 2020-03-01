package com.px.mis.whs.entity;

import java.math.BigDecimal;
import java.util.List;

//产品结构主表
public class ProdStru {
    private String momEncd;//母件编码

    private String ordrNum;//序号

    private String momNm;//母件名称

    private String edComnt;//版本说明

    private String momSpc;//母件规格

    private String memo;//备注

    private Integer isNtWms;//是否向WMS上传

    private Integer isNtChk;//是否审核

    private Integer isNtCmplt;//是否完成

    private Integer isNtClos;//是否关闭

    private Integer printCnt;//打印次数

    private String setupPers;//创建人

    private String setupTm;//创建时间

    private String mdfr;//修改人

    private String modiTm;//修改时间

    private String chkr;//审核人

    private String chkTm;//审核时间
    private String measrCorpNm;// 计量单位名称
    private BigDecimal mbxRule;// 箱规
    private BigDecimal moptaxRate;// 销项税率
    private BigDecimal mrefCost;// 参考成本
    private String mcrspdBarCd;// 对应条形码
    private String minvtyCd;// 存货代码
    private String mbaoZhiQiDt;// 保质期天数

    private List<ProdStruSubTab> struSubList;

    public String getMbaoZhiQiDt() {
        return mbaoZhiQiDt;
    }

    public void setMbaoZhiQiDt(String mbaoZhiQiDt) {
        this.mbaoZhiQiDt = mbaoZhiQiDt;
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

    /**
     * @return measrCorpNm
     * @date 2019年4月15日
     */
    public final String getMeasrCorpNm() {
        return measrCorpNm;
    }


    /**
     * @param measrCorpNm
     * @date 2019年4月15日
     */
    public final void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm;
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

    public List<ProdStruSubTab> getStruSubList() {
        return struSubList;
    }

    public void setStruSubList(List<ProdStruSubTab> struSubList) {
        this.struSubList = struSubList;
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

    public String getMomEncd() {
        return momEncd;
    }

    public void setMomEncd(String momEncd) {
        this.momEncd = momEncd == null ? null : momEncd.trim();
    }


    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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


    public String getChkr() {
        return chkr;
    }

    public void setChkr(String chkr) {
        this.chkr = chkr == null ? null : chkr.trim();
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

    public String getChkTm() {
        return chkTm;
    }

    public void setChkTm(String chkTm) {
        this.chkTm = chkTm;
    }


}