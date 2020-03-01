package com.px.mis.ec.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.MemberRecord;
import com.px.mis.ec.service.MemberRecordService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//会员档案
@RequestMapping(value = "ec/memberRecord", method = RequestMethod.POST)
@Controller
public class MemberRecordController {

	private Logger logger = LoggerFactory.getLogger(MemberRecordController.class);

	@Autowired
	private MemberRecordService memberRecordService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/memberRecord/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			MemberRecord memberRecord = BaseJson.getPOJO(jsonBody, MemberRecord.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 1.设置日历类
			Calendar calendar = Calendar.getInstance();
			// 2.设置时间
			Date date = new Date();
			memberRecord.setMemRegDate(sdf.format(date));
			calendar.setTime(date);
			// 3.增减时间
			calendar.add(Calendar.YEAR, +1);// 减1年
			// 4.获取操作后的日期
			date = calendar.getTime();
			String dateStr = sdf.format(date);
			memberRecord.setStopDate(dateStr);
			resp = memberRecordService.add(memberRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/memberRecord/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			MemberRecord memberRecord = BaseJson.getPOJO(jsonBody, MemberRecord.class);
			resp = memberRecordService.edit(memberRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/memberRecord/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = memberRecordService.delete(BaseJson.getReqBody(jsonBody).get("memId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/memberRecord/query");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = memberRecordService.query(BaseJson.getReqBody(jsonBody).get("memId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/memberRecord/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = memberRecordService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
