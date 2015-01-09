package easyjdbc.query;

import java.util.List;

import org.junit.Test;

import easyjdbc.object.User;

public class QueryExecuterTest {

	QueryExecuter qe = new QueryExecuter();

	@Test
	public void getTest() {
		User user = qe.get(User.class, "zerohouse");
		System.out.println(user);
	}

	@Test
	public void getListTest() {
		List<User> userList = qe.getList(User.class);
		System.out.println(userList);
	}

	@Test
	public void insertTest() {
		User user = qe.get(User.class, "zerohouse");
		user.setId("zerohou");
		qe.insert(user);
	}
	
	@Test
	public void updateTest() {
		User user = qe.get(User.class, "zerohouse");
		user.setId("zerohou");
		user.setEmail("zerohou@ff.com");
		qe.update(user);
	}
	
	@Test
	public void deleteTest() {
		User user = qe.get(User.class, "zerohouse");
		user.setId("zerohou");
		qe.delete(user);
	}


}
