import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// Board class inherits JPanel class and ActionListener interface to use ActionEvenets and KeyEvenets
public class Board extends JPanel implements ActionListener{
    // Setting the final height and width of our board (unchangable)
    final int B_HEIGHT = 400;
    final int B_WIDTH = 400;
    // Our B_HEIGHT * B_WIDTH board can hold atmost 1600 dots of 10 pixels each
    final int maxDots = 1600;
    final int dotSize = 10;
    int dots;
    // x & y arrays hold the overal length of the snake on x and y exis.
    // Overal length of snake can be atmost 1600 dots only
    int x[] = new int[maxDots];
    int y[] = new int[maxDots];
    // Variables to store the position of Apple on x and y exis
    int appleX;
    int appleY;
    // Objects of images of snake's head & body and the apple. We will use these images to draw the snake on the board
    Image body, head, apple;
    // A timer class object to keep track of the movement of the snake
    Timer timer;
    // DELAY signifies the time after which the snake will make a movement from point A to point B
    // in this case, 100 means 1/10th of a second. It means the snake will move after every 1/10th of a second
    // the delay value is negiatively proportionate to the snake's speed. The higher the DELAY value, the lower the snake's speed
    int delay = 100;
    // Variable to keep track in which direction the snake is moving at a given time
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;
    // Variable to keep track if the game has been ended or not
    boolean inGame = true;
    // As soon as the main function in the SnakeGame class runs, it calls it's constructor and from there,
    // the constructor of the Board class is called. Therefore, this is the very first line to get executed in the Board class
    Board(){
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        // 1 - Defining the properties of the board
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        // 2 - Once the properties of the board are set, we initialize the game
        initGame();
        // 3 - Function to load the images of head, body and apple on the board
        loadImages();
    }
    // 2.1 - Game initializes
    public void initGame(){
        // Initial length of snake is 3 dots (1 for head, 2 for body)
        dots = 3;
        // Intial posiotion of the snake's on the boeard (on x and y exis)
        x[0] = 250;
        y[0] = 250;
        // Now we set the positions of body dots behind the head on x and y exis
        for(int i = 1; i < dots; i++){
            x[i] = x[0] + dotSize * i;
            y[i] = y[0];
        }
        // After setting the position of the snake, we set the position of the apple on the board
        locateApple();
        // Once we have set the postions of snake and apple, we start a timer. The snake moves after every defined time window
        // In this case, that time window is of 1/10th of a second. We use delay variable for the time window length.
        // Every time when timer runs, it generates an action event and actionPerformed function is called.
        timer = new Timer(delay, this);
        timer.start();
    }
    // 2.2 - Function to set the position of apple
    public void locateApple(){
        // We assign x and y values randomly. These values should lie between 0 to 390 (within the range of the hieght and width of the board)
        appleX = ((int)(Math.random()*39)) * dotSize;
        appleY = ((int)(Math.random()*39)) * dotSize;
    }
    // 2.3 - actionPerformed function is called every time the timer runs
    @Override
    public void actionPerformed(ActionEvent e){
        // Initially the game is on so we enter the if condition
        if(inGame){
            // Function to handle the case when snake eats the apple. (Apple's loctaion and snake's head's location matches on the board)
            eatApple();
            // Function to move the snake on the board
            moveSnake();
            // Function to check if the snake has collided with the body or border or not         
            checkCollison();       
        }
        // We call the repaint function to again draw the images accoring to the snake's and apple's new positions
        repaint();
    }
    // 3.1 - Function to load the images of the head, body and apple on the board
    public void loadImages(){
        // Load the images using the ImageIcon class by providing the address of each image
        // Then get the corresponding images
        ImageIcon bodyIcon = new ImageIcon("src/res/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/res/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/res/apple.png");
        apple = appleIcon.getImage();
    }
    // 3.2 - After loading the images, draw those imgaes accordingly
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    // 3.3 - Function to draw the images
    public void draw(Graphics g){
        // when the game is on, 
        if(inGame){
            // Drwaing apple's image on x and y exis using appleX and appleX values generated by locateApple() function
            g.drawImage(apple, appleX, appleY, this);
            // Drwaing the snake
            for(int i = 0; i < dots; i++){
                // first index represents the head of snake so draw the head image
                if(i == 0){
                    g.drawImage(head, x[i], y[i], this);
                // rest index represent the body so draw the body images
                }else {
                    g.drawImage(body, x[i], y[i], this);
                }
            }
        // when the inGame variable is set to be false by the checkCollison() function
        } else {
            // We call the game over function to print the 'GAME OVER' message and Score and stop the timer to finish the game
            gameOver(g);
            timer.stop();
        }
    }
    // Function to move the snake on the board
    public void moveSnake(){
        // We change the position of the snake according to the direction in which it is moving on x and y exis
        for(int i = dots-1; i >= 0; i--){
            // We change the position of the head
            if(i == 0){
                if(leftDirection){
                    x[0] -= dotSize;
                }
                if(rightDirection){
                    x[0] += dotSize;
                }
                if(upDirection){
                    y[0] -= dotSize;
                }
                if(downDirection){
                    y[0] += dotSize;
                }
            // And then shift the body images (dots) by one unit by copying the locations of nebhouring image's location
            } else {
                x[i] = x[i-1];
                y[i] = y[i-1];
            }
        }
    }

    // Function to handle the case when snake eats the apple. (Apple's loctaion and snake's head's location matches on the board)
    public void eatApple(){
        if(x[0] == appleX && y[0] == appleY){
            // Ever time when snake eats the apple, we increase the number of dots in the snake by one
            dots++;
            // We relocate the apple by calling the locateApple() function
            locateApple();
        }
    }
    // Function to check if the snake has collided with the body or border or not 
    public void checkCollison(){
        // Collison with Body: If the head's location matches with any of the body dot's location, we set inGame variable as false 
        for(int i = 1; i < dots; i++){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
                break;
            }
        }
        // Collison with Border: if the head of the snake goes out of the boundries of the board (B_HEIGHT & B_WIDTH), 
        // we set inGame variable as false
        if(x[0] < 0){
            inGame = false;
        }
        if(x[0] >= B_WIDTH){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
        if(y[0] >= B_HEIGHT){
            inGame = false;
        }
    }
    // function to print the 'GAME OVER' message and Score
    public void gameOver(Graphics g){
        String msg = "GAME OVER";
        // We calculate a score of 10 for every dot. We decrease the dots' count by 3 before calculating the answer
        // because the game was intialized with 3 dots and there is no score for those 3 dots
        int score = (dots - 3) * 10;
        String scoreMsg = "SCORE: "+Integer.toString(score);
        Font font = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(font);
        // Setting the properties of the message and location on the board
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg, (B_WIDTH - fontMetrics.stringWidth(msg))/2, B_HEIGHT / 4);
        g.drawString(scoreMsg, (B_HEIGHT - fontMetrics.stringWidth(scoreMsg))/2, 3 * (B_HEIGHT / 4));

    }
    // We inherit KeyAdapter class to use it's function keyPressed() and overrride it
    private class TAdapter extends KeyAdapter{
        // Every time a key is pressed, keyPressed function is triggered
        @Override
        public void keyPressed(KeyEvent keyEvent){
            // Get the keyCode and check which key is pressed. 
            // Whichever key is pressed, make it's correspnding direction variable true and keep rest to false
            // moveSnake() will then move the snake according to the true direction variable 

            // This is to keep in mind that when an arrow key is pressed, the exact opposite of the pressed arrow key should be already false
            // It means, when leftDirection key is pressed to move the snake left, the snake should not be already moving to the rightDirection
            int key = keyEvent.getKeyCode();

            if(key == KeyEvent.VK_LEFT && !rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && !leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key == KeyEvent.VK_UP && !downDirection){
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && !upDirection){
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}
