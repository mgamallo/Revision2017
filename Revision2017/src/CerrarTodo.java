import java.awt.Color;
import java.io.File;
import java.io.IOException;


public class CerrarTodo {
	
/*	public static void main(String[] args){
		CerrarTodo cl = new CerrarTodo();
		cl.close();
	}
*/	
	public void close(){

		 String cmd = "taskkill.exe /F /IM AcroRd32.exe /T";
		 String cmd2 = "taskkill.exe /F /IM Acrobat.exe /T";
		// String cmd3 = "taskkill.exe /F /IM TeclasRapidas.exe /T";
		 String cmd4 = "taskkill.exe /F /IM FocoAcrobat.exe /T";
		 String cmd5 = "taskkill.exe /F /IM FocoNHC.exe /T";
			
		 String cmd6 = "taskkill.exe /F /IM FocoAcrobat2.exe /T";
		 String cmd7 = "taskkill.exe /F /IM FocoAcrobatV.exe /T";
		 
		 String cmd8 = "taskkill.exe /F /IM FocoAcrobat2015.exe /T";
		 String cmd9 = "taskkill.exe /F /IM FocoAcrobat2015v7.exe /T";
		 
		 String cmd10 = "taskkill.exe /F /IM FocoAcrobatV2.exe /T";
		 String cmd11 = "taskkill.exe /F /IM FocoAcrobatV3.exe /T";
		 
		 Process hijo, hijo2, hijo3, hijo4, hijo6, hijo7, hijo8, hijo9, hijo10, hijo11;
		 try {
				hijo = Runtime.getRuntime().exec(cmd);
				hijo2 = Runtime.getRuntime().exec(cmd2);
				hijo3 = Runtime.getRuntime().exec(cmd4);
				hijo4 = Runtime.getRuntime().exec(cmd5);
				hijo6 = Runtime.getRuntime().exec(cmd6);
				hijo7 = Runtime.getRuntime().exec(cmd7);
				hijo8 = Runtime.getRuntime().exec(cmd8);
				hijo9 = Runtime.getRuntime().exec(cmd9);
				hijo10 = Runtime.getRuntime().exec(cmd10);
				hijo11 = Runtime.getRuntime().exec(cmd11);
				
				hijo.waitFor();
				hijo2.waitFor();
				hijo3.waitFor();
				hijo4.waitFor();
				hijo6.waitFor();
				hijo7.waitFor();
				hijo8.waitFor();
				hijo9.waitFor();
				hijo10.waitFor();
				hijo11.waitFor();
				
				Thread.sleep(500);
								
				
				if ( hijo.exitValue()== 0 && hijo2.exitValue()== 0){
					System.out.println("acrobat matado con exito");
				}else{
					System.out.println("Incapaz de matar acrobat. Exit code: " + hijo.exitValue()+"n");
				}
		 } catch (IOException e) {
			System.out.println("Incapaz de matar.");
		 } catch (InterruptedException e) {
			System.out.println("Incapaz de matar.");
		 }
		 
		 Inicio.ventanaExplorador.dispose();
		 if(Inicio.ventanaRevisionAbierta){
			 Inicio.ventanaPrincipal.dispose();
		 }

		 
		 try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 System.out.println("Inicio cierre carpetas");
		 borrarCarpetas();
		
	}
	
	public void closePdf(){
		 String cmd = "taskkill.exe /F /IM AcroRd32.exe /T";
		 String cmd2 = "taskkill.exe /F /IM Acrobat.exe /T";

		 Process hijo, hijo2;
		 try {
				hijo = Runtime.getRuntime().exec(cmd);
									
				hijo.waitFor();
				
				hijo2 = Runtime.getRuntime().exec(cmd2);
				hijo2.waitFor();
				Thread.sleep(500);
						
				if ( hijo.exitValue()== 0 ){
					System.out.println("acrobat matado con exito");
				}else{
					System.out.println("Incapaz de matar acrobat. Exit code: " + hijo.exitValue()+"n");
				}
		 } catch (IOException e) {
			System.out.println("Incapaz de matar.");
		 } catch (InterruptedException e) {
			System.out.println("Incapaz de matar.");
		 }
	}
	
	public void borrarCarpetas(){
		for(int i = 0; i < Inicio.listaCarpetasRegistradas.size();i++){
			System.out.println("Borrando carpeta " + Inicio.listaCarpetasRegistradas.get(i));
			File directorio = new File(Inicio.listaCarpetasRegistradas.get(i));
			File[] ficheros = directorio.listFiles();
			for(int j=0;j<ficheros.length;j++){
				System.out.println("Borrando fichero " + ficheros[j]);
				if(ficheros[j].delete()){
					System.out.println("Fichero borrado");
				}
				else{
					System.out.println("Fichero no borrado");
				}
			}
			directorio.delete();
		}
	}
}
