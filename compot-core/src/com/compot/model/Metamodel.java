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
package com.compot.model;

import java.util.LinkedList;

import com.compot.InvalidModelException;

/**
 * Metadata for binding a Java type and a table in the database. It keeps all 
 * the {@link Column}s of the Java type, as well as the inheritance hierarchy 
 * of the type - the parent and all children. 
 * 
 * @param <T> the Java type 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class Metamodel<T> {

	private Class<T> type;
	private FieldColumn[] columns;
	private FieldColumn idColumn;
	private Column typeColumn;

	private Metamodel<? super T> parent;
	private Metamodel<? extends T>[] children;

	/**
	 * Used by the MetamodelBuilder, because the children are derived later
	 * @param children
	 */
	void setChildren(Metamodel<? extends T>[] children) {
		this.children = children;
	}

	public Metamodel(Class<T> type, FieldColumn[] columns, FieldColumn idColumn, Metamodel<? super T> parent) {
		this.type = type;
		this.columns = columns;
		this.idColumn = idColumn;
		this.parent = parent;

		typeColumn = new TypeColumn(this);
	}

	/**
	 * @return the parent {@link Metamodel}, if such one exists, or null
	 */
	public Metamodel<? super T> getParent() {
		return parent;
	}

	/**
	 * @return the child metamodels of this one
	 */
	public Metamodel<? extends T>[] getChildren() {
		return children;
	}

	/**
	 * @return the Java type
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @return the columns defined in this Java type
	 */
	public FieldColumn[] getColumns() {
		return columns;
	}

	/**
	 * The id column of this metamodel
	 * @return
	 */
	public FieldColumn getIdColumn() {
		return idColumn;
	}

	/**
	 * the table name of this 
	 * @return
	 */
	public String getTableName() {
		return type.getSimpleName();
	}

	/**
	 * @return  the root model of this metamodel. That is, if we have the hierarchy A extends B extends C,   
	 * C is  root model for both A and B.
	 * 
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public Metamodel<? super T> getRootMetamodel() throws InvalidModelException {
		Metamodel<? super T> mm = this;
		while (mm.hasParent()) {
			mm = mm.getParent();
		}
		return mm;
	}

	/**
	 * @return If we have the hierarchy A extends B extends C, and this metamodel's type is A, 
	 * then the result would be C, B, A
	 *    
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public LinkedList<Metamodel<?>> getHierarchyParentFirst() throws InvalidModelException {
		// TODO cache ???
		LinkedList<Metamodel<?>> ret = new LinkedList<Metamodel<?>>();
		Metamodel<? super T> mm = this;

		ret.addFirst(mm);

		while (mm.hasParent()) {
			mm = mm.getParent();
			ret.addFirst(mm);
		}
		return ret;
	}

	/**
	 * @return If we have the hierarchy A extends B extends C, and this metamodel's type is A, 
	 * then the result would be C, B
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public LinkedList<Metamodel<?>> getHierarchyParentFirstExclusive() throws InvalidModelException {
		// TODO cache ???
		LinkedList<Metamodel<?>> ret = getHierarchyParentFirst();
		ret.removeLast();
		return ret;
	}

	/**
	 * @return If we have the hierarchy A extends B extends C, and this metamodel's type is A, 
	 * then the result would be C, B, A
	 *    
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public LinkedList<Metamodel<?>> getHierarchyParentLast() throws InvalidModelException {
		// TODO cache ???
		LinkedList<Metamodel<?>> ret = new LinkedList<Metamodel<?>>();
		Metamodel<? super T> mm = this;

		ret.addFirst(mm);

		while (mm.hasParent()) {
			mm = mm.getParent();
			ret.add(mm);
		}
		return ret;
	}

	/**
	 * @return a new Java object of this metamodel's type.
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public T newInstance() throws InvalidModelException {
		try {
			return type.newInstance();
		}
		catch (Exception e) {
			throw new InvalidModelException(e);
		}
	}

	/**
	 * @return whether this metamodel is a parent (i.e. whether it has children)
	 */
	public boolean isParent() {
		return children.length > 0;
	}

	/**
	 * 
	 * @return whether this metamodel has a parent 
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public boolean hasParent() throws InvalidModelException {
		return parent != null;
	}

	/**
	 * Returns the full type column name. The type column is not an actual column. It 
	 * exists in the DB table, but not in the Java type. In the type column is saved the 
	 * actual type of an object. e.g. if we have the hierarchy: A extends B extends C and 
	 * if we save an object of type A in the DB, then the type column of B and C will contain 
	 * the A type. 

	 * @return the type column
	 */
	public Column getTypeColumn() {
		return typeColumn;
	}

	/**
	 * @param name 
	 * @return the column in this metamodel with the specified name
	 */
	public Column getColumn(String name) {
		for (Column c : getColumns()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		throw new RuntimeException("No such column with name: " + name);
	}

	@Override
	public String toString() {
		return type.toString();
	}

}
