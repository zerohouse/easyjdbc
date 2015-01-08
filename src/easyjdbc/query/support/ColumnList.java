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
	
	public String getJoinedName(String delimiter, boolean isEnd) {
		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getColumnName() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}
	
	
	public String getJoinedNameAndValue(String delimiter, boolean isEnd) {
		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getNameAndValue() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String addAndGetString(List<Object> parameters, String delimiter, boolean isEnd) {
		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			DBColumn column = list.get(i);
			if (column.hasObject())
				result += column.getNameAndValue() + delimiter;
			column.addObject(parameters);
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

}
