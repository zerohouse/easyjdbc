package easyjdbc.test;

import static org.junit.Assert.*;

import org.junit.Test;

import easyjdbc.dao.DBMethods;
import easyjdbc.dao.Util;

public class DateHeaderTest {

	@Test
	public void test() {
		System.out.println(DBMethods.get(DateHeader.class, "2014-12-30","zerohouse"));
	}
	
	@Test
	public void insertTest() {
		DateHeader d = new DateHeader();
		d.setDate(Util.parseDate("yyyy-MM-dd", "2014-12-13"));
		d.setUserId("zerohouse");
		d.setHeader("235253");
		d.setEmoticon("zer");
		DateHeader a = new DateHeader();
		a.setDate(Util.parseDate("yyyy-MM-dd", "2014-12-14"));
		a.setUserId("zerohouse");
		a.setHeader("235253");
		a.setEmoticon("zer");
		DateHeader c = new DateHeader();
		c.setDate(Util.parseDate("yyyy-MM-dd", "2014-12-15"));
		c.setUserId("zerohouse");
		c.setHeader("235253");
		c.setEmoticon("zer");
		
		System.out.println(DBMethods.insert(d,a,c));
	}

}
