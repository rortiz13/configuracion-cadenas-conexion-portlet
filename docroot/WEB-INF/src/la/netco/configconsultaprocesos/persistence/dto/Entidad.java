package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T051BAENTIGENE")
public class Entidad implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nombre;
		
	@Id
	@Column(name = "A051CODIENTI")
	public String getCodigo() {
		return this.codigo;
	}
	
	@Column(name = "A051DESCENTI")
	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
