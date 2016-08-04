package la.netco.configconsultaprocesos.uilayer.beans;

import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.services.impl.SpringApplicationContext;

public class DataProviderUtil {

	
	public static ServiceDao lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
    	return dataProvider;
    }
}
