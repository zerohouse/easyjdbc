package easyjdbc.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Query {
	
	Object execute(PreparedStatement pstmt, Connection conn, ResultSet rs) throws SQLException;
	
	public void setSql(String sql);
	
	public void addSql(String sql);
	
	public void addParameters(Object... parameters);
	
	public void setParameters(List<Object> parameters);

}
