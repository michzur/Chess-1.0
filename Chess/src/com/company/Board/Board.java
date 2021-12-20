package com.company.Board;
import com.company.Pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Board {
    private boolean isWhiteTurn=true,selected=false;
    private final Square[][] squares; // Square is a JButton extension containing Piece
    private final HashMap<Boolean,Square> kingsMap=new HashMap<>();
    private final Stack<Square> moves=new Stack<>();
    private Square start,end;
    private AI ai;
    private final Windows wind;

    public Board(String fen) {
        wind=new Windows("chess",this);
        wind.loadFen(fen,squares=new Square[8][8],kingsMap);
        wind.setupWindow(squares,new HandlerClass());
        wind.display(squares);
        wind.menuBar();
    }
    public Square getLastMove(){
        return moves.pop();
    }
    private Point getDirection(Square start,Square end) {
        int i=(int)Math.signum(end.getI()-start.getI());
        int j=(int)Math.signum(end.getJ()-start.getJ());
        return new Point(j,i);
    }
    private Square getKing(Square[][] squares){
        for(Square[]sq:squares)
            for(Square s:sq)
                if(s.getPiece().getType().equals("King")
                        && s.getPiece().isWhite()==isWhiteTurn)
                    return s;

        return null;
    }

    private void changeTwoTileMove() {
        // changing Pieces movedTwoTilesThisRound after one round using Stack
        if(moves.isEmpty())
            return;
        Square s=moves.peek(); // move from last round
        s.getPiece().setHasMovedTwoTiles();
    }

    private void findKing(){
        for(Square[] sq :squares)
            for(Square s:sq)
                //finding King Square
                if(s.getPiece().getType().equals("King"))
                    kingsMap.put(s.getPiece().isWhite(),s);
    }

    public void display(){
        wind.display(squares);
    }
    private void selectFigure(Square currentSq) {
        // Main function for selecting pieces after handlerClass passes clicked square


        for(Square[] sq :squares)
            for(Square s:sq)
            { //finding King Square
                if(s.getPiece().getType().equals("King"))
                    kingsMap.put(s.getPiece().isWhite(),s);
            }

        ai=new AI(squares,kingsMap.get(isWhiteTurn));
        start = currentSq;
        wind.display(squares);
        selected = true;
        start.showPossibleMoves(squares,ai.getPossibleMoves());
    }
    private void moveFigure(Square currentSq) {
        // Main function for moving pieces after handlerClass passes clicked square
        // checks if possibleMove List contains start and end square selected by player
        end = currentSq;
        for(Square s:ai.getPossibleMoves())
            if(Arrays.equals(s.getCords(), start.getCords())
                    &&  Arrays.equals(s.getNewPosition().getCords(), end.getCords()))
                 endOfPlayerTurn(s);
    }

   private void makeMove(Square start, Square end) {
       if(start.getPiece().getType().equals("Pawn")
               && ((Pawn)start.getPiece()).isPromoteRank(end.getI()))
       { // Pawn promotion
           start.setPiece(new Queen(start.getPiece().isWhite()));
           start.setHasPromotedThisTurn(true);
       }

       Square extraSquare = start.getExtraSquare();
       if(start.hasExtraSquare()
             && start.extraMoveCheck(end))
           {    // en passant and castle check
               squares[extraSquare.getNewPosition().getI()]
                       [extraSquare.getNewPosition().getJ()].setPiece(extraSquare.getNewPosition().getPiece());
               if(start.getPiece().getType().equals("King"))
                   squares[extraSquare.getI()][extraSquare.getJ()].setPiece(extraSquare.getPiece());
           }
           if (!start.getPiece().getMoved())
               start.setHasMovedThisTurn(true);

           start.getPiece().setMoved(true);

       squares[end.getI()][end.getJ()].setPiece(start.getPiece());
       squares[start.getI()][start.getJ()].setPiece(new Empty());
   }
    private void endOfPlayerTurn(Square move) {
        boolean turn=!isWhiteTurn;
        start=move;
        end=move.getNewPosition();
        makeMove(start,end);
        isWhiteTurn = turn;
        changeTwoTileMove();
        moves.push(move);
        selected = false;
        wind.display(squares);
        findKing();

        if(((ai=new AI(squares,kingsMap.get(isWhiteTurn))).getPossibleMoves().size()==0)) {
            wind.winWindow(isWhiteTurn ? "Black is the Winner!" : "White is the winner!");
            return;
        }


        if(!isWhiteTurn)
            endOfPlayerTurn(ai.getBestMove());

    }

   private void aiMove(Square move){
       makeMove(move,move.getNewPosition());
   }

   public void unMakeMove(Square move){
       if(move.hasPromotedThisTurn())
           move.setPiece(new Pawn(move.getPiece().isWhite()));

       if(move.hasMovedThisTurn())
       {
           move.getPiece().setMoved(false);
           move.setHasMovedThisTurn(false);
       }

       squares[move.getI()][move.getJ()].setPiece(move.getPiece());
       squares[move.getNewPosition().getI()][move.getNewPosition().getJ()].setPiece(move.getNewPosition().getPiece());


        Square extraSquare=move.getExtraSquare();
        if(!move.hasExtraSquare())
            return;

        if(squares[extraSquare.getI()][extraSquare.getJ()].getPiece().getType().equals("empty"))
        { // Setting up en passanted enemy pawn and castled rook
            squares[extraSquare.getI()]
                    [extraSquare.getJ()].setPiece(extraSquare.getNewPosition().getPiece());
            squares[extraSquare.getNewPosition().getI()]
                    [extraSquare.getNewPosition().getJ()].setPiece(extraSquare.getPiece());
        }
   }

    private class HandlerClass implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            Square currentSq = (Square)e.getSource();
            if (!currentSq.isSquareEmpty()
                    && currentSq.getPiece().isWhite() == isWhiteTurn)
                selectFigure(currentSq);

            else if(selected)
                moveFigure(currentSq);
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent en) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
    }
    int moveGenerationTest(int depth){
        if(depth == 0)
            return 1;
    Square king=getKing(squares);
    if(king==null)
        return 0;

        ArrayList<Square> moves=new AI(squares,getKing(squares)).getPossibleMoves();
        isWhiteTurn=!isWhiteTurn;
        int numPositions=0;

        for (Square move:moves) {
            aiMove(move);
            numPositions+=moveGenerationTest(depth -1);
            unMakeMove(move);
        }

        return numPositions;
    }




}