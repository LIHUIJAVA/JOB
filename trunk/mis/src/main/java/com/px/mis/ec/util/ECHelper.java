package com.px.mis.ec.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.util.HttpHelper;

public class ECHelper {

	private static Logger logger = LoggerFactory.getLogger(ECHelper.class);
	// private static String url = "http://127.0.0.1:8080/HttpTrans/JDTrans";
	private static String url = "http://116.196.118.150:8080/HttpTrans/JDTrans";

	public static String getJD(String method, StoreSettings storeSettings, String json)
			throws IOException, NoSuchAlgorithmException {
//		String url = "https://api.jd.com/routerjson";
		// String url = "http://116.196.118.150:8080/HttpTrans/JDTrans";
		// String url = "http://127.0.0.1:8080/HttpTrans/JDTrans";
		/*
		 * String appKey = "827ADD63009AFF59FF31ED1F73D5459B"; String appSecret =
		 * "9b322be686914c3cb4cf10cee27d3c57";
		 */
		// 美素
		// String appKey = "E919A528050AF559167A498E87B832D8";
		// String appSecret = "5785f78fc26d44ad8db4696cf21ec266";
		String v = "2.0";

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> params = new HashMap();
		params.put("v", v);
		params.put("method", method);
		params.put("app_key", storeSettings.getAppKey());
		params.put("access_token", storeSettings.getAccessToken());
		params.put("timestamp", time);

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair("v", v));
		pairList.add(new BasicNameValuePair("method", method));
		pairList.add(new BasicNameValuePair("app_key", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("access_token", storeSettings.getAccessToken()));
		pairList.add(new BasicNameValuePair("360buy_param_json", json));
		pairList.add(new BasicNameValuePair("timestamp", time));
		pairList.add(new BasicNameValuePair("sign", Sign.signTopRequest(params, storeSettings.getAppSecret(), "md5")));
		String jdRequest = HttpHelper.doPost(url, pairList);
		logger.info("jd Request:" + jdRequest);
		return jdRequest;
	}

	// 获取京东电子面单
	public static String getJDExpress(String method, StoreSettings storeSettings, String json)
			throws IOException, NoSuchAlgorithmException {
//		String url = "https://api.jd.com/routerjson";
		// String url = "http://116.196.118.150:8080/HttpTrans/JDTrans";
		// String url = "http://127.0.0.1:8080/HttpTrans/JDTrans";
		/*
		 * String appKey = "827ADD63009AFF59FF31ED1F73D5459B"; String appSecret =
		 * "9b322be686914c3cb4cf10cee27d3c57";
		 */
		// 美素
		// String appKey = "E919A528050AF559167A498E87B832D8";
		// String appSecret = "5785f78fc26d44ad8db4696cf21ec266";
		String v = "2.0";

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> params = new HashMap();
		params.put("v", v);
		params.put("method", method);
		params.put("app_key", storeSettings.getAppKey());
		params.put("access_token", storeSettings.getAccessToken());
		params.put("timestamp", time);

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair("v", v));
		pairList.add(new BasicNameValuePair("method", method));
		pairList.add(new BasicNameValuePair("app_key", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("access_token", storeSettings.getAccessToken()));
		pairList.add(new BasicNameValuePair("content", json));
		pairList.add(new BasicNameValuePair("timestamp", time));
		pairList.add(new BasicNameValuePair("sign", Sign.signTopRequest(params, storeSettings.getAppSecret(), "md5")));
		String jdRequest = HttpHelper.doPost(url, pairList);
		logger.info("jd Request:" + jdRequest);
		return jdRequest;
	}

	// 获取京东大头笔信息
	public static String getJDBigShot(String method, StoreSettings storeSettings, String json)
			throws IOException, NoSuchAlgorithmException {
//			String url = "https://api.jd.com/routerjson";
		// String url = "http://116.196.118.150:8080/HttpTrans/JDTrans";
		// String url = "http://127.0.0.1:8080/HttpTrans/JDTrans";
		/*
		 * String appKey = "827ADD63009AFF59FF31ED1F73D5459B"; String appSecret =
		 * "9b322be686914c3cb4cf10cee27d3c57";
		 */
		// 美素
		// String appKey = "E919A528050AF559167A498E87B832D8";
		// String appSecret = "5785f78fc26d44ad8db4696cf21ec266";
		String v = "2.0";

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> params = new HashMap();
		params.put("v", v);
		params.put("method", method);
		params.put("app_key", storeSettings.getAppKey());
		params.put("access_token", storeSettings.getAccessToken());
		params.put("timestamp", time);

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair("v", v));
		pairList.add(new BasicNameValuePair("method", method));
		pairList.add(new BasicNameValuePair("app_key", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("access_token", storeSettings.getAccessToken()));
		pairList.add(new BasicNameValuePair("format", json));
		pairList.add(new BasicNameValuePair("timestamp", time));
		pairList.add(new BasicNameValuePair("sign", Sign.signTopRequest(params, storeSettings.getAppSecret(), "md5")));
		String jdRequest = HttpHelper.doPost(url, pairList);
		logger.info("jd Request:" + jdRequest);
		return jdRequest;
	}

	public static String getTB(String method, StoreSettings storeSettings, Map<String, String> map)
			throws IOException, NoSuchAlgorithmException {
		String url = "http://39.98.141.250/HttpTrans/TBTrans";
//		String url = "http://192.168.1.118:8080/HttpTrans/TBTrans";
//		String url = "http://116.196.118.150:8080/HttpTrans/TMTrans";
////		String appKey = "25598839";
////		String appSecret = "75964573abfc8e910bbe5cc498632346";
//		String v = "2.0";
//		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("app_key", storeSettings.getAppKey());
//		params.put("format", "json");
//		params.put("method", method);
//		params.put("session", storeSettings.getAccessToken());
//		params.put("sign_method", "md5");
//		params.put("timestamp", time);
//		params.put("v", v);
//		params.put("simplify", "false");
//
//		for (Entry<String, String> entry : map.entrySet()) {
//			params.put(entry.getKey(), entry.getValue());
//		}
//
//		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
//
//		pairList.add(new BasicNameValuePair("method", method));
//		pairList.add(new BasicNameValuePair("app_key", storeSettings.getAppKey()));
//		pairList.add(new BasicNameValuePair("sign_method", "md5"));
//		pairList.add(new BasicNameValuePair("sign", Sign.signTopRequest(params, storeSettings.getAppSecret(), "md5")));
//		pairList.add(new BasicNameValuePair("session", storeSettings.getAccessToken()));
//		pairList.add(new BasicNameValuePair("timestamp", time));
//		pairList.add(new BasicNameValuePair("format", "json"));
//		pairList.add(new BasicNameValuePair("v", v));
//		// pairList.add(new BasicNameValuePair("partner_id", ""));
//		pairList.add(new BasicNameValuePair("simplify", "false"));
//
//		for (Entry<String, String> entry : map.entrySet()) {
//			pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//		}
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair("AppKey", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("AccessToken", storeSettings.getAccessToken()));
		pairList.add(new BasicNameValuePair("AppSecret", storeSettings.getAppSecret()));
		for (Entry<String, String> entry : map.entrySet()) {
			pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		String tmRequest = HttpHelper.doPostTM(url, pairList);
		logger.info("tm Request:" + tmRequest);
		return tmRequest;
	}

	public static String getPDD(String method, StoreSettings storeSettings, Map<String, String> map)
			throws IOException, NoSuchAlgorithmException {
		String url = "http://gw-api.pinduoduo.com/api/router";
//		String url = "http://116.196.118.150:8080/HttpTrans/TMTrans";
//		String appKey = "25598839";
//		String appSecret = "75964573abfc8e910bbe5cc498632346";
		String v = "V1";

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> params = new HashMap<String, String>();

		params.put("type", method);// API接口名称
		params.put("client_id", storeSettings.getAppKey());// POP分配给应用的client_id
		params.put("data_type", "JSON");// 响应格式，即返回数据的格式，JSON或者XML（二选一），默认JSON，注意是大写
		params.put("timestamp", time);// UNIX时间戳，单位秒，需要与拼多多服务器时间差值在10分钟内
		params.put("version", v);// API协议版本号，默认为V1，可不填
		params.put("access_token", storeSettings.getAccessToken());// 通过code获取的access_token(无需授权的接口，该字段不参与sign签名运算)

		for (Entry<String, String> entry : map.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
		}

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();

		pairList.add(new BasicNameValuePair("type", method));
		pairList.add(new BasicNameValuePair("client_id", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("data_type", "JSON"));
		pairList.add(new BasicNameValuePair("sign", Sign.signTopRequest(params, storeSettings.getAppSecret(), "md5")));
		pairList.add(new BasicNameValuePair("access_token", storeSettings.getAccessToken()));
		pairList.add(new BasicNameValuePair("timestamp", time));
		pairList.add(new BasicNameValuePair("version", v));

		for (Entry<String, String> entry : map.entrySet()) {
			pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		String pddRequest = HttpHelper.doPost(url, pairList);
		logger.info("pdd Request:" + pddRequest);
		System.err.println("pdd Request:" + pddRequest);
		return pddRequest;
	}

	private static String format = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取固定间隔时刻集合
	 * 
	 * @param start    开始时间
	 * @param end      结束时间
	 * @param interval 时间间隔(单位：分钟)
	 * @return
	 */
	public static List<String> getIntervalTimeList(String start, String end, int interval) {
		Date startDate = convertString2Date(format, start);
		Date endDate = convertString2Date(format, end);
		List<String> list = new ArrayList<>();
		while (startDate.getTime() <= endDate.getTime()) {
			list.add(convertDate2String(format, startDate));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.MINUTE, interval);
			if (calendar.getTime().getTime() > endDate.getTime()) {
				if (!startDate.equals(endDate)) {
					list.add(convertDate2String(format, endDate));
				}
				startDate = calendar.getTime();
			} else {
				startDate = calendar.getTime();
			}

		}
		return list;
	}

	public static Date convertString2Date(String format, String dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		try {
			Date date = simpleDateFormat.parse(dateStr);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String convertDate2String(String format, Date dateStr) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(dateStr);
	}

	public static String getKaola(String method, StoreSettings storeSettings, String json)
			throws IOException, NoSuchAlgorithmException {
		String url = "http://openapi.kaola.com/router";
		// String v = "1.0";

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Map<String, String> params = new HashMap();
		// params.put("v", v);
		params.put("method", method);
		params.put("app_key", storeSettings.getAppKey());
		// System.out.println("APPKEY:"+storeSettings.getAppKey());
		params.put("access_token", storeSettings.getAccessToken());
		params.put("timestamp", time);
		/*
		 * params.put("page_no", "1"); params.put("page_size", "20");
		 * params.put("item_edit_status", "5");
		 */

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		if (json.length() > 0) {
			Map<String, String> maps = (Map) JSON.parse(json);
			for (Entry<String, String> entry : maps.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
				pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// pairList.add(new BasicNameValuePair("v", v));
		pairList.add(new BasicNameValuePair("method", method));
		pairList.add(new BasicNameValuePair("app_key", storeSettings.getAppKey()));
		pairList.add(new BasicNameValuePair("access_token", storeSettings.getAccessToken()));
		// pairList.add(new BasicNameValuePair("", json));
		pairList.add(new BasicNameValuePair("timestamp", time));
		pairList.add(
				new BasicNameValuePair("sign", Sign.signTopRequestKaoLa(params, storeSettings.getAppSecret(), "md5")));
		String kaolaRequest = HttpHelper.doPost(url, pairList);
		logger.info("kaola Request:" + kaolaRequest);
		return kaolaRequest;
	}

	public static JSONObject getXHS(String method, StoreSettings storeSettings, Map<String, String> map)
			throws IOException, NoSuchAlgorithmException, URISyntaxException {
		String url = "https://ark.xiaohongshu.com/";
//		https://ark.xiaohongshu.com/
//		String url = "http://flssandbox.xiaohongshu.com";
//		app-key 7957bf2231
//		app-secret 9bd4b0eaf047fb288e3fc6511ed78354
		URIBuilder uri = new URIBuilder(url + method);

		Long time = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("app-key", storeSettings.getAppKey());
		params.put("timestamp", time.toString());
		for (Entry<String, String> entry : map.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
			uri.setParameter(entry.getKey(), entry.getValue());
		}

		HttpGet httpGet = new HttpGet();
		httpGet.setHeader("app-key", storeSettings.getAppKey());
		httpGet.setHeader("timestamp", time.toString());
		httpGet.setHeader("sign", XHSSignUtil.signTopRequest(params, storeSettings.getAppSecret(), "md5", method));
		httpGet.setURI(uri.build());
		logger.info("httpGet:" + httpGet);

		String XHSRString = HttpHelper.doPostXHS(httpGet);
		JSONObject XHSRequest = JSON.parseObject(XHSRString);
		logger.info("XHS:" + XHSRequest);
		return XHSRequest;
	}

	public static JSONObject getXHSPUT(String method, StoreSettings storeSettings, Map<String, String> map)
			throws IOException, NoSuchAlgorithmException, URISyntaxException {
		String url = "https://ark.xiaohongshu.com/";
//		https://ark.xiaohongshu.com/
//		String url = "http://flssandbox.xiaohongshu.com";
//		app-key 7957bf2231
//		app-secret 9bd4b0eaf047fb288e3fc6511ed78354
		URIBuilder uri = new URIBuilder(url + method);

		Long time = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("app-key", storeSettings.getAppKey());
		params.put("timestamp", time.toString());
		for (Entry<String, String> entry : map.entrySet()) {
			params.put(entry.getKey(), entry.getValue());
			uri.setParameter(entry.getKey(), entry.getValue());
		}

		HttpPut httpPut = new HttpPut();
		httpPut.setHeader("app-key", storeSettings.getAppKey());
		httpPut.setHeader("timestamp", time.toString());
		httpPut.setHeader("sign", XHSSignUtil.signTopRequest(params, storeSettings.getAppSecret(), "md5", method));
		httpPut.setURI(uri.build());

		String content = JSON.toJSONString(map);
		// 创建带字符创参数和字符编码的
		StringEntity entity = new StringEntity(content, "UTF-8");
		httpPut.setEntity(entity);

		logger.info("httpPut:" + httpPut);

		String XHSRString = HttpHelper.doPostXHS(httpPut);
		JSONObject XHSRequest = JSON.parseObject(XHSRString);
		logger.info("XHS:" + XHSRequest);
		return XHSRequest;
	}
}
