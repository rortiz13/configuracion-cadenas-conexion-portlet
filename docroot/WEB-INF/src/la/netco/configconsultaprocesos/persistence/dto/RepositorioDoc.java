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
@Table(name = "RepositorioDoc")
@NamedQuery(name=RepositorioDoc.NAMED_QUERY_FIND_BY_ID_CIUDAD, query=RepositorioDoc.QUERY_FIND_BY_ID_CIUDAD)
public class RepositorioDoc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String ipPrivada;
	private String ipPublica;
	private String carpeta;
	private String usuario;
	private String contrasena;
	private String dominio;
	private boolean habilitado;
	private boolean auth;
	private String mail;
	private Ciudad ciudad;
	private Alcance alcance;

	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "IdRepositorioDoc")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "IPPrivada")
	public String getIpPrivada() {
		return ipPrivada;
	}
	public void setIpPrivada(String ipPrivada) {
		this.ipPrivada = ipPrivada;
	}
	@Column(name = "IPPublica")
	public String getIpPublica() {
		return ipPublica;
	}
	public void setIpPublica(String ipPublica) {
		this.ipPublica = ipPublica;
	}
	@Column(name = "Carpeta")
	public String getCarpeta() {
		return carpeta;
	}
	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}
	@Column(name = "Usuario")
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	@Column(name = "Contraseña")
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	@Column(name = "Dominio")
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	@Column(name = "habilitado")
	public boolean isHabilitado() {
		return habilitado;
	}
	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	@Column(name = "RequiereAutenticación")
	public boolean isAuth() {
		return auth;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	@Column(name = "MailResponsable")
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}	
	@ManyToOne
	@JoinColumn(name = "CODIGO_ALCANCE")	
	@ForeignKey(name = "FK_RepositorioDOC_ALCANCE")
	public Alcance getAlcance() {
		return alcance;
	}	
	public void setAlcance(Alcance alcance) {
		this.alcance = alcance;
	}
	@ManyToOne
	@JoinColumn(name = "A065CODICIUD")	
	@ForeignKey(name = "FK_RepositorioDOC_T065BACIUDGENE")
	public Ciudad getCiudad() {
		return ciudad;
	}
	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}
	public static final String NAMED_QUERY_FIND_BY_ID_CIUDAD  = "findRepositorioDocByIdCiudad";
	public static final String QUERY_FIND_BY_ID_CIUDAD	= "from RepositorioDoc repo   WHERE repo.ciudad.codigo=? ";
	
}