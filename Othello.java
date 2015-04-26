import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/*java checks for nqueens : http://www.codeshare.io/KExMg*/
public class Othello {
	// color values (for prettiness!)
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	
	//our board, recording placements of tiles for both players
	static int[][] board = new int[8][8];
	
	//our value assessor, a legacy from an earlier time
	static int[][] moveValues = new int[8][8];
	
	//turn counter (odd = player 1, even = player 2)
	static int turn = 0;
	
	//our end-game checker; if we can't move, we set noMoves to false. If noMoves if already false, we're in a deadlock
	//between both players and the game is over since *nobody* can move.
	static boolean noMoves = false;
	static boolean gameOver = false;
	
	//the depth of our minimax/alpha-beta algorithm. The actual depth is DEPTH + 1, because of our treatment of leaf nodes.
	static int DEPTH;
	
	//human and computer values
	static int COMPUTER;
	static int HUMAN;
	
	//black is represented by a 1 (for player 1), white is represented by a 2 (for player 2)
	static int BLACK = 1;
	static int WHITE = 2;

	static int HUMANMOVE;
	static int COMPUTERMOVE;
	
	/* main - runs the game.
	 * 
	 */
	public Othello(int compColor, int time1, int time2, int depth) {
		//which player will the computer be?
		if(compColor == 1){
			COMPUTER = BLACK;
			HUMAN = WHITE;
		}else{
			COMPUTER = WHITE;
			HUMAN = BLACK;
		}
		//right now, our algorithm always works at a fixed depth
		DEPTH = 2;
		//but we still need a check to make sure our depth is less than the depth limit
		if(DEPTH > depth)
			DEPTH = depth;
		
		//create a scanner, initialize the board in the starting state, and print it out
		Scanner scan = new Scanner(System.in);
		initializeTable(board);
		printTable();
		
		//for checking whose move it is
		HUMANMOVE = HUMAN % 2;
		COMPUTERMOVE = COMPUTER % 2;

		//if gameOver is true, both players can't move and the game is over
		while (!gameOver) { 
			
			// values to store player input x and y values
			int n1 = 0, n2 = 0;

			//check  whose turn it is
			if (turn % 2 == HUMANMOVE) {
				// player 1 moves
				ArrayList<Node> nodes = allNextMoves();
				
				//check if there's an available move for player 1
				if (nodes.isEmpty()) {
					//alert the player
					System.out.println("NO MOVES AVAILABLE FOR USER.");
					//increment the turn
					turn++;
					//if noMoves is already true, the game is over because neither player can move
					if (noMoves == true)
						gameOver = true;
					//otherwise, indicate that the current player can't move
					noMoves = true;
				} else {
					//if the player can move, record that a movement is occurring
					noMoves = false;
					// string to store player input
					String a;
					do {
						//clear the string (in case we're restarting from a failed player move entry)
						a = "";
						//print out the player's options
						System.out.println("Your options for player "
								+ HUMAN + " are: " + nodes.toString());
						System.out.println("choose coordinates a,b");
						
						//read in player input
						a = scan.next();
						
						// check if the input string is the correct format
						if (a.matches("^[0-9]+(,[0-9]+)")) {
							String[] aa = a.split(",");
							//parse the input string into two integers for movement
							n1 = Integer.parseInt(aa[0]);
							n2 = Integer.parseInt(aa[1]);
						}
					} while (!validMove(n1, n2, nodes));

					// once we've determined that the player has selected a
					// valid move
					// we change the tile
					board[n1][n2] = HUMAN;

					// flip the resulting changed tiles
					doFlip(turn, n1, n2);

					// increment the turn
					turn++;

					// and print out the board state for the next player.
					printTable();
				}
			} else {
				// all our potential player 2 moves are stored within nodes
				ArrayList<Node> nodes = allNextMoves();
				
				// if player 2 has no moves
				if (nodes.isEmpty()) {
					// alert the player that player 2 can't move
					System.out.println("NO MOVES AVAILABLE FOR COMPUTER.");
					
					//increment the turn
					turn++;
					
					//check if we're in a deadlock; if so, end the game by setting gameOver to true
					if (noMoves == true)
						gameOver = true;
					
					//set noMoves to true to indicate that no movement was made
					noMoves = true;
				} else {
					//otherwise,
					//indicate that a movement was made
					noMoves = false;

					//set corner to false (we haven't identified if we can move to a corner yet
					boolean corner = false;
					
					//temporary storage for a potential corner node
					Node cornerNode = null;
					
					//our storage array for computer moves
					int[] n = new int[2];

					//check if there's a corner available: if so, we want to take it.
					for (int i = 0; i < nodes.size(); i++) {
						if ((nodes.get(i).Y == 7) || (nodes.get(i).Y == 0))
							if ((nodes.get(i).X == 7) || (nodes.get(i).X == 0)) {
								//if we find a corner, mark corner as true and save the node in temporary node storage
								corner = true;
								cornerNode = nodes.get(i);
							}
					}
					
					//take the corner if possible
					if (corner) {
						//the n array stores our chosen move's x and y coordinates
						n[0] = cornerNode.X;
						n[1] = cornerNode.Y;
						
					//otherwise, choose the best possible move according to our minimax
					} else {
						
						//create an array of Game3s to store our potential movements
						Game3[] leaves = new Game3[nodes.size()];
						
						//traverse the node list, creating a new Game3 for each
						for (int i = 0; i < leaves.length; i++) {
							//Game3 will branch to the designated depth (+ 1) and evaluate the worst value in each branch
							leaves[i] = new Game3(move(nodes.get(i)), turn + 1,
									noMoves, gameOver, DEPTH, HUMAN, COMPUTER);
						}

						// store the leaf results in the leafResult integer array
						int[] leafResults = new int[leaves.length];
						
						// miniMax stores our chosen leaf index within the leaf array
						int miniMax = 0;
						
						// find the best value of our possible choices (mind you, these record their worst value - hence the minimax)
						for (int i = 0; i < leaves.length; i++) {
							leafResults[i] = leaves[i].chosenValue;
							if(leafResults[i] > leafResults[miniMax])
								miniMax = i;
						}

						//record the chosen values within the n array
						n[0] = nodes.get(miniMax).X;
						n[1] = nodes.get(miniMax).Y;
					}

					//print out the chosen computer move
					System.out.println("Computer moves to: (" + n[1] + ","
							+ n[0] + ")");
					
					//change that spot to the computer tile value
					board[n[0]][n[1]] = COMPUTER;
					//execute a flip of adjacent tiles as per Othello rules
					doFlip(turn, n[0], n[1]);
					//increment the turn
					turn++;
					//print the resulting table
					printTable();
				}
			}
		}
		
		// count who won if the board is full
		if (howMany(1) > howMany(2)){
			System.out.println("Player 1 wins");
			System.out.println("Player 1" + howMany(1) );
			System.out.println("Player 2" + howMany(2) );
			}
		else if (howMany(1) < howMany(2)){
			System.out.println("Player 2 wins");
			System.out.println("Player 1" + howMany(1) );
			System.out.println("Player 2" + howMany(2) );
			}
		else
			System.out.println("Draw game");

		System.out.println();
		System.out.println("player 1 has "+ howMany(1) + " tiles");
		System.out.println("player 2 has "+ howMany(2) + " tiles");
	}

	/*
	 * move - creates a new board with a potential computer move
	 */
	public static int[][] move(Node n) {
		// 2D array for storage of our deep copy
		int[][] newBoard = new int[8][8];
		
		// traverse the 2D board, recording all values in newBoard
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}

		// flip our tile of choice, designated by the parameter node
		newBoard[n.Y][n.X] = COMPUTER;

		// flip the resulting changed tiles
		doFlip(turn, n.Y, n.X, newBoard);
		
		// return the deep copy, one move further into the game
		return newBoard;
	}

	/*
	 * validMove - identifies if a provided (x,y) coordinate lies within the provided arraylist
	 */
	private static boolean validMove(int x, int y, ArrayList<Node> possMoves) {
		
		//traverse the arraylist
		for (int i = 0; i < possMoves.size(); i++) {
			//if we find a corresponding identical move, return true
			if (possMoves.get(i).X == x && possMoves.get(i).Y == y)
				return true;
		}
		//otherwise return false
		return false;
	}

	/* howMany - counts the number of tiles of a certain denomination (free, player 1, player 2) on the board
	 * 
	 */
	private static int howMany(int a) {
		//increment for our count
		int answer = 0;
		//traverse the 2D board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				//increment answer only if we identify a tile of the same denomination as our parameter
				if (board[i][j] == a)
					answer++;
			}
		}
		return answer;
	}

	/* numOfEmptyAdj - Returns the number of adjancent tiles of a given player
	 * 
	 */
	private static int numOfEmptyAdj(int a) {
		int[][] tempBoard = copyBoard(board);
		ArrayList<Node> myPieces = whereAreMyPieces(a);
		int answer = 0;
		for (int i = 0; i < myPieces.size(); i++) {
			int tempX = myPieces.get(i).X;
			int tempY = myPieces.get(i).Y;
			if (tempX > 0) {
				if (tempBoard[tempX-1][tempY+0] == 0) {
					// System.out.printf("checked west: [%d, %d]\n", tempX-1, tempY+0);
					tempBoard[tempX-1][tempY+0] = 5;
					answer++;
				} // check west
				if (tempY < 7)
					if (tempBoard[tempX-1][tempY+1] == 0) {
						// System.out.println("checked southwest["+(tempX-1)+","+(tempY+1)+"]");
						tempBoard[tempX-1][tempY+1] = 5;
						answer++;
					} // check southwest
				if (tempY > 0)
					if (tempBoard[tempX-1][tempY-1] == 0) {
						// System.out.printf("checked northwest: [%d, %d]\n", tempX-1, tempY-1);
						tempBoard[tempX-1][tempY-1] = 5;
						answer++;
					} // check northwest
			}
			if (tempX < 7) {
				if (tempY < 7)
					if (tempBoard[tempX+1][tempY+1] == 0) {
						// System.out.printf("checked southeast: [%d, %d]\n", tempX+1, tempY+1);
						tempBoard[tempX+1][tempY+1] = 5;
						answer++;
					}	// check southeast
				if (tempBoard[tempX+1][tempY+0] == 0) {
					// System.out.printf("checked east: [%d, %d]\n", tempX+1, tempY);
					tempBoard[tempX+1][tempY+0] = 5;
					answer++;
				}	// check east
				if (tempY > 0)
					if (tempBoard[tempX+1][tempY-1] == 0) {
						// System.out.printf("checked northeast: [%d, %d]\n", tempX+1, tempY-1);
						tempBoard[tempX+1][tempY-1] = 5;
						answer++;
					} // check northeast
			}
			if (tempY < 7)
				if (tempBoard[tempX+0][tempY+1] == 0) {
					// System.out.printf("checked south: [%d, %d]\n", tempX, tempY+1);
					tempBoard[tempX+0][tempY+1] = 5;
					answer++;
				} // check south
			if (tempY > 0)
				if (tempBoard[tempX+0][tempY-1] == 0) {
					// System.out.printf("checked north: [%d, %d]\n", tempX, tempY-1);
					tempBoard[tempX+0][tempY-1] = 5;
					answer++;
				} // check north
			// printTable(tempBoard);
		}
		// System.out.println("Player " +(a)+ " has " +answer+ " number of empty adjancent tiles.");
		return answer;
	}

	/* copyBoard - Deep copy of the board, just to keep the global board safe from African danger and other unwanted changes.
	 * 
	 */
	private static int[][] copyBoard(int[][] board)	 {
		int [][] a = new int [board.length][board[0].length];
		for(int i=0;i<board.length;i++)
			for (int j=0;j<board.length;j++)
				a[i][j]= board[i][j];
		return a;
	}

	/* whereAreMyPieces - returns the coordinates of a player's pieces
	 * 
	 */
	private static ArrayList<Node> whereAreMyPieces(int a) {
		ArrayList<Node> myPieces = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (board[i][j] == a) {
					Node newNode = new Node(i, j);
					myPieces.add(newNode);
				}
		//System.out.println("my pieces are at: "+myPieces);
		return myPieces;
	}

	/* doFlip - flips all pieces that were effected by a move
	 * 
	 */
	private static void doFlip(int turn, int newx, int newy, int[][] board) {
		flipCheck(turn, newx, newy, -1, 0, board); // Checks west
		flipCheck(turn, newx, newy, -1, 1, board); // Checks north-west
		flipCheck(turn, newx, newy, 0, 1, board); // Checks north
		flipCheck(turn, newx, newy, 1, 1, board); // Checks north-east
		flipCheck(turn, newx, newy, 1, 0, board); // Checks east
		flipCheck(turn, newx, newy, 1, -1, board); // Checks south-east
		flipCheck(turn, newx, newy, 0, -1, board); // Checks south
		flipCheck(turn, newx, newy, -1, -1, board); // Checks south
	}

	/* flipCheck - actually flips the tiles in a specific direction
	 * 
	 */
	private static boolean flipCheck(int turn, int newx, int newy, int dirx,
			int diry, int[][] board) {
		int player, oppositePlayer;
		int currentx = newx;
		int currenty = newy;
		boolean flipThis = false;

		// define who attacking and defending players are based on turn
		if (turn % 2 == HUMANMOVE) {
			player = HUMAN;
			oppositePlayer = COMPUTER;
		} else {
			player = COMPUTER;
			oppositePlayer = HUMAN;
		}

		if (currentx + dirx < 8 && currentx + dirx >= 0 && currenty + diry < 8
				&& currenty + diry >= 0
				&& board[currentx + dirx][currenty + diry] == oppositePlayer) {
			flipThis = flipCheck(turn, currentx + dirx, currenty + diry, dirx,
					diry, board);
		} else if (currentx + dirx < 8 && currentx + dirx >= 0
				&& currenty + diry < 8 && currenty + diry >= 0
				&& board[currentx + dirx][currenty + diry] == player)
			return true;

		if (flipThis) {
			board[currentx + dirx][currenty + diry] = player;
			return true;
		}

		return false;
	}

	/*
	 * doFlip - flips all pieces that were effected by a move
	 */
	private static void doFlip(int turn, int newx, int newy) {
		flipCheck(turn, newx, newy, -1, 0); // Checks west
		flipCheck(turn, newx, newy, -1, 1); // Checks north-west
		flipCheck(turn, newx, newy, 0, 1); // Checks north
		flipCheck(turn, newx, newy, 1, 1); // Checks north-east
		flipCheck(turn, newx, newy, 1, 0); // Checks east
		flipCheck(turn, newx, newy, 1, -1); // Checks south-east
		flipCheck(turn, newx, newy, 0, -1); // Checks south
		flipCheck(turn, newx, newy, -1, -1); // Checks south
	}

	/*
	 * flipCheck - actually flips the tiles in a specific direction
	 */
	private static boolean flipCheck(int turn, int newx, int newy, int dirx,
			int diry) {
		int player, oppositePlayer;
		int currentx = newx;
		int currenty = newy;
		boolean flipThis = false;

		// define who attacking and defending players are based on turn
		if (turn % 2 == HUMANMOVE) {
			player = HUMAN;
			oppositePlayer = COMPUTER;
		} else {
			player = COMPUTER;
			oppositePlayer = HUMAN;
		}

		if (currentx + dirx < 8 && currentx + dirx >= 0 && currenty + diry < 8
				&& currenty + diry >= 0
				&& board[currentx + dirx][currenty + diry] == oppositePlayer) {
			flipThis = flipCheck(turn, currentx + dirx, currenty + diry, dirx,
					diry, board);
		} else if (currentx + dirx < 8 && currentx + dirx >= 0
				&& currenty + diry < 8 && currenty + diry >= 0
				&& board[currentx + dirx][currenty + diry] == player)
			return true;

		if (flipThis) {
			board[currentx + dirx][currenty + diry] = player;
			return true;
		}

		return false;
	}

	/*
	 * printTable - not sure why we have specific methods to print two
	 * identically-sized 2D arrays... but oh well. "Memory is cheap" - Ted
	 */
	private static void printTable() {
		for (int i = 0; i < board.length; i++) {
			if (i == 0)
				System.out.println("    0 1 2 3 4 5 6 7\n   ----------------");
			System.out.print(i + " | ");
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 1){
					System.out.print(ANSI_RED + board[i][j] + " " + ANSI_RESET);
				}
				else if (board[i][j] == 2){
					System.out.print(ANSI_BLACK + board[i][j] + " " + ANSI_RESET);
				}
				else{
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println();
		}
		numOfEmptyAdj(turn%2+1);
	}
	
	/* initializeTable - creates the initial playing board
	 * 
	 */
	public static void initializeTable(int[][] a) {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++)
				a[i][j] = 0;

		a[3][3] = 1;
		a[4][4] = 1;
		a[3][4] = 2;
		a[4][3] = 2;
	}

	/* CanFlip - moves out in a given direction from an empty tile to identify
	 * if that tile represents a valid move for the team moving on the current
	 * turn. dirX, dirY should be {-1, 0, 1}, but should never BOTH be zero.
	 * 
	 */
	public static boolean CanFlip(int X, int Y, int dirX, int dirY) {
		int player, oppositePlayer;
		// define who attacking and defending players are based on turn
		if (turn % 2 == HUMANMOVE) {
			player = HUMAN;
			oppositePlayer = COMPUTER;
		} else {
			player = COMPUTER;
			oppositePlayer = HUMAN;
		}

		boolean capture = false;
		while (X + dirX < 8 && X + dirX >= 0 && Y + dirY < 8 && Y + dirY >= 0
				&& board[X + dirX][Y + dirY] == oppositePlayer) {

			X = X + dirX;
			Y = Y + dirY;
			capture = true;
		}
		if (capture == false)
			return false;

		if (X + dirX < 8 && X + dirX >= 0 && Y + dirY < 8 && Y + dirY >= 0
				&& board[X + dirX][Y + dirY] == player)
			return true;

		else
			return false;
	}

	/*
	 * Legal - checks forward/backward/diagonal in all directions for opposing
	 * color tiles using -1, 0, and 1 as possible directions to check. Calls
	 * CanFlip. NOTE: this probably doesn't actually work.
	 */
	public static boolean Legal(int X, int Y) {
		int i, j;
		if (board[X][Y] != 0)
			return false;
		// method to explore
		// up/down/left/right/upright/upleft/downright/downleft directions
		for (i = -1; i <= 1; i++)
			for (j = -1; j <= 1; j++)
				if ((i != 0 || j != 0) && CanFlip(X, Y, i, j))
					return true;
		return false;
	}

	/*
	 * allNextMoves - traverses the entire array. Calls Legal on each to
	 * generate valid move spaces.
	 */
	public static ArrayList<Node> allNextMoves() {
		ArrayList<Node> nextMoves = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (Legal(i, j)) {
					Node newNode = new Node(i, j);
					nextMoves.add(newNode);
				}
		return nextMoves;
	}

	/*
	 * shittyHeuristic - a rough, initial heuristic that simply prioritizes
	 * corners > edges > inner rings
	 */
	public static void shittyHeuristic() {
		int[][] shit = 
			{
				{ 20, -3, 11, 8, 8, 11, -3, 20 },
				{ -3, -7, -4, 1, 1, -4, -7, -3 },
				{ 11, -4, 2, 2, 2, 2, -4, 11 }, 
				{ 8, 1, 2, -3, -3, 2, 1, 8 },
				{ 8, 1, 2, -3, -3, 2, 1, 8 }, 
				{ 11, -4, 2, 2, 2, 2, -4, 11 },
				{ -3, -7, -4, 1, 1, -4, -7, -3 },
				{ 20, -3, 11, 8, 8, 11, -3, 20 } 
			};
		moveValues = shit;
	}

	/*
	 * refreshMoveValues - refreshes every position in the parallel moveValues
	 * array to zero
	 */
	public static void refreshMoveValues() {
		for (int i = 0; i < moveValues.length; i++) {
			for (int j = 0; j < moveValues[i].length; j++) {
				moveValues[i][j] = 0;
			}
		}
	}
}
