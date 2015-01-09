package easyjdbc.column.list;

import java.sql.ResultSet;
import java.util.List;

public interface ColumnList {

	public static final int ALL = 0;
	public static final int KEY = 1;
	public static final int COLUMN = 2;
	
	String getTableName();

	Object objFromResultSet(ResultSet rs);

	String getNameAndValue(int type, String deter, boolean isLast);

	void addParameters(int type, List<Object> parameters);

	String addAndGetString(int type, List<Object> parameters, String deter, boolean isLast);

	String getJoinedName(int type, String deter, boolean isLast);

}
