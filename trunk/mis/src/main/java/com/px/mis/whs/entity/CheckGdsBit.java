package com.px.mis.whs.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * check_gds_bit
 *
 * @author
 */
public class CheckGdsBit implements Serializable {
    private Long id;

    /**
     * 子单号
     */
    private String ordrNum;

    /**
     * 主单号
     */
    private String formNum;

    /**
     * 账面数量
     */
    private BigDecimal bookQty;

    /**
     * 盘点数量
     */
    private BigDecimal checkQty;

    /**
     * 盈亏数量
     */
    private BigDecimal prftLossQty;// 盈亏数量
    private BigDecimal prftLossQtys;// 损益数量
    private BigDecimal adjIntoWhsQty;// 调整入库数量
    private BigDecimal adjOutWhsQty;// 调整出库数量
    private BigDecimal bookAdjustQty;// 账面调节数量

    /**
     * 仓库编码
     */
    private String whsEncd;

    /**
     * 区域编码
     */
    private String regnEncd;

    /**
     * 货位编码
     */
    private String gdsBitEncd;

    public final BigDecimal getPrftLossQty() {
        return prftLossQty;
    }

    public final void setPrftLossQty(BigDecimal prftLossQty) {
        this.prftLossQty = prftLossQty;
    }

    public final BigDecimal getPrftLossQtys() {
        return prftLossQtys;
    }

    public final void setPrftLossQtys(BigDecimal prftLossQtys) {
        this.prftLossQtys = prftLossQtys;
    }

    public final BigDecimal getAdjIntoWhsQty() {
        return adjIntoWhsQty;
    }

    public final void setAdjIntoWhsQty(BigDecimal adjIntoWhsQty) {
        this.adjIntoWhsQty = adjIntoWhsQty;
    }

    public final BigDecimal getAdjOutWhsQty() {
        return adjOutWhsQty;
    }

    public final void setAdjOutWhsQty(BigDecimal adjOutWhsQty) {
        this.adjOutWhsQty = adjOutWhsQty;
    }

    public final BigDecimal getBookAdjustQty() {
        return bookAdjustQty;
    }

    public final void setBookAdjustQty(BigDecimal bookAdjustQty) {
        this.bookAdjustQty = bookAdjustQty;
    }

    public static final long getSerialversionuid() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(String ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }

    public BigDecimal getBookQty() {
        return bookQty;
    }

    public void setBookQty(BigDecimal bookQty) {
        this.bookQty = bookQty;
    }

    public BigDecimal getCheckQty() {
        return checkQty;
    }

    public void setCheckQty(BigDecimal checkQty) {
        this.checkQty = checkQty;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public String getGdsBitEncd() {
        return gdsBitEncd;
    }

    public void setGdsBitEncd(String gdsBitEncd) {
        this.gdsBitEncd = gdsBitEncd;
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
        CheckGdsBit other = (CheckGdsBit) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOrdrNum() == null ? other.getOrdrNum() == null
                : this.getOrdrNum().equals(other.getOrdrNum()))
                && (this.getFormNum() == null ? other.getFormNum() == null
                : this.getFormNum().equals(other.getFormNum()))
                && (this.getBookQty() == null ? other.getBookQty() == null
                : this.getBookQty().equals(other.getBookQty()))
                && (this.getCheckQty() == null ? other.getCheckQty() == null
                : this.getCheckQty().equals(other.getCheckQty()))
                && (this.getWhsEncd() == null ? other.getWhsEncd() == null
                : this.getWhsEncd().equals(other.getWhsEncd()))
                && (this.getRegnEncd() == null ? other.getRegnEncd() == null
                : this.getRegnEncd().equals(other.getRegnEncd()))
                && (this.getGdsBitEncd() == null ? other.getGdsBitEncd() == null
                : this.getGdsBitEncd().equals(other.getGdsBitEncd()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrdrNum() == null) ? 0 : getOrdrNum().hashCode());
        result = prime * result + ((getFormNum() == null) ? 0 : getFormNum().hashCode());
        result = prime * result + ((getBookQty() == null) ? 0 : getBookQty().hashCode());
        result = prime * result + ((getCheckQty() == null) ? 0 : getCheckQty().hashCode());
        result = prime * result + ((getWhsEncd() == null) ? 0 : getWhsEncd().hashCode());
        result = prime * result + ((getRegnEncd() == null) ? 0 : getRegnEncd().hashCode());
        result = prime * result + ((getGdsBitEncd() == null) ? 0 : getGdsBitEncd().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", ordrNum=").append(ordrNum);
        sb.append(", formNum=").append(formNum);
        sb.append(", bookQty=").append(bookQty);
        sb.append(", checkQty=").append(checkQty);
        sb.append(", whsEncd=").append(whsEncd);
        sb.append(", regnEncd=").append(regnEncd);
        sb.append(", gdsBitEncd=").append(gdsBitEncd);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}