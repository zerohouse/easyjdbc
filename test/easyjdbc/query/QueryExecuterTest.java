package easyjdbc.query;

import static org.junit.Assert.*;

import org.junit.Test;

import easyjdbc.object.User;
import easyjdbc.object.UserAndType;

public class QueryExecuterTest {

	QueryExecuter qe = new QueryExecuter();

	@Test
	public void test() {
		User user = qe.get(User.class, "zerohouse");
		System.out.println(user);
	}

	@Test
	public void test2() {
		UserAndType user = qe.get(UserAndType.class, "zerohouse");
		System.out.println(user);
	}



}
