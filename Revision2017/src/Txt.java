

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;


public class Txt {
	
/*	
	static public boolean escribirIndiceTxt(TreeMap<String, Indices>  indiceGeneral){
		
		boolean correcto = true;
		
		String cadena = "";
		
		Iterator<String> it = indiceGeneral.keySet().iterator();
		while(it.hasNext()){
		  String key = (String) it.next();
		  
		  cadena = cadena + key + ";";
		  for(int i=0;i<indiceGeneral.get(key).indices.size();i++){
			  cadena = cadena + indiceGeneral.get(key).indices.get(i);
			  if (i!=indiceGeneral.get(key).indices.size()-1){
				  cadena = cadena + ";";
			  }else{
				  cadena = cadena + System.getProperty("line.separator");
			  }
		  }
		}
		
		Writer out = null;
		
		try {
			FileOutputStream fos = new FileOutputStream(Inicio.rutaHermes_TXT);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");
					
			out = new BufferedWriter(osw);
			out.write(cadena);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			correcto = false;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			correcto = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			correcto = false;
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return correcto;
	}
*/	
	
	static public TreeMap<String, Indices> leerIndiceTxt(String ruta){
		
		TreeMap<String, Indices> indiceGeneral = new TreeMap<String, Indices>();

		FileInputStream fr = null;
		
	    BufferedReader br = null;
	 
	    try {
	    	
	    	fr = new FileInputStream(ruta);
	    	
	         br = new BufferedReader(new InputStreamReader(fr,"utf-8"));
	 
	         // Lectura del fichero
	         String linea;
	         String texto = "";
	         while((linea=br.readLine())!=null){
	        	 // System.out.println(linea);
	        	 String campos[] = linea.split(";");
	        	 if(campos.length > 0){
	        		 String valor = campos[0];
	        		 Indices indice = new Indices();
	        		 for(int i=1;i<campos.length;i++){
	        			 indice.indices.add(campos[i]);
	        		 }
	        		 indiceGeneral.put(valor, indice);
	        	 }
	         }

	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	    
	    /*
	    Iterator<String> it = indiceGeneral.keySet().iterator();
		while(it.hasNext()){
		  String key = (String) it.next();
		  System.out.println("Clave: " + key + " ->"); 
		  for(int i=0;i<indiceGeneral.get(key).indices.size();i++){
			  System.out.println("   Valor: " + indiceGeneral.get(key).indices.get(i));
		  }
		  
		}
		*/
	    
	      return indiceGeneral;

	}


	//  Metodo para borrar
	static public void escribirNormasTxt(String rutaCompleta, String[] campos){
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(rutaCompleta);
            pw = new PrintWriter(fichero);


            pw.println(campos[0]);
            pw.println("@0@");
            pw.println("No");
            pw.println("@0@");
            pw.println(campos[3]);
            pw.println("@0@");
            pw.println(campos[1]);
            pw.println("@0@");
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
	
	static public void escribirError(String error){
		FileWriter fichero = null;
		PrintWriter pw = null;
		
		String carpetaLogs = "c:\\errorRevision\\";
		File log = new File(carpetaLogs);
		if(!log.exists()){
			log.mkdirs();
		}

		
		Calendar hoy = Calendar.getInstance();
		String nombreFichero = String.valueOf(hoy.get(Calendar.YEAR));
		String mes = "" + (hoy.get(Calendar.MONTH)+1);
		if(mes.length() < 2){
			mes = "0" + mes;
		}
		nombreFichero += mes;
		
		String dia = "" + hoy.get(Calendar.DAY_OF_MONTH);
		if(dia.length() <2){
			dia = "0" + dia;
		}
		nombreFichero += dia;
		
		char caracteres[] = "abcdefghijklmn".toCharArray();
		int tam = caracteres.length;
		Random ra = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<10;i++){
			sb.append(caracteres[ra.nextInt(tam)]);
		}
		
		nombreFichero += (" " + sb);
		carpetaLogs += (nombreFichero + ".txt");
		
		System.out.println(carpetaLogs);
		
		try {
			fichero = new FileWriter(carpetaLogs);
			pw = new PrintWriter(fichero);
				pw.println(error);
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null != fichero){
				try {
					fichero.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	static public void escribirListaFicheros(ArrayList<String> lista){
		FileWriter fichero = null;
		PrintWriter pw = null;
		
		
		System.out.println(lista);
		
		String carpetaLogs = "c:\\logs\\";
		File log = new File(carpetaLogs);
		if(!log.exists()){
			log.mkdirs();
		}

		String numCarpeta = "";
		if(lista.size()>0){
			int aux = lista.get(0).indexOf("$");
			if(aux != -1){
				numCarpeta = lista.get(0).substring(aux+1);
				aux = numCarpeta.indexOf(" ");
				numCarpeta = numCarpeta.substring(0, aux);
			}
		}
		
		Calendar hoy = Calendar.getInstance();
		String nombreFichero = String.valueOf(hoy.get(Calendar.YEAR));
		String mes = "" + (hoy.get(Calendar.MONTH)+1);
		if(mes.length() < 2){
			mes = "0" + mes;
		}
		nombreFichero += mes;
		
		String dia = "" + hoy.get(Calendar.DAY_OF_MONTH);
		if(dia.length() <2){
			dia = "0" + dia;
		}
		nombreFichero += dia;
		
		nombreFichero += (" " + numCarpeta + " ");
		
		char caracteres[] = "abcdefghijklmn".toCharArray();
		int tam = caracteres.length;
		Random ra = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<10;i++){
			sb.append(caracteres[ra.nextInt(tam)]);
		}
		
		nombreFichero += (" " + sb);
		carpetaLogs += (nombreFichero + ".txt");
		
		System.out.println(carpetaLogs);
		
		try {
			fichero = new FileWriter(carpetaLogs);
			pw = new PrintWriter(fichero);
			for(String pdf : lista){
				pw.println(pdf);
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(null != fichero){
				try {
					fichero.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	
	public static void main(String args[]){
		/*
		Norma norma = new Norma();
		
		norma = leerNormaTxt("C:\\Desarrollo\\git\\prometeo\\prometeo\\Prometeo\\Prometeo\\txt\\Normas\\N 0006.txt");
		
		System.out.println(norma.fecha);
		System.out.println(norma.rutaImagen);
		System.out.println(norma.servicios);
		System.out.println(norma.texto);
		 */
		/*
		ArrayList<Norma> listaNormas = new ArrayList<Norma>();
		
		listaNormas = Txt.leerNormasTxt("C:\\Desarrollo\\git\\prometeo\\prometeo\\Prometeo\\Prometeo\\txt\\Normas\\");
		
		System.out.println(listaNormas.size());
		System.out.println(listaNormas.get(6).texto);
		*/
		
		Txt.leerIndiceTxt("j" + Inicio.rutaHermes_TXT);
	}
}
