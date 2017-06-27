import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.itextpdf.awt.geom.Rectangle;



class Worker extends SwingWorker<Double, Integer>{

	JProgressBar progresoNHCs;
	JProgressBar progresoServicios;
	JProgressBar progresoNombres;
	JProgressBar progresoRenombrar;
	
	JTextField textoPdfExaminado;
	
	VentanaProgreso vProgreso;
	CargaListaPdfs pdfs;
	int visualizacion;
	
	static final String FUSIONAR = "Fusionar";    /* VESASEV */
	
	public Worker(VentanaProgreso vProgreso,CargaListaPdfs pdfs, int visualizacion, JProgressBar progresoNHCs, JProgressBar progresoServicios, JProgressBar progresoNombres, JProgressBar progresoRenombrar, JTextField textoPdfExaminado){
		this.progresoNHCs = progresoNHCs;
		this.progresoServicios = progresoServicios;
		this.progresoNombres = progresoNombres;
		this.progresoRenombrar = progresoRenombrar;
		
		this.textoPdfExaminado = textoPdfExaminado;
		
		this.pdfs = pdfs;
		this.visualizacion = visualizacion;
		this.vProgreso = vProgreso;
	}
	
	
	@Override
	protected Double doInBackground() {
		// TODO Auto-generated method stub
		
		int tamaño = pdfs.nombrePdfs.length;
		int tamañoLista = tamaño;
		        					
		Inicio.rutaCompletaPdfs = new String[tamaño];
//		rutaCompletaPdfs = new String[tamaño];
//		objetoPuente = new Object[tamañoLista];	//	Para pasar los datos a un jOptionPane (ya subidos)
	
		int numeroDocs = pdfs.ficheros.length;
		Inicio.listaDocumentos = new Documento[numeroDocs];
		int tamModelos = Inicio.modelos.size();
		System.out.println("Estamos en doInBackground, en el hilo " + 
				Thread.currentThread().getName());
		
		for(int i=0;i<numeroDocs;i++){
			Inicio.listaDocumentos[i] = new Documento(pdfs.ficheros[i].getAbsolutePath());
			Inicio.listaDocumentos[i].getNhc();
			publish( i*100/numeroDocs,0,0,0,i);
		}
		
		for(int i=0;i<numeroDocs;i++){
			
			System.out.println(Inicio.listaDocumentos[i].cadenaOCR);

			if(Inicio.modoAdministrador){
				// JOptionPane.showMessageDialog(null, Inicio.listaDocumentos[i].cadenaOCR);
				//JOptionPane.showMessageDialog(null, Inicio.listaDocumentos[i].cadenaOCR,pdfs.ficheros[i].getName() , JOptionPane.PLAIN_MESSAGE );
				JTextArea txt = new JTextArea();
				txt.setText(Inicio.listaDocumentos[i].cadenaOCR);
				txt.setEditable(false);
				JOptionPane.showOptionDialog(null, txt, pdfs.ficheros[i].getName(), JOptionPane.OK_OPTION,
				                JOptionPane.INFORMATION_MESSAGE, null, null, null);
			}
			
			
			boolean find = false;
			for(int j=0;j<tamModelos;j++){
				if(Inicio.listaDocumentos[i].detector(Inicio.modelos.get(j),1)){
					find = true;
					break;
				}
			}
			
			for(int j=0;j<tamModelos && !find;j++){
				if(Inicio.listaDocumentos[i].detector(Inicio.modelos.get(j),2)){
					find = true;
					break;
				}
			}
			
			for(int j=0;j<tamModelos && !find;j++){
				if(Inicio.listaDocumentos[i].detector(Inicio.modelos.get(j),3)){
					find = true;
					break;
				}
			}

			// publish( Porcentaje NHC, PorcentajeDocumentos, PorcentajeServicios, PorcentajeRenombrar, nº de pdf)
			// System.out.println("*************************************************************");
			// System.out.println("Pdf número nhc..." + i );
			
			// JOptionPane.showMessageDialog(null, "NHC ... " + Inicio.listaDocumentos[i].nhc);
			
			publish( 0,i*100/numeroDocs,0,0,i);
		}
		
		System.out.println("Segunda tanda de reconocimiento...");

		
	//	JOptionPane.showMessageDialog(null, "Prueba nhc");
		
		
		try {
			for(int i=0;i<numeroDocs;i++){
				
				Inicio.listaDocumentos[i].escanerNHC(Inicio.listaDocumentos[i].numeroModelo);
				Inicio.listaDocumentos[i].nhc = esNumerico(Inicio.listaDocumentos[i].nhc);
				
				//	JOptionPane.showMessageDialog(null, "Documento... " + i + "  " + Inicio.listaDocumentos[i].nhc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error... ");
			JOptionPane.showMessageDialog(null, "Error");
		}
		
	//	JOptionPane.showMessageDialog(null, "Fin Prueba nhc");
		
		/*
		for(int i=0;i<aux;i++){
			
			if(Inicio.listaDocumentos[i].nhc.equals("NO")){
				int lim = Inicio.listaDocumentos[i].cadenaOCR.length();
				if(lim > 300){
					lim = 300;
				}
				
	//			JOptionPane.showMessageDialog(null, Inicio.listaDocumentos[i].cadenaOCR.substring(0,lim));
				
				for(int j=0;j<tamModelos;j++){
					if(Inicio.listaDocumentos[i].reDetectorNHC(Inicio.modelos.get(j))){
						break;
					}
				}
			}
			
			

		}
		*/

		
		// Tercera tanda de reconocimiento solo para urgencias
		for(int i=0;i<numeroDocs;i++){
//	JOptionPane.showMessageDialog(null, "Nhc... " + Inicio.listaDocumentos[i].nhc);
				Inicio.listaDocumentos[i].reDetectorNHCUrgencias();
		}
		
		
		// Reconocimientos varios
		for(int i=0;i<numeroDocs;i++){
			 
			// Inicio.listaDocumentos[i].nhc = NHC.nhcTriaje143(Inicio.listaDocumentos[i]);
			 
			if(NHC.borrarNHC(Inicio.listaDocumentos[i])){
				 Inicio.listaDocumentos[i].nhc = "NO";
			}
			
			if(Inicio.listaDocumentos[i].nombreNormalizado.equals("X")){
				if(Excepciones.detectaOrdenesMedicas(i)){
					Inicio.listaDocumentos[i].nombreNormalizado = Inicio.ORDENES_MEDICAS;
				}
				
				if(Excepciones.detectaProtocolo(i)){
					Inicio.listaDocumentos[i].nombreNormalizado = Inicio.REGISTRO_ANESTESIA;
				}
			}
			
			if(Inicio.listaDocumentos[i].nombreNormalizado.equals("X")){

			}
		}
		
		/*
		 JOptionPane.showMessageDialog(null, "Limpiar consola");
		//	Reconocimiento de ekg´s y ecos
		
		System.out.println("Peso \t Alto \t Ancho");
		for(int i=0;i<numeroDocs;i++){
			System.out.println(Inicio.listaDocumentos[i].fisica.peso + "\t" +
								Inicio.listaDocumentos[i].fisica.dimensiones.alto + "\t" + 
								Inicio.listaDocumentos[i].fisica.dimensiones.ancho);
		}
		JOptionPane.showMessageDialog(null, "Ver dimensiones");
		*/
		

		
		
		Inicio.separadores = new ArrayList<Integer>();
		Inicio.separadores = new Separadores().getNumOrdenSeparadores();
		System.out.println("El primer separador vale: " + Inicio.separadores);
   		
		
		//	Adivina nombre separador
		int numSeparador = 1;
		for(int i=Inicio.separadores.get(0);i<Inicio.listaDocumentos.length;i++){
			String servicioPosible = AdivinaServicio.getServicio(i + 1,Inicio.separadores.get(numSeparador));
			
			System.out.println("Servicio Posible: " + servicioPosible);
			
			if(i == -1){
				i = 0;
			}
			for(int j=i;j<Inicio.separadores.get(numSeparador);j++){
				
				//Comprobamos si el servicio es anestesia para hacer el cambio anrc - carc
				if(servicioPosible.equals(Inicio.ANRC) || servicioPosible.equals(Inicio.NRLC)){
					System.out.println("Anestesia o Neurologia");
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)) {
						Inicio.listaDocumentos[j].servicio = Inicio.CARC;
					}
					else{
						Inicio.listaDocumentos[j].servicio = servicioPosible;
					}
					if(j-1 >= i){
						
						System.out.println("Neurologia - interconsulta");
						
						if(servicioPosible.equals(Inicio.NRLC)){
							if(Inicio.listaDocumentos[j-1].nombreNormalizado.equals(Inicio.EKG) 
									&& Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.INTERCONSULTA)) {
								Inicio.listaDocumentos[j].servicio = Inicio.CARC;
							}
						}
					}
				}
				else if(servicioPosible.equals(Inicio.ORLC)){
					
					System.out.println("Otorrino - videonistag");
					
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
						// JOptionPane.showMessageDialog(null, "Renombrado de ekg a video... " + Inicio.listaDocumentos[j].numeroModelo);
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.VIDEONISTAGMOGRAFÍA;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				else if(servicioPosible.equals(Inicio.UDOC)){
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.MAPA_DERMATOMAS;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				else if(servicioPosible.equals(Inicio.CIA)){
					
					System.out.println("CIAS toas");
					
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				
				else if(servicioPosible.equals(Inicio.HOSP)){
					System.out.println("Hospitalizac. menos las excepciones");
					if(!Excepciones.excepcionesIngresos(j)){
						Inicio.listaDocumentos[j].servicio = servicioPosible;
					}
					
				}
				else if(servicioPosible.equals(Inicio.CARC) || servicioPosible.equals(Inicio.PEDC)){
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.ECO)){
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.ECOCARDIOGRAFIA;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				else if(!servicioPosible.equals("")){
					
					System.out.println("Toas las demas");
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
		
				
			}
			
			publish(100,100,i*100/Inicio.listaDocumentos.length,0,0);
			
			i= Inicio.separadores.get(numSeparador) -1 ;
			numSeparador++;
		}
		
		
		for(int i=0;i<numeroDocs;i++){
			
			// JOptionPane.showMessageDialog(null, Inicio.listaDocumentos[i].nhc);
			System.out.println("***********************************************");
			System.out.println(new File(Inicio.listaDocumentos[i].rutaArchivo).getName());
			System.out.println("NHC: " +Inicio.listaDocumentos[i].nhc);
			System.out.println("Vertical: " +Inicio.listaDocumentos[i].fisica.vertical);
			System.out.println("Tam OCR: " + Inicio.listaDocumentos[i].cadenaOCR.length());
			System.out.println("Tam Pag: " + Inicio.listaDocumentos[i].fisica.tamañoPagina);
			System.out.println(Inicio.listaDocumentos[i].cadenaOCR);
			
			
			Inicio.listaDocumentos[i].detectaUsmi();
			Inicio.listaDocumentos[i].detectaEKGsA5();
			Inicio.listaDocumentos[i].detectaEKGs();
			Inicio.listaDocumentos[i].detectaMonitor();
			Inicio.listaDocumentos[i].detectaDocRosa();
			Inicio.listaDocumentos[i].detectaEcos();
			
			if(Inicio.listaDocumentos[i].nhc.equals(Inicio.SEPARADOR) || 
			   Inicio.listaDocumentos[i].nhc.equals(Inicio.SEPARADOR_FUSIONAR))
					
					Inicio.listaDocumentos[i].nombreNormalizado = "X";
			
			System.out.println();
			//System.out.println("Publico... " + i);
			
			
			
			publish(100,i*100/numeroDocs,0,0,i);
		}
		
		// Bis. Gran chapuza.
		//	Adivina nombre separador
		numSeparador = 1;
		for(int i=Inicio.separadores.get(0);i<Inicio.listaDocumentos.length;i++){
			String servicioPosible = AdivinaServicio.getServicio(i + 1,Inicio.separadores.get(numSeparador));
			
			System.out.println("Servicio Posible: " + servicioPosible);
			
			if(i == -1){
				i = 0;
			}
			for(int j=i;j<Inicio.separadores.get(numSeparador);j++){
				
				//Comprobamos si el servicio es anestesia para hacer el cambio anrc - carc
				if(servicioPosible.equals(Inicio.ANRC) || servicioPosible.equals(Inicio.NRLC)){
					System.out.println("Anestesia o Neurologia");
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)) {
						Inicio.listaDocumentos[j].servicio = Inicio.CARC;
					}
					else{
						Inicio.listaDocumentos[j].servicio = servicioPosible;
					}
					if(j-1 >= i){
						
						System.out.println("Neurologia - interconsulta");
						
						if(servicioPosible.equals(Inicio.NRLC)){
							if(Inicio.listaDocumentos[j-1].nombreNormalizado.equals(Inicio.EKG) 
									&& Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.INTERCONSULTA)) {
								Inicio.listaDocumentos[j].servicio = Inicio.CARC;
							}
						}
					}
				}
				else if(servicioPosible.equals(Inicio.ORLC)){
					
					System.out.println("Otorrino - videonistag");
					
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
						// JOptionPane.showMessageDialog(null, "Renombrado de ekg a video... " + Inicio.listaDocumentos[j].numeroModelo);
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.VIDEONISTAGMOGRAFÍA;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				else if(servicioPosible.equals(Inicio.UDOC)){
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.EKG)){
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.MAPA_DERMATOMAS;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				else if(servicioPosible.equals(Inicio.CIA)){
					
					System.out.println("CIAS toas");
					
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				
				else if(servicioPosible.equals(Inicio.HOSP)){
					System.out.println("Hospitalizac. menos las excepciones");
					if(!Excepciones.excepcionesIngresos(j)){
						Inicio.listaDocumentos[j].servicio = servicioPosible;
					}
					
				}
				else if(servicioPosible.equals(Inicio.CARC) || servicioPosible.equals(Inicio.PEDC)){
					if(Inicio.listaDocumentos[j].nombreNormalizado.equals(Inicio.ECO)){
						Inicio.listaDocumentos[j].nombreNormalizado = Inicio.ECOCARDIOGRAFIA;
					}
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
				
				else if(servicioPosible.equals(Inicio.USMI)){
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
				}
				
				else if(!servicioPosible.equals("")){
					
					System.out.println("Toas las demas");
					Inicio.listaDocumentos[j].servicio = servicioPosible;
				}
		
				
			}
			
			publish(100,100,i*100/Inicio.listaDocumentos.length,0,0);
			
			i= Inicio.separadores.get(numSeparador) -1 ;
			numSeparador++;
		}
		
		int errores = 0;
		for(int i=0;i<Inicio.listaDocumentos.length;i++){
			
			/*
			if(Inicio.documentacion == 2 && Inicio.listaDocumentos[i].servicio.equals("CARC") 
					&& Inicio.listaDocumentos[i].nombreNormalizado.equals(Inicio.EKG)){
				 RotarEkg.rotarEkg(Inicio.listaDocumentos[i].rutaArchivo, Inicio.listaDocumentos[i].rutaArchivo);
			}
			*/
			
			if(!Inicio.listaDocumentos[i].renombraFichero(Inicio.listaDocumentos[0]))
				errores++;

			publish(100,100,100,i*100/Inicio.listaDocumentos.length,i);
		}
		
		System.out.println(errores + " errores");
		        					
		Inicio.modelo = new DefaultListModel();

		//	Almacena las carpetas por las que navega el usuario
		if(tamaño>0){
			String auxS = pdfs.rutaPdfs[0];
			int auxInt = auxS.lastIndexOf("\\");
			auxS = auxS.substring(0,auxInt);
			auxInt = auxS.lastIndexOf("\\");
			auxS = auxS.substring(0, auxInt);
			//System.out.println(aux);
			Inicio.carpetasAbiertas.add(auxS);
		}
		        			

/*   Prueba fusionado */
		
		Fusion fusion = new Fusion(Inicio.listaDocumentos);
		
		Inicio.listaDocumentos = fusion.getListadoFinal();		
		
//		JOptionPane.showMessageDialog(null, "Ver listado anterior... ");
		
		Inicio.tamañoCarpetaPdf = Inicio.listaDocumentos.length;
		
		tamaño = Inicio.tamañoCarpetaPdf;
		
		pdfs.nombrePdfs = new String[tamaño];
		pdfs.rutaPdfs = new String[tamaño];
		
		for(int i=0;i<tamaño;i++){
			
			File fichero = new File(Inicio.listaDocumentos[i].rutaArchivo);
			String nombreAux = fichero.getName();
	//		Inicio.modelo.addElement(pdfs.nombrePdfs[i]);
			pdfs.nombrePdfs[i] = nombreAux;
			
			Inicio.modelo.addElement(nombreAux);

	//		Inicio.rutaCompletaPdfs[i] = pdfs.rutaPdfs[i];
			pdfs.rutaPdfs[i] = Inicio.listaDocumentos[i].rutaArchivo;
			Inicio.rutaCompletaPdfs[i] = Inicio.listaDocumentos[i].rutaArchivo;
			
	/*		System.out.println("**************************************************");
			System.out.println("Pdfs.nombrePdfs[i]: " + pdfs.nombrePdfs[i]);
			System.out.println("Inicio.rutaCompletaPdfs[i]: " + Inicio.rutaCompletaPdfs[i]);
			System.out.println("**************************************************");
			System.out.println(" ");
	*/
		}
		
		
		for(int i=0;i<tamaño;i++){
			System.out.println("listadocumentos: " + Inicio.listaDocumentos[i].rutaArchivo );
			System.out.println("nhc: " + Inicio.listaDocumentos[i].nhc);
			System.out.println("**************************************************");
			System.out.println(" ");
		}

		

		
		//	Determina el directorio firmados
		
		System.out.println("Determinando el directorio firmados");
		        					
		Inicio.ventanaExplorador.listaPdfs.setModel(Inicio.modelo);
//  					listaPdfs.setFont(new Font("Arial",Font.BOLD,10));
    	Inicio.ventanaExplorador.setTitle(pdfs.getRutaCarpeta());
		Inicio.ficherosCargados= true;
	

    	
    	/*
    	else{
    		if(Inicio.separadores.get(0) == -1){
        		JOptionPane.showMessageDialog(null, "No se ha detectado un separador. Puedes fijar el" +
        				" servicio de los documentos, en el botón fijar servicios");
        	}
    	}
    	*/
    	// vp.dispose();


		return 100.0;
	}
	
	protected void done(){
		System.out.println("hecho");
		Inicio.progreso = true;
	    if(Inicio.ficherosCargados){
	    	// vp.dispose();
	    	if(Inicio.ventanaRevisionAbierta == false){
	        /*
	        	 java.awt.EventQueue.invokeLater(new Runnable() {
	        		        		        		public void run() {
	        	        jMenu3.setEnabled(true);
	        	        jMenu2.setEnabled(true);
	        	        jMenuItem51.setEnabled(true);

	        		//	Inicio.ventanaD = new InterFazTabla();
	        		//	Inicio.ventanaD.setVisible(true);  
	        	        
	        	        Inicio.ventanaPrincipal = new VentanaPrincipal();
	        	        Inicio.ventanaCompacta = new VentanaCompacta();
	        			
	        		}
	        	});
			*/
	    		System.out.println("Las ventanas no estan abiertas");
	    		
	    		System.out.println("Visualización vale..." + visualizacion);

	 
	    		if(Inicio.ventanaPrincipal == null){
	    			Inicio.ventanaPrincipal = new VentanaPrincipal();
	    		}
	    		
		   //     Inicio.ventanaCompacta = new VentanaCompacta();
		        Inicio.ventanaPrincipal.setBounds(Inicio.coordenadas.coordenadas[3].x-26, Inicio.coordenadas.coordenadas[3].y, 750, 970);
		   //     Inicio.ventanaPrincipal.setResizable(false);
		        
		   /*     Inicio.ventanaCompacta.setBounds(Inicio.coordenadas.coordenadas[2].x, Inicio.coordenadas.coordenadas[2].y, 750, 180);
		        Inicio.ventanaCompacta.jPanel1.removeKeyListener(Inicio.ventanaCompacta.listener);
		        Inicio.ventanaCompacta.setVisible(false); */
		        
		        Inicio.ventanaMicro = new VentanaMicro();
    	        Inicio.ventanaMicro.setBounds(Inicio.coordenadas.coordenadas[5].x, Inicio.coordenadas.coordenadas[5].y, 730, 60);
    	        
    		    Inicio.ventanaFechas = new VentanaFechas();
    		    Inicio.ventanaFechas.setVisible(false);
    		    java.awt.Rectangle rect  = Inicio.ventanaFechas.getBounds();
    		    
		        Inicio.ventanaFechas.setBounds(Inicio.coordenadas.coordenadas[3].x, Inicio.coordenadas.coordenadas[3].y + 1000, rect.width, rect.height);

    	        
		        System.out.println("Visualización vale..." + visualizacion);
		        
	    		if(visualizacion == 2 || visualizacion == 1){
		        	Inicio.ventanaPrincipal.setVisible(false);
	    	       //

		    	        Inicio.ventanaIntegral = new VentanaIntegral();
		    	        Inicio.ventanaIntegral.setBounds(Inicio.coordenadas.coordenadas[4].x, Inicio.coordenadas.coordenadas[4].y, 360,1150);

	        

	    	        // Inicio.ventanaNombres = new VentanaNombres();
	    	        // Inicio.ventanaNombresYServicios = new VentanaNombresYServicios();
	    			if(Inicio.nombrePc.toLowerCase().contains("mahc13p")
	    					|| Inicio.nombrePc.toLowerCase().contains("mahc35p")
	    					|| Inicio.nombrePc.toLowerCase().contains("mahc03p") 
	    					|| Inicio.nombrePc.toLowerCase().contains("mahc01p") 
	    					|| Inicio.nombrePc.toLowerCase().contains("mahc04p") 
	    					|| Inicio.nombrePc.toLowerCase().contains("mahc17p") 
	    					){
	    				// Inicio.acrobatAntiguo = true;
	    				Inicio.rutaFocoAcrobatV = "cal\\FocoAcrobatV3.exe";
	    			}
	    			else{
	    				Inicio.acrobatAntiguo = false;
	    				Inicio.rutaFocoAcrobatV = "cal\\FocoAcrobatV.exe";
	    			}
	    			/*
	    			else{
	    				Inicio.rutaFocoAcrobat = "cal\\FocoAcrobatV.exe";
	    			}
	    			*/
	    			Inicio.ventanaExplorador.setState(Frame.ICONIFIED);
	    		}
		        
	    		File archivo2 = null;
	    		if(visualizacion == 0){
	    			archivo2 = new File(Inicio.rutaFocoAcrobat);
	    		}
	    		else{
	    			archivo2 = new File(Inicio.rutaFocoAcrobatV);
	    		}
		        
		        System.out.println("El archivo existe?" + archivo2.exists());
		 //       File archivo3 = new File(Inicio.rutaFocoNHC);
		        try {
					 Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo2);
		//			 Process pNHC = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo3);
		        	    		        	        	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
	        	Inicio.ventanaRevisionAbierta = true;

	    	}
	    	else{
	    			    		
	    		if(visualizacion == 0){
	    			if(Inicio.ventanaPrincipal == null){
	    				Inicio.ventanaPrincipal = new VentanaPrincipal();
	    			}
		    		
	 		        Inicio.ventanaPrincipal.setBounds(Inicio.coordenadas.coordenadas[3].x, Inicio.coordenadas.coordenadas[3].y, 750, 970);
	 		        Inicio.ventanaPrincipal.setResizable(false);
	    			Inicio.ventanaPrincipal.setVisible(true);
	    			
	    			Inicio.utiles.habilitarTeclas("Teclas On", Inicio.visualizacion);
	    			Inicio.utiles.habilitarTeclas("Teclas Off", Inicio.visualizacion);
	    			
	    			if(Inicio.ventanaIntegral != null)
	    				Inicio.ventanaIntegral.setVisible(false);
	    			
	    			Inicio.ventanaExplorador.setState(Frame.NORMAL);
	    		}
	    		else if(visualizacion == 1 || visualizacion == 2){
	    			
	    			Inicio.utiles.habilitarTeclas("Teclas On", Inicio.visualizacion);
	    			Inicio.utiles.habilitarTeclas("Teclas Off", Inicio.visualizacion);
	    			
	    			if(Inicio.ventanaPrincipal != null)
	    				Inicio.ventanaPrincipal.setVisible(false);
	    			if(Inicio.ventanaIntegral != null)
	    				Inicio.ventanaIntegral.setVisible(true);
	    			else{
		    	        Inicio.ventanaIntegral = new VentanaIntegral();
		    	        Inicio.ventanaIntegral.setBounds(Inicio.coordenadas.coordenadas[4].x, Inicio.coordenadas.coordenadas[4].y, 360,1150);
	    			}
	    		}
	    	}
	    	
	    	if(Inicio.destinoDocumentacion == 0){
	    		Inicio.ventanaExplorador.renombraURG();
	    	}
	    }
		vProgreso.dispose();
		
		abrePrimerPdf();
		
		/*
		// Inicializa los parametros de visualización del adobe de los pcs nuevos
		
		File archivo3 = null;
		archivo3 = new File(Inicio.rutaPreferenciasAdobe);

        
        System.out.println("El archivo existe?" + archivo3.exists());
        
 //       File archivo3 = new File(Inicio.rutaFocoNHC);
        try {
        	
        	 Thread.sleep(2500);
			 Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo3);
//			 Process pNHC = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + archivo3);
        	    		        	        	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}
	
	@Override
    protected void process(List<Integer> chunks) {
        System.out.println("process() esta en el hilo "
                + Thread.currentThread().getName());
        progresoNHCs.setValue(chunks.get(0));
        progresoServicios.setValue(chunks.get(1));
        progresoNombres.setValue(chunks.get(2));
        progresoRenombrar.setValue(chunks.get(3));
        
        File file = new File(Inicio.listaDocumentos[chunks.get(4)].rutaArchivo);
        

		if(file.getName().length() > 40){
			textoPdfExaminado.setFont(new Font("TimesRoman", Font.BOLD, 12));
		}
		else{
			textoPdfExaminado.setFont(new Font("TimesRoman", Font.BOLD, 20));
		}
        
        textoPdfExaminado.setText(file.getName());
    }
	
	
	String esNumerico(String s){
		
		if(!s.equals(Inicio.SEPARADOR) && !s.equals(Inicio.SEPARADOR_FUSIONAR)){
			
			try {
				int in = Integer.parseInt(s);
				if(in > 0 && in < 2400000){
					return String.valueOf(in);
				}
				else{
					return "ERROR";
				}
				
			} catch (NumberFormatException nfe){	
				// JOptionPane.showMessageDialog(null, s);
				return "ERROR";
			}
		}
		
		return s;

	}
	
	void abrePrimerPdf(){
		Inicio.numeroPdf = 0;
		if (Inicio.numeroPdf < Inicio.tamañoCarpetaPdf - 1) {


			File archivo = new File(
					Inicio.listaDocumentos[Inicio.numeroPdf].rutaArchivo);

			Inicio.utiles.detectaNHCsecuencial();

			if (Inicio.menuVertical) {
				Inicio.ventanaIntegral.listaPdfs
						.setSelectedIndex(Inicio.numeroPdf);
			} else {
				Inicio.ventanaExplorador.listaPdfs
						.setSelectedIndex(Inicio.numeroPdf);
			}

			try {
				 Process p = Runtime.getRuntime().exec(
				 "rundll32 url.dll,FileProtocolHandler " + archivo);

				System.out.println(Inicio.listaDocumentos[Inicio.numeroPdf].rutaArchivo);
				//Desktop.getDesktop().open(archivo);

				Inicio.jBNHC
						.setText(Inicio.listaDocumentos[Inicio.numeroPdf].nhc);
				if (Inicio.listaDocumentos[Inicio.numeroPdf].nhc
						.equals(Inicio.SEPARADOR)
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.contains("ERROR")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.equals("NO")
						|| Inicio.listaDocumentos[Inicio.numeroPdf].nhc
								.equals(Inicio.SEPARADOR_FUSIONAR)) {
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
	}
}