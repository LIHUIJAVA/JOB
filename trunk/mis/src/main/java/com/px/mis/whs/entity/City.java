package com.px.mis.whs.entity;

public class City {

    private String codeId;//����

    private String codeName;//��������

    private String abbreviation;//���

    private String simplicity;//��ƴ

    private String codeLevel;//���뼶��

    private String superiorCode;//�ϼ�����

    private String sign;//ϵͳ���Զ����־

    private Integer isSubordinateCode;//�Ƿ����¼�����(0���¼���1���¼�)

    private Integer displayOrHide;//��ʾ������

    private String memo;//��ע

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
