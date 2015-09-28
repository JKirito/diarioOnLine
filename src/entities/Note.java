package entities;


import java.util.Date;

public class Note {
	private String volante;
	private String titulo;
	private String descripcion;
	private String cuerpo;
	private String autor;
	private String link;
	private Date fechaInit;
	private Date fechaFin;

	public Note(String volante, String titulo, String descripcion, String cuerpo, String autor, String link, Date fechaPublicacion) {
		super();
		this.volante = volante;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.cuerpo = cuerpo;
		this.autor = autor;
		this.link = link;
		this.fechaInit = fechaPublicacion;
	}

	public String getVolante() {
		return volante;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public String getAutor() {
		return autor;
	}

	public String getLink() {
		return link;
	}

	public Date getFechaInit() {
		return fechaInit;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date date) {
		this.fechaFin = date;
	}

	public Integer getSegundosOnLine() {
		//Lo paso a Segundos
		Integer segs = (int) ((this.fechaFin.getTime() - fechaInit.getTime())/1000);
		return segs;
	}

	public String toString(){
		String nota = "";
		if(!this.volante.trim().isEmpty()){
			nota+=this.volante + "\r\n";
		}
		if(!this.titulo.trim().isEmpty()){
			nota+= this.titulo +"\r\n";
		}
		if(!this.descripcion.trim().isEmpty()){
			nota+= this.descripcion + "\r\n";
		}
		if(!this.cuerpo.trim().isEmpty()){
			nota+= this.cuerpo + "\r\n";
		}
		if(!this.autor.trim().isEmpty()){
			nota+= this.autor + "\r\n";
		}
		if(!this.link.trim().isEmpty()){
			nota+= this.link + "\r\n";
		}
		if(this.fechaInit != null){
			nota+= "apareció: " + this.fechaInit + "\r\n";
		}
		if(this.fechaFin != null){
			nota+= "Fin:" + this.fechaFin + "\r\n";
		}
		if(this.fechaInit != null && this.fechaFin != null){
			nota+= "Segs online: " + this.getSegundosOnLine() + "\r\n";
		}
		return nota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((volante == null) ? 0 : volante.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (volante == null) {
			if (other.volante != null)
				return false;
		} else if (!volante.equals(other.volante))
			return false;
		return true;
	}

}
