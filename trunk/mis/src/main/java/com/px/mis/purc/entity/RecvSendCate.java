package com.px.mis.purc.entity;

public class RecvSendCate {
    private String recvSendCateId; //收发类别编号

    private String recvSendCateNm; //收发类别名称

    private Integer recvSendInd;   //收发标识 — 收或发(1收 0发)

    private String ico;    //ico图片 
    
    private Integer level;  //级别
    
    private String pid;    //对应父级id
    
    private String cntPtySubjEncd; //对方科目编号
    
    private String memo;   //备注
    
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


    public String getRecvSendCateId() {
        return recvSendCateId;
    }

    public void setRecvSendCateId(String recvSendCateId) {
        this.recvSendCateId = recvSendCateId == null ? null : recvSendCateId.trim();
    }

    public String getRecvSendCateNm() {
        return recvSendCateNm;
    }

    public void setRecvSendCateNm(String recvSendCateNm) {
        this.recvSendCateNm = recvSendCateNm == null ? null : recvSendCateNm.trim();
    }

    public Integer getRecvSendInd() {
        return recvSendInd;
    }

    public void setRecvSendInd(Integer recvSendInd) {
        this.recvSendInd = recvSendInd;
    }

    public String getCntPtySubjEncd() {
        return cntPtySubjEncd;
    }

    public void setCntPtySubjEncd(String cntPtySubjEncd) {
        this.cntPtySubjEncd = cntPtySubjEncd == null ? null : cntPtySubjEncd.trim();
    }
}