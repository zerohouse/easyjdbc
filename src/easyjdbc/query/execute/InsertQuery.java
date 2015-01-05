package easyjdbc.query.execute;

import easyjdbc.annotation.Table;

public class InsertQuery extends ExecuteableQuery {

	private String tableName;
	private Object instance;

	public InsertQuery(Object instance) {
		this.instance = instance;
		Class<?> type = instance.getClass();
		setByInstance(instance);
		Table table = type.getAnnotation(Table.class);
		this.tableName = table.value();
		sql = "insert into " + tableName + " (" + joinedString(columns, ", ") + joinedString(keys, ", ", 2) + ") ";
		sql += "values (" + getQuestionComma(columns.size() + keys.size()) + ")";
		for (String key : columns.keySet()) {
			parameters.add(columns.get(key).getInvokedObject(this.instance));
		}
		for (String key : keys.keySet()) {
			parameters.add(keys.get(key).getInvokedObject(this.instance));
		}
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + joinedString(columns, "=?, ") + joinedString(keys, "=?, ", 2);
		for (String key : columns.keySet()) {
			parameters.add(columns.get(key).getInvokedObject(this.instance));
		}
		for (String key : keys.keySet()) {
			parameters.add(keys.get(key).getInvokedObject(this.instance));
		}
	}

}
