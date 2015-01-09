package easyjdbc.query.execute;

import easyjdbc.columnset.ColumnList;
import easyjdbc.columnset.HasObject;

public class UpdateQuery extends ExecuteableQuery {

	public UpdateQuery(Object instance) {
		list = new HasObject(instance);
		sql = "update " + list.getTableName() + " set " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

}
