package com.px.mis.whs.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * labeltemplates 打印模板
 *
 * @author
 */
public class LabelTemplates implements Serializable {
    /**
     * 编号
     */
    private Long idx;

    /**
     * 模板编码
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 操作员
     */
    private String opid;

    /**
     * 操作员姓名
     */
    private String opname;

    /**
     * 操作日期
     */
    private String opdate;

    /**
     * 备注
     */
    private String reamrk;

    private static final long serialVersionUID = 1L;

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }

    public String getOpname() {
        return opname;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }


    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk;
    }

    public final String getOpdate() {
        return opdate;
    }

    public final void setOpdate(String opdate) {
        this.opdate = opdate;
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
        LabelTemplates other = (LabelTemplates) that;
        return (this.getIdx() == null ? other.getIdx() == null : this.getIdx().equals(other.getIdx()))
                && (this.getTemplateId() == null ? other.getTemplateId() == null
                : this.getTemplateId().equals(other.getTemplateId()))
                && (this.getTemplateName() == null ? other.getTemplateName() == null
                : this.getTemplateName().equals(other.getTemplateName()))
                && (this.getOpid() == null ? other.getOpid() == null : this.getOpid().equals(other.getOpid()))
                && (this.getOpname() == null ? other.getOpname() == null : this.getOpname().equals(other.getOpname()))
                && (this.getOpdate() == null ? other.getOpdate() == null : this.getOpdate().equals(other.getOpdate()))
                && (this.getReamrk() == null ? other.getReamrk() == null : this.getReamrk().equals(other.getReamrk()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdx() == null) ? 0 : getIdx().hashCode());
        result = prime * result + ((getTemplateId() == null) ? 0 : getTemplateId().hashCode());
        result = prime * result + ((getTemplateName() == null) ? 0 : getTemplateName().hashCode());
        result = prime * result + ((getOpid() == null) ? 0 : getOpid().hashCode());
        result = prime * result + ((getOpname() == null) ? 0 : getOpname().hashCode());
        result = prime * result + ((getOpdate() == null) ? 0 : getOpdate().hashCode());
        result = prime * result + ((getReamrk() == null) ? 0 : getReamrk().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", idx=").append(idx);
        sb.append(", templateId=").append(templateId);
        sb.append(", templateName=").append(templateName);
        sb.append(", opid=").append(opid);
        sb.append(", opname=").append(opname);
        sb.append(", opdate=").append(opdate);
        sb.append(", reamrk=").append(reamrk);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}