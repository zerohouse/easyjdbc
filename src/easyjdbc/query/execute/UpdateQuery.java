package easyjdbc.query.execute;

import easyjdbc.annotation.Table;
import easyjdbc.query.support.DBColumn;

public class UpdateQuery extends ExecuteableQuery {

	private String tableName;
	private Object instance;

	public UpdateQuery(Object instance) {
		this.instance = instance;
		Class<?> type = instance.getClass();
		setByInstance(instance);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "update " + tableName + " set " + getNotNullFieldString(columns, "=?, ", 2) + WHERE + joinedString(keys, "=? and ", 5);
		for (String key : columns.keySet()) {
			DBColumn column = columns.get(key);
			if (column.hasObject())
				parameters.add(column.getInvokedObject(this.instance));
		}
		for (String key : keys.keySet()) {
			parameters.add(keys.get(key).getInvokedObject(this.instance));
		}
	}

}
