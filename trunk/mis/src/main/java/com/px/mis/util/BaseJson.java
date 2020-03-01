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
		System.out.println("��ʼ");
		System.out.println(onReqBody.get("list"));

		if (onReqBody.has("list")) {
			return JacksonUtil.getPOJOList(onReqBody.get("list").toString(), objClass);

		} else {
			throw new RuntimeException("������û�м���");
		}

	}

	/********************* �°汾 �����л�ʵ�� ���� ******************************/

	public static <T> T getPOJO(String jsonBody, Class<T> objClass) throws IOException {
		ObjectNode node = JacksonUtil.getObjectNode(jsonBody);
		if (node.get("reqBody") != null) {
			node = (ObjectNode) node.get("reqBody");
		}
		return JacksonUtil.getPOJO(node, objClass);
	}

	// ָ��RequestBody�м��ϵļ� ->key
	public static <T> List<T> getPOJOList(String jsonBody, String key, Class<T> objClass) throws IOException {
		JsonNode node = JacksonUtil.getObjectNode(jsonBody);
		if (node.get("reqBody") != null) {
			node = node.get("reqBody");
		}
		node = node.get(key);
		if (node != null) {
			return JacksonUtil.getPOJOList(node.toString(), objClass);
		} else {
			throw new RuntimeException("������û�м���");
		}
	}

	/*************** ����Ϊ���л����� **************/

	public static List<Map> getList(String request) throws IOException {
		return JacksonUtil.getList(getReqBody(request).get("list").toString());
	}

	public static ObjectNode getReqHead(String request) throws IOException {
		return (ObjectNode) JacksonUtil.getObjectNode(request).get("reqHead");
	}

	public static ObjectNode getReqBody(String request) throws IOException {
		return (ObjectNode) JacksonUtil.getObjectNode(request).get("reqBody");
	}

	// ���ص�������
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

	// ���ص�������,����ע��
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
			JacksonUtil.turnOffAnno();// �ر�ע��
		}

	}

	// ���ض���+����,����ҳ
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

	// �����ķ���,����JsonPropertiesע��
	public static synchronized <T> String returnRespObjListAnno(String url, Boolean isSuccess, String message,
			Object object, List<T> objList) throws IOException {
		try {
			JacksonUtil.turnOnAnno();// ����ע��
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
			return (JacksonUtil.getObjectNode("").put("message", "���ݵ�������!")).toString();
		} finally {
			JacksonUtil.turnOffAnno();// �ر�ע��
		}

	}

	// ����ҳ,���ؼ���
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

	// ��ҳ,���ؼ���
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
		respBody.put("pageNo", pageNo);// ��ǰҳ��
		respBody.put("pageSize", pageSize);// ��ǰҳ��ʾ����
		respBody.put("listNum", objList.size());// ��������
		respBody.put("pages", (count + pageSize - 1) / pageSize);// ��ҳ��
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// ��ҳ,���ؼ���+�ܼ�
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
		respBody.put("pageNo", pageNo);// ��ǰҳ��
		respBody.put("pageSize", pageSize);// ��ǰҳ��ʾ����
		respBody.put("listNum", objList.size());// ��������
		respBody.put("pages", (count + pageSize - 1) / pageSize);// ��ҳ��
		respBody.put("tableSums", tableSums);
		respon.put("respBody", respBody);
		return respon.toString();
	}

	// ����ҳ,���ؼ���+�ܼ�
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

	// ����ҳ,���ؼ���,����ע��
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

	// ��ҳ,���ؼ���,����ע��
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
			respBody.put("pageNo", pageNo);// ��ǰҳ��
			respBody.put("pageSize", pageSize);// ��ǰҳ��ʾ����
			respBody.put("listNum", objList.size());// ��������
			respBody.put("pages", (count + pageSize - 1) / pageSize);// ��ҳ��
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
