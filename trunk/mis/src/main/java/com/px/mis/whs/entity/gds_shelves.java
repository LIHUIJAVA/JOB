package com.px.mis.whs.entity;

import java.math.BigDecimal;

//货架
public class gds_shelves {
    private String gdsShelvesEncd;//货架编码

    private String gdsShelvesNm;//货架名称

    private String trayTyp;//托盘类型

    private BigDecimal trayQty;//托盘数量

    public String getGdsShelvesEncd() {
        return gdsShelvesEncd;
    }

    public void setGdsShelvesEncd(String gdsShelvesEncd) {
        this.gdsShelvesEncd = gdsShelvesEncd == null ? null : gdsShelvesEncd.trim();
    }

    public String getGdsShelvesNm() {
        return gdsShelvesNm;
    }

    public void setGdsShelvesNm(String gdsShelvesNm) {
        this.gdsShelvesNm = gdsShelvesNm == null ? null : gdsShelvesNm.trim();
    }

    public String getTrayTyp() {
        return trayTyp;
    }

    public void setTrayTyp(String trayTyp) {
        this.trayTyp = trayTyp == null ? null : trayTyp.trim();
    }

    public BigDecimal getTrayQty() {
        return trayQty;
    }

    public void setTrayQty(BigDecimal trayQty) {
        this.trayQty = trayQty;
    }
}