package com.px.mis.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.system.dao.OrderNoDao;
import com.px.mis.system.entity.OrderNo;

@Component
public class GetOrderNo {
    private static final Logger logger = LoggerFactory.getLogger(GetOrderNo.class);

    @Autowired
    private OrderNoDao orderNoDao;
    @Autowired
    private GetOrderNoList getOrderNoList;

    //@Transactional
    public synchronized String getSeqNo(String transType, String userId, String... loginTime) {
        //改前获取单号
//		String threanName = Thread.currentThread().getName();
//		logger.info(threanName + " in");
//
//		String transDate = null;
//		if (loginTime == null) {
//			throw new RuntimeException("登录信息有误,请尝试重新登录");
//		} else if (loginTime.length == 0) {
//			transDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
//		} else if (loginTime.length > 0) {
//			String time = loginTime[0];
//			if (time == null || time.trim().length() == 0) {
//				throw new RuntimeException("登录信息有误,请尝试重新登录");
//			}
//			transDate = time.replace("-", "").substring(0, 8);// 20191225
//		}
//
//		OrderNo on = new OrderNo();
//		on.setTransDate(transDate);
//		on.setTransType(transType);
//		on.setCounter(0);
//		OrderNo orderNo = orderNoDao.select(on);
//		if (orderNo == null) {
//			orderNoDao.insert(on);
//			orderNo = on;
//		}
//		int counter = orderNo.getCounter();
//		orderNo.setCounter(counter + 1);
//		orderNoDao.update(orderNo);
//		//update a set c =c+1   where a=?
//
//		int length = 0;
//		if (transType == "ec") {
//			length = 7;
//		} else {
//			length = 4;
//		}
//
//		logger.info(String.valueOf(orderNo.getCounter()));
//		logger.info(threanName + " out");
//		return transType + userId + transDate + fixStr(orderNo.getCounter() + "", length);


        //改后获取单号
        String[] LG = null;
        if (loginTime != null) {
            LG = new String[loginTime.length];
            for (int i = 0; i < loginTime.length; i++) {
                LG[i] = loginTime[i];
            }
        }else {
            LG = new String[0];
        }
        return getOrderNoList.getSeqNoList(1, transType, userId, LG).get(0);
    }

//    private String fixStr(String src, int length) {
//        if (src == null)
//            src = "";
//        while (src.length() < length) {
//            src = "0" + src;
//        }
//        return src;
//    }

}
