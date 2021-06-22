package com.company.Pieces;

public class Queen extends Pieces {
    public Queen(boolean white,String iconDir) {
        super(white, iconDir);
        type="Queen";
    }
    public boolean canMove(Square start,Square end,Square[][]squares) {
            if(new Bishop(this.isWhite()).canMove(start,end,squares))
                return true;
            else return new Rook(this.isWhite()).canMove(start, end, squares);
    }
        }




