package com.px.mis.whs.entity;

import java.math.BigDecimal;

//�ض�
public class site {
    private String trayTyp;//��������

    private BigDecimal trayQty;//��������

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