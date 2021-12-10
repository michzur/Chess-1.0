package com.company.Pieces;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece {
    private final int promoteRank;
    public Pawn(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 10:-10;
        String iconDir=isWhite()?"Pictures\\whitePawn.png":"Pictures\\blackPawn.png";
        this.icon=new ImageIcon(iconDir);
        legalMoves = isWhite() ? new ArrayList<>(Arrays.asList(south, southEast, southWest))
                               : new ArrayList<>(Arrays.asList(north, northEast, northWest));
        promoteRank = isWhite() ? 0:7;
        type = "Pawn";
    }
    public boolean isPromoteRank(int i){
        return i==promoteRank;
    }
    public boolean canCapture(Square start, Square end, Square[][]board, Point point){
        return (point.equals(legalMoves.get(1))
                || point.equals(legalMoves.get(2)))
                && board[start.getI() + point.y][start.getJ() + point.x].equals(end);
    }
    public boolean canMove(Square start, Square end, Square[][] squares, Point point) {
        if( !(isInBound(start) && isInBound(end))) return false;
        if (!legalMoves.contains(point)
                || end.getPiece().isWhite() == isWhite())
            return false;

        if(pinnedMoves.size()>0
                && !pinnedMoves.contains(point))
            return false;

        if (point.equals(legalMoves.get(0)))
        { // 0 = move forward
            int mathAbsI = Math.abs(start.getI() - end.getI());
            if (mathAbsI > 2) return false;
            if (mathAbsI == 2)
            {
                if(moved || !squares[start.getI()+point.y][start.getJ()].isSquareEmpty())
                    return false;
                hasMovedTwoTilesThisRound=true;
            }
            return end.getPiece().getType().equals("empty");
        }

        if (!end.getPiece().isTypeEmpty())   // capture
            return squares[start.getI() + point.y][start.getJ() + point.x] == end;

        //en passant
        if (squares[start.getI()][end.getJ()].getPiece().hasMovedTwoTilesThisRound
        && squares[start.getI()][end.getJ()].getPiece().isWhite()!=isWhite())
        {
            Square extraSquare=new Square(squares[start.getI()][end.getJ()]);
            extraSquare.setNewPosition(new Square(start.getI(),end.getJ(),new Empty()));
            start.setExtraSquare(extraSquare);

            return true;
        }
        return false;
    }
}







