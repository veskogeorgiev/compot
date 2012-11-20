package com.compot.sql;

import com.compot.model.Metamodel;

public class DeleteStatemenet {

	private Metamodel<?> metamodel;
	private long id;

	public DeleteStatemenet() {
	}

	public DeleteStatemenet(Metamodel<?> metamodel) {
		this.metamodel = metamodel;
	}

	public DeleteStatemenet from(Metamodel<?> metamodel) {
		this.metamodel = metamodel;
		return this;
	}

	public DeleteStatemenet withId(long id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		return "delete from " + metamodel.getTableName() + " where " + metamodel.getIdColumn().getName() + " = " + id;
	}
}
