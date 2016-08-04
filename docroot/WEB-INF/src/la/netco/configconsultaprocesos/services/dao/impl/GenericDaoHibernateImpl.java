package la.netco.configconsultaprocesos.services.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import la.netco.configconsultaprocesos.services.dao.GenericDao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Service("genericDao")
public class GenericDaoHibernateImpl <T, PK extends Serializable>  extends HibernateDaoSupport implements GenericDao<T, PK> {
	
	private Class<T> type;

	@Autowired
	@Resource(name = "sessionFactory")
	public void init(SessionFactory factory) {
		setSessionFactory(factory);
	}

	public GenericDaoHibernateImpl() {

	}
    public GenericDaoHibernateImpl(Class<T> type) {
        this.type = type;
    }
    
    public GenericDaoHibernateImpl(Class<T> type, HibernateTemplate hibernateTemplate) {
        this.type = type;
        setHibernateTemplate(hibernateTemplate);
    }
	
    public T read(PK id) {
        return (T) getHibernateTemplate().get(type, id);
   }
    
	
	
	
	@SuppressWarnings("unchecked")
   	//@Auditable(tipoOperacion = Auditoria.OPERACION_SAVE)  
	@Transactional(propagation = Propagation.REQUIRED)
	public PK create(T o) {
        return (PK) getHibernateTemplate().save(o);
  
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
    public void update(T o) {
    	getHibernateTemplate().update(o);
    }
	 
	
	@Transactional(propagation = Propagation.REQUIRED)
    public void update( Class<T> type, T o) {
    	getHibernateTemplate().update(type.getName(),o);
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
    public void delete(T o) {
    	getHibernateTemplate().delete(o);
    }

	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios,	final Map<String, String> mapAlias ,  final int startRecord, final int endRecord,  final Order orderBy) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
				
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				for (Criterion iterable_element :  criterios) {
				
					criteria.add(iterable_element);

				}
				
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
				
				if(orderBy != null)
					criteria.addOrder(orderBy);
			
				
				System.out.println("EN DAO XXXXX");
				
				return criteria.list();
			}
		});
	
	}
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final int startRecord, final int endRecord, final Order orderBy) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
			
				if(orderBy != null)
					criteria.addOrder(orderBy);
			
				return criteria.list();
			}
		});
	
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFindPaged(final List<Criterion> criterios, final int startRecord, final int endRecord, final Order orderBy ) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);
							
				criteria.setFirstResult(startRecord);
				criteria.setMaxResults(endRecord);	
				
				if (criterios != null) {
					for (Criterion iterable_element : criterios) {
						criteria.add(iterable_element);
					}
				}
				
				if(orderBy != null)
					criteria.addOrder(orderBy);

				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
				
				return criteria.list();
			}
		});
	
	}
	
	public Long executeCriteriaCount(final List<Criterion> criterios,
			final Map<String, String> mapAlias) {

		final Class<T> type = this.type;

		Long count = (Long) getHibernateTemplate().execute(
				new HibernateCallback<Object>() {
					public Long doInHibernate(Session session)
							throws HibernateException, SQLException {

						Criteria criteria = session.createCriteria(type);

						if (mapAlias != null) {
							for (Entry<String, String> alias : mapAlias.entrySet()) {
								criteria.createAlias(alias.getKey(),alias.getValue());
							}
						}

						if (criterios != null) {
							for (Criterion iterable_element : criterios) {
								criteria.add(iterable_element);
							}
						}

						criteria.setProjection(Projections.rowCount());
						criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY) ;
						
						return ((Long) criteria.list().get(0));
					}
				});
		if (count != null)
			return count;
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFind(final List<Criterion> criterios ) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				Criteria criteria       = session.createCriteria(type);		
				
				if (criterios != null) {
					for (Criterion iterable_element : criterios) {
						criteria.add(iterable_element);
					}
					
				}
				
				return criteria.list();
			}
		});
	
	}
	
	public List<T> loadAll(Class<T> type) {	    
	    return (List<T> ) getHibernateTemplate().loadAll(type);
	}

	
	@SuppressWarnings("unchecked")
	public List<T> executeCriteriaFind(final List<Criterion> criterios ,	final Map<String, String> mapAlias ) {

		final Class<T> type  = this.type;
	
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
			Criteria criteria       = session.createCriteria(type);	
			
				if(mapAlias != null){
					for (Entry<String, String> alias : mapAlias.entrySet()) {
						criteria.createAlias(alias.getKey(), alias.getValue());
					}
				}
				
				for (Criterion iterable_element :  criterios) {				
					criteria.add(iterable_element);
				}	
				return criteria.list();
			}
		});
	
	}
	
	
	public List<?> executeFind(final Class<?> type, final Map<Integer, ?> params, final String namedQuery) {
		
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.getNamedQuery(namedQuery);
				
				for (Entry<Integer, ?> iterable_element :  params.entrySet()) {
					Integer position = iterable_element.getKey();
					query.setParameter(position, iterable_element.getValue());
				}
				return query.list();
			}
		});
	
	}
	
	public int count() {
		class ReturnValue {
			Long value;
		}
		final ReturnValue rv = new ReturnValue();
		final Class<T> type = this.type;
		getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {			
				rv.value = (Long) session.createQuery("select count(*) from " + type.getSimpleName()).uniqueResult();
				return null;
			}
		});
		return rv.value.intValue();
	}


	public void setType(Class<T> type) {
		this.type = type;
	}
    
	
}
