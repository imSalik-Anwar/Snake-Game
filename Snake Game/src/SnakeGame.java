import javax.swing.JFrame;

public class SnakeGame extends JFrame{
    // Initializing an instance of Board class which inherits JPanel class
    // All the logic of our game resides in the Board (JPanel) itself. 
    Board board;
    SnakeGame(){

        board = new Board();
        // We add our board to to the SnakeGame JFrame
        add(board);
        pack();
        // Setting resizability of the frame to false to keep it in sync with board size 
        setResizable(false);
        setVisible(true);
    }
    // SnakeGame class inherits JFrame class, so it behaves like JFrame. We initialize a JFrame by
    // by calling the constructor
    public static void main(String[] args) throws Exception {
        new SnakeGame();
    }
}
