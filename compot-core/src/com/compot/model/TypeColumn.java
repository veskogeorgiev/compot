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
