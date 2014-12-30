package easyjdbc.dao;

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

	public void add(Field field) {
		keys.add(field);
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
			result[i] = DBMethods.getFieldObject(keys.get(i).getName(), record);
		}
		return result;
	}
	
}
