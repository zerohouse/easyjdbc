package easyjdbc.query.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import easyjdbc.query.EasyQuery;

public class ExecuteableQuery extends EasyQuery {

	public Boolean execute(Connection conn) {
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
			return result == 1;
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
		return false;
	}

}
