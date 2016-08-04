package la.netco.configconsultaprocesos.uilayer.beans;

import la.netco.configconsultaprocesos.uilayer.beans.utils.PermissionCheckerUtil;

import com.liferay.portal.security.permission.ActionKeys;

public class BaseBean {

	private Boolean permittedToUpdate;
	private Boolean permittedToView;
	private Boolean permittedToAdd;
	private Boolean permittedToDelete;
	private String resource;
	
	public BaseBean(String resource){
		this.resource = resource;
	}
	
	public Boolean getPermittedToUpdate() {
		if (permittedToUpdate == null) {
			permittedToUpdate = PermissionCheckerUtil.getPermittedToUpdate(resource, ActionKeys.UPDATE);
		}
		return permittedToUpdate;
	}
	public Boolean getPermittedToView() {
		if (permittedToView== null) {
			permittedToView = PermissionCheckerUtil.getPermittedToUpdate(resource, ActionKeys.VIEW);
		}
		return permittedToView;
	}
	public Boolean getPermittedToDelete() {
		if (permittedToDelete == null) {
			permittedToDelete = PermissionCheckerUtil.getPermittedToUpdate(resource, ActionKeys.DELETE);
		}
		return permittedToDelete;
	}
	public Boolean getPermittedToAdd() {
		if (permittedToAdd == null) {
			permittedToAdd = PermissionCheckerUtil.getPermittedToUpdate(resource, ActionKeys.ADD_RECORD);
		}
		return permittedToAdd;
	}

}
