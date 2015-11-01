package servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Note;
import exceptions.ExceptionAlDescargarLink;

public class NoteDownloader implements Callable<Note> {

	private volatile DiarioDigital diario;
	private FormatoSalida formatoSalida;
	private String link;

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
	
	@Override
	public Note call() throws Exception {
//		System.out.println("procesor!!! "+Thread.currentThread());

		// Si el link no pertenece al diario, ni lo descargo xq no va a respetar la
		// estructura del diario (puede ser un link externo) 
		if(!link.contains(diario.getTextoEnLink()))
			return null;
		
		Document doc = null;
		try {
			doc = Jsoup.connect(link).timeout(Conexion.TIMEOUT_MS_L).get();
//			doc = Jsoup.parse(new URL(url).openStream(), this.diario.getCharsetName(), url);
		} catch (UnknownHostException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new UnknownHostException("Esto puede deberse a una desconexión de internet o una URL mal formada"+" "+errors.toString());
		} catch (SocketTimeoutException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new SocketTimeoutException("Al intentar descargar nota: "+link+"\n"+errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new ExceptionAlDescargarLink(errors.toString());
		}

		if (doc == null) {
			return null;
		}

		Document nuevoDoc = this.getDiario().getNotaPreProcesadaFromDocument(doc);
		if(nuevoDoc == null)
			System.out.println("LINK: "+link);
		Note nota = diario.getNotaProcesadaFromDocument(nuevoDoc);
		nota.setLink(this.link);
		return nota;
	}
	
}
