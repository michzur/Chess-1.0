package com.company.Pieces;
import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    private Pieces Piece;
    private int i, j, possibleI,possibleJ;
    private int score=0;
    private String color;
    public Square(int i, int j, Pieces Piece) {
        this.setPiece(Piece);
        this.setI(i);
        this.setJ(j);
    }
    public Square(int i, int j, Pieces Piece, int possibleI, int possibleJ) {
        this.setPiece(Piece);
        this.setI(i);
        this.setJ(j);
        this.setPossibleI(possibleI);
        this.setPossibleJ(possibleJ);
    }
    public String getColor(){return color;}
    public void clearScore(){
        score=0;
    }
    public int getScore(){
        return score;
    }
    public void addScore(){
        score++;
    }
    public void subtractScore(){
        score--;
    }
    public Square getPossibleSquare() { // used in PossibleMoves Array
        return new Square(possibleI,possibleJ,getPiece());
    }

    public int getPossibleI() {
        return possibleI;
    }

    public int getPossibleJ() {
        return possibleJ;
    }

    public void setPossibleI(int possibleI) {
        this.possibleI = possibleI;
    }

    public void setPossibleJ(int possibleJ) {
        this.possibleJ = possibleJ;
    }

    public Pieces getPiece() {
        return Piece;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public void setPiece(Pieces piece) {
        Piece = piece;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    ImageIcon icon;
    Color background;
    ImageIcon coloredIcon;
    public void setupColors(){
        if((i+j)%2==0)
        {
            color="white";
            icon=new ImageIcon("Pictures\\whitetile.png");
            coloredIcon=new ImageIcon("Pictures\\colloredWhiteTile.png");
            background=Color.WHITE;
        }
            else{
                color="black";
                icon=new ImageIcon("Pictures\\blacktile.png");
                coloredIcon=new ImageIcon("Pictures\\colloredBlackTile.png");
            background=Color.BLACK;
        }

    }
    public void setEmptyIcon(){
        setIcon(icon);
        setBackground(background);
    }
    public void setColloredIcon(){
        if(getPiece().getType().equals("empty"))
        setIcon(coloredIcon);
    }
    public String getInfo() {
        return new String("I :"+getI()+" J: "+getJ()+" Type: "+getPiece().getType()+" White? "+getPiece().isWhite());
    }

    public void evaluateScore(Square[][]squares){
        this.getPiece().simulatingMoves=true;
        for(Square[]sq:squares)
            for (Square s : sq)
            {
                if (this.getPiece().canMove(this, s, squares))
                    s.subtractScore();
            }
       this.getPiece(). simulatingMoves=false;
    }
}
