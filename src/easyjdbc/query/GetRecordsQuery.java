package easyjdbc.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRecordsQuery implements Query {

	private int resultSize;
	private String sql;
	private List<Object> parameters;

	@Override
	public Object execute(PreparedStatement pstmt, java.sql.Connection conn, ResultSet rs) throws SQLException {
		List<Object> record;
		List<List<Object>> result = new ArrayList<List<Object>>();
		pstmt = conn.prepareStatement(sql);
		for (int j = 0; j < parameters.size(); j++) {
			pstmt.setObject(j + 1, parameters.get(j));
		}
		rs = pstmt.executeQuery();
		while (rs.next()) {
			record = new ArrayList<Object>();
			for (int i = 0; i < resultSize; i++) {
				record.add(rs.getObject(i + 1));
			}
			result.add(record);
		}
		return result;
	}

	public GetRecordsQuery(int resultSize, String sql, List<Object> parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = parameters;
	}
}
