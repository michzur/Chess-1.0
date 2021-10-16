package com.company.Pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Pieces {
    public Rook(boolean white, String iconDir) {
        super(white, iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(north,south,east,west));
        type = "Rook";
        moved = false;
    }
}


