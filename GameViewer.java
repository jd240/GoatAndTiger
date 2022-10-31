
/**
 * Controls the drawing of the board and game play. 
 * Allows the human player to make goat moves.
 * Calls AIplayer to make tiger moves.
 *
 * @Student 1 Name: Du "Justin" Duong 
 * @Student 1 Number: 23179573
 * 
 * @Student 2 Name: Midhin Viju
 * @Student 2 Number: 22850881
 */

import java.awt.*;
import java.awt.event.*; 
import javax.swing.SwingUtilities;

public class GameViewer implements MouseListener, ActionListener
{
    // instance variables
    private int bkSize; // block size - all other measurements to be derived from bkSize
    private int brdSize; // board size
    private SimpleCanvas sc; // an object of SimpleCanvas to draw 
    private GameRules rules; // an object of GameRules
    private Board bd; // an object of Board
    private AIplayer ai; //an object of AIplayer
    private AIGoatplayer aiG;
    private boolean isTiger; // determine if the player want to be the tiger
    // 2D coordinates of valid locations on the board in steps of block size                                  
    public static final int[][] locs = {{1,1},                  {4,1},                  {7,1},
    
                                                {2,2},          {4,2},          {6,2},
                                                
                                                        {3,3},  {4,3},  {5,3}, 
                                                        
                                        {1,4},  {2,4},  {3,4},          {5,4},  {6,4},  {7,4},
                                        
                                                        {3,5},  {4,5},  {5,5},
                                                        
                                                {2,6},          {4,6},          {6,6},        
                                        
                                        {1,7},                  {4,7},                  {7,7} };
                                 
    // source and destination for the goat moves                             
    private int[] mov = {-1,-1}; //-1 means no selection

    /**
     * Constructor for objects of class GameViewer. 
     * Initializes instance variables and adds mouse listener.
     * Draws the board.
     * Human play as goat
     */
    public GameViewer(int bkSize)
    {        
        this.bkSize = bkSize;
        isTiger = false;
        brdSize = bkSize*8;
        sc = new SimpleCanvas("Tigers and Goats", brdSize, brdSize, Color.BLUE);
        sc.addMouseListener(this);           
        rules = new GameRules();
        bd = new Board();
        ai = new AIplayer();              
        drawBoard();                      
    }
    
    /**
     * Constructor for objects of class GameViewer
     * Initializes instance variables and adds mouse listener.
     * Draws the board.
     * Human get to choose to play as either goat or tiger
     */
    public GameViewer(int bkSize, boolean isTiger)
    {        
        this.bkSize = bkSize;
        this.isTiger = isTiger;
        brdSize = bkSize*8;
        sc = new SimpleCanvas("Tigers and Goats", brdSize, brdSize, Color.BLUE);
        sc.addMouseListener(this);           
        rules = new GameRules();
        bd = new Board();
        if (isTiger)
        {aiG = new AIGoatplayer(bkSize);}
        else if(!isTiger)
        {ai = new AIplayer();}
        drawBoard();                      
    }

    /**
     * Constructor with default block size and player as Goat
     */
    public GameViewer()
    {
        this(80, false);
    }
    
    /**
     * Draws the boad lines and the pieces as per their locations.
     * Drawing of lines is provided, students to implement drawing 
     * of pieces and number of goats.
     */
    private void drawBoard()
    {
        sc.drawRectangle(0,0,brdSize,brdSize,Color.BLUE); //wipe the canvas
        int numbGoat = rules.getNumGoats();
        boolean isMovestage = rules.isMoveStage();
        //draw shadows of Goats and Tigers - not compulsory, for beauty only /////////////
        
        //////////////////////////////////////////////////////                
        // Draw the lines
        for(int i=1; i<9; i++)
        {
            //draw the red lines
            if(i<4)
                sc.drawLine(locs[i-1][0]*bkSize, locs[i-1][1]*bkSize,
                        locs[i+5][0]*bkSize, locs[i+5][1]*bkSize, Color.red);
            else if(i==4)
                sc.drawLine(locs[i+5][0]*bkSize, locs[i+5][1]*bkSize,
                        locs[i+7][0]*bkSize, locs[i+7][1]*bkSize, Color.red);
            else if(i==5)
                sc.drawLine(locs[i+7][0]*bkSize, locs[i+7][1]*bkSize,
                        locs[i+9][0]*bkSize, locs[i+9][1]*bkSize, Color.red);              
            else
                sc.drawLine(locs[i+9][0]*bkSize, locs[i+9][1]*bkSize,
                        locs[i+15][0]*bkSize, locs[i+15][1]*bkSize, Color.red);              
           
            if(i==4 || i==8) continue; //no more to draw at i=4,8
            // vertical white lines
            sc.drawLine(i*bkSize, i*bkSize,
                        i*bkSize, brdSize-i*bkSize,Color.white);            
            // horizontal white lines
            sc.drawLine(i*bkSize,         i*bkSize,
                        brdSize-i*bkSize, i*bkSize, Color.white);  
            
        }
        
        // TODO 10 
        // Draw the goats and tigers. (Drawing the shadows is not compulsory)
        for (int i = 0; i < 24; i++)
        {
            if(bd.isGoat(i))
            {
                sc.drawDisc(locs[i][0]*bkSize + bkSize / 8, locs[i][1]*bkSize + bkSize / 8, bkSize / 4, Color.BLACK);
                sc.drawDisc(locs[i][0]*bkSize, locs[i][1]*bkSize, bkSize / 4, Color.GREEN);
            }
            else if(!(bd.isVacant(i)))
            {
                int upperX = locs[i][0]*bkSize - ((bkSize * 5 / 8) / 2); 
                int upperY = locs[i][1]*bkSize - ((bkSize * 5 / 8) / 2);
                int lowerX = upperX + (bkSize  * 5 / 8) ; 
                int lowerY = upperY + (bkSize  * 5 / 8) ;
                sc.drawRectangle(upperX + bkSize / 8, upperY + bkSize / 8, lowerX + bkSize / 8, lowerY + bkSize / 8, Color.BLACK);
                sc.drawRectangle(upperX , upperY , lowerX, lowerY, Color.RED);
            }
        }
        // Display the number of goats        
        
        String display = "Number of Goats: " + numbGoat;
        sc.drawString(display, locs[22][0] * bkSize - bkSize, (locs[22][1]) * bkSize + (bkSize  * 6 / 8), Color.WHITE);
        sc.setForegroundColour(Color.GREEN);
        
        if(!isTiger && !isMovestage)
        {
                String display2 = "Please Place Your Goat."; 
                sc.drawString(display2, locs[1][0] * bkSize -bkSize, (locs[1][1]) * bkSize -bkSize/2, Color.WHITE);
        }
        else if(isTiger && !isMovestage)
        {
            if(numbGoat > 0 && numbGoat % 4 == 0)
            {
                String display2 = "Please Place Your Tiger."; 
                sc.drawString(display2, locs[1][0] * bkSize - bkSize, (locs[1][1]) * bkSize -bkSize/2, Color.WHITE);
            }
            else
            {
                String display2 = "Click to signal the AI to place a goat"; 
                sc.drawString(display2, locs[1][0] * bkSize - bkSize, (locs[1][1]) * bkSize -bkSize/2, Color.WHITE);
            }
        }
        
        if (!isMovestage)
        {
        String display3 = "Current Stage: Placement";
        sc.drawString(display3, locs[22][0] * bkSize - bkSize, (locs[22][1]) * bkSize + (bkSize / 2) , Color.WHITE);
        }
        else 
        {
        String display3 = "Current Stage: Move";
        sc.drawString(display3, locs[22][0] * bkSize - bkSize, (locs[22][1]) * bkSize + (bkSize / 2) , Color.WHITE);
        }
    }
    
    /**
     * If vacant, place a goat at the user clicked location on board.
     * Update goat count in rules and draw the updated board
     */
    public void placeGoat(int loc) 
    {   
        //TODO 2
        if (bd.isVacant(loc))
        {
            bd.setGoat(loc);
            rules.addGoat(1);
            drawBoard();
        }
    }
    
    /**
     * If vacant, place a tiger at the user clicked location on board.
     * If the number of goat is 12 after placing a tiger (you have placed the third tiger)
     * call aiGoatMove() to tell the ai to move a goat since goat get to go first.
     * Update tiger count in rules and draw the updated board
     */
    public void HumanPlaceTiger(int loc)
    {
        if (bd.isVacant(loc) && !(rules.isGoatsTurn()))
        {
            bd.setTiger(loc);
            rules.incrTigers();
            if(rules.getNumGoats() == 12)
            {
                aiGoatMove();
            }
            drawBoard();
        }
    }
    
    /**
     * Calls the placeTiger method of AIplayer to place a tiger on the board.
     * Increments tiger count in rules.
     * Draws the updated board.
     */
    public void placeTiger() 
    {   
        //TODO 13
            ai.placeTiger(bd);
            rules.incrTigers();
            drawBoard();
        
    }
    
    /**
     * Calls the placeGoat method of AiGoatplayer to place a goat on the board.
     * Increments goat count in rules.
     * Draws the updated board.
     */
    public void AiplaceGoat() 
    {   
        if(!rules.isMoveStage() && rules.getNumGoats() < 12)
        {
        aiG.placeGoat(bd);
        rules.addGoat(1);
        drawBoard();
        }
    }
   
    /**
     * Toggles goat selection - changes the colour of selected goat.
     * Resets selection and changes the colour back when the same goat is clicked again.
     * Selects destination (if vacant) to move and calls moveGoat to make the move.
     */
    public void selectGoatMove(int loc) 
    {   
        //TODO 16
        //Color currentColor = sc.getForegroundColour();
        if (!(bd.isVacant(loc)) && bd.isGoat(loc) && mov[0] == -1)
            {
                Color selected = Color.YELLOW;
                sc.drawDisc(locs[loc][0]*bkSize, locs[loc][1]*bkSize, bkSize / 4, selected);
                mov[0] = loc;
                
            }
        
        else if (bd.isVacant(loc))
            {
                mov[1] = loc;
            }
        else if (!(bd.isVacant(loc)) && bd.isGoat(loc) && mov[0] == loc)
            {
                sc.drawDisc(locs[loc][0]*bkSize, locs[loc][1]*bkSize, bkSize / 4, Color.GREEN);
                mov[0] = -1;
            }
        
       if (mov[1] != -1 && mov[0] != -1 && (bd.isVacant(loc)))
        {
          moveGoat();
        }
    }
    
    /**
     * Make the user selected goat move only if legal otherwise set the destination to -1 (invalid).
     * If did make a goat move, then update board, draw the updated board, reset mov to -1,-1.
     * and call tigersMove() since after every goat move, there is a tiger move.
     */
    public void moveGoat() 
    {   
        //TODO 18        
        if (rules.isLegalMove(mov[0],mov[1]))
        {
            bd.swap(mov[0],mov[1]);
            drawBoard();
            mov[0] = -1;
            mov[1] = -1;
            tigersMove();
            
        }
        else
        {
            mov[1] = -1;
        }
    }

    /**
     * Toggles Tiger selection - changes the colour of selected Tiger.
     * Resets selection and changes the colour back when the same Tiger is clicked again.
     * Selects destination (if vacant) to move and calls playerTigermove to make the move.
     */
    public void selectTigerMove(int loc) 
    {   
        if (loc !=-1 && !(bd.isVacant(loc)) && !(bd.isGoat(loc)) && mov[0] == -1)
            {
                Color selected = Color.YELLOW;
                int upperX = locs[loc][0]*bkSize - ((bkSize * 5 / 8) / 2); 
                int upperY = locs[loc][1]*bkSize - ((bkSize * 5 / 8) / 2);
                int lowerX = upperX + (bkSize  * 5 / 8) ; 
                int lowerY = upperY + (bkSize  * 5 / 8) ;
                sc.drawRectangle(upperX , upperY , lowerX, lowerY, selected);
                mov[0] = loc;  
            }
        
        else if (bd.isVacant(loc))
            {
                mov[1] = loc;
            }
        else if (loc !=-1 && !(bd.isVacant(loc)) && !(bd.isGoat(loc)) && mov[0] == loc)
            {
                int upperX = locs[loc][0]*bkSize - ((bkSize * 5 / 8) / 2); 
                int upperY = locs[loc][1]*bkSize - ((bkSize * 5 / 8) / 2);
                int lowerX = upperX + (bkSize  * 5 / 8) ; 
                int lowerY = upperY + (bkSize  * 5 / 8) ;
                sc.drawRectangle(upperX , upperY , lowerX, lowerY, Color.RED);
                mov[0] = -1;
            }
        
       if (mov[1] != -1 && mov[0] != -1 && (bd.isVacant(loc)))
        {
          playerTigermove();
        }
    }
    
    /**
     * Make the user selected tiger move only if legal otherwise set the destination to -1 (invalid).
     * If did make a tiger move, then update board, draw the updated board, reset mov to -1,-1.
     * and call aiGoatMove() to tell the AI to move the goat.
     */
    public void playerTigermove() 
    {          
        int goatToEat = aiG.islegalEatMove(mov[0],mov[1], bd);
        if(goatToEat!= -1)
        {
            bd.swap(mov[0],mov[1]);
            bd.setVacant(goatToEat);
            drawBoard();
            mov[0] = -1;
            mov[1] = -1;
            rules.addGoat(-1);
            aiGoatMove();
        }
        else if (rules.isLegalMove(mov[0],mov[1]))
        {
            bd.swap(mov[0],mov[1]);
            drawBoard();
            mov[0] = -1;
            mov[1] = -1;
            aiGoatMove();
        }
        else
        {
            mov[1] = -1;
        }
    }
    
/**
     * Call AIplayer to make its move. Update and draw the board after the move.
     * If Tigers cannot move, display "Goats Win!".
     * If goats are less than 6, display "Tigers Win!".
     * No need to terminate the game.
     */
    public void tigersMove()
    {
        //TODO 20
        int status;
        status = ai.makeAmove(bd);
        if (status == 1)
        {
            rules.addGoat(-1);
            if (rules.getNumGoats() < 6)
            {
                drawBoard();
                String display = "TIGER WIN!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
            }
            else
                drawBoard();
        }
        else if (status == -1)
        {
            drawBoard();
            String display = "GOAT WIN!!!"; 
            sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
        }
        else
        {
            drawBoard();
        }
        
    }
   
    /**
     * Call AiGoatplayer to make its move. Update and draw the board after the move.
     * If Tigers cannot move, display "Goats Win!".
     * If goats are less than 6, display "Tigers Win!".
     * 
     */
    public void aiGoatMove()
    {   
        int status = aiG.makeAmove(bd);
        if (status == 1)
        {
            if (rules.getNumGoats() < 6)
            {
                drawBoard();
                String display = "Wait, Tiger win? Is that even possible!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
            }
            else if (aiG.numTigerblocked(bd) == 3)
            {
                drawBoard();
                String display = "OMG THE STUPID AI GOAT ACTUALLY WIN!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
                
            }
                else
                drawBoard();
        }
        else if (status == 2)
        {
           if (rules.getNumGoats() < 6)
            {
                drawBoard();
                String display = "Wait, Tiger win? Is that even possible!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
            }
            else if (aiG.numTigerblocked(bd) == 3)
            {
                drawBoard();
                String display = "OMG THE STUPID AI GOAT ACTUALLY WIN!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
                
            }
                else
                drawBoard();
        }
        else if (status == 3)
        {
           if (rules.getNumGoats() < 6)
            {
                drawBoard();
                String display = "Wait, Tiger win? Is that even possible!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
            }
            else if (aiG.numTigerblocked(bd) == 3)
            {
                drawBoard();
                String display = "OMG THE STUPID AI GOAT ACTUALLY WIN!!!"; 
                sc.drawString(display, locs[1][0] * bkSize - (bkSize  * 5 / 8) , (locs[1][1]) * bkSize - (bkSize  * 5 / 8) , Color.WHITE);
                
            }
            else
                drawBoard();
        }  
    }    
    
    /**
     * Respond to a mouse click on the board. 
     * Get a valid location nearest to the click (from GameRules). 
     * If nearest location is still far, do nothing. 
     * Otherwise, call placeGoat to place a goat at the location.
     * Call this.placeTiger when it is the tigers turn to place.
     * When the game changes to move stage, call selectGoatMove to move 
     * the user selected goat to the user selected destination.
     */
    public void mousePressed(MouseEvent e) 
    {
        //TODO 1
        int click = rules.nearestLoc(e.getX(), e.getY(), bkSize);
        
        // This if statement handles clicks during placement 
        // stage when the Human player is the goats, and AI is the tigers
        if (!(rules.isMoveStage()) && !isTiger) 
        {
            if(rules.getNumGoats() < 12 && click != -1 && rules.isGoatsTurn())
            {
                placeGoat(click); 
            }
            if (rules.getNumGoats() % 4 == 0 && !(rules.isGoatsTurn()))
            {
                this.placeTiger();
            }
        }
        // This if statement handles clicks during placement 
        // stage when the Human player is the tigers, and AI is the goats
        else if (!(rules.isMoveStage()) && isTiger) 
        {
            if(rules.getNumGoats() <= 12 && click != -1 && !(rules.isGoatsTurn()))
            {
                HumanPlaceTiger(click); 
            }
            if (rules.isGoatsTurn())
            {
                this.AiplaceGoat();
            }
        }
        // This if statement handles clicks during move 
        // stage when the Human player is the goats, and AI is the tigers
        else if (rules.isMoveStage() && !isTiger) 
        {
            selectGoatMove(click);
        }
        // This else statement handles clicks during placement 
        // stage when the Human player is the tigers, and AI is the goats
        else
        {
            selectTigerMove(click);
        }
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void actionPerformed(ActionEvent e) 
    {
        
    }
}
