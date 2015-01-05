package easyjdbc.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import easyjdbc.query.execute.DeleteQuery;
import easyjdbc.query.execute.DeleteWhereQuery;
import easyjdbc.query.execute.InsertQuery;
import easyjdbc.query.execute.UpdateQuery;
import easyjdbc.query.raw.GetRecordQuery;
import easyjdbc.query.raw.GetRecordsQuery;
import easyjdbc.query.select.ListQuery;
import easyjdbc.query.select.SelectQuery;
import easyjdbc.query.select.SelectWhereQuery;
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

	public List<Object> execute(GetRecordQuery sql) {
		try {
			return sql.execute(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public List<List<Object>> execute(GetRecordsQuery sql) {
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

	public int insert(Object... records) {
		int doneQueries = 0;
		InsertQuery query;
		for (int i = 0; i < records.length; i++) {
			query = new InsertQuery(records[i]);
			if (query.execute(conn))
				doneQueries++;
		}
		return doneQueries;
	}

	public int update(Object... records) {
		int doneQueries = 0;
		UpdateQuery query;
		for (int i = 0; i < records.length; i++) {
			query = new UpdateQuery(records[i]);
			if (query.execute(conn))
				doneQueries++;
		}
		return doneQueries;
	}

	public int delete(Object... records) {
		int doneQueries = 0;
		DeleteQuery query;
		for (int i = 0; i < records.length; i++) {
			query = new DeleteQuery(records[i]);
			if (query.execute(conn))
				doneQueries++;
		}
		return doneQueries;
	}

	public boolean delete(Class<?> cLass, String WhereClause, Object... parameters) {
		DeleteWhereQuery query = new DeleteWhereQuery(cLass, WhereClause, parameters);
		return query.execute(conn);
	}

	public Object insertAndGetPrimaryKey(Object record) {
		InsertQuery query = new InsertQuery(record);
		GetRecordQuery getPrimaryKey = new GetRecordQuery(1, "SELECT LAST_INSERT_ID();");
		if(!query.execute(conn))
			return null;
		return execute(getPrimaryKey).get(0);
	}

	public <T> List<T> getList(Class<T> cLass) {
		ListQuery<T> query = new ListQuery<T>(cLass);
		return query.execute(conn);
	}

	public <T> List<T> getList(Class<T> cLass, String condition, Object... parameters) {
		ListQuery<T> query = new ListQuery<T>(cLass, condition, parameters);
		return query.execute(conn);
	}

	public <T> T get(Class<T> cLass, Object... primaryKey) {
		SelectQuery<T> query = new SelectQuery<T>(cLass, primaryKey);
		return query.execute(conn);
	}

	public <T> T getWhere(Class<T> cLass, String WhereClause, Object... keys) {
		SelectWhereQuery<T> query = new SelectWhereQuery<T>(cLass, WhereClause, keys);
		return query.execute(conn);
	}

	public boolean execute(InsertQuery query) {
		return query.execute(conn);
	}


}
