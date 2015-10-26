package entities;

public abstract class Seccion {
	private String codigoSeccion;
	private String numeroSeccion;
	private String nombreSeccion;

	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	public void setCodigoSeccion(String nombreSeccion) {
		this.codigoSeccion = nombreSeccion;
	}

	public String getNombreSeccion() {
		return nombreSeccion;
	}

	public void setNombreSeccion(String nombreSeccion) {
		this.nombreSeccion = nombreSeccion;
	}

	public String getNumeroSeccion() {
		return this.numeroSeccion;
	}

	public void setNumeroSeccion(String numeroSeccion) {
		this.numeroSeccion = numeroSeccion;
	}

}
