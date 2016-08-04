package la.netco.configconsultaprocesos.uilayer.beans.utils;

import la.netco.configconsultaprocesos.persistence.dto.custom.Responsable;

import com.liferay.portal.model.User;

public class CopyObjects {

	
	public static Responsable copyUserObjetToResponsable(User userLiferay){
		Responsable responsable = new Responsable();
		responsable.setUserId(userLiferay.getUserId());
		responsable.setEmailAddress(userLiferay.getEmailAddress());
		responsable.setScreenName(userLiferay.getScreenName());
		return responsable;
	}
}
