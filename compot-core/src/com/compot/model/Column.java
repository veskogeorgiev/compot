package com.compot.model;


/**
 * Metadata for describing an SQL table column
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface Column {

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

}
