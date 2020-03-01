package com.px.mis.ec.entity;

public class MemberRecord {
	
	private String memId;//会员编号
    private String ecId;//电商平台编号
    private String nick;//昵称
    private String name;//姓名
    private String memRegDate;//会员注册日期
    private String memLevName;//会员等级名称
    private String mobile;//联系手机
    private String qq;//QQ
    private String email;//邮箱地址
    private String wechat;//微信
    private String province;//省
    private String city;//市
    private String county;//县（区）
    private String detAddress;//详细地址
    private String validDocType;//有效证件类型
    private String validDocNo;//有效证件编号
    private String stopDate;//停用日期
    private String alipayNo;//支付宝账号
    private Long memPoints;//会员积分
    private Long memTimes;//会员购物次数
    private String memo;//备注

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId == null ? null : ecId.trim();
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId == null ? null : memId.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMemRegDate() {
        return memRegDate;
    }

    public void setMemRegDate(String memRegDate) {
        this.memRegDate = memRegDate == null ? null : memRegDate.trim();
    }

    public String getMemLevName() {
        return memLevName;
    }

    public void setMemLevName(String memLevName) {
        this.memLevName = memLevName == null ? null : memLevName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getDetAddress() {
        return detAddress;
    }

    public void setDetAddress(String detAddress) {
        this.detAddress = detAddress == null ? null : detAddress.trim();
    }

    public String getValidDocType() {
        return validDocType;
    }

    public void setValidDocType(String validDocType) {
        this.validDocType = validDocType == null ? null : validDocType.trim();
    }

    public String getValidDocNo() {
        return validDocNo;
    }

    public void setValidDocNo(String validDocNo) {
        this.validDocNo = validDocNo == null ? null : validDocNo.trim();
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate == null ? null : stopDate.trim();
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo == null ? null : alipayNo.trim();
    }

    public Long getMemPoints() {
        return memPoints;
    }

    public void setMemPoints(Long memPoints) {
        this.memPoints = memPoints;
    }

    public Long getMemTimes() {
        return memTimes;
    }

    public void setMemTimes(Long memTimes) {
        this.memTimes = memTimes;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}