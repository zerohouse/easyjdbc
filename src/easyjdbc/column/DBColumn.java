package easyjdbc.column;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import easyjdbc.annotation.Column;

public class DBColumn {

	private boolean otherTable = false;
	private String columnName;
	private String format;
	private Field field;
	
	public boolean isOtherTable() {
		return otherTable;
	}

	public void setOtherTable(boolean otherTable) {
		this.otherTable = otherTable;
	}

	private Object object;
	private int expectedSize = 1;

	public DBColumn(Field field) {
		this.field = field;
		columnSetting();
	}

	private void columnSetting() {
		this.columnName = field.getName();
		if (!field.isAnnotationPresent(Column.class)) {
			return;
		}
		Column column = field.getAnnotation(Column.class);
		if (!column.value().equals(""))
			this.columnName = column.value();
		format = column.valueFormat();
		if (format.equals("?"))
			return;
		Pattern pattern = Pattern.compile("[?]");
		Matcher matcher = pattern.matcher(format);
		int count = 0;
		while (matcher.find())
			count++;
		expectedSize = count;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getObject() {
		return object;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setObjectField(Object instance, Object parameter) {
		try {
			instance.getClass().getMethod(setterString(field.getName()), field.getType()).invoke(instance, parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getInvokedObject(Object instance) {
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

	public String getNameAndValue() {
		if (format == null)
			return columnName + "=?";
		if (format.equals(""))
			return columnName + "=?";
		return columnName + "=" + format;
	}

	public void addObject(List<Object> parameters) {
		for (int i = 0; i < expectedSize; i++)
			parameters.add(object);
	}

	public void setObject(Object object) {
		this.object = object;
	}

}
