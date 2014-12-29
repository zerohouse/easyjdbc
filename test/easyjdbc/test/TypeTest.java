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

}
