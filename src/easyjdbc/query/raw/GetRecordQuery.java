package easyjdbc.query.raw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import easyjdbc.query.Query;

public class GetRecordQuery extends Query {
	
	public List<Object> execute(Connection conn) {
		List<Object> record = new ArrayList<Object>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			if (parameters != null)
				for (int j = 0; j < parameters.size(); j++) {
					pstmt.setObject(j + 1, parameters.get(j));
				}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				for (int i = 0; i < resultSize; i++) {
					record.add(rs.getObject(i + 1));
				}
			}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(parameters);
			e.printStackTrace();
		}
		return record;
	}

	public Map<String, Object> getMap(Connection conn) {
		Map<String, Object> record = new LinkedHashMap<String, Object>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			if (parameters != null)
				for (int j = 0; j < parameters.size(); j++) {
					pstmt.setObject(j + 1, parameters.get(j));
				}
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
			    for (int i = 1; i <= columnCount; i++) {
			    	record.put(metaData.getColumnLabel(i), rs.getObject(i));
			    }
			}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException sqle) {
				}
		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(parameters);
			e.printStackTrace();
		}
		return record;
	}

	public GetRecordQuery(int resultSize, String sql, List<Object> parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = parameters;
		if (parameters == null) {
			this.parameters = new ArrayList<Object>();
		}
	}

	public GetRecordQuery(int resultSize, String sql, Object... parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = new ArrayList<Object>();
		for (int i = 0; i < parameters.length; i++) {
			this.parameters.add(parameters[i]);
		}
	}
}
