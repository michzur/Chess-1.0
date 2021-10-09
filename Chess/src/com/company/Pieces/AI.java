package com.company.Pieces;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AI {
    ArrayList<Square> possibleMoves=new ArrayList<>();
    boolean isWhite;
    Square[][] squares;
    public AI(boolean isWhite,Square[][] squares) {
        positionChecker(squares);
       this.isWhite=isWhite;
       this.squares= squares;
    }

    public Square randomMove() {
        int random = ((int) (Math.random() * possibleMoves.size()));
        return possibleMoves.get(random);
    }

    public Square move(Square[][] board) {
        TreeMap<Integer, Square> map = new TreeMap<>();
        for (Square sq : possibleMoves)
        {
            int points = 0;
            Square endSquare = board[sq.getPossibleI()][sq.getPossibleJ()];
            Pieces piece = sq.getPiece();
            if (!endSquare.getPiece().getType().equals("empty")) {   //points granted by capturing enemy piece
                points++;
            }
            map.put(endSquare.getScore()+points, sq);
        }
      //  for (Map.Entry<Integer, Square> entry : map.entrySet()) {
       //     System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue().getPiece().getType()+" Square: i; "+entry.getValue().getI()+" j; "+entry.getValue().getJ());
     //   }
        if (map.lastKey() > 0) return map.get(map.lastKey());
        return randomMove();
    }

    private void tester(Square[][]squares){
        for(Square[]sq:squares)
            for(Square s:sq)
                System.out.println(s.getScore());
    }
    private void positionChecker(Square[][] squares) {
        possibleMoves.clear();
        System.out.println(isWhite+" Ai white?");
        for (Square[] sq : squares) //adding every possible move to array
            for (Square s : sq)
            {
                s.clearScore();
                if(s.getPiece().getType().equals("empty")||s.getPiece().getType().equals("King"))
                    continue;
                if (s.getPiece().isWhite() != isWhite)
                {
                    s.getPiece().evaluateScore(s, squares);
                    continue;
                }
                s.getPiece().addPossibleMoves(s, squares, possibleMoves);
            }
    }

    public ArrayList<Square> getPossibleMoves(){
        positionChecker(squares);
        return possibleMoves;
    }
    public Square[][]getSquares(){
        return squares;
    }
}
