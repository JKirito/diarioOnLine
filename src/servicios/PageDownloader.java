package servicios;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.DiarioDigital;
import entities.Note;
import entities.Seccion;

public class PageDownloader {

	private DiarioDigital diario;
	private boolean soloPortada;
	private Seccion seccion;
	private List<String> erroresDescarga = new ArrayList<String>();
	// private boolean override;
	private Integer THREADS_NUMBER = 20; // Por defecto uso 20 hilos

	public PageDownloader(DiarioDigital diario, boolean soloPortada) {
		super();
		this.diario = diario;
		this.soloPortada = soloPortada;
	}

	public List<String> getErroresDescarga() {
		return erroresDescarga;
	}

	public Set<Note> downloadTitulos() {

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

		Date fecha = new Date();

		try {
			page = Jsoup.connect(linkActual).timeout(0).get();
		} catch (UnknownHostException e) {
			erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + " del día: " + fecha
					+ ". Esto puede deberse a una desconexión de internet.\r\n");
			return null;
		} catch (SocketTimeoutException e) {
			erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + " del día: " + fecha
					+ ". Error por Time out.\r\n");
			return null;
		} catch (IOException e) {
			erroresDescarga.add("No se pudo descargar el diario " + diario.getNombreDiario() + " del día: " + fecha
					+ ".\r\n");
			return null;
		}

		if (!diario.esValidoPortada(page)) {
			erroresDescarga.add("Al parecer " + diario.getNombreDiario() + " no tiene noticias"
					+ "en la portada del día: " + fecha + ".\r\n");
			portada = null;
		} else {
			portada = diario.getPortada(page);
		}

		Elements articulos = portada.getElementsByTag("article");

		if(!soloPortada)
		{
			if (!diario.esValidoMosaico(page)) {
				erroresDescarga.add("Al parecer " + diario.getNombreDiario() + " no tiene noticias"
						+ "en mosaico del día: " + fecha + ".\r\n");
				mosaico = null;
			} else {
				mosaico = diario.getMosaico(page);
			}
			articulos.addAll(mosaico.getElementsByTag("article"));
		}



		Set<Note> titulos = new HashSet<Note>();
		Date now = new Date();
		// Obtener los links asociados a las notas de cada archivo
		for (Element E : articulos) {
			String link = E.select("h2").select("a").attr("href");
			link = link.startsWith("/") ? diario.getLINK() + link.substring(1) : link;
			String volante = E.select("h3").text();
			int posDescripcion = volante.isEmpty() ? 2 : 3;
			String descripcion = (E.select("a").size() > 3 && E.select("a").get(3) != null) ? E.select("a").get(posDescripcion).text() : "";
			Note nota = new Note(volante, E.select("h2").text(), descripcion, "", "", link, now);
			titulos.add(nota);
		}

		return titulos;
	}

}