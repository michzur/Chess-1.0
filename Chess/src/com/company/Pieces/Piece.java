package com.company.Pieces;

import com.company.Board.Square;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Piece {

    protected Point north = new Point(0, 1),
                    northEast = new Point(1, 1),
                    south = new Point(0, -1),
                    southEast = new Point(1, -1),
                    east = new Point(1, 0),
                    northWest = new Point(-1, 1),
                    west = new Point(-1, 0),
                    southWest = new Point(-1, -1);

    protected ArrayList<Point> legalMoves=new ArrayList<>(Arrays.asList(north,northEast,south,southEast,east,northWest,west,southWest)),
                               pinnedMoves=new ArrayList<>();
    protected int evaluationPoints;
    protected Boolean white;
    protected String type;
    protected ImageIcon icon;
    protected boolean hasMovedTwoTilesThisRound = false ,moved=false;
    protected boolean simulatingMoves = false; // prevents changing variables like moved and hasMovedTwoTilesThisRound

    // Constructors
    public Piece(Boolean white) {
        this.white = white;
    }
    public Piece() {
    }


    //          Getters
    public int getEvaluationPoints() {
        return evaluationPoints;
    }
    public String getType() {
        return type;
    }
    public boolean getMoved() {
        return moved;
    }
    public ArrayList<Point> getLegalMoves() {
        return legalMoves;
    }
    public ImageIcon getImageIcon() {
        return icon;
    }
    public boolean isTypeEmpty(){
        return type.equals("empty");
    }
    public Boolean isWhite() {
        return white;
    }

    //          Setters
    public void setPinnedMoves(Point p1,Point p2) {
       pinnedMoves.add(p1);
       pinnedMoves.add(p2);
    }
    public void setMoved(boolean b) {
        moved = b;
    }
    public void setHasMovedTwoTiles() { // this method is for pawn only
        if (hasMovedTwoTilesThisRound)
            hasMovedTwoTilesThisRound = false;
    }
    public void setSimulatingMoves(boolean simulatingMoves){
        this.simulatingMoves=simulatingMoves;
    }

    public boolean isInBound(Square square){
        return (square.getI()>=0 && square.getI()<=7
                && square.getJ()>=0 && square.getJ()<=7);
    }


    // Functions
    public void clearPinnedMoves(){
        pinnedMoves.clear();
    }
    public boolean canCapture(Square start, Square end, Square[][]board, Point point){
        Square temp=new Square(end);
        end.setPiece(new Empty());
        boolean result = canMove(start, end, board, point);
        end.setPiece(temp.getPiece());
        return result;
    }
    public boolean canMove(Square start, Square end, Square[][] squares, Point direction) {
        if( ! (isInBound(start) && isInBound(end))) return false;
        if (!legalMoves.contains(direction)
                || end.getPiece().isWhite() == isWhite())
            return false;

        if(pinnedMoves.size()>0
                && !pinnedMoves.contains(direction))
            return false;

        int i = start.getI() + direction.y,
                j = start.getJ() + direction.x;
        while (start.isInBound(i,j) && squares[i][j] != end)
        {
            if (!squares[i][j].isSquareEmpty())
            return false;

            i += direction.y;
            j += direction.x;
        }
        return true;
    }
}

