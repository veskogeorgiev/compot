package com.compot.model;

/**
 * Cascade operations enumeration
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public enum CascadeOperation {
	ON_ALL("ON ALL"), ON_DELETE("ON DELETE"), ON_UPDATE("ON UPDATE"), NONE(null);
	private String msg;

	private CascadeOperation(String msg) {
		this.msg = msg;
	}

	public String toString() {
		return this.msg;
	}
}
