package com.px.mis.purc.entity;

import java.util.List;

/*�ͻ�����*/
public class CustCls {
    private String clsId;  //�ͻ�����id

    private String clsNm;  //�ͻ���������
    
    private String ico;    //icoͼƬ
    
    private Integer level;  //����
    
    private String pid;    //��Ӧ����id
    
    private String memo;   //��ע
    
    private List<CustDoc> custDoc; //�ͻ���������
    
    

	public List<CustDoc> getCustDoc() {
		return custDoc;
	}

	public void setCustDoc(List<CustDoc> custDoc) {
		this.custDoc = custDoc;
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


    public String getClsId() {
        return clsId;
    }

    public void setClsId(String clsId) {
        this.clsId = clsId == null ? null : clsId.trim();
    }

    public String getClsNm() {
        return clsNm;
    }

    public void setClsNm(String clsNm) {
        this.clsNm = clsNm == null ? null : clsNm.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}