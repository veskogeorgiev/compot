package com.compot.dao;

import android.database.Cursor;

import com.compot.InvalidModelException;
import com.compot.model.FieldColumn;

/**
 * A class used to bind Java type and SQL types.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public interface TypeManager {

	/**
	 * Returns the SQL declaration of a column
	 * @param col the column
	 * @throws InvalidModelException if the model is incorrectly defined
	 */
	public String getSQLTypeDeclaration(FieldColumn col) throws InvalidModelException;

	/**
	 * Takes the column with <i>colIdx</i> from the cursor and ties to parse it 
	 * as the passed as <i>cls</i>
	 * @param cursor the cursor 
	 * @param colIdx the column index
	 * @param cls the class to parse the column as
	 * @return the parsed value
	 */
	public <T> T convert(Cursor cursor, int colIdx, Class<?> cls);
}
