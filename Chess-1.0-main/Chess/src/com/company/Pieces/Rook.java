package com.company.Pieces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Piece {
    public Rook(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 50:-50;
        String iconDir=isWhite()?"Chess\\Pictures\\whiteRook.png":"Chess\\Pictures\\blackRook.png";
        this.icon=new ImageIcon(iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(north,south,east,west));
        type = "Rook";
        moved = false;
    }

}


