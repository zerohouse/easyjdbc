짜증나는 JDBC 정리하기.
===========

[easyjdbc-0.0.1.jar](http://mylikenews.com/easyjdbc-0.0.1.jar)

	<dependency>
		<groupId>easy</groupId>
		<artifactId>easyjdbc</artifactId>
		<version>0.0.1</version>
		<scope>system</scope>
	   	<systemPath>${basedir}/lib/easyjdbc-0.0.1.jar</systemPath>
	</dependency>

Json형식으로 WEB-INF/database.setting 파일을 읽음

	{
		"url" : "Database URL", // 데이터 베이스 URL
		"id" : "Database ID", // 데이터베이스 Id
		"password" : "Database Password" //데이터 베이스 패스워드
	}

오브젝트 --------------

	1. 각 필드명 = DB의 필드명과 일치
	2. @Table어노테이션으로 테이블명 지정
	3. @Key어노테이션으로 프라이머리 키 지정
	4. 파라미터가 없는 생성자가 있어야 합니다.
	5. 각 필드에 대한 게터메소드와 세터메소드가 있어야 합니다.

어노테이션 -------------
	
	@Table(value = "테이블네임", defaultCondition = "기본 조건",
	 pageSize = 기본 페이지 사이즈(int), joinWith = "조인할 테이블",
	  on = "조인조건 ex) user.id = type.userId",
	  joinType = "조인타입 ex) left, right, inner")
		
	@Key = 프라이머리 필드
	@Exclude = DB에 없는 필드
	@Othertable = 조인사용시 다른 테이블의 필드

지원하는 메소드 -----------

	QueryExecuter qe = new QueryExecuter();
	User user = qe.get(User.class, "zerohouse");
	List<User> userlist = qe.getList(User.class);
	boolean result = qe.insert(new User());
	boolean result =  qe.update(new User());
	boolean result = qe.delete(new User());
	long key = qe.insertAndGetPrimarykey(new User());
	boolean result = qe.insertIfExistIgnore(new User());
	boolean result = qe.insertIfExistUpdate(new User());
	GetRecordQuery query = new GetRecordQuery(6, "select * from user where id=?", "zerohouse");
	List<Object> list = qe.execute(query);
	GetRecordsQuery query2 = new GetRecordsQuery(6, "select * from user where date=?", "2014-12-10");
	List<List<Object>> lists = qe.execute(query2);
	ExecuteQuery query3 = new ExecuteQuery("insert into user values(1,2,3,?)", "zerohouse");
	Boolean result = qe.execute(query3);
	ListQuery<User> user = new ListQuery<User>(User.class);
	user.setPage(1, 3); //페이지 인덱스 , 한페이지당 갯수
	user.setOrder("id", true); // 정렬 기준 칼럼, boolean asc
	List<User> userList = user.execute(qe.conn));
	qe.close();
	

샘플 오브젝트 -------------------

	@Table(value = "user", joinWith = "type", on = "user.id=type.userId", joinType = "left")
	public class User {

	@Key
	private String id;
	@Column(value = "password", valueFormat = "HEX(AES_ENCRYPT(?, ?))")
	private String password;
	private String email;
	private String name;
	private String nickname;
	private String gender;
	private Date timestamp;
	
	@OtherTable("name")
	private String typeName;
	
	@Exclude
	private int userScore;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public boolean isPasswordCorrect(User userpassed) {
		return password.equals(userpassed.getPassword());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void update(User usermod) {
		String password = usermod.getPassword();
		String email = usermod.getEmail();
		String name = usermod.getName();
		String nickname = usermod.getNickname();
		String gender = usermod.getGender();

		if (password != null)
			this.password = password;
		if (email != null)
			this.email = email;
		if (name != null)
			this.name = name;
		if (nickname != null)
			this.nickname = nickname;
		if (gender != null)
			this.gender = gender;
	}

	}

