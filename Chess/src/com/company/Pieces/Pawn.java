package com.company.Pieces;

public class Pawn extends Pieces {
    public Pawn(boolean white, String iconDir) {
        super(white, iconDir);
        type = "Pawn";
    }

    public void showPossibleMoves(Square start, Square[][] squares) {
        simulatingMoves = true;
        for (Square[] sq : squares)
            for (Square s : sq)
                if (canMove(start, s, squares))
                    s.setColloredIcon();
        simulatingMoves = false;
    }
    private void promote(Square start) {
        String icondir = isWhite() ? "Pictures\\whiteQueen.png" : "Pictures\\blackQueen.png";
        start.setPiece(new Queen(isWhite(), icondir));
    }

    public boolean canCapture(Square start, Square end) { // used only for King moves (default capture is in canMove)
        int nMove = isWhite() ? 1 : -1;
        return (start.getI() - end.getI() == nMove && Math.abs(start.getJ() - end.getJ()) == 1) &&
                (end.getPiece().isWhite() != isWhite());
    }

    public boolean canMove(Square start, Square end, Square[][] squares) {
        int nMove = isWhite() ? 1 : -1;
        int promoteSquare = isWhite() ? 0 : 7;
        // MOVE
        if (end.getPiece().getType().equals("empty"))
        {
            if (Math.abs(start.getI() - end.getI()) < 3 && start.getJ() == end.getJ())
            {
                if ((start.getI() - end.getI() + -nMove == nMove)
                        && !moved
                        && squares[start.getI() + -nMove][start.getJ()].getPiece().getType().equals("empty"))
                {   // 2 square move
                    if (!simulatingMoves) hasMovedTwoTilesThisRound = true;
                    return true;
                }
                if (start.getI() - end.getI() == nMove)
                { // normal move
                    if (!simulatingMoves && end.getI() == promoteSquare)
                        promote(start); // promoting if pawn end up on last rank

                    return true;
                }
            }
            if ((squares[start.getI()][end.getJ()].getPiece().hasMovedTwoTilesThisRound)
                    && (start.getI() - end.getI() == nMove && Math.abs(start.getJ() - end.getJ()) == 1))
            {   //en passant
                if (!simulatingMoves) squares[start.getI()][end.getJ()].setPiece(new Empty());
                return true;
            }
            return false;
        }
        // Capture
        else if (start.getI() - end.getI() == nMove && Math.abs(start.getJ() - end.getJ()) == 1
                && end.getPiece().isWhite() != isWhite())
        {
            if (!simulatingMoves && end.getI() == promoteSquare)
                promote(start); // promoting if pawn end up on last rank

            return true;
        }
        return false;
    }
}







