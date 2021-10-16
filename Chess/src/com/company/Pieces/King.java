package com.company.Pieces;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class King extends Pieces {
    int castlingI;

    public King(boolean white, String iconDir) {
        super(white, iconDir);
        legalMoves = new ArrayList<>(Arrays.asList(east, west, north, south, northWest, northEast, southEast, southWest));
        castlingI = isWhite() ? 7 : 0;
        type = "King";
        moved = false;
    }

    public boolean isInCheck(Square[][] board, Square kingSquare) {
        return false;
    }

    public boolean isMate(Square start, Square[][] squares, ArrayList<Square> possibleMoves) {
        return false;
    }


    public boolean isInCheckAfterMove(Square start, Square end, Square[][] square) {
        //  System.out.println(end.getScore());
        return end.getScore() == 0;
    }

    private boolean checkCheck(Square end, Square[][] squares) {
        return false;
    }

    /*
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
     */
    public boolean canMove(Square start, Square end, Square[][] squares, Point direction) {
        if (!squares[start.getI() + direction.y][start.getJ() + direction.x].equals(end)
                || end.getPiece().isWhite() == isWhite()) {
            if (!moved && checkCastlingRules(start, end, squares, direction))
            {
                if(!simulatingMoves) doCastle(start,end,squares,direction);
                return true;
            }
                return false;
        }
        //TODO indanger checker with evaluate score
        return end.getScore()==0;
    }
    private void castleMove(Square oldSquare, Square newSquare, Square[][] squares) {
        oldSquare.getPiece().setMoved(true);
        squares[newSquare.getI()][newSquare.getJ()].setPiece(oldSquare.getPiece());
        squares[oldSquare.getI()][oldSquare.getJ()].setPiece(new Empty());
    }
    private void doCastle(Square start,Square end,Square[][] squares, Point direction) {
        Square rookSquare;
        if(direction.equals(legalMoves.get(0)))
        { // 0=east
            rookSquare=squares[end.getI()][end.getJ()+direction.x];
        }
        else{ // moving west
            rookSquare=squares[end.getI()][end.getJ()+ direction.x*2];
        }
        start.getPiece().hasMovedTwoTilesThisRound=true;
        castleMove(start,end,squares);
        castleMove(rookSquare,squares[start.getI()][start.getJ()+direction.x],squares);
    }

    private boolean checkCastlingRules(Square start, Square end, Square[][] squares, Point direction) {
        if (!(legalMoves.get(0).equals(direction) || legalMoves.get(1).equals(direction)) // 0=east, 1=west
                || end.getJ() == 1)
            return false;

        if (!(end.getI() == castlingI)
                && (end.getJ() == 2 || end.getJ() == 6))
            return false;

        int positionX = start.getJ() + direction.x;
        if(squares[start.getI()][positionX].getScore()!=0
                  || end.getScore()!=0)
            return false;

        Pieces currentPiece = squares[start.getI()][positionX].getPiece();
        while (!currentPiece.getType().equals("Rook")
                && positionX > 0 && positionX < 7)
        {
            if (!currentPiece.getType().equals("empty"))
                return false;

            currentPiece = squares[start.getI()][positionX += direction.x].getPiece();
        }
        return currentPiece.getType().equals("Rook") && !currentPiece.moved;
    }
}






