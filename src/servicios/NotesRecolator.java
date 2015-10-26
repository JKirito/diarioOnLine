package servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import entities.DiarioDigital;
import entities.FormatoSalida;
import entities.Seccion;

public class NotesRecolator extends Observable {

	private String pathAGuardar;
	private String pathOrigen;
	private Integer THREADS_NUMBER;
	private DiarioDigital diario;
	private FormatoSalida formatoSalida;
	ExecutorService executor;
	/**
	 * Contador para todas las descargas - Utilzado para JProgress
	 */
	private AtomicInteger descargasARealizar = new AtomicInteger(0);
	/**
	 * Contador para las descargas que realmente se descargaron
	 */
	private AtomicInteger descargasRealizadas = new AtomicInteger(0);
	/**
	 * Contador para las descargas que no fueron necesarias porque ya existían
	 * los archivos
	 */
	private AtomicInteger descargasNoNecesarias = new AtomicInteger(0);
	/**
	 * Contador para las descargas fallidas-No realizadas
	 */
	private AtomicInteger descargasFallidas = new AtomicInteger(0);
	/**
	 * Si true, detiene la descarga
	 */
	private boolean detener = false;
	private List<String> erroresDescarga = new ArrayList<String>();
	private boolean override;
	private Seccion seccion;
	private Elements notasABuscar;

//	public NotesRecolator(String carpetaOrigen, String carpetaDestino, int cantHilos, DiarioDigital diario,
//			Seccion seccion, FormatoSalida formato, boolean override) {
//		this.pathOrigen = carpetaOrigen;
//		this.pathAGuardar = carpetaDestino;
//		this.THREADS_NUMBER = cantHilos;
//		this.diario = diario;
//		this.seccion = seccion;
//		this.formatoSalida = formato;
//		executor = Executors.newFixedThreadPool(THREADS_NUMBER);
//		this.override = override;
//	}

	public NotesRecolator(Integer tHREADS_NUMBER2, DiarioDigital diario2, Elements articulos, String pathAGuardar2) {
		this.THREADS_NUMBER = tHREADS_NUMBER2;
		this.diario = diario2;
		this.notasABuscar = articulos;
		this.pathAGuardar = pathAGuardar2;
		executor = Executors.newFixedThreadPool(THREADS_NUMBER);
	}

	public void iniciar() {

		// Obtener los links asociados a las notas de cada archivo
		for (Element E : notasABuscar) {
			NoteProcessor np = null;
			np = new NoteProcessor(this, "archivoName", E, pathAGuardar, diario, seccion, formatoSalida, override);
			while (((ThreadPoolExecutor) executor).getActiveCount() == THREADS_NUMBER) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			executor.submit(np);
		}
		// Hasta que no termine de descargar todos, que espere..
		while (((ThreadPoolExecutor) executor).getActiveCount() != 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (detener) {
			detener = false;
		}
	}


	public List<String> getErroresDescarga() {
		return erroresDescarga;
	}

}
