import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class Inicio extends JFrame {

	/**
	 * @param args
	 */
	
	static String RUTA = ":/digitalización/00 documentacion/01 Escaneado";
//	static final String RUTAB = "h:/digitalización/00 documentacion/01 Escaneado";
	static String RUTAURG =":/DIGITALIZACIÓN/01 INFORMES URG (Colectiva)"; 
//	static String RUTASAL =":/digitalización/02 Salnés/01 Escaneado";
	static final String RUTASAL_DOC = ":/01 Escaneado Documentacion";
	static final String RUTASAL_URG = ":/01 Escaneado Urgencias";
	static String RUTAMENSAJES_DOC = ":/02 Mensajes Doc";
	static String RUTAMENSAJES_URG = ":/02 Mensajes Urg";
	static String RUTASAL =":/01 Escaneado Documentacion";

	static String RUTAEXP = ":/digitalización/03 EXPERIMENTAL/01 Escaneado";

//	static final String RUTAURGB ="H:/DIGITALIZACIÓN/01 INFORMES URG (Colectiva)";
	static String RUTA_NO_RECONOCIDOS = ":/digitalización/00 documentacion/10 Registrar docs";
	
	static final String rutaImagenes = "Hermes/ImagenesPdfs/";
	static public String rutaHermes = ":\\DIGITALIZACIÓN\\00 DOCUMENTACION\\99 Nombres Normalizados\\Hermes\\ImagenesPdfs";
	static public String rutaHermes_TXT = ":\\DIGITALIZACIÓN\\00 DOCUMENTACION\\99 Nombres Normalizados\\Hermes.txt";
	static public String rutaHermes_XLS = ":\\DIGITALIZACIÓN\\00 DOCUMENTACION\\99 Nombres Normalizados\\Hermes.xls";
	
	static public TreeMap<String, Indices> indiceGeneralAyuda = new TreeMap<String, Indices>();
	
	static String unidadHDD = "";
	
	static final String RUTAPC = "c:/ianus/ianus.txt"; 
	static String nombrePc;
	static boolean acrobatAntiguo = false;
	
	static String rutaFocoAcrobat = "cal\\FocoAcrobat2015.exe";
//	static String rutaFocoNHC = "cal\\FocoNHC.exe";
	static String rutaFocoAcrobatV = "cal\\FocoAcrobatV.exe";
	static String rutaFocoAcrobat2015v7 = "cal\\FocoAcrobat2015v7";
	static String rutaPreferenciasAdobe = "cal\\prefAcrobat.exe";

	static boolean menuVertical = false;
	
	static final String SEPARADOR = "Separador";
	
	static final String SEPARADOR_FUSIONAR = "Fusionar";
	
	static final String CONSENTIMIENTO = "Consentimento informado";
	static final String INCLUSION = "Folla inclusión LE";
	static final String EKG = "ECG";
	static final String ECO = "Ecografía";
	static final String ECOCARDIOGRAFIA = "Ecocardiografía";
	static final String MONITORIZACION = "Cardiotocografía";
	static final String DOC = "Documento non clasificado";
	static final String CURSOCLINICO = "Evolutivo";
	static final String CRIBADO = "Cribado xordeira";
	static final String HOSPITALIZACION = "Hospitalización";
	static final String CIA = "CIA";
	static final String INTERCONSULTA = "Interconsulta";
	public static final String VIDEONISTAGMOGRAFÍA = "Videonistagmografia";
	public static final String URPA = "Postanestesia";
	public static final String CUIDADOS_INTENSIVOS = "Evolutivo enfermaría";
	public static final String MAPA_DERMATOMAS = "Anamnese (Mapa dermatomas)";
	public static final String ENFERMERIA_QUIRURGICA = "Folla enfermaría circulante";
	public static final String ESPIROMETRIA = "Espirometría";
	public static final String DENSITOMETRIA = "Densitometría centro externo";
	public static final String ORDENES_MEDICAS = "Ordes médicas";
	public static final String REGISTRO_ANESTESIA = "Rexistro anestesia";
	public static final String INFORME_ALTA = "Informe alta";
	public static final String TEST_PSICOLOXICO = "Test psicolóxico";
	public static final String INFORME_PSICOPEDAGOXICO = "Informe psicopedagoxía centro externo";
	
    public static final String CARC = "CARC";
    public static final String PEDC = "PEDC";
	public static final String ANRC = "ANRC";
	public static final String DES = "Des";
	public static final String HOSP = "HOSP";
	public static final String URG = "URG";
	public static final String NRLC = "NRLC";
	public static final String ORLC = "ORLC";
	public static final String DIGC = "DIGC";
	
	public static final String ETMC = "ETMC";
	public static final String DERC = "DERC";
	public static final String UDOC = "UDOC";
	public static final String USMI = "USMI";
	
	public static final String CONS = "CONS";

	public static final int ECG_A5_ALTO_MIN = 311;
	public static final int ECG_A5_ALTO_MAX = 324;
	public static final int ECG_A5_ANCHO_MIN_A = 815;
	public static final int ECG_A5_ANCHO_MIN_B = 420;
	public static final int ECG_A5_ANCHO_MAX_A = 875;
	public static final int ECG_A5_ANCHO_MAX_B = 444;
	
	public static final int ECO_ALTO_MIN = 300;
	public static final int ECO_ALTO_MAX = 314;
	public static final int ECO_ANCHO_MAX = 800;

	public static String paresUsmi[][];

	//	1 Documentacion; 2 Salnes; 0 Urgencias
	static int destinoDocumentacion = 1;
	
	static JButton jBNHC = new javax.swing.JButton();
    static JButton jBServicio = new javax.swing.JButton();
    static JLabel jLServicio = new JLabel(); 
    static JButton jBNombreDoc = new javax.swing.JButton();
    static JButton jBServiciop = new javax.swing.JButton();
    static JButton jBNombreDocp = new javax.swing.JButton();
    static JButton jBNHCp = new javax.swing.JButton();
    static JButton jBDeshabilitar;
    
    static JCheckBox jCheckBox1 = new JCheckBox();
    
    static JList jLServicios = new javax.swing.JList();
    static JList jLNombresDoc = new javax.swing.JList();
    
    static LeerExcel excel;
    static ArrayList<Object> documentosServicio;
    
    static String rutaCarpetaEscaneadaUsuario = "";    //	almacena la ruta de la carpeta actual
    static ArrayList<String> listaCarpetasRegistradas = new ArrayList<String>();  // almacena las rutas de las carpetas registradas
    static String carpetaActualRevisando = "";   // almacena la carpeta que se está revisando
    
    static String rutaCompletaPdfs[];
    static ArrayList<String> carpetasAbiertas = new ArrayList<String>();
    static boolean ficherosCargados = true;
    static boolean ventanaRevisionAbierta = false;
    static boolean carpetaRecienCargada = true;

    static VentanaPrincipal ventanaPrincipal;
    static VentanaExplorador ventanaExplorador;
    static VentanaComprobar ventanaComprobacion;
    static InterfazIntroducirNHC ventanaIntroducirNHC;
    static VentanaExtraer ventanaExtraer;
    static VentanaA3 ventanaA3;
    static VentanaNombresYServicios ventanaNombresYServicios;
    static VentanaNombres ventanaNombres;
    static VentanaIntegral ventanaIntegral;
    static VentanaMicro ventanaMicro;
    
    static InterfazAyuda ventanaAyuda;
    static Visor visorAyuda;
        
    static VentanaFechas ventanaFechas;
    static boolean esperarFecha = false;
    
    static int visualizacion = 0;    	// 0 horizontal
    									// 1 vertical
    									// 2 vertical a3
     
    static int numeroPdf;
    static int tamañoCarpetaPdf;
    static String rutaDirectorio;
    static int numeroCarpeta;
    
    static Documento[] listaDocumentos;
    static ArrayList<Modelo> modelos = new ArrayList<Modelo>();
    static ArrayList<Modelo> listaCompletaModelos = new ArrayList<Modelo>();
    static Set<String> conjuntoClavesNhc = new HashSet<String>();
    static ArrayList<Integer> separadores;

    static DefaultListModel modelo;
    
    static boolean progreso = false;
    static boolean erroresAntesRegistrar = false;
    
    static Progress frame; 
    static VentanaProgreso vProgreso;
    
    static int numeroPantallas;
    static int documentacion = 1; // Documentacion 1; Urgencias 0; Salnes 2;
    static boolean A3 = false;
    static String usuario = "";
    static PreferenciasUsuario coordenadas;
    
    static String auxRutaImagen = "";				//	Para ayudar a la hora de asignar una imagen a una norma, aviso, comentario...
	
    static Utiles utiles = new Utiles();
    
    static boolean modoAdministrador = false;
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaExplorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaExplorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaExplorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaExplorador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
		*/
		
		boolean rebotado = false;
		
		if(args.length>0){
			usuario = args[0];
			destinoDocumentacion = Integer.valueOf(args[1]);

			rebotado = true;
		}
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
				
		numeroPantallas = gs.length;
		
		nombrePc = new IdentificarPc().getIdentificacion(RUTAPC);
		
	//	nombrePc = new IdentificarPcYusuario(RUTAPC).nombrePc;
		
		System.out.println("Nombre pc... " + nombrePc);
		
		if(nombrePc.toLowerCase().contains("mahc13p") 
				|| nombrePc.toLowerCase().contains("mahc35p")
				|| nombrePc.toLowerCase().contains("mahc03p") 
				|| nombrePc.toLowerCase().contains("mahc01p") 
				|| nombrePc.toLowerCase().contains("mahc04p") 
				|| nombrePc.toLowerCase().contains("mahc17p") 
				){
			
		//	acrobatAntiguo = true;
		//	rutaFocoAcrobat = "cal\\FocoAcrobat2.exe";
			rutaFocoAcrobatV = "cal\\FocoAcrobatV3.exe";
			
			
			rutaFocoAcrobat = rutaFocoAcrobat2015v7;
			
			System.out.println("Escogemos... " + rutaFocoAcrobatV);
			
		}
		else if(  	
						nombrePc.toLowerCase().contains("mahc21p")
					||  nombrePc.toLowerCase().contains("mahc33p")
					
				){

			rutaFocoAcrobat = rutaFocoAcrobat2015v7;
			
		}

//		JOptionPane.showMessageDialog(null, rutaFocoAcrobat);
		
		unidadHDD = detectaUnidadHDD();
		RUTA = unidadHDD + RUTA;
		RUTAURG = unidadHDD + RUTAURG;
		// RUTASAL = detectaUnidadSalnes() + RUTASAL;
		RUTA_NO_RECONOCIDOS = unidadHDD + RUTA_NO_RECONOCIDOS;
		
		rutaHermes = unidadHDD + rutaHermes;
		rutaHermes_TXT = unidadHDD + rutaHermes_TXT;
		rutaHermes_XLS = unidadHDD + rutaHermes_XLS;
				
		RUTAEXP = unidadHDD + RUTAEXP;
		
		excel = new LeerExcel();

		
		// JOptionPane.showMessageDialog(null, "Empezamos a leer excel documentos");
		System.out.println("Leemos documentos.xls");
		excel.getTablaDocumentos("Documentos.xls");
		// JOptionPane.showMessageDialog(null, "Empezamos a leer excel coordenadas");
		//System.out.println("Leemos coordenadas R.xls");
		excel.getPreferencias("CoordenadasR.xls");
		
		
		// Obtiene los nombres de los facultativos. Posiblemente borrar.
		excel.leerFacultativos("facSalnes.xls", 1);
		
		// Obtiene los pares valores de USMI
	    paresUsmi = excel.getParesUsmi();
		
		
//		CapturaRatonYTeclado capturaBorrar = new CapturaRatonYTeclado();
		
		
		if(!rebotado){
			 	VentanaTipoDeDocumentacion vtd =  new VentanaTipoDeDocumentacion();
			 	
			int tipoDoc = vtd.getTipoDocumentacion();
			if (tipoDoc != -1) {
				modoAdministrador = vtd.administrador;
				destinoDocumentacion = tipoDoc;

			} else {
				System.exit(0);
			}
			    
			    System.out.println("documentacion de urgencias: " + destinoDocumentacion);
			    
			    VentanaInicio dialog = new VentanaInicio(new javax.swing.JFrame(), true);
			    dialog.addWindowListener(new java.awt.event.WindowAdapter() {

			                @Override
			                public void windowClosing(java.awt.event.WindowEvent e) {
			                  System.exit(0);
			                }
			         });
			    dialog.setVisible(true);
			    	
		}
	
	 //   modelos = excel.leerModelos("DocumentosOCR.xls", documentacionDeUrgencias);
	    modelos = excel.leerModelos("Hermes.xls", destinoDocumentacion);
	    
	    
	    listaCompletaModelos = excel.leerListaTotalModelos("Hermes.xls");
		
	    /*
	    for(int i=0;i<listaCompletaModelos.size();i++){
	    	if(!listaCompletaModelos.get(i).instruccionesNHC.equals("")){
	    		conjuntoClavesNhc.add(listaCompletaModelos.get(i).instruccionesNHC);
	    	}
	    }
	    */
    
	    
	    /*
	    System.out.println("Numero de elementos del conjunto: " + conjuntoClavesNhc.size());
	    
	    Iterator<String> it = conjuntoClavesNhc.iterator();
	    while(it.hasNext()){
	    	JOptionPane.showMessageDialog(null, it.next());
	    }
	    */
	    
		// System.out.println(modelos.get(83).instruccionesNHC);
	    
/*
		frame = new Progress();
		frame.pack();
		frame.setVisible(true);
		frame.iterate();
*/
	    
	    if (usuario != ""){
        	
        	//Inicio.coordenadasVentanas.leerCoordenadasVentana("Coordenadas.xls");
        	
	    	System.out.println("Obtenemos preferencias del usuario");
	    	
           	coordenadas = new PreferenciasUsuario();
           	// numeroPantallas = coordenadas.numPantallas;
        	
           // Inicio.navegador1.frame.setBounds(Inicio.coordenadasVentanas.vPdf1);
           // Inicio.navegador1.frame.setVisible(true);
 
           	
            //ventanaE = new VentanaExplorador();
            //ventanaE.setBounds(Inicio.coordenadasVentanas.vExplorador);

           	/*
           	System.out.println("El numero de coordenadas es: " + coordenadas.coordenadas.length);
           	for(int i=0;i<coordenadas.coordenadas.length;i++){
           		System.out.println(i + " coordenadas: " + coordenadas.coordenadas[i].x + ", " +
           					coordenadas.coordenadas[i].y);
           	}
           	System.out.println();
           	*/
           	
           	ventanaExplorador = new VentanaExplorador();
           	ventanaExplorador.setBounds(Inicio.coordenadas.coordenadas[0].x,Inicio.coordenadas.coordenadas[0].y,
           			                    Inicio.coordenadas.coordenadas[1].x,Inicio.coordenadas.coordenadas[1].y);
           	
            ventanaAyuda = new InterfazAyuda();
            visorAyuda = new Visor();
            
            Point pVentanaAyuda = Inicio.ventanaAyuda.getLocation();
            Point pVisorAyuda = Inicio.visorAyuda.getLocation();
            
 /*
            System.out.println("Localización ventana ayuda x: " + pVentanaAyuda.x);
            System.out.println("Localización ventana ayuda y: " + pVentanaAyuda.y);
            System.out.println("Localización visor ayuda x: " + pVisorAyuda.x);
            System.out.println("Localización visor ayuda y: " + pVisorAyuda.y);
 */
            
            int x = pVentanaAyuda.x-250;
            int dist = x + 800;
            
    		if(Inicio.numeroPantallas == 2){
    			x += 200;
    			dist += 400;
    		}

            
            ventanaAyuda.setBounds(x,pVentanaAyuda.y,800,900);
            visorAyuda.setBounds(dist,pVentanaAyuda.y,700,900);
	    
	    }
	    
	    System.out.println("Iniciando la captura del teclado.");
	    new CapturaRatonYTeclado();
		
		
	  //  new VentanaPrincipal();
	  //  new VentanaCompacta();
		
	}

	
	public static String detectaUnidadSalnes(){
		ArrayList<String> unidades = new ArrayList<String>();
		
		String unidad = "k";
		
		File[] hdds = File.listRoots();
		for(int i=0;i<hdds.length;i++){
			unidades.add(hdds[i].getAbsolutePath().substring(0,1));
		}
		
		for(int i=0;i<unidades.size();i++){
			String posibleRuta = unidades.get(i) + RUTASAL;
			File ruta = new File(posibleRuta);
			if(ruta.exists()){
				return unidades.get(i);
			}
			
		}
		
		return unidad;
	}
	
	private static String detectaUnidadHDD(){
		
		ArrayList<String> unidades = new ArrayList<String>();
		
		File[] hdds = File.listRoots();
		for(int i=0;i<hdds.length;i++){
			unidades.add(hdds[i].getAbsolutePath().substring(0,1));
		}
		
		
		String posibleRuta = "h" + RUTA;
		File ruta = new File(posibleRuta);
		if(ruta.exists()){
			return "h";
		}
		else{
			posibleRuta = "j" + RUTA;
			ruta = new File(posibleRuta);
			if(ruta.exists())
				return "j";
			
		}

		for(int i=0;i<unidades.size();i++){
			posibleRuta = unidades.get(i) + RUTA;
			ruta = new File(posibleRuta);
			if(ruta.exists()){
				return unidades.get(i);
			}
			
		}
		
		
		
		
		JOptionPane.showMessageDialog(null, "Problemas con la unidad de disco");
		
		return null;
	}
	
	
}

class VentanaTipoDeDocumentacion{
	
	JCheckBox   modoAdmin = new JCheckBox("Administrador");
	boolean administrador = false;
	
	int getTipoDocumentacion(){
		
		int opcion = JOptionPane.showOptionDialog(null, "¿Qué documentación vas a revisar?", "Selector de documentación", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, new Object[] {"Urgencias","Documentación","Salnés Doc","Salnés Urg",modoAdmin}, "Documentación");
		/*
			1 Documentacion
			2 Salnes Doc
			3 Salnes Urg
			0 Urgencias
		}
		*/
		
		Inicio.documentacion = opcion;
		
		administrador = modoAdmin.isSelected();
		
		return opcion;
	}
	
}




/* Obsoleta */
class IdentificarPc {

	
	String getIdentificacion(String ruta){
		File f = new File(ruta);
		Scanner s;
		String pc = "NoN";
		try{
			s = new Scanner(f);
			if (s.hasNextLine()){
				 pc = s.nextLine();
				System.out.println(pc);
			}
			s.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		return pc;
	}
}	

class IdentificarPcYusuario {
	
	String nombrePc = "";
	String usuarioSesion = "";
	String rutaTrabajo = "";
	
	public IdentificarPcYusuario(String ruta){
		usuarioSesion = System.getProperty("user.name");
		try {
			nombrePc = InetAddress.getLocalHost().getHostName();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No se puede averiguar el nombre del host.");
			System.out.println("Version manual.");
			
			nombrePc = new IdentificarPc().getIdentificacion(ruta);
		}
	}
	
}	