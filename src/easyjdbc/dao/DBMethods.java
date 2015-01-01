package easyjdbc.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

public class DBMethods {

	private DBMethods() {
	}

	public static boolean insertIfExistUpdate(Object record) {
		List<Field> fields = excludeNotThisDB(record.getClass());
		String tableName = record.getClass().getAnnotation(Table.class).value();
		String valueString = "";
		String fieldsString = "";
		DAO dao = DAOfactory.getDAO();
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
		String sql = "insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ")";
		String updateString = addParams(record, dao);
		sql += " on duplicate key update " + updateString;

		return dao.doQuery(sql);

	}



	public static boolean insertIfNotExist(Object record) {
		Table anotation = record.getClass().getAnnotation(Table.class);
		String tableName = anotation.value();
		List<Field> fields = excludeNotThisDB(record.getClass());

		DAO dao = DAOfactory.getDAO();
		Object param;

		String valueString = "";
		String fieldsString = "";
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
		String sql = "insert ignore into " + tableName + " (" + fieldsString + ") values(" + valueString + ")";

		return dao.doQuery(sql);
	}

	public static boolean insert(Object... records) {
		DAO dao = DAOfactory.getDAO();
		Object param;
		String valueString;
		String fieldsString;
		String tableName;
		String sql;
		List<String> sqls = new ArrayList<String>();
		List<Field> fields;
		List<Object> params;
		List<List<Object>> parameterArrays = new ArrayList<List<Object>>();
		for (int j = 0; j < records.length; j++) {
			params = new ArrayList<Object>();
			valueString = "";
			fieldsString = "";
			tableName = records[j].getClass().getAnnotation(Table.class).value();
			fields = excludeNotThisDB(records[j].getClass());

			for (int i = 0; i < fields.size(); i++) {
				try {
					param = getFieldObject(fields.get(i).getName(), records[j]);
					if (param != null) {
						fieldsString += fields.get(i).getName() + ",";
						valueString += "?,";
						params.add(param);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			fieldsString = fieldsString.substring(0, fieldsString.length() - 1);
			valueString = valueString.substring(0, valueString.length() - 1);
			sql = "insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ")";
			sqls.add(sql);
			parameterArrays.add(params);
		}
		return dao.doQueries(sqls, parameterArrays);
	}

	public static Object insertAndGetPrimaryKey(Object record) {
		Table anotation = record.getClass().getAnnotation(Table.class);
		String tableName = anotation.value();
		List<Field> fields = excludeNotThisDB(record.getClass());

		String fieldsString = "";
		String valueString = "";
		Object param;
		ArrayList<Object> params = new ArrayList<Object>();

		for (int i = 0; i < fields.size(); i++) {
			try {
				param = getFieldObject(fields.get(i).getName(), record);
				if (param != null) {
					fieldsString += fields.get(i).getName() + ",";
					valueString += "?,";
					params.add(param);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		fieldsString = fieldsString.substring(0, fieldsString.length() - 1);
		valueString = valueString.substring(0, valueString.length() - 1);

		List<String> sqls = new ArrayList<String>();
		sqls.add("insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ");");
		sqls.add("SELECT LAST_INSERT_ID();");
		List<List<Object>> parameterArrays = new ArrayList<List<Object>>();
		parameterArrays.add(params);
		parameterArrays.add(null);
		DAO dao = new DAOjdbc();
		
		return dao.doQueriesAndReturnLast(sqls, parameterArrays, 1).get(0);
	}

	public static <T> List<T> getList(Class<T> cLass) {
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = DAOfactory.getDAO();
		String defaultCondition = table.defaultCondition();
		String sql = "select * from " + table.value();

		if (defaultCondition != null)
			sql += " where " + table.defaultCondition();

		return getList(cLass, sql ,dao);
	}

	public static <T> List<T> getList(Class<T> cLass, String condition, Object... parameters) {
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = DAOfactory.getDAO();
		String defaultCondition = table.defaultCondition();
		String sql = "select * from " + table.value();

		if (condition != null)
			sql += " where " + condition;
		else if (defaultCondition != null)
			sql += " where " + table.defaultCondition();

		for (int i = 0; i < parameters.length; i++) {
			dao.addParameter(parameters[i]);
		}
		return getList(cLass, sql, dao);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> getList(Class<T> cLass, String sql, DAO dao) {
		List<T> result = new ArrayList<T>();
		List<Field> excludesFields = excludeNotThisDB(cLass);
		List<List<Object>> sqlArray = dao.getRecords(sql, excludesFields.size());
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
		DAO dao = DAOfactory.getDAO();
		String sql = "select * from " + anotation.value() + " where " + primaryField.getCondition();
		dao.addParameter(primaryKey);
		return gets(cLass, sql, dao);
	}

	public static <T> T getMoreCondition(Class<T> cLass, String condition, Object... parameters) {
		Table anotation = cLass.getAnnotation(Table.class);
		DAO dao = DAOfactory.getDAO();
		String sql = "select * from " + anotation.value();
		if (condition != null)
			sql += " where " + condition;
		for (int i = 0; i < parameters.length; i++)
			dao.addParameter(parameters[i]);
		return gets(cLass, sql, dao);
	}

	@SuppressWarnings("unchecked")
	private static <T> T gets(Class<T> cLass, String sql, DAO dao) {
		List<Field> excludesFields = excludeNotThisDB(cLass);
		List<Object> record = dao.getRecord(sql, excludesFields.size());
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

	public static boolean update(Object record, String whereClause) {
		DAO dao = DAOfactory.getDAO();
		String tableName = record.getClass().getAnnotation(Table.class).value();
		PrimaryFields primaryField = getPrimaryField(record.getClass());
		Object[] primaryKey = primaryField.getParams(record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where " + primaryField.getCondition();
		dao.addParameter(primaryKey);

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		return dao.doQuery(sql);
	}

	public static boolean update(Object record) {
		DAO dao = DAOfactory.getDAO();
		String tableName = record.getClass().getAnnotation(Table.class).value();
		PrimaryFields primaryField = getPrimaryField(record.getClass());
		Object[] primaryKey = primaryField.getParams(record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where " + primaryField.getCondition();
		dao.addParameter(primaryKey);
		return dao.doQuery(sql);
	}

	public static boolean delete(Class<?> cLass, String whereClause, Object... obj) {
		DAO dao = DAOfactory.getDAO();
		String tableName = cLass.getAnnotation(Table.class).value();

		String sql = "delete from " + tableName + " where " + whereClause;

		for (int i = 0; i < obj.length; i++)
			dao.addParameter(obj[i]);
		return dao.doQuery(sql);
	}

	public static boolean delete(Object record, String whereClause) {
		DAO dao = DAOfactory.getDAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		PrimaryFields primaryField = getPrimaryField(cLass);
		Object[] primaryKey = primaryField.getParams(record);

		String sql = "delete from " + tableName + " where " + primaryField.getCondition();

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		dao.addParameter(primaryKey);
		return dao.doQuery(sql);
	}

	public static boolean delete(Object record) {
		DAO dao = DAOfactory.getDAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		PrimaryFields primaryField = getPrimaryField(cLass);
		Object[] primaryKey = primaryField.getParams(record);

		String sql = "delete from " + tableName + " where " + primaryField.getCondition();

		dao.addParameter(primaryKey);
		return dao.doQuery(sql);
	}

	private static String addParams(Object field, DAO dao) {
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
					dao.addParameter(param);
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
			System.out.println(fieldName + record);
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
