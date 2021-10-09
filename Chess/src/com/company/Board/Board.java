package com.company.Board;
import com.company.Pieces.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Board  extends JFrame {

    private boolean isWhiteTurn=true;
    private boolean selected = false;
    private Square[][] squares; // Square is a JButton extension containing Piece
    HashMap<Boolean,Square> kingsMap=new HashMap<>();
    private final HandlerClass handler = new HandlerClass(); // mouse detection
    Stack<Square[]> moves=new Stack<>(); // @TODO history implementation
    Square start,end;
    private boolean isWhitePC;
    public Board(String fen) {
        super("Chess");
        loadFen(fen);
        setupWindow();
        display();
        menuBar();
    }
    public Board(String fen,boolean isWhitePC) {
        super("Chess");
        loadFen(fen);
        setupWindow();
        display();
        menuBar();
        this.isWhitePC=isWhitePC;
        if(isWhitePC==isWhiteTurn)
            computerMove();
    }

    private void loadFen(String fen){
        squares = new Square[8][8];
        for(int i=0;i<8;i++)
        for(int j=0;j<8;j++)
           squares[i][j] = new Square(i, j, new Empty());
        String fenBoard=fen.split(" ")[0];
        int i=0,j=0;
        for(char symbol:fenBoard.toCharArray()) {
            if (symbol == '/') {
                j = 0;
                i++;
            } else {
                if (Character.isDigit(symbol))
                 j+=Character.getNumericValue(symbol);
                 else {
                    Pieces p=null;
                    switch (symbol)
                    {
                        case 'K' -> p=new King(true,"Pictures\\whiteKing.png");
                        case 'P' -> p=new Pawn(true,"Pictures\\whitePawn.png");
                        case 'N' -> p=new Knight(true,"Pictures\\whiteKnight.png");
                        case 'B' -> p=new Bishop(true,"Pictures\\whiteBishop.png");
                        case 'R' -> p=new Rook(true,"Pictures\\whiteRook.png");
                        case 'Q' -> p=new Queen(true,"Pictures\\whiteQueen.png");

                        case 'k' -> p=new King(false,"Pictures\\blackKing.png");
                        case 'p' -> p=new Pawn(false,"Pictures\\blackPawn.png");
                        case 'n' -> p=new Knight(false,"Pictures\\blackKnight.png");
                        case 'b' -> p=new Bishop(false,"Pictures\\blackBishop.png");
                        case 'r' -> p=new Rook(false,"Pictures\\blackRook.png");
                        case 'q' -> p=new Queen(false,"Pictures\\blackQueen.png");

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(this);
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
        System.out.println(currentKing.isRookInLine(squares,currentKingSquare));
       // AI ai=new AI(isWhiteTurn,squares);
       // squares=ai.getSquares();


     //   if (currentKing.isSafeAfterMove(end))
      //  {   // making move only if King is not in danger
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
     //   }
        if (currentKing.isMate(currentKingSquare, squares,new AI(isWhiteTurn,squares).getPossibleMoves()))
        {
            String winner = isWhiteTurn ? "Black" : "White";
            winWindow("    Check Mate " + winner + " has won! ");
        }

        if(isWhiteTurn==isWhitePC) {
       //     computerMove();
        }
    }

    private void computerMove() {
        AI ai=new AI(isWhiteTurn,squares);
        if(ai.getPossibleMoves().size()==0)
        {
            winWindow("Winner winner chicken dinner");
            return;
        }
        Square computerSquare=ai.randomMove();
        start = computerSquare;
        selected = true;

        end=computerSquare.getPossibleSquare();
        endOfPlayerTurn();
    }
    private void reset() {
        this.dispose(); // closing window
        Board board=new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");    // initiating new Board
    }
    private void reset(String fen) {
        this.dispose(); // closing window
        Board board=new Board(fen);    // initiating new Board
    }
    private Square getCurrentSq(MouseEvent e){
        return (Square)e.getSource();
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
    private JMenuItem createItem(String title,JMenu menu){
        JMenuItem menuItem = new JMenuItem(title,
                new ImageIcon("images/newfile.png"));
        menu.add(menuItem);
    return menuItem;
    }

    private void menuBar(){
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_F);
     //   menu.getAccessibleContext().setAccessibleDescription("Dealing with Files");
// create menu item and add it to the menu
        JMenuItem reset=createItem("Reset",menu);
        reset.addActionListener(e ->reset());
        JMenuItem loadFEN=createItem("loadFEN",menu);
        loadFEN.addActionListener(e -> reset(JOptionPane.showInputDialog("enter Fen String")));
        JMenuBar menuBar=new JMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.pack();
    }
}