package com.px.mis.purc.entity;

public class RecvSendCate {
    private String recvSendCateId; //�շ������

    private String recvSendCateNm; //�շ��������

    private Integer recvSendInd;   //�շ���ʶ �� �ջ�(1�� 0��)

    private String ico;    //icoͼƬ 
    
    private Integer level;  //����
    
    private String pid;    //��Ӧ����id
    
    private String cntPtySubjEncd; //�Է���Ŀ���
    
    private String memo;   //��ע
    
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