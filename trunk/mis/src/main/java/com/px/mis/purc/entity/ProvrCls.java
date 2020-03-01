package com.px.mis.purc.entity;

import java.util.List;

//供应商分类
public class ProvrCls {
    private String provrClsId; //供应商分类编号

    private String provrClsNm; //供应商名称

    private String ico;    //ico图片
    
    private Integer level;  //级别
    
    private String memo;   //备注
    
    private String pid;    //对应父级id
    
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