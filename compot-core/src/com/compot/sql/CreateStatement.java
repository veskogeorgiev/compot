/*
Copyright 2012  Vesko Georgiev vesko.m.georgiev@gmail.com

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either 
version 3 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public 
License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.compot.sql;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.compot.model.CascadeOperation;

/**
 * Builder for SQL create table statement
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class CreateStatement {

	private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS";

	private static final String SPACE = " ";
	private static final String COMMA = ", ";

	private String tableName;
	private List<Statement> statements;

	public CreateStatement(String tableName) {
		this.tableName = tableName;
		statements = new LinkedList<Statement>();
	}

	private CreateStatement addStatement(Statement columnStatment) {
		statements.add(columnStatment);
		return this;
	}

	public CreateStatement addColumn(String name, String stm) {
		return addStatement(new ColumnStatment(name + " " + stm));
	}

	public CreateStatement addPrimaryKey(String column) {
		return addStatement(new PKStatment("PRIMARY KEY (" + column + ")"));
	}

	public CreateStatement addConstraint(String stm) {
		return addStatement(new ConstraintStatment("CONSTRAINT " + stm));
	}

	public CreateStatement addForeignKey(String col, String fTable, String fCol) {
		return addStatement(new FKStatment("FOREIGN KEY (" + col + ") REFERENCES " + fTable + " (" + fCol + ")"));
	}

	// @formatter:off
	public CreateStatement addForeignKey(
		String col, 
		String fTable, 
		String fCol, 
		CascadeOperation op
	) {
		return addStatement(new FKStatment("FOREIGN KEY (" + col + ") REFERENCES " + fTable + " (" + fCol + ") " + op + " CASCADE"));
	}
	// @formatter:on

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATE_TABLE_IF_NOT_EXISTS).append(SPACE).append(tableName).append(SPACE).append("(");
		Collections.sort(statements);
		for (Statement c : statements) {
			sb.append(c).append(COMMA);
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(");");

		return sb.toString();
	}

	private static class Statement implements Comparable<Statement> {
		String stm;
		int rank;
		public Statement(String stm, int rank) {
			this.stm = stm;
			this.rank = rank;
		}
		@Override
		public int compareTo(Statement stm) {
			if (rank < stm.rank) return -1;
			if (rank > stm.rank) return 1;
			return 0;
		}
		@Override
		public String toString() {
			return stm;
		}
	}

	private static class ColumnStatment extends Statement {
		public ColumnStatment(String stm) {
			super(stm, 1);
		}
	}

	private static class PKStatment extends Statement {
		public PKStatment(String stm) {
			super(stm, 0);
		}
	}

	private static class FKStatment extends Statement {
		public FKStatment(String stm) {
			super(stm, 2);
		}
	}

	private static class ConstraintStatment extends Statement {
		public ConstraintStatment(String stm) {
			super(stm, 3);
		}
	}

}
