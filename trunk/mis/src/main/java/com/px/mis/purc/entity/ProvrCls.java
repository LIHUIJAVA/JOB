package com.px.mis.purc.entity;

import java.util.List;

//��Ӧ�̷���
public class ProvrCls {
    private String provrClsId; //��Ӧ�̷�����

    private String provrClsNm; //��Ӧ������

    private String ico;    //icoͼƬ
    
    private Integer level;  //����
    
    private String memo;   //��ע
    
    private String pid;    //��Ӧ����id
    
    private List<ProvrDoc> provrDoc;

	public List<ProvrDoc> getProvrDoc() {
		return provrDoc;
	}

	public void setProvrDoc(List<ProvrDoc> provrDoc) {
		this.provrDoc = provrDoc;
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

	public String getProvrClsId() {
        return provrClsId;
    }

    public void setProvrClsId(String provrClsId) {
        this.provrClsId = provrClsId == null ? null : provrClsId.trim();
    }



    public String getProvrClsNm() {
		return provrClsNm;
	}

	public void setProvrClsNm(String provrClsNm) {
		this.provrClsNm = provrClsNm;
	}

	
}