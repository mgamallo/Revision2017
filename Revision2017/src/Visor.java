import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;


public class Visor extends JDialog{

	/**
	 * @param args
	 */
	JLabel nombreDocLabel = new JLabel();
	JButton botonI = new JButton("<");
	JButton botonD = new JButton(">");
	JLabel fotoLabel = new JLabel();
	JScrollPane scroll = new JScrollPane();
	ImageIcon imagen;
	
	JLabel obsvLabel = new JLabel("Observaciones:");
	JLabel contObsvLabel = new JLabel("");
	
	ArrayList<String> rutasJpgs = new ArrayList<String>();
	ArrayList<String> nombresDocumentos = new ArrayList<String>();
	ArrayList<String> servicios = new ArrayList<String>();
	ArrayList<String> observaciones = new ArrayList<String>();
	
	URL urlDeLaImagen;
	
	int fotoVisible = 0;
	
	Visor(String nombreDocumento, String nombreJpg){
		
		if(Inicio.numeroPantallas == 2){
			this.setLocation(1100, 10);
		}
		else{
			this.setLocation(1100, 10);
		}
		
		setSize(700,900);
		JPanel panelUp = new JPanel();
		JPanel panelDw = new JPanel();
		
		this.setModal(false);

		

		imagen = new ImageIcon( "Imagenes\\600x800\\" + nombreJpg + ".jpg");
		fotoLabel.setIcon(imagen);

		
	    nombreDocLabel.setText(nombreDocumento);
	    nombreDocLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    nombreDocLabel.setFont(new Font("TimesRoman",Font.BOLD,30));
	    nombreDocLabel.setForeground(Color.red);
	    
	    fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    scroll.setViewportView(fotoLabel);
	    
	    panelUp.setBackground(Color.PINK);
		panelUp.setLayout(new BorderLayout());
		panelUp.add(scroll,BorderLayout.CENTER);
		panelUp.add(nombreDocLabel,BorderLayout.NORTH);
		
		
		setLayout(new BorderLayout());
		add(panelUp,BorderLayout.CENTER);
		add(panelDw,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
		
	}
	
	Visor(){
		
		setSize(700,900);
		
		JPanel panelUp = new JPanel();
		JPanel panelDw = new JPanel();
		
	    nombreDocLabel.setText("Nombre del documento");
	    nombreDocLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    nombreDocLabel.setFont(new Font("TimesRoman",Font.BOLD,30));
	    nombreDocLabel.setForeground(Color.red);
	    
	    fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    scroll.setViewportView(fotoLabel);
	    
		panelUp.setLayout(new BorderLayout());
		panelUp.add(botonI,BorderLayout.WEST);
		panelUp.add(scroll,BorderLayout.CENTER);
		panelUp.add(botonD,BorderLayout.EAST);
		panelUp.add(nombreDocLabel,BorderLayout.NORTH);
		
		panelDw.setLayout(new BorderLayout());
		panelDw.add(obsvLabel,BorderLayout.WEST);
		panelDw.add(contObsvLabel,BorderLayout.CENTER);
		
		obsvLabel.setForeground(Color.black);
		obsvLabel.setFont(new Font("TimesRoman",Font.BOLD,20));
		
		contObsvLabel.setText("");
		contObsvLabel.setForeground(Color.red);
		contObsvLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contObsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,25));
		obsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,20));
		
	    botonD.addActionListener(new ActionListener(){
	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		if(fotoVisible < rutasJpgs.size()-1){
	    			fotoVisible++;
	    			/*
	        		imagen = new ImageIcon("Imagenes\\600x800\\" + rutasJpgs.get(fotoVisible));
	        		*/
	    			
	    			imagen = rescaleImage(new File(rutasJpgs.get(fotoVisible)), 600, 800);
	    			
		    		imagen.getImage().flush();
		    		fotoLabel.setIcon(imagen);
		    		nombreDocLabel.setText(nombresDocumentos.get(fotoVisible));
		    		contObsvLabel.setText(servicios.get(fotoVisible) + ". " + observaciones.get(fotoVisible));
	    		}
	    	}
	    });
		
	    botonI.addActionListener(new ActionListener(){
	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		if(fotoVisible > 0){
	    			fotoVisible--;
	        		//imagen = new ImageIcon("Imagenes\\600x800\\" + rutasJpgs.get(fotoVisible));
		    		
	    			imagen = rescaleImage(new File(rutasJpgs.get(fotoVisible)), 600, 800);
	    			
	    			imagen.getImage().flush();
		    		fotoLabel.setIcon(imagen);
		    		nombreDocLabel.setText(nombresDocumentos.get(fotoVisible));
		    		contObsvLabel.setText(servicios.get(fotoVisible) + ". " + observaciones.get(fotoVisible));

	    		}
	    	}
	    });
		
		
		setLayout(new BorderLayout());
		add(panelUp,BorderLayout.CENTER);
		add(panelDw,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
	}
	
	
	Visor(final ArrayList<String> nombresDocumentos, final ArrayList<String> rutasJpgs,final int fotoVisionar, final ArrayList<String> observaciones, ImageIcon imagenSel){
		
		if(Inicio.numeroPantallas == 2){
			this.setLocation(1100, 10);
		}
		else{
			this.setLocation(1100, 10);
		}
		
		setSize(700,900);
		JPanel panelUp = new JPanel();
		JPanel panelDw = new JPanel();
		
		fotoVisible = fotoVisionar;
		
	//	urlDeLaImagen = this.getClass().getResource("Imagenes\\600x800\\" + rutasJpgs.get(fotoVisionar));
		imagen = imagenSel;
	//	imagen.getImage().flush();
		fotoLabel.setIcon(imagen);
		
	    botonD.addActionListener(new ActionListener(){
	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		if(fotoVisible < rutasJpgs.size()-1){
	    			fotoVisible++;
	    			/*
	        		imagen = new ImageIcon("Imagenes\\600x800\\" + rutasJpgs.get(fotoVisible));
	        		*/
	    			
	    			imagen = rescaleImage(new File(rutasJpgs.get(fotoVisible)), 600, 800);
	    			
		    		imagen.getImage().flush();
		    		fotoLabel.setIcon(imagen);
		    		nombreDocLabel.setText(nombresDocumentos.get(fotoVisible));
		    		contObsvLabel.setText(observaciones.get(fotoVisible));
		    		contObsvLabel.setText(servicios.get(fotoVisible) + ". " + observaciones.get(fotoVisible));

	    		}
	    	}
	    });
		
	    botonI.addActionListener(new ActionListener(){
	    	public void actionPerformed(java.awt.event.ActionEvent evt) {
	    		if(fotoVisible > 0){
	    			fotoVisible--;
	        		//imagen = new ImageIcon("Imagenes\\600x800\\" + rutasJpgs.get(fotoVisible));
		    		
	    			imagen = rescaleImage(new File(rutasJpgs.get(fotoVisible)), 600, 800);
	    			
	    			imagen.getImage().flush();
		    		fotoLabel.setIcon(imagen);
		    		nombreDocLabel.setText(nombresDocumentos.get(fotoVisible));
		    		contObsvLabel.setText(observaciones.get(fotoVisible));
		    		contObsvLabel.setText(servicios.get(fotoVisible) + ". " + observaciones.get(fotoVisible));

	    		}
	    	}
	    });
		
	    nombreDocLabel.setText(nombresDocumentos.get(fotoVisible));
	    nombreDocLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    nombreDocLabel.setFont(new Font("TimesRoman",Font.BOLD,30));
	    nombreDocLabel.setForeground(Color.red);
	    
	    fotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    scroll.setViewportView(fotoLabel);
	    
		panelUp.setLayout(new BorderLayout());
		panelUp.add(botonI,BorderLayout.WEST);
		panelUp.add(scroll,BorderLayout.CENTER);
		panelUp.add(botonD,BorderLayout.EAST);
		panelUp.add(nombreDocLabel,BorderLayout.NORTH);
		
		panelDw.setLayout(new BorderLayout());
		panelDw.add(obsvLabel,BorderLayout.WEST);
		panelDw.add(contObsvLabel,BorderLayout.CENTER);
		
		obsvLabel.setForeground(Color.black);
		obsvLabel.setFont(new Font("TimesRoman",Font.BOLD,20));
		
		contObsvLabel.setText(servicios.get(fotoVisible) + ". " + observaciones.get(fotoVisible));
		contObsvLabel.setForeground(Color.red);
		contObsvLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contObsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,25));
		obsvLabel.setFont(new Font("TimesRoman",Font.PLAIN,20));
		
		setLayout(new BorderLayout());
		add(panelUp,BorderLayout.CENTER);
		add(panelDw,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(false);
		
	}
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Visor v = new Visor();
	}
*/
	public void setImagen
		(final ArrayList<String> nombresDocumentos, final ArrayList<String> rutasJpgs
				,final int fotoVisionar,final ArrayList<String> servicios, final ArrayList<String> observaciones, ImageIcon imagenSel){
		
		this.nombresDocumentos = nombresDocumentos;
		this.rutasJpgs = rutasJpgs;
		this.servicios = servicios;
		this.observaciones = observaciones;
		
		fotoVisible = fotoVisionar;
		imagen = imagenSel;
		fotoLabel.setIcon(imagen);

		this.contObsvLabel.setText(this.servicios.get(fotoVisible) + ". " + this.observaciones.get(fotoVisionar));
		this.nombreDocLabel.setText(this.nombresDocumentos.get(fotoVisionar));
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
}
