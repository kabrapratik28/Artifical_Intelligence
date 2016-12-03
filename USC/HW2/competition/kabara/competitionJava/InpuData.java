package competitionJava;
import java.util.*;

public class InpuData {
	int dimensionOFBoard;
	String modeOFGame;
	String youPlay;
	float cpuTime;
	ArrayList<ArrayList<Integer>> boardValues;
	ArrayList<ArrayList<String>> occupiedPositions;
	
	public InpuData(int dimensionOFBoard,String modeOFGame,String youPlay,float cpuTime,ArrayList<ArrayList<Integer>> boardValues,ArrayList<ArrayList<String>> occupiedPositions) {
		this.dimensionOFBoard = dimensionOFBoard;
		this.modeOFGame = modeOFGame;
		this.youPlay = youPlay;
		this.cpuTime = cpuTime;
		this.boardValues = boardValues;
		this.occupiedPositions = occupiedPositions;
	}	
}
