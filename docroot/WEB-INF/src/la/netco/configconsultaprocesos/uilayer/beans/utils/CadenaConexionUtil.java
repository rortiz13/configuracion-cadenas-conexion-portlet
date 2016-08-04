package la.netco.configconsultaprocesos.uilayer.beans.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.liferay.faces.util.logging.Logger;
import com.liferay.faces.util.logging.LoggerFactory;

import la.netco.configconsultaprocesos.services.impl.PropertyPlaceholderCustom;


public class CadenaConexionUtil {
	private static final transient Logger logger = LoggerFactory
			.getLogger(CadenaConexionUtil.class);
	public static boolean testJTDSConnection(String cadenaConexion, String user, String pass) {
		try {
			
			
			String proxyEnable = PropertyPlaceholderCustom.getProperty("proxy.connection");
			
			if (proxyEnable != null && proxyEnable.equals("true")) {
				System.setProperty("socksProxyHost",PropertyPlaceholderCustom.getProperty("proxy.host"));
				System.setProperty("socksProxyPort",PropertyPlaceholderCustom.getProperty("proxy.port"));
			}	
			
			Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
			String url = cadenaConexion;			
			System.out.println(">>>>>> PROBANDO CONEXION " + cadenaConexion);	
			DriverManager.setLoginTimeout(5);
			Connection conn =  DriverManager.getConnection(url, user, pass);
			
			Statement stmt =conn.createStatement();
			String query = "select top 1 * from T081BRESPEENTI ";
		    ResultSet rs = stmt.executeQuery(query);
		    if(rs.next()){
		    	conn.close();	
		    	return true;
		    }else{
		    	return false;
		    }
			
		} catch (Exception e) {
			logger.error(" ERROR PROBANDO:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
}
