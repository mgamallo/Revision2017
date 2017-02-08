
public class Facultativo {

	private String nombre;
	private String servicio;
	
	/**
	 * @param nombre
	 * @param servicio
	 */
	public Facultativo(String nombre, String servicio) {
		super();
		this.nombre = nombre;
		this.servicio = servicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	

}
