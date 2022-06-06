package game;

import gui.GuiScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

// Food is a food item that the snake can eat. It is placed randomly in the pit.
public class Food {
    //current food location(x,y) in cells
    private int x,y;
    //color for display
    private Color color = Color.BLUE;
    //for randomly placing the food
    private Random rand = new Random();

    private ImageIcon iconFood = new ImageIcon(getClass().getResource("/images/food.png"), "FOOD");

//    private ImagePanel food ;
//    try {
//        food = new ImagePanel(ImageIO.read(new File
//                ("/home/mano/Pictures/cartoons/tangled.jpg")));
//    } catch (IOException e) {
//        e.printStackTrace();
//    }

    //default constructor
    public Food() {
        //place outside the pit, so that it will not be "displayed"
        x = -1;
        y = -1;
    }

    //Regenerate a food item. Randomly place inside the pit
    public void regenerate() {
        x = rand.nextInt(GuiScreen.COLUMNS - 4) + 2;
        y = rand.nextInt(GuiScreen.ROWS - 4) + 2;

    }
    //Return the x, y coordinate of the cell that contains this food item
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //Draw itself
    public void draw(Graphics g) {

        g.setColor(color);
//        g.fill3DRect(x * GameMain.CELL_SIZE,
//                y* GameMain.CELL_SIZE,
//                GameMain.CELL_SIZE,
//                GameMain.CELL_SIZE,
//                true);
//        g.drawImage()
        iconFood.paintIcon(null, g, x * GuiScreen.CELL_SIZE, y* GuiScreen.CELL_SIZE);
    }
}
