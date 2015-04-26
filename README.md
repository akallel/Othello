# Othello

CSC 242 final project

By Anis Kallel, John Donner, Nathan Contino

aka Team Daybreak

shitty heuristic - corner > edge > inside > more inside

Our project implements the minimax best-move chooser alongside a heuristic of our own design, shittyHeuristic, to create an AI for the popular tile-flipping game Othello. As Dr. Frankenstein once said, "I've created a monster."



MINIMAX

Our implementation of minimax works primarily through the Game3 class, which is assigned a depth by its caller (in our case, the caller is always an instance of class Othello). This class continuously calls itself on every possible move at decreasing depths until it reaches depth 0, at which point each instance of Game3 (there will be quite a few) calls a new instance of Game using every available move. Game contains our actual move-quality heuristic, titled "shittyHeuristic," which assesses the quality of a particular move. Each instance of Game3 then "harvests" the validity of each branching game state, recording the worst and best moves at a particular time. As the Game3 calls return, their parent Game3 calls check the best of their worst possible moves and the worst of their best possible moves. This continues up the "tree" of Game3 calls until we have a final "chosenValue" (a veritable "minimax") for our given gamestate. Our AI then chooses the corresponding move aligned with "chosenValue" that indicates the most promising possible value of our given move choices.
