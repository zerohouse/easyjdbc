package easyjdbc.test;


import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import easyjdbc.dao.DBMethods;

public class UserTest {


	@Test
	public void updateTest() {
		User user = new User();
		DBMethods.update(user);
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
		System.out.println(DBMethods.getList(Schedule.class,"userId=? and date=?", "zerohouse", "2014-12-12"));
	}
	
	public static Date parseDate(String format, Object object) {
		Date date = null;
		SimpleDateFormat datetime = new SimpleDateFormat(format);
		try {
			date = datetime.parse(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

}
