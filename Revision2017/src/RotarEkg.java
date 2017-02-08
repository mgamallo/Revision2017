import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/** 
 * 
 * @author mgamgul1
 *
 *	Clase de prueba. En sí, los métodos se aplican dentro de la clase Documento
 */
public class RotarEkg {
	
	

	public static void rotarEkg(String src, String dest){
		try {
			PdfReader reader = new PdfReader(src);
			
			for(int k = 1; k <= reader.getNumberOfPages();k++){
				for(int z=1;z <= reader.getNumberOfPages();z++){
					Rectangle formatoPagina = reader.getPageSize(z);
					int alto = (int)formatoPagina.getHeight();
					int ancho = (int) formatoPagina.getWidth();
					//	Hoja vertical
					if(alto >= ancho){
						reader.getPageN(z).put(PdfName.ROTATE, new PdfNumber(90));
					}
				}
			}
			
			PdfStamper stp = new PdfStamper(reader, new FileOutputStream(new File(dest)));
			

			stp.close();
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void rotarEkg2(String src, String dest){
		try {
			PdfReader reader = new PdfReader(src);
			
			int n = reader.getNumberOfPages();
			int rot;
			
			PdfDictionary pageDict;
			for(int i=1;i<=n;i++){
				
				
				
				
				rot = reader.getPageRotation(i);
				System.out.println("Rotación de la página " + i + " vale " + rot);
				
				Rectangle formatoPagina = reader.getPageSize(i);
				int alto = (int)formatoPagina.getHeight();
				int ancho = (int) formatoPagina.getWidth();
				
				System.out.println("Alto: " + alto + " . Ancho: " + ancho);
				
				boolean rotar = false;
				switch (rot) {
				case 0:
					rot -= 90;
					rotar = true;
					break;
				case 90:
					rotar = false;				
					break;
				case 180:
					rotar = false;					
					break;
				case 270:
					rot += 90;
					rotar = true;
					break;

				default:
					rotar = false;
					break;
				}
				
				if(rotar){
					pageDict = reader.getPageN(i);
					pageDict.put(PdfName.ROTATE, new PdfNumber(rot));
				}
				
			}
			
			PdfStamper stp = new PdfStamper(reader, new FileOutputStream(new File(dest)));
			

			stp.close();
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void rotarEkg3(String src, String dest){
		try {
			PdfReader reader = new PdfReader(src);
			
			int n = reader.getNumberOfPages();
			int rot, or;
			
			PdfDictionary pageDict;
			for(int i=1;i<=n;i++){
				
				
				
				
				rot = reader.getPageRotation(i);
				System.out.println("Rotación de la página " + i + " vale " + rot);
				
				Rectangle formatoPagina = reader.getPageSize(i);
				int alto = (int)formatoPagina.getHeight();
				int ancho = (int) formatoPagina.getWidth();
				
				System.out.println("Alto: " + alto + " . Ancho: " + ancho);
				
				boolean rotar = false;
				switch (rot) {
				case 0:
					rot -= 90;
					rotar = true;
					break;
				case 90:
					rotar = false;				
					break;
				case 180:
					rotar = false;					
					break;
				case 270:
					rot += 90;
					rotar = true;
					break;

				default:
					rotar = false;
					break;
				}
				

				pageDict = reader.getPageN(i);
				PdfNumber orientacion = PdfPage.PORTRAIT;
				pageDict.put(PdfName.ROTATE, orientacion);
				
				pageDict = reader.getPageN(i);
				
				System.out.println("         " + "Rotado " + reader.getPageRotation(i));
				
				if(alto > ancho){
					pageDict.put(PdfName.ROTATE, new PdfNumber(90));
				}
				
			}
			
			PdfStamper stp = new PdfStamper(reader, new FileOutputStream(new File(dest)));
			

			stp.close();
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	static public void main(String args[]){

	//	RotarEkg.rotarEkg2("originalcardioa.pdf", "originalcardiob.pdf");
	//	RotarEkg.rotarEkg3("fuionado.pdf", "originalcardiod.pdf");
		RotarEkg.rotarEkg3("fusionado.pdf", "originalcardioe.pdf");
		System.out.println("Terminado");
	}
}
