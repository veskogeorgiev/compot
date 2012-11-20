package com.compot;

import android.database.sqlite.SQLiteDatabase;

import com.compot.dao.DAO;
import com.compot.dao.Query;
import com.compot.dao.internal.DAOFactory;

/**
 * The generic data access object. It acts as a delegate to the actual {@link DAO} instance.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
@SuppressWarnings("unchecked")
public class Compot {

	private DAOFactory daoFactory;
	private SQLiteDatabase db;

	public Compot(DAOFactory daoFactory, SQLiteDatabase db) {
		this.daoFactory = daoFactory;
		this.db = db;
	}

	/**
	 * @return the underlying DB connection
	 */
	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * Finds the object of the given type with the specified id. 
	 * @param cls
	 * @param id
	 * @return the object of the given type with the specified id.
	 * @throws DBOperationException
	 */
	public <T> T findById(Class<T> cls, Long id) {
		return getDAO(cls).findById(id);
	}

	public <T> Query<T> query(Class<T> cls) {
		return getDAO(cls).query();
	}

	public <T> T save(T obj) throws DBOperationException {
		DAO<T> dao = (DAO<T>) getDAO(obj.getClass());
		return dao.save(obj);
	}

	public <T> void delete(T obj) throws DBOperationException {
		DAO<T> dao = (DAO<T>) getDAO(obj.getClass());
		dao.delete(obj);
	}

	////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////////////////////////////////////////////////////////////

	private <T> DAO<T> getDAO(Class<T> cls) throws InvalidModelException {
		return daoFactory.createDAO(cls, this);
	}

}
