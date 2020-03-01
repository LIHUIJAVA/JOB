package com.px.mis.ec.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class Json {
	
	/**
	 * jsonת���ɶ���
	 */
    public static <T> T jsonToBean(String jsonString, Class<T> cls){
        T t = null;
        try {
            t = JSON.parseObject(jsonString,cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    /**
     * json�ַ���תmap����
     */
    public static HashMap<String, Object> jsonToMap(String jsonStr){
        return JSON.parseObject(jsonStr, new HashMap<String, Object>().getClass());
    }

   /**
    * jsonת����list
    */
    public static JSONArray jsonToList(String jsonString){
    	 return JSON.parseArray(jsonString);
    }
    /**
     * jsonתlist
     * (��Ҫʵ����)
     */
    public static <T>List<T> jsonToArrayList(String jsonString,Class cls){
        List<T> list = null;
        try {
            list = (List<T>) JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * objת����json
     */
    public static String beanToJson(Object obj){
        return JSON.toJSONString(obj);
    }
    /**
     * listתjson
     */
    public static String listToJson(Object obj) {
    	
    	return JSONArray.toJSONString(obj);
    }
    
    /**
     * mapתjson�ַ���
     */
    public static String mapToJson(Map<String, Object> map){
        String jsonStr = JSON.toJSONString(map);
        return jsonStr;
    } 
}
