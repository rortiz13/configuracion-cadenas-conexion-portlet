package la.netco.configconsultaprocesos.services;

import la.netco.configconsultaprocesos.persistence.dto.Alcance;
import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.CadenaUsuario;
import la.netco.configconsultaprocesos.persistence.dto.CadenaUsuarioPK;
import la.netco.configconsultaprocesos.persistence.dto.Ciudad;
import la.netco.configconsultaprocesos.persistence.dto.Entidad;
import la.netco.configconsultaprocesos.persistence.dto.EntidadEspecialidad;
import la.netco.configconsultaprocesos.persistence.dto.Especialidad;
import la.netco.configconsultaprocesos.persistence.dto.RepositorioDoc;
import la.netco.configconsultaprocesos.services.dao.GenericDao;

public interface ServiceDao {

	public GenericDao<Ciudad, String> getCiudadDao();
	public GenericDao<RepositorioDoc, Integer> getRepositorioDocDao();
	public GenericDao<Alcance, String> getAlcanceDao();
	public GenericDao<?, ?> getGenericCommonDao() ;
	public GenericDao<Especialidad, String> getEspecialidadDao();
	public GenericDao<Entidad, String> getEntidadDao();
	public GenericDao<EntidadEspecialidad, Integer> getEntidadEspecialidadDao();
	public GenericDao<CadenaConexion, Integer> getCadenaConexionDao() ;
	public GenericDao<CadenaUsuario, CadenaUsuarioPK> getCadenaUsuarioDao();
}
