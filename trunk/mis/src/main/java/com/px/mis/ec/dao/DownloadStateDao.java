package com.px.mis.ec.dao;

import com.px.mis.ec.entity.DownloadState;

public interface DownloadStateDao {
    //添加正在操作的店铺
    public void insert(String storeId);

//	public void update(DownloadState downloadState);

    public DownloadState selectById(String storeId);

    public void delete(String storeId);
}
