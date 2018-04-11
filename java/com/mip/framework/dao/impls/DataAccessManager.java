package com.mip.framework.dao.impls;

import java.io.Serializable;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.mip.framework.dao.ifcs.DataAccessInstructor;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class serves as the base class for all other DAO implementation. 
 * 
 * @author THBS
 *
 * @param <T>
 * 			a type variable.
 * @param <PK>
 * 			the primary key for that type
 * 
 */
public class DataAccessManager<T, PK extends Serializable>
	extends HibernateDaoSupport implements DataAccessInstructor<T, PK> 
{

	/**
     * Transient log to prevent session synchronization issues.
     * children can use instance for logging.
     */
    protected final transient MISPLogger logger = LoggerFactory.getInstance()
												.getLogger(getClass());
    
	/**
     * A <code>Class<T></code> representing persistentClass.
     */
    protected Class<T> persistentClass;

    /**
     * Constructor that takes in a class to see which type of entity
     * to persist.
     * 
     * @param persistentClass
     *            the class type you'd like to persist
     */
    public DataAccessManager(final Class<T> persistentClass) {

        this.persistentClass = persistentClass;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public List<T> fetchAll() {

        List<T> list = null;
        try {

            list = super.getHibernateTemplate().loadAll(
                this.persistentClass);
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public T fetch(final PK id) {

        T entity = null;
        try {

            entity = (T) super.getHibernateTemplate().get(
                this.persistentClass, id);
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
	public boolean ifExists(final PK id) {

        T entity = null;
        try {

            entity = (T) super.getHibernateTemplate().get(
                this.persistentClass, id);
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }

        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T save(final T object) {

        T entity = null;
        try {

            entity = (T) super.getHibernateTemplate().merge(object);
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public void update(final T object) {

        try {

            super.getHibernateTemplate().update(object);
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final PK id) {

        try {

            super.getHibernateTemplate().delete(this.fetch(id));	
        }
        catch (DataAccessException dataAccessException) {

        	logger.error("Data Access Exception: ", dataAccessException
                .getRootCause());
            throw dataAccessException;
        }
    }
}
