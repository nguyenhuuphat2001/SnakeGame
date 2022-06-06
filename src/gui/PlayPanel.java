package gui;

import game.Food;
import game.Snake;
import game.SoundEffect;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//main class for the game
public class PlayPanel extends JPanel{

    //Enumeration for the statas of the game
    enum GameState {
        INITIALIZED, PLAYING, PAUSED, GAMEOVER, DESTROYED
    }

    //Declare menubar
    static JMenuBar menuBar;

    // current state of the game
    static GameState state;

    //Define instance variables for the game objects
    private Food food;
    private Snake snake;

    // Handle for the custom drawing panel and UI components
    private BoardGame board;
    private ControlPanel control;
    private JLabel lblScore;
    private int level;
    private int score = 0;

    private JButton btnExit;

    // Constructor to init the UI components and game object
    public PlayPanel() {

    }

    public PlayPanel(int level) {
        //init the game objects
        gameInit();
        //set level
        this.level = level;
        // create UI components
        setLayout(new BorderLayout());
        //drawing panel
        board = new BoardGame();
        board.setPreferredSize(new Dimension(GuiScreen.BOARD_WIDTH,GuiScreen.BOARD_HEIGHT));
        add(board, BorderLayout.SOUTH);
        //control panel to the bottom
        control = new ControlPanel();
        add(control, BorderLayout.CENTER);

        // start the game
        gameStart();
    }

    // init all the game objects, run only once in the constructor of the main class
    public void gameInit() {
        //Allocate a new snake and a food item, do not regenerate
        snake = new Snake();
        food = new Food();
        state = GameState.INITIALIZED;
    }

    // to start and restart the game
    public void gameStart() {
        // Create a new thread
        Thread gameThread = new Thread() {
            //Override run() to provide the running behavior of this thread
            public void run() {
                gameLoop();
            }
        };
        //Start the thread.start() calls run, which in turn calls gameLoop()
        gameThread.start();
    }

    // run the game loop here
    private void gameLoop() {
        //Regenerate and reset the game objects for a new game
        if(state == GameState.INITIALIZED || state == GameState.GAMEOVER) {
            //Generate a new snake and a food item
            snake.regenerate();

            //regenerate if food placed under the snake
            int x, y;
            do {
                food.regenerate();
                x = food.getX();
                y = food.getY();
            }while(snake.contains(x,y));

            state = GameState.PLAYING;

        }
        //Game loop
        long beginTime, timeTaken, timeLeft; //in msec
        while(state != GameState.GAMEOVER) {
            beginTime = System.nanoTime();
            if(state == GameState.PLAYING) {
                //update the state and position of all the game objects
                //detect collisions and provide responses
                gameUpdate();
            }
            // Refresh the display
            repaint();
            //Delay timer to provide the necessary delay to meet the target rate
            timeTaken = System.nanoTime() - beginTime;
            // in milliseconds
            timeLeft = (GuiScreen.UPDATE_PERIOD_NSEC - timeTaken)/ 1000000;
            if(timeLeft < 10) timeLeft = 10; //set a minium
            try {
                //Provides the necessary delay and also yields control
                //so that other thread can do work
                Thread.sleep(timeLeft/level);
            }catch(InterruptedException ex) {}
        }
    }

    //update the state and position of all the game objects
    // detect collisions and provide responses
    public void gameUpdate() {
        snake.update();
        processCollision();
    }

    //Collision detection and response
    public void processCollision() {
        // check if this snake eats the food item
        int headX = snake.getHeadX();
        int headY = snake.getHeadY();

        if(headX == food.getX() && headY == food.getY()) {
            // to play a specific sound
            SoundEffect.EAT.play();
            score = score + (1*level);
            lblScore.setText("Score: "+score);

            //food eaten, regenerate one
            int x, y;
            do {
                food.regenerate();
                x = food.getX();
                y = food.getY();
            }while(snake.contains(x, y));
        }else {
            //not eaten, shrink the tail
            snake.shrink();
        }

        // Check if the snake moves out of bounds
        if(!board.contains(headX, headY)) {
            state = GameState.GAMEOVER;
            // to play a specific sound
            SoundEffect.DIE.play();
//            score = 0;
//            lblScore.setText("Score: "+score);
            return;
        }

        // Check if the snake eats itself
        if(snake.eatItself()) {
            state = GameState.GAMEOVER;
            // to play a specific sound
            SoundEffect.DIE.play();
//            score = 0;
//            lblScore.setText("Score: "+score);
            return;
        }
    }

    // Refresh the display. Called back via repaint(), which invoke the paintComponent()
    private void gameDraw(Graphics g) {
        //draw game objects
        snake.draw(g);
        food.draw(g);

        if(state == GameState.GAMEOVER) {
            g.setFont(new Font("Verdana", Font.BOLD, 30));
            g.setColor(Color.RED);
            g.drawString("GAME OVER!", 200, GuiScreen.BOARD_HEIGHT / 2);
        }

    }

    //Process a key-pressed event. Update the current state
    public void gameKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.Direction.RIGHT);
                break;
        }
    }


    //Game Control Panel with Start, Stop, Pause, Mute and Exit buttons, designed as an inner class
    class ControlPanel extends JPanel {
        private JButton btnStartPause;
        private JButton btnStop;
        private JButton btnMute;

        //import icons for buttons
        private ImageIcon iconStart = new ImageIcon(getClass().getResource("/images/start.png"), "START");
        private ImageIcon iconPause = new ImageIcon(getClass().getResource("/images/pause.png"), "PAUSE");private ImageIcon iconStop = new ImageIcon(getClass().getResource("/images/stop.png"), "STOP");
        private ImageIcon iconSound = new ImageIcon(getClass().getResource("/images/sound.png"), "SOUND ON");
        private ImageIcon iconMuted = new ImageIcon(getClass().getResource("/images/muted.png"), "MUTED");
        private ImageIcon iconExit = new ImageIcon(getClass().getResource("/images/exit.png"), "EXIT");


        public ControlPanel () {
            this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            btnStartPause = new JButton(iconPause);
            btnStartPause.setToolTipText("Pause");
            btnStartPause.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStartPause.setEnabled(true);
            add(btnStartPause);

            btnStop = new JButton(iconStop);
            btnStop.setToolTipText("Stop");
            btnStop.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnStop.setEnabled(true);
            add(btnStop);

            btnMute = new JButton(iconSound);
            btnMute.setToolTipText("Mute");
            btnMute.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMute.setEnabled(true);
            add(btnMute);

            btnExit = new JButton(iconExit);
            btnExit.setToolTipText("Exit");
            btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnExit.setEnabled(true);
            add(btnExit);

            JLabel lblSpace = new JLabel("    ");
            add(lblSpace);

            lblScore = new JLabel("Score: 0");
            lblScore.setFont(new Font("Arial",3,20));
            lblScore.setForeground(Color.red);

            add(lblScore);

            //handle click events on buttons
            btnStartPause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch(state) {
                        case INITIALIZED:
                        case GAMEOVER:
                            btnStartPause.setIcon(iconPause);
                            btnStartPause.setToolTipText("Pause");
                            gameStart();
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            score = 0;
                            lblScore.setText("Score: "+score);
                            break;
                        case PLAYING:
                            state = GameState.PAUSED;
                            btnStartPause.setIcon(iconStart);
                            btnStartPause.setToolTipText("Start");
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            break;
                        case PAUSED:
                            state = GameState.PLAYING;
                            btnStartPause.setIcon(iconPause);
                            btnStartPause.setToolTipText("Pause");
                            //To play a specific sound
                            SoundEffect.CLICK.play();
                            break;
                    }
                    btnStop.setEnabled(true);
                    board.requestFocus();

                }
            });

            btnStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = GameState.GAMEOVER;
                    btnStartPause.setIcon(iconStart);
                    btnStartPause.setEnabled(true);
                    btnStop.setEnabled(false);
                    //To play a specific sound
                    SoundEffect.CLICK.play();

                }
            });

            btnMute.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(SoundEffect.volume == SoundEffect.Volume.MUTE) {
                        SoundEffect.volume = SoundEffect.Volume.LOW;
                        btnMute.setIcon(iconSound);
                        //To play a specific sound
                        SoundEffect.CLICK.play();
                        board.requestFocus();
                    }else {
                        SoundEffect.volume = SoundEffect.Volume.MUTE;
                        btnMute.setIcon(iconMuted);
                        //To play a specific sound
                        SoundEffect.CLICK.play();
                        board.requestFocus();
                    }

                }
            });

            btnExit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    state = GameState.GAMEOVER;
                    SoundEffect.CLICK.play();
                }
            });
        }

        // Reset control for a new game
        public void reset() {
            btnStartPause.setIcon(iconStart);
            btnStartPause.setEnabled(true);
            btnStop.setEnabled(false);
        }
    }

    // Custom drawing panel, written as an inner class
    class BoardGame extends JPanel implements KeyListener {

        //constructor
        public BoardGame() {
            setFocusable(true); //so that can receive key-events
            requestFocus();
            addKeyListener(this);
        }

        //overwrite paintComponent to do custom drawing
        //called back by repaint()
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //paint background, may use an image for background
            //set background color
            setBackground(Color.decode("0x3F919E"));
            g.setColor(Color.decode("0xB9BAB1"));

            for(int i = 0; i < GuiScreen.COLUMNS; i++)
                drawRectOneCell(g, i,0);
            for(int i = 1; i < GuiScreen.ROWS - 1; i++){
                for(int j = 0; j < GuiScreen.COLUMNS; j++){
                    if(j == 0 || j == GuiScreen.ROWS-1){
                        drawRectOneCell(g, j, i);
                    }
                }
            }
            for(int i = 0; i < GuiScreen.COLUMNS; i++)
                drawRectOneCell(g, i,GuiScreen.ROWS-1);

            //draw the game objects
            gameDraw(g);
        }

        private void drawRectOneCell(Graphics g,int x, int y){
            g.fillRect(
                    x * GuiScreen.CELL_SIZE,
                    y * GuiScreen.CELL_SIZE,
                    GuiScreen.CELL_SIZE,
                    GuiScreen.CELL_SIZE);
        }

        //KeyEvent handlers
        @Override
        public void keyPressed(KeyEvent e) {
            gameKeyPressed(e.getKeyCode());

        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub

        }

        // Check if this pit contains the given(x,y) for collision detection
        public boolean contains (int x, int y) {
            if((x<2)|| (x>=GuiScreen.ROWS - 2)) return false;
            if((y<2)|| (y>=GuiScreen.COLUMNS - 2)) return false;
            return true;
        }
    }
    public void addButtonExitActionListener(ActionListener listener) {
        btnExit.addActionListener(listener);
    }
}