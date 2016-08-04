package la.netco.configconsultaprocesos.services.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Service("springApplicationContext")
public class SpringApplicationContext implements ApplicationContextAware {

	
	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		CONTEXT = context;

	}

	public static Object getBean(String beanName) {
	
		return CONTEXT.getBean(beanName);
	}
	
	public static void publishEvent(Object beanName) {
		 CONTEXT.publishEvent((ApplicationEvent) beanName);
	}

}
