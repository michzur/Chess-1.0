package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
    public boolean canMoveEvaluate(Square start, Square end, Square[][]board, Point point){
        return (point == legalMoves.get(1)
                || point == legalMoves.get(2))
                && board[start.getI() + point.y][start.getJ() + point.x].equals(end);
    }
    public boolean canMove(Square start, Square end, Square[][] squares, Point point) {
        if (!legalMoves.contains(point)
                || end.getPiece().isWhite() == isWhite())
            return false;

        if (point.equals(legalMoves.get(0)))
        {
            int mathAbsI = Math.abs(start.getI() - end.getI());
            if (mathAbsI > 2) return false;
            if (mathAbsI == 2)
            {
                if(moved) return false;
                if (!simulatingMoves) hasMovedTwoTilesThisRound = true;
            }
            return end.getPiece().getType().equals("empty");
        }

        if (!end.getPiece().getType().equals("empty"))   // capture
            return squares[start.getI() + point.y][start.getJ() + point.x] == end;

        //en passant
        if (squares[start.getI()][end.getJ()].getPiece().hasMovedTwoTilesThisRound)
        {
            if(!simulatingMoves) squares[start.getI()][end.getJ()].setPiece(new Empty());
            return true;
        }
        return false;
    }
}







