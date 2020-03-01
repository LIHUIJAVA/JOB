package com.px.mis.whs.entity;

import java.io.Serializable;

/**
 * labeltemplaterelation ģ���Ӧ��
 *
 * @author
 */
public class LabelTemplateRelation implements Serializable {
    /**
     * ���
     */
    private Integer idx;

    /**
     * �ֿ����
     */
    private String warehouseId;

    /**
     * �ֿ�����
     */
    private String warehouseName;

    /**
     * ƽ̨����
     */
    private String platformId;

    /**
     * ƽ̨����
     */
    private String platformName;

    /**
     * ģ�����(��ݹ�˾)
     */
    private String templateId;

    private static final long serialVersionUID = 1L;

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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
        LabelTemplateRelation other = (LabelTemplateRelation) that;
        return (this.getIdx() == null ? other.getIdx() == null : this.getIdx().equals(other.getIdx()))
                && (this.getWarehouseId() == null ? other.getWarehouseId() == null
                : this.getWarehouseId().equals(other.getWarehouseId()))
                && (this.getWarehouseName() == null ? other.getWarehouseName() == null
                : this.getWarehouseName().equals(other.getWarehouseName()))
                && (this.getPlatformId() == null ? other.getPlatformId() == null
                : this.getPlatformId().equals(other.getPlatformId()))
                && (this.getPlatformName() == null ? other.getPlatformName() == null
                : this.getPlatformName().equals(other.getPlatformName()))
                && (this.getTemplateId() == null ? other.getTemplateId() == null
                : this.getTemplateId().equals(other.getTemplateId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdx() == null) ? 0 : getIdx().hashCode());
        result = prime * result + ((getWarehouseId() == null) ? 0 : getWarehouseId().hashCode());
        result = prime * result + ((getWarehouseName() == null) ? 0 : getWarehouseName().hashCode());
        result = prime * result + ((getPlatformId() == null) ? 0 : getPlatformId().hashCode());
        result = prime * result + ((getPlatformName() == null) ? 0 : getPlatformName().hashCode());
        result = prime * result + ((getTemplateId() == null) ? 0 : getTemplateId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", idx=").append(idx);
        sb.append(", warehouseId=").append(warehouseId);
        sb.append(", warehouseName=").append(warehouseName);
        sb.append(", platformId=").append(platformId);
        sb.append(", platformName=").append(platformName);
        sb.append(", templateId=").append(templateId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}