package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiScreen {

    //Define constants for the game
    static final String TITLE = "Snake game";
    //number of rows (in cells)
    public static final int ROWS = 25;
    //number of columns (in cells)
    public static final int COLUMNS = 25;
    //Size of a cell (in pixels)
    public static final int CELL_SIZE = 24;
    //width and height of the game screen
    static final int BOARD_WIDTH = COLUMNS * CELL_SIZE;
    static final int BOARD_HEIGHT = ROWS * CELL_SIZE;
    //number of game update per second = 10;
    static final int UPDATE_PER_SEC = 4;
    //per nanoseconds
    static final long UPDATE_PERIOD_NSEC = 1000000000L / UPDATE_PER_SEC;

    public static enum STATE{
        MENU,
        GAME
    }
    public static GuiScreen.STATE state = GuiScreen.STATE.MENU;

    private MainMenuPanel menu = new MainMenuPanel();
    private PlayPanel game = new PlayPanel();

    public static enum LEVEL{
        EASY,
        NORMAL,
        HARD
    }
    public static GuiScreen.LEVEL level = GuiScreen.LEVEL.EASY;

    public GuiScreen(){
        JFrame frame = new JFrame(TITLE);
        frame.setBounds(150, 150, ROWS*CELL_SIZE, (COLUMNS+3)*CELL_SIZE);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        render(frame);
        frame.setVisible(true);
    }

    private void render(JFrame frame){
        if(state == STATE.MENU){
            menu = new MainMenuPanel();
            menu.addButtonPlayActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = STATE.GAME;
                    render(frame);
                }
            });
            frame.add(menu);
            game.setVisible(false);
            menu.setVisible(true);
        }else if(state == STATE.GAME){
            if(level == LEVEL.EASY){
                game = new PlayPanel(1);
            } else if(level == LEVEL.NORMAL){
                game = new PlayPanel(2);
            } else{
                game = new PlayPanel(3);
            }
            game.addButtonExitActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = STATE.MENU;
                    render(frame);
                }
            });
            frame.add(game);
            menu.setVisible(false);
            game.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new GuiScreen();
    }
}

