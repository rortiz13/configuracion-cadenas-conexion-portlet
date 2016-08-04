package la.netco.configconsultaprocesos.persistence.dto;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name = "BasesDatos_Responsables")
@NamedQueries(value = {  
		@NamedQuery(name=CadenaUsuario.NAMED_QUERY_FIND_BY_ID_CADENA, query=CadenaUsuario.QUERY_FIND_BY_ID_CADENA),
		@NamedQuery(name=CadenaUsuario.NAMED_QUERY_FIND_BY_ID_CADENA_ID_USUARIO, query=CadenaUsuario.QUERY_FIND_BY_ID_CADENA_ID_USUARIO)
})
public class CadenaUsuario implements Serializable {


	public CadenaUsuario(CadenaUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}

	public CadenaUsuario() {
	}

	private static final long serialVersionUID = 7927418387785654961L;
	
	private CadenaUsuarioPK compositePK;
	private CadenaConexion cadenaConexion;
	
	public CadenaUsuario(Integer id_cadena, Long id_usuario){
		setCompositePK(new CadenaUsuarioPK(id_cadena, id_usuario));
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "id_cadena", column = @Column(name = "id_cadena", nullable = false)),
			@AttributeOverride(name = "id_usuario", column = @Column(name = "id_usuario", nullable = false)) })
	public CadenaUsuarioPK getCompositePK() {
		return compositePK;
	}
	
	public void setCompositePK(CadenaUsuarioPK compositePK) {
		this.compositePK = compositePK;
	}

	
	
	
	@Override
	public String toString() {		
		return "id_cadena : "+compositePK.getId_cadena()+" - id_usuario : " + compositePK.getId_usuario();
	}

	@ManyToOne
	@JoinColumn(name="id_cadena", insertable = false, updatable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_BasesDatos_Responsables_BasesDatosCSJ")
	public CadenaConexion getCadenaConexion() {
		return cadenaConexion;
	}

	public void setCadenaConexion(CadenaConexion cadenaConexion) {
		this.cadenaConexion = cadenaConexion;
	}

	public static final String NAMED_QUERY_FIND_BY_ID_CADENA  = "findByIdCadena";
	public static final String QUERY_FIND_BY_ID_CADENA	= "from CadenaUsuario cadena   WHERE cadena.compositePK.id_cadena=? ";
	

	public static final String NAMED_QUERY_FIND_BY_ID_CADENA_ID_USUARIO  = "findByIdCadenaIdUsuario";
	public static final String QUERY_FIND_BY_ID_CADENA_ID_USUARIO 	= "from CadenaUsuario cadena   WHERE cadena.compositePK.id_cadena=? and cadena.compositePK.id_usuario=? ";
	
}
