package la.netco.configconsultaprocesos.services.impl;

import javax.annotation.Resource;

import la.netco.configconsultaprocesos.persistence.dto.Alcance;
import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.CadenaUsuario;
import la.netco.configconsultaprocesos.persistence.dto.CadenaUsuarioPK;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
import la.netco.configconsultaprocesos.persistence.dto.Entidad;
import la.netco.configconsultaprocesos.persistence.dto.EntidadEspecialidad;
import la.netco.configconsultaprocesos.persistence.dto.Especialidad;
import la.netco.configconsultaprocesos.persistence.dto.RepositorioDoc;
import la.netco.configconsultaprocesos.persistence.dto.UsuarioCiudad;
import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.services.dao.GenericDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("serviceDao")
public class ServiceDaoImpl implements ServiceDao {

	private GenericDao<?, ?> genericCommonDao;
	private GenericDao<Ciudad, String> ciudadDao;
	private GenericDao<Entidad, String> entidadDao;
	private GenericDao<Especialidad, String> especialidadDao;
	private GenericDao<EntidadEspecialidad, Integer> entidadEspecialidadDao;
	private GenericDao<CadenaConexion, Integer> cadenaConexionDao;
	private GenericDao<CadenaUsuario, CadenaUsuarioPK> cadenaUsuarioDao;
	private GenericDao<Alcance, String> alcanceDao;
	private GenericDao<RepositorioDoc, Integer> repositorioDocDao;
	private GenericDao<UsuarioCiudad, String> usuariociudadDao;
	@Autowired
	@Resource(name="genericDao")
	public void setGenericCommonDao(GenericDao<?, ?> genericCommonDao) {
		this.genericCommonDao = genericCommonDao;
	}
	
	
	@SuppressWarnings("unchecked")
	public GenericDao<Entidad, String> getEntidadDao() {
		entidadDao  = (GenericDao<Entidad, String> )genericCommonDao;		
		entidadDao.setType(Entidad.class);
		return entidadDao;
	}
	
	@SuppressWarnings("unchecked")
	public GenericDao<Especialidad, String> getEspecialidadDao() {
		especialidadDao  = (GenericDao<Especialidad, String> )genericCommonDao;		
		especialidadDao.setType(Especialidad.class);
		return especialidadDao;
	}

	@SuppressWarnings("unchecked")
	public GenericDao<CadenaConexion, Integer> getCadenaConexionDao() {
		cadenaConexionDao  = (GenericDao<CadenaConexion, Integer> )genericCommonDao;		
		cadenaConexionDao.setType(CadenaConexion.class);
		return cadenaConexionDao;
	}

	@SuppressWarnings("unchecked")
	public GenericDao<CadenaUsuario, CadenaUsuarioPK> getCadenaUsuarioDao() {
		cadenaUsuarioDao  = (GenericDao<CadenaUsuario, CadenaUsuarioPK> )genericCommonDao;		
		cadenaUsuarioDao.setType(CadenaUsuario.class);
		return cadenaUsuarioDao;
	}
	
	@SuppressWarnings("unchecked")
	public GenericDao<Ciudad, String> getCiudadDao() {
		ciudadDao  = (GenericDao<Ciudad, String> )genericCommonDao;		
		ciudadDao.setType(Ciudad.class);
		return ciudadDao;
	}	
	
	@SuppressWarnings("unchecked")
	public GenericDao<Alcance, String> getAlcanceDao() {
		alcanceDao  = (GenericDao<Alcance, String> )genericCommonDao;		
		alcanceDao.setType(Alcance.class);
		return alcanceDao;
	}


	@SuppressWarnings("unchecked")
	public GenericDao<EntidadEspecialidad, Integer> getEntidadEspecialidadDao() {
		entidadEspecialidadDao  = (GenericDao<EntidadEspecialidad, Integer> )genericCommonDao;		
		entidadEspecialidadDao.setType(EntidadEspecialidad.class);
		return entidadEspecialidadDao;
	}

	public GenericDao<?, ?> getGenericCommonDao() {
		return genericCommonDao;
	}

	public void setCadenaUsuarioDao(GenericDao<CadenaUsuario, CadenaUsuarioPK> cadenaUsuarioDao) {
		this.cadenaUsuarioDao = cadenaUsuarioDao;
	}

	@SuppressWarnings("unchecked")
	public GenericDao<RepositorioDoc, Integer> getRepositorioDocDao() {
		repositorioDocDao = (GenericDao<RepositorioDoc, Integer> )genericCommonDao;
		repositorioDocDao.setType(RepositorioDoc.class);
		return repositorioDocDao;
	}


	@SuppressWarnings("unchecked")
	public GenericDao<UsuarioCiudad, String> getUsuarioCiudadDao() {
		usuariociudadDao  = (GenericDao<UsuarioCiudad, String> )genericCommonDao;		
		usuariociudadDao.setType(UsuarioCiudad.class);
		return usuariociudadDao;
	}

	
	
	
}
