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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.compot.DBOperationException;
import com.compot.InvalidModelException;
import com.compot.annotations.Entity;
import com.compot.dao.CursorMapper;
import com.compot.dao.DAO;
import com.compot.dao.Query;
import com.compot.dao.TypeManager;
import com.compot.model.Column;
import com.compot.model.FieldColumn;
import com.compot.model.Metamodel;
import com.compot.model.MetamodelFactory;
import com.compot.sql.SelectStatement;

/**
 * Implementation for DAO object
 * @param <T> entity type
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
class DAOImpl<T> implements DAO<T>, CursorMapper<T> {
	private Metamodel<T> metamodel;
	private TypeManager typeManager;
	private SQLiteDatabase db;

	DAOImpl(Metamodel<T> metamodel, TypeManager typeManager, SQLiteDatabase db) {
		this.metamodel = metamodel;
		this.typeManager = typeManager;
		this.db = db;
	}

	@Override
	public T save(T obj) {
		Class<? extends Object> cls = obj.getClass();
		Metamodel<? extends Object> mm = MetamodelFactory.get(cls);
		LinkedList<Metamodel<?>> mmHierarchy = mm.getHierarchyParentFirst();

		try {
			Object commonId = null;

			for (Metamodel<?> m : mmHierarchy) {
				FieldColumn idColumn = m.getIdColumn();
				Object id = idColumn.get(obj);
	
				if (id == null || id.equals(Long.valueOf(0))) {
					if (commonId != null) {
						id = commonId;
						idColumn.set(obj, id);
					}
					id = insert(m, Utils.contentValues(obj, m));
					idColumn.set(obj, id);

					if (commonId == null) {
						commonId = id;
					}
				}
				else {
					db.update(m.getTableName(), 
							Utils.contentValues(obj, m), 
							Utils.kv(m.getIdColumn().getName()), 
							Utils.strings(idColumn.get(obj)));
				}
			}
		}
		catch (Exception e) {
			throw new DBOperationException(e);
		}
		return obj;
	}

	@Override
	public T findById(long id) {
		return query().equals(metamodel.getIdColumn(), id).first();
	}

	@Override
	public Query<T> query() {
		return new QueryImpl<T>(db, getJoinStatementWithAllChildren(), this);
	}

	@Override
	public boolean delete(T obj) {
		LinkedList<Metamodel<?>> mms = metamodel.getHierarchyParentLast();
		FieldColumn idColumn = metamodel.getIdColumn();
		Long id = (Long) idColumn.get(obj);

		if (id == null || id == 0) {
			return false;
		}

		int totalCnt = 0;

		for (Metamodel<?> m : mms) {
			int cnt = db.delete(m.getTableName(), 
					Utils.kv(m.getIdColumn().getName()), 
					Utils.strings(id));
			totalCnt += cnt;
		}

		return totalCnt == mms.size();
	}

	// ////////////////////////////////////////////////////////////////////
	// CursorMapper<T> methods
	// ////////////////////////////////////////////////////////////////////

	@Override
	public T map(Cursor c) throws Exception {
		Metamodel<? extends T> amm = getActualMetamodel(c);
		
		return fillRecursive(c, amm.newInstance(), amm.getHierarchyParentFirst());
	}

	// ////////////////////////////////////////////////////////////////////
	// Private methods
	// ////////////////////////////////////////////////////////////////////

	/**
	 * Returns a join statement for the entity type of this DAO. The result contains 
	 * selection of all field of the current type, joins all parent entity types along 
	 * the root entity (the root is the first entity annotated with {@link Entity} in 
	 * this inheritance hierarchy). It also joins all the child entities down the tree. 
	 * 
	 * @return a select statement to select all parent entities, the current entity and all 
	 * possible child entities
	 */
	private SelectStatement getJoinStatementWithAllChildren() {
		// add the current entity type
		SelectStatement stm = new SelectStatement(metamodel);
		stm.addFields(metamodel);

		// add the each parent entity
		for (Metamodel<?> mm : metamodel.getHierarchyParentFirstExclusive()) {
			stm.leftJoin(mm).addFields(mm);
		}

		// Perform DFS to add all possible child entities
		Queue<Metamodel<?>> queue = new LinkedList<Metamodel<?>>();

		// The DFS starts from the child entities of the current one
		for (Metamodel<?> child : metamodel.getChildren()) {
			queue.offer(child);
		}

		while (!queue.isEmpty()) {
			Metamodel<?> mm = queue.poll();
			stm.leftJoin(mm).addFields(mm);

			for (Metamodel<?> child : mm.getChildren()) {
				queue.offer(child);
			}
		}
		return stm;			
	}

	/**
	 * Goes through all given metamodels and uses each one to fill the given object with using the given cursor. 
	 * Assumes that the given cursor contains all the necessary fields for the given metamodels
	 * @param cursor the cursor to use
	 * @param obj the object to set the fields to
	 * @param mms the metamodels
	 * 
	 * @return the given object for utility
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private T fillRecursive(Cursor cursor, T obj, List<Metamodel<?>> mms) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		for (Metamodel<?> mm : mms) {
			fill(cursor, obj, mm);
		}
		return obj;
	}

	/**
	 * Uses the given metamodel to fill the given object from the given cursor. It assumes that the 
	 * given cursor contains all necessary fields from the metamodel
	 * 
	 * @param cursor the cursor
	 * @param obj the object
	 * @param mm the metamodel
	 *
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void fill(Cursor cursor, Object obj, Metamodel<?> mm) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		for (FieldColumn col : mm.getColumns()) {
			String name = col.getAlias();//FullName();
			int colIdx = cursor.getColumnIndex(name);
			if (colIdx >= 0) {
				Object val = typeManager.convert(cursor, colIdx, col.getType());
				col.set(obj, val);				
			}
			else {
				System.err.println(colIdx);
			}
		}
	}

	/**
	 * Returns the actual metamodel from the cursor, that is if this entity type is a parent type 
	 * (i.e. has children types) it returns the metamodel for the type saved in the type column. 
	 * If this entity type is not a parent, the result is this entity type's metamodel 
	 * @param cursor the cursor to use
	 * 
	 * @throws InvalidModelException
	 * @throws ClassNotFoundException
	 */
	private Metamodel<? extends T> getActualMetamodel(Cursor cursor) throws InvalidModelException, ClassNotFoundException {
		return MetamodelFactory.get(getActualType(cursor));
	}

	/**
	 * Returns the actual type from the cursor, that is if this entity type is a parent type 
	 * (i.e. has children types) it returns the type saved in the type column. If this entity 
	 * type is not a parent, the result is this entity type.
	 *
	 * @param cursor the cursor to use
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends T> getActualType(Cursor cursor) throws ClassNotFoundException {
		if (metamodel.isParent()) {
			Column typeColumn = metamodel.getTypeColumn();
			int colIdx = cursor.getColumnIndex(typeColumn.getAlias());
			String className = cursor.getString(colIdx);
			return (Class<? extends T>) Class.forName(className);
		}
		return metamodel.getType();
	}

	/**
	 * Inserts the given content values in the database using the null hack hint
	 * @param mm the metamodel to insert in
	 * @param cv the content values
	 * @return the id of the inserted row
	 */
	protected long insert(Metamodel<?> mm, ContentValues cv) {
		String nullValueHack = cv.size() == 0 ? metamodel.getIdColumn().getName() : null;
		return db.insertOrThrow(mm.getTableName(), nullValueHack, cv);
	}

}
