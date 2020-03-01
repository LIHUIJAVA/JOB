package com.px.mis.ec.util;

public class PartnerInfo {
    public String partnerId;
    public String name;
    public String key;
    public String aesKey;

    public PartnerInfo(String partnerId, String name, String key, String aesKey) {
        this.partnerId = partnerId;
        this.name = name;
        this.key = key;
        this.aesKey = aesKey;
    }
}