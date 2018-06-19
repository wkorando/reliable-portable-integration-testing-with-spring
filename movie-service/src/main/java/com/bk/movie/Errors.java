package com.bk.movie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Errors implements Serializable {
	private static final long serialVersionUID = -8209377375355034016L;
	private List<String> errorMessages = new ArrayList<String>();

	public void add(String errorMessage) {
		errorMessages.add(errorMessage);
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}
}