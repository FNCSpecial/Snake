import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
static final int SCREEN_WIDTH = 1080;
static final int SCREEN_HEIGHT = 720;
static final int UNIT_SIZE = 20;
static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE*UNIT_SIZE);
static final int DELAY = 60;

final int[] x = new int[GAME_UNITS];
final int[] y = new int[GAME_UNITS];

int bodyParts = 5;
int applesEaten ;
int appleX;
int appleY;
char direction = 'R';
boolean running = false;

boolean menu = false;
Timer timer;
Random random;


GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.addMouseListener(new MyMouseListener());
        startGame();
    }
 public void startGame(){
    menu=true;
     timer = new Timer(DELAY,this);
 }
 public void paintComponent(Graphics g){
super.paintComponent(g);
draw(g);

 }
 public void draw(Graphics g) {
        if(menu){
            gameMenu(g);
        }
    else if (running) {
         for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
             g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
             g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
         }

         g.setColor(Color.green);
         g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

         for (int i = 0; i < bodyParts; i++) {
             if (i == 0) {
                 g.setColor(Color.BLUE);
                 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             } else {
                 g.setColor(Color.WHITE);
                 g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
             }
         }
         g.setColor(Color.red);
         g.setFont(new Font("Ink Free",Font.BOLD,40));
         FontMetrics metrics = getFontMetrics(g.getFont());
         g.drawString("Score:"+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score:"+applesEaten)), g.getFont().getSize());
     }
     else {
         gameOver(g);
     }
 }
 public void newApple(){
    appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
    appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
 }
 public void move(){
    for(int i = bodyParts; i > 0 ; i--){
        x[i] = x[i-1];
        y[i] = y[i-1];
    }

     switch (direction) {
         case 'U' -> y[0] = y[0] - UNIT_SIZE;
         case 'D' -> y[0] = y[0] + UNIT_SIZE;
         case 'L' -> x[0] = x[0] - UNIT_SIZE;
         case 'R' -> x[0] = x[0] + UNIT_SIZE;
     }

 }
 public void checkApple(){
if( x[0] == appleX && y[0] == appleY){
    bodyParts++;
    applesEaten++;
    newApple();
}
 }
 public void checkCollisions(){
        //head collisions with body
        for(int i=bodyParts; i>0; i--){
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        //head touches borders
     if(x[0] < 0){
         running = false;
     }

     if(x[0] > SCREEN_WIDTH){
         running = false;
     }

     if(y[0] < 0){
         running = false;
     }

     if(y[0] > SCREEN_HEIGHT){
         running = false;
     }

     if (!running){
         timer.stop();
     }


 }

 public void gameOver(Graphics g){
//game over text
     g.setColor(Color.red);
     g.setFont(new Font("Ink Free",Font.BOLD,75));
     FontMetrics metrics1 = getFontMetrics(g.getFont());
     g.drawString("Game Over",(SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

     g.setColor(Color.red);
     g.setFont(new Font("Ink Free",Font.BOLD,40));
     FontMetrics metrics2 = getFontMetrics(g.getFont());
     g.drawString("Score:"+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score:"+applesEaten))/2, (SCREEN_HEIGHT/2)+50);
 }

    public void gameMenu(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Menu",(SCREEN_WIDTH - metrics1.stringWidth("Menu"))/2, g.getFont().getSize());

        g.drawString("Start",(SCREEN_WIDTH - metrics1.stringWidth("Start"))/2,SCREEN_HEIGHT/2 + 60);
        g.draw3DRect(SCREEN_WIDTH/2 - 150,SCREEN_HEIGHT/2,300,80,true);




    }
  public class MyKeyAdapter extends KeyAdapter{
      @Override
      public void keyPressed(KeyEvent e) {
          switch (e.getKeyCode()) {
              case KeyEvent.VK_LEFT -> {
                  if (direction != 'R') {
                      direction = 'L';
                  }
              }
              case KeyEvent.VK_RIGHT -> {
                  if (direction != 'L') {
                      direction = 'R';
                  }
              }
              case KeyEvent.VK_UP -> {
                  if (direction != 'D') {
                      direction = 'U';
                  }
              }
              case KeyEvent.VK_DOWN -> {
                  if (direction != 'U') {
                      direction = 'D';
                  }
              }
          }
      }

  }

    public class MyMouseListener implements MouseListener{
//

        @Override
        public void mouseClicked(MouseEvent e) {
            int pointX = e.getX();
            int pointY = e.getY();

          if(pointX > 360 && pointX < 690 && pointY > 360 && pointY < 440){
              menu=false;
              newApple();
              running=true;

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
    if(running){
        move();
        checkApple();
        checkCollisions();

    }
    repaint();
    }
}