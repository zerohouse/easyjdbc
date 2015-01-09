package easyjdbc.query;

import easyjdbc.column.list.ColumnList;

public abstract class EasyQuery extends Query {

	protected final static String WHERE = " where ";
	protected static final String AND = " and ";
	protected static final String COMMA = " , ";

	protected ColumnList list;

	public EasyQuery() {

	}

}
