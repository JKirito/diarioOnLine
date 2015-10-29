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

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public void setFechaInit(Date fechaInit) {
		this.fechaInit = fechaInit;
	}

	public String toString(){
		String nota = "";
		if(!this.volante.trim().isEmpty()){
			nota+="VOLANTE-->"+this.volante + "\r\n";
		}
		if(!this.titulo.trim().isEmpty()){
			nota+= "TITULO-->"+this.titulo +"\r\n";
		}
		if(!this.descripcion.trim().isEmpty()){
			nota+= "DESCRIPCION-->"+this.descripcion + "\r\n";
		}
		if(!this.cuerpo.trim().isEmpty()){
			nota+= "CUERPO-->"+this.cuerpo + "\r\n";
		}
		if(!this.autor.trim().isEmpty()){
			nota+= "AUTOR-->"+this.autor + "\r\n";
		}
		if(!this.link.trim().isEmpty()){
			nota+= "LINK-->"+this.link + "\r\n";
		}
		if(this.fechaInit != null){
			nota+= "FechaInicio-->"+this.fechaInit + "\r\n";
		}
		if(this.fechaFin != null){
			nota+= "FechaFin-->"+this.fechaFin + "\r\n";
		}
		return nota;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}

	public Note clone()
	{
		Note n = new Note(this.volante, this.titulo, this.descripcion, this.cuerpo, this.autor, this.link, this.fechaInit);
		n.setFechaFin(this.fechaFin);
		return n;
	}
	
	/**
	 * 
	 * @param other 
	 * @return true sii el link y título de this y other (parametro) son iguales,
	 * pero el cuerpo es diferente
	 */
	public boolean isSoloCuerpoDiferente(Note other)
	{
		if(other != null && this.getId() == other.getId())
		{
			return other.getCuerpo().equals(this.cuerpo);
		}
		return false;
	}
	
	public int getId()
	{
		return (this.link+this.titulo).hashCode();
	}

}
