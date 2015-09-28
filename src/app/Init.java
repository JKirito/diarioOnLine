package app;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import servicios.PageDownloader;
import Utils.StoreFile;
import Utils.Utils;
import entities.DiarioDigital;
import entities.LaNacion;
import entities.Note;

public class Init {

	static String ultimoMsj = "";
	static String MSJ_BUSCANDO_CAMBIOS = "Buscando cambios...";

	public static void main(String[] args) {

		//String pathAGuardar = "/home/pruebahadoop/Documentos/DataSets/diarioOnLine/LaNacion/";

		if(args == null || args.length != 3)
		{
			System.err.println("Debe indicar los parámetros: 'RutaCarpetaGuardarArchivo SegundosABuscarNuevasNotas SoloPortada(true/false') ");
			System.exit(1);
		}

		// PATH A GUARDAR
		String pathAGuardar = args[0];
		if(!(new File(pathAGuardar).exists())){
			System.err.println("La ruta ingresada no existe! Ingrese el path de una carpeta existente");
			System.exit(1);
		}
		if(!(new File(pathAGuardar).isDirectory())){
			System.err.println("La ruta ingresada no es un directorio! Ingrese el path de una carpeta existente");
			System.exit(1);
		}


		// POLL TIME
		Integer pollTime = null;

		try {
			pollTime = Integer.valueOf(args[1]); // seg
		} catch (Exception e) {
			System.err.println("Ingrese correctamente los parámetros: 'pathGuardarArchivo(texto) SegundosABuscarNuevasNotas(Número) SoloPortada(true/false')' ");
			System.exit(1);
		}

		if(!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
			System.err.println("Ingrese correctamente los parámetros: 'pathGuardarArchivo(texto) SegundosABuscarNuevasNotas(Número) SoloPortada(true/false')' ");
			System.err.println("El parámetro SoloPortada = true solamente buscará y descargará títulos de la portada online");
			System.err.println("El parámetro SoloPortada = false buscará y descargará tanto títulos de la portada como mosaico online");
			System.exit(1);
		}

		// SOLO PORTADA?
		boolean soloPortada = args[2].equalsIgnoreCase("true") ? true : false;


		Set<Note> ultimosTitulos = new HashSet<Note>();

		DiarioDigital dd = new LaNacion();
		int contPuntosBuscando = 0;

		while (true) {

			PageDownloader pd = new PageDownloader(dd, soloPortada);
			Set<Note> nuevosTitulos = pd.downloadTitulos();

			/**
			 * 1) Busco si los ultimosTitulos están tmb en los nuevos. Si NO: -a
			 * ese titulo que no está entre los nuevos, eliminarlo de
			 * ultimosTitulos y nuevosTitulos, agregarle la fecha fin y
			 * guardarlo en un archivo. -Mostrar ese titulo por pantalla con el
			 * tiempo que estuvo online Si SI: lo elimino de los nuevos.
			 *
			 * 2) Si hay titulos en los nuevos, entonces los agrego a los
			 * ultimos titulos con la fecha
			 *
			 */
			Date fin = new Date();
			Set<Note> copiaUltimosTitulos = new HashSet<Note>(ultimosTitulos);
			for (Note T : copiaUltimosTitulos)
			{
				if (!nuevosTitulos.contains(T))
				{
					ultimosTitulos.remove(T);
					T.setFechaFin(fin);
					mostrarMensaje("Segs online: " + T.getSegundosOnLine() + " seg. - "+T.toString(), true, true);
					// TODO: guardar en archivo
					String fecha = Utils.dtoYYYY_MM_DD(T.getFechaInit());
					StoreFile s = new StoreFile(pathAGuardar, ".txt", T.toString(), fecha, dd.getCharsetName());
					try {
						s.store(true);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
				{
					nuevosTitulos.remove(T);
				}
			}

			int contTitulos = 1;
			if(ultimosTitulos.size() > 0 && nuevosTitulos.size() > 0)
			{
			mostrarMensaje("\nTitulos actuales\n", true, true);
			for (Note N : ultimosTitulos) {
				mostrarMensaje(contTitulos+") "+N.toString(), true, true);
				contTitulos++;
			}

			}

			////////////////////////////////
			//   AGREGAR NUEVOS TITULOS  //
			//////////////////////////////
			for (Note N : nuevosTitulos)
			{
				ultimosTitulos.add(N);
				mostrarMensaje(contTitulos+") " + "NUEVA NOTA: " + N.toString(), true, true);
				contTitulos++;
			}

			// Mostrar mensaje buscando...
			if(!ultimoMsj.equals(MSJ_BUSCANDO_CAMBIOS))
			{
				mostrarMensaje("", true, false);
				mostrarMensaje(MSJ_BUSCANDO_CAMBIOS, false, true);
			}
			else
			{
				//Si es 58, entonces ya mostró 57 "." (más los 3 iniciales del MSJ_BUSCANDO_CAMBIOS, son 60 = 1hs)
				//Cada 60 ".", sigo mostrando "." debajo.
				boolean nuevaLinea = contPuntosBuscando == 58;
				mostrarMensaje(".", nuevaLinea, false);
				contPuntosBuscando++;
			}

			try {
				Thread.sleep(pollTime * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 *
	 * @param mensaje: texto a mostrar por pantalla
	 * @param nuevaLinea: deja nueva linea al mostrar el msj (\n)
	 */
	private static void mostrarMensaje(String mensaje, boolean nuevaLinea, boolean guardarMsj) {
		if(ultimoMsj.equals(MSJ_BUSCANDO_CAMBIOS) && !mensaje.equals(MSJ_BUSCANDO_CAMBIOS) && !mensaje.equals("."))
			mensaje = "\n "+ mensaje;
		if(guardarMsj)
			ultimoMsj = mensaje;
		if(nuevaLinea)
			System.out.println(mensaje);
		else
			System.out.print(mensaje);
	}

}
