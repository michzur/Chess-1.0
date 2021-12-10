package com.company.Pieces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 30:-30;
        String iconDir=isWhite()?"Chess\\Pictures\\whiteBishop.png":"Chess\\Pictures\\blackBishop.png";
        this.icon=new ImageIcon(iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest));
        type = "Bishop";
    }
}
