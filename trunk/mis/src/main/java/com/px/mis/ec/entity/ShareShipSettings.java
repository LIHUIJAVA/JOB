package com.px.mis.ec.entity;

public class ShareShipSettings {
	
    private Integer settingId;//�ϲ������������ñ��
    private Integer ec;//����ƽ̨
    private Integer store;//����
    private Integer sellerNick;//����ǳ�
    private Integer refPhone;//�ջ����ֻ���
    private Integer ref;//�ջ���
    private Integer conAddress;//�ջ���ַ
    private Integer whs;//�ֿ�
    private Integer hasMergeOrder;//��δ��ѡ�ƺϲ�����
    private Integer isSellerNote;//���ұ�עǿ�Ʋ��ϲ�
    private String sellerNote;//���ұ�עǿ�Ʋ��ϲ��ֶ�

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