package com.px.mis.account.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.util.JacksonUtil;

public class TransformJson {
	public static <T> String returnRespList(String url, Boolean isSuccess, String massage,  List<T> objList) throws IOException {
        ObjectNode respon=JacksonUtil.getObjectNode("");
        ObjectNode respHead=JacksonUtil.getObjectNode("");
        respHead.put("url", url);
        respHead.put("isSuccess", isSuccess);
        respHead.put("message", massage);
        respon.put("respHead", respHead);
        ObjectNode  respBody=JacksonUtil.getObjectNode("");
        respBody.put("list",JacksonUtil.getObjectNode(objList).get("list"));
        respon.put("respBody", respBody);
        return  respon.toString();
    }
}
