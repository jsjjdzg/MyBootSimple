package com.bocloud.emaas.database.core.meta;

/**
 * 数据库字段和实体类之间的成员变量关系类
 * @author dmw
 *
 */
public class ColumnField {
	// java字段名
	private String fieldName;
	// 数据库字段名
	private String columnName;
	// 是否是主键
	private boolean isPk = false;
	// update时是否需要更新
	private boolean isUpdate = true;
	// insert时是否需要插入
	private boolean isInsert = true;

	private Class<?> type;

	public boolean isPk() {
		return isPk;
	}

	public void setIsPk(boolean isPk) {
		this.isPk = isPk;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isInsert() {
		return isInsert;
	}

	public void setIsInsert(boolean isInsert) {
		this.isInsert = isInsert;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

}