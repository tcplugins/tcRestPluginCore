package teamcity.plugin.rest.core.exception;

public class UnrecoverableErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public UnrecoverableErrorException(String messasge) {
		super(messasge);
	}
	
}
