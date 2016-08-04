package com.bocloud.emaas.database.core;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bocloud.emaas.database.core.annotations.PK;
import com.bocloud.emaas.database.core.meta.ColumnField;
import com.bocloud.emaas.database.core.meta.Condition;
import com.bocloud.emaas.database.core.meta.ConditionDef;
import com.bocloud.emaas.database.core.meta.GenerateStrategy;
import com.bocloud.emaas.database.core.meta.Order;
import com.bocloud.emaas.database.engine.SQLEngine;
import com.bocloud.emaas.database.exception.BoCloudDBException;
import com.bocloud.emaas.database.utils.Conditions;
import com.bocloud.emaas.database.utils.PageHelper;

/**
 * ORM映射基础类
 * 
 * @author dmw
 *
 */
@Repository
public class BaseDao {
	private static Logger logger = LoggerFactory.getLogger(BaseDao.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	/**
	 * 根据单一主键获取实体对象，必须为单一主键，无主键或者复合主键将会抛出异常
	 * 
	 * @param clazz
	 *            实体类
	 * @param pk
	 *            主键值
	 * @return 实体对象
	 * @throws Exception
	 */
	public Object loadEntity(Class<?> clazz, Serializable pk) throws Exception {
		String pkColumn = SQLEngine.loadSinglePk(clazz);
		if (null == pkColumn) {
			logger.error("No primary key exist or multi primay key!");
			throw new Exception("No Primary Key Exist Or MultiKey!");
		}
		ConditionDef conditionDef = Conditions.simpleCondition(pkColumn, pkColumn);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(pkColumn, pk);
		return this.queryForEntity(clazz, conditionDef, paramMap);
	}

	/**
	 * 保存实体类，返回主键
	 * 
	 * @param bean
	 *            实体类对象
	 * @return 带有实体类的对象
	 * @throws Exception
	 */
	public Object saveEntity(Object bean) throws Exception {
		GenerateStrategy strategy = getPkGenStrategy(bean);
		switch (strategy) {
		case UUID:
			setPk(bean, UUID.randomUUID().toString());
			break;
		default:
			break;
		}
		final String sql = SQLEngine.buildInsertSql(bean.getClass());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(bean);
		namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
		switch (strategy) {
		case AUTO_INCREMENT:
			setPk(bean, keyHolder.getKey().longValue());
			break;
		default:
			break;
		}
		return bean;
	}

	/**
	 * 指定无参sql语句
	 * 
	 * @param sql
	 * @throws Exception
	 */
	public void execute(String sql) throws Exception {
		this.jdbcTemplate.execute(sql);
	}

	/**
	 * 执行带参sql语句
	 * 
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public int update(String sql, Map<String, Object> paramMap) {
		return this.namedParameterJdbcTemplate.update(sql, paramMap);
	}

	/**
	 * 返回原始数据的列表查询
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForList(String sql) throws Exception {
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * 返回实体对象列表的查询
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryForList(String sql, Class<?> clazz) throws Exception {
		List<Map<String, Object>> metaList = this.queryForList(sql);
		List<Object> list = new ArrayList<Object>();
		for (Map<String, Object> meta : metaList) {
			list.add(SQLEngine.coverMapToBean(meta, clazz));
		}
		return list;
	}

	public List<Map<String, Object>> queryForList(String sql, Map<String, ?> paramMap) {
		return this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
	}

	public List<Object> queryForList(String sql, Map<String, ?> paramMap, Class<?> clazz) throws Exception {
		List<Map<String, Object>> metaList = this.queryForList(sql, paramMap);
		List<Object> list = new ArrayList<Object>();
		for (Map<String, Object> meta : metaList) {
			list.add(SQLEngine.coverMapToBean(meta, clazz));
		}
		return list;
	}

	private GenerateStrategy getPkGenStrategy(Object bean) {
		GenerateStrategy strategy = GenerateStrategy.DEFINITION;
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(PK.class)) {
				strategy = (field.getAnnotation(PK.class)).value();
				break;
			}
		}
		return strategy;
	}

	private void setPk(Object bean, Serializable pk) throws Exception {
		List<ColumnField> columnFieldList = SQLEngine.loadColumnField(bean.getClass());
		if (null == columnFieldList || columnFieldList.isEmpty()) {
			return;
		}
		for (ColumnField columnField : columnFieldList) {
			if (columnField.isPk()) {
				String fieldName = columnField.getFieldName();
				String setMethoName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);//抽象出的方法名
				Method m = bean.getClass().getMethod(setMethoName, columnField.getType());//根据方法名和类型获取该方法 如此处是 setId()方法
				m.invoke(bean, pk);//setId()方法 将对象和需要set的序列化内容执行
				break;
			}
		}
		return;
	}

	/**
	 * 保存新增的实体对象
	 * 
	 * @param bean
	 * @return
	 */
	public boolean baseSaveEntity(Object bean) {
		String sql = SQLEngine.buildInsertSql(bean.getClass());
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/**
	 * 根据主键保存修改的实体对象
	 * 
	 * @param bean
	 * @return
	 */
	public boolean updateEntity(Object bean) {
		String sql = SQLEngine.buildUpdateSql(bean.getClass());
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/**
	 * 根据bean的部分字段的条件来更新bean的信息
	 * 
	 * @param bean
	 * @param fileds
	 * @return
	 * @throws Exception
	 */
	public boolean updateWithColumn(Object bean, String[] fileds) throws Exception {
		String sql = SQLEngine.buildUpdateSqlByColumns(bean.getClass(), fileds);
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/**
	 * 根据bean的pk来删除bean
	 * 
	 * @param bean
	 * @return
	 */
	public boolean baseDelete(Object bean) {
		String sql = null;
		try {
			sql = SQLEngine.buildDeleteSql(bean.getClass());
		} catch (BoCloudDBException e) {
			logger.error("BaseDelete Exception:",e);
			return false;
		}
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/**
	 * 根据bean的部分字段的条件来删除bean
	 * 
	 * @param bean
	 * @param fileds
	 * @return
	 * @throws Exception
	 */
	public boolean deleteWithColumn(Object bean, String[] fileds) throws Exception {
		String sql = SQLEngine.buildDeleteSqlByColumns(bean.getClass(), fileds);
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	public Long countQuery(String sql, Map<String, Object> param) throws Exception {
		return this.namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
	}

	public Long countQuery(String sql) throws Exception {
		return this.jdbcTemplate.queryForObject(sql, Long.class);
	}
	public Long listCount(Class<?> cs, ConditionDef conditionDef, Map<String, Object> paramMap) throws Exception {
		String sql = "SELECT COUNT(1) FROM " + SQLEngine.loadTableName(cs) + " ";
		Condition condition = new Condition(conditionDef, paramMap);
		sql += condition.getConditionClauseWithWhere();
		return this.countQuery(sql, paramMap);
	}

	/**
	 * 自动分页/不分页查询返回list
	 * 
	 * @param cs
	 * @param conditionDef
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> baseQueryForList(Class<?> cs, ConditionDef conditionDef,
			Map<String, Object> paramMap, Map<String, Order> orderMap) throws Exception {
		Condition condition = new Condition(conditionDef, paramMap);
		String sql = SQLEngine.buildSelectSql(cs) + condition.getConditionClauseWithWhere();
		logger.debug("Sql is {}, Param is {}", sql, paramMap.toString());
		if (null != orderMap && !orderMap.isEmpty()) {
			StringBuffer sqlBuf = new StringBuffer(sql);
			for (String key : orderMap.keySet()) {
				sqlBuf.append(" ORDER BY ").append(key).append(" ").append(orderMap.get(key).toString());
			}
			sql = sqlBuf.toString();
		}
		if (PageHelper.isPagingSearchRequest(paramMap)) {
			return PageHelper.pagingQuery(namedParameterJdbcTemplate, sql, cs, paramMap);
		} else {
			return namedParameterJdbcTemplate.queryForList(sql, paramMap);
		}
	}

	/**
	 * 查询满足条件的单条记录的实体对象，如果超过1条则抛出异常，没查询到则返回null
	 * 
	 * @param cs
	 * @param conditionDef
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Object queryForEntity(Class<?> cs, ConditionDef conditionDef, Map<String, Object> paramMap)
			throws Exception {
		Condition condition = new Condition(conditionDef, paramMap);
		String sql = SQLEngine.buildSelectSql(cs) + condition.getConditionClauseWithWhere();
		List<Map<String, Object>> list = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		if (null == list || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			throw new Exception("query record more then one!!");
		} else {
			Map<String, Object> map = (Map<String, Object>) list.get(0);
			return SQLEngine.coverMapToBean(map, cs);
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
}
