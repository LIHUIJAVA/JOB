package com.px.mis.whs.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * real_whs
 *
 * @author
 */
public class RealWhs implements Serializable {
    private Integer id;

    /**
     * 总仓库编码
     */
    private String realWhs;
    /**
     * 仓库名称
     */
    private String realNm;
    /**
     * 部门编码
     */
    private String deptEncd;
    /**
     * 部门编码
     */
    private String deptName;
    /**
     * 仓库地址
     */
    private String whsAddr;

    /**
     * 电话
     */
    private String tel;

    /**
     * 负责人
     */
    private String princ;

    /**
     * 对应条形码
     */
    private String crspdBarCd;

    /**
     * 是否进行货位管理
     */
    private Integer isNtPrgrGdsBitMgmt;

    /**
     * 仓库属性
     */
    private String whsAttr;

    /**
     * 销售可用量控制方式
     */
    private String sellAvalQtyCtrlMode;

    /**
     * 库存可用量控制方式
     */
    private String invtyAvalQtyCtrlMode;

    /**
     * 备注
     */
    private String memo;

    /**
     * 计价方式
     */
    private String valtnMode;

    /**
     * 是否门店
     */
    private Integer isNtShop;

    /**
     * 停用日期
     */
    private String stpUseDt;

    /**
     * 省/直辖市
     */
    private String prov;

    /**
     * 市
     */
    private String cty;

    /**
     * 县
     */
    private String cnty;

    /**
     * 虚拟仓
     */
    private Integer dumyWhs;

    /**
     * 创建人
     */
    private String setupPers;

    /**
     * 创建时间
     */
    private Date setupTm;

    /**
     * 修改人
     */
    private String mdfr;

    /**
     * 修改时间
     */
    private String modiTm;

    private List<RealWhs> realWhsList;


    public List<RealWhs> getRealWhsList() {
        return realWhsList;
    }

    public void setRealWhsList(List<RealWhs> realWhsList) {
        this.realWhsList = realWhsList;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
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

    public String getValtnMode() {
        return valtnMode;
    }

    public void setValtnMode(String valtnMode) {
        this.valtnMode = valtnMode;
    }

    public Integer getIsNtShop() {
        return isNtShop;
    }

    public void setIsNtShop(Integer isNtShop) {
        this.isNtShop = isNtShop;
    }

    public String getStpUseDt() {
        return stpUseDt;
    }

    public void setStpUseDt(String stpUseDt) {
        this.stpUseDt = stpUseDt;
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

    public Date getSetupTm() {
        return setupTm;
    }

    public void setSetupTm(Date setupTm) {
        this.setupTm = setupTm;
    }

    public String getMdfr() {
        return mdfr;
    }

    public void setMdfr(String mdfr) {
        this.mdfr = mdfr;
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

    public String getRealNm() {
        return realNm;
    }

    public void setRealNm(String realNm) {
        this.realNm = realNm;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RealWhs other = (RealWhs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRealWhs() == null ? other.getRealWhs() == null
                : this.getRealWhs().equals(other.getRealWhs()))
                && (this.getDeptEncd() == null ? other.getDeptEncd() == null
                : this.getDeptEncd().equals(other.getDeptEncd()))
                && (this.getWhsAddr() == null ? other.getWhsAddr() == null
                : this.getWhsAddr().equals(other.getWhsAddr()))
                && (this.getTel() == null ? other.getTel() == null : this.getTel().equals(other.getTel()))
                && (this.getPrinc() == null ? other.getPrinc() == null : this.getPrinc().equals(other.getPrinc()))
                && (this.getCrspdBarCd() == null ? other.getCrspdBarCd() == null
                : this.getCrspdBarCd().equals(other.getCrspdBarCd()))
                && (this.getIsNtPrgrGdsBitMgmt() == null ? other.getIsNtPrgrGdsBitMgmt() == null
                : this.getIsNtPrgrGdsBitMgmt().equals(other.getIsNtPrgrGdsBitMgmt()))
                && (this.getWhsAttr() == null ? other.getWhsAttr() == null
                : this.getWhsAttr().equals(other.getWhsAttr()))
                && (this.getSellAvalQtyCtrlMode() == null ? other.getSellAvalQtyCtrlMode() == null
                : this.getSellAvalQtyCtrlMode().equals(other.getSellAvalQtyCtrlMode()))
                && (this.getInvtyAvalQtyCtrlMode() == null ? other.getInvtyAvalQtyCtrlMode() == null
                : this.getInvtyAvalQtyCtrlMode().equals(other.getInvtyAvalQtyCtrlMode()))
                && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
                && (this.getValtnMode() == null ? other.getValtnMode() == null
                : this.getValtnMode().equals(other.getValtnMode()))
                && (this.getIsNtShop() == null ? other.getIsNtShop() == null
                : this.getIsNtShop().equals(other.getIsNtShop()))
                && (this.getStpUseDt() == null ? other.getStpUseDt() == null
                : this.getStpUseDt().equals(other.getStpUseDt()))
                && (this.getProv() == null ? other.getProv() == null : this.getProv().equals(other.getProv()))
                && (this.getCty() == null ? other.getCty() == null : this.getCty().equals(other.getCty()))
                && (this.getCnty() == null ? other.getCnty() == null : this.getCnty().equals(other.getCnty()))
                && (this.getDumyWhs() == null ? other.getDumyWhs() == null
                : this.getDumyWhs().equals(other.getDumyWhs()))
                && (this.getSetupPers() == null ? other.getSetupPers() == null
                : this.getSetupPers().equals(other.getSetupPers()))
                && (this.getSetupTm() == null ? other.getSetupTm() == null
                : this.getSetupTm().equals(other.getSetupTm()))
                && (this.getMdfr() == null ? other.getMdfr() == null : this.getMdfr().equals(other.getMdfr()))
                && (this.getModiTm() == null ? other.getModiTm() == null : this.getModiTm().equals(other.getModiTm()))
                && (this.getRealNm() == null ? other.getRealNm() == null : this.getRealNm().equals(other.getRealNm()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRealWhs() == null) ? 0 : getRealWhs().hashCode());
        result = prime * result + ((getDeptEncd() == null) ? 0 : getDeptEncd().hashCode());
        result = prime * result + ((getWhsAddr() == null) ? 0 : getWhsAddr().hashCode());
        result = prime * result + ((getTel() == null) ? 0 : getTel().hashCode());
        result = prime * result + ((getPrinc() == null) ? 0 : getPrinc().hashCode());
        result = prime * result + ((getCrspdBarCd() == null) ? 0 : getCrspdBarCd().hashCode());
        result = prime * result + ((getIsNtPrgrGdsBitMgmt() == null) ? 0 : getIsNtPrgrGdsBitMgmt().hashCode());
        result = prime * result + ((getWhsAttr() == null) ? 0 : getWhsAttr().hashCode());
        result = prime * result + ((getSellAvalQtyCtrlMode() == null) ? 0 : getSellAvalQtyCtrlMode().hashCode());
        result = prime * result + ((getInvtyAvalQtyCtrlMode() == null) ? 0 : getInvtyAvalQtyCtrlMode().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getValtnMode() == null) ? 0 : getValtnMode().hashCode());
        result = prime * result + ((getIsNtShop() == null) ? 0 : getIsNtShop().hashCode());
        result = prime * result + ((getStpUseDt() == null) ? 0 : getStpUseDt().hashCode());
        result = prime * result + ((getProv() == null) ? 0 : getProv().hashCode());
        result = prime * result + ((getCty() == null) ? 0 : getCty().hashCode());
        result = prime * result + ((getCnty() == null) ? 0 : getCnty().hashCode());
        result = prime * result + ((getDumyWhs() == null) ? 0 : getDumyWhs().hashCode());
        result = prime * result + ((getSetupPers() == null) ? 0 : getSetupPers().hashCode());
        result = prime * result + ((getSetupTm() == null) ? 0 : getSetupTm().hashCode());
        result = prime * result + ((getMdfr() == null) ? 0 : getMdfr().hashCode());
        result = prime * result + ((getModiTm() == null) ? 0 : getModiTm().hashCode());
        result = prime * result + ((getRealNm() == null) ? 0 : getRealNm().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", realWhs=").append(realWhs);
        sb.append(", deptEncd=").append(deptEncd);
        sb.append(", whsAddr=").append(whsAddr);
        sb.append(", tel=").append(tel);
        sb.append(", princ=").append(princ);
        sb.append(", crspdBarCd=").append(crspdBarCd);
        sb.append(", isNtPrgrGdsBitMgmt=").append(isNtPrgrGdsBitMgmt);
        sb.append(", whsAttr=").append(whsAttr);
        sb.append(", sellAvalQtyCtrlMode=").append(sellAvalQtyCtrlMode);
        sb.append(", invtyAvalQtyCtrlMode=").append(invtyAvalQtyCtrlMode);
        sb.append(", memo=").append(memo);
        sb.append(", valtnMode=").append(valtnMode);
        sb.append(", isNtShop=").append(isNtShop);
        sb.append(", stpUseDt=").append(stpUseDt);
        sb.append(", prov=").append(prov);
        sb.append(", cty=").append(cty);
        sb.append(", cnty=").append(cnty);
        sb.append(", dumyWhs=").append(dumyWhs);
        sb.append(", setupPers=").append(setupPers);
        sb.append(", setupTm=").append(setupTm);
        sb.append(", mdfr=").append(mdfr);
        sb.append(", modiTm=").append(modiTm);
        sb.append(", realNm=").append(realNm);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}