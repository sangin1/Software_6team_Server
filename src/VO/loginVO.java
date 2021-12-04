package VO;

import java.io.Serializable;

public class loginVO implements Serializable{
	private static final long serialVersionUID = 2L;
	
	private String login_id;
	private String id;
	private String pw;
	
	public void loginVO(String login_id,String id,String pw) {
		this.login_id = login_id;
		this.id = id;
		this.pw = pw;
	}
	@Override
	public String toString() {
		return "Information:"+getLogin_id()+"/"+getId()+"/"+getPw();
		
	}
 
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
}
