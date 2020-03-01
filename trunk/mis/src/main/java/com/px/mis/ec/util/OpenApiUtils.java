package com.px.mis.ec.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;


public class OpenApiUtils {

    private PartnerInfo partner;

    private final String urlTest = "http://openapi.test.youpin.mi.com";
    private final String urlOnline = "https://shopapi.io.mi.com";

    private final Map<String, String> apiDict = new HashMap<String, String>() {
        {
            put("orderstatus", "/openapi/shop/orderstatus");
            put("orderlist", "/openapi/shop/orderlist");
            put("proplist", "/openapi/shop/proplist");
            put("setdelivery", "/openapi/shop/setdelivery");
            put("single", "/mtop/aftersale/openapi/delivery/single");
            put("aftersaleorders", "/mtop/aftersale/openapi/aftersaleorders");
            put("update", "/mtop/aftersale/openapi/delivery/update");


        }
    };

    public void SetShop(PartnerInfo partner) {
        this.partner = partner;
    }

    public String api(String method, Map<String, String> data, String env) {
        if (!env.equals("online")) {
            env = "test";
        }

        try {
            String res = fetch(method, data, env);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String apiV2(String method,   String   sss, String env) {
        if (!env.equals("online")) {
            env = "test";
        }

        try {
            String res = fetchV2(method, sss, env);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private Map<String, String> parseParam(Map<String, String> param) throws Exception {


        String dataValue = AesUtils.encrypt(partner.aesKey, urlEncoded(param));

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> signParam = new TreeMap<String, String>(new MapKeyComparator());
        signParam.put("data", dataValue);
        signParam.put("timestamp", timeStamp);
        signParam.put("partner_id", partner.partnerId);
        String sign = DigestUtils.md5Hex((urlEncoded(signParam) + partner.key).getBytes());

        signParam.put("sign", sign);
        return signParam;
    }
    private Map<String, String> parseParamV2( String  param) throws Exception {
        String dataValue = AesUtils.encrypt(partner.aesKey,  param );

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        Map<String, String> signParam = new TreeMap<String, String>(new MapKeyComparator());
        signParam.put("data", dataValue);
        signParam.put("timestamp", timeStamp);
        signParam.put("partnerId", partner.partnerId);
        System.out.println(partner.partnerId);
        String sign = DigestUtils.md5Hex((urlEncoded(signParam) + partner.key).getBytes());
        System.out.println(  partner.key);
        System.out.println((urlEncoded(signParam) + partner.key));

        signParam.put("sign", sign);
        return signParam;
    }
    private class MapKeyComparator implements Comparator<String> {

        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }


    private String fetch(String method, Map<String, String> data, String env) throws Exception {

        Map<String, String> request;
        try {
            request = parseParam(data);
        } catch (Exception e) {
            throw new Exception(e.toString() + "参数解析错误");
        }

        String url = env.equals("online") ? urlOnline : urlTest;

        System.out.printf("\nurl: %s", url);
        System.out.printf("\nrequest: %s", request);
        String content = HttpUtils.postUrlEncoded(url + apiDict.get(method), null, request);

        return content;

    }
    private String fetchV2(String method,   String  data, String env) throws Exception {

        Map<String, String> request;
        try {
            request = parseParamV2(data);
        } catch (Exception e) {
            throw new Exception(e.toString() + "参数解析错误");
        }
        System.out.printf("\nenv: %s", env);

        String url = env.equals("online") ? urlOnline : urlTest;

        System.out.printf("\nurl: %s", url);
        System.out.printf("\nrequest: %s", request);
        ObjectMapper mapper=new ObjectMapper();
        JsonNode  asdas=mapper.readTree(mapper.writeValueAsString(request));

        List<JsonNode> asli=Arrays.asList(asdas);
        System.out.printf("\nasli: %s", asli);
//        String texts = mapper.writeValueAsString(asli);
//        System.out.printf("\ntexts: %s", texts);
         String content = HttpUtils.postJson(url + apiDict.get(method), null ,null ,asli.toString());

        return content;

    }
    private String urlEncoded(Map<String, String> param) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            builder.append("&");
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString().substring(1);
    }

}