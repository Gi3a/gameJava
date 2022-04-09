package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow gameWindow;
    private static long lastFrameTime;
    
    private static Image backgroundImage;
    private static Image gameoverImage;
    private static Image dropImage;

    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropSpeed = 200;

    private static int scoreGame = 0;

    public static void main(String[] args) throws IOException {
        backgroundImage = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        gameoverImage = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        dropImage = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));

	    gameWindow = new GameWindow();

        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(200, 100);
        gameWindow.setSize(906, 478);
        gameWindow.setResizable(false);

        lastFrameTime = System.nanoTime();

        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + dropImage.getWidth(null);
                float dropBottom = dropTop + dropImage.getHeight(null);
                boolean isDrop = x >= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;
                if (isDrop)
                    dropTop = -100;
                    dropLeft = (int)(Math.random() * (gameField.getWidth() - dropImage.getWidth(null)));
                    dropSpeed = dropSpeed + 20;
                    scoreGame++;
                    gameWindow.setTitle("Score game: " + scoreGame);
            }
        });

        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }

    private static void onRepaint(Graphics graphics) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        dropTop = dropTop + dropSpeed * deltaTime;

        graphics.drawImage(backgroundImage, 0, 0, null);
        graphics.drawImage(dropImage, (int)dropLeft, (int)dropTop, null);

        if (dropTop > gameWindow.getHeight())
            graphics.drawImage(gameoverImage, 280, 120, null);
    }

    private static class GameField extends JPanel {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            onRepaint(graphics);
            repaint();
        }
    }
}
