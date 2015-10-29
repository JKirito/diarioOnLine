package servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.DiarioDigital;
import entities.FormatoTexto;
import entities.Note;
import exceptions.ExceptionAlDescargarLink;
import exceptions.ExceptionEstructuraNoValida;
import exceptions.ExceptionSinConexion;

public class PageDownloader {

	private DiarioDigital diario;
	private boolean soloPortada;

	public PageDownloader(DiarioDigital diario, boolean soloPortada) {
		super();
		this.diario = diario;
		this.soloPortada = soloPortada;
	}

	public Set<Note> downloadTitulos() throws Exception {

		// String fecha = diario.getFechaConFormato(fechaDate);
		String linkActual = diario.getLINK();

		// while (((ThreadPoolExecutor) executor).getActiveCount() ==
		// ((ThreadPoolExecutor) executor)
		// .getCorePoolSize()) {
		// try {
		// Thread.sleep(300);
		// System.out.println("300 ml");
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		Document page = null;
		Element portada = null;
		Element mosaico = null;

		try {
			page = Jsoup.connect(linkActual).timeout(0).get();
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

		if (!diario.esValidoPortada(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html del id portada!");
		} else {
			portada = diario.getPortada(page);
		}

		Elements articulos = portada.getElementsByTag("article");

		if(!soloPortada)
		{
			if (!diario.esValidoMosaico(page)) {
				throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html del id mosaico!");
			} else {
				mosaico = diario.getMosaico(page);
			}
			articulos.addAll(mosaico.getElementsByTag("article"));
		}


		Set<Note> titulos = new HashSet<Note>();
		Date now = new Date();
		// Obtener los links asociados a las notas de cada archivo
		for (Element articulo : articulos) {
			String link = articulo.select("h2").select("a").attr("href");
			link = link.startsWith("/") ? diario.getLINK() + link : link;
//			String volante = E.select("h3").text();
//			int posDescripcion = volante.isEmpty() ? 2 : 3;
//			String descripcion = (E.select("a").size() > 3 && E.select("a").get(3) != null) ? E.select("a").get(posDescripcion).text() : "";
//			Note nota = new Note(volante, E.select("h2").text(), descripcion, "", "", link, now);
			NoteDownloader downloader = new NoteDownloader(diario, link);
//			downloader.run();
//			Note nota = downloader.getNota();
			Note nota = null;
			try {
				nota = downloader.call();
			} catch (Exception e2) {
				System.out.println("Error al descargar!!!"+articulo.select("h2").text());
				if(Conexion.isOnline())
					continue;
				else
					throw e2;
			}
			nota.setFechaInit(now);
			if(validarNota(nota))
				titulos.add(nota);
		}

		return titulos;
	}
	
	private boolean validarNota(Note nota)
	{
		return nota != null & nota.getTitulo() != null && nota.getCuerpo() != null && !nota.getCuerpo().trim().isEmpty(); 
	}

}