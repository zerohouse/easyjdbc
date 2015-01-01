package easyjdbc.dao;

import java.util.ArrayList;
import java.util.List;

public class TestDAO implements DAO {

	List<Object> params = new ArrayList<Object>();

	@Override
	public void addParameter(Object... param) {
		for(int i=0;i<param.length;i++){
			params.add(param[i]);
		}
	}

	@Override
	public boolean doQuery(String sql) {
		System.out.println("sql : " + sql);
		if (params.size() != 0)
			System.out.println("passedparams : " + params);
		return true;
	}

	@Override
	public boolean doQueries(List<String> sqls, List<List<Object>> parameterArrays) {
		for(int i=0;i<sqls.size();i++){
			System.out.println("sql : " + sqls.get(i));
			System.out.println("passedparams : " + params.get(i));
		}
		return true;
	}

	@Override
	public List<Object> doQueriesAndReturnLast(List<String> sqls, List<List<Object>> parameterArrays, int resultSize) {
		for(int i=0;i<sqls.size();i++){
			System.out.println("sql : " + sqls.get(i));
			System.out.println("passedparams : " + params.get(i));
		}
		return null;
	}

	@Override
	public List<List<Object>> getRecords(String sql, int size) {
		System.out.println("sql : " + sql);
		if (params.size() != 0)
			System.out.println("passedparams : " + params);
		System.out.println("resultSetSize : " + size);
		return null;
	}

	@Override
	public List<Object> getRecord(String sql, int size) {
		System.out.println("sql : " + sql);
		if (params.size() != 0)
			System.out.println("passedparams : " + params);
		System.out.println("resultSetSize : " + size);
		return null;
	}

}
