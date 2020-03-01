package com.px.mis.ec.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private HttpUtils() {

    }

    /**
     * application/json 方式请求
     *
     * @param host    请求地址
     * @param headers 请求头, 使用默认时传 null
     * @param params  请求参数, 无时传 null, 就是拼接在主机名后的key=value参数, 可直接处理成host代替
     * @param body    请求体
     * @return {@link String} content
     */
    public static String postJson(String host, Map<String, String> headers, Map<String, String> params, String body) throws IOException {
        return post(host, getHeaders(headers, RequestContentType.JSON), params, RequestContentType.JSON, body);
    }

    /**
     * application/x-www-form-urlencoded 方式请求
     *
     * @param host    请求地址
     * @param headers 请求头, 使用默认传 null
     * @param params  请求参数
     * @return {@link String} content
     * @throws IOException {@link IOException} error
     */
    public static String postUrlEncoded(String host, Map<String, String> headers, Map<String, String> params) throws IOException {
        return post(host, getHeaders(headers, RequestContentType.URL_ENCODED), params, RequestContentType.URL_ENCODED, null);
    }

    /**
     * 获取头信息, 无时提供内容的头信息
     *
     * @param headers 已有头信息
     * @param type    头内容类型
     * @return 头内容类型
     */
    private static Map<String, String> getHeaders(Map<String, String> headers, RequestContentType type) {
        if (headers != null && headers.size() >= 1) {
            return headers;
        }
        Map<String, String> defaultHeaders = new HashMap<String, String>();
        switch (type) {
            case JSON:
                defaultHeaders.put("Content-Type", "application/json");
                break;
            case URL_ENCODED:
                defaultHeaders.put("Content-Type", "application/x-www-form-urlencoded");
                break;
        }
        return defaultHeaders;
    }

    /**
     * 请求头内容类型
     */
    private enum RequestContentType {
        // Content-Type: application/json
        JSON,
        // Content-Type: application/x-www-form-urlencoded
        URL_ENCODED
    }

    /**
     * 发起 http post请求
     *
     * @param uri     请求主机名称, 必须存在
     * @param headers 请求头信息, 内部已处理
     * @param params  请求参数内容, 可能不存在
     * @param type    请求头内容类型
     * @param body    请求内容体, 可能不存在
     * @return 响应
     * @throws IOException 响应失败
     */
    private static String post(
            String uri,
            Map<String, String> headers,
            Map<String, String> params,
            RequestContentType type,
            String body) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;

        // for test
        System.out.printf("\nuri: %s\n", uri);
        // 添加参数
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        try {
            // 请求参数除body外最终都需要拼接到地址栏中
            // 如果需要单独设置 Entity 再处理
            URIBuilder builder = new URIBuilder(uri);
            if (pairList.size() > 0) {
                builder.setParameters(pairList);
            }
            httpPost = new HttpPost(builder.build());

            // 存在连接超时和数据超时, 会导致请求卡住, 需要主动放弃
            RequestConfig config = RequestConfig.custom()
                    // 设置连接超时时间, 单位毫秒
                    .setConnectTimeout(10000)
                    // 请求获取数据的超时时间, 单位毫秒
                    // 如果访问一个接口, 多少时间内无法返回数据, 就直接放弃此次调用
                    .setSocketTimeout(10000)
                    .build();
            httpPost.setConfig(config);

            // entity 实体处理
            HttpEntity entity = null;
            if (type.equals(RequestContentType.JSON)) {
                // 有 body 内容需要处理body
                if (body != null && body.length() > 0) {
                    entity = new StringEntity(body, Charset.forName("UTF-8"));
                }
            }

            if (entity != null) {
                httpPost.setEntity(entity);
            }

            // 添加头内容
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity());

            if (code == HttpStatus.SC_OK) {
                return content;
            }
            // 暂不做重试, 直接抛出异常
            throw new IOException("服务响应失败");
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {

                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
    }
}
