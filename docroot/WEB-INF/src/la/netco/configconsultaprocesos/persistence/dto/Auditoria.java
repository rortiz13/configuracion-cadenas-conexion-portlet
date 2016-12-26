package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Audi")
public class Auditoria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cadenaActual;
	private String cadenaAnterior;
	private int accion;
	private int idCadena;
	private int idUsuario;
	private Date fecha;
	private int id;
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name = "id")
	public int getId() {
		return id;
	}

	@Column(name = "fecha")
	public Date getFecha() {
		return fecha;
	}
	
	@Column(name = "idCadena")
	public int getIdCadena() {
		return idCadena;
	}
	
	@Column(name = "accion")
	public int getAccion() {
		return accion;
	}
	
	@Column(name = "cadenaAnterior")
	public String getCadenaAnterior() {
		return cadenaAnterior;
	}
	
	@Column(name = "cadenaActual")
	public String getCadenaActual() {
		return cadenaActual;
	}
	
	@Column(name = "idUsuario")
	public int getIdUsuario() {
		return idUsuario;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setIdCadena(int idCadena) {
		this.idCadena = idCadena;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
}
