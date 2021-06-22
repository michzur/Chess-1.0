package com.company.Pieces;
import java.util.ArrayList;

public class King extends Pieces {
    public King(boolean white, String iconDir) {
        super(white, iconDir);
        type = "King";
        moved = false;
    }
    public King(Pieces piece){
        white=piece.isWhite();
        icon=piece.icon;
        type="King";
        moved=false;
    }

    public boolean canCapture (Square start,Square end) {
        int xdiff = Math.abs(start.getI() - end.getI());
        int ydiff = Math.abs(start.getJ() - end.getJ());
        return (Math.abs(xdiff) < 2 && Math.abs(ydiff) < 2)
                && (end.getPiece().isWhite()!=isWhite() && end.getPiece().isWhite()!=null);
    }

public boolean isMate(Square start, Square[][]squares, ArrayList<Square> possibleMoves) {
        if(checkCheck(start,squares)) { // checking if King is in check
        return false;
        }
    simulatingMoves=true;
    int moves=0;
    for(Square[]sq:squares)
        for (Square s : sq)
            if (canMove(start, s, squares))
                moves++; // moves that king can make to avoid check

    simulatingMoves=false;
   if(moves==0)
       for(Square sq:possibleMoves)
       { // checking potential cover moves
           if (sq.getPiece().isWhite() == isWhite()
           && !isInCheckAfterMove(sq,sq.getPossibleSquare(),squares))
               moves++; // moves that other figures can make to protect king
       }
return moves==0;
}

    public boolean isInCheckAfterMove(Square start, Square end, Square[][] square) {
        Square kingSquare=null;
        Square[][]squareClone=new Square[square.length][square.length];
        // Cloning Square array
        for (int i = 0; i < square.length; i++)
            for (int j = 0; j < square.length; j++)
                squareClone[i][j] = new Square(i, j, square[i][j].getPiece());

            // moving piece
        squareClone[end.getI()][end.getJ()].setPiece(squareClone[start.getI()][start.getJ()].getPiece());
        squareClone[start.getI()][start.getJ()].setPiece(new Empty());

        // searching king position
        for(Square[] squares:squareClone)
            for(Square s:squares)
                if(s.getPiece().isWhite()==start.getPiece().isWhite()
                        && s.getPiece().getType().equals("King"))
                    kingSquare=s;

        return !checkCheck(kingSquare,squareClone);
    }

    private boolean checkCheck(Square end, Square[][]squares){
        for (Square[] sq : squares)
                for (Square s : sq)
                {
                    if (!s.getPiece().getType().equals("empty") && s.getPiece().isWhite() != isWhite())
                        if (s.getPiece().getType().equals("Pawn") || s.getPiece().getType().equals("King"))
                        {
                            if (s.getPiece().canCapture(s, end))
                                return false;
                        }
                        else if (s.getPiece().canMove(s, end, squares))
                            return false;
                }
        return true;
    }
    private void move(Square oldSquare, Square newSquare, Square[][] squares) { // used in Castling
        squares[newSquare.getI()][newSquare.getJ()].setPiece(oldSquare.getPiece());
        squares[oldSquare.getI()][oldSquare.getJ()].setPiece(new Empty());
    }

private boolean CheckCastle(Square kingSquare, Square end, Square[][] squares) {
        int castleI = isWhite() ? 7 : 0; // only viable castling I
if(kingSquare.getPiece().getMoved() || end.getI()!=castleI || isInCheckAfterMove(kingSquare,end,squares))
    return false; // checking castle condition and not allowing to move King to danger position

    if(end.getJ()==2)
    {
        for(int i=-1;i!=-4;i--) // checking if between king and rook is any piece
            if(squares[castleI][kingSquare.getJ()+i].getPiece().isWhite()!=null)
            return false;

        if (squares[castleI][0].getPiece().getMoved()) // checking if rook has moved
            return false;

        move(squares[castleI][0], squares[castleI][3], squares);
        return true;
    }
if(end.getJ()==6)
{
    for(int i=1;i!=3;i++) // checking if between king and rook is any piece
        if(squares[castleI][kingSquare.getJ()+i].getPiece().isWhite()!=null)
            return false;

    if (squares[castleI][7].getPiece().getMoved()) // checking if rook has moved
        return false;

    move(squares[castleI][7], squares[castleI][5], squares);
    return true;
}
    return false;
}

    public boolean canMove(Square start, Square end, Square[][] squares) {

        int xdiff = Math.abs(start.getI() - end.getI());
        int ydiff = Math.abs(start.getJ() - end.getJ());
        if((Math.abs(xdiff) < 2 && Math.abs(ydiff) < 2))
        {
            if (end.getPiece().getType().equals("empty")
                    || (end.getPiece().isWhite()!=isWhite()&&end.getPiece().isWhite()!=null))
            { // checking if end Square is empty or enemy
                return !isInCheckAfterMove(start, end, squares);
            }
        }
        if((end.getJ()==2 || end.getJ()==6)&&!simulatingMoves)
        { // if Squares are coordinates of castling, and are chosen by player, check castle @Todo fix bug when program is checking castling to showPossibleMoves
            return CheckCastle(start, end, squares);
        }
        return false;
    }
}





