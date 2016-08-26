package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UsuarioCiudad1")
public class UsuarioCiudad implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ciudad;
	private String userid;
	private String id;
	
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "userid")
	public String getUserid() {
		return userid;
	}
	
	@Column(name = "ciudad")
	public String getCiudad() {
		return ciudad;
	}
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
}
