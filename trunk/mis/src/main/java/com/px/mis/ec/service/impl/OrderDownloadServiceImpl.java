package com.px.mis.ec.service.impl;

import com.px.mis.ec.dao.DownloadStateDao;
import com.px.mis.ec.entity.DownloadState;
import com.px.mis.ec.service.OrderDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderDownloadServiceImpl implements OrderDownloadService {

    @Autowired
    private DownloadStateDao downloadStateDao;

    @Override
    public Boolean isOperation(String storeId) {

        DownloadState DLS = downloadStateDao.selectById(storeId);
        if (DLS != null) {
            //���ڲ��� ����ʾ
            return false;
        } else {
            // �޲��� ���߳�
            return true;
        }
    }

    @Override
    public void addState(String storeId) {
        //������ڲ����ĵ���
        downloadStateDao.insert(storeId);
    }

    @Override
    public void rmState(String storeId) {
        //ɾ��������ɵĵĵ���
        downloadStateDao.delete(storeId);
    }
}
