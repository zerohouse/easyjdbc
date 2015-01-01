package easyjdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.setting.Setting;

public class MySql implements DAO {

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

	public boolean doQuery(String sql, List<Object> params) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(params, pstmt);
			pstmt.execute();
			return pstmt.getUpdateCount() == 1;

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
		}
		return false;
	}

	public boolean doQueries(List<String> sqls, List<List<Object>> parameterArrays) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();

			for (int i = 0; i < sqls.size(); i++) {
				System.out.println(sqls.get(i));
				pstmt = conn.prepareStatement(sqls.get(i));
				setParameters(parameterArrays.get(i), pstmt);
				pstmt.execute();
			}
			return pstmt.getUpdateCount() == 1;

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
		}
		return false;
	}

	public ArrayList<Object> getRecord(String sql, int resultSize, List<Object> params) {
		ArrayList<Object> result = new ArrayList<Object>();
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(params, pstmt);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				for (int i = 0; i < resultSize; i++) {
					result.add(rs.getObject(i + 1));
				}
			}
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
		return result;
	}

	public List<List<Object>> getRecords(String sql, int resultSize, List<Object> params) {
		List<List<Object>> result = new ArrayList<List<Object>>();
		ArrayList<Object> record;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(params, pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				record = new ArrayList<Object>();
				for (int i = 0; i < resultSize; i++) {
					record.add(rs.getObject(i + 1));
				}
				result.add(record);
			}
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
		return result;
	}

	static void setParameters(List<Object> parameters, PreparedStatement pstmt) throws SQLException {
		if (parameters == null)
			return;
		for (int i = 0; i < parameters.size(); i++) {
			pstmt.setObject(i + 1, parameters.get(i));
		}
	}

	@Override
	public List<Object> doQueriesAndReturnLast(List<String> sqls, List<List<Object>> parameterArrays, int resultSize) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ArrayList<Object> result = new ArrayList<Object>();
		try {
			conn = getConnection();
			for (int i = 0; i < sqls.size() - 1; i++) {
				pstmt = conn.prepareStatement(sqls.get(i));
				setParameters(parameterArrays.get(i), pstmt);
				pstmt.execute();
			}
			pstmt = conn.prepareStatement(sqls.get(sqls.size() - 1));
			setParameters(parameterArrays.get(sqls.size() - 1), pstmt);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				for (int i = 0; i < resultSize; i++) {
					result.add(rs.getObject(i + 1));
				}
			}
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
		}
		return result;
	}

}
