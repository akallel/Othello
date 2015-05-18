import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * CSC 242
 * Group Daybreak
 * Othello
 * Nathan Contino, Anis Kallel, John Donner
 * 
 */
public class play {
	public static void main(String[] args) throws FileNotFoundException{
		Scanner scan = new Scanner(System.in);
		
			//String line = scan.nextLine();
            //System.out.println(line);
            //String [] input = line.split("\\s");
			
            String cats = scan.next();
            String COLOR = scan.next();		//Color of computer tiles; B or W; B goes first, W goes second
    		String DEPTHLIMIT = scan.next();//input[1];	//cutoff depth, 0 if none
    		String TIMELIMIT1 = scan.next();	//CPU time (ms) allowed for one move; 0 if no limit
    		String TIMELIMIT2 = scan.next(); //total time for game in ms, 0 if none. If timelimit2 != 0, DEPTHLIMIT and TIMELIMIT1 do not matter.
        
    		int compColor = 1;
    		int time1 = Integer.valueOf(TIMELIMIT1);
    		int time2 = Integer.valueOf(TIMELIMIT2);
    		int depth = Integer.valueOf(DEPTHLIMIT);
    		if(COLOR.equalsIgnoreCase("B"))
    			compColor = 0;
    		if(time2 != 0){
    			time1 = 0;
    			depth = 0;
    		}
    		Othello instance = new Othello(compColor, time1, time2, depth);	//lines are specified as "X Y" on a line (\n at end). no move, print "pass"
        scan.close();
		}
}
