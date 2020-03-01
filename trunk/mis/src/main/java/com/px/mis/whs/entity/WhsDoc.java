package com.px.mis.whs.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//仓库档案
public class WhsDoc {
    //
//	private String whsEncd;// 仓库编码
//
//	private Long ordrNum;// 序号
//
//	private String whsNm;// 仓库名称
//
//	private String deptEncd;// 部门编码
//
//	private String whsAddr;// 仓库地址
//
//	private String tel;// 电话
//
//	private String princ;// 负责人
//
//	private String valtnMode;// 计价方式
//
//	private String crspdBarCd;// 对应条形码
//
//	private Integer isNtPrgrGdsBitMgmt;// 是否进行货位管理
//
//	private String whsAttr;// 仓库属性
//
//	private String sellAvalQtyCtrlMode;// 销售可用量控制方式
//
//	private String invtyAvalQtyCtrlMode;// 库存可用量控制方式
//
//	private String memo;// 备注
//
//	private Integer isNtShop;// 是否门店
//
//	private String stpUseDt;// 停用日期
//
//	private String prov;// 省/直辖市
//
//	private String cty;// 市
//
//	private String cnty;// 县
//
//	private Integer dumyWhs;// 虚拟仓(是否是虚拟仓)
//
//	private String setupPers;// 创建人
//
//	private String setupTm;// 创建时间
//
//	private String mdfr;// 修改人
//
//	private String modiTm;// 修改时间
//
//	private String deptName;// 部门名称
//
//	private String realWhs;// 大仓
    @JsonProperty("仓库编码")
    private String whsEncd;// 仓库编码
    //	@JsonProperty("序号")
    @JsonIgnore
    private Long ordrNum;// 序号
    @JsonProperty("仓库名称")
    private String whsNm;// 仓库名称
    @JsonProperty("部门编码")
    private String deptEncd;// 部门编码
    @JsonProperty("部门名称")
    private String deptName;// 部门名称
    @JsonProperty("仓库地址")
    private String whsAddr;// 仓库地址
    @JsonProperty("电话")
    private String tel;// 电话
    @JsonProperty("负责人")
    private String princ;// 负责人
    @JsonProperty("计价方式")
    private String valtnMode;// 计价方式
    @JsonProperty("对应条形码")
    private String crspdBarCd;// 对应条形码
    @JsonProperty("是否货位管理")
    private Integer isNtPrgrGdsBitMgmt;// 是否进行货位管理
    @JsonProperty("仓库属性")
    private String whsAttr;// 仓库属性
    @JsonProperty("销售可用量控制方式")
    private String sellAvalQtyCtrlMode;// 销售可用量控制方式
    @JsonProperty("库存可用量控制方式")
    private String invtyAvalQtyCtrlMode;// 库存可用量控制方式
    @JsonProperty("备注")
    private String memo;// 备注
    @JsonProperty("是否门店")
    private Integer isNtShop;// 是否门店
    @JsonProperty("停用日期")
    private String stpUseDt;// 停用日期
    @JsonProperty("省/直辖市")
    private String prov;// 省/直辖市
    @JsonProperty("市")
    private String cty;// 市
    @JsonProperty("区县")
    private String cnty;// 县
    @JsonProperty("是否虚拟仓")
    private Integer dumyWhs;// 虚拟仓(是否是虚拟仓)
    @JsonProperty("创建人")
    private String setupPers;// 创建人
    @JsonProperty("创建时间")
    private String setupTm;// 创建时间
    @JsonProperty("修改人")
    private String mdfr;// 修改人
    @JsonProperty("修改时间")
    private String modiTm;// 修改时间
    @JsonProperty("实体仓库")
    private String realWhs;// 大仓
    @JsonIgnore
    private List<MovBitTab> movBitList;

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getModiTm() {
        return modiTm;
    }

    public void setModiTm(String modiTm) {
        this.modiTm = modiTm;
    }


    public List<MovBitTab> getMovBitList() {
        return movBitList;
    }

    public void setMovBitList(List<MovBitTab> movBitList) {
        this.movBitList = movBitList;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public String getDeptEncd() {
        return deptEncd;
    }

    public void setDeptEncd(String deptEncd) {
        this.deptEncd = deptEncd;
    }

    public String getWhsAddr() {
        return whsAddr;
    }

    public void setWhsAddr(String whsAddr) {
        this.whsAddr = whsAddr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPrinc() {
        return princ;
    }

    public void setPrinc(String princ) {
        this.princ = princ;
    }

    public String getValtnMode() {
        return valtnMode;
    }

    public void setValtnMode(String valtnMode) {
        this.valtnMode = valtnMode;
    }

    public String getCrspdBarCd() {
        return crspdBarCd;
    }

    public void setCrspdBarCd(String crspdBarCd) {
        this.crspdBarCd = crspdBarCd;
    }

    public Integer getIsNtPrgrGdsBitMgmt() {
        return isNtPrgrGdsBitMgmt;
    }

    public void setIsNtPrgrGdsBitMgmt(Integer isNtPrgrGdsBitMgmt) {
        this.isNtPrgrGdsBitMgmt = isNtPrgrGdsBitMgmt;
    }

    public String getWhsAttr() {
        return whsAttr;
    }

    public void setWhsAttr(String whsAttr) {
        this.whsAttr = whsAttr;
    }

    public String getSellAvalQtyCtrlMode() {
        return sellAvalQtyCtrlMode;
    }

    public void setSellAvalQtyCtrlMode(String sellAvalQtyCtrlMode) {
        this.sellAvalQtyCtrlMode = sellAvalQtyCtrlMode;
    }

    public String getInvtyAvalQtyCtrlMode() {
        return invtyAvalQtyCtrlMode;
    }

    public void setInvtyAvalQtyCtrlMode(String invtyAvalQtyCtrlMode) {
        this.invtyAvalQtyCtrlMode = invtyAvalQtyCtrlMode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsNtShop() {
        return isNtShop;
    }

    public void setIsNtShop(Integer isNtShop) {
        this.isNtShop = isNtShop;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCty() {
        return cty;
    }

    public void setCty(String cty) {
        this.cty = cty;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public Integer getDumyWhs() {
        return dumyWhs;
    }

    public void setDumyWhs(Integer dumyWhs) {
        this.dumyWhs = dumyWhs;
    }

    public String getSetupPers() {
        return setupPers;
    }

    public void setSetupPers(String setupPers) {
        this.setupPers = setupPers;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
    }

    public String getStpUseDt() {
        return stpUseDt;
    }

    public void setStpUseDt(String stpUseDt) {
        this.stpUseDt = stpUseDt;
    }

    public String getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(String setupTm) {
        this.setupTm = setupTm;
    }

}