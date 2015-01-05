package easyjdbc.query.support;

import java.lang.reflect.Field;

import easyjdbc.annotation.Column;

public class DBColumn {

	private String columnName;
	private Field field;
	private Object object;

	public DBColumn(Field field, Object object) {
		this(field);
		this.object = object;
	}

	public DBColumn(Field field) {
		this.field = field;
		if (field.isAnnotationPresent(Column.class)) {
			this.columnName = field.getAnnotation(Column.class).value();
			return;
		}
		this.columnName = field.getName();
	}

	public Object getObject() {
		return object;
	}

	public String getColumnName() {
		return columnName;
	}

	public Object setObjectField(Object instance, Object parameter) {
		try {
			return instance.getClass().getMethod(setterString(field.getName()), field.getType()).invoke(instance, parameter);
		} catch (Exception e) {
			return null;
		}
	}

	public Object getInvokedObject(Object instance) {
		try {
			return instance.getClass().getMethod(getterString(field.getName()), (Class<?>[]) null).invoke(instance);
		} catch (Exception e) {
			return null;
		}
	}

	public String getterString(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public String setterString(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public void setByInstance(Object instance) {
		this.object = getInvokedObject(instance);
	}

	public boolean hasObject() {
		if (object == null)
			return false;
		return true;
	}

}
