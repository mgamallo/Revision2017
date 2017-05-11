import java.util.ArrayList;


public class Modelo {
	
	String rutaImagen ="";
	String nombreNormalizado ="";
	ArrayList<String> servicios = new ArrayList<String>();           // 0 los propios del nombre Normalizado
	
	int numImagen = 0;
	
	Fisica fisica = new Fisica();
	
	Metadatos metadatos = new Metadatos();
	
//	String nombreAlternativo = "";
	
	String instruccionesNHC = "";
	String instruccionesCIP = "";
	String instruccionesNSS = "";
	
	boolean servicioFijo = false;
	boolean modeloEspecial = false;
	
	String centroExterno = "";
	
	String claveCentroExterno1 = "";
	String claveCentroExterno2 = "";
	String claveCentroExterno3 = "";
	
	void setMetadatos(){
		
	}
	
	void setFisica(){
		
	}
	

	
	void setServiciosModelo(String cadenaExcel){
		if(!cadenaExcel.equals("0") && !cadenaExcel.equals("Todos")){
			servicios.add(cadenaExcel);
			servicioFijo = true;
		}
		else{
			servicios.add("X");
		}
	}
}

class Metadatos{
	
	String metaLocalizacion[] = new String[2];
	
	String metaServicioNombre = "";
	String metaNombre = "";
	String metaServicio = "";
	String metaModelo = "";
	
	/*
	ArrayList<String> metaAuxiliares = new ArrayList<String>();
	*/
	
	
}