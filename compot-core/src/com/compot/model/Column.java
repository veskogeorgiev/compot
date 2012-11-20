package com.compot.model;

import java.lang.reflect.Field;

import com.compot.annotations.Foreign;
import com.compot.annotations.Id;

/**
 * Metadata for binding a Java class field and a column in the database.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class Column {

	// //////////////////////////////////////////////////////////////
	// Member fields
	// //////////////////////////////////////////////////////////////

	private Field field;

	public Column(Field field) {
		this.field = field;
	}

	/**
	 * @return the metamodel this column belongs to
	 */
	public Metamodel<?> getMetamodel() {
		return MetamodelFactory.get(field.getDeclaringClass());
	}

	/**
	 * @return the Java class field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @return the Java class field name
	 */
	public String getName() {
		return field.getName();
	}

	/**
	 * @return the Java class field type
	 */
	public Class<?> getType() {
		return field.getType();
	}

	/**
	 * @return whether this column is the one annotated with {@link Id} or not
	 */
	public boolean isId() {
		return field.isAnnotationPresent(Id.class);
	}

	/**
	 * Returns the fully qualified name of the table column 
	 * @return 'tableName'.'fieldName'. 
	 */
	public String getFullName() {
		return getMetamodel().getTableName() + "." + getName();
	}

	/**
	 * @return column alias to but in the 'as' statement in the SQL query
	 */
	public String getAlias() {
		return getMetamodel().getTableName() + "_" + getName();		
	}

	/**
	 * Returns the value of the this column in the given object
	 * @param object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object get(Object object) throws IllegalArgumentException, IllegalAccessException {
		return field.get(object);
	}

	/**
	 * Sets the value of this column in the given object
	 * @param object
	 * @param value the value to set
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void set(Object object, Object value) throws IllegalArgumentException, IllegalAccessException {
		field.set(object, value);
	}

	/**
	 * @return whether this column is annotated with {@link Foreign}
	 */
	public boolean isForeign() {
		return field.isAnnotationPresent(Foreign.class);
	}

	/**
	 * @return the {@link Foreign} descriptor
	 */
	public Foreign getForeign() {
		return field.getAnnotation(Foreign.class);
	}

}
