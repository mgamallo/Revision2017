
public class RegistroAyuda {

	private String rutaImagen;
	private String nombreDocumento;
	private String servicios;
	private String observaciones;
		
	public RegistroAyuda(String rutaImagen, String nombreDocumento,
			String servicios, String observaciones) {
		super();
		this.rutaImagen = rutaImagen;
		this.nombreDocumento = nombreDocumento;
		this.servicios = servicios;
		this.observaciones = observaciones;
	}
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	public String getNombreDocumento() {
		return nombreDocumento;
	}
	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}
	public String getServicios() {
		return servicios;
	}
	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
}
