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
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		// 第二步：把所有参数名和参数值串在一起

		URIBuilder query = new URIBuilder(method);

		for (String key : keys) {
			String value = params.get(key);
			if (key != null && value != null && !"".equals(key) && !"".equals(value)) {
				query.setParameter(key, value);
			}
		}
		// 第三步：使用MD5/HMAC加密
		byte[] bytes;
		if ("hmac".equals(signMethod)) {
			bytes = encryptHMAC(query.toString(), secret);
		} else {

			bytes = encryptMD5(query.toString() + secret);
		}

		// 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
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