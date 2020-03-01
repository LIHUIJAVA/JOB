package com.px.mis.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BaseJson {

	public static <T> List<T> getPOJOList(String request, Class<T> objClass) throws IOException {
		ObjectNode onReqBody = getReqBody(request);
		System.out.println("开始");
		System.out.println(onReqBody.get("list"));

		if (onReqBody.has("list")) {
			return JacksonUtil.getPOJOList(onReqBody.get("list").toString(), objClass);

		} else {
			throw new RuntimeException("请求中没有集合");
		}

	}

	/********************* 新版本 反序列化实体 方法 ******************************/

	public static <T> T getPOJO(String jsonBody, Class<T> objClass) throws IOException {
		ObjectNode node = JacksonUtil.getObjectNode(jsonBody);
		if (node.get("reqBody") != null) {
			node = (ObjectNode) node.get("reqBody");
		}
		return JacksonUtil.getPOJO(node, objClass);
	}

	// 指定RequestBody中集合的键 ->key
	public static <T> List<T> getPOJOList(String jsonBody, String key, Class<T> objClass) throws IOException {
		JsonNode node = JacksonUtil.getObjectNode(jsonBody);
		if (node.get("reqBody") != null) {
			node = node.get("reqBody");
		}
		node = node.get(key);
		if (node != null) {
			return JacksonUtil.getPOJOList(node.toString(), objClass);
		} else {
			throw new RuntimeException("请求中没有集合");
		}
	}

	/*************** 以下为序列化方法 **************/

	public static List<Map> getList(String request) throws IOException {
		return JacksonUtil.getList(getReqBody(request).get("list").toString());
	}

	public static ObjectNode getReqHead(String request) throws IOException {
		return (ObjectNode) JacksonUtil.getObjectNode(request).get("reqHead");
	}

	public static ObjectNode getReqBody(String request) throws IOException {
		return (ObjectNode) JacksonUtil.getObjectNode(request).get("reqBody");
	}

	// 返回单个对象
	public static String returnRespObj(String url, Boolean isSuccess, String message, Object object)
			throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		if (object == null) {
			respon.put("respBody", JacksonUtil.getObjectNode(""));
		} else {
			respon.put("respBody", JacksonUtil.getObjectNode(object));
		}
		return respon.toString();
	}

	// 返回单个对象,启用注解
	@Transactional
	public static String returnRespObjAnno(String url, Boolean isSuccess, String message, Object object)
			throws IOException {
		try {
			JacksonUtil.turnOnAnno();
			ObjectNode respon = JacksonUtil.getObjectNode("");
			ObjectNode respHead = JacksonUtil.getObjectNode("");
			respHead.put("url", url);
			respHead.put("isSuccess", isSuccess);
			respHead.put("message", message);
			respon.put("respHead", respHead);
			if (object == null) {
				respon.put("respBody", JacksonUtil.getObjectNode(""));
			} else {
				respon.put("respBody", JacksonUtil.getObjectNode(object));
			}
			return respon.toString();
		} finally {
			JacksonUtil.turnOffAnno();// 关闭注解
		}

	}

	// 返回对象+集合,不分页
	public static <T> String returnRespObjList(String url, Boolean isSuccess, String message, Object object,
			List<T> objList) throws IOException {

		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		ObjectNode respBody = null;
		if (object == null) {
			respBody = JacksonUtil.getObjectNode("");
		} else {
			respBody = JacksonUtil.getObjectNode(object);
		}
		respBody.put("list", JacksonUtil.getArrayNode(objList));
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// 导出的方法,启用JsonProperties注解
	public static synchronized <T> String returnRespObjListAnno(String url, Boolean isSuccess, String message,
			Object object, List<T> objList) throws IOException {
		try {
			JacksonUtil.turnOnAnno();// 启用注解
			ObjectNode respon = JacksonUtil.getObjectNode("");
			ObjectNode respHead = JacksonUtil.getObjectNode("");
			respHead.put("url", url);
			respHead.put("isSuccess", isSuccess);
			respHead.put("message", message);
			respon.put("respHead", respHead);
			ObjectNode respBody = null;
			if (object == null) {
				respBody = JacksonUtil.getObjectNode("");
			} else {
				respBody = JacksonUtil.getObjectNode(object);
			}
			respBody.put("list", JacksonUtil.getArrayNode(objList));
			respon.put("respBody", respBody);
			return respon.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return (JacksonUtil.getObjectNode("").put("message", "数据导出错误!")).toString();
		} finally {
			JacksonUtil.turnOffAnno();// 关闭注解
		}

	}

	// 不分页,返回集合
	public static <T> String returnRespList(String url, Boolean isSuccess, String message, List<T> objList)
			throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		ObjectNode respBody = JacksonUtil.getObjectNode("");
		respBody.set("list", JacksonUtil.getArrayNode(objList));
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// 分页,返回集合
	public static <T> String returnRespList(String url, Boolean isSuccess, String message, int count, int pageNo,
			int pageSize, int listNum, int pages, List<T> objList) throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		ObjectNode respBody = JacksonUtil.getObjectNode("");
		respBody.put("list", JacksonUtil.getArrayNode(objList));
		respBody.put("count", count);
		respBody.put("pageNo", pageNo);// 当前页数
		respBody.put("pageSize", pageSize);// 当前页显示条数
		respBody.put("listNum", objList.size());// 返回条数
		respBody.put("pages", (count + pageSize - 1) / pageSize);// 总页数
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// 分页,返回集合+总计
	public static <T> String returnRespList(String url, Boolean isSuccess, String message, int count, int pageNo,
			int pageSize, int listNum, int pages, List<T> objList, Map map) throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		ObjectNode respBody = JacksonUtil.getObjectNode("");
		ObjectNode tableSums = JacksonUtil.getObjectNode(map);
		respBody.put("list", JacksonUtil.getArrayNode(objList));
		respBody.put("count", count);
		respBody.put("pageNo", pageNo);// 当前页数
		respBody.put("pageSize", pageSize);// 当前页显示条数
		respBody.put("listNum", objList.size());// 返回条数
		respBody.put("pages", (count + pageSize - 1) / pageSize);// 总页数
		respBody.put("tableSums", tableSums);
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// 不分页,返回集合+总计
	public static <T> String returnRespList(String url, Boolean isSuccess, String message, List<T> objList, Map map)
			throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		ObjectNode respBody = JacksonUtil.getObjectNode("");
		ObjectNode tableSums = JacksonUtil.getObjectNode(map);
		respBody.put("list", JacksonUtil.getArrayNode(objList));
		respBody.put("tableSums", tableSums);
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// 不分页,返回集合,启用注解
	public static synchronized <T> String returnRespListAnno(String url, Boolean isSuccess, String message,
			List<T> objList) throws IOException {
		try {
			JacksonUtil.turnOnAnno();
			ObjectNode respon = JacksonUtil.getObjectNode("");
			ObjectNode respHead = JacksonUtil.getObjectNode("");
			respHead.put("url", url);
			respHead.put("isSuccess", isSuccess);
			respHead.put("message", message);
			respon.put("respHead", respHead);
			ObjectNode respBody = JacksonUtil.getObjectNode("");
			respBody.put("list", JacksonUtil.getArrayNode(objList));
			respon.put("respBody", respBody);
			String s = respon.toString();
			return s;
		} finally {
			JacksonUtil.turnOffAnno();
		}

	}

	// 分页,返回集合,启用注解
	public static synchronized <T> String returnRespListAnno(String url, Boolean isSuccess, String message, int count,
			int pageNo, int pageSize, int listNum, int pages, List<T> objList, Map map) throws IOException {
		try {
			JacksonUtil.turnOnAnno();
			ObjectNode respon = JacksonUtil.getObjectNode("");
			ObjectNode respHead = JacksonUtil.getObjectNode("");
			respHead.put("url", url);
			respHead.put("isSuccess", isSuccess);
			respHead.put("message", message);
			respon.put("respHead", respHead);
			ObjectNode respBody = JacksonUtil.getObjectNode("");
			ObjectNode tableSums = JacksonUtil.getObjectNode(map);
			respBody.put("list", JacksonUtil.getArrayNode(objList));
			respBody.put("count", count);
			respBody.put("pageNo", pageNo);// 当前页数
			respBody.put("pageSize", pageSize);// 当前页显示条数
			respBody.put("listNum", objList.size());// 返回条数
			respBody.put("pages", (count + pageSize - 1) / pageSize);// 总页数
			respBody.put("tableSums", tableSums);
			respon.put("respBody", respBody);
			String s = respon.toString();

			return s;
		} finally {
			JacksonUtil.turnOffAnno();
		}
	}

	public static String returnResp(String url, Boolean isSuccess, String message, ObjectNode respBody)
			throws IOException {
		ObjectNode respon = JacksonUtil.getObjectNode("");
		ObjectNode respHead = JacksonUtil.getObjectNode("");
		respHead.put("url", url);
		respHead.put("isSuccess", isSuccess);
		respHead.put("message", message);
		respon.put("respHead", respHead);
		if (respBody == null) {
			respBody = JacksonUtil.getObjectNode("");
			respon.put("respBody", respBody);
		} else {
			respon.put("respBody", respBody);
		}
		return respon.toString();
	}

	public static Map getMap(ObjectNode node) throws IOException {
		return JacksonUtil.getMap(node.toString());

	}

}
