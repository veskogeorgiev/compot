package com.compot.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.compot.InvalidModelException;
import com.compot.annotations.Entity;

/**
 * This class builds the class hierarchy. It is extended by a APT generated class that scans the class path
 * and registers all entity types in the builder. Later this builder build the class hierarchy tree and creates
 * all necessary {@link Metamodel}s
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class MetamodelBuilder {

	private Map<Class<?>, Collection<Class<?>>> hierarchy = new HashMap<Class<?>, Collection<Class<?>>>();
	private Map<Class<?>, Metamodel<?>> metamodels = new HashMap<Class<?>, Metamodel<?>>();

	// Prevent direct instantiation
	protected MetamodelBuilder() {
		
	}

	/**
	 * Adds the given type in this builder
	 * @param type the type to add
	 * @return this builder
	 */
	public MetamodelBuilder add(Class<?> type) {
		Class<?> parentType = type.getSuperclass();

		if (parentType.isAnnotationPresent(Entity.class)) {
			Collection<Class<?>> children = hierarchy.get(parentType);
			if (children == null) {
				children = new LinkedList<Class<?>>();
				hierarchy.put(parentType, children);
			}
			children.add(type);
		}
		if (!hierarchy.containsKey(type)) {
			hierarchy.put(type, new LinkedList<Class<?>>());
		}
		return this;
	}

	/**
	 * Reads all registered classes and creates the necessary metamodels. 
	 * @return map of class to metamodel
	 */
	public Map<Class<?>, Metamodel<?>> build() {
		for (Class<?> type : hierarchy.keySet()) {
			get(type);
		}
		return metamodels;
	}

	/**
	 * @return the hierarchy in the form of child list.
	 */
	public Map<Class<?>, Collection<Class<?>>> getHierarchy() {
		return hierarchy;
	}

	/**
	 * @return map of class to metamodel
	 */
	public Map<Class<?>, Metamodel<?>> getMetamodels() {
		return metamodels;
	}

	/////////////////////////////////////////////////////////////////////////////////
	// Private
	/////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	private <T> Metamodel<T> get(Class<T> cls) throws InvalidModelException {
		Metamodel<T> mm = (Metamodel<T>) metamodels.get(cls);
		if (mm == null) {
			mm = create(cls);
			metamodels.put(cls, mm);
			Metamodel<? extends T>[] children = getChildren(cls);
			mm.setChildren(children);
		}
		return (Metamodel<T>) mm;
	}

	private <T> Metamodel<T> create(Class<T> type) {
		FieldColumn[] columns = getColumns(type);
		FieldColumn idColumn = getIdColumn(type, columns);

		Metamodel<? super T> parent = getParent(type);

		return new Metamodel<T>(type, columns, idColumn, parent);
	}

	@SuppressWarnings("unchecked")
	private <T> Metamodel<? extends T>[] getChildren(Class<T> type) {
		Collection<Class<?>> childrenCollection = hierarchy.get(type);
		Metamodel<? extends T>[] ret = new Metamodel[childrenCollection.size()];

		int idx = 0;
		for (Class<?> c : childrenCollection) {
			ret[idx++] = (Metamodel<? extends T>) get(c);
		}
		return ret;
	}

	private <T> Metamodel<? super T> getParent(Class<T> type) {
		Class<? super T> parent = type.getSuperclass();
		if (parent != Object.class && parent.isAnnotationPresent(Entity.class)) {
			return get(parent);
		}
		return null;
	}

	private FieldColumn getIdColumn(Class<?> type, FieldColumn[] columns) {
		FieldColumn idColumn = null;

		for (FieldColumn col : columns) {
			if (col.isId()) {
				idColumn = col;
				break;
			}
		}
		if (idColumn == null) {
			throw new InvalidModelException(type + " does not have an @Id");
		}
		if (idColumn.getType() != Long.class && idColumn.getType() != long.class) {
			throw new InvalidModelException("The id of type " + type + " must be " + Long.class.getName() + " or " + long.class.getName());
		}
		return idColumn; 
	}

	private <T> FieldColumn[] getColumns(Class<T> type) {
		LinkedList<FieldColumn> columns = new LinkedList<FieldColumn>();

		for (Field f : type.getDeclaredFields()) {
			if (!isTransient(f) && !isStatic(f)) {
				if (!f.isAccessible()) {
					f.setAccessible(true);
				}
				columns.add(new FieldColumn(f));
			}
		}
		return columns.toArray(new FieldColumn[0]);
	}

	private boolean isStatic(Field f) {
		return (f.getModifiers() & Modifier.STATIC) != 0;
	}

	private boolean isTransient(Field f) {
		return (f.getModifiers() & Modifier.TRANSIENT) != 0;
	}

}
