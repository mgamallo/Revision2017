

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
import java.util.Iterator;
import java.util.TreeMap;


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
