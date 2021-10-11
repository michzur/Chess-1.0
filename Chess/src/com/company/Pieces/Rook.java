package com.company.Pieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Rook extends Pieces {
    public Rook(boolean white, String iconDir) {
        super(white, iconDir);
        legalMoves= new ArrayList<>(Arrays.asList(north,south,east,west));
        type = "Rook";
        moved = false;
    }
    public Rook(boolean white){
        super(white);
    }
    /*
    public boolean canMove(Square start, Square end, Square[][] squares, Point direction) {
        int count = 0;
        if (!legalMoves.contains(direction))
            return false;
        System.out.println(start.getInfo() + " Starting");
        int i = start.getI() + direction.y, j = start.getJ() + direction.x;
        while (squares[i][j] != end && count < 5) {
            System.out.println(squares[i][j].getInfo() + " " + end.getInfo());
            if (squares[i][j].getPiece().isWhite() == isWhite())
                return false;
            i = start.getI() + direction.y;
            j = start.getJ() + direction.x;
            count++;
        }
        return false;
    }

        if (start.getI() != end.getI())
        { // horizontal move
            int offset = start.getI() < end.getI() ? 1 : -1;
            for (int i = start.getI(); i != end.getI()-offset; i += offset)
                if (!(squares[i+offset][start.getJ()].getPiece().getType().equals("empty")))
                    return false;
        }
        else if (start.getJ() != end.getJ())
        { // vertical move
            int offset = start.getJ() < end.getJ() ? 1 : -1;
            for (int i = start.getJ(); i != end.getJ()-offset; i += offset)
                if (!(squares[start.getI()][i+offset].getPiece().getType().equals("empty")))
                    return false;
        }

        return end.getPiece().isWhite()!=start.getPiece().isWhite(); // checking if end square is free or enemy

 */


    public boolean canMove(Square start, Square end, Square[][] squares) {

        if ((start.getI() == end.getI() && start.getJ() == end.getJ())
                || (start.getI() != end.getI() && start.getJ() != end.getJ()))
                            return false; // checking illegal rook moves

        if (start.getI() != end.getI())
        { // horizontal move
            int offset = start.getI() < end.getI() ? 1 : -1;
            for (int i = start.getI(); i != end.getI()-offset; i += offset)
                if (!(squares[i+offset][start.getJ()].getPiece().getType().equals("empty")))
                    return false;
        }
        else if (start.getJ() != end.getJ())
        { // vertical move
            int offset = start.getJ() < end.getJ() ? 1 : -1;
            for (int i = start.getJ(); i != end.getJ()-offset; i += offset)
                if (!(squares[start.getI()][i+offset].getPiece().getType().equals("empty")))
                    return false;
        }

        return end.getPiece().isWhite()!=start.getPiece().isWhite(); // checking if end square is free or enemy
    }
}


