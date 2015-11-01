package entities;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Utils.Utils;

public class LaNacion extends DiarioDigital {

	private static final String LINK_LANACION = "http://www.lanacion.com.ar";
	private static final String TEXTO_EN_LINK_LANACION = "lanacion.com.ar";
	private static final String CHARSETNAME_LANACION = "utf-8";
	private static final String NOMBREPREFIJO_AGUARDAR = "LaNacion";
	public static final String NOMBRE_DIARIO = "La Nación";
	public static final String NOMBRE_GRUPO_NOTICIAS = "archivo-notas-";
	public static final String NOMBRE_PORTADA = "apertura";
	public static final String NOMBRE_MOSAICO = "mosaico";
	public static final String CLASS_DEPORTES = "categoria deportes floatFix";
	public static final String CLASS_NEGOCIOS = "categoria negocios floatFix";
	public static final String CLASS_SOCIEDAD = "categoria sociedad floatFix";
	public static final String CLASS_IDEAS = "categoria ideas floatFix";
	public static final String CLASS_ESPECTACULOS = "categoria espectaculos floatFix";
	public static final String CLASS_REVISTAS = "categoria revistas floatFix";
	public static final String CLASS_TABLERO_RUGBY = "cajaTablero rugby";
	
	//Descripción nota (volanta) 
	public static final String CLASS_BAJADA = "bajada";
	//Donde está la nota y demás elementos
	public static final String ID_NOTA= "nota";
	//Donde está el cuerpo de la nota (dentro de ID_NOTA)
	public static final String ID_CUERPO= "cuerpo";
	
	
	//Elementos a eliminar de una nota (en el ID_NOTA)
	public static final String CLASS_ARCHIVOS_RELACIONADOS = "S relacionadas";
	public static final String ID_HERRAMIENTAS_SOCIALES = "herramientas-sociales";
	public static final String CLASS_HERRAMIENTAS_HORIZONTALES = "herramientas horizontales";
	public static final String CLASS_MAS_DATOS = "mas-datos";
	public static final String ID_APERTURATECHO = "aperturaTecho";
	public static final String CLASS_EN_ESTA_NOTA = "en-esta-nota";
	public static final String CLASS_MAS_SOBRE_EL_TEMA = "mas-sobre-tema";
	public static final String CLASS_IMAGEN_DESCRIP_NOTA = "primer-parrafo conModal";
	public static final String SELECT_FIGURE = "figure";
	public static final String CLASS_FIRMALANACION = "path floatFix breadcrumb";
	public static final String CLASS_VIDEO_LN_PRIMER_PARRAFO = "primer-parrafo video LN_include_inited";
	public static final String CLASS_TEXTO_CITADO = "S aside-texto";
	
	
	
	

//	public LaNacion(Seccion seccion) {
//		super.setCharsetName(CHARSETNAME_LANACION);
//		super.setLINK(LINK_LANACION);
//		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
//		super.setNombreGrupoNoticias(NOMBRE_GRUPO_NOTICIAS+seccion.getNumeroSeccion());
//		super.setNombreDiario(NOMBRE_DIARIO);
//	}

	public LaNacion() {
		super.setCharsetName(CHARSETNAME_LANACION);
		super.setLINK(LINK_LANACION);
		super.setTextoEnLink(TEXTO_EN_LINK_LANACION);
		super.setNombrePrefijoAGuardar(NOMBREPREFIJO_AGUARDAR);
		super.setNombreGrupoNoticias(NOMBRE_GRUPO_NOTICIAS);
		super.setNombrePortada(NOMBRE_PORTADA);
		super.setNombreMosaico(NOMBRE_MOSAICO);
		super.setNombreDeportes(CLASS_DEPORTES);
		super.setNombreSociedad(CLASS_SOCIEDAD);
		super.setNombreNegocios(CLASS_NEGOCIOS);
		super.setNombreIdeas(CLASS_IDEAS);
		super.setNombreEspectaculos(CLASS_ESPECTACULOS);
		super.setNombreRevistas(CLASS_REVISTAS);
		super.setNombreDiario(NOMBRE_DIARIO);
	}

	@Override
	public String armarLinkActual(String fecha, Seccion seccion) {
		return LINK_LANACION + fecha + seccion.getCodigoSeccion();
	}

	@Override
	public String getNombreArchivo(String fecha) {
		return this.nombrePrefijoAGuardar + "_" + (fecha.contains("/") ? fecha.replace("/", "-") : fecha);
	}

	@Override
	public boolean esValidoPortada(Document doc) {
		return doc.getElementById(this.getNombrePortada()) != null;
	}

	@Override
	public boolean esValidoMosaico(Document doc) {
		return doc.getElementById(this.getNombreMosaico()) != null;
	}
	
	@Override
	public boolean esValidoDeportes(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreDeportes()) != null;
	}

	@Override
	public boolean esValidoSociedad(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreSociedad()) != null;
	}

	@Override
	public boolean esValidoNegocios(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreNegocios()) != null;
	}

	@Override
	public boolean esValidoIdeas(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreIdeas()) != null;
	}

	@Override
	public boolean esValidoEspectaculos(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreEspectaculos()) != null;
	}

	@Override
	public boolean esValidoRevistas(Document doc) {
		return doc.getElementsByAttributeValue("class", this.getNombreRevistas()) != null;
	}

	@Override
	public Element getPortada(Document page) {
		return page.getElementById(this.getNombrePortada());
	}

	@Override
	public Element getMosaico(Document page) {
		return page.getElementById(this.getNombreMosaico());
	}
	
	@Override
	public Element getDeportes(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreDeportes()).first();
	}
	
	@Override
	public Element getSociedad(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreSociedad()).first();
	}

	@Override
	public Element getNegocios(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreNegocios()).first();
	}

	@Override
	public Element getIdeas(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreIdeas()).first();
	}

	@Override
	public Element getEspectaculos(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreEspectaculos()).first();
	}

	@Override
	public Element getRevistas(Document page) {
		return page.getElementsByAttributeValue("class", this.getNombreRevistas()).first();
	}

	@Override
	public Element getNombreGrupoNoticias(Document page) {
		return page.getElementById(this.getNombreGrupoNoticias());
	}

	@Override
	// formato es dd/MM/yyyy
	public String getFechaConFormato(Date fechaDate) {
		return Utils.dtoDD_MM_YYYY(fechaDate);
	}

	@Override
	public Elements getElementNotasABuscar(File file) {
		Element notasABuscar = null;
		try {
			// notasABuscar = Jsoup.parse(file,
			// this.getCharsetName()).getElementById(this.getNombreGrupoNoticias());
			notasABuscar = Jsoup.parse(file, this.getCharsetName()).body();
		} catch (IOException e) {
			return null;
		}
		Elements notas = notasABuscar.getElementsByTag("a").select("[href]");
		return notas;
	}

	@Override
	public boolean isPagina12() {
		return false;
	}

	@Override
	public boolean isLaNacion() {
		return true;
	}

	@Override
	public Note getNotaProcesadaFromDocument(Document doc) {
		if (doc.getElementById(ID_NOTA) == null) {
			System.out.println("No tiene encabezado");
			return null;
		}
		Element encabezado = doc.getElementById(ID_NOTA);
		// Elements firma = encabezado.getElementsByAttributeValue("class",
		// "firma");
		encabezado.getElementsByClass("firma").remove();
		// encabezado.getElementsByClass("bajada").remove();
		Elements volanta = encabezado.getElementsByAttributeValue("class", "volanta");
		Elements titulo = encabezado.getAllElements().select("h1");
		Elements descripcion = encabezado.getAllElements().select("p");
		descripcion.removeAll(volanta);
		Element cuerpo = doc.getElementById("cuerpo");
		
		if(cuerpo == null)
			return null;

		if (cuerpo.getElementsByClass("foto-encolumnada") != null) {
			cuerpo.getElementsByClass("foto-encolumnada").remove();
		}
//		if (cuerpo.getElementsByAttributeValue("class", "conModal") != null) {
//			System.out.println("imagen descripciuon");
//			cuerpo.getElementsByAttributeValue("class", "conModal").remove();
//		}
		if (cuerpo.select(SELECT_FIGURE) != null) {
			cuerpo.select(SELECT_FIGURE).remove();
		}
		if (cuerpo.getElementsByAttributeValue("class", CLASS_TEXTO_CITADO) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_TEXTO_CITADO).remove();
		}
		if (cuerpo.getElementsByAttributeValue("class", CLASS_TABLERO_RUGBY) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_TABLERO_RUGBY).remove();
		}
	
		// Elements archRel = cuerpo.getElementsByAttributeValue("class",
		// "archivos-relacionados");
		// Elements fin = cuerpo.getElementsByAttributeValue("class", "fin");

		return new Note(volanta.text(), titulo.text(), descripcion.text(), cuerpo.text(), "", "", null);
	}

	/**
	 * Elimina partes de la nota que no son necesarias ni para formato html ni txt
	 */
	@Override
	public Document getNotaPreProcesadaFromDocument(Document doc) {
		if (doc.getElementById(ID_NOTA) == null) {
			System.out.println("No tiene encabezado");
			return null;
		}
		Element encabezado = doc.getElementById(ID_NOTA);
		Element cuerpo = doc.getElementById(ID_CUERPO);

		// Eliminar datos innecesarios
		if (cuerpo.getElementsByAttributeValue("class", CLASS_ARCHIVOS_RELACIONADOS) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_ARCHIVOS_RELACIONADOS).remove();
		}
		if (cuerpo.getElementsByClass("fin") != null) {
			cuerpo.getElementsByClass("fin").remove();
		}
		if (encabezado.getElementById("archivoPDF") != null) {
			encabezado.getElementById("archivoPDF").remove();
		}
		if (cuerpo.getElementsByClass(CLASS_EN_ESTA_NOTA) != null) {
			cuerpo.getElementsByClass(CLASS_EN_ESTA_NOTA).remove();
		}
		if (cuerpo.getElementById(ID_APERTURATECHO) != null) {
			cuerpo.getElementById(ID_APERTURATECHO).remove();
		}
		if (cuerpo.getElementById(ID_HERRAMIENTAS_SOCIALES) != null) {
			cuerpo.getElementById(ID_HERRAMIENTAS_SOCIALES).remove();
		}
		if (cuerpo.getElementsByClass(CLASS_MAS_DATOS) != null) {
			cuerpo.getElementsByClass(CLASS_MAS_DATOS).remove();
		}
		if (cuerpo.getElementsByClass(CLASS_MAS_SOBRE_EL_TEMA) != null) {
			cuerpo.getElementsByClass(CLASS_MAS_SOBRE_EL_TEMA).remove();
		}
		if (cuerpo.getElementsByAttributeValue("class", CLASS_FIRMALANACION) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_FIRMALANACION).remove();
		}
		if (cuerpo.getElementsByAttributeValue("class", CLASS_VIDEO_LN_PRIMER_PARRAFO) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_VIDEO_LN_PRIMER_PARRAFO).remove();
		}
		if (cuerpo.getElementsByAttributeValue("class", CLASS_HERRAMIENTAS_HORIZONTALES) != null) {
			cuerpo.getElementsByAttributeValue("class", CLASS_HERRAMIENTAS_HORIZONTALES).remove();
		}

		Document nd = new Document("");
		nd.appendChild(encabezado).appendChild(cuerpo);

		return nd;
	}
	
}
