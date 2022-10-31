
/**
 * Implments a simple AI player to 
 * automatically contol the tiger moves
 *
 * @Student 1 Name: Du "Justin" Duong 
 * @Student 1 Number: 23179573
 * 
 * @Student 2 Name: Midhin Viju
 * @Student 2 Number: 22850881
 */

import java.util.*;

public class AIplayer
{
    private Random rn; // for random tiger or location selection
    private GameRules rul; // an instance of GameRules to check for legal moves
    private int[] tigerLocs; // location of tigers for convenience 
    private int ntigers; // number of tigers placed
    
    
    //use these two arrays only for the implementation the of the strat for the AI placement of tigers, and nothing else
    private int[] goatLocs; // used to store locations of all the goats
    private final int[][] legalEats = {{1,2, 9,21, 3,6}, {4,7}, {1,0, 5,8, 14,23}, {4,5, 10,18},{},
    {4,3, 13,20}, {3,0, 7,8, 11,15}, {4,1}, {5,2, 7,6, 12,17}, {10,11},{}, {10,9}, {13,14},{},
    {13,12}, {11,6, 16,17, 18,21}, {19,22}, {12,8, 16,15, 20,23}, {10,3, 19,20},{}, {13,5, 19,18},
    {9,0, 18,15, 22,23}, {19,16}, {14,2, 20,17, 22,21}};
    /**
     * Constructor for objects of class AIplayer.
     * Initializes instance variables.
     */
    public AIplayer()
    {
        // TODO 14
        ntigers = 0;
        rn = new Random();
        tigerLocs = new int[3];
        rul = new GameRules();
        goatLocs = new int[12];
    }

    /**
     * Place tiger in a vacant location, choosing location in such a way that
     * if possible will set the tiger up to eat a goat
     * Update the board, the tiger count and tigerLocs.
     */
    public void placeTiger(Board bd)
    {
        //TODO 15
        int isPlaced = 0;
        int randX = 0;
        int goatLoc = 0;
        int [] corners = new int[]{0 , 2 , 6 , 8 , 15 , 17 ,21 , 23};    
        // obtain the locations of all the goats
        for (int i = 0; i < 24; i++)
                {
                    if(bd.isGoat(i))
                    {
                        goatLocs[goatLoc] = i;
                        goatLoc +=1;
                    }
                }
        Arrays.sort(corners);
        // this triple nested loop run through the list of goat locations
        // and pick out the first one that is eatable by a tiger, then place
        // a tiger in such a way that will allow the tiger to eat that goat if the human player
        // does not interfere
        outerloop:
                for (int loc : goatLocs)
                {
                    for (int i = 0; i < legalEats.length; i++) 
                    {  
                        for (int j = 0; j < legalEats[i].length; j+=2) 
                          {
                            if(Arrays.binarySearch(corners,loc) < 0 && bd.isVacant(i) && legalEats[i][j] == loc && bd.isVacant(legalEats[i][j+1]))
                            {
                            bd.setTiger(i);
                            isPlaced = 1;
                            ntigers +=1;
                            tigerLocs[ntigers - 1] = i;
                            break outerloop;
                            }
                      }
                    }
                }
        // if there is no goat that is eateble, place the tiger in a random spot
        if (ntigers < 3 && isPlaced == 0)
        {
            do{    
            randX = rn.nextInt(24);
            }while (!(bd.isVacant(randX)));
            bd.setTiger(randX);
            ntigers +=1;
            tigerLocs[ntigers - 1] = randX;
        }
    }
    
    /**
     * If possible to eat a goat, must eat and return 1
     * If cannot eat any goat, make a move and return 0
     * If cannot make any move (all Tigers blocked), return -1
     */
    public int makeAmove(Board bd)
    {
        if (eatGoat(bd))  return 1; // did eat a goat
        else if (simpleMove(bd)) return 0; // made a simple move
        else return -1; // could not move
    }
    
    /**
     * Randomly choose a tiger, move it to a legal destination and return true
     * if none of the tigers can move, return false.
     * Update the board and the tiger location (tigerLocs)
     */
    private boolean simpleMove(Board bd)
    {
        //TODO 21
        int availMove = 0;
        int[] dummyPara = new int[2];
        for(int tiger : tigerLocs)
        {
            int countMove = 0;
            for(int i=0;i<rul.legalMoves[tiger].length;i++)
            {
                if(bd.isVacant(rul.legalMoves[tiger][i]) || rul.canEatGoat(tiger, bd, dummyPara))
                {
                        countMove+=1;
                        availMove+=countMove;
                }
            }
        }
        
        if(availMove > 0)
        {
            int rando;
            int chosenTiger;
            do
            {
            rando = rn.nextInt(3);
            chosenTiger = tigerLocs[rando]; 
            for(int j=0;j<rul.legalMoves[chosenTiger].length;j++)
            {
                if(bd.isVacant(rul.legalMoves[chosenTiger][j]))
                {
                   bd.swap(tigerLocs[rando],rul.legalMoves[chosenTiger][j]);
                   tigerLocs[rando] = rul.legalMoves[chosenTiger][j];
                }
            }
            }while(tigerLocs[rando] == chosenTiger);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    /**
     * If possible, eat a goat and return true otherwise return false.
     * Update the board and the tiger location (tigerLocs)
     * 
     * Hint: use the canEatGoat method of GameRules
     */
    private boolean eatGoat(Board bd)
    {
        //TODO 22 
        boolean didEat = false;
        int[] scapeGoat = new int[2];
        for(int i =0; i <tigerLocs.length; i++)
        {
            if(rul.canEatGoat(tigerLocs[i], bd, scapeGoat))
            {
                bd.swap(tigerLocs[i],scapeGoat[1]);
                tigerLocs[i] = scapeGoat[1];
                bd.setVacant(scapeGoat[0]);
                didEat = true;
                break;
            }
        }
        return didEat;
    }
   
}
