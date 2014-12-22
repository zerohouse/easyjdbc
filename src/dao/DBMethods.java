package dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import annotation.Exclude;
import annotation.Key;
import annotation.Table;

public class DBMethods {

	private DBMethods() {
	}

	public static boolean insert(Record record) {
		Table anotation = record.getClass().getAnnotation(Table.class);
		String tableName = anotation.value();
		List<Field> fields = excludeNotThisDB(record.getClass());

		DAO dao = new DAO();
		String fieldsString = "";
		String valueString = "";
		Object param;

		for (int i = 0; i < fields.size(); i++) {
			try {
				param = getFieldObject(fields.get(i).getName(), record);
				if (param != null) {
					fieldsString += fields.get(i).getName() + ",";
					valueString += "?,";
					dao.addParameter(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		fieldsString = fieldsString.substring(0, fieldsString.length() - 1);
		valueString = valueString.substring(0, valueString.length() - 1);
		dao.setSql("insert into " + tableName + " (" + fieldsString
				+ ") values(" + valueString + ")");

		return dao.doQuery();
	}

	public static List<Record> getList(Class<?> cLass) {
		List<Record> result = new ArrayList<Record>();
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = new DAO();
		String sql = "select * from " + table.value() + " " + table.defaultCondition();
		dao.setSql(sql);
		dao.setResultSize(excludeNotThisDB(cLass).size());
		ArrayList<ArrayList<Object>> sqlArray = dao.getRecords();
		Iterator<ArrayList<Object>> iterator = sqlArray.iterator();
		Object eachInstance;
		ArrayList<Object> next;
		while (iterator.hasNext()) {
			try {
				next = iterator.next();
				if (next.size() == 0)
					continue;
				eachInstance = cLass.getConstructor().newInstance();
				cLass.getMethod("set", Object[].class).invoke(eachInstance, (Object) next.toArray());
				result.add((Record) eachInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(result.size()==0)
			return null;
		return result;
	}

	public static List<Record> getList(Class<?> cLass,
			String condition) {
		List<Record> result = new ArrayList<Record>();
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = new DAO();
		String sql = "select * from " + table.value();
		if (condition != null)
			sql += " " + condition;
		dao.setSql(sql);
		dao.setResultSize(excludeNotThisDB(cLass).size());
		ArrayList<ArrayList<Object>> sqlArray = dao.getRecords();
		Iterator<ArrayList<Object>> iterator = sqlArray.iterator();
		Object eachInstance;
		ArrayList<Object> next;
		while (iterator.hasNext()) {
			try {
				next = iterator.next();
				if (next.size() == 0)
					continue;
				eachInstance = cLass.getConstructor().newInstance();
				cLass.getMethod("set", Object[].class).invoke(eachInstance, (Object) next.toArray());
				result.add((Record) eachInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(result.size()==0)
			return null;
		return result;
	}

	public static <T> Object get(Class<?> cLass, Object primaryKey) {
		Table anotation = cLass.getAnnotation(Table.class);
		Field primaryField = getPrimaryField(cLass);
		DAO dao = new DAO();
		dao.setSql("select * from " + anotation.value() + " where "
				+ primaryField.getName() + "=?");
		dao.addParameter(primaryKey);
		dao.setResultSize(excludeNotThisDB(cLass).size());
		ArrayList<Object> record = dao.getRecord();
		Object eachInstance;
		if (record.size() == 0)
			return null;
		try {
			eachInstance = cLass.getConstructor().newInstance();
			cLass.getMethod("set", Object[].class).invoke(eachInstance, (Object) record.toArray());
			return cLass.cast(eachInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean update(Record record, String whereClause) {
		DAO dao = new DAO();
		String tableName = record.getClass().getAnnotation(Table.class)
				.value();
		Field primaryField = getPrimaryField(record.getClass());
		Object primaryKey = getFieldObject(primaryField.getName(), record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where "
				+ primaryField.getName() + "=?";
		dao.addParameter(primaryKey);

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		dao.setSql(sql);
		return dao.doQuery();
	}

	public static boolean update(Record record) {
		DAO dao = new DAO();
		String tableName = record.getClass().getAnnotation(Table.class)
				.value();
		Field primaryField = getPrimaryField(record.getClass());
		Object primaryKey = getFieldObject(primaryField.getName(), record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where "
				+ primaryField.getName() + "=?";
		dao.addParameter(primaryKey);

		dao.setSql(sql);
		return dao.doQuery();
	}

	public static boolean delete(Record record, String whereClause) {
		DAO dao = new DAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(cLass);
		Object primaryKey = getFieldObject(primaryField.getName(), record);

		String sql = "delete from " + tableName + " where "
				+ primaryField.getName() + "=?";

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		dao.setSql(sql);
		dao.addParameter(primaryKey);
		return dao.doQuery();
	}

	public static boolean delete(Record record) {
		DAO dao = new DAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(cLass);
		Object primaryKey = getFieldObject(primaryField.getName(), record);

		String sql = "delete from " + tableName + " where "
				+ primaryField.getName() + "=?";

		dao.setSql(sql);
		dao.addParameter(primaryKey);
		return dao.doQuery();
	}

	private static String addParams(Object field, DAO dao) {
		String fieldsString = "";
		List<Field> fields = excludeNotThisDB(field.getClass());
		Object param;
		for (int i = 0; i < fields.size(); i++) {
			try {
				param = getFieldObject(fields.get(i).getName(), field);
				if (param != null) {
					fieldsString += fields.get(i).getName() + "=?, ";
					dao.addParameter(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fieldsString = fieldsString.substring(0, fieldsString.length() - 2);
		return fieldsString;
	}

	private static Field getPrimaryField(Class<?> cLass) {
		List<Field> fields = excludeNotThisDB(cLass);
		int columnSize = fields.size();
		for (int i = 0; i < columnSize; i++)
			if (fields.get(i).isAnnotationPresent(Key.class))
				return fields.get(i);
		return null;
	}

	private static Object getFieldObject(String fieldName, Object record) {
		try {
			return record.getClass()
					.getMethod(getterString(fieldName), (Class<?>[]) null)
					.invoke(record);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getterString(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);
	}

	private static List<Field> excludeNotThisDB(Class<?> cLass) {
		List<Field> result = new ArrayList<Field>();
		Field[] fields = cLass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].isAnnotationPresent(Exclude.class)) {
				result.add(fields[i]);
			}
		}
		return result;
	}

}
