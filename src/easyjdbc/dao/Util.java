package easyjdbc.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
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
