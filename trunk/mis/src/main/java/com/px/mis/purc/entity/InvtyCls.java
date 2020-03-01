package com.px.mis.purc.entity;

/*存货分类*/
public class InvtyCls {
    private String invtyClsEncd; //存货分类编码

    private String invtyClsNm; //存货分类名称

    private String ico;    //ico图片
    
    private Integer level;  //级别
    
    private String pid;    //对应父级id
    
    private String memo;   //备注
    
   private String projEncd;   //项目编码

	public String getProjEncd() {
		return projEncd;
	}

	public void setProjEncd(String projEncd) {
		this.projEncd = projEncd;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}
	

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getInvtyClsEncd() {
        return invtyClsEncd;
    }

    public void setInvtyClsEncd(String invtyClsEncd) {
        this.invtyClsEncd = invtyClsEncd == null ? null : invtyClsEncd.trim();
    }

    public String getInvtyClsNm() {
        return invtyClsNm;
    }

    public void setInvtyClsNm(String invtyClsNm) {
        this.invtyClsNm = invtyClsNm == null ? null : invtyClsNm.trim();
    }

}