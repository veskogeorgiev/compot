package com.compot.model;

import java.lang.annotation.Annotation;

import com.compot.annotations.Foreign;
import com.compot.annotations.Id;

/**
 * Metadata for describing an SQL table column
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface Column {

	/**
	 * @param annotationType
	 * @return Whether the given annotation is presented in this column's field
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * @param annotationType
	 * @return the requested annotation from this field
	 */
	public <T extends Annotation> T getAnnotation(Class<T> annotationType);

	/**
	 * @return the name of this column name
	 */
	public String getName();

	/**
	 * Returns the fully qualified name of the table column 
	 * @return 'tableName'.'fieldName'. 
	 */
	public String getFullName();

	/**
	 * @return column alias to but in the 'as' statement in the SQL query
	 */
	public String getAlias();

	/**
	 * @return the Java class field type
	 */
	public Class<?> getType();

	/**
	 * Returns the value of the this column in the given object
	 * @param object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException;

	/**
	 * Sets the value of this column in the given object
	 * @param object
	 * @param value the value to set
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object value) throws IllegalArgumentException, IllegalAccessException;

	/**
	 * @return whether this column is the one annotated with {@link Id} or not
	 */
	public boolean isId();

	/**
	 * @return whether this column is annotated with {@link Foreign}
	 */
	public boolean isForeign();

	/**
	 * @return the {@link Foreign} descriptor
	 */
	public Foreign getForeign();

}
