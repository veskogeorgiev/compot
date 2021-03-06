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

import java.util.Map;

import android.content.ContentValues;

import com.compot.DBOperationException;
import com.compot.model.FieldColumn;
import com.compot.model.Metamodel;

/**
 * Contains utility methods
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public class Utils {

	/**
	 * Helps for the WHERE clause generating. For each value in the <code>kyes</code> array it puts '=?' after each one
	 * and returns the conjunction of all 
	 * @param keys
	 * @return string in form <code>key1='?' [ AND keyN=? ]*</code>
	 */
	public static String kv(String... keys) {
		if (keys == null || keys.length == 0) {
			return null;
		}
		StringBuilder b = new StringBuilder();
		String and = " and ";
		for (String k : keys) {
			b.append(k).append(" = ?").append(and);
		}
		return b.delete(b.length() - and.length(), b.length()).toString();
	}

	/**
	 * Maps the input array to an output array with the toString() method.
	 * @param args
	 */
	public static String[] strings(Object... args) {
		String[] result = new String[args.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = args[i] != null ? args[i].toString() : null;
		}
		return result;
	}

	/**
	 * 
	 * @param obj
	 * @param mm
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws DBOperationException
	 */
	public static <T> ContentValues contentValues(T obj, Metamodel<?> mm) throws IllegalArgumentException, IllegalAccessException, DBOperationException {
		ContentValues cv = new ContentValues();

		for (FieldColumn c : mm.getColumns()) {
			if (!c.isId()) {
				putProperType(cv, c.getName(), c.getType(), c.get(obj));
			}
		}
		// if it's a child table then assume we already have the Id
		if (mm.hasParent()) {
			FieldColumn idc = mm.getIdColumn();
			putProperType(cv, idc.getName(), idc.getType(), idc.get(obj));
		}
		// if it's an abstract table, put type
		if (mm.isParent()) {
			cv.put(mm.getTypeColumn().getName(), obj.getClass().getName());
		}
		return cv;
	}

	public static void putProperType(ContentValues cv, String name, Class<?> type, Object val) throws IllegalArgumentException, IllegalAccessException {
		if (type == String.class) {
			cv.put(name, (String) val);
		}
		else if (type == Integer.class || type == int.class) {
			cv.put(name, (Integer) val);
		}
		else if (type == Long.class || type == long.class) {
			cv.put(name, (Long) val);
		}
		else if (type == Double.class || type == double.class) {
			cv.put(name, (Double) val);
		}
		else if (type == Float.class || type == float.class) {
			cv.put(name, (Float) val);
		}
		else if (type == Boolean.class || type == boolean.class) {
			cv.put(name, (Boolean) val);
		}
		else {
			throw new DBOperationException("Invalid type " + type);
		}
	}

	/**
	 * Returns the projection as two dimensional string array. In the first dimension are the keys, in the second one
	 * the values
	 */
	public static String[][] getProjection(Map<String, Object> projValues) {
		String[] keys = new String[projValues.size()];
		String[] vals = new String[projValues.size()];
		int idx = 0;
		for (Map.Entry<String, Object> projValue : projValues.entrySet()) {
			keys[idx] = projValue.getKey();
			vals[idx] = projValue.getValue().toString();
			idx++;
		}
		return new String[][] { keys.length > 0 ? keys : null, vals.length > 0 ? vals : null };
	}

	public static String quote(Object val) {
		return "'" + val + "'";
	}

	public static String brackets(Object val) {
		return "(" + val + ")";
	}

}
