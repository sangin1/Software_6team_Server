package VO;

import java.io.Serializable;

public class duesVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String dues_id;
	private String dues_name;
	private String dues;
	private String dues_date;
	private String login_id;
	
	public duesVO(String dues_id,String dues_name,String dues,String dues_date,String login_id) {
		this.dues_id = dues_id;
		this.dues_name = dues_name;
		this.dues = dues;
		this.dues_date = dues_date;
		this.login_id = login_id;
	}
	@Override
	public String toString() {
		return "Information:"+getDues_id()+"/"+getDues_name()+"/"+getDues()+"/"+getDues_date()+"/"+getLogin_id();
		
	}

	public String getDues_id() {
		return dues_id;
	}

	public void setDues_id(String dues_id) {
		this.dues_id = dues_id;
	}

	public String getDues_name() {
		return dues_name;
	}

	public void setDues_name(String dues_name) {
		this.dues_name = dues_name;
	}

	public String getDues() {
		return dues;
	}

	public void setDues(String dues) {
		this.dues = dues;
	}

	public String getDues_date() {
		return dues_date;
	}

	public void setDues_date(String dues_date) {
		this.dues_date = dues_date;
	}

	public String getLogin_id() {
		return login_id;
	}

	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	
}
