package easyjdbc.dao;

public class DAOfactory {
	
	public static boolean TEST = false;
	
	private DAOfactory(){
	}
	
	
	public static DAO getDAO() {
		if(TEST)
			return new TestDAO();
		return new MySql();
	}
}
