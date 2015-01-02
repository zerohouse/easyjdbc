package easyjdbc.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

public class MakeSql {

	private MakeSql() {
		
	}

	public static SqlHolder getInsertSQLholder(Object record, boolean ifNotExistIgnore) {
		Table anotation = record.getClass().getAnnotation(Table.class);
		String tableName = anotation.value();
		List<Field> fields = excludeNotThisDB(record.getClass());
		String valueString = "";
		String fieldsString = "";
		SqlHolder sqlholder = new SqlHolder();
		sqlholder.setReturnType(SqlHolder.RETURN_NULL);
		Object param;
		for (int i = 0; i < fields.size(); i++) {
			try {
				param = getFieldObject(fields.get(i).getName(), record);
				if (param != null) {
					fieldsString += fields.get(i).getName() + ",";
					valueString += "?,";
					sqlholder.addParameters(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fieldsString = fieldsString.substring(0, fieldsString.length() - 1);
		valueString = valueString.substring(0, valueString.length() - 1);
		if (ifNotExistIgnore) {
			sqlholder.setSql("insert ignore into " + tableName + " (" + fieldsString + ") values(" + valueString + ")");
			return sqlholder;
		}
		sqlholder.setSql("insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ")");
		return sqlholder;
	}


	public static SqlHolder getListSQLholder(Class<?> cLass, String condition, Object... parameters) {
		Table table = cLass.getAnnotation(Table.class);
		String sql = "select * from " + table.value();
		SqlHolder sqlholder = new SqlHolder();
		if (condition != null)
			sql += " where " + condition;
		int resultSize = excludeNotThisDB(cLass).size();
		sqlholder.setSql(sql);
		sqlholder.setReturnType(SqlHolder.RETURN_RECORDS);
		sqlholder.setResultSize(resultSize);
		for (int i = 0; i < parameters.length; i++) {
			sqlholder.addParameters(parameters[i]);
		}
		return sqlholder;
	}
	
	public static SqlHolder getSQLholder(Class<?> cLass, String condition, Object... parameters) {
		Table table = cLass.getAnnotation(Table.class);
		String sql = "select * from " + table.value();
		SqlHolder sqlholder = new SqlHolder();
		if (condition != null)
			sql += " where " + condition;
		int resultSize = excludeNotThisDB(cLass).size();
		sqlholder.setSql(sql);
		sqlholder.setReturnType(SqlHolder.RETURN_SINGLE_RECORD);
		sqlholder.setResultSize(resultSize);
		for (int i = 0; i < parameters.length; i++) {
			sqlholder.addParameters(parameters[i]);
		}
		return sqlholder;
	}
	
	public static SqlHolder getByKeySQLholder(Class<?> cLass) {
		
		Table anotation = cLass.getAnnotation(Table.class);
		PrimaryFields primaryField = getPrimaryField(cLass);
		String sql = "select * from " + anotation.value() + " where " + primaryField.getCondition();
		List<Object> params = new ArrayList<Object>();
		params.add(primaryKey);
		return gets(cLass, sql, params);
		
		
		Table table = cLass.getAnnotation(Table.class);
		String sql = "select * from " + table.value();
		SqlHolder sqlholder = new SqlHolder();
		if (condition != null)
			sql += " where " + condition;
		int resultSize = excludeNotThisDB(cLass).size();
		sqlholder.setSql(sql);
		sqlholder.setReturnType(SqlHolder.RETURN_SINGLE_RECORD);
		sqlholder.setResultSize(resultSize);
		for (int i = 0; i < parameters.length; i++) {
			sqlholder.addParameters(parameters[i]);
		}
		return sqlholder;
	}
	

	@SuppressWarnings("unchecked")
	private static <T> List<T> getList(Class<T> cLass, String sql, List<Object> params) {
		List<T> result = new ArrayList<T>();
		DAO dao = DAOfactory.getDAO();
		List<Field> excludesFields = excludeNotThisDB(cLass);
		List<List<Object>> sqlArray = dao.getRecords(sql, excludesFields.size(), params);
		Iterator<List<Object>> iterator = sqlArray.iterator();
		Object eachInstance;
		List<Object> next;
		while (iterator.hasNext()) {
			try {
				next = iterator.next();
				if (next.size() == 0)
					continue;
				eachInstance = setRecords(cLass, excludesFields, next);
				result.add((T) eachInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.size() == 0)
			return null;
		return result;
	}

	public static <T> T get(Class<T> cLass, Object... primaryKey) {
		Table anotation = cLass.getAnnotation(Table.class);
		PrimaryFields primaryField = getPrimaryField(cLass);
		String sql = "select * from " + anotation.value() + " where " + primaryField.getCondition();
		List<Object> params = new ArrayList<Object>();
		params.add(primaryKey);
		return gets(cLass, sql, params);
	}

	public static <T> T getMoreCondition(Class<T> cLass, String condition, Object... parameters) {
		Table anotation = cLass.getAnnotation(Table.class);
		String sql = "select * from " + anotation.value();
		List<Object> params = new ArrayList<Object>();
		if (condition != null)
			sql += " where " + condition;
		for (int i = 0; i < parameters.length; i++)
			params.add(parameters[i]);
		return gets(cLass, sql, params);
	}

	@SuppressWarnings("unchecked")
	private static <T> T gets(Class<T> cLass, String sql, List<Object> params) {
		List<Field> excludesFields = excludeNotThisDB(cLass);
		DAO dao = DAOfactory.getDAO();
		List<Object> record = dao.getRecord(sql, excludesFields.size(), params);
		Object eachInstance;
		if (record.size() == 0)
			return null;
		try {
			eachInstance = setRecords(cLass, excludesFields, record);
			return (T) eachInstance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static <T> Object setRecords(Class<T> cLass, List<Field> excludedFields, List<Object> next) throws InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object eachInstance;
		eachInstance = cLass.getConstructor().newInstance();
		List<Class<?>> fieldTypes = getFieldsType(cLass);
		for (int i = 0; i < excludedFields.size(); i++) {
			cLass.getMethod(setterString(excludedFields.get(i).getName()), fieldTypes.get(i)).invoke(eachInstance, next.get(i));
		}

		return eachInstance;
	}

	public static boolean update(Object... record) {
		DAO dao = DAOfactory.getDAO();
		String tableName;
		PrimaryFields primaryField;
		Object[] primaryKey;
		String fieldsString;
		List<List<Object>> parameterArrays = new ArrayList<List<Object>>();
		List<Object> params;
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < record.length; i++) {
			params = new ArrayList<Object>();
			tableName = record[i].getClass().getAnnotation(Table.class).value();
			primaryField = getPrimaryField(record[i].getClass());
			primaryKey = primaryField.getParams(record[i]);
			fieldsString = addParams(record[i], params);

			sqls.add("update " + tableName + " set " + fieldsString + " where " + primaryField.getCondition());

			addToArrayList(primaryKey, params);
			parameterArrays.add(params);
		}
		return dao.doQueries(sqls, parameterArrays);
	}

	private static void addToArrayList(Object[] array, List<Object> params) {
		for (int i = 0; i < array.length; i++) {
			params.add(array[i]);
		}
	}

	public static boolean deleteWhere(Class<?> cLass, String whereClause, Object... obj) {
		DAO dao = DAOfactory.getDAO();
		String tableName = cLass.getAnnotation(Table.class).value();

		String sql = "delete from " + tableName + " where " + whereClause;
		List<Object> params = new ArrayList<Object>();
		addToArrayList(obj, params);
		return dao.doQuery(sql, params);
	}

	public static boolean delete(Object... record) {
		DAO dao = DAOfactory.getDAO();
		Class<?> cLass;
		String tableName;
		PrimaryFields primaryField;
		Object[] primaryKey;
		List<List<Object>> parameterArray = new ArrayList<List<Object>>();
		List<Object> params;
		List<String> sqls = new ArrayList<String>();
		for (int i = 0; i < record.length; i++) {
			params = new ArrayList<Object>();
			cLass = record[i].getClass();
			tableName = cLass.getAnnotation(Table.class).value();
			primaryField = getPrimaryField(cLass);
			primaryKey = primaryField.getParams(record[i]);
			addToArrayList(primaryKey, params);
			System.out.println(params);
			sqls.add("delete from " + tableName + " where " + primaryField.getCondition());
			parameterArray.add(params);

		}
		return dao.doQueries(sqls, parameterArray);
	}

	private static String addParams(Object field, List<Object> params) {
		String fieldsString = "";
		List<Field> fields = excludeNotThisDB(field.getClass());
		Object param;
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isAnnotationPresent(Key.class))
				continue;
			try {
				param = getFieldObject(fields.get(i).getName(), field);
				if (param != null) {
					fieldsString += fields.get(i).getName() + "=?, ";
					params.add(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fieldsString = fieldsString.substring(0, fieldsString.length() - 2);
		return fieldsString;
	}

	private static PrimaryFields getPrimaryField(Class<?> cLass) {
		List<Field> fields = excludeNotThisDB(cLass);
		PrimaryFields result = new PrimaryFields(fields);
		return result;
	}

	static Object getFieldObject(String fieldName, Object record) {
		try {
			return record.getClass().getMethod(getterString(fieldName), (Class<?>[]) null).invoke(record);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getterString(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	private static String setterString(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	private static List<Field> excludeNotThisDB(Class<?> cLass) {
		List<Field> result = new ArrayList<Field>();
		Field[] fields = cLass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].isAnnotationPresent(Exclude.class))
				result.add(fields[i]);
		}
		return result;
	}

	private static List<Class<?>> getFieldsType(Class<?> cLass) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		Field[] fields = cLass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].isAnnotationPresent(Exclude.class))
				result.add(fields[i].getType());
		}
		return result;
	}

}
