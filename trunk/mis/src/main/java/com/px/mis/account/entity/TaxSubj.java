package com.px.mis.account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//˰���Ŀ��ʵ��
public class TaxSubj {
	//@JsonIgnore
	@JsonProperty("���")
	private Integer autoId;//�Զ�id
	@JsonProperty("��Ӧ�̷������")
	private String provrClsEncd;//��Ӧ�̷�����룻
	@JsonProperty("��Ӧ�̷�������")
	private String provrClsNm;//��Ӧ�̷�������
	@JsonProperty("�ɹ���Ŀ����")
	private String pursSubjEncd;//�ɹ���Ŀ���룻
	@JsonProperty("�ɹ���Ŀ����")
	private String pursSubjNm;//�ɹ���Ŀ����
	@JsonProperty("�ɹ�˰�����")
	private String pursTaxEncd;//�ɹ�˰����룻
	@JsonProperty("�ɹ�˰���Ŀ")
	private String pursTaxNm;//�ɹ�˰������
	
	//

	
	

	public TaxSubj() {
	}
	public Integer getAutoId() {
		return autoId;
	}
	public void setAutoId(Integer autoId) {
		this.autoId = autoId;
	}
	public String getProvrClsEncd() {
		return provrClsEncd;
	}
	public void setProvrClsEncd(String provrClsEncd) {
		this.provrClsEncd = provrClsEncd;
	}
	public String getPursSubjEncd() {
		return pursSubjEncd;
	}
	public void setPursSubjEncd(String pursSubjEncd) {
		this.pursSubjEncd = pursSubjEncd;
	}
	public String getPursTaxEncd() {
		return pursTaxEncd;
	}
	public void setPursTaxEncd(String pursTaxEncd) {
		this.pursTaxEncd = pursTaxEncd;
	}
	public String getProvrClsNm() {
		return provrClsNm;
	}
	public void setProvrClsNm(String provrClsNm) {
		this.provrClsNm = provrClsNm;
	}
	public String getPursSubjNm() {
		return pursSubjNm;
	}
	public void setPursSubjNm(String pursSubjNm) {
		this.pursSubjNm = pursSubjNm;
	}
	public String getPursTaxNm() {
		return pursTaxNm;
	}
	public void setPursTaxNm(String pursTaxNm) {
		this.pursTaxNm = pursTaxNm;
	}
	
}
