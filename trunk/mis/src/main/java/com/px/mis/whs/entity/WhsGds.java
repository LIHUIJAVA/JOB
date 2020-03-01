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
     * pc �ܲ� <br>
     * pda �ֿ����
     */

    private String realWhs;

    /**
     * pc �ܲ� <br>
     * pda �ֿ�����
     */

    private String realNm;
    /**
     * �������
     */
    private String regnEncd;
    /**
     * ��������
     */
    private String regnNm;

    /**
     * ��λ����
     */
    private String gdsBitEncd;
    /**
     * ��λ����
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