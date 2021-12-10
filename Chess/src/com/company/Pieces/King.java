package com.company.Pieces;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class King extends Piece {
    int castlingI;

    public King(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 900:-900;
        String iconDir=isWhite()?"Pictures\\whiteKing.png" : "Pictures\\blackKing.png";
        this.icon=new ImageIcon(iconDir);
        legalMoves = new ArrayList<>(Arrays.asList(east, west, north, south, northWest, northEast, southEast, southWest));
        castlingI = isWhite() ? 7 : 0;
        type = "King";
        moved = false;
    }

    private ArrayList<Square>attackers;
    public ArrayList<Square> getAttackers() {
        return attackers;
    }

    public ArrayList<Square> getDangerSquares(Square kingSquare, Square[][]squares){
        ArrayList<Square> dangerSquares=new ArrayList<>();
        attackers=new ArrayList<>();
        for(Point point:getLegalMoves())
        {
            Square allySquare=null;
            for (int i = kingSquare.getI() + point.y, j = kingSquare.getJ() + point.x
                             ; kingSquare.isInBound(i, j)
                             ; i += point.y, j += point.x)
            {
                Square currSquare = squares[i][j];
                Piece currPiece = currSquare.getPiece();
                if(currPiece.isWhite()==isWhite())
                {
                    if (allySquare == null) allySquare = currSquare;
                    else break;
                }
                if (currPiece.isWhite() != isWhite()
                        && !currPiece.isTypeEmpty())
                {
                    if (currPiece.getType().equals("Knight")
                            || currPiece.getType().equals("Pawn")
                            || currPiece.getType().equals("King")){
                        if(currPiece.canCapture(currSquare,kingSquare,squares,new Point(point.x*-1,point.y*-1)))
                        {
                            attackers.add(currSquare);
                            dangerSquares.add(currSquare);
                            currSquare.subtractScore(900);
                        }
                        break; // Skipping unblockable checks
                    }
                    if(currPiece.getLegalMoves().contains(new Point(point.x*-1,point.y*-1)))
                    {
                        while (squares[i += point.y * -1][j += point.x * -1] != kingSquare)
                        {
                            dangerSquares.add(squares[i][j]);
                            if (allySquare == null) squares[i][j].subtractScore(900);
                        }
                        dangerSquares.add(currSquare);
                        if (allySquare == null) currSquare.subtractScore(900);
                        attackers.add(currSquare);

                        if (allySquare != null && attackers.size() > 0)
                            allySquare.getPiece().setPinnedMoves(point, new Point(point.x * -1, point.y * -1));
                    }
                    break;
                }
            }
        }
        return dangerSquares;
    }

    public boolean canMove(Square start, Square end, Square[][] squares, Point direction) {
        if( !(isInBound(start) || !isInBound(end))) return false;
        if (!squares[start.getI() + direction.y][start.getJ() + direction.x].equals(end)
                || end.getPiece().isWhite() == isWhite())
        {
            if (!moved && checkCastlingRules(start, end, squares, direction))
            {
                int rookJ=end.getJ()== 2? 0:7;
                Square extraSquare=new Square(start.getI(),rookJ,new Empty());
                extraSquare.setNewPosition(new Square(start.getI(),start.getJ()+direction.x,new Rook(start.getPiece().isWhite()))); // get rook there !
                start.setExtraSquare(extraSquare);
                return true;
            }
                return false;
        }
        return end.getScore()==0 || end.getScore()==-900;
    }

    private boolean checkCastlingRules(Square start, Square end, Square[][] squares, Point direction) {
        if (!(legalMoves.get(0).equals(direction) || legalMoves.get(1).equals(direction)) // 0=east, 1=west
                || end.getJ() == 1)
            return false;

        if (!(end.getI() == castlingI))
            return false;

        if(!end.isSquareEmpty())
            return false;

        int positionX = start.getJ() + direction.x; // checking if square next to king is under attack
        if(start.getScore()!=0 || squares[start.getI()][positionX].getScore()!=0
                  || end.getScore()!=0)
            return false;

        Piece currentPiece = squares[start.getI()][positionX].getPiece();
        while (!currentPiece.getType().equals("Rook")
                && positionX > 0 && positionX < 7)
        {
            if (!currentPiece.isTypeEmpty())
                return false;

            currentPiece = squares[start.getI()][positionX += direction.x].getPiece();
        }
        return currentPiece.getType().equals("Rook") && !currentPiece.moved;
    }
}






