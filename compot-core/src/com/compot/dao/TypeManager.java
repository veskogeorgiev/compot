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

import android.database.Cursor;

import com.compot.InvalidModelException;
import com.compot.model.FieldColumn;

/**
 * A class used to bind Java type and SQL types.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface TypeManager {

	/**
	 * Returns the SQL declaration of a column
	 * @param col the column
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public String getSQLTypeDeclaration(FieldColumn col) throws InvalidModelException;

	/**
	 * Takes the column with <i>colIdx</i> from the cursor and ties to parse it 
	 * as the passed as <i>cls</i>
	 * @param cursor the cursor 
	 * @param colIdx the column index
	 * @param cls the class to parse the column as
	 * @return the parsed value
	 */
	public <T> T convert(Cursor cursor, int colIdx, Class<?> cls);
}
