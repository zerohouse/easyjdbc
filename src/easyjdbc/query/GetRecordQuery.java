package easyjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRecordQuery implements Query {

	private int resultSize;
	private String sql;
	private List<Object> parameters;
	
	@Override
	public Object execute(PreparedStatement pstmt, Connection conn, ResultSet rs) throws SQLException {
		List<Object> record = new ArrayList<Object>();
		pstmt = conn.prepareStatement(sql);
		for (int j = 0; j < parameters.size(); j++) {
			pstmt.setObject(j + 1, parameters.get(j));
		}
		rs = pstmt.executeQuery();
		while (rs.next()) {
			for (int i = 0; i < resultSize; i++) {
				record.add(rs.getObject(i + 1));
			}
		}
		return record;
	}

	public GetRecordQuery(int resultSize, String sql, List<Object> parameters) {
		this.resultSize = resultSize;
		this.sql = sql;
		this.parameters = parameters;
	}
}
