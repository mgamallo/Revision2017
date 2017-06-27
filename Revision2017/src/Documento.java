import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.Base64.InputStream;



public class Documento {
	
	public static final int TAMAÑO_PAG_BLANCO = 20;

	String rutaArchivo ="";					//	Ruta absoluta
	String nhc ="X";												// ok
	String servicio ="X";
	String nombreNormalizado = "X";
	String fecha = "";
	
	String cadenaOCR = "";										// ok
	
	String numeroModelo = "";
	
	ArrayList<String> metadatos = new ArrayList<String>();
	Fisica fisica = new Fisica();								// ok
	
	Boolean revisado = false;
	Boolean semaforoAmarilloServicio = false;
	Boolean semaforoAmarilloNombre = false;
	Boolean semaforoAmarilloNhc = false;
	
	Boolean modificado = false;   // Saber si reconoció mal el documento
	
	Mapa mapa;
	
	Documento(String rutaArchivo){
		this.rutaArchivo = rutaArchivo;
		mapa = new Mapa(rutaArchivo);
		cadenaOCR = mapa.textoPag1;
		// nhc = mapa.nhc;
		// fisica = mapa.fisica;
		/* Si no localizamos el nhc, luego lo mapeamos, primero con la ayuda de saber
		   que documento es, luego, fuerza bruta.
		 */
		
		// detectaPaginasBlanco();
	}

	public void getNhc(){
		nhc = mapa.nhc;
		fisica = mapa.fisica;
	}
	
	void setNumeroModelo(int imagen, String columna){
		numeroModelo = (imagen) + columna;
		
		// System.out.println(numeroModelo);
		
	}
	
	boolean reDetectorNHCUrgencias(){
		if(nhc.contains("ERROR") || nhc.contains("NO")){
			if(nombreNormalizado.equals("Informe urgencias") || nombreNormalizado.equals("Enfermería urgencias") ){
				int limiteCadena = cadenaOCR.length();
				if(limiteCadena > 200)
					limiteCadena =  200;
				String subCadena = cadenaOCR.substring(0, limiteCadena);
				String nhcS = "";
				boolean buscandoFinal = false;
				for(int i=0;i<limiteCadena;i++){
					char c = subCadena.charAt(i);
					if(c >= '0' && c <= '9'){
						buscandoFinal = true;
						nhcS += c;
					}
					else if(buscandoFinal){
						if(c != ' ' && c != 10){
							return false;
						}
						else{
							if(nhcS.charAt(0) == '0'){
								return false;
							}
							nhc = nhcS;
							semaforoAmarilloNhc = true;
							return true;
						}
						
					}
				}
				
			}
		}
		return false;
	}
	
	
	boolean reDetectorNHC(Modelo modelo){
		
		//System.out.println("Imprimimos nhc: " + nhc);
		if(nhc.contains("ERROR") || nhc.contains("NO")){
			//System.out.println("Instrucciones: " + modelo.instruccionesNHC);
			if(!modelo.instruccionesNHC.equals("")){
											
				int limiteCadena = cadenaOCR.length();
				if(limiteCadena > 300)
					limiteCadena =  300;
				String subCadena = cadenaOCR.substring(0, limiteCadena);
				
				System.out.println("Buscando nhc:");
				System.out.println("Palabra clave... " + modelo.instruccionesNHC);
				System.out.println(subCadena);
				
				// JOptionPane.showMessageDialog(null, subCadena);
				
				
				int contador = subCadena.indexOf(modelo.instruccionesNHC);
								
				//System.out.println("El numero del contador: " + contador);
				
				if( contador != -1){
					
					System.out.println("El dato clave es... " + modelo.instruccionesNHC);
					
					contador = contador + modelo.instruccionesNHC.length() /* + 1 */;
					if(contador >= subCadena.length()){
						contador = subCadena.length() - 1;
					}
					
					System.out.println(this.rutaArchivo);
					//System.out.println("Cacho cadena: " + subCadena.substring(contador - modelo.instruccionesNHC.length() - 2 , contador + 10));
					
					String nhcS = "";
					boolean fin = false;
					boolean error = false;
					
				//	System.out.println("SubCadena... " + subCadena + ". Contador... " + contador);
					
					while(!fin && contador!= -1 && (contador < subCadena.length())){
						
						char c = subCadena.charAt(contador);
						System.out.println("Caracter:  " + c);
						if(c != ' ' && c != 10 ){
							if(c >= '0' && c <= '9'){
								nhcS += c;
							}
							else if(c !='.'){
								error = true;
								fin = true;
							}
						}
						else if(nhcS.length() > 0){
							fin = true;
						}
						
						contador++;
					}
					
					if(!error){
						if(nhcS.length()>0){
							nhc = nhcS;
							System.out.println("Detectado ianus, ahora vale..." + nhc);
							return true;
						}
					}
					else{
						nhc = nhcS + "ERROR";
						return false;
					}
				}
			}
		}
		
		return false;
	}
	
	
	boolean escanerNHC(String numeroModelo){
		
		
		ArrayList<ParClaveNumero> listaNumeros = new ArrayList<ParClaveNumero>();
		
		//System.out.println("Imprimimos nhc: " + nhc);
		// JOptionPane.showMessageDialog(null, "Imprimimos nhc: " + nhc);
		if(nhc.contains("ERROR") || nhc.contains("NO")){
			//System.out.println("Instrucciones: " + modelo.instruccionesNHC);
			
			
			int limiteCadena = cadenaOCR.length();
			String subCadena;
			if(limiteCadena > 300){
				subCadena = cadenaOCR.substring(0, 300);
				limiteCadena = subCadena.length();
			}
			else{
				subCadena = cadenaOCR;
			}

			if(nombreNormalizado.equals(Inicio.DENSITOMETRIA)){
				limiteCadena = cadenaOCR.length();
				if(limiteCadena > 500){
					subCadena = cadenaOCR.substring(0, 500);
					limiteCadena = subCadena.length();
				}
				else{
					subCadena = cadenaOCR;
				}
			}
			
			boolean inicio = false;
			String numero = "";
			int indiceFin = 0;
			String clave = "";
			
		//	JOptionPane.showMessageDialog(null, subCadena );
			
			for(int i=0;i<limiteCadena;i++){
				char caracter = subCadena.charAt(i);
				if((caracter >= '0' && caracter <= '9') || caracter == '.'){
					if(inicio){
						numero += String.valueOf(caracter);
					}
					else{
						if(caracter != '.'){
							inicio = true;
							numero = String.valueOf(caracter);
						}
						
					}
				}
				else{
					if(inicio){
						if(clave.length()>16){
							clave = clave.substring(clave.length()- 15);
						}

						listaNumeros.add(new ParClaveNumero(clave, numero, indiceFin));
						inicio = false;
						clave = "";
						numero = "";
					}
					else{
						clave += String.valueOf(caracter);
					}
				}
				
				
			}
			
			
			for(int i=0;i<listaNumeros.size();i++){
				System.out.println(listaNumeros.get(i).clave + "  " + listaNumeros.get(i).numero);
			}
			
		//	JOptionPane.showMessageDialog(null, "Fin obtener numeros" );
			
			boolean encontrado = false;
			
			if(numeroModelo.length()>0){
				int i = Integer.valueOf(numeroModelo.substring(0,numeroModelo.length()-1));
				System.out.println("Número de modelo .... " + i);
				
				System.out.println("Instrucciones... " + Inicio.listaCompletaModelos.get(i-1).instruccionesNHC);
				System.out.println("Longitud Instrucciones... " + Inicio.listaCompletaModelos.get(i-1).instruccionesNHC.length());
				if(Inicio.listaCompletaModelos.get(i-1).instruccionesNHC.length() > 0){
					for(int z=0;z<listaNumeros.size();z++){
						if(listaNumeros.get(z).clave.contains(Inicio.listaCompletaModelos.get(i-1).instruccionesNHC)){
						//	JOptionPane.showMessageDialog(null, listaNumeros.get(z).numero);
							
							if(esNumerico(numeroSinPunto(listaNumeros.get(z).numero))){
								nhc = numeroSinPunto(listaNumeros.get(z).numero);
								encontrado = true;
								break;
							}

						}
					}
				}

			}
			
			
			// JOptionPane.showMessageDialog(null, nombreNormalizado + "  " + encontrado + "  " + nhc);
			// JOptionPane.showMessageDialog(null, subCadena);
			
			if(!encontrado){
				
				// JOptionPane.showMessageDialog(null, encontrado + "  " + listaNumeros.size());
				
				for(int z=0;z<listaNumeros.size();z++){
					Iterator<String> it = Inicio.conjuntoClavesNhc.iterator();
					while(it.hasNext() && !encontrado){
						String cl = it.next();
						
						if(listaNumeros.get(z).clave.contains(cl)){
							// JOptionPane.showMessageDialog(null, listaNumeros.get(z).clave + " es la clave de " + listaNumeros.get(z).numero);
							
							// JOptionPane.showMessageDialog(null, listaNumeros.get(z).clave + " es la clave de " + listaNumeros.get(z).numero);
							// JOptionPane.showMessageDialog(null, numeroModelo);
							 
							 if(numeroModelo.length() > 0){
									int i = Integer.valueOf(numeroModelo.substring(0,numeroModelo.length()-1));
									
									if(!( Inicio.listaCompletaModelos.get(i-1).nombreNormalizado.equals(Inicio.ESPIROMETRIA) &&
											cl.equals("Nombre:"))){
										String aux = listaNumeros.get(z).numero;
										
										aux = compruebaErrores(aux, subCadena, cl);
										if(esNumerico(numeroSinPunto(aux))){
											nhc = numeroSinPunto(aux);
											encontrado = true;
											break;
										}
										
									//	encontrado = true;
										
									//	JOptionPane.showMessageDialog(null, subCadena);
									//	JOptionPane.showMessageDialog(null, nhc);
										
									}
									else{
										int in = 0;
										int valMaximo = 0;
										for(int k=0;k<listaNumeros.size();k++){
											if(listaNumeros.get(k).numero.length() > valMaximo){
												in = k;
												valMaximo = listaNumeros.get(k).numero.length();
											}
										}
										String aux = listaNumeros.get(in).numero;
										aux = compruebaErrores(aux, subCadena, cl);
										
										if(esNumerico(numeroSinPunto(aux))){
											nhc = numeroSinPunto(aux);
											encontrado = true;
											semaforoAmarilloNhc = true;
										}
									//	JOptionPane.showMessageDialog(null, subCadena);
									//	JOptionPane.showMessageDialog(null, nhc);
										
									}
							 }
							 /*
							 else{
								 JOptionPane.showMessageDialog(null, "Seguimos buscando");
								
								int in = 0;
								int valMaximo = 0;
								for(int k=0;k<listaNumeros.size();k++){
									if(listaNumeros.get(k).numero.length() > valMaximo){
										in = k;
										valMaximo = listaNumeros.get(k).numero.length();
									}
								}
								nhc = listaNumeros.get(in).numero;
								JOptionPane.showMessageDialog(null, nhc);
								encontrado = true;
							}
							 */
							 
						}
					}
					
					
					if(!encontrado && nombreNormalizado.equals(Inicio.EKG)){
						int in = 0;
						int valMaximo = 0;
						for(int k=0;k<listaNumeros.size();k++){
							if(listaNumeros.get(k).numero.length() > valMaximo){
								in = k;
								valMaximo = listaNumeros.get(k).numero.length();
							}
						}
						
						String aux = listaNumeros.get(in).numero;
						
						if(esNumerico(numeroSinPunto(aux))){
							nhc = numeroSinPunto(aux);
							encontrado = true;
							semaforoAmarilloNhc = true;
						}
					}
					
					if(!encontrado){
						int in = 0;
						int valMaximo = 0;
						for(int k=0;k<listaNumeros.size();k++){
							if(listaNumeros.get(k).numero.length() > valMaximo){
								in = k;
								valMaximo = listaNumeros.get(k).numero.length();
							}
						}
						
						
						if(esNumerico(numeroSinPunto(listaNumeros.get(in).numero))){
							int numer = Integer.valueOf(numeroSinPunto(listaNumeros.get(in).numero));
							
							if((numer > 999 && numer < 700000) || (numer > 1999999 && numer < 2290000)){
								nhc = listaNumeros.get(in).numero;
								// JOptionPane.showMessageDialog(null, nhc);
								
								if(nhc.equals("1986")){
									if(subCadena.contains("/1986")){
										nhc = "NO";
									}
								}
								else{
									semaforoAmarilloNhc = true;
								}
								
								encontrado = true;
								
							}
						}

						
					}
					
					
					
				}
			}
			
	/*
			System.out.println("Inicio " + numeroModelo);
			for(int i=0;i<listaNumeros.size();i++){
				System.out.println(listaNumeros.get(i).clave + "   " + listaNumeros.get(i).numero);
			}
			System.out.println("Fin");
			System.out.println("");
	*/
		}
		return false;
	}
	
	String compruebaErrores(String nhc, String cadena, String clave){
		
	//	JOptionPane.showMessageDialog(null,nhc);
		
		// Errores posteriores al nhc
		int indicePosterior = nhc.length();
		indicePosterior = cadena.indexOf(nhc) + indicePosterior;
		char c = cadena.charAt(indicePosterior);
		
		// JOptionPane.showMessageDialog(null,"Posterior... " + cadena.substring(indicePosterior));
		
		if(c != ' ' && c != 10 ){
			nhc = nhc + "ERROR";
			return nhc;
		}
		
		// Errores anteriores al nhc
		int indiceAnterior = cadena.indexOf(nhc);
		int indiceFinclave = clave.length();
		indiceFinclave = cadena.indexOf(clave) + indiceFinclave;
		
		
	//	JOptionPane.showMessageDialog(null,"Anterior... " + cadena.substring(indiceFinclave,indiceAnterior));
		
		for(int i=indiceFinclave;i<indiceAnterior;i++){
			c = cadena.charAt(i);
			if(!cadena.contains(".") && c != ' ' && c != 10 ){
				nhc = "ERROR" + nhc;
				return nhc;
			}
			else if(cadena.contains(".")){
				String aux = "";
				for(int j=0;j<nhc.length();j++){
					c = nhc.charAt(j);
					if(c != '.'){
						aux += c;
					}
				}
				return aux;
			}
		}
		
		String aux = "";
		for(int i=0;i<nhc.length();i++){
			c = nhc.charAt(i);
			if(c != '.'){
				aux += c;
			}
		}
		/*
		if(aux.equals("1986")){
			if(cadena.contains("/1986")){
				return "NO";
			}
			
		}
		*/
		return aux;
	}
	
	String numeroSinPunto(String posibleNumero){
		
		String aux = "";
		for(int i=0;i<posibleNumero.length(); i++){
			char c = posibleNumero.charAt(i);
			if(c != '.'){
				aux += c;
			}
		}
		
		return aux;
	}
	
	boolean esNumerico(String s){
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException nfe){	
			return false;
		}
	}
	
	
	boolean detector(Modelo modelo, int colum) { // column = 1, servynombre;
													// column = 2, nombre;
													// column = 3, modelo
/*
		System.out.println(modelo.numImagen + "  " + modelo.nombreNormalizado
				+ "  " + modelo.servicioFijo + " "
				+ modelo.metadatos.metaServicioNombre);
				
*/	
		
		if (!nhc.equals(Inicio.SEPARADOR) && !nhc.equals(Inicio.SEPARADOR_FUSIONAR)) {

			if (colum == 1 && !modelo.metadatos.metaServicioNombre.equals("@")) {

				if (cadenaOCR.contains(modelo.metadatos.metaServicioNombre)) {
					
					if(getLocalizado(modelo)){
						nombreNormalizado = modelo.nombreNormalizado;
						servicio = modelo.servicios.get(0);
						// System.out.println("Caso 0: " +modelo.fisica.tamañoPagina
						// + " = " + fisica.tamañoPagina);
						System.out.println("Se encontró por esta palabra: "
								+ modelo.metadatos.metaServicioNombre);
						System.out.println("El número de imagen es... "
								+ modelo.numImagen);
						
						String letra = "A";
						
						
						System.out.println(modelo.numImagen);
						System.out.println(modelo.modeloEspecial);
						
						
						if(modelo.modeloEspecial){
							letra = letra.toLowerCase();
						}
						
						setNumeroModelo(modelo.numImagen, letra);

						return true;
					}

				}

			} else if (colum == 2 && !modelo.metadatos.metaNombre.equals("@")) {
				if (cadenaOCR.contains(modelo.metadatos.metaNombre)) {
					
					if(getLocalizado(modelo)){
						nombreNormalizado = modelo.nombreNormalizado;
						// System.out.println("Caso 1: " +modelo.fisica.tamañoPagina
						// + " = " + fisica.tamañoPagina);
						System.out.println("Se encontró por esta palabra: "
								+ modelo.metadatos.metaNombre);
						
						String letra = "B";
						if(modelo.modeloEspecial){
							letra = letra.toLowerCase();
						}
						
						setNumeroModelo(modelo.numImagen, letra);

						return true;
					}

				}

			} else if (colum == 3 && !modelo.metadatos.metaModelo.equals("@")) {
				if (cadenaOCR.contains(modelo.metadatos.metaModelo)) {
					
					if(getLocalizado(modelo)){
						nombreNormalizado = modelo.nombreNormalizado;
						setNumeroModelo(modelo.numImagen, "C");
						// System.out.println("Caso 2: " +modelo.fisica.tamañoPagina
						// + " = " + fisica.tamañoPagina);
						System.out.println("Se encontró por esta palabra: "
								+ modelo.metadatos.metaModelo);

						String letra = "C";
						if(modelo.modeloEspecial){
							letra = letra.toLowerCase();
						}
						
						setNumeroModelo(modelo.numImagen, letra);
						
						return true;
					}
				}

			}

		}

		return false;
	}
	
	
	boolean getLocalizado(Modelo modelo){
		
		boolean localizado = false;
		
		if(!modelo.centroExterno.equals("")){
			
		//	JOptionPane.showMessageDialog(null, "Posible documento centro externo");
			
			if(modelo.claveCentroExterno1.length() > 0 && cadenaOCR.contains(modelo.claveCentroExterno1)){
		//		JOptionPane.showMessageDialog(null, "Clave externa 1");
				localizado = true;
			}
			else if(modelo.claveCentroExterno2.length() > 0 && cadenaOCR.contains(modelo.claveCentroExterno2)){
		//		JOptionPane.showMessageDialog(null, "Clave externa 2");
				localizado = true;
			}
			else if(modelo.claveCentroExterno3.length() > 0 && cadenaOCR.contains(modelo.claveCentroExterno3)){
		//		JOptionPane.showMessageDialog(null, "Clave externa 3");
				localizado = true;
			}
		}
		else{
			localizado = true;
		}
		
		return localizado;
	}
	
/*	
	boolean detector(Modelo modelo){   // column = 1, servynombre; column = 2, nombre; column = 3, modelo
		
		System.out.println(modelo.numImagen + "  " + modelo.nombreNormalizado + "  " + modelo.servicioFijo + " " + modelo.metadatos.metaServicioNombre);
		
		if(!nhc.equals("Separador")){
			// detectaEcos();
			
				if(!modelo.metadatos.metaServicioNombre.equals("@")){
					//System.out.println(cadenaOCR);
					
					if(cadenaOCR.contains(modelo.metadatos.metaServicioNombre)){
						nombreNormalizado = modelo.nombreNormalizado;
						servicio = modelo.servicios.get(0);
						// System.out.println("Caso 0: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
						System.out.println("Se encontró por esta palabra: " + modelo.metadatos.metaServicioNombre);
						System.out.println("El número de imagen es... " + modelo.numImagen);
						setNumeroModelo(modelo.numImagen,"A");
						
						return true;
					}
					else{
						//		Bloque copiado ****************************************************************
						boolean marca = false; 
						if(!modelo.metadatos.metaNombre.equals("@")){
							if(cadenaOCR.contains(modelo.metadatos.metaNombre)){
								nombreNormalizado = modelo.nombreNormalizado;
								//System.out.println("Caso 1: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
								System.out.println("Se encontró por esta palabra: " + modelo.metadatos.metaNombre);
								setNumeroModelo(modelo.numImagen, "B");
								marca = true;
							}
							else if(!modelo.metadatos.metaModelo.equals("@")){
								if(cadenaOCR.contains(modelo.metadatos.metaModelo)){
									nombreNormalizado = modelo.nombreNormalizado;
									setNumeroModelo(modelo.numImagen, "C");
									marca = true;
									// System.out.println("Caso 2: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
									System.out.println("Se encontró por esta palabra: " + modelo.metadatos.metaModelo);
								}
							}
						}
						if(!modelo.metadatos.metaServicio.equals("@") && marca){
							if(cadenaOCR.contains(modelo.metadatos.metaServicio)){
								this.servicio = modelo.servicios.get(0);
								// System.out.println("Caso 3: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
								System.out.println("El servicio se encontró por esta palabra: " + modelo.metadatos.metaServicio);
							}
						}
						if(marca == true)
							return true;
						// *************************************************************************************
						
					}
				}
				else
					{
					boolean marca = false; 
					if(!modelo.metadatos.metaNombre.equals("@")){
						if(cadenaOCR.contains(modelo.metadatos.metaNombre)){
							nombreNormalizado = modelo.nombreNormalizado;
							// System.out.println("Caso 1: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
							System.out.println("Se encontró por esta palabra: " + modelo.metadatos.metaNombre);
							setNumeroModelo(modelo.numImagen, "B");
							marca = true;
						}
						else if(!modelo.metadatos.metaModelo.equals("@")){
							if(cadenaOCR.contains(modelo.metadatos.metaModelo)){
								nombreNormalizado = modelo.nombreNormalizado;
								setNumeroModelo(modelo.numImagen, "C");
								marca = true;
								//System.out.println("Caso 2: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
								System.out.println("Se encontró por esta palabra: " + modelo.metadatos.metaModelo);

							}
						}
					}
					if(!modelo.metadatos.metaServicio.equals("@") && marca){
						if(cadenaOCR.contains(modelo.metadatos.metaServicio)){
							this.servicio = modelo.servicios.get(0);
							// System.out.println("Caso 3: " +modelo.fisica.tamañoPagina + " = " + fisica.tamañoPagina);
							System.out.println("El servicio se encontró por esta palabra: " + modelo.metadatos.metaServicio);

						}
					}
					if(marca == true)
						return true;
				}
			}
		
		return false;
	}
	
*/	
	
	boolean detectorRevisado(){
		return false;
	}
	
	public void getServicio(){}
	public void getNombreNormalizado(){}
	public void getMetadatos(){};
	
	
	String registraFichero(){
		
		String fechaSinBarras = devuelveCadenaFechaServicio();
		System.out.println("Fecha sin barras: " + fechaSinBarras);
		
		int aux = this.rutaArchivo.indexOf("@");
		String auxS = this.rutaArchivo.substring(0,aux + 1);
		String nuevaS = this.nhc + " @" + fechaSinBarras + " @" + this.nombreNormalizado + " r.pdf";
		nuevaS = auxS + nuevaS;
		System.out.println(nuevaS);
		
		this.rutaArchivo = nuevaS;
		
		return nuevaS;
	}
	
	private String devuelveCadenaFechaServicio(){
		
		String cadena = this.servicio;
		
		if(!this.fecha.equals("")){
			
			System.out.println(this.fecha);
			
			String fechaSinEspacios = this.fecha.replaceAll(" ", "");
			
			String fechaSinBarras[] = fechaSinEspacios.split("/");
			if(fechaSinBarras.length == 3)
				cadena += " " + fechaSinBarras[0] + fechaSinBarras[1] + fechaSinBarras[2];
			
			System.out.println(cadena);
		}
		
		return cadena;
	}
	
	String apartaFichero(){
		
		String rutaApartar = "";
		
		int aux = this.rutaArchivo.lastIndexOf("\\");
		rutaApartar = rutaArchivo.substring(0,aux);
		aux = rutaApartar.lastIndexOf("\\") + 1;
		rutaApartar = rutaArchivo.substring(0,aux);
		
		return rutaApartar;
	}
	
	
	boolean renombraFichero(Documento documentoInicial){
		
		/***************  También rota los ekgs   ************************/
		
		String rutaOriginal = "";
					
		boolean indicadorNumeroCarpeta = false;
		boolean nombreCarpetaAuto = false;
		
		String carpetaRenombradaAuto ="";
		String numeroCarpeta = "";

		int indexCarpeta = Inicio.rutaDirectorio.lastIndexOf("\\");
		
		String carpeta = Inicio.rutaDirectorio.substring(indexCarpeta);
		System.out.println(carpeta);
		
		if(carpeta.contains("#") ){
			indicadorNumeroCarpeta = true;
			int posicionAlm = carpeta.indexOf("#");
			int z = 1;
			while(carpeta.charAt(posicionAlm + z) != ' '){
				numeroCarpeta += carpeta.charAt(posicionAlm + z);
				z++;
			}
			
			System.out.println("El numero de carpeta es... " + numeroCarpeta);
			
			if(posicionAlm == 1){
				nombreCarpetaAuto = true;
				
				
				int indexNombrePdf = documentoInicial.rutaArchivo.lastIndexOf("\\");
				String nuevoNombrePdf = documentoInicial.rutaArchivo.substring(indexNombrePdf);
				
				int aux_1 = nuevoNombrePdf.lastIndexOf("_");
				if(aux_1 != -1){
					String hora = nuevoNombrePdf.substring(aux_1 + 1,aux_1 + 5);
					System.out.println("La hora es... " + hora);
					String fecha = nuevoNombrePdf.substring(aux_1 - 8, aux_1);
					System.out.println("La fecha es... " + fecha);
					
					carpetaRenombradaAuto = "\\" + fecha + " " + hora + " " + carpeta.substring(1,carpeta.length());
					System.out.println("Nombre carpeta renombrada auto... " + carpetaRenombradaAuto);
					carpeta = carpetaRenombradaAuto;
				}
				else{
					carpetaRenombradaAuto = "\\" + "OCR " + carpeta.substring(1,carpeta.length());
					System.out.println(carpetaRenombradaAuto);
					carpeta = carpetaRenombradaAuto;
				}
			}

		}
		
		String raiz = Inicio.rutaDirectorio.substring(0,indexCarpeta);
		System.out.println("Raiz... " + raiz);
		while(raiz.contains("01 Esc")){
			int indexRaiz = raiz.lastIndexOf("\\");
			raiz = raiz.substring(0,indexRaiz);
		}

		if(Inicio.destinoDocumentacion >= 2){
			raiz = Inicio.unidadHDD + ":\\DIGITALIZACIÓN\\02 SALNÉS";
		}
		
		
		System.out.println(raiz);
		raiz += "\\02 Revisado" + carpeta;
		System.out.println(raiz);
		
		
		File fichero = new File(raiz);
		if(fichero.mkdirs()){
			System.out.println("Directorio creado");
			Inicio.carpetaActualRevisando = raiz;
		}
			
		System.out.println("Ruta Archivo... " + rutaArchivo);
		int indexNombrePdf = rutaArchivo.lastIndexOf("\\");
		String nuevoNombrePdf = rutaArchivo.substring(indexNombrePdf);
		rutaOriginal = rutaArchivo;
		raiz += nuevoNombrePdf;
		System.out.println(raiz);
		
		int indice= raiz.lastIndexOf(".pdf");
		String nuevaRuta = raiz.substring(0, indice);
		String añadido = "";
		
		if(numeroModelo.length()> 0){
			añadido = " " + numeroModelo;
		}
		
		if(indicadorNumeroCarpeta){
			añadido += (" $" + numeroCarpeta);
		}
		
		añadido += (" @" + nhc + " @" + servicio + " @" + this.nombreNormalizado +" r.pdf");
		
		nuevaRuta = nuevaRuta + añadido;
		
		
		System.out.println("***************************");
		System.out.println("NuevaRutaRevisados... " + nuevaRuta);
		
		
		// Hace una copia de los documentos no reconocidos
		/*
		if(this.nombreNormalizado.toLowerCase().equals("x") && !nhc.equals("Separador")){
			String rutaNoReconocidos = Inicio.RUTA_NO_RECONOCIDOS + "/Nombre/" + carpeta.substring(1);
			File direct = new File(rutaNoReconocidos);
			direct.mkdirs();
			rutaNoReconocidos += ("/" + nuevoNombrePdf.substring(1,nuevoNombrePdf.indexOf(".pdf")) + añadido);
			
			System.out.println("Doc. No reconocido...");
			System.out.println("RutaOriginal... " + "\t" + rutaOriginal);
			System.out.println("RutaNoRecono... " + "\t" + rutaNoReconocidos);
			
			CopiarFichero.copiar(rutaOriginal, rutaNoReconocidos);
		}
		*/
		
		System.out.println(nuevaRuta);
		
		
		try {
			
			PdfReader pdf = new PdfReader(rutaArchivo);
			
			/****************   Mira si es un ekg y está en formato vertical, lo rota    ***********************/
			
			System.out.println("Nhc: " + nhc);
			System.out.println("Servicio: " + servicio);
			System.out.println("Nombre: " + nombreNormalizado);
			
			/*
			if(		(servicio.equals("CAR")&& nombreNormalizado.equals("X")) ||
					(servicio.equals("ANR")&& nombreNormalizado.equals("X")) || 
					 nombreNormalizado.equals("EKG")){
			*/
			
			if(Inicio.destinoDocumentacion == 3 && nombreNormalizado.toLowerCase().equals("x") && fisica.tamañoPagina == 0){
				nombreNormalizado = Inicio.EKG;
			}
			
			
			if(		(servicio.equals(Inicio.CARC)|| servicio.equals(Inicio.ANRC)) && nombreNormalizado.equals(Inicio.EKG)
					|| (Inicio.destinoDocumentacion == 2 && servicio.equals(Inicio.CARC) && nombreNormalizado.toLowerCase().equals("x"))
					|| (Inicio.destinoDocumentacion == 3 && nombreNormalizado.equals(Inicio.EKG))){
				
						// System.out.println("Es un ekg...");
						boolean girado = false;
						
						/*
						for(int z=1;z <= pdf.getNumberOfPages();z++){
							Rectangle formatoPagina = pdf.getPageSize(z);
							int alto = (int)formatoPagina.getHeight();
							int ancho = (int) formatoPagina.getWidth();
							//	Hoja vertical
							if(alto >= ancho){
								pdf.getPageN(z).put(PdfName.ROTATE, new PdfNumber(90));
								girado = true;
							}
						}
						*/
						
						int n = pdf.getNumberOfPages();
						
						PdfDictionary pageDict;
						for(int i=1;i<=n;i++){
							
							Rectangle formatoPagina = pdf.getPageSize(i);
							int alto = (int)formatoPagina.getHeight();
							int ancho = (int) formatoPagina.getWidth();
							
							pageDict = pdf.getPageN(i);
							PdfNumber orientacion = PdfPage.PORTRAIT;
							pageDict.put(PdfName.ROTATE, orientacion);
							
							pageDict = pdf.getPageN(i);
							
											
							if(alto > ancho){
								pageDict.put(PdfName.ROTATE, new PdfNumber(-90));
								girado = true;
							}
							if(Inicio.destinoDocumentacion == 2){
								girado = true;
							}
							
							/*
							if(rotar){
								pageDict = pdf.getPageN(i);
								pageDict.put(PdfName.ROTATE, new PdfNumber(rot));
							}
							*/
						}
						
						if(girado)
							nombreNormalizado = Inicio.EKG;
				
			}
			
			PdfStamper stp = new PdfStamper(pdf, new FileOutputStream(nuevaRuta));
			PdfWriter writer = stp.getWriter();
			PdfAction pdfAcc;
			
			/*
			if(this.fisica.tamañoPagina == 1){
				pdfAcc = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.XYZ,fisica.dimensiones.ancho*2/3,-1, 1), writer);
				writer.setOpenAction(pdfAcc);
			}
			*/
		
					/*
					else{
						//pdfAcc = PdfAction.gotoLocalPage(1, new PdfDestination(PdfDestination.FIT,10000), writer);
						pdfAcc = PdfAction.gotoLocalPage(1,new PdfDestination(PdfDestination.FITV), writer);
		
					}
					*/
			
			
			stp.close();
			
			pdf.close();
			
			rutaOriginal = rutaArchivo;
			rutaArchivo = nuevaRuta;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
/*
		File ficheroBorrar = new File(rutaOriginal);
		if(ficheroBorrar.delete()){
			System.out.println("Fichero Borrado");
		}
		else{
			JOptionPane.showMessageDialog(null, "Fichero no borrado. Debe de estar en uso.");
			return false;
		}
*/
		return true;
		
		/*
		File fichero = new File(ruta);
//		System.out.println(ruta);
		int aux = ruta.lastIndexOf(".");
		ruta = ruta.substring(0,aux);
		File ficheroN = new File(ruta + " @" + orientacion + 
				numPaginas + dimensiones + " " + alto + "-" + ancho + ".pdf");
//		System.out.println(ficheroN.getAbsolutePath());
		boolean renombrado = fichero.renameTo(ficheroN);
		if(renombrado){
			System.out.println("Renombrado");
		}else{
			System.out.println("Error");
		}
		*/
	}
	
	void detectaEKGs(){
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
			if(this.fisica.tamañoPagina == 0 && this.fisica.vertical == 2){
				this.nombreNormalizado = Inicio.EKG;
				}
		}
	}
	
	void detectaEKGsA5(){
		
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
		
			int altoReal = (this.fisica.dimensiones.alto < this.fisica.dimensiones.ancho) ? this.fisica.dimensiones.alto : this.fisica.dimensiones.ancho;
			int anchoReal = (this.fisica.dimensiones.alto > this.fisica.dimensiones.ancho) ? this.fisica.dimensiones.alto : this.fisica.dimensiones.ancho;
			
			if( (altoReal > Inicio.ECG_A5_ALTO_MIN && altoReal < Inicio.ECG_A5_ALTO_MAX) && (anchoReal > Inicio.ECG_A5_ANCHO_MIN_A && anchoReal < Inicio.ECG_A5_ANCHO_MAX_A)){
				this.nombreNormalizado = Inicio.EKG;
			}
			else if((altoReal > Inicio.ECG_A5_ALTO_MIN && altoReal < Inicio.ECG_A5_ALTO_MAX) && (anchoReal > Inicio.ECG_A5_ANCHO_MIN_B && anchoReal < Inicio.ECG_A5_ANCHO_MAX_B)){
				this.nombreNormalizado = Inicio.EKG;
			}
		}
	}

	void detectaEcos(){
		
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
			if((this.fisica.dimensiones.alto <= Inicio.ECO_ALTO_MAX && this.fisica.dimensiones.alto >= Inicio.ECO_ALTO_MIN) || 
					this.fisica.dimensiones.ancho <= Inicio.ECO_ALTO_MAX && this.fisica.dimensiones.ancho >= Inicio.ECO_ALTO_MIN){
				if(( this.servicio.equals(Inicio.CARC) || this.servicio.equals(Inicio.PEDC)) 
						&& (fisica.dimensiones.ancho < Inicio.ECO_ANCHO_MAX && fisica.dimensiones.alto < Inicio.ECO_ANCHO_MAX)){
					this.nombreNormalizado = Inicio.ECOCARDIOGRAFIA;
				}
				else{
					this.nombreNormalizado = Inicio.ECO;
				}
				
			}
		}
	}
	
	void detectaMonitor(){
		
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
			if((this.fisica.dimensiones.alto <= 426 && this.fisica.dimensiones.alto >= 420) || 
					this.fisica.dimensiones.ancho <= 426 && this.fisica.dimensiones.ancho >= 420){
				this.nombreNormalizado = Inicio.MONITORIZACION;
			}
		}
	}
	
	void detectaDocRosa(){
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
			if((this.fisica.dimensiones.alto <= 455  && this.fisica.dimensiones.alto >= 451) || 
					this.fisica.dimensiones.ancho <= 455 && this.fisica.dimensiones.ancho >= 451){
				if(this.cadenaOCR.contains("Tratamiento") || this.cadenaOCR.contains("Diagnóstico")){
					this.nombreNormalizado = Inicio.DOC;
				}
				
			}
		}
	}
	
	void detectaUsmi(){
		if(this.nombreNormalizado.equals("X") && !this.nhc.equals(Inicio.SEPARADOR)
				&& !this.nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
			if(servicio.equals(Inicio.USMI) && this.fisica.tamañoPagina == 0 && this.fisica.vertical == 2){
				this.nombreNormalizado = Inicio.TEST_PSICOLOXICO;
				}
			else if(servicio.equals(Inicio.USMI) && cadenaOCR.length() <150){
				this.nombreNormalizado = Inicio.TEST_PSICOLOXICO;
			}
		}
	}
	
	
	public void detectaPaginasBlanco()     /*(String pdfSourceFile, String pdfDestinationFile)  */
    {
		
		String pdfDestinationFile = "hola.pdf";
		
        try
        {
            // step 1: create new reader
            PdfReader r = new PdfReader(rutaArchivo);
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(rutaArchivo);
            Document document = new Document(r.getPageSizeWithRotation(1));
            // step 2: create a writer that listens to the document
            PdfCopy writer = new PdfCopy(document, new FileOutputStream(pdfDestinationFile));
            // step 3: we open the document
            document.open();
            // step 4: we add content
            PdfImportedPage page = null;


            //loop through each page and if the bs is larger than 20 than we know it is not blank.
            //if it is less than 20 than we don't include that blank page.
            for (int i=1;i<=r.getNumberOfPages();i++)
            {
                //get the page content
                byte bContent [] = r.getPageContent(i,raf);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                //write the content to an output stream
                bs.write(bContent);
                int tam = bs.size();
                System.out.println("Tamaño de la página "+i+" = "+bs.size());
                //add the page to the new pdf
                
                if(tam <= TAMAÑO_PAG_BLANCO)
                			System.out.println("**************  Posible página en blanco ************");
                
                /*
                if (bs.size() > TAMAÑO_PAG_BLANCO)
                {
                    page = writer.getImportedPage(r, i);
                    writer.addPage(page);
                }
				*/
                bs.close();
            }
            
            JOptionPane.showMessageDialog(null, "Fin paciente");
            
            //close everything
            document.close();
            writer.close();
            raf.close();
            r.close();
        }
        catch(Exception e)
        {
        //do what you need here
        }
    }
	
	
}




class Servicio{
	
	String nombreServicio = "";
	ArrayList<String> nombresNormalizados = new ArrayList<String>();
	Estadistica estadisticaServicio = new Estadistica();
	
}

class NombreNormalizado{
	String nombreNormalizado = "";
	ArrayList<String> servicios = new ArrayList<String>();
	ArrayList<String> modelos = new ArrayList<String>();
}

class Fisica{
	
	Dimensiones dimensiones = new Dimensiones();
	int vertical = 1;			//	1 vertical; 2 horizontal; 0 variable
	int tamañoPagina = 0;		//	0 A4; 1 A3; -1 A5; 3 A4 o A5
	int numPaginas = 1;
	int peso = 0;
}

class Dimensiones{
	int alto = 0;
	int ancho = 0;
}

class Estadistica{
	
}

class ParClaveNumero{
	String clave = "";
	String numero = "";
	int indiceFin;
	
	public ParClaveNumero(String clave, String numero, int indiceFin) {
		this.clave = clave;
		this.numero = numero;
		this.indiceFin = indiceFin;
	}
	
}
