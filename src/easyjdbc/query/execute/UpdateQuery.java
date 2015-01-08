package easyjdbc.query.execute;

import easyjdbc.query.support.ColumnList;
import easyjdbc.query.support.DBColumn;

public class UpdateQuery extends ExecuteableQuery {

	public UpdateQuery(Object instance) {
		list = new ColumnList(instance, DBColumn.PHASE_UPDATE);
		sql = "update " + list.getTableName() + " set " + list.addAndGetString(ColumnList.ALL, parameters, COMMA, true);
	}

}
