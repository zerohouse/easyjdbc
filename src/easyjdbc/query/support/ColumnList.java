package easyjdbc.query.support;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

public class ColumnList {
	private List<DBColumn> keys = new ArrayList<DBColumn>();
	private List<DBColumn> columns = new ArrayList<DBColumn>();

	private Class<?> type;

	public String getTableName() {
		return tableName;
	}

	private String tableName;

	public static final int ALL = 0;
	public static final int KEY = 1;
	public static final int COLUMN = 2;

	public ColumnList(Object instance, int phase) {
		Class<?> cLass = instance.getClass();
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			DBColumn dbCol = new DBColumn(fields[i], phase);
			dbCol.setByInstance(instance);
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(dbCol);
				continue;
			}
			columns.add(dbCol);
		}
	}

	public ColumnList(Class<?> cLass, int phase) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i], phase));
				continue;
			}
			columns.add(new DBColumn(fields[i], phase));
		}
	}

	public ColumnList(Class<?> cLass, int phase, Object... primaryKey) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		int j = 0;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i], phase, primaryKey[j]));
				j++;
				continue;
			}
			columns.add(new DBColumn(fields[i], phase));
		}
	}

	public String getJoinedName(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getColumnName() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String getNameAndValue(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getNameAndValue() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String addAndGetString(int type, List<Object> parameters, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			DBColumn column = list.get(i);
			if (column.hasObject())
				result += column.getNameAndValue() + delimiter;
			column.addObject(parameters);
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public void addParameters(int type, List<Object> parameters) {
		List<DBColumn> list = typeDefine(type);
		for (int i = 0; i < list.size(); i++) {
			DBColumn column = list.get(i);
			column.addObject(parameters);
		}
	}

	public Object getObject(int type, ResultSet rs) {
		List<DBColumn> list = typeDefine(type);
		Object instance;
		try {
			instance = this.type.getConstructor().newInstance();
			for (int i = 0; i < list.size(); i++) {
				DBColumn column = list.get(i);
				column.setObjectField(instance, rs.getObject(column.getColumnName()));
			}
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<DBColumn> typeDefine(int type) {
		List<DBColumn> list;
		switch (type) {
		case ALL:
			list = new ArrayList<DBColumn>();
			list.addAll(keys);
			list.addAll(columns);
			break;
		case KEY:
			list = keys;
			break;
		case COLUMN:
			list = columns;
			break;
		default:
			list = columns;
		}
		return list;
	}

	public Class<?> getType() {
		return null;
	}

}
