import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/*java checks for nqueens : http://www.codeshare.io/KExMg*/
public class main {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	static int[][] board = new int[8][8];
	static int[][] moveValues = new int[8][8];
	static int turn = 0;
	static boolean noMoves = false;
	static boolean gameOver = false;

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		initializeTable(board);
		printTable();
		
		while (!gameOver) { // not the only stopping condition,
										// other cases
			// where all are 1s or 2s should be included
			int n1 = 0, n2 = 0;

			if (turn % 2 == 0) {
				// player 1 moves
				ArrayList<Node> nodes = allNextMoves();
				if (nodes.isEmpty()) {
					System.out.println("NO MOVES AVAILABLE FOR USER.");
					turn++;
					if(noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					noMoves = false;
					// prints out the valid moves for the non-computer player.
					// continues to prompt for a move until a valid move is
					// supplied.
					String a;
					do {
						a = "";
						System.out.println("Your options for player "
								+ (turn % 2 + 1) + " are: " + nodes.toString());
						System.out.println("choose coordinates a,b");
						a = scan.next();
						if (a.matches("^[0-9]+(,[0-9]+)")) {
							String[] aa = a.split(",");
							n2 = Integer.parseInt(aa[0]);
							n1 = Integer.parseInt(aa[1]);
						}
					} while (!validMove(n1, n2, nodes));

					// once we've determined that the player has selected a
					// valid move
					// we change the tile
					board[n1][n2] = 1;

					// flip the resulting changed tiles
					doFlip(turn, n1, n2);

					// increment the turn
					turn++;

					// and print out the board state for the next player.
					printTable();
				}
			} else {
				// player 2 moves
				ArrayList<Node> nodes = allNextMoves();
				// printTable();
				if (nodes.isEmpty()) {
					System.out.println("NO MOVES AVAILABLE FOR COMPUTER.");
					turn++;
					if(noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					noMoves = false;
					System.out.println("Your options for player "
								+ (turn % 2 + 1) + " are: " + nodes.toString());
					boolean corner = false;
					Node cornerNode = null;
					int[] n = new int[2];

					for (int i = 0; i < nodes.size(); i++) {
						if ((nodes.get(i).Y == 7) || (nodes.get(i).Y == 0))
							if ((nodes.get(i).X == 7) || (nodes.get(i).X == 0)) {
								corner = true;
								cornerNode = nodes.get(i);
							}
					}
					if (corner) {
						n[0] = cornerNode.X;
						n[1] = cornerNode.Y;
					} else {
						Game[] leaves = new Game[nodes.size()];
						for (int i = 0; i < leaves.length; i++) {
							leaves[i] = new Game(move(nodes.get(i)), turn + 1,
									noMoves, gameOver);
							// System.out.println(nodes.get(i));
						}

						int[] leafResults = new int[leaves.length];
						for (int i = 0; i < leaves.length; i++) {
							leafResults[i] = leaves[i].winValue;
							// System.out.println("Value: " + leafResults[i] +
							// " " + leaves[i].winValue);
						}

						int bestMove = bestMove(leafResults);
						n[0] = nodes.get(bestMove).X;
						n[1] = nodes.get(bestMove).Y;
					}

					System.out.println("Computer moves to: (" + n[1] + ","
							+ n[0] + ")");

					board[n[0]][n[1]] = 2;
					doFlip(turn, n[0], n[1]);
					turn++;
					printTable();
				}
			}
		}

		// count who won if the board is full
		if (howMany(1) > howMany(2))
			System.out.println("Player 1 wins");
		else if (howMany(1) < howMany(2))
			System.out.println("Player 2 wins");
		else
			System.out.println("Draw game");
		
		System.out.println();
		System.out.println("player 1 has "+ howMany(1) + " tiles");
		System.out.println("player 2 has "+ howMany(2) + " tiles");

	}

	/* move - creates a new board with a potential computer move
	 * 
	 */
	public static int[][] move(Node n){
		int[][] newBoard = new int[8][8];
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				newBoard[i][j] = board[i][j];
			}
		}
		
		newBoard[n.Y][n.X] = 1;

		// flip the resulting changed tiles
		doFlip(turn, n.Y, n.X, newBoard);
		return newBoard;
	}

	/* bestMove - traverses the 2D array, recording the coordinates of the best valued position
	 * 
	 */
	private static int bestMove(int[] leafResults) {
		int index = 0;
		for(int i = 1; i < leafResults.length; i++){
			if(leafResults[i] > leafResults[index]){
				index = i;
			}
		}
		return index;
	}
	
	/* validMove - identifies if a provided (x,y) coordinate is a valid position to place a tile
	 * 
	 */
	private static boolean validMove(int x, int y, ArrayList<Node> possMoves) {
		for (int i = 0; i < possMoves.size(); i++) {
			if (possMoves.get(i).X == x && possMoves.get(i).Y == y)
				return true;
		}
		return false;
	}
	
	/* howMany - what the fuck is wrong with Anis
	 * 
	 */
	private static int howMany(int a) {
		int answer = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == a)
					answer++;
			}
		}
		return answer;
	}

	/*
	 * numOfEmptyAdj - Returns the number of adjancent tiles of a given player
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

	/*Deep copy of the board, just to keep the global board safe from African danger and other unwanted changes.*/
	private static int[][] copyBoard(int[][] board)	 {
		int [][] a = new int [board.length][board[0].length];
		for(int i=0;i<board.length;i++)
			for (int j=0;j<board.length;j++)
				a[i][j]= board[i][j];
		return a;
	}


	/*
	 * whereAreMyPieces - returns the coordinates of a player's pieces
	 */
	private static ArrayList<Node> whereAreMyPieces(int a) {
		ArrayList<Node> myPieces = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (board[i][j] == a) {
					Node newNode = new Node(i, j);
					myPieces.add(newNode);
				}
		System.out.println("my pieces are at: "+myPieces);
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
	 */
	private static boolean flipCheck(int turn, int newx, int newy, int dirx,
			int diry, int[][] board) {
		int player, oppositePlayer;
		int currentx = newx;
		int currenty = newy;
		boolean flipThis = false;

		// define who attacking and defending players are based on turn
		if (turn % 2 == 0) {
			player = 1;
			oppositePlayer = 2;
		} else {
			player = 2;
			oppositePlayer = 1;
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
	
	/* doFlip - flips all pieces that were effected by a move
	 * 
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

	/* flipCheck - actually flips the tiles in a specific direction
	 */
	private static boolean flipCheck(int turn, int newx, int newy, int dirx,
			int diry) {
		int player, oppositePlayer;
		int currentx = newx;
		int currenty = newy;
		boolean flipThis = false;

		// define who attacking and defending players are based on turn
		if (turn % 2 == 0) {
			player = 1;
			oppositePlayer = 2;
		} else {
			player = 2;
			oppositePlayer = 1;
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

	/* printTable - not sure why we have specific methods to print two identically-sized
	 * 2D arrays... but oh well. "Memory is cheap" - Ted
	 * 
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

	/* printTable - Weeeee printing a table as input for table Copies
	 * 2D arrays... but oh well. "Memory is cheap" - Ted
	 * 
	 */
	private static void printTable(int[][] tempBoard) {
		for (int i = 0; i < tempBoard.length; i++) {
			if (i == 0)
				System.out.println("    0 1 2 3 4 5 6 7\n   ----------------");
			System.out.print(i + " | ");
			for (int j = 0; j < tempBoard.length; j++) {
				if (tempBoard[i][j] == 1){
					System.out.print(ANSI_RED + tempBoard[i][j] + " " + ANSI_RESET);
				}
				else if (tempBoard[i][j] == 2){
					System.out.print(ANSI_BLACK + tempBoard[i][j] + " " + ANSI_RESET);
				}
				else{
					System.out.print(tempBoard[i][j] + " ");
				}
			}
			System.out.println();
		}
	}

	/* printTable - not sure why we have specific methods to print two identically-sized
	 * 2D arrays... but oh well. "Memory is cheap" - Ted
	 * 
	 */
	private static void printTable(int[] leafResults) {
		for (int i = 0; i < leafResults.length; i++) {
			System.out.print(leafResults[i]+ "  ");
		}
		System.out.println();
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

	/*
	 * CanFlip - moves out in a given direction from an empty tile to identify
	 * if that tile represents a valid move for the team moving on the current
	 * turn. dirX, dirY should be {-1, 0, 1}, but should never BOTH be zero.
	 */
	public static boolean CanFlip(int X, int Y, int dirX, int dirY) {
		int player, oppositePlayer;
		// define who attacking and defending players are based on turn
		if (turn % 2 == 0) {
			player = 1;
			oppositePlayer = 2;
		} else {
			player = 2;
			oppositePlayer = 1;
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
		int i, j, captures;
		captures = 0;
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

	/* shittyHeuristic - a rough, initial heuristic that simply
	 * prioritizes corners > edges > inner rings
	 * 
	 */
	public static void shittyHeuristic() {
		int[][] shit = {{20, -3, 11, 8, 8, 11, -3, 20},
				{-3, -7, -4, 1, 1, -4, -7, -3},
				{11, -4, 2, 2, 2, 2, -4, 11},
				{8, 1, 2, -3, -3, 2, 1, 8},
				{8, 1, 2, -3, -3, 2, 1, 8},
				{11, -4, 2, 2, 2, 2, -4, 11},
				{-3, -7, -4, 1, 1, -4, -7, -3},
				{20, -3, 11, 8, 8, 11, -3, 20}};
		moveValues = shit;
	}

	/* refreshMoveValues - refreshes every position in the parallel moveValues array to zero
	 * 
	 */
	public static void refreshMoveValues() {
		for (int i = 0; i < moveValues.length; i++) {
			for (int j = 0; j < moveValues[i].length; j++) {
				moveValues[i][j] = 0;
			}
		}
	}

	/* printMoveValues - prints out the 2D array of move values
	 * 
	 */
	private static void printMoveValues() {
		for (int i = 0; i < moveValues.length; i++) {
			if (i == 0)
				System.out.println("   0 1 2 3 4 5 6 7\n   ---------------");
			System.out.print(i + " | ");
			for (int j = 0; j < moveValues.length; j++) {
				if (moveValues[i][j] == 1)
					System.out.print(moveValues[i][j] + " ");
				else if (moveValues[i][j] == 2)
					System.out.print(moveValues[i][j] + " ");
				else
					System.out.print(moveValues[i][j] + " ");
			}
			System.out.println();
		}
	}

}
