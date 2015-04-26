import java.util.ArrayList;

public class Game {
	static int gameNum;
	int turn;
	int[][] board = new int[8][8];
	int[][] moveValues = { { 20, -3, 11, 8, 8, 11, -3, 20 },
				{ -3, -7, -4, 1, 1, -4, -7, -3 }, 
				{ 11, -4, 2, 2, 2, 2, -4, 11 },
				{ 8, 1, 2, -3, -3, 2, 1, 8 }, 
				{ 8, 1, 2, -3, -3, 2, 1, 8 },
				{ 11, -4, 2, 2, 2, 2, -4, 11 }, 
				{ -3, -7, -4, 1, 1, -4, -7, -3 },
				{ 20, -3, 11, 8, 8, 11, -3, 20 } };

	boolean noMoves = false;
	boolean gameOver = false;
	int winValue;
	int HUMAN;
	int COMPUTER;

	public Game(int[][] board, int turn, boolean noMoves, boolean gameOver, int HUMAN, int COMPUTER) {
		//or are we dancer?
		this.HUMAN = HUMAN;
		this.COMPUTER = COMPUTER;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				this.board[i][j] = board[i][j];
			}
		}
		this.turn = turn;
		this.noMoves = noMoves;
		this.gameOver = gameOver;
		this.play();
	}
	
	public void play() {
		gameNum++;
		while (!gameOver) {
			if (turn % 2 == HUMAN % 2) {
				ArrayList<Node> nodes = allNextMoves();
				if (nodes.isEmpty()) {
					turn++;
					if (noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					
					noMoves = false;
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
						n = bestMove(nodes);
					}
					board[n[0]][n[1]] = 1;
					doFlip(turn, n[0], n[1]);
					turn++;
				}
			} else {
				// player 2 moves
				ArrayList<Node> nodes = allNextMoves();
				if (nodes.isEmpty()) {
					turn++;
					if (noMoves == true)
						gameOver = true;
					noMoves = true;
				} else {
					noMoves = false;
					
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
						n = bestMove(nodes);
					}	
					board[n[0]][n[1]] = 2;
					doFlip(turn, n[0], n[1]);
					turn++;
				}
			}
			shittyHeuristic();
		}
		
		// count who won if the board is full
		winValue = howMany(COMPUTER) - howMany(HUMAN);
	}
	
	/*
	 * bestMove - traverses the 2D array, recording the coordinates of the best
	 * valued position
	 */
	private int[] bestMove(ArrayList<Node> possMoves) {
		int x = possMoves.get(0).X;
		int y = possMoves.get(0).Y;
		int frontiers = numOfEmptyAdj(turn%2+1);
		int val = moveValues[y][x]- frontiers;
		// Need to make this "frontiers" value be calculated for each possible move
		// so that number of frontier spaces are compared across different moves

		for (int i = 1; i < possMoves.size(); i++) {
			if (val < (moveValues[possMoves.get(i).Y][possMoves.get(i).X] - frontiers)) {
				val = moveValues[possMoves.get(i).Y][possMoves.get(i).X] - frontiers;
				x = possMoves.get(i).X;
				y = possMoves.get(i).Y;
			}
		}
		int[] n = { x, y };
		return n;
	}

	/*
	 * howMany - how many tiles belong to player a?
	 */
	private int howMany(int a) {
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
	 * numOfEmptyAdj - Returns the number of adjacent tiles of a given player
	 */
	private int numOfEmptyAdj(int a) {
		int[][] tempBoard = copyBoard(board);
		ArrayList<Node> myPieces = whereAreMyPieces(a);
		int answer = 0;
		for (int i = 0; i < myPieces.size(); i++) {
			int tempX = myPieces.get(i).X;
			int tempY = myPieces.get(i).Y;
			if (tempX > 0) {
				if (tempBoard[tempX-1][tempY+0] == 0) {
					tempBoard[tempX-1][tempY+0] = 5;
					answer++;
				} // check west
				if (tempY < 7)
					if (tempBoard[tempX-1][tempY+1] == 0) {
						tempBoard[tempX-1][tempY+1] = 5;
						answer++;
					} // check southwest
				if (tempY > 0)
					if (tempBoard[tempX-1][tempY-1] == 0) {
						tempBoard[tempX-1][tempY-1] = 5;
						answer++;
					} // check northwest
			}
			if (tempX < 7) {
				if (tempY < 7)
					if (tempBoard[tempX+1][tempY+1] == 0) {
						tempBoard[tempX+1][tempY+1] = 5;
						answer++;
					}	// check southeast
				if (tempBoard[tempX+1][tempY+0] == 0) {
					tempBoard[tempX+1][tempY+0] = 5;
					answer++;
				}	// check east
				if (tempY > 0)
					if (tempBoard[tempX+1][tempY-1] == 0) {
						tempBoard[tempX+1][tempY-1] = 5;
						answer++;
					} // check northeast
			}
			if (tempY < 7)
				if (tempBoard[tempX+0][tempY+1] == 0) {
					tempBoard[tempX+0][tempY+1] = 5;
					answer++;
				} // check south
			if (tempY > 0)
				if (tempBoard[tempX+0][tempY-1] == 0) {
					tempBoard[tempX+0][tempY-1] = 5;
					answer++;
				} // check north
		}
		return answer;
	}

	/*Deep copy of the board, just to keep the global board safe from African danger and other unwanted changes.*/
	private int[][] copyBoard(int[][] board)	 {
		int [][] a = new int [board.length][board[0].length];
		for(int i=0;i<board.length;i++)
			for (int j=0;j<board.length;j++)
				a[i][j]= board[i][j];
		return a;
	}


	/*
	 * whereAreMyPieces - returns the coordinates of a player's pieces
	 */
	private ArrayList<Node> whereAreMyPieces(int a) {
		ArrayList<Node> myPieces = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (board[i][j] == a) {
					Node newNode = new Node(i, j);
					myPieces.add(newNode);
				}
		return myPieces;
	}

	/*
	 * doFlip - flips all pieces that were effected by a move
	 */
	private void doFlip(int turn, int newx, int newy) {
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
	private boolean flipCheck(int turn, int newx, int newy, int dirx,
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
	public boolean CanFlip(int X, int Y, int dirX, int dirY) {
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
	public boolean Legal(int X, int Y) {
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
	public ArrayList<Node> allNextMoves() {
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
	public void shittyHeuristic() {
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
}
