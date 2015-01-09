package easyjdbc.query.raw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
