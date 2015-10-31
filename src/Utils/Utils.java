package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

	/**
	 * Convert date to string with format "yyyy-MM-dd"
	 *
	 * @param sdate
	 *            object date
	 * @return string
	 */
	public static String dtoYYYY_MM_DD(Date sdate) {
		SimpleDateFormat formatedate = new SimpleDateFormat("yyyy-MM-dd");
		// formatedate.setLenient(false);
		return formatedate.format(sdate);
	}
	
	/**
	 * Convert date to string with format "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param sdate
	 * @return
	 */
	public static String dtoYYYY_MM_DD_HH_mm_ss(Date sdate) {
		SimpleDateFormat formatedate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// formatedate.setLenient(false);
		return formatedate.format(sdate);
	}

	/**
	 * Convert date to string with format "dd/mm/yyyy"
	 *
	 * @param sdate
	 *            object date
	 * @return string
	 */
	public static String dtoDD_MM_YYYY(Date sdate) {
		SimpleDateFormat formatedate = new SimpleDateFormat("dd/MM/yyyy");
		// formatedate.setLenient(false);
		return formatedate.format(sdate);
	}

	/**
	 * Suma (o resta si dias <0) los días recibidos a la fecha
	 *
	 * @param fecha
	 * @param dias
	 *            : si >0 suma esa cantidad a fecha. Resta en caso de dias<0
	 * @return
	 */
	public static Date sumarRestarDiasFecha(Date fecha, int dias) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		// numero de días a añadir, o restar en caso de días<0
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

	public static String stringListToString(List<String> stringList,
			String simbolo) {
		String string = "";
		for (String e : stringList) {
			string += simbolo + e;
		}
		return string;
	}

	/**
	 * convierte un string del tipo "yyyy.mm.dd hh:mm:ss" a date
	 * 
	 * @param dateInString
	 * @return
	 */
	public static Date convertStringToDate(String dateInString) {
		return convertToDate(dateInString, "yyyy.MM.dd HH:mm:ss");
	}

	/**
	 * convierte un string del tipo "Fri Oct 02 12:11:25 ART 2015" a date
	 * 
	 * @param dateInString
	 * @return
	 */
	public static Date convertDateStringToDate(String dateInString) {
		return convertToDate(dateInString, "EEE MMM dd HH:mm:ss z yyyy");
	}

	public static Date convertToDate(String dateInString, String formato) {
		if (dateInString != null) {
			Date newDate = null;
			SimpleDateFormat formatter = new SimpleDateFormat(formato,
					Locale.US);
			formatter.setTimeZone(TimeZone.getDefault());
			try {
				newDate = formatter.parse(dateInString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newDate;
		}
		return null;
	}
	
	/**
	 *
	 * @param n: número decimal a redondear
	 * @param cantDecimales: números que van a quedar despues de la ","
	 *
	 * @return n pero con cantDecimales despues de las ","
	 */
	public static Double redondear(Double n, int cantDecimales) {
		return Math.round(n * Math.pow(10, cantDecimales)) / Math.pow(10, cantDecimales);
	}

	/**
	 *
	 * @param n: número decimal a redondear
	 * @param cantDecimales: números que van a quedar despues de la ","
	 *
	 * @return n pero con cantDecimales despues de las "," y lo pasa a entero
	 */
	public static Integer redondearAEntero(Double n, int cantDecimales) {
		return redondear(n, cantDecimales).intValue();
	}
	
	/**
	 * pasa de segundos a min u horas si es necesario. Ejemplo: recibe 120 (son segs);
	 * entonces devuelve el String '2 min'
	 * 
	 * @param segs: tiempo en segundos a cambiar de unidad (si es necesario)
	 * @return el tiempo redondeado a dos decimales en seg, min u hs. 
	 */
	public static String getTime(long segs) {
		double tiempo = 0.0;
		String unidad = "";
		if (segs > 3600) {
			tiempo = (segs / 3600.0);
			unidad = "hs";
		} else if (segs > 60) {
			tiempo = (segs / 60.0);
			unidad = "mins";
		} else {
			tiempo = segs;
			unidad = "segs";
		}
		return Utils.redondear(tiempo, 2) + " " + unidad;
	}
	
	public static String getTime(Integer segs)
	{
		return getTime(segs.longValue());
	}
	
	public static Integer getDiferenciaEnSegundos(Date inicio, Date fin)
	{
		return (int)Math.abs(((fin.getTime() - inicio.getTime()) / 1000));
	}

	/**
	 *  Convierte de una fecha date a string del tipo "yyyymmdd hhmmss"
	 *  Ejemplo date(30/01/2012 20:15:12) lo pasa al string "20120130 201512" 
	 * @param fecha
	 * @return
	 */
	public static String dateToString1(Date fecha) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMdd HHmmss");
		dateFormat.format(fecha);

		return dateFormat.format(fecha).toString();
	}

}
