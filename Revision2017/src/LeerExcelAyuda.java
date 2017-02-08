import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.CountryCode;
import jxl.biff.WorkbookMethods;
import jxl.read.biff.BiffException;

public class LeerExcelAyuda {

	
	private String[] servicios;
	private String[] nombres;
	private String[][] tablaDocumentos;
	private String[][] asociacionesDocumentos;
	private String[][] habituales;
	private String[][] tablaHermesAyuda;
	private String[][] tablaHermesOCR;
	
	//	Para manipular el fichero de ayuda
	
	String proximoIndice = "";
	int ultimaFila = 0;

	public String[] getServicios(){
		return servicios;
	}
	
	public String[] getNombres(){
		return nombres;
	}
	
	public String[][] getTabla(){
		return tablaDocumentos;
	}
	
	public String[][] getTablaHermesAyuda(){
		return tablaHermesAyuda;
	}
	
	public String[][] getTablaHermesOCR(){
		return tablaHermesOCR;
	}
	
	public String[][] getHabituales(){
		return habituales;
	}
	
	public String[][] getAsociaciones(){
		return asociacionesDocumentos;
	}
	
	
	public void leerAyuda(String archivoFuente){
		WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setEncoding("ISO-8859-1");
        wbSettings.setLocale(new Locale("es", "ES"));
        wbSettings.setExcelDisplayLanguage("ES"); 
        wbSettings.setExcelRegionalSettings("ES"); 
        wbSettings.setCharacterSet(CountryCode.SPAIN.getValue());
        
        Workbook archivoExcel;
		try {
			archivoExcel = Workbook.getWorkbook(new File(archivoFuente));
			
			//	Hojas pdf 			
	        Sheet hoja = archivoExcel.getSheet(0);
	        
	        //	Obtiene los nombres
	        int numColumnas = 11;
	        int numFilas = 1;
	        
	        while(!hoja.getCell(0,numFilas).getContents().toString().equals("ultimo")){
	        	numFilas++;
	        }
	        
	        // La última fila será donde escribamos el nuevo registro
	        ultimaFila = numFilas;
	        
	        tablaHermesAyuda = new String[ultimaFila-1][numColumnas];
	        for(int i=0;i<ultimaFila-1;i++){
	        	for(int j=0;j<numColumnas;j++){
	        		tablaHermesAyuda[i][j] = hoja.getCell(j,i+1).getContents().toString();
	        	}
	        }
	        
	        /*
	        for(int i=0;i<ultimaFila-1;i++){
	        	for(int j=0;j<numColumnas;j++){
	        		System.out.print(tablaHermesAyuda[i][j] + "  ");
	        	}
	        	System.out.println();
	        }
	        */
	        
	        if(ultimaFila != 1){
	        	
	        	proximoIndice = "Index_";
	        	int indice = ultimaFila;
	        	if(indice > 999 && indice < 10000 ){
	        		proximoIndice += "0";
	        	}
	        	else if(indice > 99 && indice < 1000 ){
	        		proximoIndice += "00";
	        	}
	        	else if(indice > 9 && indice < 100 ){
	        		proximoIndice += "000";
	        	}
	        	else if(indice < 10 ){
	        		proximoIndice += "0000";
	        	}
	        	proximoIndice += String.valueOf(indice);
	        }
	        else{
	        	proximoIndice = "Index_00001";
	        }
	        
	        System.out.println("Num filas = " + numFilas + ". Num columnas = " + numColumnas);
	        System.out.println("Indice: " + proximoIndice);
	        
	        
	        
			//	Hojas pdf 			
	        hoja = archivoExcel.getSheet(1);
	        
	        //	Obtiene los nombres
	        numColumnas = 13;
	        
	        // La última fila será donde escribamos el nuevo registro
	        ultimaFila = numFilas;
	        
	        tablaHermesOCR = new String[ultimaFila-1][numColumnas];
	        for(int i=0;i<ultimaFila-1;i++){
	        	for(int j=0;j<numColumnas;j++){
	        		tablaHermesOCR[i][j] = hoja.getCell(j,i+1).getContents().toString();
	        	}
	        }
	        
	        /*
	        for(int i=0;i<ultimaFila-1;i++){
	        	for(int j=0;j<numColumnas;j++){
	        		System.out.print(tablaHermesOCR[i][j] + "  ");
	        	}
	        	System.out.println();
	        }
	        */
	        
	        
	        
	        
	        
	        
	        
	        
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	
	static public void main(String args[]){
		LeerExcelAyuda leerExcel = new LeerExcelAyuda();
		leerExcel.leerAyuda("Ayuda documentos.xls");
	}

	
}
