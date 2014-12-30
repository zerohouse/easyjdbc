package easyjdbc.test;

import java.sql.SQLException;

import org.junit.Test;

import easyjdbc.dao.DBMethods;

public class TypeTest {

	@Test
	public void test() throws SQLException {
		Type t = new Type();
		t.setName("s");
		t.setUserId("zerohouse");
		t.setColor("#000000");
		System.out.println(DBMethods.insertAndGetPrimaryKey(t));
	}
	
	@Test
	public void getTest() throws SQLException {
		System.out.println(DBMethods.get(Type.class, 30));
	}
	

}
