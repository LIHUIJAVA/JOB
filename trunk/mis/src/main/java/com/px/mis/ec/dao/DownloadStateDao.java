package com.px.mis.ec.dao;

import com.px.mis.ec.entity.DownloadState;

public interface DownloadStateDao {
    //������ڲ����ĵ���
    public void insert(String storeId);

//	public void update(DownloadState downloadState);

    public DownloadState selectById(String storeId);

    public void delete(String storeId);
}
