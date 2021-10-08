package com.company.Pieces;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AI {
    ArrayList<Square> possibleMoves=new ArrayList<>();
    boolean isWhite;
    Square[][] squares;
    public AI(boolean isWhite,Square[][] squares) {

       this.isWhite=isWhite;
       this.squares= squares;
    }

    public Square randomMove() {
        positionChecker(squares);
        int random = ((int) (Math.random() * possibleMoves.size()));
        return possibleMoves.get(random);
    }

    public Square move(Square[][] board) {
        positionChecker(squares);
        TreeMap<Integer, Square> map = new TreeMap<>();
        for (Square sq : possibleMoves) {
            int points = 0;
            Pieces enemyPiece = board[sq.getPossibleI()][sq.getPossibleJ()].getPiece();
            Pieces piece = sq.getPiece();
            if (!enemyPiece.getType().equals("empty")) {   //points granted by capturing enemy piece
                points++;
            }
            map.put(sq.getScore()+points, sq);
        }
        for (Map.Entry<Integer, Square> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue().getPiece());
        }
        if (map.lastKey() > 0) return map.get(map.lastKey());
        return randomMove();
    }

    private void tester(Square[][]squares){
        for(Square[]sq:squares)
            for(Square s:sq)
                System.out.println(s.getScore());
    }
    int count=0;


    private void positionChecker(Square[][] squares) {
        possibleMoves.clear();
        System.out.println("Call AI");
        for (Square[] sq : squares) //adding every possible move to array
            for (Square s : sq)
            {

                if(s.getPiece().getType().equals("empty"))
                    continue;
                if (s.getPiece().isWhite() != isWhite)
                {
                    s.clearScore();
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
}
