package com.compot.dao;


/**
 * Generic data access object.
 *  
 * @param <T> the type to access
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface DAO<T> {

	/**
	 * Saves an object to the database. If the object does not have an id. It creates a new record in the database.
	 * If an id already exists, updates the existing record.
	 * 
	 * @param obj the object to save
	 * @return the same object (with eventually a generated id)
	 */
	public T save(T obj);

	/**
	 * Finds an object with the given id
	 * @param id the id to look for
	 * @return the object with the given id, or null
	 */
	public T findById(long id);

	/**
	 * Queries a selection from this type's objects
	 * @return a query object for the current selection
	 */
	public Query<T> query();

	/**
	 * Deletes an object from the database. Uses its id to identify the record to delete
	 * @param obj the object to delete
	 * @return whether it actually deleted the object
	 */
	public boolean delete(T obj);

}
