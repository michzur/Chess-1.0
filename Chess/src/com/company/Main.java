package com.company;
import com.company.Board.Board;
import javax.swing.JFrame;

public class Main  extends JFrame {

    public static void main(String[] args) {
        String defaultPosition="rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String test="8/8/8/3K4/7k/8/6R1/5R2 w - - 0 1";
        String rookChecker="r3k2r/8/8/8/8/8/8/R3K2N w KQkq - 0 1";
        String castlerChecker="r3k2r/ppp1ppbp/2nqb1pn/3p4/2B1P3/1PNP1N2/PBP1QPPP/R3K2R w KQkq - 0 1";
         Board board=new Board(defaultPosition,false);
            }


        }






