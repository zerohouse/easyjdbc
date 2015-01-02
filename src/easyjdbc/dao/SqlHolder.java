package easyjdbc.dao;

import java.util.ArrayList;
import java.util.List;

public class SqlHolder {

	private int returnType;
	private int resultSize;

	private String sql;
	private List<Object> parameters = new ArrayList<Object>();
	
	public static final int RETURN_RECORDS = 1;
	public static final int RETURN_SINGLE_RECORD = 2;
	public static final int RETURN_NULL = 3;
	
	public SqlHolder(){
		
	}
	
	public void setReturnType(int returnType) {
		this.returnType = returnType;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public int getReturnType() {
		return returnType;
	}

	public String getSql() {
		return sql;
	}
	
	public void addParameters(Object... params){
		for(int i=0; i<params.length; i++){
			parameters.add(params[i]);
		}
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public Integer getResultSize() {
		return resultSize;
	}
	
}
