package com.px.mis.util;

import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.system.dao.OrderNoDao;
import com.px.mis.system.entity.OrderNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GetOrderNoList {

    private static final Logger logger = LoggerFactory.getLogger(GetOrderNo.class);

    @Autowired
    private OrderNoDao orderNoDao;

    /**
     *
     * @param count 单号数量
     * @param transType 操作模块
     * @param userId 用户id
     * @param loginTime 登陆时间
     * @return 单号list
     */
    @Transactional
    public synchronized List<String> getSeqNoList(int count, String transType, String userId, String... loginTime) {
        List<String> seqNoList = new ArrayList<>();
        String threanName = Thread.currentThread().getName();
        logger.info(threanName + " in");
        String transDate = null;

        if (loginTime == null) {
            throw new RuntimeException("登录信息有误,请尝试重新登录");
        } else if (loginTime.length == 0) {
            transDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        } else if (loginTime.length > 0) {
            String time = loginTime[0];
            if (time == null || time.trim().length() == 0) {
                throw new RuntimeException("登录信息有误,请尝试重新登录");
            }
            transDate = time.replace("-", "").substring(0, 8);// 20191225
        }

        OrderNo on = new OrderNo();
        on.setTransDate(transDate);
        on.setTransType(transType);
        on.setCounter(0);
        OrderNo orderNo = orderNoDao.select(on);
        if (orderNo == null) {
            orderNoDao.insert(on);
            orderNo = on;
        }

        int length = 0;
        if (transType == "ec") {
            length = 7;
        } else {
            length = 4;
        }

        int counterDB = orderNo.getCounter();
        for (int i = counterDB; i < counterDB + count; i++) {
            seqNoList.add(transType + userId + transDate + fixStr(i + "", length));
        }
        orderNo.setCounter(counterDB + count);
        orderNoDao.update(orderNo);

        return seqNoList;
    }

    private String fixStr(String src, int length) {
        if (src == null)
            src = "";
        while (src.length() < length) {
            src = "0" + src;
        }
        return src;
    }

}
