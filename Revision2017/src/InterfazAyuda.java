import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;




public class InterfazAyuda extends JDialog{

	/**
	 * @param args
	 */
	
	LeerExcelAyuda leerExcel;	
	String tablaAyuda[][];
	
	
	JComboBox comboMeta = new JComboBox();
	// JComboBox comboColor = new JComboBox();
	// JComboBox comboGrafico = new JComboBox();
	// JTextField texto = new JTextField(15);
	JButton botonBuscar = new JButton("Buscar");
	JButton botonSalir = new JButton("Salir");
	
	JLabel obsvLabel = new JLabel("Observaciones:");
	JLabel contObsvLabel = new JLabel();
	
	
	JButton JBmodificar = new JButton("Modificar");
    
	MyTableModel modelo;
	
	ArrayList<String> nombres = new ArrayList<String>();
	ArrayList<String> rutaJpgs = new ArrayList<String>();
	ArrayList<String> observaciones = new ArrayList<String>();
	ArrayList<String> servicios = new ArrayList<String>();

	Object[] filaObjetos = new Object[]{"1","2","3"};

	Object[][] imagenes;
	
	InterfazAyuda(){
		setTitle("Visor de metaDatos");
		setModal(false);
		
			
		leerExcel = new LeerExcelAyuda();
    	leerExcel.leerAyuda(Inicio.rutaHermes_XLS);
		
    	tablaAyuda = leerExcel.getTablaHermesAyuda();

	    comboMeta.setBackground(new java.awt.Color(255, 204, 204));
	    comboMeta.setMaximumRowCount(20);
	    comboMeta.setModel(this.listaMeta());

	    comboMeta.setSelectedIndex(0);
	    comboMeta.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {

				}
				
	        });
		
	    
	//    comboColor.addItem("Color");
	/*    
	    comboColor.setModel(this.listaColor());
	    comboColor.setMaximumRowCount(15);
	    comboColor.setSelectedIndex(0);
	    comboColor.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String[] docmetaDatos;
            	docmetaDatos = Inicio.excel.getDocServicioVisor(comboMeta.getSelectedItem().toString());

            	//	Obtiene el nombre de los Documentos
            	nombres = this.getDocumentosJpg();
            	int tamArray = nombres.size();

    	
            	//	Obtiene la ruta de las imagenes
            	rutaJpgs = this.getRutaJpg();
            	tamArray = rutaJpgs.size();
            	
            	//	Obtiene las observaciones de cada documento
            	observaciones = this.getObservaciones();
            	          	
            	
            	int filas=0;
            	int numFotos = tamArray;
            	if(numFotos % 3 == 0){
            		filas = numFotos/3;
            	}else{
            		filas = numFotos/3 +1;
            	}
            	
            	Object[][] objetosM =new Object[filas][3];
            	Object[][] imagenesR = new Object[filas][3];
            	int fil=0;
            	int columnas=0;
            	int aux=0;
            	
            	for(int i=0;i<imagenesR.length;i++)
            		for(int j=0;j<3;j++)
            			imagenesR[i][j]="";
            	
            	while(aux < numFotos){
            		if(columnas ==3){
            			columnas = 0;
            			fil++;
            		}
            		objetosM[fil][columnas]= rutaJpgs.get(aux);
            		imagenesR[fil][columnas]= crearImagen(objetosM[fil][columnas].toString());
            		aux++;
            		columnas++;
            	}
            	
            	
   	
    			filas = modelo.getRowCount();
				for(int i=0;i<filas;i++){
					modelo.removeRow(0);
				}

				filas = imagenesR.length;

				aux=0;
				int conteo=0;
				Object[] v = new Object[3];
				for(int i=0;i<filas;i++){
					while(aux < 3 ){
						if(conteo<numFotos){
							v[aux] = new Object();
							v[aux] = imagenesR[i][aux];
							aux++;
							conteo++;
						}
						else{
							v[aux] = "";
							aux++;
						}
					}
					aux = 0;	
					modelo.addRow(v);	
				}
            }
            
        	//	Método para cargar la lista de nombres de los documentos que contienen el color
            private ArrayList<String> getDocumentosJpg() {
				ArrayList<String> listaNombreDocumentos = new ArrayList<String>();
				int numFilas = Inicio.excel.tablaVisor.length;
				for(int i=1;i<numFilas;i++){
						if(Inicio.excel.tablaVisor[i][3].toString().contains(comboColor.getSelectedItem().toString())){
							listaNombreDocumentos.add(Inicio.excel.tablaVisor[i][1].toString());
						}
				}
				return listaNombreDocumentos;
			}

	
        	
			//	Método para cargar la lista de rutas de los jpg que tienen el color
			private ArrayList<String> getRutaJpg() {
				ArrayList<String> listaRuta = new ArrayList<String>();
				int numFilas = Inicio.excel.tablaVisor.length;

				for(int i=1;i<numFilas;i++){
						if(Inicio.excel.tablaVisor[i][3].toString().contains(comboColor.getSelectedItem().toString())){
							listaRuta.add(Inicio.excel.tablaVisor[i][0].toString() + ".jpg");
						}
				}
				return listaRuta;
			}
			
			//	Método para cargar la lista de observaciones de los documentos
			private ArrayList<String> getObservaciones() {
				ArrayList<String> listaObs = new ArrayList<String>();

				int numFilas = Inicio.excel.tablaVisor.length;
				for(int i=1;i<numFilas;i++){
					if(Inicio.excel.tablaVisor[i][3].toString().contains(comboColor.getSelectedItem().toString())){
						listaObs.add(Inicio.excel.tablaVisor[i][5].toString());
						
					}
				}
				
				return listaObs;
			}
			
	    });
	    
	    */
	    /*
	    comboGrafico.setModel(this.listaApariencia());
	    comboGrafico.setMaximumRowCount(10);
	    comboGrafico.setSelectedIndex(0);
	    comboGrafico.addActionListener(new ActionListener(){
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	String[] docmetaDatos;
	            	docmetaDatos = Inicio.excel.getDocServicioVisor(comboMeta.getSelectedItem().toString());

	            	//	Obtiene el nombre de los Documentos
	            	nombres = this.getDocumentosJpg();
	            	int tamArray = nombres.size();

	    	
	            	//	Obtiene la ruta de las imagenes
	            	rutaJpgs = this.getRutaJpg();
	            	tamArray = rutaJpgs.size();

	            	//	Obtiene las observaciones de cada documento
	            	observaciones = this.getObservaciones();          	
	            	
	            	int filas=0;
	            	int numFotos = tamArray;
	            	if(numFotos % 3 == 0){
	            		filas = numFotos/3;
	            	}else{
	            		filas = numFotos/3 +1;
	            	}
	            	
	            	Object[][] objetosM =new Object[filas][3];
	            	Object[][] imagenesR = new Object[filas][3];
	            	int fil=0;
	            	int columnas=0;
	            	int aux=0;
	            	
	            	for(int i=0;i<imagenesR.length;i++)
	            		for(int j=0;j<3;j++)
	            			imagenesR[i][j]="";
	            	
	            	while(aux < numFotos){
	            		if(columnas ==3){
	            			columnas = 0;
	            			fil++;
	            		}
	            		objetosM[fil][columnas]= rutaJpgs.get(aux);
	            		imagenesR[fil][columnas]= crearImagen(objetosM[fil][columnas].toString());
	            		aux++;
	            		columnas++;
	            	}

	    			filas = modelo.getRowCount();
					for(int i=0;i<filas;i++){
						modelo.removeRow(0);
					}

					filas = imagenesR.length;

					aux=0;
					int conteo=0;
					Object[] v = new Object[3];
					for(int i=0;i<filas;i++){
						while(aux < 3 ){
							if(conteo<numFotos){
								v[aux] = new Object();
								v[aux] = imagenesR[i][aux];
								aux++;
								conteo++;
							}
							else{
								v[aux] = "";
								aux++;
							}
						}
						aux = 0;	
						modelo.addRow(v);	
					}
	            }
	            
	        	//	Método para cargar la lista de nombres de los documentos que contienen Graficos o imagenes
	            private ArrayList<String> getDocumentosJpg() {
					ArrayList<String> listaNombreDocumentos = new ArrayList<String>();
					int numFilas = Inicio.excel.tablaVisor.length;
					for(int i=1;i<numFilas;i++){
							if(Inicio.excel.tablaVisor[i][4].toString().contains(comboGrafico.getSelectedItem().toString())){
								listaNombreDocumentos.add(Inicio.excel.tablaVisor[i][1].toString());
							}
					}
					return listaNombreDocumentos;
				}

		
	        	
				//	Método para cargar la lista de rutas de los jpg que tienen graficos o imagenes
				private ArrayList<String> getRutaJpg() {
					ArrayList<String> listaRuta = new ArrayList<String>();
					int numFilas = Inicio.excel.tablaVisor.length;

					for(int i=1;i<numFilas;i++){
							if(Inicio.excel.tablaVisor[i][4].toString().contains(comboGrafico.getSelectedItem().toString())){
								listaRuta.add(Inicio.excel.tablaVisor[i][0].toString() + ".jpg");
							}
					}
					return listaRuta;
				}
				
				//	Método para cargar la lista de observaciones de los documentos
				private ArrayList<String> getObservaciones() {
					ArrayList<String> listaObs = new ArrayList<String>();

					int numFilas = Inicio.excel.tablaVisor.length;
					for(int i=1;i<numFilas;i++){
						if(Inicio.excel.tablaVisor[i][4].toString().contains(comboGrafico.getSelectedItem().toString())){
							listaObs.add(Inicio.excel.tablaVisor[i][5].toString());
							
						}
					}
					
					return listaObs;
				}
	              	
	        });

	    */
	    
	    botonBuscar.addActionListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	buscar(comboMeta.getSelectedItem().toString());
            }
              	
        });

	    
	    botonSalir.addActionListener(new ActionListener(){
	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		// buscar("");
	    		comboMeta.setSelectedIndex(0);
	    		while(modelo.getRowCount() != 0){
	    			modelo.removeRow(0);
	    		}
	    		
	    		Inicio.utiles.habilitarTeclas(Inicio.jBDeshabilitar.getText(),Inicio.visualizacion);
	    		Inicio.visorAyuda.setVisible(false);
	    		setVisible(false);

	    	}
	    });
	    
	    
	    modelo = new MyTableModel(imagenes,filaObjetos);

	
		JTable tabla = new JTable(modelo);
		tabla.setRowHeight(380);		
		tabla.setTableHeader(null);
		tabla.setCellSelectionEnabled(true);
		tabla.setEnabled(false);
		tabla.setBackground(Color.black);
		
		JScrollPane scroll = new JScrollPane(tabla);
		
		EventoMouseClicked(tabla);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JPanel panelSuperior = new JPanel();
	//	panelSuperior.setLayout();

		/*
		//	Si se presiona Enter se ejecuta el método buscar metadato
    	texto.addActionListener(new ActionListener(){
 			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				buscar();
			}
    	});
		*/
		
		panelSuperior.add(comboMeta);
	//	panelSuperior.add(comboGrafico);
	//	panelSuperior.add(comboColor);
	//	panelSuperior.add(texto);
		panelSuperior.add(botonBuscar);
		panelSuperior.add(botonSalir);
		
		/*
		texto.setFont(new Font("Serif",Font.BOLD,18));
		texto.setForeground(Color.blue);
		texto.setEditable(true);
		*/
		
		JPanel panelInferior = new JPanel();
		panelInferior.setLayout(new BorderLayout());
		
		JBmodificar.setEnabled(false);
		JBmodificar.setVisible(false);
		JBmodificar.setBackground(Color.green);
		panelInferior.add(obsvLabel,BorderLayout.WEST);
		panelInferior.add(contObsvLabel,BorderLayout.SOUTH);
		panelInferior.add(JBmodificar,BorderLayout.EAST);
		
		
		JBmodificar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//new VentanaModificarDocumentos("Modelo");
			}
			
		});
		
		obsvLabel.setForeground(Color.black);
		obsvLabel.setFont(new Font("TimesRoman",Font.BOLD,20));
		
	//	contObsvLabel.setText(observaciones.get(fotoVisible));
		contObsvLabel.setForeground(Color.red);
		contObsvLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contObsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,25));
		obsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,20));


		panel.add(panelSuperior,BorderLayout.NORTH);
		panel.add(scroll,BorderLayout.CENTER);
		panel.add(panelInferior,BorderLayout.SOUTH);
		setContentPane(panel);

		setSize(800,900);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	//	pack();
		this.setLocationRelativeTo(null);
		
		setVisible(false);
		// texto.requestFocus();
	}
	
	
	public void EventoMouseClicked(final JTable tabla){
		tabla.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mousePressed(MouseEvent e){
				
				int fil = tabla.rowAtPoint(e.getPoint());
				int column = tabla.columnAtPoint(e.getPoint());
				
				/*
				if(e.getClickCount() == 2){
					Inicio.visorAyuda = new Visor(nombres,rutaJpgs,fil*3+column,observaciones, rescaleImage(new File(rutaJpgs.get(fil*3+column)), 600, 800));
					System.out.println(nombres.get(fil*3+column));
				}
				*/
				// else{
				
				//	texto.setText("     " + nombres.get(fil*3 + column));
					Inicio.visorAyuda.setImagen(nombres, rutaJpgs, fil*3+column, servicios, observaciones, rescaleImage(new File(rutaJpgs.get(fil*3+column)), 600, 800));
					Inicio.visorAyuda.setVisible(true);
					
					//= new Visor(nombres,rutaJpgs,fil*3+column,observaciones, rescaleImage(new File(rutaJpgs.get(fil*3+column)), 600, 800));
					contObsvLabel.setText(servicios.get(fil*3 + column) + ". " + 
											observaciones.get(fil*3 + column));
				//}
				
				Inicio.auxRutaImagen = rutaJpgs.get(fil*3 + column);
			}

		});
	}	
	
	
	public ImageIcon crearImagen(String ruta){
		BufferedImage miImagen;
		try{
			String rutaCompleta = /* "Imagenes\\250x350\\"  + */ ruta;
			miImagen = ImageIO.read(new File(rutaCompleta));
			
			return rescaleImage(new File(rutaCompleta),250,350);		
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}	
	
	
	public ImageIcon rescaleImage(File source, int maxWidth, int maxHeight){
		
		//int maxHeight = 350, maxWidth = 250;
	    int newHeight = 0, newWidth = 0;        // Variables for the new height and width
	    int priorHeight = 0, priorWidth = 0;
	    BufferedImage image = null;
	    ImageIcon sizeImage;

	    try {
	            image = ImageIO.read(source);        // get the image
	    } catch (Exception e) {

	            e.printStackTrace();
	            System.out.println("Picture upload attempted & failed");
	    }

	    sizeImage = new ImageIcon(image);

	    if(sizeImage != null)
	    {
	        priorHeight = sizeImage.getIconHeight(); 
	        priorWidth = sizeImage.getIconWidth();
	    }

	    // Calculate the correct new height and width
	    if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
	    {
	        newHeight = maxHeight;
	        newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
	    }
	    else 
	    {
	        newWidth = maxWidth;
	        newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
	    }


	    // Resize the image

	    // 1. Create a new Buffered Image and Graphic2D object
	    BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    // 2. Use the Graphic object to draw a new image to the image in the buffer
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(image, 0, 0, newWidth, newHeight, null);
	    g2.dispose();

	    // 3. Convert the buffered image into an ImageIcon for return
	    return (new ImageIcon(resizedImg));
	}

	
	
	class MyTableModel extends DefaultTableModel{
		public MyTableModel(Object[][] data, Object[] columnNames){
			super(data, columnNames);
		}
		
		 
		  @Override
		  public Class<?> getColumnClass(int columnIndex) {
		   Class<?> clazz = Object.class;
		   Object aux = getValueAt(0, columnIndex);
		   if (aux != null) {
		    clazz = aux.getClass();
		   }
		 
		   return clazz;
		  }
	}
	
	
	
	//	Método para cargar la lista de documentos con metaDatos
	DefaultComboBoxModel listaMeta(){
		
		
		Inicio.indiceGeneralAyuda = Txt.leerIndiceTxt(Inicio.rutaHermes_TXT);
		
		DefaultComboBoxModel metaDatosDCBM = new DefaultComboBoxModel();
		metaDatosDCBM.addElement("");
		
		Iterator<String> it = Inicio.indiceGeneralAyuda.keySet().iterator();
		while(it.hasNext()){
		  String key = (String) it.next();
		  /*
		  System.out.println("Clave: " + key + " ->"); 
		  for(int i=0;i<Inicio.indiceGeneralAyuda.get(key).indices.size();i++){
			  System.out.println("   Valor: " + Inicio.indiceGeneralAyuda.get(key).indices.get(i));
		  }
		  */
		  metaDatosDCBM.addElement(key);
		}
	
		
		return metaDatosDCBM;
	}
	
	void buscar(String nombreABuscar){
		
		if(!nombreABuscar.equals("")){
			int tamaño = Inicio.indiceGeneralAyuda.get(nombreABuscar).indices.size();
			
			ArrayList<RegistroAyuda> registrosEncontrados = new ArrayList<RegistroAyuda>();
			
			for(int i=0;i<tamaño;i++){
				int fila = Integer.valueOf(Inicio.indiceGeneralAyuda.get(nombreABuscar).indices.get(i)) - 1;
				System.out.println(Inicio.indiceGeneralAyuda.get(nombreABuscar).indices.get(i));
				registrosEncontrados
					.add(new RegistroAyuda(tablaAyuda[fila][0], tablaAyuda[fila][1], tablaAyuda[fila][9], tablaAyuda[fila][10]));
			}
			
			for(int i=0;i<tamaño;i++){
				
				System.out.println("*****************************************************");
				System.out.println(registrosEncontrados.get(i).getNombreDocumento());
				System.out.println(registrosEncontrados.get(i).getRutaImagen());
				System.out.println(registrosEncontrados.get(i).getServicios());
				System.out.println(registrosEncontrados.get(i).getObservaciones());
				System.out.println("*****************************************************");
			}
			
			String[] docmetaDatos;
	    	// docmetaDatos = Inicio.excel.getDocServicioVisor(comboMeta.getSelectedItem().toString());

	    	// String nombreABuscar = texto.getText();
	    	
	    	if(!nombreABuscar.isEmpty()){
	        	
	        	//	Obtiene el nombre de los Documentos
	        	nombres = this.getDocumentosJpg(registrosEncontrados);
	        	int tamArray = nombres.size();
		
	        	//	Obtiene la ruta de las imagenes
	        	rutaJpgs = this.getRutaJpg(registrosEncontrados);
	        	
	        	//  Obtiene los servicios de cada documento
	        	servicios = this.getServicios(registrosEncontrados);
	        	
	        	//	Obtiene las observaciones de cada documento
	        	observaciones = this.getObservaciones(registrosEncontrados);      
	        	
	        	tamArray = rutaJpgs.size();
	        	
	        	if(tamArray == 0){
	        		JOptionPane.showMessageDialog(null, "No se ha encontrado ninguna coincidencia");
	        	}
	        	else{
	        		

	            	int filas=0;
	            	int numFotos = tamArray;
	            	if(numFotos % 3 == 0){
	            		filas = numFotos/3;
	            	}else{
	            		filas = numFotos/3 +1;
	            	}
	            	
	            	Object[][] objetosM =new Object[filas][3];
	            	Object[][] imagenesR = new Object[filas][3];
	            	int fil=0;
	            	int columnas=0;
	            	int aux=0;
	            	
	            	for(int i=0;i<imagenesR.length;i++)
	            		for(int j=0;j<3;j++)
	            			imagenesR[i][j]="";
	            	
	            	while(aux < numFotos){
	            		if(columnas ==3){
	            			columnas = 0;
	            			fil++;
	            		}
	            		objetosM[fil][columnas]= rutaJpgs.get(aux);
	            		imagenesR[fil][columnas]= crearImagen(objetosM[fil][columnas].toString());
	            		aux++;
	            		columnas++;
	            	}
	            	
	    			filas = modelo.getRowCount();
					for(int i=0;i<filas;i++){
						modelo.removeRow(0);
					}

					filas = imagenesR.length;

					aux=0;
					int conteo=0;
					Object[] v = new Object[3];
					for(int i=0;i<filas;i++){
						while(aux < 3 ){
							if(conteo<numFotos){
								v[aux] = new Object();
								v[aux] = imagenesR[i][aux];
								aux++;
								conteo++;
							}
							else{
								v[aux] = "";
								aux++;
							}
						}
						aux = 0;	
						modelo.addRow(v);	
					}
	        	}
	    	}
		}

    }
    
	//	Método para cargar la lista de nombres de los documentos que contienen el metadato
    private ArrayList<String> getDocumentosJpg(ArrayList<RegistroAyuda> registros) {
		ArrayList<String> listaNombreDocumentos = new ArrayList<String>();
		int numFilas = registros.size();
		for(int i=0;i<numFilas;i++){
			listaNombreDocumentos.add(registros.get(i).getNombreDocumento());
		}
		
		return listaNombreDocumentos;
	}


	
	//	Método para cargar la lista de rutas de los jpg que tienen el metadato
	private ArrayList<String> getRutaJpg(ArrayList<RegistroAyuda> registros) {
		
		ArrayList<String> listaRuta = new ArrayList<String>();
		int numFilas = registros.size();
		for(int i=0;i<numFilas;i++){
			listaRuta.add(Inicio.rutaHermes + "\\"+ registros.get(i).getRutaImagen() + ".jpg");
			System.out.println(listaRuta.get(i));
		}
		
		return listaRuta;

	}
	
	//	Método para cargar la lista de los servicios de cada documento
	private ArrayList<String> getServicios(ArrayList<RegistroAyuda> registros) {
		
		ArrayList<String> listaServicios = new ArrayList<String>();
		int numFilas = registros.size();
		for(int i=0;i<numFilas;i++){
			listaServicios.add(registros.get(i).getServicios());
		}
		
		return listaServicios;

	}	
	
	
	//	Método para cargar la lista de observaciones de los documentos
	private ArrayList<String> getObservaciones(ArrayList<RegistroAyuda> registros) {
		ArrayList<String> listaObs = new ArrayList<String>();
		int numFilas = registros.size();
		for(int i=0;i<numFilas;i++){
			listaObs.add(registros.get(i).getObservaciones());
		}
		
		return listaObs;
	}
	
	public static void main(String args[]){
		new InterfazAyuda();
	}
}
