package com.bocloud.emaas.database.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.bocloud.emaas.common.model.Param;
import com.bocloud.emaas.common.model.Relation;
import com.bocloud.emaas.common.model.Sign;
import com.bocloud.emaas.common.utils.Common;

/**
 * SQL语句构建工具类
 * 
 * @author dmw
 *
 */
public class SQLHelper {

	private static final String SPACE = " ";
	private static final String DOT = ".";
	private static final String SPRATE = " :";
	private static final String GROUPBY = " GROUP BY ";
	private static final String ORDERBY = " ORDER BY ";
	private static final String ASC = " ASC,";
	private static final String DESC = " DESC,";
	private static final String QUOTE = "'";

	/**
	 * 构建查询语句
	 * 
	 * @param sql
	 *            初始查询语句
	 * @param page
	 *            当前页码
	 * @param rows
	 *            页面大小
	 * @param params
	 *            查询参数
	 * @param sorter
	 *            排序参数
	 * @param tag
	 *            多表查询标签
	 * @return 构建后的查询语句
	 */
	public static String buildSql(String sql, int page, int rows, List<Param> params, Map<String, String> sorter,
			String tag) {
		StringBuffer resultSql = new StringBuffer(buildSql(sql, params, sorter, tag));
		resultSql.append(PageHelper.buildPageSql(page, rows));
		return resultSql.toString();
	}

	/**
	 * 构建查询语句
	 * 
	 * @param sql
	 *            初始查询语句
	 * @param page
	 *            当前页码
	 * @param rows
	 *            页面大小
	 * @param params
	 *            查询参数
	 * @param sorter
	 *            排序参数
	 * @param tag
	 *            多表查询标签
	 * @return 构建后的查询语句
	 */
	public static String buildRawSql(String sql, int page, int rows, List<Param> params, Map<String, String> sorter,
			String tag) {
		StringBuffer resultSql = new StringBuffer(buildRawSql(sql, params, sorter, tag));
		resultSql.append(PageHelper.buildPageSql(page, rows));
		return resultSql.toString();
	}

	/**
	 * 构建查询语句
	 * 
	 * @param sql
	 *            初始查询语句
	 * @param params
	 *            查询参数
	 * @param sorter
	 *            排序参数
	 * @param tag
	 *            多表查询标签
	 * @return 构建后的查询语句
	 * 单个Param==>
	 *  private Relation relation = Relation.AND;
		private Map<String, Object> param;
		private Sign sign;
	 */
	public static String buildSql(String sql, List<Param> params, Map<String, String> sorter, String tag) {
		StringBuffer resultSql = new StringBuffer(sql);
		if (StringUtils.hasText(tag)) {//判断值中是否是null或者空字符串 是false 不是true
			tag = tag + DOT;	//这里的tag是表名，单表 直接tag为空字符串即可
		}
		/*
		 * 查询参数列表不为空时构建查询语句
		 */
		if (null != params && !params.isEmpty()) {
			Sign sign = null;//标志Enum --是like，或者其他关键词EQ("=")
			Relation relation = null;//关系 and 和 or
			for (Param param : params) {
				sign = param.getSign();
				relation = param.getRelation();
				switch (sign) {
				case LK:// 关键字为like
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + SPRATE + key); //==> " AND xx.dzg_name LIKE :dzgName" 实际使用直接替换该dzgName
					}
					break;
				case GET:// 关键字为>=
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + SPRATE + key);//==> " AND xx.dzg_name >= : dzgName"
					}
					break;
				case LET:// 关键字为<=
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + SPRATE + key);//==> " AND xx.dzg_name <= : dzgName"
					}
					break;
				case NUL:// 关键字为 is null
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign());//==> " AND xx.dzg_name IS NULL"
					}
					break;
				default:// 其他情形一律按照此逻辑构建
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + SPRATE + key);//==> " AND xx.dzg_name XX : dzgName"
					}
					break;
				}
			}
		}
		/*
		 * 排序参数不为空时构建排序语句，0表示升序，1表示降序
		 */
		if (null != sorter && !sorter.isEmpty()) {
			resultSql.append(ORDERBY);
			for (String column : sorter.keySet()) {
				if (sorter.get(column).equals(Common.ZERO)) {
					resultSql.append(SPACE + tag + fieldToColumn(column) + ASC);//==> " ORDER BY xx.dzg_name ASC,"
				} else {
					resultSql.append(SPACE + tag + fieldToColumn(column) + DESC);//==> " ORDER BY xx.dzg_name DESC,"
				}
			}
			resultSql.delete(resultSql.length() - 1, resultSql.length());
		}

		return resultSql.toString();
	}

	/**
	 * 构建查询语句
	 * 
	 * @param sql
	 *            初始查询语句
	 * @param params
	 *            查询参数
	 * @param sorter
	 *            排序参数
	 * @param tag
	 *            多表查询标签
	 * @return 构建后的查询语句
	 */
	public static String buildRawSql(String sql, List<Param> params, Map<String, String> sorter, String tag) {
		StringBuffer resultSql = new StringBuffer(sql);
		if (StringUtils.hasText(tag)) {
			tag = tag + DOT;
		}
		/*
		 * 查询参数列表不为空时构建查询语句
		 */
		if (null != params && !params.isEmpty()) {
			Sign sign = null;
			Relation relation = null;
			for (Param param : params) {
				sign = param.getSign();
				relation = param.getRelation();
				switch (sign) {
				case LK:// 关键字为like
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + QUOTE + param.getParam().get(key) + QUOTE);// " AND xx.dzg_name LIKE 'xxxx(dzgName的值)'"
					}
					break;
				case GET:// 关键字为>=
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + QUOTE + param.getParam().get(key) + QUOTE);// " AND xx.dzg_name >= 'xxxx(dzgName的值)'"
					}
					break;
				case LET:// 关键字为<=
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + QUOTE + param.getParam().get(key) + QUOTE);// " AND xx.dzg_name <= 'xxxx(dzgName的值)'"
					}
					break;
				case NUL:// 关键字为 is null
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign());// " AND xx.dzg_name IS NULL"
					}
					break;
				default:// 其他情形一律按照此逻辑构建
					for (String key : param.getParam().keySet()) {
						resultSql.append(SPACE + relation.getRelation() + SPACE + tag + fieldToColumn(key) + SPACE
								+ sign.getSign() + QUOTE + param.getParam().get(key) + QUOTE);// " AND xx.dzg_name (default) 'xxxx(dzgName的值)'"
					}
					break;
				}
			}
		}
		/*
		 * 排序参数不为空时构建排序语句，0表示升序，1表示降序
		 */
		if (null != sorter && !sorter.isEmpty()) {
			resultSql.append(ORDERBY);
			for (String column : sorter.keySet()) {
				if (sorter.get(column).equals(Common.ZERO)) {//"0"为asc "1"为desc
					resultSql.append(SPACE + tag + fieldToColumn(column) + ASC);//==> " ORDER BY xx.dzg_name ASC,"
				} else {
					resultSql.append(SPACE + tag + fieldToColumn(column) + DESC);//==> " ORDER BY xx.dzg_name DESC,"
				}
			}
			resultSql.delete(resultSql.length() - 1, resultSql.length());
		}

		return resultSql.toString();
	}

	/**
	 * 构建参数
	 * 
	 * @param params
	 *            参数列表
	 * @return
	 */
	public static Map<String, Object> getParam(List<Param> params) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> map = null;
		if (null == params) {
			return null;
		}
		for (Param param : params) {
			map = param.getParam();
			for (String key : map.keySet()) {
				if (param.getSign().equals(Sign.LK)) {
					paramMap.put(key, LikeString.assemble(map.get(key).toString()));
				} else {
					paramMap.put(key, map.get(key));
				}
			}
		}
		return paramMap;
	}

	/**
	 * 对象属性转换为库表列名
	 * 
	 * @param str
	 * @return
	 */
	private static String fieldToColumn(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		StringBuilder sb = new StringBuilder(str);
		char c;
		int count = 0;
		sb.replace(0, 1, (str.charAt(0) + "").toLowerCase());
		for (int i = 1; i < str.length(); i++) {
			c = str.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				sb.replace(i + count, i + count + 1, (c + "").toLowerCase());
				sb.insert(i + count, "_");
				count++;
			}
		}
		return sb.toString();
	}

	public static String buildGroupSql(String sql, List<String> groups) {
		if (StringUtils.hasText(sql)) {
			StringBuffer stringBuffer = new StringBuffer(sql);
			stringBuffer.append(GROUPBY);
			for (String group : groups) {
				if (null == group || group.isEmpty()) {
					continue;
				}
				stringBuffer.append(group).append(",");
			}
			stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
			sql = stringBuffer.toString();
		}
		return sql;
	}
}
