package com.px.mis.ec.entity;

//��Ʒ��Χ�б�
public class PresentRangeList {
	private Long id;// ���
	private String presentRangeEncd;// ��Ʒ��Χ����
	private String presentEncd;// ��Ʒ����
	// �����ֶ�
	private String presentNm;// ��Ʒ����

	public final Long getId() {
		return id;
	}

	public final void setId(Long id) {
		this.id = id;
	}

	public final String getPresentRangeEncd() {
		return presentRangeEncd;
	}

	public final void setPresentRangeEncd(String presentRangeEncd) {
		this.presentRangeEncd = presentRangeEncd;
	}

	public final String getPresentEncd() {
		return presentEncd;
	}

	public final void setPresentEncd(String presentEncd) {
		this.presentEncd = presentEncd;
	}

	public final String getPresentNm() {
		return presentNm;
	}

	public final void setPresentNm(String presentNm) {
		this.presentNm = presentNm;
	}

	@Override
	public String toString() {
		return String.format("PresentRangeList [id=%s, presentRangeEncd=%s, presentEncd=%s, presentNm=%s]", id,
				presentRangeEncd, presentEncd, presentNm);
	}

}
