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
		d.setDate(Util.parseDate("yyyy-MM-dd", "2014-12-31"));
		d.setUserId("zerohouse");
		d.setHeader("235253");
		d.setEmoticon("zer");
		
		DBMethods.insertIfExistUpdate(d);
		DBMethods.insertIfNotExist(d)
	}

}
