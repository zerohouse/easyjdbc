package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class DeleteQuery extends ExecuteableQuery {

	private String tableName;
	Class<?> type;

	public DeleteQuery(Class<?> type, Object... primaryKey) {
		this.type = type;
		setByTypeAndPrimaryKey(type, primaryKey);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "delete from " + tableName + WHERE + joinedString(keys, "=? and ", 5);
		for (int i = 0; i < primaryKey.length; i++)
			parameters.add(primaryKey[i]);
	}

	public DeleteQuery(Object instance) {
		setByInstance(instance);
		Table table = instance.getClass().getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "delete from " + tableName + WHERE + joinedString(keys, "=? and ", 5);
		for (String key : keys.keySet()) {
			DBColumn column = keys.get(key);
			parameters.add(column.getInvokedObject(instance));
		}
	}
}
