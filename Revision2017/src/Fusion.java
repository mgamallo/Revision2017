import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;


public class Fusion {
	
	static final int MAXIMO_A_FUSIONAR = 10;
	static final String BORRAR = "-BORRAR-";

	private Documento conjuntoInicial[];
	private Documento conjuntoFinal[];
	
	public Fusion(Documento listaDocumentos[]) {
		// TODO Auto-generated constructor stub
		
		conjuntoInicial = listaDocumentos;
		
		buscaConjuntos();
		
		conjuntoFinal = devuelveListaFinal();
	}
	
	Documento[] getListadoFinal(){
		return conjuntoFinal;
	}
	
	
	boolean fusionar (ArrayList<Documento> lista){
		
		if(lista.size() > 0){
			Document documento = new Document();
			
			String pdfFusionadoFinal = lista.get(0).rutaArchivo;
			String pdfFusion = pdfFusionadoFinal + "Fus";

			try {
				PdfCopy copy = new PdfCopy(documento, new FileOutputStream(pdfFusion));
				
				documento.open();
				
				for(int i=0;i<lista.size();i++){
					PdfReader pdf = new PdfReader(lista.get(i).rutaArchivo);
					copy.addDocument(pdf);
					pdf.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			documento.close();
			
			
			for(int i=0;i<lista.size();i++){
					File fichero = new File(lista.get(i).rutaArchivo);
					fichero.delete();
			}
			
			File fichero = new File(pdfFusion);
			String aux = pdfFusion.substring(0,pdfFusion.length()-3);
			fichero.renameTo(new File(aux));
			
			
			return true;
		}

		
		return false;
	}
	
	
	void buscaConjuntos(){
		
		boolean encontradoInicio = false;
		boolean encontradoFinal = false;
		int inicio = 0;
		int fin = 0;
		
		for(int i=0;i < conjuntoInicial.length;i++ ){
			
			if(conjuntoInicial[i].nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
				
			//	JOptionPane.showMessageDialog(null, "Fusion: encontrado inicio. Pdf " + i);
				
				encontradoInicio = true;
				inicio = i;
				for(int j=i+1, z=0;j< conjuntoInicial.length && z < MAXIMO_A_FUSIONAR;j++, z++){
					if(conjuntoInicial[j].nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
						encontradoFinal = true;
						fin = j;
						i = j;
						break;
					}
				}
				
				
				if(!encontradoFinal){
					String nombreInicio = "";
					for(int j=i+1, z=0;j< conjuntoInicial.length - 1 && z < MAXIMO_A_FUSIONAR-1;j++, z++){
						if(!conjuntoInicial[j].nombreNormalizado.toLowerCase().equals("x")
								&& nombreInicio.length() == 0){
							nombreInicio = conjuntoInicial[j].nombreNormalizado;
						}
						if(nombreInicio.length() > 0){
							if(!conjuntoInicial[j].nombreNormalizado.toLowerCase().equals("x")){
								if(!conjuntoInicial[j].nombreNormalizado
										.equals(conjuntoInicial[j+1].nombreNormalizado)){
									encontradoFinal = true;
									fin = j+1;
									break;
								}
							}
						}
					}
					
				}
				
					// Si no encontramos el final, abortamos la fusion,
					//   y buscamos la siguiente  
					if(!encontradoFinal){
						encontradoInicio = false;
						inicio = 0;
						fin = 0;
					}

				
				if(encontradoFinal){
					
				//	JOptionPane.showMessageDialog(null, "Fusion: encontrado final. Pdf " + fin);
					
					ArrayList<Documento> seleccion = new ArrayList<Documento>();
					for(int x=inicio + 1; x < fin;x++){
						seleccion.add(conjuntoInicial[x]);
					}
					
					if(fusionar(seleccion)){
						
						conjuntoInicial[inicio].nhc = BORRAR;
						
						Set<String> conjuntoNHC = new HashSet<String>();
						Set<String> conjuntoNombres = new HashSet<String>();
						
						for(int x = inicio + 1;x<=fin;x++){
							
							if(!conjuntoInicial[x].nhc.toLowerCase().equals("no") 
									&& !conjuntoInicial[x].nhc.toLowerCase().equals("error")
									&& !conjuntoInicial[x].nhc.equals(Inicio.SEPARADOR_FUSIONAR)
									&& !conjuntoInicial[x].nhc.equals(Inicio.SEPARADOR)){
								
								conjuntoNHC.add(conjuntoInicial[x].nhc);
							}
							
							if(!conjuntoInicial[x].nombreNormalizado.toLowerCase().equals("x")){
								
								conjuntoNombres.add(conjuntoInicial[x].nombreNormalizado);
							}
							
							if( x > inicio + 1 && x < fin){
								conjuntoInicial[x].nhc ="-BORRAR-";
							}
							
							if(conjuntoInicial[fin].nhc.equals(Inicio.SEPARADOR_FUSIONAR)){
								conjuntoInicial[x].nhc ="-BORRAR-";
							}
						}
						
						System.out.println("Conjunto de nhcs... Tamaño: " + conjuntoNHC.size());
						Iterator<String> it = conjuntoNHC.iterator();
						int z = 0;
						while(it.hasNext()){
							
							String aux = it.next();
							
							if(z == 0){
								conjuntoInicial[inicio+1].nhc = aux;
							}
							System.out.println(aux);
						}
						
						System.out.println("Conjunto de nombres.... Tamaño: " + conjuntoNombres.size() );
						Iterator<String> itn = conjuntoNombres.iterator();
						while(itn.hasNext()){
							
							String aux = itn.next();
							
							if(z == 0){
								conjuntoInicial[inicio+1].nombreNormalizado = aux;
								conjuntoInicial[inicio+1].semaforoAmarilloNhc= true;
								conjuntoInicial[inicio+1].semaforoAmarilloNombre = true;
							}
							
							System.out.println(aux);
						}

						
		//				JOptionPane.showMessageDialog(null, "Fusionado con exito " + seleccion.size() + " pdfs.");
						
					
					}
					i= fin;
					inicio= 0;
					fin = 0;
					encontradoInicio= false;
					encontradoFinal = false;
				}

			}
			
		}
	}
	
	Documento[] devuelveListaFinal(){
		
		ArrayList<Documento> nuevaLista = new ArrayList<Documento>();
		
	//	JOptionPane.showMessageDialog(null,
	//			"Elaborando nuevo listado. Tamaño anterior " + conjuntoInicial.length);
		
		for(int i=0;i< conjuntoInicial.length;i++){
			if(!conjuntoInicial[i].nhc.equals(BORRAR)){
				nuevaLista.add(conjuntoInicial[i]);
			}
			else{
				File fichero = new File(conjuntoInicial[i].rutaArchivo);
				System.out.println("Fichero borrado: " + fichero.delete());
			}
		}
		
		Documento listaFinal[] = new Documento[nuevaLista.size()];
		
		for(int i=0;i<nuevaLista.size();i++){
			listaFinal[i] = nuevaLista.get(i);
		}
		
	//	JOptionPane.showMessageDialog(null,
	//			"Elaborado nuevo listado. Tamaño actual " + listaFinal.length);
		
		return listaFinal;
		
	}
}
