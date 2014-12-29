package easyjdbc.test;

import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

@Table("type")
public class Type {
	
	@Key
	private int id;
	private String userId;
	private String name;
	private String color;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}