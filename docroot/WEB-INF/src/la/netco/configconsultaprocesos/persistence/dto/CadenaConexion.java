package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;



@Entity
@Table(name = "BasesDatosCSJ")
@NamedQuery(name=CadenaConexion.NAMED_QUERY_FIND_BY_ID_CIUDAD, query=CadenaConexion.QUERY_FIND_BY_ID_CIUDAD)
public class CadenaConexion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Ciudad ciudad;
	private String ipBaseDatos;
	private String nombreBaseDatos;
	private String usuarioBaseDatos;
	private String contrasenaBaseDatos;
	private Boolean jurisprudencia = true;
	private Boolean procesos = true;
	private Boolean habilitado;
	private String cadenaConexion;
	private String mailResponsable;
	private Alcance alcance;


	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "idConexion")
	public Integer getId() {
		return id;
	}
	
	@ManyToOne
	@JoinColumn(name = "A065CODICIUD", nullable = false)	
	@ForeignKey(name = "FK_BasesDatosCSJ_T065BACIUDGENE")
	public Ciudad getCiudad() {
		return ciudad;
	}
	@Column(name = "IPBaseDatos", nullable = false, length = 255)
	public String getIpBaseDatos() {
		return ipBaseDatos;
	}
	@Column(name = "NombreBaseDatos", nullable = false, length = 50)
	public String getNombreBaseDatos() {
		return nombreBaseDatos;
	}
	@Column(name = "UsuarioBaseDatos", nullable = false, length = 20)
	public String getUsuarioBaseDatos() {
		return usuarioBaseDatos;
	}
	@Column(name = "ContraseñaBaseDatos", nullable = false, length = 20)
	public String getContrasenaBaseDatos() {
		return contrasenaBaseDatos;
	}
	@Column(name = "Jurisprudencia", nullable = false)
	public Boolean getJurisprudencia() {
		return jurisprudencia;
	}
	@Column(name = "Procesos", nullable = false)
	public Boolean getProcesos() {
		return procesos;
	}
	@Column(name = "habilitado", nullable = false)
	public Boolean getHabilitado() {
		return habilitado;
	}
	@Column(name = "CadenaConexion", nullable = false, length = 255)
	public String getCadenaConexion() {
		return cadenaConexion;
	}
	
	@Column(name = "mailResponsable", nullable = false, length = 50)
	public String getMailResponsable() {
		return mailResponsable;
	}
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_ALCANCE", nullable = false)	
	@ForeignKey(name = "FK_BasesDatosCSJ_ALCANCE")
	public Alcance getAlcance() {
		return alcance;
	}

	public void setMailResponsable(String mailResponsable) {
		this.mailResponsable = mailResponsable;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	public void setIpBaseDatos(String iPBaseDatos) {
		this.ipBaseDatos = iPBaseDatos;
	}

	public void setNombreBaseDatos(String nombreBaseDatos) {
		this.nombreBaseDatos = nombreBaseDatos;
	}

	public void setUsuarioBaseDatos(String usuarioBaseDatos) {
		this.usuarioBaseDatos = usuarioBaseDatos;
	}

	public void setContrasenaBaseDatos(String contrasenaBaseDatos) {
		this.contrasenaBaseDatos = contrasenaBaseDatos;
	}

	public void setJurisprudencia(Boolean jurisprudencia) {
		this.jurisprudencia = jurisprudencia;
	}

	public void setProcesos(Boolean procesos) {
		this.procesos = procesos;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	public void setCadenaConexion(String cadenaConexion) {
		this.cadenaConexion = cadenaConexion;
	}
	
	public void setAlcance(Alcance alcance) {
		this.alcance = alcance;
	}


	public static final String NAMED_QUERY_FIND_BY_ID_CIUDAD  = "findCadenaConexionByIdCiudad";
	public static final String QUERY_FIND_BY_ID_CIUDAD	= "from CadenaConexion cadena   WHERE cadena.ciudad.codigo=? ";

}
