package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class DeleteQuery extends ExecuteableQuery {

	private String tableName;
	Class<?> type;

	public DeleteQuery(Class<?> type, Object... primaryKey) {
		this.type = type;
		setByTypeAndPrimaryKey(type, DBColumn.PHASE_DELETE, primaryKey);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "delete from " + tableName + WHERE + joinedString(keys, AND, true);
		for (int i = 0; i < primaryKey.length; i++)
			parameters.add(primaryKey[i]);
	}

	public DeleteQuery(Object instance) {
		setByInstance(instance, DBColumn.PHASE_DELETE);
		Table table = instance.getClass().getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "delete from " + tableName + WHERE + joinedString(keys, AND, true);
		for (int i = 0; i < keys.size(); i++) {
			DBColumn column = keys.get(i);
			column.addObject(parameters);
		}
	}
}
