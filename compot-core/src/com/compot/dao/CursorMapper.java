/**
 * 
 */
package com.compot.dao;

import android.database.Cursor;

/**
 * An interface used to creates an object and fills all its fields 
 * out of a given cursor
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev</a>
 */
public interface CursorMapper<T> {

	/**
	 * Creates an object and fills all its fields out of a given cursor
	 * @param c the cursor
	 * @throws Exception
	 */
	public T map(Cursor c) throws Exception;
}
