

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

public class CopiarFichero {

	static public boolean copiar(String origen, String destino){
		
		try {
			File fOrigen = new File(origen);
			File fDestino = new File(destino);
			
			System.out.println("-----------");
			System.out.println(fOrigen.getAbsolutePath());
			System.out.println(fDestino.getAbsolutePath());
			System.out.println("-----------");
			
			FileInputStream in = new FileInputStream(fOrigen);
			FileOutputStream out = new FileOutputStream(fDestino);
			
			byte[] buf = new byte[1024];
			int len;
			
			while((len = in.read(buf)) > 0){
				out.write(buf,0,len);
			}
			
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al copiar el archivo");
			return false;
		}
		
		return true;
	}
}
