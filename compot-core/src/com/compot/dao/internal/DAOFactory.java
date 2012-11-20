package com.compot.dao.internal;

import java.util.HashMap;
import java.util.Map;

import com.compot.Compot;
import com.compot.InvalidModelException;
import com.compot.dao.DAO;
import com.compot.dao.TypeManager;
import com.compot.model.Metamodel;
import com.compot.model.MetamodelFactory;

/**
 * Factory that keeps all DAO objects 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class DAOFactory {

	private TypeManager typeManager;
	private final Map<Class<?>, DAO<?>> daos = new HashMap<Class<?>, DAO<?>>();

	public DAOFactory(TypeManager typeManager) {
		this.typeManager = typeManager;
	}

	/**
	 * Returns the DAO object for the given <i>cls</i>. If such DAO does not yet exist
	 * it creates it.
	 * 
	 * @param cls the type of the desired DAO
	 * @param compot the compot object
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	@SuppressWarnings("unchecked")
	public <T> DAO<T> createDAO(Class<T> cls, Compot compot) throws InvalidModelException {
		DAO<?> dao = daos.get(cls);
		if (dao == null) {
			Metamodel<T> mm = MetamodelFactory.get(cls);
			dao = new DAOImpl<T>(mm, typeManager, compot.getDb());
			daos.put(cls, dao);
		}
		return (DAO<T>) dao;
	}

}
