
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

import partidasJugadas.partidasJugadas;




public class tablero extends JPanel{

	private boolean turno=true,juegoActivo=true;
	private int movimientos=0;
	private computadora comp;
	private boolean bandoComputadora; 
	private JFrame ventanaProvisional= new JFrame();
	private JButton casillas[][]=new JButton[8][8], ultimaCasilla=new JButton(),casillaSeleccionada=new JButton();
	private Board b=new Board();
	private historial [] historia=new historial[100];
	private JPanel panelBotones=new JPanel();
	private JButton retroceder=new JButton("<"),avanzar=new JButton(">");	
	private Piece piezaPromoción;
	
	public static void main(String arg[]) {
		
		try {
		tablero t=new tablero(false);//true para blancas false para negras
	} catch (MoveGeneratorException e) {
		
		e.printStackTrace();
	}
	}
	
	tablero(boolean bando) throws MoveGeneratorException{
	
		
		ventanaProvisional.setSize(540,600);
		ventanaProvisional.setMinimumSize(new Dimension(50,50));
		ventanaProvisional.setMaximumSize(new Dimension(300,300));
		ventanaProvisional.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ventanaProvisional.setLocationRelativeTo(null);
		ventanaProvisional.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		comp=new computadora(!bando);
		this.setSize(540,540);
		this.setLayout(new GridLayout(8,8));
		
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				casillas[i][j]=new JButton();
				casillas[i][j].setName(i+""+j);
				casillas[i][j].setBorder(null);
				casillas[i][j].addActionListener(listenerCasillas);
				this.add(casillas[i][j]);
			}
		}
		retroceder.addActionListener(regresar);
		avanzar.addActionListener(listenerAvanzar);
		panelBotones.setLayout(new GridLayout(1,2));
		panelBotones.add(retroceder);
		panelBotones.add(avanzar);
		retroceder.setSize(90,70);
		despintar();
		ponerPiezas();
		c.gridx = 0;
		c.gridy = 0;
		ventanaProvisional.add(this,c);
		c.ipady = 20; 
		c.ipadx = 150; 
		c.gridx = 0;
		c.gridy = 1;
		ventanaProvisional.add(panelBotones,c);
		
		
		ventanaProvisional.setVisible(true);
		
		if(!bando) {
			bandoComputadora=true;
			movimientoComputadora();
		
		}
		
	}
	
	
	ActionListener listenerCasillas =new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				casillaSeleccionada=(JButton)e.getSource();
				try {
					mover();
				} catch (MoveGeneratorException e1) {
					e1.printStackTrace();
				}
				
			}
		};
	
	ActionListener regresar	= new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			regresarMovimiento();
			regresarMovimiento();
		}
		};
		
		public void regresarMovimiento() {
	
			if(movimientos>0) {
				b.undoMove();	
				casillas[historia[movimientos-1].posición1[0]] [historia[movimientos-1].posición1[1]].
				setIcon(historia[movimientos-1].img1);
				casillas[historia[movimientos-1].posición2[0]] [historia[movimientos-1].posición2[1]].
				setIcon(historia[movimientos-1].img2);
				
				//pintar el enroque
				if(historia[movimientos-1].enroque) {
					
					String torre="/piezas/Chess_";
					int columna1=3;
					int columna2=0;
					if(historia[movimientos-1].posición1[0]==0) {
						torre+="B";
					}
					torre+="1.png";
					if(historia[movimientos-1].posición2[1]==6) {
						
						columna1=5;
						columna2=7;
					}
					
					casillas[historia[movimientos-1].posición1[0]] [columna1].setIcon(null);
					casillas[historia[movimientos-1].posición1[0]] [columna2].setIcon(
						new ImageIcon(getClass().getResource(torre))
							);
				
				}
				
				ultimaCasilla=null;
				movimientos--;
				turno=!turno;
				despintar();
			}
};
		
		ActionListener listenerAvanzar= new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			
				
			if(historia[movimientos]!=null) {
				
				casillas[historia[movimientos].posición1[0]]
						[historia[movimientos].posición1[1]].setIcon(
						null	);
				
				casillas[historia[movimientos].posición2[0]]
						[historia[movimientos].posición2[1]].setIcon(
						historia[movimientos].img1	);

				if(historia[movimientos].enroque) {
					String torre="/piezas/Chess_";
					int columna1=3;
					int columna2=0;
					if(historia[movimientos].posición1[0]==0) {
						torre+="B";
					}
					torre+="1.png";
					if(historia[movimientos].posición2[1]==6) {
						
						columna1=5;
						columna2=7;
					}
					
					casillas[historia[movimientos].posición1[0]] [columna1].setIcon(new ImageIcon(getClass().getResource(torre)));
					casillas[historia[movimientos].posición1[0]] [columna2].setIcon(null);
				}
				int[] c1={historia[movimientos].posición1[0],historia[movimientos].posición1[1]};
				int[] c2={historia[movimientos].posición2[0],historia[movimientos].posición2[1]};

				
				if(historia[movimientos].promoción) {
					util.source="/piezas/Chess_";
					piezaPromoción=util.evaluaPromoción(historia[movimientos].p,b);
						b.doMove(new Move(
								
								util.casilla(c1),
								util.casilla(c2)
								,piezaPromoción
								));
						if(historia[movimientos].posición2[0]==7) {
							util.source+='B';
						}
						switch( historia[movimientos].p ) {
							case 'q':
								util.source+='4';
								break;
							case 'r':
								util.source+='1';
								break;
							case 'k':
								util.source+='2';
								break;
							case 'b':
								util.source+='6';
								break;
						}
						util.source+=".png";
						casillas[historia[movimientos].posición2[0]]
								[historia[movimientos].posición2[1]].setIcon(new ImageIcon(getClass().getResource(util.source)));
						
				}
				else {
								
				b.doMove(new Move(
						
						util.casilla(c1),
						util.casilla(c2)
						));
				}
		
				movimientos++;
				turno=!turno;
				despintar();
			
			}
			}
		};
	
//
public void mover() throws MoveGeneratorException {
	
	
	//casilla seleccionada
	if(casillaSeleccionada.getBackground()==java.awt.Color.cyan
		||
	   casillaSeleccionada.getBackground()==java.awt.Color.red
			) 
	{
//guardar posición
		
		historia[movimientos]=new historial();
		
		historia[movimientos].posición1[0]= (int)ultimaCasilla.getName().charAt(0)-48;
		
		
		historia[movimientos].posición1[1]= (int)ultimaCasilla.getName().charAt(1)-48;
		historia[movimientos].img1=ultimaCasilla.getIcon();

		historia[movimientos].posición2[0]= (int)casillaSeleccionada.getName().charAt(0)-48;
		historia[movimientos].posición2[1]= (int)casillaSeleccionada.getName().charAt(1)-48;
		historia[movimientos].img2=casillas
				[(int)casillaSeleccionada.getName().charAt(0)-48]
				[(int)casillaSeleccionada.getName().charAt(1)-48]
				.getIcon();

						movimientos++;
//mover la pieza(imagen)
		
		casillas[(int)casillaSeleccionada.getName().charAt(0)-48]
				[(int)casillaSeleccionada.getName().charAt(1)-48].setIcon(
				ultimaCasilla.getIcon() );
		casillas[(int)casillaSeleccionada.getName().charAt(0)-48]
				[(int)casillaSeleccionada.getName().charAt(1)-48].setMnemonic(
						ultimaCasilla.getMnemonic() );

		
		System.out.println("moviendo");
		
		ultimaCasilla.setMnemonic(0);
		
		
		ultimaCasilla.setIcon(null);
		
		int[] casilla1={((int)ultimaCasilla.getName().charAt(0))-48,((int)ultimaCasilla.getName().charAt(1))-48};
		int[] casilla2={((int)casillaSeleccionada.getName().charAt(0))-48,((int)casillaSeleccionada.getName().charAt(1))-48};
		           
		enroque();
	
		if(corona(casilla1)) {
			if(bandoComputadora!=turno) {
			seleccionarPieza(casilla1,casilla2);
			}
			if(piezaPromoción!=null) {
				b.doMove(new Move(
					util.casilla(casilla1),
					util.casilla(casilla2),
					piezaPromoción
					));
			}
		}
		else {
		
			b.doMove(new Move(
				util.casilla(casilla1),
				util.casilla(casilla2)
				));
		turno=!turno;
		}
		
		despintar();
		
		if(bandoComputadora==turno&&juegoActivo) {
		try {
			
			movimientoComputadora();
		} catch (MoveGeneratorException e) {
			e.printStackTrace();
			
		}
		}
		}

//casilla sin seleccionar

	else {
	despintar();
	//no selecciona si el vando de la casilla no lleva el turno o está vacia
	if( turno== 
    (casillas[(int)casillaSeleccionada.getName().charAt(0)-48][(int)casillaSeleccionada.getName().charAt(1)-48]
    .getMnemonic()==78 ) 
    &&
    casillas[(int)casillaSeleccionada.getName().charAt(0)-48][(int)casillaSeleccionada.getName().charAt(1)-48]
		    .getMnemonic()!=0 
			) {
	}

	else {
	//deseccionar con doble click
		
	if(ultimaCasilla== 
			casillas[(int)casillaSeleccionada.getName().charAt(0)-48]
					[(int)casillaSeleccionada.getName().charAt(1)-48])
	{
		ultimaCasilla=null;
	}
	
	else{
			
		
	casillas[(int)casillaSeleccionada.getName().charAt(0)-48]
			[(int)casillaSeleccionada.getName().charAt(1)-48].
			setBackground(java.awt.Color.green);
		int [][] jugadas=util.jugadasVálidas(casillaSeleccionada.getName(),b);
		
		for(int i=0;i<jugadas.length;i++) {
			if(casillas[jugadas[i][0]]
					[jugadas[i][1]].getIcon()==null)
			{
			casillas[jugadas[i][0]]
					[jugadas[i][1]].
					setBackground(java.awt.Color.cyan);
			}
			else {
				casillas[jugadas[i][0]]
						[jugadas[i][1]].
						setBackground(java.awt.Color.red);
				
			}
			}
		ultimaCasilla=casillas[(int)casillaSeleccionada.getName().charAt(0)-48]
							  [(int)casillaSeleccionada.getName().charAt(1)-48];
	
	}
	
	}
	}
	
}
	
	
private void despintar() {
	
	for(int i=0;i<8;i++) {
		for(int j=0;j<8;j++) {
			casillas[i][j].setBackground((i+j)%2==0?java.awt.Color.white : new Color(102,51,0) );
		}
	}
}

private void ponerPiezas() {
	
	for(int i=1;i<9;i++) {
	ImageIcon img1=new ImageIcon(getClass().getResource(util.source+i+".png"));
	ImageIcon img2=new ImageIcon(getClass().getResource(util.source+"p.png"));
	casillas[7][i-1].setMnemonic('b');
	casillas[6][i-1].setMnemonic('b');		
	
	casillas[7][i-1].setIcon(img1);
 	casillas[6][i-1].setIcon(img2);
	
	}
	for(int i=1;i<9;i++) {
		
	ImageIcon img1=new ImageIcon(getClass().getResource(util.source+"B"+i+".png"));
	ImageIcon img2=new ImageIcon(getClass().getResource(util.source+"Bp.png"));
	img1.setDescription("negra");
	casillas[0][i-1].setMnemonic('n');		
	casillas[1][i-1].setMnemonic('n');		
	
	casillas[0][i-1].setIcon(img1);
	casillas[1][i-1].setIcon(img2);
	}

	for(int i=2;i<6;i++) {
		for(int j=0;j<8;j++) {
			casillas[i][j].setMnemonic(0) ;
		}
	}
}

private int[] casilla(String s) {
	int[]m= {7-((int)s.charAt(1)-96)-47,(((int)s.charAt(0)-96)-1), 
			7-((int)s.charAt(3)-96)-47,(((int)s.charAt(2)-96)-1)};
	return m;
}





private void enroque() {
	
	if( 
    	((int)ultimaCasilla.getName().charAt(1))-48==4 
    	&&
    	((int)casillaSeleccionada.getName().charAt(1)-48==2
    	|| 
    	(int)casillaSeleccionada.getName().charAt(1)-48==6)
			)
	{
		historia[movimientos-1].enroque=true;
		int columna1=3;
		int columna2=0;
		if((int)casillaSeleccionada.getName().charAt(1)-48==6) {
			columna1=5;
			columna2=7;
		}
		casillas[(int)casillaSeleccionada.getName().charAt(0)-48][columna1].setIcon(
				casillas[(int)casillaSeleccionada.getName().charAt(0)-48][columna2].getIcon() ) ;
		casillas[(int)casillaSeleccionada.getName().charAt(0)-48][columna2].setIcon(null) ;

	}
}
private void movimientoComputadora() throws MoveGeneratorException {
	
	
	
	Move mmovimiento=comp.mejorMovimiento(b);
	if(mmovimiento==null) {
		  JOptionPane.showMessageDialog(null, "Victoria!");
		  partidasJugadas.registraPartida("Victoria!");
		  juegoActivo=false;
	}
	
	else{	
	//movimiento en el jpanel
	String m=mmovimiento.toString();
	
	ultimaCasilla=casillas[casilla(m)[0]][casilla(m)[1]];
	casillaSeleccionada=casillas[casilla(m)[2]][casilla(m)[3]];
	if(casillaSeleccionada.getIcon()!=null) {
		casillaSeleccionada.setBackground(java.awt.Color.red);
	}
	else {
	casillaSeleccionada.setBackground(java.awt.Color.cyan);
	}
	
	int []casilla= {casilla(m)[0],casilla(m)[1]};
	
	if(corona(casilla)) {
		piezaPromoción=util.evaluaPromoción((char)m.charAt(4),b);
		
	}
	mover();
	casillaSeleccionada.setBackground(new Color(204,204,204));
	if(b.isMated()) {
		System.out.println("Lamentable Situación");
		partidasJugadas.registraPartida("Derrota");
	}
	}
}

private boolean corona(int[] casilla1) {
	return (b.getPiece(util.casilla(casilla1))==Piece.WHITE_PAWN||b.getPiece(util.casilla(casilla1))==Piece.BLACK_PAWN)
			&&
			(((int)casillaSeleccionada.getName().charAt(0))-48==0||((int)casillaSeleccionada.getName().charAt(0))-48==7)
			;
}


private void seleccionarPieza(int []casilla1,int []casilla2){
	
	util.source="/piezas/Chess_";
historia[movimientos-1].promoción=true;

//mostrar jPanel de seleccion

		JFrame selección=new JFrame ();
		selección.setSize(250,400);
		selección.setLayout(new GridLayout(4,1));
		selección.setLocationRelativeTo(null);
		JButton Q=new JButton();
		JButton R=new JButton();
		JButton B=new JButton();
		JButton K=new JButton();
		
		
		if(!turno) {
			util.source+="B";
		}
		
		Q.setIcon(new ImageIcon( getClass().getResource(util.source+"4.png")));
		R.setIcon(new ImageIcon(getClass().getResource(util.source+"1.png")));
		B.setIcon(new ImageIcon(getClass().getResource(util.source+"3.png")));
		K.setIcon(new ImageIcon(getClass().getResource(util.source+"2.png")));
		Q.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(turno) {piezaPromoción=Piece.WHITE_QUEEN;}
				else {piezaPromoción=Piece.BLACK_QUEEN;}
				selección.setVisible(false);
				casillaSeleccionada.setIcon(new ImageIcon(getClass().getResource(util.source+"4.png")));
				historia[movimientos-1].p ='q';
				turno=!turno;
				b.doMove(new Move(
						util.casilla(casilla1),
						util.casilla(casilla2),
						piezaPromoción
						)
						);
				piezaPromoción=null;
				try {
					despintar();
					movimientoComputadora();
				} catch (MoveGeneratorException e1) {
					e1.printStackTrace();
					
				}
			}
			
		});

		R.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(turno) {piezaPromoción=Piece.WHITE_ROOK;}
				else {piezaPromoción=Piece.BLACK_ROOK;}
				selección.setVisible(false);
				casillaSeleccionada.setIcon(new ImageIcon(getClass().getResource(util.source+"1.png")));
				historia[movimientos-1].p='r';
				b.doMove(new Move(
						util.casilla(casilla1),
						util.casilla(casilla2),
						piezaPromoción
						));
				piezaPromoción=null;
				turno=!turno;
				try {
					despintar();
					movimientoComputadora();
				} catch (MoveGeneratorException e1) {
					e1.printStackTrace();
					
				}
			}
			
		});

		B.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(turno) {piezaPromoción=Piece.WHITE_BISHOP;}
				else {piezaPromoción=Piece.BLACK_BISHOP;}
				selección.setVisible(false);
				casillaSeleccionada.setIcon(new ImageIcon(getClass().getResource(util.source+"3.png")));
				historia[movimientos-1].p='b';
				b.doMove(new Move(
						util.casilla(casilla1),
						util.casilla(casilla2),
						piezaPromoción
						));
				piezaPromoción=null;
				turno=!turno;
				try {
					despintar();
					movimientoComputadora();
				} catch (MoveGeneratorException e1) {
					e1.printStackTrace();
					
				}
			}
			
		});

		K.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(turno) {piezaPromoción=Piece.WHITE_KNIGHT;}
				else {piezaPromoción=Piece.BLACK_KNIGHT;}
				selección.setVisible(false);
				casillaSeleccionada.setIcon(new ImageIcon(getClass().getResource(util.source+"2.png")));
				historia[movimientos-1].p='k';
				b.doMove(new Move(
						util.casilla(casilla1),
						util.casilla(casilla2),
						piezaPromoción
						));
				piezaPromoción=null;
				turno=!turno;
				try {
					despintar();
					movimientoComputadora();
				} catch (MoveGeneratorException e1) {
					e1.printStackTrace();
					
				}
			}
			
		});

		
		selección.add(Q);
		selección.add(R);
		selección.add(B);
		selección.add(K);
		selección.setVisible(true);			
}
}
