package com.px.mis.ec.thread;

import com.px.mis.ec.service.OrderDownloadService;
import com.px.mis.ec.service.PlatOrderService;

/**
 *  定义任务类
 *  2019-12-27
 */
public class Task {

    private String userId;
    private String startDate;
    private String endDate;
    private int pageNo;
    private int pageSize;
    private String storeId;
    private PlatOrderService platOrderService;
    private OrderDownloadService orderDownloadService;


    // 任务的初始化方法
    public Task(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,
                PlatOrderService platOrderService, OrderDownloadService orderDownloadService) {
        this.userId = userId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.storeId = storeId;
        this.platOrderService = platOrderService;
        this.orderDownloadService = orderDownloadService;
    }

    /**
     * 执行任务
     */  
    public void execute() {
        try {
            System.out.println("--->" + Thread.currentThread().getName());
            platOrderService.download(userId, startDate, endDate, pageNo, pageSize, storeId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除正在操作店铺
        orderDownloadService.rmState(storeId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public PlatOrderService getPlatOrderService() {
        return platOrderService;
    }

    public void setPlatOrderService(PlatOrderService platOrderService) {
        this.platOrderService = platOrderService;
    }

    public OrderDownloadService getOrderDownloadService() {
        return orderDownloadService;
    }

    public void setOrderDownloadService(OrderDownloadService orderDownloadService) {
        this.orderDownloadService = orderDownloadService;
    }
}
