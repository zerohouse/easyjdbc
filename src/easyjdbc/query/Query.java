package easyjdbc.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Query {
	
	<T> Object execute(Connection conn) throws SQLException;
	
	public void setSql(String sql);
	
	public void addSql(String sql);
	
	public void addParameters(Object... parameters);
	
	public void setParameters(List<Object> parameters);

}
