import java.util.LinkedList;
import java.util.Scanner;
/*java checks for nqueens : http://www.codeshare.io/KExMg*/
public class main {
  	public static final String ANSI_RESET = "\u001B[0m";
  	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	static int[][] board = new int [8][8];
	static int turn= 0, TRUE = 1, FALSE = 0;
	public static void main(String [] args){
	Scanner scan = new Scanner(System.in); 
		initializeTable(board);
		printTable();
	
	while(notFull()){ // not the only stopping condition, other cases where all are 1s or 2s should be included
		LinkedList<Node> nodes = allNextMoves();
		System.out.println("Your options for player "+ (turn%2+1) + " are: " + nodes.toString());
		System.out.println("choose coordinates a,b");
		String a = scan.next();
		String[] aa= a.split(",");
		int n2 =Integer.parseInt(aa[0]);
		int n1= Integer.parseInt(aa[1]);

		if(turn%2 == 0){
			// player 1 moves
			board[n1][n2]= 1;
			doFlip(turn, n1, n2);
			turn++;
			printTable();
		}
		else {
			// player 2 moves 
			// algorithms for computer moves should go here
			board[n1][n2]= 2;	
			doFlip(turn, n1, n2);
			turn++;
			printTable();
			}
		}
	 if(!notFull()){
	      	// count who won if the board is full
	        if (howMany(1)>howMany(2)) System.out.println("Player 1 wins");
	        else if(howMany(1)<howMany(2)) System.out.println("Player 2 wins");
	        else System.out.println("Draw game");
	      }
	}
  
	private static int howMany(int a){
	  	int answer=0;
	    for(int i=0;i<board.length;i++){
			for(int j=0;j<board[0].length;j++){
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
    

    // flipCheck recursively checks a straight path in a direction to flip the pieces
   private static boolean flipCheck(int turn, int newx, int newy, int dirx, int diry) {
		int player, oppositePlayer;
		int currentx = newx;
		int currenty = newy;
		boolean flipThis = false;

		// define who attacking and defending players are based on turn
     	if (turn%2==0) {
     		player = 1;
     		oppositePlayer = 2;
     	} else {
     		player = 2;
     		oppositePlayer = 1;
     	}

     	if (currentx+dirx<8 && currentx+dirx>=0 && currenty+diry<8 && currenty+diry>=0 && board[currentx+dirx][currenty+diry]==oppositePlayer) {
         	flipThis = flipCheck(turn, currentx+dirx, currenty+diry, dirx, diry);
       } else if (currentx+dirx<8 && currentx+dirx>=0 && currenty+diry<8 && currenty+diry>=0 && board[currentx+dirx][currenty+diry]==player)
         	return true;
       
       if (flipThis) {
       	board[currentx+dirx][currenty+diry] = player;
       	return true;
       }

       return false;
   }
    
	private static void printTable() {
		for(int i=0;i<board.length;i++){
			for(int j=0;j<board.length;j++){
              	if (board[i][j] == 1)
					System.out.print(board[i][j]+ "\t");
              	else if (board[i][j] == 2)
                  	System.out.print(board[i][j] + "\t" );
                else
                	System.out.print(board[i][j] + "\t");
			}
			System.out.println();
		}
	}

	private static boolean notFull() {
		for(int i=0;i<board.length;i++)
			for(int j=0;j<board[0].length;j++)
				if(board[i][j]==0) 
					return true;
		return false;
	}
	
	public static void initializeTable(int[][]a) {
		for(int i=0;i<a.length;i++)
			for(int j=0;j<a[0].length;j++)
				a[i][j]=0;

		a[3][3]=1;
		a[4][4]=1;
		a[3][4]=2;
		a[4][3]=2;
				
	}
  	
  	public static boolean CanFlip(int X, int Y, int dirX, int dirY) {
		int player, oppositePlayer;
		// define who attacking and defending players are based on turn
     	if (turn % 2 ==0) {
     		player = 1;
     		oppositePlayer = 2;
     	} else {
     		player = 2;
     		oppositePlayer = 1;
     	}
     	
      boolean capture = false;
    	while (X+dirX < 8 && X+dirX >= 0 && Y+dirY < 8 && Y+dirY >= 0 && board[X+dirX][Y+dirY]==oppositePlayer) {
    		
        	X = X+dirX; Y = Y+dirY;
        	capture = true;
    	}
    	if (capture == false) return false;
    	
    	if (X+dirX < 8 && X+dirX >= 0 && Y+dirY < 8 && Y+dirY >= 0 && board[X+dirX][Y+dirY]== player)
        	return true;
    	
    	else return false;
	}

  
  	public static boolean Legal(int X, int Y){
    	int i,j, captures;
    	captures = 0;
    	if (board[X][Y]!=0) return false;
    		for (i=-1; i<=1; i++)
				for (j=-1; j<=1; j++)
	   			 	if ((i!=0 || j!=0) && CanFlip(X, Y, i, j))
						return true;
    	return false;
	}
  	
  	public static LinkedList<Node> allNextMoves (){
  	 	LinkedList<Node> nextMoves= new LinkedList<Node>();
  		for(int i=0;i<board.length;i++)
  	      for(int j=0;j<board[0].length;j++)
  	      	if(Legal(i,j)){
  	      		Node newNode = new Node(i,j);
  	      	nextMoves.add(newNode);
  	      	}	
  		return nextMoves;
  	  }
  	
}
