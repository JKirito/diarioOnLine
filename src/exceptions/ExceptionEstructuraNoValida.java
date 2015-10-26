package exceptions;

public class ExceptionEstructuraNoValida extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionEstructuraNoValida() {
		super();
	}

	public ExceptionEstructuraNoValida(String message) {
		super(message);
	}

}
