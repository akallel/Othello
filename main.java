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
	static int turn = 0, TRUE = 1, FALSE = 0;

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		initializeTable(board);
		printTable();

		while (notFull()) { // not the only stopping condition, other cases
							// where all are 1s or 2s should be included
			int n1 = 0, n2 = 0;

			if (turn % 2 == 0) {
				// player 1 moves
				ArrayList<Node> nodes = allNextMoves();
				
				//prints out the valid moves for the non-computer player. 
				//continues to prompt for a move until a valid move is supplied.
				do {
					System.out.println("Your options for player "
							+ (turn % 2 + 1) + " are: " + nodes.toString());
					System.out.println("choose coordinates a,b");
					String a = scan.next();
					String[] aa = a.split(",");
					n2 = Integer.parseInt(aa[0]);
					n1 = Integer.parseInt(aa[1]);
				} while (!validMove(n1, n2, nodes));
				
				//once we've determined that the player has selected a valid move
				//we change the tile
				board[n1][n2] = 1;
				
				//flip the resulting changed tiles
				doFlip(turn, n1, n2);
				
				//increment the turn
				turn++;
				
				//and print out the board state for the next player.
				printTable();
			} else {
				// player 2 moves
				ArrayList<Node> nodes = allNextMoves();
				// algorithms for computer moves should go here

				// currently, we'll just let player 2 pick from anything; eventually AI will be implemented
				System.out.println("Your options for player " + (turn % 2 + 1)
						+ " are: " + nodes.toString());
				System.out.println("choose coordinates a,b");
				String a = scan.next();
				String[] aa = a.split(",");
				n2 = Integer.parseInt(aa[0]);
				n1 = Integer.parseInt(aa[1]);

				
				board[n1][n2] = 2;
				doFlip(turn, n1, n2);
				turn++;
				printTable();
			}
		}
		if (!notFull()) {
			// count who won if the board is full
			if (howMany(1) > howMany(2))
				System.out.println("Player 1 wins");
			else if (howMany(1) < howMany(2))
				System.out.println("Player 2 wins");
			else
				System.out.println("Draw game");
		}
	}

	private static boolean validMove(int x, int y, ArrayList<Node> possMoves) {
		for (int i = 0; i < possMoves.size(); i++) {
			if (possMoves.get(i).X == x && possMoves.get(i).Y == y)
				return true;
		}
		return false;
	}

	private static int howMany(int a) {
		int answer = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == a)
					answer++;
			}
		}
		return a;
	}

	// doFlip flips all pieces that were effected by a move
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

	// flipCheck recursively checks a straight path in a direction to flip the
	// pieces
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
					diry);
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

	private static void printTable() {
		for (int i = 0; i < board.length; i++) {
			if (i == 0)
				System.out.println("   0 1 2 3 4 5 6 7\n   ---------------");
			System.out.print(i + " |");
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 1)
					System.out.print(board[i][j] + " ");
				else if (board[i][j] == 2)
					System.out.print(board[i][j] + " ");
				else
					System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	private static boolean notFull() {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (board[i][j] == 0)
					return true;
		return false;
	}

	public static void initializeTable(int[][] a) {
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++)
				a[i][j] = 0;

		a[3][3] = 1;
		a[4][4] = 1;
		a[3][4] = 2;
		a[4][3] = 2;

	}

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
		
		//we need to weed out examples of empty tiles immediately adjacent to occupied-by-player tiles.
		if(X + dirX < 8 && X + dirX >= 0 && Y + dirY < 8 && Y + dirY >= 0)
			if((dirX == 1 && dirY == 1 ) || (dirX == -1 && dirY == -1) && 
					(board[X + dirX][Y + dirY] == player || board[X + dirX][Y + dirY] == 0))
				return false;
		
		//checks for conflicts with the edges (top/bottom/left/right)
		while (X + dirX < 8 && X + dirX >= 0 && Y + dirY < 8 && Y + dirY >= 0) {

			//increments/decrements the X and Y coordinates, rather than the "direction" element to maintain direction.
			//if the next tile in our direction is the opposite player, we want to keep checking (increment in direction)
			if(board[X + dirX][Y + dirY] == oppositePlayer){
				X = X + dirX;
				Y = Y + dirY;
			}
			//if we come to an empty tile, we know that we can't move in that direction
			else if(board[X + dirX][Y + dirY] == 0)
				return false;
			//the only time when the move is valid is when we hit a tile of our own denomination
			//after exploring >= 1 tile of the opposing denomination. in this case, we can capture the empty tile
			else if(board[X + dirX][Y + dirY] == player)
				return true;
		}
		return false;
	}

	/* Legal - checks forward/backward/diagonal in all directions for opposing color tiles
	 * using -1, 0, and 1 as possible directions to check. Calls CanFlip.
	 * NOTE: this probably doesn't actually work. 
	 */
	public static boolean Legal(int X, int Y) {
		int i, j, captures;
		captures = 0;
		if (board[X][Y] != 0)
			return false;
		// method to explore up/down/left/right/upright/upleft/downright/downleft directions
		for (i = -1; i <= 1; i++)
			for (j = -1; j <= 1; j++)
				if ((i != 0 && j != 0) && CanFlip(X, Y, i, j))
					return true;
		return false;
	}
	
	/* allNextMoves - traverses the entire array. Calls Legal on each to generate valid move spaces.
	 * 
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
}
