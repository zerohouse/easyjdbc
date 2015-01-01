package easyjdbc.dao;

public class DAOfactory {
	
	private DAOfactory(){
	}
	
	public static DAO getDAO() {
		return new MySql();
	}
}
