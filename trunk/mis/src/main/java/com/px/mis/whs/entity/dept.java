package com.px.mis.whs.entity;

//部门
public class dept {
    private String deptEncd;//部门编码

    private Long ordrNum;//序号

    private String whsEncd;//仓库编码

    private String deptNm;//部门名称

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