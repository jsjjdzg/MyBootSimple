package com.bocloud.emaas.database.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class PageHelper {

	public static final String IS_PAGING = "IS_PAGING";
	public static final String PAGE_NUM = "PAGE_NUM";
	public static final String PAGE_SIZE = "PAGE_SIZE";

	public static Map<String, Object> initPage(int pageNum, int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(IS_PAGING, true);
		map.put(PAGE_NUM, pageNum);
		map.put(PAGE_SIZE, pageSize);
		return map;
	}

	public static boolean isPagingSearchRequest(Map<String, Object> paramMap) {
		for (String key : paramMap.keySet()) {
			if (key.equals(IS_PAGING) && Boolean.valueOf(paramMap.get(key).toString())) {
				return true;
			}
		}
		return false;
	}

	public static List<Map<String, Object>> pagingQuery(NamedParameterJdbcTemplate jdbcTemplate, String sql,
			Class<?> cs, Map<String, Object> paramMap) throws Exception {
		int pageNum = 0;
		int pageSize = 0;
		String pageNumStr = paramMap.get(PAGE_NUM).toString();
		if (NumberUtils.isNumber(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		String pageSizeStr = paramMap.get(PAGE_SIZE).toString();
		if (NumberUtils.isNumber(pageNumStr)) {
			pageSize = Integer.valueOf(pageSizeStr);
		}
		sql = sql + " " + buildPageSql(pageSize, pageNum);
		return jdbcTemplate.queryForList(sql, paramMap);
	}

	public static String buildPageSql(int pageNum, int pageSize) {
		pageSize = 0 >= pageSize ? 10 : pageSize;//pageSize大于0则用pageSize，否则默认10
		pageNum = 0 >= pageNum ? 1 : pageNum;//pageSize大于0则用pageSize，否则默认第1页
		return " LIMIT " + (pageNum - 1) * pageSize + "," + pageSize;
	}
}
