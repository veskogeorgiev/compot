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

/**
 * 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class TypeColumn implements Column {
	private static final String TYPE = "type";

	private Metamodel<?> metamodel;

	public TypeColumn(Metamodel<?> metamodel) {
		this.metamodel = metamodel;
	}

	@Override
	public String getName() {
		return metamodel.getTableName() + "_" + TYPE;
	}

	@Override
	public String getFullName() {
		return metamodel.getTableName() + "." + getName();
	}

	@Override
	public String getAlias() {
		return metamodel.getTableName() + "_" + getName();
	}

	@Override
	public Class<?> getType() {
		return String.class;
	}

}
