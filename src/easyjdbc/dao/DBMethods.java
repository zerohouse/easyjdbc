package easyjdbc.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

public class DBMethods {

	private DBMethods() {
	}

	public static boolean insert(Object record) {
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
		dao.setSql("insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ")");

		return dao.doQuery();
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

		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = DAO.getConnection();
			pstmt = conn.prepareStatement("insert into " + tableName + " (" + fieldsString + ") values(" + valueString + ");");
			DAO.setParameters(params, pstmt);
			pstmt.execute();
			pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID();");
			rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getObject(1);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException sqle) {
				}
			if (rs != null)
				try {
					conn.close();
				} catch (SQLException sqle) {
				}
		}
		return rs;
	}

	public static <T> List<T> getList(Class<T> cLass) {
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = new DAO();
		String defaultCondition = table.defaultCondition();
		String sql = "select * from " + table.value();

		if (defaultCondition != null)
			sql += " where " + table.defaultCondition();

		dao.setSql(sql);
		return getList(cLass, dao);
	}

	public static <T> List<T> getList(Class<T> cLass, String condition, Object... parameters) {
		Table table = cLass.getAnnotation(Table.class);
		DAO dao = new DAO();
		String defaultCondition = table.defaultCondition();
		String sql = "select * from " + table.value();

		if (condition != null)
			sql += " where " + condition;
		else if (defaultCondition != null)
			sql += " where " + table.defaultCondition();

		dao.setSql(sql);
		for (int i = 0; i < parameters.length; i++) {
			dao.addParameter(parameters[i]);
		}
		return getList(cLass, dao);
	}

	@SuppressWarnings("unchecked")
	private static <T> List<T> getList(Class<T> cLass, DAO dao) {
		List<T> result = new ArrayList<T>();
		List<Field> excludesFields = excludeNotThisDB(cLass);
		dao.setResultSize(excludesFields.size());
		ArrayList<ArrayList<Object>> sqlArray = dao.getRecords();
		Iterator<ArrayList<Object>> iterator = sqlArray.iterator();
		Object eachInstance;
		ArrayList<Object> next;
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

	public static <T> T get(Class<T> cLass, Object primaryKey) {
		Table anotation = cLass.getAnnotation(Table.class);
		Field primaryField = getPrimaryField(cLass);
		DAO dao = new DAO();
		String sql = "select * from " + anotation.value() + " where " + primaryField.getName() + "=?";
		dao.setSql(sql);
		dao.addParameter(primaryKey);
		return get(cLass, dao);
	}

	public static <T> T get(Class<T> cLass, String condition, Object... parameters) {
		Table anotation = cLass.getAnnotation(Table.class);
		DAO dao = new DAO();
		String sql = "select * from " + anotation.value();
		if (condition != null)
			sql += " where " + condition;
		dao.setSql(sql);
		for (int i = 0; i < parameters.length; i++)
			dao.addParameter(parameters[i]);
		return get(cLass, dao);
	}

	@SuppressWarnings("unchecked")
	private static <T> T get(Class<T> cLass, DAO dao) {
		List<Field> excludesFields = excludeNotThisDB(cLass);
		dao.setResultSize(excludesFields.size());
		ArrayList<Object> record = dao.getRecord();
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

	private static <T> Object setRecords(Class<T> cLass, List<Field> excludedFields, ArrayList<Object> record) throws InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object eachInstance;
		eachInstance = cLass.getConstructor().newInstance();
		List<Class<?>> fieldTypes = getFieldsType(cLass);
		for (int i = 0; i < excludedFields.size(); i++) {
			cLass.getMethod(setterString(excludedFields.get(i).getName()), fieldTypes.get(i)).invoke(eachInstance, record.get(i));
		}

		return eachInstance;
	}

	public static boolean update(Object record, String whereClause) {
		DAO dao = new DAO();
		String tableName = record.getClass().getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(record.getClass());
		Object primaryKey = getFieldObject(primaryField.getName(), record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where " + primaryField.getName() + "=?";
		dao.addParameter(primaryKey);

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		dao.setSql(sql);
		return dao.doQuery();
	}

	public static boolean update(Object record) {
		DAO dao = new DAO();
		String tableName = record.getClass().getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(record.getClass());
		Object primaryKey = getFieldObject(primaryField.getName(), record);
		String fieldsString = addParams(record, dao);

		String sql = "update " + tableName + " set " + fieldsString + " where " + primaryField.getName() + "=?";
		dao.addParameter(primaryKey);

		dao.setSql(sql);
		return dao.doQuery();
	}

	public static boolean delete(Object record, String whereClause) {
		DAO dao = new DAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(cLass);
		Object primaryKey = getFieldObject(primaryField.getName(), record);

		String sql = "delete from " + tableName + " where " + primaryField.getName() + "=?";

		if (whereClause != null) {
			sql += " and " + whereClause;
		}

		dao.setSql(sql);
		dao.addParameter(primaryKey);
		return dao.doQuery();
	}

	public static boolean delete(Object record) {
		DAO dao = new DAO();
		Class<?> cLass = record.getClass();
		String tableName = cLass.getAnnotation(Table.class).value();
		Field primaryField = getPrimaryField(cLass);
		Object primaryKey = getFieldObject(primaryField.getName(), record);

		String sql = "delete from " + tableName + " where " + primaryField.getName() + "=?";

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
