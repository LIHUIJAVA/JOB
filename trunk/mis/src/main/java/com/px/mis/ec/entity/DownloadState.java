package com.px.mis.ec.entity;

public class DownloadState {
    private Integer id;
    private String storeId;
    private String stateTime;

    public DownloadState() {
    }

    public DownloadState(String storeId) {
        this.storeId = storeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStateTime() {
        return stateTime;
    }

    public void setStateTime(String stateTime) {
        this.stateTime = stateTime;
    }

    @Override
    public String toString() {
        return "DownloadState{" +
                "id=" + id +
                ", storeId='" + storeId + '\'' +
                ", stateTime='" + stateTime + '\'' +
                '}';
    }
}
