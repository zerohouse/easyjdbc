package easyjdbc.query.execute;

import easyjdbc.columnset.TypeOnly;

public class DeleteWhereQuery extends ExecuteableQuery {

	public DeleteWhereQuery(Class<?> type, String whereClause, Object... keys) {
		list = new TypeOnly(type);
		sql = "delete from " + list.getTableName() + WHERE + whereClause;
		for (int i = 0; i < keys.length; i++)
			parameters.add(keys[i]);
	}
}
