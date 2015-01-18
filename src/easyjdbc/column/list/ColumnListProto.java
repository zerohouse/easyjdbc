package easyjdbc.column.list;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.column.DBColumn;

public abstract class ColumnListProto implements ColumnList {

	protected List<DBColumn> keys = new ArrayList<DBColumn>();
	protected List<DBColumn> columns = new ArrayList<DBColumn>();

	protected Class<?> type;
	protected String tableName;
	protected boolean isJoined = false;

	public String getTableName() {
		return tableName;
	}

	public String getJoinedName(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		String result = new String();
		DBColumn each;
		for (int i = 0; i < list.size(); i++) {
			each = list.get(i);
			result += eachColumnName(delimiter, each);
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	public String getNameAndValue(int type, String delimiter, boolean isEnd) {
		List<DBColumn> list = typeDefine(type);

		DBColumn each;
		String result = new String();
		for (int i = 0; i < list.size(); i++) {
			each = list.get(i);
			result += eachNameAndValue(delimiter, each);
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
			if (column.hasObject()) {
				result += eachNameAndValue(delimiter, column);
				column.addObject(parameters);
			}
		}
		if (isEnd)
			result = result.substring(0, result.length() - delimiter.length());
		return result;
	}

	protected String eachColumnName(String delimiter, DBColumn each) {
		return each.getColumnName() + delimiter;
	}

	protected String eachNameAndValue(String delimiter, DBColumn each) {
		return each.getNameAndValue() + delimiter;
	}

	public void addParameters(int type, List<Object> parameters) {
		List<DBColumn> list = typeDefine(type);
		for (int i = 0; i < list.size(); i++) {
			DBColumn column = list.get(i);
			column.addObject(parameters);
		}
	}

	public Object objFromResultSet(ResultSet rs) {
		List<DBColumn> list = typeDefine(ColumnList.ALL);
		Object instance;
		try {
			instance = this.type.getConstructor().newInstance();
			for (int i = 0; i < list.size(); i++) {
				DBColumn column = list.get(i);
				column.setObjectField(instance, rs.getObject(column.getColumnName()));
			}
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected List<DBColumn> typeDefine(int type) {
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
