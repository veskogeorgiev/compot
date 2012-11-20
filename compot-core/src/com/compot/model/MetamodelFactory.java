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

import java.util.Collection;
import java.util.Map;

/**
 * This class bootstraps the APT generated class which has all registered entities. After this class is loaded, 
 * the static member <i>metamodels</i> contains all the entity metamodels in the class path. 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class MetamodelFactory {

	static final String BOOTSTRAP_PACKAGE_NAME = "com.compot";
	static final String BOOTSTRAP_SIMPLE_CLASS_NAME = "CompotMetamodelBuilder";
	static final String BOOTSTRAP_CLASS_NAME = BOOTSTRAP_PACKAGE_NAME + "." + BOOTSTRAP_SIMPLE_CLASS_NAME;
	
	private static Map<Class<?>, Metamodel<?>> metamodels;

	private static Map<Class<?>, Metamodel<?>> _getMetamodels() {
		if (metamodels == null) {
			try {
				Class<?> bootstrapClass = Class.forName(BOOTSTRAP_CLASS_NAME);
				MetamodelBuilder bootstrapInstance = (MetamodelBuilder) bootstrapClass.newInstance();
				metamodels = bootstrapInstance.build();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return metamodels;
	}

	/**
	 * @param type
	 * @return the metamodel for the given class
	 */
	@SuppressWarnings("unchecked")
	public static <T> Metamodel<T> get(Class<T> type) {
		return (Metamodel<T>) _getMetamodels().get(type);
	}

	/**
	 * @return all metamodels
	 */
	public static Collection<Metamodel<?>> getMetamodels() {
		return _getMetamodels().values();
	}

}
