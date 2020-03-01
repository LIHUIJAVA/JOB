package com.px.mis.whs.entity;

public class City {

    private String codeId;//代码

    private String codeName;//代码名称

    private String abbreviation;//简称

    private String simplicity;//简拼

    private String codeLevel;//代码级别

    private String superiorCode;//上级代码

    private String sign;//系统或自定义标志

    private Integer isSubordinateCode;//是否有下级代码(0无下级，1有下级)

    private Integer displayOrHide;//显示或隐藏

    private String memo;//备注

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getSimplicity() {
        return simplicity;
    }

    public void setSimplicity(String simplicity) {
        this.simplicity = simplicity;
    }

    public String getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(String codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getSuperiorCode() {
        return superiorCode;
    }

    public void setSuperiorCode(String superiorCode) {
        this.superiorCode = superiorCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getIsSubordinateCode() {
        return isSubordinateCode;
    }

    public void setIsSubordinateCode(Integer isSubordinateCode) {
        this.isSubordinateCode = isSubordinateCode;
    }

    public Integer getDisplayOrHide() {
        return displayOrHide;
    }

    public void setDisplayOrHide(Integer displayOrHide) {
        this.displayOrHide = displayOrHide;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


}
