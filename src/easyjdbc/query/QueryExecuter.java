package easyjdbc.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	PreparedStatement pstmt;
	ResultSet rs;

	public QueryExecuter() {
		conn = getConnection();
	}

	public List<Object> execute(Query... sqls) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			List<Object> results = new ArrayList<Object>();
			for (int i = 0; i < sqls.length; i++) {
				results.add(sqls[i].execute(pstmt, conn, rs));
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public void close() {
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
				rs.close();
			} catch (SQLException sqle) {
			}
	}

	public static boolean insertIfExistUpdate(Object record) {
		return false;
	}

	public static boolean insertIfNotExist(Object record) {
		return false;
	}

	public boolean insert(Object... records) {
		Query query = QueryFactory.getInsertQuery(records[0]);
		return (boolean) execute(query).get(0);
	}

	public static Object insertAndGetPrimaryKey(Object record) {
		return record;
	}

	public static <T> List<T> getList(Class<T> cLass) {
		return null;
	}

	public static <T> List<T> getList(Class<T> cLass, String condition, Object... parameters) {
		return null;
	}

	public static <T> T get(Class<T> cLass, Object... primaryKey) {
		return null;
	}

	public static boolean update(Object... record) {
		return false;
	}

	public static boolean delete(Object... record) {
		return false;
	}

}
