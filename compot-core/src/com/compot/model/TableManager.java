package com.compot.model;

import com.compot.InvalidModelException;
import com.compot.annotations.Foreign;
import com.compot.dao.TypeManager;
import com.compot.sql.CreateStatement;

/**
 * Creates {@link CreateStatement} objects given a {@link Metamodel}
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class TableManager {

	private TypeManager typeManager;

	public TableManager(TypeManager typeManager) {
		this.typeManager = typeManager;
	}

	public <T> CreateStatement getCreateStatement(Metamodel<T> metamodel) throws InvalidModelException {
		if (metamodel.isParent()) {
			return createParentTable(metamodel);
		}
		else if (metamodel.hasParent()) {
			return createChildTable(metamodel);
		}
		else {
			return createTable(metamodel);
		}
	}

	// ////////////////////////////////////////////////////////////////////////

	private CreateStatement createTable(Metamodel<?> mm) throws InvalidModelException {
		CreateStatement statement = new CreateStatement(mm.getTableName());
		for (FieldColumn col : mm.getColumns()) {
			statement.addColumn(col.getName(), typeManager.getSQLTypeDeclaration(col));
			if (col.isForeign()) {
				Foreign foreign = col.getForeign();
				String fTable = MetamodelFactory.get(foreign.entity()).getTableName();

				if (foreign.operation() != CascadeOperation.NONE) {
					statement.addForeignKey(col.getName(), fTable, foreign.property(), foreign.operation());
				}
				else {
					statement.addForeignKey(col.getName(), fTable, foreign.property());
				}
			}
		}
		return statement;
	}

	private CreateStatement createChildTable(Metamodel<?> mm) throws InvalidModelException {
		CreateStatement statement = createTable(mm);

		// constraint the primary key as a foreign to the parent table
		String parentTableName = mm.getParent().getTableName();
		String idField = mm.getIdColumn().getName();
		statement.addForeignKey(idField, parentTableName, idField, CascadeOperation.ON_DELETE);

		return statement;
	}

	private CreateStatement createParentTable(Metamodel<?> mm) throws InvalidModelException {
		CreateStatement statement = createTable(mm);
		// add a column to hold the actual type
		statement.addColumn(mm.getTypeColumn().getName(), "varchar(100) NOT NULL");

		return statement;
	}

}
