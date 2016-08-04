package la.netco.configconsultaprocesos.scheduler;

import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import la.netco.configconsultaprocesos.persistence.dto.CadenaConexion;
import la.netco.configconsultaprocesos.persistence.dto.CadenaUsuario;
import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.services.dao.GenericDao;
import la.netco.configconsultaprocesos.services.impl.SpringApplicationContext;
import la.netco.configconsultaprocesos.uilayer.beans.utils.CadenaConexionUtil;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

@Service
public class CheckDataBaseConnection implements CheckDataBaseConnectionImpl {
	public CheckDataBaseConnection() {
	}

	private static final transient Logger logger = LoggerFactory
			.getLogger(CheckDataBaseConnection.class);

	
	private String from;
	private String subject;
	private String body;
	private boolean emailEnable;
	private boolean serviceEnable;
	
	private static final long fixedDelay =1800000;

	@Override
	@Scheduled(fixedDelay = fixedDelay)
	@Async
	public void checkDataBaseConnections()  {
		if(serviceEnable){
			try {
				 logger.info("EJECUTANDO SERVICIO DE COMPROBACION DE ESTADO DE CADENA DE CONEXION");
				 
				List<CadenaConexion> cadenasConexion = lookupDataProvider().loadAll(CadenaConexion.class);
				for (CadenaConexion cadenaConexion : cadenasConexion) {
					StringBuffer stringConnection = new StringBuffer();
					stringConnection.append("jdbc:jtds:sqlserver://" + cadenaConexion.getIpBaseDatos().trim());
					stringConnection.append("/" + cadenaConexion.getNombreBaseDatos().trim());		
					Boolean connection = CadenaConexionUtil.testJTDSConnection(stringConnection.toString(), cadenaConexion.getUsuarioBaseDatos().trim(), cadenaConexion.getContrasenaBaseDatos().trim());
					
					
					if(connection != null && connection.equals(false) && cadenaConexion.getHabilitado() != null && cadenaConexion.getHabilitado().equals(true)){
						logger.info("DESHABILITANDO CONEXION BASE DE DATOS CONSULTA DE PROCESOS");
						cadenaConexion.setHabilitado(false);
						lookupDataProvider().update(CadenaConexion.class, cadenaConexion);
						
						
						
						
					}else if(connection != null && connection.equals(true) &&  cadenaConexion.getHabilitado() != null && cadenaConexion.getHabilitado().equals(false)){
						
						logger.info("HABILITANDO CONEXION BASE DE DATOS CONSULTA DE PROCESOS");
						cadenaConexion.setHabilitado(true);
						lookupDataProvider().update(CadenaConexion.class, cadenaConexion);
					}
					
					if(connection != null && connection.equals(false)){
						if(emailEnable){
						//	List<CadenaUsuario> responsables = cadenaConexion.getResponsables();
//							if (responsables != null && !responsables.isEmpty()) {
//								for (CadenaUsuario cadenaUsuario : responsables) {
//									User userLiferay;
//									try {
//										userLiferay = UserLocalServiceUtil.getUser(cadenaUsuario.getCompositePK().getId_usuario());
//										if (userLiferay != null) {
//											sendMail(userLiferay.getEmailAddress());
//										}
//
//									} catch (Exception e) {
//										logger.error("ERROR RECUPERANDO USUARIO DE LIFERAY "+ e.getMessage());
//									}
//
//								}
//							}
						}
						
					}
				}
				
				
			
			} catch (Exception e) {
				logger.error("CheckDataBaseConnection, ERROR:" + e);
			}finally{
				 logger.info("FINALIZA SERVICIO DE COMPROBACION DE ESTADO DE CADENA DE CONEXION ");
			}
			
			
		}
		
	}

	private  void sendMail(String emailResponsable) {
		// InternetAddress from = new InternetAddress(emailResponsable);

		InternetAddress to;
		try {
			to = new InternetAddress(emailResponsable);
			
			MailMessage mailMessage = new MailMessage(new InternetAddress(from), to, subject,body, true);
			logger.info(" ENVIANDO CORREO NOTIFICACION CADENAS DE CONEXION " +emailResponsable);
			MailServiceUtil.sendEmail(mailMessage);
		} catch (AddressException e) {
			logger.error("CheckDataBaseConnection, error enviando correo:" + e);
			//e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	private GenericDao<CadenaConexion, Integer> lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");    	
		GenericDao<CadenaConexion, Integer> daoAccess =  (GenericDao<CadenaConexion, Integer>) dataProvider.getGenericCommonDao();
    	//daoAccess.setType(CadenaConexion.class);
    	return daoAccess;
    }

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
	@Value("${mail.service.from}")
	public void setFrom(String from) {
		this.from = from;
	}
	
	@Value("${mail.service.subject}")
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Value("${mail.service.body}")
	public void setBody(String body) {
		this.body = body;
	}

	public boolean isEmailEnable() {
		return emailEnable;
	}

	@Value("${mail.service.enable}")
	public void setEmailEnable(boolean emailEnable) {
		this.emailEnable = emailEnable;
	}


	public boolean isServiceEnable() {
		return serviceEnable;
	}

	@Value("${job.service.enable}")
	public void setServiceEnable(boolean serviceEnable) {
		this.serviceEnable = serviceEnable;
	}

	

}
