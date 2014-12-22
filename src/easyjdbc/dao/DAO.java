package easyjdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import easyjdbc.setting.Setting;

public class DAO {

	private String sql;
	private Integer resultSize;
	private ArrayList<Object> parameters = new ArrayList<Object>();

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setResultSize(Integer resultSize) {
		this.resultSize = resultSize;
	}

	public void addParameter(Object parameter) {
		parameters.add(parameter);
	}

	public Connection getConnection() {
		Connection con = null;
		String url = Setting.get("url");
		String id = Setting.get("id");
		String pw = Setting.get("password");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}


	public boolean doQuery() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(pstmt);
			return pstmt.execute();

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

	public ArrayList<Object> getRecord() {
		ArrayList<Object> result = new ArrayList<Object>();
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(pstmt);
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

	public ArrayList<ArrayList<Object>> getRecords() {
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> record;
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setParameters(pstmt);
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

	private void setParameters(PreparedStatement pstmt) throws SQLException {
		Date date;
		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i) instanceof String) {
				String param = (String) parameters.get(i);
				pstmt.setString(i + 1, param.replaceAll("\\<.*?\\>", "")
						.replaceAll("BrAkELInE", "<br>"));
			} else if (parameters.get(i) instanceof Integer) {
				pstmt.setInt(i + 1, (Integer) parameters.get(i));
			} else if (parameters.get(i) instanceof Long) {
				pstmt.setLong(i + 1, (long) parameters.get(i));
			} else if (parameters.get(i) instanceof Date) {
				date = (Date) parameters.get(i);
				pstmt.setTimestamp(i + 1, new Timestamp(date.getTime()));
			}
		}
	}
}
