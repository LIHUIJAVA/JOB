package com.px.mis.sell.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.sell.dao.PrintersDao;
import com.px.mis.sell.entity.Printers;
import com.px.mis.sell.service.PrintersService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Transactional
@Service
public class PrintersServiceImpl implements PrintersService{
	private Logger logger = LoggerFactory.getLogger(PrintersServiceImpl.class);

	@Autowired
	private PrintersDao printersDao;

	@Override
	public String selectListPrinters(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<Printers> printerslist;
		int count;
		int pageNo;
		int pageSize;
		int listNum;
		int pages;
		try {
			count = printersDao.printersByidx(map);
			printerslist = printersDao.selectListPrinters(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum = printerslist.size();
			pages = count / pageSize + 1;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			isSuccess=false;
			message="��ѯʧ�ܣ������ԡ�";
			count=0;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum=0;
			pages=0;
			printerslist=null;
		}
		try {
			resp = BaseJson.returnRespList("sell/Management/selectList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, printerslist);
		} catch (IOException e) {
			logger.error("URL:sell/Management/selectList �쳣˵����ƴ��resp�쳣", e);
		}
		return resp;
	}

	
	@Override
	public Printers selectByIdx(int idx) {
		// TODO Auto-generated method stub
		return printersDao.selectByIdx(idx);
	}


	

	@Override
	public ObjectNode deletePrinters(Integer idx) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (printersDao.selectByIdx(idx)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+idx+"�����ڣ�");
		}else {
			int deletePrinters = printersDao.deletePrinters(idx);
			if(deletePrinters==1) {
				on.put("isSuccess", true);
				on.put("message", "ɾ���ɹ�");
			}else if(deletePrinters==0){
				on.put("isSuccess", true);
				on.put("message", "û��Ҫɾ��������");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "ɾ��ʧ��");
			}
		}
		return on;
	}


	@Override
	public ObjectNode updatePrinters(Printers printers) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer idx = printers.getIdx();
		if(idx==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ���������Ϊnull������");
		}else if (printersDao.selectByIdx(idx)==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+idx+"�����ڣ�");
		}else {
			int updatePrinters = printersDao.updatePrinters(printers);
			if(updatePrinters==1) {
				on.put("isSuccess", true);
				on.put("message", "���³ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
			}
		}
		return on;
	}


	@Override
	public ObjectNode insertPrinters(Printers printers) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			int insertResult = printersDao.insertPrinters(printers);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
			}
		
		return on;
	}
	

}
