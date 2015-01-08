package easyjdbc.query;

import java.lang.reflect.Field;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.query.support.ColumnList;
import easyjdbc.query.support.DBColumn;

public abstract class EasyQuery extends Query {

	protected final static String WHERE = " where ";
	protected static final String AND = " and ";
	protected static final String COMMA = " , ";

	protected ColumnList columns = new ColumnList();
	protected ColumnList keys = new ColumnList();

	public EasyQuery() {

	}

	protected void setByInstance(Object instance, int phase) {
		Field[] fields = instance.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			DBColumn dbCol = new DBColumn(fields[i], phase);
			dbCol.setByInstance(instance);
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(dbCol);
				continue;
			}
			columns.add(dbCol);
		}
	}

	protected void setByType(Class<?> cLass, int phase) {
		Field[] fields = cLass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i], phase));
				continue;
			}
			columns.add(new DBColumn(fields[i], phase));
		}
	}

	protected void setByTypeAndPrimaryKey(Class<?> cLass, int phase, Object... primaryKey) {
		Field[] fields = cLass.getDeclaredFields();
		int j = 0;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isAnnotationPresent(Exclude.class))
				continue;
			if (fields[i].isAnnotationPresent(Key.class)) {
				keys.add(new DBColumn(fields[i], phase, primaryKey[j]));
				j++;
				continue;
			}
			columns.add(new DBColumn(fields[i], phase));
		}
	}

}
