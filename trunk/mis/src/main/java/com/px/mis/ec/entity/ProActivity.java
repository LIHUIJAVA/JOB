package com.px.mis.ec.entity;

public class ProActivity {

    private String proActId; // ��������
    private String proActName; // ���������
    private String startDate; // ��ʼ����
    private String endDate; // ��������
    private String aloneExecute; // ����ִ��
    private Integer limitPro; // ��������
    private Integer priority; // ���ȼ�
    private String takeStore; // �������
    private String creator; // �Ƶ���
    private String createDate; // �Ƶ�����
    private String auditor; // �����
    private String auditDate; // �������
    private String auditResult; // ��˽��
    private String memo; // ��ע

    public final Integer getPriority() {
        return priority;
    }

    public final void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getProActId() {
        return proActId;
    }

    public void setProActId(String proActId) {
        this.proActId = proActId;
    }

    public String getProActName() {
        return proActName;
    }

    public void setProActName(String proActName) {
        this.proActName = proActName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAloneExecute() {
        return aloneExecute;
    }

    public void setAloneExecute(String aloneExecute) {
        this.aloneExecute = aloneExecute;
    }

    public int getLimitPro() {
        return limitPro;
    }

    public void setLimitPro(Integer limitPro) {
        this.limitPro = limitPro;
    }

    public String getTakeStore() {
        return takeStore;
    }

    public void setTakeStore(String takeStore) {
        this.takeStore = takeStore;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
