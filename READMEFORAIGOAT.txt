Hello, Justin here. I would like to apologize to the person that would read the spaghetti that is my code for the AI goats. 
It took more than I expected since there are so many factors you have to consider. 
I would go over what the class does here:
First, let’s talk about goats placement: It’s relatively simple. 
The first four goats will always be in the outermost corners because they are broken positions to be in, simple as that. 
After the first four goats, the first tiger spawn, and the AI will start blocking this tiger as soon as it spawns. 
Once this tiger is blocked, it will move on to blocking the second tiger or at least try to, so in essence, the only truly free tiger the player has is the third one.

Next, let’s talk about the movement of goats. 
The AI employs a few helper methods to help make these moves, such as checking if a move is safe or a goat is free to move 
(This means that goat is not currently blocking a tiger or is preventing a nearby tiger to eat another goat, there are methods to check this). 
There are three types of moves the AI could make: defensive, offensive, and “Advancing” move. 

Defensive is when the AI checks for any goat that is being threatened (via a method) and either move that goat out of the way or moves another free goat in to block the eat. 
This move is always the priority, so an AI will always look for any chance to make a defensive move first.

Offensive occurs when no goat is threatened. The AI tries to corner the tigers by putting a goat straight in its face. 
It does this by first choosing a tiger to target, then it checks all that tiger’s possible non-eat moves, and then decides on which spot it should move a free goat toward that.
 
“Advancing” move is when the AI cannot make an Offensive move, either due to no goat being suitable to move, or moving would just make the goat be eaten by the tiger. 
In the case that 2 tigers have been blocked, an “Advancing” move will always be made if the previous move was an Offensive one. 
This means to give a higher chance that a free goat far from the tiger needed blocking can move closer and join the tiger hunting operation. 
Basically what it does is that it picks the free goat farthest from a specifically chosen tiger then tries to move it in such a that will put the goat closer to the tiger; 
however If somehow there is no goat that is free, the AI will choose a goat that is occupied, which it will never touch unless a tiger is threatening to eat that occupied goat, 
and move it somewhere (This rarely happens, but I put it there just in case)

Together these three moves make it seem like the AI knows what’s doing (It’s still pretty dumb), and it can score a win under the right circumstances; 
though I must admit that I don’t think the human Tiger can eat enough goats to win due to how surprisingly effective the Defensive Move does its job. 
During my testing, the best I can get was 4 goats, and that require 2 tigers attacking in coordination to do so. 
(Then the goats proceeds to absolutely stalemate me into eternity - explain this further down) 
Anyway, due to how hard it is for the tiger to win, it can cause the unfortunate situation where neither side can win, 
a stalemate of short where tigers cant eat goat and goat cant corner all 3 tigers 
It tends to happen when the AI manages to lock 2 tigers, but doing so makes it so that it doesn’t have enough goats left to block the third tiger.
(either because blocking the other two goats use 9 goats, or one or more free goats are trapped and cannot move to a position to block the third tiger) 
Since the AI will never move goats that are already blocking the other two tigers, 
and the human player cannot eat any goat with just 1 free tiger, this causes a stalemate.
I was thinking of adding a draw condition for this using something similar to the threefold repetition in chess 
but then gave up bc it is too complicated and time-consuming to implement.

I am pretty sure that there should be no major bug that crashes the program. 
Some minor bugs (that I did not have enough time to patch) include improper eat, and sometimes, the AI just gets confused and won’t move. 
The first one involves eating a goat. On rare occasions, if a tiger eats a goat at position A, the goat at the adjacent position will be eaten instead. 
The amount of goat count is still updated, and the game still treats as if a goat has been eaten, and thus still operates like normal. Have no idea what causes this.

The second bug involves the AI not moving any goat when you move a tiger. 
If you move the tiger again, the AI will almost always recuperate and operate like normal. 
I suspect this is some loophole in the Moves Coding, but did not have enough time to investigate.

That’s it. This project has been very fun. Thank You!  
