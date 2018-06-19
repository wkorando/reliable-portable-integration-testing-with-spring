package com.bk.movie;

public class MovieClientException extends RuntimeException {
	private static final long serialVersionUID = -5656806753048920763L;
	Errors errors;

	public MovieClientException(Errors errors) {
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}

}
