package com.compot.model;

import java.lang.annotation.Annotation;

import com.compot.annotations.Foreign;

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
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return false;
	}

	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return null;
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

	@Override
	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		throw new IllegalAccessException("Not supported operation");
	}

	@Override
	public void set(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
		throw new IllegalAccessException("Not supported operation");
	}

	@Override
	public boolean isId() {
		return false;
	}

	@Override
	public boolean isForeign() {
		return false;
	}

	@Override
	public Foreign getForeign() {
		return null;
	}

}
