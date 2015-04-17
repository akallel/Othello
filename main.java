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
		System.out.println("choose coordinates for where you want to put the numbers: a,b");
		String a = scan.next();
		String[] aa= a.split(",");
		int n2 =Integer.parseInt(aa[0]);
		int n1 = Integer.parseInt(aa[1]);
      
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
			for(int j=0;j<board[0].length;j++){
              	if (board[i][j] == 1)
					System.out.print(ANSI_RED + board[i][j]+ " " + ANSI_RESET);
              	else if (board[i][j] == 2)
                  	System.out.print(ANSI_BLACK + board[i][j] + " " + ANSI_RESET);
                else
                	System.out.print(board[i][j] + " ");
			}
			System.out.println();
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
  /*
  	public static void MakeMove(){
    	int X,Y;
		int a =0;
		int b = 0;

		X = -1;
		Y = -1;
		for(a = 0; a<8; a++){
			for(b = 0; b<8; b++){
				if(Legal(gamestate,me, a,b))
					{
					X = a;
					Y = b;
					break;
				}
			}
		}

    	if (X>=0) {
			Update(gamestate, me, X, Y);
			System.out.println("%d %d\n", X, Y);
    	} else {
			System.out.println("pass\n");
    	}
    	if (debug) printboard(gamestate, me, ++turn, X, Y);
	}
  	/*
  //Returns the possible spots for the next move
  	public static int CanFlip(int X, int Y, int dirX, int dirY) {
		int player;
      	if (turn%2==0) player = 1; else player = 1;    	
      
      int capture = FALSE;
    	while (X+dirX < 8 && X+dirX >= 0 && Y+dirY < 8 && Y+dirY >= 0 && state[X+dirX][Y+dirY]==-player) {
        	X = X+dirX; Y = Y+dirY;
        	capture = TRUE;
    	}
    	if (capture == FALSE) return FALSE;
    	if (X+dirX < 8 && X+dirX >= 0 && Y+dirY < 8 && Y+dirY >= 0 && state[X+dirX][Y+dirY]==player)
        	return TRUE;
    	else return FALSE;
	}
  
  	public static boolean Legal(board state, int player, int X, int Y){
    	int i,j, captures;
    	captures = 0;
    	if (state[X][Y]!=0) return false;
    		for (i=-1; i<=1; i++)
				for (j=-1; j<=1; j++)
	   			 	if ((i!=0 || j!=0) && CanFlip(state, player, X, Y, i, j))
						return true;
    	return false;
	}
	*/
}

/* 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#define FALSE 0
#define TRUE 1

typedef signed char board[8][8];

int me;
int depthlimit, timelimit1, timelimit2;
board gamestate;

int debug = FALSE;
int turn;

void error(char * msg)
{
    fprintf(stderr,"%s\n", msg);
    exit(-1);
}

void DoFlip(board state, int player, int X, int Y, int dirX, int dirY)
{
    while (X+dirX < 8 && X+dirX >= 0 && Y+dirY < 8 && Y+dirY >= 0 && state[X+dirX][Y+dirY]==-player) {
        X = X+dirX; Y = Y+dirY;
        state[X][Y] = player;
    }
}

void Update(board state, int player, int X, int Y)
{
    int i,j;

    if (X<0) return; // pass move 
    if (state[X][Y] != 0) {
	printboard(state, player, turn, X, Y);
	error("Illegal move");
    }
    state[X][Y] = player;
    for (i=-1; i<=1; i++)
	for (j=-1; j<=1; j++)
	    if ((i!=0 || j!=0) && CanFlip(state, player, X, Y, i, j))
			DoFlip(state, player, X, Y, i, j);
}

    

void Result(board oldstate, board newstate, int player, int X, int Y)
{
    int i, j;
    for (i=0; i<8; i++)
	for (j=0; j<8; j++)
	    newstate[i][j] = oldstate[i][j];
    Update(newstate, player, X, Y);
}

int GameOver(board state)
{
    int X,Y;

    for (X=0; X<8; X++)
	for (Y=0; Y<8; Y++) {
	    if (Legal(state, 1, X, Y)) return FALSE;
	    if (Legal(state, -1, X, Y)) return FALSE;
	}
    return TRUE;
}



void MakeMove(void)
{
    int X,Y;
	int a =0;
	int b = 0;

	X = -1;
	Y = -1;
	for(a = 0; a<8; a++)
	{
		for(b = 0; b<8; b++)
		{
			if(Legal(gamestate,me, a,b))
			{
				X = a;
				Y = b;
				break;
			}
		}
	}

    if (X>=0) {
	Update(gamestate, me, X, Y);
	printf("%d %d\n", X, Y);
	fflush(stdout);
    } else {
	printf("pass\n");
	fflush(stdout);
    }
    if (debug) printboard(gamestate, me, ++turn, X, Y);
}


int main(int argc, char** argv)
{
    char inbuf[256];
    char playerstring[1];
    int X,Y;

    if (argc >= 2 && strncmp(argv[1],"-d",2)==0) debug = TRUE;
    turn = 0;
    fgets(inbuf, 256, stdin);
    if (sscanf(inbuf, "game %1s %d %d %d", playerstring, &depthlimit, &timelimit1, &timelimit2) != 4) error("Bad initial input");
    depthlimit = 4;
    if (playerstring[0] == 'B') me = 1; else me = -1;
    NewGame();
    if (me == 1) MakeMove();
    while (fgets(inbuf, 256, stdin)!=NULL){
	if (strncmp(inbuf,"pass",4)!=0) {
	    if (sscanf(inbuf, "%d %d", &X, &Y) != 2) return 0;
	    Update(gamestate, -me, X, Y);
	    if (debug) printboard(gamestate, -me, ++turn, X, Y);
	}
	MakeMove();
    }
    return 0;
}
*/