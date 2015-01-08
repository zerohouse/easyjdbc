package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class InsertQuery extends ExecuteableQuery {

	private String tableName;

	public InsertQuery(Object instance) {
		Class<?> type = instance.getClass();
		setByInstance(instance, DBColumn.PHASE_INSERT);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "insert " + tableName + " set " + keys.addAndGetString(parameters, COMMA, false) + columns.addAndGetString(parameters, COMMA, true);
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + keys.addAndGetString(parameters, COMMA, false) + columns.addAndGetString(parameters, COMMA, true);
	}

}
