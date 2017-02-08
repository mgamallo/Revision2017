
public class Excepciones {
	
	static public boolean excepcionesIngresos(int numPdf) {
		// TODO Auto-generated method stub
		if(Inicio.listaDocumentos[numPdf].nombreNormalizado.equals(Inicio.CONSENTIMIENTO) ){
			if(Inicio.listaDocumentos[numPdf].servicio.equals(Inicio.HOSP)){
				Inicio.listaDocumentos[numPdf].servicio = "X";
			}
			
			System.out.println("Esta es una excepción al ingreso");
			
			return true;
		}
		else if(Inicio.listaDocumentos[numPdf].nombreNormalizado.equals(Inicio.CRIBADO)){
			Inicio.listaDocumentos[numPdf].servicio = Inicio.ORLC;
			
			System.out.println("Esta es una excepción al ingreso");
			
			return true;
		}
		else if(Inicio.listaDocumentos[numPdf].servicio.equals(Inicio.DIGC)){
			return true;
		}
		else if(Inicio.listaDocumentos[numPdf].nombreNormalizado.equals(Inicio.INCLUSION)){
			return true;
		}

		return false;
	}

	static public boolean excepcionesNeuro(int numPdf){
		
		return false;
	}
	
	static public boolean detectaMonitoriz(int numPdf){
		return false;
	}
	
	static public boolean detectaDocRosaNeuro(int numPdf){
		return false;
	}
	
	static public boolean detectaOrdenesMedicas(int numPdf){
		
		boolean encontrado = false;
		
		String claves[] = {"41.0","ORDES MEDICAS","ORDES MED","MEDICAS","DICAS","ORDES M","DES MEDIC","023664"};
		
		for(int i=0;i<claves.length;i++){
			if(Inicio.listaDocumentos[numPdf].cadenaOCR.contains(claves[i])){
				encontrado = true;
				break;
			}
		}
		
		return encontrado;
	}
	
	static public boolean detectaProtocolo(int numPdf){
		
		boolean encontrado = false;
		
		if(Inicio.destinoDocumentacion == 0){
			return false;
		}
		else{
			String claves[] = {"52.0","PRO","OTOCOLO","023669","ANEST"};
			
			for(int i=0;i<claves.length;i++){
				if(Inicio.listaDocumentos[numPdf].cadenaOCR.length()<25 && Inicio.listaDocumentos[numPdf].cadenaOCR.contains(claves[i])){
					encontrado = true;
					break;
				}
			}
		}

		
		return encontrado;
	}
}
