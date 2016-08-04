package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CadenaUsuarioPK implements Serializable{

	
	private static final long serialVersionUID = 3470184965137007172L;

	private Long id_usuario;
	private Integer id_cadena;
	public CadenaUsuarioPK() {
	}
	
	public CadenaUsuarioPK(Integer idCadena, Long idUsuario) {
		id_cadena = idCadena;
		id_usuario = idUsuario;
	}

	@Column(name = "id_cadena", nullable = false)
	public Integer getId_cadena() {
		return id_cadena;
	}
	
	@Column(name = "id_usuario", nullable = false)
	public Long getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(Long idUsuario) {
		id_usuario = idUsuario;
	}
	
	
	@Override
	public String toString() {		
		return "id_cadena : "+id_cadena+" - id_usuario : " + id_usuario;
	}

	

	public void setId_cadena(Integer id_cadena) {
		this.id_cadena = id_cadena;
	}


}
