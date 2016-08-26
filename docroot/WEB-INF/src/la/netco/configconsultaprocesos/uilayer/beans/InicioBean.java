package la.netco.configconsultaprocesos.uilayer.beans;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;


@ManagedBean(name = "inicioBean")
@ViewScoped
public class InicioBean extends BaseBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InicioBean(String resource) {
		super(resource);
		// TODO Auto-generated constructor stub
	}
	
	public boolean esAdmin(){
		boolean band = false;
		
		//Chequear si el usuario es admin
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();
		ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long _roles[];
		try {
			_roles=themeDisplay.getUser().getRoleIds();
			for(Long rol : _roles){				
				if(rol == 10209){
					band = true;
				}
			}
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return band;
	}
}
