package exceptions;

public class ExceptionSinConexion extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionSinConexion() {
		super("Ups! Parece que se fue la conexión a internet :(");
	}

	public ExceptionSinConexion(String message) {
		super(message);
	}

}
