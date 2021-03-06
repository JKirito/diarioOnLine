package app;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import servicios.AdminNotes;
import servicios.NoteDownloader;
import servicios.PageDownloader;
import Utils.StoreFile;
import Utils.Utils;
import entities.DiarioDigital;
import entities.LaNacion;
import entities.Note;
import exceptions.ExceptionAlDescargarLink;
import exceptions.ExceptionEstructuraNoValida;

public class Init {

	static String ultimoMsj = "";
	static String MSJ_BUSCANDO_CAMBIOS = "Buscando cambios";
	static String SEPARADOR = "##";
	static boolean MOSTRAR_NOTICIAS = true;
	static String MSJ_INGRESE_PARAMETROS_CORRECTAMENTE = "Ingrese correctamente los parámetros: 'pathGuardarArchivo(texto) SegundosABuscarNuevasNotas(Número) MostrarNoticiasEnConsola(true/false)' ";
	static Integer tiempoReconexion = 60;
	static Integer pollTime;

	public static void main(String[] args) throws InterruptedException {

		//String pathAGuardar = "/home/pruebahadoop/Documentos/DataSets/diarioOnLine/LaNacion/";

		if (args == null || args.length != 3) {
			System.err.println(MSJ_INGRESE_PARAMETROS_CORRECTAMENTE);
			System.exit(1);
		}

		// PATH A GUARDAR [0]
		String pathAGuardar = args[0];
		if(!(new File(pathAGuardar).exists())){
			System.err.println("La ruta ingresada no existe! Ingrese el path de una carpeta existente");
			System.exit(1);
		}
		if(!(new File(pathAGuardar).isDirectory())){
			System.err.println("La ruta ingresada no es un directorio! Ingrese el path de una carpeta existente");
			System.exit(1);
		}


		// POLL TIME [1]
		pollTime = null;

		try {
			pollTime = Integer.valueOf(args[1]); // seg
		} catch (Exception e) {
			System.err.println(MSJ_INGRESE_PARAMETROS_CORRECTAMENTE);
			System.err.println("La frecuencia con la que se buscará nuevas notas debe ser un número expresado en segundos. Ejemplo: 60");
			System.exit(1);
		}
		
		

		//MOSTRAR NOTICIAS [2]
		if(!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
			System.err.println(MSJ_INGRESE_PARAMETROS_CORRECTAMENTE);
			System.exit(1);
		}
		
		MOSTRAR_NOTICIAS = args[2].equalsIgnoreCase("true") ? true : false;
		
		//Carpeta extra para los títulos
		String pathTitulos = pathAGuardar+File.separatorChar+"titulos"+File.separatorChar;
		String pathNotas = pathAGuardar+File.separatorChar+"notas"+File.separatorChar;
		try {
			new File(pathTitulos).mkdirs();
			new File(pathNotas).mkdirs();
		} catch (Exception e) {
			System.out.println("Verifique que tenga permisos para crear archivos y carpetas en la ruta especificada: "+pathAGuardar+"\n\n");
			e.printStackTrace();
			System.exit(1);
		}

		Set<Note> ultimosTitulos = new HashSet<Note>();

		DiarioDigital dLaNacion = new LaNacion();

		System.out.println("Iniciando...");

		while (true) {
//			Date init = new Date();
			PageDownloader pd = new PageDownloader(dLaNacion);
			Set<Note> nuevosTitulos = null;
			boolean pausarDescarga = false;
			boolean detener = false;
			String infoError = "";
			try {
					nuevosTitulos = pd.downloadTitulos();
//					System.out.println("cant titulos online: "+nuevosTitulos.size());
//					System.out.println("tardo "+(new Date().getTime()-init.getTime())/1000);
			} catch (ExceptionAlDescargarLink e) {
				System.out.println("\n"+"Ups! Esto es embarazoso: parece que no ha descargado el link. ¿Está conectado a internet?");
				infoError = e.toString();
				pausarDescarga = true;
			} catch (SocketTimeoutException e) {
				System.out.println("\n"+"Error por Time out");
				infoError = e.toString();
				pausarDescarga = true;
			} catch (UnknownHostException e) {
				System.out.println("\n"+"Error! Esto puede deberse a una desconexión de internet.");
				infoError = e.toString();
				pausarDescarga = true;
			} catch (ExceptionEstructuraNoValida e) {
				System.out.println("\n"+e.getMessage());
				infoError = e.toString();
				detener = true;
			} catch (Exception e) {
				infoError = e.toString();
				pausarDescarga = true;
				mostrarMensaje(e.toString(), true, false, true);
			} finally {
				if(pausarDescarga)
				{
					mostrarMensaje("INFO ADICIONAL DEL ERROR:", true, false, true);
					mostrarMensaje(infoError, true, false, true);
				}
			}
			
			
			if(pausarDescarga)
			{
				mostrarMensaje(Utils.dtoYYYY_MM_DD_HH_mm_ss(new Date())+": Intentando ejecutar en " +Utils.getTime(tiempoReconexion)+ "...\n\n\n", true, true, true);
				Thread.sleep(tiempoReconexion * 1000);
			}
			else if(detener)
			{
				System.out.println("\n"+Utils.dtoYYYY_MM_DD_HH_mm_ss(new Date())+"*PROGRAMA DETENIDO*\n");
				System.exit(1);
			}
			else
			{
				//TODO: agregar que se reconecto (si hubo un error y no estaba descargando notas...)
				/**
				 * 1) Busco si los ultimosTitulos están tmb en los nuevos.
				 * Si NO:
				 * 	-a ese titulo que no está entre los nuevos, eliminarlo de
				 * ultimosTitulos y nuevosTitulos, agregarle la fecha fin y
				 * guardarlo en un archivo.
				 * 	-Mostrar ese titulo por pantalla con el tiempo que estuvo online
				 * Si SI: lo elimino de los nuevos.
				 *
				 * 2) Si hay titulos en los nuevos, entonces los agrego a los
				 * ultimos titulos con la fecha
				 *
				 */
				Date fin = new Date();
				Set<Note> copiaUltimosTitulos = new HashSet<Note>(ultimosTitulos);
				for (Note T : copiaUltimosTitulos)
				{
					//eliminar T de ultimosTitulos, agregarle la fecha fin y guardarlo en un archivo
					if (!nuevosTitulos.contains(T))
					{
						Note copiaAEliminar = T.clone();
						T.setFechaFin(fin);
						mostrarMensaje("Segs online: " + AdminNotes.getSegundosOnLine(T) + " seg. - "+T.toString(), true, true, false);
						NoteDownloader nd = new NoteDownloader(dLaNacion, T.getLink());
						Note notaFinal = null;
						try {
							notaFinal = nd.call();
						} catch (Exception e) {
							mostrarMensaje(e.toString(), true, false, true);
							continue;
						}
						String stringFechaDiaApareceNota = Utils.dtoYYYY_MM_DD(T.getFechaInit());
						//Agrega una linea al archivo con los titulos
						StoreFile s = new StoreFile(pathTitulos, ".txt", AdminNotes.getInfoAGuardar(T, SEPARADOR), stringFechaDiaApareceNota, dLaNacion.getCharsetName());
						
						//Creo un nuevo archivo con el id de la nota, que contiene una única línea con el cuerpo de la nota original y final (por si se modificó)
						StoreFile s2 = new StoreFile(pathNotas, ".txt", AdminNotes.getCuerposNotas(T, notaFinal, SEPARADOR), T.getId()+"", dLaNacion.getCharsetName());
						try {
							s.store(true);
							s2.store(true);
						} catch (IOException e) {
							mostrarMensaje(e.toString(), true, false, true);
							continue;
						}
						ultimosTitulos.remove(copiaAEliminar);
					} else
					{
						nuevosTitulos.remove(T);
					}
				}
	
				int contTitulos = 1;
				if(ultimosTitulos.size() > 0 && nuevosTitulos.size() > 0)
				{
					mostrarMensaje("\nTitulos actuales\n", true, true, false);
					for (Note N : ultimosTitulos)
					{
						mostrarMensaje(contTitulos + ") " + N.toString(), true, true, false);
						contTitulos++;
					}
	
				}
	
				////////////////////////////////
				//   AGREGAR NUEVOS TITULOS  //
				//////////////////////////////
				for (Note N : nuevosTitulos)
				{
					ultimosTitulos.add(N);
					mostrarMensaje(contTitulos+") " + "NUEVA NOTA: " + N.toString(), true, true, false);
					contTitulos++;
				}
	
				// Mostrar mensaje buscando...
				if(!ultimoMsj.equals(MSJ_BUSCANDO_CAMBIOS) && !ultimoMsj.equals("."))
				{
					mostrarMensaje(MSJ_BUSCANDO_CAMBIOS, false, true, false);
				}
				else
				{
					mostrarMensaje(".", false, true, false);
				}
	
				try {
					Thread.sleep(pollTime * 1000);
				} catch (InterruptedException e) {
					mostrarMensaje(e.toString(), true, false, true);
				}
			}
		}

	}

	/**
	 *
	 * @param mensaje: texto a mostrar por pantalla
	 * @param nuevaLinea: deja nueva linea al mostrar el msj (\n)
	 * @guardarMsj: Guarda en una variable el último a mostrar
	 */
	private static void mostrarMensaje(String mensaje, boolean nuevaLinea, boolean guardarMsj, boolean isError) {
		//Si no quiere ver noticias, sólo muestro que está "Buscando cambios..."
		if ((!MOSTRAR_NOTICIAS && !isError && !mensaje.equals(MSJ_BUSCANDO_CAMBIOS) && !mensaje.equals("."))
				|| (mensaje.equals(MSJ_BUSCANDO_CAMBIOS) && ultimoMsj.equals(MSJ_BUSCANDO_CAMBIOS)))
			return;
		
		if(guardarMsj)
			ultimoMsj = mensaje;
		
		if(mensaje.equals(MSJ_BUSCANDO_CAMBIOS))
			mensaje = Utils.dtoYYYY_MM_DD_HH_mm_ss(new Date())+ ": " + mensaje + " (cada "+pollTime+" segs)";
		if(nuevaLinea)
			System.out.println(mensaje);
		else
			System.out.print(mensaje);
	}

}
