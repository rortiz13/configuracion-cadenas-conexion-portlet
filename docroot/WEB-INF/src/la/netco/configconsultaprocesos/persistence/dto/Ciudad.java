package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T065BACIUDGENE")
public class Ciudad implements Serializable  {


	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nombre;
	private String Departamento;	
	@Id
	@Column(name = "A065CODICIUD")
	public String getCodigo() {
		return this.codigo;
	}
	
	@Column(name = "A065DESCCIUD")
	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	
	
	@Column(name = "DEPARTAMENTO")	
	public String getDepartamento() {
		return Departamento;
	}

	public void setDepartamento(String departamento) {
		Departamento = departamento;
	}

	public static final String NAMED_QUERY_GET_ALL_CIUDAD_BY_DEPT  = "getAllCiudadesByDept";
	public static final String QUERY_GET_ALL_CIUDAD_BY_DEPT = "FROM Ciudad ciudad WHERE ciudad.departamento.id_dept = ? order by  ciudad.nombre_mcpio asc";
	
}
