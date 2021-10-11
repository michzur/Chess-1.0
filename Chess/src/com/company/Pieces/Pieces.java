package com.company.Pieces;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Pieces {
    public Point north=new Point(0,1);
    public Point northEast=new Point(1,1);
    public Point south=new Point(0,-1);
    public Point southEast=new Point(1,-1);
    public Point east=new Point(1,0);
    public Point northWest=new Point(-1,1);
    public Point west=new Point(-1,0);
    public Point southWest=new Point(-1,-1);
    ArrayList<Point> legalMoves;
    protected Boolean white;
    protected String type;
    protected ImageIcon icon;
    protected boolean hasMovedTwoTilesThisRound=false;
    protected boolean moved=false;
    protected boolean simulatingMoves=false; // prevents changing variables like moved and hasMovedTwoTilesThisRound

    public Pieces(Boolean white){
        this.white=white;
    }
     public Pieces(){}
     public Pieces(Boolean white, String iconDir){
        this.icon=new ImageIcon(iconDir);
        this.white=white;
    }

    public boolean getMoved(){
        return moved;
    }
    public void setMoved(boolean b){
        moved=b;
    }
    public String getType(){
        return type;
    }
    public Boolean isWhite(){
        return white;
    }
    public ImageIcon getImageIcon(){
        return icon;
    }

    public void changehasMovedTwoTiles() { // this method is for pawn only
        if(hasMovedTwoTilesThisRound)
            hasMovedTwoTilesThisRound=false;
    }
    public boolean canMove(Square start, Square end, Square[][] squares, Point direction) {
         System.out.println(this.getType());
         System.out.println(this.legalMoves);
         if (!legalMoves.contains(direction)
                 || end.getPiece().isWhite()==isWhite())
            return false;

         int i = start.getI() + direction.y,
                 j = start.getJ() + direction.x;
        while (true)
        {
            if (squares[i][j].getPiece().isWhite() == isWhite())
                return false;

            if(squares[i][j] == end)
                return true;
            i+=direction.y;
            j+=direction.x;
        }
    }

public boolean canMove(Square start,Square end, Square[][] squares) { // override in child's classes
       return false;
    }
    public boolean canCapture(Square start,Square end){ // override in child's classes
       return false;
    }

public void addPossibleMoves(Square start,Square[][] squares,ArrayList<Square> possibleMoves) {
        simulatingMoves=true;
    for(Square[]sq:squares)
    {  // checking every possible move of every figure and if can move - adding that Square with Piece to ArrayList
        for (Square s : sq)
            if (canMove(start, s, squares)) // if can move adding Square that has starting coordinates and Piece, and potential ending coordinates
                possibleMoves.add(new Square(start.getI(),start.getJ(),start.getPiece(),s.getI(), s.getJ()));
    }
    simulatingMoves=false;
}
    public void showPossibleMoves(Square start, Square[][] squares) {
        simulatingMoves=true;

        for(Square[]sq:squares)
            for (Square s : sq)
                if (canMove(start, s, squares))
                    s.setColloredIcon();

        simulatingMoves=false;
    }
}

