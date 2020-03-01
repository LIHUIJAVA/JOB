package com.px.mis.whs.entity;

import java.math.BigDecimal;

//地堆
public class site {
    private String trayTyp;//托盘类型

    private BigDecimal trayQty;//托盘数量

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