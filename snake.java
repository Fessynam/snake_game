import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;
    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(2, 0));
        snake.add(new Point(1, 0));
        food = new Point(5, 5);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    public void startGame() {
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw snake
        for (Point point : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(point.x * UNIT_SIZE, point.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * UNIT_SIZE, food.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    public void move() {
        // Move the snake
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.y--;
                break;
            case 'D':
                newHead.y++;
                break;
            case 'L':
                newHead.x--;
                break;
            case 'R':
                newHead.x++;
                break;
        }

        snake.add(0, newHead);

        // Check if the snake ate the food
        if (newHead.equals(food)) {
            // Generate new food location
            Random rand = new Random();
            int x = rand.nextInt(WIDTH / UNIT_SIZE);
            int y = rand.nextInt(HEIGHT / UNIT_SIZE);
            food.setLocation(x, y);
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollision() {
        Point head = snake.get(0);
        // Check if the snake hits the wall or itself
        if (head.x < 0 || head.x >= WIDTH / UNIT_SIZE || head.y < 0 || head.y >= HEIGHT / UNIT_SIZE) {
            running = false;
            timer.stop();
        }
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                timer.stop();
                break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != 'D')
                    direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame();
        frame.add(snakeGame);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
