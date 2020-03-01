package com.px.mis.ec.service;

public interface OrderDownloadService {
    //判断店铺是否正在操作
    Boolean isOperation(String storeId);

    //添加当前店铺状态
    void addState(String storeId);

    //删除当前店铺状态
    void rmState(String storeId);
}
