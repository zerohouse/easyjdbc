package easyjdbc.query.execute;

import easyjdbc.query.support.ColumnList;
import easyjdbc.query.support.DBColumn;

public class DeleteQuery extends ExecuteableQuery {

	public DeleteQuery(Class<?> type, Object... primaryKey) {
		list = new ColumnList(type, DBColumn.PHASE_DELETE, primaryKey);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		for (int i = 0; i < primaryKey.length; i++)
			parameters.add(primaryKey[i]);
	}

	public DeleteQuery(Object instance) {
		list = new ColumnList(instance, DBColumn.PHASE_DELETE);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		list.addParameters(ColumnList.KEY, parameters);
	}
}
