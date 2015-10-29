package servicios;

import entities.Note;
import Utils.Utils;

public class AdminNotes {

	/**
	 * Texto a guardar en una única linea:
	 *  "id##link titulo##fechaInicio#fechaFin
	 * @param separador
	 * @return
	 */
	public static String getInfoAGuardar(Note note, String separador) {
		String nota = "";

		if(!validarDatosAGuardar(note))
			return null;

		//ID##Link Titulo##fechaHoraInicio##fechaHoraFin
		return nota+= note.getId()+ separador + note.getLink()+" "+note.getTitulo()+separador + Utils.dateToString1(note.getFechaInit()) + separador+note.getFechaFin();
	}
	
	private static boolean validarDatosAGuardar(Note note)
	{
		return note.getLink() != null && !note.getLink().trim().isEmpty() && note.getTitulo() != null && note.getFechaInit() != null && note.getFechaFin() != null;
	}
	
	/**
	 * Devuelve la diferencia entre la fechaHora que dejó de estar online con la fechaHora
	 * que apareció online
	 * 
	 * @return cantidad de segundos que estuvo online la nota
	 */
	public static Integer getSegundosOnLine(Note note) {
		Integer segs = (int) ((note.getFechaFin().getTime() - note.getFechaInit().getTime())/1000);
		return segs;
	}
	
	/**
	 * Devuelve un string de una linea:
	 * cuerpoN1##cuerpoN2 donde "##" es el parametro 'separador'.
	 * Si el cuerpoN1 == cuerpoN2, entonces devuelve cuerpoN1##void
	 * 
	 * @param n1
	 * @param n2
	 * @param separador
	 * @return
	 */
	public static String getCuerposNotas(Note n1, Note n2, String separador)
	{
		String vacio = "void";
		String cuerpo1 = n1.getCuerpo();
		String cuerpo2 = cuerpo1.equals(n2.getCuerpo()) ? vacio : n2.getCuerpo();
		
		return cuerpo1 + separador + cuerpo2;
	}
}