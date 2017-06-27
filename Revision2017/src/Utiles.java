import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;


public class Utiles {
	
	
	public void encajarNombreNormalizado(String cadena){
		// String cadena = Inicio.jBNombreDocp.getText();

		if(cadena.length() > 20){
			Inicio.jBNombreDocp.setFont(new java.awt.Font("Serif", 1, 24));
			Inicio.jBNombreDoc.setFont(new java.awt.Font("Serif", 1, 24));
			// JOptionPane.showMessageDialog(null, "Mayor de 20");
		}
		else{
			Inicio.jBNombreDocp.setFont(new java.awt.Font("Serif", 1, 36));
			Inicio.jBNombreDoc.setFont(new java.awt.Font("Serif", 1, 36));
			// JOptionPane.showMessageDialog(null, "Menor de 20");
		}
	}


	public void ventanaRenombrarServicios() {
		// TODO Auto-generated method stub
		
		Object seleccion = JOptionPane.showInputDialog(
				Inicio.jBServicio, "¿Fijar el servicio de todos los pdfs hasta el próximo separador?",
				"Fijar servicio en bloque",
				JOptionPane.OK_OPTION,
				null, // icono, // unIcono, // null para icono defecto
				new Object[] { "SI", "NO" },
				"NO"
				);
		if(seleccion.equals("SI")){
			renombraServicios();	
			Inicio.jLServicios.setSelectedValue(Inicio.jBServicio.getText(), true);
			Inicio.jLNombresDoc.setModel(Inicio.excel.getDocServicio(Inicio.jBServicio.getText()));
		}
	}

	public void renombraServicios(){
		for(int i=0;i < Inicio.separadores.size()-1;i++){
			if(Inicio.numeroPdf >= Inicio.separadores.get(i) && Inicio.numeroPdf < Inicio.separadores.get(i+1)){
				
					
				for(int j= Inicio.numeroPdf + 1; j < Inicio.separadores.get(i+1); j++){
					if( Inicio.listaDocumentos[j].servicio != "X" && Inicio.listaDocumentos[j].servicio != Inicio.jBServicio.getText()){
						Inicio.listaDocumentos[j].semaforoAmarilloServicio = true;
					}

						//	Comprobamos si el servicio es anestesia para hacer el cambio anrc - carc
						if(Inicio.jBServicio.getText().equals(Inicio.ANRC)){
							if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
								Inicio.listaDocumentos[j].servicio = Inicio.CARC;
							}
							else{
								Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
							}
						}
						
						else if(Inicio.jBServicio.getText().equals(Inicio.ORLC)){
							if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
					//			JOptionPane.showMessageDialog(null, "Renombrado de ekg a video...");
								Inicio.listaDocumentos[j].nombreNormalizado = Inicio.VIDEONISTAGMOGRAFÍA;
							}
							Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
						}

						
						else if(Inicio.jBServicio.getText().equals(Inicio.DERC) || Inicio.jBServicio.getText().equals(Inicio.ETMC) ){
							if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.ENFERMERIA_QUIRURGICA)){
								Inicio.listaDocumentos[j].servicio = Inicio.CIA;
							}
							Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
						}
						else if(Inicio.jBServicio.getText().equals(Inicio.CARC) || Inicio.jBServicio.getText().equals(Inicio.PEDC)){
							if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.ECO)){
								Inicio.listaDocumentos[j].nombreNormalizado = Inicio.ECOCARDIOGRAFIA;
							}
							Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
						}
						else if(Inicio.jBServicio.getText().equals(Inicio.USMI)){
							if(Inicio.listaDocumentos[j].nombreNormalizado.equals("X")){
								switch (Utiles.esUsmi(Inicio.listaDocumentos[j].cadenaOCR, Inicio.paresUsmi)) {
								case 2:
									Inicio.listaDocumentos[j].nombreNormalizado = Inicio.INFORME_PSICOPEDAGOXICO;
									break;
								case 1:
									Inicio.listaDocumentos[j].nombreNormalizado = Inicio.INFORME_PSICOPEDAGOXICO;
									Inicio.listaDocumentos[j].semaforoAmarilloNombre = true;
									break;
								default:
									break;
								}
							}
							Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
						}
						else{
							Inicio.listaDocumentos[j].servicio = Inicio.jBServicio.getText();
						}
						
						
						// Inicio.listaDocumentos[j].revisado = true;
						System.out.println("Modificando pdf numero " + j + " a " + Inicio.listaDocumentos[j].servicio);
						
					}
			}
		}
	}
	
	public void ventanaRenombrarNombres() {
		// TODO Auto-generated method stub
		Object seleccion = JOptionPane.showInputDialog(
				Inicio.jBServicio, "¿Cambiar el nombre de todos los pdfs hasta el próximo separador?",
				"Fijar nombre en bloque",
				JOptionPane.OK_OPTION,
				null, // icono, // unIcono, // null para icono defecto
				new Object[] { "SI", "NO" },
				"NO"
				);
		if(seleccion.equals("SI")){
			renombraNombres();
		}
	}
	
	public void renombraNombres(){
		for(int i=0;i < Inicio.separadores.size()-1;i++){
			if(Inicio.numeroPdf >= Inicio.separadores.get(i) && Inicio.numeroPdf < Inicio.separadores.get(i+1)){
				for(int j= Inicio.numeroPdf + 1; j < Inicio.separadores.get(i+1); j++){
					if( Inicio.listaDocumentos[j].nombreNormalizado != "X" && Inicio.listaDocumentos[j].nombreNormalizado != Inicio.jBNombreDoc.getText()){
						Inicio.listaDocumentos[j].semaforoAmarilloNombre = true;
					}
					Inicio.listaDocumentos[j].nombreNormalizado = Inicio.jBNombreDoc.getText();
					// Inicio.listaDocumentos[j].revisado = true;
					System.out.println("Modificando pdf numero " + j + " a " + Inicio.listaDocumentos[j].nombreNormalizado);
					
				}
			}
		}
	}

	public void actualizaServicio(){
    	Inicio.jBServicio.setText(Inicio.jLServicios.getSelectedValue().toString());
		Inicio.jBServiciop.setText(Inicio.jLServicios.getSelectedValue().toString());
		/*
		if(Inicio.menuVertical){
			Inicio.ventanaMicro.jBServiciom.setText(Inicio.jLServicios.getSelectedValue().toString());
			Inicio.ventanaMicro.jBServiciom.setBackground(new java.awt.Color(153, 255, 153));
		}
		*/
	
		if(Inicio.A3 && (Inicio.jBServiciop.getText().equals(Inicio.HOSP) || Inicio.jBServiciop.getText().equals(Inicio.CIA))){
			DefaultListModel modelo = new DefaultListModel();
			modelo.addElement(Inicio.CUIDADOS_INTENSIVOS);
			modelo.addElement(Inicio.URPA);
			
			Inicio.jLNombresDoc.setModel(modelo);
		}
		else{
			Inicio.jLNombresDoc.setModel(Inicio.excel.getDocServicio(Inicio.jBServicio.getText()));
		}
		
		Inicio.jBServicio.setBackground(new java.awt.Color(153, 255, 153));
		Inicio.jBServiciop.setBackground(new java.awt.Color(153, 255, 153));
		Inicio.jBServicio.setIcon(null);
		Inicio.jBServiciop.setIcon(null);
		Inicio.jCheckBox1.setSelected(false);
		
				
		if(Inicio.jBNHC.getText().equals(Inicio.SEPARADOR)){
			Inicio.jBNHC.setBackground(new java.awt.Color(153, 255, 153));
			Inicio.jBNHCp.setBackground(new java.awt.Color(153, 255, 153));
			/*
			if(Inicio.menuVertical){
				Inicio.ventanaMicro.jBNHCm.setBackground(new java.awt.Color(153, 255, 153));
			}
			*/
			
			Inicio.utiles.renombraServicios();
		}
		
		
		new FocalAdobe(100);
	}

	public void jCheckBox1ActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		if (Inicio.jCheckBox1.isSelected()) {
			Inicio.jLNombresDoc.setModel(Inicio.excel.listaDocumentosDLM);
		} else if (!Inicio.jCheckBox1.isSelected()) {
			Inicio.jLNombresDoc.setModel(Inicio.excel.getDocServicio(Inicio.jLServicios
					.getSelectedValue().toString()));
		}
		
	}
	
	public void registraCambiosFinales() {
		// TODO Auto-generated method stub
		
		boolean ajustarCIdeRADM = false;
		boolean errorAlRenombrar = false;
		
		ArrayList<String> listaArchivos = new ArrayList<String>();
		
		
		if(Inicio.ventanaComprobacion != null){
			Inicio.ventanaComprobacion.dispose();
		}
		
    	new Acrobat().guardarPagina();
    	revisarPropiedadesDocumento();
    	
    	Inicio.ventanaComprobacion = new VentanaComprobar();
    	if(Inicio.erroresAntesRegistrar){
    		int opcion = JOptionPane.showOptionDialog(null, "Hay errores en la revisión. ¿Quieres continuar registrando o quieres corregir los errores?", "Registrando", 
    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, new Object[] {"Continuar","Corregir"}, "Corregir");
    		if(opcion == 0){
    			Inicio.erroresAntesRegistrar = false;
    			Inicio.ventanaComprobacion.dispose();
    		}
    		else{
    			Inicio.erroresAntesRegistrar = true;
    		}
    	}
 
    	
    	if(!Inicio.erroresAntesRegistrar){
	    	new CerrarTodo().closePdf();
	    	System.out.println("Cerrados los pdfs");
 
	    	//ajustarCIdeRADM = esCIdeRx();
	    	ajustarCIdeRADM = true;
	    	
	    	try {
				for(int i=0;i<Inicio.listaDocumentos.length;i++){
					
						File nombreAntiguo = new File(Inicio.listaDocumentos[i].rutaArchivo);
						
						if(nombreAntiguo.exists()){
							if(Inicio.listaDocumentos[i].nhc.equals("Eliminar")){
								if(nombreAntiguo.delete()){
									System.out.println("Fichero eliminado");
								}else{
									System.out.println("Fichero imposible de eliminar");
								}
							}
							else if(Inicio.listaDocumentos[i].nhc.equals("Apartar")){
								
								int aux = Inicio.listaDocumentos[i].rutaArchivo.lastIndexOf("\\");
								String nombrepdf = Inicio.listaDocumentos[i].rutaArchivo.substring(aux);
								
								String rutaNueva = Inicio.listaDocumentos[i].apartaFichero() + "Apartado por " + Inicio.usuario + ".";
								
								File carpetaNueva = new File(rutaNueva);
								carpetaNueva.mkdirs();
								
								rutaNueva = rutaNueva + nombrepdf;
								System.out.println("Ruta apartado... " + rutaNueva);
								
								File nombreNuevo = new File(rutaNueva);
								boolean correcto = nombreAntiguo.renameTo(nombreNuevo);
								if(correcto){
									System.out.println("Renombrado con exito");
								}
								else{
									System.out.println("Error al renombrar");
									errorAlRenombrar = true;
								}
								
							}
							else if(Inicio.listaDocumentos[i].nhc.equals("Escanear")){
								
								int aux = Inicio.listaDocumentos[i].rutaArchivo.lastIndexOf("\\");
								String nombrepdf = Inicio.listaDocumentos[i].rutaArchivo.substring(aux);
								
								String rutaNueva = Inicio.listaDocumentos[i].apartaFichero() + "Reescanear. " + Inicio.usuario + ".";
								
								File carpetaNueva = new File(rutaNueva);
								carpetaNueva.mkdirs();
								
								rutaNueva = rutaNueva + nombrepdf;
								System.out.println("Ruta reescanear... " + rutaNueva);
								
								File nombreNuevo = new File(rutaNueva);
								
								
								boolean correcto = nombreAntiguo.renameTo(nombreNuevo);
								if(correcto){
									System.out.println("Renombrado con exito");
								}
								else{
									System.out.println("Error al renombrar");
									errorAlRenombrar = true;
								}
								
							}						
							else{
									    		
								//	Si el nombre fue modificado se guarda una copia
								if(Inicio.listaDocumentos[i].modificado && Inicio.listaDocumentos[i].fisica.tamañoPagina != 1 ){
									
									File carpeta = new File(Inicio.listaDocumentos[i].rutaArchivo);
									carpeta = carpeta.getParentFile();
									
									String nombreCarpeta = carpeta.getName();
									
									if(!nombreCarpeta.toLowerCase().contains("scanner") 
											|| !nombreCarpeta.toLowerCase().contains("scaner")
											|| !nombreCarpeta.toLowerCase().contains("a3") ){
										
										System.out.println();
										String rutaMalReconocidos = Inicio.RUTA_NO_RECONOCIDOS + "/Equivocados/" + nombreCarpeta ;
										File direct = new File(rutaMalReconocidos);
										direct.mkdirs();
										rutaMalReconocidos += ("/" + nombreAntiguo.getName());
										
										System.out.println("Doc. No reconocido...");
										System.out.println("RutaOriginal... " + "\t" + Inicio.listaDocumentos[i].rutaArchivo);
										System.out.println("RutaNoRecono... " + "\t" + rutaMalReconocidos);
										
										CopiarFichero.copiar(Inicio.listaDocumentos[i].rutaArchivo, rutaMalReconocidos);
									}
									

								}
								
								if(ajustarCIdeRADM){
									
									if(Inicio.listaDocumentos[i].servicio.equals("RADM") 
											&& Inicio.listaDocumentos[i].nombreNormalizado.equals(Inicio.CONSENTIMIENTO)){
										
										boolean ajustar = false;
										
										String numeroModelo =  Inicio.listaDocumentos[i].numeroModelo;
										
										if(numeroModelo != null && numeroModelo.length() > 0){
											
											char caracter = numeroModelo.charAt(numeroModelo.length()-1);
											
											System.out.println(caracter);
											
											switch (caracter) {
											case 'a':
											case 'b':
											case 'c':	ajustar = true;	break;
											}
											
											if(ajustar){
												borrarPaginaInicial(Inicio.listaDocumentos[i].rutaArchivo, Inicio.listaDocumentos[i].rutaArchivo + "f");
											}
										}

											
									}
								}
								
								
								String rutaNueva = Inicio.listaDocumentos[i].registraFichero();
								System.out.println("Nombre Nuevo final con fecha: ");
								System.out.println(rutaNueva);
								File nombreNuevo = new File(rutaNueva);
								
								listaArchivos.add(nombreNuevo.getName());
								
								boolean correcto = nombreAntiguo.renameTo(nombreNuevo);
								if(correcto){
									System.out.println("Renombrado con exito");
								}
								else{
									System.out.println("Error al renombrar");
									errorAlRenombrar = true;
								}
								
								

								
							}
						}

					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String textoError = sw.toString();
				Txt.escribirError(textoError);
			}finally{
				Txt.escribirListaFicheros(listaArchivos);
			}
	    	
	    	
	    	if(!errorAlRenombrar){
		    	Inicio.listaCarpetasRegistradas.add(Inicio.rutaCarpetaEscaneadaUsuario);
		    	
		    	Inicio.modelo.removeAllElements();
		    	Inicio.jBNHC.setText("");
		    	Inicio.jBNHCp.setText("");
		    	/*
				if(Inicio.menuVertical){
					Inicio.ventanaMicro.jBNHCm.setText("");
					Inicio.ventanaMicro.jBServiciom.setText("");
					Inicio.ventanaMicro.jBNombreDocm.setText("");
				}
				*/
		    	Inicio.jBServicio.setText("");
		    	Inicio.jBServiciop.setText("");
		    	Inicio.jBNombreDoc.setText("");
		    	Inicio.jBNombreDocp.setText("");
		    	Inicio.jBNombreDocp.setToolTipText(Inicio.jBNombreDocp.getText());
		    	Inicio.utiles.encajarNombreNormalizado(Inicio.jBNombreDoc.getText());
	    			
	    		}
	    	}


    	//	    	Eliminar la carpeta de asociados
		/*
		File borrarCarpeta = new File(Inicio.rutaCarpetaEscaneadaUsuario);
		
		File[] listaFicheros = borrarCarpeta.listFiles();
		for(int z=0;z<listaFicheros.length;z++){
			if(listaFicheros[z].delete())
				System.out.println("Fichero borrado en la carpeta escaneado");;
		}
		
		if(borrarCarpeta.delete()){
			System.out.println("El directorio ha sido borrado");
		}else{
			System.out.println("Directorio no borrado");
		}
		*/
 
  
    	
		if(Inicio.A3){
			
		  	JOptionPane.showMessageDialog(null, "A continuación se va a abrir el programa de formatear A3");
			
			System.out.println("Iniciando programa");
			File archivo3 = new File("FormatoA3.jar");
			try {
				Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo3);
	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}


	}

	
	public void revisarPropiedadesDocumento(){
	    	if(!Inicio.listaDocumentos[Inicio.numeroPdf].nhc.equals(Inicio.jBNHCp.getText())){
	    		System.out.println("Cambiamos el nhc");
	    		Inicio.listaDocumentos[Inicio.numeroPdf].nhc = Inicio.jBNHCp.getText();
	    		Inicio.listaDocumentos[Inicio.numeroPdf].revisado = true;
	    	}
	    	if(!Inicio.listaDocumentos[Inicio.numeroPdf].servicio.equals(Inicio.jBServiciop.getText())){
	    		System.out.println("Cambiamos el servicio");
	    		Inicio.listaDocumentos[Inicio.numeroPdf].servicio = Inicio.jBServiciop.getText();
	    		Inicio.listaDocumentos[Inicio.numeroPdf].revisado = true;
	    	}
	    	if(!Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado.equals(Inicio.jBNombreDocp.getText())){
	    		System.out.println("Cambiamos el nombre del documento");
	    		Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado = Inicio.jBNombreDocp.getText();
	    		Inicio.listaDocumentos[Inicio.numeroPdf].revisado = true;
	    	}
	    	Inicio.listaDocumentos[Inicio.numeroPdf].semaforoAmarilloServicio = false;
	    	Inicio.listaDocumentos[Inicio.numeroPdf].semaforoAmarilloNombre = false;

	    }

	
	public boolean esCIdeRx(){
		
		HashSet<String> servicios = new HashSet<String>();
		
		for(Documento documento : Inicio.listaDocumentos){
			servicios.add(documento.servicio);
		}
		
		System.out.println("esCIdeRx. Tamaño del conjunto: " + servicios.size());
		for(String servicio : servicios){
			System.out.println(servicio);
		}
		
		// if(servicios.size() == 1){
			if(servicios.contains("RADM")){
				return true;
			}
		// }
		
		return false;
	}
	
	
	/**
	 * Borra la página inical de un pdf. Ideado para los CI de Rx
	 * 
	 * @param rutaOrigen
	 * @param rutaDestino
	 */
	public static void borrarPaginaInicial(String rutaOrigen, String rutaDestino){
		
		PdfReader reader = null;
		Document documento = null;
		
		PdfCopy writer = null;
		PdfImportedPage pagina = null;
		
		boolean hecho = false;
		
		try {
			
			reader = new PdfReader(rutaOrigen);
			
			documento = new Document(reader.getPageSizeWithRotation(1));
			
			writer = new PdfCopy(documento, new FileOutputStream(rutaDestino));
			
			documento.open();
			
			for(int i=2;i<=reader.getNumberOfPages();i++){
				pagina = writer.getImportedPage(reader, i);
				writer.addPage(pagina);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(documento != null) documento.close();
			if(writer != null) writer.close();
			if(reader != null) reader.close();
			hecho = true;
		}
		

		if(hecho){
			File viejo = new File(rutaOrigen);
			
			if(viejo.delete()){
				System.out.println("Borrado original");
				
				File nuevo = new File(rutaDestino);
				nuevo.renameTo(viejo);
				System.out.println("Renombrado el nuevo");
			}

		}

	}
	
	
	public void jBApartarActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		
		Inicio.jBNHC.setText("Apartar");
		Inicio.jBNHCp.setText("Apartar");
		Inicio.jBServicio.setText("Apartar");
		Inicio.jBServiciop.setText("Apartar");
		Inicio.jBNombreDoc.setText("Apartar");
		Inicio.jBNombreDocp.setText("Apartar");
		Inicio.utiles.encajarNombreNormalizado(Inicio.jBNombreDoc.getText());
		Inicio.jBNombreDocp.setToolTipText(Inicio.jBNombreDocp.getText());
		
		/*
		if(Inicio.menuVertical){
			Inicio.ventanaMicro.jBNHCm.setText("Apartar");
			Inicio.ventanaMicro.jBServiciom.setText("Apartar");
			Inicio.ventanaMicro.jBNombreDocm.setText("Apartar");
			
			Inicio.ventanaMicro.jBNHCm.setBackground(Color.gray);
			Inicio.ventanaMicro.jBServiciom.setBackground(Color.gray);
			Inicio.ventanaMicro.jBNombreDocm.setBackground(Color.gray);
		}
		*/
		
		Inicio.jBNHC.setBackground(Color.gray);
		Inicio.jBNHCp.setBackground(Color.gray);
		Inicio.jBServicio.setBackground(Color.gray);
		Inicio.jBServiciop.setBackground(Color.gray);
		Inicio.jBNombreDoc.setBackground(Color.gray);
		Inicio.jBNombreDocp.setBackground(Color.gray);
	}
	
	
	public void jBReEscanearActionPerformed(ActionEvent evt){
		
		Inicio.jBNHC.setText("Escanear");
		Inicio.jBNHCp.setText("Escanear");
		Inicio.jBServicio.setText("Escanear");
		Inicio.jBServiciop.setText("Escanear");
		Inicio.jBNombreDoc.setText("Escanear");
		Inicio.jBNombreDocp.setText("Escanear");
		Inicio.utiles.encajarNombreNormalizado(Inicio.jBNombreDoc.getText());
		Inicio.jBNombreDocp.setToolTipText(Inicio.jBNombreDocp.getText());
		
		Inicio.jBNHC.setBackground(Color.gray);
		Inicio.jBNHCp.setBackground(Color.gray);
		Inicio.jBServicio.setBackground(Color.gray);
		Inicio.jBServiciop.setBackground(Color.gray);
		Inicio.jBNombreDoc.setBackground(Color.gray);
		Inicio.jBNombreDocp.setBackground(Color.gray);
		
		
		/*
		if(Inicio.)
		Object comentario = JOptionPane.showInputDialog(null,"Escribe un breve comentario porqué hay que volver a escanear el documento","Anotación",JOptionPane.QUESTION_MESSAGE);
		if(comentario.toString() != null){
				String ruta = Inicio.rutaFirmados + "\\Apartado por " + Inicio.usuario.alias + ". Pendiente";
				
				if(Inicio.usuario.tipoDocumentacion == 0){
					ruta = Inicio.rutaFirmadosUrgencias+ "\\01 " + Inicio.usuario.alias  + "\\03 Firmado\\Apartado por " + Inicio.usuario.alias + ". Pendiente";
				}
				else if(Inicio.usuario.tipoDocumentacion == 2){
					ruta = Inicio.rutaFirmadosSalnes + "\\Apartado por " + Inicio.usuario.alias + ". Pendiente";						}
				
				ruta += "\\" + comentario.toString() + "\\";
				File directorio = new File(ruta);
				boolean directorioCreado = directorio.mkdirs();
				if(directorioCreado || directorio.exists()){
					ruta += Inicio.documento[indexApartado].nombreArchivo;
					CopiarFichero.copiar(Inicio.documento[indexApartado].rutaArchivo,ruta);
					JOptionPane.showMessageDialog(null, "Pdf apartado");
				}
				else{
					JOptionPane.showMessageDialog(null, "Error al apartar el documento");;
				}

		}
		*/
	}
	

	public void jBCarpetaActionPerformed(ActionEvent evt) {
			// TODO Auto-generated method stub
	    	String cadena = "explorer.exe " + Inicio.carpetaActualRevisando;
			try {
				Runtime.getRuntime().exec(cadena);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void habilitarTeclas(String texto, int visualizacion) {
		if (texto.equals("Teclas On")) {
			String cmd = "taskkill.exe /F /IM FocoAcrobat2015.exe /T";
			String cmd2 = "taskkill.exe /F /IM FocoNHC.exe /T";
			String cmd3 = "taskkill.exe /F /IM FocoAcrobat2.exe /T";
			String cmd4 = "taskkill.exe /F /IM FocoAcrobatV.exe /T";
			String cmd5 = "taskkill.exe /F /IM FocoAcrobat2015v7.exe /T";
			String cmd6 = "taskkill.exe /F /IM FocoAcrobatV2.exe /T";
			String cmd7 = "taskkill.exe /F /IM FocoAcrobatV3.exe /T";
			
			
			Process hijo, hijo2, hijo3, hijo4,hijo5, hijo6, hijo7;
			try {
				hijo = Runtime.getRuntime().exec(cmd);

				hijo.waitFor();
				hijo2 = Runtime.getRuntime().exec(cmd2);

				hijo2.waitFor();
				
				hijo3 = Runtime.getRuntime().exec(cmd3);

				hijo3.waitFor();
				
				hijo4 = Runtime.getRuntime().exec(cmd4);
				hijo4.waitFor();
				
				hijo5 = Runtime.getRuntime().exec(cmd5);
				hijo5.waitFor();
				
				hijo6 = Runtime.getRuntime().exec(cmd6);
				hijo6.waitFor();
				
				hijo7 = Runtime.getRuntime().exec(cmd7);
				hijo7.waitFor();

				Thread.sleep(300);

			} catch (IOException e) {
				// System.out.println("Incapaz de matar.");
			} catch (InterruptedException e) {
				// System.out.println("Incapaz de matar.");
			}

			Inicio.jBDeshabilitar.setText("Teclas Off");
			Inicio.jBDeshabilitar.setBackground(Color.cyan);

		} else if (texto.equals("Teclas Off")) {

			File archivo2 = null;
			System.out.println("Visualizacion ... " + visualizacion);
			if(visualizacion == 0){
				archivo2 = new File(Inicio.rutaFocoAcrobat);
			}
			else if(visualizacion == 1 || visualizacion == 2){
				archivo2 = new File(Inicio.rutaFocoAcrobatV);
			}
			System.out.println("autohotkey..." + archivo2.getName());
			
			
			// File archivo3 = new File(Inicio.rutaFocoNHC);
			try {
				Process p = Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler " + archivo2);
			//	Process pNHC = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo3);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			Inicio.jBDeshabilitar.setText("Teclas On");
			Inicio.jBDeshabilitar.setBackground(Color.pink);

		}
	}
	
	public void detectaNHCsecuencial(){
		
		/*
		 System.out.println("Vamos en el pdf numero " + Inicio.numeroPdf);

		System.out.println("Empieza la busqueda secuencial de nhc");
		
		System.out.println(Inicio.listaDocumentos[Inicio.numeroPdf].nhc);
		*/
		
		if(Inicio.listaDocumentos[Inicio.numeroPdf].nhc.contains("ERROR") || 
				Inicio.listaDocumentos[Inicio.numeroPdf].nhc.contains("NO")){
			if(Inicio.numeroPdf >0 && Inicio.numeroPdf < (Inicio.tamañoCarpetaPdf - 2) ){
				/*
				System.out.println("Numero pdf " + Inicio.numeroPdf);
				System.out.println("Tamaño carpeta, a la que se le resta 2: " +  Inicio.tamañoCarpetaPdf);
				*/
				
				int proximoNHC = Inicio.numeroPdf;
				boolean encontrado = false;
				while((proximoNHC +1 < Inicio.tamañoCarpetaPdf) && !encontrado){
					
					// System.out.println("ProximoNHC " + proximoNHC);
					
					if(Inicio.listaDocumentos[proximoNHC+1].nhc.contains("ERROR") || 
						Inicio.listaDocumentos[proximoNHC+1].nhc.contains("NO")){
							proximoNHC++;
					}
					else if(Inicio.listaDocumentos[proximoNHC].nhc.contains(Inicio.SEPARADOR)){
						break;
					}
					else{
						if(Inicio.listaDocumentos[Inicio.numeroPdf-1].nhc.
								equals(Inicio.listaDocumentos[proximoNHC +1 ].nhc)){
							encontrado = true;
							proximoNHC++;
						}
						else{
							break;
						}
					}
				}
				
				if(encontrado){
					for(int z = Inicio.numeroPdf; z < proximoNHC;z++){
						Inicio.listaDocumentos[z].nhc = Inicio.listaDocumentos[proximoNHC].nhc;
						Inicio.listaDocumentos[z].semaforoAmarilloNhc = true;
					}
				}
			
			}
		}
	}
	
	
	public void registraFecha(){
		Inicio.ventanaFechas.setVisible(false);
		Inicio.listaDocumentos[Inicio.numeroPdf].fecha = Inicio.ventanaFechas.jLfechaRegistrada.getText();
		Inicio.jLServicio.setText(Inicio.listaDocumentos[Inicio.numeroPdf].fecha);
		Inicio.ventanaFechas.jTextField1.setText("");
		Inicio.ventanaFechas.jLfechaRegistrada.setText(Inicio.ventanaFechas.getFechaActiva());
		
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_W);
			robot.delay(50);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private boolean necesitaFecha(){
		
		String nombreDoc = Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado;
		String serv = Inicio.listaDocumentos[Inicio.numeroPdf].servicio;
		
		if(!serv.equals("HOSP") && !serv.equals("CIA") && !serv.equals("URG")){
			String tipoIndexacion = Inicio.excel.documentosXedoc.get(nombreDoc);
			System.out.println(tipoIndexacion);
			if(tipoIndexacion != null && (tipoIndexacion.toLowerCase().equals("x") || tipoIndexacion.toLowerCase().equals("c"))){
				System.out.println("Necesita fecha");
				return true;
			}
		}
		else{
			return false;
		}
		
		return false;
	}
	
	
	public void jBGrabarPagina() {
		

		revisarPropiedadesDocumento();

		if (!Inicio.esperarFecha && Inicio.listaDocumentos[Inicio.numeroPdf].fecha.equals("") && necesitaFecha()) {
			Inicio.ventanaFechas.setVisible(true);
			Inicio.ventanaPrincipal.jBFechas.setBackground(Color.green);
			Inicio.esperarFecha = true; 
		}
		else{
			Inicio.esperarFecha = false;
		}
		
		if(!Inicio.esperarFecha){
			new Acrobat().guardarPagina();
		}

		System.out.println("Numero de pdf " + Inicio.numeroPdf
				+ ". Tamaño Carpeta: " + Inicio.tamañoCarpetaPdf);
		if (!Inicio.esperarFecha && (Inicio.numeroPdf < Inicio.tamañoCarpetaPdf - 1)) {

			// File archivo = new
			// File(Inicio.rutaCompletaPdfs[++Inicio.numeroPdf]);

			// Inicio.ventanaExplorador.listaPdfs.setSelectedIndex(Inicio.numeroPdf);

			File archivo = new File(
					Inicio.listaDocumentos[++Inicio.numeroPdf].rutaArchivo);

			detectaNHCsecuencial();

			if (Inicio.menuVertical) {
				Inicio.ventanaIntegral.listaPdfs
						.setSelectedIndex(Inicio.numeroPdf);
			} else {
				Inicio.ventanaExplorador.listaPdfs
						.setSelectedIndex(Inicio.numeroPdf);
			}

			try {
				// Process p = Runtime.getRuntime().exec(
				// "rundll32 url.dll,FileProtocolHandler " + archivo);

				Desktop.getDesktop().open(archivo);

				Inicio.jBNHC
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].nhc);
				if (Inicio.listaDocumentos[Inicio.numeroPdf].nhc
						.equals(Inicio.SEPARADOR)
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.contains("ERROR")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.equals("NO")) {
					Inicio.jBNHC.setBackground(Color.red);
					Inicio.jBNHCp.setBackground(Color.red);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNHCm.setBackground(Color.red); }
					 */
				} else if (Inicio.listaDocumentos[Inicio.numeroPdf].nhc
						.equals("Eliminar")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.equals("Apartar")) {
					Inicio.jBNHC.setBackground(Color.GRAY);
					Inicio.jBNHCp.setBackground(Color.GRAY);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNHCm.setBackground(Color.GRAY); }
					 */
				} else if (Inicio.listaDocumentos[Inicio.numeroPdf].semaforoAmarilloNhc) {
					Inicio.jBNHC.setBackground(Color.yellow);
					Inicio.jBNHCp.setBackground(Color.yellow);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNHCm.setBackground(Color.yellow); }
					 */
				} else {
					Inicio.jBNHC.setBackground(Color.green);
					Inicio.jBNHCp.setBackground(Color.green);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNHCm.setBackground(Color.green); }
					 */
				}

				if (Inicio.listaDocumentos[Inicio.numeroPdf].nhc
						.equals(Inicio.SEPARADOR)
						&& !Inicio.listaDocumentos[Inicio.numeroPdf].servicio
								.equals("X")) {
					Inicio.jBNHC.setBackground(Color.green);
					Inicio.jBNHCp.setBackground(Color.green);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNHCm.setBackground(Color.green); }
					 */
				}

				Inicio.jBServicio
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].servicio);
				if (Inicio.listaDocumentos[Inicio.numeroPdf].servicio
						.equals("X")) {
					Inicio.jBServicio.setBackground(Color.red);
					Inicio.jBServiciop.setBackground(Color.red);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBServiciom.setBackground(Color.red);
					 * }
					 */
				} else if (Inicio.listaDocumentos[Inicio.numeroPdf].servicio
						.equals("Eliminar")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].servicio
								.equals("Apartar")) {
					Inicio.jBServicio.setBackground(Color.GRAY);
					Inicio.jBServiciop.setBackground(Color.GRAY);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBServiciom.setBackground
					 * (Color.gray); }
					 */
				} else {
					Inicio.jBServicio.setBackground(Color.green);
					Inicio.jBServiciop.setBackground(Color.green);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBServiciom.setBackground
					 * (Color.green); }
					 */
				}

				Inicio.jBNombreDoc
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado);
				if (Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado
						.equals("X")) {
					Inicio.jBNombreDoc.setBackground(Color.red);
					Inicio.jBNombreDocp.setBackground(Color.red);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNombreDocm.
					 * setBackground(Color.red); }
					 */
				} else if (Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado
						.equals("Eliminar")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado
								.equals("Apartar")) {
					Inicio.jBNombreDoc.setBackground(Color.GRAY);
					Inicio.jBNombreDocp.setBackground(Color.GRAY);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNombreDocm.
					 * setBackground(Color.gray); }
					 */
				} else {
					Inicio.jBNombreDoc.setBackground(Color.green);
					Inicio.jBNombreDocp.setBackground(Color.green);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBNombreDocm.
					 * setBackground(Color.green); }
					 */
				}
				
				Inicio.utiles.encajarNombreNormalizado(Inicio.jBNombreDoc.getText());

				Inicio.jBNHCp
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].nhc);
				Inicio.jBServiciop
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].servicio);
				Inicio.jBNombreDocp
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].nombreNormalizado);
				/*
				 * if(Inicio.menuVertical){
				 * Inicio.ventanaMicro.jBNHCm.setText(Inicio
				 * .listaDocumentos[Inicio.numeroPdf].nhc);
				 * Inicio.ventanaMicro.jBServiciom
				 * .setText(Inicio.listaDocumentos[Inicio.numeroPdf].servicio);
				 * Inicio
				 * .ventanaMicro.jBNombreDocm.setText(Inicio.listaDocumentos
				 * [Inicio.numeroPdf].nombreNormalizado); }
				 */
				if (Inicio.listaDocumentos[Inicio.numeroPdf].semaforoAmarilloServicio == true) {
					Inicio.jBServicio.setBackground(Color.yellow);
					Inicio.jBServiciop.setBackground(Color.yellow);
					/*
					 * if(Inicio.menuVertical){
					 * Inicio.ventanaMicro.jBServiciom.setBackground
					 * (Color.gray); }
					 */
				}

				// Actualiza fecha

				String fecha = Inicio.listaDocumentos[Inicio.numeroPdf].fecha;
				if (!fecha.equals("")) {
					Inicio.jLServicio
							.setText(Inicio.listaDocumentos[Inicio.numeroPdf].fecha);
				} else {
					Inicio.jLServicio.setText("Sin fecha");
				}

				// Actualiza al servicio del documento

				Inicio.jLServicios.setSelectedValue(
						Inicio.jBServicio.getText(), true);
				Inicio.jLNombresDoc.setModel(Inicio.excel
						.getDocServicio(Inicio.jLServicios.getSelectedValue()
								.toString()));

				if (Inicio.listaDocumentos[Inicio.numeroPdf].nhc.equals("NO")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.contains("ERROR")) {

					/*
					 * try { Robot robot = new Robot(); robot.delay(1300); }
					 * catch (AWTException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); }
					 */

					Inicio.ventanaIntroducirNHC = new InterfazIntroducirNHC(
							null, false, Inicio.jBNHC);
					Inicio.ventanaIntroducirNHC.setVisible(true);

					// dialog.requestFocus();
					// jPanel1.requestFocus();
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		} 
		else {
			if(!Inicio.esperarFecha){
				int registrar = 0;
				
				if (!Inicio.menuVertical) {
					registrar = JOptionPane.showConfirmDialog(
							Inicio.ventanaPrincipal,
							"Carpeta revisada. ¿Quieres registrar?");
				} else {
					registrar = JOptionPane.showConfirmDialog(
							Inicio.ventanaIntegral,
							"Carpeta revisada. ¿Quieres registrar?");
				}
				if (JOptionPane.OK_OPTION == registrar) {
					Inicio.utiles.registraCambiosFinales();
				}
			}
		}
	}
	
	
	
	
	/**
	 * @param textoOcr
	 * @return 0 No es. 1 Probable. 2 Casi seguro 
	 */
	public static int esUsmi(String textoOcr, String pares[][]){
		
		int suma = 0;
		
		for(int i=0;i<pares.length;i++){
			System.out.println(i	+ "\t" + pares[i][0]);
			if(textoOcr.contains(pares[i][0])){
				int parcial = Integer.valueOf(pares[i][1]);
				suma += parcial;
			}
		}
		
		// JOptionPane.showMessageDialog(null, suma);
		System.out.println(suma);
		
		if(suma >= 5){
			return 2;
		}
		else if (suma >= 4) {
			return 1;
		}
		else{
			return 0;
		}
		
		
	}
	
	public static void recuperaRutaArchivos(){
		
		File rutaCarpeta;
		
		String ruta = "";
		switch (Inicio.destinoDocumentacion) {
		case 1:
			ruta = Inicio.RUTA;
			break;
		case 2:
			ruta = Inicio.RUTAURG;
			break;
		case 3:
			ruta = Inicio.RUTASAL;
			break;
		}
		
		System.out.println(ruta);
		
		rutaCarpeta = new File(ruta).getParentFile();
		String rutaRevision = rutaCarpeta.getAbsolutePath()  + "\\02 Revisado";
		System.out.println(rutaRevision);
		
		JFileChooser selector = new JFileChooser();
		selector.setDialogTitle("Selecciona la carpeta revisada...");
		selector.setCurrentDirectory(new File(rutaRevision));
		selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		selector.showOpenDialog(null);
		File pdfs[];
		
		rutaCarpeta= selector.getSelectedFile();
		
		if(rutaCarpeta != null){
			pdfs = rutaCarpeta.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File arg0, String arg1) {
					// TODO Auto-generated method stub
					return arg1.toLowerCase().endsWith(".pdf");
				}
			});

			for(Documento d : Inicio.listaDocumentos){
				
				String nombreArchivoMemoria = new File(d.rutaArchivo).getName();
				System.out.println("NombreArchivoMemoria:\n" + nombreArchivoMemoria);
				for(File f : pdfs){
					String nombreArchivoRevisado = f.getName();
					if(nombreArchivoMemoria.equals(nombreArchivoRevisado)){
						d.rutaArchivo = f.getAbsolutePath();
						System.out.println("NombreArchivoRevisado:\n" + f.getName());
						break;
					}
				}
				// System.out.println(d.rutaArchivo);
			}
			
			/*
			for( File f : pdfs){
				System.out.println(f.getAbsolutePath());
			}
			*/
		}

	}
	
	public static void main(String args[]){
		
		recuperaRutaArchivos();
	}
}
