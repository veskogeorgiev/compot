package com.compot;

/**
 * 
 * @author <a href="mailto:vesko.m.georgiev@gmail.com">Vesko Georgiev<a>
 */
public class InvalidModelException extends RuntimeException {

	private static final long serialVersionUID = 5330293374582541569L;

	public InvalidModelException() {
		
	}
	
	public InvalidModelException(String msg) {
		super(msg);
	}

	public InvalidModelException(Exception e) {
		super(e);
	}
}
