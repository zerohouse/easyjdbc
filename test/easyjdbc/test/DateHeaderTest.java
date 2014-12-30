package easyjdbc.test;

import static org.junit.Assert.*;

import org.junit.Test;

import easyjdbc.dao.DBMethods;

public class DateHeaderTest {

	@Test
	public void test() {
		System.out.println(DBMethods.get(DateHeader.class, "2014-12-30","zerohouse"));
	}

}
