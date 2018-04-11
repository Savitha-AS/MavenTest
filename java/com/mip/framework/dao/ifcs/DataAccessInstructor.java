package com.mip.framework.dao.ifcs;

import java.io.Serializable;
import java.util.List;

public interface DataAccessInstructor<T, PK extends Serializable> {

    /**
     * Generic method used to get all objects of a particular type.
     * This is the same as lookup up all rows in a table.
     *
     * @return List of populated objects
     */
    List<T> fetchAll();

    /**
     * Generic method to get an object based on class and identifier.
     * An ObjectRetrievalFailureException Runtime Exception is thrown
     * if nothing is found.
     *
     * @param id
     *            the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T fetch(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     *
     * @param id
     *            the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean ifExists(PK id);

    /**
     * Generic method to save an object.
     *
     * @param object
     *            the object to save
     * @return the persisted object
     */
    T save(T object);

    /**
     * Generic method to update an object.
     *
     * @param object
     *            the object to be updated
     */
    void update(T object);

    /**
     * Generic method to delete an object based on class and id.
     *
     * @param id
     *            the identifier (primary key) of the object to remove
     */
    void delete(PK id);
}
