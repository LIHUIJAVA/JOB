package com.px.mis.ec.entity;

//ÔùÆ··¶Î§ÁĞ±í
public class PresentRangeList {
	private Long id;// ĞòºÅ
	private String presentRangeEncd;// ÔùÆ··¶Î§±àÂë
	private String presentEncd;// ÔùÆ·±àÂë
	// ¹ØÁª×Ö¶Î
	private String presentNm;// ÔùÆ·Ãû³Æ

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
