package com.company.Pieces;

import com.company.Board.Board;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Windows extends JFrame {

    public Windows(String title){
        super(title);

}
    private void setupFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);   // centring window
        frame.setVisible(true);
    }
    public void setupWindow(Square[][]squares,MouseListener handler) {
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
    public void loadFen(String fen, Square[][]squares, HashMap<Boolean,Square> kingsMap){
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
            {
                Square sq=new Square(i, j, new Empty());
                sq.setupColors();
                squares[i][j] =sq;
            }
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
                    squares[i][j].setPiece(p);

                    if(p.getType().equals("King"))
                        kingsMap.put(p.isWhite(),squares[i][j]);

                    j++;
                }
            }
        }
    }
    public void display(Square[][]squares) { // resets background color and setting up images
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
    public void winWindow(String text) {
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
    private void reset() {
        this.dispose(); // closing window
        Board board=new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");    // initiating new Board
    }
    private void reset(String fen) {
        this.dispose(); // closing window
        Board board=new Board(fen);    // initiating new Board
    }
    private JMenuItem createItem(String title,JMenu menu){
        JMenuItem menuItem = new JMenuItem(title,
                new ImageIcon("images/newfile.png"));
        menu.add(menuItem);
        return menuItem;
    }
    public void menuBar(){
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
