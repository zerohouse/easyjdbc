package easyjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExecuteQuery extends QueryProto {

	public ExecuteQuery(String sql, List<Object> parameters) {
		this.sql = sql;
		this.parameters = parameters;
		if (parameters == null) {
			this.parameters = new ArrayList<Object>();
		}
	}

	@Override
	public Object execute(PreparedStatement pstmt, Connection conn, ResultSet rs) throws SQLException {
		pstmt = conn.prepareStatement(sql);
		if (parameters != null)
			for (int j = 0; j < parameters.size(); j++) {
				pstmt.setObject(j + 1, parameters.get(j));
			}
		pstmt.execute();
		return pstmt.getUpdateCount() == 1;
	}

}
