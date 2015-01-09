package easyjdbc.column.list;

import java.lang.reflect.Field;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.OtherTable;
import easyjdbc.annotation.Table;
import easyjdbc.column.DBColumn;

public class DeleteList extends ColumnListProto {

	public DeleteList(Class<?> cLass, Object... primaryKey) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		int j = 0;
		for (int i = 0; i < fields.length; i++) {
			DBColumn dbcol = new DBColumn(fields[i]);
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(OtherTable.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				if (primaryKey != null) {
					dbcol.setObject(primaryKey[j]);
					j++;
				}
				keys.add(dbcol);
				continue;
			}
			columns.add(dbcol);
		}
	}

}
