package game;

import gui.GuiScreen;

import javax.swing.*;
import java.awt.*;
import java.util.*;


/*
 * A snake is made up of one or more SnakeSegment. The first SnakeSegment is the "head"
 * of the snake . The last is the "tail". As the snake moves, it adds one cell to the head
 * and then removes one from the tail
 * if the snake eats food, the head adds one cell but the tail will not shrink
 * */
public class Snake {
    private static final int INIT_LENGTH = 3; //snake's cells
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private Color color = Color.decode("0x89BF11"); // color for the snake body
    private Color colorHead = Color.decode("0x3F919E"); // color for the head
    private Snake.Direction direction; // get the current direction of the snake's head

    // the snake segments that forms the snake
    private java.util.List<SnakeSegment> snakeSegments = new ArrayList<SnakeSegment>();

    private boolean dirUpdatePending; //Pending update for a direction change?

    private Random random = new Random(); // randomly regenerating a snake

    private ImageIcon iconSnakeUp = new ImageIcon(getClass().getResource("/images/snake_up.png"), "UP");
    private ImageIcon iconSnakeDown = new ImageIcon(getClass().getResource("/images/snake_down.png"), "DOWN");
    private ImageIcon iconSnakeLeft = new ImageIcon(getClass().getResource("/images/snake_left.png"), "LEFT");
    private ImageIcon iconSnakeRight = new ImageIcon(getClass().getResource("/images/snake_right.png"), "RIGHT");

    //Regenerate the snake
    public void regenerate() {
        snakeSegments.clear();
        //Randomly generate a snake inside a pit
        int length = INIT_LENGTH; // 3 cells
        int headX = random.nextInt(GuiScreen.COLUMNS - length * 2) + length;
        int headY = random.nextInt(GuiScreen.ROWS - length * 2) + length;
        direction = Snake.Direction
                .values()[random.nextInt(Snake.Direction.values().length)];
        snakeSegments.add(new SnakeSegment(headX, headY, length, direction));
        dirUpdatePending = false;

    }

    //Change the direction of the snake, but no 180 degree turn allowed
    public void setDirection(Snake.Direction newDir) {
        // Ignore if there is a direction change pending and no 180 degree turn
        if(!dirUpdatePending
                && (newDir != direction)
                &&((newDir == Snake.Direction.UP && direction != Snake.Direction.DOWN)
                ||(newDir == Snake.Direction.DOWN && direction != Snake.Direction.UP)
                ||(newDir == Snake.Direction.LEFT && direction != Snake.Direction.RIGHT)
                ||(newDir == Snake.Direction.RIGHT && direction != Snake.Direction.LEFT
        ))) {
            SnakeSegment headSegment = snakeSegments.get(0);
            int x = headSegment.getHeadX();
            int y = headSegment.getHeadY();
            //add a new segment with zero length as the new head segment
            snakeSegments.add(0, new SnakeSegment(x, y, 0, newDir));
            direction = newDir;
            dirUpdatePending = true; //will be cleared after updated
        }

    }

    //Move the snake by one step. The snake "head" segment grows by one cell
    //the rest of the segments remain unchanged. The "tail" will later be shrink
    //if collision detected
    public void update() {
        SnakeSegment headSegment = snakeSegments.get(0);
        headSegment.grow();
        dirUpdatePending = false; //can process the key input again
    }

    //Not eaten a food item. Shrink the tail by one cell
    public void shrink() {
        SnakeSegment tailSegment = snakeSegments.get(snakeSegments.size()-1);
        tailSegment.shrink();
        if(tailSegment.getLength() == 0) snakeSegments.remove(tailSegment);
    }

    //Get the X,Y coordinate of the cell that contains the snake's head segment
    public int getHeadX() {
        return snakeSegments.get(0).getHeadX();
    }
    public int getHeadY() {
        return snakeSegments.get(0).getHeadY();
    }

    // Returns true if the snake contains the given (x,y) cell, Used in collision dectection
    public boolean contains(int x, int y) {
        for(int i =0; i< snakeSegments.size(); ++i) {
            SnakeSegment segment = snakeSegments.get(i);
            if(segment.contains(x,y))return true;
        }
        return false;
    }

    // return true if the snake eats itself
    public boolean eatItself() {
        int headX = getHeadX();
        int headY = getHeadY();
        //eat itself if the headX, headY hits its body segment (4th onwards)
        for(int i =3; i <snakeSegments.size(); ++i) {
            SnakeSegment segment = snakeSegments.get(i);
            if(segment.contains(headX, headY)) return true;
        }
        return false;
    }

    // Draw itself
    public void draw(Graphics g) {
        g.setColor(color);
        for(int i = 0; i< snakeSegments.size(); ++i) {
            snakeSegments.get(i).draw(g); //draw all the segments
        }

        if(snakeSegments.size() > 0) {
            g.setColor(colorHead);
            g.fillOval(
                    getHeadX() * GuiScreen.CELL_SIZE + 2,
                    getHeadY() * GuiScreen.CELL_SIZE + 2,
                    GuiScreen.CELL_SIZE - 4,
                    GuiScreen.CELL_SIZE - 4
            );
            switch (direction){
                case UP:
                    iconSnakeUp.paintIcon(null, g, getHeadX() * GuiScreen.CELL_SIZE, getHeadY()* GuiScreen.CELL_SIZE);
                    break;
                case DOWN:
                    iconSnakeDown.paintIcon(null, g, getHeadX() * GuiScreen.CELL_SIZE, getHeadY()* GuiScreen.CELL_SIZE);
                    break;
                case LEFT:
                    iconSnakeLeft.paintIcon(null, g, getHeadX() * GuiScreen.CELL_SIZE, getHeadY()* GuiScreen.CELL_SIZE);
                    break;
                case RIGHT:
                    iconSnakeRight.paintIcon(null, g, getHeadX() * GuiScreen.CELL_SIZE, getHeadY()* GuiScreen.CELL_SIZE);
                    break;
            }
        }

    }
}
