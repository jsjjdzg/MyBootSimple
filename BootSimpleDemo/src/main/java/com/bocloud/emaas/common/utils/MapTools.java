package com.bocloud.emaas.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class MapTools {

	public static Map<String, Object> simpleMap(String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		return map;
	}

	public static String mapToString(Map<String, Object> map) {
		if (null == map || map.isEmpty()) {
			return null;
		}
		StringBuffer stringBuffer = new StringBuffer();
		List<String> keyList = new ArrayList<String>();
		keyList.addAll(map.keySet());
		Collections.sort(keyList);
		for (String key : keyList) {
			stringBuffer.append(key).append("=").append(JSONObject.toJSONString(map.get(key)).replace("\"", "")).append("&");
		}
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		return stringBuffer.toString();
	}
}
