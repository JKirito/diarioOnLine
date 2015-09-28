package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Utils.StoreFile;
import entities.DiarioDigital;
import entities.FormatoHtml;
import entities.FormatoSalida;
import entities.FormatoTexto;
import entities.Seccion;

public class NoteProcessor implements Runnable {

	private Element elem;
	private String nombreArchivoAParsear;
	private String pathAGuardar;
	private volatile DiarioDigital diario;
	private FormatoSalida formatoSalida;
	private NotesRecolator recolector;
	private boolean override;
	private Seccion seccion;

	public NoteProcessor(NotesRecolator recolector, String archivo, Element elem, String pathAGuardar,
			DiarioDigital diario, Seccion seccion, FormatoSalida formato, boolean override) {
		super();
		this.nombreArchivoAParsear = archivo;
		this.elem = elem;
		this.pathAGuardar = pathAGuardar;
		this.diario = diario;
		this.seccion = seccion;
		this.formatoSalida = formato;
		this.recolector = recolector;
		this.override = override;
	}

	public Element getElem() {
		return elem;
	}

	public String getNombreArchivoAParsear() {
		return nombreArchivoAParsear;
	}

	public String getPathAGuardar() {
		return pathAGuardar;
	}

	public DiarioDigital getDiario() {
		return diario;
	}

	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}

	public NotesRecolator getRecolector() {
		return recolector;
	}

	public boolean isOverride() {
		return this.override;
	}

	public Seccion getSeccion() {
		return this.seccion;
	}

	public void run() {
//		this.getRecolector().incrementDescargasARealizar();
		//TODO si esta al ppio avanza demasiado rapido la barra de jprogres!
		System.out.println("procesor!!! "+Thread.currentThread());
		System.out.println(this.elem.select("h2").text());

		System.out.println();System.out.println();
		String nombreArchivoAGuarduar = getNombreArchivoAGuardar();

		if (!this.isOverride()) {
			if (StoreFile.fileExists(this.getPathAGuardar(), nombreArchivoAGuarduar, this.getFormatoSalida()
					.getExtension())) {
				return;
			}
		}

		Document doc = null;
		try {
			doc = Jsoup.connect(this.getDiario().getlinkNota(this.getElem().attr("href"))).timeout(0).get();
//			String url = this.getDiario().getlinkNota(this.getElem().attr("href"));
//			doc = Jsoup.parse(new URL(url).openStream(), this.diario.getCharsetName(), url);
		} catch (UnknownHostException e) {
			return;
		} catch (SocketTimeoutException e) {
			return;
		} catch (IOException e) {
			return;
		}

		if (doc == null) {
			return;
		}

		// Element encabezado = doc.getElementById("encabezado");
		// Elements titulo = encabezado.getAllElements().select("h1");
		// String nombreArchivo = titulo.text();
		Document nuevoDoc = this.getDiario().getNotaPreProcesadaFromDocument(doc);

		if (this.getFormatoSalida() instanceof FormatoHtml) {
			guardarNotaHTML(nuevoDoc, nombreArchivoAGuarduar);
		} else if (this.getFormatoSalida() instanceof FormatoTexto) {
			LimpiarHtml limpiador = new LimpiarHtml(this.getPathAGuardar(), this.getDiario(), this.isOverride());
			limpiador.limpiarDocumentoYGuardar(nuevoDoc, nombreArchivoAGuarduar);
		}

	}

	public void guardarNotaHTML(Document doc, String nombreArchivo) {
		StoreFile sf = new StoreFile(this.getPathAGuardar(), ".html", doc.html(), nombreArchivo, this.getDiario().getCharsetName());
		try {
			sf.store(this.isOverride());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNombreArchivoAGuardar() {
		String nombreArchivoAGuarduar = this.getNombreArchivoAParsear().replace(".html", "_" + this.getElem().text());

		if (nombreArchivoAGuarduar.contains("/"))
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replace("/", "-");
		if (nombreArchivoAGuarduar.contains(";"))
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replace(";", ",");
		// TODO! si contiene ciertos caracteres,guarda caracteres "raros" en el
		// nombrearchivo
		// "temporalmente", si tiene estos caracteres, los saco
		if (nombreArchivoAGuarduar.contains("")) {
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replaceAll("", "");
		}
		if (nombreArchivoAGuarduar.contains("")) {
			nombreArchivoAGuarduar = nombreArchivoAGuarduar.replaceAll("", "");
		}

		return nombreArchivoAGuarduar;
	}
}
