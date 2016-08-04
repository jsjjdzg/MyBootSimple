package com.bocloud.emaas.common.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class JSONTools {

	private static Logger logger = LoggerFactory.getLogger(JSONTools.class);

	public static boolean isMap(String jsonString) {
		try {
			JSONObject.parseObject(jsonString, HashMap.class);
			return true;
		} catch (Exception e) {
			logger.error("Param formater exception:", e);
			return false;
		}
	}

	public static JSONObject isJSONObj(String jsonString) {
		if (StringUtils.hasText(jsonString)) {
			try {
				return JSONObject.parseObject(jsonString, JSONObject.class);
			} catch (Exception e) {
				logger.error("Param formater exception:", e);
				return null;
			}
		} else {
			return null;
		}
	}
}
