import java.util.ArrayList;


public class Game3{
	int turn;
	int worstValue, bestValue, chosenValue;
	int COMPUTER;
	double winPercent;
	public Game3(int[][] board, int turn, boolean noMoves, boolean gameOver, int depth, int HUMAN, int COMPUTER){
		this.turn = turn;
		this.COMPUTER = COMPUTER;
		worstValue = Integer.MIN_VALUE;
		bestValue = Integer.MAX_VALUE;
		double lossCount = 0;
		//if the depth is > 0, we want to call Game3 on all of our children
		if(depth > 0){
			//generate move boards
			ArrayList<Node> branches = allNextMoves(board);
			Game3[] branchPlays = new Game3[branches.size()];
			for(int i = 0; i < branches.size(); i++){
				branchPlays[i] = new Game3(move(branches.get(i), board), turn, noMoves, gameOver, depth - 1, HUMAN, COMPUTER);
				if(branchPlays[i].bestValue < bestValue){
					bestValue = branchPlays[i].bestValue;
				}
				if(branchPlays[i].worstValue > worstValue){
					worstValue = branchPlays[i].worstValue;
				}
				winPercent += branchPlays[i].winPercent;
			}
			winPercent /= branches.size();
			if(turn%2==COMPUTER % 2)
				chosenValue = bestValue;
			else 
				chosenValue = worstValue;
		} //if the depth is 0, we want to call Game on all of our children
		else{
			ArrayList<Node> branches = allNextMoves(board);
			Game[] branchPlays = new Game[branches.size()];
			int winCount = 0;
			for(int i = 0; i < branches.size(); i++){
				branchPlays[i] = new Game(move(branches.get(i), board), turn, noMoves, gameOver, HUMAN, COMPUTER);
				if(branchPlays[i].winValue < worstValue)
					worstValue = branchPlays[i].winValue;
				if(branchPlays[i].winValue > bestValue)
					bestValue = branchPlays[i].winValue;
				if(branchPlays[i].winValue > 0)
					winCount++;
			}
			winPercent = (double)winCount / branches.size();
			}
			if(turn%2==COMPUTER % 2)
				chosenValue = bestValue;
			else 
				chosenValue = worstValue;
		
		
	}
	
	/*
	 * move - creates a new board with a potential computer move
	 */
	public int[][] move(Node n, int[][] board) {
		int[][] newBoard = new int[8][8];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}

		newBoard[n.Y][n.X] = COMPUTER;

		// flip the resulting changed tiles
		doFlip(turn, n.Y, n.X, newBoard);
		return newBoard;
	}
	
	/* doFlip - flips all pieces that were effected by a move
	 * 
	 */
	private void doFlip(int turn, int newx, int newy, int[][] board) {
		flipCheck(turn, newx, newy, -1, 0, board); // Checks west
		flipCheck(turn, newx, newy, -1, 1, board); // Checks north-west
		flipCheck(turn, newx, newy, 0, 1, board); // Checks north
		flipCheck(turn, newx, newy, 1, 1, board); // Checks north-east
		flipCheck(turn, newx, newy, 1, 0, board); // Checks east
		flipCheck(turn, newx, newy, 1, -1, board); // Checks south-east
		flipCheck(turn, newx, newy, 0, -1, board); // Checks south
		flipCheck(turn, newx, newy, -1, -1, board); // Checks south
	}

	/*
	 * flipCheck - actually flips the tiles in a specific direction
	 */
	private boolean flipCheck(int turn, int newx, int newy, int dirx,
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
	
	/*
	 * CanFlip - moves out in a given direction from an empty tile to identify
	 * if that tile represents a valid move for the team moving on the current
	 * turn. dirX, dirY should be {-1, 0, 1}, but should never BOTH be zero.
	 */
	public boolean CanFlip(int X, int Y, int dirX, int dirY, int[][] board) {
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
	public boolean Legal(int X, int Y, int[][] board) {
		int i, j;
		if (board[X][Y] != 0)
			return false;
		// method to explore
		// up/down/left/right/upright/upleft/downright/downleft directions
		for (i = -1; i <= 1; i++)
			for (j = -1; j <= 1; j++)
				if ((i != 0 || j != 0) && CanFlip(X, Y, i, j, board))
					return true;
		return false;
	}

	/*
	 * allNextMoves - traverses the entire array. Calls Legal on each to
	 * generate valid move spaces.
	 */
	public ArrayList<Node> allNextMoves(int[][] board) {
		ArrayList<Node> nextMoves = new ArrayList<Node>();
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				if (Legal(i, j, board)) {
					Node newNode = new Node(i, j);
					nextMoves.add(newNode);
				}
		return nextMoves;
	}
}