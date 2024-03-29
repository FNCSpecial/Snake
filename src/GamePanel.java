import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // game settings and const
    static final int SCREEN_WIDTH = 1080;
    static final int SCREEN_HEIGHT = 720;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 80;

    // snake head and body
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 5;

    // apples cords and score
    int applesEaten;
    int appleX;
    int appleY;
    // initial direction and game states
    char direction = 'R';
    char[] bodyPartDirection = new char[GAME_UNITS];
    boolean running = false;

    boolean menu = false;
    Timer timer;
    Random random;

    private BufferedImage img;

    // Object
    Texture tex;

    // panel creation
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);

        BufferedImageLoader loader = new BufferedImageLoader();
        img = loader.loadImage("/Snake.png");
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MyMouseListener());
        tex = new Texture();

        startGame();
    }

    // game starts in menu and timer is set
    public void startGame() {
        menu = true;
        timer = new Timer(DELAY, this);

        for (int i = 0; i < bodyParts ; i++) {
            x[i] = SCREEN_WIDTH / 2; // Set initial X position at the center of the screen
            y[i] = SCREEN_HEIGHT / 2; // Set initial Y position at the center of the screen
            bodyPartDirection[i] = direction;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // menu draw, game draw and end screen draw
    public void draw(Graphics g) {
        if (menu) {
            gameMenu(g);
        } else if (running) {
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.drawImage(tex.apple[0], appleX, appleY, null);

            switch (direction) {
                case 'R' -> g.drawImage(tex.player[1], x[0], y[0], null);
                case 'L' -> g.drawImage(tex.player[0], x[0], y[0], null);
                case 'U' -> g.drawImage(tex.player[2], x[0], y[0], null);
                case 'D' -> g.drawImage(tex.player[3], x[0], y[0], null);
            }

            // Draw the body parts
            for (int i = 1; i < bodyParts -1; i++) {
                int textureIndex;
                int dx = x[i] - x[i - 1];

                if (dx != 0) {
                    // Horizontal body part
                    textureIndex = 4;
                } else {
                    // Vertical body part
                    textureIndex = 6;
                }

                g.drawImage(tex.player[textureIndex], x[i], y[i], null);
            }



            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten)),
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    // new apple spawn
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    // moving logic
    public void move() {
        // Move the body parts
        for (int i = bodyParts - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
            bodyPartDirection[i] = bodyPartDirection[i - 1];
        }

        // Move the head based on the current direction
        switch (direction) {
            case 'U' -> y[0] -= UNIT_SIZE;
            case 'D' -> y[0] += UNIT_SIZE;
            case 'L' -> x[0] -= UNIT_SIZE;
            case 'R' -> x[0] += UNIT_SIZE;
        }

        bodyPartDirection[0] = direction;
    }



    private void playSound(String soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File f = new File(soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        if (!clip.isRunning())
            clip.close();

        clip.open(audioIn);
        clip.start();
    }

    // checking if head of the snake is in the apple if so add a point and spawn new one
    public void checkApple() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (x[0] == appleX && y[0] == appleY) {
            playSound("src/apple.wav");
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    // collisions logic with snake body and the screen borders
    public void checkCollisions() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // head collisions with body
        for (int i = bodyParts - 1; i > 0; i--) {
            if (i > 1 && x[0] == x[i] && y[0] == y[i]) {
                running = false;
                playSound("src/end.wav");
                break;
            }
        }

        // head touches borders
        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            playSound("src/end.wav");
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }





    public void gameOver(Graphics g) {
        // game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score:" + applesEaten)) / 2,
                (SCREEN_HEIGHT / 2) + 50);
    }

    // game menu text
    public void gameMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Menu", (SCREEN_WIDTH - metrics1.stringWidth("Menu")) / 2, g.getFont().getSize());

        g.drawString("Start", (SCREEN_WIDTH - metrics1.stringWidth("Start")) / 2, SCREEN_HEIGHT / 2 + 60);
        g.draw3DRect(SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2, 300, 80, true);
    }

    // keys action to change the movement
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }

    public class MyMouseListener implements MouseListener {
        // mouse action to click start game and restart the game

        @Override
        public void mouseClicked(MouseEvent e) {
            int pointX = e.getX();
            int pointY = e.getY();

            if (pointX > 360 && pointX < 690 && pointY > 360 && pointY < 440) {
                menu = false;
                newApple();
                running = true;
                timer.start();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            try {
                checkApple();
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                checkCollisions();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }
}
