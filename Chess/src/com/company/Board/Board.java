package com.company.Board;
import com.company.Pieces.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Board  extends JFrame {
    private Square[][] squares; // Square is a JButton extension containing Piece
    private boolean isWhiteTurn=true;
    private boolean selected = false;
    private ArrayList<Square> possibleMoves = new ArrayList<>();
    private final HandlerClass handler = new HandlerClass(); // mouse select detection
    private King currentKing;
    private Square currentKingSquare,start,end;
    public Board() {
        super("Chess");
        initiatingPieces();
        setupWindow();
        display();
        positionChecker(); // adding every possible move to array and looking for currentKing
    }

    private void initiatingPieces() {
        squares = new Square[8][8];
        squares[7][4] = new Square(7, 4, new King(true, "Pictures\\whiteKing.png"));
        squares[0][4] = new Square(0, 4, new King(false, "Pictures\\blackKing.png"));

        squares[7][7] = new Square(7, 7, new Rook(true, "Pictures\\whiteRook.png"));
        squares[7][0] = new Square(7, 0, new Rook(true, "Pictures\\whiteRook.png"));
        squares[0][7] = new Square(0, 7, new Rook(false, "Pictures\\blackRook.png"));
        squares[0][0] = new Square(0, 0, new Rook(false, "Pictures\\blackRook.png"));
        for (int i = 0; i < 8; i++)
        {
            squares[6][i] = new Square(6, i, new Pawn(true, "Pictures\\whitePawn.png"));
            squares[1][i] = new Square(1, i, new Pawn(false, "Pictures\\blackPawn.png"));
        }
        squares[7][6] = new Square(7, 6, new Knight(true, "Pictures\\whiteKnight.png"));
        squares[7][1] = new Square(7, 1, new Knight(true, "Pictures\\whiteKnight.png"));
        squares[0][6] = new Square(0, 6, new Knight(false, "Pictures\\blackKnight.png"));
        squares[0][1] = new Square(0, 1, new Knight(false, "Pictures\\blackKnight.png"));
        squares[7][5] = new Square(7, 5, new Bishop(true, "Pictures\\whiteBishop.png"));
        squares[7][2] = new Square(7, 2, new Bishop(true, "Pictures\\whiteBishop.png"));
        squares[0][5] = new Square(0, 5, new Bishop(false, "Pictures\\blackBishop.png"));
        squares[0][2] = new Square(0, 2, new Bishop(false, "Pictures\\blackBishop.png"));
        squares[7][3] = new Square(7, 3, new Queen(true, "Pictures\\whiteQueen.png"));
        squares[0][3] = new Square(0, 3, new Queen(false, "Pictures\\blackQueen.png"));

        for (int i = 2; i < 6; i++)
            for (int j = 0; j < 8; j++)
                squares[i][j] = new Square(i, j, new Empty());
    }
    private void setupWindow() {
        JPanel chessBoardPanel = new JPanel(new GridLayout(0, 9));
        chessBoardPanel.setBorder(new LineBorder(Color.BLACK));
        final String cols = " ABCDEFGH";
        // Creating 8x8 JButton grid filled with black and white squares with MouseMotionListener from HandlerClass each
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 9; j++)
            {
                if (j == 0) // adding numbers on left side
                    chessBoardPanel.add(new JLabel("" + (8 - i),
                            SwingConstants.CENTER));
                else
                { // setting Squares buttons and adding them to main panel
                    Square b = squares[i][j - 1]; // j-1 because of number setting
                    b.setIcon(new ImageIcon( // making transparent image
                            new BufferedImage(65, 65, BufferedImage.TYPE_INT_ARGB)));

                    b.setPreferredSize(new Dimension(65, 65));
                    b.addMouseListener(handler);
                    chessBoardPanel.add(b);
                }
            }

        for (int i = 0; i < cols.length(); i++) // setting Cols ABC... at the bottom
            chessBoardPanel.add(new JLabel(cols.substring(i, i + 1), SwingConstants.CENTER));
        this.add(chessBoardPanel);
        setupFrame(this); // this = Board extends JFrame
    }
    private void setupFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);   // centring window
        frame.setVisible(true);
    }

    private void selectFigure(Square currentSq) {
        // Main function for selecting pieces after handlerClass passes clicked square
        start = currentSq;
        display();
        selected = true;
        start.getPiece().showPossibleMoves(start, squares);
    }

    private void moveFigure(Square currentSq) {
        // Main function for moving pieces after handlerClass passes clicked square
        end = currentSq;
        if (start.getPiece().canMove(start, end, squares))
            endOfPlayerTurn();
    }

    private Square getCurrentSq(MouseEvent e) {
        // used by handlerClass to detect current clicked Square
        Object source = e.getSource();
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
                // changeToEmptySquare(i,j);
                if (source == squares[i][j])
                    return squares[i][j];
        }
        return null;
    }

    private void display() {
        for (int i = 0; i < squares.length; i++)
            for (int j = 0; j < squares.length; j++)
            { // resets background color and setting up images
                Pieces currentPiece=squares[i][j].getPiece();
                Square currentSQ=squares[i][j];
                Color color = (i + j) % 2 == 0 ? Color.WHITE : Color.BLACK;
                currentSQ.setBackground(color);
                if (!currentPiece.getType().equals("empty"))
                    currentSQ.setIcon(currentPiece.getImageIcon());
                else
                    currentSQ.setEmptyIcon();
            }
    }
    private void changePawnsMoved() {
        // changing pawn's movedTwoTilesThisRound after one round
        for (Square[] sq : squares)
            for (Square s : sq)
                if (s.getPiece().getHasMovedTwoTiles()
                        && s.getPiece().isWhite() == isWhiteTurn)
                    s.getPiece().changehasMovedTwoTiles();
    }
    private void endOfPlayerTurn() {
        if (!currentKing.isInCheckAfterMove(start, end, squares))
        {   // making move only if King is not in danger
            start.getPiece().setMoved(true);
            squares[end.getI()][end.getJ()].setPiece(start.getPiece());
            squares[start.getI()][start.getJ()].setPiece(new Empty());
            isWhiteTurn = !isWhiteTurn;
            changePawnsMoved(); //TODO move pawn changing to position checker
            selected = false;
            display();
        }
        positionChecker();// adding every possible move to array and looking for currentKing
        if (currentKing.isMate(currentKingSquare, squares, possibleMoves))
        {
            String winner = isWhiteTurn ? "Black" : "White";
            winWindow("    Check Mate " + winner + " has won! ");
        }
    }

    private void winWindow(String text) {
        JFrame frame = new JFrame(text);
        frame.setAlwaysOnTop(true);
        frame.setSize(new Dimension(250,100));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);

        JButton newGame=new JButton("New Game");
        newGame.addActionListener(e -> { // on click reset game and close window
            reset();
            frame.dispose();
            });
        newGame.setFocusable(false);
        newGame.setBackground(Color.lightGray);

        JButton exit=new JButton("Exit");
        exit.addActionListener(e -> System.exit(0));// on click stopping the program
        exit.setFocusable(false);
        exit.setBackground(Color.lightGray);

        JLabel label=new JLabel(text); // label with winner text
        JPanel panel=new JPanel();
        panel.add(label);
        panel.add(newGame);
        panel.add(exit);
        frame.add(panel);
    }

    private void positionChecker() {
        possibleMoves.clear();
        for (Square[] sq : squares) //adding every possible move to array
            for (Square s : sq)
            {
                if (!s.getPiece().getType().equals("empty"))
                    s.getPiece().addPossibleMoves(s, squares, possibleMoves);

                if (s.getPiece().getType().equals("King"))
                    if (s.getPiece().isWhite() == isWhiteTurn)
                    {   // setting king Square
                        currentKing = new King(s.getPiece());
                        currentKingSquare = s;
                    }
            }
    }

    private void reset() {
        this.dispose(); // closing window
        Board board=new Board();    // initiating new Board
    }

    private class HandlerClass implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            Square currentSq = getCurrentSq(e);
            if (!currentSq.getPiece().getType().equals("empty")
                    &&currentSq.getPiece().isWhite() == isWhiteTurn)
            {   // selecting only Square of player turn
                selectFigure(currentSq);
            }   // if proper Piece is selected, move Piece
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

}

