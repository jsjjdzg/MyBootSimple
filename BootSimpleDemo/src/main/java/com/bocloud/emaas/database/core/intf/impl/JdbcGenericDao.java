package com.bocloud.emaas.database.core.intf.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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

import com.bocloud.emaas.database.core.BaseDao;
import com.bocloud.emaas.database.core.annotations.PK;
import com.bocloud.emaas.database.core.entity.GenericEntity;
import com.bocloud.emaas.database.core.intf.GenericDao;
import com.bocloud.emaas.database.core.meta.ColumnField;
import com.bocloud.emaas.database.core.meta.Condition;
import com.bocloud.emaas.database.core.meta.ConditionDef;
import com.bocloud.emaas.database.core.meta.GenerateStrategy;
import com.bocloud.emaas.database.engine.SQLEngine;
import com.bocloud.emaas.database.utils.Conditions;

/**
 * @author dmw
 *
 * @param <T>
 * @param <Pk>
 */
@Repository
public class JdbcGenericDao<T extends GenericEntity, Pk extends Serializable> implements GenericDao<T, Pk> {

	private final static Logger logger = LoggerFactory.getLogger(BaseDao.class);
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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

	private void setPk(Object bean, Serializable pk) throws Throwable {
		List<ColumnField> columnFieldList = SQLEngine.loadColumnField(bean.getClass());
		if (null == columnFieldList || columnFieldList.isEmpty()) {
			return;
		}
		for (ColumnField columnField : columnFieldList) {
			if (columnField.isPk()) {
				String fieldName = columnField.getFieldName();
				String setMethoName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method m = bean.getClass().getMethod(setMethoName, columnField.getType());
				m.invoke(bean, pk);
				break;
			}
		}
		return;
	}

	private void preInsert(T entity) {
		entity.setGmtCreate(new Date());
		entity.setGmtModify(new Date());
		entity.setIsDeleted(false);
		entity.setIsLocked(false);
	}

	private void preUpdate(T entity) {
		entity.setGmtModify(new Date());
		entity.setIsDeleted(false);
		if (!entity.getIsLocked()) {
			entity.setIsLocked(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#baseSave(com.bocloud.database.
	 * core.entity.GenericEntity)
	 */
	@Override
	public boolean baseSave(T entity) throws Throwable {
		this.preInsert(entity);
		String sql = SQLEngine.buildInsertSql(entity.getClass());
		SqlParameterSource sps = new BeanPropertySqlParameterSource(entity);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#save(com.bocloud.database.core.
	 * entity.GenericEntity)
	 */
	@Override
	public T save(T entity) throws Throwable {
		this.preInsert(entity);
		GenerateStrategy strategy = getPkGenStrategy(entity);
		switch (strategy) {
		case UUID:
			setPk(entity, UUID.randomUUID().toString());
			break;
		default:
			break;
		}
		final String sql = SQLEngine.buildInsertSql(entity.getClass());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(entity);
		namedParameterJdbcTemplate.update(sql, paramSource, keyHolder);
		switch (strategy) {
		case AUTO_INCREMENT:
			setPk(entity, keyHolder.getKey().longValue());
			break;
		default:
			break;
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#baseUpdate(com.bocloud.database
	 * .core.entity.GenericEntity)
	 */
	public boolean baseUpdate(T entity) throws Throwable {
		this.preUpdate(entity);
		String sql = SQLEngine.buildUpdateSql(entity.getClass());
		SqlParameterSource sps = new BeanPropertySqlParameterSource(entity);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#update(com.bocloud.database.
	 * core.entity.GenericEntity)
	 */
	@Override
	public boolean update(T entity) throws Throwable {
		this.preUpdate(entity);
		String sql = SQLEngine.buildUpdateSql(entity.getClass());
		SqlParameterSource sps = new BeanPropertySqlParameterSource(entity);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#delete(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public boolean delete(Class<T> clazz, Pk id) throws Throwable {
		String sql = SQLEngine.buildDeleteSql(clazz);
		Object bean = clazz.newInstance();
		setPk(bean, id);
		SqlParameterSource sps = new BeanPropertySqlParameterSource(bean);
		return this.namedParameterJdbcTemplate.update(sql, sps) > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#remove(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public boolean remove(Class<T> clazz, Pk id) throws Throwable {
		String tableName = SQLEngine.loadTableName(clazz);
		String sql = "update " + tableName + " set is_deleted = true , gmt_modify = :gmtModify where id = :id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("gmtModify", new Date());
		int efectedRows = this.execute(sql, params);
		return efectedRows > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#execute(java.lang.String)
	 */
	@Override
	public void execute(String sql) throws Throwable {
		this.jdbcTemplate.execute(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#execute(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public int execute(String sql, Map<String, Object> params) throws Throwable {
		return this.namedParameterJdbcTemplate.update(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#queryForEntity(java.lang.Class,
	 * com.bocloud.database.core.meta.ConditionDef, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T queryForEntity(Class<T> cs, ConditionDef conditionDef, Map<String, Object> paramMap) throws Exception {
		Condition condition = new Condition(conditionDef, paramMap);
		String sql = SQLEngine.buildSelectSql(cs) + condition.getConditionClauseWithWhere();
		List<Map<String, Object>> list = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		if (null == list || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			throw new Exception("query record more than one!!");
		} else {
			Map<String, Object> map = (Map<String, Object>) list.get(0);
			return (T) SQLEngine.coverMapToBean(map, cs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public T get(Class<T> clazz, Pk id) throws Throwable {
		String pkColumn = SQLEngine.loadSinglePk(clazz);
		if (null == pkColumn) {
			logger.error("No primary key exist or multi primay key!");
			throw new Exception("No Primary Key Exist Or MultiKey!");
		}
		ConditionDef conditionDef = Conditions.simpleCondition(pkColumn, pkColumn);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(pkColumn, id);
		return this.queryForEntity(clazz, conditionDef, paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#load(java.lang.Class,
	 * java.io.Serializable)
	 */
	@Override
	public T load(Class<T> clazz, Pk id) throws Throwable {
		String pkColumn = SQLEngine.loadSinglePk(clazz);
		if (null == pkColumn) {
			logger.error("No primary key exist or multi primay key!");
			throw new Exception("No Primary Key Exist Or MultiKey!");
		}
		ConditionDef conditionDef = Conditions.simpleCondition(pkColumn, pkColumn);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(pkColumn, id);
		return this.queryForEntity(clazz, conditionDef, paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#list(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> list(String sql) throws Throwable {
		return this.jdbcTemplate.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#list(java.lang.Class,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(Class<T> clazz, String sql) throws Throwable {
		List<Map<String, Object>> metaList = this.list(sql);
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> meta : metaList) {
			list.add((T) SQLEngine.coverMapToBean(meta, clazz));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#list(java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> list(String sql, Map<String, Object> params) throws Throwable {
		return this.namedParameterJdbcTemplate.queryForList(sql, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(Class<T> clazz, String sql, Map<String, Object> params) throws Throwable {
		List<Map<String, Object>> metaList = this.list(sql, params);
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> meta : metaList) {
			list.add((T) SQLEngine.coverMapToBean(meta, clazz));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#countQuery(java.lang.String,
	 * java.util.Map)
	 */
	public Long countQuery(String sql, Map<String, Object> param) throws Exception {
		return this.namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bocloud.database.core.intf.GenericDao#count(java.lang.Class,
	 * com.bocloud.database.core.meta.ConditionDef, java.util.Map)
	 */
	@Override
	public Long count(Class<T> clazz, ConditionDef conditionDef, Map<String, Object> params) throws Throwable {
		String sql = "SELECT COUNT(1) FROM " + SQLEngine.loadTableName(clazz) + " ";
		Condition condition = new Condition(conditionDef, params);
		sql += condition.getConditionClauseWithWhere();
		return this.countQuery(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bocloud.database.core.intf.GenericDao#queryForList(java.lang.Class,
	 * com.bocloud.database.core.meta.ConditionDef, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryForList(Class<T> clazz, ConditionDef conditionDef, Map<String, Object> paramMap)
			throws Throwable {
		Condition condition = new Condition(conditionDef, paramMap);
		String sql = SQLEngine.buildSelectSql(clazz) + condition.getConditionClauseWithWhere();
		List<Map<String, Object>> metaList = this.namedParameterJdbcTemplate.queryForList(sql, paramMap);
		if (null == metaList || metaList.isEmpty()) {
			return new ArrayList<T>();
		}
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> meta : metaList) {
			list.add((T) SQLEngine.coverMapToBean(meta, clazz));
		}
		return list;
	}
}
