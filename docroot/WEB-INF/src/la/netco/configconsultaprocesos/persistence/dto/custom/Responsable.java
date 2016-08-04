package la.netco.configconsultaprocesos.persistence.dto.custom;

import java.io.Serializable;

public class Responsable implements Serializable{

	private static final long serialVersionUID = 4459608412296049437L;
	private long userId;
	private String screenName;
	private String emailAddress;
	
	public long getUserId() {
		return userId;
	}
	public String getScreenName() {
		return screenName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj != null && obj.toString().equals(toString())){
			return true;
		}else
			return false;
		
	}
	
	@Override
	public String toString() {
		return ""+userId;
	}
	
}
