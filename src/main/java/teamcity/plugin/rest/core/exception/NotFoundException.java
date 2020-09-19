package teamcity.plugin.rest.core.exception;

public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotFoundException(String error) {
		super(error);
	}
}
