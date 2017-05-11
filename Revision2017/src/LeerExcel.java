import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CountryCode;
import jxl.read.biff.BiffException;


public class LeerExcel {

	
	enum Centro { Documentación, Urgencias, Salnés, Ambas}
	
	String[][] tablaDocumentos;
	String[] listaDocumentos;
	String[] listaServicios;
	
	DefaultListModel listaServiciosLista;
	DefaultListModel listaDocumentosDLM;
	DefaultListModel listaComunes;
	DefaultListModel listaHabituales1;
    DefaultListModel listaHabituales2;
    DefaultListModel listaHabitualesUrg;
    
    DefaultListModel vinculacionServicio;
    
    public TreeMap<String, String> documentosXedoc = new TreeMap<String, String>();
    
    int numServicios = 0;
    int numDocumentos = 0;
    
    String[][] conjuntoHabituales;
    String[] comunes;
    String[] habituales1;
    String[] habituales2;
    String[] habitualesUrg;
    
    Object[][] tablaCoordenadas;
    Object[][] tablaVisor;
    DefaultListModel listaUsuariosLista;
    DefaultListModel listaUsuariosListaUrg;
    DefaultComboBoxModel listaUsuarios;
    DefaultComboBoxModel listaUsuariosUrg;
    
    boolean coordenadasGrabadas = false;
    
    public void getPreferencias(String archivo){
		try {
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setEncoding("ISO-8859-1");
			wbSettings.setLocale(new Locale("es", "ES"));
			wbSettings.setExcelDisplayLanguage("ES");
			wbSettings.setExcelRegionalSettings("ES");
			wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
			Workbook archivoExcel = Workbook.getWorkbook(new File(archivo));
			
			Sheet hoja = archivoExcel.getSheet(0);
			
	        int numColumnas = 0;
	        int numFilas = 0;
	        
            while(!hoja.getCell(numColumnas,0).getContents().toString().equals("#finH")){
            	numColumnas++;
            }

	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("#finV")){
	        	numFilas++;
	        }
			
            tablaCoordenadas = new Object[numFilas][numColumnas];
            
          //  System.out.println("Longitud de la la tablacoordenadas " + tablaCoordenadas.length);
            
            //	Leer coordenadas usuarios
            for (int fila=0;fila<numFilas-1;fila++){
                for(int columna=0;columna<numColumnas;columna++){
                	if(hoja.getCell(columna,fila+1).getContents()!="")
                		tablaCoordenadas[fila][columna] = hoja.getCell(columna, fila+1).getContents();
                	else
                		tablaCoordenadas[fila][columna] = 0;

                }
            } 
            
            /*
            for(int i=0;i<numFilas-1;i++){
            	for(int j=0;j<numColumnas;j++){
            		System.out.print(tablaCoordenadas[i][j] + "   ");
            	}
            	System.out.println();
            }
            */
            
//        	Leer cuadro documentos Visor
            hoja = archivoExcel.getSheet(1);
            int numFilasVi = hoja.getRows();
            int numColumVi = hoja.getColumns();
            
            tablaVisor = new Object[numFilasVi][numColumVi];
            
            for (int fila=0;fila<numFilasVi;fila++){
                    for(int columna=0;columna<numColumVi;columna++){					
                    tablaVisor[fila][columna] = hoja.getCell(columna, fila).getContents();
                    }
            }
            
            
            archivoExcel.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
    }
	
	void getTablaDocumentos(String archivo){
        
        
		try {
		
			
			WorkbookSettings wbSettings = new WorkbookSettings();
	        wbSettings.setEncoding("ISO-8859-1");
	        wbSettings.setLocale(new Locale("es", "ES"));
	        wbSettings.setExcelDisplayLanguage("ES"); 
	        wbSettings.setExcelRegionalSettings("ES"); 
	        wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());

	        Workbook archivoExcel = Workbook.getWorkbook(new File(archivo));
		
	        Sheet hoja = archivoExcel.getSheet(0);
	        int numColumnas = 0;
	        int numFilas = 0;
	        
	        
	        
            while(!hoja.getCell(numColumnas,0).getContents().toString().equals("@finH")){
            	numColumnas++;
            }

	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("@finV")){
	        	numFilas++;
	        }
            
         //  System.out.println("El numero de columnas es... " + numColumnas);
	        
	        numServicios = numColumnas-14;
	        
	     //   System.out.println("El numero de servicios es..." + numServicios);
	        
	        numDocumentos = numFilas-1;
	        
	    //    System.out.println("El numero de filas es..." + numFilas);
	    //    System.out.println("El numero de documentos es..." + numDocumentos);
	        
	        tablaDocumentos = new String[numDocumentos][numServicios];
	        listaServicios = new String[numServicios];
	        listaDocumentos = new String[numDocumentos];
	        
	        //	Tabla de todos los documentos
	        for(int fila=0;fila<numDocumentos;fila++){
	        	for(int columna=0;columna<numServicios;columna++){
	        		 tablaDocumentos[fila][columna] = hoja.getCell(columna+2,fila+1).getContents().toString();
	        		// tablaDocumentos[fila][columna] = hoja.getCell(columna+2,fila).getContents().toString();

	        	}
	        }
	        
	        
	        // Lista de todos los servicios
	        for(int columna = 0;columna<numServicios;columna++){
	        	listaServicios[columna] = hoja.getCell(columna + 2,0).getContents().toString();
	        }
	        
	        // Lista de todos los nombres
	        for(int fila = 0;fila<numDocumentos;fila++){
	        	listaDocumentos[fila] = hoja.getCell(0,fila + 1).getContents().toString();
	        }
	        
	        
     
	        listaServiciosLista = new DefaultListModel();
	        for(int i=0;i<numServicios;i++){
	    //    	System.out.println(listaServicios[i].toString());
	        	listaServiciosLista.addElement(listaServicios[i]);
	        }
	        
	    //  Leer lista de todos los documentos        
            listaDocumentosDLM = new DefaultListModel();
            for(int i=0;i<listaDocumentos.length;i++){
                listaDocumentosDLM.addElement(listaDocumentos[i]);
            }   
	        
	        
	    //  Leer hoja excel habituales
            
	        //  Habituales **********************************************
	        // 0..... comunes
	        // 1..... habituales 1
	        // 2..... habituales 2
	        // 3..... habituales urgencias
	        
	        conjuntoHabituales = new String[numDocumentos][4];
	        
	        int numHabituales1 = 0;
	        int numHabituales2 = 0;
	        int numHabitualesU = 0;
	        int numComunes = 0;
	        
	        listaComunes = new DefaultListModel();
	        listaHabituales1 = new DefaultListModel();
            listaHabituales2 = new DefaultListModel();
            listaHabitualesUrg = new DefaultListModel();
	        
	        for(int fila = 0; fila < numDocumentos;fila++){
	        	for(int columna = 0;columna < 4; columna++){
	        		conjuntoHabituales[fila][columna] = hoja.getCell(columna + numServicios + 2 + 7,fila +1).getContents().toString();
	        		if(columna == 0){
	        			if(conjuntoHabituales[fila][columna].toLowerCase().equals("s")){
	        				numComunes++;
	        				listaComunes.addElement(listaDocumentos[fila]);
	        			}
	        		}
	        		else if(columna == 1){
	        			if(conjuntoHabituales[fila][columna].toLowerCase().equals("s")){
	        				numHabituales1++;
	        				listaHabituales1.addElement(listaDocumentos[fila]);
	        			}
	        		}
	        		else if(columna == 2){
	        			if(conjuntoHabituales[fila][columna].toLowerCase().equals("s")){
	        				numHabituales2++;
	        				listaHabituales2.addElement(listaDocumentos[fila]);
	        			}
	        		}
	        		else if(columna == 3){
	        			if(conjuntoHabituales[fila][columna].toLowerCase().equals("s")){
	        				numHabitualesU++;
	        				listaHabitualesUrg.addElement(listaDocumentos[fila]);
	        			}
	        			
	        		}
	        	}
	        }

	        comunes = new String[numComunes];
	        for(int i=0;i<numComunes;i++){
	        	comunes[i] = listaComunes.getElementAt(i).toString();
	        }
	        habituales1 = new String[numHabituales1];
	        for(int i=0;i<numHabituales1;i++){
	        	habituales1[i] = listaHabituales1.getElementAt(i).toString();
	        }
	        habituales2 = new String[numHabituales2];
	        for(int i=0;i<numHabituales2;i++){
	        	habituales2[i] = listaHabituales2.getElementAt(i).toString();
	        }
	        habitualesUrg = new String[numHabitualesU];
	        for(int i=0;i<numHabitualesU;i++){
	        	habitualesUrg[i] = listaHabitualesUrg.getElementAt(i).toString();
	        }

	        
	        int col = numServicios + 2 + 1;
	       
	        //	leer documentos que van a ir a Xedoc
	        
	        for(int fila=0;fila<numFilas-1;fila++){
	        	documentosXedoc.put(hoja.getCell(0,fila+1).getContents().toString(),
	        			hoja.getCell(col,fila+1).getContents().toString());
	        }
	        
	        /*
	        JOptionPane.showMessageDialog(null, "Ver lista documentos Xedoc");
	        
	        System.out.println("Documentos Xedoc");
	        for( Iterator it = documentosXedoc.keySet().iterator(); it.hasNext();) {
	        	String clave = (String)it.next();
	        	String valor = (String)documentosXedoc.get(clave);
	        	System.out.println(clave + " : " + valor);
	        }
	        
	        JOptionPane.showMessageDialog(null, "Vista lista documentos Xedoc");
	        */
	        
	        /*
	        for(int i=0;i<habituales2.length;i++){
	        	System.out.println(habituales2[i]);
	        }
	        */
            
//        	Leer hoja excel usuarios Documentacion
            hoja=archivoExcel.getSheet(6); 
            int numFilasUsDoc = 0;
	        while(!hoja.getCell(0,numFilasUsDoc).getContents().toString().equals("#finV")){
	        	numFilasUsDoc++;
	        }
            System.out.println("número de filas " + numFilasUsDoc);
            listaUsuarios = new DefaultComboBoxModel();
            listaUsuariosLista = new DefaultListModel();
            for(int i=0;i<numFilasUsDoc;i++){
            	listaUsuarios.addElement(hoja.getCell(0, i).getContents().toString());
            	listaUsuariosLista.addElement(hoja.getCell(0, i).getContents().toString());
            	System.out.println(hoja.getCell(0, i).getContents().toString());
            }
                                    
            /*
            tablaCoordenadas = new Object[numFilasUs-1][numColumUs];
            
            //	Leer coordenadas usuarios
            for (int fila=0;fila<numFilasUs-1;fila++){
                for(int columna=0;columna<numColumUs;columna++){
                	if(hoja.getCell(columna,fila+1).getContents()!="")
                		tablaCoordenadas[fila][columna] = hoja.getCell(columna, fila+1).getContents();
                	else
                		tablaCoordenadas[fila][columna] = 0;

                }
            } 
            
            for(int i=0;i<numFilasUs-1;i++){
            	for(int j=0;j<numColumUs;j++){
            		System.out.print(tablaCoordenadas[i][j] + "   ");
            	}
            	System.out.println();
            }
            */
            
//        	Leer hoja excel usuariosUrgencias
            hoja=archivoExcel.getSheet(5);
            int numFilasUsUrg = 0;
	        while(!hoja.getCell(0,numFilasUsUrg).getContents().toString().equals("#finV")){
	        	numFilasUsUrg++;
	        }
            System.out.println("Numero filas urgencias " + numFilasUsUrg);
            
            listaUsuariosUrg = new DefaultComboBoxModel();
            listaUsuariosListaUrg = new DefaultListModel();
            for(int i=0;i<numFilasUsUrg;i++){
            	listaUsuariosUrg.addElement(hoja.getCell(0, i).getContents().toString());
            	listaUsuariosListaUrg.addElement(hoja.getCell(0, i).getContents().toString());
            	System.out.println("hoja 14 " + hoja.getCell(0, i).getContents().toString());
            }
            
            
            
            /*
            tablaCoordenadas = new Object[numFilasUsUrg-1][numColumUsUrg];
                   
            
            //	Leer coordenadas usuariosUrgencias
            for (int fila=0;fila<numFilasUsUrg-1;fila++){
                for(int columna=0;columna<numColumUsUrg;columna++){
                	if(hoja.getCell(columna,fila+1).getContents()!="")
                		tablaCoordenadas[fila][columna] = hoja.getCell(columna, fila+1).getContents();
                	else
                		tablaCoordenadas[fila][columna] = 0;

                }
            } 
            */
            
//        	Leer cuadro documentos Visor
            hoja = archivoExcel.getSheet(4);
            int numFilasVi = hoja.getRows();
            int numColumVi = hoja.getColumns();
            
            tablaVisor = new Object[numFilasVi][numColumVi];
            
            for (int fila=0;fila<numFilasVi;fila++){
                    for(int columna=0;columna<numColumVi;columna++){					
                    tablaVisor[fila][columna] = hoja.getCell(columna, fila).getContents();
                    }
            }
	        
            archivoExcel.close();
            
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        			
		

	}
	
	
	public ArrayList<Modelo> leerListaTotalModelos(String archivo){

		ArrayList<Modelo> listaModelos = new ArrayList<Modelo>();
		
		
		try {
			
			
			
			WorkbookSettings wbSettings = new WorkbookSettings();
	        wbSettings.setEncoding("ISO-8859-1");
	        wbSettings.setLocale(new Locale("es", "ES"));
	        wbSettings.setExcelDisplayLanguage("ES"); 
	        wbSettings.setExcelRegionalSettings("ES"); 
	        wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
		
	        Workbook archivoExcel = Workbook.getWorkbook(new File(archivo));
	        
			
	        int hojaExcel = 1;
	        
	        
	        
	        Sheet hoja = archivoExcel.getSheet(hojaExcel);
	        int numColumnas = 12;
	        int numFilas = 0;
	        
	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("ultimo")){
	        	numFilas++;
	        }
	       // numFilas--;
	        
	        for(int fila=1;fila<numFilas;fila++){
	        	Modelo modelo = new Modelo();
	        	
	        	// System.out.println("Fila... " + fila);
	        	
	        		
		        	modelo.rutaImagen = hoja.getCell(0,fila).getContents();
		        	modelo.nombreNormalizado = hoja.getCell(1,fila).getContents();
		        	modelo.setServiciosModelo(hoja.getCell(2,fila).getContents());
		        	
		        	modelo.numImagen = Integer.valueOf(modelo.rutaImagen.substring(6));
		        	
		        	String aux = hoja.getCell(4,fila).getContents();
		        	int formato = 3;  // Formato o A4, o A5 por defecto
		        	if(aux.equals("A4")){
		        		formato = 0;
		        	}
		        	else if(aux.equals("A5")){
		        		formato = -1;
		        	}
		        	else if(aux.equals("A3")){
		        		formato = 1;
		        	}
		        	modelo.fisica.tamañoPagina = formato;
		        	
		        	aux = hoja.getCell(3,fila).getContents();
		        	int orientacion = 0; // Por defecto vertical u horizontal
		        	if(aux.equals("Vertical")){
		        		orientacion = 1;
		        	}
		        	else {
		        		orientacion = 2;
		        	}
		        	modelo.fisica.vertical = orientacion;
		        	
		        	/*
		        	modelo.metadatos.metaLocalizacion[0] = hoja.getCell(5,fila).getContents();
		        	modelo.metadatos.metaLocalizacion[1] = hoja.getCell(6,fila).getContents();
		        	*/
		        	
		        	modelo.metadatos.metaServicio = hoja.getCell(6,fila).getContents();
		        	modelo.metadatos.metaNombre =  hoja.getCell(7,fila).getContents();
		        	modelo.metadatos.metaModelo =  hoja.getCell(8,fila).getContents();
		        	modelo.metadatos.metaServicioNombre =  hoja.getCell(9,fila).getContents();
		        	
		        	if(modelo.metadatos.metaServicio.equals("")){
		        		modelo.metadatos.metaServicio = "@";
		        	}
		        	if(modelo.metadatos.metaNombre.equals("")){
		        		modelo.metadatos.metaNombre = "@";
		        	}
		        	if(modelo.metadatos.metaModelo.equals("")){
		        		modelo.metadatos.metaModelo = "@";
		        	}
		        	if(modelo.metadatos.metaServicioNombre.equals("")){
		        		modelo.metadatos.metaServicioNombre = "@";
		        	}
		        	
		        	/*
		        	for(int i=0;i<6;i++){
		        		aux = hoja.getCell(10 + i ,fila).getContents();
		        		if(aux == ""){
		        			break;
		        		}
		        		else{
		        			modelo.metadatos.metaAuxiliares.add(aux); 
		        		}
		        	}
		        	*/
		        	
		        	String modeloBisEspecial = hoja.getCell(13,fila).getContents();
		        	if(modeloBisEspecial.equals("Especial")){
		        		modelo.modeloEspecial = true;
		        	}
		        	else{
		        		modelo.modeloEspecial = false;
		        	}
		        	
		        //	modelo.nombreAlternativo = hoja.getCell(21, fila).getContents();
		        	modelo.instruccionesNHC = hoja.getCell(10,fila).getContents();
		        	modelo.instruccionesCIP = hoja.getCell(11,fila).getContents();
		        	modelo.instruccionesNSS = hoja.getCell(12,fila).getContents();
		        	
		        	modelo.centroExterno = hoja.getCell(14,fila).getContents();
		        	modelo.claveCentroExterno1 = hoja.getCell(15,fila).getContents();
		        	modelo.claveCentroExterno2 = hoja.getCell(16,fila).getContents();
		        	modelo.claveCentroExterno3 = hoja.getCell(17,fila).getContents();
		        	
		        	listaModelos.add(modelo);



	        }
	        
	        /*
        	for(int i=0;i<listaModelos.size();i++){
        		System.out.print(listaModelos.get(i).rutaImagen + "\t");
        		System.out.print(listaModelos.get(i).nombreNormalizado + "\t");
        		System.out.print(listaModelos.get(i).servicios.get(0) + "\t");
        		
        	}
	        */
	        
        	archivoExcel.close();
	        
	        return listaModelos;
	        
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;			

	}
	
	public ArrayList<Modelo> leerModelos(String archivo, int destinoDocumentacion){

		ArrayList<Modelo> listaModelos = new ArrayList<Modelo>();
		
		
		try {
			
			
			
			WorkbookSettings wbSettings = new WorkbookSettings();
	        wbSettings.setEncoding("ISO-8859-1");
	        wbSettings.setLocale(new Locale("es", "ES"));
	        wbSettings.setExcelDisplayLanguage("ES"); 
	        wbSettings.setExcelRegionalSettings("ES"); 
	        wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
		
	        Workbook archivoExcel = Workbook.getWorkbook(new File(archivo));
	        
			
	        int hojaExcel = 1;
	        
	        // Hacemos la elección al reves. Si subimos documentación
	        // Introducimos en la tabla todos los documentos menos los de urgencias.
	        String tipoSubida = "";
	        
	        switch (destinoDocumentacion) {
			case 0:
				tipoSubida = Centro.Urgencias.toString();
				break;
			case 1:
				tipoSubida = Centro.Documentación.toString();
				break;
			case 2:
			case 3:
				tipoSubida = Centro.Salnés.toString();
				break;
			}

	        
	        
	        Sheet hoja = archivoExcel.getSheet(hojaExcel);
	        int numColumnas = 12;
	        int numFilas = 0;
	        
	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("ultimo")){
	        	numFilas++;
	        }
	       // numFilas--;
	        
	        for(int fila=1;fila<numFilas;fila++){
	        	Modelo modelo = new Modelo();
	        	
	        	// System.out.println("Fila... " + fila);
	        	
	        	String celda = hoja.getCell(5,fila).getContents().toString();
	        	if(celda.equals(tipoSubida) || celda.equals(Centro.Ambas.toString())){
	        		
	        		System.out.println(fila + "   :   " + celda);
	        		
		        	modelo.rutaImagen = hoja.getCell(0,fila).getContents();
		        	modelo.nombreNormalizado = hoja.getCell(1,fila).getContents();
		        	modelo.setServiciosModelo(hoja.getCell(2,fila).getContents());
		        	
		        	modelo.numImagen = Integer.valueOf(modelo.rutaImagen.substring(6));
		        	
		        	String aux = hoja.getCell(4,fila).getContents();
		        	int formato = 3;  // Formato o A4, o A5 por defecto
		        	if(aux.equals("A4")){
		        		formato = 0;
		        	}
		        	else if(aux.equals("A5")){
		        		formato = -1;
		        	}
		        	else if(aux.equals("A3")){
		        		formato = 1;
		        	}
		        	modelo.fisica.tamañoPagina = formato;
		        	
		        	aux = hoja.getCell(3,fila).getContents();
		        	int orientacion = 0; // Por defecto vertical u horizontal
		        	if(aux.equals("Vertical")){
		        		orientacion = 1;
		        	}
		        	else {
		        		orientacion = 2;
		        	}
		        	modelo.fisica.vertical = orientacion;
		        	
		        	/*
		        	modelo.metadatos.metaLocalizacion[0] = hoja.getCell(5,fila).getContents();
		        	modelo.metadatos.metaLocalizacion[1] = hoja.getCell(6,fila).getContents();
		        	*/
		        	
		        	modelo.metadatos.metaServicio = hoja.getCell(6,fila).getContents();
		        	modelo.metadatos.metaNombre =  hoja.getCell(7,fila).getContents();
		        	modelo.metadatos.metaModelo =  hoja.getCell(8,fila).getContents();
		        	modelo.metadatos.metaServicioNombre =  hoja.getCell(9,fila).getContents();
		        	
		        	if(modelo.metadatos.metaServicio.equals("")){
		        		modelo.metadatos.metaServicio = "@";
		        	}
		        	if(modelo.metadatos.metaNombre.equals("")){
		        		modelo.metadatos.metaNombre = "@";
		        	}
		        	if(modelo.metadatos.metaModelo.equals("")){
		        		modelo.metadatos.metaModelo = "@";
		        	}
		        	if(modelo.metadatos.metaServicioNombre.equals("")){
		        		modelo.metadatos.metaServicioNombre = "@";
		        	}
		        	
		        	/*
		        	for(int i=0;i<6;i++){
		        		aux = hoja.getCell(10 + i ,fila).getContents();
		        		if(aux == ""){
		        			break;
		        		}
		        		else{
		        			modelo.metadatos.metaAuxiliares.add(aux); 
		        		}
		        	}
		        	*/
		        	
		        //	modelo.nombreAlternativo = hoja.getCell(21, fila).getContents();
		        	modelo.instruccionesNHC = hoja.getCell(10,fila).getContents();
		        	modelo.instruccionesCIP = hoja.getCell(11,fila).getContents();
		        	modelo.instruccionesNSS = hoja.getCell(12,fila).getContents();
		        			        	
		        	String modeloBisEspecial = hoja.getCell(13,fila).getContents();
		        	if(modeloBisEspecial.equals("Especial")){
		        		modelo.modeloEspecial = true;
		        	}
		        	else{
		        		modelo.modeloEspecial = false;
		        	}
		        	        	
		        	modelo.centroExterno = hoja.getCell(14,fila).getContents();
		        	modelo.claveCentroExterno1 = hoja.getCell(15,fila).getContents();
		        	modelo.claveCentroExterno2 = hoja.getCell(16,fila).getContents();
		        	modelo.claveCentroExterno3 = hoja.getCell(17,fila).getContents();
		        	
		        	listaModelos.add(modelo);
	        	}


	        }
	        
	        
        	for(int i=0;i<listaModelos.size();i++){
        		System.out.print(listaModelos.get(i).rutaImagen + "\t");
        		System.out.print(listaModelos.get(i).nombreNormalizado + "\t");
        		System.out.print(listaModelos.get(i).servicios.get(0) + "\t");
        		
        	}
	        
	        
        	archivoExcel.close();
	        
	        return listaModelos;
	        
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;			

	}
	
	public ArrayList<Facultativo> leerFacultativos(String archivo, int centro){
		
		//	2 Montecelo
		//	1 Salnes
		
		ArrayList<Facultativo> listaFacultativos = new ArrayList<Facultativo>();
		
		try {
			
			WorkbookSettings wbSettings = new WorkbookSettings();
	        wbSettings.setEncoding("ISO-8859-1");
	        wbSettings.setLocale(new Locale("es", "ES"));
	        wbSettings.setExcelDisplayLanguage("ES"); 
	        wbSettings.setExcelRegionalSettings("ES"); 
	        wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
		
	        Workbook archivoExcel = Workbook.getWorkbook(new File(archivo));
	        
			
	        int hojaExcel = centro - 1;
	        
	        Sheet hoja = archivoExcel.getSheet(hojaExcel);
	        int numFilas = 1;
	        
	        
	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("ultimo")){
	        	numFilas++;
	        }
	       // numFilas--;
	        
	        for(int fila=0;fila<numFilas;fila++){
	        	Facultativo facultativo;
	        	
	        	// System.out.println("Fila... " + fila);
	        	String servicio = hoja.getCell(0,fila).getContents().toString();
	        	String nombre = hoja.getCell(1,fila).getContents().toString();
	        	
	        	facultativo = new Facultativo(nombre, servicio);
		        	
		        listaFacultativos.add(facultativo);
	        }
	        
		}catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Facultativo fal: listaFacultativos){
			System.out.println("Servicio: " + fal.getServicio() + " Nombre: " + fal.getNombre());
		}
		
		
		return listaFacultativos;
	}
	
    DefaultListModel getDocServicio(String servicio){
        int numVinculaciones = 0;
        int numServicio =1;
        boolean encontrado = false;
        DefaultListModel vinculacionAux = new DefaultListModel();

        for(int i=0;i<listaServicios.length;i++){
        	if(listaServicios[i].contains(servicio)){
        		numServicio = i;
        		break;
        	}
        }
        
        /*
        //  Encontramos el número del servicio
        while(!encontrado && numServicio<=numServicios){
            if(tablaDocumentos[0][numServicio].toString().contains(servicio))
                encontrado = true;
            else
                numServicio++;
        }
        */
        
        for(int i=0;i<numDocumentos;i++){
        	if(!tablaDocumentos[i][numServicio].toString().equals("")){
        		numVinculaciones++;
        	}
        }
        
        /*
        //  Encontramos el número de vinculaciones
        for(int i=1;i<=numDocumentos;i++){
            if (tablaDocumentos[i][numServicio].toString().equals("x"))
                numVinculaciones++;
        }
        */
        
        vinculacionServicio = new DefaultListModel();
        
        //  Devolvemos las vinculaciones en un array de cadena
        String[] vinculaciones = new String[numVinculaciones+1];

        Inicio.documentosServicio = new ArrayList<Object>();

        for(int i =0; i<numDocumentos;i++){
            if(!tablaDocumentos[i][numServicio].toString().equals("")){
                vinculacionServicio.addElement(listaDocumentos[i]);
                Inicio.documentosServicio.add(listaDocumentos[i]);
            }
        }
        
        
        //	Quitamos de la lista de documentos del servicio, los que ya estén en habituales
        //  y en comunes.
        
        int tamaño = habituales1.length + habituales2.length + comunes.length;
        int tamaño1 = habituales1.length;
        int tamaño2 = habituales2.length;
        
    //    System.out.println(tamaño + " " + tamaño1 + " " + tamaño2);
        
        String[] todosLosHabituales = new String[tamaño];
		for(int i=0;i<tamaño1;i++){
			todosLosHabituales[i] = habituales1[i];
		}
		for(int i=tamaño1;i<tamaño1 + tamaño2;i++){
			todosLosHabituales[i] = habituales2[i-tamaño1];
		}
		for(int i=tamaño1 + tamaño2 ;i<tamaño;i++){
			todosLosHabituales[i] = comunes[i-tamaño1 -tamaño2];
		}
		
		
		
        for(int i = 0; i<vinculacionServicio.size();i++){
        	encontrado = false;
        	for(int j=0;(j<tamaño && !encontrado);j++){
            	if(vinculacionServicio.getElementAt(i).equals(todosLosHabituales[j])){
            		encontrado = true;
             	}
        	}
        	if(!encontrado){
        		vinculacionAux.addElement((String) vinculacionServicio.getElementAt(i));
        	}
        }

        return vinculacionAux;
        
        
        
    }
	
    
    Point[] getPreferencias(String nombreUser, int numPantallas){
    	
    //	System.out.println(tablaCoordenadas.length);
    	
   	 int numUsers = tablaCoordenadas.length;
   	 Point[] parejaCoordenadas = new Point[6];
   	 for(int i= 0;i<6;i++)
   		 parejaCoordenadas[i]= new Point();

   	 int indice;
   	 int indiceIntegral;
   	 if(numPantallas == 1){
   		 indiceIntegral = 19;
   		 indice = 1;
   	 }
   	 else {
   		 indiceIntegral= 23;
   		 indice = 10;
   	 } 	 
   	 
   	 /*
   	 System.out.println("Número de usuarios " + numUsers);
   	 System.out.println("Número de filas " + tablaCoordenadas.length);
   	 for(int i=0;i<numUsers;i++){
   		 System.out.println(tablaCoordenadas[i][0]);
   	 }
   	 */
   	 
   	 for(int i=0;i<numUsers;i++){
   	//	 System.out.println(tablaCoordenadas[i][0]);
   		 if(tablaCoordenadas[i][0].toString().contains(nombreUser)){
   			 System.out.println("Encontrado nombre de usuario..." + nombreUser);
   			 if(!(tablaCoordenadas[i][1].toString().contains("N"))){
   			//	 System.out.println("hole");
   				 coordenadasGrabadas = true;
	    			 for(int j=0;j<4;j++){
	    				 parejaCoordenadas[j].x = Integer.parseInt(tablaCoordenadas[i][indice +1].toString());indice++;
	    				 parejaCoordenadas[j].y = Integer.parseInt(tablaCoordenadas[i][indice +1].toString());indice++;   				 
	    			 }
	    			 
	    			 //  Coordenadas de las ventanas integrales y de aviso
	    			 if(indiceIntegral == 19){
	    				 indice = 19;
	    			 }else{
	    				 indice = 23;
	    			 }
	    		//	 System.out.println("Indice es " + indice);
	    			 for(int j=0;j<2;j++){
	    		//		 System.out.println(Integer.parseInt(tablaCoordenadas[i][indice].toString()));
	    				 parejaCoordenadas[4 + j].x = Integer.parseInt(tablaCoordenadas[i][indice].toString());indice++;
	    		//		 System.out.println(Integer.parseInt(tablaCoordenadas[i][indice].toString()));
	    				 parejaCoordenadas[4 + j].y = Integer.parseInt(tablaCoordenadas[i][indice].toString());indice++;
	    			 }
	    			 
	    			 break;
   			 }
   		 }
     }

   	
   	 /*
   	System.out.println("El numero de coordenadas es: " + parejaCoordenadas.length);
   	for(int i=0;i<parejaCoordenadas.length;i++){
   		System.out.println(i + " coordenadas: " + parejaCoordenadas[i].x + ", " +
   					parejaCoordenadas[i].y);
   	}
   	 */
   	 
   	 return parejaCoordenadas;
    }
    
    
    String[] getDocServicioVisor(String servicio){
        int numVinculaciones = 0;
        int numServicio =1;
        boolean encontrado = false;
        //  Encontramos el número del servicio
        while(!encontrado && numServicio<numServicios){
            if(servicio == tablaDocumentos[0][numServicio])
                encontrado = true;
            else
                numServicio++;
        }

        //  Encontramos el número de vinculaciones
        for(int i=1;i<numDocumentos;i++){
            if (tablaDocumentos[i][numServicio].toString().equals("x"))
                numVinculaciones++;
        }

        String[] vinculacionServicio = new String[numVinculaciones];
        
        //  Devolvemos las vinculaciones en un array de cadena
        String[] vinculaciones = new String[numVinculaciones];
        int aux=0;
        for(int i = 1; i<numDocumentos;i++){
            if(!tablaDocumentos[i][numServicio].toString().equals("")){
                vinculacionServicio[aux]= tablaDocumentos[i][0].toString();
                aux++;
            }
        }
        
        return vinculacionServicio;
    }
    
}
