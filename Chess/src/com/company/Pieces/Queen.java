package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Queen extends Pieces {
    ArrayList<Point> legalMoves= new ArrayList<>(Arrays.asList(northWest,northEast,southEast,southWest,north,south,east,west));
    public Queen(boolean white,String iconDir) {
        super(white, iconDir);
        type="Queen";
    }
    public boolean canMove(Square start,Square end,Square[][]squares) {
        return new Bishop(this.isWhite()).canMove(start,end,squares)
                || new Rook(this.isWhite()).canMove(start,end,squares);
    }
        }




