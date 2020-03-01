package com.px.mis.ec.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.utils.URIBuilder;

public class XHSSignUtil {

	public static String signTopRequest(Map<String, String> params, String secret, String signMethod, String method)
			throws IOException, NoSuchAlgorithmException, URISyntaxException {
		// ��һ�����������Ƿ��Ѿ�����
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		// �ڶ����������в������Ͳ���ֵ����һ��

		URIBuilder query = new URIBuilder(method);

		for (String key : keys) {
			String value = params.get(key);
			if (key != null && value != null && !"".equals(key) && !"".equals(value)) {
				query.setParameter(key, value);
			}
		}
		// ��������ʹ��MD5/HMAC����
		byte[] bytes;
		if ("hmac".equals(signMethod)) {
			bytes = encryptHMAC(query.toString(), secret);
		} else {

			bytes = encryptMD5(query.toString() + secret);
		}

		// ���Ĳ����Ѷ�����ת��Ϊ��д��ʮ������(��ȷǩ��Ӧ��Ϊ32��д�ַ������˷�����Ҫʱʹ��)
//		return byte2hex(bytes);
		return byte1hex(bytes);
	}

	public static byte[] encryptHMAC(String data, String secret) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacMD5");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data.getBytes("UTF-8"));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.toString());
		}
		return bytes;
	}

	public static byte[] encryptMD5(String data) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(data.getBytes("UTF-8"));
	}

	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	public static String byte1hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

}