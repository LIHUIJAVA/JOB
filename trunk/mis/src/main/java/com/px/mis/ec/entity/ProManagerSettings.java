package com.px.mis.ec.entity;

public class ProManagerSettings {
	
    private String settingId;//���ñ��
    private Integer downPro;//��������ƥ������
    private Integer auditPro;//�������ƥ������
    private Integer offlineOnline;//���ϴ��������������´���
    private Integer actTime;//�ʱ��ƥ��

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