package easyjdbc.query.execute;

import easyjdbc.query.support.ColumnList;
import easyjdbc.query.support.DBColumn;

public class DeleteWhereQuery extends ExecuteableQuery {

	public DeleteWhereQuery(Class<?> type, String whereClause, Object... keys) {
		list = new ColumnList(type, DBColumn.PHASE_DELETE);
		sql = "delete from " + list.getTableName() + WHERE + whereClause;
		for (int i = 0; i < keys.length; i++)
			parameters.add(keys[i]);
	}
}
