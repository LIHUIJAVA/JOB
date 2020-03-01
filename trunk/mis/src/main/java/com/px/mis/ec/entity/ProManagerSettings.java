package com.px.mis.ec.entity;

public class ProManagerSettings {
	
    private String settingId;//设置编号
    private Integer downPro;//订单下载匹配促销活动
    private Integer auditPro;//订单审核匹配促销活动
    private Integer offlineOnline;//线上促销订单参与线下促销
    private Integer actTime;//活动时间匹配

    public String getSettingId() {
        return settingId;
    }

    public void setSettingId(String settingId) {
        this.settingId = settingId;
    }

    public Integer getDownPro() {
        return downPro;
    }

    public void setDownPro(Integer downPro) {
        this.downPro = downPro;
    }

    public Integer getAuditPro() {
        return auditPro;
    }

    public void setAuditPro(Integer auditPro) {
        this.auditPro = auditPro;
    }

    public Integer getOfflineOnline() {
        return offlineOnline;
    }

    public void setOfflineOnline(Integer offlineOnline) {
        this.offlineOnline = offlineOnline;
    }

    public Integer getActTime() {
        return actTime;
    }

    public void setActTime(Integer actTime) {
        this.actTime = actTime;
    }
}