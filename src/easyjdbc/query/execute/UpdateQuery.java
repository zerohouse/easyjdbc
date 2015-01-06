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
		sql = "update " + tableName + " set " + getNotNullFieldString(columns, COMMA, true) + WHERE + getNotNullFieldString(keys, AND, true);
		for (int i = 0; i < columns.size(); i++) {
			DBColumn column = columns.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
		for (int i = 0; i < keys.size(); i++) {
			DBColumn column = keys.get(i);
			if (column.hasObject())
				column.addObject(parameters);
		}
	}

}
