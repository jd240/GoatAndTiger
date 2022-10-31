
/**
 * Maintains and updates the status of the board
 * i.e. the locations of goats and tigers
 *
 * @Student 1 Name: Du "Justin" Duong 
 * @Student 1 Number: 23179573
 * 
 * @Student 2 Name: Midhin Viju
 * @Student 2 Number: 22850881
 */
public class Board
{
    // An enumated type for the three possibilities
    private enum Piece {GOAT, TIGER, VACANT};
    Piece[] board;
    Piece p;

    /**
     * Constructor for objects of class Board.
     * Initializes the board VACANT.
     */
    public Board()
    {
        // TODO 3
        p = Piece.TIGER;
        board = new Piece[24];
        for (int i = 0; i < board.length; i++)
        {
            setVacant(i);
        }
        for (Piece pie : Piece.values())
        {
            if (pie == p)
            System.out.print("worked");
        }
    }

            
    /**
     * Checks if the location a is VACANT on the board
     */
    public boolean isVacant(int a)
    {
        if ((0 <= a && a <= 23) && board[a] == Piece.VACANT)
        {
            return true;
        }
        
        else 
        {
            return false;
        }
    }
    
    /**
     * Sets the location a on the board to VACANT
     */
    public void setVacant(int a)
    {
        //TODO 5
        board[a] = Piece.VACANT;
    }
    
    /**
     * Checks if the location a on the board is a GOAT
     */
    public boolean isGoat(int a)
    {
        //TODO 6
        if ((0 <= a && a <= 23) && board[a] == Piece.GOAT)
        {
            return true;
        }
        
        else 
        {
            return false;
        }
    }
    
    /**
     * Sets the location a on the board to GOAT
     */
    public void setGoat(int a)
    {
        //TODO 7
        board[a] = Piece.GOAT;
    }
    
    /**
     * Sets the location a on the board to TIGER
     */
    public void setTiger(int a)
    {
        //TODO 8
        board[a] = Piece.TIGER;
    }
    
    /**
     * Moves a piece by swaping the contents of a and b
     */
    public void swap(int a, int b)
    {
        //TODO 9
        Piece placeholder = board[b];
        board[b] = board[a];
        board[a] = placeholder; 
    }
    
}
