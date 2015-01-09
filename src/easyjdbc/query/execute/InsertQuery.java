package easyjdbc.query.execute;

import easyjdbc.columnset.ColumnList;
import easyjdbc.columnset.HasObject;

public class InsertQuery extends ExecuteableQuery {

	public InsertQuery(Object instance) {
		list = new HasObject(instance);
		sql = "insert " + list.getTableName() + " set " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

}
