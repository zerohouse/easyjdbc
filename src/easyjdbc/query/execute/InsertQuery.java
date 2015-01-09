package easyjdbc.query.execute;

import easyjdbc.column.list.ColumnList;
import easyjdbc.column.list.ObjectList;

public class InsertQuery extends ExecuteableQuery {

	public InsertQuery(Object instance) {
		list = new ObjectList(instance);
		sql = "insert " + list.getTableName() + " set " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

}
