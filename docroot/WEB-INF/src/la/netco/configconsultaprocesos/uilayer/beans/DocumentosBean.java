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

import la.netco.configconsultaprocesos.persistence.dto.Alcance;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
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



@ManagedBean(name = "documentosBean")
@ViewScoped
public class DocumentosBean extends BaseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SelectItem> ciudadesItems;
	private String idCiudadSeleccionada;
	@ManagedProperty(value = "#{serviceDao}")
	transient private ServiceDao serviceDao;
	private DocumentosDataModel listDataModel;	
	private RepositorioDoc nuevoRegistro;	
	private RepositorioDoc registroSelecionado;
	private String ipPrivada;
	private String ipPublica;
	private boolean flg;
	private List<SelectItem> alcanceItems;
	private String idAlcance;
	private boolean conexionexitosa;
	private boolean probarconexion = false;
	
	
	
	public boolean isFlg() {
		return flg;
	}
	public void setFlg(boolean flg) {
		this.flg = flg;
	}
	public String getIpPrivada() {
		return ipPrivada;
	}
	public void setIpPrivada(String ipPrivada) {
		this.ipPrivada = ipPrivada;
	}
	public String getIpPublica() {
		return ipPublica;
	}
	public void setIpPublica(String ipPublica) {
		this.ipPublica = ipPublica;
	}
	private String dominio;
	private Integer idRegSeleccionado;
	
	public static String RESOURCE =  "la.netco.configconsultaprocesos.documentos";
	
	public DocumentosBean(){
		super(RESOURCE);
		
	}
	
	private static final class DocumentosDataModel extends
	GenericDataModel<RepositorioDoc, Integer> {
		private static final long serialVersionUID = 1L;
		
		private DocumentosDataModel() {
			super(RepositorioDoc.class);
			setOrderBy(Order.asc("id"));
		}
		
		@Override
		protected Object getId(RepositorioDoc t) {
			return t.getId();
		}
	}
	
	@PostConstruct
	public void init(){
		nuevoRegistro = new RepositorioDoc();
		cargaFiltrosDataModel();
		flg=false;
	}
	
	public void changeflg(){
		System.out.println("Badera  "+flg);
		if(flg){
			setFlg(false);
		
		}else{
			setFlg(true);
		}
	}
	public String cargaFiltrosDataModel(){
		int tamanho = 0;
    	try {
    		tamanho = getCiudadesItems().size();
		} catch (SystemException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Error: "+e1.getMessage());
		}
    	
		try{
			System.out.println("cargar filtros");
			List<Criterion> filtros = new ArrayList<Criterion>();	    
	    	Map<String, String> alias = new HashMap<String, String>();		
	    	if(tamanho > 0)
	    	{
		    	if(ipPrivada!= null  && !ipPrivada.trim().equals("") ){    		
		    		Criterion ipPrivadas = Restrictions.eq("ipPrivada",  ipPrivada);
					filtros.add(ipPrivadas);
		    	}
		    	
		    	if(dominio!= null  && !dominio.trim().equals("") ){    		
		    		Criterion ipPrivada = Restrictions.eq("dominio",  dominio);
					filtros.add(ipPrivada);    		
		    	}	    	
		    	if(idCiudadSeleccionada!= null  && !idCiudadSeleccionada.equals("")  ){
		    		filtros.add(Restrictions.eq("ciudad.codigo", idCiudadSeleccionada));    	
		    	}
	//	    	else{ 
	//	    		
	//	    		
	//	    		try {
	////	    			String filt[] = new String[getCiudadesItems().size()];
	////	    			int i=0;
	//	    			Disjunction disjunction=Restrictions.disjunction();
	//					for(SelectItem e : getCiudadesItems()){
	////						filt[i]= e.getValue().toString();
	//						disjunction.add(Restrictions.eq("ciudad.codigo", e.getValue().toString()));
	////						i++;
	//					}
	//					filtros.add(disjunction);
	////					filtros.add(Restrictions.eq("ciudad.codigo", filt));
	//				} catch (SystemException e) {
	//					System.out.println("error al cargar listado de documentos"+e);
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//	    		
	//	    	}
	    	
	    		listDataModel = new DocumentosDataModel();    
		    	listDataModel.setAlias(alias);
		    	listDataModel.setFiltros(filtros);
	    	}
	    	
	    	return "listado";
		}catch(Exception e){
			System.out.println("error "+e);
			return "";
		}
	}
	public String limpiar(){

		ipPrivada=null;
		dominio=null;
		return cargaFiltrosDataModel();
	}
	
	public String  crearRegistro() {
		String page =null;
		
		System.out.println("0");
		try {
			List<RepositorioDoc> lista= (List<RepositorioDoc>) serviceDao.getRepositorioDocDao().loadAll(RepositorioDoc.class);
			
			for(RepositorioDoc repo : lista){
				
				
					if(nuevoRegistro.getIpPrivada().trim().equals(repo.getIpPrivada().trim()) && nuevoRegistro.getCarpeta().trim().equals(repo.getCarpeta().trim())){
						FacesMessageUtil. error(FacesContext.getCurrentInstance(),	"Repositorio existente .. !!!!");
						return page;
					}
				
					
								
			}
			
			
			System.out.println("1");
			nuevoRegistro.setIpPrivada(nuevoRegistro.getIpPrivada());				
			nuevoRegistro.setIpPublica("");
			nuevoRegistro.setAuth(true);
			if(nuevoRegistro.getDominio()==null){
				nuevoRegistro.setDominio("");
			}
			if(nuevoRegistro.getCarpeta()==null){
				nuevoRegistro.setCarpeta("");
			}
			System.out.println("2");
			if(nuevoRegistro.getUsuario()==null){
				nuevoRegistro.setUsuario("");
			}
			if(nuevoRegistro.getContrasena()==null){
				nuevoRegistro.setContrasena("");
			}
			System.out.println("3");
			nuevoRegistro.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));
			nuevoRegistro.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			
			
			
			serviceDao.getRepositorioDocDao().create(nuevoRegistro);
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			System.out.println("4");
			page = "transaccionExitosa";
			nuevoRegistro = new RepositorioDoc();
		} catch (Exception e) {
			System.out.println(e);
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
	}
	
	public void cargarObjeto(){	
		
		try {
	
			
			if(idRegSeleccionado == null){
				 FacesContext facesContext = FacesContext.getCurrentInstance();
			     String idRegSeleccionado = facesContext.getExternalContext().getRequestParameterMap().get("idRegSeleccionado");			   
			   
			     if(idRegSeleccionado != null) this.idRegSeleccionado = Integer.parseInt(idRegSeleccionado);	
			     
			}
				System.out.println("iddddddddddd  "+idRegSeleccionado);
			if((idRegSeleccionado != null && !idRegSeleccionado.equals(""))){
				registroSelecionado =serviceDao.getRepositorioDocDao().read(idRegSeleccionado);							
				Map<Integer, Integer> params = new HashMap<Integer, Integer>();
				params.put(0, idRegSeleccionado);
				idAlcance=registroSelecionado.getAlcance().getCodigo();
				idCiudadSeleccionada = registroSelecionado.getCiudad().getCodigo();
				
				
			
			}else{
				registroSelecionado = new RepositorioDoc();
				System.out.println("nuevo objeto");
				
			}
		
		} catch (Exception e) {
		System.err.println(e.getMessage());
		}	
	}
	
	public String  actualizarRegistro() {
		String page =null;
		try {
			
			
			System.out.println("Privada");
			//registroSelecionado.setIpPrivada(registroSelecionado.getIpPublica());
			registroSelecionado.setHabilitado(true);
			registroSelecionado.setIpPublica("");
			//registroSelecionado.setAuth(true);
			
			if(registroSelecionado.getDominio()==null){
				registroSelecionado.setDominio("");
			}
			if(registroSelecionado.getCarpeta()==null){
				registroSelecionado.setCarpeta("");
			}
			if(registroSelecionado.getUsuario()==null){
				registroSelecionado.setUsuario("");
			}
			if(registroSelecionado.getContrasena()==null){
				registroSelecionado.setContrasena("");
			}
			
			registroSelecionado.setAlcance(serviceDao.getAlcanceDao().read(idAlcance));
			registroSelecionado.setCiudad(serviceDao.getCiudadDao().read(idCiudadSeleccionada));
			serviceDao.getRepositorioDocDao().update(registroSelecionado);
	
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			
			return cargaFiltrosDataModel();
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
			
			
			serviceDao.getRepositorioDocDao().delete(registroSelecionado);
			
			FacesMessageUtil.info(FacesContext.getCurrentInstance(), Constants.OPERACION_EXITOSA);
			
			page = "transaccionExitosa";
			registroSelecionado = new RepositorioDoc();
		} catch (Exception e) {
			FacesMessageUtil.error(FacesContext.getCurrentInstance(), Constants.ERROR_OPERACION);
			e.printStackTrace();
		}
		
		return page;
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
						System.out.println(ciudad.getOrganizacion());
						for(Long orga : organizacion){
							if(ciudad.getOrganizacion().equals(orga.toString())){
								ciudadesItems.add(new SelectItem(ciudad.getCodigo(), ciudad
								.getNombre()));
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
	
	public void probarCadenaConexionNuevo(){
		try{	
			System.out.println("probar1");
			if(nuevoRegistro == null || nuevoRegistro.getIpPrivada() == null || nuevoRegistro.getIpPrivada().trim().equals("")){
				setConexionexitosa(false);
				setProbarconexion(true);	
				return;
			}
			
//				String url="\\"+nuevoRegistro.getIpPublica()+"\\"+nuevoRegistro.getCarpeta();
//				File directory=new File (url);
//				System.out.println("URL "+url+"    probando "+directory.getAbsolutePath());
				setConexionexitosa(ping(nuevoRegistro.getIpPrivada()));
				setProbarconexion(true);
			
			
			
		} catch (Exception e) {
			System.out.println("Error probando conexion"+e);
			e.printStackTrace();
		}
		
	}
	
	 public boolean ping(String host) {
		 try{
	         System.out.println("ping a "+host);   
			 String cmd = "";
	            if(System.getProperty("os.name").startsWith("Windows")) {   
	                    // For Windows
	                    cmd = "ping -n 1 " + host;
	            } else {
	                    // For Linux and OSX
	                    cmd = "ping -c 1 " + host;
	            }

	            Process myProcess = Runtime.getRuntime().exec(cmd);
	            myProcess.waitFor();

	            if(myProcess.exitValue() == 0) {

	                    return true;
	            } else {

	                    return false;
	            }

	    } catch( Exception e ) {

	            e.printStackTrace();
	            System.out.println(e);
	            return false;
	    } 
	    }
	

	
	public void probarCadenaConexionActualizar(){
		try{	
			System.out.println("probar1");
			if(registroSelecionado == null || registroSelecionado.getIpPrivada() == null || registroSelecionado.getIpPrivada().trim().equals("")){
				setConexionexitosa(false);
				setProbarconexion(true);	
				return;
			}
			
//				String url=registroSelecionado.getIpPublica()+"\\"+registroSelecionado.getCarpeta();
//				File directory=new File (url);
				System.out.println("IPPPPP "+registroSelecionado.getIpPrivada());
				setConexionexitosa(ping(registroSelecionado.getIpPrivada()));
				setProbarconexion(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	public void setCiudadesItems(List<SelectItem> ciudadesItems) {
		this.ciudadesItems = ciudadesItems;
	}
	
	public String getIdCiudadSeleccionada() {
		return idCiudadSeleccionada;
	}

	public void setIdCiudadSeleccionada(String idCiudadSeleccionada) {
		this.idCiudadSeleccionada = idCiudadSeleccionada;
	}
	
	public RepositorioDoc getNuevoRegistro() {
		return nuevoRegistro;
	}
	public void setNuevoRegistro(RepositorioDoc nuevoRegistro) {
		this.nuevoRegistro = nuevoRegistro;
	}
	
	public DocumentosDataModel getListDataModel() {
		return listDataModel;
	}
	public void setListDataModel(DocumentosDataModel listDataModel) {
		this.listDataModel = listDataModel;
	}

	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public Integer getIdRegSeleccionado() {
		return idRegSeleccionado;
	}
	public void setIdRegSeleccionado(Integer idRegSeleccionado) {
		this.idRegSeleccionado = idRegSeleccionado;
	}
	public RepositorioDoc getRegistroSelecionado() {
		return registroSelecionado;
	}
	public void setRegistroSelecionado(RepositorioDoc registroSelecionado) {
		this.registroSelecionado = registroSelecionado;
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
	public void setAlcanceItems(List<SelectItem> alcanceItems) {
		this.alcanceItems = alcanceItems;
	}
	public String getIdAlcance() {
		return idAlcance;
	}
	public void setIdAlcance(String idAlcance) {
		this.idAlcance = idAlcance;
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
	
	
	
	
}