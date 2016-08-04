package la.netco.configconsultaprocesos.uilayer.beans.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.liferay.faces.portal.context.LiferayFacesContext;
import com.liferay.portal.security.permission.PermissionChecker;

public class PermissionCheckerUtil {

	public static Boolean  getPermittedToUpdate(String resource, String action) {

		try {
			LiferayFacesContext liferayFacesContext = LiferayFacesContext
					.getInstance();
			PermissionChecker permissionChecker = LiferayFacesContext
					.getInstance().getPermissionChecker();
			return permissionChecker.hasPermission(
					liferayFacesContext.getScopeGroupId(), resource,
					liferayFacesContext.getScopeGroupId(), action);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static Long getCurrentUserId() {
		Long id = null;
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext externalContext = fc.getExternalContext();
		if (externalContext.getUserPrincipal() != null) {
			 id = Long.parseLong(externalContext.getUserPrincipal().getName());
			
		}
		return id;
	}

}
