package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.service.CouponDetailService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class CouponDetailServiceImpl implements CouponDetailService {

	private Logger logger = LoggerFactory.getLogger(AuditStrategyServiceImpl.class);
	
	@Autowired
	private CouponDetailDao couponDetailDao;
	@Override
	public String couponDetailList(String ecOrderId) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<CouponDetail> couponDetails = couponDetailDao.couponDetailList(ecOrderId);
		try {
			resp = BaseJson.returnRespList("ec/couponDetail/couponDetailList", isSuccess, message, 1, 1, 1,
					1, 1, couponDetails);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

}
