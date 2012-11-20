package com.compot;

/**
 * 
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class DBOperationException extends RuntimeException {

	private static final long serialVersionUID = -6306288706675461992L;

	public DBOperationException() {

	}

	public DBOperationException(String msg) {
		super(msg);
	}

	public DBOperationException(Exception e) {
		super(e);
	}
}
