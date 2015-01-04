package easyjdbc.query;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.annotation.Table;
import easyjdbc.setting.Setting;

public class QueryExecuter {

	public static Connection getConnection() {
		Connection con = null;
		String url = Setting.get(Setting.URL);
		String id = Setting.get(Setting.ID);
		String pw = Setting.get(Setting.PASSWORD);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	Connection conn;

	public QueryExecuter() {
		conn = getConnection();
	}

	public List<Object> executeQuries(Query... sqls) {
		try {
			List<Object> results = new ArrayList<Object>();
			for (int i = 0; i < sqls.length; i++) {
				results.add(sqls[i].execute(conn));
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public Object execute(Query sql) {
		try {
			return sql.execute(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException sqle) {
			}
	}

	public int insertIfExistUpdate(Object... record) {
		int doneQueries = 0;
		Query query;
		for (int i = 0; i < record.length; i++) {
			query = QueryFactory.insertIfExistUpdate(record[i]);
			if ((boolean) execute(query))
				doneQueries++;
		}
		return doneQueries;
	}

	public int insertIfNotExist(Object... records) {
		int doneQueries = 0;
		Query query;
		for (int i = 0; i < records.length; i++) {
			query = QueryFactory.insertIfNotExistIgnore(records[i]);
			if ((boolean) execute(query))
				doneQueries++;
		}
		return doneQueries;
	}

	public int insert(Object... records) {
		int doneQueries = 0;
		Query query;
		for (int i = 0; i < records.length; i++) {
			query = QueryFactory.getInsertQuery(records[i]);
			if ((boolean) execute(query))
				doneQueries++;
		}
		return doneQueries;
	}

	public int update(Object... records) {
		int doneQueries = 0;
		Query query;
		for (int i = 0; i < records.length; i++) {
			query = QueryFactory.getUpdateQuery(records[i]);
			if ((boolean) execute(query))
				doneQueries++;
		}
		return doneQueries;
	}

	public int delete(Object... records) {
		int doneQueries = 0;
		Query query;
		for (int i = 0; i < records.length; i++) {
			query = QueryFactory.getDeleteQuery(records[i]);
			if ((boolean) execute(query))
				doneQueries++;
		}
		return doneQueries;
	}

	public boolean delete(Class<?> cLass, String WhereClause, Object... parameters) {
		ExecuteQuery exe = new ExecuteQuery("delete from " + cLass.getAnnotation(Table.class).value() + " where " + WhereClause, null);
		for (int i = 0; i < parameters.length; i++) {
			exe.addParameters(parameters[i]);
		}
		return (boolean) execute(exe);
	}

	@SuppressWarnings("unchecked")
	public Object insertAndGetPrimaryKey(Object record) {
		Query insert = QueryFactory.getInsertQuery(record);
		Query getPrimaryKey = new GetRecordQuery(1, "SELECT LAST_INSERT_ID();");
		if (!(boolean) execute(insert))
			return null;
		return ((List<Object>) execute(getPrimaryKey)).get(0);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> cLass) {
		List<T> result = new ArrayList<T>();
		Query query = QueryFactory.getRecordsQuery(cLass, null);
		List<List<Object>> records = (List<List<Object>>) execute(query);
		List<Field> fields = QueryFactory.excludeNotThisDB(cLass);
		records.forEach(record -> {
			Object eachInstance;
			try {
				eachInstance = cLass.getConstructor().newInstance();
				for (int i = 0; i < record.size(); i++) {
					String methodName = setterString(fields.get(i).getName());
					cLass.getMethod(methodName, fields.get(i).getType()).invoke(eachInstance, record.get(i));
				}
				result.add((T) eachInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getList(Class<T> cLass, String condition, Object... parameters) {
		List<T> result = new ArrayList<T>();
		Query query = QueryFactory.getRecordsQuery(cLass, condition, parameters);
		List<List<Object>> records = (List<List<Object>>) execute(query);
		List<Field> fields = QueryFactory.excludeNotThisDB(cLass);
		records.forEach(record -> {
			Object eachInstance;
			try {
				eachInstance = cLass.getConstructor().newInstance();
				for (int i = 0; i < record.size(); i++) {
					String methodName = setterString(fields.get(i).getName());
					cLass.getMethod(methodName, fields.get(i).getType()).invoke(eachInstance, record.get(i));
				}
				result.add((T) eachInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> cLass, Object... primaryKey) {
		Query query = QueryFactory.getRecordQuery(cLass, primaryKey);
		Object eachInstance = null;
		List<Object> records = (List<Object>) execute(query);
		List<Field> fields = QueryFactory.excludeNotThisDB(cLass);
		if (records.size() == 0)
			return null;
		try {
			eachInstance = cLass.getConstructor().newInstance();
			for (int i = 0; i < fields.size(); i++) {
				String methodName = setterString(fields.get(i).getName());
				cLass.getMethod(methodName, fields.get(i).getType()).invoke(eachInstance, records.get(i));
			}
		} catch (Exception e) {
		}
		return (T) eachInstance;
	}

	@SuppressWarnings("unchecked")
	public <T> T getWhere(Class<T> cLass, String WhereClause, Object... keys) {
		Query query = QueryFactory.getRecordQuery(cLass);
		Object eachInstance = null;
		List<Object> records = (List<Object>) execute(query);
		List<Field> fields = QueryFactory.excludeNotThisDB(cLass);
		if (WhereClause != null) {
			query.addSql(" where " + WhereClause);
		}
		for (int i = 0; i < keys.length; i++) {
			query.addParameters(keys[i]);
		}
		if (records.size() == 0)
			return null;
		try {
			eachInstance = cLass.getConstructor().newInstance();
			for (int i = 0; i < fields.size(); i++) {
				String methodName = setterString(fields.get(i).getName());
				cLass.getMethod(methodName, fields.get(i).getType()).invoke(eachInstance, records.get(i));
			}
		} catch (Exception e) {
		}
		return (T) eachInstance;
	}

	private static String setterString(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

}
