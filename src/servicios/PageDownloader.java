package servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.DiarioDigital;
import entities.Note;
import exceptions.ExceptionAlDescargarLink;
import exceptions.ExceptionEstructuraNoValida;

public class PageDownloader {

	private DiarioDigital diario;
	//Asigno un hilo por cada nota a descargar
	private int THREADS_NUMBER = 20;

	public PageDownloader(DiarioDigital diario) {
		super();
		this.diario = diario;
	}

	public Set<Note> downloadTitulos() throws Exception {

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
		Element deportes = null;
		Element sociedad = null;
		Element negocios = null;
		Element ideas = null;
		Element espectaculos = null;

		try {
			page = Jsoup.connect(linkActual).timeout(Conexion.TIMEOUT_MS_L).get();
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

		//Sección mosaico
		if (!diario.esValidoMosaico(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html del id mosaico!");
		} else {
			mosaico = diario.getMosaico(page);
			articulos.addAll(mosaico.getElementsByTag("article"));
		}
		
		//Sección deportes
		if (!diario.esValidoDeportes(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html de la class deportes!");
		} else {
			deportes = diario.getDeportes(page);
			articulos.addAll(deportes.getElementsByTag("article"));
		}
		
		//Sección sociedad
		if (!diario.esValidoSociedad(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html de la class sociedad!");
		} else {
			sociedad = diario.getSociedad(page);
			articulos.addAll(sociedad.getElementsByTag("article"));
		}
		
		//Sección negocios
		if (!diario.esValidoNegocios(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html de la class negocios!");
		} else {
			negocios = diario.getNegocios(page);
			articulos.addAll(negocios.getElementsByTag("article"));
		}
		
		//Sección ideas
		if (!diario.esValidoIdeas(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html de la class ideas!");
		} else {
			ideas = diario.getIdeas(page);
			articulos.addAll(ideas.getElementsByTag("article"));
		}
		
		//Sección espectaculos
		if (!diario.esValidoEspectaculos(page)) {
			throw new ExceptionEstructuraNoValida("Error! Parece que se modificó la estructura html de la class espectáculos!");
		} else {
			espectaculos = diario.getEspectaculos(page);
			articulos.addAll(espectaculos.getElementsByTag("article"));
		}
	
		Set<Note> titulos = new HashSet<Note>();
		Date now = new Date();
		
		ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);
		List<Future<Note>> listFutureNotes = new ArrayList<Future<Note>>();
		
		// Obtener los links asociados a las notas de cada archivo
		for (Element articulo : articulos)
		{
			String link = articulo.select("h2").select("a").attr("href");
			link = link.startsWith("/") ? diario.getLINK() + link : link;
//			String volante = E.select("h3").text();
//			int posDescripcion = volante.isEmpty() ? 2 : 3;
//			String descripcion = (E.select("a").size() > 3 && E.select("a").get(3) != null) ? E.select("a").get(posDescripcion).text() : "";
//			Note nota = new Note(volante, E.select("h2").text(), descripcion, "", "", link, now);
			NoteDownloader downloader = new NoteDownloader(diario, link);
			listFutureNotes.add(executor.submit(downloader));
		}
		
		for(Future<Note> F : listFutureNotes)
		{
			Note n = null;
			try
			{
				n = F.get();	
			}
			catch (Exception e)
			{
				if(Conexion.isOnline())
				{
//					e.printStackTrace();
					continue;
				}
				else
				{
					if(!Conexion.isNetworkInterfacesAvailable())
							System.out.println("No hay intenet porque NO hay red habilitada! ");
					throw e;
				}

			}
			
			if(validarNota(n))
			{
				n.setFechaInit(now);
				titulos.add(n);
			}
				
			//TODO: si tiene titulo la nota pero no cuerpo, es valida o no?
//			else
//				System.out.println("NO VALIDA:" + n);
		}
		
		//limpio variables
		listFutureNotes = null;

		return titulos;
	}
	
	private boolean validarNota(Note nota)
	{
		return nota != null && nota.getTitulo() != null && nota.getCuerpo() != null && !nota.getCuerpo().trim().isEmpty(); 
	}
	
}