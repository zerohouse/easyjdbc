package easyjdbc.column.list;

import java.lang.reflect.Field;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.OtherTable;
import easyjdbc.annotation.Table;
import easyjdbc.column.DBColumn;

public class ObjectList extends ColumnListProto {

	public ObjectList(Object instance) {
		Class<?> cLass = instance.getClass();
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(OtherTable.class))
				continue;
			DBColumn dbCol = new DBColumn(fields[i]);
			dbCol.setByInstance(instance);
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(dbCol);
				continue;
			}
			columns.add(dbCol);
		}
	}


}
