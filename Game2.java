import java.util.ArrayList;

public class Game2 {
	static int turn;
	static int[][] board2 = new int[8][8];
	static int[][] moveValues = 
		{
			{ 50, -3, 11, 8, 8, 11, -3, 50 },
			{ -3, -7, -4, 1, 1, -4, -7, -3 },
			{ 11, -4, 2, 2, 2, 2, -4, 11 },
			{ 8, 1, 2, -3, -3, 2, 1, 8 },
			{ 8, 1, 2, -3, -3, 2, 1, 8 },
			{ 11, -4, 2, 2, 2, 2, -4, 11 },
			{ -3, -7, -4, 1, 1, -4, -7, -3 },
			{ 50, -3, 11, 8, 8, 11, -3, 50 }
		};

	static boolean noMoves = false;
	static boolean gameOver = false;

	public Game2(int[][] board, int turn, boolean noMoves, boolean gameOver) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board2[i][j] = board[i][j];
			}
		}
		this.turn = turn;
		this.noMoves = noMoves;
		this.gameOver = gameOver;
	}
	
	public int play(){
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
					board2[n[0]][n[1]] = 1;
					doFlip(turn, n[0], n[1]) ;
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
					//board2[n[0]][n[1]] = 2;
					//doFlip(turn, n[0], n[1]);*/
					alphaBeta(new Node(n[0],n[1]), turn, Integer.MIN_VALUE, Integer.MAX_VALUE,true);
					turn++;
				}
			}
		}
		shittyHeuristic();
		// count who won if the board is full
		return howMany(2) - howMany(1);
	}

	//alphabeta(origin, depth, -∞, +∞, TRUE)
	
	/*Alpha Beta pruning */
	public static int alphaBeta(Node node, int depth, int alpha, int beta, boolean maximizingPlayer){	
	   // int v=0;  
		if (depth == 0) //or node is a terminal node
	          return board2[node.X][node.Y];//the heuristic value of node
	    if (maximizingPlayer){
	    	int v = Integer.MIN_VALUE;
			for (int i=0; i<allNextMoves().size();i++){
	              v= Math.max(v, alphaBeta(allNextMoves().get(i), depth - 1, alpha, beta,false));
	              alpha = Math.max(alpha, v);
	             if (beta <= alpha)
	                  break ;
			}
	          return v;
	      }
	      else{
	         int v= Integer.MAX_VALUE;
				for (int i=0; i<allNextMoves().size();i++){
	              v = Math.min(v, alphaBeta(allNextMoves().get(i), depth - 1, alpha, beta, true));
	              beta = Math.min(beta, v);
	              if (beta <= alpha){
	                 break;}
				}
	          return v;}
	}
	
	/*
	 * valued position
	 */
	private static int[] bestMove(ArrayList<Node> possMoves) {
		int x = possMoves.get(0).X;
		int y = possMoves.get(0).Y; 
		moveValues[y][x]= moveValues[y][x] + fakeGain(turn,board2, fakeFlip(turn, possMoves.get(0).Y, possMoves.get(0).X));
		int val = moveValues[y][x];
		
		for (int i = 1; i < possMoves.size(); i++) {
			//System.out.println(i+":   Fake gain is for : ("+ possMoves.get(i).Y + ","+ possMoves.get(i).X + ") is "+ fakeGain(turn,board, fakeFlip(turn, possMoves.get(i).Y, possMoves.get(i).X)));
			moveValues[possMoves.get(i).Y][possMoves.get(i).X]= 
					moveValues[possMoves.get(i).Y][possMoves.get(i).X] + fakeGain(turn,board2, fakeFlip(turn, possMoves.get(i).Y, possMoves.get(i).X));
			if (val <= moveValues[possMoves.get(i).Y][possMoves.get(i).X] )
			{
				val = moveValues[possMoves.get(i).Y][possMoves.get(i).X];
				
				x = possMoves.get(i).X;
				y = possMoves.get(i).Y;
			}
		}
		//System.out.println("The best value for turn is "+val);
		int[] n = {x, y};
		//printTable(moveValues);
		//rest after each move to only change the possible next moves.
		shittyHeuristic();
		return n;
	}

	private static int[] shittyMove(ArrayList<Node> possMoves) {
		int x = possMoves.get(0).X;
		int y = possMoves.get(0).Y; 
		moveValues[y][x]= moveValues[y][x] ;
		int val = moveValues[y][x];
		
		for (int i = 1; i < possMoves.size(); i++) {
			//System.out.println(i+":   Fake gain is for : ("+ possMoves.get(i).Y + ","+ possMoves.get(i).X + ") is "+ fakeGain(turn,board, fakeFlip(turn, possMoves.get(i).Y, possMoves.get(i).X)));
			if (val < moveValues[possMoves.get(i).Y][possMoves.get(i).X] )
			{
				val = moveValues[possMoves.get(i).Y][possMoves.get(i).X];
				
				x = possMoves.get(i).X;
				y = possMoves.get(i).Y;
			}
		}
		//System.out.println("The best value for turn is "+val);
		int[] n = {x, y};
		//printTable(moveValues);
		//rest after each move to only change the possible next moves.
		//shittyHeuristic();
		return n;
	}
	/* printTable - not sure why we have specific methods to print two identically-sized
	 * 2D arrays... but oh well. "Memory is cheap" - Ted
	 * 
	 */
	private static void printTable(int [][] board) {
		for (int i = 0; i < board.length; i++) {
			if (i == 0)
				System.out.println("    0 1 2 3 4 5 6 7\n   ----------------");
			System.out.print(i + " | ");
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 1){
					System.out.print(board[i][j] + " " );
				}
				else if (board[i][j] == 2){
					System.out.print(board[i][j] + " ");
				}
				else{
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println();
		}
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
		for (int i = 0; i < board2.length; i++) {
			for (int j = 0; j < board2[0].length; j++) {
				if (board2[i][j] == a)
					answer++;
			}
		}
		return answer;
	}
	
	private static int howManyFake(int [][] fake,int a) {
		int answer = 0;
		for (int i = 0; i < fake.length; i++) {
			for (int j = 0; j < fake[0].length; j++) {
				if (fake[i][j] == a)
					answer++;
			}
		}
		return answer;
	}
	
	/*
	 * Calculates the fake gain in a fake next move situation and returns the max of that and the current table. 
	 * Probably not exactly right, but the difference of howmanyfake and howmany would give mostly 0 and that doesnt help. 
	 * */
	private static int fakeGain(int turn, int [][] board, int [][] fakeBoard){
		if(turn%2 ==0) {
			//System.out.println("Faaake gain !! "+ (howManyFake(fakeBoard,1)-howMany(1)));
			return Math.max(howManyFake(fakeBoard,1),howMany(1));
		}
		else {
			//System.out.println("Faaake gain 2 !! "+ (howManyFake(fakeBoard,2)-howMany(2)));
			return Math.max(howManyFake(fakeBoard,2),howMany(2));
		}
	}
	
	// basically creates a fake board without messing with the actual board
	// we kind of did this before with adding an extra parameter, but I forgot and did it again. 
	private static int [][] fakeFlip(int turn, int newx, int newy){
		int [][] copyboard = copyBoard(board2); 
		fakeFlipCheck(copyboard,turn, newx, newy, -1, 0); // Checks west
		fakeFlipCheck(copyboard,turn, newx, newy, -1, 1); // Checks north-west
		fakeFlipCheck(copyboard,turn, newx, newy, 0, 1); // Checks north
		fakeFlipCheck(copyboard,turn, newx, newy, 1, 1); // Checks north-east
		fakeFlipCheck(copyboard,turn, newx, newy, 1, 0); // Checks east
		fakeFlipCheck(copyboard,turn, newx, newy, 1, -1); // Checks south-east
		fakeFlipCheck(copyboard,turn, newx, newy, 0, -1); // Checks south
		fakeFlipCheck(copyboard,turn, newx, newy, -1, -1); // Checks south
	return copyboard;
	}
	
	/*
	 * Again, flips the fake board and returns a new state of the board that we are considering for best move
	 * */
	private static int[][] fakeFlipCheck(int [][] copyboard,int turn, int newx, int newy, int dirx,
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
				&& copyboard[currentx + dirx][currenty + diry] == oppositePlayer) {
			flipThis = flipCheck(turn, currentx + dirx, currenty + diry, dirx,
					diry);
		} else if (currentx + dirx < 8 && currentx + dirx >= 0
				&& currenty + diry < 8 && currenty + diry >= 0
				&& copyboard[currentx + dirx][currenty + diry] == player)
			//return true;

		if (flipThis) {
			copyboard[currentx + dirx][currenty + diry] = player;
			//return true;
		}

		//return false;
		return copyboard;
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
				&& board2[currentx + dirx][currenty + diry] == oppositePlayer) {
			flipThis = flipCheck(turn, currentx + dirx, currenty + diry, dirx,
					diry);
		} else if (currentx + dirx < 8 && currentx + dirx >= 0
				&& currenty + diry < 8 && currenty + diry >= 0
				&& board2[currentx + dirx][currenty + diry] == player)
			return true;

		if (flipThis) {
			board2[currentx + dirx][currenty + diry] = player;
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
				&& board2[X + dirX][Y + dirY] == oppositePlayer) {

			X = X + dirX;
			Y = Y + dirY;
			capture = true;
		}
		if (capture == false)
			return false;

		if (X + dirX < 8 && X + dirX >= 0 && Y + dirY < 8 && Y + dirY >= 0
				&& board2[X + dirX][Y + dirY] == player)
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
		if (board2[X][Y] != 0)
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
		for (int i = 0; i < board2.length; i++)
			for (int j = 0; j < board2[0].length; j++)
				if (Legal(i, j)) {
					Node newNode = new Node(i, j);
					nextMoves.add(newNode);
				}
		return nextMoves;
	}
	
	public static ArrayList<Node> allNextMoves(int[][]a) {
		ArrayList<Node> nextMoves = new ArrayList<Node>();
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[0].length; j++)
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
		int[][] shit = {{50, -3, 11, 8, 8, 11, -3, 50},
				{-3, -7, -4, 1, 1, -4, -7, -3},
				{11, -4, 2, 2, 2, 2, -4, 11},
				{8, 1, 2, -3, -3, 2, 1, 8},
				{8, 1, 2, -3, -3, 2, 1, 8},
				{11, -4, 2, 2, 2, 2, -4, 11},
				{-3, -7, -4, 1, 1, -4, -7, -3},
				{50, -3, 11, 8, 8, 11, -3, 50}};
		moveValues = shit;
	}
}
