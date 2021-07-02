
public class Tetromino 
{
    public int[] columns;
    public int[] rows;
    
    public Tetromino(Game.Piece thisPiece, int[][] board)
    {
        columns = new int[4];
        rows = new int[4];
        switch(thisPiece)
        {
            case I:
                rows[0] = rows[1] = rows[2] = rows[3] = 1;
                columns[0] = 3;
                columns[1] = 4;
                columns[2] = 5;
                columns[3] = 6;
            break;
            case O:
                rows[0] = rows[1] = 0;
                rows[2] = rows[3] = 1;
                columns[0] = columns[2] = 4;
                columns[1] = columns[3] = 5;
            break;
            case T:
                rows[0] = 0;
                rows[1] = rows[2] = rows[3] = 1;
                columns[0] = columns[2] = 4;
                columns[1] = 3;
                columns[3] = 5; 
            break;
            case Z:
                rows[0] = rows[1] = 0;
                rows[2] = rows[3] = 1;
                columns[0] = 3;
                columns[1] = columns[2] = 4;
                columns[3] = 5;
            break;
            case S:
                rows[0] = rows[1] = 1;
                rows[2] = rows[3] = 0;
                columns[0] = 3;
                columns[1] = columns[2] = 4;
                columns[3] = 5;
            break;
            case L:
                rows[0] = 0;
                rows[1] = rows[2] = rows[3] = 1;
                columns[0] = columns[3] = 5;
                columns[1] = 3;
                columns[2] = 4;
            break;
            default: //J shape
                rows[0] = 0;
                rows[1] = rows[2] = rows[3] = 1;
                columns[0] = columns[1] = 3;
                columns[2] = 4;
                columns[3] = 5;
            break;
        }
        for(int ii = 0; ii < 4; ii++)
        {
            board[rows[ii]][columns[ii]] = 1;
        }
    }

    private void clear(int[][] board)
    {
        // Clear current space occupied
        for(int ii = 0; ii < 4; ii++)
        {
            board[rows[ii]][columns[ii]] = 0;
        }
    }

    private void refill(int[][] board)
    {
        // Occupy new space or refill old space
        for(int ii = 0; ii < 4; ii++)
        {
            board[rows[ii]][columns[ii]] = 1;
        }
    }

    public boolean canDrop(int[][] board)
    {
        boolean ans = true;
        // Clear current space occupied
        clear(board);

        // Check if any block is in the last row
        // or if any block is on top of others (not including self)
        for(int ii = 0; ii < 4; ii++)
        {
            if(rows[ii] == 21)
            {
                ans = false;
                break;
            }
            else if(board[rows[ii]+1][columns[ii]] == 1)
            {
                ans = false;
                break;
            }
        }
        // Increment rows if drop is possible
        if(ans)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                rows[ii] = rows[ii] + 1;
            }
        }
        // Occupy new space or refill old space
        refill(board);
        return ans;
    }
    
    public boolean canShiftLeft(int[][] board)
    {
        boolean ans = true;
        // Clear current space occupied
        clear(board);
        //Check if column = 0
        // or if any block exists to the left
        for(int ii = 0; ii < 4; ii++)
        {
            if(columns[ii] == 0)
            {
                ans = false;
                break;
            }
            else if(board[rows[ii]][columns[ii]-1] == 1)
            {
                ans = false;
                break;
            }
        }
        // Decrement columns if left shift is possible
        if(ans)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                columns[ii] = columns[ii] - 1;
            }
        }
        // Occupy new space or refill old space
        refill(board);
        return ans;
    }

    public boolean canShiftRight(int[][] board)
    {
        boolean ans = true;
        // Clear current space occupied
        clear(board);

        for(int ii = 0; ii < 4; ii++)
        {
            //Check if any column == 9
            if(columns[ii] == 9)
            {
                ans = false;
                break;
            }
            // or if any block exists to the Right
            else if(board[rows[ii]][columns[ii]+1] == 1)
            {
                ans = false;
                break;
            }
        }
        // Increment columns if right shift is possible
        if(ans)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                columns[ii] = columns[ii] + 1;
            }
        }
        // Occupy new space or refill old space
        refill(board);
        return ans;
    }

    public boolean canRotateRight(Game.Piece thisPiece, int[][] board)
    {
        boolean ans = true;
        int[] newRows = new int[4];
        int[] newColumns = new int[4];
        boolean cantRotate = (thisPiece == Game.Piece.O); 
        if(!cantRotate)
            clear(board);
        
        switch(thisPiece)
        {
            case I:
            case Z:
            case S:
                // I pivots off position 1
                if(rows[0] == rows[1])
                {
                    newRows[0] = rows[1] - 1;
                    newRows[1] = newRows[2] = rows[1];
                    newRows[3] = rows[1] + 1;

                    if(thisPiece == Game.Piece.I)
                    {
                        newColumns[0] = newColumns[1] = newColumns[2] = newColumns[3] = columns[1];
                        newRows[2] = rows[1]+1;
                        newRows[3] = rows[1] + 2;
                    }
                    else if(thisPiece == Game.Piece.Z)
                    {
                        newColumns[0] = newColumns[1] = columns[1];
                        newColumns[2] = newColumns[3] = columns[1] - 1;
                    }
                    else
                    {
                        newColumns[0] = newColumns[1] = columns[1];
                        newColumns[2] = newColumns[3] = columns[1]+1;
                    }
                }
                else
                {
                    
                    newRows[0] = newRows[1] = newRows[2] = newRows[3] = rows[1];
                    newColumns[0] = columns[1] - 1;
                    newColumns[1] = newColumns[2] = columns[1];
                    newColumns[3] = columns[1] + 1; 
                    if(thisPiece == Game.Piece.I)
                    {
                        newRows[0] = newRows[1] = newRows[2] = newRows[3] = rows[1];
                        newColumns[2] =  columns[1] + 1;
                        newColumns[3] = columns[1] + 2; 
                    }
                    else if(thisPiece == Game.Piece.Z)
                    {
                        newRows[0] = newRows[1] = rows[1];
                        newRows[2] = newRows[3] = rows[1] + 1;
                    }
                    else
                    {
                        newRows[0] = newRows[1] = rows[1];
                        newRows[2] = newRows[3] = rows[1] - 1;
                    }
                }
            break;
            //NOTE: L, J, and T have similar rotates.
            case L:
                // L pivots off position 2
            case J:
                //J pivots off position 2
            case T:
                //T pivots off position 2
                if(rows[1] == rows[2])
                {
                    if(thisPiece == Game.Piece.L)
                        newRows[0] = rows[2]+1;
                    else if(thisPiece == Game.Piece.J)
                        newRows[0] = rows[2]-1;
                    else
                        newRows[0] = rows[2];

                    newRows[1] = rows[2] - 1;
                    newRows[2] = rows[2];
                    newRows[3] = rows[2] + 1;
                    newColumns[1] = newColumns[3] = newColumns[2] = columns[2];
                    if(rows[0] < rows[2])
                    {
                        newColumns[0] = columns[2] + 1;
                    }
                    else
                    {
                        newColumns[0] = columns[2] - 1;
                    }

                }   
                else
                {
                    if(thisPiece == Game.Piece.L)
                        newColumns[0] = columns[2]+1;
                    else if(thisPiece == Game.Piece.J)
                        newColumns[0] = columns[2]-1;
                    else
                        newColumns[0] = columns[2];

                    newColumns[1] = columns[2] - 1;
                    newColumns[2] = columns[2];
                    newColumns[3] = columns[2] + 1;
                    newRows[1] = newRows[2] = newRows[3] = rows[2];
                    if(columns[0] < columns[2])
                    {
                        newRows[0] = rows[2] - 1;
                    }
                    else
                    {
                        newRows[0] = rows[2] + 1;
                    }
                } 
            break;
            default:
                //O cannot rotate
                ans = false;
            break;
        }

        //check if new space is available
        if(!cantRotate)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                if(newColumns[ii] > 0 && newColumns[ii] < 9 && newRows[ii]>0 && newRows[ii] < 21)
                {
                    if(board[newRows[ii]][newColumns[ii]] == 1)
                    {
                        ans = false;
                        break;
                    }
                }
                else
                {
                    ans = false;
                    break;
                }
            }
        }
        //TODO: check if available with wall kick.
        if(!ans && !cantRotate)
        {
            boolean canWallkick = true;
            for(int ii = 0; ii < 4; ii++)
            {   
                if(newColumns[ii]-1 < 0)
                {
                    canWallkick = false;
                    break;
                }
                else
                {
                    if(board[newRows[ii]][newColumns[ii]-1] == 1)
                    {
                        canWallkick = false;
                        break;
                    }
                }
            }
            if(canWallkick)
            {
                ans = true;
                for(int ii = 0;ii<4;ii++)
                {
                    newColumns[ii] = newColumns[ii]-1;
                }
            }
            else
            {
                canWallkick = true;
                for(int ii = 0;ii<4;ii++)
                {
                    if(newColumns[ii]+1 > 9)
                    {
                        canWallkick = false;
                        break;
                    }
                    else
                    {
                        if(board[newRows[ii]][newColumns[ii]+1] == 1)
                        {
                            canWallkick = false;
                            break;
                        }
                    }
                }
                if(canWallkick)
                {
                    ans = true;
                    for(int ii = 0;ii<4;ii++)
                    {
                        newColumns[ii] = newColumns[ii]+1;
                    }
                }
            }
        }
        if(ans)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                rows[ii] = newRows[ii];
                columns[ii] = newColumns[ii];
            }
        }
        if(!cantRotate)
            refill(board);
        return ans;
    }

    public boolean canRotateLeft(Game.Piece thisPiece, int[][] board)
    {
        boolean ans = true;
        int[] newRows = new int[4];
        int[] newColumns = new int[4];
        boolean cantRotate = (thisPiece == Game.Piece.O); 
        if(!cantRotate)
            clear(board);
        
        switch(thisPiece)
        {
            case I:
            case Z:
            case S:
                // I pivots off position 1
                if(rows[0] == rows[1])
                {
                    newRows[0] = rows[1] - 1;
                    newRows[1] = newRows[2] = rows[1];
                    newRows[3] = rows[1] + 1;

                    if(thisPiece == Game.Piece.I)
                    {
                        newColumns[0] = newColumns[1] = newColumns[2] = newColumns[3] = columns[1];
                        newRows[2] = rows[1]+1;
                        newRows[3] = rows[1] + 2;
                    }
                    else if(thisPiece == Game.Piece.Z)
                    {
                        newColumns[0] = newColumns[1] = columns[1];
                        newColumns[2] = newColumns[3] = columns[1] - 1;
                    }
                    else
                    {
                        newColumns[0] = newColumns[1] = columns[1];
                        newColumns[2] = newColumns[3] = columns[1]+1;
                    }
                }
                else
                {
                    
                    newRows[0] = newRows[1] = newRows[2] = newRows[3] = rows[1];
                    newColumns[0] = columns[1] - 1;
                    newColumns[1] = newColumns[2] = columns[1];
                    newColumns[3] = columns[1] + 1; 
                    if(thisPiece == Game.Piece.I)
                    {
                        newRows[0] = newRows[1] = newRows[2] = newRows[3] = rows[1];
                        newColumns[2] =  columns[1] + 1;
                        newColumns[3] = columns[1] + 2; 
                    }
                    else if(thisPiece == Game.Piece.Z)
                    {
                        newRows[0] = newRows[1] = rows[1];
                        newRows[2] = newRows[3] = rows[1] + 1;
                    }
                    else
                    {
                        newRows[0] = newRows[1] = rows[1];
                        newRows[2] = newRows[3] = rows[1] - 1;
                    }
                }
            break;
            //NOTE: L, J, and T have similar rotates.
            case L:
                // L pivots off position 2
            case J:
                //J pivots off position 2
            case T:
                //T pivots off position 2
                if(rows[1] == rows[2])
                {
                    if(thisPiece == Game.Piece.L)
                        newRows[0] = rows[2]+1;
                    else if(thisPiece == Game.Piece.J)
                        newRows[0] = rows[2]-1;
                    else
                        newRows[0] = rows[2];

                    newRows[1] = rows[2] - 1;
                    newRows[2] = rows[2];
                    newRows[3] = rows[2] + 1;
                    newColumns[1] = newColumns[3] = newColumns[2] = columns[2];
                    if(rows[0] > rows[2])
                    {
                        newColumns[0] = columns[2] + 1;
                    }
                    else
                    {
                        newColumns[0] = columns[2] - 1;
                    }

                }   
                else
                {
                    if(thisPiece == Game.Piece.L)
                        newColumns[0] = columns[2]+1;
                    else if(thisPiece == Game.Piece.J)
                        newColumns[0] = columns[2]-1;
                    else
                        newColumns[0] = columns[2];

                    newColumns[1] = columns[2] - 1;
                    newColumns[2] = columns[2];
                    newColumns[3] = columns[2] + 1;
                    newRows[1] = newRows[2] = newRows[3] = rows[2];
                    if(columns[0] > columns[2])
                    {
                        newRows[0] = rows[2] - 1;
                    }
                    else
                    {
                        newRows[0] = rows[2] + 1;
                    }
                } 
            break;
            default:
                //O cannot rotate
                ans = false;
            break;
        }

        //check if new space is available
        if(!cantRotate)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                if(newColumns[ii] > 0 && newColumns[ii] < 9 && newRows[ii]>0 && newRows[ii] < 21)
                {
                    if(board[newRows[ii]][newColumns[ii]] == 1)
                    {
                        ans = false;
                        break;
                    }
                }
                else
                {
                    ans = false;
                    break;
                }
            }
        }
        //TODO: check if available with wall kick.
        if(!ans && !cantRotate)
        {
            boolean canWallkick = true;
            for(int ii = 0; ii < 4; ii++)
            {   
                if(newColumns[ii]-1 < 0)
                {
                    canWallkick = false;
                    break;
                }
                else
                {
                    if(board[newRows[ii]][newColumns[ii]-1] == 1)
                    {
                        canWallkick = false;
                        break;
                    }
                }
            }
            if(canWallkick)
            {
                ans = true;
                for(int ii = 0;ii<4;ii++)
                {
                    newColumns[ii] = newColumns[ii]-1;
                }
            }
            else
            {
                canWallkick = true;
                for(int ii = 0;ii<4;ii++)
                {
                    if(newColumns[ii]+1 > 9)
                    {
                        canWallkick = false;
                        break;
                    }
                    else
                    {
                        if(board[newRows[ii]][newColumns[ii]+1] == 1)
                        {
                            canWallkick = false;
                            break;
                        }
                    }
                }
                if(canWallkick)
                {
                    ans = true;
                    for(int ii = 0;ii<4;ii++)
                    {
                        newColumns[ii] = newColumns[ii]+1;
                    }
                }
            }
        }
        if(ans)
        {
            for(int ii = 0; ii < 4; ii++)
            {
                rows[ii] = newRows[ii];
                columns[ii] = newColumns[ii];
            }
        }
        if(!cantRotate)
            refill(board);
        return ans;
    }
}