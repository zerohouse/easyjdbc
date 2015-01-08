package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class UpdateQuery extends ExecuteableQuery {

	private String tableName;

	public UpdateQuery(Object instance) {
		Class<?> type = instance.getClass();
		setByInstance(instance, DBColumn.PHASE_UPDATE);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "update " + tableName + " set " + columns.addAndGetString(parameters, COMMA, true) + WHERE
				+ keys.addAndGetString(parameters, AND, true);
	}

}
