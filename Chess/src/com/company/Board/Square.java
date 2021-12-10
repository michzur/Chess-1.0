package com.company.Board;
import com.company.Pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Square extends JButton {
    private com.company.Pieces.Piece Piece;
    private int i, j,score=0;
    private String color;
    private Square newPosition,extraSquare; // newPosition - estimated by AI class as a possible Square that Piece can move to
                                            // extraSquare - location of Square used by en Passant or Castling

    private boolean hasMovedThisTurn,hasPromotedThisTurn;
    ImageIcon icon,coloredIcon;
    Color background;

    //          Constructors
    public Square(Square square) {
            if(square.hasExtraSquare())
                this.setExtraSquare(square.getExtraSquare());

        this.setPiece(square.getPiece());
        this.setI(square.getI());
        this.setJ(square.getJ());
        setupColors();
    }

    public Square(int i, int j, Piece Piece) {
        this.setPiece(Piece);
        this.setI(i);
        this.setJ(j);
        setupColors();
    }

    //          Getters
    public boolean hasMovedThisTurn() {
        return hasMovedThisTurn;
    }
    public boolean hasPromotedThisTurn() {
        return hasPromotedThisTurn;
    }
    public Square getNewPosition(){
        return newPosition;
    }
    public Square getExtraSquare() {
        return extraSquare;
    }
    public String getColor(){return color;}
    public int getScore(){
        return score;
    }
    public int[] getCords(){
        return new int[]{i,j};
    }
    public com.company.Pieces.Piece getPiece() {
        return Piece;
    }
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
    public String getInfo() { // Debug use only
        return "I :"+getI()+" J: "+getJ()+" Type: "+getPiece().getType()+" White? "+getPiece().isWhite();
    }
    public boolean hasExtraSquare(){
        return(extraSquare!=null);
    }
    public boolean isSquareEmpty(){
        return getPiece().getType().equals("empty");
    }

    //          Setters
    public void setHasMovedThisTurn(boolean hasMovedThisTurn) {
        this.hasMovedThisTurn = hasMovedThisTurn;
    }
    public void setHasPromotedThisTurn(boolean hasPromotedThisTurn) {
        this.hasPromotedThisTurn = hasPromotedThisTurn;
    }
    public void setExtraSquare(Square extraSquare) {
        this.extraSquare = extraSquare;
    }
    public void setNewPosition(Square move){
        newPosition=new Square(move);
    }
    public void setPiece(com.company.Pieces.Piece piece) {
        Piece = piece;
    }
    public void setI(int i) {
        this.i = i;
    }
    public void setJ(int j) {
        this.j = j;
    }

    public boolean extraMoveCheck(Square end) { // function used in enPassant and Castle
        if(this.getPiece().getType().equals("Pawn"))
            return this.getJ()!=end.getJ();
        else return end.getJ() == 2 || end.getJ()==6;
    }

    public void clearScore(){
        score=0;
    } // used in AI estimation
    public void subtractScore(){
        score--;
    } //  used in AI estimation
    public void subtractScore(int i){
        score-=i;
    }
    public boolean isInBound(int i,int j){
        return (i>=0 && i<=7
                && j>=0 && j<=7);
    }

    public void showPossibleMoves(Square[][] squares,ArrayList<Square> possibleMoves) {
        for(Square s:possibleMoves)
        {
            Square newPosition=s.getNewPosition();
            if (Arrays.equals(s.getCords(), this.getCords()))
                squares[newPosition.getI()][newPosition.getJ()].setColloredIcon();
        }
    }

    public void setupColors(){
        if((i+j)%2==0)
        {
            color="white";
            icon=new ImageIcon("Chess\\Pictures\\whitetile.png");
            coloredIcon=new ImageIcon("Chess\\Pictures\\colloredWhiteTile.png");
            background=Color.WHITE;
        }
            else{
                color="black";
                icon=new ImageIcon("Chess\\Pictures\\blacktile.png");
                coloredIcon=new ImageIcon("Chess\\Pictures\\colloredBlackTile.png");
            background=Color.BLACK;
        }
    }
    public void setColloredIcon(){
        if(getPiece().isTypeEmpty())
        setIcon(coloredIcon);
    }

private void knightEvaluate(Point direction, Square[][] squares, com.company.Pieces.Piece piece, int i, int j){
    if(isInBound(i,j)
            && piece.canCapture(this,squares[i][j],squares,direction))
        squares[i][j].subtractScore();
}
    public void evaluateScore(Square[][]squares) {
        // function checking squares that enemy can capture for checking checks, attacked squares and protected pieces,used by AI class
        Piece piece = getPiece();
        piece.setSimulatingMoves(true);
        for (Point direction : piece.getLegalMoves())
            for (int i=getI()+direction.y, j=getJ()+direction.x
                 ; isInBound(i, j) ; i += direction.y, j += direction.x)
             {
                 if (piece.getType().equals("Knight")) {
                     knightEvaluate(direction,squares,piece,i+direction.y,j);
                     knightEvaluate(direction,squares,piece,i,j+direction.x);
                     break;
                 }
                 if (piece.canCapture(this, squares[i][j], squares, direction))
                 {
                     squares[i][j].subtractScore();
                     if(piece.getType().equals("Pawn"))
                            break;
                     if (isInBound(i + direction.y, j + direction.x)
                             && squares[i][j].getPiece().getType().equals("King"))
                         squares[i + direction.y][j + direction.x].subtractScore();
                 }
             }
        piece.setSimulatingMoves(false);
    }
}
