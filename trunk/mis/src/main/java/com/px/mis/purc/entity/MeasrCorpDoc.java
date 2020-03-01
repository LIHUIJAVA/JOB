package com.px.mis.purc.entity;

public class MeasrCorpDoc {
    private String measrCorpId;//计量单位编号

    private String measrCorpNm;//计量单位名称

    private String memo;//备注

    public String getMeasrCorpId() {
        return measrCorpId;
    }

    public void setMeasrCorpId(String measrCorpId) {
        this.measrCorpId = measrCorpId == null ? null : measrCorpId.trim();
    }

    public String getMeasrCorpNm() {
        return measrCorpNm;
    }

    public void setMeasrCorpNm(String measrCorpNm) {
        this.measrCorpNm = measrCorpNm == null ? null : measrCorpNm.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}