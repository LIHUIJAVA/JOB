package com.px.mis.purc.entity;

/*�������*/
public class InvtyCls {
    private String invtyClsEncd; //����������

    private String invtyClsNm; //�����������

    private String ico;    //icoͼƬ
    
    private Integer level;  //����
    
    private String pid;    //��Ӧ����id
    
    private String memo;   //��ע
    
   private String projEncd;   //��Ŀ����

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