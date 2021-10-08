package com.company.Board;
import com.company.Pieces.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Board  extends JFrame {

    private boolean isWhiteTurn=true;
    private boolean selected = false;
    private Square[][] squares; // Square is a JButton extension containing Piece
    HashMap<Boolean,Square> kingsMap=new HashMap<>();
    HashMap<Square,Pieces> boardMap=new HashMap<>();
    private final HandlerClass handler = new HandlerClass(); // mouse detection
    Stack<Square[]> moves=new Stack<>(); // @TODO history implementation
    Square start,end;

    String fenStarting="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public Board() {
        super("Chess");
        fenImplementation(fenStarting);
        setupWindow();
        display();

    }

    private void fenImplementation(String fen){
        squares = new Square[8][8];
        for(int i=0;i<8;i++)
        for(int j=0;j<8;j++)
           squares[i][j] = new Square(i, j, new Empty());
     /*   HashMap<Character,Pieces> pieceTypeSymbol=new HashMap<>(){{
            put('k',new King(true,"Pictures\\whiteKing.png"));
            put('p',new Pawn(true,"Pictures\\whitePawn.png"));
            put('n',new Knight(true,"Pictures\\whiteKnight.png"));
            put('b',new Bishop(true,"Pictures\\whiteBishop.png"));
            put('r',new Rook(true,"Pictures\\whiteRook.png"));
            put('q',new Queen(true,"Pictures\\whiteQueen.png"));

            put('K',new King(false,"Pictures\\blackKing.png"));
            put('P',new Pawn(false,"Pictures\\blackPawn.png"));
            put('N',new Knight(false,"Pictures\\blackKnight.png"));
            put('B',new Bishop(false,"Pictures\\blackBishop.png"));
            put('R',new Rook(false,"Pictures\\blackRook.png"));
            put('Q',new Queen(false,"Pictures\\blackQueen.png"));
      }};
      */

        String fenBoard=fen.split(" ")[0];
        int i=7,j=0,jump=0;
        for(char symbol:fenBoard.toCharArray()) {
            if (symbol == '/') {
                j = 0;
                i--;
            } else {
                if (Character.isDigit(symbol))
                 j+=Character.getNumericValue(symbol);
                 else {
                    Pieces p=null;
                    switch (symbol)
                    {
                        case 'k' -> p=new King(true,"Pictures\\whiteKing.png");
                        case 'p' -> p=new Pawn(true,"Pictures\\whitePawn.png");
                        case 'n' -> p=new Knight(true,"Pictures\\whiteKnight.png");
                        case 'b' -> p=new Bishop(true,"Pictures\\whiteBishop.png");
                        case 'r' -> p=new Rook(true,"Pictures\\whiteRook.png");
                        case 'q' -> p=new Queen(true,"Pictures\\whiteQueen.png");

                        case 'K' -> p=new King(false,"Pictures\\blackKing.png");
                        case 'P' -> p=new Pawn(false,"Pictures\\blackPawn.png");
                        case 'N' -> p=new Knight(false,"Pictures\\blackKnight.png");
                        case 'B' -> p=new Bishop(false,"Pictures\\blackBishop.png");
                        case 'R' -> p=new Rook(false,"Pictures\\blackRook.png");
                        case 'Q' -> p=new Queen(false,"Pictures\\blackQueen.png");
                    }
                    squares[i][j]=(new Square(i,j,p));

                    if(p.getType().equals("King"))
                        kingsMap.put(p.isWhite(),squares[i][j]);

                    j++;
                }
            }
        }
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
    private void display() { // resets background color and setting up images
        for (int i = 0; i < squares.length; i++)
            for (int j = 0; j < squares.length; j++)
            {
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
        exit.addActionListener(e -> System.exit(0));// on click stop the program
        exit.setFocusable(false);
        exit.setBackground(Color.lightGray);

      //  JLabel label=new JLabel(text); // label with winner text
        JPanel panel=new JPanel();
        panel.add(new JLabel(text));
        panel.add(newGame);
        panel.add(exit);
        frame.add(panel);
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

    private void changePawnsMoved() {
        // changing pawn's movedTwoTilesThisRound after one round using Stack
        if(moves.isEmpty())
            return;
           Square s=moves.peek()[1]; // move from last round
           s.getPiece().changehasMovedTwoTiles();
    }
    private void endOfPlayerTurn() {
        Square currentKingSquare=kingsMap.get(isWhiteTurn);
        King currentKing=(King)currentKingSquare.getPiece();
        if (!currentKing.isInCheckAfterMove(start, end, squares))
        {   // making move only if King is not in danger
            if(start.getPiece().getType().equals("King")) // updating kingsmap position
                kingsMap.put(isWhiteTurn,end);

            start.getPiece().setMoved(true);
            squares[end.getI()][end.getJ()].setPiece(start.getPiece());
            squares[start.getI()][start.getJ()].setPiece(new Empty());
            isWhiteTurn = !isWhiteTurn;
            changePawnsMoved();
            moves.push(new Square[]{start,end});
            selected = false;
            display();
        }
        if (currentKing.isMate(currentKingSquare, squares,new AI(isWhiteTurn,squares).getPossibleMoves()))
        {
            String winner = isWhiteTurn ? "Black" : "White";
            winWindow("    Check Mate " + winner + " has won! ");
        }

        if(!isWhiteTurn) {
            computerMove();
        }
    }

    private void computerMove() {
        AI ai=new AI(isWhiteTurn,squares);
        Square computerSquare=ai.randomMove();
        start = computerSquare;
        selected = true;
        end=computerSquare.getPossibleSquare();
        endOfPlayerTurn();
    }
    private void reset() {
        this.dispose(); // closing window
        Board board=new Board();    // initiating new Board
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
    /*private void initiatePieces() {
        squares = new Square[8][8];
        squares[7][4] = new Square(7, 4, new King(true, "Pictures\\whiteKing.png"));
        kingsMap.put(true,squares[7][4]);
        squares[0][4] = new Square(0, 4, new King(false, "Pictures\\blackKing.png"));
        kingsMap.put(false,squares[0][4]);
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


     */