package com.company.Pieces;
import javax.swing.*;

public class Square extends JButton {
    private Pieces Piece;
    private int i, j, possibleI,possibleJ;
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

    public void setEmptyIcon(){
        setIcon((i + j) % 2 == 0 ? new ImageIcon("Pictures\\whitetile.png") : new ImageIcon("Pictures\\blacktile.png"));
    }
    public void setColloredIcon(){
        if(getPiece().getType().equals("empty"))
        setIcon((i + j) % 2 == 0 ? new ImageIcon("Pictures\\colloredWhiteTile.png") : new ImageIcon("Pictures\\colloredBlackTile.png"));
    }

    public void printInfo() {
        System.out.println("I :"+getI()+" J: "+getJ()+" Type: "+getPiece().getType()+" White? "+getPiece().isWhite());
    }
}
