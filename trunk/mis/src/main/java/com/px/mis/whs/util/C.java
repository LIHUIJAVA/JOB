//package com.px.mis.whs.util;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.lang.management.ManagementFactory;
//import java.lang.management.RuntimeMXBean;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Optional;
//import java.util.Properties;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.alibaba.fastjson.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.cglib.beans.BeanMap;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//
//public class C {
////	public static Logger logger = LoggerFactory.getLogger("SYSTEM");
////
//////			Logger.getLogger("SYSTEM");
////	public static void info(String string) {
////		HttpServletRequest sra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////
////		MDC.put("username", sra.getServerPort() + "");
////		MDC.put("acc_num_ip", sra.getSession().getId());
////		logger.info(string);
////
////	}
//
//	/**
//	 * Listתjson
//	 */
//	public void geturl(String ssssss, int i) {
////		MDC.put("username", "12121");
////		logger.info("dad" + Thread.currentThread().getId());
//
////		HttpServletRequest request = sra.getRequest();
//		// ��ʼ����
//		String date = "2019-09-01";
////		��������
//		String date2 = "2019-9-30";
////		����ip
//		String ip = "localhost";
//
//		JSONObject jsons = new JSONObject();
//		JSONObject jsonObject = new JSONObject();
//		jsons.put("reqBody", jsonObject);
//		jsons.put("accNum", "sys");
//		jsons.put("userName", "root");
//		jsons.put("loginTime", LocalDateTime.now());
//		jsonObject.put("pursOrdrDt1", date);
//		jsonObject.put("pursOrdrDt2", date2);
//
//		jsonObject.put("toGdsSnglDt1", date);
//		jsonObject.put("toGdsSnglDt2", date2);
//
//		jsonObject.put("intoWhsDt1", date);
//		jsonObject.put("intoWhsDt2", date2);
//
//		jsonObject.put("sellSnglDt1", date);
//		jsonObject.put("sellSnglDt2", date2);
//
//		jsonObject.put("formDt1", date);
//		jsonObject.put("formDt2", date2);
//
//		jsonObject.put("outWhsDt1", date);
//		jsonObject.put("outWhsDt2", date2);
//
//		jsonObject.put("rtnGoodsDt1", date);
//		jsonObject.put("rtnGoodsDt2", date2);
//
//		jsonObject.put("delvSnglDt1", date);
//		jsonObject.put("delvSnglDt2", date2);
////		jsonObject.put("isNtRtnGood", 1);
//
//		jsonObject.put("checkDt1", date);
//		jsonObject.put("checkDt2", date2);
//
//		jsonObject.put("isNtChk", 0);
//
//		jsonObject.put("pageNo", i);
//		jsonObject.put("pageSize", 300);
//		String jsonData = sendPost("http://" + ip + ":8080/mis/" + ssssss, jsons.toString());
//
//		JSONArray jsonList = JSONObject.parseObject(jsonData).getJSONObject("respBody").getJSONArray("list");
//
//		List<Map> dfas = JSON.parseArray(jsonList.toString(), Map.class);
//		JSONObject dasd = new JSONObject();
//
//		JSONObject jsonss = new JSONObject();
//		JSONObject jsonObjects = new JSONObject();
//		jsonss.put("reqHead", dasd);
//
//		dasd.put("accNum", "sys");
//		dasd.put("userName", "�¾�");
//		dasd.put("loginTime", LocalDateTime.now().toString());
//
//		for (Map asdas : dfas) {
//			asdas.put("isNtChk", 1);
//		}
//		System.err.println(dfas);
//
//		jsonObjects.put("list", dfas);
//		jsonss.put("reqBody", jsonObjects);
//
//		sendPost("http://" + ip + ":8080/mis/" + "purc/SellOutWhs/updateSellOutWhsIsNtChk", jsonss.toString());
//		if (JSONObject.parseObject(jsonData).getJSONObject("respBody").getInteger("listNum") != 0) {
//			geturl(ssssss, i);
//		}
//	}
//
//	public static void main(String[] args) {
//
////		�������б�
//		String s = "/purc/ToGdsSngl/queryToGdsSnglList";
////		�ɹ������б�
//		String ss = "/purc/PursOrdr/queryPursOrdrList";
////		�ɹ���ⵥ�б�
//		String sss = "/purc/IntoWhs/queryIntoWhsList";
////		���۵��б�
//		String ssss = "/purc/SellSngl/querySellSnglList";
////		�˻����б�
//		String sssss = "/purc/RtnGoods/queryRtnGoodsList";
////		���۳��ⵥ�б�
//		String ssssss = "/purc/SellOutWhs/querySellOutWhsList";
////		ί�д����������б�
//		String sssssss = "/purc/EntrsAgnDelv/queryEntrsAgnDelvList";
////		ί�д������㵥�б�
//		String ssssssss = "/purc/EntrsAgnStl/queryEntrsAgnStlList";
////		�������б�
//		String sssssssss = "/whs/cannib_sngl/queryList";
//
////		�̵㵥�б�
//		String ssssssssss = "/whs/check_sngl/queryList";
//
////		�̵������б�
//		String sssssssssss = "/whs/check_sngl_loss/queryList";
////		��������ⵥ�б�
//		String ssssssssssss = "/whs/out_into_whs/queryList";
////		��װ��ж���б�
//		String sssssssssssss = "/whs/amb_disamb_sngl/queryList";
//		String sssssssssssssa = "/whs/amb_disamb_sngl/updateASnglChk";
////
//
////		geturl(ssssss, 1);
////		MDC.put("username", "12121");
////		name();
//		String str = "demo";
//		// base64 encoded ��strת����base64 bytes
//		String encoded = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
//
//		// base64 decoded
//		String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
//		System.out.println("decoded:" + encoded);
//		System.out.println("decoded:" + decoded);
//		// account/form/final/dealMonth
//
////		sendPost("http://localhost:8080/mis/account/form/final/backDealMonth", kk+i+kks);
//
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/outIngWaterList", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/InvtyStandList", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/selectInvty", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/queryInvtyTabList", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/selectExtantQtyList", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/invty_tab/selectTSummaryList", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/prod_stru/queryListPrint", jsons.toJSONString());
////        sendPost("http://192.168.1.118:8080/mis/whs/gds_bit/queryListDaYin", jsons.toJSONString());
//
//		/**
//		 * for (int i = 0; i < 1; i++) { new Thread(new Runnable() {
//		 *
//		 * @Override public void run() { // TODO Auto-generated method stub try {
//		 *           Thread.sleep(100); } catch (InterruptedException e) { // TODO
//		 *           Auto-generated catch block e.printStackTrace(); } } }).start(); }
//		 */
//
//	}
//
//	/**
//	 * ��ָ�� URL ����POST����������
//	 *
//	 * @param url   ��������� URL
//	 * @param param ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
//	 * @return ������Զ����Դ����Ӧ���
//	 */
//	public static String sendPost(String url, String param) {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		StringBuffer buffer = new StringBuffer();
//		try {
//			URL realUrl = new URL(url);
//			// �򿪺�URL֮�������
//			URLConnection conn = realUrl.openConnection();
//			// ����ͨ�õ���������
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("Content-Type", "application/json"); // ���÷������ݵĸ�ʽ
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// ����POST�������������������
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// ��ȡURLConnection�����Ӧ�������
//			out = new PrintWriter(conn.getOutputStream());
//			// �����������
//			out.print(param);
//			// flush������Ļ���
//			out.flush();
//			// ����BufferedReader����������ȡURL����Ӧ
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				buffer.append(line);
//			}
//		} catch (Exception e) {
//			System.out.println("���� POST ��������쳣��" + e);
//			e.printStackTrace();
//		}
//		// ʹ��finally�����ر��������������
//		finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		System.err.println(buffer);
//		return buffer.toString();
//	}
//
//	/**
//	 * ��ȡ���е�����
//	 */
//
//	public static void name() {
//
//		Properties properties = System.getProperties();
//		// �������е�����
//		for (String key : properties.stringPropertyNames()) {
//			// �����Ӧ�ļ���ֵ
//			System.out.println(key + "=" + properties.getProperty(key));
//		}
//		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
//		String name = runtime.getName();
//		System.out.println("��ǰ���̵ı�ʶΪ��" + name);
//		int index = name.indexOf("@");
//		if (index != -1) {
//			int pid = Integer.parseInt(name.substring(0, index));
//			System.out.println("��ǰ���̵�PIDΪ��" + pid);
//		}
//		String hj = "cmd /c jps -lvm";
//		String hj1 = "cmd /c CHCP 65001";
//
//		try {
//			Process process = Runtime.getRuntime().exec(hj1 + "&&" + hj);
//			final InputStream is1 = process.getInputStream();
//			// ��ȡ���ǵĴ�����
//			final InputStream is2 = process.getErrorStream();
//			// ���������̣߳�һ���̸߳������׼���������һ���������׼������
//			// ���������̣߳�һ���̸߳������׼���������һ���������׼������
//			new Thread() {
//				public void run() {
////                    InputStreamReader isr = new InputStreamReader(is1,"GB2312");//����utf-8
//					BufferedReader br1 = null;
//					try {
//						InputStreamReader inputStreamReader = new InputStreamReader(is1);
//						System.err.println(inputStreamReader.getEncoding());
//						br1 = new BufferedReader(inputStreamReader);
//					} catch (Exception e1) {
//
//						e1.printStackTrace();
//					}
//					try {
//						String line1 = null;
//						while ((line1 = br1.readLine()) != null) {
//							if (line1 != null) {
//								System.out.println(line1);
//
//							}
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					} finally {
//						try {
//							is1.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}.start();
//
//			new Thread() {
//				public void run() {
//					BufferedReader br2 = null;
//					try {
//
//						InputStreamReader inputStreamReader = new InputStreamReader(is2);
//						System.err.println(inputStreamReader.getEncoding());
//
//						br2 = new BufferedReader(inputStreamReader);
//					} catch (Exception e1) {
//
//						e1.printStackTrace();
//					}
//					try {
//						String line2 = null;
//						while ((line2 = br2.readLine()) != null) {
//							if (line2 != null) {
//								System.out.println(line2);
//
//							}
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					} finally {
//						try {
//							is2.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}.start();
//
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
//
//		Thread thread = new Thread(new Runnable() {
//			int i = 0;
//
//			@Override
//			public void run() {
//				try {
//					for (; i < 10;) {
//						Thread.sleep(1000);
//
//						System.out.println(++i);
//					}
//
//				} catch (InterruptedException e) {
//
//					e.printStackTrace();
//				}
//
//			}
//
//		}, "l�ؼ���");
//		thread.start();
//		System.err.println("\t" + thread.getName());
//		System.err.println("\t" + thread.getId());
//
//	}
//
//	/**
//	 * ����get����
//	 *
//	 * @param requestUrl       ����url
//	 * @param requestHeader    ����ͷ
//	 * @param responseEncoding ��Ӧ����
//	 * @return ҳ����Ӧhtml
//	 */
//	public static String sendGet(String requestUrl, Map<String, String> requestHeader, String responseEncoding) {
//		String result = "";
//		BufferedReader reader = null;
//		try {
//			if (requestUrl == null || requestUrl.isEmpty()) {
//				return result;
//			}
//			URL realUrl = new URL(requestUrl);
//			URLConnection connection = realUrl.openConnection();
//			connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
//			connection.setRequestProperty("user-agent",
//					"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
//			if (requestHeader != null && requestHeader.size() > 0) {
//				for (Entry<String, String> entry : requestHeader.entrySet()) {
//					connection.setRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
//			connection.connect();
//			if (responseEncoding == null || responseEncoding.isEmpty()) {
//				responseEncoding = "UTF-8";
//			}
//			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
//			String line;
//			while ((line = reader.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("����GET��������쳣��");
//			e.printStackTrace();
//		} finally {
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * ����post����
//	 *
//	 * @param requestUrl       ����url
//	 * @param requestHeader    ����ͷ
//	 * @param formTexts        ������
//	 * @param files            �ϴ��ļ�
//	 * @param requestEncoding  �������
//	 * @param responseEncoding ��Ӧ����
//	 * @return ҳ����Ӧhtml
//	 */
//	public static String sendPost(String requestUrl, Map<String, String> requestHeader, Map<String, String> formTexts,
//			Map<String, String> files, String requestEncoding, String responseEncoding) {
//		OutputStream out = null;
//		BufferedReader reader = null;
//		String result = "";
//		try {
//			if (requestUrl == null || requestUrl.isEmpty()) {
//				return result;
//			}
//			URL realUrl = new URL(requestUrl);
//			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//			connection.setRequestProperty("accept", "text/html, application/xhtml+xml, image/jxr, */*");
//			connection.setRequestProperty("user-agent",
//					"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
//			if (requestHeader != null && requestHeader.size() > 0) {
//				for (Entry<String, String> entry : requestHeader.entrySet()) {
//					connection.setRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
//			connection.setDoOutput(true);
//			connection.setDoInput(true);
//			connection.setRequestMethod("POST");
//			if (requestEncoding == null || requestEncoding.isEmpty()) {
//				requestEncoding = "UTF-8";
//			}
//			if (responseEncoding == null || responseEncoding.isEmpty()) {
//				responseEncoding = "UTF-8";
//			}
//			if (requestHeader != null && requestHeader.size() > 0) {
//				for (Entry<String, String> entry : requestHeader.entrySet()) {
//					connection.setRequestProperty(entry.getKey(), entry.getValue());
//				}
//			}
//			if (files == null || files.size() == 0) {
//				connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//				out = new DataOutputStream(connection.getOutputStream());
//				if (formTexts != null && formTexts.size() > 0) {
//					String formData = "";
//					for (Entry<String, String> entry : formTexts.entrySet()) {
//						formData += entry.getKey() + "=" + entry.getValue() + "&";
//					}
//					formData = formData.substring(0, formData.length() - 1);
//					out.write(formData.toString().getBytes(requestEncoding));
//				}
//			} else {
//				String boundary = "-----------------------------" + String.valueOf(new Date().getTime());
//				connection.setRequestProperty("content-type", "multipart/form-data; boundary=" + boundary);
//				out = new DataOutputStream(connection.getOutputStream());
//				if (formTexts != null && formTexts.size() > 0) {
//					StringBuilder sbFormData = new StringBuilder();
//					for (Entry<String, String> entry : formTexts.entrySet()) {
//						sbFormData.append("--" + boundary + "\r\n");
//						sbFormData.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
//						sbFormData.append(entry.getValue() + "\r\n");
//					}
//					out.write(sbFormData.toString().getBytes(requestEncoding));
//				}
//				for (Entry<String, String> entry : files.entrySet()) {
//					String fileName = entry.getKey();
//					String filePath = entry.getValue();
//					if (fileName == null || fileName.isEmpty() || filePath == null || filePath.isEmpty()) {
//						continue;
//					}
//					File file = new File(filePath);
//					if (!file.exists()) {
//						continue;
//					}
//					out.write(("--" + boundary + "\r\n").getBytes(requestEncoding));
//					out.write(("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + file.getName()
//							+ "\"\r\n").getBytes(requestEncoding));
//					out.write(("Content-Type: application/x-msdownload\r\n\r\n").getBytes(requestEncoding));
//					DataInputStream in = new DataInputStream(new FileInputStream(file));
//					int bytes = 0;
//					byte[] bufferOut = new byte[1024];
//					while ((bytes = in.read(bufferOut)) != -1) {
//						out.write(bufferOut, 0, bytes);
//					}
//					in.close();
//					out.write(("\r\n").getBytes(requestEncoding));
//				}
//				out.write(("--" + boundary + "--").getBytes(requestEncoding));
//			}
//			out.flush();
//			out.close();
//			out = null;
//			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), responseEncoding));
//			String line;
//			while ((line = reader.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("����POST��������쳣��");
//			e.printStackTrace();
//		} finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	private static JSONObject map = new JSONObject();
//
//	/**
//	 * ��ҳjson
//	 */
//	public static <T> String getLimit(String url, boolean isSuccess, String message, int count, int pageNo,
//			int pageSize, int listNum, int pages, List<T> list) {
//		JSONObject respHead = new JSONObject();
//		JSONObject respBody = new JSONObject();
//		respHead.put("url", url);
//		respHead.put("isSuccess", isSuccess);
//		respHead.put("message", message);
//
//		respBody.put("count", count);
//		respBody.put("pageNo", pageNo);
//		respBody.put("pageSize", pageSize);
//		respBody.put("listNum", listNum);
//		respBody.put("pages", pages);
//		respBody.put("list", list);
//
//		map.put("respHead", respHead);
//		map.put("respBody", respBody);
//
//		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
//				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
//				SerializerFeature.PrettyFormat);
//	}
//
//	/**
//	 * Listתjson
//	 */
//	public static <T> String getRespList(String url, boolean isSuccess, String message, List<T> list) {
//
//		JSONObject mapf = new JSONObject();
//		JSONObject mapff = new JSONObject();
//
//		mapf.put("url", url);
//		mapf.put("isSuccess", isSuccess);
//		mapf.put("message", message);
//		mapff.put("list", list);
//
//		map.put("respHead", mapf);
//		map.put("respBody", mapff);
//
//		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
//				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * ��ϸ��Ϣ t list
//	 */
//	public static <T, L> String getRespObjList(String url, Boolean isSuccess, String message, T t, List<L> list) {
//		JSONObject mapf = new JSONObject();
//
//		mapf.put("url", url);
//		mapf.put("isSuccess", isSuccess);
//		mapf.put("message", message);
//
//		map.put("respHead", mapf);
//		map.put("respBody", beanToMap(t, list));
//
//		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
//				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * beanתMap
//	 */
//	public static <T> Map<String, Object> beanToMap(T bean) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (bean != null) {
//			BeanMap beanMap = BeanMap.create(bean);
//			for (Object key : beanMap.keySet()) {
//				map.put(key + "", beanMap.get(key));
//			}
//		}
//		return map;
//	}
//
//	/**
//	 * beanתMap ��� list
//	 */
//	private static <T, L> Map<String, Object> beanToMap(T bean, L list) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (bean != null) {
//			BeanMap beanMap = BeanMap.create(bean);
//			for (Object key : beanMap.keySet()) {
//				map.put(key + "", beanMap.get(key));
//			}
//			map.put("list", list);
//		}
//		return map;
//	}
//
//	/**
//	 * �������json
//	 */
//	public static <T> String getRespObj(String url, Boolean isSuccess, String message, T t) {
//		JSONObject mapf = new JSONObject();
//
//		mapf.put("url", url);
//		mapf.put("isSuccess", isSuccess);
//		mapf.put("message", message);
//
//		map.put("respHead", mapf);
//		map.put("respBody", t);
//
//		return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
//				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * jsonתbean
//	 */
//	public static <T> T getPOJO(String jsonData, Class<T> clazz) {
//		JSONObject reqBody = getReqBody(jsonData);
//		removeNull(reqBody);
//		T t = JSONObject.parseObject(reqBody.toJSONString(), clazz);
//		return t;
//	}
//
//	public static <T> List<T> getListPOJO(String jsonData, Class<T> clazz) {
//		JSONArray jsonList = getReqBodyList(jsonData);
//
//		for (int i = 0; i < jsonList.size(); i++) {
//			JSONObject json = jsonList.getJSONObject(i);
//			removeNull(json);
//		}
//
//		List<T> list = JSON.parseArray(jsonList.toString(), clazz);
//		return list;
//	}
//
//	public static JSONObject getReqBody(String jsonData) {
//		return JSONObject.parseObject(jsonData).getJSONObject("reqBody");
//	}
//
//	public static JSONArray getReqBodyList(String jsonData) {
//		JSONArray jsonList = getReqBody(jsonData).getJSONArray("list");
//
////		Object object = JSON.parse(jsonList.toString());
////		if (object instanceof JSONObject) {
////			JSONObject jsonObject = (JSONObject) object;
////		} else if (object instanceof JSONArray) {
////			JSONArray jsonArray = (JSONArray) object;
////		} else {
////			System.out.println("Neither jsonobject nor jsonarray is jsonStr");
////		}
//
//		Optional.ofNullable(jsonList).orElseThrow(() -> new RuntimeException("û��list"));
//		return jsonList;
//	}
//
//	/**
//	 * ��ҳmap
//	 */
//	public static Map<String, Object> getReqBodyMap(String jsonData) {
//		JSONObject jsonObject = getReqBody(jsonData);
//		removeNull(jsonObject);
//		if (jsonObject.containsKey("pageNo") && jsonObject.containsKey("pageSize")) {
//			Integer pageNo = jsonObject.getInteger("pageNo");
//			Integer pageSize = jsonObject.getInteger("pageSize");
//			jsonObject.put("index", (pageNo - 1) * pageSize);
//			jsonObject.put("num", pageSize);
//		}
//		return jsonObject.getInnerMap();
//	}
//
//	/**
//	 * ȥ�� '' null
//	 */
//	private static void removeNull(JSONObject jsonObject) {
////		Iterator<Entry<String, Object>> it = jsonObject.entrySet().iterator();
////		while (it.hasNext()) {
////			Entry<String, Object> entry = it.next();
////			System.out.println(entry);
////			Object value = entry.getValue();
////			if (value != null && value.toString().length() == 0) {
////				it.remove();
////			}
////		}
//	}
//
//	/**
//	 * ��mapװ��Ϊjavabean����
//	 *
//	 * @param map
//	 * @param bean
//	 * @return
//	 *
//	 *         public static <T> T mapToBean(Map<String, Object> map, T bean) {
//	 *         BeanMap beanMap = BeanMap.create(bean); beanMap.putAll(map); return
//	 *         bean; }
//	 *
//	 *         /** ������װ��Ϊmap
//	 *
//	 * @param bean
//	 * @return
//	 *
//	 *         public static <T> Map<String, Object> beanToMap(T bean) { Map<String,
//	 *         Object> map = new HashMap<String, Object>(); if (bean != null) {
//	 *         BeanMap beanMap = BeanMap.create(bean); for (Object key :
//	 *         beanMap.keySet()) { map.put(key + "", beanMap.get(key)); } } return
//	 *         map; }
//	 *
//	 *         /** ��List<T>ת��ΪList<Map<String, Object>>
//	 *
//	 * @param objList
//	 * @return
//	 * @throws JsonGenerationException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 *
//	 *                                 public static <T> List<Map<String, Object>>
//	 *                                 objectsToMaps(List<T> objList) {
//	 *                                 List<Map<String, Object>> list = new
//	 *                                 ArrayList<Map<String, Object>>(); if (objList
//	 *                                 != null && objList.size() > 0) { Map<String,
//	 *                                 Object> map = null; T bean = null; for (int i
//	 *                                 = 0, size = objList.size(); i < size; i++) {
//	 *                                 bean = objList.get(i); map = beanToMap(bean);
//	 *                                 list.add(map); } } return list; }
//	 *
//	 *                                 /** ��List<Map<String,Object>>ת��ΪList<T>
//	 *
//	 * @param maps
//	 * @param clazz
//	 * @return
//	 * @throws InstantiationException
//	 * @throws IllegalAccessException
//	 *
//	 *                                public static <T> List<T>
//	 *                                mapsToObjects(List<Map<String, Object>> maps,
//	 *                                Class<T> clazz) { List<T> list = new
//	 *                                ArrayList<T>(); if (maps != null &&
//	 *                                maps.size() > 0) { Map<String, Object> map =
//	 *                                null; T bean = null; for (int i = 0, size =
//	 *                                maps.size(); i < size; i++) { map =
//	 *                                maps.get(i); try { bean = clazz.newInstance();
//	 *                                } catch (InstantiationException |
//	 *                                IllegalAccessException e) { Auto-generated
//	 *                                catch block e.printStackTrace(); }
//	 *                                mapToBean(map, bean); list.add(bean); } }
//	 *                                return list; }
//	 */
//	// // StringתObjectNode
////	public static ObjectNode getObjectNode(String jsonStr) throws IOException {
////		if (jsonStr == null || "".equals(jsonStr)) {
////			jsonStr = "{}";
////		}
////		return (ObjectNode) mapper.readTree(jsonStr);
////	}
////
////	// ListתObjectNode
////	public static ObjectNode getObjectNode(List list) throws IOException {
////		return (ObjectNode) mapper.readTree("{\"list\":" + mapper.writeValueAsString(list) + "}");
////	}
////
////	// MapתObjectNode
////	public static ObjectNode getObjectNode(Map map) {
////		return null;
////	}
////
////	// ObjectNodeתPOJO
////	public static <T> T getPOJO(ObjectNode objectNode, Class<T> objClass) throws IOException {
////		return mapper.readValue(objectNode.toString(), objClass);
////	}
////
////	// ObjectNodeתJsonString
////
////	// ObjectNodeתList
////	public static List<Map> getList(String jsonStr) throws IOException {
////		return mapper.readValue(jsonStr, ArrayList.class);
////	}
////
////	// ObjectNodeתMap
////	public static Map getMap(String jsonStr) throws IOException {
////		return mapper.readValue(jsonStr, HashMap.class);
////	}
//	public static String getIpAddress(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("HTTP_CLIENT_IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}
//
//}
