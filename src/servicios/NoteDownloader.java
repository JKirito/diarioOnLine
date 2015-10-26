package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Note;

public class NoteDownloader implements Runnable {

	private volatile DiarioDigital diario;
	private FormatoSalida formatoSalida;
	private String link;
	private Note nota;

	public NoteDownloader(DiarioDigital diario, String link) {
		super();
		this.diario = diario;
		this.link = link;
	}

	public DiarioDigital getDiario() {
		return diario;
	}

	public FormatoSalida getFormatoSalida() {
		return formatoSalida;
	}
	
	public Note getNota() {
		return nota;
	}

	public void run() {
		System.out.println("procesor!!! "+Thread.currentThread());
		System.out.println();System.out.println();

		Document doc = null;
		try {
			doc = Jsoup.connect(this.getDiario().getlinkNota(link)).timeout(0).get();
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
		this.nota = diario.getNotaProcesadaFromDocument(nuevoDoc);
		this.nota.setLink(this.link);
	}

}
