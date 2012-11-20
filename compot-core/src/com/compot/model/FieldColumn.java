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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.compot.annotations.Foreign;
import com.compot.annotations.Id;

/**
 * Metadata for binding a Java class field and a column in the database.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class FieldColumn implements Column {

	// //////////////////////////////////////////////////////////////
	// Member fields
	// //////////////////////////////////////////////////////////////

	private Field field;

	public FieldColumn(Field field) {
		this.field = field;
	}


	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public String getFullName() {
		return getMetamodel().getTableName() + "." + getName();
	}

	@Override
	public String getAlias() {
		return getMetamodel().getTableName() + "_" + getName();		
	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	/**
	 * @param annotationType
	 * @return Whether the given annotation is presented in this column's field
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return field.isAnnotationPresent(annotationType);
	}

	/**
	 * @param annotationType
	 * @return the requested annotation from this field
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return field.getAnnotation(annotationType);
	}

	/**
	 * @return whether this column is the one annotated with {@link Id} or not
	 */
	public boolean isId() {
		return field.isAnnotationPresent(Id.class);
	}

	/**
	 * Returns the value of the this column in the given object
	 * @param object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object get(Object object) {
		// TODO ?? throw RuntimeException
		try {
			return field.get(object);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Sets the value of this column in the given object
	 * @param object
	 * @param value the value to set
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, value);
	}

	/**
	 * @return whether this column is annotated with {@link Foreign}
	 */
	public boolean isForeign() {
		return field.isAnnotationPresent(Foreign.class);
	}

	/**
	 * @return the {@link Foreign} descriptor
	 */
	public Foreign getForeign() {
		return field.getAnnotation(Foreign.class);
	}

	@Override
	public String toString() {
		return field.toString();
	}

	//////////////////////////////////////////////////////////////////
	// Private
	//////////////////////////////////////////////////////////////////

	/**
	 * @return the metamodel this column belongs to
	 */
	private Metamodel<?> getMetamodel() {
		return MetamodelFactory.get(field.getDeclaringClass());
	}

}
