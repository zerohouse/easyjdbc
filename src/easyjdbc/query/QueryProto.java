package easyjdbc.query;

import java.util.List;

public abstract class QueryProto implements Query {
	
	protected int resultSize;
	protected String sql;
	protected List<Object> parameters;

	@Override
	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public void addSql(String sql) {
		this.sql += " ";
		this.sql += sql;
	}

	@Override
	public void addParameters(Object... parameters) {
		for(int i=0;i<parameters.length; i++){
			this.parameters.add(parameters[i]);
		}
	}

	@Override
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

}
