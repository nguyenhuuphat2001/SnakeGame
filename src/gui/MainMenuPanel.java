package gui;

import game.SoundEffect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {

    private JButton btnPlay = new JButton("Play");


    public MainMenuPanel() {
        setLayout(new BorderLayout());
        JPanel panelTop = new JPanel();
        panelTop.setPreferredSize(new Dimension(GuiScreen.ROWS*GuiScreen.CELL_SIZE, GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/4));

        JLabel label = new JLabel("Snake game");
        label.setFont(new Font("Arial",3,GuiScreen.ROWS*GuiScreen.CELL_SIZE/10));
        label.setBorder(BorderFactory.createEmptyBorder(GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/10, GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/10, 10, 10));
        label.setForeground(Color.darkGray);
        panelTop.add(label,BorderLayout.CENTER);

        JPanel panelLeft = new JPanel();
        panelLeft.setPreferredSize(new Dimension(GuiScreen.ROWS*GuiScreen.CELL_SIZE/3, GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/4));

        JPanel panelRight = new JPanel();
        panelRight.setPreferredSize(new Dimension(GuiScreen.ROWS*GuiScreen.CELL_SIZE/3, GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/4));


        JPanel panelCenter = new JPanel();
        panelCenter.setPreferredSize(new Dimension(GuiScreen.ROWS*GuiScreen.CELL_SIZE, GuiScreen.COLUMNS*GuiScreen.CELL_SIZE/4));

        panelCenter.setLayout(new GridLayout(7, 1, -1, -1));

        btnPlay.setFont(new Font("Arial",1,GuiScreen.ROWS*GuiScreen.CELL_SIZE/25));
        panelCenter.add(btnPlay);

        JLabel lblSpace = new JLabel("");
        panelCenter.add(lblSpace);
        JButton btnLevel = new JButton("Easy");
        btnLevel.setFont(new Font("Arial",1,GuiScreen.ROWS*GuiScreen.CELL_SIZE/25));
        panelCenter.add(btnLevel);

        JLabel lblSpace1 = new JLabel("");
        panelCenter.add(lblSpace1);
        JButton btnQuit = new JButton("Quit");
        btnQuit.setFont(new Font("Arial",1,GuiScreen.ROWS*GuiScreen.CELL_SIZE/25));
        panelCenter.add(btnQuit);

        JPanel panelBottom = new JPanel();

        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        add(panelLeft, BorderLayout.WEST);
        add(panelRight, BorderLayout.EAST);
        add(panelCenter, BorderLayout.CENTER);

        btnPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
            }
        });

        btnLevel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                if (GuiScreen.level == GuiScreen.LEVEL.EASY) {
                    GuiScreen.level = GuiScreen.LEVEL.NORMAL;
                    btnLevel.setText("Normal");
                } else if (GuiScreen.level == GuiScreen.LEVEL.NORMAL) {
                    GuiScreen.level = GuiScreen.LEVEL.HARD;
                    btnLevel.setText("Hard");
                } else if (GuiScreen.level == GuiScreen.LEVEL.HARD) {
                    GuiScreen.level = GuiScreen.LEVEL.EASY;
                    btnLevel.setText("Easy");
                }
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundEffect.CLICK.play();
                System.exit(0);
            }
        });
    }


    public void addButtonPlayActionListener(ActionListener listener) {
        btnPlay.addActionListener(listener);
    }
}
