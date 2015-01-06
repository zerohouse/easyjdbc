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
		sql = "insert " + tableName + " set " + getNotNullFieldString(keys, COMMA, false) + getNotNullFieldString(columns, COMMA, true);
		for (int i = 0; i < keys.size(); i++) {
			DBColumn column = keys.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
		for (int i = 0; i < columns.size(); i++) {
			DBColumn column = columns.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
	}

	public void ifExistUpdate() {
		sql += " on duplicate key update " + getNotNullFieldString(columns, COMMA, false) + getNotNullFieldString(keys, COMMA, true);
		for (int i = 0; i < keys.size(); i++) {
			DBColumn column = keys.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
		for (int i = 0; i < columns.size(); i++) {
			DBColumn column = columns.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
	}

}
