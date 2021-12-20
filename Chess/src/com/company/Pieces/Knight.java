package com.company.Pieces;

import com.company.Board.Square;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Knight extends Piece {

    public Knight(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 30:-30;
    //    String iconDir=isWhite()?"Pictures\\whiteKnight.png":"Pictures\\blackKnight.png";
    //    this.icon=new ImageIcon(iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest));
        type="Knight";
    }
    public boolean canMove(Square start, Square end, Square[][]board, Point point){
        if( !(isInBound(start)&&isInBound(end))) return false;
        int absI=Math.abs(start.getI()-end.getI());
        int absJ=Math.abs(start.getJ()-end.getJ());

        if(pinnedMoves.size()>0)
            return false;

       if(!legalMoves.contains(point)
               || end.getPiece().isWhite()==isWhite()
               || absI>2 || absJ >2)
           return false;

        return(!start.getColor().equals(end.getColor()));
    }
}
