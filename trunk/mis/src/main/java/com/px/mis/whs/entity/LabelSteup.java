package com.px.mis.whs.entity;

import java.io.Serializable;

/**
 * labelsteup 标签设置
 *
 * @author
 */
public class LabelSteup implements Serializable {
    /**
     * 序号
     */
    private Long idx;

    /**
     * 模板编码
     */
    private String templateId;

    /**
     * 标签编码
     */
    private String labelId;

    /**
     * 标签名字
     */
    private String labelName;

    /**
     * 标签类型
     */
    private Integer labelTypeValue;

    /**
     * 条码类型
     */
    private Integer barcodeTypeValue;

    /**
     * 字体大小
     */
    private Integer labelSize;

    /**
     * X坐标
     */
    private Integer positionX;

    /**
     * Z坐标
     */
    private Integer positionY;

    /**
     * Y坐标
     */
    private Integer positionZ;

    /**
     * 条码高度
     */
    private Integer barcodeHigh;

    /**
     * 条码宽度
     */
    private Integer barcodeWide;

    /**
     * 旋转角度
     */
    private Integer rotation;

    /**
     * 是否固定值
     */
    private Integer isfixed;

    /**
     * 动态数据字段
     */
    private String printfieldname;

    /**
     * 打印的内容
     */
    private String printcontent;

    /**
     * 备注
     */
    private String remark;

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

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getLabelTypeValue() {
        return labelTypeValue;
    }

    public void setLabelTypeValue(Integer labelTypeValue) {
        this.labelTypeValue = labelTypeValue;
    }

    public Integer getBarcodeTypeValue() {
        return barcodeTypeValue;
    }

    public void setBarcodeTypeValue(Integer barcodeTypeValue) {
        this.barcodeTypeValue = barcodeTypeValue;
    }

    public Integer getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(Integer labelSize) {
        this.labelSize = labelSize;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public void setPositionY(Integer positionY) {
        this.positionY = positionY;
    }

    public Integer getPositionZ() {
        return positionZ;
    }

    public void setPositionZ(Integer positionZ) {
        this.positionZ = positionZ;
    }

    public Integer getBarcodeHigh() {
        return barcodeHigh;
    }

    public void setBarcodeHigh(Integer barcodeHigh) {
        this.barcodeHigh = barcodeHigh;
    }

    public Integer getBarcodeWide() {
        return barcodeWide;
    }

    public void setBarcodeWide(Integer barcodeWide) {
        this.barcodeWide = barcodeWide;
    }

    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    public Integer getIsfixed() {
        return isfixed;
    }

    public void setIsfixed(Integer isfixed) {
        this.isfixed = isfixed;
    }

    public String getPrintfieldname() {
        return printfieldname;
    }

    public void setPrintfieldname(String printfieldname) {
        this.printfieldname = printfieldname;
    }

    public String getPrintcontent() {
        return printcontent;
    }

    public void setPrintcontent(String printcontent) {
        this.printcontent = printcontent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        LabelSteup other = (LabelSteup) that;
        return (this.getIdx() == null ? other.getIdx() == null : this.getIdx().equals(other.getIdx()))
                && (this.getTemplateId() == null ? other.getTemplateId() == null
                : this.getTemplateId().equals(other.getTemplateId()))
                && (this.getLabelId() == null ? other.getLabelId() == null
                : this.getLabelId().equals(other.getLabelId()))
                && (this.getLabelName() == null ? other.getLabelName() == null
                : this.getLabelName().equals(other.getLabelName()))
                && (this.getLabelTypeValue() == null ? other.getLabelTypeValue() == null
                : this.getLabelTypeValue().equals(other.getLabelTypeValue()))
                && (this.getBarcodeTypeValue() == null ? other.getBarcodeTypeValue() == null
                : this.getBarcodeTypeValue().equals(other.getBarcodeTypeValue()))
                && (this.getLabelSize() == null ? other.getLabelSize() == null
                : this.getLabelSize().equals(other.getLabelSize()))
                && (this.getPositionX() == null ? other.getPositionX() == null
                : this.getPositionX().equals(other.getPositionX()))
                && (this.getPositionY() == null ? other.getPositionY() == null
                : this.getPositionY().equals(other.getPositionY()))
                && (this.getPositionZ() == null ? other.getPositionZ() == null
                : this.getPositionZ().equals(other.getPositionZ()))
                && (this.getBarcodeHigh() == null ? other.getBarcodeHigh() == null
                : this.getBarcodeHigh().equals(other.getBarcodeHigh()))
                && (this.getBarcodeWide() == null ? other.getBarcodeWide() == null
                : this.getBarcodeWide().equals(other.getBarcodeWide()))
                && (this.getRotation() == null ? other.getRotation() == null
                : this.getRotation().equals(other.getRotation()))
                && (this.getIsfixed() == null ? other.getIsfixed() == null
                : this.getIsfixed().equals(other.getIsfixed()))
                && (this.getPrintfieldname() == null ? other.getPrintfieldname() == null
                : this.getPrintfieldname().equals(other.getPrintfieldname()))
                && (this.getPrintcontent() == null ? other.getPrintcontent() == null
                : this.getPrintcontent().equals(other.getPrintcontent()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIdx() == null) ? 0 : getIdx().hashCode());
        result = prime * result + ((getTemplateId() == null) ? 0 : getTemplateId().hashCode());
        result = prime * result + ((getLabelId() == null) ? 0 : getLabelId().hashCode());
        result = prime * result + ((getLabelName() == null) ? 0 : getLabelName().hashCode());
        result = prime * result + ((getLabelTypeValue() == null) ? 0 : getLabelTypeValue().hashCode());
        result = prime * result + ((getBarcodeTypeValue() == null) ? 0 : getBarcodeTypeValue().hashCode());
        result = prime * result + ((getLabelSize() == null) ? 0 : getLabelSize().hashCode());
        result = prime * result + ((getPositionX() == null) ? 0 : getPositionX().hashCode());
        result = prime * result + ((getPositionY() == null) ? 0 : getPositionY().hashCode());
        result = prime * result + ((getPositionZ() == null) ? 0 : getPositionZ().hashCode());
        result = prime * result + ((getBarcodeHigh() == null) ? 0 : getBarcodeHigh().hashCode());
        result = prime * result + ((getBarcodeWide() == null) ? 0 : getBarcodeWide().hashCode());
        result = prime * result + ((getRotation() == null) ? 0 : getRotation().hashCode());
        result = prime * result + ((getIsfixed() == null) ? 0 : getIsfixed().hashCode());
        result = prime * result + ((getPrintfieldname() == null) ? 0 : getPrintfieldname().hashCode());
        result = prime * result + ((getPrintcontent() == null) ? 0 : getPrintcontent().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
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
        sb.append(", labelId=").append(labelId);
        sb.append(", labelName=").append(labelName);
        sb.append(", labelTypeValue=").append(labelTypeValue);
        sb.append(", barcodeTypeValue=").append(barcodeTypeValue);
        sb.append(", labelSize=").append(labelSize);
        sb.append(", positionX=").append(positionX);
        sb.append(", positionY=").append(positionY);
        sb.append(", positionZ=").append(positionZ);
        sb.append(", barcodeHigh=").append(barcodeHigh);
        sb.append(", barcodeWide=").append(barcodeWide);
        sb.append(", rotation=").append(rotation);
        sb.append(", isfixed=").append(isfixed);
        sb.append(", printfieldname=").append(printfieldname);
        sb.append(", printcontent=").append(printcontent);
        sb.append(", remark=").append(remark);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}