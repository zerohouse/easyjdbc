package easyjdbc.query.support;

import java.util.ArrayList;
import java.util.List;

public class ColumnList {
	private List<DBColumn> list = new ArrayList<DBColumn>();

	public void add(DBColumn dbCol) {
		list.add(dbCol);
	}

	public int size() {
		return list.size();
	}

	public DBColumn get(int i) {
		return list.get(i);
	} 
	
	
	
}
