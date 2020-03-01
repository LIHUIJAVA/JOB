package com.px.mis.ec.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SendUtil {
    /**
     * send post
     */
    public static String sendPost(String url, String data) {
        String response=null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpresponse = null;
        try {
                httpclient = HttpClients.createDefault();
                
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(
                        data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils.toString(httpresponse.getEntity());
        } catch (Exception e) {
            //e.printStackTrace();
        }finally {
            try {
				if (httpclient != null) {
					httpclient.close();
				}
				if (httpresponse != null) {
					httpresponse.close();
				} 
			} catch (Exception e2) {
				// TODO: handle exception
			}
        }
        return response;
    }
}
