package easyjdbc.query;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueryExecuterTest {

	@Test
	public void test() {
		QueryExecuter qe = new QueryExecuter();
		Type type = new Type(null, "zerohouse", "네임", "오홍");
		qe.insert(type);
	}
	
	@Test
	public void testInsert() {
		QueryExecuter qe = new QueryExecuter();
		Type type = new Type(null, "zerohouse", "네cvncn임", "오홍");
		Type type2 = new Type(null, "zerohouse", "네serysrey임", "오홍");
		qe.insert(type, type2);
	}
	
	@Test
	public void testInssert() {
		QueryExecuter qe = new QueryExecuter();
		System.out.println(qe.getList(Type.class));
	}
	
	@Test
	public void testInsssert() {
		QueryExecuter qe = new QueryExecuter();
		Type type = qe.get(Type.class, "z");
		System.out.println(type);
	}
	
	@Test
	public void testInssssert() {
		QueryExecuter qe = new QueryExecuter();
		Type type = qe.get(Type.class, "z");
		System.out.println(type);
		assertTrue(true);
	}


}
