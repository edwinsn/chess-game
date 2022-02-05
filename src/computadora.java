import java.util.ArrayList;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;


public class computadora {

	private int posicionesEvaluadas=0;
	
	private MoveList movimientosPosibles ;
	
	private ArrayList<Double> calificaciones=new  ArrayList<Double>();
	
	private int jugadasPrevistas=1;		//profundidad del análisis
	
	private boolean bando;
	
	private double calificaciónMínima=100;
	
	//######
	public static void main(String arg[]) throws MoveGeneratorException {
		computadora c=new computadora(true);
		Board T=new Board();
		Move m=c.mejorMovimiento(T);
		T.doMove(m);
		System.out.println("fin");
		System.out.println(T.toString()  );
		
	}
	//#######
	
	public computadora(boolean bbando) {

		bando=bbando;
		
	for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
			posiciónReyNegro[i][j]=-posiciónReyBlanco[7-i][j];
			}
		}
	for(int i=0;i<8;i++) {
		for(int j=0;j<8;j++) {
		posiciónPeonNegro[i][j]=-posiciónPeonBlanco[7-i][j];
		}
	}
	for(int i=0;i<8;i++) {
		for(int j=0;j<8;j++) {
		posiciónDamaBlanca[i][j]=-posiciónDamaBlanca[7-i][j];
		}
	}
	for(int i=0;i<8;i++) {
		for(int j=0;j<8;j++) {
		posiciónAlfilNegro[i][j]=-posiciónAlfilBlanco[7-i][j];
		}
	}
	for(int i=0;i<8;i++) {
		for(int j=0;j<8;j++) {
		posiciónCaballoNegro[i][j]=-posiciónCaballoBlanco[7-i][j];
		}
	}

	}
	
	public Move mejorMovimiento(Board T) throws MoveGeneratorException {
		
		int indice=0;
		posicionesEvaluadas=0;
		calificaciónMínima=101;
		calificaciones.clear();
		
		movimientosPosibles=MoveGenerator.generateLegalMoves(T);
		for(int i=0;i<movimientosPosibles.size();i++) {calificaciones.add(500.0);}
		
		for(int movimiento=0;movimiento<movimientosPosibles.size();movimiento++) {
			T.doMove(movimientosPosibles.get(movimiento));
			calificaMovimientos(T,jugadasPrevistas,movimiento);
			T.undoMove();
		}
		
		for(int i=0;i<calificaciones.size();i++){
			//cambiese por < para efectos evaluativos
			if( calificaciones.get(i)>calificaciones.get(indice) ) {
				indice=i;
			}
			
		}

		
		if(!T.isMated()) {
			return movimientosPosibles.get(indice);			
		}
		
		return null;
	}
	
	
	private void calificaMovimientos(Board T,int jugadasAdelante,int jugada) {
	
		MoveList moves=null ;
		
		
		if(jugadasAdelante!=0) {
			
		//si ni aún con una ventaja de diez puntos el movimientos sería el mejor se para la busqueda
		if( calificaciones.get(jugada)>=calificaciónMínima) {	
			
			try {

				 moves =MoveGenerator.generateLegalMoves(T);
		
			} catch (MoveGeneratorException e) {}
			
			
			 for(Move move :moves) {
				 
				T.doMove(move);
				calificaMovimientos(T,jugadasAdelante-1,jugada);
		
			}
		}
		
		else {
			System.exit(0);
			System.out.println("Ahorro*****calificación: *"+calificaciones.get(jugada)+"**"+jugada);
		}
				
		}
		else {
			///
			double result=medirMaterial(T)+medirjPosición(T);
					/*medirMate( T)*medirCaballeria( T)*medirCortadura( T)*medirSeguridad( T)*medirPiezasClavadas(T);*/
			
			
			if(calificaciones.get(jugada)>result) {
			calificaciones.set(jugada,result);
			if(calificaciónMínima>result) {
				calificaciónMínima=result;
			}
			}
			posicionesEvaluadas++;
		///
			
		}
		if(jugadasAdelante!=jugadasPrevistas) {
		T.undoMove();
		}
		
	}
	
	
	
	private double medirMaterial(Board T) {
		String tablero=T.toString();
		double material=0;
		for(int i=0;i<tablero.length();i++	) {
			switch(tablero.charAt(i)) {
			case 'p':
				material-=10 ;
				break;
			case 'r':
				material-=50 ;
				break;
			case 'n':
				material-=30 ;
				break;

			case 'b':
				material-=30 ;
				break;

			case 'q':
				material-=90;
				break;

			case 'k':
				material-=900;
				break;
			case 'P':
				material+=10 ;
				break;
			case 'R':
				material+=50 ;
				break;
			case 'N':
				material+=30 ;
				break;

			case 'B':
				material+=30  ;
				break;

			case 'Q':
				material+=90 ;
				break;
			case 'K':
				material+=900;
				break;
			}
			
		}
		
		if(!bando) {
			return -material;
		}
		
		return material;
	};

	private double medirMate(Board T) {
		
		return 1;
	};

	
	private double medirjPosición(Board T) {
		double posición=0;
		double diagonalesNegras;
		double diagonalesBlancas;
		boolean alfilBlancoBlancas;
		boolean alfilNegroBlancas;
		boolean alfilBlancoNegras;
		boolean alfilNegroNegras;
		Square casillas[];
		for(int i=0;i<8;i++) {
			
			for(int j=0;j<8;j++) {
				int[] ccasillas= {i,j };
				
				switch( T.getPiece(  casilla(ccasillas)  ) ) {
				
				case BLACK_PAWN :
					posición+= posiciónPeonNegro[i][j];
					break;
				
				case  BLACK_KING   :
					posición+= posiciónReyNegro[i][j];
					break;

				case BLACK_QUEEN:
					posición+= posiciónDamaNegra[i][j];
					break;

				case BLACK_ROOK:
					posición+= posiciónTorreNegra[i][j];
					break;

				case BLACK_BISHOP:
					posición+= posiciónAlfilNegro[i][j];
					break;

				case BLACK_KNIGHT:
					posición+=posiciónCaballoNegro[i][j];
					break;

				case WHITE_PAWN :
					posición+= posiciónPeonBlanco[i][j];
					break;
				
				case  WHITE_KING   :
					posición+= posiciónReyBlanco[i][j];
					break;

				case WHITE_QUEEN:
					posición+= posiciónDamaBlanca[i][j];
					break;

				case WHITE_ROOK:
					posición+= posiciónTorreBlanca[i][j];
					break;

				case WHITE_BISHOP:
					posición+= posiciónAlfilBlanco[i][j];
					break;

				case WHITE_KNIGHT:
					posición+= posiciónCaballoBlanco[i][j];
					break;

				default:
					break;
				}
				
			}
		}
		
		
		
		if(!bando) {
			return -posición;
		}
		return posición;
	
	};



	private double medirCaballeria(Board T) {
		return 1;
	};
	private double medirCortadura(Board T){
		return 1;
	};
	//enroque y peonadelantado
	private double medirSeguridad(Board T) {
		return 1;
	}
	private double medirPiezasClavadas(Board T) {
		return 1;
	}
	private static double[][] posiciónReyNegro=new double[8][8];
	
	private static double[][] posiciónReyBlanco= {
			{-3,-4,-4,-5,-5,-4,-4,-3},
			{-3,-4,-4,-5,-5,-4,-4,-3},
			{-3,-4,-4,-5,-5,-4,-4,-3},
			{-3,-4,-4,-5,-5,-4,-4,-3},
			{-2,-3,-3,-4,-4,-3,-3,-2},
			{-1,-2,-2,-2,-2,-2,-2,-1},
			{2,2,0,0,0,0,2,2},
			{2,3,1,0,0,1,3,2}
			
	};
	private static double[][] posiciónDamaNegra=new double[8][8];
	private static double[][] posiciónDamaBlanca= {
			{-2,-1,-1,-0.5,-0.5,-1,-1,-2},
			{-1,0,-0.5,0,0,0,0,-1},
			{-1,0.5,0.5,0.5,0.5,0.5,0,-1},
			{-0.5,0,0.5,0.5,0.5,0.5,0,-0.5},
			{0,0,0.5,0.5,0.5,0.5,0,-0.5},
			{-1,0.5,0.5,0.5,0.5,0.5,0,-1},
			{-1,0,-0.5,0,0,0,0,-1},
			{-2,-1,-1,-0.5,-0.5,-1,-1,-2}
	};
	private static double[][] posiciónTorreNegra=new double[8][8];
	private static double[][] posiciónTorreBlanca= {
			{0,0,0,0,0,0,0,0},
			{0.5,1,1,1,1,1,1,0.5},
			{-0.5,0,0,0,0,0,0,-0.5},
			{-0.5,0,0,0,0,0,0,-0.5},
			{-0.5,0,0,0,0,0,0,-0.5},
			{-0.5,0,0,0,0,0,0,-0.5},
			{-0.5,0,0,0,0,0,0,-0.5},
			{0,0,0,0.5,0.5,0,0,0}	

	};

	private static double[][] posiciónPeonNegro=new double [8][8];

	private static double[][] posiciónPeonBlanco = {
			{0,0,0,0,0,0,0,0},
			{5,5,5,5,5,5,5,5},
			{1,1,2,3,3,2,1,1},
			{0.5,0.5,1,2.5,2.5,1,0.5,0.5},
			{0,0,0,2,2,0,0,0},
			{0.5,-0.5,-1,0,0,-1,-0.5,0.5},
			{0.5,1,1,-2,-2,1,1,0.5},
			{0,0,0,0,0,0,0,0}
			
	};
	private static double[][] posiciónAlfilNegro=new double[8][8];
	private static double[][] posiciónAlfilBlanco={
			{-2,-1,-1,-1,-1,-1,-1,-2},
			{-1,0,0,0,0,0,0,-1},
			{-1,0,0.5,1,1,0.5,0,-1},
			{-1,0.5,0.5,1,1,0.5,0.5,-1},
			{-1,0,1,1,1,1,0,-1},
			{-1,1,1,1,1,1,1,-1},
			{-1,0.5,0,0,0,0,0.5,-1},
			{-2,-1,-1,-1,-1,-1,-1,-2}
	};
	private static double[][] posiciónCaballoNegro=new double[8][8];	
	private static double[][] posiciónCaballoBlanco={
			{-5,-4,-3,-3,-3,-3,-4,-5},
			{-4,-2,0,0,0,0,-2,-4},
			{-3,0,1,1.5,1.5,1,0,-3},
			{-3,0.5,1.5,2,2,1.5,0.5,-3},
			{-3,0,1.5,2,2,1.5,0,-3},
			{-3,0.5,1,1.5,1.5,1,0.5,-3},
			{-4,-2,0,0.5,0.5,0,-2,-4},
			{-5,-4,-3,-3,-3,-3,-4,-5}
			};
	
	public static Square casilla(int [] casilla) 
	{
		Square[] posiciones =Square.values();
		return posiciones[(7-(casilla[0]))*8+casilla[1]];

	}

}
