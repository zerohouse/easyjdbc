package easyjdbc.query.execute;

import easyjdbc.column.list.ColumnList;
import easyjdbc.column.list.DeleteList;
import easyjdbc.column.list.ObjectList;

public class DeleteQuery extends ExecuteableQuery {

	public DeleteQuery(Class<?> type, Object... primaryKey) {
		list = new DeleteList(type, primaryKey);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		for (int i = 0; i < primaryKey.length; i++)
			parameters.add(primaryKey[i]);
	}

	public DeleteQuery(Object instance) {
		list = new ObjectList(instance);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		list.addParameters(ColumnList.KEY, parameters);
	}
}
