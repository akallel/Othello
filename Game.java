import java.util.ArrayList;

public class Game {
	static int turn;
	static int[][] board = new int[8][8];
	static int[][] moveValues = { { 20, -3, 11, 8, 8, 11, -3, 20 },
			{ -3, -7, -4, 1, 1, -4, -7, -3 }, { 11, -4, 2, 2, 2, 2, -4, 11 },
			{ 8, 1, 2, -3, -3, 2, 1, 8 }, { 8, 1, 2, -3, -3, 2, 1, 8 },
			{ 11, -4, 2, 2, 2, 2, -4, 11 }, { -3, -7, -4, 1, 1, -4, -7, -3 },
			{ 20, -3, 11, 8, 8, 11, -3, 20 } };

	static boolean noMoves = false;
	static boolean gameOver = false;

	public Game(int[][] board, int turn, boolean noMoves, boolean gameOver) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.turn = turn;
		this.noMoves = noMoves;
		this.gameOver = gameOver;
	}
	
	public static int play(){
		while (!gameOver) {
			if (turn % 2 == 0) {
				ArrayList<Node> nodes = allNextMoves();
				if (nodes.isEmpty()) {
					turn++;
					if (noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					noMoves = false;
					// printMoveValues();
					int[] n = bestMove(nodes);
					board[n[0]][n[1]] = 2;
					doFlip(turn, n[0], n[1]);
					turn++;
				}
			} else {
				// player 2 moves
				ArrayList<Node> nodes = allNextMoves();
				// printTable();
				if (nodes.isEmpty()) {
					turn++;
					if (noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					noMoves = false;
					// printMoveValues();
					int[] n = bestMove(nodes);
					board[n[0]][n[1]] = 2;
					doFlip(turn, n[0], n[1]);
					turn++;
				}
			}
		}

		// count who won if the board is full
		return howMany(2) - howMany(1);
	}

	/*
	 * bestMove - traverses the 2D array, recording the coordinates of the best
	 * valued position
	 */
	private static int[] bestMove(ArrayList<Node> possMoves) {
		int x = possMoves.get(0).X;
		int y = possMoves.get(0).Y;
		int val = moveValues[y][x];

		for (int i = 1; i < possMoves.size(); i++) {
			if (val < moveValues[possMoves.get(i).Y][possMoves.get(i).X]) {
				val = moveValues[possMoves.get(i).Y][possMoves.get(i).X];
				x = possMoves.get(i).X;
				y = possMoves.get(i).Y;
			}
		}
		int[] n = { x, y };
		return n;
	}

	/*
	 * validMove - identifies if a provided (x,y) coordinate is a valid position
	 * to place a tile
	 */
	private static boolean validMove(int x, int y, ArrayList<Node> possMoves) {
		for (int i = 0; i < possMoves.size(); i++) {
			if (possMoves.get(i).X == x && possMoves.get(i).Y == y)
				return true;
		}
		return false;
	}

	/*
	 * howMany - what the fuck is wrong with Anis
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
}
