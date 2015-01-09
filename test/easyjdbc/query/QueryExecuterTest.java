package easyjdbc.query;

import java.util.List;

import org.junit.Test;

import easyjdbc.object.User;
import easyjdbc.query.raw.ExecuteQuery;
import easyjdbc.query.raw.GetRecordQuery;
import easyjdbc.query.raw.GetRecordsQuery;
import easyjdbc.query.select.ListQuery;

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
	
	@Test
	public void rawTest() {
		GetRecordQuery query = new GetRecordQuery(6, "select * from user where id=?", "zerohouse");
		List<Object> list = qe.execute(query);
		//GetRecordsQuery query2 = new GetRecordsQuery(6, "select * from user where date=?", "2014-12-10");
		//List<List<Object>> lists = qe.execute(query2);
		//ExecuteQuery query3 = new ExecuteQuery("insert into user values(1,2,3,?)", "zerohouse");
		//Boolean result = qe.execute(query3);
		
		ListQuery<User> user = new ListQuery<User>(User.class);
		user.setPage(1, 3);
		user.setOrder("id", true);
		System.out.println(user.execute(qe.conn));
		
		
		//System.out.println(list);
		//System.out.println(lists);
		//System.out.println(result);
	}


}
