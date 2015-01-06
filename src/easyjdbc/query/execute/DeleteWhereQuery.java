package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class DeleteWhereQuery extends ExecuteableQuery {

	private String tableName;
	Class<?> type;

	public DeleteWhereQuery(Class<?> type, String whereClause, Object... keys) {
		this.type = type;
		setByType(type, DBColumn.PHASE_DELETE);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "delete from " + tableName + WHERE + whereClause;
		for (int i = 0; i < keys.length; i++)
			parameters.add(keys[i]);
	}
}
