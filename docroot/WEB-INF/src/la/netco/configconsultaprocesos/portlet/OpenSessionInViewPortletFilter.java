package la.netco.configconsultaprocesos.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

import la.netco.configconsultaprocesos.services.impl.SpringApplicationContext;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;


public class OpenSessionInViewPortletFilter implements RenderFilter {

	
	private static Logger logger = LoggerFactory.getLogger(OpenSessionInViewPortletFilter.class);
	public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";


	private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;

	private boolean singleSession = true;

	private FlushMode flushMode = FlushMode.MANUAL;


	/**
	 * Set the bean name of the SessionFactory to fetch from Spring's
	 * root application context. Default is "sessionFactory".
	 * @see #DEFAULT_SESSION_FACTORY_BEAN_NAME
	 */
	public void setSessionFactoryBeanName(String sessionFactoryBeanName) {
		this.sessionFactoryBeanName = sessionFactoryBeanName;
	}

	/**
	 * Return the bean name of the SessionFactory to fetch from Spring's
	 * root application context.
	 */
	protected String getSessionFactoryBeanName() {
		return this.sessionFactoryBeanName;
	}
	/**
	 * Set whether to use a single session for each request. Default is "true".
	 * <p>If set to "false", each data access operation or transaction will use
	 * its own session (like without Open Session in View). Each of those
	 * sessions will be registered for deferred close, though, actually
	 * processed at request completion.
	 * @see SessionFactoryUtils#initDeferredClose
	 * @see SessionFactoryUtils#processDeferredClose
	 */
	public void setSingleSession(boolean singleSession) {
		this.singleSession = singleSession;
	}

	/**
	 * Return whether to use a single session for each request.
	 */
	protected boolean isSingleSession() {
		return this.singleSession;
	}
	
	@Override
	public void destroy() {
		System.out.println("  XXX  OpenSessionInViewPortletFilter  DESTROY");
		
	}

	@Override
	public void init(FilterConfig arg0) throws PortletException {
		System.out.println("  XXX  OpenSessionInViewPortletFilter  INIT");
		
	}

	@Override
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain filterChain) throws IOException, PortletException {
		System.out.println("  XXX  OpenSessionInViewPortletFilter  doFilter");
		
		SessionFactory sessionFactory = lookupSessionFactory(request);
		boolean participate = false;

		
		if (isSingleSession()) {
			// single session mode
						if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
							// Do not modify the Session: just set the participate flag.
							participate = true;
						}
						else {
						//	if (isFirstRequest || !asyncManager.initializeAsyncThread(key)) {
								logger.debug("Opening single Hibernate Session in OpenSessionInViewFilter");
								Session session = getSession(sessionFactory);
								SessionHolder sessionHolder = new SessionHolder(session);
								TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);

								//WebAsyncThreadInitializer initializer = createAsyncThreadInitializer(sessionFactory, sessionHolder);
								//asyncManager.registerAsyncThreadInitializer(key, initializer);
							//}
						}
		}else {
			// deferred close mode
			//Assert.state(isLastRequestThread(request), "Deferred close mode is not supported on async dispatches");
			if (SessionFactoryUtils.isDeferredCloseActive(sessionFactory)) {
				// Do not modify deferred close: just set the participate flag.
				participate = true;
			}
			else {
				SessionFactoryUtils.initDeferredClose(sessionFactory);
			}
		}
		
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			if (!participate) {
				if (isSingleSession()) {
					// single session mode
					SessionHolder sessionHolder =	(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
					//if (isLastRequestThread(request)) {
					//	logger.debug("Closing single Hibernate Session in OpenSessionInViewFilter");
						closeSession(sessionHolder.getSession(), sessionFactory);
					//}
				}
				else {
					// deferred close mode
					SessionFactoryUtils.processDeferredClose(sessionFactory);
				}
			}
		}
		/*WebAsyncManager asyncManager = AsyncWebUtils.getAsyncManager(request);
		boolean isFirstRequest = !isAsyncDispatch(request);
		String key = getAlreadyFilteredAttributeName();
		*/
//		RequestCount requestCount   = new RequestCount(request, response,      RequestCount.MANAGED_VIA_SESSION);

		//if (requestCount.isFirstRequest())
		//isf
		
	}
	
	
	/**
	 * Look up the SessionFactory that this filter should use,
	 * taking the current HTTP request as argument.
	 * <p>The default implementation delegates to the {@link #lookupSessionFactory()}
	 * variant without arguments.
	 * @param request the current request
	 * @return the SessionFactory to use
	 */
	protected SessionFactory lookupSessionFactory(RenderRequest request) {
		return lookupSessionFactory();
	}

	
	/**
	 * Look up the SessionFactory that this filter should use.
	 * <p>The default implementation looks for a bean with the specified name
	 * in Spring's root application context.
	 * @return the SessionFactory to use
	 * @see #getSessionFactoryBeanName
	 */
	protected SessionFactory lookupSessionFactory() {
		if (logger.isDebugEnabled()) {
			logger.debug("Using SessionFactory '" + getSessionFactoryBeanName() + "' for OpenSessionInViewFilter");
		}
		return (SessionFactory) SpringApplicationContext.getBean(getSessionFactoryBeanName());
	}
	
	/**
	 * Get a Session for the SessionFactory that this filter uses.
	 * Note that this just applies in single session mode!
	 * <p>The default implementation delegates to the
	 * <code>SessionFactoryUtils.getSession</code> method and
	 * sets the <code>Session</code>'s flush mode to "MANUAL".
	 * <p>Can be overridden in subclasses for creating a Session with a
	 * custom entity interceptor or JDBC exception translator.
	 * @param sessionFactory the SessionFactory that this filter uses
	 * @return the Session to use
	 * @throws DataAccessResourceFailureException if the Session could not be created
	 * @see org.springframework.orm.hibernate3.SessionFactoryUtils#getSession(SessionFactory, boolean)
	 * @see org.hibernate.FlushMode#MANUAL
	 */
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		FlushMode flushMode = getFlushMode();
		if (flushMode != null) {
			session.setFlushMode(flushMode);
		}
		return session;
	}
	
	/**
	 * Specify the Hibernate FlushMode to apply to this filter's
	 * {@link org.hibernate.Session}. Only applied in single session mode.
	 * <p>Can be populated with the corresponding constant name in XML bean
	 * definitions: e.g. "AUTO".
	 * <p>The default is "MANUAL". Specify "AUTO" if you intend to use
	 * this filter without service layer transactions.
	 * @see org.hibernate.Session#setFlushMode
	 * @see org.hibernate.FlushMode#MANUAL
	 * @see org.hibernate.FlushMode#AUTO
	 */
	public void setFlushMode(FlushMode flushMode) {
		this.flushMode = flushMode;
	}

	/**
	 * Return the Hibernate FlushMode that this filter applies to its
	 * {@link org.hibernate.Session} (in single session mode).
	 */
	protected FlushMode getFlushMode() {
		return this.flushMode;
	}

	/**
	 * Close the given Session.
	 * Note that this just applies in single session mode!
	 * <p>Can be overridden in subclasses, e.g. for flushing the Session before
	 * closing it. See class-level javadoc for a discussion of flush handling.
	 * Note that you should also override getSession accordingly, to set
	 * the flush mode to something else than NEVER.
	 * @param session the Session used for filtering
	 * @param sessionFactory the SessionFactory that this filter uses
	 */
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		SessionFactoryUtils.closeSession(session);
	}
}
