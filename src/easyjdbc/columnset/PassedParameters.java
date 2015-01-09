package easyjdbc.columnset;

import java.lang.reflect.Field;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;
import easyjdbc.column.DBColumn;

public class PassedParameters extends ColumnListProto {

	public PassedParameters(Class<?> cLass, Object[] primaryKey) {
		type = cLass;
		Field[] fields = cLass.getDeclaredFields();
		Table table = cLass.getAnnotation(Table.class);
		this.tableName = table.value();
		int j = 0;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i], primaryKey[j]));
				j++;
				continue;
			}
			columns.add(new DBColumn(fields[i]));
		}
	}

}
