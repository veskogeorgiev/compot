package com.compot.dao.internal;

import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;

import com.compot.InvalidModelException;
import com.compot.annotations.Id;
import com.compot.annotations.MaxLength;
import com.compot.annotations.NotNull;
import com.compot.annotations.Unique;
import com.compot.dao.TypeManager;
import com.compot.model.FieldColumn;

/**
 * {@link TypeManager} implementation
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class TypeManagerImpl implements TypeManager {
	public static final String INT = "INT";
	public static final String INTEGER = "INTEGER";
	public static final String BOOL = "BOOL";
	public static final String DOUBLE = "DOUBLE";
	public static final String VARCHAR = "VARCHAR";

	private static final Map<Class<?>, String> types = new HashMap<Class<?>, String>();
	static {
		types.put(int.class, INT);
		types.put(Integer.class, INT);

		types.put(long.class, INTEGER);
		types.put(Long.class, INTEGER);

		types.put(boolean.class, BOOL);
		types.put(Boolean.class, BOOL);

		types.put(double.class, DOUBLE);
		types.put(Double.class, DOUBLE);
		types.put(float.class, DOUBLE);
		types.put(Float.class, DOUBLE);

		types.put(String.class, VARCHAR);
	}

	@Override
	public String getSQLTypeDeclaration(FieldColumn col) throws InvalidModelException {
		String sqlType = types.get(col.getType());
		if (sqlType == null) {
			throw new InvalidModelException(col.getType().getName() + " is not supported type");
		}
		StringBuilder builder = new StringBuilder(sqlType);

		if (col.isAnnotationPresent(MaxLength.class)) {
			MaxLength maxLength = col.getAnnotation(MaxLength.class);
			if (col.getType() != String.class) {
				throw new InvalidModelException("Invalid usage of " + MaxLength.class + " in column " + col);
			}
			builder.append("(" + maxLength.value() + ")");
		}

		if (col.isAnnotationPresent(NotNull.class)) {
			builder.append(" NOT NULL");
		}

		if (col.isAnnotationPresent(Id.class)) {
			builder.append(" PRIMARY KEY");
			builder.append(" NOT NULL");
		}

		if (col.isAnnotationPresent(Unique.class)) {
			builder.append(" UNIQUE");
		}
		return builder.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T convert(Cursor c, int colIdx, Class<?> cls) {
		String type = types.get(cls);

		if (type != null) {
			if (INT.equals(type)) {
				return (T) Integer.valueOf(c.getInt(colIdx));
			}
			if (INTEGER.equals(type)) {
				return (T) Long.valueOf(c.getLong(colIdx));
			}
			if (BOOL.equals(type)) {
				return (T) Boolean.valueOf(c.getInt(colIdx) > 0);
			}
			if (DOUBLE.equals(type)) {
				return (T) Double.valueOf(c.getDouble(colIdx));
			}
			if (VARCHAR.equals(type)) {
				return (T) c.getString(colIdx);
			}
		}
		throw new RuntimeException("Undefined type for " + cls);
	}

}
