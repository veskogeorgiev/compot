package com.compot.dao;
import java.util.List;

import com.compot.model.Column;

/**
 * A query builder. It actually helps you build a select statement and execute it. 
 * 
 * @param <T> the result type
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface Query<T> {

	/**
	 * Filters a column by regular expression. The value is  
	 * quoted when it is being added to the statement.
	 * 
	 * @param col the column to filter
	 * @param value the regex to match in the filter
	 * @return this query
	 */
	public <Val> Query<T> filterLike(Column col, Val value);

	/**
	 * Filters a column by exact value. The value is  
	 * quoted when it is being added to the statement
	 * 
	 * @param col the column to filter
	 * @param value the value to match in the filter
	 * @return this query
	 */
	public <Val> Query<T> filterEquals(Column col, Val value);

	/**
	 * Marks the query with distinct or not. By default is false
	 * @param distinct
	 * @return this query
	 */
	public Query<T> distinct(boolean distinct);

	/**
	 * Adds an order in the query
	 * @param orderBy the column to order
	 * @return this query
	 */
	public Query<T> orderBy(Column orderBy);

	/**
	 * Adds an offset to the query
	 * @param offset the offset
	 * @return this query
	 */
	public Query<T> offset(int offset);

	/**
	 * Adds a limit to the query
	 * @param limit the limit
	 * @return this query
	 */
	public Query<T> limit(int limit);

	/**
	 * Builds the query, executes it and returns the first result.
	 * @return the first result of the query
	 */
	public T first();

	/**
	 * Builds the query, executes it and returns the result list.
	 * @return the result list of the query
	 */
	public List<T> fetch();

}
