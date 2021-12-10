package com.company.Pieces;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Queen extends Piece {

    public Queen(boolean white) {
        super(white);
        evaluationPoints= isWhite()? 90:-90;
        String iconDir=isWhite()?"Chess\\Pictures\\whiteQueen.png":"Chess\\Pictures\\blackQueen.png";
        this.icon=new ImageIcon(iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest,north,south,east,west));
        type="Queen";
    }
        }




