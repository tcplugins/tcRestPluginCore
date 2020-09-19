package teamcity.plugin.rest.core.handler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jetbrains.buildServer.serverSide.auth.AccessDeniedException;
import teamcity.plugin.rest.core.Loggers;
import teamcity.plugin.rest.core.exception.NotFoundException;
import teamcity.plugin.rest.core.exception.UnrecoverableErrorException;

@ControllerAdvice
public class CoreRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	public CoreRestResponseEntityExceptionHandler() {
		Loggers.SERVER.info("CoreRestResponseEntityExceptionHandler :: Starting");
	}
 
    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, UnrecoverableErrorException.class })
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, 
			new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()),
			new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
    	return handleExceptionInternal(ex, 
    			new Error(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
    			new HttpHeaders(), HttpStatus.NOT_FOUND, request);    			
    }
    
    @ExceptionHandler(value = { AccessDeniedException.class })
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
    	return handleExceptionInternal(ex, 
    			new Error(HttpStatus.FORBIDDEN.value(), ex.getMessage()),
    			new HttpHeaders(), HttpStatus.FORBIDDEN, request);       			
     }
    
    @XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
    public static class Error {
    	
    	int errorStatus;
    	String errorMessage;

    	// No args constructor for JAXB
    	public Error() {}
    	
    	public Error(int errorStatus, String errorMessage) {
    		this.errorStatus = errorStatus;
    		this.errorMessage = errorMessage;
		}
    }
}