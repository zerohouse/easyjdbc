package easyjdbc.query.execute;

import easyjdbc.columnset.ColumnList;
import easyjdbc.columnset.HasObject;
import easyjdbc.columnset.PassedParameters;

public class DeleteQuery extends ExecuteableQuery {

	public DeleteQuery(Class<?> type, Object... primaryKey) {
		list = new PassedParameters(type, primaryKey);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		for (int i = 0; i < primaryKey.length; i++)
			parameters.add(primaryKey[i]);
	}

	public DeleteQuery(Object instance) {
		list = new HasObject(instance);
		sql = "delete from " + list.getTableName() + WHERE + list.getNameAndValue(ColumnList.KEY, AND, true);
		list.addParameters(ColumnList.KEY, parameters);
	}
}
