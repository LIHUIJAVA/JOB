package com.px.mis.ec.util;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

// for JDK 1.7
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 * AES�ӽ��ܹ���
 * <p>
 * ��Կ(����)����ֻ��Ϊ16, 24, 32 λ����
 * <p>
 * ���ܹ���: ����-Base64���� ���ܹ���: Base64����-����
 */
public class AesUtils {

	// ����ģʽ
	private static final String TRANS_FORMATION = "AES/ECB/PKCS5Padding";
	// �����㷨
	private static final String ALGORITHM = "AES";
	// �ַ������
	private static final String CHARSET_NAME = "UTF-8";

	// for JDK 1.7
//    private static final BASE64Decoder decoder = new BASE64Decoder();
//    private static final BASE64Encoder encoder = new BASE64Encoder();

	private AesUtils() {
	}

	/**
	 * AES ����
	 *
	 * @param value ԭʼ�ַ���
	 * @return base64�����ļ����ַ���
	 * @throws Exception �����쳣
	 */
	public static String encrypt(String password, String value) throws Exception {
		SecretKeySpec aesKey = new SecretKeySpec(password.getBytes(Charset.forName(CHARSET_NAME)), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(value.getBytes(Charset.forName(CHARSET_NAME)));

		// for JDK 1.7
		// return encoder.encode(encrypted);

		return Base64.getEncoder().encodeToString(encrypted);

	}

	public static void main(String[] args) throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();

		map.put("key1", "value1");
		map.put("key2", "value");
		System.out.println(encrypt("50449ca64308d8f9fa29220ec319b7e7", "key1=value1&key2=value2"));
		String s = "DtYyMSGVkTruRnf8HrX07ea8SOxG5o/mZnMQar0miuU=";
		String ss = "DtYyMSGVkTruRnf8HrX07URD7pafWyvBoAdJg0slV9o=";
		System.out.println(decrypt("50449ca64308d8f9fa29220ec319b7e7", s));

		
	}

	/**
	 * AES ����
	 *
	 * @param value base64�����ļ����ַ���
	 * @return base64������ԭʼ�ַ���
	 * @throws Exception �����쳣
	 */
	public static String decrypt(String password, String value) throws Exception {
		byte[] decode = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));

		// for JDK 1.7
		// byte[] decode = decoder.decodeBuffer(value);

		SecretKeySpec aesKey = new SecretKeySpec(password.getBytes(Charset.forName(CHARSET_NAME)), ALGORITHM);
		Cipher cipher = Cipher.getInstance(TRANS_FORMATION);
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		return new String(cipher.doFinal(decode));
	}
}
