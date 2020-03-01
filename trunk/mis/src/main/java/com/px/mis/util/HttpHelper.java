package com.px.mis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	// 超时时间
	private static final int MAX_TIMEOUT = 10000;
	// 最大连接数
	private static final int MAX_TOTAL = 50;
	// 默认路由连接数
	private static final int DEFAULT_MAX_PER_ROUTE = 30;

	private static RequestConfig requestConfig;

	// 默认HttpClient
	private static CloseableHttpClient dfltHttpClient;

	static {
		requestConfig = RequestConfig.custom().setConnectTimeout(MAX_TIMEOUT) // 设置连接超时
				.setSocketTimeout(MAX_TIMEOUT) // 设置读取超时
				.setConnectionRequestTimeout(MAX_TIMEOUT) // 设置从连接池获取连接实例的超时
				.build();
	}

	/***
	 * 初始化默认HttpClient
	 */
	private static void initDefaultHttpClient() {
		PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
		connMgr.setMaxTotal(MAX_TOTAL);
		connMgr.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
		connMgr.setValidateAfterInactivity(1000);

		dfltHttpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(connMgr)
				.build();
	}

	public static String doPost(String url, String param) throws IOException {
		if (dfltHttpClient == null)
			initDefaultHttpClient();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		httpPost.setEntity(new StringEntity(param, Charset.forName("UTF-8")));
		return doRequest(url, dfltHttpClient, httpPost);
	}

	public static String doPost(String url, Iterable<? extends NameValuePair> parameters) throws IOException {
		if (dfltHttpClient == null)
			initDefaultHttpClient();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		httpPost.setEntity(new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));
		return doRequest(url, dfltHttpClient, httpPost);
	}

//TM转发
	public static String doPostTM(String url, Iterable<? extends NameValuePair> parameters) throws IOException {
		if (dfltHttpClient == null)
			initDefaultHttpClient();

		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
		httpPost.setEntity(new UrlEncodedFormEntity(parameters, Charset.forName("UTF-8")));

		CloseableHttpResponse response = dfltHttpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();

		String result = EntityUtils.toString(entity, "UTF-8");
		EntityUtils.consume(response.getEntity());
		return result;
	}

	public static String doPost(String url) throws IOException {
		int questionMarkIndex = url.indexOf("?");
		String path = url.substring(0, questionMarkIndex);
		String paramStr = url.substring(questionMarkIndex + 1, url.length());
		String[] params = paramStr.split("&");

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		for (String param : params) {
			String[] tmpKV = param.split("=");
			NameValuePair pair = new BasicNameValuePair(tmpKV[0], tmpKV[1]);
			pairList.add(pair);
		}

		return doPost(path, pairList);
	}

	private static String doRequest(String url, CloseableHttpClient httpClient, HttpUriRequest request)
			throws IOException {
		CloseableHttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String result = URLDecoder.decode(
				EntityUtils.toString(entity, "UTF-8").replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B"),
				"UTF-8");
		EntityUtils.consume(response.getEntity());
		return result;
	}

	// XHS转发
	public static String doPostXHS(HttpRequestBase httpRequestBase) throws IOException {
		if (dfltHttpClient == null)
			initDefaultHttpClient();
		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = null;
		httpRequestBase.setHeader("content-type", "application/json;charset=utf-8");
//		httpGet.setHeader("Accept-Charset", "charset=utf-8");
//		httpGet.setHeader("Accept", "application/json;charset=utf-8");
		try {
			HttpResponse response = dfltHttpClient.execute(httpRequestBase);

			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// 转换成字符流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("连接异常"+e.getMessage());
		} finally {
			// 关闭InputStream和response
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return buffer.toString();

	}

}
