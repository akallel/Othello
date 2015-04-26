/*
 * CSC 242
 * Group Daybreak
 * Othello
 * Nathan Contino, Anis Kallel, John Donner
 * 
 */
public class play {
	public static void main(String[] args){
		String COLOR = args[0];			//Color of computer tiles; B or W; B goes first, W goes second
		String DEPTHLIMIT = args[1];	//cutoff depth, 0 if none
		String TIMELIMIT1 = args[2];	//CPU time (ms) allowed for one move; 0 if no limit
		String TIMELIMIT2 = args[3];	//total time for game in ms, 0 if none. If timelimit2 != 0, DEPTHLIMIT and TIMELIMIT1 do not matter.
		
		int compColor = 1;
		int time1 = Integer.valueOf(TIMELIMIT1);
		int time2 = Integer.valueOf(TIMELIMIT2);
		int depth = Integer.valueOf(DEPTHLIMIT);
		if(time1 != 0)
			depth = 0;
		if(COLOR.equalsIgnoreCase("B"))
			compColor = 0;
		if(time2 != 0){
			time1 = 0;
			depth = 0;
		}
		Othello instance = new Othello(compColor, time1, time2, depth);	//lines are specified as "X Y" on a line (\n at end). no move, print "pass"
	}
}
