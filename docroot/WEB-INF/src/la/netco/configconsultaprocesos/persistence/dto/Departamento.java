package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "departamento")
@NamedQuery(name = Departamento.NAMED_QUERY_GET_ALL_DEPT, query=Departamento.QUERY_GET_ALL_DEPT)
public class Departamento  implements Serializable  {


	private static final long serialVersionUID = 1L;
	private int id_dept;
	private String nombre;
	
	@Id
	@Column(name = "id_dept")
	public int getId_dept() {
		return id_dept;
	}
	
	@Column(name = "nombre_dept")
	public String getNombre() {
		return nombre;
	}
	public void setId_dept(int id_dept) {
		this.id_dept = id_dept;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public static final String NAMED_QUERY_GET_ALL_DEPT  = "getAllDepartamentos";
	public static final String QUERY_GET_ALL_DEPT		= "FROM Departamento dept WHERE dept.nombre <> 'SIN DEPARTAMENTO'  order by  dept.nombre asc";

}
