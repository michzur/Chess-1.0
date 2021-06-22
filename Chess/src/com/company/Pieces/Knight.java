package com.company.Pieces;

public class Knight extends Pieces {

    public Knight(boolean white, String icondir){
        super(white,icondir);
        type="Knight";
    }
    public boolean canMove(Square start,Square end,Square[][]board){
        if((Math.abs(start.getI() - end.getI()) == 2 && Math.abs(start.getJ() - end.getJ()) == 1)
                    || (Math.abs(start.getI() - end.getI()) == 1 && Math.abs(start.getJ() - end.getJ()) == 2))
            return end.getPiece().getType().equals("empty") || end.getPiece().isWhite() != start.getPiece().isWhite();

                return false;
    }




}
