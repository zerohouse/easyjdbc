짜증나는 JDBC 정리하기.
===========

[a link](https://github.com/user/repo/blob/branch/other_file.md)

Json형식으로 WEB-INF/database.setting 파일을 읽음

	{
		"url" : "Database URL", // 데이터 베이스 URL
		"id" : "Database ID", // 데이터베이스 Id
		"password" : "Database Password" //데이터 베이스 패스워드!
	}

오브젝트 --------------

	1. implemets Record
	2. 각 필드명 = DB의 필드명과 일치
	3. @Table어노테이션으로 테이블명 지정
	4. @Key어노케이션으로 프라이머리 키 지정

어노테이션 -------------
	
	@Table("테이블네임")
	@Table(value = "테이블네임", defaultCondition = "기본 조건") - 리스트가져올 때 기본 조건
		
	@Key = 프라이머리 필드
	@Exclude = DB에 없는 필드

지원하는 메소드 -----------

	DBMethod.insert(Record record)
	DBMethod.update(Record record)
	DBMethod.delete(Record record)
	DBMethod.get(Class<T>, Object PrimaryKey)
	DBMethod.getList(Class<T>)
	DBMethod.getList(Class<T>, String Condition)

샘플 오브젝트 -------------------

	@Table("user")
	public class User implements Record { //레코드를 인터페이스하고,
		@Key
		private String id;
		private String password;
		private String email;
		private String nickname;
		private String gender;
		private Date timestamp;
		
		@Exclude
		private int count;
	
		public String getId() { //게터메소드를 채워줘야댐
			return id;
		}
	
		public String getPassword() {
			return password;
		}
	
		public String getEmail() {
			return email;
		}
	
		public String getNickname() {
			return nickname;
		}
	
		public String getGender() {
			return gender;
		}
	
		public Date getTimestamp() {
			return timestamp;
		}
	
		@Override
		public void set(Object... params) {  //오브젝트를 받아서 셋할 수 있게해야함
										// 필드 개수만큼 오브젝트가 들어옴
			id = params.length < 1 ? null : (String) params[0];
			password = params.length < 2 ? null : (String) params[1];
			email = params.length < 3 ? null : (String) params[2];
			nickname = params.length < 4 ? null : (String) params[3];
			gender = params.length < 5 ? null : (String) params[4];
			timestamp = params.length < 6 ? null : (Date) params[5];
		}
	}
	
사용 방법 ----------------

	User user = (User) DBMethods.get(User.class, "zerohouse");
	List<Record> = DBMethods.getList(User.class, "zerohouse");
	DBMethods.update(user);
