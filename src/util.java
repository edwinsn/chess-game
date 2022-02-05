import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;

public class util {

	private static Square[] posiciones =Square.values();
	public	static String source="/piezas/Chess_";
	

public static char letra(char c) {
	
	return (char)((int)c+49);
};

public static Square casilla(int [] casilla) 
{
	return posiciones[(7-(casilla[0]))*8+casilla[1]];

}
public static Piece evaluaPromoción (char p,Board b) {

	switch(p) {
	case 'q':
		if(b.getSideToMove()==Side.WHITE) {
			return Piece.WHITE_QUEEN;
		}
			return Piece.BLACK_QUEEN;
		
	case 'b':
		if(b.getSideToMove()==Side.WHITE) {
			return Piece.WHITE_BISHOP;
		}

			return Piece.BLACK_BISHOP;
	case 'k':
		if(b.getSideToMove()==Side.WHITE) {
			return Piece.WHITE_KNIGHT;
		}
			return Piece.BLACK_KNIGHT;

	case 'r':
		if(b.getSideToMove()==Side.WHITE) {
			return Piece.WHITE_ROOK;
		}
			return Piece.WHITE_ROOK;
	}
	return null;
}

public static int[][]  jugadasVálidas(String S,Board b){
	
	String StringCasillasVálidas="";
	String moves="" ;
	
	
	try {
	moves = MoveGenerator.generateLegalMoves(b).toString();
	} catch (MoveGeneratorException e) {
		e.printStackTrace();
	}
	for(int i=0;i<moves.length();i++)
	{
		
		if(moves.charAt(i)==' ') {i++;}
		
		if(moves.charAt(i)==util.letra(S.charAt(1))
		  &moves.charAt(++i)==(char)(-(int)S.charAt(0)+104))
		{
			StringCasillasVálidas+=(((int)moves.charAt(i+1))-96)+""+(  ((int)moves.charAt(i+2))-48  ) ;
			i+=3;
		}
		else {
			i+=3;
		}
	
		
		
	}		
	
	int [][]m=new int[(int)Math.floor(StringCasillasVálidas.length()/2)][2];
	
	for(int i=0;i<Math.floor(StringCasillasVálidas.length()/2);i++){
		m[i][0]=8-((int)StringCasillasVálidas.charAt(2*i+1)-48);
		m[i][1]=((int)StringCasillasVálidas.charAt(2*i)-49);
		}
	
	return m;
}

}
