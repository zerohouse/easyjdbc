package easyjdbc.query.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import easyjdbc.annotation.Key;

public class PrimaryFields {
	
	List<Field> keys = new ArrayList<Field>();

	public PrimaryFields(List<Field> fields) {
		int columnSize = fields.size();
		for (int i = 0; i < columnSize; i++)
			if (fields.get(i).isAnnotationPresent(Key.class))
				keys.add(fields.get(i));
	}
	
	public PrimaryFields(Class<?> cLass) {
		Field[] fields = cLass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
			if (fields[i].isAnnotationPresent(Key.class))
				keys.add(fields[i]);
	}

	public void add(Field field) {
		keys.add(field);
	}
	
	public Field get(int index){
		return keys.get(index);
	}

	public String getCondition() {
		String result = "";
		for(int i=0; i<keys.size();i++){
			result+= keys.get(i).getName()+"=? and ";
		}
		result = result.substring(0, result.length() - 4);
		return result;
	}

	public Object[] getParams(Object record) {
		Object[] result = new Object[keys.size()];
		for(int i=0; i<keys.size();i++){
			result[i] = Methods.getFieldObject(keys.get(i).getName(), record);
		}
		return result;
	}	
	
	
}
