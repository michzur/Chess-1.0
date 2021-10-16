package com.company.Pieces;
import javax.swing.*;
import java.awt.*;

public class Square extends JButton {
    private Pieces Piece;
    private int i, j, possibleI,possibleJ;
    private int score=0;
    private String color;
    public Square(Square square) {
        this.setPiece(square.getPiece());
        this.setI(square.getI());
        this.setJ(square.getJ());
    }
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

    public boolean checkRange(int i){
        return (i>=0 && i<=7);
    }
    public boolean checkRange(int i,int j){
        return checkRange(i) && checkRange(j);
    }
    public boolean isInBound(){
        return checkRange(i,j);
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
    public boolean isInBound(int i,int j){
        return (i>=0 && i<=7
                && j>=0 && j<=7);
    }


    private void evaluateScore(Point direction,Square[][] squares,Pieces piece,int i,int j) {
        if(!isInBound(i,j))
            return;
            if (piece.canMove(this, squares[i][j], squares, direction))
                squares[i][j].subtractScore();
            else if (isInBound(i+=direction.y,j+=direction.x)
                    && squares[i][j].getPiece().isWhite() == this.getPiece().isWhite())
                squares[i][j].subtractScore();

    }
    private void showPossibleForKnight(Point direction,Square[][] squares,Pieces piece,int i,int j){
        if(isInBound(i,j)
                && piece.canMove(this,squares[i][j],squares,direction))
            squares[i][j].setColloredIcon();
    }

    public void showPossibleMoves(Square[][] squares) {
        Pieces piece = getPiece();
        piece.simulatingMoves = true;
        for (Point direction : piece.getLegalMoves())
        {
            int i = getI() + direction.y;
            int j = getJ() + direction.x;
            if (piece.getType().equals("Knight"))
            {
                showPossibleForKnight(direction, squares, piece, i + direction.y, j);
                showPossibleForKnight(direction, squares, piece, i, j + direction.x);
            }
            else for (; isInBound(i, j); i += direction.y, j += direction.x)
                if (piece.canMove(this, squares[i][j], squares, direction))
                      squares[i][j].setColloredIcon();
        }
        piece.simulatingMoves = false;
    }

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
    public void setColloredIcon(){
        if(getPiece().getType().equals("empty"))
        setIcon(coloredIcon);
    }
    public String getInfo() {
        return new String("I :"+getI()+" J: "+getJ()+" Type: "+getPiece().getType()+" White? "+getPiece().isWhite());
    }

private void knightEvaluate(Point direction,Square[][] squares,Pieces piece,int i,int j){
    if(isInBound(i,j)
            && piece.canMoveEvaluate(this,squares[i][j],squares,direction))
        squares[i][j].subtractScore();
}
    public void evaluateScore(Square[][]squares) {
        Pieces piece = getPiece();
        piece.simulatingMoves = true;
        for (Point direction : piece.getLegalMoves())
            for (int i=getI()+direction.y, j=getJ()+direction.x  ; isInBound(i, j) ; i += direction.y, j += direction.x)
             {
                 if(isInBound(i+direction.y,j+direction.x)
                         && squares[i][j].getPiece().getType().equals("King"))
                   squares[i+direction.y][j+direction.x].subtractScore();

                 if (piece.getType().equals("Knight")) {
                     knightEvaluate(direction,squares,piece,i+direction.y,j);
                     knightEvaluate(direction,squares,piece,i,j+direction.x);
                     break;
                 }
                 if (piece.canMoveEvaluate(this, squares[i][j], squares, direction))
                     squares[i][j].subtractScore();
             }

        piece.simulatingMoves = false;
    }
}
