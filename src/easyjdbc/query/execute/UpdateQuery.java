package easyjdbc.query.execute;

import easyjdbc.column.list.ColumnList;
import easyjdbc.column.list.ObjectList;

public class UpdateQuery extends ExecuteableQuery {

	public UpdateQuery(Object instance) {
		list = new ObjectList(instance);
		sql = String.format("update %s set %s where %s", list.getTableName(), list.addAndGetString(ColumnList.COLUMN, parameters, COMMA, true),
				list.addAndGetString(ColumnList.KEY, parameters, AND, true));
	}
}
