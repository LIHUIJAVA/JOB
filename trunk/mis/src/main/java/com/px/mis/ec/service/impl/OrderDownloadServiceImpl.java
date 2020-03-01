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
            //正在操作 给提示
            return false;
        } else {
            // 无操作 走线程
            return true;
        }
    }

    @Override
    public void addState(String storeId) {
        //添加正在操作的店铺
        downloadStateDao.insert(storeId);
    }

    @Override
    public void rmState(String storeId) {
        //删除操作完成的的店铺
        downloadStateDao.delete(storeId);
    }
}
