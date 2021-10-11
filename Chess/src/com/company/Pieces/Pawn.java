package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Pieces {
    public Pawn(boolean white, String iconDir) {
        super(white, iconDir);
        legalMoves = isWhite() ? new ArrayList<>(Arrays.asList(south, southEast, southWest)) : new ArrayList<>(Arrays.asList(north, northEast, northWest));
        type = "Pawn";
    }
    private void promote(Square start) {
        String icondir = isWhite() ? "Pictures\\whiteQueen.png" : "Pictures\\blackQueen.png";
        start.setPiece(new Queen(isWhite(), icondir));
    }

    public boolean canMove(Square start, Square end, Square[][] squares, Point point) {
        if (!legalMoves.contains(point)
          || end.getPiece().isWhite()==isWhite())
            return false;

        if (!end.getPiece().getType().equals("empty"))   // capture
            return squares[start.getI() + point.y][start.getJ() + point.x] == end;

        if (!legalMoves.get(0).equals(point))
        {   //en passant
            if(squares[start.getI()][end.getJ()].getPiece().hasMovedTwoTilesThisRound)
            {
               squares[start.getI()][end.getJ()].setPiece(new Empty());
               return true;
            }
           return false;
        }

        int mathAbsI=Math.abs(start.getI()-end.getI());
        if(mathAbsI==2 && !moved)
        {
            hasMovedTwoTilesThisRound=true;
            return true;
        }
        return mathAbsI==1;
    }
}







