package la.netco.configconsultaprocesos.uilayer.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import la.netco.configconsultaprocesos.persistence.dto.Auditoria;
import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
import la.netco.configconsultaprocesos.persistence.dto.UsuarioCiudad;
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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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

@ManagedBean(name = "cadenaConexionBean")
@ViewScoped
public class CadenasConexionBean  extends BaseBean implements Serializable{

	private static Log _log = LogFactoryUtil.getLog(CadenasConexionBean.class);
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
	
	//-- Objeto de Auditoria --//	
	private Auditoria registroAuditoria;

	public static String RESOURCE =  "la.netco.configconsultaprocesos.cadenaconexion";

	public CadenasConexionBean(){
		super(RESOURCE);		
	}
	
	//---- Metodos de objeto Auditoria --------//
	public Auditoria getRegistroAuditoria() {
		return registroAuditoria;
	}


	public void setRegistroAuditoria(Auditoria registroAuditoria) {
		this.registroAuditoria = registroAuditoria;
	}	
	//----- fin Metodos de objeto Auditoria -----//


	private List<String> autocompleteList;
	
	@PostConstruct
	public void init(){
		nuevoRegistro = new CadenaConexion();
		registroAuditoria = new Auditoria();
		cargaFiltrosDataModel();
	}
	
	private static final class CadenaConexionDataModel extends
			GenericDataModel<CadenaConexion, Integer> {

		private static final long serialVersionUID = 1L;

		private CadenaConexionDataModel() {
			super(CadenaConexion.class);
			setOrderBy(Order.asc("ciudad"));
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

			registroAuditoria = new Auditoria();
		
		}else{
//			registroSelecionado = new CadenaConexion();
//			idCiudadSeleccionada = null;

		}
	}
	
	public String  crearRegistro() {
		String page =null;
		String cadenaActual = null;
		try {
			System.out.println("Inicia Probando Conexion");
			probarCadenaConexionNuevo();
			System.out.println("Valor de Habilitado: "+nuevoRegistro.getHabilitado());
			nuevoRegistro.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));
			nuevoRegistro.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			System.out.println("Marcela 2 ");
			
			StringBuffer dataSource = new StringBuffer();
			dataSource.append("Data Source=" + nuevoRegistro.getIpBaseDatos().trim());
			dataSource.append(";Initial Catalog=" + nuevoRegistro.getNombreBaseDatos().trim());		
			dataSource.append(";Persist Security Info=True;User ID="+nuevoRegistro.getUsuarioBaseDatos().trim());	
			dataSource.append(";Password=" + nuevoRegistro.getContrasenaBaseDatos().trim());	
			System.out.println("2 ");
			nuevoRegistro.setCadenaConexion(dataSource.toString());
			System.out.println("3 ");
			int i = serviceDao.getCadenaConexionDao().create(nuevoRegistro);
			System.out.println("4 ");	
			
			/**/
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
			
			//------------ Creacion de registro en Auditoria ------------------//
			FacesContext context = FacesContext.getCurrentInstance();
			PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

			ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long userID = themeDisplay.getUser().getUserId();
			
			
			cadenaActual = nuevoRegistro.getIpBaseDatos().trim()+", ";
			cadenaActual += nuevoRegistro.getCiudad().getCodigo().trim()+", ";
			cadenaActual += nuevoRegistro.getNombreBaseDatos().trim()+", ";
			cadenaActual += nuevoRegistro.getUsuarioBaseDatos().trim()+", ";
			cadenaActual += nuevoRegistro.getContrasenaBaseDatos().trim()+", ";
			cadenaActual += nuevoRegistro.getHabilitado()+", ";
			cadenaActual += nuevoRegistro.getCadenaConexion()+", ";
			cadenaActual += nuevoRegistro.getMailResponsable();
			
			registroAuditoria.setFecha(new Date());
			registroAuditoria.setIdUsuario((int)(userID));
			registroAuditoria.setIdCadena(i);
			registroAuditoria.setAccion(1);
			registroAuditoria.setCadenaAnterior("");
			registroAuditoria.setCadenaActual(cadenaActual);
			int id_auditoria = serviceDao.getAuditoriaDao().create(registroAuditoria);
			
			registroAuditoria = new Auditoria();
			
			//------------ Fin de Creacion de registro de Auditoria ---------------//
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
		String cadenaActual = null, cadenaAnterior = null;
		try {
			
			boolean flg=false;
			System.out.println("Inicia Probando Conexion(Actualizar)");
			probarCadenaConexionActualizar();
			
			CadenaConexion registroAnterior = serviceDao.getCadenaConexionDao().read(idRegSeleccionado);
			
			System.out.println("Valor de Habilitado: "+registroSelecionado.getHabilitado());
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			registroSelecionado.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));	
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
			
			//------------ Creacion de registro en Auditoria ------------------//
			
			FacesContext context = FacesContext.getCurrentInstance();
			PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

			ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long userID = themeDisplay.getUser().getUserId();
			
			cadenaActual = registroSelecionado.getIpBaseDatos().trim()+", ";
			cadenaActual += registroSelecionado.getCiudad().getCodigo().trim()+", ";
			cadenaActual += registroSelecionado.getNombreBaseDatos().trim()+", ";
			cadenaActual += registroSelecionado.getUsuarioBaseDatos().trim()+", ";
			cadenaActual += registroSelecionado.getContrasenaBaseDatos().trim()+", ";
			cadenaActual += registroSelecionado.getHabilitado()+", ";
			cadenaActual += registroSelecionado.getCadenaConexion()+", ";
			cadenaActual += registroSelecionado.getMailResponsable();
			
			cadenaAnterior = registroAnterior.getIpBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getCiudad().getCodigo().trim()+", ";
			cadenaAnterior += registroAnterior.getNombreBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getUsuarioBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getContrasenaBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getHabilitado()+", ";
			cadenaAnterior += registroAnterior.getCadenaConexion()+", ";
			cadenaAnterior += registroAnterior.getMailResponsable();
			
			_log.info("Registro: "+cadenaActual+" ... "+cadenaAnterior);
			
			registroAuditoria.setFecha(new Date());
			registroAuditoria.setIdUsuario((int)(userID));
			registroAuditoria.setIdCadena(idRegSeleccionado);
			registroAuditoria.setAccion(3);
			registroAuditoria.setCadenaAnterior(cadenaAnterior);
			registroAuditoria.setCadenaActual(cadenaActual);
			int id_auditoria = serviceDao.getAuditoriaDao().create(registroAuditoria);
			
			registroAuditoria = new Auditoria();
			
			//------------ Fin de Creacion de registro de Auditoria ---------------//
			
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
		String cadenaAnterior = null;
		try {
			
			CadenaConexion registroAnterior = serviceDao.getCadenaConexionDao().read(idRegSeleccionado);
			
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			registroSelecionado = serviceDao.getCadenaConexionDao().read(idRegSeleccionado);
			serviceDao.getCadenaConexionDao().delete(registroSelecionado);
			
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			
			//------------ Creacion de registro en Auditoria ------------------//
			
			FacesContext context = FacesContext.getCurrentInstance();
			PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

			ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
			long userID = themeDisplay.getUser().getUserId();
			
			cadenaAnterior = registroAnterior.getIpBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getCiudad().getCodigo().trim()+", ";
			cadenaAnterior += registroAnterior.getNombreBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getUsuarioBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getContrasenaBaseDatos().trim()+", ";
			cadenaAnterior += registroAnterior.getHabilitado()+", ";
			cadenaAnterior += registroAnterior.getCadenaConexion()+", ";
			cadenaAnterior += registroAnterior.getMailResponsable();
			
			registroAuditoria.setFecha(new Date());
			registroAuditoria.setIdUsuario((int)(userID));
			registroAuditoria.setIdCadena(idRegSeleccionado);
			registroAuditoria.setAccion(2);
			registroAuditoria.setCadenaAnterior(cadenaAnterior);
			registroAuditoria.setCadenaActual("");
			int id_auditoria = serviceDao.getAuditoriaDao().create(registroAuditoria);
			
			registroAuditoria = new Auditoria();
			
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
		System.out.println("Entro en getCiudadesItem");
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

		ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long roles[], userID;
		roles = themeDisplay.getUser().getRoleIds();
		userID = themeDisplay.getUser().getUserId();
		boolean flg=false;
		for(Long rol : roles){
			if(rol==10209){
				flg=true;			
			}
		}
		
		if (ciudadesItems == null) {
			try {
				
				List<Ciudad> allCiudades = serviceDao.getCiudadDao().loadAll(Ciudad.class);
				System.out.println("Es vacia "+allCiudades.isEmpty());
				Collections.sort(allCiudades, new Comparator<Ciudad>(){
					 
					@Override
					public int compare(Ciudad o1, Ciudad o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
					
				});
				ciudadesItems = new ArrayList<SelectItem>();
				if(flg)
				{
					for (Ciudad ciudad : allCiudades) {
						ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad.getNombre()));
					}
				}
				else
				{
					List<UsuarioCiudad> allUsuarioCiudad = serviceDao.getUsuarioCiudadDao().loadAll(UsuarioCiudad.class);
					for(UsuarioCiudad usuciud: allUsuarioCiudad)
					{
						if((usuciud.getUserid().trim()).equals(String.valueOf(userID).trim()))
						{
							for (Ciudad ciudad : allCiudades) 
							{
								if(usuciud.getCiudad().equals(ciudad.getCodigo()))
								{
									String codigo_departamento = ciudad.getDepartamento();
									for (Ciudad ciudad2 : allCiudades) 
									{
										if(codigo_departamento.equals(ciudad2.getDepartamento()))
										{
											ciudadesItems.add(new SelectItem(ciudad2.getCodigo(), ciudad2.getNombre()));
										}
									}
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
				List<Alcance> allAlcance = serviceDao.getAlcanceDao().loadAll(Alcance.class);				
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
