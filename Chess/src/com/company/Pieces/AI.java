package com.company.Pieces;
import java.awt.*;
import java.util.ArrayList;

public class AI {
    private final ArrayList<Square> possibleMoves=new ArrayList<>();
    private final boolean isWhite;
    private final Square[][] squares;
    private final Square currKingSquare;
    public AI(Square[][] squares,Square currKingSquare) {

        this.currKingSquare=currKingSquare;
        King king = (King) currKingSquare.getPiece();
        this.isWhite= king.isWhite();
        this.squares= squares;
        positionChecker();
    }

    public ArrayList<Square> getPossibleMoves(){
        return possibleMoves;
    }

    public Square getBestMove(){
        // picks up random move from possible moves, then evaluate the best piece to capture //@TODO move evaluation + depth
        int random=(int)(Math.random()*possibleMoves.size()+1);
        Square bestMove=possibleMoves.get(random);
        for(Square move:this.getPossibleMoves())
        {       // capture estimation
            Piece newPositionPiece=move.getNewPosition().getPiece();

            if(newPositionPiece.getType().equals("empty"))
                continue;

            if(newPositionPiece.isWhite()!=isWhite
            && newPositionPiece.getEvaluationPoints() > bestMove.getPiece().getEvaluationPoints())
                bestMove=move;
        }
        return bestMove;
    }

    private boolean isInBound(int i){
        return i < 8 && i>=0;
    }
    private boolean isInBound(int i,int j){
       return isInBound(i) && isInBound(j);
    }

    private void clear(){
        for(Square[]sq:squares)
            for (Square s : sq)
            {
                s.clearScore();
                s.getPiece().clearPinnedMoves();
                s.setExtraSquare(null);
            }
    }
    private void evaluate(){
        for(Square[]sq:squares)
            for (Square s : sq)
                if (!s.isSquareEmpty()
                        && s.getPiece().isWhite() != isWhite)
                    s.evaluateScore(squares);
    }

    private void setPossibleMove(Square start,Square end){
        Square square=new Square(start);
        square.setNewPosition(end);
        possibleMoves.add(square);
    }
    private void checkKnightUnderCheck(int i, int j, Point p, Piece currPiece, Square start, ArrayList<Square> dangerSquares, King currKing){
        if (canKnightMove(i+p.y,j,p,currPiece,start)
                && (dangerSquares.contains(squares[i+p.y][j]) || currKing.getAttackers().contains(squares[i+p.y][j])))
            setPossibleMove(start, squares[i + p.y][j]);
        if (canKnightMove(i,j+p.x,p,currPiece,start)
                && (dangerSquares.contains(squares[i][j+p.x]) || currKing.getAttackers().contains(squares[i][j+p.x])))
            setPossibleMove(start, squares[i][j + p.x]);
    }
    private boolean canKnightMove(int i, int j, Point p, Piece currPiece, Square start){
        return isInBound(i, j) && currPiece.canMove(start, squares[i][j], squares, p);
    }
    private void checkKnight(int i, int j, Point p, Piece currPiece, Square start){
        if (canKnightMove(i+p.y,j,p,currPiece,start))
            setPossibleMove(start, squares[i + p.y][j]);
        if (canKnightMove(i,j+p.x,p,currPiece,start))
            setPossibleMove(start, squares[i][j + p.x]);
    }
    public void positionChecker() {
        // Main AI function that creates list of every possible move
        clear();
        evaluate();
        King currKing=(King)currKingSquare.getPiece();
        ArrayList<Square>dangerSquares= currKing.getDangerSquares(currKingSquare,squares);
        possibleMoves.clear();

            for (Square[] sq : squares)
                for (Square start : sq)
                {
                    Piece currPiece = start.getPiece();
                    currPiece.simulatingMoves = true;
                    if (currPiece.isWhite() != currKing.isWhite()
                            || currPiece.isTypeEmpty())
                        continue;

                    for (Point p : currPiece.getLegalMoves())
                    {
                        int i = start.getI() + p.y, j = start.getJ() + p.x;
                        if(!isInBound(i,j))
                            continue;

                        if (currKingSquare.getScore() != 0)
                        {  // King under attack
                            if (currPiece.getType().equals("King")
                                && squares[i][j].getScore() != -900
                                        && squares[i][j].getScore() != 0) //-900 indicates danger square without any others attackers
                                    continue;

                                if (currKing.getAttackers().size() > 1)
                                    continue;
                            if (currPiece.getType().equals("Knight"))
                            {
                                checkKnightUnderCheck(i,j,p,currPiece,start,dangerSquares,currKing);
                                continue;
                            }
                                for( ;!currPiece.getType().equals("King") && isInBound(i,j); i+=p.y,j+=p.x)
                                    if (dangerSquares.contains(squares[i][j])
                                            || currKing.getAttackers().contains(squares[i][j]))
                                        break;

                                        if(!isInBound(i,j))
                                            continue;

                            else if(currPiece.canMove(start,squares[i][j],squares,p))
                                setPossibleMove(start,squares[i][j]);

                               continue;
                        }
                            if (currPiece.getType().equals("Knight"))
                            {
                                checkKnight(i,j,p,currPiece,start);
                                continue;
                            }
                            while (start.isInBound(i, j)
                                    && currPiece.canMove(start, squares[i][j], squares, p))
                            {
                                setPossibleMove(start, squares[i][j]);
                                i += p.y;
                                j += p.x;
                            }

                        }
                       currPiece.simulatingMoves = false;
                    }
    }
}
