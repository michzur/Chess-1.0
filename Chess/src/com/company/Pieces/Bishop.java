package com.company.Pieces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 30:-30;

       // this.icon=new ImageIcon(getClass().getResource("/resources/blackKing.png"));
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest));
        type = "Bishop";
    }
}
