package com.px.mis.purc.entity;

public class ProjCls {
	
	private String projEncd;//项目编码
    private String projNm;//项目名称
    private Integer	ordrNum;   //项目名称
    private String	memo;//备注
    
    public String getProjEncd() {
        return projEncd;
    }

    public void setProjEncd(String projEncd) {
        this.projEncd = projEncd == null ? null : projEncd.trim();
    }
    public String getProjNm() {
        return projNm;
    }

    public void setProjNm(String projNm) {
        this.projNm = projNm == null ? null : projNm.trim();
    }
	public Integer getordrNum() {
		return ordrNum;
	}
	public void setordrNum(Integer ordrNum) {
		this.ordrNum = ordrNum;
	}
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}