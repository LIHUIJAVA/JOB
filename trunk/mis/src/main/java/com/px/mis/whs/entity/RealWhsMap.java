package com.px.mis.whs.entity;

import java.io.Serializable;

/**
 * real_whs_map
 *
 * @author
 */
public class RealWhsMap implements Serializable {
    private Integer id;

    /**
     * ×Ü²Ö
     */
    private String realWhs;

    /**
     * ²Ö¿â
     */
    private String whsEncd;
    /**
     * ×Ü²ÖÃû³Æ
     */
    private String realNm;
    /**
     * ²Ö¿âÃû³Æ
     */
    private String whsNm;
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

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd;
    }

    public String getRealNm() {
        return realNm;
    }

    public void setRealNm(String realNm) {
        this.realNm = realNm;
    }

    public String getWhsNm() {
        return whsNm;
    }

    public void setWhsNm(String whsNm) {
        this.whsNm = whsNm;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
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
        RealWhsMap other = (RealWhsMap) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getRealWhs() == null ? other.getRealWhs() == null
                : this.getRealWhs().equals(other.getRealWhs()))
                && (this.getWhsEncd() == null ? other.getWhsEncd() == null
                : this.getWhsEncd().equals(other.getWhsEncd()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRealWhs() == null) ? 0 : getRealWhs().hashCode());
        result = prime * result + ((getWhsEncd() == null) ? 0 : getWhsEncd().hashCode());
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
        sb.append(", whsEncd=").append(whsEncd);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}