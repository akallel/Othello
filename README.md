# Othello

CSC 242 final project

By Anis Kallel, John Donner, Nathan Contino

aka Team Daybreak

shitty heuristic - corner > edge > inside > more inside

Our project implements the minimax best-move chooser alongside a heuristic of our own design, shittyHeuristic, to create an AI for the popular tile-flipping game Othello. As Dr. Frankenstein once said, "I've created a monster."



MINIMAX

Our implementation of minimax works primarily through the Game3 class, which is assigned a depth by its caller (in our case, the caller is always an instance of class Othello). This class continuously calls itself on every possible move at decreasing depths until it reaches depth 0, at which point each instance of Game3 (there will be quite a few) calls a new instance of Game using every available move. Game contains our actual move-quality heuristic, titled "shittyHeuristic," which assesses the quality of a particular move. Each instance of Game3 then "harvests" the validity of each branching game state, recording the worst and best moves at a particular time. As the Game3 calls return, their parent Game3 calls check the best of their worst possible moves and the worst of their best possible moves. This continues up the "tree" of Game3 calls until we have a final "chosenValue" (a veritable "minimax") for our given gamestate. Our AI then chooses the corresponding move aligned with "chosenValue" that indicates the most promising possible value of our given move choices.

GAME- shittyHeuristic and me

Every good minimax algorithm needs a good heuristic, and we've got the best one we could whip up in two weeks: shittyHeuristic. ShittyHeuristic is rooted in a (relatively) reliable heuristic designed to emulate a very basic AI- it's merely a 2D array that assigns each tile in the game a particular value regardless of the current board state. Corners are always weighted most heavily, followed by edge tiles, with tile values decreasing as you approach the center of the board. The one exception to this rule is corner-adjacent tiles (of which there are 12), which are actually weighted *negatively*, since it's in a player's worst interest to take a corner-adjacent tile, leaving a corner open to the opposing player. ShittyHeuristic augments this tile value system with a "frontier weight" system that either adds or subtracts value to a given board state based on the number of frontier tiles belonging to the current (AI) player, which directly correspond to movement choices for the opposing player. ShittyHeuristic doesn't stop there, though: when an instance of Game is created, *a new game begins at that point, played out entirely between two players using the two-pronged tile value heuristic described above. The final state of the board is then evaluated, returning a negative number of tiles if the AI player lost and a positive number of tiles if the AI player won. Using this heuristic, we can roughly estimate the number of tiles the AI would receive if he were to play optimally in that given branch of the game tree. This heuristic is very different from the traditional "tile counting" heuristic - and, we believe, far superior- because it actually estimates board end states for a particular game. Because of this approach, the heuristic isn't particularly potent in early moves of the game, but becomes more and more clever as the game goes on because the "projected" board end-states become more and more accurate.


