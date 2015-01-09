package easyjdbc.column.list;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.OtherTable;
import easyjdbc.annotation.Table;
import easyjdbc.column.DBColumn;

public class SelectList extends ColumnListProto {

	private String joinWith;
	private String joinType;
	private String on;

	public SelectList(Class<?> cLass, Object... primaryKey) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		if (!table.joinWith().equals("")) {
			joinWith = table.joinWith();
			on = table.on();
			joinType = table.joinType();
			this.isJoined = true;
		}
		int j = 0;
		for (int i = 0; i < fields.length; i++) {
			DBColumn dbcol = new DBColumn(fields[i]);
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(OtherTable.class)) {
				dbcol.setOtherTable(true);
				String columnName = fields[i].getAnnotation(OtherTable.class).value();
				if (!columnName.equals(""))
					dbcol.setColumnName(columnName);
			}
			if (fields[i].isAnnotationPresent(Key.class)) {
				if (primaryKey.length != 0) {
					dbcol.setObject(primaryKey[j]);
					j++;
				}
				keys.add(dbcol);
				continue;
			}
			columns.add(dbcol);
		}
	}

	@Override
	protected String eachColumnName(String delimiter, DBColumn each) {
		if (isJoined) {
			if (each.isOtherTable())
				return joinWith + "." + each.getColumnName() + delimiter;
			return tableName + "." + each.getColumnName() + delimiter;
		}
		return each.getColumnName() + delimiter;
	}

	@Override
	protected String eachNameAndValue(String delimiter, DBColumn each) {
		if (isJoined) {
			if (each.isOtherTable())
				return joinWith + "." + each.getNameAndValue() + delimiter;
			return tableName + "." + each.getNameAndValue() + delimiter;
		}
		return each.getNameAndValue() + delimiter;
	}

	@Override
	public String getTableName() {
		if (isJoined) {
			return String.format("%s %s join %s on %s", tableName, joinType ,joinWith, on);
		}
		return tableName;
	}

	@Override
	public Object objFromResultSet(ResultSet rs) {
		List<DBColumn> list = typeDefine(ColumnList.ALL);
		Object instance;
		try {
			instance = this.type.getConstructor().newInstance();
			for (int i = 0; i < list.size(); i++) {
				DBColumn column = list.get(i);
				String columnName = column.getColumnName();
				if (isJoined && column.isOtherTable())
					columnName = joinWith + "." + columnName;
				column.setObjectField(instance, rs.getObject(columnName));
			}
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
