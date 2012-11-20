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
