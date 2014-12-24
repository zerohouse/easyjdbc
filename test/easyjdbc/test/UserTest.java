package easyjdbc.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import easyjdbc.dao.DBMethods;

public class UserTest {

	@Test
	public void insertTest() {
		User user = new User();
		user.set("zssz", "ss", "d",",","",new Date());
		DBMethods.insert(user);
	}

	@Test
	public void updateTest() {
		User user = new User();
		DBMethods.update(user);
	}

	@Test
	public void setTest() {
		User user = new User();
		user.set("zz", "ss");
		assertEquals(user.getId(), "zz");
	}

	@Test
	public void getTest() {
		User user = DBMethods.get(User.class, "zz");
		assertNotNull(user);
		List<User> list = DBMethods.getList(User.class, "zerohouse");
		System.out.println(list);
		System.out.println(user);
	}

	@Test
	public void getListTest() {
		System.out.println(DBMethods.getList(User.class));
	}

	@Test
	public void fort() {
		for (int i = 0; i < 1; i++) {
			System.out.println("1");
		}
	}

}
