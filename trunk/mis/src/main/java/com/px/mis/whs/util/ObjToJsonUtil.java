//package com.px.mis.whs.util;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Optional;
//
//import javax.servlet.http.HttpServletRequest;
//
//import com.alibaba.fastjson.JSON;
//import org.springframework.cglib.beans.BeanMap;
//import org.springframework.stereotype.Component;
//
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.serializer.ValueFilter;
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//
//@Component
//public class ObjToJsonUtil {
//	private static JSONObject map = new JSONObject();
//	private static ValueFilter filter = (obj, k, v) -> {
//		if (v == null) {
//			return "";
//		}
////		else if (v instanceof BigDecimal) {
////			BigDecimal bigDecimal = (BigDecimal) v;
////			return bigDecimal.toPlainString();
////		}
//		return v;
//
//	};
//	/**
//	 * 分页json
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
//		return JSON.toJSONString(map, filter, SerializerFeature.WriteMapNullValue,
//				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
//				SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.PrettyFormat);
//	}
//
//	/**
//	 * List转json
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
//
//		return JSON.toJSONString(map, filter, SerializerFeature.WriteMapNullValue,
//				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
//				SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * 详细信息 t list
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
//		return JSON.toJSONString(map, filter, SerializerFeature.WriteMapNullValue,
//				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
//				SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * bean转Map
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
//	 * bean转Map 添加 list
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
//	 * 对象输出json
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
//		return JSON.toJSONString(map, filter, SerializerFeature.WriteMapNullValue,
//				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
//				SerializerFeature.WriteNullStringAsEmpty);
//	}
//
//	/**
//	 * json转bean
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
//		Optional.ofNullable(jsonList).orElseThrow(() -> new RuntimeException("没有list"));
//		return jsonList;
//	}
//
//	/**
//	 * 分页map
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
//	 * 去除 '' null
//	 */
//	private static void removeNull(JSONObject jsonObject) {
//		Iterator<Entry<String, Object>> it = jsonObject.entrySet().iterator();
//		while (it.hasNext()) {
//			Entry<String, Object> entry = it.next();
//			System.out.println(entry);
//			Object value = entry.getValue();
//			if (value != null && value.toString().length() == 0) {
//				it.remove();
//			}
//		}
//	}
//
//	/**
//	 * 将map装换为javabean对象
//	 *
//	 * @param map
//	 * @param bean
//	 * @return
//	 *
//	 *         public static <T> T mapToBean(Map<String, Object> map, T bean) {
//	 *         BeanMap beanMap = BeanMap.create(bean); beanMap.putAll(map); return
//	 *         bean; }
//	 *
//	 *         /** 将对象装换为map
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
//	 *         /** 将List<T>转换为List<Map<String, Object>>
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
//	 *                                 /** 将List<Map<String,Object>>转换为List<T>
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
//	// // String转ObjectNode
////	public static ObjectNode getObjectNode(String jsonStr) throws IOException {
////		if (jsonStr == null || "".equals(jsonStr)) {
////			jsonStr = "{}";
////		}
////		return (ObjectNode) mapper.readTree(jsonStr);
////	}
////
////	// List转ObjectNode
////	public static ObjectNode getObjectNode(List list) throws IOException {
////		return (ObjectNode) mapper.readTree("{\"list\":" + mapper.writeValueAsString(list) + "}");
////	}
////
////	// Map转ObjectNode
////	public static ObjectNode getObjectNode(Map map) {
////		return null;
////	}
////
////	// ObjectNode转POJO
////	public static <T> T getPOJO(ObjectNode objectNode, Class<T> objClass) throws IOException {
////		return mapper.readValue(objectNode.toString(), objClass);
////	}
////
////	// ObjectNode转JsonString
////
////	// ObjectNode转List
////	public static List<Map> getList(String jsonStr) throws IOException {
////		return mapper.readValue(jsonStr, ArrayList.class);
////	}
////
////	// ObjectNode转Map
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
//}
