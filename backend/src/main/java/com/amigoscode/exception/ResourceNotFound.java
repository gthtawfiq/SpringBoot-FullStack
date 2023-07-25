package com.amigoscode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//we use it to change from 500 Internal Server Error server to 404 Not Found 
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException{
	
	public ResourceNotFound (String message) {
		super(message);
	}

}
