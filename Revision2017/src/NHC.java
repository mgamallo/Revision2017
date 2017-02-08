import javax.swing.JOptionPane;


public class NHC {
	static public String nhcTriaje(Documento doc){
		
		final String  TRIAJE = "Informe triaje";
		
		if(doc.nombreNormalizado.equals(TRIAJE)){
			
			
			System.out.println("Detectando triajes \n");
			
			String subCadena = subCadena(doc,300);
			
			System.out.println(subCadena);
			
			int contador = subCadena.indexOf("Loca") + 10;
		
			
			System.out.println("Loca está en el número + 10... " + contador);
			
			if(contador == 9)
				return "NO";
			
			boolean primerNumero = false;
			boolean espacioBlanco = false;
			boolean encontrado = false;
			boolean segundoNumero = false;
			
			String nhcS = "";
			
			while(!encontrado && contador <subCadena.length()){
				char c = subCadena.charAt(contador);
				System.out.println(c);
				if(!primerNumero){
					if(c >= '0' && c <= '9'){
						primerNumero = true;
					}
				}
				else{
					if(!espacioBlanco){
						if(c == ' '){
							espacioBlanco = true;
						}
					}
					else{
						if(c >= '0' && c <= '9'){
							segundoNumero = true;
							nhcS += c;
						}
						else if(segundoNumero){
							return nhcS;	
						}
	
					}
				}
				contador++;
			}
		}
		
		return "NO";
	}
	
	
	static public String nhcTriaje143(Documento doc){
		final String  TRIAJE = "Informe triaje";
		
		if(doc.nombreNormalizado.equals(TRIAJE)){
			
			
			System.out.println("Detectando triajes \n");
			
			String subCadena = doc.cadenaOCR;
			
			System.out.println(subCadena);
			
			int contador = subCadena.indexOf("143");
		
			
			System.out.println("143 está en el número... " + contador);
			
			if(contador == -1)
				return "NO";
			
			boolean espacioBlanco = false;
			boolean encontrado = false;
			boolean segundoNumero = false;
			
			String nhcS = "";
			
			while(!encontrado && contador <subCadena.length()){
				char c = subCadena.charAt(contador);
				System.out.println(c);

				if(!espacioBlanco){
					if(c == ' '){
						espacioBlanco = true;
					}
				}
				else{
					if(c >= '0' && c <= '9'){
						segundoNumero = true;
						nhcS += c;
					}
					else if(segundoNumero){
						if(c == ' ' || c == 10)
							return nhcS;
						else
							return nhcS + "ERROR";
					}
	
				}
				contador++;
			}
		}
		
		return doc.nhc;
		
	}
	
	private static String subCadena(Documento doc, int tamaño){
		int limiteCadena = doc.cadenaOCR.length();
		if (limiteCadena > tamaño)
			limiteCadena = tamaño;
		
		return doc.cadenaOCR.substring(0,limiteCadena);
	}
	
	static boolean borrarNHC(Documento doc){
		
		if(doc.nhc.equals("23515")){
			doc.semaforoAmarilloNhc = true;
			if(doc.cadenaOCR.contains("023515") || doc.nombreNormalizado.equals(Inicio.ENFERMERIA_QUIRURGICA)){
				return true;
			}
			else{
				return false;
			}
		}
		else if(doc.nhc.equals("23664")){
			doc.semaforoAmarilloNhc = true;
			if(doc.cadenaOCR.contains("023664") || doc.nombreNormalizado.equals(Inicio.ORDENES_MEDICAS)){
				return true;
			}
			else{
				return false;
			}
		}
		else if(doc.nhc.equals("23669")){
			doc.semaforoAmarilloNhc = true;
			if(doc.cadenaOCR.contains("023669") || doc.nombreNormalizado.equals(Inicio.REGISTRO_ANESTESIA)){
				return true;
			}
			else{
				return false;
			}
		}
		else if(doc.nhc.equals("23630")){
			doc.semaforoAmarilloNhc = true;
			if(doc.cadenaOCR.contains("023630") || doc.nombreNormalizado.equals(Inicio.INFORME_ALTA)){
				return true;
			}
			else{
				return false;
			}
		}
		else if(doc.nhc.equals("36001") &&  doc.nombreNormalizado.equals(Inicio.DENSITOMETRIA)) {
			return true;
		}
		else{
			return false;
		}

	}
}
