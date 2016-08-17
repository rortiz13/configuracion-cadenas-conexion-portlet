package la.netco.configconsultaprocesos.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;

import la.netco.configconsultaprocesos.persistence.dto.Alcance;
import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
import la.netco.configconsultaprocesos.persistence.dto.custom.Responsable;
import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.uilayer.beans.datamodels.GenericDataModel;
import la.netco.configconsultaprocesos.uilayer.beans.utils.CadenaConexionUtil;
import la.netco.configconsultaprocesos.uilayer.beans.utils.CopyObjects;
import la.netco.configconsultaprocesos.uilayer.beans.utils.PermissionCheckerUtil;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.SortFactoryUtil;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.usersadmin.util.UsersAdminUtil;
import com.liferay.util.bridges.jsf.common.FacesMessageUtil;
import com.liferay.util.bridges.jsf.common.JSFPortletUtil;
import com.sun.faces.facelets.tag.jstl.core.SetHandler;

@ManagedBean(name = "cadenaConexionBean")
@ViewScoped
public class CadenasConexionBean  extends BaseBean implements Serializable{


	private static final long serialVersionUID = 1L;
	private List<SelectItem> ciudadesItems;
	private List<SelectItem> alcanceItems;
	private String idCiudadSeleccionada;
	private String idOrganizacion;
	private String idAlcance;
	private String direccionIP;
		
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;

	private CadenaConexionDataModel listDataModel;
	
	private CadenaConexion nuevoRegistro;
	
	private Integer idRegSeleccionado;
	
	private CadenaConexion registroSelecionado;
	
	private List<Responsable> responsables = new ArrayList<Responsable>();
	
	private String resSeleccionado;
	
	private boolean conexionexitosa;
	private boolean esadmin;
	private boolean probarconexion = false;

	public static String RESOURCE =  "la.netco.configconsultaprocesos.cadenaconexion";

	public CadenasConexionBean(){
		super(RESOURCE);		
	}

	
	private List<String> autocompleteList;
	
	@PostConstruct
	public void init(){
		nuevoRegistro = new CadenaConexion();
		cargaFiltrosDataModel();
	}
	
	private static final class CadenaConexionDataModel extends
			GenericDataModel<CadenaConexion, Integer> {

		private static final long serialVersionUID = 1L;

		private CadenaConexionDataModel() {
			super(CadenaConexion.class);
			setOrderBy(Order.asc("id"));
		}

		@Override
		protected Object getId(CadenaConexion t) {
			return t.getId();
		}
	}

	

	@SuppressWarnings("unchecked")
	public List<String> autocomplete(String prefix) {
		
		List<String> emailUsers  = new ArrayList<String>();
		
		try {

			Sort sort = SortFactoryUtil.getSort(User.class, "screen-name","asc");

			Hits resultsHits = UserLocalServiceUtil.search(JSFPortletUtil
					.getCompanyId(FacesContext.getCurrentInstance()), null,
					null, null, prefix, prefix,
					WorkflowConstants.STATUS_APPROVED, null, false, 0, 100,
					sort);

			Tuple tuple = UsersAdminUtil.getUsers(resultsHits);
			List<User> resultsUsers  = new ArrayList<User>();
			boolean corruptIndex = (Boolean) tuple.getObject(1);
			if (!corruptIndex) {
				 resultsUsers = (List<User>) tuple.getObject(0);
				 for (User user : resultsUsers) {
					 emailUsers.add(user.getEmailAddress());
				}
			}

		} catch (SystemException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		}

		return emailUsers;
	}
	
	
	public void seleccionarUsuario(ActionEvent action){
		
		
		try {
			User userLiferay = UserLocalServiceUtil.getUserByEmailAddress(JSFPortletUtil.getCompanyId(FacesContext.getCurrentInstance()), resSeleccionado);
			
			Responsable responsable = CopyObjects.copyUserObjetToResponsable(userLiferay);
			
			if(!responsables.contains(responsable)){
				responsables.add(responsable);
			}
			
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}
	
	public void quitarUsuario(Responsable responsable){
		
		try {
			if(responsables.contains(responsable)){
				responsables.remove(responsable);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void cargarObjeto(){	
	
		if(idRegSeleccionado == null){
			 FacesContext facesContext = FacesContext.getCurrentInstance();
		     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
		     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
		}
	
		if((registroSelecionado == null || registroSelecionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
			registroSelecionado = serviceDao.getCadenaConexionDao().read(idRegSeleccionado);
			
			Map<Integer, Integer> params = new HashMap<Integer, Integer>();
			params.put(0, idRegSeleccionado);
			
			idAlcance=registroSelecionado.getAlcance().getCodigo();
			idCiudadSeleccionada = registroSelecionado.getCiudad().getCodigo();

		
		}else{
//			registroSelecionado = new CadenaConexion();
//			idCiudadSeleccionada = null;

		}
	}
	
	public String  crearRegistro() {
		String page =null;
		try {
			System.out.println("Inicia Probando Conexion");
			probarCadenaConexionNuevo();
			System.out.println("Valor de Habilitado: "+nuevoRegistro.getHabilitado());
			nuevoRegistro.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));
			nuevoRegistro.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			System.out.println("Marcela 2 ");
			String code=serviceDao.getCiudadDao().read(idCiudadSeleccionada).getOrganizacion();
			System.out.println("Marcela 3 "+code);
			
			
			StringBuffer dataSource = new StringBuffer();
			dataSource.append("Data Source=" + nuevoRegistro.getIpBaseDatos().trim());
			dataSource.append(";Initial Catalog=" + nuevoRegistro.getNombreBaseDatos().trim());		
			dataSource.append(";Persist Security Info=True;User ID="+nuevoRegistro.getUsuarioBaseDatos().trim());	
			dataSource.append(";Password=" + nuevoRegistro.getContrasenaBaseDatos().trim());	
			System.out.println("2 ");
			nuevoRegistro.setCadenaConexion(dataSource.toString());
			System.out.println("3 ");
			int i=serviceDao.getCadenaConexionDao().create(nuevoRegistro);
			System.out.println("4 ");		
//			Integer id_cadena = serviceDao.getCadenaConexionDao().create(nuevoRegistro);
//			
//			
//			if(id_cadena != null && !id_cadena.equals(0)){				
//				
//				for (Responsable res : responsables) {
//					CadenaUsuario cadenaUsuario = new CadenaUsuario(id_cadena, res.getUserId());
//					serviceDao.getCadenaUsuarioDao().create(cadenaUsuario);
//				}
//				
//			}		
//			
			
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			System.out.println("Valor de Habilitado: "+nuevoRegistro.getHabilitado());
			nuevoRegistro = new CadenaConexion();
		} catch (Exception e) {
			System.out.println("errocito "+e);
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String  actualizarRegistro() {
		String page =null;
		try {
			
			boolean flg=false;
			System.out.println("Inicia Probando Conexion(Actualizar)");
			probarCadenaConexionActualizar();
			System.out.println("Valor de Habilitado: "+registroSelecionado.getHabilitado());
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			registroSelecionado.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));
			String code=serviceDao.getCiudadDao().read(idCiudadSeleccionada).getOrganizacion();
			System.out.println("Marcela  "+code);
			
			
			
			StringBuffer dataSource = new StringBuffer();
			dataSource.append("Data Source=" + registroSelecionado.getIpBaseDatos().trim());
			dataSource.append(";Initial Catalog=" + registroSelecionado.getNombreBaseDatos().trim());		
			dataSource.append(";Persist Security Info=True;User ID="+registroSelecionado.getUsuarioBaseDatos().trim());	
			dataSource.append(";Password=" + registroSelecionado.getContrasenaBaseDatos().trim());		
			registroSelecionado.setCadenaConexion(dataSource.toString());
			System.out.println(registroSelecionado.getMailResponsable());
			serviceDao.getCadenaConexionDao().update(registroSelecionado);
	
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			
			return cargaFiltrosDataModel();
//			registroSelecionado = new CadenaConexion();
		} catch (Exception e) {
			System.out.println("error al actualizar "+e);
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String  eliminarRegistro() {
		String page =null;
		try {
			
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			String code=serviceDao.getCiudadDao().read(idCiudadSeleccionada).getOrganizacion();
		
			registroSelecionado = serviceDao.getCadenaConexionDao().read(idRegSeleccionado);
			serviceDao.getCadenaConexionDao().delete(registroSelecionado);
			
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			registroSelecionado = new CadenaConexion();
		} catch (Exception e) {
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String cargaFiltrosDataModel(){
    	List<Criterion> filtros = new ArrayList<Criterion>();
    
    	Map<String, String> alias = new HashMap<String, String>();
		
    	boolean hasUpdateAll = PermissionCheckerUtil.getPermittedToUpdate(RESOURCE, "UPDATE_ALL");
    	int tamanho = 0;
    	try {
    		tamanho = getCiudadesItems().size();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Error: "+e1.getMessage());
		}
    	if(tamanho > 0)
    	{
	    	if(!hasUpdateAll){
	    		filtros.add(Restrictions.eq("res.compositePK.id_usuario", PermissionCheckerUtil.getCurrentUserId()));    	
	    		alias.put("responsables", "res");	
	    	}    	
	    	try {
					if(direccionIP != null  && !direccionIP.trim().equals("") ){    		
						Criterion nombre = Restrictions.eq("ipBaseDatos",  direccionIP);
						filtros.add(nombre);
					}    	
					System.out.println("valor de ciudad al filtrar"+idCiudadSeleccionada);
					if(idCiudadSeleccionada!= null  && !idCiudadSeleccionada.equals("")  ){
						if(!getCiudadesItems().isEmpty())
							filtros.add(Restrictions.eq("ciudad.codigo", idCiudadSeleccionada));    	
					}else{ 
						try {
							Disjunction disjunction=Restrictions.disjunction();
							for(SelectItem e : getCiudadesItems()){
								disjunction.add(Restrictions.eq("ciudad.codigo", e.getValue().toString()));
							}
							filtros.add(disjunction);
						} catch (SystemException e) {
							e.printStackTrace();
						}
					}
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			listDataModel = new CadenaConexionDataModel();    
	    	listDataModel.setAlias(alias);
	    	listDataModel.setFiltros(filtros);
    	}
    	return "listado";
    }
	
	public List<SelectItem> getCiudadesItems() throws SystemException {
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

		ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long roles[];
		roles=themeDisplay.getUser().getRoleIds();
		boolean flg=false;
		for(Long rol : roles){
//			System.out.println("rol "+rol);
			if(rol==10209){
				flg=true;
				
			}
		}
		
		if (ciudadesItems == null) {
			try {
				List<Ciudad> allCiudades = serviceDao.getCiudadDao().loadAll(Ciudad.class);
				Collections.sort(allCiudades, new Comparator<Ciudad>(){
					 
					@Override
					public int compare(Ciudad o1, Ciudad o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
					
				});
				ciudadesItems = new ArrayList<SelectItem>();
				for (Ciudad ciudad : allCiudades) {
					if(flg){
						ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad
								.getNombre()));
					}else{					
						long organizacion[];
						organizacion=themeDisplay.getUser().getOrganizationIds();
							for(Long orga : organizacion){
								System.out.println("ID ORGA: "+orga.toString());
								if(orga.toString() != null)
								{
									if(ciudad.getOrganizacion().equals(orga.toString())){
										ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad.getNombre()));
										idOrganizacion = orga.toString();
										System.out.println("ID ORGA: "+idOrganizacion);
									}
								}								
							}
					}
				}

			} catch (Exception e) {
				System.out.println("-----------------------error "+e);
				e.printStackTrace();
			}

		}

		return ciudadesItems;
	}
	
	public List<SelectItem> getAlcanceItems() throws SystemException {
		
			try {
				List<Alcance> allAlcance = serviceDao.getAlcanceDao().loadAll(
						Alcance.class);
				
				alcanceItems = new ArrayList<SelectItem>();
				for (Alcance alcance : allAlcance) {					
					alcanceItems.add(new SelectItem(alcance.getCodigo(), alcance.getNombre()));
				}

			} catch (Exception e) {
				System.out.println("-----------------------error "+e);
				e.printStackTrace();
			}	

		return alcanceItems;
	}
	public String limpiar() throws SystemException{
		idCiudadSeleccionada=null;
		direccionIP=null;
		
		return cargaFiltrosDataModel();
	}

	public void probarCadenaConexionNuevo(){
		System.out.println("probar1");
		try {
			
			if(nuevoRegistro == null || nuevoRegistro.getContrasenaBaseDatos() == null || nuevoRegistro.getContrasenaBaseDatos().trim().equals("")){
				setConexionexitosa(false);
				setProbarconexion(true);	
				nuevoRegistro.setHabilitado(false);
				return;
			}
			System.out.println("probar2");
			StringBuffer stringConnection = new StringBuffer();
			stringConnection.append("jdbc:jtds:sqlserver://" + nuevoRegistro.getIpBaseDatos().trim());
			stringConnection.append("/" + nuevoRegistro.getNombreBaseDatos().trim());		
			setConexionexitosa(CadenaConexionUtil.testJTDSConnection(stringConnection.toString(), nuevoRegistro.getUsuarioBaseDatos().trim(), nuevoRegistro.getContrasenaBaseDatos().trim()));
			nuevoRegistro.setHabilitado(CadenaConexionUtil.testJTDSConnection(stringConnection.toString(), nuevoRegistro.getUsuarioBaseDatos().trim(), nuevoRegistro.getContrasenaBaseDatos().trim()));
			setProbarconexion(true);	
			System.out.println("probar3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void probarCadenaConexionActualizar(){
		
		try {
			
			if(registroSelecionado == null || registroSelecionado.getContrasenaBaseDatos() == null || registroSelecionado.getContrasenaBaseDatos().trim().equals("")){
				setConexionexitosa(false);
				setProbarconexion(true);	
				return;
			}
				
			
			StringBuffer stringConnection = new StringBuffer();
			stringConnection.append("jdbc:jtds:sqlserver://" + registroSelecionado.getIpBaseDatos().trim());
			stringConnection.append("/" + registroSelecionado.getNombreBaseDatos().trim());
		
			
			setConexionexitosa(CadenaConexionUtil.testJTDSConnection(stringConnection.toString(), registroSelecionado.getUsuarioBaseDatos().trim(), registroSelecionado.getContrasenaBaseDatos().trim()));
			registroSelecionado.setHabilitado(CadenaConexionUtil.testJTDSConnection(stringConnection.toString(), registroSelecionado.getUsuarioBaseDatos().trim(), registroSelecionado.getContrasenaBaseDatos().trim()));
			setProbarconexion(true);	
		} catch (Exception e) {			
			e.printStackTrace();
		}
	
	}
	
	
	public String getIdCiudadSeleccionada() {
		return idCiudadSeleccionada;
	}
	
	public String getIdAlcance() {
		return idAlcance;
	}


	public void setCiudadesItems(List<SelectItem> ciudadesItems) {
		this.ciudadesItems = ciudadesItems;
	}
	
	public void setAlcanceItems(List<SelectItem> alcanceItems) {
		this.alcanceItems = alcanceItems;
	}

	public void setIdCiudadSeleccionada(String idCiudadSeleccionada) {
		this.idCiudadSeleccionada = idCiudadSeleccionada;
	}


	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}
	public CadenaConexionDataModel getListDataModel() {
		//cargaFiltrosDataModel();
		return listDataModel;
	}

	public void setListDataModel(CadenaConexionDataModel estidadesDataModel) {
		this.listDataModel = estidadesDataModel;
	}

	public CadenaConexion getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(CadenaConexion nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public CadenaConexion getRegistroSelecionado() {
		return registroSelecionado;
	}

	public void setRegistroSelecionado(CadenaConexion registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
	}

	public String getDireccionIP() {
		return direccionIP;
	}

	public void setDireccionIP(String direccionIP) {
		this.direccionIP = direccionIP;
	}

	public List<String> getAutocompleteList() {
		return autocompleteList;
	}

	public void setAutocompleteList(List<String> autocompleteList) {
		this.autocompleteList = autocompleteList;
	}


	public List<Responsable> getResponsables() {
		return responsables;
	}


	public void setResponsables(List<Responsable> responsables) {
		this.responsables = responsables;
	}


	public String getResSeleccionado() {
		return resSeleccionado;
	}


	public void setResSeleccionado(String resSeleccionado) {
		this.resSeleccionado = resSeleccionado;
	}


	public boolean isProbarconexion() {
		return probarconexion;
	}


	public void setProbarconexion(boolean probarconexion) {
		this.probarconexion = probarconexion;
	}


	public boolean isConexionexitosa() {
		return conexionexitosa;
	}


	public void setConexionexitosa(boolean conexionexitosa) {
		this.conexionexitosa = conexionexitosa;
	}
	
	public void setIdAlcance(String idAlcance) {
		
		this.idAlcance = idAlcance;
	}


	public boolean isEsadmin() throws SystemException {
		esadmin = false;
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();
		ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long roles[];
		roles=themeDisplay.getUser().getRoleIds();
		for(Long rol : roles){
			if(rol==10209)
			{
				esadmin = true;
				setEsadmin(true);				
			}
		}
		
		return esadmin;
	}


	public void setEsadmin(boolean esadmin) {
		this.esadmin = esadmin;
	}


	public String getIdOrganizacion() {
		return idOrganizacion;
	}


	public void setIdOrganizacion(String idOrganizacion) {
		this.idOrganizacion = idOrganizacion;
	}
	
	
	
	

}
