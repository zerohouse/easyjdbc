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

public class GetRecordsQuery extends Query {
	
	public List<List<Object>> execute(Connection conn) {
		List<Object> record;
		List<List<Object>> result = new ArrayList<List<Object>>();
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			if (parameters != null)
				for (int j = 0; j < parameters.size(); j++) {
					pstmt.setObject(j + 1, parameters.get(j));
				}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				record = new ArrayList<Object>();
				for (int i = 0; i < resultSize; i++) {
					record.add(rs.getObject(i + 1));
				}
				result.add(record);
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
			e.printStackTrace();
		}

		return result;
	}

	public List<Map<String, Object>> getMapList(Connection conn) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
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
			    Map<String, Object> columns = new LinkedHashMap<String, Object>();
			    for (int i = 1; i <= columnCount; i++) {
			        columns.put(metaData.getColumnLabel(i), rs.getObject(i));
			    }
			    result.add(columns);
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
			e.printStackTrace();
		}

		return result;
	}

	public GetRecordsQuery(int resultSize, String sql, List<Object> parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = parameters;
		if (parameters == null) {
			this.parameters = new ArrayList<Object>();
		}
	}

	public GetRecordsQuery(int resultSize, String sql, Object... parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = new ArrayList<Object>();
		for (int i = 0; i < parameters.length; i++) {
			this.parameters.add(parameters[i]);
		}
	}
}
