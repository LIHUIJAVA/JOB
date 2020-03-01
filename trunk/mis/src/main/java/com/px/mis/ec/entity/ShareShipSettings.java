package com.px.mis.ec.entity;

public class ShareShipSettings {
	
    private Integer settingId;//合并发货策略设置编号
    private Integer ec;//电商平台
    private Integer store;//店铺
    private Integer sellerNick;//买家昵称
    private Integer refPhone;//收货人手机号
    private Integer ref;//收货人
    private Integer conAddress;//收货地址
    private Integer whs;//仓库
    private Integer hasMergeOrder;//含未勾选科合并订单
    private Integer isSellerNote;//卖家备注强制不合并
    private String sellerNote;//卖家备注强制不合并字段

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public Integer getEc() {
        return ec;
    }

    public void setEc(Integer ec) {
        this.ec = ec;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public Integer getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(Integer sellerNick) {
        this.sellerNick = sellerNick;
    }

    public Integer getRefPhone() {
        return refPhone;
    }

    public void setRefPhone(Integer refPhone) {
        this.refPhone = refPhone;
    }

    public Integer getRef() {
        return ref;
    }

    public void setRef(Integer ref) {
        this.ref = ref;
    }

    public Integer getConAddress() {
        return conAddress;
    }

    public void setConAddress(Integer conAddress) {
        this.conAddress = conAddress;
    }

    public Integer getWhs() {
        return whs;
    }

    public void setWhs(Integer whs) {
        this.whs = whs;
    }

    public Integer getHasMergeOrder() {
        return hasMergeOrder;
    }

    public void setHasMergeOrder(Integer hasMergeOrder) {
        this.hasMergeOrder = hasMergeOrder;
    }

    public Integer getIsSellerNote() {
        return isSellerNote;
    }

    public void setIsSellerNote(Integer isSellerNote) {
        this.isSellerNote = isSellerNote;
    }

    public String getSellerNote() {
        return sellerNote;
    }

    public void setSellerNote(String sellerNote) {
        this.sellerNote = sellerNote == null ? null : sellerNote.trim();
    }
}