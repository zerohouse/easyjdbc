package easyjdbc.test;

import java.util.Date;

import easyjdbc.annotation.Exclude;
import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

@Table("user")
public class User {

	@Key
	private String id;
	private String password;
	private String email;
	private String nickname;
	private String gender;
	private Date timestamp;

	@Exclude
	private String xx;

	@Override
	public String toString() {
		return "User [id=" + id + ", password=" + password + ", email=" + email + ", nickname=" + nickname + ", gender=" + gender + ", timestamp="
				+ timestamp + "]";
	}

	public String getId() {
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

}
