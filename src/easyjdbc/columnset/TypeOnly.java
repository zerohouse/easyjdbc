package easyjdbc.columnset;

import java.lang.reflect.Field;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;
import easyjdbc.column.DBColumn;

public class TypeOnly extends ColumnListProto {

	public TypeOnly(Class<?> cLass) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i]));
				continue;
			}
			columns.add(new DBColumn(fields[i]));
		}
	}

}
