package com.px.mis.whs.entity;

import java.io.Serializable;

/**
 * whs_gds
 *
 * @author
 */
public class WhsGds implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * pc 总仓 <br>
     * pda 仓库编码
     */

    private String realWhs;

    /**
     * pc 总仓 <br>
     * pda 仓库名称
     */

    private String realNm;
    /**
     * 区域编码
     */
    private String regnEncd;
    /**
     * 区域名称
     */
    private String regnNm;

    /**
     * 货位编码
     */
    private String gdsBitEncd;
    /**
     * 货位名称
     */

    private String gdsBitNm;

    public String getRealWhs() {
        return realWhs;
    }

    public void setRealWhs(String realWhs) {
        this.realWhs = realWhs;
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

    public String getRealNm() {
        return realNm;
    }

    public void setRealNm(String realNm) {
        this.realNm = realNm;
    }

    public String getRegnEncd() {
        return regnEncd;
    }

    public void setRegnEncd(String regnEncd) {
        this.regnEncd = regnEncd;
    }

    public String getRegnNm() {
        return regnNm;
    }

    public void setRegnNm(String regnNm) {
        this.regnNm = regnNm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}