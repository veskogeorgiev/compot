/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
