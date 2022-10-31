
/**
 * Implments the AI player to 
 * automatically contol the goats
 * 
 *
 * @author Du Duong
 * @dated Oct 2021
 * 
 */
import java.util.*;

public class AIGoatplayer
{
    // instance variables - replace the example below with your own
    private Random rn; // for random tiger or location selection
    private int bkSize;
    private GameRules rul; // an instance of GameRules to check for legal moves
    private int[] goatLocs; // location of goats 
    private int ngoats; // number of goat placed
    private int[] tigerLocs; // location of goats
    private int numTigerblocked; // number of tiger blocked
    boolean AdvanceMoveNext; // determine if an Advancing move should be made
    private final int[][] legalEats = {{1,2, 9,21, 3,6}, {4,7}, {1,0, 5,8, 14,23}, {4,5, 10,18},{},
    {4,3, 13,20}, {3,0, 7,8, 11,15}, {4,1}, {5,2, 7,6, 12,17}, {10,11},{}, {10,9}, {13,14},{},
    {13,12}, {11,6, 16,17, 18,21}, {19,22}, {12,8, 16,15, 20,23}, {10,3, 19,20},{}, {13,5, 19,18},
    {9,0, 18,15, 22,23}, {19,16}, {14,2, 20,17, 22,21}};
    /**
     * Constructor for objects of class AIGoatplayer
     *  Initializes instance variables
     */
    public AIGoatplayer(int bkSize)
    {
        // initialise instance variables
        this.bkSize = bkSize;
        ngoats = 0;
        rn = new Random();
        goatLocs = new int[12];
        rul = new GameRules();
        tigerLocs = new int[3];
        AdvanceMoveNext = false;
    }

    /**
     * Checks if a tiger at location a can jump to b and eat the goat in the way
     * if so returns the goat to be eaten, otherwise returns -1.
     */
    public int islegalEatMove(int a, int b, Board bd)
    {
        int toEat = -1;
        if (!bd.isGoat(a) && !bd.isVacant(a))
        {
            for (int i=1; i<legalEats[a].length;i+=2) 
            {
               if(b == legalEats[a][i] && bd.isGoat(legalEats[a][i-1]))
               {
                   toEat = legalEats[a][i-1];
                   return toEat;
                }
            }
        }
    return toEat;
    }
    
    /**
     * This fill the array tigerLocs with tiger locations
     */
    private void updateTigerloc(Board bd)
    {
        int tigerLoc = 0;
            for (int i = 0; i < 24; i++)
                {
                    if(!(bd.isGoat(i)) && !(bd.isVacant(i)))
                    {
                        tigerLocs[tigerLoc] = i;
                        tigerLoc +=1;
                    }
                }
    }
    
    /**
     * This fill the array goatLocs with tiger locations
     */
    private void updateGoatloc(Board bd)
    {
        int goatLoc = 0;
            for (int i = 0; i < 24; i++)
                {
                    if(bd.isGoat(i))
                    {
                        goatLocs[goatLoc] = i;
                        goatLoc +=1;
                    }
                }
    }
    
    /**
     * Place goats. The fisrt four in the outermost corner 
     * then the rest eight will be used to block the first
     * and second goat, leaving only the third free.
     * Update the board, the goat count and goatLocs.
     */
    public void placeGoat(Board bd)
    {
        int [] corners = new int[]{0, 2 ,21 ,23};
        int isPlaced = 0;
        int randX = 0;
        int[] scapegoat = new int[2];
        updateTigerloc(bd);
        updateGoatloc(bd);
        for (int loc : corners)
        {
            if(bd.isVacant(loc))
            {
                bd.setGoat(loc);
                ngoats+=1;
                goatLocs[ngoats-1] = loc;
                isPlaced = 1;
                break;
            }
        }
        
        if(ngoats < 12 && isPlaced == 0)
        {
            boolean isTiger1blocked = isBlocked(tigerLocs[0],bd); // check if the first tiger is blocked
            
            // blocking the first tiger. 
            if(ngoats >= 4 && !(isTiger1blocked))
            {
                for(int i=0; i<rul.legalMoves[tigerLocs[0]].length;i++)
                {
                    if(bd.isVacant(rul.legalMoves[tigerLocs[0]][i])) 
                    {
                        bd.setGoat(rul.legalMoves[tigerLocs[0]][i]);
                        ngoats+=1;
                        goatLocs[ngoats-1] = rul.legalMoves[tigerLocs[0]][i];
                        break;
                    }
                    else if (rul.canEatGoat(tigerLocs[0],bd,scapegoat))
                    {
                        bd.setGoat(scapegoat[1]);
                        ngoats+=1;
                        goatLocs[ngoats-1] = scapegoat[1];
                        break;
                    }
                }
            }
            // blocking the second tiger
            else if(ngoats >= 4 && isTiger1blocked && !(isBlocked(tigerLocs[1],bd)))
            {
                for(int i=0; i<rul.legalMoves[tigerLocs[1]].length;i++)
                {
                    
                        if(bd.isVacant(rul.legalMoves[tigerLocs[1]][i]))
                        {
                            bd.setGoat(rul.legalMoves[tigerLocs[1]][i]);
                            ngoats+=1;
                            goatLocs[ngoats-1] = rul.legalMoves[tigerLocs[1]][i];
                            break;
                        }
                        else if (rul.canEatGoat(tigerLocs[1],bd,scapegoat))
                        {
                            bd.setGoat(scapegoat[1]);
                            ngoats+=1;
                            goatLocs[ngoats-1] = scapegoat[1];
                            break;
                        }
                    
                }
            }
            // if both tigers are lock without using all 12 goats, place goat in a random location
            else
            {
               do{    
                randX = rn.nextInt(24);
                }while (!(bd.isVacant(randX)));
                bd.setGoat(randX);
                ngoats +=1;
                goatLocs[ngoats - 1] = randX; 
            }
        }
    }
    
    /**
     * Returns true if a goat at location loc is being threatened by a tiger, otherwise returns false.
     * If true, update the array scapegoat with scapegoat[0] to the goat's location, scapegoat[1] 
     * to the location the tiger threathing the goat will land and scapegoat[2] to the location of the tiger itself
     */
    private boolean isThreatened(int loc, Board bd, int[] scapeGoat)
    {
        int numTiger = 0;
        boolean threatned = false;
        for (int i = 0; i < legalEats.length; i++) 
        {
              for (int j = 0; j < legalEats[i].length; j+=2) 
              {
                if(legalEats[i][j] == loc && (!(bd.isGoat(i)) && !(bd.isVacant(i)) && bd.isVacant(legalEats[i][j+1])))
                {
                    numTiger +=1;
                    scapeGoat[0] = loc;
                    scapeGoat[1] = legalEats[i][j+1];
                    scapeGoat[2] = numTiger; 
                    threatned = true;
                }
              }
        }
        return threatned;
    }
    
    /**
     * Returns true if a goat at location loc is "occupied", false otherwise
     * a goat is occupied if it is blocking a tiger or it is preventing another
     * goat from being eatn.
     */
    private boolean isOccupied(int loc, Board bd)
    {
        for (int i = 0; i< rul.legalMoves[loc].length; i++)
        {
            if(!(bd.isVacant(rul.legalMoves[loc][i])) && !(bd.isGoat(rul.legalMoves[loc][i])) || isPreventEat(loc, bd))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns true if a goat at location a can move to location loc
     * without putting itself in the mouth of a tiger, false otherwise
     */
    private boolean isSafemove(int a, int loc, Board bd)
    {
        boolean result = true;
        if (bd.isGoat(a))
        {
            bd.setVacant(a); 
            for (int i = 0; i < legalEats.length; i++) 
            {  
                for (int j = 0; j < legalEats[i].length; j+=2) 
                  {
                    if(legalEats[i][j] == loc && (!(bd.isGoat(i)) && !(bd.isVacant(i)) && bd.isVacant(legalEats[i][j+1])))
                    {
                        result = false;
                        break;
                    }
                    
                  }
            }
            bd.setGoat(a);
        }
        return result;
    }
    
    /**
     * Returns true if a goat at location a is preventing some other goat
     * somewhere else from being eaten by blocking the tiger, false otherwise
     */
    private boolean isPreventEat(int a, Board bd)
    {
        boolean result = false;
        if (bd.isGoat(a))
        {
            bd.setVacant(a);
            for (int i = 0; i < legalEats.length; i++) 
            {  
                for (int j = 1; j < legalEats[i].length; j+=2) 
                  {
                    if(legalEats[i][j] == a && !(bd.isGoat(i)) && !(bd.isVacant(i)) && bd.isGoat(legalEats[i][j-1]))
                    {
                        result = true;
                        break;
                    }
                    
                  }
            }
            bd.setGoat(a);
        }
        return result;
    }
    /**
     * Update and return the number of tigers that are blocked
     */
    public int numTigerblocked(Board bd)
    {
        updateTigerloc(bd);
        numTigerblocked = 0;
        for (int tiger: tigerLocs)
        {
            if(isBlocked(tiger, bd))
            {
                numTigerblocked+=1;
            }
        }
        return numTigerblocked;
    }
    
    /**
     * Return the numbers of legal and available moves a target at location loc has 
     */
    private int moveAvail (int loc, Board bd)
    {
        int countMove = 0;
        int numMove = 0;
        for(int i=0;i<rul.legalMoves[loc].length;i++)
        {
            if(bd.isVacant(rul.legalMoves[loc][i]))
            {
                countMove+=1; 
                numMove+=countMove;
            }
        }
        return numMove;
    }
    /**
     * Return true if a tiger at position loc is blocked
     * and cannot move (no legal moves + cannot eat)
     */
    private boolean isBlocked(int loc, Board bd)
    {
        int availMove = 0;
        int[] dummyParameter = new int[2];
        if(moveAvail (loc, bd) == 0 && !(rul.canEatGoat(loc,bd,dummyParameter)))
        return true;
        else
        return false;
    }
    /**
     * This method chooses and return a tiger for the goat to
     * "attack". It will always choose the tiger with the most
     * available moves.
     */
    private int tigerTarget(Board bd)
    {
        int tigerTarget = -1;
        int[] moveLefts = new int[3];
        moveLefts[0] =  moveAvail (tigerLocs[0], bd);
        
        int maxMove = moveLefts[0];
        for(int i = 1; i< tigerLocs. length;i++)
            {
                 moveLefts[i] =  moveAvail (tigerLocs[i], bd);
                 if(maxMove < moveLefts[i])
                     {
                         maxMove = moveLefts[i];
                    }
            }
        
            for(int tiger : tigerLocs)
           {
               if(!(isBlocked(tiger,bd)) && moveAvail (tiger, bd) == maxMove)
               {
                   tigerTarget = tiger;
                   break;
                }
            }
            
        return tigerTarget;
    }
    
    /**
     * This method return a list of goats that can potentially move to location moveLoc regardless
     * of whether it is safe to do so or if that goat is occupied 
     */
    private ArrayList potentialGoatTarget(int tigerLoc, int moveLoc, Board bd)
    {
        ArrayList<Integer> potentialGoatTarget = new ArrayList<Integer>();
        for(int i = 0; i < rul.legalMoves.length; i++)
           {
               for(int j = 0; j < rul.legalMoves[i].length; j++)
                {
                    if(i != tigerLoc && bd.isGoat(i) && rul.legalMoves[i][j] == moveLoc)
                    {
                        potentialGoatTarget.add(i);
                    }
                }
            }
        return potentialGoatTarget;
    }
    
    /**
     * If possible make to a defensive move, must do, 
     * then return 1 and update the number of tiger blocked
     * If cannot make a defensive move, make an Offensive Move, 
     * then if the number of tiger blocked is 2, 
     * update AdvanceMoveNext and return 0
     * If cannot make ake an Offensive, make an Advancing move
     * then update AdvanceMoveNext and return 3
     */
    public int makeAmove(Board bd)
    {
        updateGoatloc(bd);
        updateTigerloc(bd);
        numTigerblocked(bd);
        if (DefensiveMove(bd))  
        {
            numTigerblocked(bd);
            return 1; 
        }
        else if(!AdvanceMoveNext && OffensiveMove(bd)) 
        { 
            numTigerblocked(bd);
            if (numTigerblocked(bd) == 2)
                {AdvanceMoveNext = true;}
            return 2;
        }
        else 
        {
            advancingMove(bd);
            AdvanceMoveNext = false;
            numTigerblocked(bd);
            return 3;
        }
    }
    
    /**
     * Picks a free goat that is the farthest away from a specifically chosen tiger from a pool
     * then tries to move it move in such a that it will put the goat closer to the tiger; 
     * however if somehow there is no goat that is free, the AI will choose a goat that is occupied
     * and move to a possible location.
     */
     private void advancingMove(Board bd)
    {
        updateGoatloc(bd);   
        int goatTomove = -1;
        int moveTowardTiger = tigerTarget(bd);
        if(moveTowardTiger!= -1)
        {
            ArrayList<Integer> goatPool = new ArrayList<Integer>();
            int tigerXPos = GameViewer.locs[moveTowardTiger][0]*bkSize;
            int tigerYPos = GameViewer.locs[moveTowardTiger][1]*bkSize;
            for (int goat : goatLocs)
            {
                if(moveAvail(goat, bd) > 0 && !(isOccupied(goat, bd)))
                {
                    goatPool.add(goat);
                }
            }    
            if (goatPool.size() == 1)
                {
                    goatTomove = goatPool.get(0);
                }
            else if (goatPool.size() > 1)
                {
                    ArrayList<Double> distanceHolder = new ArrayList<Double>();
                    for (int loc : goatPool)
                    {
                            int xLoca = GameViewer.locs[loc][0]*bkSize;
                            int yLoca = GameViewer.locs[loc][1]*bkSize;
                            double distance = Math.sqrt((xLoca-tigerXPos)*(xLoca-tigerXPos) + (yLoca-tigerYPos)*(yLoca-tigerYPos));
                            distanceHolder.add(distance);
                    }
                    double biggerDistance =  Collections.max(distanceHolder);
                    int chosenGoat = distanceHolder.indexOf(biggerDistance);
                    goatTomove = goatPool.get(chosenGoat);
                }
            
            if (goatTomove != -1 && moveAvail(goatTomove, bd) == 1)
            {
                for (int i = 0; i < rul.legalMoves[goatTomove].length; i++)
                {
                    int moveTo = rul.legalMoves[goatTomove][i];
                    if(bd.isVacant(moveTo) && isSafemove(goatTomove, moveTo, bd))
                    {
                        bd.swap(goatTomove,moveTo);
                        break;
                    }
                }
            }
            else if (goatTomove != -1 && moveAvail(goatTomove, bd) > 1)
            {
                ArrayList<Integer> qualifiedMoves = new ArrayList<Integer>();
                for (int i = 0; i < rul.legalMoves[goatTomove].length; i++)
                {
                   int moveTo = rul.legalMoves[goatTomove][i];
                    if(bd.isVacant(moveTo) && isSafemove(goatTomove, moveTo, bd))
                    {
                        qualifiedMoves.add(moveTo);
                    } 
                }
                if (qualifiedMoves.size() == 1)
                {
                    bd.swap(goatTomove,qualifiedMoves.get(0));
                }
                else
                    {
                        ArrayList<Double> distanceToTiger = new ArrayList<Double>(); 
                        for (int i = 0; i < qualifiedMoves.size(); i++)
                        {
                            int moveTo = rul.legalMoves[goatTomove][i];
                            int xLoca = GameViewer.locs[moveTo][0]*bkSize;
                            int yLoca = GameViewer.locs[moveTo][1]*bkSize;
                            double distance = Math.sqrt((xLoca-tigerXPos)*(xLoca-tigerXPos) + (yLoca-tigerYPos)*(yLoca-tigerYPos));
                            distanceToTiger.add (distance);
                        }
                        double smallerDistance =  Collections.min(distanceToTiger);
                        int chosenMove = distanceToTiger.indexOf(smallerDistance);
                        bd.swap(goatTomove,qualifiedMoves.get(chosenMove));
                    
                    }
            }
            else
            {
                int rando;
                int chosenGoat;
                do
                {
                rando = rn.nextInt(12);
                chosenGoat = goatLocs[rando]; 
                } while(!(isPreventEat(chosenGoat, bd)) && isOccupied(chosenGoat, bd));
    
                for(int j=0;j<rul.legalMoves[chosenGoat].length;j++)
                {
                    if(bd.isVacant(rul.legalMoves[chosenGoat][j]))
                    {
                       bd.swap(goatLocs[rando],rul.legalMoves[chosenGoat][j]);
                       updateGoatloc(bd);
                       break;
                    }
                }
            }
        }
    }
    
    /**
     * Choose a tiger to be the target, then pick out a location that the tiger target
     * can legally move to (exclude eating move), then looks for any goats that can 
     * move to that same location and put them in a list. Finally, check if any goat
     * in the list of goats can safely move that location, if so move it there, and
     * return true. Return false if it is not possible to move a goat to block the tiger.
     */
    private boolean OffensiveMove(Board bd)
    {
        updateTigerloc( bd);
        int target = tigerTarget(bd);
        int destination = -1;
        ArrayList<Integer> goatTargets = new ArrayList<Integer>();
        if(target!= -1)
        {
            outerloop:
            for (int i = 0; i < rul.legalMoves[target].length; i++)
            {
                if(bd.isVacant(rul.legalMoves[target][i]))
                {
                    destination = rul.legalMoves[target][i];
                    goatTargets = potentialGoatTarget(target, destination, bd);
                    if(!(goatTargets.isEmpty()))
                    {
                        for (int loc : goatTargets)
                        {
                            if (!(isOccupied(loc, bd)) && isSafemove(loc,destination, bd)) 
                            {
                                bd.swap(loc,destination);
                                return true;
                            }
                            
                        }
                    }
                }
            
            }
            
        }
        return false;
    }
    
    /**
     * First check if any goat is threatened using isThreatened method, if not return false
     * if there is a threatended goat, then make a list of potential free goats that can
     * block the tiger from eating the threatened goat. Choose a free goat from the list
     * and move it to block the tiger. If there is not free goat, move the threatened tiger
     * out of harm's way.
     */
    private boolean DefensiveMove(Board bd)
    {
        int[] scapegoat = {0,0,0};
        updateGoatloc(bd);
        boolean threatened = false;
        ArrayList<Integer> goatDefender = new ArrayList<Integer>();
        for (int i = 0; i < goatLocs.length; i++)
        {
            if (isThreatened(goatLocs[i],bd,scapegoat))
                {
                    threatened = true;
                    break;
                }
                
        }
        if(threatened)
        {
            goatDefender = potentialGoatTarget(scapegoat[0], scapegoat[1], bd);
            if (scapegoat[2] < 2 && goatDefender.size() > 0 && !(isOccupied(goatDefender.get(0), bd)))
            {
                bd.swap(goatDefender.get(0) ,scapegoat[1]);
                return true;
            }
            else if (scapegoat[2] < 2 && goatDefender.size() > 1 && !(isOccupied(goatDefender.get(1), bd)))
            {
                bd.swap(goatDefender.get(1) ,scapegoat[1]);
                return true;
            }
            else
            {
                bd.swap(scapegoat[0],scapegoat[1]);
                return true;
            }
        }
        else
        return false; 
    }
}
