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
package com.compot.sql;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.compot.dao.internal.Utils;
import com.compot.model.Column;
import com.compot.model.Metamodel;

/**
 * Builder for SQL select statement
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class SelectStatement {
	/** The main table to select from */
	private Metamodel<?> metamodel;
	private List<Column> columns = new LinkedList<Column>();
	// private StringBuilder columns = new StringBuilder();
	private boolean distinct;
	private List<String> joinTableTerms = new LinkedList<String>();
	private Clause whereStatement;
	private String order;
	private int offest = -1;
	private int limit = -1;

	private StringBuilder builder;

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// SelectStatement
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public SelectStatement() {
	}

	public SelectStatement(Metamodel<?> metamodel) {
		this.metamodel = metamodel;
	}

	private SelectStatement join(String join, Metamodel<?> other) {
		StringBuilder builder = new StringBuilder();

		builder.append(" ").append(join).append(" ").append(other.getTableName()).append(" on ");
		builder.append(metamodel.getIdColumn().getFullName()).append(" = ").append(other.getIdColumn().getFullName());

		joinTableTerms.add(builder.toString());
		return this;
	}

	public SelectStatement join(Metamodel<?> other) {
		return join("join", other);
	}

	public SelectStatement leftJoin(Metamodel<?> other) {
		return join("left join", other);
	}

	public SelectStatement addColumn(Column column) {
		columns.add(column);
		return this;
	}

	public SelectStatement distinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	public SelectStatement where(Clause clause) {
		whereStatement = clause;
		return this;
	}

	public Clause where() {
		return whereStatement;
	}

	public SelectStatement orderBy(String key, boolean asc) {
		this.order = "order by " + key + " " + (asc ? "asc" : "desc");
		return this;
	}

	public SelectStatement limit(int limit) {
		this.limit = limit;
		return this;
	}

	public SelectStatement offset(int offset) {
		this.offest = offset;
		return this;
	}

	public void addFields(Metamodel<?> mm) {
		for (Column c : mm.getColumns()) {
			addColumn(c);
		}
		if (mm.isParent()) {
			addColumn(mm.getTypeColumn());
		}
	}

	@Override
	public String toString() {
		builder = new StringBuilder();
		append("select");
		if (distinct) {
			append("distinct");
		}
		append(getColumnSelection()).append("from").append(metamodel.getTableName());

		for (String join : joinTableTerms) {
			append(join);
		}
		if (whereStatement != null && !whereStatement.isEmpty()) {
			append("where " + whereStatement);
		}
		if (order != null) {
			append(order);
		}
		if (limit > -1) {
			append("limit").append(limit + "");
		}
		if (offest > -1) {
			append("offset").append(offest + "");
		}
		return builder.toString();
	}

	private SelectStatement append(Object s) {
		builder.append(" ").append(s).append(" ");
		return this;
	}

	private StringBuilder getColumnSelection() {
		StringBuilder res = new StringBuilder();

		for (Column col : columns) {
			if (res.length() > 0) {
				res.append(", ");
			}
			res.append(col.getFullName() + " as " + col.getAlias());
		}
		return res;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// Clause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static interface Clause {
		boolean isEmpty();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// SimpleClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class SimpleClause implements Clause {
		protected String str;

		protected SimpleClause() {
			//
		}

		public SimpleClause(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return str;
		}

		@Override
		public boolean isEmpty() {
			return str == null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// LikeClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class LikeClause extends SimpleClause {
		public LikeClause(String key, String value) {
			if (value != null) {
				str = key + " like '%" + value + "%'";
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// NotEqualsClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class NotEqualsClause extends SimpleClause {
		public NotEqualsClause(String key, Object value) {
			if (value != null) {
				String strVal = value.toString();
				if (value instanceof String) {
					strVal = Utils.quote(strVal);
				}
				str = key + " <> " + strVal;
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// EqualsClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class EqualsClause extends SimpleClause {
		public EqualsClause(String key, Object value) {
			if (value != null) {
				String strVal = value.toString();
				if (value instanceof String) {
					strVal = Utils.quote(strVal);
				}
				str = key + " = " + strVal;
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// AfterClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class AfterClause extends SimpleClause {
		public AfterClause(String key, Date date) {
			if (date != null) {
				// str = key + " > " + Utils.quote(dateFormat.format(Utils.dayBefore(date)));
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// BeforeClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class BeforeClause extends SimpleClause {
		public BeforeClause(String key, Date date) {
			if (date != null) {
				// str = key + " < " + Utils.quote(dateFormat.format(Utils.dayAfter(date)));
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// CompositeClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static abstract class CompositeClause implements Clause {
		private List<Clause> clauses = new LinkedList<Clause>();
		private String junction;

		protected CompositeClause(String junction) {
			this.junction = junction;
		}

		public CompositeClause add(Clause c) {
			if (c != null && !c.isEmpty()) {
				clauses.add(c);
			}
			return this;
		}

		public CompositeClause add(String c) {
			clauses.add(new SimpleClause(c));
			return this;
		}

		@Override
		public boolean isEmpty() {
			return clauses.isEmpty();
		}

		public CompositeClause like(String key, String value) {
			return add(new LikeClause(key, value));
		}

		public CompositeClause equals(String key, Object value) {
			return add(new EqualsClause(key, value));
		}

		public CompositeClause notEquals(String key, Object value) {
			return add(new NotEqualsClause(key, value));
		}

		public CompositeClause before(String key, Date date) {
			return add(new BeforeClause(key, date));
		}

		public CompositeClause after(String key, Date date) {
			return add(new AfterClause(key, date));
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (Clause c : clauses) {
				builder.append(c.toString()).append(" ").append(junction).append(" ");
			}
			if (builder.length() > 0) {
				int idx = builder.length() - junction.length() - 2;
				builder.delete(idx, idx + junction.length() + 2);
				builder.insert(0, "(");
				builder.append(")");
			}
			return builder.toString();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// AndsClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class AndsClause extends CompositeClause {
		public AndsClause(Clause... clauses) {
			super("and");
			for (Clause clause : clauses) {
				add(clause);
			}
		}

		@Override
		public AndsClause add(Clause c) {
			return (AndsClause) super.add(c);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// OrsClause
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public static class OrsClause extends CompositeClause {
		public OrsClause(Clause... clauses) {
			super("or");
			for (Clause clause : clauses) {
				add(clause);
			}
		}
	}

}
