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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.compot.DBOperationException;
import com.compot.dao.CursorMapper;
import com.compot.dao.Query;
import com.compot.model.Column;
import com.compot.sql.SelectStatement;
import com.compot.sql.SelectStatement.AndsClause;

/**
 * Implementation of the query builder. Uses {@link SelectStatement} to build the select statement.
 * @param <T> 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class QueryImpl<T> implements Query<T> {

	protected SelectStatement stm;
	protected SQLiteDatabase db;
	protected CursorMapper<T> mapper;

	QueryImpl(SQLiteDatabase db, SelectStatement stm, CursorMapper<T> mapper) {
		this.db = db;
		this.stm = stm;
		this.mapper = mapper;

		stm.where(new SelectStatement.AndsClause());
	}

	@Override
	public <Val> Query<T> like(Column col, Val value) {
		getWhere().like(col.getAlias(), value.toString());
		return this;
	}

	@Override
	public <Val> Query<T> equals(Column col, Val value) {
		getWhere().equals(col.getAlias(), value);
		return this;
	}

	@Override
	public Query<T> distinct(boolean distinct) {
		stm.distinct(distinct);
		return this;
	}

	@Override
	public Query<T> orderBy(Column orderBy) {
		stm.orderBy(orderBy.getAlias(), true);
		return this;
	}

	@Override
	public Query<T> offset(int offset) {
		stm.offset(offset);
		return this;
	}

	public Query<T> limit(int limit) {
		stm.limit(limit);
		return this;
	}

	@Override
	public T first() {
		// TODO Optimize
		List<T> res = fetch();
		if (!res.isEmpty()) {
			return res.iterator().next();
		}
		return null;
	}

	@Override
	public List<T> fetch() {
		Cursor c = db.rawQuery(stm.toString(), null);

		List<T> res = new LinkedList<T>();

		try {
			if (c.moveToFirst()) {
				do {
					res.add(mapper.map(c));				
				}
				while (c.moveToNext()); 
			}
		}
		catch (Exception e) {
			throw new DBOperationException(e);
		}
		return res;
	}

	protected AndsClause getWhere() {
		return (AndsClause) stm.where();
	}

}

