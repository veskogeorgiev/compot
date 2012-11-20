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
