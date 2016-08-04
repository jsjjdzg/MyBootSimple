package com.bocloud.emaas.database.core.intf;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.bocloud.emaas.database.core.entity.GenericEntity;
import com.bocloud.emaas.database.core.meta.ConditionDef;

/**
 * JDBCTemplate通用抽象DAO接口
 * 
 * @author dmw
 *
 * @param <T>
 * @param <PK>
 */
public interface GenericDao<T extends GenericEntity, Pk extends Serializable> {

	/**
	 * @param entity
	 * @return
	 * @throws Throwable
	 */
	public boolean baseSave(T entity) throws Throwable;

	/**
	 * 保存实体
	 * 
	 * @param entity
	 * @return 实体对象
	 */
	public T save(T entity) throws Throwable;

	/**
	 * @param entity
	 * @return
	 * @throws Throwable
	 */
	public boolean baseUpdate(T entity) throws Throwable;

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	public boolean update(T entity) throws Throwable;

	/**
	 * 根据主键删除实体
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public boolean delete(Class<T> clazz, Pk id) throws Throwable;
	
	/**
	 * 根据主键逻辑删除实体
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public boolean remove(Class<T> clazz, Pk id) throws Throwable;

	/**
	 * 执行SQL语句
	 * 
	 * @param sql
	 *            需要执行的参数
	 * @return
	 */
	public void execute(String sql) throws Throwable;

	/**
	 * 执行带参数的SQL语句
	 * 
	 * @param sql
	 *            待执行的SQL语句
	 * @param params
	 *            参数
	 * @return
	 */
	public int execute(String sql, Map<String, Object> params) throws Throwable;

	/**
	 * @param clazz
	 * @param conditionDef
	 * @param paramMap
	 * @return
	 * @throws Throwable
	 */
	public T queryForEntity(Class<T> clazz, ConditionDef conditionDef, Map<String, Object> paramMap) throws Throwable;

	/**
	 * @param clazz
	 * @param conditionDef
	 * @param paramMap
	 * @return
	 * @throws Throwable
	 */
	public List<T> queryForList(Class<T> clazz, ConditionDef conditionDef, Map<String, Object> paramMap) throws Throwable;
	/**
	 * 根据主键获取实体详细信息
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	public T get(Class<T> clazz, Pk id) throws Throwable;

	/**
	 * @param clazz
	 * @param id
	 * @return
	 */
	public T load(Class<T> clazz, Pk id) throws Throwable;

	/**
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> list(String sql) throws Throwable;

	/**
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public List<T> list(Class<T> clazz, String sql) throws Throwable;

	/**
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> list(String sql, Map<String, Object> params) throws Throwable;

	/**
	 * @param clazz
	 * @param params
	 * @return
	 */
	public List<T> list(Class<T> clazz, String sql, Map<String, Object> params) throws Throwable;

	/**
	 * @param sql
	 * @param param
	 * @return
	 * @throws Throwable
	 */
	public Long countQuery(String sql, Map<String, Object> param) throws Throwable;

	/**
	 * @param clazz
	 * @param condition
	 * @param params
	 * @return
	 */
	public Long count(Class<T> clazz, ConditionDef condition, Map<String, Object> params) throws Throwable;
}
