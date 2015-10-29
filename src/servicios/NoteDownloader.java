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
//	private Note nota;

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
	
//	public Note getNota() {
//		return nota;
//	}

//	public void run() {
//		System.out.println("procesor!!! "+Thread.currentThread());
//		System.out.println();System.out.println();
//
//		Document doc = null;
//		try {
//			doc = Jsoup.connect(this.getDiario().getlinkNota(link)).timeout(0).get();
////			doc = Jsoup.parse(new URL(url).openStream(), this.diario.getCharsetName(), url);
//		} catch (UnknownHostException e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			try {
//				throw new UnknownHostException("Esto puede deberse a una desconexión de internet"+" "+errors.toString());
//			} catch (UnknownHostException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		} catch (SocketTimeoutException e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			throw new SocketTimeoutException(errors.toString());
//		} catch (IOException e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			throw new ExceptionAlDescargarLink(errors.toString());
//		}
//
//		if (doc == null) {
//			return;
//		}
//
//		Document nuevoDoc = this.getDiario().getNotaPreProcesadaFromDocument(doc);
//		this.nota = diario.getNotaProcesadaFromDocument(nuevoDoc);
//		this.nota.setLink(this.link);
//	}

	@Override
	public Note call() throws Exception {
//		System.out.println("procesor!!! "+Thread.currentThread());
//		System.out.println();System.out.println();

		if(!Conexion.isOnline())
			throw new ExceptionAlDescargarLink();
		
		Document doc = null;
		try {
			doc = Jsoup.connect(this.getDiario().getlinkNota(link)).timeout(0).get();
//			doc = Jsoup.parse(new URL(url).openStream(), this.diario.getCharsetName(), url);
		} catch (UnknownHostException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new UnknownHostException("Esto puede deberse a una desconexión de internet o una URL mal formada"+" "+errors.toString());
		} catch (SocketTimeoutException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new SocketTimeoutException(errors.toString());
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			throw new ExceptionAlDescargarLink(errors.toString());
		}

		if (doc == null) {
			return null;
		}

		Document nuevoDoc = this.getDiario().getNotaPreProcesadaFromDocument(doc);
		Note nota = diario.getNotaProcesadaFromDocument(nuevoDoc);
		nota.setLink(this.link);
		return nota;
	}
	
}
