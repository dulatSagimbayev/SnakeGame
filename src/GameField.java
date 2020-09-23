import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private int apples = 0;
    private Image dot;
    private Image hole;
    private Image apple;
    private int holeX;
    private int holeY;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private java.util.Timer t;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    Border borderline = BorderFactory.createLineBorder(Color.BLACK);


    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    public GameField() {
        setBorder(borderline);
        setBackground(Color.BLACK);
        LoadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {

        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
        createHole();
    }

    public void createHole() {
        holeX = new Random().nextInt(20) * DOT_SIZE;
        holeY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void LoadImages() {
        ImageIcon iif = new ImageIcon("hole.png");
        hole = iif.getImage();
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("d.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(hole, holeX, holeY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str = "Game Over";
            Font f = new Font("Arial", Font.BOLD, 14);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str, 125, SIZE / 2);
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            apples++;
            if (apples == 5) {
                apples = 0;
                timer.setDelay(timer.getDelay() - 20);
                createHole();
            }
            createApple();
        } else if (x[0] == holeX && y[0] == holeY) {
            if (dots > 3) {
                dots--;
                timer.setDelay(timer.getDelay() + 10);
            }

        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        } else if (x[0] < 0) {
            inGame = false;
        } else if (y[0] > SIZE) {
            inGame = false;
        } else if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_UP && !down) {
                up = true;
                right = false;
                left = false;
            } else if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                right = false;
                left = false;
            } else if (!inGame && key == KeyEvent.VK_ENTER) {
                inGame = true;
                left = false;
                right = true;
                up = false;
                down = false;
                timer.stop();
                initGame();
            } else if (timer.isRunning() && key == KeyEvent.VK_SPACE) {
                timer.stop();
            } else if (!timer.isRunning() && key == KeyEvent.VK_SPACE) {
                timer.start();
            }
        }
    }

}
