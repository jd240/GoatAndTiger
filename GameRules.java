/**
 * Maintains game rules. Checks if a move is legal.
 * It also maintains move turns and the game stage.
 *
 * @Student 1 Name: Du "Justin" Duong 
 * @Student 1 Number: 23179573
 * 
 * @Student 2 Name: Midhin Viju
 * @Student 2 Number: 22850881
 */
public class GameRules
{
    // Instance variables to maintain whose move it is
    private boolean moveStage; 
    private boolean goatsTurn;
    private int numGoats; //the number of goats on the board
    private int numTigers; //the number of tigers on the board
    private final int MAXGOATS = 12;
    
    // list of all legal moves legalMoves[0] is {1,3,9} which means a piece from [0] can move to 
    // [1],[3] or [9]. legalMoves[1] is {0,2,4} meaning a piece from [1] can move to [0],[2] or [4]
    public final int[][] legalMoves = {{1,3,9},{0,2,4},{1,5,14},{0,4,6,10},{1,3,5,7},{2,4,8,13},
    {3,7,11},{4,6,8},{5,7,12},{0,10,21},{3,9,11,18},{6,10,15},{8,13,17},{5,12,14,20},{2,13,23},
    {11,16,18},{15,17,19},{12,16,20},{10,15,19,21},{16,18,20,22},{13,17,19,23},{9,18,22},{19,21,23},
    {14,20,22}};     
    
    //list of all legal eat moves by tigers e.g. legalEats[0] means that a tiger at [0] can 
    // eat a goat at [1] and land in [2] (there has to be a goat at [1] and [2] must be vacant)
    // tiger at [0] can also eat a goat at [9] and jump to 21 OR eat a goat at [3] and jump to [6]
    // legalEats[4]={} is empty meaning that at tiger at [4] has no options
    private final int[][] legalEats = {{1,2, 9,21, 3,6}, {4,7}, {1,0, 5,8, 14,23}, {4,5, 10,18},{},
    {4,3, 13,20}, {3,0, 7,8, 11,15}, {4,1}, {5,2, 7,6, 12,17}, {10,11},{}, {10,9}, {13,14},{},
    {13,12}, {11,6, 16,17, 18,21}, {19,22}, {12,8, 16,15, 20,23}, {10,3, 19,20},{}, {13,5, 19,18},
    {9,0, 18,15, 22,23}, {19,16}, {14,2, 20,17, 22,21}};                              
   
   //NOTE: You MUST use only the above 2 arrays to implement all moves.
    // Adding more arrays is NOT allowed.
    

    /**
     * Constructor for objects of class GameRules
     */
    public GameRules()
    {              
        moveStage = false;
        goatsTurn = true;
        numGoats = 0;
        numTigers = 0;
    }       
    
    /**
     * returns moveStage
     */
    public boolean isMoveStage()
    {
        return moveStage;
    }
    
    /**
     * returns true iff it is goats turn
     */
    public boolean isGoatsTurn()
    {
        return goatsTurn;
    }    
    
    
    /**
     * Adds (+1 or -1) to goat numbers.
     * Changes the goatsTurn and moveStage as per rules.
     */
    public void addGoat(int n)
    {
        //TODO 12
        numGoats +=n;
        if (!moveStage && numGoats % 4 == 0)
        {
           goatsTurn = false; 
        }
        if (!moveStage && numTigers == 3)
        {
            moveStage = true;
        }
       
    }
    
    /**
     * returns number of goats
     */
    public int getNumGoats()
    {
        return numGoats;
    }
    
    /**
     * increments tigers and gives turn back to goats
     */
    public void incrTigers()
    {
        //TODO 16
        numTigers += 1;
        addGoat(0);
        goatsTurn = true;
    }
        
    /**
     * Returns the nearest valid location (0-23) on the board to the x,y mouse click.
     * Locations are described in project description on LMS.
     * You will need bkSize & GameViewer.locs to compute the distance to a location.
     * If the click is close enough to a valid location on the board, return 
     * that location, otherwise return -1 (when the click is not close to any location).
     * Choose a threshold for proximity of click based on bkSize.
     */    
    public int nearestLoc(int x, int y, int bkSize)
    {
        // TODO 11
        int proximity = bkSize / 2;
        double minDistance = Math.sqrt((GameViewer.locs[0][0]*bkSize - x)*(GameViewer.locs[0][0]*bkSize - x) + (GameViewer.locs[0][1]*bkSize - y)*(GameViewer.locs[0][1]*bkSize - y));
        int locationIndex = 0;
        for (int i = 1; i < 24; i++)
            {
                int xLoca = GameViewer.locs[i][0]*bkSize;
                int yLoca = GameViewer.locs[i][1]*bkSize;
                if (minDistance > Math.sqrt((xLoca-x)*(xLoca-x) + (yLoca-y)*(yLoca-y)))
                {
                    minDistance = Math.sqrt((xLoca-x)*(xLoca-x) + (yLoca-y)*(yLoca-y));
                    locationIndex = i;
                }
            }
        if (minDistance > proximity)
        return -1;
        else 
        return locationIndex;
    }
    
    /**
     * Returns true iff a move from location a to b is legal, otherwise returns false.
     * For example: a,b = 1,2 -> true; 1,3 -> false; 20,17 -> true. Refer to the 
     * project description for details.
     * Throws an exception for illegal arguments.
     */
    public boolean isLegalMove(int a, int b)
    {
        //TODO 19
        if ((0 <= a && a <= 23) && ((0 <= b && b <= 23)))
        {
        for (int i = 0; i < legalMoves[a].length; i++)
                {
                    if (b == legalMoves[a][i])
                        {
                            return true;
                        }
                }
               return false;
        }
        else
        {
            throw new IllegalArgumentException("one or both of your argument(s) is not valid");
        }        
        
    }
    
    /**
     * Returns true of the tiger at tigerLoc (location) can eat any goat
     * the location of the goat that can be eaten is filled in scapeGoat[0]
     * the destination where the tiger will land after eating the goad is 
     * filled in scapeGoat[1]. Returns false if the tiger cannot eat any goat
     * 
     * NOTE: This method can use legalEats[][] only and no other array.
     */
    public boolean canEatGoat(int tigerLoc, Board bd, int[] scapeGoat)
    {
        //TODO 23      
        for (int i = 0; i < legalEats[tigerLoc].length; i+=2)
        {
           if(bd.isGoat(legalEats[tigerLoc][i]) && bd.isVacant(legalEats[tigerLoc][i+1]))
                {
                    scapeGoat[0] = legalEats[tigerLoc][i];
                    scapeGoat[1] = legalEats[tigerLoc][i+1];
                    return true;
                } 
        }
        return false;        
    }
}
