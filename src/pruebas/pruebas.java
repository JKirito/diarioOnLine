package pruebas;

import servicios.NoteDownloader;
import entities.DiarioDigital;
import entities.LaNacion;

public class pruebas {

	public static void main(String[] args) throws Exception {
		DiarioDigital dd = new LaNacion();
		
		String link = "http://www.lanacion.com.ar/1841474-reforzaron-la-seguridad-en-dos-shoppings-por-alerta-de-atentados"; 
		
		NoteDownloader n = new NoteDownloader(dd, link);
		System.out.println(n.call().toString());
	}

}
