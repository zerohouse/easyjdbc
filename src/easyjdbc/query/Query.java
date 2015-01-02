package easyjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Query {
	
	Object execute(PreparedStatement pstmt, Connection conn, ResultSet rs) throws SQLException;

}
