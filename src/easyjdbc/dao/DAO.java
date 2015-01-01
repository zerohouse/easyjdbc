package easyjdbc.dao;

import java.util.List;

public interface DAO {

	void addParameter(Object... param);

	boolean doQuery(String sql);

	boolean doQueries(List<String> sqls, List<List<Object>> parameterArrays);

	List<Object> doQueriesAndReturnLast(List<String> sqls, List<List<Object>> parameterArrays, int resultSize);

	List<List<Object>> getRecords(String sql, int size);

	List<Object> getRecord(String sql, int size);



}
