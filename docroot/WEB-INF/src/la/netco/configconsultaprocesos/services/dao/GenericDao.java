package la.netco.configconsultaprocesos.services.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public interface GenericDao <T, PK extends Serializable>  {
	
	
	public PK create(T o);
    public void update(T o);
    public void delete(T o);
    
	public List<T> executeCriteriaFindPaged(List<Criterion> criterios,	Map<String, String> mapAlias, int startRecord, int endRecord,  Order orderBy);
	public List<T> executeCriteriaFindPaged(int startRecord, int endRecord, Order orderBy);
	public List<T> executeCriteriaFindPaged(List<Criterion> criterios, int startRecord, int endRecord, Order orderBy );

	
	public Long executeCriteriaCount(List<Criterion> criterios, Map<String, String> mapAlias);

	public List<T> executeCriteriaFind(final List<Criterion> criterios );
	public List<T> executeCriteriaFind(final List<Criterion> criterios ,	final Map<String, String> mapAlias );
	public int count();
	public void setType(Class<T> type);
	public List<T> loadAll(Class<T> type);
	public List<?> executeFind(Class<?> type, Map<Integer, ?> params, String namedQuery);
	
	 public T read(PK id);
	 
	   public void update( Class<T> type, T o);
}
