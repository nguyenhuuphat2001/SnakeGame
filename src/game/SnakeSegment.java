package game;
import gui.GuiScreen;

import javax.swing.*;
import java.awt.Graphics;
/*
 * SnakeSegment represents one horizontal or vertical segment of a snake
 * the head of this segment is at(headX, headY). The segment is drawn starting
 * from the "head" and proceeding "length" cells in "direction", until
 * it reaches the "tail"
 * */
public class SnakeSegment {
    private int headX, headY; //the position of the head's segment
    private int length; //length of this segment
    private game.Snake.Direction direction;

    public SnakeSegment(int headX, int headY, int length, game.Snake.Direction direction) {
        this.headX = headX;
        this.headY = headY;
        this.direction = direction;
        this.length = length;
    }

    //Grow by adding one cell to the head of this segment
    public void grow() {
        ++length;
        //need to adjust the headX and headY
        switch (direction) {
            case LEFT:
                --headX;
                break;
            case RIGHT:
                ++headX;
                break;
            case UP:
                --headY;
                break;
            case DOWN:
                ++headY;
                break;
        }
    }

    // Shrink by removing one cell from the tail of this segment
    public void shrink() {
        length--; //no change in headX and headY needed
    }

    //Get the segment's length
    public int getLength() {
        return length;
    }

    // Get the X, Y coordinate of the cell that contains the snake's head

    public int getHeadX() {
        return headX;
    }

    public int getHeadY() {
        return headY;
    }

    //Get the X, Y coordinate of the cell that contains the snake's tail
    private int getTailX() {
        if(direction == game.Snake.Direction.LEFT) {
            return headX + length -1;
        }else if(direction == game.Snake.Direction.RIGHT) {
            return headX - length +1;
        }
        else { //up and down
            return headX;
        }

    }

    private int getTailY() {
        if(direction == game.Snake.Direction.DOWN) {
            return headY - length + 1;
        }else if(direction == game.Snake.Direction.UP) {
            return headY + length -1;
        }
        else { // left and right
            return headY;
        }
    }

    private void drawOvalOneCell(Graphics g,int x, int y){
        g.fillOval(
                x * GuiScreen.CELL_SIZE + 2,
                y * GuiScreen.CELL_SIZE + 2,
                GuiScreen.CELL_SIZE - 4,
                GuiScreen.CELL_SIZE - 4);
    }


    // Returns true if the snake segment contains the give cell. used for collision detections
    public boolean contains(int x, int y) {
        switch(direction) {
            case LEFT:
                return ((y == this.headY) && ((x >= this.headX) && (x <= getTailX() )));
            case RIGHT:
                return ((y == this.headY) && ((x <= this.headX) && (x >= getTailX() )));
            case UP:
                return ((x == this.headX) && ((y >= this.headY) && (y <= getTailY() )));
            case DOWN:
                return ((x == this.headX) && ((y <= this.headY) && (y >= getTailY() )));
        }
        return false;
    }

    //Draw this segment

    public void draw(Graphics g) {
        int x = headX;
        int y = headY;
        switch(direction) {
            case LEFT:
                for(int i =0; i< length; ++i) {
                    drawOvalOneCell(g,x,y);
                    ++x;
                }
                break;
            case RIGHT:
                for(int i =0; i< length; ++i) {
                    drawOvalOneCell(g,x,y);
                    --x;
                }
                break;
            case UP:
                for(int i =0; i< length; ++i) {
                    drawOvalOneCell(g,x,y);
                    ++y;
                }
                break;
            case DOWN:
                for(int i =0; i< length; ++i) {
                    drawOvalOneCell(g,x,y);
                    --y;
                }
                break;
        }
    }
}
