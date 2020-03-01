package com.px.mis.ec.entity;

public class ProActivity {

    private String proActId; // 促销活动编号
    private String proActName; // 促销活动名称
    private String startDate; // 起始日期
    private String endDate; // 结束日期
    private String aloneExecute; // 单独执行
    private Integer limitPro; // 限量促销
    private Integer priority; // 优先级
    private String takeStore; // 参与店铺
    private String creator; // 制单人
    private String createDate; // 制单日期
    private String auditor; // 审核人
    private String auditDate; // 审核日期
    private String auditResult; // 审核结果
    private String memo; // 备注

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
