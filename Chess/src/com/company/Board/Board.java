package com.company.Board;
import com.company.Pieces.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Board {
    private boolean isWhiteTurn=true;
    private boolean selected = false;
    private Square[][] squares; // Square is a JButton extension containing Piece
    private final HashMap<Boolean,Square> kingsMap=new HashMap<>();
    private Stack<Square[]> moves=new Stack<>(); // @TODO history implementation
    private Square start,end;
    private boolean isWhitePC=false;
    private final Windows wind;
    public Board(String fen) {
        wind=new Windows("chess");
        wind.loadFen(fen,squares=new Square[8][8],kingsMap);
        wind.setupWindow(squares,new HandlerClass());
        wind.display(squares);
        wind.menuBar();

    }
    public Board(String fen,boolean isWhitePC) {
        wind=new Windows("chess");
        wind.loadFen(fen,squares=new Square[8][8],kingsMap);
        wind.setupWindow(squares,new HandlerClass());

        wind.display(squares);
        Board.this.wind.menuBar();
        this.isWhitePC=isWhitePC;
        if(isWhitePC==isWhiteTurn)
            computerMove();


    }


    private void selectFigure(Square currentSq) {
        // Main function for selecting pieces after handlerClass passes clicked square

        for(Square[]sq:squares)
            for (Square s : sq)
                s.clearScore();
        for(Square[]sq:squares)
            for (Square s : sq)
                if (!s.getPiece().getType().equals("empty")
                        && s.getPiece().isWhite() != isWhiteTurn)
                    s.evaluateScore(squares);


        start = currentSq;
        wind.display(squares);
        selected = true;
        start.showPossibleMoves(squares);
    }

    private void moveFigure(Square currentSq) {
        // Main function for moving pieces after handlerClass passes clicked square
        end = currentSq;

        if (start.getPiece().canMove(start, end, squares,getDirection()))
            endOfPlayerTurn();
    }

    private void changeTwoTileMove() {
        // changing Pieces movedTwoTilesThisRound after one round using Stack
        if(moves.isEmpty())
            return;
           Square s=moves.peek()[1]; // move from last round
           s.getPiece().changehasMovedTwoTiles();
    }

    private Point getDirection() {
        int i=(int)Math.signum(end.getI()-start.getI());
        int j=(int)Math.signum(end.getJ()-start.getJ());
        return new Point(j,i);
    }
    private void endOfPlayerTurn() {

        for(Square[] sq :squares)
            for(Square s:sq)
            { //finding King Square
                if(s.getPiece().getType().equals("King"))
                    kingsMap.put(s.getPiece().isWhite(),s);
            }
        Square currentKingSquare=kingsMap.get(isWhiteTurn);
        King currentKing=(King)currentKingSquare.getPiece();
        start.getPiece().setMoved(true);
            if(!currentKing.getHasMovedTwoTiles())
            {
                squares[end.getI()][end.getJ()].setPiece(start.getPiece());
                squares[start.getI()][start.getJ()].setPiece(new Empty());
            }
        isWhiteTurn = !isWhiteTurn;
        changeTwoTileMove();
        moves.push(new Square[]{start,end});
        selected = false;
        wind.display(squares);

     //   AI ai=new AI(isWhiteTurn,squares);
     //   squares=ai.getSquares();

     //   if (currentKing.isSafeAfterMove(end))
      //  {   // making move only if King is not in danger


           // wind.display(squares);
     //   }
        /*
        if (currentKing.isMate(currentKingSquare, squares,new AI(isWhiteTurn,squares).getPossibleMoves()))
        {
            String winner = isWhiteTurn ? "Black" : "White";
            wind.winWindow("    Check Mate " + winner + " has won! ");
        }

         */

        if(isWhiteTurn==isWhitePC) {
       //     computerMove();
        }
    }

    private void computerMove() {
        AI ai=new AI(isWhiteTurn,squares);
        /*
        if(ai.getPossibleMoves().size()==0)
        {
            wind.winWindow("Winner winner chicken dinner");
            return;
        }

         */
        Square computerSquare=ai.randomMove();
        start = computerSquare;
        selected = true;
        end=computerSquare.getPossibleSquare();
        endOfPlayerTurn();
    }

    private class HandlerClass implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            Square currentSq = (Square)e.getSource();
            if (!currentSq.getPiece().getType().equals("empty")
                    && currentSq.getPiece().isWhite() == isWhiteTurn)
            {   // selecting only Square of player turn
                selectFigure(currentSq);
            }   // if proper Piece is selected, move Piece
                else if(selected)
                {
                    moveFigure(currentSq);
            }
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


}