package easyjdbc.query;

import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

@Table("type")
public class Type {
	
	@Override
	public String toString() {
		return "Type [id=" + id + ", userId=" + userId + ", name=" + name + ", color=" + color + "]";
	}
	@Key
	private Integer id;
	private String userId;
	private String name;
	private String color;
	
	public Type(){
		
	}
	public Type(Integer id, String userId, String name, String color) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.color = color;
	}
	public int getId() {
		return id;
	}
	public void setId(Integer id) {
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