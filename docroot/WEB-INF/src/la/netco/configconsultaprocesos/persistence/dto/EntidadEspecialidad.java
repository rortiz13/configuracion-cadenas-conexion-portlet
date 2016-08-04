package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;



@Entity
@Table(name = "T081BRESPEENTI")
public class EntidadEspecialidad implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Ciudad ciudad;
	private Entidad entidad;
	private Especialidad especialidad;
	private String nombre;
	private String codAgrupacion;
	private Integer orden;
	private CadenaConexion cadenaConexion;
	private RepositorioDoc repositorio;
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "IdRelacionEntidad")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name = "A081CODICIUD", nullable = false)	
	@ForeignKey(name = "FK_T081BRESPEENTI_T065BACIUDGENE")
	public Ciudad getCiudad() {
		return ciudad;
	}
	
	
	@ManyToOne
	@JoinColumn(name = "A081CODIENTI", nullable = false)	
	@ForeignKey(name = "FK_T081BRESPEENTI_T051BAENTIGENE")
	public Entidad getEntidad() {
		return entidad;
	}

	@ManyToOne
	@JoinColumn(name = "A081CODIESPE", nullable = false)	
	@ForeignKey(name = "FK_T081BRESPEENTI_T062BAESPEGENE")
	public Especialidad getEspecialidad() {
		return especialidad;
	}
	
	@Column(name = "A081DESCENESP", nullable = false)
	public String getNombre() {
		return nombre;
	}

	@Column(name = "CodAgrupacion", nullable = false)
	public String getCodAgrupacion() {
		return codAgrupacion;
	}
	
	@Column(name = "OrdenMuestra", nullable = false)
	public Integer getOrden() {
		return orden;
	}
	
	@ManyToOne
	@JoinColumn(name = "idConexion", nullable = true)	
	@ForeignKey(name = "FK_T081BRESPEENTI_BasesDatosCSJ")
	public CadenaConexion getCadenaConexion() {
		return cadenaConexion;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}
	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public void setCodAgrupacion(String codAgrupacion) {
		this.codAgrupacion = codAgrupacion;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public void setCadenaConexion(CadenaConexion cadenaConexion) {
		this.cadenaConexion = cadenaConexion;
	}
	@ManyToOne
	@JoinColumn(name = "IdRepositorioDoc", nullable = true)	
	@ForeignKey(name = "FK_IdRepositorioDOC")
	public RepositorioDoc getRepositorio() {
		return repositorio;
	}

	public void setRepositorio(RepositorioDoc repositorio) {
		this.repositorio = repositorio;
	}
	
	

	
	
}
