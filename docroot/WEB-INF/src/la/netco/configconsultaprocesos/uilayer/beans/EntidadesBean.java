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
import javax.faces.model.SelectItem;
import javax.portlet.PortletRequest;

import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
import la.netco.configconsultaprocesos.persistence.dto.Entidad;
import la.netco.configconsultaprocesos.persistence.dto.EntidadEspecialidad;
import la.netco.configconsultaprocesos.persistence.dto.Especialidad;
import la.netco.configconsultaprocesos.persistence.dto.RepositorioDoc;
import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.uilayer.beans.datamodels.GenericDataModel;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.util.bridges.jsf.common.FacesMessageUtil;

@ManagedBean(name = "entidadesBean")
@ViewScoped
public class EntidadesBean extends BaseBean implements Serializable{

	private static final long serialVersionUID = -2566848064546226065L;
	private List<SelectItem> ciudadesItems;
	private String idCiudadSeleccionada;	
	private List<SelectItem> entidadesItems;
	private String idEntidadSeleccionada;		
	private List<SelectItem> especialidadesItems;
	private String idEspecialidadSeleccionada;		
	private List<SelectItem>cadenasConexionItems;
	private List<SelectItem>repositorioItems;
	
	
	private Integer idCadenaConexion;	
	private Integer idRepositorio;	
	
	private String nombrePersonalizado;
	
	private List<Ciudad> ciudadesList;
	
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	
	private EntidadEspecialidadDataModel estidadesDataModel;
	
	private EntidadEspecialidad nuevoRegistro;
	
	private Integer idRegSeleccionado;
	
	private EntidadEspecialidad registroSelecionado;
	
	public static String RESOURCE =  "la.netco.configconsultaprocesos.entidadespecialidad";

	public EntidadesBean(){
		super(RESOURCE);		
	}
	
	@PostConstruct
	public void init(){
		nuevoRegistro = new EntidadEspecialidad();
		cargaFiltrosDataModel();
	}
	
	private static final class EntidadEspecialidadDataModel extends
			GenericDataModel<EntidadEspecialidad, Integer> {
		private static final long serialVersionUID = 1L;

		private EntidadEspecialidadDataModel() {
			super(EntidadEspecialidad.class);
			setOrderBy(Order.asc("nombre"));
		}

		@Override
		protected Object getId(EntidadEspecialidad t) {
			return t.getId();
		}
	}

	public void cargarObjeto(){	
		
		try {
	
			
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");
			   
			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);
			     
			     
			     
			}
				
			if((registroSelecionado == null || registroSelecionado.getId() == null)&&  (idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				registroSelecionado =serviceDao.getEntidadEspecialidadDao().read(idRegSeleccionado);
								
				idCiudadSeleccionada = registroSelecionado.getCiudad().getCodigo();
				idEntidadSeleccionada = registroSelecionado.getEntidad().getCodigo();
				idEspecialidadSeleccionada = registroSelecionado.getEspecialidad().getCodigo();
				idCadenaConexion = registroSelecionado.getCadenaConexion().getId();
				idRepositorio = registroSelecionado.getRepositorio().getId();
			
			}else{
				registroSelecionado = new EntidadEspecialidad();
				idCiudadSeleccionada = null;
				idEntidadSeleccionada = null;
				idEspecialidadSeleccionada = null;
			}
		
		} catch (Exception e) {
		System.err.println(e.getMessage());
		}	
	}
	

	public String  crearRegistro() {
		String page =null;
		try {

			nuevoRegistro.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));			
			nuevoRegistro.setEntidad(serviceDao.getEntidadDao().read(idEntidadSeleccionada));			
			nuevoRegistro.setEspecialidad(serviceDao.getEspecialidadDao().read(idEspecialidadSeleccionada));
			nuevoRegistro.setCadenaConexion(serviceDao.getCadenaConexionDao().read(idCadenaConexion));
		//	nuevoRegistro.setRepositorio(serviceDao.getRepositorioDocDao().read(idRepositorio));
			
			serviceDao.getEntidadEspecialidadDao().create(nuevoRegistro);
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			nuevoRegistro = new EntidadEspecialidad();
		} catch (Exception e) {
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	
	public String  actualizarRegistro() {
		String page =null;
		try {
			System.out.println("valor repo "+idRepositorio);
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));			
			registroSelecionado.setEntidad(serviceDao.getEntidadDao().read(idEntidadSeleccionada));			
			registroSelecionado.setEspecialidad(serviceDao.getEspecialidadDao().read(idEspecialidadSeleccionada));
			registroSelecionado.setCadenaConexion(serviceDao.getCadenaConexionDao().read(idCadenaConexion));
			String b = String.valueOf(idRepositorio);
			System.out.println("repo string  "+b);
			if(b.equals("null") || b==null){
				System.out.println("nulo");
				registroSelecionado.setRepositorio(null);
			}else{
				System.out.println("valor");
				registroSelecionado.setRepositorio(serviceDao.getRepositorioDocDao().read(idRepositorio));
			}
			serviceDao.getEntidadEspecialidadDao().update(registroSelecionado);
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			registroSelecionado = new EntidadEspecialidad();
		} catch (Exception e) {
			System.out.println("error en actualizar"+e);
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String  eliminarRegistro() {
		String page =null;
		try {
			registroSelecionado =serviceDao.getEntidadEspecialidadDao().read(idRegSeleccionado);
			serviceDao.getEntidadEspecialidadDao().delete(registroSelecionado);
			
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			registroSelecionado = new EntidadEspecialidad();
		} catch (Exception e) {
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public String cargaFiltrosDataModel(){
		
    	List<Criterion> filtros = new ArrayList<Criterion>();
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
	    	if(nombrePersonalizado != null  && !nombrePersonalizado.trim().equals("") ){    		
	    		Criterion nombre = Restrictions.ilike("nombre", "%" + nombrePersonalizado + "%");
				filtros.add(nombre);
	    	}
	    	
	    	   	    	
	    	if(idEntidadSeleccionada != null  && !idEntidadSeleccionada.equals("")  ){
	    		filtros.add(Restrictions.eq("entidad.codigo", idEntidadSeleccionada));    	
	    	}
	    	
	   	   	if(idEspecialidadSeleccionada != null && !idEspecialidadSeleccionada.equals("")  ){
	    		filtros.add(Restrictions.eq("especialidad.codigo", idEspecialidadSeleccionada));
	    	}
	   		if(idCiudadSeleccionada!= null  && !idCiudadSeleccionada.equals("")  ){
	    		filtros.add(Restrictions.eq("ciudad.codigo", idCiudadSeleccionada));    	
	    	}else{ 
	    		
	    		
	    		try {
	//    			String filt[] = new String[getCiudadesItems().size()];
	//    			int i=0;
	    			Disjunction disjunction=Restrictions.disjunction();
					for(SelectItem e : getCiudadesItems()){
	//					filt[i]= e.getValue().toString();
						disjunction.add(Restrictions.eq("ciudad.codigo", e.getValue().toString()));
	//					i++;
					}
					filtros.add(disjunction);
	//				filtros.add(Restrictions.eq("ciudad.codigo", filt));
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	   	   	
	   	   	estidadesDataModel = new EntidadEspecialidadDataModel();    	
	    	estidadesDataModel.setFiltros(filtros);
    	}
    	
    	return "";
    }
	
	public List<SelectItem> getCiudadesItems() throws SystemException {
		FacesContext context = FacesContext.getCurrentInstance();
		PortletRequest portletRequest = (PortletRequest) context.getExternalContext().getRequest();

		ThemeDisplay  themeDisplay = (ThemeDisplay) portletRequest.getAttribute(WebKeys.THEME_DISPLAY);
		long roles[];
		roles=themeDisplay.getUser().getRoleIds();
		boolean flg=false;
		for(Long rol : roles){
			
			if(rol==10209){
				flg=true;
				
			}
		}
		
		if (ciudadesItems == null) {
			try {
				List<Ciudad> allCiudades = serviceDao.getCiudadDao().loadAll(
						Ciudad.class);
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
							if(ciudad.getOrganizacion().equals(orga.toString())){
								ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad
								.getNombre()));
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return ciudadesItems;
	}
	public List<SelectItem> getEntidadesItems() {
		if (entidadesItems == null) {
			try {
				List<Entidad> alldata =serviceDao.getEntidadDao().loadAll(Entidad.class);
				entidadesItems = new ArrayList<SelectItem>();
				for (Entidad dato : alldata) {
					entidadesItems.add(new SelectItem(dato.getCodigo(), dato.getNombre()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return entidadesItems;
	}
	public List<SelectItem> getEspecialidadesItems() {
		if (especialidadesItems == null) {
			try {
				List<Especialidad> alldata =serviceDao.getEspecialidadDao().loadAll(Especialidad.class);
				Collections.sort(alldata, new Comparator<Especialidad>(){
					 
					@Override
					public int compare(Especialidad o1, Especialidad o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
					
				});
				especialidadesItems = new ArrayList<SelectItem>();
				for (Especialidad dato : alldata) {
					especialidadesItems.add(new SelectItem(dato.getCodigo(), dato.getNombre()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return especialidadesItems;
	}

	public String getIdCiudadSeleccionada() {
		return idCiudadSeleccionada;
	}

	public void setCiudadesItems(List<SelectItem> ciudadesItems) {
		this.ciudadesItems = ciudadesItems;
	}

	public void setIdCiudadSeleccionada(String idCiudadSeleccionada) {
		this.idCiudadSeleccionada = idCiudadSeleccionada;
	}



	public String getIdEntidadSeleccionada() {
		return idEntidadSeleccionada;
	}


	public String getIdEspecialidadSeleccionada() {
		return idEspecialidadSeleccionada;
	}

	public void setEntidadesItems(List<SelectItem> entidadesItems) {
		this.entidadesItems = entidadesItems;
	}

	public void setIdEntidadSeleccionada(String idEntidadSeleccionada) {
		this.idEntidadSeleccionada = idEntidadSeleccionada;
	}

	public void setEspecialidadesItems(List<SelectItem> especialidadesItems) {
		this.especialidadesItems = especialidadesItems;
	}

	public void setIdEspecialidadSeleccionada(String idEspecialidadSeleccionada) {
		this.idEspecialidadSeleccionada = idEspecialidadSeleccionada;
	}

	public String getNombrePersonalizado() {
		return nombrePersonalizado;
	}

	public void setNombrePersonalizado(String nombrePersonalizado) {
		this.nombrePersonalizado = nombrePersonalizado;
	}

	public List<Ciudad> getCiudadesList() {
		if(ciudadesList != null){
			ciudadesList = serviceDao.getCiudadDao().loadAll(Ciudad.class);
		}
		return ciudadesList;
	}

	public void setCiudadesList(List<Ciudad> ciudadesList) {
		this.ciudadesList = ciudadesList;
	}

	public EntidadEspecialidadDataModel getEstidadesDataModel() {
	///	cargaFiltrosDataModel();
		return estidadesDataModel;
	}

	public void setEstidadesDataModel(EntidadEspecialidadDataModel estidadesDataModel) {
		this.estidadesDataModel = estidadesDataModel;
	}

	public EntidadEspecialidad getNuevoRegistro() {
		return nuevoRegistro;
	}

	public void setNuevoRegistro(EntidadEspecialidad nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}

	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}

	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}

	public EntidadEspecialidad getRegistroSelecionado() {
		return registroSelecionado;
	}

	public void setRegistroSelecionado(EntidadEspecialidad registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
	}
	public ServiceDao getServiceDao() {
		return serviceDao;
	}
	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}
	@SuppressWarnings("unchecked")
	public List<SelectItem> getCadenasConexionItems() {
		if(idCiudadSeleccionada != null){
			try {
				
				List<CadenaConexion> alldata=serviceDao.getCadenaConexionDao().loadAll(CadenaConexion.class);
				cadenasConexionItems = new ArrayList<SelectItem>();
				for (CadenaConexion dato : alldata) {	
//					System.out.println("codigo  '"+dato.getAlcance().getCodigo()+"");
						if(dato.getAlcance().getCodigo().trim().equals("2")){	//nacional
							cadenasConexionItems.add(new SelectItem(dato.getId(), dato.getIpBaseDatos().trim() + " - " + dato.getNombreBaseDatos().trim()));
						}else{
							if(dato.getAlcance().getCodigo().trim().equals("1")){//departamental
								if(dato.getCiudad().getCodigo().substring(0, 2).equals(idCiudadSeleccionada.subSequence(0, 2))){								
									cadenasConexionItems.add(new SelectItem(dato.getId(), dato.getIpBaseDatos().trim() + " - " + dato.getNombreBaseDatos().trim()));
								}
							}
						}
				}
				
				Map<Integer, String> params = new HashMap<Integer, String>();
				params.put(0, idCiudadSeleccionada);
				
				List<CadenaConexion> dataciudad =(List<CadenaConexion>) serviceDao.getCadenaConexionDao().executeFind(CadenaConexion.class, params, CadenaConexion.NAMED_QUERY_FIND_BY_ID_CIUDAD);
//				cadenasConexionItems = new ArrayList<SelectItem>();
				for (CadenaConexion dato : dataciudad) {
					cadenasConexionItems.add(new SelectItem(dato.getId(), dato.getIpBaseDatos().trim() + " - " + dato.getNombreBaseDatos().trim()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cadenasConexionItems;
	}
	
	public String limpiar(){
		nombrePersonalizado=null;
		idCiudadSeleccionada=null;
		idEntidadSeleccionada=null;
		idEspecialidadSeleccionada=null;
		
		return cargaFiltrosDataModel();
	}
	public Integer getIdCadenaConexion() {
		return idCadenaConexion;
	}
	public void setCadenasConexionItems(List<SelectItem> cadenasConexionItems) {
		this.cadenasConexionItems = cadenasConexionItems;
	}
	public void setIdCadenaConexion(Integer idCadenaConexion) {
		this.idCadenaConexion = idCadenaConexion;
	}

	
	@SuppressWarnings("unchecked")
	public List<SelectItem> getRepositorioItems() {
		System.out.println("ciudaaaad "+idCiudadSeleccionada);
		
		if(idCiudadSeleccionada != null){
			try {
				
				List<RepositorioDoc> alldata=serviceDao.getRepositorioDocDao().loadAll(RepositorioDoc.class);
				repositorioItems = new ArrayList<SelectItem>();
				for (RepositorioDoc dato : alldata) {	
//					System.out.println("repos  "+dato.getId()+" "+dato.getAlcance().getCodigo()+"");
					String etq=dato.getIpPrivada()+"/"+dato.getCarpeta();
					
						if(dato.getAlcance().getCodigo().trim().equals("2")){	//nacional
							repositorioItems.add(new SelectItem(dato.getId(), etq));
						}else{
							if(dato.getAlcance().getCodigo().trim().equals("1")){//departamental
								if(dato.getCiudad().getCodigo().substring(0, 2).equals(idCiudadSeleccionada.subSequence(0, 2))){
									
									repositorioItems.add(new SelectItem(dato.getId(), etq));
								}
							}
						}
				}
				System.out.println("2ciudaaaad "+idCiudadSeleccionada);
				Map<Integer, String> params = new HashMap<Integer, String>();
				params.put(0, idCiudadSeleccionada);
				
				List<RepositorioDoc> dataciudad =(List<RepositorioDoc>) serviceDao.getRepositorioDocDao().executeFind(RepositorioDoc.class, params, RepositorioDoc.NAMED_QUERY_FIND_BY_ID_CIUDAD);
//				cadenasConexionItems = new ArrayList<SelectItem>();
				for (RepositorioDoc dato : dataciudad) {
					if("0".equals(dato.getAlcance().getCodigo().trim())){
//						System.out.println("reposciud "+dato.getId()+" "+dato.getAlcance().getCodigo()+"");
						String etq=dato.getIpPrivada()+"/"+dato.getCarpeta();
						
						repositorioItems.add(new SelectItem(dato.getId(), etq));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return repositorioItems;
	}

	public void setRepositorioItems(List<SelectItem> repositorioItems) {
		this.repositorioItems = repositorioItems;
	}

	public Integer getIdRepositorio() {
		return idRepositorio;
	}

	public void setIdRepositorio(Integer idRepositorio) {
		this.idRepositorio = idRepositorio;
	}
	
	
	

}
