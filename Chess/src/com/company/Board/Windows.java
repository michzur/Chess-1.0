package com.company.Board;

import com.company.Pieces.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Windows extends JFrame {
Board board;
    public Windows(String title,Board board){
        super(title);
        this.board=board;
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
        setupFrame(this); // this = Board extending JFrame
    }
    public void loadFen(String fen, Square[][]squares, HashMap<Boolean,Square> kingsMap) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                Square sq = new Square(i, j, new Empty());
                squares[i][j] = sq;
            }
        String fenBoard = fen.split(" ")[0];
        int i = 0, j = 0;
        for (char symbol : fenBoard.toCharArray())
            if (symbol == '/') {
                j = 0;
                i++;
            } else if (Character.isDigit(symbol))
                j += Character.getNumericValue(symbol);
            else {
                Piece p = null;
                switch (symbol) {
                    case 'K' -> p = new King(true);
                    case 'P' -> p = new Pawn(true);
                    case 'N' -> p = new Knight(true);
                    case 'B' -> p = new Bishop(true);
                    case 'R' -> p = new Rook(true);
                    case 'Q' -> p = new Queen(true);

                    case 'k' -> p = new King(false);
                    case 'p' -> p = new Pawn(false);
                    case 'n' -> p = new Knight(false);
                    case 'b' -> p = new Bishop(false);
                    case 'r' -> p = new Rook(false);
                    case 'q' -> p = new Queen(false);
                }
                squares[i][j].setPiece(p);

                if (p.getType().equals("Pawn")
                        && i != (p.isWhite() ? 6 : 1))
                    p.setMoved(true);

                if (p.getType().equals("King"))
                    kingsMap.put(p.isWhite(), squares[i][j]);
                j++;
            }
    }

    public void display(Square[][]squares) {
        for (Square[] sq : squares)
            for (Square s : sq)
            {
                s.setIcon(s.getPiece().getImageIcon());
                    s.setBackground(s.background);
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
        System.out.println("Creating new Board...");
        this.dispose(); // closing window
        board=new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");    // initiating new Board
    }
    private void reset(String fen) {
        this.dispose(); // closing window
        board=new Board(fen);    // initiating new Board from fen string
    }
    private JMenuItem createItem(String title,JMenu menu){
        JMenuItem menuItem = new JMenuItem(title,
                new ImageIcon("images/newfile.png"));
        menu.add(menuItem);
        return menuItem;
    }
    private void undo(){
        board.unMakeMove(board.getLastMove());
        board.display();
    }
    public void menuBar(){
        JMenu menu = new JMenu("Menu");
        JButton undo = new JButton("undo");
        menu.setMnemonic(KeyEvent.VK_F);
// create menu item and add it to the menu
        JMenuItem reset=createItem("Reset",menu);
        reset.addActionListener(e ->reset());
        JMenuItem loadFEN=createItem("loadFEN",menu);
        loadFEN.addActionListener(e -> reset(JOptionPane.showInputDialog("enter Fen String")));
        undo.addActionListener( e -> undo());
        undo.setFocusable(false);
        undo.setBackground(Color.GRAY);
        undo.setForeground(Color.WHITE);
        JMenuBar menuBar=new JMenuBar();
        menuBar.add(menu);
        menuBar.add(undo);
        this.setJMenuBar(menuBar);

        this.pack();
    }
}
