package hw;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;

public class MillkyWay extends JApplet implements ActionListener{
	
	public JButton  go;
 	public JTextField input;
	public int numOvals = 0;
	Random rand;
	
	public MillkyWay(){
		super();
	}
		
	public void init() {
		rand = new Random ();
		this.getContentPane().setBackground(Color.white); 
		this.setLayout( new FlowLayout( ) );
  	
    	setupButtons();

    	input = new JTextField( 10);  
		this.getContentPane().add( input );		
	}
	
	public void setupButtons(){		
		this.go = new JButton( " Go!" );
		this.go.addActionListener( this );
		this.getContentPane().add( go );		
	}
	
	public void actionPerformed( ActionEvent ae ){	
		this.numOvals = Integer.parseInt(input.getText());
		this.repaint();	
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (int i=0; i < this.numOvals; i++){
			stars(g, rand.nextInt(300), rand.nextInt(300));
		}
	}
				
	public void stars (Graphics g, int x, int y){
		g.setColor(new Color (rand.nextInt (255), rand.nextInt (255), rand.nextInt(255)));
		g.fillOval(x+20, y+20, 20, 20);
	}
}