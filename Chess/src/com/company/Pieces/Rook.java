package com.company.Pieces;

public class Rook extends Pieces {
    public Rook(boolean white, String iconDir) {
        super(white, iconDir);
        type = "Rook";
        moved = false;
    }
    public Rook(boolean white){
        super(white);
    }
    public boolean canMove(Square start, Square end, Square[][] squares) {
        if ((start.getI() == end.getI() && start.getJ() == end.getJ())
                || (start.getI() != end.getI() && start.getJ() != end.getJ()))
                            return false; // checking illegal rook moves

        if (start.getI() != end.getI())
        { // rook moving horizontal
            int offset = start.getI() < end.getI() ? 1 : -1;
            for (int i = start.getI(); i != end.getI()-offset; i += offset)
                if (!(squares[i+offset][start.getJ()].getPiece().getType().equals("empty")))
                    return false;

        }
        else if (start.getJ() != end.getJ())
        { // rook moving vertical
            int offset = start.getJ() < end.getJ() ? 1 : -1;
            for (int i = start.getJ(); i != end.getJ()-offset; i += offset)
                if (!(squares[start.getI()][i+offset].getPiece().getType().equals("empty")))
                    return false;
        }
        return end.getPiece().isWhite()!=start.getPiece().isWhite(); // checking if end square is free or enemy
    }
}


