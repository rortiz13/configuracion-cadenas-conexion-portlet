package la.netco.configconsultaprocesos.uilayer.beans.datamodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import la.netco.configconsultaprocesos.services.ServiceDao;
import la.netco.configconsultaprocesos.services.dao.GenericDao;
import la.netco.configconsultaprocesos.services.impl.SpringApplicationContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public abstract  class GenericDataModel<T, PK extends Serializable> extends ExtendedDataModel<T> implements Serializable {

  
	private static final long serialVersionUID = -7196213644109004665L;
	private Object rowKey;
    private Class<T> entityClass;
	private Order orderBy;
	private List<Criterion> filtros;
	private Map<String, String> alias;
	private Map<Object,T> wrappedData = new HashMap<Object,T>();
	private List<Object> wrappedKeys = null;
	private int rowIndex;
	
    public GenericDataModel(Class<T> entityClass) {
        super();
        this.entityClass = entityClass;
    }
    
    @Override
    public void setRowKey(Object key) {
        rowKey = key;
    }
 
    @Override
    public Object getRowKey() {
        return rowKey;
    }
    
	@Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
       
    	int firstRow = ((SequenceRange) range).getFirstRow();
		int numberOfRows = ((SequenceRange) range).getRows();
		 wrappedKeys = new ArrayList<Object>();
		GenericDao<T, ?> daoAccess = lookupDataProvider();		
		
		List<T> listData =  daoAccess.executeCriteriaFindPaged(filtros,alias, firstRow, numberOfRows, orderBy);
	

		for (T data : listData ) {
			 wrappedKeys.add( getId(data));
			 wrappedData.put( getId(data), data);
			visitor.process(context, getId(data), argument);
		}
    }
 

    @Override
    public boolean isRowAvailable() {
        return rowKey != null;
    }
    private Long rowCount = null; 
    @Override
	public int getRowCount() {
    	int rowc = 0;
    	 if (rowCount==null) {
    		 GenericDao<T, ?> daoAccess = lookupDataProvider();
    		rowCount = daoAccess.executeCriteriaCount(filtros,alias);

    		if (rowCount != null)
    				rowc = rowCount.intValue();
    	 } else {
    		 rowc=  rowCount.intValue();
	      }
		

		System.out.println("  rowCount.intValue() " +  rowc);
		return rowc;
	}
 
    @SuppressWarnings("unchecked")
	@Override
	public T getRowData() {
		/*
		 * GenericDao<T, PK> daoAccess = lookupDataProvider(); return
		 * daoAccess.read((PK) rowKey);
		 */
		if (rowKey == null) {
			return null;
		} else {
			Object ret = wrappedData.get(rowKey);
			return (T) ret;

		}
	}
 
    @Override
    public int getRowIndex() {
    	 return    this.rowIndex;
    }
 
    @Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
 
    @Override
    public Object getWrappedData() {
    	return wrappedData;
    }
 
    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }
    
    @SuppressWarnings("unchecked")
	private GenericDao<T, PK> lookupDataProvider() {
    	ServiceDao dataProvider = (ServiceDao) SpringApplicationContext.getBean("serviceDao");
    	GenericDao<T, PK> daoAccess =  (GenericDao<T, PK>) dataProvider.getGenericCommonDao();
    	daoAccess.setType(entityClass);
    	return daoAccess;
    }
    
    
    public  ExtendedDataModel<T> getSerializableModel(Range range) {
	       if (wrappedKeys!=null) {
	           return this;
	       } else {
	           return null;
	       }
	}
    
	protected abstract Object getId(T t);

	public Order getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Order orderBy) {
		this.orderBy = orderBy;
	}

	public List<Criterion> getFiltros() {
		return filtros;
	}

	public void setFiltros(List<Criterion> filtros) {
		this.filtros = filtros;
	}

	public Map<String, String> getAlias() {
		return alias;
	}

	public void setAlias(Map<String, String> alias) {
		this.alias = alias;
	}
}
