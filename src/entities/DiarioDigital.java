package entities;

import java.io.File;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class DiarioDigital {

	protected String LINK;
	private String textoEnLink;
	protected String charsetName;
	protected String nombrePrefijoAGuardar;
	protected String nombreGrupoNoticias;
	protected String nombrePortada;
	protected String nombreMosaico;
	protected String nombreDeportes;
	protected String nombreSociedad;
	protected String nombreNegocios;
	protected String nombreIdeas;
	protected String nombreEspectaculos;
	protected String nombreRevistas;
	protected String nombreDiario;
	

	public String getLINK() {
		return LINK;
	}

	public void setLINK(String lINK) {
		LINK = lINK;
	}
	
	/**
	 * textoEnLink es el texto que debe aparecer en el link para pertenecer al diario
	 * @return
	 */
	public String getTextoEnLink() {
		return textoEnLink;
	}

	public void setTextoEnLink(String textoEnLink) {
		this.textoEnLink = textoEnLink;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public String getNombrePrefijoAGuardar() {
		return nombrePrefijoAGuardar;
	}

	public void setNombrePrefijoAGuardar(String nombrePrefijoAGuardar) {
		this.nombrePrefijoAGuardar = nombrePrefijoAGuardar;
	}

	public String getNombreGrupoNoticias() {
		return nombreGrupoNoticias;
	}

	public void setNombreGrupoNoticias(String grupoNoticias) {
		this.nombreGrupoNoticias = grupoNoticias;
	}

	public String getNombrePortada() {
		return nombrePortada;
	}

	public String getNombreMosaico() {
		return nombreMosaico;
	}
	
	public String getNombreDeportes() {
		return nombreDeportes;
	}
	
	public String getNombreSociedad() {
		return nombreSociedad;
	}

	public String getNombreNegocios() {
		return nombreNegocios;
	}

	public String getNombreIdeas() {
		return nombreIdeas;
	}

	public String getNombreEspectaculos() {
		return nombreEspectaculos;
	}

	public String getNombreRevistas() {
		return nombreRevistas;
	}

	public void setNombrePortada(String nombrePortada) {
		this.nombrePortada = nombrePortada;
	}

	public void setNombreMosaico(String nombreMosaico) {
		this.nombreMosaico = nombreMosaico;
	}
	
	public void setNombreDeportes(String classDeportes) {
		this.nombreDeportes = classDeportes;
	}
	
	public void setNombreSociedad(String nombreSociedad) {
		this.nombreSociedad = nombreSociedad;
	}

	public void setNombreNegocios(String nombreNegocios) {
		this.nombreNegocios = nombreNegocios;
	}

	public void setNombreIdeas(String nombreIdeas) {
		this.nombreIdeas = nombreIdeas;
	}

	public void setNombreEspectaculos(String nombreEspectaculos) {
		this.nombreEspectaculos = nombreEspectaculos;
	}

	public void setNombreRevistas(String nombreRevistas) {
		this.nombreRevistas = nombreRevistas;
	}

	public String getNombreDiario() {
		return nombreDiario;
	}

	public void setNombreDiario(String nombreDiario) {
		this.nombreDiario = nombreDiario;
	}

	public abstract String armarLinkActual(String fecha, Seccion seccion);

	public abstract String getNombreArchivo(String fecha);

	public abstract boolean esValidoPortada(Document doc);

	public abstract boolean esValidoMosaico(Document doc);

	public abstract boolean esValidoDeportes(Document doc);

	public abstract boolean esValidoSociedad(Document doc);

	public abstract boolean esValidoNegocios(Document doc);

	public abstract boolean esValidoIdeas(Document doc);

	public abstract boolean esValidoEspectaculos(Document doc);

	public abstract boolean esValidoRevistas(Document doc);

	public abstract Element getNombreGrupoNoticias(Document page);

	public abstract Element getPortada(Document page);

	public abstract Element getMosaico(Document page);
	
	public abstract Element getDeportes(Document page);
	
	public abstract Element getSociedad(Document page);
	
	public abstract Element getNegocios(Document page);
	
	public abstract Element getIdeas(Document page);
	
	public abstract Element getEspectaculos(Document page);
	
	public abstract Element getRevistas(Document page);

	public abstract String getFechaConFormato(Date fechaDate);

	public abstract Elements getElementNotasABuscar(File file);

	public abstract boolean isPagina12();

	public abstract boolean isLaNacion();

	/**
	 * 
	 * @param doc
	 * @return Nota con tags html eliminados, sólo texto
	 */
	public abstract Note getNotaProcesadaFromDocument(Document doc);

	/**
	 * Elimina partes de la nota que no son necesarias ni para formato html ni txt
	 */
	public abstract Document getNotaPreProcesadaFromDocument(Document doc);

}
