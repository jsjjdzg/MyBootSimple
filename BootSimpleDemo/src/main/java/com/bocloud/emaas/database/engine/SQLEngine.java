package com.bocloud.emaas.database.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.bocloud.emaas.database.core.annotations.Column;
import com.bocloud.emaas.database.core.annotations.IgnoreInsert;
import com.bocloud.emaas.database.core.annotations.IgnoreUpdate;
import com.bocloud.emaas.database.core.annotations.PK;
import com.bocloud.emaas.database.core.annotations.Table;
import com.bocloud.emaas.database.core.entity.Generic;
import com.bocloud.emaas.database.core.meta.ColumnField;
import com.bocloud.emaas.database.exception.BoCloudDBException;

/**
 * SQL引擎，用来处理基本的SQL语句并进行缓存，提供Map转化为实体对象的公共方法
 * 
 * @author dmw
 *
 */
public class SQLEngine {
	private static Logger logger = LoggerFactory.getLogger(SQLEngine.class);

	private static Map<String, List<ColumnField>> ormCache = new ConcurrentHashMap<>(100, 0.9f);// 存放实体类和数据库表的映射缓存
	private static Map<String, String> insertSqlCache = new ConcurrentHashMap<>(100, 0.9f);// 存放插入语句缓存
	private static Map<String, String> updateSqlCache = new ConcurrentHashMap<>(100, 0.9f);// 存放更新语句缓存
	private static Map<String, String> deleteSqlCache = new ConcurrentHashMap<>(100, 0.9f);// 存放删除语句缓存
	private static Map<String, String> selectSqlCache = new ConcurrentHashMap<>(100, 0.9f);// 存放查询语句缓存

	/**
	 * 根据pojo类的class来构建select * from 的SQL语句
	 * 
	 * @param pojoClass
	 * @return
	 */
	public static String buildSelectSql(Class<?> pojoClass) {
		List<ColumnField> columnFiledList = loadColumnField(pojoClass);
		String sql = buildSelectSql(pojoClass, columnFiledList);
		if (logger.isDebugEnabled()) {
			logger.debug("select sql is:[{}]", sql);
		}
		return sql;
	}

	/**
	 * 根据pojo类的class来构建insert的SQL语句
	 * 
	 * @param pojoClass
	 * @return
	 */
	public static String buildInsertSql(Class<?> pojoClass) {
		List<ColumnField> columnFiledList = loadColumnField(pojoClass);//获取对应字段们的属性
		String sql = buildInsertSql(pojoClass, columnFiledList);
		if (logger.isDebugEnabled()) {
			logger.debug("insert sql is:[{}]", sql);
		}
		return sql;
	}

	/**
	 * 根据pojo类的class构建根据pk来update的SQL语句
	 * 
	 * @param pojoObject
	 * @return
	 */
	public static String buildUpdateSql(Class<?> pojoClass) {
		List<ColumnField> columnFiledList = loadColumnField(pojoClass);
		String sql = buildUpdateSqlByPK(pojoClass, columnFiledList);
		if (logger.isDebugEnabled()) {
			logger.debug("update sql is:[{}]", sql);
		}
		return sql;
	}

	/**
	 * 根据pojo类的Class和更新的条件字段来生成upate的SQL语句
	 * 
	 * @param pojoClass
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public static String buildUpdateSqlByColumns(Class<?> pojoClass, String[] columns) throws Exception {
		if (null != columns && columns.length > 0) {
			List<ColumnField> columnFiledList = loadColumnField(pojoClass);
			String sql = buildUpdateSqlByColumns(pojoClass, columnFiledList, columns);
			if (logger.isDebugEnabled()) {
				logger.debug("update sql is:[{}]", sql);
			}
			return sql;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("生成update sql error! 参数columns必须有值");
			}
			throw new Exception("参数columns必须有值！");
		}
	}

	/**
	 * 根据pojo类的Class生成根据pk来delete的SQL语句
	 * 
	 * @param pojoClass
	 * @return
	 * @throws BoCloudDBException
	 */
	public static String buildDeleteSql(Class<?> pojoClass) throws BoCloudDBException {
		List<ColumnField> columnFiledList = loadColumnField(pojoClass);
		String sql = buildDeleteSqlByPK(pojoClass, columnFiledList);
		if (logger.isDebugEnabled()) {
			logger.debug("delete sql is::[{}]", sql);
		}
		return sql;
	}

	/**
	 * 根据pojo类的Class和更新的条件字段来生成delete的SQL语句
	 * 
	 * @param pojoClass
	 * @param columns
	 * @return
	 * @throws Exception
	 */
	public static String buildDeleteSqlByColumns(Class<?> pojoClass, String[] columns) throws Exception {
		if (null == columns || columns.length <= 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("生成delete sql error! 参数columns必须有值");
			}
			throw new Exception("参数columns必须有值！");
		}
		List<ColumnField> columnFiledList = loadColumnField(pojoClass);
		String sql = buildDeleteSqlByColumns(pojoClass, columnFiledList, columns);
		if (logger.isDebugEnabled()) {
			logger.debug("delete sql is:[{}]", sql);
		}
		return sql;
	}

	/**
	 * 将SQL查询出来的map对象转成实体对象
	 * 
	 * @param map
	 * @param pojoClass
	 * @return
	 * @throws Exception
	 */
	public static Object coverMapToBean(Map<String, Object> map, Class<?> pojoClass) throws Exception {
		Object result = pojoClass.newInstance();
		List<ColumnField> list = loadColumnField(pojoClass);
		for (ColumnField columnFiled : list) {
			String columnName = columnFiled.getColumnName().toUpperCase();
			String fieldName = columnFiled.getFieldName();
			String setMethoName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			if (null == map.get(columnName)) {
				continue;
			}
			Method m = pojoClass.getMethod(setMethoName, columnFiled.getType());
			if (columnFiled.getType().equals(Boolean.class)) {
				if (map.get(columnName).toString().equals("0") || !Boolean.valueOf(map.get(columnName).toString())) {
					m.invoke(result, false);
				} else {
					m.invoke(result, true);
				}
			} else {
				m.invoke(result, map.get(columnName));
			}
		}
		return result;
	}

	/**
	 * 加载读取pojo的注解信息
	 * 
	 * @param pojoClass
	 * @return
	 */
	public static List<ColumnField> loadColumnField(Class<?> pojoClass) {
		List<ColumnField> columnFields = null;
		if (null == ormCache.get(pojoClass.getName())) { // 查看映射缓存中是否有该实体class缓存
			columnFields = new ArrayList<ColumnField>();
			List<Field> totalFields = new ArrayList<Field>();
			if (null != pojoClass.getSuperclass() && null != pojoClass.getSuperclass().getSuperclass()
					&& pojoClass.getSuperclass().getSuperclass().equals(Generic.class)) {// 判断超类的存在
				Field[] superFields = pojoClass.getSuperclass().getDeclaredFields(); // 获取目前类的超类字段
				// 反射 --返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段
				if (superFields.length > 0) {
					for (Field field : superFields) {
						totalFields.add(field); // 将反射得到的字段存起来
					}
				}
			}
			Field[] fields = pojoClass.getDeclaredFields();// 获取目前类字段
			for (Field field : fields) {
				totalFields.add(field);// 将反射得到的字段存起来（该集合可能包含超类的字段）
			}
			for (Field field : totalFields) {
				ColumnField columnFiled = new ColumnField();
				columnFiled.setFieldName(field.getName());// 设置字段名
				if (field.isAnnotationPresent(Column.class)) {//判断该字段是否有Column注解
					String value = (field.getAnnotation(Column.class)).value();// 得到配置的数据库字段名
					if (StringUtils.isEmpty(value)) {// 没有设置数据库的字段名，则取pojo的字段名
						columnFiled.setColumnName(fieldToColumn(field.getName()));
					} else {
						columnFiled.setColumnName(value);
					}
				} else {
					columnFiled.setColumnName(fieldToColumn(field.getName()));//将大写字母转换成下划线加小写字母 例:u(U)serName--> user_name
				}

				if (field.isAnnotationPresent(PK.class)) {//判断该字段是否有PK注解
					columnFiled.setIsPk(true);
				}
				if (field.isAnnotationPresent(IgnoreInsert.class)) {//判断该字段是否有IgnoreInsert注解
					columnFiled.setIsInsert(false);
				}
				if (field.isAnnotationPresent(IgnoreUpdate.class)) {//判断该字段是否有IgnoreUpdate注解
					columnFiled.setIsUpdate(false);
				}

				columnFiled.setType(field.getType());//设置该字段类型 String...

				columnFields.add(columnFiled);//放置到字段集合内
			}
			ormCache.put(pojoClass.getName(), columnFields);//ormCache缓存中放置 类-类中字段们 的键值对
		} else {
			columnFields = ormCache.get(pojoClass.getName());
		}

		return columnFields;
	}

	/**
	 * 拼接select语句
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @return
	 */
	private static String buildSelectSql(Class<?> pojoClass, List<ColumnField> columnFiledList) {
		if (selectSqlCache.get(pojoClass.getName()) != null) {
			return (String) selectSqlCache.get(pojoClass.getName());
		}
		if (null == columnFiledList || columnFiledList.isEmpty()) {
			return "SELECT * FROM " + loadTableName(pojoClass);
		}
		StringBuffer selectColumnBuffer = new StringBuffer();
		for (ColumnField filed : columnFiledList) {
			selectColumnBuffer.append(filed.getColumnName().toUpperCase()).append(",");
		}
		String selectColumn = selectColumnBuffer.substring(0, selectColumnBuffer.length() - 1);
		return "SELECT " + selectColumn + " FROM " + loadTableName(pojoClass);
	}

	/**
	 * 拼接insert的SQL
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @return
	 */
	private static String buildInsertSql(Class<?> pojoClass, List<ColumnField> columnFiledList) {
		if (insertSqlCache.get(pojoClass.getName()) != null) {
			return insertSqlCache.get(pojoClass.getName());
		}
		String result = null;
		String tableName = loadTableName(pojoClass);
		StringBuffer temp1 = new StringBuffer();
		StringBuffer temp2 = new StringBuffer();
		for (ColumnField columnFiled : columnFiledList) {
			if (columnFiled.isInsert()) {//根据是否需要插入的属性（在loadColumnField方法中将注解转换为属性）
				temp1.append(columnFiled.getColumnName()).append(",");
				temp2.append(":").append(columnFiled.getFieldName()).append(",");
			}
		}
		temp1.deleteCharAt(temp1.length() - 1); //删除最后的 ','
		temp2.deleteCharAt(temp2.length() - 1);

		StringBuffer resultSql = new StringBuffer();
		resultSql.append("insert into ");
		resultSql.append(tableName);
		resultSql.append("(");
		resultSql.append(temp1);
		resultSql.append(") values (");
		resultSql.append(temp2);
		resultSql.append(")");

		result = resultSql.toString();
		insertSqlCache.put(pojoClass.getName(), result);
		return result;
	}

	/**
	 * 生成根据主键生成删除的SQL
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @return
	 * @throws BoCloudDBException
	 */
	private static String buildDeleteSqlByPK(Class<?> pojoClass, List<ColumnField> columnFiledList)
			throws BoCloudDBException {
		String result = null;
		if (deleteSqlCache.get(pojoClass.getName() + "_pk") != null) {
			result = (String) deleteSqlCache.get(pojoClass.getName() + "_pk");
			return result;
		}

		StringBuffer resultSql = new StringBuffer();
		resultSql.append(appendBaseDeleteSQL(pojoClass));
		boolean hasPk = false;
		for (ColumnField columnFiled : columnFiledList) {
			if (columnFiled.isPk()) {
				resultSql.append(columnFiled.getColumnName());
				resultSql.append("=:").append(columnFiled.getFieldName()).append(" and ");
				hasPk = true;
			}
		}
		if (hasPk) {
			resultSql.delete(resultSql.length() - 4, resultSql.length());
			result = resultSql.toString();
			deleteSqlCache.put(pojoClass.getName() + "_pk", result);

			return result;
		} else {
			throw new BoCloudDBException("No　Primary Key Found!");
		}

	}

	/**
	 * 拼接根据主键来update的SQL
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @return
	 */
	private static String buildUpdateSqlByPK(Class<?> pojoClass, List<ColumnField> columnFiledList) {
		String result = null;
		if (updateSqlCache.get(pojoClass.getName() + "_pk") != null) {
			result = (String) updateSqlCache.get(pojoClass.getName() + "_pk");
			return result;
		}

		StringBuffer resultSql = new StringBuffer();
		resultSql.append(appendBaseUpdateSQL(pojoClass, columnFiledList));
		// UPDATE TABLENAME SET A=:A ....WHERE
		if (null == columnFiledList || columnFiledList.isEmpty()) {
			resultSql.delete(resultSql.length() - 6, resultSql.length());
		} else {
			for (ColumnField columnFiled : columnFiledList) {
				if (columnFiled.isPk()) {
					resultSql.append(columnFiled.getColumnName());
					resultSql.append("=:").append(columnFiled.getFieldName()).append(" and ");
				}
			}
			resultSql.delete(resultSql.length() - 4, resultSql.length());
		}
		// UPDATE TABLENAME SET A=:A ...WHERE PK =:PK.....
		result = resultSql.toString();
		updateSqlCache.put(pojoClass.getName() + "_pk", result);
		return result;
	}

	/**
	 * 根据用户指定的更新条件(字段)来生成update的SQL
	 * 
	 * @param pojoClass 实体类
	 * @param columnFiledList 实体字段映射列表
	 * @param columns 更新条件字段
	 * @return
	 */
	private static String buildUpdateSqlByColumns(Class<?> pojoClass, List<ColumnField> columnFiledList,
			String[] columns) {
		StringBuffer resultSql = new StringBuffer();
		if (updateSqlCache.get(pojoClass.getName() + "_columns") != null) {
			resultSql.append((String) updateSqlCache.get(pojoClass.getName() + "_columns"));
		} else {
			resultSql.append(appendBaseUpdateSQL(pojoClass, columnFiledList));
		}
		// UPDATE TABLENAME SET A=:A.... WHERE
		for (String column : columns) {
			for (ColumnField columnFiled : columnFiledList) {
				if (column.equals(columnFiled.getFieldName())) {
					resultSql.append(columnFiled.getColumnName());
					resultSql.append("=:").append(column).append(" and ");
					break;
				}
			}
		}
		// UPDATE TABLENAME SET A=:A ... WHERE COL1=:AA AND ...
		resultSql.delete(resultSql.length() - 4, resultSql.length());
		return resultSql.toString();
	}

	/**
	 * 拼接update语句的where之前的sql
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @param resultSql
	 */
	private static String appendBaseUpdateSQL(Class<?> pojoClass, List<ColumnField> columnFiledList) {
		String result = null;
		if (updateSqlCache.get(pojoClass.getName() + "_columns") != null) {
			result = (String) updateSqlCache.get(pojoClass.getName() + "_columns");
		} else {
			StringBuffer resultSql = new StringBuffer();
			String tableName = loadTableName(pojoClass);

			resultSql.append("update ").append(tableName).append(" set ");
			for (ColumnField columnFiled : columnFiledList) {
				if (columnFiled.isUpdate() && !columnFiled.isPk()) {
					resultSql.append(columnFiled.getColumnName());
					resultSql.append("=:").append(columnFiled.getFieldName()).append(",");
				}
			}
			resultSql.deleteCharAt(resultSql.length() - 1);
			resultSql.append(" where ");

			result = resultSql.toString();
			updateSqlCache.put(pojoClass.getName() + "_columns", result);
		}
		return result;
	}

	/**
	 * 根据用户指定的更新条件(字段)来生成delete的SQL
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @param columns
	 * @return
	 */
	private static String buildDeleteSqlByColumns(Class<?> pojoClass, List<ColumnField> columnFiledList,
			String[] columns) {
		StringBuffer resultSql = new StringBuffer();
		if (deleteSqlCache.get(pojoClass.getName() + "_columns") != null) {
			resultSql.append((String) deleteSqlCache.get(pojoClass.getName() + "_columns"));
		} else {
			resultSql.append(appendBaseDeleteSQL(pojoClass));
		}
		if (null == columnFiledList || columnFiledList.isEmpty()) {
			resultSql.delete(resultSql.length() - 6, resultSql.length());
		} else {
			for (String column : columns) {
				for (ColumnField columnFiled : columnFiledList) {
					if (column.equals(columnFiled.getFieldName())) {
						resultSql.append(columnFiled.getColumnName());
						resultSql.append("=:").append(column).append(" and ");
						break;
					}
				}
			}
			resultSql.delete(resultSql.length() - 4, resultSql.length());
		}
		return resultSql.toString();
	}

	/**
	 * 拼接delete语句的where之前的sql
	 * 
	 * @param pojoClass
	 * @param columnFiledList
	 * @param resultSql
	 */
	private static String appendBaseDeleteSQL(Class<?> pojoClass) {
		if (deleteSqlCache.get(pojoClass.getName() + "_columns") != null) {
			return (String) deleteSqlCache.get(pojoClass.getName() + "_columns");
		} else {
			String result = "delete from " + loadTableName(pojoClass) + " where ";
			deleteSqlCache.put(pojoClass.getName() + "_columns", result);
			return result;
		}
	}

	/**
	 * 通过类获取表名
	 * 
	 * @param pojoClass
	 * @return
	 */
	public static String loadTableName(Class<?> pojoClass) {
		if (pojoClass.isAnnotationPresent(Table.class)) {
			Table table = (Table) pojoClass.getAnnotation(Table.class);//获取@Table注解内的值
			return table.value();
		} else {
			return fieldToColumn(pojoClass.getSimpleName());//反射-获取源代码中给出的‘底层类’简称
		}
	}

	public static List<String> loadPk(Class<?> pojoClass) {
		List<String> pkList = new ArrayList<String>();
		List<ColumnField> columnFieldList = loadColumnField(pojoClass);
		if (!columnFieldList.isEmpty()) {
			for (ColumnField columnField : columnFieldList) {
				if (columnField.isPk()) {
					pkList.add(columnField.getColumnName());
				}
			}
		}
		return pkList;
	}

	public static String loadSinglePk(Class<?> pojoClass) {
		List<String> pkList = loadPk(pojoClass);
		return pkList.size() == 1 ? pkList.get(0) : null;
	}

	/**
	 * 将大写字母转换成下划线加小写字母 例:u(U)serName--> user_name
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
}
