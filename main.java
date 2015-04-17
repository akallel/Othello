import java.util.Scanner;


public class main {
	static int[][] table= new int [8][8];
	static int turn= 0;
	public static void main(String [] args){
	Scanner scan = new Scanner(System.in); 
		initializeTable(table);
	
	while(notFull(table)){ // not the only stopping condition, other cases where all are 1s or 2s should be included 
		System.out.println("choose coordinates for where you want to put the numbers: a,b");
		String a = scan.next();
		String[] aa= a.split(",");
		int n1 =Integer.parseInt(aa[0]);
		int n2= Integer.parseInt(aa[1]);
		if(turn%2 == 0){
			// player 1 moves
			table[n1][n2]= 1;
			turn++;
			printTable(table);
		}
		else {
			// player 2 moves 
			// algorithms for computer moves should go here
			table[n1][n2]= 2;	
			turn++;
			printTable(table);
		}
		}	
	}
	
	
	private static void printTable(int[][] a) {
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				System.out.print(a[i][j]+ " ");
			}
			System.out.println();
		}
	}


	private static boolean notFull(int[][] a) {
		for(int i=0;i<a.length;i++)
			for(int j=0;j<a[0].length;j++)
				if(a[i][j]==0) 
					return true;
		return false;
	}


	public static void initializeTable(int[][]a) {
		for(int i=0;i<a.length;i++)
			for(int j=0;j<a[0].length;j++)
				a[i][j]=0;
	}
	
}
