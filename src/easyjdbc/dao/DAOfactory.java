package easyjdbc.dao;

public class DAOfactory {
	
	public static DAO TEST_DAO;
	
	private DAOfactory(){
	}
	
	public static void setTester(DAO dao){
		TEST_DAO = dao;
	}
	
	public static DAO getDAO() {
		if(TEST_DAO!=null)
			return TEST_DAO;
		DAO dao = new DAOjdbc();
		return dao;
	}
}
