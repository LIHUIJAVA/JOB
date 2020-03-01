package com.px.mis.ec.entity;

//ÔùÆ··¶Î§Ö÷
public class PresentRange {
    private String presentRangeEncd;// ÔùÆ··¶Î§±àÂë
    private String presentRangeName;// ÔùÆ··¶Î§Ãû³Æ
    public final String getPresentRangeEncd() {
        return presentRangeEncd;
    }
    public final void setPresentRangeEncd(String presentRangeEncd) {
        this.presentRangeEncd = presentRangeEncd;
    }
    public final String getPresentRangeName() {
        return presentRangeName;
    }
    public final void setPresentRangeName(String presentRangeName) {
        this.presentRangeName = presentRangeName;
    }
    @Override
    public String toString() {
        return String.format("PresentRange [presentRangeEncd=%s, presentRangeName=%s]", presentRangeEncd,
                presentRangeName);
    }


}
