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
}
