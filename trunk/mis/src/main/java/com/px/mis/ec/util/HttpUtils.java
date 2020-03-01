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
     * application/json ��ʽ����
     *
     * @param host    �����ַ
     * @param headers ����ͷ, ʹ��Ĭ��ʱ�� null
     * @param params  �������, ��ʱ�� null, ����ƴ�������������key=value����, ��ֱ�Ӵ����host����
     * @param body    ������
     * @return {@link String} content
     */
    public static String postJson(String host, Map<String, String> headers, Map<String, String> params, String body) throws IOException {
        return post(host, getHeaders(headers, RequestContentType.JSON), params, RequestContentType.JSON, body);
    }

    /**
     * application/x-www-form-urlencoded ��ʽ����
     *
     * @param host    �����ַ
     * @param headers ����ͷ, ʹ��Ĭ�ϴ� null
     * @param params  �������
     * @return {@link String} content
     * @throws IOException {@link IOException} error
     */
    public static String postUrlEncoded(String host, Map<String, String> headers, Map<String, String> params) throws IOException {
        return post(host, getHeaders(headers, RequestContentType.URL_ENCODED), params, RequestContentType.URL_ENCODED, null);
    }

    /**
     * ��ȡͷ��Ϣ, ��ʱ�ṩ���ݵ�ͷ��Ϣ
     *
     * @param headers ����ͷ��Ϣ
     * @param type    ͷ��������
     * @return ͷ��������
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
     * ����ͷ��������
     */
    private enum RequestContentType {
        // Content-Type: application/json
        JSON,
        // Content-Type: application/x-www-form-urlencoded
        URL_ENCODED
    }

    /**
     * ���� http post����
     *
     * @param uri     ������������, �������
     * @param headers ����ͷ��Ϣ, �ڲ��Ѵ���
     * @param params  �����������, ���ܲ�����
     * @param type    ����ͷ��������
     * @param body    ����������, ���ܲ�����
     * @return ��Ӧ
     * @throws IOException ��Ӧʧ��
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
        // ��Ӳ���
        List<NameValuePair> pairList = new ArrayList<NameValuePair>();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        try {
            // ���������body�����ն���Ҫƴ�ӵ���ַ����
            // �����Ҫ�������� Entity �ٴ���
            URIBuilder builder = new URIBuilder(uri);
            if (pairList.size() > 0) {
                builder.setParameters(pairList);
            }
            httpPost = new HttpPost(builder.build());

            // �������ӳ�ʱ�����ݳ�ʱ, �ᵼ������ס, ��Ҫ��������
            RequestConfig config = RequestConfig.custom()
                    // �������ӳ�ʱʱ��, ��λ����
                    .setConnectTimeout(10000)
                    // �����ȡ���ݵĳ�ʱʱ��, ��λ����
                    // �������һ���ӿ�, ����ʱ�����޷���������, ��ֱ�ӷ����˴ε���
                    .setSocketTimeout(10000)
                    .build();
            httpPost.setConfig(config);

            // entity ʵ�崦��
            HttpEntity entity = null;
            if (type.equals(RequestContentType.JSON)) {
                // �� body ������Ҫ����body
                if (body != null && body.length() > 0) {
                    entity = new StringEntity(body, Charset.forName("UTF-8"));
                }
            }

            if (entity != null) {
                httpPost.setEntity(entity);
            }

            // ���ͷ����
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            response = client.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity());

            if (code == HttpStatus.SC_OK) {
                return content;
            }
            // �ݲ�������, ֱ���׳��쳣
            throw new IOException("������Ӧʧ��");
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
