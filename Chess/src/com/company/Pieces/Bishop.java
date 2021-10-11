package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Bishop extends Pieces {
    public Bishop(boolean white, String iconDir) {
        super(white, iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest));
        type = "Bishop";
    }
    public Bishop(boolean white) {
        super(white);
    }
    /*
    public boolean canMove(Square start, Square end, Square[][] squares) {
        int idiff= Math.abs(start.getI()-end.getI());
        int jdiff= Math.abs(start.getJ()-end.getJ());

        if(start.getI()==end.getI() || start.getJ()==end.getJ()
                || !(idiff==jdiff)) // illegal bishop moves
            return false;

    int iOffset=start.getI() < end.getI()? 1:-1; // checking offsets (used in loop)
    int jOffset=start.getJ() < end.getJ()? 1:-1; // checking offsets (used in loop)

    for(int i=1;i!=Math.abs(start.getI()-end.getI());i++)
    {  // loop for checking if Square is occupied
        Pieces piece=squares[start.getI() + (iOffset*i)][start.getJ() + (jOffset*i)].getPiece();
        if(!piece.getType().equals("empty"))
            return false;
    }
    return end.getPiece().isWhite()!=start.getPiece().isWhite(); // checking if end Square is free or enemy
    }

     */
}
