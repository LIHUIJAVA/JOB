package com.px.mis.whs.entity;

//����
public class dept {
    private String deptEncd;//���ű���

    private Long ordrNum;//���

    private String whsEncd;//�ֿ����

    private String deptNm;//��������

    public String getDeptEncd() {
        return deptEncd;
    }

    public void setDeptEncd(String deptEncd) {
        this.deptEncd = deptEncd == null ? null : deptEncd.trim();
    }

    public Long getOrdrNum() {
        return ordrNum;
    }

    public void setOrdrNum(Long ordrNum) {
        this.ordrNum = ordrNum;
    }

    public String getWhsEncd() {
        return whsEncd;
    }

    public void setWhsEncd(String whsEncd) {
        this.whsEncd = whsEncd == null ? null : whsEncd.trim();
    }

    public String getDeptNm() {
        return deptNm;
    }

    public void setDeptNm(String deptNm) {
        this.deptNm = deptNm == null ? null : deptNm.trim();
    }
}