import java.awt.Color;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.util.Random;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends JFrame implements KeyListener{
    
    private int score;
    private boolean gameover = false;
    private Duration dur;
    private Piece[] upcomingPiece = new Piece[4];
    public Random generator;
    private int[][] board;
    private Piece currentPiece;
    private Tetromino tetro;
    private Timer tmr;
    private TimerTask tsk;
    private boolean downReleased;

    JLabel[][] grid;

    //Constructor
    public Game()
    {
        downReleased = true;
        tmr = new Timer();
        board = new int[22][10];
        grid = new JLabel[20][10];
        score = 0;
        gameover = false;
        generator = new Random(1);
        // TODO: generate seed from datetime
        
        // set window properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Insets inset = this.getInsets();
        this.setSize(400,400 + inset.top);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.black);
        this.setLayout(null);
        
        // generate first piece
        currentPiece = Piece.values()[generator.nextInt(6)];
        // initialize empty board
        for(int ii = 0; ii < 22; ii++)   
        {
            for(int jj = 0; jj < 10; jj++)
            {
                board[ii][jj] = 0;
                //create label for each position on board except 0 and 1
                if(ii > 1)
                {
                    int i2 = ii - 2;
                    int xx = 150 + (jj*10);
                    int yy = 100 + (i2*10);
                    grid[i2][jj] = new JLabel();
                    grid[i2][jj].setBounds(xx,yy,10,10); 
                    grid[i2][jj].setBackground(Color.black);
                    grid[i2][jj].setOpaque(true);
                    this.add(grid[i2][jj]);
                }
            }
        }

        this.setVisible(true);

        createTask();
        tmr.scheduleAtFixedRate(tsk,500,500);
        
        // Generate upcoming pieces
        for(int ii = 0; ii < 4; ii++)
        {
            upcomingPiece[ii] = Piece.values()[generator.nextInt(6)];
        }

        //Keybindings
        this.addKeyListener(this);
        requestFocusInWindow();
        // Create first Piece
        CreateNextTetromino();
        //Output initial game state.
        updateBoardDisplay();
    }
    
    private void createTask()
    {
        tmr = new Timer();
        tsk = new TimerTask(){
            @Override
            public void run()
            {
                if(!gameover)
                {
                    drop();
                }
                else
                {
                    tmr.cancel();
                }
            }
        };
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // handle key code
        switch(e.getKeyCode())
        {
            case 37: // left
                ShiftLeft();
            break;
            case 38: // up
            break;
            case 39: // right
                ShiftRight();
            break;
            case 40: // down
                if(downReleased)
                {
                    increaseGravity();
                    downReleased = false;
                }
            break;
            case 68:
                RotateRight();
            break;
            case 65:
                RotateLeft();
            break;
            default:
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // does nothing, Don't delete
        switch(e.getKeyCode())
        {
            case 40:
                if(!downReleased)
                {
                    restoreGravity();
                    downReleased = true;
                }
            break;
            default:
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // does nothing, Don't delete
    }

    public void ShiftRight() 
    {
        if(!gameover)
        {
            if(tetro.canShiftRight(board))
            {
                updateBoardDisplay();
            }
    
        }
    }
    public void ShiftLeft()
    {
        if(!gameover)
        {
            if(tetro.canShiftLeft(board))
            {
                updateBoardDisplay();   
            }
        }
    }
    private void RotateRight()
    {
        if(!gameover)
        {
            if(tetro.canRotateRight(currentPiece, board))
            {
                updateBoardDisplay();
            }
        }
    }

    private void RotateLeft()
    {
        if(!gameover)
        {
            if(tetro.canRotateLeft(currentPiece, board))
            {
                updateBoardDisplay();
            }
        }
    }

    public void drop()
    {
        // If drop possible update board else place piece and get new piece
        if(!tetro.canDrop(board))
        {
            //current piece is placed
            checkPlaced();
        }
        else
        {
            updateBoardDisplay();
        }
    }


    // ========  Internals ==============
    private void checkPlaced()
    {
        // clear any tetris
        clearLines();
        // check if there is tetromino is placed at the 1st row
        for(int ii = 0; ii<10;ii++)
        {
            if(board[0][ii] == 1)
            {
                gameover = true;
                break;
            }
        }
        if(gameover)
        {
            // TODO: Show score
            System.out.println("Game Over.");
        }
        else
        {
            // new piece
            CreateNextTetromino();
        }
    }

    private void clearLines()
    {
        int combo = 0;
        for(int ii = 2; ii < 22; ii++)
        {
            if(checkTetris(ii))
            {
                // clear the row and add to points
                combo += 1;
                shiftDown(ii);
            }
            else
            {
                combo = 0;
            }
            if(combo >= 4)
            {
                combo = 0;
                //TODO:add 4 tetris score
            }
        }
        //TODO:shift down all rows above cleared rows
    }
    private void CreateNextTetromino()
    {
        currentPiece = upcomingPiece[0];
        for(int ii = 0; ii < 3; ii++)
        {
            upcomingPiece[ii] = upcomingPiece[ii+1];
        }
        upcomingPiece[3] = Piece.values()[generator.nextInt(6)];
        tetro = new Tetromino(currentPiece,board);
    }

    private boolean checkTetris(int row)
    {
        boolean ans = true;
        for(int ii = 0; ii < 10; ii++)
        {
            if(board[row][ii] == 0)
            {
                ans = false;
                break;
            }
        }
        if(ans)
        {
            //clear the whole line
            for(int ii = 0; ii < 10; ii++)
            {
                board[row][ii] = 0;
            }
        }
        return ans;
    }

    private void shiftDown(int row)
    {
        for(int ii = row; ii > 1; ii--)
        {
            board[ii] = board[ii-1];
        }
    }

    private void increaseGravity()
    {
        tmr.cancel();
        createTask();
        tmr.scheduleAtFixedRate(tsk, 0, 200);
    }

    private void restoreGravity()
    {
        tmr.cancel();
        createTask();
        tmr.scheduleAtFixedRate(tsk, 0, 500);
    }

    private void updateBoardDisplay()
    {
        for(int ii = 0; ii < 20; ii++)
        {
            for(int jj = 0;jj < 10; jj++)
            {
                if(board[ii+2][jj] == 1)
                {
                    grid[ii][jj].setBackground(Color.red);
                }
                else
                {
                    grid[ii][jj].setBackground(Color.black);
                }
            }
        }
        //System.out.println("full update completed.");
    }

    public void outputGameState()
    {
        //TODO: print out game board
        //TODO: print out tetris pieces
        //TODO: print out score
        System.out.println("------------");
        // Output lines 2 - 22, exclude line 0 and 1...
        for(int ii = 2; ii < 22; ii++)
        {
            System.out.print("-");
            for(int jj = 0; jj < 10; jj++)
            {
                if(board[ii][jj] == 1)
                {
                    System.out.print("*");
                }
                else
                {
                    System.out.print(" ");
                }
            }
            System.out.println("-");
        }
        System.out.println("------------");
    }
    // ========================== GETTERS ============================
    public boolean getGameover()
    {
        boolean ans = false;
        if(gameover)
        {
            ans = true;
        }
        return ans;
    }

    public int[][] getBoard()
    {
        return board;
    }

    public int getScore()
    {
        return this.score;
    }

    public Duration getTimer()
    {
        return this.dur;
    }

    public enum Piece{
        I,O,T,Z,S,L,J;
    }
    
}
