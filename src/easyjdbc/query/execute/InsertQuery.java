package easyjdbc.query.execute;

import easyjdbc.query.support.ColumnList;
import easyjdbc.query.support.DBColumn;

public class InsertQuery extends ExecuteableQuery {

	public InsertQuery(Object instance) {
		list = new ColumnList(instance, DBColumn.PHASE_INSERT);
		sql = "insert " + list.getTableName() + " set " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

}
