package easyjdbc.columnset;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.column.DBColumn;

public abstract class ColumnListProto implements ColumnList {

	protected List<DBColumn> keys = new ArrayList<DBColumn>();
	protected List<DBColumn> columns = new ArrayList<DBColumn>();

	protected Class<?> type;
	protected String tableName;

	
	public String getTableName() {
		return tableName;
	}


	
	public String getJoinedName(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getColumnName() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String getNameAndValue(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i).getNameAndValue() + delimiter;
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String addAndGetString(int type, List<Object> parameters, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

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

	public void addParameters(int type, List<Object> parameters) {
		List<DBColumn> list = typeDefine(type);
		for (int i = 0; i < list.size(); i++) {
			DBColumn column = list.get(i);
			column.addObject(parameters);
		}
	}

	public Object getObject(ResultSet rs) {
		List<DBColumn> list = typeDefine(ColumnList.ALL);
		Object instance;
		try {
			instance = this.type.getConstructor().newInstance();
			for (int i = 0; i < list.size(); i++) {
				DBColumn column = list.get(i);
				column.getObjectField(instance, rs.getObject(column.getColumnName()));
			}
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<DBColumn> typeDefine(int type) {
		List<DBColumn> list;
		switch (type) {
		case ALL:
			list = new ArrayList<DBColumn>();
			list.addAll(keys);
			list.addAll(columns);
			break;
		case KEY:
			list = keys;
			break;
		case COLUMN:
			list = columns;
			break;
		default:
			list = columns;
		}
		return list;
	}

}
