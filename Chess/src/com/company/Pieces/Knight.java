package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Knight extends Pieces {

    public Knight(boolean white, String icondir){
        super(white,icondir);
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest));
        type="Knight";
    }
    public boolean canMove(Square start, Square end, Square[][]board, Point point){
        int absI=Math.abs(start.getI()-end.getI());
        int absJ=Math.abs(start.getJ()-end.getJ());

       if(!legalMoves.contains(point)
               || end.getPiece().isWhite()==isWhite()
               || absI>2 || absJ >2)
           return false;

        return(!start.getColor().equals(end.getColor()));
    }




}
