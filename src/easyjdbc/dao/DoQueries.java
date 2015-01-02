package easyjdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.setting.Setting;

public class DoQueries {

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

	public static List<Object> doQueries(Sql... sqlHolders) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = getConnection();
			List<Object> results = new ArrayList<Object>();

			for (int i = 0; i < sqlHolders.length; i++) {
				results.add(doSingleQuery(sqlHolders[i], conn));
			}
			return results;

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
		return null;
	}

	private static Object doSingleQuery(Sql sqlHolder, Connection conn) throws SQLException {
		return sqlHolder.execute();
		PreparedStatement pstmt;
		String sql;
		List<Object> parameters;
		Integer returnType;
		ResultSet rs;
		Integer resultSize;
		List<Object> record;
		List<List<Object>> records;
		sql = sqlHolder.getSql();
		parameters = sqlHolder.getParameters();
		returnType = sqlHolder.getReturnType();
		resultSize = sqlHolder.getResultSize();
		pstmt = conn.prepareStatement(sql);

		for (int j = 0; j < parameters.size(); j++) {
			pstmt.setObject(j + 1, parameters.get(j));
		}
		
		

		switch (returnType) {
		case SqlHolder.RETURN_NULL:
			pstmt.execute();
			return pstmt.getUpdateCount() == 1;

		case SqlHolder.RETURN_SINGLE_RECORD:
			rs = pstmt.executeQuery();
			record = new ArrayList<Object>();
			if (rs.next()) {
				for (int j = 0; j < resultSize; j++) {
					record.add(rs.getObject(j + 1));
				}
			}
			return record;

		case SqlHolder.RETURN_RECORDS:
			rs = pstmt.executeQuery();
			records = new ArrayList<List<Object>>();

			while (rs.next()) {
				record = new ArrayList<Object>();
				for (int j = 0; j < resultSize; j++) {
					record.add(rs.getObject(j + 1));
				}
				records.add(record);
			}
			return records;
		}
		return null;
	}

}
