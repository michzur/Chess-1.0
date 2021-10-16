package com.company.Pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class Queen extends Pieces {

    public Queen(boolean white,String iconDir) {
        super(white, iconDir);legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest,north,south,east,west));
        type="Queen";
    }

        }




