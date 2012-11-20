package com.compot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.compot.model.CascadeOperation;

/**
 * Annotates a foreign column in an entity.
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Foreign {

	/**
	 * Foreign entity class
	 */
	public Class<?> entity();

	/**
	 * foreign property
	 */
	public String property();

	/**
	 * Cascade operation 
	 */
	public CascadeOperation operation() default CascadeOperation.NONE;
	
}
