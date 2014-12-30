package easyjdbc.test;

import java.util.Date;

import easyjdbc.annotation.Key;
import easyjdbc.annotation.Table;

@Table("dateheader")
public class DateHeader {
	@Key
	private Date date;
	@Key
	private String userId; 
	private String header;
	private String emoticon;
	
	public Date getDate() {
		return date;
	}
	@Override
	public String toString() {
		return "DateHeader [date=" + date + ", userId=" + userId + ", header=" + header + ", emoticon=" + emoticon + "]";
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getEmoticon() {
		return emoticon;
	}
	public void setEmoticon(String emoticon) {
		this.emoticon = emoticon;
	}
}