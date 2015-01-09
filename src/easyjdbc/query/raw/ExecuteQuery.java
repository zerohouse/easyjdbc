package easyjdbc.query.raw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.query.Query;

public class ExecuteQuery extends Query {

	public ExecuteQuery(String sql, List<Object> parameters) {
		this.sql = sql;
		this.parameters = parameters;
		if (parameters == null) {
			this.parameters = new ArrayList<Object>();
		}
	}
	
	public ExecuteQuery(String sql, Object... parameters) {
		this.sql = sql;
		this.parameters = new ArrayList<Object>();
		for (int i = 0; i < parameters.length; i++) {
			this.parameters.add(parameters[i]);
		}
	}


	public Boolean execute(Connection conn){
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			if (parameters != null)
				for (int j = 0; j < parameters.size(); j++) {
					pstmt.setObject(j + 1, parameters.get(j));
				}
			pstmt.execute();
			int result = pstmt.getUpdateCount();
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException sqle) {
				}
			return result != 0;
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
		return false;
	}

}
